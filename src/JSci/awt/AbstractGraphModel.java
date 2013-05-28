package JSci.awt;

import java.util.Vector;

/**
* The AbstractGraphModel class handles the dispatching of
* GraphDataEvents to interested listeners.
* @version 1.0
* @author Mark Hale
*/
public abstract class AbstractGraphModel extends Object {
        private final Vector<GraphDataListener> listenerList =
        				new Vector<GraphDataListener>();
        private final GraphDataEvent event=new GraphDataEvent(this);

        protected final void fireDataChanged() {
                for(int i=0;i<listenerList.size();i++)
                        ((GraphDataListener)listenerList.elementAt(i)).dataChanged(event);
        }
        public final void addGraphDataListener(GraphDataListener l) {
                listenerList.addElement(l);
        }
        public final void removeGraphDataListener(GraphDataListener l) {
                listenerList.removeElement(l);
        }
}

