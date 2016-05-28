package org.jwave.model.playlist.strategies;

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
    public LoopAllNavigator(final int initDimension, final int currentIndex) {
        super(initDimension, currentIndex);
    }

    @Override
    public int next() {
        if (this.getCurrentIndex().equals(this.getPlaylistDimension() - 1)) {
            return 0;
        }
        this.incIndex();
        return this.getCurrentIndex();
    }

    @Override
    public int prev() {
        if (this.getCurrentIndex().equals(0)) {
            return this.getPlaylistDimension();
        }
        this.decIndex();
        return this.getCurrentIndex();
    }
}
