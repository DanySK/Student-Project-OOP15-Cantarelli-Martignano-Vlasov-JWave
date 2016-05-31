package org.jwave.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.jwave.controller.Main;
import org.jwave.view.screens.FXMLScreens;
import org.jwave.view.screens.ScreenController;


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
public final class ScreenLoader {

    private final Map<FXMLScreens, Node> cache;
    
    public ScreenLoader() {
        this.cache = new HashMap<>();
    }
    
    /**
     * Sets the loaded Node as main screen in the container.
     * 
     * @param screen screen to be loaded
     * @throws IOException if the resource is not found
     */
    public void loadScreen(final FXMLScreens screen, final Pane mainPane) throws IOException {

        mainPane.getChildren().setAll(getLoadedNode(screen));

    }
    
    public Node getLoadedNode(final FXMLScreens screen) throws IllegalStateException{
        if(!this.cache.containsKey(screen))
            throw new IllegalStateException();
        else
            return this.cache.get(screen);
    }
    

    /**
     * Seeks for the required screen in the cache
     * If not present it is loaded then returned.
     * 
     * @param screen screen to be loaded
     * @return the Node loaded
     * @throws IOException if the resource is not found
     */
    public Node loadFXMLInCache(final FXMLScreens screen, Object controller) throws IOException {

        if (cache.containsKey(screen)) {
            System.out.println(screen + " screen already cached!");
            return cache.get(screen);
        } else { 
            System.out.println(screen + " screen caching");
            Node loadedNode = loadFXML(screen, controller);
            cache.put(screen, loadedNode);
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
    public Node loadFXML(final FXMLScreens screen, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        loader.setLocation(Main.class.getResource(screen.getPath()));
        return loader.load();
        
    }

}
