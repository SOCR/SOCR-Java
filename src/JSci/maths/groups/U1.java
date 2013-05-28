package JSci.maths.groups;

import JSci.maths.*;

/**
* The U1 class provides an encapsulation for U(1) groups.
* Unlike the parent LieGroup class, elements are not limited to
* being near the identity.
* The methods in this class assume a complex number representation
* for convenience.
* The LieGroup methods still provide a matrix representation.
* @version 1.3
* @author Mark Hale
*/
public final class U1 extends LieGroup {
        private static final ComplexSquareMatrix U1gens[]={ComplexDiagonalMatrix.identity(1)};

        private static U1 _instance;
        /**
        * Constructs a U(1) group.
        */
        private U1() {
                super(U1gens);
        }
        /**
        * Constructs a U(1) group.
        * Singleton.
        */
        public static final U1 getInstance() {
                if(_instance == null) {
                        synchronized(U1.class) {
                                if(_instance == null)
                                        _instance = new U1();
                        }
                }
                return _instance;
        }
        /**
        * Returns a string representing this group.
        */
        public String toString() {
                return "U(1)";
        }
        /**
        * Returns an element from within the group.
        * @param param parameter to specify the group element
        */
        public Complex getElement(double param) {
                return Complex.polar(1.0,param);
        }
        /**
        * Returns true if the element is the identity element of this group.
        * @param a a group element
        */
        public boolean isIdentity(Complex a) {
                return Complex.ONE.equals(a);
        }
        /**
        * Returns true if one element is the inverse of the other.
        * @param a a group element
        * @param b a group element
        */
        public boolean isInverse(Complex a,Complex b) {
                return Complex.ONE.equals(a.multiply(b));
        }
}

