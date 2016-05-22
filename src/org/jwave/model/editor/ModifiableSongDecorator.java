package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jwave.controller.player.FileSystemHandler;
import org.jwave.model.player.Song;

import ddf.minim.AudioSample;
import ddf.minim.Minim;

public class ModifiableSongDecorator extends SongDecorator implements ModifiableSong {
	private final static Minim minim = new Minim(FileSystemHandler.getFileSystemHandler());
	
	private final List<CutImpl> cuts;
	
	private AudioSample songSample;
	
	public ModifiableSongDecorator(Song decoratedSong) {
		super(decoratedSong);
		
		songSample = minim.loadSample(this.getAbsolutePath(), 1024);
		
		this.cuts = new ArrayList<>();
		this.cuts.add(new CutImpl(0, this.songSample.length(), new ArrayList<Pair<Integer, Integer>>(Arrays.asList(new Pair<>(new Integer(0), new Integer(this.songSample.length()))))));
	}

	@Override
	public int getLength() {
		return this.songSample.length();
	}

	@Override
	public int getModifiedLength() {
		return this.cuts.get(this.cuts.size() - 1).getTo();
	}
	
	private CutImpl generateCutFromSelection(int from, int to) {
		int copiedCutLength = to - from;
		ArrayList<Pair<Integer, Integer>> copiedSegments = new ArrayList<>();
		
		int currentCutIndex = -1;
		int currentSegmentIndex = -1;	
		
		// we find the initial cut from which to begin transfering segments
		int i = 0;
		while (i < cuts.size() && currentCutIndex == -1) {
			if (cuts.get(i).getFrom() <= from && cuts.get(i).getTo() >= from) {
				currentCutIndex = i;
			}
			
			i++;
		}
		
		// and the initial segment
		i = 0; // segment index counter
		int j = 0; // millisecond counter
		int currentSegmentLength;
		int initialSegmentOffset = 0;
		int copiedOffset = from - cuts.get(currentCutIndex).getFrom(); // WithRespectToCutFrom
		while (currentSegmentIndex == -1) {
			currentSegmentLength = cuts.get(currentCutIndex).getSegments().get(i).getY() - cuts.get(currentCutIndex).getSegments().get(i).getX();
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
			currentSegmentLength = cuts.get(i).getSegments().get(j).getY() - (cuts.get(i).getSegments().get(j).getX() + initialSegmentOffset);
		
			copiedSegments.add(new Pair<>(new Integer(cuts.get(i).getSegments().get(j).getX() + initialSegmentOffset),
										  new Integer(totalCopied + currentSegmentLength < copiedCutLength ? 
												  		cuts.get(i).getSegments().get(j).getY() :
												  		cuts.get(i).getSegments().get(j).getY() - (totalCopied + currentSegmentLength - copiedCutLength)
												  	  )));
			
			initialSegmentOffset = 0;
			totalCopied += copiedSegments.get(copiedSegments.size() - 1).getY() - copiedSegments.get(copiedSegments.size() - 1).getX();
			
			j++;
			if (j >= cuts.get(i).getSegments().size()) {
				j = 0;
				i++;
			}
		}
		
		return new CutImpl(from + 1, from + copiedCutLength, copiedSegments);
	}	

	@Override
	public void pasteSelectionAt(int from, int to, int at) {
		int cutToDivideIndex = 0;
		CutImpl cutToDivide = null;
		CutImpl cutToInsert = generateCutFromSelection(from, to);
		
		for (int i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= from && cuts.get(i).getTo() >= from) {
				cutToDivideIndex = i;
				cutToDivide = cuts.get(cutToDivideIndex);
			}
		}
		
		int leftHalfLength = from - cutToDivide.getFrom();
		int rightHalfLength = cutToDivide.getLength() - leftHalfLength;
		int halfPoint = cutToDivide.getFrom() + leftHalfLength;
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
		
		CutImpl leftCut = new CutImpl(cutToDivide.getFrom(), halfPoint, leftSegments);
		CutImpl rightCut = new CutImpl(cutToInsert.getTo() + 1, cutToInsert.getTo() + rightHalfLength, rightSegments);			
		
