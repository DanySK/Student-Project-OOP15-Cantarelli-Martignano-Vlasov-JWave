package org.jwave.controller.editor;

import java.util.ArrayList;

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
}
