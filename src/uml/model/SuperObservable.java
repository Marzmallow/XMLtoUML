package uml.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dries Marzougui
 * Superclass for all observables.
 */
public class SuperObservable implements Observable {

    public List<InvalidationListener> listenerList = new ArrayList<>();

    /**
     * Notifies all listeners when the model has changed.
     */
    public void fireInvalidationEvent() {
        for (InvalidationListener listener : listenerList) {
            listener.invalidated(this);
        }
    }

    /**
     * Adds a listener.
     * @param listener
     */
    @Override
    public void addListener(InvalidationListener listener) {
        listenerList.add(listener);
    }

    /**
     * Removes a listener.
     * @param listener
     */
    @Override
    public void removeListener(InvalidationListener listener) {
        listenerList.remove(listener);
    }

}
