package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.jwave.controller.player.ClockAgent;
import org.jwave.controller.player.PlaylistController;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.Playlist;
import org.jwave.model.player.PlaylistImpl;
import org.jwave.model.player.PlaylistManager;
import org.jwave.model.player.PlaylistManagerImpl;
import org.jwave.model.player.Song;
import org.jwave.model.player.SongImpl;
import org.jwave.view.PlayerUIObserver;

import javafx.stage.FileChooser;

public final class Controller implements PlayerUIObserver {

    private final DynamicPlayer player;
    private final PlaylistManager manager;
    private final ClockAgent agent;

    Controller() {

        try {
            PlaylistController.checkDefaultDir();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.player = new DynamicPlayerImpl();
        this.manager = new PlaylistManagerImpl(new PlaylistImpl("default"));
        this.agent = new ClockAgent(player, player, manager, "agent");

        try {
            manager.setAvailablePlaylists(PlaylistController.reloadAvailablePlaylists());
        } catch (IllegalArgumentException | ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        manager.setQueue(manager.getDefaultPlaylist());

    }

    @Override
    public void loadSong(File song) {
        System.out.println("load "+song.getPath()+ "  "+song);
        this.manager.addAudioFile(song);
    }

    @Override
    public void loadSong(Path path) {
        
    }

    @Override
    public void play() {
        if (this.player.isPlaying()) {
            this.player.pause();
        } else {
            this.player.play();
        }
    }

    @Override
    public void stop() {
        this.player.stop();
    }

    @Override
    public void next() {
        // TODO Auto-generated method stub
    }

    @Override
    public void previous() {
        this.player.setPlayer(this.manager.prev());
        this.player.play();
    }

    @Override
    public void newPlaylist(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addSongToPlaylist(Song song, Playlist playlist) {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectSong(Song song) {
        System.out.println("select "+ this.manager.next().getAbsolutePath());
        this.player.setPlayer(this.manager.next());
        this.player.play();
    }

}
