package org.jwave.model.player;

/**
 * A PlaylistSurfer contains a strategy for scrolling songs in a playlist, depending on a {@link}PlayMode.
 *
 */
public interface PlaylistNavigator {
    
    /**
     * 
     * @return
     *          next song index in the playlist.
     */
    int next();
    
    /**
     * 
     * @return
     *          previous song index in the playlist.
     */
    int prev();
    
    /**
     * Sets the playlist dimension for the navigator.
     * 
     * @param newDimension
     */
    void setPlaylistDimension(int newDimension);
}
