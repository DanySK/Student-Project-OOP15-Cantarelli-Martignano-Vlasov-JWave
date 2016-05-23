package org.jwave.model.editor;

import org.jwave.model.player.Song;

/**
 * This interface models the concept of a modifiable song, extending the
 * interface of a normal song. A modifiable song is one which can be split
 * into separate pieces, which then may be rearranged in order to create a
 * new song, based on the original.
 * 
 * All "ms" acronyms stand for milliseconds.
 *
 */
public interface ModifiableSong extends Song {
    /**
     * Gets the length of the original, non-modified song.
     *          
     * @return
     * 			the length (in ms) of the original song.
     */	
	int getLength();
	
    /**
     * Gets the length of the modified song.
     *          
     * @return
     * 			the length (in ms) of the modified song.
     */	
	int getModifiedLength();

    /**
     * Pastes the given segment of the modified song into a given position.
     *          
     * @param from
     * 			beginning (in ms) of segment to paste.
     * 
     * @param to
     * 			end (in ms) of segment to paste.
     * 
     * @param at
     * 			position (in ms) where to paste the from-to segment, shifting
     * 			all later segments down by the length of the from-to segment.
     */	
	void pasteSelectionAt(int from, int to, int at);
	
    /**
     * Cuts the given segment of the modified song, deleting it.
     *          
     * @param from
     * 			beginning (in ms) of segment to remove.
     * 
     * @param to
     * 			end (in ms) of segment to remove.
     */	
	void deleteSelection(int from, int to);
	
    /**
     * Temporary debug method for printing a text representation of all the
     * current cuts and segments of a modified song.
     * 
     */
	void printAllCuts();	
}
