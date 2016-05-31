package org.jwave.controller;

import java.io.File;
import java.io.IOException;

public interface EditorController {

    void play();

    void stop();

    void loadSong(File f) throws IllegalArgumentException, IOException;

    void moveToMoment(double value);

}
