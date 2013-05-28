package JSci.maths.groups;

import JSci.maths.*;

/**
* The LieGroup class provides an encapsulation for Lie groups.
* Elements are represented by complex matrices, and are limited
* to being near the identity.
* @planetmath LieGroup
* @version 1.3
* @author Mark Hale
*/
public class LieGroup extends Object {
        private ComplexSquareMatrix generators[];
        private ComplexSquareMatrix identityMatrix;
        /**
        * Constructs a Lie group from a Lie algebra.
        * @param gens the group generators
        */
        public LieGroup(ComplexSquareMatrix gens[]) {
                generators=gens;
                identityMatrix=ComplexDiagonalMatrix.identity(generators[0].rows());
        }
        /**
        * Returns the dimension of the group.
        */
        public final int dimension() {
                return generators.length;
        }
        /**
        * Returns an element near the identity.
        * @param v a small element from the Lie algebra
        */
        public ComplexSquareMatrix getElement(DoubleVector v) {
                if(generators.length!=v.dimension())
                        throw new IllegalArgumentException("The vector should match the generators.");
                ComplexMatrix phase=generators[0].scalarMultiply(v.getComponent(0));
                for(int i=1;i<generators.length;i++)
                        phase=phase.add(generators[i].scalarMultiply(v.getComponent(i)));
                return (ComplexSquareMatrix)identityMatrix.add(phase.scalarMultiply(Complex.I));
        }
        /**
        * Returns the identity element.
        */
        public ComplexSquareMatrix identity() {
                return identityMatrix;
        }
        /**
        * Returns true if the element is the identity element of this group.
        * @param a a group element
        */
        public final boolean isIdentity(ComplexSquareMatrix a) {
                return identityMatrix.equals(a);
        }
        /**
        * Returns true if one element is the inverse of the other.
        * @param a a group element
        * @param b a group element
        */
        public final boolean isInverse(ComplexSquareMatrix a,ComplexSquareMatrix b) {
                return isIdentity(a.multiply(b));
        }
}

