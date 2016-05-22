package org.jwave.controller.player;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

import org.jwave.model.player.Playlist;

public class Controller {

    private static final String HOME = "user.home";
    private static final String SEPARATOR = "file.separator";
    private static final String SAVE_DIR_NAME = "JWavePlaylists";
    
    private DynamicPlayer player; 
    private PlaylistManager playlistManager; 
    
    public Controller() {
        this.checkDefaultDir();
        this.player = new DynamicPlayerImpl();
        this.playlistManager = new PlaylistManagerImpl();
        this.player.addEObserver(this.playlistManager);
    }
    
    /**
     * Saves a playlist in the file system.
     * 
     * @param playlist
     *          the playlist to be saved.
     *          
     * @param name
     *          the name of the new playlist.         
     *          
     * @param path     
     *          the path where the playlist will be stored.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    void savePlaylistToFile(final Playlist playlist, final String name, final String path) throws FileNotFoundException, IOException {
        try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(new File(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME 
                        + System.getProperty(SEPARATOR) + playlist.getName() + ".jwo"))))) {
            oos.writeObject(playlist);
        }
    }

    /**
     * Refreshes the collection of available playlists.
     */
    //TODO finish implementation
    public void refreshAvailablePlaylists() {   
        final Path defaultDir = Paths.get(this.getDefaultSavePath());
        final Collection<Playlist> out = new HashSet();
    }
    
    /**
     * 
     * @return
     *     the {@link}DynamicPlayer.
     *             
     */
    public DynamicPlayer getDynamicPlayer() {
        return this.player;
    }

    /**
     * 
     * @return
     *             the {@link}PlaylistManager.
     */
    public PlaylistManager getPlaylistManager() {
        return this.playlistManager;
    }
    
    private void createSaveDir() {
        final Path saveDirPath = Paths.get(this.getDefaultSavePath());
        try {
            Files.createDirectory(saveDirPath);
        } catch (IOException e) {
            System.err.println("Cannot create default save directory.");
        }
    }
    
    private void checkDefaultDir() {
        if (!this.isDefaultSaveDirectoryPresent()) {
            this.createSaveDir();
        }
    }
    
    private boolean isDefaultSaveDirectoryPresent() {
        //inspired by Oracle Java tutorials.
        final Path userHomeDir  = Paths.get(System.getProperty(HOME));
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(userHomeDir)) {
            for (Path file : stream) {
               if (Files.isDirectory(file) && file.getFileName().toString().equals(SAVE_DIR_NAME)) {
                   return true;
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private String getDefaultSavePath() {
        return System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME;
    }
}
