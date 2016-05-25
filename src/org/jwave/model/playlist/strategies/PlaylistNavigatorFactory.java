package org.jwave.model.playlist.strategies;

import org.jwave.model.player.PlayMode;

/**
 * This class is a playlist navigator factory.
 *
 */
public class PlaylistNavigatorFactory {
    
    /**
     * Creates a new instance of this factory.
     */
    public PlaylistNavigatorFactory() { }
    
    /**
     * Creates a new PlaylistNavigator.
     * 
     * @param type
     *          the type of playlist navigator to be created.
     *          
     * @return
     *          a new playlist navigator.
     */
    public PlaylistNavigator createNavigator(final PlayMode type) {
        final int dimension = 0;
        final int index = 0;
        switch (type) {
        case NO_LOOP:        
            return new NoLoopNavigator(dimension, index);
        case LOOP_ONE:       
            return new LoopOneNavigator(index);
        case LOOP_ALL:       
            return new LoopAllNavigator(dimension, index);
        case SHUFFLE:        
            return new ShuffleNavigator(dimension, index);
        default:             
            return new NoLoopNavigator(dimension, index);
    }
    }
}
