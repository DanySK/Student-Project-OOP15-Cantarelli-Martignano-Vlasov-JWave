package org.jwave.model.player;

import java.util.Optional;

import org.jwave.controller.player.ESource;

public abstract class PlaylistNavigatorImpl implements PlaylistNavigator {

    private int playlistDimension;
    private Integer currentIndex;
    
    public PlaylistNavigatorImpl(final int initDimension, final int currentIndex) {
        this.playlistDimension = initDimension;
        this.currentIndex = currentIndex;
    }
    
    public abstract int next();

    public abstract int prev();

    @Override
    public void setPlaylistDimension(final int newDimension) {
        this.playlistDimension = newDimension;
    }
    
    public void incIndex() {
//        System.out.println("Incrementa indice");
        this.currentIndex++;
    }
    
    public void decIndex() {
//        System.out.println("Decrementa indice");
        this.currentIndex--;
    }
    
    public Integer getCurrentIndex() {
        System.out.println("indice corrente : " + this.currentIndex);
        return this.currentIndex;
    }
    
    public int getPlaylistDimension() {
//        System.out.println("Ritorna dimPlaylist del navigator");
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
    
    @Override
    public void setCurrentIndex(final int index) {
        this.currentIndex = index;
    }
}
