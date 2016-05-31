package org.jwave.model.player;

import java.util.UUID;

/**
 * An interface that models the concept of song.
 * A song is an audio file containing also metadata.
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
     *          the song object unique identifier.
     */
    UUID getSongID();
    
    /**
     * 
     * @return
     *          all the metadata contained in this song.
     */
    MetaDataManager getMetaData();
    
    /**
     * Refreshes metadata.
     */
    void refreshMetaData();
}
