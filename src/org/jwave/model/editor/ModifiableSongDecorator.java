package org.jwave.model.editor;

import java.util.UUID;

import org.jwave.model.player.MetaDataManager;
import org.jwave.model.player.Song;

public abstract class ModifiableSongDecorator implements Song {
	protected Song decoratedSong;
	
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
    final public MetaDataManager getMetaData() {
        return decoratedSong.getMetaData();
    }
}
