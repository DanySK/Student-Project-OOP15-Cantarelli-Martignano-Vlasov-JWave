package org.jwave.controller.player;

//Inspired by prof.Viroli's slides
public interface EObserver<T, Z> {
    
    public void update(ESource<? extends T, ? extends Z> s, T arg1, Z arg2);
    
}
