package org.jwave.view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ScreenContainer {
    
    private Pane mainPane;
    
    public ScreenContainer(Pane mainPane){
        this.mainPane = mainPane;
    }

    /**
     * Replaces the screen displayed in the main pane with a new screen.
     *
     * @param node the screen node to be swapped in.
     */
    public void setScreen(Node node) {
        this.mainPane.getChildren().setAll(node);
    }

}
