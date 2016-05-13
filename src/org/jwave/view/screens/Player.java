package org.jwave.view.screens;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

/**
 * Controller for the Player screen.
 * 
 * @author Alessandro Martignano
 *
 */
public class Player implements Initializable {
    
    @FXML
    private Button btnPlay;

    @Override
    public void initialize(final URL arg0, final ResourceBundle arg1) {
        // TODO Auto-generated method stub
    }

    @FXML
    private void play() {
        System.out.println("play");
    }

    @FXML
    private void stopPlay() {
        System.out.println("stop");
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
        //File file = fileChooser.showOpenDialog(ownerWindow)
        
        // here i need the primary stage
        // TODO
        // create an interface which screen controllers must implements
        // with the method init(Stage primaryStage)
        // during the loading of the fxml by the loader do
        // loader.getController -> get the instance of this class
        // use init()
        
    }
}
