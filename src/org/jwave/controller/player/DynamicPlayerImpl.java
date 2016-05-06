package org.jwave.controller.player;

import org.jwave.model.player.PlayMode;
import org.jwave.model.player.Song;

import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.ugens.FilePlayer;

/**
 * This class is an implementation of {@link}DynamicPlayer.
 */
public class DynamicPlayerImpl implements DynamicPlayer {

    private static final int BUFFER_SIZE = 1024;
    private static final int OUT_BIT_RATE = 16;
    
    private Minim minim; 
    private FilePlayer player;
    private AudioOutput out;
    private PlayMode currentPlayMode;
    
    /**
     * Creates a new DynamicPlayerImpl.
     */
    public DynamicPlayerImpl() { 
        this.minim = new Minim(FileSystemHandler.getFileSystemHandler());
        this.currentPlayMode = PlayMode.NO_LOOP;
    }
    
    
    @Override
    public void play() {
        this.player.play();
    }

    @Override
    public void pause() {
        this.player.pause();
    }

    @Override
    public void stop() {
        this.player.pause();
        this.player.rewind();
    }

    @Override
    public void cue(final int millis) {
        if (millis > this.getLength()) {
            throw new IllegalArgumentException();
        }
        this.player.cue(millis);
    }

    @Override
    public boolean isPlaying() {
        boolean out = false;
        try {
            out = this.player.isPlaying();
            return out;
        } catch (NullPointerException n) {
            return true;
        }
    }

    @Override
    public int getLength() {
        return this.player.length();
    }

    @Override
    public int getPosition() {
        return this.player.position();
    }

    @Override
    public PlayMode getPlayMode() {
        return this.currentPlayMode;
    }

    @Override
    public void setVolume(final int amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPlayMode(final PlayMode playMode) {
        this.currentPlayMode = playMode;
    }

    @Override
    public void setPlayer(final Song song) {
        final AudioPlayer sampleRateRetriever = minim.loadFile(song.getAbsolutePath());
        this.player = new FilePlayer(this.minim.loadFileStream(song.getAbsolutePath(), BUFFER_SIZE, true));
        this.pause();
        this.out = this.minim.getLineOut(Minim.STEREO, BUFFER_SIZE, sampleRateRetriever.sampleRate(), OUT_BIT_RATE);
        this.player.patch(this.out);
        sampleRateRetriever.close();
    }
}
