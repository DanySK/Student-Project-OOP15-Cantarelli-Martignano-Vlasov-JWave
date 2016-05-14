package org.jwave.model.player;

/**
 * An enum defining play modes. a play mode is a way a playlist is scanned.
 *
 */
public enum PlayMode {
    
    /**
     * The playlist is scanned in sequence.
     */
    NO_LOOP,
    
    /**
     * A single song is looped.
     */
    LOOP_ONE,
    
    /**
     * All the playlist is looped, so when arriving at the last song it returns to the first.
     */
    LOOP_ALL,
    
    /**
     * Scans the playlist in a random way.
     */
    SHUFFLE;
}
