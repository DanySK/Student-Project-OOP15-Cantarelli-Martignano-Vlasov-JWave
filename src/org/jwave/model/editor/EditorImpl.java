package org.jwave.model.editor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.jwave.controller.player.FileSystemHandler;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import ddf.minim.javasound.FloatSampleBuffer;

public class EditorImpl implements Editor {
	private final List<CutImpl> editCuts;
	
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
		editCuts.clear();
		editCuts.add(new CutImpl(0, lengthOfSong, new ArrayList<Pair<Integer, Integer>>(Arrays.asList(new Pair<>(new Integer(0), new Integer(lengthOfSong))))));
	
		System.out.println("Song loaded.");
	}

	@Override
	public boolean isSongLoaded() {
		return songLoaded;
	}

	@Override
	public int getOriginalSongLength() {
		if (isSongLoaded()) {
			return this.lengthOfSong;
		} else {
			return -1;
		}
	}
	
	@Override
	public int getModifiedSongLength() {
		if (isSongLoaded()) {
			return this.editCuts.get(this.editCuts.size() - 1).getTo();
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
	
	private CutImpl generateCopiedCut() {
		int copiedCutLength = getCopiedTo() - getCopiedFrom();
		ArrayList<Pair<Integer, Integer>> copiedSegments = new ArrayList<>();
		
		int currentCutIndex = -1;
		int currentSegmentIndex = -1;	
		
		// we find the initial cut from which to begin transfering segments
		int i = 0;
		while (i < editCuts.size() && currentCutIndex == -1) {
			if (editCuts.get(i).getFrom() <= getCopiedFrom() && editCuts.get(i).getTo() >= getCopiedFrom()) {
				currentCutIndex = i;
			}
			
			i++;
		}
		
		// and the initial segment
		i = 0; // segment index counter
		int j = 0; // millisecond counter
		int currentSegmentLength;
		int initialSegmentOffset = 0;
		int copiedOffset = getCopiedFrom() - editCuts.get(currentCutIndex).getFrom(); // WithRespectToCutFrom
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
		
		return new CutImpl(getSelectionFrom() + 1, getSelectionFrom() + copiedCutLength, copiedSegments);
	}	

	@Override
	public boolean pasteCopiedSelection() {
		if (isCursorSet() && isSomethingCopied()) {
			int cutToDivideIndex = 0;
			CutImpl cutToDivide = null;
			CutImpl cutToInsert = generateCopiedCut();
			
			for (int i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getFrom() <= getSelectionFrom() && editCuts.get(i).getTo() >= getSelectionFrom()) {
					cutToDivideIndex = i;
					cutToDivide = editCuts.get(cutToDivideIndex);
				}
			}
			
			int leftHalfLength = getSelectionFrom() - cutToDivide.getFrom();
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
			editCuts.add(new CutImpl(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
			editCuts.add(new CutImpl(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
			for (i = editCuts.size() - 1; i > cutToDivideIndex + 1; i--) {
				editCuts.set(i, editCuts.get(i - 2));
				editCuts.get(i).setFrom(editCuts.get(i).getFrom() + 1);
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
		if (isSomethingSelected()) {
			int i;
			int selectionLength = getSelectionTo() - getSelectionFrom();
			
			int firstCutToDivideIndex = 0;
			CutImpl firstCutToDivide = null;
			
			for (i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getFrom() <= getSelectionFrom() && editCuts.get(i).getTo() >= getSelectionFrom()) {
					firstCutToDivideIndex = i;
					firstCutToDivide = editCuts.get(firstCutToDivideIndex);
				}
			}
			
			int newFirstCutLength = getSelectionFrom() - firstCutToDivide.getFrom();
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
			
			for (i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getFrom() <= getSelectionTo() && editCuts.get(i).getTo() >= getSelectionTo()) {
					secondCutToDivideIndex = i;
					secondCutToDivide = editCuts.get(secondCutToDivideIndex);
				}
			}
			
			int newSecondCutLength = getSelectionTo() - secondCutToDivide.getFrom(); // length of part being cut away
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
				editCuts.remove(i);
			}
			
			// set new cuts only after building the new ones
			if (firstCutToDivideIndex == secondCutToDivideIndex) {
				editCuts.add(new CutImpl(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
			
				// shift actual cuts down to account that single cut will become two cuts
				for (i = editCuts.size() - 1; i > firstCutToDivideIndex + 1; i--) {
					editCuts.set(i, editCuts.get(i - 1));					
				}
			}
				
			int secondCutFrom = firstCutToDivideIndex != secondCutToDivideIndex ? secondCutToDivide.getFrom() - (selectionLength - (getSelectionTo() - secondCutToDivide.getFrom())) : firstCutToDivide.getFrom() + newFirstCutLength;
			
			editCuts.set(firstCutToDivideIndex + 1, new CutImpl(secondCutFrom + 1, secondCutToDivide.getTo() - selectionLength, rightSegments));
			editCuts.set(firstCutToDivideIndex, new CutImpl(firstCutToDivide.getFrom(), firstCutToDivide.getFrom() + newFirstCutLength, leftSegments));
			
			// shift cut from's and to's down to account for the gap
			for (i = firstCutToDivideIndex + 2; i < editCuts.size(); i++) {
				editCuts.get(i).setFrom(editCuts.get(i).getFrom() - selectionLength);
				editCuts.get(i).setTo(editCuts.get(i).getTo() - selectionLength);
			}
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public void exportSong(String exportPath) {
		ArrayList<FloatBuffer> buffers;
		float[][] spectra;
		FloatBuffer left;
		FloatBuffer right;
		
		String exportName = exportPath;
		AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;
		AudioFormat format = song.getFormat();		
		
		buffers = new ArrayList<FloatBuffer>(20);
		left = FloatBuffer.allocate(bufferSize * 10);
		if (format.getChannels() == Minim.STEREO) {
		  right = FloatBuffer.allocate(bufferSize * 10);
		} else {
		  right = null;
		}		
		
		float[] rightChannel = song.getChannel(AudioSample.RIGHT);
		float[] leftChannel = song.getChannel(AudioSample.LEFT);
		
		int fftSize = 1024;
		float[] fftSamplesLeft = new float[fftSize];
		float[] fftSamplesRight = new float[fftSize];
		  
		FFT fft = new FFT(fftSize, song.sampleRate());
		  
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
		
		float lengthOfBuffers = (float) lengthOfSong / (float) buffers.size();
		int channels = format.getChannels();
		int length = left.capacity();
		int totalSamples = (((int) (editCuts.get(editCuts.size() - 1).getTo() / lengthOfBuffers)) / channels) * length;
		
		FloatSampleBuffer fsb = new FloatSampleBuffer(channels, totalSamples, format.getSampleRate());
		
		int l = 0;
		for (int i = 0; i < editCuts.size(); i++) {
			for (int j = 0; j < editCuts.get(i).getSegments().size(); j++) {
				int startIndex = (int) ((editCuts.get(i).getSegments().get(j).getX()) / lengthOfBuffers);
				
				if (startIndex % 2 != 0) { // to correctly set as there are two channels
					startIndex++;
				}
				
				for (int k = startIndex; k < (int) ((editCuts.get(i).getSegments().get(j).getY()) / lengthOfBuffers) - 1; k += 2) {
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
		  
		song.close();
		songLoaded = false;
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
			System.out.println("Cut " + i + ": from " + editCuts.get(i).getFrom() + "ms to " + editCuts.get(i).getTo() + "ms");
			for (int j = 0; j < editCuts.get(i).getSegments().size(); j++) {
				System.out.println("    Segment " + j + ": from " + editCuts.get(i).getSegments().get(j).getX() + "ms to " + editCuts.get(i).getSegments().get(j).getY() + "ms");
			}
		}
	}

	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	// Example code taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public List<Float> getWaveform(int from, int to, int samples) {
		List<Float> waveformValues = new ArrayList<Float>();
		
		if (songLoaded) {	
			float lengthOfChunks;
			
			float[] rightChannel = song.getChannel(AudioSample.RIGHT);
			float[] leftChannel = song.getChannel(AudioSample.LEFT);
			
			int sampleSize = (int) ((leftChannel.length * (float) ((float) (to - from) / (float) getModifiedSongLength())) / (float) samples);
			if (sampleSize < 1) {
				sampleSize = 1;
			}
			float[] samplesLeft = new float[sampleSize];
			float[] samplesRight = new float[sampleSize];			
			  
			int totalChunks = (leftChannel.length / sampleSize) + 1;
			  
			lengthOfChunks = (float) lengthOfSong / (float) totalChunks;
			
			int startCutIndex = 0;
			int endCutIndex = 0;
			int startSegmentIndex = -1;
			int startSegmentOffset = 0;
			int endSegmentIndex = -1;
			int endSegmentLength = 0;
			int currentOffset; // used to track at which point in cut we are
			
			for (int i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getFrom() <= from && editCuts.get(i).getTo() >= from) {
					startCutIndex = i;
				}
			}
			
			int startCutOffset = from - editCuts.get(startCutIndex).getFrom();
			currentOffset = 0;
			for (int i = 0; startSegmentIndex == -1; i++) {
				if (currentOffset + (editCuts.get(startCutIndex).getSegments().get(i).getY() - editCuts.get(startCutIndex).getSegments().get(i).getX()) > startCutOffset) {
					startSegmentIndex = i;
					startSegmentOffset = startCutOffset - currentOffset;
				}
				
				currentOffset += editCuts.get(startCutIndex).getSegments().get(i).getY() - editCuts.get(startCutIndex).getSegments().get(i).getX();
			}
			
			for (int i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getFrom() <= to && editCuts.get(i).getTo() >= to) {
					endCutIndex = i;
				}
			}
			
			int endCutOffset = to - editCuts.get(endCutIndex).getFrom();
			currentOffset = 0;
			for (int i = 0; endSegmentIndex == -1; i++) {
				if (currentOffset + (editCuts.get(endCutIndex).getSegments().get(i).getY() - editCuts.get(endCutIndex).getSegments().get(i).getX()) > startCutOffset) {
					endSegmentIndex = i;
					endSegmentLength = endCutOffset - currentOffset;
				}
				
				currentOffset += editCuts.get(endCutIndex).getSegments().get(i).getY() - editCuts.get(endCutIndex).getSegments().get(i).getX();
			}
			
			int i = startCutIndex;
			int j = startSegmentIndex;

			while (i < endCutIndex || (i == endCutIndex && j <= endSegmentIndex)) {	
				int segmentFrom = (i == startCutIndex && j == startSegmentIndex) ? startSegmentOffset : this.editCuts.get(i).getSegments().get(j).getX();
				int segmentTo = (i == endCutIndex && j == endSegmentIndex) ? this.editCuts.get(i).getSegments().get(j).getX() + endSegmentLength : this.editCuts.get(i).getSegments().get(j).getY();
				
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
				
				if (j + 1 >= this.editCuts.get(i).getSegments().size()) {
					j = 0;
					i++;
				} else {
					j++;
				}
			}
		}
		
		return waveformValues;
	}
	
	public void printWaveform() {
		if (songLoaded) {
			List<Float> results = getWaveform(0, getOriginalSongLength(), 1000);
			
			System.out.println(results.size());
			
			for (int i = 0; i < results.size(); i++) {
				System.out.println(i + ", " + results.get(i));
			}
		}		
	}
}
