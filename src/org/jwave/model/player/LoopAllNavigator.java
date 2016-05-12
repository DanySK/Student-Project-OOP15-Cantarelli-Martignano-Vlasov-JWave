package org.jwave.model.player;

public class LoopAllNavigator extends PlaylistNavigatorImpl {

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
