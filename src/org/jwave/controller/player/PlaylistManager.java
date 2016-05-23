package org.jwave.controller.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.jwave.model.player.PlayMode;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistNavigator;
import org.jwave.model.player.Song;

/**
 * Interface that models the concept of playlist manager.
 * A playlist manager saves and loads playlists from the file system and contains the strategy
 * for navigate them.
 */
public interface PlaylistManager {
    
    /**
     * Loads an audio file and adds it to the default playlist.
     * 
     * @param audioFile
     *          the audioFile to be loaded.
     *           
     * @throws IllegalArgumentException
     *          when trying to open a non audio file.
     */
    void addAudioFile(File audioFile) throws IllegalArgumentException;
    
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
     */
    void deletePlaylist(Playlist playlist);
    
    
    
    /**
     * Resets the playlist manager so the default playlist will be cleaned and it will become 
     * the current playing queue.
     */
    void reset();

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
     */
    void renamePlaylist(Playlist playlist, String newName);
    
    /**
     * Sets the available playlists.
     * 
     * @param playlists
     *          the available playlists.
     */
    void setAvailablePlaylists(Collection<? extends Playlist> playlists);
    
    /**
     * 
     * @return
     *          the index of the song loaded in the connected {@link}DynamicPlayer.
     */
    Optional<Integer> getCurrentLoadedIndex();
    
    /**
     * 
     * @return
     *          the current loaded playlist.
     */
    Playlist getPlayingQueue();
    
    /**
     * 
     * @return
     *          the default playing queue.
     */
    Playlist getDefaultQueue();
    
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
