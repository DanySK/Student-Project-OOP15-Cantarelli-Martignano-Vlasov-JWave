package org.jwave.test;

import java.io.File;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
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
        p.setPlayer(new SongImpl(new File("/home/canta/Music/03. The Gift Of Music.mp3")));
        p.play();
    }
}
