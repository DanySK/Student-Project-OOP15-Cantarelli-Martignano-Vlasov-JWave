package org.jwave.model.editor;

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
