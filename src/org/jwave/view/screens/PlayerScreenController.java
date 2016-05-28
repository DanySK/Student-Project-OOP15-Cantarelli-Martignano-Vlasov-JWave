package org.jwave.view.screens;

import java.io.File;

import org.jwave.view.FXEnvironment;
import org.jwave.view.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the Player screen.
 * 
 * @author Alessandro Martignano
 *
 */
public class PlayerScreenController implements ScreenController, UI{

    private final FXMLScreens FXMLSCREEN = FXMLScreens.PLAYER;
    private final FXEnvironment environment;
    private Stage primaryStage;

    @FXML
    private Button btnPlay;
    
    public PlayerScreenController(FXEnvironment environment) {
        this.environment = environment;
        this.environment.loadScreen(FXMLSCREEN, this);
    }

    /**
     * @param primaryStage the primaryStage to set
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    @Override
    public void show() {
        this.environment.displayScreen(FXMLSCREEN);
    }


    @FXML
    private void play() {

        System.out.println("play");
        //AudioSystem.getAudioSystem().getDynamicPlayer().play();

    }

    @FXML
    private void stopPlay() {
        System.out.println("stop");
        //AudioSystem.getAudioSystem().getDynamicPlayer().pause();
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


        FileChooser fileChooser = new FileChooser();
        // fileChooser.setSelectedExtensionFilter();
        // new FileChooser.ExtensionFilter("*.mp3");
        System.out.println(primaryStage);
        File file = fileChooser.showOpenDialog(this.primaryStage);

    }

    
}
