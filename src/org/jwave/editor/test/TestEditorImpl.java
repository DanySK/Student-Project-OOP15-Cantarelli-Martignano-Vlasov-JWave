package org.jwave.editor.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jwave.controller.editor.Editor;
import org.jwave.controller.editor.EditorImpl;
import org.jwave.model.player.SongImpl;

public class TestEditorImpl {
	private static final Editor songEditor = new EditorImpl();
	
    @BeforeClass
    public static void oneTimeSetUp() {
        songEditor.loadSongToEdit(new SongImpl(new File("/Users/alexvlasov/Downloads/hello.mp3")));
    }
    
    @Before
    public void setUp() {
    	songEditor.resetSong();
    }

	@Test
	public void testCorrectModifiableSongInitialization() {
		assertTrue(songEditor.getSong().getCuts().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0);
		assertTrue(songEditor.getOriginalSongLength() > 0);
		assertTrue(songEditor.getSong().getCut(0).getTo() == songEditor.getOriginalSongLength());
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength());
	}
	
	@Test
	public void testVeryBasicCopyAndPaste() {
		songEditor.setSelectionFrom(50000);
		songEditor.setSelectionTo(60000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(5000);
		songEditor.pasteCopiedSelection();		
		
		assertTrue(songEditor.getSong().getCuts().size() == 3);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 5000);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 5000);
		
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 5001 && songEditor.getSong().getCut(1).getTo() == 15001);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 60000);		
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 15002 && songEditor.getSong().getCut(2).getTo() == songEditor.getModifiedSongLength());
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 5000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
}
