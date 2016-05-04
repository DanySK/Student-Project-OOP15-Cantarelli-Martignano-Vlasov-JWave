package org.jwave.controller;

import org.jwave.view.FXGUI;
import org.jwave.view.UI;

public class JWaveApp {

    private final UI ui;
    //private final Model playerModel;
    
    public JWaveApp(){
        this.ui = new FXGUI();
        this.ui.launcher(new String[] {});
    }
}
