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
                this.setCurrentIndex(Optional.of(this.getCurrentIndex().get() + 1));
                return this.getCurrentIndex();
            } else {
                return Optional.empty();
            }
        } else {
            if (this.getPlaylistDimension() > 0) {
                this.setCurrentIndex(Optional.of(0));
            }
            return this.getCurrentIndex();
        }
    }
    
    @Override
    public Optional<Integer> prev() {
        if (this.getCurrentIndex().isPresent()) {
            if (this.getCurrentIndex().get() > 0) {
                this.setCurrentIndex(Optional.of(this.getCurrentIndex().get() - 1));
            } else {
                return Optional.empty();
            }
        }
        return this.getCurrentIndex();
    }
}
