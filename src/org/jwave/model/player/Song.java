package org.jwave.model.player;

/**
 * An interface that models the concept of song.
 * A song is an audio file containing various information
 * stored in metadata.
 *
 */
public interface Song {

    /**
     * 
     * @return
     *          song name.
     */
    String getName();
    
    /**
     * 
     * @return
     *          the location of the song in the filesystem.
     */
    String getAbsolutePath();
    
    /**
     * 
     * @return
     *          all metadata available for the song.
     */
    MetaData getMetaData();
}
