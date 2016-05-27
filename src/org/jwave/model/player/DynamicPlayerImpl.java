package org.jwave.model.player;

import java.util.Optional;

import org.jwave.controller.player.FileSystemHandler;

import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.ugens.FilePlayer;
import ddf.minim.ugens.Gain;

/**
 * This class is an implementation of {@link}DynamicPlayer.
 */
public class DynamicPlayerImpl implements DynamicPlayer {

    private static final int BUFFER_SIZE = 1024;
    private static final int OUT_BIT_DEPTH = 16;
    
    private Minim minim; 
    private FilePlayer player;
    private Gain volumeControl;
    private AudioOutput out;
    private boolean started;
    private boolean paused;
    private Optional<Song> loaded;
    
    /**
     * Creates a new DynamicPlayerImpl.
     */
    public DynamicPlayerImpl() { 
        this.minim = new Minim(FileSystemHandler.getFileSystemHandler());
        this.volumeControl = new Gain();
        this.started = false;
        this.paused = false;
        this.loaded = Optional.empty();
    }
    
    
    @Override
    public void play() {
        this.player.play();
        if (this.isPaused()) {
            this.setPaused(false);
        }
        if (!this.hasStarted()) {
            this.started = true;
        }
    }

    @Override
    public void pause() {
        this.setPaused(true);
        this.player.pause();
    }

    @Override
    public void stop() {
        this.pause();
        this.player.rewind();
    }

    @Override
    public void cue(final int millis) {
        if (millis > this.getLength()) {
            throw new IllegalArgumentException("Out of song length");
        }
        this.setPaused(true);
        this.player.cue(millis);
        this.setPaused(false);
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
    public void setVolume(final int amount) {
        //TODO add limit to the amount value
        this.volumeControl.setValue(amount);
    }

    @Override
    public synchronized void setPlayer(final Song song) {
        AudioPlayer sampleRateRetriever = minim.loadFile(song.getAbsolutePath());
        if (this.player != null) {
            this.stop();
            this.player.unpatch(this.volumeControl);
            this.volumeControl.unpatch(this.out);
            this.out.close();
        }
        this.started = false;
        this.player = new FilePlayer(this.minim.loadFileStream(song.getAbsolutePath(), BUFFER_SIZE, false));
        this.player.pause();
        
        this.out = this.createAudioOut(sampleRateRetriever.sampleRate());
        this.player.patch(this.volumeControl);
        this.volumeControl.patch(this.out);
        sampleRateRetriever.close();
        this.loaded = Optional.of(song);
    }
    
    private void setPaused(final boolean value) {
        this.paused = value;
    }
    
    @Override
    public boolean isPlaying() {
        return this.player.isPlaying();
    }
    
    @Override
    public boolean isPaused() {
        return this.paused;
    }
    
    @Override
    public boolean hasStarted() {
        return this.started;
    }
    
    @Override
    public Optional<Song> getLoaded() {
        return this.loaded;
    }
    
    //method created to verify that ther are no error due to output creation.
    private AudioOutput createAudioOut(final float sampleRate) {
        return this.minim.getLineOut(Minim.STEREO, BUFFER_SIZE, sampleRate, OUT_BIT_DEPTH);
    }
}
