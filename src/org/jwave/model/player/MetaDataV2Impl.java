package org.jwave.model.player;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * This class is an implementation of MetaDataV2.
 *
 */
public class MetaDataV2Impl implements MetaDataV2 {

    private static final String ID3V1 = "ID3v1";
    private static final String ID3V2 = "ID3v2";

    private Path filePath;
    private Mp3File song;
    private ID3v1 id3v1Tag;
    private ID3v2 id3v2Tag;
    private Map<MetaData, String> datas;
//    private Optional<RandomAccessFile> albumImage;
    
    /**
     * Creates a new instance of the MetaDataV2Impl.
     * 
     * @param absolutePath
     *          path of the file that has to 
     */
    public MetaDataV2Impl(final String absolutePath)  {
        this.filePath = Paths.get(absolutePath);
        this.datas = new EnumMap<>(MetaData.class);
//        this.albumImage = Optional.empty();
        try {
            this.song = new Mp3File(absolutePath);
            this.fillWithTags();
        } catch (UnsupportedTagException | InvalidDataException | IOException | IllegalAccessException 
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            this.fillWithEmptyValues();
        }
//        System.out.println(this.datas.entrySet());
//        System.out.println(this.song.getFilename());
//        System.out.println(this.filePath);
    }

    @Override
    public String retrieve(final MetaData metaDataValue) {
        return this.datas.get(metaDataValue);
    }

    @Override
    public void setData(final MetaData metaDataValue, final String newValue) throws IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        this.datas.put(metaDataValue, newValue);
        this.setTag(metaDataValue, newValue);
    }
    
    @Override
    public void overWriteOriginalFile() throws NotSupportedException, IOException {
        final String outPath = this.song.getFilename() + "~";
        this.song.save(outPath);
        Files.copy(Paths.get(outPath), this.filePath, StandardCopyOption.REPLACE_EXISTING);
        Files.delete(Paths.get(outPath));
    }
   
    private void fillWithTags() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (this.song.hasId3v1Tag()) {
            this.id3v1Tag = this.song.getId3v1Tag();
            this.fill(this.id3v1Tag, ID3V1);
        }
        if (this.song.hasId3v2Tag()) {
            this.id3v2Tag = this.song.getId3v2Tag();
            this.fill(this.id3v2Tag, ID3V2);
//            this.loadAlbumArtwork();
        }
        this.fillWithEmptyValues();
    }
    
    @Override
    public Optional<InputStream> getAlbumArtwork() {
        if (this.song.hasId3v2Tag()) {
            final byte[] imageData = this.id3v2Tag.getAlbumImage();
            if (imageData != null) {
                final Optional<InputStream> out = Optional.of(new ByteArrayInputStream(imageData));
                return out;
            }
        }
        return Optional.empty();
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
    
    private <T extends ID3v1> void setTag(final MetaData tag, final String newValue) throws IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        T metaDataVersion = null;
        if (tag.getTagType().equals(ID3V1)) {
            if (this.id3v1Tag == null) {
                this.id3v1Tag = new ID3v1Tag();
            }
            metaDataVersion = (T) this.id3v1Tag;        //TODO check if it can be done better.
        } else {
            if (this.id3v2Tag == null) {
                this.id3v2Tag = new ID3v24Tag();
            }
            metaDataVersion = (T) this.id3v2Tag;
        }
        try {
            int numberValue = Integer.parseInt(newValue);
            this.fillTag(metaDataVersion, tag.getName(), numberValue);
        }   catch (NumberFormatException ne) {
            this.fillTag(metaDataVersion, tag.getName(), newValue);
        }
    }

    private <T extends ID3v1> void fillTag(final T tag, final String methodName, final String newValue) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
            NoSuchMethodException, SecurityException {  
        tag.getClass().getMethod("set" + methodName, String.class).invoke(tag, newValue);
    }
    
    private <T extends ID3v1> void fillTag(final T tag, final String methodName, final int newValue) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, 
            SecurityException {  
        tag.getClass().getMethod("set" + methodName, Integer.class).invoke(tag, newValue);
    }
}
