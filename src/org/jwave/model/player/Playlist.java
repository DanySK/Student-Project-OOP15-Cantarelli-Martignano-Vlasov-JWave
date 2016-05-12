package org.jwave.model.player;

import java.util.Optional;

import org.jwave.controller.player.EObserver;
import org.jwave.controller.player.ESource;

public interface Playlist extends ESource<Optional<Integer>, Optional<Integer>> {
    
    void addSong(Song newSong);
    
    void moveSongToPosition(int songToMoveIndex, int position) throws IllegalArgumentException;
    
    void removeFromPlaylist(Song... songNames);
    
    Optional<Song> getCurrentSelected();
    
    int indexOf(Song song);
    
    int getDimension();
    
    Song selectSong(String name) throws IllegalArgumentException;
    
    Song selectSong(int index) throws IllegalArgumentException;
    
    boolean isEmpty();
    
    void printPlaylist();
}
