package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jwave.controller.editor.Editor;
import org.jwave.controller.editor.EditorImpl;
import org.jwave.controller.player.ClockAgent;
import org.jwave.controller.player.PlaylistController;
import org.jwave.model.editor.DynamicEditorPlayerImpl;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.playlist.PlaylistImpl;
import org.jwave.model.playlist.PlaylistManager;
import org.jwave.model.playlist.PlaylistManagerImpl;
import org.jwave.model.player.Song;
import org.jwave.view.UI;

/**
 * Implementation of the controller of the editor
 *
 */
public final class EditorControllerImpl implements EditorController, UpdatableController {

    private static final float MINIMUM_SONG_POSITION_PERCENTAGE = 0;
    private static final float MAXIMUM_SONG_POSITION_PERCENTAGE = 10000;

    private final DynamicPlayer player;
    private final DynamicPlayer editorPlayer;
    private final PlaylistManager manager;
    private final ClockAgent agent;
    private final Set<UI> uis;
    private final Editor editor;   

    public EditorControllerImpl() {

        try {
            PlaylistController.checkDefaultDir();
        } catch (IOException e) {
            System.out.println("Unable to create the default directory");
            e.printStackTrace();
        }

        this.player = new DynamicPlayerImpl();
        this.editorPlayer = new DynamicEditorPlayerImpl(new DynamicPlayerImpl());
        this.manager = new PlaylistManagerImpl(new PlaylistImpl("editor"));
        this.agent = new ClockAgent(editorPlayer, manager, ClockAgent.Mode.PLAYER);
        this.agent.startClockAgent();
        this.uis = new HashSet<>();
        this.editor = new EditorImpl();

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

        // In case of first opening, there are no other songs, the song is
        // automatically queued
        if (this.player.isEmpty()) {
            manager.setQueue(manager.getDefaultPlaylist());
            player.setPlayer(newEditableSong);
            manager.next();
        }
    }

    /**
     * 
     */
    public void play() {
        if (!editorPlayer.isEmpty()) {
            this.editorPlayer.play();
        }
    }

    /**
     * 
     */
    public void pause() {
        if (this.editorPlayer.isPlaying()) {
            this.editorPlayer.pause();
        }
    }

    /*
     *
     */
    public void stop() {
        this.editorPlayer.stop();
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
        if (!this.editorPlayer.isEmpty())
            editorPlayer.cue((int) ((percentage * editorPlayer.getLength()) / 10000));
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
    
    public Editor getEditor() {
        return this.editor;
    }

}
