package org.jwave.model.player;

//to be checked
public class LoopOneNavigator extends PlaylistNavigatorImpl {

    private int repeatedSongIndex;
    
    public LoopOneNavigator(final int repeatedSongIndex) {
        super(0 , repeatedSongIndex);
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
