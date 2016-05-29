package org.jwave.model.playlist.strategies;

import java.util.Optional;

/**
 * 
 *A LoopOne navigator follows the LOOP_ALL {@link}PlayMode policy.
 */
public class LoopAllNavigator extends AbstractPlaylistNavigator {

    /**
     * Creates a new instance of this navigator.
     * 
     * @param initDimension
     *          initial playlist dimension.
     *          
     * @param currentIndex
     *          starting index.
     */
    public LoopAllNavigator(final int initDimension, final Optional<Integer> currentIndex) {
        super(initDimension, currentIndex);
    }

    @Override
    public Optional<Integer> next() {
        if (this.getCurrentIndex().isPresent()) {
            if (this.getCurrentIndex().equals(this.getPlaylistDimension() - 1)) {
                this.setCurrentIndex(Optional.of(0));
            } else {
                this.incIndex();
            }
        }
        return this.getCurrentIndex();
    }

    @Override
    public Optional<Integer> prev() {
        if (this.getCurrentIndex().isPresent()) {
            if (this.getCurrentIndex().equals(0)) {
                this.setCurrentIndex(Optional.of(this.getPlaylistDimension()));
            } else {
                this.decIndex();
            }
        }
        return this.getCurrentIndex();
    }
}
