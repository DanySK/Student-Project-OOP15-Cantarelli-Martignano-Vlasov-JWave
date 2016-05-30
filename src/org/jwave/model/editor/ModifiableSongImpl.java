package org.jwave.model.editor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.jwave.model.player.Song;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import ddf.minim.javasound.FloatSampleBuffer;

public class ModifiableSongImpl extends SongDecorator implements ModifiableSong {
	private final List<Cut> cuts;
	private final AudioSample songSample;
	
	private final List<Segment> previousCopy;
	
	public ModifiableSongImpl(Song decoratedSong, AudioSample sampleReference) {
		super(decoratedSong);
		
		this.songSample = sampleReference;
		
		if (decoratedSong instanceof ModifiableSong) {
			ModifiableSong modifiableSong = (ModifiableSong) decoratedSong;
			this.cuts = new ArrayList<>(modifiableSong.getCuts());
		} else {
			this.cuts = new ArrayList<>();
			this.cuts.add(new CutImpl(0, this.songSample.length(), new ArrayList<Segment>(Arrays.asList(new SegmentImpl(0, this.songSample.length())))));
		}
		
		previousCopy = new ArrayList<>();
	}
	
	@Override
	public void resetModifications() {
		this.cuts.clear();
		this.cuts.add(new CutImpl(0, this.songSample.length(), new ArrayList<Segment>(Arrays.asList(new SegmentImpl(0, this.songSample.length())))));
	}
	
	@Override
	public void resetPreviousCopy() {
		this.previousCopy.clear();
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
		
		if (previousCopy.size() == 0) {			
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
				totalCopied += copiedSegments.get(copiedSegments.size() - 1).getLength() + 1;
				
				j++;
				if (j >= cuts.get(i).getSegments().size()) {
					j = 0;
					i++;
				}
			}
					
			previousCopy.addAll(copiedSegments);
		}
		
