package org.jwave.model.player;

import java.util.Optional;

public interface Playlist {
    
    void addSong(Song newSong);
    
    void moveSongToPosition(int songToMoveIndex, int position) throws IllegalArgumentException;
    
    void removeFromPlaylist(Song... songNames);
    
    Optional<Song> getCurrentSelected();
    
    Song selectSong(String name) throws IllegalArgumentException;
    
    Song selectSong(int index) throws IllegalArgumentException;
    
    void printPlaylist();
}
