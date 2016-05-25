package org.jwave.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.jwave.controller.player.Controller;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.MetaData;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.SongImpl;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

public final class UsePlayer {

    private UsePlayer() { }
    
    /**
     * Main to test some features while developing.
     * 
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws ClassNotFoundException
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws InvalidDataException 
     * @throws UnsupportedTagException 
     */
    public static void main(final String... args) throws FileNotFoundException, IOException, IllegalArgumentException, ClassNotFoundException, UnsupportedTagException, InvalidDataException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        //to be filled with junit test.
        final Controller c = new Controller();
        final DynamicPlayer player = c.getDynamicPlayer();
        final PlaylistManager manager = c.getPlaylistManager();
        player.setPlayer(new SongImpl(new File("/home/canta/Music/05-infected_mushroom-feelings.mp3")));
//        System.out.println(player.getLoaded().get().getMetaDataV2().retrieve(MetaData.ALBUM_ARTIST));
//        System.out.println(player.getLoaded().get().getMetaData().getAlbum());
//        System.out.println(player.getLoaded().get().getMetaData().getAuthor());
    }
}
