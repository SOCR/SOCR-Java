package JSci.maths.algebras;

import JSci.maths.*;
import JSci.maths.fields.ComplexField;

/**
* The su3Dim3 class encapsulates su(3) algebras using
* the 3 dimensional (fundamental) representation.
* Elements are represented by vectors with a matrix basis.
* @version 1.2
* @author Mark Hale
*/
public final class su3Dim3 extends LieAlgebra {
        /**
        * Useful complex constants.
        */
        private final static Complex T8_1=new Complex(0.5/Math.sqrt(3.0),0.0);
        private final static Complex T8_2=new Complex(-1.0/Math.sqrt(3.0),0.0);

        private final static Complex t1[][]={
                {Complex.ZERO,ComplexField.HALF,Complex.ZERO},
                {ComplexField.HALF,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t2[][]={
                {Complex.ZERO,ComplexField.MINUS_HALF_I,Complex.ZERO},
                {ComplexField.HALF_I,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t3[][]={
                {ComplexField.HALF,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,ComplexField.MINUS_HALF,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t4[][]={
                {Complex.ZERO,Complex.ZERO,ComplexField.HALF},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {ComplexField.HALF,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t5[][]={
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_HALF_I},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {ComplexField.HALF_I,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t6[][]={
                {Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,ComplexField.HALF},
                {Complex.ZERO,ComplexField.HALF,Complex.ZERO}
        };
        private final static Complex t7[][]={
                {Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_HALF_I},
                {Complex.ZERO,ComplexField.HALF_I,Complex.ZERO}
        };
        private final static Complex t8[][]={
                {T8_1,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,T8_1,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,T8_2}
        };
        /**
        * Basis.
        */
        private final static ComplexSquareMatrix basisMatrices[]={
                new ComplexSquareMatrix(t1),
                new ComplexSquareMatrix(t2),
                new ComplexSquareMatrix(t3),
                new ComplexSquareMatrix(t4),
                new ComplexSquareMatrix(t5),
                new ComplexSquareMatrix(t6),
                new ComplexSquareMatrix(t7),
                new ComplexSquareMatrix(t8)
        };

        private static su3Dim3 _instance;
        /**
        * Constructs an su(3) algebra.
        */
        private su3Dim3() {
                super("su(3) [3]");
        }
        /**
        * Singleton.
        */
        public static final su3Dim3 getInstance() {
                if(_instance == null) {
                        synchronized(su3Dim3.class) {
                                if(_instance == null)
                                        _instance = new su3Dim3();
                        }
                }
                return _instance;
        }
        /**
        * Returns an element as a matrix (vector*basis).
        */
        public ComplexSquareMatrix getElement(final DoubleVector v) {
                ComplexMatrix m=basisMatrices[0].scalarMultiply(v.getComponent(0));
                m=m.add(basisMatrices[1].scalarMultiply(v.getComponent(1)));
                m=m.add(basisMatrices[2].scalarMultiply(v.getComponent(2)));
                m=m.add(basisMatrices[3].scalarMultiply(v.getComponent(3)));
                m=m.add(basisMatrices[4].scalarMultiply(v.getComponent(4)));
                m=m.add(basisMatrices[5].scalarMultiply(v.getComponent(5)));
                m=m.add(basisMatrices[6].scalarMultiply(v.getComponent(6)));
                m=m.add(basisMatrices[7].scalarMultiply(v.getComponent(7)));
                return (ComplexSquareMatrix)m.scalarMultiply(Complex.I);
        }
        /**
        * Returns the Lie bracket (commutator) of two elements.
        */
        public DoubleVector multiply(final DoubleVector a,final DoubleVector b) {
                double array[]=new double[8];
                array[0]=b.getComponent(1)*a.getComponent(2)-b.getComponent(2)*a.getComponent(1)+0.5*(
                        b.getComponent(3)*a.getComponent(6)-b.getComponent(6)*a.getComponent(3)+
                        b.getComponent(5)*a.getComponent(4)-b.getComponent(4)*a.getComponent(5));
                array[1]=b.getComponent(2)*a.getComponent(0)-b.getComponent(0)*a.getComponent(2)+0.5*(
                        b.getComponent(3)*a.getComponent(5)-b.getComponent(5)*a.getComponent(3)+
                        b.getComponent(4)*a.getComponent(6)-b.getComponent(6)*a.getComponent(4));
                array[2]=b.getComponent(0)*a.getComponent(1)-b.getComponent(1)*a.getComponent(0)+0.5*(
                        b.getComponent(3)*a.getComponent(4)-b.getComponent(4)*a.getComponent(3)+
                        b.getComponent(6)*a.getComponent(5)-b.getComponent(5)*a.getComponent(6));
                array[3]=Math.sqrt(0.75)*(b.getComponent(4)*a.getComponent(7)-b.getComponent(7)*a.getComponent(4))+0.5*(
                        b.getComponent(6)*a.getComponent(0)-b.getComponent(0)*a.getComponent(6)+
                        b.getComponent(5)*a.getComponent(1)-b.getComponent(1)*a.getComponent(5)+
                        b.getComponent(4)*a.getComponent(2)-b.getComponent(2)*a.getComponent(4));
                array[4]=Math.sqrt(0.75)*(b.getComponent(7)*a.getComponent(3)-b.getComponent(3)*a.getComponent(7))+0.5*(
                        b.getComponent(0)*a.getComponent(5)-b.getComponent(5)*a.getComponent(0)+
                        b.getComponent(6)*a.getComponent(1)-b.getComponent(1)*a.getComponent(6)+
                        b.getComponent(2)*a.getComponent(3)-b.getComponent(3)*a.getComponent(2));
                array[5]=Math.sqrt(0.75)*(b.getComponent(6)*a.getComponent(7)-b.getComponent(7)*a.getComponent(6))+0.5*(
                        b.getComponent(4)*a.getComponent(0)-b.getComponent(0)*a.getComponent(4)+
                        b.getComponent(1)*a.getComponent(3)-b.getComponent(3)*a.getComponent(1)+
                        b.getComponent(2)*a.getComponent(6)-b.getComponent(6)*a.getComponent(2));
                array[6]=Math.sqrt(0.75)*(b.getComponent(7)*a.getComponent(5)-b.getComponent(5)*a.getComponent(7))+0.5*(
                        b.getComponent(0)*a.getComponent(3)-b.getComponent(3)*a.getComponent(0)+
                        b.getComponent(1)*a.getComponent(4)-b.getComponent(4)*a.getComponent(1)+
                        b.getComponent(5)*a.getComponent(2)-b.getComponent(2)*a.getComponent(5));
                array[7]=Math.sqrt(0.75)*(b.getComponent(3)*a.getComponent(4)-b.getComponent(4)*a.getComponent(3)+
                        b.getComponent(5)*a.getComponent(6)-b.getComponent(6)*a.getComponent(5));
                return new DoubleVector(array);
        }
        /**
        * Returns the basis used to represent the Lie algebra.
        */
        public ComplexSquareMatrix[] basis() {
                return basisMatrices;
        }
}

