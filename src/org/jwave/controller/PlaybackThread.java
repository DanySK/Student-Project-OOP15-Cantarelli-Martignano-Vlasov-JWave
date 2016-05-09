package org.jwave.controller;

import org.jwave.controller.player.AudioSystem;
import org.jwave.controller.player.DynamicPlayer;
import org.jwave.controller.player.PlaylistManager;
import org.jwave.model.player.PlaylistNavigator;

public class PlaybackThread implements Runnable {
    private Thread t;
    private String threadName;
    private DynamicPlayer dynPlayer;
    private PlaylistManager playlistManager;
    private PlaylistNavigator navigator;
    private boolean stopped;
    private boolean previouslyPaused;
    
    
    
    /**
     * Consrtuctor
     * 
     * @param name
     */
    public PlaybackThread(String name) {
        this.threadName = name;
        this.t = new Thread (this, threadName);
        this.dynPlayer = AudioSystem.getAudioSystem().getDynamicPlayer();
        this.playlistManager = AudioSystem.getAudioSystem().getPlaylistManager();
        this.navigator = this.playlistManager.getPlaylistNavigator();
        this.stopped = false;
        this.previouslyPaused = false;
        System.out.println("Creating " +  threadName );
    }
    
    public void run() {
       System.out.println("Running " +  threadName );
       try {
           while (!this.isStopped()) {
              this.checkInReproduction(); //contiene notifica per il player
               Thread.sleep(10L);
           }
       } catch (InterruptedException e) {
           // System.out.println("Thread " +  threadName + " interrupted.");
       } 
//        System.out.println("Thread " +  threadName + " exiting.");
    }
    
    public void start() {
            System.out.println("Starting " +  threadName );
        
            this.setStopped(false);
            this.t.start ();        
    }
    
    public void stop() {
        this.setStopped(true);
    }
    
    private void checkInReproduction() {
       //notify player
    }
    
    private boolean isStopped() {
        return this.stopped;
    }
    
    private boolean wasPreviouslyPaused() {
        return this.previouslyPaused;
    }
    
    private void setStopped(final boolean value) {
        this.stopped = value;
    }
    
    private boolean isPlayerPresent() {
        return AudioSystem.getAudioSystem().getDynamicPlayer()!= null;
    }
 }
