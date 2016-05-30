package org.jwave.model.editor;

import java.util.UUID;

import org.jwave.model.player.MetaDataRetriever;
import org.jwave.model.player.Song;

public abstract class ModifiableSongDecorator implements Song {
	protected Song decoratedSong;
	
	public ModifiableSongDecorator(Song decoratedSong) {
		this.decoratedSong = decoratedSong;
	}

	@Override
	public String getName() {
		return decoratedSong.getName();
	}

	@Override
	public String getAbsolutePath() {
		return decoratedSong.getAbsolutePath();
	}

	@Override
	public UUID getSongID() {
		return decoratedSong.getSongID();
	}
	
	@Override
    public MetaDataRetriever getMetaData() {
        return decoratedSong.getMetaData();
    }
}
