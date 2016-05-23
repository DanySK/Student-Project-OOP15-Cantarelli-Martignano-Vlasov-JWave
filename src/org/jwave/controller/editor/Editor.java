package org.jwave.controller.editor;

import org.jwave.model.player.Song;

/**
 * This interface models the concept of a song editor, 
 * which allows the user to modify a song by copying,
 * cutting and pasting together segments in order to produce
 * a new song, which can then be saved.
 * 
 * All "ms" acronyms stand for milliseconds.
 *
 */
public interface Editor {
    /**
     * Loads the song passed in and prepares it for modification.
     * 
     * @param song
     * 			the song to load.
     */
	void loadSongToEdit(Song song);
	
    /**
     * Checks if a song has been loaded.
     *          
     * @return
     * 			true if song has been loaded, false otherwise.
     */
	boolean isSongLoaded();	
	
    /**
     * Gets the length of the original, unmodifed song.
     *          
     * @return
     * 			length of original song in ms, -1 if no song loaded.
     */
	int getOriginalSongLength();
	
    /**
     * Gets the length of the modified song.
     *          
     * @return
     * 			length of modified song in ms, -1 if no song loaded.
     */
	int getModifiedSongLength();	
	
    /**
     * Sets the selection-from cursor, specifying from which point
     * the current selection begins. Also referred to as the main
     * cursor, because of it's usage to paste copied audio.
     * 
     * @param ms
     *          the position (in ms) where to place the cursor.
     */
	void setSelectionFrom(int ms);
	
    /**
     * Sets the selection-to cursor, specifying at which point the
     * current selection end.
     * 
     * @param ms
     *          the position (in ms) where to place the cursor.
     */
	void setSelectionTo(int ms);	
	
    /**
     * Gets the position of the selection-from cursor, specifying from where
     * the current selection begins.
     *          
     * @return
     * 			the position (in ms) of the cursor, -1 if cursor not placed.
     */
	int getSelectionFrom();
	
    /**
     * Gets the position of the selection-to cursor, specifying where
     * the current selection ends.
     *          
     * @return
     * 			the position (in ms) of the cursor, -1 if cursor not placed.
     */
	int getSelectionTo();	
	
    /**
     * Deselects both cursors, forgetting the previous selection. Done by
     * setting both cursors (selectionFrom and selectionTo) to -1.
     * 
     */
	void deselectSelection();
	
    /**
     * Checks if the main, selection-from, cursor is set.
     *          
     * @return
     * 			true if set, false otherwise
     */
	boolean isCursorSet();
	
    /**
     * Checks if both the selection-from and the selection-to cursors are
     * set, necessary condition to be able to copy or cut a piece of the
     * currently loaded song.
     *          
     * @return
     * 			true if both set, false otherwise
     */
	boolean isSomethingSelected();
	
    /**
     * Copies the current selection into memory.
     *          
     * @return
     * 			true if selection exists, false otherwise
     */
	boolean copySelection();
	
    /**
     * Gets the starting point of the copied selection.
     *          
     * @return
     * 			the position (in ms) of the beginning of the copied
     * 			selection, -1 if nothing copied.
     */
	int getCopiedFrom();
	
    /**
     * Gets the end point of the copied selection.
     *          
     * @return
     * 			the position (in ms) of the end of the copied
     * 			selection, -1 if nothing copied.
     */
	int getCopiedTo();

    /**
     * Forgets the copied selection, setting copiedFrom and copiedTo to -1.
     * 
     */
	void resetCopiedSelection();
	
    /**
     * Checks if something has been copied.
     *          
     * @return
     * 			true if something has been copied, false otherwise.
     */
	boolean isSomethingCopied();
	
    /**
     * Pastes the currently copied selection at the main cursor.
     *          
     * @return
     * 			true if something has been copied and main cursor is set,
     * 			false otherwise.
     */
	boolean pasteCopiedSelection();
	
    /**
     * Cuts the current selection, deleting it.
     *          
     * @return
     * 			true if something is selected, false otherwise.
     */
	boolean cutSelection(); 
	
    /**
     * Temporary debug method for printing information relative to the current
     * state of the modified song, including cuts, segments and cursors.
     * 
     */
	void printSongDebug();	
}
