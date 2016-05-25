package org.jwave.model.player;

/**
 * An enum that models the concept of MetaData.
 *  
 */
public enum MetaData {

    //ID3v1
    /**
     * Album of the song.
     */
    ALBUM("Album", "ID3v1"),
    
    /**
     * Artist of the song.
     */
    ARTIST("Artist", "ID3v1"),
    
    /**
     * Comment.
     */
    COMMENT("Comment", "ID3v1"),
    
    /**
     * Genre.
     */
    GENRE("Genre", "ID3v1"),
    
    /**
     * Genre description.
     */
    GENRE_DESCRIPTION("GenreDescription", "ID3v1"),
    
    /**
     * Song title.
     */
    TITLE("Title", "ID3v1"),
    
    /**
     * Track of the song.
     */
    TRACK("Track", "ID3v1"),
    
    /**
     * Version.
     */
    VERSION("Version", "ID3v1"),
    
    /**
     * Year of release.
     */
    YEAR("Year", "ID3v1"),
    
    //ID3v2
    /**
     * Album artist.
     */
    ALBUM_ARTIST("AlbumArtist", "ID3v2"),
    
    /**
     * Composer.
     */
    COMPOSER("Composer", "ID3v2"),
    
    /**
     * Copyright.
     */
    COPYRIGHT("Copyright", "ID3v2"),
    
    /**
     * Data length.
     */
    DATA_LENGTH("DataLength", "ID3v2"),
    
    /**
     * Encoder.
     */
    ENCODER("Encoder", "ID3v2"),
    
    /**
     * Length.
     */
    LENGTH("Length", "ID3v2"),
    
    /**
     * Original artist.
     */
    ORIGINAL_ARTIST("OriginalArtist", "ID3v2"),
    
    /**
     * Publisher.
     */
    PUBLISHER("Publisher", "ID3v2");
    
    private final String name;
    private final String tagType;
//    private final String getter;
//    private final String setter;
    
    MetaData(final String newName, final String tagTypeArg) {
        this.name = newName;
        this.tagType = tagTypeArg;
//        this.getter = "get" + this.name();
//        this.setter = "set" + this.name();
    }
    
    /**
     * 
     * @return
     *          Enum name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 
     * @return
     *          Tag version (ID3v1 or ID3v2)
     */
    public String getTagType() {
        return this.tagType;       
    }
}
