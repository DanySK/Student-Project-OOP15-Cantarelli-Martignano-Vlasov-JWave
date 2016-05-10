package org.jwave.controller.player;

//Taken from prof.Viroli's slides
public interface EObserver<T> {
    
    public void update(ESource<? extends T> s, T arg);
    
}
