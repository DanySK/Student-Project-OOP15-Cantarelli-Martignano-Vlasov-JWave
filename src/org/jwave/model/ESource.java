package org.jwave.model;

//Inspired by prof.Viroli's slides
/**
 * This interface models the concept of source of the Observer pattern.
 * 
 * @param <T>
 *              the first type parameter.
 */
public interface ESource<T> {

    /**
     * Adds an {@link}EObserver to this subject.
     * 
     * @param obs
     *          the observer to be added.
     */
    void addEObserver(EObserver<? super T> obs);  
    
    /**
     * Notifies all attached {@link}EObservers.
     * 
     * @param arg
     *          the status change to be notified.
     */
    void notifyEObservers(T arg);
    
    /**
     * Removes all attached observers.
     */
    void clearObservers();
}
