package org.jwave.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jwave.controller.player.PlaylistController;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.PlaylistManagerImpl;
import org.jwave.model.player.SongImpl;

/**
 * This class is intended for testing player functionalities.
 *
 */
public final class UsePlayer {

    private UsePlayer() { }
    
    /**
     * Main to test some features while developing.
     * 
     * @param args
     *          arguments.
     */
    public static void main(final String... args) {
        //to be filled with junit test.
        final DynamicPlayer p = new DynamicPlayerImpl();
        final PlaylistManager m = new PlaylistManagerImpl(new PlaylistImpl("GianCartone"));
        m.addAudioFile(new File("/home/canta/Music/2-02 Here's to You.mp3"));
        p.setPlayer(m.selectSongFromPlayingQueueAtIndex(0));
        p.play();
        p.setVolume(70f);
    }
}
