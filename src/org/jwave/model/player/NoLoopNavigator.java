package org.jwave.model.player;

public class NoLoopNavigator extends PlaylistNavigatorImpl {

    public NoLoopNavigator(int initDimension) {
        super(initDimension);
    }

    @Override
    public int next() {
        if (this.getCurrentIndex() < this.getPlaylistDimension() - 1) {
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
