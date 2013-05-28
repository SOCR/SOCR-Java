package JSci.maths.algebras;

import JSci.maths.*;
import JSci.maths.fields.ComplexField;

/**
* The sp2_RDim3 class encapsulates sp(2,R) algebras using
* the 3 dimensional (adjoint) representation.
* Elements are represented by 3-vectors with a matrix basis.
* @version 1.2
* @author Mark Hale
*/
public final class sp2_RDim3 extends LieAlgebra {
        private final static Complex t1[][]={
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_HALF},
                {Complex.ZERO,Complex.ZERO,ComplexField.HALF},
                {ComplexField.MINUS_TWO,ComplexField.TWO,Complex.ZERO}
        };
        private final static Complex t2[][]={
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_HALF},
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_HALF},
                {ComplexField.TWO,ComplexField.TWO,Complex.ZERO}
        };
        private final static Complex t3[][]={
                {Complex.ONE,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,ComplexField.MINUS_ONE,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO}
        };
        /**
        * Basis.
        */
        private final static ComplexSquareMatrix basisMatrices[]={
                new ComplexSquareMatrix(t1),
                new ComplexSquareMatrix(t2),
                new ComplexSquareMatrix(t3)
        };

        private static sp2_RDim3 _instance;
        /**
        * Constructs an sp(2,R) algebra.
        */
        private sp2_RDim3() {
                super("sp(2,R) [3]");
        }
        /**
        * Singleton.
        */
        public static final sp2_RDim3 getInstance() {
                if(_instance == null) {
                        synchronized(sp2_RDim3.class) {
                                if(_instance == null)
                                        _instance = new sp2_RDim3();
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
                return (ComplexSquareMatrix)m.scalarMultiply(Complex.I);
        }
        /**
        * Returns the Lie bracket (commutator) of two elements.
        * Same as the vector cross product.
        */
        public DoubleVector multiply(final DoubleVector a,final DoubleVector b) {
                if(!(a instanceof Double3Vector) || !(b instanceof Double3Vector))
                        throw new VectorDimensionException("Vectors must be 3-vectors.");
                return new Double3Vector(
                        a.getComponent(2)*b.getComponent(1)-a.getComponent(1)*b.getComponent(2),
                        a.getComponent(2)*b.getComponent(0)-a.getComponent(0)*b.getComponent(2),
                        a.getComponent(1)*b.getComponent(0)-a.getComponent(0)*b.getComponent(1)
                );
        }
        /**
        * Returns the basis used to represent the Lie algebra.
        */
        public ComplexSquareMatrix[] basis() {
                return basisMatrices;
        }
}

