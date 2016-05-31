package org.jwave.model.playlist.strategies;

import java.util.Optional;

import org.jwave.model.player.PlayMode;

/**
 * This class is a playlist navigator factory.
 *
 */
public class PlaylistNavigatorFactory {    
    
    /**
     * Creates a new PlaylistNavigator.
     * 
     * @param type
     *          the play mode corresponding to the type of navigator to be created.
     *          
     * @param playlistDimension
     *          the playlist dimension the navigator has to manage.
     *          
     * @param currentIndex
     *          the current selected index in the playlsit.
     *          
     * @return
     *          a new playlist navigator.
     */
    public PlaylistNavigator createNavigator(final PlayMode type, final int playlistDimension, final Optional<Integer> currentIndex) {
        switch (type) {
            case SHUFFLE:        
                return new ShuffleNavigator(playlistDimension);
            default:             
                return new LinearNavigator(playlistDimension, currentIndex);
        }
    }
}
