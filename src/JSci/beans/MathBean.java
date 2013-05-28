package JSci.beans;

import java.awt.event.*;
import java.beans.*;
import java.util.*;
import JSci.maths.*;
import JSci.io.*;
import JSci.util.*;

public final class MathBean extends Object implements java.io.Serializable,
VariableListener, ActionListener {
        private PropertyChangeSupport changes=new PropertyChangeSupport(this);
        private MathMLExpression expr;
        private String mathml=new String();
        private Hashtable variables=new Hashtable();
        private Object result=new MathDouble(Double.NaN);

        public MathBean() {}
        public void setMathML(String uri) {
                try {
                        MathMLParser parser=new MathMLParser();
                        parser.parse(uri);
                        expr=(MathMLExpression)(parser.translateToJSciObjects()[0]);
                } catch(Exception e) {}
                String oldUri=mathml;
                mathml=uri;
                changes.firePropertyChange("mathml",oldUri,uri);
        }
        public String getMathML() {
                return mathml;
        }
        public double getResultAs0DArray() {
                if(result instanceof MathDouble)
                        return ((MathDouble)result).value();
                else if(result instanceof MathInteger)
                        return ((MathInteger)result).value();
                else
                        return Double.NaN;
        }
        public double[] getResultAs1DArray() {
                if(result instanceof Complex) {
                        double array[]={((Complex)result).real(),((Complex)result).imag()};
                        return array;
                } else if(result instanceof DoubleVector)
                        return VectorToolkit.toArray((DoubleVector)result);
                else if(result instanceof IntegerVector)
                        return VectorToolkit.toArray(((IntegerVector)result).toDoubleVector());
                else
                        return null;
        }
        public double[][] getResultAs2DArray() {
                if(result instanceof ComplexVector) {
                        double array[][]=new double[2][];
                        array[0]=VectorToolkit.toArray(((ComplexVector)result).real());
                        array[1]=VectorToolkit.toArray(((ComplexVector)result).imag());
                        return array;
                } else if(result instanceof DoubleMatrix)
                        return MatrixToolkit.toArray((DoubleMatrix)result);
                else if(result instanceof IntegerMatrix)
                        return MatrixToolkit.toArray(((IntegerMatrix)result).toDoubleMatrix());
                else
                        return null;
        }
        public double[][][] getResultAs3DArray() {
                if(result instanceof ComplexMatrix) {
                        double array[][][]=new double[2][][];
                        array[0]=MatrixToolkit.toArray(((ComplexMatrix)result).real());
                        array[1]=MatrixToolkit.toArray(((ComplexMatrix)result).imag());
                        return array;
                } else
                        return null;
        }
        public void variableChanged(VariableEvent evt) {
                variables.put(evt.getVariable(),evt.getValue());
        }
        public void actionPerformed(ActionEvent evt) {
                MathMLExpression evalExp=expr;
                Enumeration vars=variables.keys();
                while(vars.hasMoreElements()) {
                        Object var=vars.nextElement();
                        evalExp=evalExp.substitute(var.toString(),variables.get(var));
                }
                result=evalExp.evaluate();
                changes.firePropertyChange("resultAs0DArray",null,new Double(getResultAs0DArray()));
                changes.firePropertyChange("resultAs1DArray",null,getResultAs1DArray());
                changes.firePropertyChange("resultAs2DArray",null,getResultAs2DArray());
                changes.firePropertyChange("resultAs3DArray",null,getResultAs3DArray());
        }
        public void addPropertyChangeListener(PropertyChangeListener l) {
                changes.addPropertyChangeListener(l);
        }
        public void removePropertyChangeListener(PropertyChangeListener l) {
                changes.removePropertyChangeListener(l);
        }
}

