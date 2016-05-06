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
               this.checkInReproduction();
               Thread.sleep(10L);
           }
       } catch (InterruptedException e) {
           // System.out.println("Thread " +  threadName + " interrupted.");
       } catch (NullPointerException n) { }
       
       // System.out.println("Thread " +  threadName + " exiting.");
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
        if (!this.dynPlayer.isPlaying()) {
            this.dynPlayer.setPlayer(this.playlistManager.getPlayingQueue().selectSong(
                    this.navigator.next()));
            this.dynPlayer.play();
        }
    }
    
    private void setStopped(boolean value) {
        this.stopped = value;
    }
 }
