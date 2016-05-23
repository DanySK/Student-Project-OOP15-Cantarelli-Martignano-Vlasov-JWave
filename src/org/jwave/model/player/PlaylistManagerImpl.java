package org.jwave.model.player;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This is an implementation of {@link Playlist}.
 */
public class PlaylistManagerImpl implements PlaylistManager {
    
    private Set<Playlist> availablePlaylists;   
    private Playlist defaultQueue;
    private Playlist loadedPlaylist;
    private Optional<Integer> currentIndexLoaded;
    private PlaylistNavigator navigator;
    private PlayMode playMode;
    
    /**
     * Creates a new PlaylistManagerImpl.
     * 
     * @param newDefaultQueue
     *          the default playlist.
     */
    public PlaylistManagerImpl(final Playlist newDefaultQueue) {
        this.defaultQueue = newDefaultQueue;
        this.availablePlaylists = new HashSet<>();
        this.currentIndexLoaded = Optional.empty();
        this.navigator = new NoLoopNavigator(this.loadedPlaylist.getDimension(), 0);
        this.setQueue(this.defaultQueue);
        this.playMode = PlayMode.NO_LOOP;
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
       return this.availablePlaylists;
    }

    @Override
    public PlayMode getPlayMode() {
        return this.playMode;
    }

    @Override
    public void setPlayMode(final PlayMode newPlayMode) {
        this.playMode = newPlayMode;
    }
    
    @Override
    public void setQueue(final Playlist playlist) {
        this.loadedPlaylist.clearObservers();
        this.loadedPlaylist = playlist;
        this.loadedPlaylist.addEObserver(this.navigator);
    }
    
    private void setNavigator(final PlayMode mode) {    //TODO create a simple factory
        final int dimension = this.loadedPlaylist.getDimension();
        int index = 0; 
        if (this.currentIndexLoaded.isPresent()) {
            index = this.getCurrentLoadedIndex().get();
        }
       switch (mode) {
           case NO_LOOP:        
               this.navigator = new NoLoopNavigator(dimension, index);
               break;
           case LOOP_ONE:       
               this.navigator = new LoopOneNavigator(index);
               break;
           case LOOP_ALL:       
               this.navigator = new LoopAllNavigator(dimension, index);
               break;
           case SHUFFLE:        
               this.navigator = new ShuffleNavigator(dimension, index);
               break;
           default:             
               this.navigator = new NoLoopNavigator(dimension, index);
               break;
       }
    }
    
    private PlaylistNavigator getPlaylistNavigator() { 
        return this.navigator;
     }

    @Override
    public void setAvailablePlaylists(final Collection<? extends Playlist> playlists) {
        this.availablePlaylists = new HashSet<>();
        this.availablePlaylists.addAll(playlists);
    }

    private boolean isNameAlreadyPresent(final String name) {
        return this.availablePlaylists.stream().anyMatch(p -> p.getName().equals(name));
    }
}
