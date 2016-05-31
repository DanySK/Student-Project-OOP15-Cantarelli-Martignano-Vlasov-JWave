package org.jwave.model.player;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.mpatric.mp3agic.NotSupportedException;

/**
 * A meta data retriever manages metadata contained in an audio file.
 *
 */
public interface MetaDataManager {
    
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
     *                 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    void setData(MetaData metaDataValue, String newValue) throws IllegalAccessException, IllegalArgumentException, 
    InvocationTargetException, NoSuchMethodException, SecurityException;
    
    /**
     * This method effectively writes the new metaData into the file.
     * 
     * @throws IOException 
     * @throws NotSupportedException 
     */
    void writeMetaDataToFile() throws NotSupportedException, IOException; 
}
