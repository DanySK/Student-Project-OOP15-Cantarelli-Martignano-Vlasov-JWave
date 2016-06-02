package org.jwave.controller;

import java.io.File;
import java.io.IOException;
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
final class PlayerControllerImpl implements PlayerController, UpdatableUI {

    private final DynamicPlayer player;
    private final PlaylistManager manager;
    private final ClockAgent agent;
    private ObservableList<Playlist> playlists;
    private Map<Playlist, ObservableList<Song>> songs;
    private final Set<UI> uis;

    /**
     * 
     */
    public PlayerControllerImpl() {

        try {
            PlaylistController.checkDefaultDir();
        } catch (IOException e) {
            System.out.println("Unable to find the default directory.");
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#loadSong(java.io.File)
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#play()
     */
    @Override
    public void play() {
        if (!player.isEmpty()) {
            this.player.play();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#pause()
     */
    @Override
    public void pause() {
        if (!player.isEmpty()) {
            this.player.pause();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#isPlaying()
     */
    @Override
    public boolean isPlaying() {
        return this.player.isPlaying();
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#stop()
     */
    @Override
    public void stop() {
        this.player.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#next()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#previous()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#newPlaylist(java.lang.String)
     */
    @Override
    public void newPlaylist(String name) {
        Playlist newPlaylist = this.manager.createNewPlaylist(name);
        this.playlists.add(newPlaylist);
        this.songs.put(newPlaylist, FXCollections.observableArrayList());
        try {
            PlaylistController.savePlaylistToFile(newPlaylist, name);
        } catch (IOException e) {
            System.out.println("Unable to create playlist " + name);
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwave.controller.PlayerController#addSongToPlaylist(org.jwave.model.
     * player.Song, org.jwave.model.playlist.Playlist)
     */
    @Override
    public void addSongToPlaylist(Song song, Playlist playlist) throws IOException {
        playlist.addSong(song);
        PlaylistController.savePlaylistToFile(playlist, playlist.getName());
        songs.get(playlist).add(song);
    }

    /**
     * 
     * @param song
     * @param playlist
     */
    @Override
    public void removeSongFromPlaylist(Song song, Playlist playlist) throws IOException {
        //Could have been way more easy with more support from the model
        if (playlist.equals(manager.getDefaultPlaylist())) {
            manager.getAvailablePlaylists().forEach(p -> {
                p.getPlaylistContent().forEach(s->{
                    if(s.getName().equals(song.getName())){
                        p.removeFromPlaylist(s.getSongID());
                    }
                });
                try {
                    if (p.equals(manager.getDefaultPlaylist())) {
                        PlaylistController.saveDefaultPlaylistToFile(p, p.getName());
                    } else {
                        PlaylistController.savePlaylistToFile(p, p.getName());
                    }
                } catch (Exception x) {
                    System.out.println("Unable to save playlist " + p.getName());
                }
            });
        } else {
            // playlist.removeFromPlaylist(song.getSongID());
            playlist.getPlaylistContent().forEach(s -> {
                if (song.getName().equals(s.getName())) {
                    playlist.removeFromPlaylist(s.getSongID());
                }
            });
            PlaylistController.savePlaylistToFile(playlist, playlist.getName());
        }   
        //Had to reinitialize because modify them would have implied a ConcurrentOperationException
        playlists = FXCollections.observableArrayList(this.manager.getAvailablePlaylists());
        songs = new HashMap<>();
        manager.getAvailablePlaylists().forEach(e -> {
            songs.put(e, FXCollections.observableArrayList(e.getPlaylistContent()));
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwave.controller.PlayerController#selectSong(org.jwave.model.player.
     * Song)
     */
    @Override
    public void selectSong(Song song) {
        this.player.setPlayer(this.manager.selectSongFromPlayingQueue(song.getSongID()));
        this.player.play();
    }

    /*
     * * Keeps the gui slider updated with the song playing.
     */
    public void updatePosition(Integer ms) {
        uis.forEach(e -> e.updatePosition(ms, player.getLength()));
    }

    /**
     * Moves through the song.
     */
    @Override
    public void moveToMoment(Double percentage) {
        if (!this.player.isEmpty())
            player.cue((int) ((percentage * player.getLength()) / 10000));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#setVolume(java.lang.Integer)
     */
    public void setVolume(Integer amount) {
        this.player.setVolume(amount);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#getObservablePlaylists()
     */
    @Override
    public ObservableList<Playlist> getObservablePlaylists() {
        return this.playlists;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwave.controller.PlayerController#getObservablePlaylistContent(org.
     * jwave.model.playlist.Playlist)
     */
    @Override
    public ObservableList<Song> getObservablePlaylistContent(Playlist playlist) {
        return songs.get(playlist);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#terminate()
     */
    @Override
    public void terminate() {
        this.player.releasePlayerResources();
        // this.agent.KILL
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwave.controller.UpdatableController#updateReproductionInfo(org.jwave
     * .model.player.Song)
     */
    @Override
    public void updateReproductionInfo(Song song) {
        uis.forEach(e -> e.updateReproductionInfo(song));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwave.controller.PlayerController#setMode(org.jwave.model.playlist.
     * PlayMode)
     */
    @Override
    public void setMode(PlayMode mode) {
        manager.setPlayMode(mode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwave.controller.PlayerController#attachUI(org.jwave.view.UI)
     */
    @Override
    public void attachUI(UI UI) {
        uis.add(UI);

    }

}
