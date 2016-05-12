package org.jwave.controller.player;

import java.util.Optional;

import org.jwave.model.player.PlayMode;
import org.jwave.model.player.Song;

/**
 * This interface represents a dynamic player.
 */
public interface DynamicPlayer extends ESource<Optional<PlayMode>, Optional<Song>> {

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
     * @throws IllegalArgumentException
     *                   
     */
    void cue(int millis) throws IllegalArgumentException;
    
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
     * @return
     *          the current play mode (default is {@link}Playmode.NO_LOOP)
     */
    PlayMode getPlayMode();
    
    /**
     * Modifies the current volume.
     * 
     * @param amount
     *          the amount of volume to be set.         
     */
    void setVolume(int amount);
    
    /**
     * 
     * @param playMode
     *          the play mode to be set.
     */
    void setPlayMode(PlayMode playMode);
    
    /**
     * Sets the player by loading a song.
     * 
     * @param song
     *          the song to be loaded.
     */
    void setPlayer(Song song);
}
