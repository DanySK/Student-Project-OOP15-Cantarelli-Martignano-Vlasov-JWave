package org.jwave.model.playlist;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jwave.model.EObserver;
import org.jwave.model.player.Song;

/**
 * This is an implementation of {@link Playlist} that can be serialized.
 *
 */
public class PlaylistImpl implements Playlist, Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4440054649095302226L;
    
    private transient Set<EObserver<? super Integer>> set;
    
    private Map<UUID, Song> map;
    private List<UUID> idList;
    private final UUID playlistID;
    private String playlistName;
    
    /**
     * Creates a new empty playlist.
     * 
     * @param name
     *          the name of the playlist.
     */
    public PlaylistImpl(final String name) {
        this.playlistName = name;
        this.playlistID = UUID.randomUUID();
        this.idList = new LinkedList<>();
        this.map = new HashMap<>();
        this.set = new HashSet<>();
    }
    
    @Override
    public void addSong(final Song newSong) {
        final UUID id = newSong.getSongID();
        if (!this.map.containsKey(id)) {
            this.map.put(id, newSong);
        }
        this.idList.add(id);
        this.notifyEObservers(this.getDimension());
    }

    @Override
    public void moveSongToPosition(final int songToMoveID, final int position) throws IllegalArgumentException {
        if (position > this.idList.size() || position < 0) {
            throw new IllegalArgumentException("Position is out of playlist borders");
        }
        final UUID tmp = this.idList.get(songToMoveID);
        final UUID tmpTwo = this.idList.get(position);
        this.idList.remove(songToMoveID);
        this.idList.remove(position);
        this.idList.add(position, tmp);
        this.idList.add(songToMoveID, tmpTwo);
    }

    @Override
    public void removeFromPlaylist(final UUID songID) {
        this.checkSongPresence(songID);
        this.idList.remove(songID);
        this.map.remove(songID);
        this.notifyEObservers(this.getDimension());
    }
    
    @Override
    public int indexOf(final UUID songID) {
        this.checkSongPresence(songID);
        return this.idList.indexOf(songID);
    }

    @Override
    public int getDimension() {
        return this.idList.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.idList.isEmpty();
    }
    
    @Override
    public Song getSong(final UUID songID) {
        return this.map.get(songID);
    }
    
    @Override
    public Song getSongAtIndex(final int index) throws IllegalArgumentException {
        if (index > (this.idList.size() - 1) || index < 0) {  
            throw new IllegalArgumentException("Out of playlsit borders");
        }
        return this.map.get(this.idList.get(index));
    }
    
    @Override
    public List<Song> getPlaylistContent() {
       final List<Song> out = new LinkedList<>();
       this.idList.stream()
       .forEachOrdered(id -> out.add(this.map.get(id)));
       return out;
    }
    
    @Override
    public String getName() {
        return this.playlistName;
    }
    
    @Override
    public UUID getPlaylistID() {
        return this.playlistID;
    }

    @Override
    public void clear() {
        this.idList = new LinkedList<>();
    }

    @Override
    public void setName(final String newName) {
        this.playlistName = newName;
    }
    
    @Override
    public void refreshContent() {
        this.map.values().forEach(s -> s.refreshMetaData());
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    @Override
    public void addEObserver(final EObserver<? super Integer> obs) {
        this.set.add(obs);
    }
    
    @Override
    public void notifyEObservers(final Integer arg) {
        this.set.forEach(obs -> obs.update(this, arg));
    }
    
    @Override
    public void clearObservers() {
        this.set = new HashSet<>();
    }
    
    private void checkSongPresence(final UUID id) {
        if (!this.idList.contains(id)) {
            throw new IllegalArgumentException("Song not found");
        }
    }
}
