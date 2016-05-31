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
        try {
            PlaylistController.saveDefaultPlaylistToFile(m.getDefaultPlaylist(), m.getDefaultPlaylist().getName());
        } catch (IOException e1) {
            System.out.println("catched exception");
            e1.printStackTrace();
        }
        Playlist p1 = m.createNewPlaylist("colorata");
        p1.addSong(new SongImpl(new File("/home/canta/Music/03. The Gift Of Music.mp3")));
        try {
            m.addAudioFile(new File("/home/canta/Music/03. The Gift Of Music.mp3"));
        } catch (Exception e) {
            System.out.println("Catturata eccezione in addFile");
        }
        Playlist extractedPlaylist;
        try {
            PlaylistController.savePlaylistToFile(p1, p1.getName());
        } catch (IOException e1) {
            System.out.println("Eccezione in scrittura di colorata");
            e1.printStackTrace();
        } 
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(new File("/home/canta/JWavePlaylists/colorata.jwo"))))) {
            extractedPlaylist = (Playlist) ois.readObject();
            System.out.println("dim = " + extractedPlaylist.getDimension());
            System.out.println(extractedPlaylist.getSongAtIndex(0).getName());
        } catch (FileNotFoundException e) {
           System.out.println("Eccezione in lettura");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Eccezione 2 in lettura");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Eccezione 3 in lettura");
            e.printStackTrace();
        } 
    }
}
