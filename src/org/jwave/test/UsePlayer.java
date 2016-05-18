package org.jwave.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jwave.controller.PlaybackThread;
import org.jwave.controller.player.AudioSystem;
import org.jwave.controller.player.DynamicPlayer;
import org.jwave.controller.player.PlaylistManager;

public final class UsePlayer {

//    private static final String PATH = "/Users/alexvlasov/Music";
//    private static final String PATH = "/home/canta/Music";
    private static final String WIN_PATH = "C:\\03 Amaranth.mp3";
    private UsePlayer() { }
    
    /**
     * Main to test some features while developing.
     * 
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws ClassNotFoundException
     */
    public static void main(final String... args) throws FileNotFoundException, IOException, IllegalArgumentException, ClassNotFoundException {
        final DynamicPlayer player = AudioSystem.getAudioSystem().getDynamicPlayer();
        final PlaylistManager manager = AudioSystem.getAudioSystem().getPlaylistManager();
//        final PlaybackThread t = new PlaybackThread("playback thread");
//        manager.openDir(PATH, true);
//        manager.savePlaylistToFile("playlistProva", PATH);
//        manager.loadPlaylist(PATH + "/playlistProva.jwo");
//        manager.getPlayingQueue().printPlaylist();
//        player.setPlayer(new SongImpl(PATH + "/03. The Gift Of Music.mp3"));
//        player.setPlayer(manager.getPlayingQueue().selectSong(0));
//        player.play();
//        player.pause();
//        t.start();
//        player.cue(235000);
//        System.out.println("current loaded: " + manager.getCurrentLoaded().get().getName());
//        System.out.println(player.isPlaying());
        //retrieving a value from metadata
//        System.out.println(manager.getPlayingQueue().getCurrentSelected().get().getMetaData().getAlbum());
//        manager.openDir(PATH + "/Infected_Mushroom-Converting_Vegetarians_II-mp3", false);
        manager.openFile(WIN_PATH, false);
        player.setPlayer(manager.getPlayingQueue().selectSong(0));
        manager.getPlayingQueue().printPlaylist();
        player.play();
    }

}
