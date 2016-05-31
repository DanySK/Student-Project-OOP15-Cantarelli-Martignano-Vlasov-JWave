package org.jwave.model.editor;

import java.util.UUID;

import org.jwave.model.player.MetaDataRetriever;
import org.jwave.model.player.Song;

public abstract class ModifiableSongDecorator implements Song {
	protected final Song decoratedSong;
	
	public ModifiableSongDecorator(final Song decoratedSong) {
		this.decoratedSong = decoratedSong;
	}

	@Override
	final public String getName() {
		return decoratedSong.getName();
	}

	@Override
	final public String getAbsolutePath() {
		return decoratedSong.getAbsolutePath();
	}

	@Override
	final public UUID getSongID() {
		return decoratedSong.getSongID();
	}
	
	@Override
    final public MetaDataRetriever getMetaData() {
        return decoratedSong.getMetaData();
    }
}
