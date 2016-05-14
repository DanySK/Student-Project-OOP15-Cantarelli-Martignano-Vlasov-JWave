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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
 * This is an implmementation of {@link}Playlist.
 */
public class PlaylistManagerImpl implements PlaylistManager {

    private Playlist loadedPlaylist;
    private PlaylistNavigator navigator;
    private Optional<Song> currentLoaded;
    private Optional<Integer> currentIndexLoaded;
    
    /**
     * Creates a new PlaylistManagerImpl.
     */
    public PlaylistManagerImpl() {
        this.loadedPlaylist = new PlaylistImpl();
        this.navigator = new NoLoopNavigator(this.getPlayingQueue().getDimension(), 0);
        this.currentLoaded = Optional.empty();
        this.currentIndexLoaded = Optional.empty();
    }
    
    @Override
    public void savePlaylistToFile(final String name, final String path) throws FileNotFoundException, IOException {
       try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
               new FileOutputStream(new File(path + System.getProperty("file.separator") + name + ".jwo"))))) {
           oos.writeObject(this.loadedPlaylist);
       }
    }

    @Override
    public void loadPlaylist(final String path) throws IllegalArgumentException, ClassNotFoundException, IOException {
        if (!path.contains(".jwo")) {
            throw new IllegalArgumentException("File not recognized");
        }
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(new File(path))))) {
            this.loadedPlaylist = (Playlist) ois.readObject();
        }
    }

    @Override
    public void openFile(final String path, final boolean enqueue) {
        if (!path.contains(".mp3") || !path.contains(".wav")) {
            throw new IllegalArgumentException("Trying to open a wrong type of file");
        }
        this.checkEnqueue(enqueue);
        this.loadedPlaylist.addSong(new SongImpl(path));
    }

    @Override
    public void openDir(final String path, final boolean enqueue) {
        this.checkEnqueue(enqueue);
        final File dir = new File(path);
//        final Path dir = Paths.get(path);
//      Files.newDirectoryStream(dir, filter) da provare che è più elegante
        final List<File> filesList = Arrays.asList(dir.listFiles());
        final List<File> audioFiles = filesList.stream()
                .filter(s -> s.getName().contains(".mp3") || s.getName().contains(".wav"))
                .collect(Collectors.toList());
        audioFiles.forEach(f -> {
            this.loadedPlaylist.addSong(new SongImpl(f.getAbsolutePath()));
        });
    }

    @Override
    public void reset() {
        //add isEmpty() method in Playlist so this method can check it and optimize operations.
        this.loadedPlaylist = new PlaylistImpl();
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
    
    private void checkEnqueue(final boolean enqueueValue) {
        if (!enqueueValue) {
            this.reset();
        }
    }

    @Override
    public PlaylistNavigator getPlaylistNavigator() {
       return this.navigator;
    }
    
    private void setNavigator(final PlayMode mode) {
        final int dimension = this.getPlayingQueue().getDimension();
        int index = 0; 
        if (this.currentIndexLoaded.isPresent()) {
            index = this.getCurrentLoadedIndex().get();
        }
       switch (mode) {
           case NO_LOOP:        this.navigator = new NoLoopNavigator(dimension, index);
                               break;
           case LOOP_ONE:       this.navigator = new LoopOneNavigator(index);
                               break;
           case LOOP_ALL:       this.navigator = new LoopAllNavigator(dimension, index);
                               break;
           case SHUFFLE:        this.navigator = new ShuffleNavigator(dimension, index);
                               break;
           default:             this.navigator = new NoLoopNavigator(dimension, index);
                               break;
       }
    }

    @Override
    public void update(final ESource<? extends Optional<PlayMode>, ? extends Optional<Song>> s, 
            final Optional<PlayMode> arg1, final Optional<Song> arg2) {
        if (arg1.isPresent()) {
            this.setNavigator(arg1.get());
        }
        if (arg2.isPresent()) {
            this.currentLoaded = arg2;
            this.getPlaylistNavigator().setCurrentIndex(this.getCurrentLoadedIndex().get());
        }
    }

    @Override
    public void update(final ESource<? extends Optional<PlayMode>, ? extends Optional<Song>> s, 
            final Optional<PlayMode> arg) {
        if (arg.isPresent()) {
            this.setNavigator(arg.get());
        }
    }
}
