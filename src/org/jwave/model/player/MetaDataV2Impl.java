package org.jwave.model.player;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

public class MetaDataV2Impl implements MetaDataV2 {

    private Mp3File song;
    private Optional<ID3v1> id3v1Tag;
    private Optional<ID3v2> id3v2Tag;
    private Map<MetaData, String> datas;
    
    public MetaDataV2Impl(final String absolutePath) throws UnsupportedTagException, InvalidDataException, 
    IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        this.song = new Mp3File(absolutePath);
        this.datas = new EnumMap<>(MetaData.class);
        this.fillWithTags();
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
        
        if (this.id3v1Tag.isPresent()) {
            for (MetaData d : l) {
                if (d.getName().equals("ID3v1")) {
                    value = (String) ID3v1.class.getMethod("get" + d.getName()).invoke(this.id3v1Tag.get());
                } else {
                    value = "Not available";
                }
                this.datas.put(d, value);
            }
            return;
        }
        if (this.id3v2Tag.isPresent()) {
            for (MetaData d : l) {
                if (d.getName().equals("ID3v2")) {
                    value = (String) ID3v1.class.getMethod("get" + d.getName()).invoke(this.id3v2Tag.get());
                } else {
                    value = "Not available";
                }
                this.datas.put(d, value);
            }
            return;
        }
        for (MetaData d : l) {
            this.datas.put(d, "Not available");
        }
    }
}
