package org.jwave.model.editor;

import java.util.List;
import java.util.Optional;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.Song;

public abstract class DynamicPlayerDecorator implements DynamicPlayer {
	protected DynamicPlayer decoratedDynamicPlayer;
	
	private ModifiableSong song;
	
	private List<Cut> cuts;
	private Segment currentSegment;
	private int currentSegmentIndex;
	private int currentCutIndex;
	
	public DynamicPlayerDecorator(final DynamicPlayer player) {
		this.decoratedDynamicPlayer = player;
	}
	
	@Override
	public void play() {
		this.decoratedDynamicPlayer.play();
	}

	@Override
	public void pause() {
		this.decoratedDynamicPlayer.pause();
	}

	@Override
	public void stop() {
		this.decoratedDynamicPlayer.stop();
		
		this.cuts = this.song.getCuts();
		this.currentSegmentIndex = 0;
		this.currentCutIndex = 0;
		this.currentSegment = this.cuts.get(this.currentCutIndex).getSegment(this.currentSegmentIndex);		
	}

	@Override
	public void cue(final int millis) {
		this.decoratedDynamicPlayer.cue(millis);
	}

	@Override
	public int getLength() {
		return this.decoratedDynamicPlayer.getLength();
	}
	
	private int getSongPosition() {
		return this.decoratedDynamicPlayer.getPosition();
	}

	@Override
	public int getPosition() {
		int currentPosition = 0;
		Cut currentCut;
		Segment currentSegment;
		
		currentCut = this.cuts.get(this.currentCutIndex);
		
		currentPosition = currentCut.getFrom();
		
		for (int i = 0; i < this.currentSegmentIndex; i++) {
			currentPosition += this.cuts.get(this.currentCutIndex).getSegment(i).getLength();
		}
		
		currentSegment = currentCut.getSegment(this.currentSegmentIndex);
		
		currentPosition += this.getSongPosition() - currentSegment.getFrom();
		
		return currentPosition;
	}

	@Override
	public Optional<Song> getLoaded() {
		return this.decoratedDynamicPlayer.getLoaded();
	}

	@Override
	public boolean isPlaying() {
		if (this.decoratedDynamicPlayer.isPlaying()) {
			if (this.currentCutIndex < this.cuts.size()) {
			    if (this.getSongPosition() >= this.currentSegment.getTo()) {
			    	this.currentSegmentIndex++;

			        if (this.currentSegmentIndex >= this.cuts.get(this.currentCutIndex).getSegments().size()) {
			        	this.currentCutIndex++;
			        	this.currentSegmentIndex = 0;
			        	this.currentSegment = this.cuts.get(this.currentCutIndex).getSegment(this.currentSegmentIndex);
			        }

			        this.cue(currentSegment.getFrom());
			    }
			    
			    return true;
			} else {
				this.stop();
				
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean isPaused() {
		return this.decoratedDynamicPlayer.isPaused();
	}

	@Override
	public boolean hasStarted() {
		return this.decoratedDynamicPlayer.hasStarted();
	}

	@Override
	public void setVolume(final float amount) {
		this.decoratedDynamicPlayer.setVolume(amount);
	}
	
	@Override
	public void setPlayer(final Song song) {
		this.song = (ModifiableSong) song;
		
		this.decoratedDynamicPlayer.setPlayer(this.song);
		
		this.cuts = this.song.getCuts();
		this.currentSegmentIndex = 0;
		this.currentCutIndex = 0;
		this.currentSegment = this.cuts.get(this.currentCutIndex).getSegment(this.currentSegmentIndex);
	}
	
	@Override
	public boolean isEmpty() {
	    return this.decoratedDynamicPlayer.isEmpty();
	}
	
	@Override
	public void resetPlayer() {
		this.decoratedDynamicPlayer.resetPlayer();
	}

	@Override
	public void releasePlayerResources() {
		this.decoratedDynamicPlayer.releasePlayerResources();
	}	
}
