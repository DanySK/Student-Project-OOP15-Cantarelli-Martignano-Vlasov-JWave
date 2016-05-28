package org.jwave.model.playlist.strategies;

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
     * Creates a new PlaylistNavigatorImpl.
     * 
     * @param initDimension
     *          initial playlist dimension.
     *          
     */
    public PlaylistNavigatorImpl(final int initDimension) {
        this(initDimension, 0);
    }
    
    /**
     * Creates a new PlaylistNavigatorImpl.
     * 
     *          
     */
    public PlaylistNavigatorImpl() {
        this(0, 0);
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
    public void update(final ESource<? extends Integer> s, final Integer arg) {
        this.setPlaylistDimension(arg);
    }
    
    @Override
    public void setCurrentIndex(final int index) {
        this.currentIndex = index;
    }
}
