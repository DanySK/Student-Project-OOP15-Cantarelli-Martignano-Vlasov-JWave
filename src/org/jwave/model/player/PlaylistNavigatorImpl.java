package org.jwave.model.player;

public abstract class PlaylistNavigatorImpl implements PlaylistNavigator {

    private int playlistDimension;
    private Integer currentIndex;
    
    public PlaylistNavigatorImpl(final int initDimension) {
        this.playlistDimension = initDimension;
    }
    
    public abstract int next();

    public abstract int prev();

    @Override
    public void setPlaylistDimension(final int newDimension) {
        this.playlistDimension = newDimension;
    }
    
    public void incIndex() {
        this.currentIndex++;
    }
    
    public void decIndex() {
        this.currentIndex--;
    }
    
    public Integer getCurrentIndex() {
        return this.currentIndex;
    }
    
    public int getPlaylistDimension() {
        return this.playlistDimension;
    }
}
