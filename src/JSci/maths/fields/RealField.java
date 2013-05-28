package JSci.maths.fields;

import JSci.maths.*;
import JSci.maths.groups.AbelianGroup;

/**
* The RealField class encapsulates the field of real numbers.
* @version 1.0
* @author Mark Hale
*/
public final class RealField extends Object implements Field {
        public final static MathDouble ZERO=new MathDouble(0.0);
        public final static MathDouble ONE=new MathDouble(1.0);
        public final static MathDouble PI=new MathDouble(Math.PI);
        public final static MathDouble E=new MathDouble(Math.E);
        public final static MathDouble GAMMA=new MathDouble(NumericalConstants.GAMMA);
        public final static MathDouble INFINITY=new MathDouble(Double.POSITIVE_INFINITY);
        public final static MathDouble NaN=new MathDouble(Double.NaN);

        private static RealField _instance;
        /**
        * Constructs a field of real numbers.
        */
        private RealField() {}
        /**
        * Constructs a field of real numbers.
        * Singleton.
        */
        public static final RealField getInstance() {
                if(_instance == null) {
                        synchronized(RealField.class) {
                                if(_instance == null)
                                        _instance = new RealField();
                        }
                }
                return _instance;
        }
        /**
        * Returns the real number zero.
        */
        public AbelianGroup.Member zero() {
                return ZERO;
        }
        /**
        * Returns true if the real number is equal to zero.
        */
        public boolean isZero(AbelianGroup.Member g) {
                return ZERO.equals(g);
        }
        /**
        * Returns true if one real number is the negative of the other.
        */
        public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
                return ZERO.equals(a.add(b));
        }
        /**
        * Returns the real number one.
        */
        public Ring.Member one() {
                return ONE;
        }
        /**
        * Returns true if the real number is equal to one.
        */
        public boolean isOne(Ring.Member r) {
                return ONE.equals(r);
        }
        /**
        * Returns true if one real number is the inverse of the other.
        */
        public boolean isInverse(Field.Member a, Field.Member b) {
                return ONE.equals(a.multiply(b));
        }
}

