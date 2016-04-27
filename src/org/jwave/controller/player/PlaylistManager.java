package org.jwave.controller.player;

import org.jwave.model.player.Playlist;
import org.jwave.model.player.Song;

/**
 * Interface PlaylistManager
 */
public interface PlaylistManager {

    public void savePlaylistToFile(String name, String path);

    public Playlist loadPlaylist(String path);

    public Playlist openFile(String path, boolean enqueue);

    public Playlist openDir(String path, boolean enqueue);

    public void reset();

    public Song getCurrentLoaded();
    
    public Playlist getPlayingQueue();


}
