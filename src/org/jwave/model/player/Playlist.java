package org.jwave.model.player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jwave.model.ESource;

/**
 * A playlist is a collection of {@link}Song.
 *
 */
public interface Playlist extends ESource<Integer> {
    
    /**
     * Add a song to the playlist.
     * 
     * @param newSong
     *          the song to be added.
     */
    void addSong(Song newSong);
    
    /**
     * Moves the selected song in a new position.
     * 
     * @param songToMoveID
     *          the song to be moved.
     * @param position
     *          the new position index.
     *          
     * @throws IllegalArgumentException
     *          if the new position is out of playlist borders.
     */
    void moveSongToPosition(int songToMoveID, int position) throws IllegalArgumentException;
    
    /**
     * Removes songs from the playlist.
     * 
     * @param songID
     *          the songs to be removed.
     *          
     * @throws IllegalArgumentException
     *          when the playlist doesn't contain the id.
     *          
     */
    void removeFromPlaylist(UUID songID) throws IllegalArgumentException;
    
    /**
     * 
     * @param songID
     *          the id of the song contained in the playlist.
     * @return
     *          the index of a song in the playlist.
     *          
     * @throws IllegalArgumentException
     *          when the playlist doesn't contain the song id.         
     */
    int indexOf(UUID songID) throws IllegalArgumentException;
    
    /**
     * 
     * @return
     *          the playlist dimension.
     */
    int getDimension();
    
    /**
     * 
     * 
     * @param songID
     *          the song identifier.
     *          
     * @return
     *          the selected song.
     *          
     *          
     */
    Song getSong(UUID songID) throws IllegalArgumentException;  
    
    /**
     * 
     * @param index
     *          the index of the playlist.
     *          
     * @return
     *          the selected song
     *          
     * @throws IllegalArgumentException
     *          if passing an out of border index.
     */
    Song getSongAtIndex(int index) throws IllegalArgumentException;
    
    /**
     * 
     * @return
     *          all the songs contained in this playlist in a defensive copy.
     */
    List<Song> getPlaylistContent();
    
    /**
     * 
     * @return
     *          wether playlist is empty or not.
     */
    boolean isEmpty();
    
    /**
     * 
     * @return
     *          the playlist name.
     */
    String getName();
    
    /**
     * 
     * @return
     *          the playlist unique identifier.
     */
    UUID getPlaylistID();
    
    /**
     * Clears the playlist.
     */
    void clear();
    
    /**
     * Sets the playlist name.
     * 
     * @param newName
     *          the new playlist name.
     */
    void setName(final String newName);
}
