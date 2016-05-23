package org.jwave.model.player;

/**
 * This interface models the concept of metadata, which is 
 * all the information contained in a {@link}Song.
 *
 */
public interface MetaDataV1 {
    
    /**
     * 
     * @return
     *          song album.
     */
    String getAlbum();
    
    /**
     * 
     * @return
     *          song author.
     */
    String getAuthor();
    
    /**
     * 
     * @return
     *          song comment.
     */
    String getComment();
    
    /**
     * 
     * @return
     *          song composer.
     */
    String getComposer();
    
    /**
     * 
     * @return
     *          song copyright.
     */
    String getCopyright();
    
    /**
     * 
     * @return
     *          the date the song was recorded.
     */
    String getDate();
    
    /**
     * 
     * @return
     *          song disc.
     */
    String getDisc();
    
    /**
     * 
     * @return
     *          the software that encoded the song.
     */
    String getEncoded();
    
    /**
     * 
     * @return
     *          the file containing the song.
     */
    String getFileName();
    
    /**
     * 
     * @return
     *          song genre.
     */
    String getGenre();
    
    /**
     * 
     * @return
     *          song length in milliseconds.
     */
    String getLength();
    
    /**
     * 
     * @return
     *          song lyrics.
     */
    String getLyrics();
    
    /**
     * 
     * @return
     *          song orchestra.
     */
    String getOrchestra();
    
    /**
     * 
     * @return
     *          the orchestra that performed the recording.
     */
    String getPublisher();
    
    /**
     * 
     * @return
     *          the number of sample frames in the recording.
     */
    String getSampleFrameCount();
    
    /**
     * 
     * @return
     *          song title.
     */
    String getTitle();
    
    /**
     * 
     * @return
     *          the track number.
     */
    String getTrack();
    
    /**
     * 
     * @param metaDataValue
     * 
     * @return
     */
    String retrieve(String metaDataValue);
}
