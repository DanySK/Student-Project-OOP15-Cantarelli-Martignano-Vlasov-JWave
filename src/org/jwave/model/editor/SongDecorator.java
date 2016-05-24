package org.jwave.model.editor;

import org.jwave.model.player.MetaDataV1;
import org.jwave.model.player.MetaDataV2;
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

	public MetaDataV1 getMetaData() {
		return decoratedSong.getMetaData();
	}
	
    @Override
    public MetaDataV2 getMetaDataV2() {
        return decoratedSong.getMetaDataV2();
    }
}
