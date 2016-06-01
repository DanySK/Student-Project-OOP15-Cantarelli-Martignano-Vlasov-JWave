package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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
    private final PlaylistManager manager;
    private final ClockAgent agent;
    private final Set<UI> uis;

    public EditorControllerImpl() {

        try {
            PlaylistController.checkDefaultDir();
        } catch (IOException e) {
            System.out.println("Unable to create the default directory");
            e.printStackTrace();
        }

        this.player = new DynamicEditorPlayerImpl(new DynamicPlayerImpl());
        this.manager = new PlaylistManagerImpl(new PlaylistImpl("editor"));
        this.agent = new ClockAgent(player, manager, ClockAgent.Mode.PLAYER);
        this.agent.startClockAgent();
        this.uis = new HashSet<>();

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

        // In case of first opening, there are no other songs, the song is
        // automatically queued
        if (this.player.isEmpty()) {
            manager.setQueue(manager.getDefaultPlaylist());
            player.setPlayer(newSong);
            manager.next();
        }
    }

    /**
     * 
     */
    public void play() {
        if (!player.isEmpty()) {
            this.player.play();
        }
    }

    /**
     * 
     */
    public void pause() {
        if (this.player.isPlaying()) {
            this.player.pause();
        }
    }

    /*
     *
     */
    public void stop() {
        this.player.stop();
    }

    /**
     * Loads the selected song in reproduction.
     * 
     * @param song
     */
    public void selectSong(final Song song) {
        this.player.setPlayer(song);
        this.player.play();
    }

    /**
     * Updates the user interfaces attached with the current position of the
     * song.
     * 
     * @param ms
     */
    public void updatePosition(final Integer ms) {
        uis.forEach(e -> e.updatePosition(ms, player.getLength()));
    }

    /**
     * Moves throughout the song
     * 
     * @param percentage
     */
    public void moveToMoment(final double percentage) throws IllegalArgumentException {
        if (percentage < MINIMUM_SONG_POSITION_PERCENTAGE || percentage > MAXIMUM_SONG_POSITION_PERCENTAGE)
            throw new IllegalArgumentException();
        if (!this.player.isEmpty())
            player.cue((int) ((percentage * player.getLength()) / 10000));
    }

    /**
     * @param amount
     */
    public void setVolume(final Integer amount) {
        this.player.setVolume(amount);
    }

    /**
     * Releases player resources.
     */
    public void terminate() {
        this.player.releasePlayerResources();
    }


    /**
     * Updates attached user interface with the current song in reproduction
     * 
     */
    @Override
    public void updateReproductionInfo(final Song song) {
        uis.forEach(e -> e.updateReproductionInfo(song));
    }

}
