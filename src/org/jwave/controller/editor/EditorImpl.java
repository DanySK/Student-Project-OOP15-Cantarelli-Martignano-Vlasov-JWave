package org.jwave.controller.editor;

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
}
