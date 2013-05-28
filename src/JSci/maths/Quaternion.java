package JSci.maths;

import JSci.GlobalSettings;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.fields.*;
import JSci.maths.algebras.*;

/**
* The Quaternion class encapsulates quaternions.
* @planetmath Quaternions
* @version 1.1
* @author Mark Hale
*
* Quaternions are a noncommutative C<sup>*</sup>-algebra over the reals.
*/
public final class Quaternion extends Object implements Field.Member, CStarAlgebra.Member {
        private static final long serialVersionUID = 1605315490425547301L;

        private double re;
        private double imi, imj, imk;

        public static final Quaternion ONE=new Quaternion(1.0, 0.0, 0.0, 0.0);
        public static final Quaternion I=new Quaternion(0.0, 1.0, 0.0, 0.0);
        public static final Quaternion J=new Quaternion(0.0, 0.0, 1.0, 0.0);
        public static final Quaternion K=new Quaternion(0.0, 0.0, 0.0, 1.0);
        /**
        * Constructs a quaternion.
        */
        public Quaternion(final double real,final Double3Vector imag) {
                re=real;
                imi=imag.vector[0];
                imj=imag.vector[1];
                imk=imag.vector[2];
        }
        /**
        * Constructs the quaternion q<sub>0</sub>+iq<sub>1</sub>+jq<sub>2</sub>+kq<sub>3</sub>.
        */
        public Quaternion(final double q0,final double q1,final double q2,final double q3) {
                re=q0;
                imi=q1;
                imj=q2;
                imk=q3;
        }
        /**
        * Compares two quaternions for equality.
        * @param obj a quaternion
        */
        public boolean equals(Object obj) {
                if(obj!=null && (obj instanceof Quaternion)) {
                        final Quaternion q=(Quaternion)obj;
                        return Math.abs(re-q.re)<=GlobalSettings.ZERO_TOL &&
                                Math.abs(imi-q.imi)<=GlobalSettings.ZERO_TOL &&
                                Math.abs(imj-q.imj)<=GlobalSettings.ZERO_TOL &&
                                Math.abs(imk-q.imk)<=GlobalSettings.ZERO_TOL;
                } else
                        return false;
        }
        /**
        * Returns a string representing the value of this quaternion.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(40);
                buf.append(re);
                if(imi>=0.0)
                        buf.append("+");
                buf.append(imi);
                buf.append("i");
                if(imj>=0.0)
                        buf.append("+");
                buf.append(imj);
                buf.append("j");
                if(imk>=0.0)
                        buf.append("+");
                buf.append(imk);
                buf.append("k");
                return buf.toString();
        }
        /**
        * Returns a hashcode for this quaternion.
        */
        public int hashCode() {
                return (int)(Math.exp(norm()));
        }
        /**
        * Returns true if either the real or imaginary part is NaN.
        */
        public boolean isNaN() {
                return (re==Double.NaN) || (imi==Double.NaN) ||
                (imj==Double.NaN) || (imk==Double.NaN);
        }
        /**
        * Returns true if either the real or imaginary part is infinite.
        */
        public boolean isInfinite() {
                return (re==Double.POSITIVE_INFINITY) || (re==Double.NEGATIVE_INFINITY)
                        || (imi==Double.POSITIVE_INFINITY) || (imi==Double.NEGATIVE_INFINITY)
                        || (imj==Double.POSITIVE_INFINITY) || (imj==Double.NEGATIVE_INFINITY)
                        || (imk==Double.POSITIVE_INFINITY) || (imk==Double.NEGATIVE_INFINITY);
        }
        /**
        * Returns the real part of this quaternion.
        */
        public double real() {
                return re;
        }
        /**
        * Returns the imaginary part of this quaternion.
        */
        public Double3Vector imag() {
                return new Double3Vector(imi, imj, imk);
        }
        /**
        * Returns the l<sup>2</sup>-norm (magnitude),
        * which is also the C<sup>*</sup> norm.
        */
        public double norm() {
                return Math.sqrt(sumSquares());
        }
        /**
        * Returns the sum of the squares of the components.
        */
        public double sumSquares() {
                return re*re+imi*imi+imj*imj+imk*imk;
        }

//============
// OPERATIONS
//============

        /**
        * Returns the negative of this quaternion.
        */
        public AbelianGroup.Member negate() {
                return new Quaternion(-re, -imi, -imj, -imk);
        }
        /**
        * Returns the inverse of this quaternion.
        */
        public Field.Member inverse() {
                final double sumSqr=sumSquares();
                return new Quaternion(re/sumSqr, -imi/sumSqr, -imj/sumSqr, -imk/sumSqr);
        }
        /**
        * Returns the involution of this quaternion.
        */
        public CStarAlgebra.Member involution() {
                return conjugate();
        }
        /**
        * Returns the conjugate of this quaternion.
        */
        public Quaternion conjugate() {
                return new Quaternion(re, -imi, -imj, -imk);
        }

// ADDITION

