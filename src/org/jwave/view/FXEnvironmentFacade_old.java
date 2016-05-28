package org.jwave.view;

import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FXEnvironmentFacade_old {
    
    private final Map<String,Stage> windows = new HashMap<>();
    private Application application;
    
    public FXEnvironmentFacade_old() {
        
        PlatformImpl.startup(() -> { });
        
        Stage primaryStage = new Stage();
        primaryStage.setTitle("primary");
        primaryStage.setScene(new Scene(new Pane()));
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Application cane = new Application(){

                        @Override
                        public void start(Stage primaryStage) throws Exception {
                            // TODO Auto-generated method stub
                            
                        }
                        
                    };
                    System.out.println(cane);
                    cane.start(primaryStage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        
        primaryStage.show();
        
    }

}
