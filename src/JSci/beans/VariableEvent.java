package JSci.beans;

import java.util.EventObject;

public final class VariableEvent extends EventObject {
        private String variable;
        private Object value;

        public VariableEvent(Object src,String var,Object val) {
                super(src);
                variable=var;
                value=val;
        }
        public String getVariable() {
                return variable;
        }
        public Object getValue() {
                return value;
        }
}

