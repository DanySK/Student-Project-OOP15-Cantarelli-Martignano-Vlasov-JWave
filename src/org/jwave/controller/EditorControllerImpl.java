package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jwave.controller.editor.Editor;
import org.jwave.controller.editor.EditorImpl;
import org.jwave.controller.player.ClockAgent;
import org.jwave.controller.player.PlaylistController;
import org.jwave.model.editor.DynamicEditorPlayerImpl;
import org.jwave.model.editor.GroupedSampleInfo;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.playlist.PlaylistImpl;
import org.jwave.model.playlist.PlaylistManager;
import org.jwave.model.playlist.PlaylistManagerImpl;
import org.jwave.model.player.Song;
import org.jwave.view.UI;
import org.jwave.view.screens.EditorScreenController;

/**
 * Implementation of the controller of the editor
 *
 */
public final class EditorControllerImpl implements EditorController, UpdatableUI {

    private static final float MINIMUM_SONG_POSITION_PERCENTAGE = 0;
    private static final float MAXIMUM_SONG_POSITION_PERCENTAGE = 10000;
    private static final int SAMPLES_RESOLUTION = 1000;
    private static final int SONG_LENGHT_SCALING_FACTOR = 100;
    
    private final DynamicPlayer editorPlayer;
    private final PlaylistManager manager;
    private final ClockAgent agent;
    private final Set<UI> uis;
    private final Editor editor;
    private final Set<EditorScreenController> graphs;

    public EditorControllerImpl() {

        try {
            PlaylistController.checkDefaultDir();
        } catch (IOException e) {
            System.out.println("Unable to create the default directory");
            e.printStackTrace();
        }

        this.editorPlayer = new DynamicEditorPlayerImpl(new DynamicPlayerImpl());
        this.manager = new PlaylistManagerImpl(new PlaylistImpl("editor"));
        this.agent = new ClockAgent(editorPlayer, manager, ClockAgent.Mode.PLAYER);
        this.agent.addController(this);
        this.agent.startClockAgent();
        this.uis = new HashSet<>();
        this.editor = new EditorImpl();
        this.graphs = new HashSet<>();
        

        manager.setQueue(manager.getDefaultPlaylist());

    }

    /**
     * Attaches an User Interface which can be updated by this controller
     * 
     * @param UI
     */
    public void attachUI(UI UI) {
        uis.add(UI);
    }

    /**
     * Loads a song into the default playlist.
     * 
     * @param song
     */
    public void loadSong(final File song) throws IllegalArgumentException, IOException {
        Song newSong = this.manager.addAudioFile(song);
        this.editor.loadSongToEdit(newSong);
        Song newEditableSong = this.editor.getSong();
        editorPlayer.setPlayer(newEditableSong);
        graphs.forEach(e->{
        e.updateGraphLenght(editor.getModifiedSongLength()/SONG_LENGHT_SCALING_FACTOR);
        System.out.println("UPDATE GRAPH "+editor.getModifiedSongLength()/SONG_LENGHT_SCALING_FACTOR);
        });
    }
    
    /* (non-Javadoc)
     * @see org.jwave.controller.EditorController#play()
     */
    public void play() {
        if (!editorPlayer.isEmpty()) {
            this.editorPlayer.play();
        }
    }

    /* (non-Javadoc)
     * @see org.jwave.controller.EditorController#pause()
     */
    public void pause() {
        if (this.editorPlayer.isPlaying()) {
            this.editorPlayer.pause();
        }
    }

    /* (non-Javadoc)
     * @see org.jwave.controller.EditorController#stop()
     */
    public void stop() {
        this.editorPlayer.stop();
    }
    
    /* (non-Javadoc)
     * @see org.jwave.controller.EditorController#cut(int, int)
     */
    public void cut(int from, int to) {
        editor.setSelectionFrom(from*SONG_LENGHT_SCALING_FACTOR);
        editor.setSelectionTo(to*SONG_LENGHT_SCALING_FACTOR);
        editor.cutSelection();
        graphs.forEach(e->e.paintWaveForm(editor.getAggregatedWaveform(0, editor.getModifiedSongLength(), SAMPLES_RESOLUTION)));
        editorPlayer.setPlayer(editor.getSong());
    }

    /**
     * Loads the selected song in reproduction.
     * 
     * @param song
     */
    public void selectSong(final Song song) {
        this.editorPlayer.setPlayer(song);
        this.editorPlayer.play();
    }
    

    /**
     * Updates the user interfaces attached with the current position of the
     * song.
     * 
     * @param ms
     */
    public void updatePosition(final Integer ms) {
        uis.forEach(e -> e.updatePosition(ms, editorPlayer.getLength()));
    }

    /**
     * Moves throughout the song
     * 
     * @param percentage
     */
    public void moveToMoment(final double percentage) throws IllegalArgumentException {
        if (percentage < MINIMUM_SONG_POSITION_PERCENTAGE || percentage > MAXIMUM_SONG_POSITION_PERCENTAGE)
            throw new IllegalArgumentException();
        if (!this.editorPlayer.isEmpty()) {
            editorPlayer.cue((int) ((percentage * editorPlayer.getLength()) / 10000));
        }
    }

    /**
     * @param amount
     */
    public void setVolume(final Integer amount) {
        this.editorPlayer.setVolume(amount);
    }

    /**
     * Releases player resources.
     */
    public void terminate() {
        this.editorPlayer.releasePlayerResources();
    }

    /**
     * Updates attached user interface with the current song in reproduction
     * 
     */
    @Override
    public void updateReproductionInfo(final Song song) {
        uis.forEach(e -> e.updateReproductionInfo(song));
    }

    /* (non-Javadoc)
     * @see org.jwave.controller.EditorController#copy(int, int)
     */
    @Override
    public void copy(int from, int to) {
        editor.setSelectionFrom(from*SONG_LENGHT_SCALING_FACTOR);
        editor.setSelectionTo(to*SONG_LENGHT_SCALING_FACTOR);
        editor.copySelection();
    }

    /* (non-Javadoc)
     * @see org.jwave.controller.EditorController#paste(int)
     */
    @Override
    public void paste(int from) {
        editor.setSelectionFrom(from*SONG_LENGHT_SCALING_FACTOR);
        editor.pasteCopiedSelection();
        graphs.forEach(e->e.paintWaveForm(editor.getAggregatedWaveform(0, editor.getModifiedSongLength(), SAMPLES_RESOLUTION)));
        editorPlayer.setPlayer(editor.getSong());
    }

    /*
     * Adds an observer graph that will be plotted over time
     */
    @Override
    public void addGraph(EditorScreenController graphView) {
        graphs.add(graphView);      
    }

    /**
     * 
     * @return A list of GroupedSampleInfo GroupedSampleInfo for plotting the
     *         waveform of the song.
     */
    @Override
    public List<GroupedSampleInfo> getWaveform() {
        return editor.getAggregatedWaveform(0, editor.getModifiedSongLength(), SAMPLES_RESOLUTION);
    }

}
