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
	public void testBasicLongLengthCopyAndPaste() {
		songEditor.setSelectionFrom(0);
		songEditor.setSelectionTo(songEditor.getOriginalSongLength());
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(songEditor.getOriginalSongLength());
		songEditor.pasteCopiedSelection();		
		
		assertTrue(songEditor.getSong().getCuts().size() == 3);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == songEditor.getOriginalSongLength() - 1);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == songEditor.getOriginalSongLength() - 1);
		
		assertTrue(songEditor.getSong().getCut(1).getFrom() == songEditor.getOriginalSongLength() && songEditor.getSong().getCut(1).getTo() == songEditor.getOriginalSongLength() + songEditor.getOriginalSongLength() - 1);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(1).getSegment(0).getTo() == songEditor.getOriginalSongLength() - 1);			
	}
	
	@Test
	public void testMultiSegmentCopyAndPaste() {
		songEditor.setSelectionFrom(50000);
		songEditor.setSelectionTo(60000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(10000);
		songEditor.pasteCopiedSelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(5000);
		songEditor.setSelectionTo(25000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(50000);
		songEditor.pasteCopiedSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 5);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 30000);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 19999);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 59999);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 20000 && songEditor.getSong().getCut(2).getTo() == 49999);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 39999);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 50000 && songEditor.getSong().getCut(3).getTo() == 69999);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 3);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 5000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(3).getSegment(1).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(1).getTo() == 59999);
		assertTrue(songEditor.getSong().getCut(3).getSegment(2).getFrom() == 10000 && songEditor.getSong().getCut(3).getSegment(2).getTo() == 14999);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 70000 && songEditor.getSong().getCut(4).getTo() == songEditor.getOriginalSongLength() + 30000);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 40000 && songEditor.getSong().getCut(4).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
	
	@Test
	public void testSimpleCut() {
		songEditor.setSelectionFrom(10000);
		songEditor.setSelectionTo(100000);
		songEditor.cutSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 7);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 270000);
	}
	
	@Test
	public void testComplexTripleCopyAndPasteWithSameSelectionSingleSegments() {
		songEditor.setSelectionFrom(10000);
		songEditor.setSelectionTo(100000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(10000);
		songEditor.pasteCopiedSelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(200000);
		songEditor.pasteCopiedSelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(50000);
		songEditor.pasteCopiedSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 7);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 270000);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 49999);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 49999);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 50000 && songEditor.getSong().getCut(2).getTo() == 139999);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 99999);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 140000 && songEditor.getSong().getCut(3).getTo() == 189999);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 99999);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 190000 && songEditor.getSong().getCut(4).getTo() == 289999);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(4).getSegment(0).getTo() == 109999);
		
		assertTrue(songEditor.getSong().getCut(5).getFrom() == 290000 && songEditor.getSong().getCut(5).getTo() == 379999);
		assertTrue(songEditor.getSong().getCut(5).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(5).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(5).getSegment(0).getTo() == 99999);
		
		assertTrue(songEditor.getSong().getCut(6).getFrom() == 380000 && songEditor.getSong().getCut(6).getTo() == songEditor.getOriginalSongLength() + 270000);
		assertTrue(songEditor.getSong().getCut(6).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(6).getSegment(0).getFrom() == 110000 && songEditor.getSong().getCut(6).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}
	
	@Test
	public void testDoubleBasicCopyAndPaste() {
		songEditor.setSelectionFrom(50000);
		songEditor.setSelectionTo(60000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(10000);
		songEditor.pasteCopiedSelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(50000);
		songEditor.pasteCopiedSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 5);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 20000);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 19999);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 59999);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 20000 && songEditor.getSong().getCut(2).getTo() == 49999);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 39999);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 50000 && songEditor.getSong().getCut(3).getTo() == 59999);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 59999);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 60000 && songEditor.getSong().getCut(4).getTo() == songEditor.getOriginalSongLength() + 20000);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 40000 && songEditor.getSong().getCut(4).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
	
	@Test
	public void testVeryBasicThreeCutCopyAndPaste() {
		songEditor.setSelectionFrom(50000);
		songEditor.setSelectionTo(60000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(5000);
		songEditor.pasteCopiedSelection();		
		
		assertTrue(songEditor.getSong().getCuts().size() == 3);
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 10000);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 4999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 4999);
		
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 5000 && songEditor.getSong().getCut(1).getTo() == 14999);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 59999);		
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 15000  && songEditor.getSong().getCut(2).getTo() == songEditor.getModifiedSongLength());
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 5000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
}
