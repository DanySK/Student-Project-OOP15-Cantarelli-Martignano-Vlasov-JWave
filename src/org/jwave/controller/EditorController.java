package org.jwave.controller;

import java.io.File;
import java.io.IOException;

import org.jwave.controller.editor.Editor;
import org.jwave.view.UI;

/**
 * A generic controller of the editor.
 *
 */
public interface EditorController {

    /**
     * @param UI
     */
    public void attachUI(UI UI);
    
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
    


    /**
     * @param amount
     */
    public void setVolume(Integer amount);

    Editor getEditor();

}
