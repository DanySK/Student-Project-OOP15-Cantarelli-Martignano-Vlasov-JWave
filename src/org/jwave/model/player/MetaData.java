package org.jwave.model.player;

/**
 * An enum that models the concept of MetaData.
 *  
 */
public enum MetaData {

    //ID3v1
    ALBUM("Album", "ID3v1"),
    ARTIST("Artist", "ID3v1"),
    COMMENT("Comment", "ID3v1"),
    GENRE("Genre", "ID3v1"),
    GENRE_DESCRIPTION("GenreDescription", "ID3v1"),
    TITLE("Title", "ID3v1"),
    TRACK("Track", "ID3v1"),
    VERSION("Version", "ID3v1"),
    YEAR("Year", "ID3v1"),
    
    //ID3v2
    ALBUM_ARTIST("AlbumArtist", "ID3v2"),
    ALBUM_IMAGE("AlbumImage", "ID3v2"),
    COMPOSER("Composer", "ID3v2"),
    COPYRIGHT("Copyright", "ID3v2"),
    DATA_LENGTH("DataLength", "ID3v2"),
    ENCODER("Encoder", "ID3v2"),
    LENGTH("Length", "ID3v2"),
    ORIGINAL_ARTIST("OriginalArtist", "ID3v2"),
    PUBLISHER("Publisher", "ID3v2");
    
    private final String name;
    private final String tagType;
//    private final String getter;
//    private final String setter;
    
    private MetaData(final String name, final String tagType) {
        this.name = name;
        this.tagType = tagType;
//        this.getter = "get" + this.name();
//        this.setter = "set" + this.name();
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getTagType() {
        return this.tagType;       
    }
}
