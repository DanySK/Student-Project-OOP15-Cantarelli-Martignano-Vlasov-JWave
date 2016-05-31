package org.jwave.controller.player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.PlayMode;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.Song;
import org.jwave.view.PlayerController;

/**
 * This class is a clock for {@link DynamicPlayer}, determining controls at a specified interval. 
 *
 */
public class ClockAgent implements Runnable {
    
    public static enum Mode {
        /**
         * 
         */
        PLAYER,
        
        EDITOR;
    }
    
    private final Thread t;
    private final DynamicPlayer dynPlayer;
    private PlaylistManager playlistManager;
    private Set<PlayerController> controllerSet;
    private final ClockAgent.Mode mode;
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
        this.notifyControllers();
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
    public void addController(final PlayerController newController) {
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
    
    private void notifyControllers() {
        if (!this.dynPlayer.isEmpty()) {
            this.controllerSet.forEach(c -> c.updatePosition(this.dynPlayer.getPosition()));
        }
    }
}
