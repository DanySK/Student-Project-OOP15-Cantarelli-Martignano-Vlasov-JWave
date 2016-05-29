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
    private static final int LOWER_VOLUME_BOUND = -60;
    private static final int UPPER_VOLUME_BOUND = 20;
    
    private final Minim minim; 
    private FilePlayer player;
    private final Gain volumeControl;
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
    public void play() throws IllegalStateException {
        this.checkPlayerLoaded();
        this.player.play();
        if (this.isPaused()) {
            this.setPaused(false);
        }
        if (!this.hasStarted()) {
            this.started = true;
        }
    }

    @Override
    public void pause() throws IllegalStateException {
        this.checkPlayerLoaded();
        this.setPaused(true);
        this.player.pause();
    }

    @Override
    public void stop() {
        this.checkPlayerLoaded();
        this.pause();
        this.player.rewind();
    }

    @Override
    public void cue(final int millis) {
        this.checkPlayerLoaded();
        this.setPaused(true);
        this.player.cue(millis);
        this.setPaused(false);
    }

    @Override
    public int getLength() {
        this.checkPlayerLoaded();
        return this.player.length();
    }

    @Override
    public int getPosition() {
       this.checkPlayerLoaded();
       return this.player.position();
    }

    @Override
    public Optional<Song> getLoaded() {
        return this.loaded;
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
    public void setVolume(final int amount) {
        if (amount < LOWER_VOLUME_BOUND || amount > UPPER_VOLUME_BOUND) {
            throw new IllegalArgumentException("Value not allowed");
        }
        this.volumeControl.setValue(amount);
    }
    
    @Override
    public synchronized void setPlayer(final Song song) {
        final AudioPlayer sampleRateRetriever = minim.loadFile(song.getAbsolutePath());
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
    
    private AudioOutput createAudioOut(final float sampleRate) {
        return this.minim.getLineOut(Minim.STEREO, BUFFER_SIZE, sampleRate, OUT_BIT_DEPTH);
    }
    
    private void checkPlayerLoaded() {
        if (this.player == null) {
            throw new IllegalStateException("No song has been loaded");
        }
    }
}
