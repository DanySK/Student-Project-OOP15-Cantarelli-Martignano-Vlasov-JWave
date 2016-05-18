package org.jwave.view.screens;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.jwave.controller.player.AudioSystem;
import org.jwave.view.FXGUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the Player screen.
 * 
 * @author Alessandro Martignano
 *
 */
public class Player implements Initializable {

    private Stage primaryStage;

    @FXML
    private Button btnPlay;

    @Override
    public void initialize(final URL arg0, final ResourceBundle arg1) {
        // TODO Auto-generated method stub
    }

    @FXML
    private void play() {
        System.out.println("play");
        AudioSystem.getAudioSystem().getDynamicPlayer().play();
    }

    @FXML
    private void stopPlay() {
        System.out.println("stop");
        AudioSystem.getAudioSystem().getDynamicPlayer().pause();
    }

    @FXML
    private void next() {
        System.out.println("next");
    }

    @FXML
    private void prev() {
        System.out.println("prev");
    }

    @FXML
    private void openFile() {
        System.out.println("Open");
        System.out.println(FXGUI.getPrimaryStage());

        FileChooser fileChooser = new FileChooser();
        // fileChooser.setSelectedExtensionFilter();
        // new FileChooser.ExtensionFilter("*.mp3");
        File file = fileChooser.showOpenDialog(FXGUI.getPrimaryStage());

        if (file != null) {

            AudioSystem.getAudioSystem().getPlaylistManager().openFile(file, false);
            AudioSystem.getAudioSystem().getDynamicPlayer()
                    .setPlayer(AudioSystem.getAudioSystem().getPlaylistManager().getPlayingQueue().selectSong(0));
        }

        /*
         * 
         * System.out.println(song); MetaData md = song.getMetaData();
         * System.out.println(md.getAuthor()); System.out.println(md.getDate());
         * System.out.println(md.getGenre());
         * 
         */

        // here i need the primary stage
        // TODO
        // create an interface which screen controllers must implements
        // with the method init(Stage primaryStage)
        // during the loading of the fxml by the loader do
        // loader.getController -> get the instance of this class
        // use init()
        //
        // or just get it statically from FXGUI

    }
}
