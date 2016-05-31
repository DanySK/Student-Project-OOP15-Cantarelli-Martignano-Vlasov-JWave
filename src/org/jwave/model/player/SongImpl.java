package org.jwave.model.player;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
    
    private final File decorated;
    private final UUID songID;
    private transient MetaDataRetriever metaData;
    
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
    public SongImpl(final File audioFile) {
        this.decorated = audioFile;   
        this.songID = UUID.randomUUID();
        final Path pathForMetaData = Paths.get(this.decorated.getAbsolutePath());
        this.metaData = new MetaDataRetrieverImpl(pathForMetaData);  
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
    public MetaDataRetriever getMetaData() {
        return this.metaData;
    }

    @Override
    public UUID getSongID() {   //check if it is better to return a defensive copy
        return this.songID;
    }

    @Override
    public void refreshMetaData() {
        final Path pathForMetaData = Paths.get(this.decorated.getAbsolutePath());
        this.metaData = new MetaDataRetrieverImpl(pathForMetaData); 
    }
}
