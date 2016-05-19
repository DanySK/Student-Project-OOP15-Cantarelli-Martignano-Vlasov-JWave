package org.jwave.model.player;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.jwave.controller.player.FileSystemHandler;

import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

/**
 * This class is an implementation of {@link}MetaData.
 *
 */
public class MetaDataImpl implements MetaData, Serializable {
   
    /**
     * 
     */
    private static final long serialVersionUID = -854965524145699984L;

    private enum MData {
        ALBUM, AUTHOR, COMMENT, COMPOSER, COPYRIGHT, DATE, DISC, ENCODED, FILENAME, GENRE, LENGTH, LYRICS, 
        ORCHESTRA, PUBLISHER, SAMPLE_FRAME_COUNT, TITLE, TRACK;
    }
    
    private transient Minim minim;
    private transient AudioPlayer metaDataLoader;
    
    private Map<String, String> data;
    
    //TODO check if deleting AudioPlayer after loading metadata is possible then enumType ecc.. is useless.
    
    /**
     * Creates a new MetaDataImpl.
     * 
     * @param absoluteFilePath
     *          the path of the file containing metadata.
     */
    public MetaDataImpl(final String absoluteFilePath) {
        this.minim = new Minim(FileSystemHandler.getFileSystemHandler());
        this.metaDataLoader = this.minim.loadFile(absoluteFilePath);
        final AudioMetaData tmpData = this.metaDataLoader.getMetaData();
        this.data = new HashMap<>();
        final List<Method> methods = Arrays.asList(AudioMetaData.class.getMethods()); 
        //loading metadata
        methods.forEach(m -> {
            String name = m.getName();
            String value;
            try {
                value = (String) m.invoke(tmpData);
                this.data.put(name, value);
            } catch (Exception e) {
                System.out.println("Problem while loading metadata");
            }
        });
        this.metaDataLoader.close();
        this.minim.stop();
    }
    
    @Override
    public String getAlbum() {
        return this.retrieve("album");
    }

    @Override
    public String getAuthor() {
        return this.retrieve("author");
    }

    @Override
    public String getComment() {
        return this.retrieve("comment");
    }

    @Override
    public String getComposer() {
        return this.retrieve("composer");
    }

    @Override
    public String getCopyright() {
        return this.retrieve("copyright");
    }

    @Override
    public String getDate() {
        return this.retrieve("date");
    }

    @Override
    public String getDisc() {
        return this.retrieve("disc");
    }

    @Override
    public String getEncoded() {
        return this.retrieve("encoded");
    }

    @Override
    public String getFileName() {
        return this.retrieve("fileName");
    }

    @Override
    public String getGenre() {
        return this.retrieve("genre");
    }

    @Override
    public String getLength() {
        return this.retrieve("length");
    }

    @Override
    public String getLyrics() {
        return this.retrieve("lyrics");
    }

    @Override
    public String getOrchestra() {
        return this.retrieve("orchestra");
    }

    @Override
    public String getPublisher() {
        return this.retrieve("publisher");
    }

    @Override
    public String getSampleFrameCount() {
        return this.retrieve("sampleFrameCount");
    }

    @Override
    public String getTitle() {
        return this.retrieve("title");
    }

    @Override
    public String getTrack() {
        return this.retrieve("track");
    }
    
    private String retrieve(final String key) {
        return this.data.get(key);
    }
}
