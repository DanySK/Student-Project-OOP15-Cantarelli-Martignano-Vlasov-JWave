package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jwave.controller.player.FileSystemHandler;

import ddf.minim.AudioSample;
import ddf.minim.Minim;

public class EditorImpl implements Editor {
	private final ArrayList<Cut> editCuts;
	
	private int selectionFrom;
	private int selectionTo;
	
	private int copiedFrom;
	private int copiedTo;
	
	private final static Minim minim = new Minim(FileSystemHandler.getFileSystemHandler());
	AudioSample song;
	private boolean songLoaded = false;
	
	// temporary variables currently needed for debugging
	private int lengthOfSong;
	private int bufferSize = 2048;
	
	public EditorImpl() {
		editCuts = new ArrayList<>();
		this.selectionFrom = -1;
		this.selectionTo = -1;
		this.copiedFrom = -1;
		this.copiedTo = -1;
	}
	
	@Override
	public void loadSongToEdit(String songPath) {
		song = minim.loadSample(songPath, bufferSize);
		
		lengthOfSong = song.length(); // in ms
		
		songLoaded = true;
		
		// default initial cut, entire original song in a single cut
		editCuts.add(new Cut(0, lengthOfSong, new ArrayList<Pair<Integer, Integer>>(Arrays.asList(new Pair<>(new Integer(0), new Integer(lengthOfSong))))));
	
		System.out.println("Song loaded.");
	}

	@Override
	public boolean isSongLoaded() {
		return songLoaded;
	}

	@Override
	public int getSongLength() {
		if (isSongLoaded()) {
			return lengthOfSong;
		} else {
			return -1;
		}
	}

	@Override
	public void setSelectionFrom(int ms) {
		this.selectionFrom = ms;		
	}

	@Override
	public void setSelectionTo(int ms) {
		this.selectionTo = ms;
	}

	@Override
	public int getSelectionFrom() {
		return this.selectionFrom;
	}

	@Override
	public int getSelectionTo() {
		return this.selectionTo;
	}

	@Override
	public void deselectSelection() {
		this.selectionFrom = -1;
		this.selectionTo = -1;
	}

	@Override
	public boolean isCursorSet() {
		return (this.selectionFrom >= 0);
	}

	@Override
	public boolean isSomethingSelected() {
		return (this.selectionFrom >= 0 && this.selectionTo >= 0);
	}

	@Override
	public boolean copySelection() {
		if (isSomethingSelected()) {
			this.copiedFrom = this.selectionFrom;
			this.copiedTo = this.selectionTo;
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getCopiedFrom() {
		return this.copiedFrom;
	}

	@Override
	public int getCopiedTo() {
		return this.copiedTo;
	}

	@Override
	public void resetCopiedSelection() {
		this.copiedFrom = -1;
		this.copiedTo = -1;
	}

	@Override
	public boolean isSomethingCopied() {
		return (this.copiedFrom >= 0 && this.copiedTo >= 0);
	}

	@Override
	public boolean pasteCopiedSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cutSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void exportSong(String exportPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportSongMP3(String sourcePath, String destinationPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printAllCuts() {
		System.out.println("Current selection: from " + getSelectionFrom() + "ms to " + getSelectionTo() + "ms");
		System.out.println("Copied selection: from " + getCopiedFrom() + "ms to " + getCopiedTo() + "ms");
		
		for (int i = 0; i < editCuts.size(); i++) {
			System.out.println("Cut " + i + ": from " + editCuts.get(i).getCutFrom() + "ms to " + editCuts.get(i).getCutTo() + "ms");
			for (int j = 0; j < editCuts.get(i).getSegments().size(); j++) {
				System.out.println("    Segment " + j + ": from " + editCuts.get(i).getSegments().get(j).getX() + "ms to " + editCuts.get(i).getSegments().get(j).getY() + "ms");
			}
		}
	}

	@Override
	public List<Integer> getWaveform(int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}
}
