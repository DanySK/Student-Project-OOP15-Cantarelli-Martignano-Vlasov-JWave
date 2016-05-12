package org.jwave.model.player;

import java.util.Optional;

import org.jwave.controller.player.EObserver;

/**
 * A PlaylistSurfer contains a strategy for scrolling songs in a playlist, depending on a {@link}PlayMode.
 *
 */
public interface PlaylistNavigator extends EObserver<Optional<Integer>, Optional<Integer>> {
    
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
    void setPlaylistDimension(int newDimension);        //to be removed if the navigator update himself privately.
}
