package org.jwave.view;

import org.jwave.model.player.Song;

public interface UI {
    
    public void show();
    
    public void updatePosition(Integer ms, Integer lenght);
    
    public void updateReproductionInfo(Song song);

}
