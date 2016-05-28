package org.jwave.model.player;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jwave.model.EObserver;

/**
 * This is an implementation of {@link}Playlist that can be serialized.
 *
 */
public class PlaylistImpl implements Playlist, Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4440054649095302226L;
    
    private Set<EObserver<? super Optional<Integer>, ? super Optional<Integer>>> set;
    
    private List<Song> list;
    private String playlistName;
    private transient Optional<Song> currentSelected;
    
    /**
     * Creates a new empty playlist.
     * 
     * @param name
     *          the name of the playlist.
     */
    public PlaylistImpl(final String name) {
        this.playlistName = name;
        this.list = new LinkedList<>();
        this.currentSelected = Optional.empty();
        this.set = new HashSet<>();
    }
    
    @Override
    public void addSong(final Song newSong) {
        this.list.add(newSong);
        this.notifyEObservers(Optional.of(this.getDimension()));
    }

    @Override
    public void moveSongToPosition(final int songToMoveIndex, final int position) throws IllegalArgumentException {
        if (position > this.list.size() || position < 0) {
            throw new IllegalArgumentException("Position is out of playlist borders");
        }
        final Song tmp = this.list.get(songToMoveIndex);
        final Song tmpTwo = this.list.get(position);
        this.list.remove(songToMoveIndex);
        this.list.remove(position);
        this.list.add(position, tmp);
        this.list.add(songToMoveIndex, tmpTwo);
    }

    @Override
    public void removeFromPlaylist(final Song songToBeRemoved) {
        this.checkSongPresence(songToBeRemoved);
        this.list.remove(songToBeRemoved);
        this.notifyEObservers(Optional.of(this.getDimension()));
    }

    @Override
    public Optional<Song> getCurrentSelected() {
        return this.currentSelected;
    }

    @Override
    public Song selectSong(final String name) throws IllegalArgumentException  {
        final Song out = this.list.stream()
                                .filter(s -> s.getName().equals(name))
                                .findFirst().get();
        this.setCurrentSong(out);
        return out;
    }

    @Override
    public synchronized Song selectSong(final int index) throws IllegalArgumentException {
        if (index > (this.getDimension() - 1)) {
            throw new IllegalArgumentException("Out of playlist borders.");
        }
        final Song out = this.list.get(index);
        this.setCurrentSong(out);
        return out;        
    }
    
    @Override
    public int getDimension() {
        return this.list.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }
    
    @Override
    public void printPlaylist() {
        this.list.forEach(s -> {
            System.out.println(this.list.indexOf(s) + " " + s.getName() + "\n");
        });
    }
    
    private void setCurrentSong(final Song newCurrentSong) {
        this.currentSelected = Optional.of(newCurrentSong);
    }

    @Override
    public void addEObserver(final EObserver<? super Optional<Integer>, ? super Optional<Integer>> obs) {
        this.set.add(obs);
    }

    @Override
    public void notifyEObservers(final Optional<Integer> arg1, final Optional<Integer> arg2) {
        this.set.forEach(obs -> obs.update(this, arg1, arg2));
    }

    @Override
    public int indexOf(final Song song) {
        this.checkSongPresence(song);
        return this.list.indexOf(song);
    }

    @Override
    public void notifyEObservers(final Optional<Integer> arg) {
        this.set.forEach(obs -> obs.update(this, arg));
    }

    @Override
    public String getName() {
        return this.playlistName;
    }

    @Override
    public void clearObservers() {
        if (!this.set.isEmpty()) {
            this.set = new HashSet<>();
        }   
    }

    @Override
    public void setName(final String newName) {
        this.playlistName = newName;
    }

    @Override
    public void clear() {
        this.list = new LinkedList<>();
    }
    
    private void checkSongPresence(final Song song) {
        if (!this.list.contains(song)) {
            throw new IllegalArgumentException("Song not found");
        }
    }
}
