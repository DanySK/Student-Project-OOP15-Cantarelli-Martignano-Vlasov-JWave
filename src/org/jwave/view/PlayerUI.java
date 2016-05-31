package org.jwave.view;

import org.jwave.model.player.Song;

public interface PlayerUI extends UI{
    
    public void updatePosition(Integer ms, Integer lenght);
    
    public void updateReproductionInfo(Song song);

}
