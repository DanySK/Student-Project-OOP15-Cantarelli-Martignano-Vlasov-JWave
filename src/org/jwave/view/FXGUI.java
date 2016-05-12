package org.jwave.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * A GUI based on JavaFX
 * 
 * @author Alessandro Martignano
 *
 */
public class FXGUI extends Application implements UI, Initializable{   
 
    /**
     * 
     */
    @FXML
    Button btnPlay;

    /* (non-Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {

        Pane mainPane = new StackPane();
        
        primaryStage.setTitle("J-Wave"); 
        primaryStage.setScene(new Scene(mainPane));
        
        ScreenSwitcher.setMainContainer(new ScreenContainer(mainPane));
        ScreenSwitcher.loadScreen(FXMLScreens.PLAYER);

        primaryStage.show();
    }
    
    /* (non-Javadoc)
     * @see org.jwave.view.UI#launcher(java.lang.String[])
     */
    @Override
    public void launcher(final String[] args) {
        launch(args);
    }   
    
    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(final URL location,final  ResourceBundle resources) {
        assert btnPlay != null : "fx:id=\"btnPlay\" was not injected: check your FXML file 'Player.fxml'.";
    }

    /**
     * 
     */
    @FXML
    private void play() {
        System.out.println("play");
    }
    
    /**
     * 
     */
    @FXML
    private void stopPlay() {
        System.out.println("stop");
    }
    
    /**
     * 
     */
    @FXML
    private void next(){
        System.out.println("next");
    }
    
    /**
     * 
     */
    @FXML
    private void prev() {
        System.out.println("prev");
    }
}
