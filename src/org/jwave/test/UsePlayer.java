package org.jwave.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.jwave.controller.player.Controller;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.MetaData;
import org.jwave.model.player.SongImpl;

import com.mpatric.mp3agic.NotSupportedException;

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
        player.setPlayer(new SongImpl(new File("/home/canta/Music/02. Cornfield Chase.mp3")));
//        player.setPlayer(new SongImpl(new File("/home/canta/Music/DREAM OF LOVE.wav")));
//        player.play();
//        System.out.println("has album image " + player.getLoaded().get().getMetaDataV2().getAlbumArtwork().isPresent());
        System.out.println(player.getLoaded().get().getMetaData().retrieve(MetaData.ALBUM));
        try {
            player.getLoaded().get().getMetaData().setData(MetaData.ALBUM, "Mastrota Lord of the Steel");
            player.getLoaded().get().getMetaData().writeMetaDataToFile();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | NotSupportedException | IOException e) {
            System.err.println("Problem encountered while writing metadata.");
            e.printStackTrace();
        } 
        System.out.println(player.getLoaded().get().getMetaData().retrieve(MetaData.ALBUM));
    }
}
