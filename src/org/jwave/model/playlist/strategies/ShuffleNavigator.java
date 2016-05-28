package org.jwave.model.playlist.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * This is an implementation of PlaylistSurfer that follows the shuffle {@link}PlayMode policy.
 *
 */
public final class ShuffleNavigator extends AbstractPlaylistNavigator {
    
    private final Random seed;
    private final List<Integer> shuffledList;
    
    /**
     * Creates a new instance of ShuffleNavigator.
     * 
     * @param playlistDimension
     *          the initial playlist dimension.
     *          
     * @param currentIndex
     *          the index the navigator has to start with.          
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
    
    private void shuffle() {
        final int dim = this.getPlaylistDimension();
        final List<Integer> tempShuffled = new ArrayList<>();
        final Set<Integer> indexCache = new HashSet<>();
        int index;
        for (int i = 0; i < dim; i++) {
            do {
                index = this.seed.nextInt(dim);
            } while(indexCache.contains(index));
            indexCache.add(index);
            tempShuffled.add(index);
        }
        this.shuffledList.addAll(tempShuffled);
    }
}
