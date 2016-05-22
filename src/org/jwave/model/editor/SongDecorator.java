package org.jwave.model.editor;

import org.jwave.model.player.MetaData;
import org.jwave.model.player.Song;

public abstract class SongDecorator implements Song {
	protected Song decoratedSong;
	
	public SongDecorator(Song decoratedSong) {
		this.decoratedSong = decoratedSong;
	}

	public String getName() {
		return decoratedSong.getName();
	}

	public String getAbsolutePath() {
		return decoratedSong.getAbsolutePath();
	}

	public MetaData getMetaData() {
		return decoratedSong.getMetaData();
	}
}
