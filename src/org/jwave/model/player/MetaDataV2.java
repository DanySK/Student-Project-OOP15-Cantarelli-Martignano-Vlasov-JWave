package org.jwave.model.player;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.mpatric.mp3agic.NotSupportedException;

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
    Optional<InputStream> getAlbumArtwork();
    
    /**
     * Sets a value for a meta data.
     * 
     * @param metaDataValue
     *          the new MetaDataValue
     *          
     * @param newValue
     *          the new value to be set.         
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    void setData(MetaData metaDataValue, String newValue) throws IllegalAccessException, IllegalArgumentException, 
    InvocationTargetException, NoSuchMethodException, SecurityException;
    
    /**
     * This method write effectively the new metaData into the file.
     * @throws IOException 
     * @throws NotSupportedException 
     */
    void overWriteOriginalFile() throws NotSupportedException, IOException; 
}
