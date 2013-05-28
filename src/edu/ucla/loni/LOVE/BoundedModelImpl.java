// 	$Id: BoundedModelImpl.java,v 1.1 2010/03/10 20:35:29 jiecui Exp $
package edu.ucla.loni.LOVE;

//import

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

//local import


/**
 * Base class for bounded model.
 * As indicated in Java Bean, this model have a list of
 * listener. Once the bounded value of this model changes
 * it will fire a <code> PropertyChangeEvent </code> to
 * notify all the listeners.
 * Listener of this class should implement
 * <code> PropertyChangeListener </code> interface.
 */
public class BoundedModelImpl implements BoundedModel
{
    /** Java Bean's class to support property change. */
    private PropertyChangeSupport
	changeSupport = new PropertyChangeSupport(this);

    /**
     * Constructor
     */
    public BoundedModelImpl() {
    }

    /**
     * Add a <code> PropertyChangeListener </code> to this
     * model.
     *
     * @param listener  The listener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove the <code> PropertyChangeListener </code> from
     * this model.
     *
     * @param listener  The listener to be removed.
     */
    public void removePropertyListener(PropertyChangeListener listener) {
	changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Fire a <code> PropertyChangeEvent </code>.
     *
     * @param propertyName  The name of the changed property.
     * @param oldValue      Original value of the changed property.
     * @param newValue      New value of the changed property.
     */
    public void firePropertyChange(String propertyName,
				   Object oldValue,
				   Object newValue) {
	changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Fire a <code> PropertyChangeEvent </code>.
     *
     * @param propertyChangeEvent A <code> PropertyChangeEvent </code>.
     */
    public void firePropertyChange(PropertyChangeEvent
				   propertyChangeEvent) {
	changeSupport.firePropertyChange(propertyChangeEvent);
    }
}

