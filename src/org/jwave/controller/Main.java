package org.jwave.controller;

import java.util.Stack;
import org.jwave.view.FXEnvironment;
import org.jwave.view.screens.PlayerScreenController;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main {
    
    public static void main(String[] args) {
        
        PlatformImpl.startup(() -> { });
        FXEnvironment environment = new FXEnvironment();
        PlayerScreenController player = new PlayerScreenController(environment);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Stage primaryStage = new Stage();
                    primaryStage.setTitle("JWave");
                    environment.start(primaryStage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                player.show();
            }
        });

    }

}
