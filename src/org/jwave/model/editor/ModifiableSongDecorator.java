package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jwave.controller.player.FileSystemHandler;
import org.jwave.model.player.Song;

import ddf.minim.AudioSample;
import ddf.minim.Minim;

public class ModifiableSongDecorator extends SongDecorator implements ModifiableSong {
	private final static Minim minim = new Minim(FileSystemHandler.getFileSystemHandler());
	
	private final List<CutImpl> cuts;
	
	private AudioSample songSample;
	
	public ModifiableSongDecorator(Song decoratedSong) {
		super(decoratedSong);
		
		songSample = minim.loadSample(this.getAbsolutePath(), 1024);
		
		this.cuts = new ArrayList<>();
		this.cuts.add(new CutImpl(0, this.songSample.length(), new ArrayList<Pair<Integer, Integer>>(Arrays.asList(new Pair<>(new Integer(0), new Integer(this.songSample.length()))))));
	}

	@Override
	public int getLength() {
		return this.songSample.length();
	}
}
