package org.jwave.controller.player;

public final class AudioSystem {

	private static final AudioSystem SINGLETON = new AudioSystem();
	private DynamicPlayer player; 
	private PlaylistManager playlistManager; 
	
	private AudioSystem() {
	    this.player = new DynamicPlayerImpl();
	    this.playlistManager = new PlaylistManagerImpl();
	    this.getDynamicPlayer().addEObserver(this.getPlaylistManager());
	};
	
	//inspired by prof.Viroli slides
	public static synchronized AudioSystem getAudioSystem() {
	    return SINGLETON;
	}
	
	public synchronized DynamicPlayer getDynamicPlayer() {
	    return this.player;
	}
	
	public synchronized PlaylistManager getPlaylistManager() {
	    return this.playlistManager;
	}
}
