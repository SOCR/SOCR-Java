package JSci.beans;

import java.beans.*;
import java.util.Vector;
import JSci.maths.*;

public final class VariableBean extends Object implements java.io.Serializable {
        private PropertyChangeSupport changes=new PropertyChangeSupport(this);
        private Vector variableListeners=new Vector();
        private String variable=new String();
        private Object value;

        public VariableBean() {}
        public void setVariable(String var) {
                String oldVar=variable;
                variable=var;
                changes.firePropertyChange("variable",oldVar,var);
        }
        public String getVariable() {
                return variable;
        }
        public void setValueAsNumber(double x) {
                value=new MathDouble(x);
                changes.firePropertyChange("valueAsNumber",null,new Double(x));
                fireVariableChanged(new VariableEvent(this,variable,value));
        }
        public double getValueAsNumber() {
                if(value instanceof MathDouble)
                        return ((MathDouble)value).value();
                else
                        return Double.NaN;
        }
        public void setValueAsVector(double v[]) {
                value=new DoubleVector(v);
                changes.firePropertyChange("valueAsVector",null,v);
                fireVariableChanged(new VariableEvent(this,variable,value));
        }
        public void setValueAsMatrix(double m[][]) {
                value=new DoubleMatrix(m);
                changes.firePropertyChange("valueAsMatrix",null,m);
                fireVariableChanged(new VariableEvent(this,variable,value));
        }
        public void addPropertyChangeListener(PropertyChangeListener l) {
                changes.addPropertyChangeListener(l);
        }
        public void removePropertyChangeListener(PropertyChangeListener l) {
                changes.removePropertyChangeListener(l);
        }
        public void addVariableListener(VariableListener l) {
                variableListeners.addElement(l);
        }
        public void removeVariableListener(VariableListener l) {
                variableListeners.removeElement(l);
        }
        private void fireVariableChanged(VariableEvent evt) {
                for(int i=0;i<variableListeners.size();i++)
                        ((VariableListener)variableListeners.elementAt(i)).variableChanged(evt);
        }
}

