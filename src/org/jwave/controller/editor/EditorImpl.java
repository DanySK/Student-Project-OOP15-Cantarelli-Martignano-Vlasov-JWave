package org.jwave.controller.editor;

import java.util.List;

import org.jwave.model.editor.ModifiableSongDecorator;
import org.jwave.model.player.Song;

public class EditorImpl implements Editor {
	private int selectionFrom;
	private int selectionTo;
	private int copiedFrom;
	private int copiedTo;
	
	private ModifiableSongDecorator song;	
	
	public EditorImpl() {
		this.selectionFrom = -1;
		this.selectionTo = -1;
		this.copiedFrom = -1;
		this.copiedTo = -1;
		
		this.song = null;
	}

	@Override
	public void loadSongToEdit(Song song) {
		this.song = new ModifiableSongDecorator(song);		
	}

	@Override
	public boolean isSongLoaded() {
		return this.song != null;
	}	
	
	@Override
	public int getOriginalSongLength() {
		if (this.isSongLoaded()) {
			return this.song.getLength();
		} else {
			return -1;
		}
	}
	
	@Override
	public int getModifiedSongLength() {
		if (isSongLoaded()) {
			return this.song.getModifiedLength();
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
		if (isCursorSet() && isSomethingCopied()) {
			this.song.pasteSelectionAt(getCopiedFrom(), getCopiedTo(), getSelectionFrom());
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean cutSelection() {
		if (isSomethingSelected()) {
			this.song.deleteSelection(getSelectionFrom(), getSelectionTo());
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	// Example code taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public List<Float> getWaveform(int from, int to, int samples) {
		return this.song.getWaveform(from, to, samples);
	}	
	
	public void printWaveform() {
		System.out.println("asdasdas");
		
		if (this.isSongLoaded()) {
			System.out.println("asdasdas");
			
			List<Float> results = this.song.getWaveform(0, (int) (this.song.getModifiedLength() / 1), 1000);
			
			System.out.println(results.size());
			
			for (int i = 0; i < results.size(); i += 8) {
				System.out.println(i / 8 + ", " + results.get(i) + ", " + results.get(i + 1) + ", " + results.get(i + 2) + ", " + results.get(i + 3));
			}
		}		
	}
	
	@Override
	public void printSongDebug() {
		System.out.println("Current selection: from " + getSelectionFrom() + "ms to " + getSelectionTo() + "ms");
		System.out.println("Copied selection: from " + getCopiedFrom() + "ms to " + getCopiedTo() + "ms");
		
		this.song.printAllCuts();
	}	
}
