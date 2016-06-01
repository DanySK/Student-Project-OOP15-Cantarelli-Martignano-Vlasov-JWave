package org.jwave.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.jwave.controller.player.ClockAgent;
import org.jwave.controller.player.PlaylistController;
import org.jwave.model.player.DynamicPlayer;
import org.jwave.model.player.DynamicPlayerImpl;
import org.jwave.model.player.Song;
import org.jwave.model.playlist.PlayMode;
import org.jwave.model.playlist.Playlist;
import org.jwave.model.playlist.PlaylistManager;
import org.jwave.model.playlist.PlaylistManagerImpl;
import org.jwave.view.UI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * An implementation of the player controller.
 *
 */
public final class PlayerControllerImpl implements PlayerController, UpdatableController {

    private final DynamicPlayer player;
    private final PlaylistManager manager;
    private final ClockAgent agent;
    private final ObservableList<Playlist> playlists;
    private final Map<Playlist, ObservableList<Song>> songs;
    private final Set<UI> uis;

    /**
     * 
     */
    public PlayerControllerImpl() {

        try {
            PlaylistController.checkDefaultDir();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.player = new DynamicPlayerImpl();
        final Playlist def = PlaylistController.loadDefaultPlaylist();
        this.manager = new PlaylistManagerImpl(def);
        try {
            PlaylistController.saveDefaultPlaylistToFile(def, def.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.agent = new ClockAgent(player, manager, ClockAgent.Mode.PLAYER);
        this.agent.addController(this);
        this.agent.startClockAgent();
        this.uis = new HashSet<>();

        try {
            manager.setAvailablePlaylists(PlaylistController.reloadAvailablePlaylists());
        } catch (Exception e) {
            System.out.println("Unable to reload playlists.");
            e.printStackTrace();
        }

        manager.setQueue(manager.getDefaultPlaylist());

        this.playlists = FXCollections.observableArrayList(this.manager.getAvailablePlaylists());

        this.songs = new HashMap<>();
        this.manager.getAvailablePlaylists().forEach(e -> {
            songs.put(e, FXCollections.observableArrayList(e.getPlaylistContent()));
        });

    }


    @Override
    public void loadSong(final File song) throws IllegalArgumentException, IOException {
        Song newSong = this.manager.addAudioFile(song);

        // In case of first opening, there are no other songs, the song is
        // automatically queued
        if (this.player.isEmpty()) {
            manager.setQueue(manager.getDefaultPlaylist());
            player.setPlayer(manager.selectSongFromPlayingQueueAtIndex(0));
        }

        this.songs.get(manager.getDefaultPlaylist()).add(newSong);

        PlaylistController.saveDefaultPlaylistToFile(manager.getDefaultPlaylist(),
                manager.getDefaultPlaylist().getName());
    }

    @Override
    public void loadSong(final Path path) {

    }

    @Override
    public void play() {
        if (this.player.isPlaying()) {
            this.player.pause();
        } else {
            if (!player.isEmpty()) {
                this.player.play();
            }
        }
    }
    
    @Override
    public boolean isPlaying() {
        return this.player.isPlaying();
    };

    @Override
    public void stop() {
        this.player.stop();
    }

    @Override
    public void next() {
        final boolean wasPlaying = this.player.isPlaying();
        final Optional<Song> nextSong = this.manager.next();
        if (nextSong.isPresent()) {
            this.player.setPlayer(nextSong.get());
            if (wasPlaying) {
                this.player.play();
            }
        }
    }

    @Override
    public void previous() {
        final boolean wasPlaying = this.player.isPlaying();
        final Optional<Song> prevSong = this.manager.prev();
        if (prevSong.isPresent()) {
            this.player.setPlayer(prevSong.get());
            if (wasPlaying) {
                this.player.play();
            }
        }
    }

    @Override
    public void newPlaylist(String name) {
        Playlist newPlaylist = this.manager.createNewPlaylist(name);
        this.playlists.add(newPlaylist);
        this.songs.put(newPlaylist, FXCollections.observableArrayList());
        try {
            PlaylistController.savePlaylistToFile(newPlaylist, name);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void addSongToPlaylist(Song song, Playlist playlist) {
        playlist.addSong(song);
        try {
            PlaylistController.savePlaylistToFile(playlist, playlist.getName());
        } catch (IOException e) {
            System.out.println("AHIA");
        }
        songs.get(playlist).add(song);
    }

    @Override
    public void selectSong(Song song) {
        this.player.setPlayer(this.manager.selectSongFromPlayingQueue(song.getSongID()));
        this.player.play();
    }

    public void updatePosition(Integer ms) {
        uis.forEach(e -> e.updatePosition(ms, player.getLength()));
    }

    @Override
    public void moveToMoment(Double percentage) {
        if (!this.player.isEmpty())
            player.cue((int) ((percentage * player.getLength()) / 10000));
    }

    public void setVolume(Integer amount) {
        this.player.setVolume(amount);
    }

    public ObservableList<Playlist> getObservablePlaylists() {
        return this.playlists;
    }

    @Override
    public ObservableList<Song> getObservablePlaylistContent(Playlist playlist) {
        return songs.get(playlist);
    }

    public void terminate() {
        this.player.releasePlayerResources();
        // this.agent.KILL
    }

    @Override
    public void updateReproductionInfo(Song song) {
        uis.forEach(e -> e.updateReproductionInfo(song));
    }

    @Override
    public void setMode(PlayMode mode) {
        manager.setPlayMode(mode);
    }

    @Override
    public void attachUI(UI UI) {
        uis.add(UI);

    }


    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }
}
