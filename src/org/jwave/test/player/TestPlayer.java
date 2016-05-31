package org.jwave.test.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.PlaylistManagerImpl;
import org.jwave.model.player.Song;
import org.jwave.model.player.SongImpl;

public class TestPlayer {

    private static final String PATH_ONE = "/home/canta/Music/Mistery.mp3";
    private static final String PATH_TWO = "/home/canta/Music/Snow Time.mp3";
    private static Song songOne;
    private static DynamicPlayer player;
    private static PlaylistManager manager;
    
    @BeforeClass
    public static void oneTimeSetUp() {
        player = new DynamicPlayerImpl();
        songOne = new SongImpl(new File(PATH_ONE));
        manager = new PlaylistManagerImpl(new PlaylistImpl("defaultProva"));
    }
    
//    @Before
//    public void setUp() {
//        
//    }
    
    @Test
    public void testPlayerInitialization() {
        assertTrue("Player should be empty.", player.isEmpty());
        assertFalse("Player should have not started", player.hasStarted());
        assertFalse("Player should not be paused", player.isPaused());
    }
    
    @Test
    public void testPlayerSetUpAndReproduction() {
        player.setPlayer(songOne);
        assertFalse("Player should not be empty", player.isEmpty());
        player.play();
        assertTrue("Player should have started", player.hasStarted());
        assertTrue("Player should be playing", player.isPlaying());
        player.pause();
        assertTrue("Player should be in pause", player.isPaused());
        player.stop();
        assertTrue("Player should have started the loaded song at least one time.", player.hasStarted());
    }
    
    @Test
    public void testPlaylistManagerInitialization() {
        assertEquals("No song should have been loaded in the default playlsit", 
                manager.getDefaultPlaylist().getDimension(), 0);
        assertEquals("The playing queue should correspond to the default playlist", manager.getDefaultPlaylist(), manager.getPlayingQueue());
        try {
            manager.selectSongFromPlayingQueueAtIndex(0);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalArgumentException ex) { }
    }
    
    @Test
    public void testPlaylistManagerFunctionalities() {
        try {
            manager.addAudioFile(new File(PATH_ONE));
        } catch (Exception ex) {
            fail("An exception occurring while creating file");
        }
        assertEquals("Now default playlist dimension should be 1.", manager.getDefaultPlaylist().getDimension(), 1); 
        final Playlist playlist = manager.createNewPlaylist("z1b");
        try {
            final Playlist playlistTwo = manager.createNewPlaylist("z1b");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ie) { }
        
    }
}
