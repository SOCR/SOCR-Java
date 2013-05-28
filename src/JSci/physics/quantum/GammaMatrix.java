package JSci.physics.quantum;

import JSci.maths.*;
import JSci.maths.fields.ComplexField;

/**
* The GammaMatrix class provides an object for encapsulating the gamma matrices.
* @version 1.2
* @author Mark Hale
*/
public final class GammaMatrix extends ComplexSquareMatrix {
        private static final Complex y0_D[][]={
                {ComplexField.ONE,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ONE,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_ONE,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_ONE}
        };
        private static final Complex y1_D[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ONE},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ONE,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.MINUS_ONE,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.MINUS_ONE,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO}
        };
        private static final Complex y2_D[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.I},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.I,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO}
        };
        private static final Complex y3_D[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ONE,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_ONE},
                {ComplexField.MINUS_ONE,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ONE,ComplexField.ZERO,ComplexField.ZERO}
        };
        private static final Complex y5_D[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ONE,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ONE},
                {ComplexField.ONE,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ONE,ComplexField.ZERO,ComplexField.ZERO}
        };
        private static final Complex y0_M[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.I},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.I,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.MINUS_I,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO}
        };
        private static final Complex y1_M[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.I,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_I},
                {ComplexField.I,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO,ComplexField.ZERO}
        };
        private static final Complex y2_M[][]={
                {ComplexField.I,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.I,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_I}
        };
        private static final Complex y3_M[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_I},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.MINUS_I,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO}
        };
        private static final Complex y5_M[][]={
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.I,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO,ComplexField.I},
                {ComplexField.MINUS_I,ComplexField.ZERO,ComplexField.ZERO,ComplexField.ZERO},
                {ComplexField.ZERO,ComplexField.MINUS_I,ComplexField.ZERO,ComplexField.ZERO}
        };
        /**
        * Gamma 0 matrix (Dirac representation).
        */
        public static final GammaMatrix Y0_D=new GammaMatrix(y0_D);
        /**
        * Gamma 1 matrix (Dirac representation).
        */
        public static final GammaMatrix Y1_D=new GammaMatrix(y1_D);
        /**
        * Gamma 2 matrix (Dirac representation).
        */
        public static final GammaMatrix Y2_D=new GammaMatrix(y2_D);
        /**
        * Gamma 3 matrix (Dirac representation).
        */
        public static final GammaMatrix Y3_D=new GammaMatrix(y3_D);
        /**
        * Gamma 5 matrix (Dirac representation).
        */
        public static final GammaMatrix Y5_D=new GammaMatrix(y5_D);
        /**
        * Gamma 0 matrix (Weyl representation).
        */
        public static final GammaMatrix Y0_W=Y5_D;
        /**
        * Gamma 1 matrix (Weyl representation).
        */
        public static final GammaMatrix Y1_W=Y1_D;
        /**
        * Gamma 2 matrix (Weyl representation).
        */
        public static final GammaMatrix Y2_W=Y2_D;
        /**
        * Gamma 3 matrix (Weyl representation).
        */
        public static final GammaMatrix Y3_W=Y3_D;
        /**
        * Gamma 5 matrix (Weyl representation).
        */
        public static final GammaMatrix Y5_W=Y0_D;
        /**
        * Gamma 0 matrix (Majorana representation).
        */
        public static final GammaMatrix Y0_M=new GammaMatrix(y0_M);
        /**
        * Gamma 1 matrix (Majorana representation).
        */
        public static final GammaMatrix Y1_M=new GammaMatrix(y1_M);
        /**
        * Gamma 2 matrix (Majorana representation).
        */
        public static final GammaMatrix Y2_M=new GammaMatrix(y2_M);
        /**
        * Gamma 3 matrix (Majorana representation).
        */
        public static final GammaMatrix Y3_M=new GammaMatrix(y3_M);
        /**
        * Gamma 5 matrix (Majorana representation).
        */
        public static final GammaMatrix Y5_M=new GammaMatrix(y5_M);
        /**
        * Constructs a gamma matrix.
        */
        private GammaMatrix(Complex gammaArray[][]) {
                super(gammaArray);
        }
        /**
        * Returns true if this matrix is unitary.
        */
        public boolean isUnitary() {
                return true;
        }
        /**
        * Returns the determinant.
        */
        public Complex det() {
                return ComplexField.MINUS_ONE;
        }
        /**
        * Returns the trace.
        */
        public Complex trace() {
                return ComplexField.ZERO;
        }
}

