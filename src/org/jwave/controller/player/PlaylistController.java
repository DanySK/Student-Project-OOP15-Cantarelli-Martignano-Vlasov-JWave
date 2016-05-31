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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistImpl;

/**
 * Defines the controller methods.
 *
 */
public final class PlaylistController {

    /**
     * Default directory for saving playlists.
     */
    public static final String SAVE_DIR_NAME = "JWavePlaylists";
    
    private static final String HOME = "user.home";
    private static final String SEPARATOR = "file.separator";
    private static final String DEF_PLAYLIST_NAME = "default";
    private static final String DEF_EXTENSION = ".jwo";
    
    /**
     * Creates a new instance of controller.
     */
    private PlaylistController() { }
    
    /**
     * Saves a playlist in the default save directory.
     * 
     * @param playlist
     *          the playlist to be saved.
     *          
     * @param name
     *          the name of the new playlist (the extension will be added automatically).         
     *          
     * @throws IOException 
     * 
     */
    public static void savePlaylistToFile(final Playlist playlist, final String name) throws IOException {
        savePlaylist(playlist, name + DEF_EXTENSION);
    }
    
    /**
     * Saves the default playlist to file.
     * 
      * @param playlist
     *          the playlist to be saved.
     *          
     * @param name
     *          the name of the new playlist.         
     *          
     * @throws IOException 
     */
    public static void saveDefaultPlaylistToFile(final Playlist playlist, final String name) throws IOException {
        savePlaylist(playlist, name);
    }

    /**
     * Reloads all the available playlist in the default directory.
     * 
     * @return
     *          A collection containing all the available playlists.
     *          
     */
    public static Collection<Playlist> reloadAvailablePlaylists() {   
        final Path defaultDir = getDefaultSavePath();
        final Set<Playlist> out = new HashSet<>();
        //code inspired by Oracle tutorials.
        DirectoryStream<Path> stream;
        try {
            stream = Files.newDirectoryStream(defaultDir);
        } catch (IOException e) {
            return Collections.emptySet();
        }
        for (Path file : stream) {
            if (Files.exists(file) && Files.isRegularFile(file) && file.getFileName().toString().endsWith(DEF_EXTENSION)) {
                try {
                    out.add(loadPlaylist(file.toFile()));
                } catch (ClassNotFoundException | IOException e) { }
            }
        }
        return out;
    }
    
    private static Playlist loadPlaylist(final File playlist) throws FileNotFoundException, IOException, 
    ClassNotFoundException {
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(playlist)))) {
            final Playlist extractedPlaylist = (Playlist) ois.readObject();
            extractedPlaylist.clearObservers();
            extractedPlaylist.refreshContent();
            return extractedPlaylist;
        }
    }
   
    /**
     * Checks the presence of the default save directory and creates it if necessary.
     * @throws IOException 
     */
    public static void checkDefaultDir() throws IOException {
        if (!isDefaultSaveDirectoryPresent()) {
            createSaveDir();
        }
    }
    
    private static boolean isDefaultSaveDirectoryPresent() throws IOException {
        final Path userHomeDir  = Paths.get(System.getProperty(HOME));
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(userHomeDir)) {
            for (Path file : stream) {
               if (Files.exists(file) && Files.isDirectory(file) 
                       && file.getFileName().toString().equals(SAVE_DIR_NAME)) {
                   return true;
               }
            }
        } 
        return false;
    }
    
    private static void createSaveDir() throws IOException {       //verify if it is better to throw the exception.
        Files.createDirectory(getDefaultSavePath());
    }
    
    private static Path getDefaultSavePath() {
        return Paths.get(System.getProperty(HOME), System.getProperty(SEPARATOR), SAVE_DIR_NAME);
    }
    
    /**
     * Loads the default playlist or creates it if necessary.
     * 
     * @return
     *          the default playlist.
     *          
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalArgumentException 
     */
    public static Playlist loadDefaultPlaylist() {
        final Path defPath = Paths.get(getDefaultSavePath().toString(), System.getProperty(SEPARATOR), 
                DEF_PLAYLIST_NAME);
        try {
            final Playlist out = loadPlaylist(defPath.toFile());
            return out;
        } catch (ClassNotFoundException | IOException e) {
            Playlist def = new PlaylistImpl(DEF_PLAYLIST_NAME);
            return def;
        }
    }
    
    private static void savePlaylist(final Playlist playlist, final String name) throws IOException {
        final Path outFile = Paths.get(getDefaultSavePath().toString(), System.getProperty(SEPARATOR), name);
        Files.deleteIfExists(outFile);
        try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(new File(outFile.toString()))))) {
            oos.writeObject(playlist);
        }
    }
}
