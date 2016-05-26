package org.jwave.test;

import java.io.File;

import org.jwave.controller.player.Controller;
import org.jwave.model.player.DynamicPlayer;
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
        final Controller c = new Controller();
        final DynamicPlayer player = c.getDynamicPlayer();
//        final PlaylistManager manager = c.getPlaylistManager();
//        player.setPlayer(new SongImpl(new File("/home/canta/Music/02. Cornfield Chase.mp3")));
//        player.setPlayer(new SongImpl(new File("/home/canta/Music/DREAM OF LOVE.wav")));
//        player.play();
//        System.out.println("has album image " + player.getLoaded().get().getMetaDataV2().getAlbumArtwork().isPresent());
    }
}
