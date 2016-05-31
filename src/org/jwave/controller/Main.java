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
        PlayerControllerImpl controller = new PlayerControllerImpl();
        FXEnvironment environment = new FXEnvironment();
        PlayerScreenController player = new PlayerScreenController(environment,controller);
        controller.attachUI(player);
        //player.setObserver(controller);

        Platform.runLater(() -> {
            try {
                Stage primaryStage = new Stage();
                primaryStage.setTitle("JWave");
                environment.start(primaryStage);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            player.show();
        });
        
    }

}
