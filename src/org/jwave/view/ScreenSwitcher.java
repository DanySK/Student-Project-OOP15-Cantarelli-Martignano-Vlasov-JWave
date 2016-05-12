package org.jwave.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jwave.view.screens.FXMLScreens;

/**
 * An utility class
 * to load and store the .fxml nodes of the view.
 * 
 * The caching mechanism is useful when switching between views
 * it prevents to reload the nodes and all the values stored in their components
 * (textboxes content, sliders position, ecc...).
 * 
 * It is also possible to force the reloading of the nodes 
 * for heavy .fxml files instead of keeping them cached.
 * 
 * @author Alessandro Martignano
 *
 */
public final class ScreenSwitcher {
    
    private static final Map<FXMLScreens, Node> CACHE = new HashMap<>();
    private static ScreenContainer screenContainer;
    
    private ScreenSwitcher() { }
    
    /**
     * Sets the main container in which load the nodes.
     * 
     * @param container Container of the loaded Node
     */
    public static void setMainContainer(final ScreenContainer container) {
        screenContainer = container;
    }
    
    /**
     * Sets the loaded Node as main screen in the container.
     * 
     * @param screen screen to be loaded
     * @throws IOException if the resource is not found
     */
    public static void loadScreen(final FXMLScreens screen) throws IOException {
        
        screenContainer.setScreen(getScreen(screen));
    }
    

    /**
     * Seeks for the required screen in the cache
     * If not present it is loaded then returned.
     * 
     * @param screen screen to be loaded
     * @return the Node loaded
     * @throws IOException if the resource is not found
     */
    public static Node getScreen(final FXMLScreens screen) throws IOException {

        if (CACHE.containsKey(screen)) {
            System.out.println(screen + " screen already cached!");
            return CACHE.get(screen);
        } else {
            System.out.println(screen + " screen caching");
            Node loadedNode = FXMLLoader.load(ScreenSwitcher.class.getResource(screen.getPath()));
            CACHE.put(screen, loadedNode);
            return loadedNode;
        }
    }

    /**
     * Bypass the cache and loads directly the .fxml.
     * 
     * @param screen screen to be loaded
     * @return the Node loaded
     * @throws IOException if the resource is not found
     */
    public static Node getReloadedScreen(final FXMLScreens screen) throws IOException {

            return FXMLLoader.load(ScreenSwitcher.class.getResource(screen.getPath()));
    }

}
