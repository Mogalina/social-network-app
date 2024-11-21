package org.example.models;

/**
 * The {@code Observer} interface is part of the Observer design pattern.
 * It defines the method that must be implemented by all observers that want to receive updates from an observable
 * object.
 */
public interface Observer {

    /**
     * This method is called when the observed object is changed.
     *
     * @param o the observable object that is notifying the observer
     * @param arg an argument passed by the observable, providing information about the change
     */
    public void update(Observable o, Object arg);
}
