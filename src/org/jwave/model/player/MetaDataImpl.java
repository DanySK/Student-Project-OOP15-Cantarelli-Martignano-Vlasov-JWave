package org.jwave.model.player;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import org.jwave.controller.player.FileSystemHandler;

/**
 * This class contains metadata of a file.
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
    
    private Minim minim;
    private AudioPlayer metaDataLoader;
    
    private Map<MData, String> data;
   
//    private Optional<String> album;
//    private Optional<String> author;
//    private Optional<String> comment;
//    private Optional<String> composer;
//    private Optional<String> copyright;
//    private Optional<String> date;
//    private Optional<String> disc;
//    private Optional<String> encoded;
    
    //to be checked if deleting AudioPlayer after loading metadata is possible then enumType ecc.. is useless.
    public MetaDataImpl(final String absoluteFilePath) {
        this.minim = new Minim(FileSystemHandler.getFileSystemHandler());
        this.metaDataLoader = this.minim.loadFile(absoluteFilePath);
        final AudioMetaData tmpData = this.metaDataLoader.getMetaData();
        this.data = new EnumMap<>(MData.class);
        final List<Method> m = Arrays.asList(AudioMetaData.class.getMethods()); 
        final ListIterator<Method> mit = m.listIterator();
        this.data.keySet().forEach(k -> {
            if (mit.hasNext()) {
                try {
                    this.data.put(k, (String) mit.next().invoke(tmpData));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        });
    }
    
    @Override
    public String getAlbum() {
        return this.retrieve(MData.ALBUM);
    }

    @Override
    public String getAuthor() {
        return this.retrieve(MData.AUTHOR);
    }

    @Override
    public String getComment() {
        return this.retrieve(MData.COMMENT);
    }

    @Override
    public String getComposer() {
        return this.retrieve(MData.COMPOSER);
    }

    @Override
    public String getCopyright() {
        return this.retrieve(MData.COPYRIGHT);
    }

    @Override
    public String getDate() {
        return this.retrieve(MData.DATE);
    }

    @Override
    public String getDisc() {
        return this.retrieve(MData.DISC);
    }

    @Override
    public String getEncoded() {
        return this.retrieve(MData.ENCODED);
    }

    @Override
    public String getFileName() {
        return this.retrieve(MData.FILENAME);
    }

    @Override
    public String getGenre() {
        return this.retrieve(MData.GENRE);
    }

    @Override
    public String getLength() {
        return this.retrieve(MData.LENGTH);
    }

    @Override
    public String getLyrics() {
        return this.retrieve(MData.LYRICS);
    }

    @Override
    public String getOrchestra() {
        return this.retrieve(MData.ORCHESTRA);
    }

    @Override
    public String getPublisher() {
        return this.retrieve(MData.PUBLISHER);
    }

    @Override
    public String getSampleFrameCount() {
        return this.retrieve(MData.SAMPLE_FRAME_COUNT);
    }

    @Override
    public String getTitle() {
        return this.retrieve(MData.TITLE);
    }

    @Override
    public String getTrack() {
        return this.retrieve(MData.TRACK);
    }
    
    private String retrieve(final MData key) {
        return this.data.get(key);
    }

}
