package JSci.maths.fields;

import JSci.maths.Complex;
import JSci.maths.groups.AbelianGroup;

/**
* The ComplexField class encapsulates the field of complex numbers.
* @version 1.0
* @author Mark Hale
*/
public final class ComplexField extends Object implements Field {
        public static final Complex ZERO=new Complex(0.0,0.0);
        public static final Complex I=new Complex(0.0,1.0);
        public static final Complex ONE=new Complex(1.0,0.0);
        public static final Complex MINUS_ONE=new Complex(-1.0,0.0);
        public static final Complex MINUS_I=new Complex(0.0,-1.0);
        public static final Complex HALF=new Complex(0.5,0.0);
        public static final Complex MINUS_HALF=new Complex(-0.5,0.0);
        public static final Complex HALF_I=new Complex(0.0,0.5);
        public static final Complex MINUS_HALF_I=new Complex(0.0,-0.5);
        public static final Complex TWO=new Complex(2.0,0.0);
        public static final Complex MINUS_TWO=new Complex(-2.0,0.0);
        public static final Complex SQRT_HALF=new Complex(Math.sqrt(0.5),0.0);
        public static final Complex SQRT_HALF_I=new Complex(0.0,Math.sqrt(0.5));
        public static final Complex MINUS_SQRT_HALF_I=new Complex(0.0,-Math.sqrt(0.5));
        public static final Complex PI=new Complex(Math.PI,0.0);
        public static final Complex PI_I=new Complex(0.0,Math.PI);
        public static final Complex PI_2=new Complex(Math.PI/2.0,0.0);
        public static final Complex MINUS_PI_2=new Complex(-Math.PI/2.0,0.0);
        public static final Complex PI_2_I=new Complex(0.0,Math.PI/2.0);
        public static final Complex MINUS_PI_2_I=new Complex(0.0,-Math.PI/2.0);

        private static ComplexField _instance;
        /**
        * Constructs a field of complex numbers.
        */
        private ComplexField() {}
        /**
        * Constructs a field of complex numbers.
        * Singleton.
        */
        public static final ComplexField getInstance() {
                if(_instance == null) {
                        synchronized(ComplexField.class) {
                                if(_instance == null)
                                        _instance = new ComplexField();
                        }
                }
                return _instance;
        }
        /**
        * Returns the complex number zero.
        */
        public AbelianGroup.Member zero() {
                return ZERO;
        }
        /**
        * Returns true if the complex number is equal to zero.
        */
        public boolean isZero(AbelianGroup.Member g) {
                return ZERO.equals(g);
        }
        /**
        * Returns true if one complex number is the negative of the other.
        */
        public boolean isNegative(AbelianGroup.Member a,AbelianGroup.Member b) {
                return ZERO.equals(a.add(b));
        }
        /**
        * Returns the complex number one.
        */
        public Ring.Member one() {
                return ONE;
        }
        /**
        * Returns true if the complex number is equal to one.
        */
        public boolean isOne(Ring.Member r) {
                return ONE.equals(r);
        }
        /**
        * Returns true if one complex number is the inverse of the other.
        */
        public boolean isInverse(Field.Member a, Field.Member b) {
                return ONE.equals(a.multiply(b));
        }
}