		return new CutImpl(at, at + copiedCutLength, previousCopy);
	}	

	@Override
	public void pasteSelectionAt(int from, int to, int at) {
		int cutToDivideIndex = 0;
		Optional<Cut> cutToDivide = Optional.empty();
		Cut cutToInsert;
		
		/*
		 * First we get the cut that we will be dividing in two parts
		 * in order to make room for the inserted paste in the middle.
		 */
		for (int i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= at && cuts.get(i).getTo() >= at) {
				cutToDivideIndex = i;
				cutToDivide = Optional.of(cuts.get(cutToDivideIndex));
			}
		}
		
		if (cutToDivide.isPresent()) {
			cutToInsert = generateCutFromSelection(from, to, at);
			
			int leftHalfLength = at - cutToDivide.get().getFrom();
			int rightHalfLength = cutToDivide.get().getLength() - leftHalfLength;
			int halfPoint = cutToDivide.get().getFrom() + leftHalfLength;
			ArrayList<Segment> leftSegments = new ArrayList<>();
			ArrayList<Segment> rightSegments = new ArrayList<>();
			
			int i = 0;
			int segmentCounter = 0;
			while (segmentCounter + (cutToDivide.get().getSegment(i).getLength()) < leftHalfLength) {
				leftSegments.add(new SegmentImpl(cutToDivide.get().getSegment(i).getFrom(), cutToDivide.get().getSegment(i).getTo()));
				segmentCounter += (cutToDivide.get().getSegment(i).getTo() - cutToDivide.get().getSegment(i).getFrom());
				i++;
			}
			
			// middle segments
			leftSegments.add(new SegmentImpl(cutToDivide.get().getSegment(i).getFrom(), cutToDivide.get().getSegment(i).getFrom() + (leftHalfLength - segmentCounter) - 1));
			rightSegments.add(new SegmentImpl(cutToDivide.get().getSegment(i).getFrom() + (leftHalfLength - segmentCounter), cutToDivide.get().getSegment(i).getTo()));
			
			for (i++; i < cutToDivide.get().getSegments().size(); i++) {
				rightSegments.add(new SegmentImpl(cutToDivide.get().getSegment(i).getFrom(), cutToDivide.get().getSegment(i).getTo()));
			}
			
			CutImpl leftCut = new CutImpl(cutToDivide.get().getFrom(), halfPoint - 1, leftSegments);
			CutImpl rightCut = new CutImpl(cutToInsert.getTo() + 1, cutToInsert.getTo() + rightHalfLength + 1, rightSegments);			
			
			// shift all cuts after cut that was divided
			cuts.add(new CutImpl(0, 0, new ArrayList<Segment>())); // filler cut, to increase size
			cuts.add(new CutImpl(0, 0, new ArrayList<Segment>())); // filler cut, to increase size
			for (i = cuts.size() - 1; i > cutToDivideIndex + 1; i--) {
				cuts.set(i, cuts.get(i - 2));
				cuts.get(i).setFrom(cuts.get(i).getFrom() + cutToInsert.getLength() + 1);
				cuts.get(i).setTo(cuts.get(i).getTo() + cutToInsert.getLength() + 1);
			}
			
			// finally set all three cuts to the new ones
			cuts.set(cutToDivideIndex, leftCut);
			cuts.set(cutToDivideIndex + 1, cutToInsert);
			cuts.set(cutToDivideIndex + 2, rightCut);
		} else {
			if (at < 0) {
				at = 0;
				
				cutToInsert = generateCutFromSelection(from, to, at);
				
				/* shift all cuts up by one */
				cuts.add(new CutImpl(0, 0, new ArrayList<Segment>())); // filler cut, to increase size
				for (int i = cuts.size() - 1; i > 0; i--) {
					cuts.set(i, cuts.get(i - 1));
					cuts.get(i).setFrom(cuts.get(i).getFrom() + cutToInsert.getLength() + 1);
					cuts.get(i).setTo(cuts.get(i).getTo() + cutToInsert.getLength() + 1);
				}
				
				cuts.set(0, cutToInsert);				
			} else {
				cutToInsert = generateCutFromSelection(from, to, at);
				
				cuts.add(cutToInsert);
			}
		}
	}
	
	@Override
	public void deleteSelection(int from, int to) {
		previousCopy.clear();
		previousCopy.addAll(this.generateCutFromSelection(from, to, 0).getSegments());
		
		int i;
		int selectionLength = to - from + 1; // plus one because algebraic subtraction forgets about one ms index

		int firstCutToDivideIndex = 0;
		Optional<Cut> firstCutToDivide = Optional.empty();
		
		int secondCutToDivideIndex = 0;
		Optional<Cut> secondCutToDivide = Optional.empty();
		
		int segmentCounter;
		int newFirstCutLength = 0;
		int newSecondCutLength = 0;
		int secondCutFrom = 0;
		ArrayList<Segment> leftSegments = new ArrayList<>();
		ArrayList<Segment> rightSegments = new ArrayList<>();
		
		from = from - 1; // the actual "from" ms is getting cut
		to = to + 1; // as is the actual "to" ms		
		
		for (i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= from && cuts.get(i).getTo() >= from) {
				firstCutToDivideIndex = i;
				firstCutToDivide = Optional.of(cuts.get(firstCutToDivideIndex));
			}
		}
		
		if (firstCutToDivide.isPresent()) {
			newFirstCutLength = from - firstCutToDivide.get().getFrom();
			
			i = 0;
			segmentCounter = 0;
			
			while (segmentCounter + (firstCutToDivide.get().getSegment(i).getLength()) < newFirstCutLength) {
				leftSegments.add(new SegmentImpl(firstCutToDivide.get().getSegment(i).getFrom(), firstCutToDivide.get().getSegment(i).getTo()));				
				segmentCounter += (firstCutToDivide.get().getSegment(i).getLength() + 1);
				i++;
			}
			
			leftSegments.add(new SegmentImpl(firstCutToDivide.get().getSegment(i).getFrom(), firstCutToDivide.get().getSegment(i).getFrom() + (newFirstCutLength - segmentCounter)));			
		}
		
		for (i = 0; i < cuts.size(); i++) {
			if (cuts.get(i).getFrom() <= to && cuts.get(i).getTo() >= to) {
				secondCutToDivideIndex = i;
				secondCutToDivide = Optional.of(cuts.get(secondCutToDivideIndex));
			}
		}
		
		if (secondCutToDivide.isPresent()) {
			newSecondCutLength = to - secondCutToDivide.get().getFrom(); // length of part being cut away			
			
			i = 0;
			segmentCounter = 0;
			
			while (segmentCounter + (secondCutToDivide.get().getSegment(i).getLength()) < newSecondCutLength) {				
				segmentCounter += (secondCutToDivide.get().getSegment(i).getLength() + 1);
				i++;
			}
			
			rightSegments.add(new SegmentImpl(secondCutToDivide.get().getSegment(i).getFrom() + (newSecondCutLength - segmentCounter), secondCutToDivide.get().getSegment(i).getTo()));
			
			for (i++; i < secondCutToDivide.get().getSegments().size(); i++) {
				rightSegments.add(new SegmentImpl(secondCutToDivide.get().getSegment(i).getFrom(), secondCutToDivide.get().getSegment(i).getTo()));
			}
			
			// remove all and any cuts between the two cuts
			for (i = secondCutToDivideIndex - 1; i > firstCutToDivideIndex; i--) {
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
				
			// particular logic dependant on whether or not first and second cut to divide are the same or not, and whether or not first cut exists
			if (firstCutToDivide.isPresent()) {
				secondCutFrom = (firstCutToDivideIndex != secondCutToDivideIndex) ? secondCutToDivide.get().getFrom() - ((selectionLength) - (to - secondCutToDivide.get().getFrom())) : firstCutToDivide.get().getFrom() + newFirstCutLength + 1;
			} else {
				secondCutFrom = 0;
			}
		}
		
		if (firstCutToDivide.isPresent()) {
			cuts.set(firstCutToDivideIndex, new CutImpl(firstCutToDivide.get().getFrom(), firstCutToDivide.get().getFrom() + newFirstCutLength, leftSegments));
		} else {
			cuts.remove(firstCutToDivideIndex);
			firstCutToDivideIndex--;
		}
		
		if (secondCutToDivide.isPresent()) {
			cuts.set(firstCutToDivideIndex + 1, new CutImpl(secondCutFrom, secondCutToDivide.get().getTo() - selectionLength, rightSegments));			
		}
		
		// shift cut from's and to's down to account for the gap
		for (i = firstCutToDivideIndex + 2; i < cuts.size(); i++) {
			cuts.get(i).setFrom(cuts.get(i).getFrom() - selectionLength);
			cuts.get(i).setTo(cuts.get(i).getTo() - selectionLength);
		}		
	}
	
	@Override
	public boolean isMaxResolution(int from, int to, int samples) {	
		float[] leftChannel = this.songSample.getChannel(AudioSample.LEFT);
		
		int sampleSize = (int) ((leftChannel.length * (float) ((float) this.getModifiedLength() / (float) this.getLength())) / (float) samples);
		
		if (sampleSize < 1) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	// Example code taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public List<SimpleSampleInfo> getSimpleWaveform(int from, int to, int samples) throws IllegalArgumentException {
		if (this.isMaxResolution(from, to, samples)) {
			List<SimpleSampleInfo> waveformValues = new ArrayList<SimpleSampleInfo>();
				
			float lengthOfChunks;
			
			float[] rightChannel = this.songSample.getChannel(AudioSample.RIGHT);
			float[] leftChannel = this.songSample.getChannel(AudioSample.LEFT);
			
			int sampleSize = 1;
	
			float[] samplesLeft = new float[sampleSize];
			float[] samplesRight = new float[sampleSize];			
	
			int totalChunks = (leftChannel.length / sampleSize) + 1;
			
//			System.out.println(leftChannel.length);
			  
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
	
					waveformValues.add(new SimpleSampleInfoImpl(samplesLeft[0], samplesRight[0]));					
				}
				
				if (j + 1 >= this.cuts.get(i).getSegments().size()) {
					j = 0;
					i++;
				} else {
					j++;
				}
			}
	
			return waveformValues;
		} else {
			throw new IllegalArgumentException();
		}
	}	
	
	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	// Example code taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public List<GroupedSampleInfo> getAggregatedWaveform(int from, int to, int samples) {
		List<GroupedSampleInfo> waveformValues = new ArrayList<GroupedSampleInfo>();
			
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
		
