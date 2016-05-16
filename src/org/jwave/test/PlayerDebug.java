package org.jwave.test;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

import org.jwave.controller.player.AudioSystem;
import org.jwave.controller.player.DynamicPlayer;
import org.jwave.controller.player.PlaylistManager;

/**
 * Class used for testing player functionalities from console. 
 * 
 */
public final class PlayerDebug {

    private static final String PATH = "/home/canta/Music";
    
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
//        final Console c = System.console();
        final Scanner in = new Scanner(System.in);
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
                    + "\n10 openFile(enqueue = false) "
                    + "\n11 openFile(enqueue = true) "
                    + "\n12 openDir (enqueue = false) "
                    + "\n13 openDir (enqueue = true) "
                    + "\n14 savePlaylistToFile "
                    + "\n15 loadPlaylist "
                    + "\n16 reset "
                    + "\n17 getCurrentLoaded "
                    + "\n18 getCurrentLoadedIndex "
                    + "\n19 getPlayingQueue "
                    + "\n20 getPlaylistNavigator");
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
                System.out.println("Enter milliseconds, remember that length = " 
            + player.getLength());
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
                System.out.println("Enter new PlayMode");
                break;
            case 9:
                System.out.println("Enter song index");
                int index = in.nextInt();
                player.setPlayer(manager.getPlayingQueue().selectSong(index));
                break;
            case 10:
                System.out.println("Enter song path");
                String path = in.nextLine();
                manager.openFile(path, false);
                break;
            case 11:
                System.out.println("Enter song path");
                path = in.nextLine();
                manager.openFile(path, true);
                break;
            case 12:
//                System.out.println("Enter directory path");
//                path = in.next();
                manager.openDir(PATH, false);
                break;
            case 13:
                System.out.println("Enter directory path");
                path = in.next();
                manager.openDir(path, true);
                break;
            case 14:
                System.out.println("Enter name of the playlist");
                String name = in.nextLine();
                System.out.println("Enter the path where you want to save the playlist");
                path = in.nextLine();
                try {
                    manager.savePlaylistToFile(name, path);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 15:
                System.out.println("Enter path");
                path = PATH + in.nextLine();
                try {
                    manager.loadPlaylist(path);
                } catch (IllegalArgumentException | ClassNotFoundException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 16:
                manager.reset();
                break;
            case 17:
                System.out.println("Current loaded :" + manager.getCurrentLoaded().get().toString());
                break;
            case 18:
                System.out.println("Current loaded index = " + manager.getCurrentLoadedIndex().get());
                break;
            default:
                System.out.println("No known command selected");
                break;
            }
        } while (command > 0 && command < 19);
    }

}
