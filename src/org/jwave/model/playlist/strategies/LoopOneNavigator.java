package org.jwave.model.playlist.strategies;

import java.util.Optional;

/**
 * A LoopOne navigator follows the LOOP_ONE {@link}PlayMode policy.
 * 
 */
public class LoopOneNavigator extends AbstractPlaylistNavigator {
    
    /**
     * Creates a new instance of this navigator.
     * 
     * @param initRepeatedSongIndex
     *          the index of the song that has to be repeated.
     */
    public LoopOneNavigator(final Optional<Integer> initRepeatedSongIndex) {
        super(1, initRepeatedSongIndex);
    }

    @Override
    protected void incIndex() { }
    
    @Override
    protected void decIndex() { }
    
    @Override
    public Optional<Integer> next() {
        return this.getCurrentIndex();
    }

    @Override
    public Optional<Integer> prev() {
        return this.next();
    }
}
