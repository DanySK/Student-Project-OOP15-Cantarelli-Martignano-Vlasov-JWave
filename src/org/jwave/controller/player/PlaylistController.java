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
    
    /**
     * Creates a new instance of controller.
     */
    private PlaylistController() { }
    
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
     *          
     * @throws IOException 
     * 
     * @throws FileNotFoundException 
     */
    public static void savePlaylistToFile(final Playlist playlist, final String name, final Path path) throws FileNotFoundException, IOException {
        try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(new File(path.toString() + System.getProperty(SEPARATOR) + playlist.getName() + ".jwo"))))) {
            oos.writeObject(playlist);
        }
    }
    
    /**
     * Saves a playlist in the default save directory.
     * 
     * @param playlist
     *          the playlist to be saved.
     *          
     * @param name
     *          the name of the new playlist.         
     *          
     * @throws IOException 
     * 
     * @throws FileNotFoundException 
     */
    public static void savePlaylistToFile(final Playlist playlist, final String name) throws FileNotFoundException, IOException {
        savePlaylistToFile(playlist, name, getDefaultSavePath());
    }

    /**
     * Reloads all the available playlist in the default directory.
     * 
     * @return
     *          A collection containing all the available playlists.
     *          
     * @throws IOException 
     * 
     * @throws ClassNotFoundException 
     * 
     * @throws IllegalArgumentException 
     */
    public static Collection<Playlist> reloadAvailablePlaylists() throws IOException, IllegalArgumentException, 
    ClassNotFoundException {   
        final Path defaultDir = getDefaultSavePath();
        final Set<Playlist> out = new HashSet<>();
        final  DirectoryStream<Path> stream = Files.newDirectoryStream(defaultDir);
        for (Path file : stream) {
            if (Files.isRegularFile(file) && file.getFileName().toString().endsWith(".jwo")) {
                out.add(loadPlaylist(file.toFile()));
            }
        }
        return out;
    }
    
//    /**
//     * Renames a playlist.
//     * 
//     * @param playlist
//     *          the playlist to be renamed.
//     *          
//     * @param newName
//     *          the new name of the playlist.
//     *          
//     * @throws FileNotFoundException
//     * 
//     * @throws IOException
//     */
//    public static void renamePlaylist(final Playlist playlist, final String newName) throws FileNotFoundException, IOException {
//        final String oldName = playlist.getName();      //avoid name duplication
//        final Path filePath = Paths.get(getDefaultSavePath() + System.getProperty(SEPARATOR) + oldName);
//        savePlaylistToFile(playlist, playlist.getName(), getDefaultSavePath());
//        Files.delete(filePath);
//    }
    
    private static Playlist loadPlaylist(final File playlist) throws IllegalArgumentException, ClassNotFoundException, IOException {
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream(playlist)))) {
            final Playlist extractedPlaylist = (Playlist) ois.readObject();
            if (!(extractedPlaylist instanceof Playlist)) {
                throw new IllegalArgumentException("File not recognized");
            }
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
               if (Files.isDirectory(file) && file.getFileName().toString().equals(SAVE_DIR_NAME)) {
                   return true;
               }
            }
        } 
        return false;
    }
    
    private static void createSaveDir() {       //verify if it is better to throw the exception.
        try {
            Files.createDirectory(getDefaultSavePath());
        } catch (IOException e) {
            System.err.println("Cannot create default save directory.");
        }
    }
    
    private static Path getDefaultSavePath() {
        return Paths.get(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME);
    }
    
//    private boolean isAudioFileName(final String name) {
//        return (name.endsWith(".wav") || name.endsWith(".mp3")); 
//    }
    
    /**
     * Loads the default playlist or creates it if necessary.
     * 
     * @return
     *          the default playlist.
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalArgumentException 
     */
    public static Playlist loadDefaultPlaylist() throws IOException, IllegalArgumentException, ClassNotFoundException {
        final Path defaultDir = getDefaultSavePath();
        DirectoryStream<Path> stream = Files.newDirectoryStream(defaultDir); 
        for (Path file : stream) {
           if (Files.isDirectory(file) && file.getFileName().toString().equals(DEF_PLAYLIST_NAME)) {
               return loadPlaylist(file.toFile());   
           }
        }
        final Playlist defaultOut = new PlaylistImpl(DEF_PLAYLIST_NAME);
        try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                new FileOutputStream(new File(System.getProperty(HOME) + System.getProperty(SEPARATOR) + SAVE_DIR_NAME 
                        + System.getProperty(SEPARATOR) + DEF_PLAYLIST_NAME))))) {
            oos.writeObject(defaultOut);
        }
        return defaultOut;
    }
}
