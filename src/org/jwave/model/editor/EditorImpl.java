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
	
	private Cut generateCopiedCut() {
		int copiedCutLength = getCopiedTo() - getCopiedFrom();
		ArrayList<Pair<Integer, Integer>> copiedSegments = new ArrayList<>();
		
		int currentCutIndex = -1;
		int currentSegmentIndex = -1;	
		
		// we find the initial cut from which to begin transfering segments
		int i = 0;
		while (i < editCuts.size() && currentCutIndex == -1) {
			if (editCuts.get(i).getCutFrom() <= getCopiedFrom() && editCuts.get(i).getCutTo() >= getCopiedFrom()) {
				currentCutIndex = i;
			}
			
			i++;
		}
		
		// and the initial segment
		i = 0; // segment index counter
		int j = 0; // millisecond counter
		int currentSegmentLength;
		int initialSegmentOffset = 0;
		int copiedOffset = getCopiedFrom() - editCuts.get(currentCutIndex).getCutFrom(); // WithRespectToCutFrom
		while (currentSegmentIndex == -1) {
			currentSegmentLength = editCuts.get(currentCutIndex).getSegments().get(i).getY() - editCuts.get(currentCutIndex).getSegments().get(i).getX();
			if (j <= copiedOffset && (j + currentSegmentLength) >= copiedOffset) {
				currentSegmentIndex = i;
				initialSegmentOffset = copiedOffset - j;
			}
			
			j += currentSegmentLength;
			i++;
		}
		
		// start copying in segments
		int totalCopied = 0; // in ms
		i = currentCutIndex;
		j = currentSegmentIndex;
		while (totalCopied < copiedCutLength) {
			currentSegmentLength = editCuts.get(i).getSegments().get(j).getY() - (editCuts.get(i).getSegments().get(j).getX() + initialSegmentOffset);
		
			copiedSegments.add(new Pair<>(new Integer(editCuts.get(i).getSegments().get(j).getX() + initialSegmentOffset),
										  new Integer(totalCopied + currentSegmentLength < copiedCutLength ? 
												  		editCuts.get(i).getSegments().get(j).getY() :
												  		editCuts.get(i).getSegments().get(j).getY() - (totalCopied + currentSegmentLength - copiedCutLength)
												  	  )));
			
			initialSegmentOffset = 0;
			totalCopied += copiedSegments.get(copiedSegments.size() - 1).getY() - copiedSegments.get(copiedSegments.size() - 1).getX();
			
			j++;
			if (j >= editCuts.get(i).getSegments().size()) {
				j = 0;
				i++;
			}
		}
		
		return new Cut(getSelectionFrom(), getSelectionFrom() + copiedCutLength, copiedSegments);
	}	

	@Override
	public boolean pasteCopiedSelection() {
		if (isCursorSet() && isSomethingCopied()) {
			int cutToDivideIndex = 0;
			Cut cutToDivide = null;
			Cut cutToInsert = generateCopiedCut();
			
			for (int i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getCutFrom() <= getSelectionFrom() && editCuts.get(i).getCutTo() >= getSelectionFrom()) {
					cutToDivideIndex = i;
					cutToDivide = editCuts.get(cutToDivideIndex);
				}
			}
			
			int leftHalfLength = getSelectionFrom() - cutToDivide.getCutFrom();
			int rightHalfLength = cutToDivide.getCutLength() - leftHalfLength;
			int halfPoint = cutToDivide.getCutFrom() + leftHalfLength;
			ArrayList<Pair<Integer, Integer>> leftSegments = new ArrayList<>();
			ArrayList<Pair<Integer, Integer>> rightSegments = new ArrayList<>();
			
			int i = 0;
			int segmentCounter = 0;
			while (segmentCounter + (cutToDivide.getSegments().get(i).getY() - cutToDivide.getSegments().get(i).getX()) < leftHalfLength) {
				leftSegments.add(new Pair<>(cutToDivide.getSegments().get(i).getX(), cutToDivide.getSegments().get(i).getY()));
				segmentCounter += (cutToDivide.getSegments().get(i).getY() - cutToDivide.getSegments().get(i).getX());
				i++;
			}
			
			// middle segments
			leftSegments.add(new Pair<>(cutToDivide.getSegments().get(i).getX(), cutToDivide.getSegments().get(i).getX() + (leftHalfLength - segmentCounter)));
			rightSegments.add(new Pair<>(cutToDivide.getSegments().get(i).getX() + (leftHalfLength - segmentCounter), cutToDivide.getSegments().get(i).getY()));
			
			for (i++; i < cutToDivide.getSegments().size(); i++) {
				rightSegments.add(new Pair<>(cutToDivide.getSegments().get(i).getX(), cutToDivide.getSegments().get(i).getY()));
			}
			
			Cut leftCut = new Cut(cutToDivide.getCutFrom(), halfPoint, leftSegments);
			Cut rightCut = new Cut(cutToInsert.getCutTo(), cutToInsert.getCutTo() + rightHalfLength, rightSegments);			
			
			// shift all cuts after cut that was divided
			editCuts.add(new Cut(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
			editCuts.add(new Cut(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
			for (i = editCuts.size() - 1; i > cutToDivideIndex + 1; i--) {
				editCuts.set(i, editCuts.get(i - 2));
			}
			
			// finally set all three cuts to the new ones
			editCuts.set(cutToDivideIndex, leftCut);
			editCuts.set(cutToDivideIndex + 1, cutToInsert);
			editCuts.set(cutToDivideIndex + 2, rightCut);
			
			return true;
		} else {
			return false;
		}
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
