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
}
