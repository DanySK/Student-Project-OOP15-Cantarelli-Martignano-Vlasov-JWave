package org.jwave.controller.player;
/**
 * An AudioSystem is a system that allows the user to manage and reproduce audio files.
 * It contains a DynamicPlayer and a PlaylistManager.
 *
 */
public final class AudioSystem {

	private static final AudioSystem SINGLETON = new AudioSystem();
	private DynamicPlayer player; 
	private PlaylistManager playlistManager; 
	
	private AudioSystem() {
	    this.player = DynamicPlayerImpl.getDynamicPlayer();
	    this.playlistManager = PlaylistManagerImpl.getPlaylistManager();
	    this.getDynamicPlayer().addEObserver(getPlaylistManager());
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
