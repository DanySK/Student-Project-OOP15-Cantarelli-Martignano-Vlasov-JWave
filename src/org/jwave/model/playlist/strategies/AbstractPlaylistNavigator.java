package org.jwave.model.playlist.strategies;

import java.util.Optional;

import org.jwave.model.ESource;

/**
 * This is an implementation of {@link}PlaylistNavigator.
 *
 */
public abstract class AbstractPlaylistNavigator implements PlaylistNavigator {

    private int playlistDimension;
    private Optional<Integer> currentIndex;
    
    /**
     * Creates a new PlaylistNavigatorImpl.
     * 
     * @param initDimension
     *          initial playlist dimension.
     *          
     * @param index
     *          the current selected index of the playing queue.         
     *          
     */
    public AbstractPlaylistNavigator(final int initDimension, final Optional<Integer> index) {
        this.playlistDimension = initDimension;
        this.currentIndex = Optional.empty();
    }
    
    /**
     * @return 
     *          the next index that has to be selected in playlist.
     */
    public abstract Optional<Integer> next();

    /**
     * @return 
     *          the previous index that has to be selected in playlist.
     */
    public abstract Optional<Integer> prev();

    @Override
    public void setPlaylistDimension(final int newDimension) {
        this.playlistDimension = newDimension;
    }
    
    /**
     * Increases the current index of one.
     */
    public void incIndex() {
        this.currentIndex = Optional.of(this.currentIndex.get() + 1);
    }
    
    /**
     * Decreases the current index of one.
     */
    public void decIndex() {
        this.currentIndex = Optional.of(this.currentIndex.get() - 1);
    }
    
    /**
     * 
     * @return
     *          the current index.
     */
    protected Optional<Integer> getCurrentIndex() {
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
    public void update(final ESource<? extends Integer> s, final Integer arg) {
        this.setPlaylistDimension(arg);
    }
    
    @Override
    public void setCurrentIndex(final Optional<Integer> index) {
        this.currentIndex = index;
    }
    
    /**
     * Checks the presence of the current index.
     */
    protected void checkCurrentIndex() {
        if (!this.currentIndex.isPresent()) {
            throw new IllegalStateException();
        }
    }
}
