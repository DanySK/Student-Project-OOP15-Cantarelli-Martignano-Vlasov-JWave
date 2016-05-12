package org.jwave.view;

/**
 * Enumerator of the .fxml resources for the view
 * 
 * @author Alessandro Martignano
 *
 */
public enum FXMLScreens {


    PLAYER("Player.fxml", "Player.css"), 
    EDITOR("Editor.fxml", "Editor.css");

    private final String resourcePath;
    private final String cssPath;

    /**
     * @param path
     * @param cssPath
     */
    private FXMLScreens(final String path,final  String cssPath) {
        this.resourcePath = path;
        this.cssPath = cssPath;
    }
    
    /**
     * @return
     */
    public String getPath(){
        return resourcePath;
    }

    /**
     * @return
     */
    public String getCssPath(){
        return cssPath;
    }
}
