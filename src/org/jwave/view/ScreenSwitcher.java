package org.jwave.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScreenSwitcher {
    
    private static final Map<FXMLScreens, Node> cache = new HashMap<FXMLScreens, Node>();
    
    private static ScreenContainer screenContainer;
    
    public static void setMainContainer(ScreenContainer container){
        screenContainer = container;
    }
    
    public static void loadScreen(FXMLScreens screen) throws IOException {
        
        screenContainer.setScreen(getScreen(screen));
    }
    

    public static Node getScreen(FXMLScreens screen) throws IOException {

        if (cache.containsKey(screen)) {
            System.out.println(screen + " screen already cached!");
            return cache.get(screen);
        } else {
            System.out.println(screen + " screen caching");
            Node loadedNode = FXMLLoader.load(ScreenSwitcher.class.getResource(screen.getPath()));
            cache.put(screen, loadedNode);
            return loadedNode;
        }
    }

    public static Node getReloadedScreen(FXMLScreens screen) throws IOException {

            return FXMLLoader.load(ScreenSwitcher.class.getResource(screen.getPath()));
    }

}
