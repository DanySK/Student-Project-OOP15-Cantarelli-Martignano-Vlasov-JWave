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

import org.jwave.model.player.NoLoopNavigator;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistNavigator;
import org.jwave.model.player.Song;
import org.jwave.model.player.SongImpl;

public class PlaylistManagerImpl implements PlaylistManager {

    private Playlist loadedPlaylist;
    private PlaylistNavigator navigator;
    
    /**
     * Creates a new PlaylistManagerImpl.
     */
    public PlaylistManagerImpl() {
        this.loadedPlaylist = new PlaylistImpl();
        this.navigator = new NoLoopNavigator(this.loadedPlaylist.getDimension());
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
    }

    @Override
    public Optional<Song> getCurrentLoaded() {
        return this.getPlayingQueue().getCurrentSelected();
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
    
    @Override
    public void setSurfer(final PlaylistNavigator newNavigator) {
        this.navigator = newNavigator;
    }
}
