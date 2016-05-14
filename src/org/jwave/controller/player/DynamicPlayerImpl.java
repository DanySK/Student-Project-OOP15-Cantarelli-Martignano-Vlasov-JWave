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

/**
 * This class is an implementation of {@link}DynamicPlayer.
 */
public class DynamicPlayerImpl implements DynamicPlayer {

    private static final int BUFFER_SIZE = 1024;
    private static final int OUT_BIT_RATE = 16;

    private Set<EObserver<? super Optional<PlayMode>, ? super Optional<Song>>> set;
    
    private Minim minim; 
    private FilePlayer player;
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
        this.started = false;
        this.paused = false;
        this.agent = new ClockAgent("Playback");
        this.set = new HashSet<>();
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
        this.player.pause();
        this.setPaused(true);
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
        this.notifyEObservers(Optional.of(playMode), Optional.empty());
    }

    @Override
    public synchronized void setPlayer(final Song song) {
        AudioPlayer sampleRateRetriever = minim.loadFile(song.getAbsolutePath());
        if (this.player != null) {
            this.stop();
            this.out.close();
        }
        this.started = false;
        this.player = new FilePlayer(this.minim.loadFileStream(song.getAbsolutePath(), BUFFER_SIZE, true));
        this.stop();
        
        this.out = this.minim.getLineOut(Minim.STEREO, BUFFER_SIZE, sampleRateRetriever.sampleRate(), OUT_BIT_RATE);
        this.player.patch(this.out);
        sampleRateRetriever.close();
        this.notifyEObservers(Optional.empty(), Optional.of(song));
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
        
        public ClockAgent(final String threadName) {
            this.stopped = false;
            this.name = threadName;
            this.t = new Thread(this, this.name);
            this.t.start();
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
        
        public void stop() {
            this.setStopped(true);
        }
        
        private boolean isStopped() {
            return this.stopped;
        }
        
        private void setStopped(final boolean value) {
            this.stopped = value;
        }
    }

    @Override
    public void addEObserver(final EObserver<? super Optional<PlayMode>, ? super Optional<Song>> obs) {
        this.set.add(obs);
    }


    @Override
    public void notifyEObservers(final Optional<PlayMode> arg1, final Optional<Song> arg2) {
        this.set.forEach(obs -> obs.update(this, arg1, arg2));
    }


    @Override
    public void notifyEObservers(final Optional<PlayMode> arg) {
        this.set.forEach(obs -> obs.update(this, arg));
    }
}
