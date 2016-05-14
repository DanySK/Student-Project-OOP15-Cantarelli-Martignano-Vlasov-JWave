package org.jwave.model.player;

/**
 * A LoopOne navigator follows the LOOP_ONE {@link}PlayMode policy.
 * 
 */
public class LoopOneNavigator extends PlaylistNavigatorImpl {

    private int repeatedSongIndex;
    
    /**
     * Creates a new instance of this navigator.
     * 
     * @param initRepeatedSongIndex
     *          the index of the song that has to be repeated.
     */
    public LoopOneNavigator(final int initRepeatedSongIndex) {
        super(0, initRepeatedSongIndex);
        this.repeatedSongIndex = initRepeatedSongIndex;
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
