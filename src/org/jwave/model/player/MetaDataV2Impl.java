package org.jwave.model.player;

import java.io.IOException;
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
    
    public MetaDataV2Impl(final String absolutePath) throws UnsupportedTagException, InvalidDataException, IOException {
        this.song = new Mp3File(absolutePath);
    }

    @Override
    public String retrieve(MetaData metaDataValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setData(MetaData metaDataValue, String newValue) {
        // TODO Auto-generated method stub
        
    }
   

}
