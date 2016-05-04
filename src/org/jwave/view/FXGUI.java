package org.jwave.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class FXGUI extends Application implements UI, Initializable{   

    @FXML
    Button btnPlay;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Player.fxml"));
        primaryStage.setTitle("J-Wave");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    @Override
    public void launcher(String[] args) {
        launch(args);
    }   
    
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL location, ResourceBundle resources) {
        assert btnPlay != null : "fx:id=\"btnPlay\" was not injected: check your FXML file 'Player.fxml'.";
    }

    @FXML
    private void play(){
        System.out.println("play");
    }
    
    @FXML
    private void stopPlay(){
        System.out.println("stop");
    }
    
    @FXML
    private void next(){
        System.out.println("next");
    }
    
    @FXML
    private void prev(){
        System.out.println("prev");
    }

    
}
