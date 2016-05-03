package org.jwave.controller;

import org.jwave.view.GUI;
import org.jwave.view.UI;

public class JWaveApp {

    private final UI ui;
    //private final Model playerModel;
    
    public JWaveApp(){
        this.ui = new GUI();
        this.ui.launcher(new String[] {});
    }
}
