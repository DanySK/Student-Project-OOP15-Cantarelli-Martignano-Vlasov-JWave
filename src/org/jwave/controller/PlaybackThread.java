package org.jwave.controller;

import org.jwave.controller.player.AudioSystem;
import org.jwave.controller.player.DynamicPlayer;
import org.jwave.controller.player.PlaylistManager;

public class PlaybackThread implements Runnable {
    private Thread t;
    private String threadName;
    private DynamicPlayer dynPlayer;
    private PlaylistManager playlistManager;
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
    PlaybackThread(String name) {
        threadName = name;
        this.dynPlayer = AudioSystem.getAudioSystem().getDynamicPlayer();
        this.playlistManager = AudioSystem.getAudioSystem().getPlaylistManager();
        
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
           while (this.dynPlayer.getPosition() < this.dynPlayer.getLength()) {
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
           
                   Thread.sleep(10);
           }
       } catch (InterruptedException e) {
           // System.out.println("Thread " +  threadName + " interrupted.");
       }
       
       // System.out.println("Thread " +  threadName + " exiting.");
    }
    
    public void start() {
            System.out.println("Starting " +  threadName );
        
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();         
        }
    }
 }
