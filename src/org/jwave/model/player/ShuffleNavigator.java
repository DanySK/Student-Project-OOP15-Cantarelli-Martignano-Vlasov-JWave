package org.jwave.model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.jwave.controller.player.ESource;

/**
 * This is an implementation of PlaylistSurfer.
 *
 */
public final class ShuffleNavigator extends PlaylistNavigatorImpl {
    
    private Random seed;
    private List<Integer> shuffledList;
    
    /**
     * Creates a new instance of the class.
     * 
     * @param playlistDimension
     */
    public ShuffleNavigator(final int playlistDimension, final int currentIndex) {
        super(playlistDimension, currentIndex);
        this.seed = new Random();
        this.shuffledList = new ArrayList<>();
    }
    
    @Override
    public int next() {        
        if (this.getCurrentIndex().equals(this.shuffledList.size() - 1)) {
            this.shuffle();
        }
        this.incIndex();
        return this.getCurrentIndex();
    }

    //check if it can be implemented better.
    @Override
    public int prev() {
        if (this.getCurrentIndex().equals(0)) {
            return 0;
        }
        this.decIndex();
        return this.getCurrentIndex();
    }
    
    //can be implemented better
    //TODO check how to manage an openFile/openDir operation result while shuffling.
    private void shuffle() {
        final List<Integer> tempShuffled = new ArrayList<>();
        int index;
        for (int i = 0; i < this.getPlaylistDimension(); i++) {
            do {
                index = this.seed.nextInt(this.getPlaylistDimension());
            } while(this.shuffledList.contains(index));
            tempShuffled.add(index);
        }
        this.shuffledList.addAll(tempShuffled);
    }
}
