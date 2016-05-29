package org.jwave.test;

import java.io.File;
import java.util.Optional;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.PlaylistManagerImpl;
import org.jwave.model.player.Song;

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
        m.addAudioFile(new File("/home/canta/Music/03. The Gift Of Music.mp3"));
        m.addAudioFile(new File("/home/canta/Music/05-infected_mushroom-feelings.mp3"));
        System.out.println("PlaylistDimension = " + m.getDefaultPlaylist().getDimension());
        final Optional<Song> s = m.next(); 
        if (s.isPresent()) {
            p.setPlayer(s.get());
            p.play();
        } else {
            System.out.println("can't load the song");
        }
    }
}
