package org.jwave.model.player;

//to be checked
public class LoopOneNavigator extends PlaylistNavigatorImpl {

    private int repeatedSongIndex;
    
    public LoopOneNavigator(final int initDimension, final int repeatedSongIndex) {
        super(initDimension);
        if (initDimension > 1) {
            throw new IllegalArgumentException();
        }
        this.repeatedSongIndex = repeatedSongIndex;
    }

    @Override
    public int next() {
        return this.repeatedSongIndex;
    }

    @Override
    public int prev() {
        return this.repeatedSongIndex;
    }

}
