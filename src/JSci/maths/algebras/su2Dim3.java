package JSci.maths.algebras;

import JSci.maths.*;
import JSci.maths.fields.ComplexField;

/**
* The su2Dim3 class encapsulates su(2) algebras using
* the 3 dimensional (adjoint) representation.
* Elements are represented by 3-vectors with a matrix basis.
* @version 1.2
* @author Mark Hale
*/
public final class su2Dim3 extends LieAlgebra {
        /**
        * Basis array.
        */
        private final static Complex t1[][]={
                {Complex.ZERO,ComplexField.SQRT_HALF,Complex.ZERO},
                {ComplexField.SQRT_HALF,Complex.ZERO,ComplexField.SQRT_HALF},
                {Complex.ZERO,ComplexField.SQRT_HALF,Complex.ZERO}
        };
        private final static Complex t2[][]={
                {Complex.ZERO,ComplexField.MINUS_SQRT_HALF_I,Complex.ZERO},
                {ComplexField.SQRT_HALF_I,Complex.ZERO,ComplexField.MINUS_SQRT_HALF_I},
                {Complex.ZERO,ComplexField.SQRT_HALF_I,Complex.ZERO}
        };
        private final static Complex t3[][]={
                {Complex.ONE,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,Complex.ZERO},
                {Complex.ZERO,Complex.ZERO,ComplexField.MINUS_ONE}
        };
        /**
        * Basis.
        */
        private final static ComplexSquareMatrix basisMatrices[]={
                new ComplexSquareMatrix(t1),
                new ComplexSquareMatrix(t2),
                new ComplexSquareMatrix(t3)
        };
        /**
        * Metric array.
        */
        private final static double g[][]={
                {-2.0,0.0,0.0},
                {0.0,-2.0,0.0},
                {0.0,0.0,-2.0}
        };
        /**
        * Cartan metric.
        */
        private final static DoubleSquareMatrix metricMatrix=new DoubleSquareMatrix(g);

        private static su2Dim3 _instance;
        /**
        * Constructs an su(2) algebra.
        */
        private su2Dim3() {
                super("su(2) [3]");
        }
        /**
        * Singleton.
        */
        public static final su2Dim3 getInstance() {
                if(_instance == null) {
                        synchronized(su2Dim3.class) {
                                if(_instance == null)
                                        _instance = new su2Dim3();
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
                return ((Double3Vector)b).multiply((Double3Vector)a);
        }
        /**
        * Returns the Killing Form of two elements (scalar product).
        */
        public double killingForm(final DoubleVector a,final DoubleVector b) {
                return a.scalarProduct(metricMatrix.multiply(b));
        }
        /**
        * Returns the basis used to represent the Lie algebra.
        */
        public ComplexSquareMatrix[] basis() {
                return basisMatrices;
        }
        /**
        * Returns the Cartan metric.
        */
        public DoubleSquareMatrix cartanMetric() {
                return metricMatrix;
        }
}

