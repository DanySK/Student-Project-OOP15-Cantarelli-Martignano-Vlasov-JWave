package org.jwave.model.editor;

/**
 * This interface models the concept of segments, of specific length, that
 * subdivide any given cut of a song into multiple pieces. Segments have a
 * beginning and an end that are relative to the length of the original,
 * un-modified song.
 * 
 * Segments are similiar to cuts but are used for obtaining actual audio.
 * 
 * All "ms" acronyms stand for milliseconds.
 *
 */
public interface Segment {
    /**
     * Gets the position where this segment begins.
     *          
     * @return
     * 			the position (in ms) of the beginning of the segment.
     */	
	int getFrom();
	
    /**
     * Gets the position where this segment ends.
     *          
     * @return
     * 			the position (in ms) of the end of the segment.
     */		
	int getTo();
	
    /**
     * Gets the length of the segment.
     *          
     * @return
     * 			the length (in ms) of the segment.
     */		
	int getLength();
}
