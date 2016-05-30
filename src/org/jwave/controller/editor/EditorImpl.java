package org.jwave.controller.editor;

import java.util.List;

import org.jwave.controller.player.FileSystemHandler;
import org.jwave.model.editor.GroupedSampleInfo;
import org.jwave.model.editor.ModifiableSongImpl;
import org.jwave.model.editor.SimpleSampleInfo;
import org.jwave.model.player.Song;

import ddf.minim.AudioSample;
import ddf.minim.Minim;

public class EditorImpl implements Editor {
	private final static Minim minim = new Minim(FileSystemHandler.getFileSystemHandler());
	
	private AudioSample songSample;
	
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
	public ModifiableSongImpl getSong() throws IllegalStateException {
		if (this.isSongLoaded()) {
			return new ModifiableSongImpl(this.song, this.songSample);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void loadSongToEdit(final Song song) {
		this.songSample = minim.loadSample(song.getAbsolutePath(), 2048);
		this.song = new ModifiableSongImpl(song, this.songSample);		
	}
	
	@Override
	public void resetSong() throws IllegalStateException {
		if (this.isSongLoaded()) {
			this.song.resetModifications();
		} else {
			throw new IllegalStateException();
		}
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
	public void setSelectionFrom(final int ms) throws IllegalArgumentException {
		if (ms >= -1 && ms <= this.song.getModifiedLength() + 1) {
			if (this.getSelectionTo() > -1 && ms > this.getSelectionTo()) {
				this.selectionFrom = this.getSelectionTo();
				this.selectionTo = ms;
			} else {
				this.selectionFrom = ms;
			}			
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setSelectionTo(final int ms) throws IllegalArgumentException {
		if (ms >= -1 && ms <= this.song.getModifiedLength() + 1) {
			if (this.getSelectionFrom() > -1 && ms < this.getSelectionFrom()) {
				this.selectionTo = this.getSelectionFrom();
				this.selectionFrom = ms;
			} else {
				this.selectionTo = ms;
			}			
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
		return (this.selectionFrom >= -1);
	}

	@Override
	public boolean isSomethingSelected() {
		return (this.selectionFrom >= -1 && this.selectionTo >= -1);
	}

	@Override
	public void copySelection() throws IllegalStateException {
		if (isSomethingSelected()) {
			this.song.resetPreviousCopy();
			this.copiedFrom = (this.selectionFrom < 0 ? 0 : this.selectionFrom);
			this.copiedTo = (this.selectionTo > this.getModifiedSongLength() ? this.getModifiedSongLength() : this.selectionTo);			
		} else {
			throw new IllegalStateException();
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
			this.copySelection();
			this.song.deleteSelection(getSelectionFrom(), getSelectionTo());
		} else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public boolean isMaxResolution(final int from, final int to, final int samples) {
		return this.song.isMaxResolution(from, to, samples);
	}
	
	@Override
	public List<SimpleSampleInfo> getSimpleWaveform(final int from, final int to, final int samples) {
		return this.song.getSimpleWaveform(from, to, samples);
	}
	
	@Override
	public List<GroupedSampleInfo> getAggregatedWaveform(final int from, final int to, final int samples) {
		return this.song.getAggregatedWaveform(from, to, samples);
	}	
	
	public void printWaveform() {
		if (this.isSongLoaded()) {
			List<GroupedSampleInfo> results = this.song.getAggregatedWaveform(0, (int) (this.song.getModifiedLength() / 1), 1000);
			
			for (int i = 0; i < results.size(); i++) {
				System.out.println(i + ", " + results.get(i).getLeftChannelMax() + ", " + results.get(i).getLeftChannelMin() + ", " + results.get(i).getRightChannelMax() + ", " + results.get(i).getRightChannelMin());
			}
		}		
	}
	
	@Override
	public void exportSong(final String exportPath) {
		this.song.exportSong(exportPath);
	}
	
	@Override
	public void printSongDebug() {
		System.out.println("Current selection: from " + getSelectionFrom() + "ms to " + getSelectionTo() + "ms");
		System.out.println("Copied selection: from " + getCopiedFrom() + "ms to " + getCopiedTo() + "ms");
		
		this.song.printAllCuts();
	}	
}
