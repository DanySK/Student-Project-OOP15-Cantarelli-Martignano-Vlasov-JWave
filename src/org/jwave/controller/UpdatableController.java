package org.jwave.controller;

import org.jwave.model.player.Song;

public interface UpdatableController {

    public void updatePosition(Integer ms);

    public void updateReproductionInfo(Song song);

}
