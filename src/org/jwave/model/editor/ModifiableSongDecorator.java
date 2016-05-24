package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jwave.controller.player.FileSystemHandler;
import org.jwave.model.player.MetaDataV2;
import org.jwave.model.player.Song;

import ddf.minim.AudioSample;
import ddf.minim.Minim;

public class ModifiableSongDecorator extends SongDecorator implements ModifiableSong {
	private final static Minim minim = new Minim(FileSystemHandler.getFileSystemHandler());
	
	private final List<CutImpl> cuts;
	
	private AudioSample songSample;
	
	public ModifiableSongDecorator(Song decoratedSong) {
		super(decoratedSong);
		
		this.songSample = minim.loadSample(this.getAbsolutePath(), 2048);
		
		this.cuts = new ArrayList<>();
		this.cuts.add(new CutImpl(0, this.songSample.length(), new ArrayList<Segment>(Arrays.asList(new SegmentImpl(0, this.songSample.length())))));
	}

	@Override
	public int getLength() {
		return this.songSample.length();
	}

	@Override
	public int getModifiedLength() {
		return this.cuts.get(this.cuts.size() - 1).getTo();
	}
	
	private CutImpl generateCutFromSelection(int from, int to, int at) {
		int copiedCutLength = to - from;
		ArrayList<Segment> copiedSegments = new ArrayList<>();
		
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
			currentSegmentLength = cuts.get(currentCutIndex).getSegment(i).getLength();
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
			currentSegmentLength = cuts.get(i).getSegment(j).getTo() - (cuts.get(i).getSegment(j).getFrom() + initialSegmentOffset);
		
			copiedSegments.add(new SegmentImpl(cuts.get(i).getSegment(j).getFrom() + initialSegmentOffset,
										  totalCopied + currentSegmentLength < copiedCutLength ? 
												  		cuts.get(i).getSegment(j).getTo() :
												  		cuts.get(i).getSegment(j).getTo() - (totalCopied + currentSegmentLength - copiedCutLength)
												  	  ));
			
			initialSegmentOffset = 0;
			totalCopied += copiedSegments.get(copiedSegments.size() - 1).getTo() - copiedSegments.get(copiedSegments.size() - 1).getFrom();
			
			j++;
			if (j >= cuts.get(i).getSegments().size()) {
				j = 0;
				i++;
			}
		}
		
