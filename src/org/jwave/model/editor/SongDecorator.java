package org.jwave.model.editor;

import java.util.UUID;

import org.jwave.model.player.MetaDataRetriever;
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

	public UUID getSongID() {
		return decoratedSong.getSongID();
	}
	
    @Override
    public MetaDataRetriever getMetaData() {
        return decoratedSong.getMetaData();
    }
}
