package org.jwave.controller.player;

import org.jwave.model.player.PlayMode;
import org.jwave.model.player.Song;

/**
 * Interface DynamicPlayer
 */
public interface DynamicPlayer {

    public void play();

    public void pause();

    public void stop();
    
    public void cue(int millis);

    public boolean isPlaying();
    
    public int getLength();

    public int getPosition();

    public PlayMode getPlayMode();
    
    public void setVolume(int amount);
    
    public void setPlayMode(PlayMode playMode);
    
    public void setPlayer(Song song);
}
