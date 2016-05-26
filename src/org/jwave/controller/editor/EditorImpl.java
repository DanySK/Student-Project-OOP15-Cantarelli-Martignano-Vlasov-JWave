package org.jwave.controller.editor;

import java.util.List;

import org.jwave.model.editor.GroupedSampleInfo;
import org.jwave.model.editor.ModifiableSongImpl;
import org.jwave.model.player.Song;

public class EditorImpl implements Editor {
	private int selectionFrom;
	private int selectionTo;
	private int copiedFrom;
	private int copiedTo;
	
	private ModifiableSongImpl song;	
	
	public EditorImpl() {
		this.selectionFrom = -1;
		this.selectionTo = -1;
		this.copiedFrom = -1;
		this.copiedTo = -1;
		
		this.song = null;
	}
	
	@Override
	public ModifiableSongImpl getSong() {
		return new ModifiableSongImpl(this.song);
	}

	@Override
	public void loadSongToEdit(Song song) {
		this.song = new ModifiableSongImpl(song);
	}

	@Override
	public boolean isSongLoaded() {
		return this.song != null;
	}	
	
	@Override
	public int getOriginalSongLength() throws IllegalStateException {
		if (this.isSongLoaded()) {
			return this.song.getLength();
		} else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public int getModifiedSongLength() throws IllegalStateException {
		if (isSongLoaded()) {
			return this.song.getModifiedLength();
		} else {
			throw new IllegalStateException();
		}
	}		
	
	@Override
	public void setSelectionFrom(int ms) throws IllegalArgumentException {
		if (ms >= 0 && ms <= this.song.getModifiedLength()) {
			this.selectionFrom = ms;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setSelectionTo(int ms) throws IllegalArgumentException {
		if (ms >= 0 && ms <= this.song.getModifiedLength()) {
			this.selectionTo = ms;
		} else {
			throw new IllegalArgumentException();
		}
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
	public void copySelection() throws IllegalStateException {
		if (isSomethingSelected()) {
			this.copiedFrom = this.selectionFrom;
			this.copiedTo = this.selectionTo;
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public int getCopiedFrom() throws IllegalStateException {
		if (this.isSomethingCopied()) {
			return this.copiedFrom;
		} else {
			throw new IllegalStateException();
		}	
	}

	@Override
	public int getCopiedTo() throws IllegalStateException {
		if (this.isSomethingCopied()) {
			return this.copiedTo;
		} else {
			throw new IllegalStateException();
		}
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
	public void pasteCopiedSelection() throws IllegalStateException {
		if (isCursorSet() && isSomethingCopied()) {
			this.song.pasteSelectionAt(getCopiedFrom(), getCopiedTo(), getSelectionFrom());
		} else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public void cutSelection() throws IllegalStateException {
		if (isSomethingSelected()) {
			this.song.deleteSelection(getSelectionFrom(), getSelectionTo());
		} else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	// Code based on example taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	// Example code taken from minim repository (Minim/examples/Analysis/offlineAnalysis/offlineAnalysis.pde)
	public List<GroupedSampleInfo> getWaveform(int from, int to, int samples) {
		return this.song.getAggregatedWaveform(from, to, samples);
	}	
	
	public void printWaveform() {
		if (this.isSongLoaded()) {
			List<GroupedSampleInfo> results = this.song.getAggregatedWaveform(0, (int) (this.song.getModifiedLength() / 1), 1000);
			
			for (int i = 0; i < results.size(); i += 8) {
				System.out.println(i / 8 + ", " + results.get(i) + ", " + results.get(i + 1) + ", " + results.get(i + 2) + ", " + results.get(i + 3));
			}
		}		
	}
	
	@Override
	public void exportSong(String exportPath) {
		this.song.exportSong(exportPath);
	}
	
	@Override
	public void printSongDebug() {
//		System.out.println("Current selection: from " + getSelectionFrom() + "ms to " + getSelectionTo() + "ms");
//		System.out.println("Copied selection: from " + getCopiedFrom() + "ms to " + getCopiedTo() + "ms");
		
		this.song.printAllCuts();
	}	
}
