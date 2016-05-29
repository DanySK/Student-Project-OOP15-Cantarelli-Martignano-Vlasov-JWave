package org.jwave.model.player;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

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
     */
    void addAudioFile(File audioFile);
    
    /**
     * Creates a new playlist and adds it to the collection of available playlists.
     * 
     * @param name
     *          new playlist name.
     * 
     * @return 
     *          the playlist created
     * 
     */
    Playlist createNewPlaylist(String name);
    
    /**
     * Deletes a playlist.
     * 
     * @param playlist
     *          the playlist to be deleted.
     *                    
     */
    void deletePlaylist(Playlist playlist);
    
    /**
     * Selects a song from the current playing queue, making it the current selected.
     * 
     * @param name
     *          the name of the song to be loaded.
     *          
     * @return
     *          the selected song.
     */
    Song selectSongFromPlayingQueue(String name);
    
    /**
     * Selects a song from the current playing queue, making it the current selected.
     * 
     * @param index
     *          the index of the song to be loaded.
     *          
     * @return
     *          the selected song.
     */
    Song selectSongFromPlayingQueue(int index);
    
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
     * @param name
     *          the name of the playlist to be selected.
     * @return
     *          the selected playlist.
     */
    Playlist selectPlaylist(String name);
    
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
