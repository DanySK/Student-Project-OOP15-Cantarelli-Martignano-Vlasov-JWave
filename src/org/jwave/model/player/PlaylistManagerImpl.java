package org.jwave.model.player;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.jwave.controller.player.FileSystemHandler;
import org.jwave.model.playlist.strategies.PlaylistNavigator;
import org.jwave.model.playlist.strategies.PlaylistNavigatorFactory;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

/**
 * This is an implementation of {@link PlaylistManager}.
 */
public class PlaylistManagerImpl implements PlaylistManager {
    
    private Set<Playlist> availablePlaylists;   
    private Playlist defaultQueue;
    private Playlist loadedPlaylist;
    private Optional<Integer> currentIndex;
    private PlaylistNavigator navigator;
    private PlayMode playMode;
    private final PlaylistNavigatorFactory navFactory;
    
    /**
     * Creates a new PlaylistManagerImpl.
     * 
     * @param newDefaultQueue
     *          the default playlist.
     */
    public PlaylistManagerImpl(final Playlist newDefaultQueue) {
        this.navFactory = new PlaylistNavigatorFactory();
        this.defaultQueue = newDefaultQueue;
        this.availablePlaylists = new HashSet<>();
        this.currentIndex = Optional.empty();
        this.loadedPlaylist = this.defaultQueue;
        this.playMode = PlayMode.NO_LOOP;
        this.navigator = this.navFactory.createNavigator(this.playMode, this.loadedPlaylist.getDimension(), Optional.empty());
        this.loadedPlaylist.addEObserver(this.navigator);
    }

    @Override
    public Song addAudioFile(final File audioFile) throws Exception {
        final Song out = new SongImpl(audioFile);
        this.defaultQueue.addSong(out);
        return out;
    }  
    
    @Override
    public Playlist createNewPlaylist(final String name) {
        if (this.isNameAlreadyPresent(name)) {
            throw new IllegalArgumentException("Name already present");
        }
        final Playlist out = new PlaylistImpl(name);
        this.availablePlaylists.add(out);
        return out;
    }


    @Override
    public void deletePlaylist(final Playlist playlist) {
        this.availablePlaylists.remove(playlist);
    }
    
    @Override
    public Optional<Song> next() {
        if (this.loadedPlaylist.isEmpty()) {
            throw new IllegalStateException();
        }
        final Optional<Integer> nextIndex = this.navigator.next();
        if (nextIndex.isPresent()) {
            return Optional.of(this.loadedPlaylist.getSongAtIndex(nextIndex.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Song> prev() {
        if (this.loadedPlaylist.isEmpty()) {
            throw new IllegalStateException();
        }
        final Optional<Integer> prevIndex = this.navigator.prev();
        if (prevIndex.isPresent()) {
            return Optional.of(this.loadedPlaylist.getSongAtIndex(prevIndex.get()));
        }
        return Optional.empty();
    }
    
    @Override
    public Playlist selectPlaylist(final UUID playlistID) {
        return this.availablePlaylists.stream()
                .filter(p -> p.getName().equals(playlistID))
                .findAny()
                .get();
    }

    @Override
    public void renamePlaylist(final Playlist playlist, final String newName) throws IllegalArgumentException {
        if (this.isNameAlreadyPresent(newName)) {
            throw new IllegalArgumentException("Cannot have two playlists with the same name.");
        }
        playlist.setName(newName);
    }
    
    @Override
    public void reset() {
        if (!this.defaultQueue.isEmpty()) {
            this.defaultQueue.clear();
        }   
        this.setQueue(this.defaultQueue);
    }
   
    @Override
    public Playlist getPlayingQueue() {
        return this.loadedPlaylist;
    }

    @Override
    public Playlist getDefaultPlaylist() {
        return this.defaultQueue;
    }
    
    @Override
    public Collection<Playlist> getAvailablePlaylists() {
        final Set<Playlist> out = new HashSet<>(this.availablePlaylists);
        out.add(this.defaultQueue);
        return Collections.unmodifiableSet(out);
    }

    @Override
    public PlayMode getPlayMode() {
        return this.playMode;
    }

    @Override
    public void setAvailablePlaylists(final Collection<? extends Playlist> playlists) {
        this.availablePlaylists = new HashSet<>();
        this.availablePlaylists.addAll(playlists);
    }

    @Override
    public void setPlayMode(final PlayMode newPlayMode) {
        this.playMode = newPlayMode;
        this.setNavigator(newPlayMode);
    }
    
    @Override
    public void setQueue(final Playlist playlist) {
        if (this.loadedPlaylist != null) {
            this.loadedPlaylist.clearObservers();
        }
        this.loadedPlaylist = playlist;
        this.loadedPlaylist.addEObserver(this.navigator);
    }
    
    private boolean isNameAlreadyPresent(final String name) {
        return this.availablePlaylists.stream().anyMatch(p -> p.getName().equals(name));
    }
    
    private void setNavigator(final PlayMode mode) {  
        final int dimension = this.loadedPlaylist.getDimension();
        this.navigator = this.navFactory.createNavigator(mode, dimension, this.currentIndex);
        this.loadedPlaylist.clearObservers();
        this.loadedPlaylist.addEObserver(this.navigator);
    }

    @Override
    public Song selectSongFromPlayingQueue(final UUID songID) throws IllegalArgumentException {
        final Song out = this.defaultQueue.getSong(songID);
        this.navigator.setCurrentIndex(Optional.of(this.loadedPlaylist.indexOf(songID)));
        return out;
    }

    @Override
    public Song selectSongFromPlayingQueueAtIndex(final int index) throws IllegalArgumentException {
        final Song out = this.defaultQueue.getSongAtIndex(index);
        this.navigator.setCurrentIndex(Optional.of(index));
        return out;
    }

    

   
}
