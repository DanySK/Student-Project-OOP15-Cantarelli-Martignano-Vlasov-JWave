package org.jwave.controller.player;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.PlaylistManager;

public class ClockAgent implements Runnable {
    
    private Thread t;
    private String name;
    private DynamicPlayer dynPlayer;
    private PlaylistManager playlistManager;
    private volatile boolean stopped;
    
    public ClockAgent(final DynamicPlayer player, final PlaylistManager manager, final String threadName) {
        this.dynPlayer = player;
        this.playlistManager = manager;
        this.stopped = false;
        this.name = threadName;
        this.t = new Thread(this, this.name);
    }
    
    @Override
    public void run() {
        System.out.println("Running thread" + this.name);
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
