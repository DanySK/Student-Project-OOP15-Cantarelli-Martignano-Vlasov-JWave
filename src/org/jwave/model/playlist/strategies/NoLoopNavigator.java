package org.jwave.model.playlist.strategies;

import java.util.Optional;

/**
 * A NoLoop navigator follows the NO_LOOP {@link}PlayMode policy.
 *
 */
public class NoLoopNavigator extends AbstractPlaylistNavigator {

    /**
     * Creates a new NoLoop Navigator.
     * 
     * @param initDimension
     *          initial playlist dimension.
     *          
     * @param index
     *          the starting index.
     */
    public NoLoopNavigator(final int initDimension, final Optional<Integer> index) {
        super(initDimension, index);
    }

    @Override
    public Optional<Integer> next() {
        if (this.getCurrentIndex().isPresent()) {
            if (this.getCurrentIndex().get() < (this.getPlaylistDimension() - 1)) {
                this.incIndex();
            }
        }
        return this.getCurrentIndex();
    }
    
    @Override
    public Optional<Integer> prev() {
        if (this.getCurrentIndex().isPresent()) {
            if (this.getCurrentIndex().get() > 0) {
                this.decIndex();
            }
        }
        return this.getCurrentIndex();
    }
}
