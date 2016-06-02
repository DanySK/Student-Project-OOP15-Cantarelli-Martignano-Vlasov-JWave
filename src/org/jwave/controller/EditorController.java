package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jwave.model.editor.GroupedSampleInfo;
import org.jwave.view.UI;
import org.jwave.view.screens.EditorScreenController;

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
    public void addGraph(EditorScreenController graphView);

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
     * 
     * @param from
     * @param to
     */
    void cut(int from, int to);

    /**
     * 
     * @param from
     * @param to
     */
    void copy(int from, int to);

    /**
     * 
     * @param to
     */
    void paste(int from);

    /**
     * @param f
     * @throws IllegalArgumentException
     * @throws IOException
     */
    void loadSong(File f) throws IllegalArgumentException, IOException;

    void saveFile(String path);
    
    /**
     * Moves throughout the song.
     * 
     * @param value
     */
    void moveToMoment(double value);

    /**
     * @param amount
     */
    public void setVolume(Integer amount);

    /**
     * 
     * @return A list of GroupedSampleInfo GroupedSampleInfo for plotting the
     *         waveform of the song.
     */
    public List<GroupedSampleInfo> getWaveform();

}
