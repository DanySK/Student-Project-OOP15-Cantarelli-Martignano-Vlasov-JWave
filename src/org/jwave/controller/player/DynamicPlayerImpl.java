package org.jwave.controller.player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jwave.model.player.PlayMode;
import org.jwave.model.player.Song;

import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.ugens.FilePlayer;
import ddf.minim.ugens.Gain;

/**
 * This class is an implementation of {@link}DynamicPlayer.
 */
final class DynamicPlayerImpl implements DynamicPlayer {

    private static final int BUFFER_SIZE = 1024;
    private static final int OUT_BIT_DEPTH = 16;
    
    private Set<EObserver<? super Optional<PlayMode>, ? super Optional<Song>>> set;
    
    private Minim minim; 
    private FilePlayer player;
    private Gain volumeControl;
    private AudioOutput out;
    private PlayMode currentPlayMode;
    private boolean started;
    private boolean paused;
    private ClockAgent agent;
    
    /**
     * Creates a new DynamicPlayerImpl.
     */
    public DynamicPlayerImpl() { 
        this.minim = new Minim(FileSystemHandler.getFileSystemHandler());
        this.currentPlayMode = PlayMode.NO_LOOP;
        this.volumeControl = new Gain();
        this.started = false;
        this.paused = false;
        this.agent = new ClockAgent("Playback");
        this.set = new HashSet<>();
        this.agent.startClockAgent();
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
            throw new IllegalArgumentException();
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
        
        this.out = this.minim.getLineOut(Minim.STEREO, BUFFER_SIZE, sampleRateRetriever.sampleRate(), OUT_BIT_DEPTH);
        this.player.patch(this.volumeControl);
        this.volumeControl.patch(this.out);
        sampleRateRetriever.close();
    }
    
    private void setPaused(final boolean value) {
        this.paused = value;
    }
    
    private boolean isPlaying() {
        return this.player.isPlaying();
    }
    
    private boolean isPaused() {
        return this.paused;
    }
    
    private boolean hasStarted() {
        return this.started;
    }
    
    private void checkInReproduction() {
        if (this.isPlayerPresent() && !this.isPlaying() && this.hasStarted() && !this.isPaused()) {
            this.setPlayer(AudioSystem.getAudioSystem().getPlaylistManager().getPlayingQueue()
                    .selectSong(AudioSystem.getAudioSystem().getPlaylistManager().getPlaylistNavigator().next()));
            this.play();
        }
    }
    
    private boolean isPlayerPresent() {
        return this.player != null;
    }
    
    private final class ClockAgent implements Runnable {

        private Thread t;
        private String name;
        private volatile boolean stopped;
        
        ClockAgent(final String threadName) {
            this.stopped = false;
            this.name = threadName;
            this.t = new Thread(this, this.name);
//            this.t.start();
        }
        
        @Override
        public void run() {
            System.out.println("Running thread" + this.name);
            while (!this.isStopped()) {
                try {
                    DynamicPlayerImpl.this.checkInReproduction();
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                }
            }
        }
        
        public void startClockAgent() {
            this.setStopped(false);
            this.t.start();
        }
        
        public void stopClockAgent() {
            this.setStopped(true);
        }
        
        private boolean isStopped() {
            return this.stopped;
        }
        
        private void setStopped(final boolean value) {
            this.stopped = value;
        }
    }
}
