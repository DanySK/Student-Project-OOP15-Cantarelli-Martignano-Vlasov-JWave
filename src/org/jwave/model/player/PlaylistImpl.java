package org.jwave.model.player;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class PlaylistImpl implements Playlist, Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4440054649095302226L;
    //to be checked if it is possible to avoid song duplication with a different implementation
//    private Set<Song> s = new HashSet<>();
    private List<Song> list;
    private Optional<Song> currentSelected;
    private ListIterator<Song> it;
    
    public PlaylistImpl() {
        this.list = new LinkedList<>();
        this.currentSelected = Optional.empty();
        this.it = this.list.listIterator();
    }
    
    @Override
    public void addSong(final Song newSong) {
        this.list.add(newSong);
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
    public void removeFromPlaylist(final Song... songNames) {
        for (Song s : songNames) {
            this.list.remove(s);
        }
    }

    @Override
    public Optional<Song> getCurrentSelected() {
        return this.currentSelected;
    }

    @Override
    public Song getNext() {
        if (!this.it.hasNext()) {
            throw new IllegalStateException();
        }
        this.currentSelected = Optional.of(this.it.next());
        return this.currentSelected.get();
    }

    @Override
    public Song getPrev() {
        if (!this.it.hasPrevious()) {
            throw new IllegalStateException();
        }
        this.currentSelected = Optional.of(this.it.previous());
        return this.currentSelected.get();
    }

    @Override
    public void printPlaylist() {
        this.list.forEach(s -> {
            System.out.println(this.list.indexOf(s) + " " + s.getName() + "\n");
        });
    }
}
