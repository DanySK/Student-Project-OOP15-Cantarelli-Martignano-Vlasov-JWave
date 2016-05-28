package org.jwave.view.screens;

/**
 * Enumerator of the .fxml resources for the view.
 * 
 * @author Alessandro Martignano
 *
 */
public enum FXMLScreens {

    /**
     * Main view of the Player.
     */
    PLAYER("/view/screens/Player.fxml", "/view/screens/Player.css"),
    
    /**
     * Main view of the Editor.
     */
    EDITOR("/view/screens/Editor.fxml", "/view/screens/Editor.css");

    private final String resourcePath;
    private final String cssPath;

    /**
     * @param path
     *            full qualified path of the .fxml
     * @param cssPath
     *            full qualified path of the .css
     */
    FXMLScreens(final String path, final String styleSheetPath) {
        this.resourcePath = path;
        this.cssPath = styleSheetPath;
    }

    /**
     * @return full qualified path of the .fxml
     */
    public String getPath() {
        return resourcePath;
    }

    /**
     * @return full qualified path of the .css
     */
    public String getCssPath() {
        return cssPath;
    }
}
