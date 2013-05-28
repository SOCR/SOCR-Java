package JSci.physics.quantum;

import JSci.maths.*;

/**
* The Quantum math library.
* This class cannot be subclassed or instantiated because all methods are static.
* @version 1.5
* @author Mark Hale
*/
public final class QuantumMath extends AbstractMath {
        private QuantumMath() {}

// COMMUTATOR

        /**
        * Returns the commutator [A,B].
        * @param A an operator
        * @param B an operator
        */
        public static Operator commutator(final Operator A, final Operator B) {
                return (A.multiply(B)).subtract(B.multiply(A));
        }

// ANTICOMMUTATOR

        /**
        * Returns the anticommutator {A,B}.
        * @param A an operator
        * @param B an operator
        */
        public static Operator anticommutator(final Operator A, final Operator B) {
                return (A.multiply(B)).add(B.multiply(A));
        }

// EXPECTATION VALUES

        /**
        * Returns the expectation value.
        * @param op an operator
        * @param ket a ket vector
        * @exception DimensionException If the operator and vector have different dimensions.
        */
        public static Complex expectation(final Operator op, final KetVector ket) {
                return ket.toBraVector().multiply(op).multiply(ket);
        }
        /**
        * Returns the expectation value.
        * @param dm a density matrix
        * @param op an operator
        * @exception MatrixDimensionException If the operator and matrix have different dimensions.
        */
        public static Complex expectation(final DensityMatrix dm, final Operator op) {
                return dm.multiply(op).trace();
        }

// PROBABILITIES

        /**
        * Returns the probability.
        * @param p a projector
        * @param ket a ket vector
        * @exception DimensionException If the projector and vector have different dimensions.
        */
        public static Complex probability(final Projector p, final KetVector ket) {
                return ket.toBraVector().multiply(p).multiply(ket);
        }
        /**
        * Returns the probability.
        * @param dm a density matrix
        * @param p a projector
        * @exception MatrixDimensionException If the projector and matrix have different dimensions.
        */
        public static Complex probability(final DensityMatrix dm, final Projector p) {
                return dm.multiply(p).trace();
        }
}