        /**
        * Returns the addition of this number and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member x) {
                if(x instanceof Quaternion)
                        return add((Quaternion)x);
                else if(x instanceof MathDouble)
                        return addReal(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return addReal(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this quaternion and another.
        * @param q a quaternion
        */
        public Quaternion add(final Quaternion q) {
                return new Quaternion(re+q.re, imi+q.imi, imj+q.imj, imk+q.imk);
        }
        /**
        * Returns the addition of this quaternion with a real part.
        * @param real a real part
        */
        public Quaternion addReal(final double real) {
                return new Quaternion(re+real, imi, imj, imk);
        }
        /**
        * Returns the addition of this quaternion with an imaginary part.
        * @param imag an imaginary part
        */
        public Quaternion addImag(final Double3Vector imag) {
                return new Quaternion(re, imi+imag.vector[0], imj+imag.vector[1], imk+imag.vector[2]);
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this number and another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member x) {
                if(x instanceof Quaternion)
                        return subtract((Quaternion)x);
                else if(x instanceof MathDouble)
                        return subtractReal(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return subtractReal(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this quaternion by another.
        * @param q a quaternion
        */
        public Quaternion subtract(final Quaternion q) {
                return new Quaternion(re-q.re, imi-q.imi, imj-q.imj, imk-q.imk);
        }
        /**
        * Returns the subtraction of this quaternion by a real part.
        * @param real a real part
        */
        public Quaternion subtractReal(final double real) {
                return new Quaternion(re-real, imi, imj, imk);
        }
        /**
        * Returns the subtraction of this quaternion by an imaginary part.
        * @param imag an imaginary part
        */
        public Quaternion subtractImag(final Double3Vector imag) {
                return new Quaternion(re, imi-imag.vector[0], imj-imag.vector[1], imk-imag.vector[2]);
        }

// MULTIPLICATION

        /**
        * Returns the multiplication of this number by a real scalar.
        */
        public Module.Member scalarMultiply(final Ring.Member x) {
                if(x instanceof MathDouble)
                        return multiply(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return multiply(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this number and another.
        */
        public Ring.Member multiply(final Ring.Member x) {
                if(x instanceof Quaternion)
                        return multiply((Quaternion)x);
                else if(x instanceof MathDouble)
                        return multiply(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return multiply(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this quaternion and another.
        * @param q a quaternion
        */
        public Quaternion multiply(final Quaternion q) {
                return new Quaternion(
                        re*q.re-imi*q.imi-imj*q.imj-imk*q.imk,
                        re*q.imi+q.re*imi+(imj*q.imk-q.imj*imk),
                        re*q.imj+q.re*imj+(imk*q.imi-q.imk*imi),
                        re*q.imk+q.re*imk+(imi*q.imj-q.imi*imj)
                );
        }
        /**
        * Returns the multiplication of this quaternion by a scalar.
        * @param x a real number
        */
        public Quaternion multiply(final double x) {
                return new Quaternion(x*re, x*imi, x*imj, x*imk);
        }

// DIVISION

        /**
        * Returns the division of this number by a real scalar.
        */
        public VectorSpace.Member scalarDivide(final Field.Member x) {
                if(x instanceof MathDouble)
                        return divide(((MathDouble)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the division of this number and another.
        */
        public Field.Member divide(final Field.Member x) {
                if(x instanceof Quaternion)
                        return divide((Quaternion)x);
                else if(x instanceof MathDouble)
                        return divide(((MathDouble)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the division of this quaternion by another.
        * @param q a quaternion
        * @exception ArithmeticException If divide by zero.
        */
        public Quaternion divide(final Quaternion q) {
                final double qSumSqr=q.sumSquares();
                return new Quaternion(
                        (re*q.re+imi*q.imi+imj*q.imj+imk*q.imk)/qSumSqr,
                        (q.re*imi-re*q.imi-(imj*q.imk-q.imj*imk))/qSumSqr,
                        (q.re*imj-re*q.imj-(imk*q.imi-q.imk*imi))/qSumSqr,
                        (q.re*imk-re*q.imk-(imi*q.imj-q.imi*imj))/qSumSqr
                );
        }
        /**
        * Returns the division of this quaternion by a scalar.
        * @param x a real number
        * @exception ArithmeticException If divide by zero.
        */
        public Quaternion divide(final double x) {
                return new Quaternion(re/x, imi/x, imj/x, imk/x);
        }
}

