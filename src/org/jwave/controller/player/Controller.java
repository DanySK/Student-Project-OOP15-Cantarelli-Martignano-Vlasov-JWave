package org.jwave.controller.player;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.PlaylistManagerImpl;

/**
 * Defines the controller methods.
 *
 */
public class Controller {

    /**
     * Default directory for saving playlists.
     */
    public static final String SAVE_DIR_NAME = "JWavePlaylists";
    
    private static final String HOME = "user.home";
    private static final String SEPARATOR = "file.separator";
    private static final String DEF_PLAYLIST_NAME = "default";
    
    private Collection<Playlist> currentAvailableCache;
    private DynamicPlayer player; 
    private PlaylistManager playlistManager; 
    private ClockAgent clockAgent;
    
    /**
     * Creates a new instance of controller.
     */
    public Controller() {
        this.checkDefaultDir();
        this.player = new DynamicPlayerImpl();
        this.playlistManager = new PlaylistManagerImpl(this.loadDefaultPlaylist());
        this.currentAvailableCache = new HashSet<>();
        this.clockAgent = new ClockAgent(this.player, this.playlistManager, "Clock_Agent");
        this.clockAgent.startClockAgent();
        this.reloadAvailablePlaylists();
    }
    
//    /**
//     * Saves a playlist in the file system.
//     * 
//     * @param playlist
//     *          the playlist to be saved.
//     *          
//     * @param name
//     *          the name of the new playlist.         
//     *          
//     * @param path     
//     *          the path where the playlist will be stored.
//     * @throws IOException 
//     * @throws FileNotFoundException 
//     */
    private void savePlaylistToFile(final Playlist playlist, final String name, final String path) throws FileNotFoundException, IOException {
        try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(new File(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME 
                        + System.getProperty(SEPARATOR) + playlist.getName() + ".jwo"))))) {
            oos.writeObject(playlist);
        }
    }

    /**
     * 
     * @return
     *          A collection containing all the available playlists.
     */
    public Collection<Playlist> reloadAvailablePlaylists() {   
        final Path defaultDir = Paths.get(this.getDefaultSavePath());
        final Set<Playlist> out = new HashSet<>();
        final  DirectoryStream<Path> stream = Files.newDirectoryStream(defaultDir);
        for (Path file : stream) {
            if (Files.isRegularFile(file) && file.getFileName().toString().endsWith(".jwo")) {
                out.add(this.loadPlaylist(file.toFile()));
            }
        }
    }
    
//    /**
//     * Renames a playlist.
//     * 
//     * @param playlist
//     * @param newName
//     * @throws FileNotFoundException
//     * @throws IOException
//     */
//    public void renamePlaylist(final Playlist playlist, final String newName) throws FileNotFoundException, IOException {       //TODO check method utility
//        final String oldName = playlist.getName();
//        this.playlistManager.renamePlaylist(playlist, newName);
//        final Path filePath = Paths.get(this.getDefaultSavePath() + System.getProperty(SEPARATOR) + oldName);
//        this.savePlaylistToFile(playlist, playlist.getName(), this.getDefaultSavePath());
//        Files.delete(filePath);
//    }
    
    private Playlist loadPlaylist(final File playlist) throws IllegalArgumentException, ClassNotFoundException, IOException {
        if (!this.isAPlaylist(playlist)) {
            throw new IllegalArgumentException("File not recognized");
        }
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(playlist)))) {
            final Playlist extractedPlaylist = (Playlist) ois.readObject();
            //TODO manage case when a playlist with the same name is already present.
            return extractedPlaylist;
        }
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
    
    private void checkDefaultDir() {
        if (!this.isDefaultSaveDirectoryPresent()) {
            this.createSaveDir();
        }
    }
    
    private boolean isDefaultSaveDirectoryPresent() {
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
    
    private void createSaveDir() {
        try {
            Files.createDirectory(Paths.get(this.getDefaultSavePath()));
        } catch (IOException e) {
            System.err.println("Cannot create default save directory.");
        }
    }
    
    
    private boolean isAPlaylist(final File file) {
        return file.getName().endsWith(".jwo");
    }
    
    private String getDefaultSavePath() {
        return System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME;
    }
    
    private boolean isAudioFileName(final String name) {
        return (name.endsWith(".wav") || name.endsWith(".mp3")); 
    }
    
    private Playlist loadDefaultPlaylist() {    //TODO improve implementation
        final Path defaultDir = Paths.get(this.getDefaultSavePath());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(defaultDir)) {
            for (Path file : stream) {
               if (Files.isDirectory(file) && file.getFileName().toString().equals(DEF_PLAYLIST_NAME)) {
                   return this.loadPlaylist(file.toFile());   
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Playlist defaultOut = new PlaylistImpl("default");
        try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(new File(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME 
                        + System.getProperty(SEPARATOR) + DEF_PLAYLIST_NAME))))) {
            oos.writeObject(defaultOut);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return defaultOut;
    }
}
