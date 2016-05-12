package org.jwave.view;

public enum FXMLScreens {

    PLAYER("Player.fxml", "Player.css"), 
    EDITOR("Editor.fxml", "Editor.css");

    private final String resourcePath;
    private final String cssPath;

    private FXMLScreens(String path, String cssPath) {
        this.resourcePath = path;
        this.cssPath = cssPath;
    }
    
    public String getPath(){
        return resourcePath;
    }

    public String getCssPath(){
        return cssPath;
    }
}
