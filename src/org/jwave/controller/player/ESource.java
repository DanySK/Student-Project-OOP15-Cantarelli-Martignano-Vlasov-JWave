package org.jwave.controller.player;

import java.util.HashSet;
import java.util.Set;

//Taken from prof.Viroli's slides
public class ESource<T> {
    
    private final Set<EObserver<? super T>> set = new HashSet<>();
    
    public void addEObserver(EObserver<? super T> obs){
            this.set.add(obs);
    }
    
    public void notifyEObservers(T arg){
            for (final EObserver<? super T> obs : this.set){
                    obs.update(this, arg);
            }
    }
}
