package org.jwave.controller.player;

//Inspired by prof.Viroli's slides
/**
 * This interface models the concept of source of the Observer pattern.
 * 
 * @param <T>
 *              the first type parameter.
 * @param <Z>
 *              the second type parameter.
 */
public interface ESource<T, Z> {

    /**
     * Adds an {@link}EObserver to this subject.
     * 
     * @param obs
     *          the observer to be added.
     */
    void addEObserver(EObserver<? super T, ? super Z> obs);

    /**
     * Notifies all attached {@link}EObserver.
     * 
     * @param arg1
     *          first source status change to be notified.
     *          
     * @param arg2
     *          second source status change to be notified.
     */
    void notifyEObservers(T arg1, Z arg2);  
    
    /**
     * Notifies all attached {@link}EObservers.
     * 
     * @param arg
     *          the status change to be notified.
     */
    void notifyEObservers(T arg);
    
//    /**
//     * Notifies all attached {@link}EObservers.
//     * 
//     * @param arg
//     *          the status change to be notified.
//     */
//    void notifyEObserver(Z arg);
}
