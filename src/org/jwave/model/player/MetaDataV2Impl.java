package org.jwave.model.player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * This class is an implementation of MetaDataV2.
 *
 */
public class MetaDataV2Impl implements MetaDataV2 {

    private Mp3File song;
    private ID3v1 id3v1Tag;
    private ID3v2 id3v2Tag;
    private Map<MetaData, String> datas;
    private Optional<RandomAccessFile> albumImage;
    
    /**
     * Creates a new instance of the MetaDataV2Impl.
     * 
     * @param absolutePath
     *          path of the file that has to 
     */
    public MetaDataV2Impl(final String absolutePath)  {
        this.datas = new EnumMap<>(MetaData.class);
        try {
            this.song = new Mp3File(absolutePath);
            this.fillWithTags();
        } catch (UnsupportedTagException | InvalidDataException | IOException | IllegalAccessException 
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            this.fillWithEmptyValues();
        }
        this.loadAlbumArtwork();
//        System.out.println(this.datas.entrySet());
    }

    @Override
    public String retrieve(final MetaData metaDataValue) {
        return this.datas.get(metaDataValue);
    }

    @Override
    public void setData(final MetaData metaDataValue, final String newValue) {
        // TODO Auto-generated method stub 
    }
   
    private void fillWithTags() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        final List<MetaData> l = Arrays.asList(MetaData.values());
        String value;
        if (this.song.hasId3v2Tag()) {
            this.id3v2Tag = this.song.getId3v2Tag();
            for (MetaData d : l) {
                if (d.getTagType().equals("ID3v2")) {
                    Method m = ID3v2.class.getMethod("get" + d.getName());
                    try {
                        value = (String) m.invoke(this.id3v2Tag);
                        this.datas.put(d, value);
                    } catch (ClassCastException ce) {
                        value = m.invoke(this.id3v2Tag).toString();
                        this.datas.put(d, value);
                    }
                }
            }
        }
        if (this.song.hasId3v1Tag()) {
            this.id3v1Tag = this.song.getId3v1Tag();
            for (MetaData d : l) {
                if (d.getTagType().equals("ID3v1")) {
                    Method m = ID3v1.class.getMethod("get" + d.getName());
                    try {
                        value = (String) m.invoke(this.id3v1Tag);
                        this.datas.put(d, value);
                    } catch (ClassCastException ce) {
                        value = m.invoke(this.id3v1Tag).toString();
                        this.datas.put(d, value);
                    }
                }
            }
            return;
        }
        //case no data is available
        this.fillWithEmptyValues();
    }
    
    private void fillWithEmptyValues() {
        final List<MetaData> l = Arrays.asList(MetaData.values());
        for (MetaData d : l) {
            this.datas.put(d, "Not available");
        }
    }
    
    private void loadAlbumArtwork() {
        //code inspired by
        //https://github.com/mpatric/mp3agic-examples/blob/master/src/main/java/com/mpatric/mp3agic/example/Example.java
        if (this.song.hasId3v2Tag()) {
            final byte[] imageData = this.id3v2Tag.getAlbumImage();
            if (imageData != null) {
//                final String mimeType = this.id3v2Tag.getAlbumImageMimeType();
//                System.out.println("Mime type: " + mimeType);
                // Write image to file - can determine appropriate file extension from the mime type
                try {
                    this.albumImage = Optional.of(new RandomAccessFile("album-artwork", "rw"));
                    this.albumImage.get().write(imageData);
                    this.albumImage.get().close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
            }
        }
    }

    @Override
    public Optional<RandomAccessFile> getAlbumArtwork() {
        return this.albumImage;
    }
}
