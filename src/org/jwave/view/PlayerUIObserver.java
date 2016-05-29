package org.jwave.view;

import java.io.File;
import java.nio.file.Path;

import org.jwave.model.player.Playlist;
import org.jwave.model.player.Song;

public interface PlayerUIObserver {
    
    public void loadSong(File song);
    
    public void loadSong(Path path);
    
    public void selectSong(Song song);
    
    public void play();
    
    public void stop();
    
    public void next();
    
    public void previous();
    
    public void newPlaylist(String name);
    
    public void addSongToPlaylist(Song song, Playlist playlist);

}
