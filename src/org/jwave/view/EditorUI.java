package org.jwave.view;

public interface EditorUI {

    void show();
    
    void setObserver(PlayerController observer);

    void updatePosition(Integer ms, Integer lenght);

    

}
