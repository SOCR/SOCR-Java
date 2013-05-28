package JSci.physics.quantum;

import JSci.maths.*;
import JSci.maths.algebras.*;

/**
* The SpinOperator class provides an object for encapsulating spin operators.
* @version 2.0
* @author Mark Hale
*/
public final class SpinOperator extends Operator {
        private static final LieAlgebra spin1_2=su2Dim2.getInstance();
        private static final LieAlgebra spin1=su2Dim3.getInstance();
        /**
        * Spin 1/2 operator (x).
        */
        public static final SpinOperator X1_2=new SpinOperator(spin1_2.basis()[0]);
        /**
        * Spin 1/2 operator (y).
        */
        public static final SpinOperator Y1_2=new SpinOperator(spin1_2.basis()[1]);
        /**
        * Spin 1/2 operator (z).
        */
        public static final SpinOperator Z1_2=new SpinOperator(spin1_2.basis()[2]);
        /**
        * Spin 1 operator (x).
        */
        public static final SpinOperator X1=new SpinOperator(spin1.basis()[0]);
        /**
        * Spin 1 operator (y).
        */
        public static final SpinOperator Y1=new SpinOperator(spin1.basis()[1]);
        /**
        * Spin 1 operator (z).
        */
        public static final SpinOperator Z1=new SpinOperator(spin1.basis()[2]);
        /**
        * Constructs a spin operator.
        */
        private SpinOperator(ComplexSquareMatrix spinMatrix) {
                super(spinMatrix);
        }
        /**
        * Returns true if this operator is self-adjoint.
        */
        public boolean isSelfAdjoint() {
                return true;
        }
        /**
        * Returns true if this operator is unitary.
        */
        public boolean isUnitary() {
                return true;
        }
        /**
        * Returns the trace.
        */
        public Complex trace() {
                return Complex.ZERO;
        }
}

