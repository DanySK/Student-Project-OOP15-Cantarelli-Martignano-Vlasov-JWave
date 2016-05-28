package org.jwave.model.playlist.strategies;

import java.util.Optional;

import org.jwave.model.EObserver;

/**
 * A PlaylistNavigator contains a strategy for scrolling a playlist, depending on a {@link}PlayMode.
 *
 */
public interface PlaylistNavigator extends EObserver<Optional<Integer>> {
    
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
     *          the new playlist dimension the navigator has to scroll.
     */
    void setPlaylistDimension(int newDimension);  
    
    /**
     * Sets the current index in the navigator.
     * 
     * @param index
     *          new current index.
     */
    void setCurrentIndex(int index);
}
