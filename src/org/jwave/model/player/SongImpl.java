package org.jwave.model.player;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
/**
 * An implementation of Song that can be serialized.
 *
 */
public class SongImpl implements Song, Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 9045721077357297256L;
    private File decorated;
    private MetaDataV1 metaData;
    private MetaDataV2 metaData2;
    
    /**
     * Creates a new song object.
     * 
     * @param audioFile
     *          the audio file that will become a song.
     * @throws IOException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws InvalidDataException 
     * @throws UnsupportedTagException 
     */
    public SongImpl(final File audioFile) throws UnsupportedTagException, InvalidDataException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
        this.decorated = audioFile;     
        this.metaData = new MetaDataImpl(this.decorated.getAbsolutePath());
        this.metaData2 = new MetaDataV2Impl(this.decorated.getAbsolutePath());
        
    }
    
    @Override
    public String getName() {
        return this.decorated.getName();
    }

    @Override
    public String getAbsolutePath() {
        return this.decorated.getAbsolutePath();
    }

    @Override
    public MetaDataV1 getMetaData() {
        return this.metaData;
    }

    @Override
    public MetaDataV2 getMetaDataV2() {
        return this.metaData2;
    }
    
//    private void setMetaData() {
//        if (!this.decorated.getName().endsWith(".mp3")) {
//            throw
//        }
//    }
}
