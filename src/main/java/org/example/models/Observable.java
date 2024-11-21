package org.example.models;

/**
 * The {@code Observable} interface is part of the Observer design pattern.
 * It defines the methods for managing a list of observers and notifying them when there are changes in the observable's
 * state.
 */
public interface Observable {

    /**
     * Adds an observer to the list of observers.
     * This observer will be notified when the observable's state changes.
     *
     * @param observer the observer to be added
     */
    public void addObserver(Observer observer);

    /**
     * Removes an observer from the list of observers.
     * This observer will no longer be notified of any state changes in the observable.
     *
     * @param observer the observer to be removed
     */
    public void removeObserver(Observer observer);

    /**
     * Notifies all registered observers that the observable's state has changed.
     * This method will trigger the {@code update} method on each observer.
     *
     * @param arg an argument passed by the observable, providing information about the change
     */
    public void notifyObservers(Object arg);
}
