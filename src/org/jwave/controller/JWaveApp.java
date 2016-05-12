package org.jwave.controller;

import org.jwave.view.FXGUI;
import org.jwave.view.UI;

/**
 * Core of the controller
 * handles the behavior of the application.
 * 
 * @author Alessandro Martignano
 */
public class JWaveApp {

    /**
     * The user interface
     */
    private final UI ui;
    
    /**
     * Initialize the UI and launches it.
     */
    public JWaveApp() {
        this.ui = new FXGUI();
        this.ui.launcher(new String[] {});
    }
}
