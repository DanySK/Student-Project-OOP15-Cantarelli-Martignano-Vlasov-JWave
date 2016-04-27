package org.jwave.controller.player;

public class AudioSystem {

	private static final AudioSystem SINGLETON = new AudioSystem();
	private DynamicPlayer player = new DynamicPlayerImpl();
	private PlaylistManager playlistManager = new PlaylistManagerImpl();
	
	private AudioSystem() { };
	
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
