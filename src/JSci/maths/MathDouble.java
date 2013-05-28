package JSci.maths;

import JSci.GlobalSettings;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.fields.*;

/**
* The MathDouble class encapsulates double numbers.
* @see JSci.maths.fields.RealField
* @version 1.1
* @author Mark Hale
*/
public final class MathDouble extends Object implements Comparable, Field.Member {
        private static final long serialVersionUID = 8616680319093653108L;

        private final double x;
        /**
        * Constructs a double number.
        */
        public MathDouble(final double num) {
                x=num;
        }
        /**
        * Constructs the double number represented by a string.
        * @param s a string representing a double number.
        * @exception NumberFormatException if the string does not contain a parsable number.
        */
        public MathDouble(final String s) throws NumberFormatException {
                x=Double.parseDouble(s);
        }
        /**
        * Compares two double numbers for equality.
        * @param obj a double number.
        */
        public boolean equals(Object obj) {
                if(obj!=null && (obj instanceof MathDouble)) {
                        return Math.abs(x-((MathDouble)obj).value())<=GlobalSettings.ZERO_TOL;
                } else
                        return false;
        }
        /**
        * Compares two double numbers.
        * @param obj a double number.
        * @return a negative value if <code>this&lt;obj</code>,
        * zero if <code>this==obj</code>,
        * and a positive value if <code>this&gt;obj</code>.
        */
        public int compareTo(Object obj) throws IllegalArgumentException {
                if(obj!=null && (obj instanceof MathDouble)) {
                        if(Math.abs(x-((MathDouble)obj).value())<=GlobalSettings.ZERO_TOL)
                                return 0;
                        else
                                return (int)(x-((MathDouble)obj).value());
                } else
                        throw new IllegalArgumentException("Invalid object.");
        }
        /**
        * Returns a string representing the value of this double number.
        */
        public String toString() {
                return Double.toString(x);
        }
        /**
        * Returns the double value.
        */
        public double value() {
                return x;
        }
        /**
        * Returns true if this number is NaN.
        */
        public boolean isNaN() {
                return (x==Double.NaN);
        }
        /**
        * Returns true if this number is infinite.
        */
        public boolean isInfinite() {
                return (x==Double.POSITIVE_INFINITY) || (x==Double.NEGATIVE_INFINITY);
        }
        /**
        * Returns the negative of this number.
        */
        public AbelianGroup.Member negate() {
                return new MathDouble(-x);
        }
        /**
        * Returns the inverse of this number.
        */
        public Field.Member inverse() {
                return new MathDouble(1.0/x);
        }
        /**
        * Returns the addition of this number and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member n) {
                if(n instanceof MathDouble)
                        return add((MathDouble)n);
                else if(n instanceof MathInteger)
                        return add(new MathDouble(((MathInteger)n).value()));
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this double number and another.
        */
        public MathDouble add(final MathDouble n) {
                return new MathDouble(x+n.x);
        }
        /**
        * Returns the subtraction of this number and another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member n) {
                if(n instanceof MathDouble)
                        return subtract((MathDouble)n);
                else if(n instanceof MathInteger)
                        return subtract(new MathDouble(((MathInteger)n).value()));
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this double number and another.
        */
        public MathDouble subtract(final MathDouble n) {
                return new MathDouble(x-n.x);
        }
        /**
        * Returns the multiplication of this number and another.
        */
        public Ring.Member multiply(final Ring.Member n) {
                if(n instanceof MathDouble)
                        return multiply((MathDouble)n);
                else if(n instanceof MathInteger)
                        return multiply(new MathDouble(((MathInteger)n).value()));
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this double number and another.
        */
        public MathDouble multiply(final MathDouble n) {
                return new MathDouble(x*n.x);
        }
        /**
        * Returns the division of this number and another.
        */
        public Field.Member divide(final Field.Member n) {
                if(n instanceof MathDouble)
                        return divide((MathDouble)n);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the division of this double number and another.
        */
        public MathDouble divide(final MathDouble n) {
                return new MathDouble(x/n.x);
        }

//===========
// FUNCTIONS
//===========

// EXP

        /**
        * Returns the exponential number e(2.718...) raised to the power of a number.
        */
        public static MathDouble exp(final MathDouble x) {
                return new MathDouble(Math.exp(x.x));
        }

// LOG

        /**
        * Returns the natural logarithm (base e) of a number.
        */
        public static MathDouble log(final MathDouble x) {
                return new MathDouble(Math.log(x.x));
        }

// SIN

        /**
        * Returns the trigonometric sine of an angle.
        * @param x an angle that is measured in radians
        */
        public static MathDouble sin(final MathDouble x) {
                return new MathDouble(Math.sin(x.x));
        }

// COS

        /**
        * Returns the trigonometric cosine of an angle.
        * @param x an angle that is measured in radians
        */
        public static MathDouble cos(final MathDouble x) {
                return new MathDouble(Math.cos(x.x));
        }

// TAN

        /**
        * Returns the trigonometric tangent of an angle.
        * @param x an angle that is measured in radians
        */
        public static MathDouble tan(final MathDouble x) {
                return new MathDouble(Math.tan(x.x));
        }

// SINH

        /**
        * Returns the hyperbolic sine of a number.
        */
        public static MathDouble sinh(final MathDouble x) {
                return new MathDouble(ExtraMath.sinh(x.x));
        }

// COSH

        /**
        * Returns the hyperbolic cosine of a number.
        */
        public static MathDouble cosh(final MathDouble x) {
                return new MathDouble(ExtraMath.cosh(x.x));
        }

// TANH

        /**
        * Returns the hyperbolic tangent of a number.
        */
        public static MathDouble tanh(final MathDouble x) {
                return new MathDouble(ExtraMath.tanh(x.x));
        }

// INVERSE SIN

        /**
        * Returns the arc sine of a number.
        */
        public static MathDouble asin(final MathDouble x) {
                return new MathDouble(Math.asin(x.x));
        }

// INVERSE COS

        /**
        * Returns the arc cosine of a number.
        */
        public static MathDouble acos(final MathDouble x) {
                return new MathDouble(Math.acos(x.x));
        }

// INVERSE TAN

        /**
        * Returns the arc tangent of a number.
        */
        public static MathDouble atan(final MathDouble x) {
                return new MathDouble(Math.atan(x.x));
        }

// INVERSE SINH

        /**
        * Returns the arc hyperbolic sine of a number.
        */
        public static MathDouble asinh(final MathDouble x) {
                return new MathDouble(ExtraMath.asinh(x.x));
        }

// INVERSE COSH

        /**
        * Returns the arc hyperbolic cosine of a number.
        */
        public static MathDouble acosh(final MathDouble x) {
                return new MathDouble(ExtraMath.acosh(x.x));
        }

// INVERSE TANH

        /**
        * Returns the arc hyperbolic tangent of a number.
        */
        public static MathDouble atanh(final MathDouble x) {
                return new MathDouble(ExtraMath.atanh(x.x));
        }
}

