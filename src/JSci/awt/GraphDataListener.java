package JSci.awt;

import java.util.EventListener;

/**
* GraphDataListener.
* @version 1.0
* @author Mark Hale
*/
public interface GraphDataListener extends EventListener {
        /**
        * Sent when the contents of the model has changed.
        */
        void dataChanged(GraphDataEvent e);
}

