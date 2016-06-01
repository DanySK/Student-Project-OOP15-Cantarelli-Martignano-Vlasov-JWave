package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.jwave.model.player.Song;
import org.jwave.model.playlist.PlayMode;
import org.jwave.model.playlist.Playlist;
import org.jwave.view.UI;

import javafx.collections.ObservableList;

/**
 * @author KERBEROS
 *
 */
public interface PlayerController {

    /**
     * @param UI
     */
    public void attachUI(UI UI);

    /**
     * @param song
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void loadSong(File song) throws IllegalArgumentException, IOException;

    /**
     * @param path
     */
    public void loadSong(Path path);

    /**
     * @param song
     */
    public void selectSong(Song song);

    /**
     * 
     */
    public void play();
    
    /**
     * 
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
     * @param name
     */
    public void newPlaylist(String name);

    /**
     * @param song
     * @param playlist
     */
    public void addSongToPlaylist(Song song, Playlist playlist);

    /**
     * @return
     */
    public ObservableList<Playlist> getObservablePlaylists();

    /**
     * @param playlist
     * @return
     */
    public ObservableList<Song> getObservablePlaylistContent(Playlist playlist);

    /**
     * 
     */
    public void terminate();

    /**
     * @param mode
     */
    public void setMode(PlayMode mode);

}
