package org.jwave.controller.player;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jwave.model.player.LoopAllNavigator;
import org.jwave.model.player.LoopOneNavigator;
import org.jwave.model.player.NoLoopNavigator;
import org.jwave.model.player.PlayMode;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistNavigator;
import org.jwave.model.player.ShuffleNavigator;
import org.jwave.model.player.Song;
import org.jwave.model.player.SongImpl;

/**
 * This is an implementation of {@link}Playlist.
 */
final class PlaylistManagerImpl implements PlaylistManager {
    
    private Set<Playlist> availablePlaylists;   //all the available playlists in the default directory
    private Playlist defaultQueue;
    private Playlist loadedPlaylist;
    private PlaylistNavigator navigator;
    private Optional<Song> currentLoaded;
    private Optional<Integer> currentIndexLoaded;
    
    /**
     * Creates a new PlaylistManagerImpl.
     */
    public PlaylistManagerImpl() {     
        this.availablePlaylists = new HashSet<>();
//        this.refreshAvailablePlaylists();
//        this.checkDefaultPlaylistLoading();
        this.loadedPlaylist = this.defaultQueue;
        this.navigator = new NoLoopNavigator(this.loadedPlaylist.getDimension(), 0);
        this.loadedPlaylist.addEObserver(this.navigator);
        this.currentLoaded = Optional.empty();
        this.currentIndexLoaded = Optional.empty();
    }

    @Override
    public void openAudioFile(final File audioFile) {
        if (!this.isAudioFileName(audioFile.getName())) {
            throw new IllegalArgumentException("Trying to open a wrong type of file");
        }
        this.defaultQueue.addSong(new SongImpl(audioFile));
    }  

    @Override
    public void reset() {
        if (!this.defaultQueue.isEmpty()) {
            this.defaultQueue.clear();
        }   
        this.loadedPlaylist = this.defaultQueue;
        this.loadedPlaylist.addEObserver(this.navigator);
    }

    @Override
    public Optional<Song> getCurrentLoaded() {
        return this.currentLoaded;
    }
    
    @Override
    public Optional<Integer> getCurrentLoadedIndex() {
        return Optional.of(this.loadedPlaylist.indexOf(this.currentLoaded.get()));
    }

    @Override
    public Playlist getPlayingQueue() {
        return this.loadedPlaylist;
    }

    @Override
    public PlaylistNavigator getPlaylistNavigator() {
       return this.navigator;
    }
    
    @Override
    public void update(final ESource<? extends Optional<PlayMode>, ? extends Optional<Song>> s, 
            final Optional<PlayMode> arg1, final Optional<Song> arg2) {
        if (arg1.isPresent()) {
            this.setNavigator(arg1.get());
        }
        if (arg2.isPresent()) {
            this.currentLoaded = arg2;
            this.navigator.setCurrentIndex(this.getCurrentLoadedIndex().get());
        }
    }

    @Override
    public void update(final ESource<? extends Optional<PlayMode>, ? extends Optional<Song>> s, 
            final Optional<PlayMode> arg) {
        if (arg.isPresent()) {
            this.setNavigator(arg.get());
        }
    }

    @Override
    public Playlist createNewPlaylist(final String name) {
        if (this.isNameAlreadyPresent(name)) {
            throw new IllegalArgumentException("Name already present");
        }
        final Playlist out = new PlaylistImpl(name);
        this.availablePlaylists.add(out);
        return out;
    }

    @Override
    public Playlist getDefaultQueue() {
        return this.defaultQueue;
    }
    
    @Override
    public Collection<Playlist> getAvailablePlaylists() {
       return this.availablePlaylists;
    }
    
    @Override
    public void deletePlaylist(final Playlist playlist) throws IllegalArgumentException {
        if (!playlist.getName().equals("default")) {
            this.availablePlaylists.remove(playlist);
        }
        throw new IllegalArgumentException("Cannot delete the default playlist");
    }

    @Override
    public Playlist selectPlaylist(final String name) {
        return this.availablePlaylists.stream()
                .filter(p -> p.getName().equals(name))
                .findAny()
                .get();
    }

    @Override
    public void renamePlaylist(final Playlist playlist, final String newName) throws IllegalArgumentException, FileNotFoundException, IOException {
        if (this.isNameAlreadyPresent(newName)) {
            throw new IllegalArgumentException("Cannot have two playlists with the same name.");
        }
        final String oldName = playlist.getName();
        playlist.setName(newName);
        final Path filePath = Paths.get(this.getDefaultSavePath() + System.getProperty(SEPARATOR) + oldName);
        this.savePlaylistToFile(playlist, playlist.getName(), this.getDefaultSavePath());
        Files.delete(filePath);
    }    
    
    @Override
    public void setQueue(final Playlist playlist) {
        this.loadedPlaylist.clearObservers();
        this.loadedPlaylist = playlist;
        this.loadedPlaylist.addEObserver(this.navigator);
    }
    
    private void setNavigator(final PlayMode mode) {
        final int dimension = this.loadedPlaylist.getDimension();
        int index = 0; 
        if (this.currentIndexLoaded.isPresent()) {
            index = this.getCurrentLoadedIndex().get();
        }
       switch (mode) {
           case NO_LOOP:        
               this.navigator = new NoLoopNavigator(dimension, index);
               break;
           case LOOP_ONE:       
               this.navigator = new LoopOneNavigator(index);
               break;
           case LOOP_ALL:       
               this.navigator = new LoopAllNavigator(dimension, index);
               break;
           case SHUFFLE:        
               this.navigator = new ShuffleNavigator(dimension, index);
               break;
           default:             
               this.navigator = new NoLoopNavigator(dimension, index);
               break;
       }
    }
    
    private Playlist loadPlaylist(final File playlist) throws IllegalArgumentException, ClassNotFoundException, IOException {
        if (!this.isAPlaylist(playlist)) {
            throw new IllegalArgumentException("File not recognized");
        }
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(playlist)))) {
            final Playlist extractedPlaylist = (Playlist) ois.readObject();
            if (this.isNameAlreadyPresent(extractedPlaylist.getName())) {
                throw new IllegalArgumentException("A playlist with the same name is already present");
            }
            this.availablePlaylists.add(extractedPlaylist);     //TODO check that there are no duplicates.
            return extractedPlaylist;
        }
    }

    
    
    private void createSaveDir() {
        final Path saveDirPath = Paths.get(this.getDefaultSavePath());
        try {
            Files.createDirectory(saveDirPath);
        } catch (IOException e) {
            System.err.println("Cannot create default save directory.");
        }
    }
    
    private boolean isAudioFileName(final String name) {
        return (name.endsWith(".wav") || name.endsWith(".mp3")); 
    }
    
    private boolean isNameAlreadyPresent(final String name) {
        return this.availablePlaylists.stream().anyMatch(p -> p.getName().equals(name));
    }
    
    private boolean isAPlaylist(final File file) {
        return file.getName().endsWith(".jwo");
    }
    
    
    
    private void checkDefaultPlaylistLoading() {
        if (this.defaultQueue == null) {       
            try {
                this.defaultQueue = this.createNewPlaylist("default");
            } catch (IOException e) {
                System.err.println("Cannot create default palylist.");
            }
        }
    }
    
    
}
