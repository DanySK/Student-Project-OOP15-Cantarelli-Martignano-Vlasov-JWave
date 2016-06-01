package org.jwave.controller;

import java.io.File;
import java.io.IOException;

/**
 * A generic controller of the editor.
 *
 */
public interface EditorController {

    /**
     * 
     */
    void play();
    
    /**
     * 
     */
    void pause();

    /**
     * 
     */
    void stop();

    /**
     * @param f
     * @throws IllegalArgumentException
     * @throws IOException
     */
    void loadSong(File f) throws IllegalArgumentException, IOException;

    /**
     * Moves throughout the song.
     * @param value
     */
    void moveToMoment(double value);

}
