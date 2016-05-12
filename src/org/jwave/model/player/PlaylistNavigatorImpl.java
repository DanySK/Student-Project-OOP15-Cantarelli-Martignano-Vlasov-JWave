package org.jwave.model.player;

import java.util.Optional;

import org.jwave.controller.player.ESource;

public abstract class PlaylistNavigatorImpl implements PlaylistNavigator {

    private int playlistDimension;
    private Integer currentIndex;
    
    public PlaylistNavigatorImpl(final int initDimension, final int currentIndex) {
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
    
    @Override
    public void update(final ESource<? extends Optional<Integer>, ? extends Optional<Integer>> s, 
            final Optional<Integer> arg1, final Optional<Integer> arg2) {
        if (arg1.isPresent()) {
            this.setPlaylistDimension(arg1.get());
        }
        if (arg2.isPresent()) {
            throw new IllegalArgumentException();
        }
    }
}
