package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.List;

import org.jwave.model.player.Song;

public class ModifiableSongDecorator extends SongDecorator {
	private final List<CutImpl> cuts;
	
	public ModifiableSongDecorator(Song decoratedSong) {
		super(decoratedSong);
		this.cuts = new ArrayList<>();
	}
}
