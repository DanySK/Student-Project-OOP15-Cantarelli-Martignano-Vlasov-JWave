package org.jwave.model.player;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
/**
 * An implementation of Song that can be serialized.
 *
 */
public class SongImpl extends File implements Song, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3265120631427974789L;
    //check if it is better to use the class Path for playlist portability in various OS;
//    private String absolutePath;
//    private String name;
    
    private MetaData metaData;
    
    /**
     * Creates a new song object.
     * 
     * @param absoluteName
     *          the path in the fileSystem
     */
    public SongImpl(final String absoluteName) {
        super(Paths.get(absoluteName).toString());
//        this.absolutePath = absoluteName;
//        this.name = absoluteName.substring(absoluteName.lastIndexOf(System.getProperty("file.separator")));
        this.metaData = new MetaDataImpl(this.getAbsolutePath());
    }
    
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getAbsolutePath() {
        return super.getAbsolutePath();
    }

    @Override
    public MetaData getMetaData() {
        return this.metaData;
    }
}
