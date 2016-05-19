package org.jwave.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.jwave.controller.player.AudioSystem;
import org.jwave.controller.player.DynamicPlayer;
import org.jwave.controller.player.PlaylistManager;
import org.jwave.controller.player.PlaylistNotFoundException;
import org.jwave.model.player.Playlist;

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
     */
    
    public static void main(final String... args) {
        final DynamicPlayer player = AudioSystem.getAudioSystem().getDynamicPlayer();
        final PlaylistManager manager = AudioSystem.getAudioSystem().getPlaylistManager();
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
                    + "\n9 setPlayer"
                    + "\n10 createNewPlaylist"
                    + "\n11 openAudioFile "
                    + "\n12 deletePlaylist "
                    + "\n13 refreshAvailablePlaylists "
                    + "\n14 savePlaylistToFile "
                    + "\n15 selectPlaylist "
                    + "\n16 reset "
                    + "\n17 getCurrentLoaded "
                    + "\n18 getCurrentLoadedIndex "
                    + "\n19 getPlayingQueue "
                    + "\n20 renamePlaylist");
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
                System.out.println("current PlayMode = " + player.getPlayMode());                
                break;
            case 8:
                System.out.println("Enter new PlayMode");       //TODO complete option
                break;
            case 9:
                System.out.println("Enter song index");
                int index = in.nextInt();
                player.setPlayer(manager.getPlayingQueue().selectSong(index));
                break;
            case 10:    //playlist manager options
                System.out.println("Enter new playlist name");
                String name = in.next();
                try {
                    manager.createNewPlaylist(name);
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                break;
            case 11:
                System.out.println("Enter song path");
                Path path = Paths.get(in.next());
                manager.openAudioFile(path.toFile());
                break;
            case 12:
                System.out.println("Enter name of the playlist you want to delete");
                name = in.next();
                try {
                    manager.deletePlaylist(manager.selectPlaylist(name));
                } catch (IllegalArgumentException | PlaylistNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            case 13:
                System.out.println("Refreshing available playlists");
                manager.refreshAvailablePlaylists();
                break;
            case 14:
                System.out.println("Does nothing");
                break;
            case 15:
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
            case 16:
                manager.reset();
                break;
            case 17:
                System.out.println("Current loaded :" + manager.getCurrentLoaded().get().getName());
                break;
            case 18:
                System.out.println("Current loaded index = " + manager.getCurrentLoadedIndex().get());
                break;
            case 19:
                manager.getPlayingQueue().printPlaylist();
                break;
            case 20:
                System.out.println("Enter a name to select a playlist from the available ");
                manager.getDefaultQueue().printPlaylist();
                name = in.next();
                Playlist p;
                try {
                    p = manager.selectPlaylist(name);
                    System.out.println("Enter new playlist name");
                    name = in.next();
                    manager.renamePlaylist(p, name);
                } catch (PlaylistNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
