package org.jwave.model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is an implementation of PlaylistSurfer.
 *
 */
public final class ShuffleNavigator implements PlaylistNavigator {
    
    private List<Integer> shuffledList;
    private int playlistDimension;
    private Random seed;
    private Integer currentIndex;
    
    /**
     * Creates a new instance of the class.
     * 
     * @param playlistDimension
     */
    public ShuffleNavigator(final int playlistDimension) {
        this.seed = new Random();
        this.shuffledList = new ArrayList<>();
        this.setPlaylistDimension(playlistDimension);
        this.currentIndex = 0;
    }
    
    @Override
    public int next() {        
        if (this.currentIndex.equals(this.shuffledList.size() - 1)) {
            this.shuffle();
        }
        this.currentIndex++;
        return this.currentIndex;
    }

    //check if it can be implemented better.
    @Override
    public int prev() {
        if (this.currentIndex.equals(0)) {
            return 0;
        }
        this.currentIndex--;
        return this.currentIndex;
    }

    @Override
    public void setPlaylistDimension(final int newDimension) {
        this.playlistDimension = newDimension;
        this.shuffle();
    }
    
    //can be implemented better
    private void shuffle() {
        final List<Integer> tempShuffled = new ArrayList<>();
        int index;
        for (int i = 0; i < this.playlistDimension; i++) {
            do {
                index = this.seed.nextInt(this.playlistDimension);
            } while(this.shuffledList.contains(index));
            tempShuffled.add(index);
        }
        this.shuffledList.addAll(tempShuffled);
    }

}
