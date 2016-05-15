package org.jwave.model.player;

import java.io.File;
import java.io.Serializable;
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
    private MetaData metaData;
    
    /**
     * Creates a new song object.
     * 
     * @param audioFile
     *          the audio file that will become a song.
     */
    public SongImpl(final File audioFile) {
        this.decorated = audioFile;     
        this.metaData = new MetaDataImpl(this.decorated.getAbsolutePath());
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
    public MetaData getMetaData() {
        return this.metaData;
    }
}