		// shift all cuts after cut that was divided
		cuts.add(new CutImpl(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
		cuts.add(new CutImpl(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
		for (i = cuts.size() - 1; i > cutToDivideIndex + 1; i--) {
			cuts.set(i, cuts.get(i - 2));
			cuts.get(i).setFrom(cuts.get(i).getFrom() + 1);
		}
		
		// finally set all three cuts to the new ones
		cuts.set(cutToDivideIndex, leftCut);
		cuts.set(cutToDivideIndex + 1, cutToInsert);
		cuts.set(cutToDivideIndex + 2, rightCut);
	}
	
	@Override
	public void deleteSelection(int from, int to) {
		int i;
		int selectionLength = to - from;
		
		int firstCutToDivideIndex = 0;
		CutImpl firstCutToDivide = null;
		
		for (i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= from && cuts.get(i).getTo() >= from) {
				firstCutToDivideIndex = i;
				firstCutToDivide = cuts.get(firstCutToDivideIndex);
			}
		}
		
		int newFirstCutLength = from - firstCutToDivide.getFrom();
		ArrayList<Pair<Integer, Integer>> leftSegments = new ArrayList<>();
		
		i = 0;
		int segmentCounter = 0;
		while (segmentCounter + (firstCutToDivide.getSegments().get(i).getY() - firstCutToDivide.getSegments().get(i).getX()) < newFirstCutLength) {
			leftSegments.add(new Pair<>(firstCutToDivide.getSegments().get(i).getX(), firstCutToDivide.getSegments().get(i).getY()));				
			segmentCounter += (firstCutToDivide.getSegments().get(i).getY() - firstCutToDivide.getSegments().get(i).getX());
			i++;
		}
		
		leftSegments.add(new Pair<>(firstCutToDivide.getSegments().get(i).getX(), firstCutToDivide.getSegments().get(i).getX() + (newFirstCutLength - segmentCounter)));			
		
		int secondCutToDivideIndex = 0;
		CutImpl secondCutToDivide = null;
		
		for (i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= to && cuts.get(i).getTo() >= to) {
				secondCutToDivideIndex = i;
				secondCutToDivide = cuts.get(secondCutToDivideIndex);
			}
		}
		
		int newSecondCutLength = to - secondCutToDivide.getFrom(); // length of part being cut away
		ArrayList<Pair<Integer, Integer>> rightSegments = new ArrayList<>();
		
		i = 0;
		segmentCounter = 0;
		
		System.out.println(newSecondCutLength);
		System.out.println(segmentCounter + (secondCutToDivide.getSegments().get(i).getY() - secondCutToDivide.getSegments().get(i).getX()));
		
		while (segmentCounter + (secondCutToDivide.getSegments().get(i).getY() - secondCutToDivide.getSegments().get(i).getX()) < newSecondCutLength) {				
			segmentCounter += (secondCutToDivide.getSegments().get(i).getY() - secondCutToDivide.getSegments().get(i).getX());
			i++;
		}
		
		rightSegments.add(new Pair<>(secondCutToDivide.getSegments().get(i).getX() + (newSecondCutLength - segmentCounter), secondCutToDivide.getSegments().get(i).getY()));
		
		for (i++; i < secondCutToDivide.getSegments().size(); i++) {
			rightSegments.add(new Pair<>(secondCutToDivide.getSegments().get(i).getX(), secondCutToDivide.getSegments().get(i).getY()));
		}
		
		for (i = secondCutToDivideIndex - 1; i > firstCutToDivideIndex; i--) {
			// remove all and any cuts between the two cuts
			cuts.remove(i);
		}
		
		// set new cuts only after building the new ones
		if (firstCutToDivideIndex == secondCutToDivideIndex) {
			cuts.add(new CutImpl(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
		
			// shift actual cuts down to account that single cut will become two cuts
			for (i = cuts.size() - 1; i > firstCutToDivideIndex + 1; i--) {
				cuts.set(i, cuts.get(i - 1));					
			}
		}
			
		int secondCutFrom = firstCutToDivideIndex != secondCutToDivideIndex ? secondCutToDivide.getFrom() - (selectionLength - (to - secondCutToDivide.getFrom())) : firstCutToDivide.getFrom() + newFirstCutLength;
		
		cuts.set(firstCutToDivideIndex + 1, new CutImpl(secondCutFrom + 1, secondCutToDivide.getTo() - selectionLength, rightSegments));
		cuts.set(firstCutToDivideIndex, new CutImpl(firstCutToDivide.getFrom(), firstCutToDivide.getFrom() + newFirstCutLength, leftSegments));
		
		// shift cut from's and to's down to account for the gap
		for (i = firstCutToDivideIndex + 2; i < cuts.size(); i++) {
			cuts.get(i).setFrom(cuts.get(i).getFrom() - selectionLength);
			cuts.get(i).setTo(cuts.get(i).getTo() - selectionLength);
		}		
	}
}
