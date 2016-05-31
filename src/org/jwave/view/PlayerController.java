package org.jwave.view;

import java.io.File;
import java.nio.file.Path;

import org.jwave.model.player.Playlist;
import org.jwave.model.player.Song;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

public interface PlayerController {
    
    public void loadSong(File song) throws Exception;
    
    public void loadSong(Path path);
    
    public void selectSong(Song song);
    
    public void play();
    
    public void moveToMoment(Double percentage);
    
    public void updatePosition(Integer ms);
    
    public void setVolume(Integer amount);
    
    public void stop();
    
    public void next();
    
    public void previous();
    
    public void newPlaylist(String name);
    
    public void addSongToPlaylist(Song song, Playlist playlist);
    
    public ObservableList<Playlist> getObservablePlaylists();
    
    public ObservableList<Song> getObservablePlaylistContent(Playlist playlist);
    
    public void terminate();

}