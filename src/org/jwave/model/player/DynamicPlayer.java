package org.jwave.model.player;

import java.util.Optional;

/**
 * This interface represents a dynamic player.
 */
public interface DynamicPlayer {

    /**
     * Starts reproducing audio.
     */
    void play();

    /**
     * Pauses audio reproduction.
     */
    void pause();

    /**
     * Stops audio reproduction, rewinds the loaded audio file.
     */
    void stop();
    
    /**
     * Moves the reproduction cursor in a specific position of the audio file.
     * 
     * @param millis
     *          the point of the file where the cursor has to be moved.
     *                   
     */
    void cue(int millis);
    
    /**
     * @return
     *          the length of the loaded file in milliseconds.
     */
    int getLength();

    /**
     * @return
     *          the current position in the loaded audio file.
     */
    int getPosition();

    /**
     * 
     * @return
     *          the song loaded in the dynamic player.
     */
    Optional<Song> getLoaded();
    
    /**
     * 
     * @return
     *          whether the player is playing sound.
     */
    boolean isPlaying();
    
    /**
     * 
     * @return
     *          whether the player is paused.
     */
    boolean isPaused();
    
    /**
     * 
     * @return
     *          whether the player has started.
     * 
     */
    boolean hasStarted();
        
    /**
     * Modifies the current volume.
     * 
     * @param amount
     *          the amount of volume to be set.         
     */
    void setVolume(int amount);
    
    /**
     * Sets the player by loading a song.
     * 
     * @param song
     *          the song to be loaded.
     */
    void setPlayer(Song song);
}
