package org.jwave.controller.player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistNavigator;
import org.jwave.model.player.Song;

/**
 * Interface PlaylistManager.
 */
public interface PlaylistManager {

    void savePlaylistToFile(String name, String path) throws FileNotFoundException, IOException;

    void loadPlaylist(String path) throws IllegalArgumentException, ClassNotFoundException, IOException;

    void openFile(String path, boolean enqueue);

    void openDir(String path, boolean enqueue);

    void reset();

    Optional<Song> getCurrentLoaded();
    
    Playlist getPlayingQueue();
    
    PlaylistNavigator getPlaylistNavigator();

    void setNavigator(PlaylistNavigator newNavigator);
}
