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
	private final List<Cut> editCuts;
	
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
		if (isSomethingSelected()) {
			int i;
			int selectionLength = getSelectionTo() - getSelectionFrom();
			
			int firstCutToDivideIndex = 0;
			Cut firstCutToDivide = null;
			
			for (i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getCutFrom() <= getSelectionFrom() && editCuts.get(i).getCutTo() >= getSelectionFrom()) {
					firstCutToDivideIndex = i;
					firstCutToDivide = editCuts.get(firstCutToDivideIndex);
				}
			}
			
			int newFirstCutLength = getSelectionFrom() - firstCutToDivide.getCutFrom();
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
			Cut secondCutToDivide = null;
			
			for (i = 0; i < editCuts.size(); i++) {
				if (editCuts.get(i).getCutFrom() <= getSelectionTo() && editCuts.get(i).getCutTo() >= getSelectionTo()) {
					secondCutToDivideIndex = i;
					secondCutToDivide = editCuts.get(secondCutToDivideIndex);
				}
			}
			
			int newSecondCutLength = getSelectionTo() - secondCutToDivide.getCutFrom(); // length of part being cut away
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
				editCuts.add(new Cut(new Integer(0), new Integer(0), new ArrayList<Pair<Integer, Integer>>())); // filler cut, to increase size
			
				// shift actual cuts down to account that single cut will become two cuts
				for (i = editCuts.size() - 1; i > firstCutToDivideIndex + 1; i--) {
					editCuts.set(i, editCuts.get(i - 1));					
				}
			}
				
			int secondCutFrom = firstCutToDivideIndex != secondCutToDivideIndex ? secondCutToDivide.getCutFrom() - (selectionLength - (getSelectionTo() - secondCutToDivide.getCutFrom())) : firstCutToDivide.getCutFrom() + newFirstCutLength;
			
			editCuts.set(firstCutToDivideIndex + 1, new Cut(secondCutFrom, secondCutToDivide.getCutTo() - selectionLength, rightSegments));
			editCuts.set(firstCutToDivideIndex, new Cut(firstCutToDivide.getCutFrom(), firstCutToDivide.getCutFrom() + newFirstCutLength, leftSegments));
			
			// shift cut from's and to's down to account for the gap
			for (i = firstCutToDivideIndex + 2; i < editCuts.size(); i++) {
				editCuts.get(i).setCutFrom(editCuts.get(i).getCutFrom() - selectionLength);
				editCuts.get(i).setCutTo(editCuts.get(i).getCutTo() - selectionLength);
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
		int totalSamples = (((int) (editCuts.get(editCuts.size() - 1).getCutTo() / lengthOfBuffers)) / channels) * length;
		
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
			System.out.println("Cut " + i + ": from " + editCuts.get(i).getCutFrom() + "ms to " + editCuts.get(i).getCutTo() + "ms");
			for (int j = 0; j < editCuts.get(i).getSegments().size(); j++) {
				System.out.println("    Segment " + j + ": from " + editCuts.get(i).getSegments().get(j).getX() + "ms to " + editCuts.get(i).getSegments().get(j).getY() + "ms");
			}
		}
	}

	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public List<Float> getWaveform(int from, int to, int samples, int maxValue) {
		List<Float> waveformValues = new ArrayList<Float>();
		ArrayList<FloatBuffer> buffers;
		float[][] spectra;
		FloatBuffer left;
		FloatBuffer right;
		float lengthOfChunks;
		float runningTotal = 0;
		float maxLoopAverage = 0;
		
		if (songLoaded) {
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
			int loopLength = totalChunks / samples;
			  
			lengthOfChunks = (float) lengthOfSong / (float) totalChunks;
			
			spectra = new float[totalChunks][fftSize / 2];
			
			for (int chunkIdx = 0; chunkIdx < totalChunks; ++chunkIdx) {
				if (chunkIdx % loopLength == 0) { // then we have collected enough song samples, get the average
					float loopAverage = runningTotal / loopLength;
					waveformValues.add(loopAverage);
					
					if (loopAverage > maxLoopAverage) {
						maxLoopAverage = loopAverage; // find the max value for normalization at the end
					}
					
					runningTotal = 0; // and then reset the running total
				}
				
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
				
				float total = 0;			
				for (int i = 0; i < spectra[chunkIdx].length - 1; ++i) {		
					total += spectra[chunkIdx][i];
				}
				
				runningTotal += total;
			}
			
			// perform some normalization on the waveform values
			for (int i = 0; i < waveformValues.size(); i++) {
				waveformValues.set(i, (waveformValues.get(i) / maxLoopAverage) * maxValue);
			}
		}
		
		return waveformValues;
	}
	
	public void printWaveform() {
		if (songLoaded) {
			List<Float> results = getWaveform(0, getSongLength(), 1000, 500);
			
			System.out.println(results.size());
			
			for (int i = 0; i < results.size(); i++) {
				System.out.println(i + ", " + results.get(i));
			}
		}		
	}
}
