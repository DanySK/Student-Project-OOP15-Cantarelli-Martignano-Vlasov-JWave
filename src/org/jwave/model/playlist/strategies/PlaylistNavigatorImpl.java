package org.jwave.model.playlist.strategies;

import java.util.Optional;

import org.jwave.model.ESource;

/**
 * This is an implementation of {@link}PlaylistNavigator.
 *
 */
public abstract class PlaylistNavigatorImpl implements PlaylistNavigator {

    private int playlistDimension;
    private Integer currentIndex;
    
    /**
     * Creates a new PlaylistNavigatorImpl.
     * 
     * @param initDimension
     *          initial playlist dimension.
     *          
     * @param initCurrentIndex
     *          the current song index.
     */
    public PlaylistNavigatorImpl(final int initDimension, final int initCurrentIndex) {
        this.playlistDimension = initDimension;
        this.currentIndex = initCurrentIndex;
    }
    
    /**
     * @return 
     *          the next index that has to be selected in playlist.
     */
    public abstract int next();

    /**
     * @return 
     *          the previous index that has to be selected in playlist.
     */
    public abstract int prev();

    @Override
    public void setPlaylistDimension(final int newDimension) {
        this.playlistDimension = newDimension;
    }
    
    /**
     * Increases the current index of one.
     */
    public void incIndex() {
        this.currentIndex++;
    }
    
    /**
     * Decreases the current index of one.
     */
    public void decIndex() {
        this.currentIndex--;
    }
    
    /**
     * 
     * @return
     *          the current index.
     */
    public Integer getCurrentIndex() {
        System.out.println("indice corrente : " + this.currentIndex);
        return this.currentIndex;
    }
    
    /**
     * 
     * @return
     *          the current dimension of the playlist it is navigating.
     */
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
    
    @Override
    public void update(final ESource<? extends Optional<Integer>, ? extends Optional<Integer>> s, 
            final Optional<Integer> arg) {
        if (arg.isPresent()) {
            this.setPlaylistDimension(arg.get());
        }
    }
    
    @Override
    public void setCurrentIndex(final int index) {
        this.currentIndex = index;
    }
}
