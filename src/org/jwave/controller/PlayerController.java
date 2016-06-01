package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import org.jwave.model.player.Song;
import org.jwave.model.playlist.PlayMode;
import org.jwave.model.playlist.Playlist;
import org.jwave.view.UI;

import javafx.collections.ObservableList;

/**
 *
 */
public interface PlayerController {

    /**
     * @param UI A user interface to be attached
     */
    public void attachUI(UI UI);

    /**
     * @param song
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void loadSong(File song) throws IllegalArgumentException, IOException;

    /**
     * @param song
     */
    public void selectSong(Song song);

    /**
     * 
     */
    public void play();
    
    /**
     * @return
     */
    public boolean isPlaying();
    
    /**
     * 
     */
    public void pause();

    /**
     * @param percentage
     */
    public void moveToMoment(Double percentage);

    /**
     * @param ms
     */
    public void updatePosition(Integer ms);

    /**
     * @param amount
     */
    public void setVolume(Integer amount);

    /**
     * 
     */
    public void stop();

    /**
     * 
     */
    public void next();

    /**
     * 
     */
    public void previous();
    
    /**
     * @param mode Sets the reproduction order strategy of the player
     */
    public void setMode(PlayMode mode);

    /**
     * @param name name of the new playlist
     */
    public void newPlaylist(String name);

    /**
     * @param song
     * @param playlist
     */
    public void addSongToPlaylist(Song song, Playlist playlist);

    /**
     * @return A collection that wraps the list of playlists and permit the gui to be notified when changes occurs
     */
    public ObservableList<Playlist> getObservablePlaylists();

    /**
     * @param playlist
     * @return  A collection that wraps the list of songs of a playlist and permit the gui to be notified when changes occurs
     */
    public ObservableList<Song> getObservablePlaylistContent(Playlist playlist);

    /**
     * Releases the resources
     */
    public void terminate();

}
