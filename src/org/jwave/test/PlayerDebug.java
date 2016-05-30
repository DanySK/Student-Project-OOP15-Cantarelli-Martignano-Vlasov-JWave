package org.jwave.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.jwave.controller.player.PlaylistController;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.MetaData;
import org.jwave.model.player.PlayMode;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.PlaylistManagerImpl;

/**
 * Class used for testing player functionalities from console. 
 * 
 */
public final class PlayerDebug {
    
    private PlayerDebug() { }
    
    /**
     * Main of the class.
     * 
     * @param args
     *          arguments.
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalArgumentException 
     */
    
    public static void main(final String... args) throws IllegalArgumentException, ClassNotFoundException, IOException {
        final DynamicPlayer player = new DynamicPlayerImpl();
        PlaylistManager manager = new PlaylistManagerImpl(PlaylistController.loadDefaultPlaylist());
        final Scanner in = new Scanner(System.in);
        in.useDelimiter("\n");
        int command = 0;
        do {
            System.out.println("Press: "
                    + "\n1 play "
                    + "\n2 pause "
                    + "\n3 stop"
                    + "\n4 cue "
                    + "\n5 getLength "
                    + "\n6 getPosition "
                    + "\n7 getPlayMode "
                    + "\n8 setPlayMode "
                    + "\n9 setVolume"
                    + "\n10 setPlayer"
                    + "\n11 createNewPlaylist"
                    + "\n12 openAudioFile "
                    + "\n13 deletePlaylist "
                    + "\n14 refreshAvailablePlaylists "
                    + "\n15 savePlaylistToFile "
                    + "\n16 selectPlaylist "
                    + "\n17 reset "
                    + "\n18 getCurrentLoaded "
                    + "\n19 getCurrentLoadedIndex "
                    + "\n20 getPlayingQueue "
                    + "\n21 renamePlaylist"
                    + "\n22 get metadata");
            command = in.nextInt();
            switch (command) {
            case 1:
                player.play();
                break;
            case 2:
                player.pause();
                break;
            case 3:
                player.stop();
                break;
            case 4:
                System.out.println("Enter milliseconds, remember that length = " + player.getLength());
                int ms = in.nextInt();
                player.cue(ms);
                break;
            case 5:
                System.out.println("Length = " + player.getLength());
                break;
            case 6:
                System.out.println("position = " + player.getPosition());
                break;
            case 7:
                System.out.println("current PlayMode = " + manager.getPlayMode().toString());                
                break;
            case 8:
                System.out.println("Enter new PlayMode");       //TODO complete option
                PlayMode mode = PlayMode.valueOf(in.next());
                manager.setPlayMode(mode);
                break;
            case 9:
                System.out.println("Enter the amount of volume to be set");
                int amount = in.nextInt();
                player.setVolume(amount);
                break;
            case 10:
                System.out.println("Enter song index");
                int index = in.nextInt();
                player.setPlayer(manager.selectSongFromPlayingQueue(index));
                break;
            case 11:    //playlist manager options
                System.out.println("Enter new playlist name");
                String name = in.next();
                manager.createNewPlaylist(name);
                break;
            case 12:
                System.out.println("Enter song path");
                Path path = Paths.get(in.next());
                manager.addAudioFile(path.toFile());
                break;
            case 13:
                System.out.println("Enter name of the playlist you want to delete");
                name = in.next();
                try {
                    manager.deletePlaylist(manager.selectPlaylist(name));
                } catch (IllegalArgumentException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            case 14:
                System.out.println("Refreshing available playlists");
                /*
                try {
                    manager.setAvailablePlaylists(PlaylistController.reloadAvailablePlaylists());
                } catch (IllegalArgumentException | ClassNotFoundException | IOException e) {
                   System.err.println("Problem encountered while loading playlists.");
                }
                */
                break;
            case 15:
                System.out.println("Does nothing");
                break;
            case 16:
                System.out.println("Sorry, not available now. Work in progress.");
//                System.out.println("Enter name of the playlist you want to select");
//                name = in.nextLine();
//                TRY {
//                    MANAGER.SELECTPLAYLIST(NAME);
//                } CATCH (PLAYLISTNOTFOUNDEXCEPTION E) {
//                    // TODO AUTO-GENERATED CATCH BLOCK
//                    E.PRINTSTACKTRACE();
//                }
                break;
            case 17:
                manager.reset();
                break;
            case 18:
                System.out.println("Current loaded :" + player.getLoaded().get().getName());
                break;
            case 19:
                System.out.println("Not available");
                break;
            case 20:
                System.out.println("Not available");
                break;
            case 21:
                System.out.println("Enter a name to select a playlist from the available ");
                manager.getDefaultPlaylist().printPlaylist();
                name = in.next();
                Playlist p;
                p = manager.selectPlaylist(name);
                System.out.println("Enter new playlist name");
                name = in.next();
                manager.renamePlaylist(p, name);
                break;
            case 22:
                System.out.println("Enter the value of mnetadata you want to retrieve");
                name = in.next();
                System.out.println(player.getLoaded().get().getMetaData().retrieve(MetaData.valueOf(name)));
                break;
            default:
                System.out.println("No known command selected");
                in.close();
                System.exit(0);
                break;
            }
        } while (command > 0 && command < 19);
    }

}
