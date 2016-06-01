package org.jwave.controller;

import org.jwave.model.player.Song;

public interface UpdatableUI {

    /**
     * Updates the the ui
     * @param ms current position of the song
     */
    public void updatePosition(Integer ms);

    /**
     * Updates the ui with infos about the song
     * @param song
     */
    public void updateReproductionInfo(Song song);

}
