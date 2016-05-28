package org.jwave.model.player;

import java.util.Optional;

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
     * @param songToMoveIndex
     *          the song to be moved.
     * @param position
     *          the new position index.
     *          
     * @throws IllegalArgumentException
     *          if the new position is out of playlist borders.
     */
    void moveSongToPosition(int songToMoveIndex, int position) throws IllegalArgumentException;
    
    /**
     * Removes songs from the playlist.
     * 
     * @param songToBeRemoved
     *          the songs to be removed.
     */
    void removeFromPlaylist(Song songToBeRemoved);
    
    /**
     * 
     * @return
     *          an {@link}Optional containing the selected song if present.
     */
    Optional<Song> getCurrentSelected();
    
    /**
     * 
     * @param song
     *          the song contained in the playlist.
     * @return
     *          the index of a song in the playlist.
     */
    int indexOf(Song song);
    
    /**
     * 
     * @return
     *          the playlist dimension.
     */
    int getDimension();
    
    /**
     * Selects a song.
     * 
     * @param name
     *          name of the song that has to be selected.
     * @return
     *          the selected song.
     *          
     * @throws IllegalArgumentException
     *          if the song is not present.
     */
    Song selectSong(String name) throws IllegalArgumentException;
    
    /**
     * Selects a song.
     * 
     * @param index
     *          index of the song that has to be selected.
     * @return
     *          the selected song.
     *          
     * @throws IllegalArgumentException
     *          if the song is not present.
     */
    Song selectSong(int index) throws IllegalArgumentException;
    
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
     * Prints the playlist.
     * 
     */
    void printPlaylist();
    
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
