package org.jwave.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.jwave.model.player.Song;
import org.jwave.model.playlist.PlayMode;
import org.jwave.model.playlist.Playlist;

import javafx.collections.ObservableList;

public interface PlayerController {
    
    public void attachUI(PlayerUI UI);
    
    public void loadSong(File song) throws IllegalArgumentException, IOException;
    
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

    public void setMode(PlayMode mode);

}
