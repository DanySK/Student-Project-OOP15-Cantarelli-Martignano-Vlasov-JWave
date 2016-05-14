package org.jwave.controller.player;

//Inspired by prof.Viroli's slides
/**
 * This interface models the concept of observer of the Observer pattern.
 * 
 * @param <T>
 * 
 * @param <Z>
 */
public interface EObserver<T, Z> {
    
    /**
     * Updates the observer status.
     * 
     * @param s
     *          the source that notifies this observer.
     *          
     * @param arg1
     *          first source status notified.
     *                  
     * @param arg2
     *          second source status notified.
     */
    void update(ESource<? extends T, ? extends Z> s, T arg1, Z arg2);
    
    /**
     * Updates the observer status.
     * 
     * @param s
     *          the source that notifies this observer.
     *          
     * @param arg
     *          the notified status.
     */
    void update(ESource<? extends T, ? extends Z> s, T arg);
    
}
