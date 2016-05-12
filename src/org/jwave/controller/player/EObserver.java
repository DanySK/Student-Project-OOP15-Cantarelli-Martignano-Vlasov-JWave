package org.jwave.controller.player;

//Taken from prof.Viroli's slides
public interface EObserver<T, Z> {
    
    public void update(ESource<? extends T, ? extends Z> s, T arg);
    
}
