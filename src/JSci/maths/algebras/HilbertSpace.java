package JSci.maths.algebras;

import JSci.maths.*;
import JSci.maths.groups.*;

/**
* The HilbertSpace class encapsulates Hilbert spaces.
* @planetmath HilbertSpace
* @version 1.0
* @author Mark Hale
*/
public class HilbertSpace extends Object implements BanachSpace {
        private int dim;
        private ComplexVector ZERO;
        /**
        * Constructs a Hilbert space.
        */
        public HilbertSpace(int n) {
                dim=n;
                ZERO=new ComplexVector(dim);
        }
        /**
        * Returns a vector from the Hilbert space.
        */
        public VectorSpace.Member getVector(Complex array[]) {
                return new ComplexVector(array);
        }
        /**
        * Returns the dimension.
        */
        public int dimension() {
                return dim;
        }
        /**
        * Returns the zero vector.
        */
        public AbelianGroup.Member zero() {
                return ZERO;
        }
        /**
        * Returns true if the vector is equal to zero.
        */
        public boolean isZero(AbelianGroup.Member v) {
                return ZERO.equals(v);
        }
        /**
        * Returns true if one vector is the negative of the other.
        */
        public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
                return ZERO.equals(a.add(b));
        }

        /**
        * This interface defines a member of a Hilbert space.
        */
        public interface Member extends BanachSpace.Member {
                /**
                * The scalar product law.
                * @param v a Hilbert space vector
                */
                Complex scalarProduct(Member v);
        }
}

