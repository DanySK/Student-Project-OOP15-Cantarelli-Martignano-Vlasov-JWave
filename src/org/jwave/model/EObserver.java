package org.jwave.model;

//Inspired by prof.Viroli's slides
/**
 * This interface models the concept of observer of the Observer pattern.
 * 
 * @param <T>
 * 
 */
public interface EObserver<T> {
    
    /**
     * Updates the observer status.
     * 
     * @param s
     *          the source that notifies this observer.
     *          
     * @param arg
     *          the notified status.
     */
    void update(ESource<? extends T> s, T arg);
}
