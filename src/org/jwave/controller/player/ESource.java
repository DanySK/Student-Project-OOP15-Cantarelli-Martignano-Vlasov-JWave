package org.jwave.controller.player;

public interface ESource<T, Z> {

    void addEObserver(EObserver<? super T, ? super Z> obs);

    void notifyEObservers(T arg1, Z arg2);
}
