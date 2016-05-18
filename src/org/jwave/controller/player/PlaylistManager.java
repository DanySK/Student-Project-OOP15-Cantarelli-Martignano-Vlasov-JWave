package org.jwave.controller.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public interface PlaylistManager extends EObserver<Optional<PlayMode>, Optional<Song>> {

    /**
     * Saves the current loaded playlist in the file system.
     * 
     * @param playlist
     *          the playlist to be saved.
     *          
     * @param name
     *          the name of the new playlist.         
     *          
     * @param path     
     *          the path where the playlist will be stored.
     *          
     * @throws FileNotFoundException
     *          if the path is not correct.
     *          
     * @throws IOException
     *          if the path is not correct.
     */
    void savePlaylistToFile(Playlist playlist, String name, String path) throws FileNotFoundException, IOException;

//    /**
//     * Loads a playlist from the filesystem and adds it to the collection of available playlists.
//     * 
//     * @param playlist
//     *          the file containing the playlist to be loaded.
//     *          
//     * @throws IllegalArgumentException
//     *          if the file is not recognized as a playlist.
//     *          
//     * @throws ClassNotFoundException
//     *          if the file doesn't contain a playlist.
//     * 
//     * @throws IOException
//     *          if the path is wrong.
//     */
//    void loadPlaylist(File playlist) throws IllegalArgumentException, ClassNotFoundException, IOException;
    
    /**
     * Loads a file from the file system and adds it to the default playlist.
     * 
     * @param audioFile
     *          the audioFile to be loaded.
     *           
     * @throws IllegalArgumentException
     *          when trying to open a non audio file.
     */
    void openAudioFile(File audioFile) throws IllegalArgumentException;

//    /**
//     * Open all the audio files contained in a directory.
//     * 
//     * @param path
//     *          the absolute path of the directory.
//     *          
//     * @param enqueue
//     *          if true all the files will be added to the current playlist,
//     *          else the will be put in a new playlist.
//     */
//    void openDir(String path, boolean enqueue); //maybe it will be useless.
    
    /**
     * Creates a new playlist and saves it in the program default directory.
     * 
     * @param name
     *          new playlist name.
     * 
     * @return 
     *          the playlist created
     * 
     * @throws {@link FileNotFoundException}
     * 
     * @throws IOException 
     *          when trying to save the playlist in the file system.
     * 
     * @throws FileNotFoundException                 
     *          when having problems creating the file.
     *      
     */
    Playlist createNewPlaylist(String name) throws FileNotFoundException, IOException;
    
    /**
     * Deletes a selected playlist.
     * 
     * @param playlist
     *          the playlist to be deleted.
     *          
     * @throws IllegalArgumentException
     *          when s
     */
    void deletePlaylist(Playlist playlist) throws IllegalArgumentException;
    
    /**
     * Refreshes the collection of available playlists.
     */
    void refreshAvailablePlaylists();
    
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
     *          
     * @throws PlaylistNotFoundException
     *          when the collection of available playlists doesn't contain a playlist with
     *          the specified name.
     */
    Playlist selectPlaylist(String name) throws PlaylistNotFoundException;
    
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
     *          when passing a wrong name or a name that is already present in another playlist.
     */
    void renamePlaylist(Playlist playlist, String newName) throws IllegalArgumentException;
    
    /**
     *
     * @return
     *          the current song loaded in the connected {@link}DynamicPlayer.
     */
    Optional<Song> getCurrentLoaded();
    
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
     *          the playlistnavigator object.
     */
    PlaylistNavigator getPlaylistNavigator();
    
    /**
     * Sets the current playing queue.
     * 
     * @param playlist
     *          the new playing queue.
     */
    void setQueue(Playlist playlist);
}
