package org.jwave.view;

public interface PlayerUI extends UI{
    
    public void setObserver(PlayerController observer);
    
    public void updatePosition(Integer ms, Integer lenght);

}
