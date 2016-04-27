package org.jwave.model.player;

public interface Playlist {
    
    void addSong(Song newSong);
    
    void moveSongToPosition(int position) throws IllegalArgumentException;
    
    void removeFromPlaylist(Song... songNames);
    
    Song getCurrentSelected();
    
    Song getNext();
    
    Song getPrev();
    
    void printPlaylist();
}
