package org.jwave.model.player;

import java.io.File;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface that models the concept of playlist manager. A playlist manager manages playlists and contains the 
 * strategy for navigating them.
 */
public interface PlaylistManager {
    
    /**
     * Loads an audio file and adds it to the default playlist.
     * 
     * @param audioFile
     *          the audioFile to be loaded.
     *          
     * @throws Exception 
     *          if the file doesn't contain WAVE or MP3 data.
     *          
     * @return
     *          the added song.         
     */
    Song addAudioFile(File audioFile) throws IllegalArgumentException;
    
    /**
     * Creates a new playlist and adds it to the collection of available playlists.
     * 
     * @param name
     *          new playlist name.
     * 
     * @return 
     *          the playlist created
     *          
     * @throws IllegalArgumentException
     *          when inserting a name that is already present in the avalable playlists.         
     * 
     */
    Playlist createNewPlaylist(String name) throws IllegalArgumentException;
    
    /**
     * Deletes a playlist.
     * 
     * @param playlistID
     *          the playlist to be deleted.
     *                    
     */
    void deletePlaylist(UUID playlistID);
    
    /**
     * Selects a song from the current playing queue, making it the current selected.
     * 
     * @param songID
     *          the song identifier.
     *          
     * @return
     *  the selected song.
     *  
     *  @throws NoSuchElementException
     *          when the playing queue doesn't contain the songID.               
     */
    Song selectSongFromPlayingQueue(UUID songID) throws NoSuchElementException;
    
    /**
     * 
     * @param index
     *          the song index.
     * @return
     *          the selecteed song
     *          
     * @throws IllegalArgumentException
     *          when passing an out of border index.
     */
    Song selectSongFromPlayingQueueAtIndex(int index) throws IllegalArgumentException;
    
    /**
     * 
     * @return
     *          the next song in the playing queue.
     */
    Optional<Song> next();
    
    /**
     * 
     * @return
     *          the previous song in the playing queue.
     */
    Optional<Song> prev();

    /**
     * Selects a playlist from the collection of available playlists.
     * 
     * @param playlistID
     *          the name of the playlist to be selected.
     * @return
     *          the selected playlist.
     */
    Playlist selectPlaylist(UUID playlistID);
    
    /**
     * Renames the selected playlist.
     * 
     * @param playlist
     *          the playlist to be renamed.
     *
     * @param newName
     *          the new name to be assigned to the playlist.          
     *          
     * @throws IllegalArgumentException
     *          if there is already a playlist with the same name.         
     */
    void renamePlaylist(Playlist playlist, String newName) throws IllegalArgumentException;
    
    /**
     * Resets the playlist manager so the default playlist will be cleaned and it will become 
     * the current playing queue.
     */
    void reset();
    
    /**
     * 
     * @return
     *          the current loaded playlist.
     */
    Playlist getPlayingQueue();
    
    /**
     * 
     * @return
     *          the default playilist.
     */
    Playlist getDefaultPlaylist();
    
    /**
     * 
     * @return
     *          all the available playlists.
     */
    Collection<Playlist> getAvailablePlaylists();
    
    /**
     * @return
     *          the current play mode (default is {@link Playmode.NO_LOOP})
     */
    PlayMode getPlayMode();
    
    /**
     * Sets the available playlists.
     * 
     * @param playlists
     *          the available playlists.
     */
    void setAvailablePlaylists(Collection<? extends Playlist> playlists);
    
    /**
     * 
     * @param newPlayMode
     *          the play mode to be set.
     */
    void setPlayMode(PlayMode newPlayMode);
    
    
    /**
     * Sets the current playing queue.
     * 
     * @param playlist
     *          the new playing queue.
     */
    void setQueue(Playlist playlist);
}
