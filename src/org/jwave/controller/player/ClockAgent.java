package org.jwave.controller.player;

import java.util.Optional;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.PlayMode;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.Song;

/**
 * This class is a clock for {@link DynamicPlayer}, determining controls at a specified interval. 
 *
 */
public class ClockAgent implements Runnable {
    
    private final Thread t;
    private final String name;
    private DynamicPlayer dynPlayer;
    private DynamicPlayer dynEditorPlayer;
    private PlaylistManager playlistManager;
    private volatile boolean stopped;
    
    /**
     * Creates a new instance of ClockAgent.
     * 
     * @param player
     *          the player this clock agent has to control.
     *          
     * @param editorPlayer
     * 			the editor player this clock agent has to control.
     *          
     * @param manager
     *          the manager this clock agent has to maintain referenced.
     *          
     * @param threadName
     *          name of the thread.
     */
    public ClockAgent(final DynamicPlayer player, final DynamicPlayer editorPlayer, final PlaylistManager manager, final String threadName) {
        this.dynPlayer = player;
        this.dynEditorPlayer = editorPlayer;
        this.playlistManager = manager;
        this.stopped = false;
        this.name = threadName;
        this.t = new Thread(this, this.name);
    }
    
    @Override
    public void run() {
//        System.out.println("Running thread" + this.name);
        while (!this.isStopped()) {
            try {
                this.checkInReproduction();
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }
    
    private void checkInReproduction() {
        this.checkPlayer();
        this.checkEditor();
    }
    
    /**
     * Starts the clock agent thread.
     */
    public void startClockAgent() {
        this.setStopped(false);
        this.t.start();
    }
    
    /**
     * Sets the {@link DynamicPlayer} referenced by this agent.
     * 
     * @param player
     *          the player to be referenced by this agent.
     */
    public void setPlayer(final DynamicPlayer player) {
        this.dynPlayer = player;
    }
    
    /**
     * Stops the clock agent thread.
     */
    public void stopClockAgent() {
        this.setStopped(true);
    }
    
    private boolean isStopped() {
        return this.stopped;
    }
    
    private void setStopped(final boolean value) {
        this.stopped = value;
    }
    
    private void checkPlayer() {
        final PlayMode currentMode = this.playlistManager.getPlayMode();
        if (!this.dynPlayer.isEmpty() && !this.dynPlayer.isPlaying() && this.dynPlayer.hasStarted() 
                && !this.dynPlayer.isPaused()) {
            switch (currentMode) {
            case LOOP_ONE:
                this.dynPlayer.stop();
                this.dynPlayer.play();
                break;
            case NO_LOOP:
                final Optional<Song> current = this.dynPlayer.getLoaded();
                if (current.isPresent()) {
                    if (this.playlistManager.getPlayingQueue().indexOf(current.get().getSongID()) 
                            >= (this.playlistManager.getPlayingQueue().getDimension() - 1)) {
                        this.dynPlayer.setPlayer(this.playlistManager.selectSongFromPlayingQueueAtIndex(0));
                        break;
                    }
                }
            default:
                final Optional<Song> nextSong = this.playlistManager.next();
                if (nextSong.isPresent()) {
                    this.dynPlayer.setPlayer(nextSong.get());
                    this.dynPlayer.play();
                }
            }
        }
    }
    
    private void checkEditor() {
        if (this.dynEditorPlayer.getLoaded().isPresent()) {
            this.dynEditorPlayer.isPlaying();
        }
    }
}
