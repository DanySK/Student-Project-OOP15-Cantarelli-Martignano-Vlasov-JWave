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
     * @param name
     *          name of the playlist.
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
    void savePlaylistToFile(String name, String path) throws FileNotFoundException, IOException;

    /**
     * Loads a playlist from the filesystem.
     * 
     * @param path
     *          the path where the playlist is stored.
     *          
     * @throws IllegalArgumentException
     *          if the file is not recognized as a playlist.
     *          
     * @throws ClassNotFoundException
     *          if the file doesn't contain a playlist.
     * 
     * @throws IOException
     *          if the path is wrong.
     */
    void loadPlaylist(String path) throws IllegalArgumentException, ClassNotFoundException, IOException;

    /**
     * Loads a file from the file system.
     * 
     * @param audioFile
     *          the audioFile to be loaded.
     *          
     * @param enqueue
     *          if true the file will be appended to the playlist, else 
     *          a new playlist will be created and the file will be added to that.
     * 
     * @throws IllegalArgumentException
     *          when trying to open a non audio file.
     */
    void openFile(File audioFile, boolean enqueue) throws IllegalArgumentException;

    /**
     * Open all the audio files contained in a directory.
     * 
     * @param path
     *          the absolute path of the directory.
     *          
     * @param enqueue
     *          if true all the files will be added to the current playlist,
     *          else the will be put in a new playlist.
     */
    void openDir(String path, boolean enqueue);

    /**
     * Reset the playlist manager.
     */
    void reset();

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
     *          the playlistnavigator object.
     */
    PlaylistNavigator getPlaylistNavigator();
}
