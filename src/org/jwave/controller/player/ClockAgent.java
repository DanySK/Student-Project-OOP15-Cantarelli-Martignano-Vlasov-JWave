package org.jwave.controller.player;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.PlaylistManager;

/**
 * This class is a clock for {@link DynamicPlayer}, determining controls at a specified interval. 
 *
 */
public class ClockAgent implements Runnable {
    
    private Thread t;
    private String name;
    private DynamicPlayer dynPlayer;
    private PlaylistManager playlistManager;
    private volatile boolean stopped;
    
    /**
     * Creates a new instance of ClockAgent.
     * 
     * @param player
     *          the player this clock agent has to control.
     *          
     * @param manager
     *          the manager this clock agent has to maintain referenced.
     *          
     * @param threadName
     *          name of the thread.
     */
    public ClockAgent(final DynamicPlayer player, final PlaylistManager manager, final String threadName) {
        this.dynPlayer = player;
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
        if (this.dynPlayer.getLoaded().isPresent() && !this.dynPlayer.isPlaying() && this.dynPlayer.hasStarted() && !this.dynPlayer.isPaused()) {
            this.dynPlayer.setPlayer(this.playlistManager.next());
            this.dynPlayer.play();
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


}