//		System.out.println(leftChannel.length);
		  
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
				float leftHighest = 0;
				float leftLowest = 0;
				float leftQuadraticTotalPositive = 0;
				float leftQuadraticTotalNegative = 0;
				
				for (int k = 0; k < samplesLeft.length; k++) {
					if (samplesLeft[k] > 0) {
						leftQuadraticTotalPositive += Math.pow(samplesLeft[k], 2);
						
						if (samplesLeft[k] > leftHighest) {
							leftHighest = samplesLeft[k];
						}							
					} else if (samplesLeft[k] < 0) {
						leftQuadraticTotalNegative += Math.pow(samplesLeft[k], 2);
						
						if (samplesLeft[k] < leftLowest) {
							leftLowest = samplesLeft[k];
						}
					}
				}				
				
				/*
				 * then we do the right channel
				 */
				float rightHighest = 0;
				float rightLowest = 0;
				float rightQuadraticTotalPositive = 0;
				float rightQuadraticTotalNegative = 0;
				
				for (int k = 0; k < samplesRight.length; k++) {
					if (samplesRight[k] > 0) {
						rightQuadraticTotalPositive += Math.pow(samplesRight[k], 2);
						
						if (samplesRight[k] > rightHighest) {
							rightHighest = samplesRight[k];
						}							
					} else if (samplesRight[k] < 0) {
						rightQuadraticTotalNegative += Math.pow(samplesRight[k], 2);
						
						if (samplesRight[k] < rightLowest) {
							rightLowest = samplesRight[k];
						}
					}
				}
				
				waveformValues.add(new GroupedSampleInfoImpl(leftHighest, leftLowest, (float) Math.sqrt(leftQuadraticTotalPositive * ((float) 1 / (float) samplesLeft.length)), -1 * (float) Math.sqrt(leftQuadraticTotalNegative * ((float) 1 / (float) samplesLeft.length)), rightHighest, rightLowest, (float) Math.sqrt(rightQuadraticTotalPositive * ((float) 1 / (float) samplesRight.length)), -1 * (float) Math.sqrt(rightQuadraticTotalNegative * ((float) 1 / (float) samplesRight.length))));					
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
	public void exportSong(String exportPath) {
		String exportName = exportPath;
		AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;
		AudioFormat format = songSample.getFormat();
		
		ArrayList<FloatBuffer> buffers;
		float[][] spectra;
		int bufferSize = 2048;
		FloatBuffer left;
		FloatBuffer right;		
		
		buffers = new ArrayList<FloatBuffer>(20);
		left = FloatBuffer.allocate(bufferSize * 10);
		if (format.getChannels() == Minim.STEREO) {
		  right = FloatBuffer.allocate(bufferSize * 10);
		} else {
		  right = null;
		}		
		
		float[] rightChannel = songSample.getChannel(AudioSample.RIGHT);
		float[] leftChannel = songSample.getChannel(AudioSample.LEFT);
		
		int fftSize = 1024;
		float[] fftSamplesLeft = new float[fftSize];
		float[] fftSamplesRight = new float[fftSize];
		  
		FFT fft = new FFT(fftSize, songSample.sampleRate());
		  
		int totalChunks = (leftChannel.length / fftSize) + 1;
		
		spectra = new float[totalChunks][fftSize / 2];
		
		for (int chunkIdx = 0; chunkIdx < totalChunks; ++chunkIdx) {
			int chunkStartIndex = chunkIdx * fftSize;
			int chunkSize = Math.min(leftChannel.length - chunkStartIndex, fftSize);
			
			System.arraycopy(leftChannel, chunkStartIndex, fftSamplesLeft, 0, chunkSize);
			System.arraycopy(rightChannel, chunkStartIndex, fftSamplesRight, 0, chunkSize);
			
			if (chunkSize < fftSize) {
				for (int i = chunkSize; i < fftSamplesLeft.length - 1; i++) {
					fftSamplesLeft[i] = (float) 0.0;
				}
				
				for (int i = chunkSize; i < fftSamplesRight.length - 1; i++) {
					fftSamplesRight[i] = (float) 0.0;
				}
			}
			
			fft.forward(fftSamplesLeft);
			fft.forward(fftSamplesRight);
			
			for (int i = 0; i < 512; ++i) {				
				spectra[chunkIdx][i] = fft.getBand(i);
			}
			
			left.put(fftSamplesLeft);
			right.put(fftSamplesRight);
			
			if (!left.hasRemaining()) {
				buffers.add(left);
				buffers.add(right);
				left = FloatBuffer.allocate(left.capacity());
				right = FloatBuffer.allocate(right.capacity());				
			}
		}
		
		float lengthOfBuffers = (float) this.getLength() / (float) buffers.size();
		int channels = format.getChannels();
		int length = left.capacity();
		int totalSamples = (((int) (this.cuts.get(this.cuts.size() - 1).getTo() / lengthOfBuffers)) / channels) * length;
		
		FloatSampleBuffer fsb = new FloatSampleBuffer(channels, totalSamples, format.getSampleRate());
		
		int l = 0;
		for (int i = 0; i < this.cuts.size(); i++) {
			for (int j = 0; j < this.cuts.get(i).getSegments().size(); j++) {
				int startIndex = (int) ((this.cuts.get(i).getSegment(j).getFrom()) / lengthOfBuffers);
				
				if (startIndex % 2 != 0) { // to correctly set as there are two channels
					startIndex++;
				}
				
				for (int k = startIndex; k < (int) ((cuts.get(i).getSegment(j).getTo()) / lengthOfBuffers) - 1; k += 2) {
					int offset = (l / 2) * length;
					FloatBuffer fbL = (FloatBuffer) buffers.get(k);
					FloatBuffer fbR = (FloatBuffer) buffers.get(k + 1);
					fbL.rewind(); // perche' rewind?
					fbL.get(fsb.getChannel(0), offset, length); // come fa la get su fbL ad influenzare fsb?				
					fbR.rewind();
					fbR.get(fsb.getChannel(1), offset, length);
					l += 2;
				}
			}
		}
		
		int sampleFrames = fsb.getByteArrayBufferSize(format) / format.getFrameSize();
		ByteArrayInputStream bais = new ByteArrayInputStream(fsb.convertToByteArray(format));
		AudioInputStream ais = new AudioInputStream(bais, format, sampleFrames);
		
		if (AudioSystem.isFileTypeSupported(type, ais)) {
			File out = new File(exportName);
			
		    try {
		      AudioSystem.write(ais, type, out);
		    } catch (IOException e) {
		    	System.out.println("AudioRecorder.save: Error attempting to save buffer to " + exportName + "\n" + e.getMessage());
		    }
		    
		    if (out.length() == 0) {
		    	System.out.println("AudioRecorder.save: Error attempting to save buffer to " + exportName + ", the output file is empty.");
		    }
		} else {
			System.out.println("AudioRecorder.save: Can't write " + type.toString() + " using format " + format.toString() + ".");
		} 
			  
		System.out.println("Song exported.");
		  
		songSample.close(); 		
	}	
	
	@Override
	public List<Cut> getCuts() {
		return new ArrayList<Cut>(this.cuts);
	}
	
	@Override
	public Cut getCut(int i) {
		return new CutImpl(this.cuts.get(i).getFrom(), this.cuts.get(i).getTo(), this.cuts.get(i).getSegments());
	}
	
	@Override
	public void printAllCuts() {
		for (int i = 0; i < cuts.size(); i++) {
			System.out.println("Cut " + i + ": from " + cuts.get(i).getFrom() + "ms to " + cuts.get(i).getTo() + "ms");
			for (int j = 0; j < cuts.get(i).getSegments().size(); j++) {
				System.out.println("    		Segment " + j + ": from " + cuts.get(i).getSegment(j).getFrom() + "ms to " + cuts.get(i).getSegment(j).getTo() + "ms");
			}
		}
	}
}
