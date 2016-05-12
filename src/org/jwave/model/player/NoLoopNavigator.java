package org.jwave.model.player;

public class NoLoopNavigator extends PlaylistNavigatorImpl {

    public NoLoopNavigator(final int initDimension, final int currentIndex) {
        super(initDimension, currentIndex);
    }

    @Override
    public int next() {
        if (this.getCurrentIndex() < (this.getPlaylistDimension() - 1)) {
            this.incIndex();
        }
        this.incIndex();
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
