package org.jwave.controller.player;

/**
 * An exception thrown when looking for a playlist in a collection and it can't be found,
 * possibly beacuse it is not present.
 *
 */
public class PlaylistNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2244358657884138453L;

    /**
     * Creates a new instance of PlaylistNotFoundException.
     */
    public PlaylistNotFoundException() {
        super();
    }
    
    @Override
    public String toString() {
        return "The playlist can't be found.";
    }
}
