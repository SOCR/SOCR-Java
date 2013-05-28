package JSci.maths.fields;

import JSci.maths.*;
import JSci.maths.groups.AbelianGroup;

/**
* The IntegerRing class encapsulates the ring of integer numbers.
* @version 1.0
* @author Mark Hale
*/
public final class IntegerRing extends Object implements Ring {
        public final static MathInteger ZERO=new MathInteger(0);
        public final static MathInteger ONE=new MathInteger(1);

        private static IntegerRing _instance;
        /**
        * Constructs a ring of integer numbers.
        */
        private IntegerRing() {}
        /**
        * Constructs a ring of integer numbers.
        * Singleton.
        */
        public static final IntegerRing getInstance() {
                if(_instance == null) {
                        synchronized(IntegerRing.class) {
                                if(_instance == null)
                                        _instance = new IntegerRing();
                        }
                }
                return _instance;
        }
        /**
        * Returns the integer number zero.
        */
        public AbelianGroup.Member zero() {
                return ZERO;
        }
        /**
        * Returns true if the integer number is equal to zero.
        */
        public boolean isZero(AbelianGroup.Member g) {
                return ZERO.equals(g);
        }
        /**
        * Returns true if one integer number is the negative of the other.
        */
        public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
                return ZERO.equals(a.add(b));
        }
        /**
        * Returns the integer number one.
        */
        public Ring.Member one() {
                return ONE;
        }
        /**
        * Returns true if the integer number is equal to one.
        */
        public boolean isOne(Ring.Member r) {
                return ONE.equals(r);
        }
}

