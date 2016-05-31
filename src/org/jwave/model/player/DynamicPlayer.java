package org.jwave.model.player;

import java.util.Optional;

/**
 * This interface represents a dynamic player that can reproduce {@link Song}.
 */
public interface DynamicPlayer {

    /**
     * Starts reproducing audio.
     * 
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.
     */
    void play() throws IllegalStateException;

    /**
     * Pauses audio reproduction.
     * 
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.
     */
    void pause() throws IllegalStateException;

    /**
     * Stops audio reproduction, rewinds the loaded audio file.
     * 
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.
     */
    void stop() throws IllegalStateException;
    
    /**
     * Moves the reproduction cursor in a specific position of the audio file.
     * 
     * @param millis
     *          the point of the file where the cursor has to be moved.
     *          
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.          
     *                   
     */
    void cue(int millis) throws IllegalStateException;
    
    /**
     * @return
     *          the length of the loaded file in milliseconds.
     *          
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.
     */
    int getLength() throws IllegalStateException;

    /**
     * @return
     *          the current position in the loaded audio file.
     *          
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.          
     */
    int getPosition() throws IllegalStateException;

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
     *          
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.          
     */
    boolean isPlaying() throws IllegalStateException;
    
    /**
     * 
     * @return
     *          whether the player is paused.
     *          
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.          
     */
    boolean isPaused() throws IllegalStateException;
    
    /**
     * 
     * @return
     *          whether the player has started.
     *          
     * @throws IllegalStateException
     *          when no {@link Song} is loaded.
     */
    boolean hasStarted() throws IllegalStateException;
    
    /**
     * 
     * @return
     *          if the player has any song loaded.
     */
    boolean isEmpty();
        
    /**
     * Modifies the current volume.
     * 
     * @param amount
     *          the amount of volume to be set.         
     *  @throws IllegalArgumentException
     *                  if amount < 0 || amount > 50.
     *                           
     */
    void setVolume(float amount);
    
    /**
     * Sets the player by loading a song.
     * 
     * @param song
     *          the song to be loaded.
     */
    void setPlayer(Song song);
    
    /**
     * resets this player.
     */
    void resetPlayer();
    
    /**
     * Releases all the resources related to this player.
     */
    void releasePlayerResources();
}
