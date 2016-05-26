package org.jwave.model.player;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jwave.controller.player.Controller;

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

    private static final String ID3V1 = "ID3v1";
    private static final String ID3V2 = "ID3v2";

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
        this.albumImage = Optional.empty();
        try {
            this.song = new Mp3File(absolutePath);
            this.fillWithTags();
        } catch (UnsupportedTagException | InvalidDataException | IOException | IllegalAccessException 
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            this.fillWithEmptyValues();
        }
//        System.out.println(this.datas.entrySet());
    }

    @Override
    public String retrieve(final MetaData metaDataValue) {
        return this.datas.get(metaDataValue);
    }

    @Override
    public void setData(final MetaData metaDataValue, final String newValue) {
        this.datas.put(metaDataValue, newValue);
//        this.setTag();
    }
   
    private void fillWithTags() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (this.song.hasId3v1Tag()) {
            this.id3v1Tag = this.song.getId3v1Tag();
            this.fill(this.id3v1Tag, ID3V1);
        }
        if (this.song.hasId3v2Tag()) {
            this.id3v2Tag = this.song.getId3v2Tag();
            this.fill(this.id3v2Tag, ID3V2);
            this.loadAlbumArtwork();
        }
        this.fillWithEmptyValues();
    }

    private void loadAlbumArtwork() {
        //code inspired by
        //https://github.com/mpatric/mp3agic-examples/blob/master/src/main/java/com/mpatric/mp3agic/example/Example.java
        final byte[] imageData = this.id3v2Tag.getAlbumImage();
        if (imageData != null) {
//                final String mimeType = this.id3v2Tag.getAlbumImageMimeType();
//                System.out.println("Mime type: " + mimeType);
            // Write image to file - can determine appropriate file extension from the mime type
            try {
                final File tmp = new File(System.getProperty("user.home") + System.getProperty("file.separator") 
                + Controller.SAVE_DIR_NAME + System.getProperty("file.separator") + Controller.COVERART_DIR_NAME 
                +  System.getProperty("file.separator") + "arturo");    //TODO finish implementation.
                this.albumImage = Optional.of(new RandomAccessFile(tmp, "rw"));
                this.albumImage.get().write(imageData);
                this.albumImage.get().close(); //verify if this method causes impossibility to read the file.
            } catch (IOException e) {
                System.out.println("Catched IOException");
                this.albumImage = Optional.empty();     //TODO check if there is a better way to manage exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<RandomAccessFile> getAlbumArtwork() {
        return this.albumImage;
    }
    
    private <T extends ID3v1> void fill(final T tag, final String tagType) throws NoSuchMethodException, SecurityException, 
    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final List<MetaData> l = Arrays.asList(MetaData.values()).stream()
        .filter(t -> t.getTagType().equals(tagType))
        .collect(Collectors.toList());
        String value;
        for (MetaData d : l) {
            Method m = tag.getClass().getMethod("get" + d.getName());
            try {
                value = (String) m.invoke(tag);
                this.datas.put(d, value);
            } catch (ClassCastException ce) {
                value = m.invoke(tag).toString();
                this.datas.put(d, value);
            }
        }
    }
    
    private void fillWithEmptyValues() {
        final List<MetaData> l = Arrays.asList(MetaData.values()).stream()
                .filter(t -> !this.datas.containsKey(t) || this.datas.get(t) == null)
                .collect(Collectors.toList());
        for (MetaData d : l) {
            this.datas.put(d, "");
        }
    }
    
//  private void setTag(final String tagName, final String newValue) {
//  
//}
//
//private void setTag(final int newValue) {
//  
//}
}
