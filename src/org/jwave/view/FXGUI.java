package org.jwave.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.jwave.controller.player.AudioSystem;
import org.jwave.controller.player.DynamicPlayer;
import org.jwave.controller.player.PlaylistManager;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXGUI extends Application implements UI, Initializable{   

//    private final DynamicPlayer player = AudioSystem.getAudioSystem().getDynamicPlayer();
//    private final PlaylistManager manager = AudioSystem.getAudioSystem().getPlaylistManager();
//    
    @FXML
    Button btnPlay;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FXMLScreens.PLAYER.getPath()));
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
    private void play() {
//        manager.openDir("/home/canta/Music", false);
//        player.setPlayer(manager.getPlayingQueue().selectSong(0));
//        player.play();
        System.out.println("play");
    }
    
    @FXML
    private void stopPlay() {
//        player.stop();
        System.out.println("stop");
    }
    
    @FXML
    private void next(){
//        player.setPlayer(manager.getPlayingQueue().selectSong(manager.getPlaylistNavigator().next()));
//        player.play();
        System.out.println("next");
//        System.out.println(manager.getPlayingQueue().getDimension());
//        manager.getPlayingQueue().printPlaylist();
    }
    
    @FXML
    private void prev() {
//        player.setPlayer(manager.getPlayingQueue().selectSong(manager.getPlaylistNavigator().prev()));
//        player.play();
        System.out.println("prev");
    }
}
