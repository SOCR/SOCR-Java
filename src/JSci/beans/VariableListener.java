package JSci.beans;

import java.util.EventListener;

public interface VariableListener extends EventListener {
        void variableChanged(VariableEvent evt);
}