		return new CutImpl(at + 1, at + copiedCutLength, copiedSegments);
	}	

	@Override
	public void pasteSelectionAt(int from, int to, int at) {
		int cutToDivideIndex = 0;
		CutImpl cutToDivide = null;
		CutImpl cutToInsert = generateCutFromSelection(from, to, at);
		
		for (int i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= at && cuts.get(i).getTo() >= at) {
				cutToDivideIndex = i;
				cutToDivide = cuts.get(cutToDivideIndex);
			}
		}
		
		int leftHalfLength = at - cutToDivide.getFrom();
		int rightHalfLength = cutToDivide.getLength() - leftHalfLength;
		int halfPoint = cutToDivide.getFrom() + leftHalfLength;
		ArrayList<Segment> leftSegments = new ArrayList<>();
		ArrayList<Segment> rightSegments = new ArrayList<>();
		
		System.out.println(leftHalfLength);
		System.out.println(rightHalfLength);
		
		int i = 0;
		int segmentCounter = 0;
		while (segmentCounter + (cutToDivide.getSegment(i).getTo() - cutToDivide.getSegment(i).getFrom()) < leftHalfLength) {
			leftSegments.add(new SegmentImpl(cutToDivide.getSegment(i).getFrom(), cutToDivide.getSegment(i).getTo()));
			segmentCounter += (cutToDivide.getSegment(i).getTo() - cutToDivide.getSegment(i).getFrom());
			i++;
		}
		
		// middle segments
		leftSegments.add(new SegmentImpl(cutToDivide.getSegment(i).getFrom(), cutToDivide.getSegment(i).getFrom() + (leftHalfLength - segmentCounter)));
		rightSegments.add(new SegmentImpl(cutToDivide.getSegment(i).getFrom() + (leftHalfLength - segmentCounter), cutToDivide.getSegment(i).getTo()));
		
		for (i++; i < cutToDivide.getSegments().size(); i++) {
			rightSegments.add(new SegmentImpl(cutToDivide.getSegment(i).getFrom(), cutToDivide.getSegment(i).getTo()));
		}
		
		CutImpl leftCut = new CutImpl(cutToDivide.getFrom(), halfPoint, leftSegments);
		CutImpl rightCut = new CutImpl(cutToInsert.getTo() + 1, cutToInsert.getTo() + rightHalfLength, rightSegments);			
		
		// shift all cuts after cut that was divided
		cuts.add(new CutImpl(0, 0, new ArrayList<Segment>())); // filler cut, to increase size
		cuts.add(new CutImpl(0, 0, new ArrayList<Segment>())); // filler cut, to increase size
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
		ArrayList<Segment> leftSegments = new ArrayList<>();
		
		i = 0;
		int segmentCounter = 0;
		while (segmentCounter + (firstCutToDivide.getSegment(i).getLength()) < newFirstCutLength) {
			leftSegments.add(new SegmentImpl(firstCutToDivide.getSegment(i).getFrom(), firstCutToDivide.getSegment(i).getTo()));				
			segmentCounter += (firstCutToDivide.getSegment(i).getLength());
			i++;
		}
		
		leftSegments.add(new SegmentImpl(firstCutToDivide.getSegment(i).getFrom(), firstCutToDivide.getSegment(i).getFrom() + (newFirstCutLength - segmentCounter)));			
		
		int secondCutToDivideIndex = 0;
		CutImpl secondCutToDivide = null;
		
		for (i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= to && cuts.get(i).getTo() >= to) {
				secondCutToDivideIndex = i;
				secondCutToDivide = cuts.get(secondCutToDivideIndex);
			}
		}
		
		int newSecondCutLength = to - secondCutToDivide.getFrom(); // length of part being cut away
		ArrayList<Segment> rightSegments = new ArrayList<>();
		
		i = 0;
		segmentCounter = 0;
		
		while (segmentCounter + (secondCutToDivide.getSegment(i).getLength()) < newSecondCutLength) {				
			segmentCounter += (secondCutToDivide.getSegment(i).getLength());
			i++;
		}
		
		rightSegments.add(new SegmentImpl(secondCutToDivide.getSegment(i).getFrom() + (newSecondCutLength - segmentCounter), secondCutToDivide.getSegment(i).getTo()));
		
		for (i++; i < secondCutToDivide.getSegments().size(); i++) {
			rightSegments.add(new SegmentImpl(secondCutToDivide.getSegment(i).getFrom(), secondCutToDivide.getSegment(i).getTo()));
		}
		
		for (i = secondCutToDivideIndex - 1; i > firstCutToDivideIndex; i--) {
			// remove all and any cuts between the two cuts
			cuts.remove(i);
		}
		
		// set new cuts only after building the new ones
		if (firstCutToDivideIndex == secondCutToDivideIndex) {
			cuts.add(new CutImpl(0, 0, new ArrayList<Segment>())); // filler cut, to increase size
		
			// shift actual cuts down to account that single cut will become two cuts
			for (i = cuts.size() - 1; i > firstCutToDivideIndex + 1; i--) {
				cuts.set(i, cuts.get(i - 1));					
			}
		}
			
		// particular logic dependant on whether or not first and second cut to divide are the same or not
		int secondCutFrom = firstCutToDivideIndex != secondCutToDivideIndex ? secondCutToDivide.getFrom() - (selectionLength - (to - secondCutToDivide.getFrom())) : firstCutToDivide.getFrom() + newFirstCutLength;
		
		cuts.set(firstCutToDivideIndex + 1, new CutImpl(secondCutFrom + 1, secondCutToDivide.getTo() - selectionLength, rightSegments));
		cuts.set(firstCutToDivideIndex, new CutImpl(firstCutToDivide.getFrom(), firstCutToDivide.getFrom() + newFirstCutLength, leftSegments));
		
		// shift cut from's and to's down to account for the gap
		for (i = firstCutToDivideIndex + 2; i < cuts.size(); i++) {
			cuts.get(i).setFrom(cuts.get(i).getFrom() - selectionLength);
			cuts.get(i).setTo(cuts.get(i).getTo() - selectionLength);
		}		
	}
	
	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	// Example code taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public List<Float> getWaveform(int from, int to, int samples) {
		List<Float> waveformValues = new ArrayList<Float>();
			
		float lengthOfChunks;
		
		float[] rightChannel = this.songSample.getChannel(AudioSample.RIGHT);
		float[] leftChannel = this.songSample.getChannel(AudioSample.LEFT);
		
		int sampleSize = (int) ((leftChannel.length * (float) ((float) this.getModifiedLength() / (float) this.getLength())) / (float) samples);
		if (sampleSize < 1) {
			sampleSize = 1;
		}
		float[] samplesLeft = new float[sampleSize];
		float[] samplesRight = new float[sampleSize];			

		int totalChunks = (leftChannel.length / sampleSize) + 1;
		
		System.out.println(leftChannel.length);
		  
		lengthOfChunks = (float) this.getLength() / (float) totalChunks;
		
		int startCutIndex = 0;
		int endCutIndex = 0;
		int startSegmentIndex = -1;
		int startSegmentOffset = 0;
		int endSegmentIndex = -1;
		int endSegmentLength = 0;
		int currentOffset; // used to track at which point in cut we are
		
		for (int i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= from && cuts.get(i).getTo() >= from) {
				startCutIndex = i;
			}
		}
		
		int startCutOffset = from - cuts.get(startCutIndex).getFrom();
		currentOffset = 0;
		for (int i = 0; startSegmentIndex == -1; i++) {
			if (currentOffset + (cuts.get(startCutIndex).getSegment(i).getLength()) > startCutOffset) {
				startSegmentIndex = i;
				startSegmentOffset = startCutOffset - currentOffset;
			}
			
			currentOffset += cuts.get(startCutIndex).getSegment(i).getLength();
		}
		
		for (int i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= to && cuts.get(i).getTo() >= to) {
				endCutIndex = i;
			}
		}
		
		int endCutOffset = to - cuts.get(endCutIndex).getFrom();
		currentOffset = 0;
		for (int i = 0; endSegmentIndex == -1; i++) {
			if (currentOffset + (cuts.get(endCutIndex).getSegment(i).getLength()) > startCutOffset) {
				endSegmentIndex = i;
				endSegmentLength = endCutOffset - currentOffset;
			}
			
			currentOffset += cuts.get(endCutIndex).getSegment(i).getLength();
		}
		
		int i = startCutIndex;
		int j = startSegmentIndex;

		while (i < endCutIndex || (i == endCutIndex && j <= endSegmentIndex)) {	
			int segmentFrom = (i == startCutIndex && j == startSegmentIndex) ? startSegmentOffset : this.cuts.get(i).getSegment(j).getFrom();
			int segmentTo = (i == endCutIndex && j == endSegmentIndex) ? this.cuts.get(i).getSegment(j).getFrom() + endSegmentLength : this.cuts.get(i).getSegment(j).getTo();
			
			for (int chunkIdx = (int) Math.floor(segmentFrom / lengthOfChunks); chunkIdx < (int) Math.floor(segmentTo / lengthOfChunks); ++chunkIdx) {
				int chunkStartIndex = chunkIdx * sampleSize;
				int chunkSize = Math.min(leftChannel.length - chunkStartIndex, sampleSize);
				
				System.arraycopy(leftChannel, chunkStartIndex, samplesLeft, 0, chunkSize);
				System.arraycopy(rightChannel, chunkStartIndex, samplesRight, 0, chunkSize);
				
				if (chunkSize < sampleSize) {
					for (int k = chunkSize; k < samplesLeft.length - 1; k++) {
						samplesLeft[k] = (float) 0.0;
					}
					
					for (int k = chunkSize; k < samplesRight.length - 1; k++) {
						samplesRight[k] = (float) 0.0;
					}
				}

				/*
				 * first we do the left channel
				 */
				float highest = 0;
				float lowest = 0;
				float quadraticTotalPositive = 0;
				float quadraticTotalNegative = 0;
				
				for (int k = 0; k < samplesLeft.length; k++) {
					if (samplesLeft[k] > 0) {
						quadraticTotalPositive += Math.pow(samplesLeft[k], 2);
						
						if (samplesLeft[k] > highest) {
							highest = samplesLeft[k];
						}							
					} else if (samplesLeft[k] < 0) {
						quadraticTotalNegative += Math.pow(samplesLeft[k], 2);
						
						if (samplesLeft[k] < lowest) {
							lowest = samplesLeft[k];
						}
					}
				}
				
				waveformValues.add(highest);
				waveformValues.add(lowest);
				waveformValues.add((float) Math.sqrt(quadraticTotalPositive * ((float) 1 / (float) samplesLeft.length)));
				waveformValues.add(-1 * (float) Math.sqrt(quadraticTotalNegative * ((float) 1 / (float) samplesLeft.length)));
				
				/*
				 * then we do the right channel
				 */
				highest = 0;
				lowest = 0;
				quadraticTotalPositive = 0;
				quadraticTotalNegative = 0;
				
				for (int k = 0; k < samplesRight.length; k++) {
					if (samplesRight[k] > 0) {
						quadraticTotalPositive += Math.pow(samplesRight[k], 2);
						
						if (samplesRight[k] > highest) {
							highest = samplesRight[k];
						}							
					} else if (samplesRight[k] < 0) {
						quadraticTotalNegative += Math.pow(samplesRight[k], 2);
						
						if (samplesRight[k] < lowest) {
							lowest = samplesRight[k];
						}
					}
				}
				
				waveformValues.add(highest);
				waveformValues.add(lowest);
				waveformValues.add((float) Math.sqrt(quadraticTotalPositive * ((float) 1 / (float) samplesRight.length)));
				waveformValues.add(-1 * (float) Math.sqrt(quadraticTotalNegative * ((float) 1 / (float) samplesRight.length)));					
			}
			
			if (j + 1 >= this.cuts.get(i).getSegments().size()) {
				j = 0;
				i++;
			} else {
				j++;
			}
		}

		return waveformValues;
	}	
	
	@Override
	public void printAllCuts() {
		for (int i = 0; i < cuts.size(); i++) {
			System.out.println("Cut " + i + ": from " + cuts.get(i).getFrom() + "ms to " + cuts.get(i).getTo() + "ms");
			for (int j = 0; j < cuts.get(i).getSegments().size(); j++) {
				System.out.println("    Segment " + j + ": from " + cuts.get(i).getSegment(j).getFrom() + "ms to " + cuts.get(i).getSegment(j).getTo() + "ms");
			}
		}
	}
}
