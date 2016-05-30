package org.jwave.model.playlist.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * This is an implementation of PlaylistSurfer that follows the shuffle {@link}PlayMode policy.
 *
 */
public final class ShuffleNavigator extends AbstractPlaylistNavigator {
    
    private final Random seed;
    private final List<Integer> shuffledList;
    private Optional<Integer> shuffledIndex;
    
    /**
     * Creates a new instance of ShuffleNavigator.
     * 
     * @param playlistDimension
     *          the initial playlist dimension.
     *                    
     */
    public ShuffleNavigator(final int playlistDimension) {
        super(playlistDimension, Optional.empty());
        this.seed = new Random();
        this.shuffledList = new ArrayList<>();
        this.shuffledIndex = Optional.empty();
        this.shuffle();
    }
    
    @Override
    public Optional<Integer> next() {        
       if (this.shuffledIndex.isPresent()) {
           if (this.shuffledIndex.get() >= (this.shuffledList.size() - 1)) {
               this.shuffle();
           } 
           this.shuffledIndex = Optional.of(this.shuffledIndex.get() + 1);
       } else {
           if (this.getPlaylistDimension() > 0) {
               this.shuffle();
               this.shuffledIndex = Optional.of(this.shuffledList.get(0));
           }
       }
       return this.shuffledIndex;
    }

    @Override
    public Optional<Integer> prev() {
       if (this.shuffledIndex.equals(Optional.empty())) {
           return this.shuffledIndex;
       } else {
           if (this.shuffledIndex.get().equals(0)) {
               return Optional.empty();
           }
           this.shuffledIndex = Optional.of(this.shuffledIndex.get() - 1);
           return this.shuffledIndex;
       }
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
