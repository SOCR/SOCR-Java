package JSci.maths.algebras;

import JSci.maths.*;
import JSci.maths.fields.ComplexField;

/**
* The so3_1Dim4 class encapsulates so(3,1) algebras using
* the 4 dimensional (fundamental) representation.
* Elements are represented by vectors with a matrix basis.
* @version 1.2
* @author Mark Hale
*/
public final class so3_1Dim4 extends LieAlgebra {
// Rotations
        private final static Complex t1[][]={
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,ComplexField.MINUS_I},
                {Complex.ZERO,Complex.ZERO,Complex.I,Complex.ZERO}
        };
        private final static Complex t2[][]={
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.I},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,ComplexField.MINUS_I,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t3[][]={
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_I,Complex.ZERO},
                {Complex.ZERO,Complex.I,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO}
        };
// Boosts
        private final static Complex t4[][]={
                {Complex.ZERO,ComplexField.MINUS_I,Complex.ZERO,Complex.ZERO},
                {ComplexField.MINUS_I,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t5[][]={
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_I,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {ComplexField.MINUS_I,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO}
        };
        private final static Complex t6[][]={
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,ComplexField.MINUS_I},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {ComplexField.MINUS_I,Complex.ZERO,Complex.ZERO,Complex.ZERO}
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
                new ComplexSquareMatrix(t6)
        };

        private static so3_1Dim4 _instance;
        /**
        * Constructs an so(3,1) algebra.
        */
        private so3_1Dim4() {
                super("so(3,1) [4]");
        }
        /**
        * Singleton.
        */
        public static final so3_1Dim4 getInstance() {
                if(_instance == null) {
                        synchronized(so3_1Dim4.class) {
                                if(_instance == null)
                                        _instance = new so3_1Dim4();
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
                return (ComplexSquareMatrix)m.scalarMultiply(Complex.I);
        }
        /**
        * Returns the Lie bracket (commutator) of two elements.
        * Same as the vector cross product.
        */
        public DoubleVector multiply(final DoubleVector a,final DoubleVector b) {
                double array[]=new double[6];
                array[0]=a.getComponent(2)*b.getComponent(1)-a.getComponent(1)*b.getComponent(2)+
                        a.getComponent(4)*b.getComponent(5)-a.getComponent(5)*b.getComponent(4);
                array[1]=a.getComponent(0)*b.getComponent(2)-a.getComponent(2)*b.getComponent(0)+
                        a.getComponent(5)*b.getComponent(3)-a.getComponent(3)*b.getComponent(5);
                array[2]=a.getComponent(1)*b.getComponent(0)-a.getComponent(0)*b.getComponent(1)+
                        a.getComponent(3)*b.getComponent(4)-a.getComponent(4)*b.getComponent(3);
                array[3]=a.getComponent(2)*b.getComponent(4)-a.getComponent(1)*b.getComponent(5)+
                        a.getComponent(5)*b.getComponent(1)-a.getComponent(4)*b.getComponent(2);
                array[4]=a.getComponent(0)*b.getComponent(5)-a.getComponent(2)*b.getComponent(3)+
                        a.getComponent(3)*b.getComponent(2)-a.getComponent(5)*b.getComponent(0);
                array[5]=a.getComponent(1)*b.getComponent(3)-a.getComponent(0)*b.getComponent(4)+
                        a.getComponent(4)*b.getComponent(0)-a.getComponent(3)*b.getComponent(1);
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