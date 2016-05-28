package org.jwave.model.playlist.strategies;

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
     * @param currentIndex
     *          the starting index.
     */
    public NoLoopNavigator(final int initDimension, final int currentIndex) {
        super(initDimension, currentIndex);
    }

    @Override
    public int next() {
        if (this.getCurrentIndex() < (this.getPlaylistDimension() - 1)) {
            this.incIndex();
        }
        return this.getCurrentIndex();
    }

    @Override
    public int prev() {
        if (this.getCurrentIndex() > 0) {
            this.decIndex();
        }
        return this.getCurrentIndex();
    }
}
