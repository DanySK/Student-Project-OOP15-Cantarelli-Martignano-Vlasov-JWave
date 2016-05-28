package org.jwave.model.playlist.strategies;

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
    public PlaylistNavigator createNavigator(final PlayMode type, final int playlistDimension, final int currentIndex) {
        switch (type) {
            case NO_LOOP:        
                return new NoLoopNavigator(playlistDimension, currentIndex);
            case LOOP_ONE:       
                return new LoopOneNavigator(currentIndex);
            case LOOP_ALL:       
                return new LoopAllNavigator(playlistDimension, currentIndex);
            case SHUFFLE:        
                return new ShuffleNavigator(playlistDimension, currentIndex);
            default:             
                return new NoLoopNavigator(playlistDimension, currentIndex);
        }
    }
}
