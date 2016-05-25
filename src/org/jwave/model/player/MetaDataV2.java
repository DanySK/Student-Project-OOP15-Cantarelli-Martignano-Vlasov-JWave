package org.jwave.model.player;

import java.io.RandomAccessFile;
import java.util.Optional;

/**
 * This interface models metadata in a more detailed way, letting user to set them.
 *
 */
public interface MetaDataV2 {
    
    /**
     * 
     * @param metaDataValue
     *          the type of meta data you want to retrieve.
     * 
     * @return
     *          a String specifing the meta data value.
     */
    String retrieve(MetaData metaDataValue);
    
    /**
     * 
     * @return
     *          An Optional containing the album artwork, if present.
     */
    Optional<RandomAccessFile> getAlbumArtwork();
    
    /**
     * Sets a value for a meta data.
     * 
     * @param metaDataValue
     *          the new MetaDataValue
     *          
     * @param newValue
     *          the new value to be set.         
     */
    void setData(MetaData metaDataValue, String newValue);
    
}
