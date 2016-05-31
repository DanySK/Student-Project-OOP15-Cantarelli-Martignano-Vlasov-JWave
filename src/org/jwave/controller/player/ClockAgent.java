package org.jwave.controller.player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jwave.controller.UpdatableController;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.Song;
import org.jwave.model.playlist.PlayMode;
import org.jwave.model.playlist.PlaylistManager;

/**
 * This class is a clock for {@link DynamicPlayer}, determining controls at a specified interval. 
 *
 */
public class ClockAgent implements Runnable {
    
    /**
     * Enum describing ClockAgent modes.
     *
     */
    public enum Mode {
        /**
         * Checks what to to do when a song has finished. 
         */
        PLAYER,
        
        /**
         * Checks if the reproduced song has some cuts inside.
         */
        EDITOR;
    }
    
    private final Thread t;
    private final DynamicPlayer dynPlayer;
    private PlaylistManager playlistManager;
    private Set<UpdatableController> controllerSet;
    private final ClockAgent.Mode mode;
    private volatile boolean stopped;
    
    /**
     * Creates a new instance of ClockAgent.
     * 
     * @param player
     *          the player this clock agent has to control.
     *          
     *          
     * @param manager
     *          the manager this clock agent has to maintain referenced.
     *          
     */
    public ClockAgent(final DynamicPlayer player, final PlaylistManager manager, final ClockAgent.Mode checkMode) {
        this.dynPlayer = player;
        this.playlistManager = manager;
        this.stopped = false;
        this.controllerSet = new HashSet<>();
        this.mode = checkMode;
        this.t = new Thread(this);
    }
    
    @Override
    public void run() {
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
        switch (this.mode) {
        case EDITOR:
            this.checkEditor();
            break;
        default:
            this.checkPlayer();
        }
        if (this.dynPlayer.isPlaying()) {
            this.controllerSet.forEach(c -> {
                c.updatePosition(this.dynPlayer.getPosition());
                c.updateReproductionInfo(this.dynPlayer.getLoaded().get());
            });
            
        }
    }
    
    /**
     * Starts the clock agent thread.
     */
    public void startClockAgent() {
        this.setStopped(false);
        this.t.start();
    }
    
    /**
     * Adds a controller to the agent. 
     * 
     * @param newController
     *          the controller to be added.
     */
    public void addController(final UpdatableController newController) {
        this.controllerSet.add(newController);
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
        if (this.dynPlayer.getLoaded().isPresent()) {
            this.dynPlayer.isPlaying();
        }
    }
}
