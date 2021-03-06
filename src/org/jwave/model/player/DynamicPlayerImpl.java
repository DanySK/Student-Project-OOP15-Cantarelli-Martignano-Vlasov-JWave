package org.jwave.model.player;

import java.util.Optional;

import org.jwave.model.FileSystemHandler;

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
    private static final float LOWER_VOLUME_BOUND = 0f;
    private static final float UPPER_VOLUME_BOUND = 70.0f;
    private static final float NORMALIZER = 60f;
    
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
        this.minim = new Minim(new FileSystemHandler());
        this.volumeControl = new Gain();
        this.volumeControl.setValue(10f);
        this.started = false;
        this.paused = false;
        this.loaded = Optional.empty();
    }
    
    
    @Override
    public void play() {
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
    public void pause() {
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
        if (this.isEmpty()) {
            return false;
        }
        return this.player.isPlaying();
    }
    
    @Override
    public boolean isPaused() {
        if (this.isEmpty()) {
            return false;
        }
        return this.paused;
    }
    
    @Override
    public boolean hasStarted() {
        if (this.isEmpty()) {
            return false;
        }
        return this.started;
    }
   
    @Override
    public boolean isEmpty() {
        return this.player == null;
    }

    @Override
    public void setVolume(final float amount) throws IllegalArgumentException {
        if (amount < LOWER_VOLUME_BOUND || amount > UPPER_VOLUME_BOUND) {
            throw new IllegalArgumentException("Value not allowed");
        }
        this.volumeControl.setValue(amount - NORMALIZER);
    }
    
    @Override
    public synchronized void setPlayer(final Song song) {
        final AudioPlayer sampleRateRetriever = minim.loadFile(song.getAbsolutePath());
        this.clearPlayer();
        this.player = new FilePlayer(this.minim.loadFileStream(song.getAbsolutePath(), BUFFER_SIZE, false));
        this.player.pause();
        
        this.out = this.createAudioOut(sampleRateRetriever.sampleRate());
        this.player.patch(this.volumeControl);
        this.volumeControl.patch(this.out);
        sampleRateRetriever.close();
        this.loaded = Optional.of(song);
    }
    
    @Override
    public void resetPlayer() {
        this.clearPlayer();
    }
    
    private void setPaused(final boolean value) {
        this.paused = value;
    }
    
    @Override
    public void releasePlayerResources() {
        if (this.clearPlayer()) {
            this.minim.stop();
        }
    }  
    
    private AudioOutput createAudioOut(final float sampleRate) {
        return this.minim.getLineOut(Minim.STEREO, BUFFER_SIZE, sampleRate, OUT_BIT_DEPTH);
    }
    
    private void checkPlayerLoaded() {
        if (this.isEmpty()) {
            throw new IllegalStateException("No song has been loaded");
        }
    }

    private boolean clearPlayer() {
        if (this.player != null) {
            this.stop();
            this.player.unpatch(this.volumeControl);
            this.volumeControl.unpatch(this.out);
            this.out.close();
            this.player.close();
            this.started = false;
            return true;
        }
        return false;
    }
}
