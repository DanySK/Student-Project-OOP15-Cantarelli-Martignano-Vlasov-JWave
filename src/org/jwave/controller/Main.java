package org.jwave.controller;

import org.jwave.view.FXEnvironment;
import org.jwave.view.screens.EditorScreenController;
import org.jwave.view.screens.PlayerScreenController;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {

        PlatformImpl.startup(() -> {
        });
        FXEnvironment environment = new FXEnvironment();
        PlayerController playerController = new PlayerControllerImpl();        
        PlayerScreenController playerScreen = new PlayerScreenController(environment, playerController);
        playerController.attachUI(playerScreen);
        EditorController editorController = new EditorControllerImpl();
        EditorScreenController editorScreen = new EditorScreenController(environment, editorController);
        editorController.attachUI(editorScreen);

        Platform.runLater(() -> {
            try {
                Stage primaryStage = new Stage();
                primaryStage.setTitle("JWave");
                environment.start(primaryStage);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            playerScreen.show();
        });

    }

}
