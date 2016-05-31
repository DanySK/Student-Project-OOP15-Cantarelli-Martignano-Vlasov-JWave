package org.jwave.test.player;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.Song;
import org.jwave.model.player.SongImpl;

public class TestPlayer {

    private static final String PATH_ONE = "/home/canta/Music/Mistery.mp3";
    private static Song songOne;
    private static DynamicPlayer player;
    
    @BeforeClass
    public void oneTimeSetUp() {
        player = new DynamicPlayerImpl();
        songOne = new SongImpl(new File(PATH_ONE));
    }
    
    @Before
    public void setUp() {
        
    }
    
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
    }
}
