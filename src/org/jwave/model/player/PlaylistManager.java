package org.jwave.model.player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

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
     * @throws IOException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws InvalidDataException 
     * @throws UnsupportedTagException 
     */
    void addAudioFile(File audioFile) throws UnsupportedTagException, InvalidDataException, 
    IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException;
    
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
     * 
     * @return
     *          the next song in the playing queue.
     */
    Song next();
    
    /**
     * 
     * @return
     *          the previous song in the playing queue.
     */
    Song prev();
    
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
