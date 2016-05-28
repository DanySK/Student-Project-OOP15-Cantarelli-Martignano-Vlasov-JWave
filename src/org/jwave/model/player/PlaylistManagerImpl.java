package org.jwave.model.player;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jwave.model.playlist.strategies.PlaylistNavigator;
import org.jwave.model.playlist.strategies.PlaylistNavigatorFactory;

/**
 * This is an implementation of {@link PlaylistManager}.
 */
public class PlaylistManagerImpl implements PlaylistManager {
    
    private Set<Playlist> availablePlaylists;   
    private Playlist defaultQueue;
    private Playlist loadedPlaylist;
    private Optional<Integer> currentIndexLoaded;
    private PlaylistNavigator navigator;
    private PlayMode playMode;
    private PlaylistNavigatorFactory navFactory;
    
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
        this.currentIndexLoaded = Optional.empty();
        this.defaultQueue = newDefaultQueue;
        this.loadedPlaylist = this.defaultQueue;
        this.playMode = PlayMode.NO_LOOP;
        this.navigator = this.navFactory.createNavigator(this.playMode, this.loadedPlaylist.getDimension(), 0);
        this.loadedPlaylist.addEObserver(this.navigator);
    }

    @Override
    public void addAudioFile(final File audioFile) {
        this.defaultQueue.addSong(new SongImpl(audioFile));
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
    public void deletePlaylist(final Playlist playlist) throws IllegalArgumentException {
        this.availablePlaylists.remove(playlist);
    }

    @Override
    public void reset() {
        if (!this.defaultQueue.isEmpty()) {
            this.defaultQueue.clear();
        }   
        this.setQueue(this.defaultQueue);
    }
    
    @Override
    public Playlist selectPlaylist(final String name) {
        return this.availablePlaylists.stream()
                .filter(p -> p.getName().equals(name))
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
    public Optional<Integer> getCurrentLoadedIndex() {
        return this.currentIndexLoaded;
    }

    @Override
    public Playlist getPlayingQueue() {
        return this.loadedPlaylist;
    }

    @Override
    public Playlist getDefaultQueue() {
        return this.defaultQueue;
    }
    
    @Override
    public Collection<Playlist> getAvailablePlaylists() {
       return Collections.unmodifiableSet(this.availablePlaylists);
    }

    @Override
    public PlayMode getPlayMode() {
        return this.playMode;
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
    
    private void setNavigator(final PlayMode mode) {  
        final int dimension = this.loadedPlaylist.getDimension();
        int index = 0; 
        if (this.currentIndexLoaded.isPresent()) {
            index = this.getCurrentLoadedIndex().get();
        }
        this.navigator = this.navFactory.createNavigator(mode, dimension, index);
    }

    @Override
    public void setAvailablePlaylists(final Collection<? extends Playlist> playlists) {
        this.availablePlaylists = new HashSet<>();
        this.availablePlaylists.addAll(playlists);
    }

    private boolean isNameAlreadyPresent(final String name) {
        return this.availablePlaylists.stream().anyMatch(p -> p.getName().equals(name));
    }

    @Override
    public Song next() {
        final int nextIndex = this.navigator.next();
        this.navigator.setCurrentIndex(nextIndex);
        return this.loadedPlaylist.selectSong(nextIndex);
    }

    @Override
    public Song prev() {
        final int prevIndex = this.navigator.prev();
        this.navigator.setCurrentIndex(prevIndex);
        return this.loadedPlaylist.selectSong(prevIndex);
    }
}
