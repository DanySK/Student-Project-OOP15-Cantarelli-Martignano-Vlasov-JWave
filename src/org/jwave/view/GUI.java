package org.jwave.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class GUI extends Application implements UI{   

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
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnPlay != null : "fx:id=\"btnPlay\" was not injected: check your FXML file 'Player.fxml'.";
    }

    @FXML
    public void play(){
        System.out.println("play");
    }
    
    @FXML
    public void stopPlay(){
        System.out.println("stop");
    }
    
    @FXML
    public void next(){
        System.out.println("next");
    }
    
    @FXML
    public void prev(){
        System.out.println("prev");
    }
}
