package JSci.io;

import java.util.*;
import JSci.maths.*;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.algebras.*;
import JSci.maths.fields.*;

/**
* The MathMLExpression class is used by the MathMLParser to
* encapsulate math expressions described by the <code>&lt;apply&gt;</code> tag.
* @version 0.6
* @author Mark Hale
*/
public final class MathMLExpression extends Object {
        private String operation;
        private List args=new ArrayList();

        /**
        * Constructs a MathML expression.
        */
        public MathMLExpression() {}
        /**
        * Set the operation to be applied to the arguments.
        * @param op a MathML tag name
        * (<code>plus</code>, <code>minus</code>, <code>times</code>, <code>divide</code>, etc).
        */
        public void setOperation(String op) {
                operation=op;
        }
        /**
        * Returns the operation to be applied to the arguments.
        * @return a MathML tag name.
        */
        public String getOperation() {
                return operation;
        }
        /**
        * Adds an argument to this expression.
        */
        public void addArgument(Object obj) {
                args.add(obj);
        }
        /**
        * Returns an argument from this expression.
        */
        public Object getArgument(int n) {
                return args.get(n);
        }
        /**
        * Returns the number of arguments.
        */
        public int length() {
                return args.size();
        }
        /**
        * Substitutes a value for a variable in this expression.
        * @param var the variable to substitute for.
        * @param value the value of the variable.
        * @return the expression after substitution.
        */
        public MathMLExpression substitute(String var,Object value) {
                MathMLExpression subExpr=new MathMLExpression();
                subExpr.operation=operation;
                Object arg;
                for(int i=0;i<length();i++) {
                        arg=getArgument(i);
                        if(arg instanceof MathMLExpression)
                                arg=((MathMLExpression)arg).substitute(var,value);
                        else if(arg.equals(var))
                                arg=value;
                        subExpr.addArgument(arg);
                }
                return subExpr;
        }
        /**
        * Substitutes several values for variables in this expression.
        * @param vars a hashtable of variables and values to substitute.
        * @return the expression after substitution.
        */
        public MathMLExpression substitute(Hashtable vars) {
                MathMLExpression subExpr=new MathMLExpression();
                subExpr.operation=operation;
                Object arg;
                for(int i=0;i<length();i++) {
                        arg=getArgument(i);
                        if(arg instanceof MathMLExpression)
                                arg=((MathMLExpression)arg).substitute(vars);
                        else if(vars.containsKey(arg))
                                arg=vars.get(arg);
                        subExpr.addArgument(arg);
                }
                return subExpr;
        }
        /**
        * Evaluates this expression.
        */
        public Object evaluate() {
                if(length()==1)
                        return unaryEvaluate();
                else if(length()==2)
                        return binaryEvaluate();
                else
                        return nAryEvaluate();
        }
        /**
        * Evaluates unary expressions.
        */
        private Object unaryEvaluate() {
                Object value=getArgument(0);
                if(value instanceof MathMLExpression)
                        value=((MathMLExpression)value).evaluate();
                if(operation.equals("abs")) {
                        if(value instanceof MathDouble)
                                value=new MathDouble(Math.abs(((MathDouble)value).value()));
                        else if(value instanceof Complex)
                                value=new MathDouble(((Complex)value).mod());
                } else if(operation.equals("arg")) {
                        if(value instanceof MathDouble) {
                                if(((MathDouble)value).value()>=0.0)
                                        value=RealField.ZERO;
                                else
                                        value=RealField.PI;
                        } else if(value instanceof Complex)
                                value=new MathDouble(((Complex)value).arg());
                } else if(operation.equals("real")) {
                        if(value instanceof Complex)
                                value=new MathDouble(((Complex)value).real());
                        else if(value instanceof ComplexVector)
                                value=((ComplexVector)value).real();
                        else if(value instanceof ComplexMatrix)
                                value=((ComplexMatrix)value).real();
                } else if(operation.equals("imaginary")) {
                        if(value instanceof Complex)
                                value=new MathDouble(((Complex)value).imag());
                        else if(value instanceof ComplexVector)
                                value=((ComplexVector)value).imag();
                        else if(value instanceof ComplexMatrix)
                                value=((ComplexMatrix)value).imag();
                } else if(operation.equals("conjugate")) {
                        if(value instanceof Complex)
                                value=((Complex)value).conjugate();
                        else if(value instanceof ComplexVector)
                                value=((ComplexVector)value).conjugate();
                        else if(value instanceof ComplexMatrix)
                                value=((ComplexMatrix)value).conjugate();
                } else if(operation.equals("transpose")) {
                        if(value instanceof Matrix)
                                value=((Matrix)value).transpose();
                } else if(operation.equals("exp")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.exp((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.exp((Complex)value);
                } else if(operation.equals("ln")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.log((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.log((Complex)value);
                } else if(operation.equals("sin")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.sin((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.sin((Complex)value);
                } else if(operation.equals("cos")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.cos((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.cos((Complex)value);
                } else if(operation.equals("tan")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.tan((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.tan((Complex)value);
                } else if(operation.equals("arcsin")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.asin((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.asin((Complex)value);
                } else if(operation.equals("arccos")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.acos((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.acos((Complex)value);
                } else if(operation.equals("arctan")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.atan((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.atan((Complex)value);
                } else if(operation.equals("sinh")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.sinh((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.sinh((Complex)value);
                } else if(operation.equals("cosh")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.cosh((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.cosh((Complex)value);
                } else if(operation.equals("tanh")) {
                        if(value instanceof MathDouble)
                                value=MathDouble.tanh((MathDouble)value);
                        else if(value instanceof Complex)
                                value=Complex.tanh((Complex)value);
                } else if(operation.equals("factorial")) {
                        if(value instanceof MathDouble)
                                value=new MathDouble(ExtraMath.factorial(((MathDouble)value).value()));
                } else if(operation.equals("not")) {
                        if(value instanceof Boolean)
                                value=new Boolean(!((Boolean)value).booleanValue());
                }
                return value;
        }
        /**
        * Evaluates binary expressions.
        */
        private Object binaryEvaluate() {
                Object value=getArgument(0);
                if(value instanceof MathMLExpression)
                        value=((MathMLExpression)value).evaluate();
                Object next=getArgument(1);
                if(next instanceof MathMLExpression)
                        next=((MathMLExpression)next).evaluate();
                if(operation.equals("minus")) {
                        if(value instanceof AbelianGroup.Member)
                                value=((AbelianGroup.Member)value).subtract((AbelianGroup.Member)next);
                } else if(operation.equals("divide")) {
                        if(next instanceof Field.Member) {
                                if(value instanceof Field.Member)
                                        value=((Field.Member)value).divide((Field.Member)next);
                                else if(value instanceof VectorSpace.Member)
                                        value=((VectorSpace.Member)value).scalarDivide((Field.Member)next);
                        }
                } else if(operation.equals("scalarproduct")) {
                        if(value instanceof DoubleVector)
                                value=new MathDouble(((DoubleVector)value).scalarProduct((DoubleVector)next));
                        else if(value instanceof ComplexVector)
                                value=((ComplexVector)value).scalarProduct((ComplexVector)next);
                } else if(operation.equals("vectorproduct")) {
                        if(value instanceof Double3Vector)
                                value=((Double3Vector)value).multiply((Double3Vector)next);
                        else if(value instanceof Complex3Vector)
                                value=((Complex3Vector)value).multiply((Complex3Vector)next);
                } else if(operation.equals("power")) {
                        if(value instanceof MathDouble)
                                value=new MathDouble(Math.pow(((MathDouble)value).value(),((MathDouble)next).value()));
                } else if(operation.equals("neq")) {
                        value=new Boolean(!value.equals(next));
                } else
                        return nAryEvaluate();
                return value;
        }
        /**
        * Evaluates n-ary expressions.
        */
        private Object nAryEvaluate() {
                Object value=getArgument(0);
                if(value instanceof MathMLExpression)
                        value=((MathMLExpression)value).evaluate();
                if(operation.equals("plus")) {
                        if(value instanceof AbelianGroup.Member) {
                                AbelianGroup.Member ans=(AbelianGroup.Member)value;
                                for(int i=1;i<length();i++) {
                                        Object next=getArgument(i);
                                        if(next instanceof MathMLExpression)
                                                next=((MathMLExpression)next).evaluate();
                                        if(next instanceof AbelianGroup.Member)
                                                ans=ans.add((AbelianGroup.Member)next);
                                }
                                value=ans;
                        }
                } else if(operation.equals("times")) {
                        if(value instanceof Ring.Member) {
                                for(int i=1;i<length();i++) {
                                        Object next=getArgument(i);
                                        if(next instanceof MathMLExpression)
                                                next=((MathMLExpression)next).evaluate();
                                        if(next instanceof Ring.Member)
                                                value=((Ring.Member)value).multiply((Ring.Member)next);
                                        else if(next instanceof Module.Member)
                                                value=((Module.Member)next).scalarMultiply((Ring.Member)value);
                                }
                        }
                } else if(operation.equals("min")) {
                        Comparable ans=(Comparable)value;
                        for(int i=1;i<length();i++) {
                                Object next=getArgument(i);
                                if(next instanceof MathMLExpression)
                                        next=((MathMLExpression)next).evaluate();
                                if(ans.compareTo(next)>0)
                                        ans=(Comparable)next;
                        }
                        value=ans;
                } else if(operation.equals("max")) {
                        Comparable ans=(Comparable)value;
                        for(int i=1;i<length();i++) {
                                Object next=getArgument(i);
                                if(next instanceof MathMLExpression)
                                        next=((MathMLExpression)next).evaluate();
                                if(ans.compareTo(next)<0)
                                        ans=(Comparable)next;
                        }
                        value=ans;
                } else if(operation.equals("mean")) {
                        value=new MathDouble(mean());
                } else if(operation.equals("sdev")) {
                        value=new MathDouble(Math.sqrt(variance()));
                } else if(operation.equals("var")) {
                        value=new MathDouble(variance());
                } else if(operation.equals("median")) {
                        double nums[]=new double[length()];
                        nums[0]=((MathDouble)value).value();
                        for(int i=1;i<nums.length;i++) {
                                Object next=getArgument(i);
                                if(next instanceof MathMLExpression)
                                        next=((MathMLExpression)next).evaluate();
                                nums[i]=((MathDouble)next).value();
                        }
                        value=new MathDouble(ArrayMath.median(nums));
                } else if(operation.equals("union")) {
                        if(value instanceof MathSet) {
                                MathSet ans=(MathSet)value;
                                for(int i=1;i<length();i++) {
                                        Object next=getArgument(i);
                                        if(next instanceof MathMLExpression)
                                                next=((MathMLExpression)next).evaluate();
                                        if(next instanceof MathSet)
                                                ans=ans.union((MathSet)next);
                                }
                                value=ans;
                        }
                } else if(operation.equals("intersect")) {
                        if(value instanceof MathSet) {
                                MathSet ans=(MathSet)value;
                                for(int i=1;i<length();i++) {
                                        Object next=getArgument(i);
                                        if(next instanceof MathMLExpression)
                                                next=((MathMLExpression)next).evaluate();
                                        if(next instanceof MathSet)
                                                ans=ans.intersect((MathSet)next);
                                }
                                value=ans;
                        }
                } else if(operation.equals("and")) {
                        if(value instanceof Boolean) {
                                boolean ans=((Boolean)value).booleanValue();
                                for(int i=1;i<length();i++) {
                                        Object next=getArgument(i);
                                        if(next instanceof MathMLExpression)
                                                next=((MathMLExpression)next).evaluate();
                                        if(next instanceof Boolean)
                                                ans&=((Boolean)next).booleanValue();
                                }
                                value=new Boolean(ans);
                        }
                } else if(operation.equals("or")) {
                        if(value instanceof Boolean) {
                                boolean ans=((Boolean)value).booleanValue();
                                for(int i=1;i<length();i++) {
                                        Object next=getArgument(i);
                                        if(next instanceof MathMLExpression)
                                                next=((MathMLExpression)next).evaluate();
                                        if(next instanceof Boolean)
                                                ans|=((Boolean)next).booleanValue();
                                }
                                value=new Boolean(ans);
                        }
                } else if(operation.equals("xor")) {
                        if(value instanceof Boolean) {
                                boolean ans=((Boolean)value).booleanValue();
                                for(int i=1;i<length();i++) {
                                        Object next=getArgument(i);
                                        if(next instanceof MathMLExpression)
                                                next=((MathMLExpression)next).evaluate();
                                        if(next instanceof Boolean)
                                                ans^=((Boolean)next).booleanValue();
                                }
                                value=new Boolean(ans);
                        }
                } else if(operation.equals("eq")) {
                        Object arg1=value;
                        boolean ans=true;
                        for(int i=1;i<length();i++) {
                                Object arg2=getArgument(i);
                                if(arg2 instanceof MathMLExpression)
                                        arg2=((MathMLExpression)arg2).evaluate();
                                ans&=arg1.equals(arg2);
                                arg1=arg2;
                        }
                        value=new Boolean(ans);
                } else if(operation.equals("lt")) {
                        if(value instanceof Comparable) {
                                Comparable arg1=(Comparable)value;
                                boolean ans=true;
                                for(int i=1;i<length();i++) {
                                        Object arg2=getArgument(i);
                                        if(arg2 instanceof MathMLExpression)
                                                arg2=((MathMLExpression)arg2).evaluate();
                                        ans&=arg1.compareTo(arg2)<0;
                                        if(arg2 instanceof Comparable)
                                                arg1=(Comparable)arg2;
                                }
                                value=new Boolean(ans);
                        }
                } else if(operation.equals("leq")) {
                        if(value instanceof Comparable) {
                                Comparable arg1=(Comparable)value;
                                boolean ans=true;
                                for(int i=1;i<length();i++) {
                                        Object arg2=getArgument(i);
                                        if(arg2 instanceof MathMLExpression)
                                                arg2=((MathMLExpression)arg2).evaluate();
                                        ans&=arg1.compareTo(arg2)<=0;
                                        if(arg2 instanceof Comparable)
                                                arg1=(Comparable)arg2;
                                }
                                value=new Boolean(ans);
                        }
                } else if(operation.equals("gt")) {
                        if(value instanceof Comparable) {
                                Comparable arg1=(Comparable)value;
                                boolean ans=true;
                                for(int i=1;i<length();i++) {
                                        Object arg2=getArgument(i);
                                        if(arg2 instanceof MathMLExpression)
                                                arg2=((MathMLExpression)arg2).evaluate();
                                        ans&=arg1.compareTo(arg2)>0;
                                        if(arg2 instanceof Comparable)
                                                arg1=(Comparable)arg2;
                                }
                                value=new Boolean(ans);
                        }
                } else if(operation.equals("geq")) {
                        if(value instanceof Comparable) {
                                Comparable arg1=(Comparable)value;
                                boolean ans=true;
                                for(int i=1;i<length();i++) {
                                        Object arg2=getArgument(i);
                                        if(arg2 instanceof MathMLExpression)
                                                arg2=((MathMLExpression)arg2).evaluate();
                                        ans&=arg1.compareTo(arg2)>=0;
                                        if(arg2 instanceof Comparable)
                                                arg1=(Comparable)arg2;
                                }
                                value=new Boolean(ans);
                        }
                }
                return value;
        }
        /**
        * Calculates the mean for this expression.
        */
        private double mean() {
                double sum=((MathDouble)getArgument(0)).value();
                for(int i=1;i<length();i++) {
                        Object next=getArgument(i);
                        if(next instanceof MathMLExpression)
                                next=((MathMLExpression)next).evaluate();
                        sum+=((MathDouble)next).value();
                }
                return sum/length();
        }
        /**
        * Calculates the variance for this expression.
        */
        private double variance() {
                final double m=mean();
                double num=((MathDouble)getArgument(0)).value();
                double ans=(num-m)*(num-m);
                for(int i=1;i<length();i++) {
                        Object next=getArgument(i);
                        if(next instanceof MathMLExpression)
                                next=((MathMLExpression)next).evaluate();
                        num=((MathDouble)next).value();
                        ans+=(num-m)*(num-m);
                }
                return ans/(length()-1);
        }
}

