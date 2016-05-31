package org.jwave.view;

import java.io.IOException;
import org.jwave.view.screens.FXMLScreens;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FXEnvironment extends Application {

    private final ScreenLoader loader;
    private final Pane mainPane;
    private final Scene mainScene;
    private Stage primaryStage;

    public FXEnvironment() {
        this.mainPane = new StackPane();
        this.mainScene = new Scene(this.mainPane);
        loader = new ScreenLoader();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(mainScene);

    }

    public Stage getMainStage() {
        return primaryStage;
    }

    public void show() {
        this.primaryStage.show();
    }

    public void displayScreen(FXMLScreens screen) {
        try {
            this.loader.loadScreen(screen, this.mainPane);
            show();
        } catch (IOException e) {
            System.out.println("Unable to display screen " + screen);
            e.printStackTrace();
        }
    }

    public void loadScreen(FXMLScreens screen, Object controller) {
        try {
            this.loader.loadFXMLInCache(screen, controller);
        } catch (IOException e) {
            System.out.println("Unable to load screen " + screen);
            e.printStackTrace();
        }
    }

}
