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

    private static final PlaylistManager SINGLETON = new PlaylistManagerImpl();
     
    private static final String HOME = "user.home";
    private static final String SEPARATOR = "file.separator";
    private static final String SAVE_DIR_NAME = "JWavePlaylists";
    
    private Set<Playlist> availablePlaylists;   //all the available playlists in the default directory
    private Playlist defaultQueue;
    private Playlist loadedPlaylist;
    private PlaylistNavigator navigator;
    private Optional<Song> currentLoaded;
    private Optional<Integer> currentIndexLoaded;
    
    private PlaylistManagerImpl() {     
        this.checkDefaultDir();
        this.availablePlaylists = new HashSet<>();
        this.refreshAvailablePlaylists();
        this.checkDefaultPlaylistLoading();
        this.loadedPlaylist = this.defaultQueue;
        this.navigator = new NoLoopNavigator(this.loadedPlaylist.getDimension(), 0);
        this.loadedPlaylist.addEObserver(this.navigator);
        this.currentLoaded = Optional.empty();
        this.currentIndexLoaded = Optional.empty();
    }
    
    @Override
    public void savePlaylistToFile(final Playlist playlist, final String name, final String path) throws FileNotFoundException, IOException {
       try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
               new FileOutputStream(new File(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME 
                       + System.getProperty(SEPARATOR) + playlist.getName() + ".jwo"))))) {
           oos.writeObject(playlist);
       }
    }

    @Override
    public void openAudioFile(final File audioFile) {
        if (!this.isAudioFileName(audioFile.getName())) {
            throw new IllegalArgumentException("Trying to open a wrong type of file");
        }
        this.defaultQueue.addSong(new SongImpl(audioFile));
    }   //TODO check if to add a control for NON audio files with the extension ".mp3" or ".wav"

//    @Override
//    public void openDir(final String path, final boolean enqueue) {
//        this.checkEnqueue(enqueue);
//        final File dir = new File(path);
////        final Path dir = Paths.get(path);
////      Files.newDirectoryStream(dir, filter) da provare che è più elegante
//        final List<File> filesList = Arrays.asList(dir.listFiles());
//        final List<File> audioFiles = filesList.stream()
//                .filter(s -> s.getName().contains(".mp3") || s.getName().contains(".wav"))
//                .collect(Collectors.toList());
//        audioFiles.forEach(f -> {
//            this.loadedPlaylist.addSong(new SongImpl(f));
//        });
//    }

    @Override
    public void reset() {
        if (!this.defaultQueue.isEmpty()) {
            try {
                this.defaultQueue = this.createNewPlaylist("default");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } //TODO check if it is better to add a method in Playlist named "clean" or similar.
        }   
        this.loadedPlaylist = this.getDefaultQueue();
        this.loadedPlaylist.addEObserver(this.getPlaylistNavigator());
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
    
    /**
     * 
     * @return
     *          the singleton instance of PlaylistManagerImpl.
     */
    public static PlaylistManager getPlaylistManager() {
        return SINGLETON;
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
    public Playlist createNewPlaylist(final String name) throws IllegalArgumentException, FileNotFoundException, IOException {
        if (this.isNameAlreadyPresent(name)) {
            throw new IllegalArgumentException("Name already present");
        }
        final Playlist out = new PlaylistImpl(name);
        this.availablePlaylists.add(out);
        this.savePlaylistToFile(out, name, System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME);
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
        if (playlist.getName().equals("default.jwo")) {
            throw new IllegalArgumentException("Cannot delete the default playlist");
        } else {
            this.availablePlaylists.remove(playlist);
            final Path filePath = Paths.get(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME 
                    + System.getProperty(SEPARATOR) + playlist.getName());
           try {
               Files.delete(filePath);
           } catch (NoSuchFileException ex) {
               System.err.println("FileContainingPlaylistNotFound");
           } catch (IOException e) {
               e.printStackTrace();
           }
        }
    }

    @Override
    public void refreshAvailablePlaylists() {
        final Path defaultDir = Paths.get(System.getProperty("user.home") + System.getProperty("file.separator") + SAVE_DIR_NAME);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(defaultDir)) {
            for (Path file : stream) {
               if (Files.isRegularFile(file)) {
                   if (file.getFileName().equals("default.jwo")) {
                       this.defaultQueue = this.loadPlaylist(file.toFile());    //TODO refactoring duplicate code and if structure
                   } else {
                       if (file.getFileName().endsWith(".jwo")) {
                           this.availablePlaylists.add(this.loadPlaylist(file.toFile()));
                       }
                   }
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Playlist selectPlaylist(final String name) throws PlaylistNotFoundException {
        if (this.isNameAlreadyPresent(name)) {
            return this.availablePlaylists.stream()
                    .filter(p -> p.getName().equals(name))
                    .findAny()
                    .get();
        }
        throw new PlaylistNotFoundException();
    }

    @Override
    public void renamePlaylist(final Playlist playlist, final String newName) throws IllegalArgumentException {
        if (this.isNameAlreadyPresent(newName)) {
            throw new IllegalArgumentException("Cannot have two playlists with the same name.");
        }
        //TODO to be completed
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

    private boolean isDefaultSaveDirectoryPresent() {
        //inspired by Oracle Java tutorials.
        final Path userHomeDir  = Paths.get(System.getProperty(HOME));
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(userHomeDir)) {
            for (Path file : stream) {
               if (Files.isDirectory(file) && file.getFileName().toString().equals(SAVE_DIR_NAME)) {
                   return true;
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void createSaveDir() {
        final Path saveDirPath = Paths.get(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME);
        try {
            Files.createDirectory(saveDirPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    
    private void checkDefaultDir() {
        if (!this.isDefaultSaveDirectoryPresent()) {
            this.createSaveDir();
        }
    }
    
    private void checkDefaultPlaylistLoading() {
        if (this.defaultQueue == null) {       
            try {
                this.defaultQueue = this.createNewPlaylist("default");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
