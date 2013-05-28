package edu.ucla.loni.LOVE;

//import
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//local import


/**
 * Interface for bounded model.
 * As indicated in Java Bean, this model have a list of
 * listener. Once the bounded value of this model changes
 * it will fire a <code>PropertyChangeEvent</code> to
 * notify all the listeners.
 * Listener of this class should implement
 * <code>PropertyChangeListener</code> interface.
 */
public interface BoundedModel {
    /**
     * Add a <code> PropertyChangeListener </code> to this
     * model.
     *
     * @param listener  The listener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove the <code> PropertyChangeListener </code> from
     * this model.
     *
     * @param listener  The listener to be removed.
     */
    public void removePropertyListener(PropertyChangeListener listener);

    /**
     * Fire a <code> PropertyChangeEvent </code>.
     *
     * @param propertyName  The name of the changed property.
     * @param oldValue      Original value of the changed property.
     * @param newValue      New value of the changed property.
     */
    public void firePropertyChange(String propertyName,
				   Object oldValue,
				   Object newValue);
    /**
     * Fire a <code> PropertyChangeEvent </code>.
     *
     * @param propertyChangeEvent A <code> PropertyChangeEvent </code>.
     */
    public void firePropertyChange(PropertyChangeEvent
				   propertyChangeEvent);
}

