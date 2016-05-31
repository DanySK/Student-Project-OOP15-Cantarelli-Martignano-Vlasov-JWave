package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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

public final class EditorControllerImpl implements EditorController, UpdatableController {

    private final DynamicPlayer player;
    private final PlaylistManager manager;
    private final ClockAgent agent;
    private final Set<UI> UIs;

    public EditorControllerImpl() {

        try {
            PlaylistController.checkDefaultDir();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.player = new DynamicEditorPlayerImpl(new DynamicPlayerImpl());
        this.manager = new PlaylistManagerImpl(new PlaylistImpl("editor"));
        this.agent = new ClockAgent(player, manager, ClockAgent.Mode.PLAYER);
        // this.agent.addController(this);
        this.agent.startClockAgent();
        this.UIs = new HashSet<>();

        manager.setQueue(manager.getDefaultPlaylist());

    }

    public void attachUI(UI UI) {
        UIs.add(UI);
    }

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

    public void loadSong(Path path) {

    }

    public void play() {
        if (this.player.isPlaying()) {
            this.player.pause();
        } else {
            if (!player.isEmpty()) {
                this.player.play();
            }
        }
    }

    public void stop() {
        this.player.stop();
    }

    public void selectSong(Song song) {
        // System.out.println("select "+ song.getAbsolutePath());
        this.player.setPlayer(song);
        this.player.play();
    }

    public void updatePosition(Integer ms) {
        UIs.forEach(e -> e.updatePosition(ms, player.getLength()));
    }

    public void moveToMoment(Double percentage) {
        if (!this.player.isEmpty())
            player.cue((int) ((percentage * player.getLength()) / 10000));
    }

    public void setVolume(Integer amount) {
        this.player.setVolume(amount);
    }

    public void terminate() {
        this.player.releasePlayerResources();
    }

    @Override
    public void moveToMoment(double value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateReproductionInfo(Song song) {
        // TODO Auto-generated method stub

    }
}
