package org.jwave.model.playlist.strategies;

import java.util.Optional;

/**
 * 
 *A LoopOne navigator follows the LOOP_ALL {@link}PlayMode policy.
 */
public class LinearNavigator extends AbstractPlaylistNavigator {

    /**
     * Creates a new instance of this navigator.
     * 
     * @param initDimension
     *          initial playlist dimension.
     *          
     * @param currentIndex
     *          starting index.
     */
    public LinearNavigator(final int initDimension, final Optional<Integer> currentIndex) {
        super(initDimension, currentIndex);
    }

    @Override
    public Optional<Integer> next() {
        if (this.getCurrentIndex().isPresent()) {
            if (this.getCurrentIndex().get() < (this.getPlaylistDimension() - 1)) {
                this.setCurrentIndex(Optional.of(this.getCurrentIndex().get() + 1));
            } else {
                this.setCurrentIndex(Optional.of(0));
            }
        } else {
            if (this.getPlaylistDimension() > 0) {
                this.setCurrentIndex(Optional.of(0));
            }
        }
        return this.getCurrentIndex();
    }

    @Override
    public Optional<Integer> prev() {
        if (this.getCurrentIndex().isPresent()) {
            if (this.getCurrentIndex().get() == 0) {
                this.setCurrentIndex(Optional.of(this.getPlaylistDimension() - 1));
            } else {
                this.setCurrentIndex(Optional.of(this.getCurrentIndex().get() - 1));
            }
        }
        return this.getCurrentIndex();
    }
}
