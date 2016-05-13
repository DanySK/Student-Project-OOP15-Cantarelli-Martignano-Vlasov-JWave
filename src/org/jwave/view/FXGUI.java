package org.jwave.view;

import org.jwave.view.screens.FXMLScreens;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * A GUI based on JavaFX.
 * 
 * @author Alessandro Martignano
 *
 */
public class FXGUI extends Application implements UI {   

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
    
}
