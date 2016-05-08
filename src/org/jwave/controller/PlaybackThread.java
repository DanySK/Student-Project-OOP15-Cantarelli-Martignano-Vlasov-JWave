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
    private boolean playerInitialized;
//    AudioPlayer player;
//    List<Cut> editCuts;
//    Pair<Integer, Integer> currentSegment;
//    int currentSegmentIndex;
//    int currentCutIndex;
    
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
        this.stopped = true;
        this.playerInitialized = false;
//        player = playa;
//        editCuts = new ArrayList<Cut>(cuts);
        System.out.println("Creating " +  threadName );
        
//        currentSegmentIndex = 0;
//        currentCutIndex = 0;
//        currentSegment = editCuts.get(currentCutIndex).getSegments().get(currentSegmentIndex);
    }
    
    public void run() {
       System.out.println("Running " +  threadName );
       try {
           while (!this.isStopped()) {
//               System.out.println("Dentro al while");
//                   if (player.position() >= (int)currentSegment.getY()) {
//                           currentSegmentIndex++;
//                           
//                           if (currentSegmentIndex >= editCuts.get(currentCutIndex).getSegments().size()) {
//                                   currentCutIndex++;
//                                   currentSegmentIndex = 0;
//                                   currentSegment = editCuts.get(currentCutIndex).getSegments().get(currentSegmentIndex);
//                           }
//                           
//                           player.cue((int)currentSegment.getX());
//                   }
               try {
                   this.checkInReproduction();
               } catch (NullPointerException n) {
                   System.out.println("Null avoided");
               }
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
    
    private boolean isStopped() {
        return this.stopped;
    }
    
    private void checkInReproduction() {
        if (this.isPlayerPresent()) {
            if (!this.isPlayerInitialized()) {
                this.playerInitialized = true;
                System.out.println("Player initialized");
            }
            System.out.println("Player is playing");
            return;
        }
        if (!this.isPlayerInitialized()) {
            System.out.println("Player not init");
            return;
        }
        System.out.println("Setting player...");
        this.dynPlayer.setPlayer(this.playlistManager.getPlayingQueue().selectSong
               (this.playlistManager.getPlaylistNavigator().next()));
        return;
    }
    
    private void setStopped(boolean value) {
        this.stopped = value;
    }
    
    private boolean isPlayerInitialized() {
        return this.playerInitialized;
    }
    
    private boolean isPlayerPresent() {
        try {
            this.dynPlayer.isPlaying();
        } catch(NullPointerException ne) {
            return false;
        } 
        return true;
    }
 }
