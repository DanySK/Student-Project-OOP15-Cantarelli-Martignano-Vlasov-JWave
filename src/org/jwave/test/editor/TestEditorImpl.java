package org.jwave.test.editor;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jwave.controller.editor.Editor;
import org.jwave.controller.editor.EditorImpl;
import org.jwave.model.player.SongImpl;

/**
 * Song loaded for testing purposes must have a minimum length (of >300s), otherwise
 * cursor positioning will not work as expected and tests will fail incorrectly.
 *
 */
public class TestEditorImpl {
	private static final Editor songEditor = new EditorImpl();
	
    @BeforeClass
    public static void oneTimeSetUp() {
        songEditor.loadSongToEdit(new SongImpl(
        						new File(System.getProperty("user.dir") + 
        								System.getProperty("file.separator") + "res" + 
        								System.getProperty("file.separator") + "Snow Time.mp3")));
    }
    
    @Before
    public void setUp() {
    	songEditor.resetSong();
    	songEditor.deselectSelection();
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
	public void testMultiSegmentCut() {
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
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(57500);
		songEditor.setSelectionTo(75000);
		songEditor.cutSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 5);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 12501);		
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 20000);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 60000);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 20001 && songEditor.getSong().getCut(2).getTo() == 49999);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 39998);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 50000 && songEditor.getSong().getCut(3).getTo() == 57499);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 2);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 5000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(3).getSegment(1).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(1).getTo() == 52499);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 57500 && songEditor.getSong().getCut(4).getTo() == songEditor.getOriginalSongLength() + 12501);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 44999 && songEditor.getSong().getCut(4).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
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
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 30002);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 20000);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 60000);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 20001 && songEditor.getSong().getCut(2).getTo() == 49999);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 39998);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 50000 && songEditor.getSong().getCut(3).getTo() == 70000);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 3);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 5000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(3).getSegment(1).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(1).getTo() == 60000);
		assertTrue(songEditor.getSong().getCut(3).getSegment(2).getFrom() == 10000 && songEditor.getSong().getCut(3).getSegment(2).getTo() == 14999);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 70001 && songEditor.getSong().getCut(4).getTo() == songEditor.getOriginalSongLength() + 30002);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 39999 && songEditor.getSong().getCut(4).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
	
	@Test
	public void testSimpleCut() {
		songEditor.setSelectionFrom(10000);
		songEditor.setSelectionTo(100000);
		songEditor.cutSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 2);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() - 90001);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == songEditor.getOriginalSongLength() - 90001);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 100001 && songEditor.getSong().getCut(1).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}
	
	@Test
	public void testSimpleCutAtVeryEndOfSong() {
		songEditor.setSelectionFrom(songEditor.getOriginalSongLength() - 10000);
		songEditor.setSelectionTo(songEditor.getOriginalSongLength());
		songEditor.cutSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 1);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() - 10001);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == songEditor.getOriginalSongLength() - 10001);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == songEditor.getOriginalSongLength() - 10001);	
	}
	
	@Test
	public void testSimplePasteAtVeryBeginningOfSong() {
		songEditor.setSelectionFrom(25000);
		songEditor.setSelectionTo(50000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(-1);
		songEditor.pasteCopiedSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 2);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 25001);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 25000);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 25000 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 50000);
		
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 25001 && songEditor.getSong().getCut(1).getTo() == songEditor.getOriginalSongLength() + 25001);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(1).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
	
	@Test
	public void testSimplePasteAtVeryEndOfSong() {
		songEditor.setSelectionFrom(0);
		songEditor.setSelectionTo(songEditor.getOriginalSongLength());
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(songEditor.getOriginalSongLength() + 1);
		songEditor.pasteCopiedSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 2);
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + songEditor.getOriginalSongLength() + 1);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == songEditor.getOriginalSongLength());
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == songEditor.getOriginalSongLength());
		
		assertTrue(songEditor.getSong().getCut(1).getFrom() == songEditor.getOriginalSongLength() + 1 && songEditor.getSong().getCut(1).getTo() == songEditor.getOriginalSongLength() + songEditor.getOriginalSongLength() + 1);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(1).getSegment(0).getTo() == songEditor.getOriginalSongLength());			
	}	
	
	@Test
	public void testSimpleCutAtVeryBeginningOfSong() {
		songEditor.setSelectionFrom(0);
		songEditor.setSelectionTo(10000);
		songEditor.cutSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 1);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() - 10001);		
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == songEditor.getOriginalSongLength() - 10001);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 10001 && songEditor.getSong().getCut(0).getSegment(0).getTo() == songEditor.getOriginalSongLength());	
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
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 270003);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 49999);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 49999);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 50000 && songEditor.getSong().getCut(2).getTo() == 140000);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 100000);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 140001 && songEditor.getSong().getCut(3).getTo() == 190001);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 100000);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 190002 && songEditor.getSong().getCut(4).getTo() == 290000);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(4).getSegment(0).getTo() == 109998);
		
		assertTrue(songEditor.getSong().getCut(5).getFrom() == 290001 && songEditor.getSong().getCut(5).getTo() == 380001);
		assertTrue(songEditor.getSong().getCut(5).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(5).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(5).getSegment(0).getTo() == 100000);
		
		assertTrue(songEditor.getSong().getCut(6).getFrom() == 380002 && songEditor.getSong().getCut(6).getTo() == songEditor.getOriginalSongLength() + 270003);
		assertTrue(songEditor.getSong().getCut(6).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(6).getSegment(0).getFrom() == 109999 && songEditor.getSong().getCut(6).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
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
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 20002);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 20000);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 60000);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 20001 && songEditor.getSong().getCut(2).getTo() == 49999);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 39998);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 50000 && songEditor.getSong().getCut(3).getTo() == 60000);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 60000);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 60001 && songEditor.getSong().getCut(4).getTo() == songEditor.getOriginalSongLength() + 20002);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 39999 && songEditor.getSong().getCut(4).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
	
	@Test
	public void testDoubleBasicCopyAndPasteReverseOrder() {
		songEditor.setSelectionFrom(50000);
		songEditor.setSelectionTo(60000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(50000);
		songEditor.pasteCopiedSelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(10000);
		songEditor.pasteCopiedSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 5);	
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 20002);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 9999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 9999);
	
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 10000 && songEditor.getSong().getCut(1).getTo() == 20000);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 60000);
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 20001 && songEditor.getSong().getCut(2).getTo() == 60000);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 10000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == 49999);
		
		assertTrue(songEditor.getSong().getCut(3).getFrom() == 60001 && songEditor.getSong().getCut(3).getTo() == 70001);
		assertTrue(songEditor.getSong().getCut(3).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(3).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(3).getSegment(0).getTo() == 60000);
		
		assertTrue(songEditor.getSong().getCut(4).getFrom() == 70002 && songEditor.getSong().getCut(4).getTo() == songEditor.getOriginalSongLength() + 20002);
		assertTrue(songEditor.getSong().getCut(4).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(4).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(4).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
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
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() + 10001);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 4999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 4999);
		
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 5000 && songEditor.getSong().getCut(1).getTo() == 15000);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 50000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == 60000);		
		
		assertTrue(songEditor.getSong().getCut(2).getFrom() == 15001  && songEditor.getSong().getCut(2).getTo() == songEditor.getOriginalSongLength() + 10001);
		assertTrue(songEditor.getSong().getCut(2).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(2).getSegment(0).getFrom() == 5000 && songEditor.getSong().getCut(2).getSegment(0).getTo() == songEditor.getOriginalSongLength());		
	}	
	
	@Test
	public void testVeryBasicCopyAndPasteAndCutToRemoveCenter() {
		songEditor.setSelectionFrom(150000);
		songEditor.setSelectionTo(300000);
		songEditor.copySelection();
		songEditor.deselectSelection();
		songEditor.setSelectionFrom(200000);
		songEditor.pasteCopiedSelection();	
		songEditor.setSelectionFrom(100000);
		songEditor.setSelectionTo(400000);
		songEditor.cutSelection();
		
		assertTrue(songEditor.getSong().getCuts().size() == 2);
		assertTrue(songEditor.getModifiedSongLength() == songEditor.getOriginalSongLength() - 150000);
		
		assertTrue(songEditor.getSong().getCut(0).getFrom() == 0 && songEditor.getSong().getCut(0).getTo() == 99999);
		assertTrue(songEditor.getSong().getCut(0).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(0).getSegment(0).getFrom() == 0 && songEditor.getSong().getCut(0).getSegment(0).getTo() == 99999);
		
		assertTrue(songEditor.getSong().getCut(1).getFrom() == 100000 && songEditor.getSong().getCut(1).getTo() == songEditor.getOriginalSongLength() - 150000);
		assertTrue(songEditor.getSong().getCut(1).getSegments().size() == 1);
		assertTrue(songEditor.getSong().getCut(1).getSegment(0).getFrom() == 250000 && songEditor.getSong().getCut(1).getSegment(0).getTo() == songEditor.getOriginalSongLength());			
	}		
}
