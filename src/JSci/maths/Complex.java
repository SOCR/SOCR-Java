package JSci.maths;

import JSci.GlobalSettings;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.fields.*;
import JSci.maths.algebras.*;

/**
* The Complex class encapsulates complex numbers.
* @planetmath Complex
* @version 2.25
* @author Mark Hale
*/
public final class Complex extends Object implements Field.Member, CStarAlgebra.Member {
        private static final long serialVersionUID = 6561957920497208796L;

        private double re;
        private double im;
        /**
        * Caching.
        */
        private transient boolean isModCached=false;
        private transient double modCache;
        private transient boolean isArgCached=false;
        private transient double argCache;
        /**
        * The complex number 0+1i.
        */
        public static final Complex I=ComplexField.I;
        /**
        * The complex number 1+0i.
        */
        public static final Complex ONE=ComplexField.ONE;
        /**
        * The complex number 0+0i.
        */
        public static final Complex ZERO=ComplexField.ZERO;
        /**
        * Constructs the complex number x+iy.
        * @param x the real value of a complex number.
        * @param y the imaginary value of a complex number.
        */
        public Complex(final double x,final double y) {
                re=x;
                im=y;
        }
        /**
        * Constructs the complex number represented by a string.
        * @param s a string representing a complex number.
        * @exception NumberFormatException if the string does not contain a parsable number.
        */
        public Complex(final String s) throws NumberFormatException {
                final int iPos=s.indexOf('i');
                if(iPos==-1) {
                        re=Double.parseDouble(s);
                        im=0.0;
                } else {
                        String imStr;
                        int signPos=s.indexOf('+',1);
                        if(signPos==-1)
                                signPos=s.indexOf('-',1);
                        if(signPos==-1) {
                                re=0.0;
                                imStr=s;
                        } else {
                                if(iPos<signPos) {
                                        imStr=s.substring(0,signPos);
                                        re=Double.parseDouble(s.substring(signPos+1));
                                } else {
                                        re=Double.parseDouble(s.substring(0,signPos));
                                        imStr=s.substring(signPos+1);
                                }
                        }
                        if(imStr.startsWith("i"))
                                im=Double.parseDouble(imStr.substring(1));
                        else if(imStr.endsWith("i"))
                                im=Double.parseDouble(imStr.substring(0,imStr.length()-1));
                }
        }
        /**
        * Creates a complex number with the given modulus and argument.
        * @param mod the modulus of a complex number.
        * @param arg the argument of a complex number.
        */
        public static Complex polar(final double mod,final double arg) {
                final Complex z=new Complex(mod*Math.cos(arg),mod*Math.sin(arg));
                z.modCache=mod;
                z.isModCached=true;
                z.argCache=arg;
                z.isArgCached=true;
                return z;
        }
        /**
        * Compares two complex numbers for equality.
        * @param obj a complex number.
        */
        public boolean equals(Object obj) {
                if(obj!=null && (obj instanceof Complex)) {
                        final Complex z=(Complex)obj;
                        return equals(z.re,z.im);
                } else
                        return false;
        }
        /**
        * Compares two complex numbers for equality.
        */
        public boolean equals(double real,double imag) {
                return Math.abs(re-real)<=GlobalSettings.ZERO_TOL &&
                        Math.abs(im-imag)<=GlobalSettings.ZERO_TOL;
        }
        /**
        * Returns a string representing the value of this complex number.
        */
        public String toString() {
                return toString(re,im);
        }
        /**
        * Returns a string representing the value of this complex number.
        */
        public static String toString(double real,double imag) {
                final StringBuffer buf=new StringBuffer(10);
                buf.append(real);
                if(imag>=0.0)
                        buf.append("+");
                buf.append(imag);
                buf.append("i");
                return buf.toString();
        }
        /**
        * Returns a hashcode for this complex number.
        */
        public int hashCode() {
                return (int)(Math.exp(mod()));
        }
        /**
        * Returns true if either the real or imaginary part is NaN.
        */
        public boolean isNaN() {
                return (re==Double.NaN) || (im==Double.NaN);
        }
        /**
        * Returns true if either the real or imaginary part is infinite.
        */
        public boolean isInfinite() {
                return (re==Double.POSITIVE_INFINITY) || (re==Double.NEGATIVE_INFINITY)
                        || (im==Double.POSITIVE_INFINITY) || (im==Double.NEGATIVE_INFINITY);
        }
        /**
        * Returns the real part of this complex number.
        */
        public double real() {
                return re;
        }
        /**
        * Returns the imaginary part of this complex number.
        */
        public double imag() {
                return im;
        }
        /**
        * Returns the modulus of this complex number.
        */
        public double mod() {
                if(isModCached)
                        return modCache;
                modCache=mod(re,im);
                isModCached=true;
                return modCache;
        }
        private static double mod(final double real,final double imag) {
                final double reAbs=Math.abs(real);
                final double imAbs=Math.abs(imag);
                if(reAbs==0.0 && imAbs==0.0)
                        return 0.0;
                else if(reAbs<imAbs)
                        return imAbs*Math.sqrt(1.0+(real/imag)*(real/imag));
                else
                        return reAbs*Math.sqrt(1.0+(imag/real)*(imag/real));
        }
        /**
        * Returns the argument of this complex number.
        */
        public double arg() {
                if(isArgCached)
                        return argCache;
                argCache=arg(re,im);
                isArgCached=true;
                return argCache;
        }
        private static double arg(final double real,final double imag) {
                return Math.atan2(imag,real);
        }
        /**
        * Returns the C<sup>*</sup> norm.
        */
        public double norm() {
                return mod();
        }

//============
// OPERATIONS
//============

        /**
        * Returns the negative of this complex number.
        */
        public AbelianGroup.Member negate() {
                return new Complex(-re,-im);
        }
        /**
        * Returns the inverse of this complex number.
        */
        public Field.Member inverse() {
                double denominator,real,imag;             
                if(Math.abs(re)<Math.abs(im)) {
                        real=re/im;
                        imag=-1.0;
                        denominator=re*real+im;
                } else {
                        real=1.0;
                        imag=-im/re;
                        denominator=re-im*imag;
                }
                return new Complex(real/denominator,imag/denominator);
        }
        /**
        * Returns the involution of this complex number.
        */
        public CStarAlgebra.Member involution() {
                return conjugate();
        }
        /**
        * Returns the complex conjugate of this complex number.
        */
        public Complex conjugate() {
                return new Complex(re,-im);
        }

// ADDITION

        /**
        * Returns the addition of this number and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member x) {
                if(x instanceof Complex)
                        return add((Complex)x);
                else if(x instanceof MathDouble)
                        return addReal(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return addReal(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this complex number and another.
        * @param z a complex number.
        */
        public Complex add(final Complex z) {
                return new Complex(re+z.re,im+z.im);
        }
        /**
        * Returns the addition of this complex number with a real part.
        * @param real a real part.
        */
        public Complex addReal(final double real) {
                return new Complex(re+real,im);
        }
        /**
        * Returns the addition of this complex number with an imaginary part.
        * @param imag an imaginary part.
        */
        public Complex addImag(final double imag) {
                return new Complex(re,im+imag);
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this number and another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member x) {
                if(x instanceof Complex)
                        return subtract((Complex)x);
                else if(x instanceof MathDouble)
                        return subtractReal(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return subtractReal(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this complex number by another.
        * @param z a complex number.
        */
        public Complex subtract(final Complex z) {
                return new Complex(re-z.re,im-z.im);
        }
        /**
        * Returns the subtraction of this complex number by a real part.
        * @param real a real part.
        */
        public Complex subtractReal(final double real) {
                return new Complex(re-real,im);
        }
        /**
        * Returns the subtraction of this complex number by an imaginary part.
        * @param imag an imaginary part.
        */
        public Complex subtractImag(final double imag) {
                return new Complex(re,im-imag);
        }

// MULTIPLICATION

        /**
        * Returns the multiplication of this number by a complex scalar.
        */
        public Module.Member scalarMultiply(final Ring.Member x) {
                return (Complex)multiply(x);
        }
        /**
        * Returns the multiplication of this number and another.
        */
        public Ring.Member multiply(final Ring.Member x) {
                if(x instanceof Complex)
                        return multiply((Complex)x);
                else if(x instanceof MathDouble)
                        return multiply(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return multiply(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this complex number and another.
        * @param z a complex number.
        */
        public Complex multiply(final Complex z) {
                return new Complex(re*z.re-im*z.im,re*z.im+im*z.re);
        }
        /**
        * Returns the multiplication of this complex number by a scalar.
        * @param x a real number.
        */
        public Complex multiply(final double x) {
                return new Complex(x*re,x*im);
        }

// DIVISION

        /**
        * Returns the division of this number by a complex scalar.
        */
        public VectorSpace.Member scalarDivide(final Field.Member x) {
                return (Complex)divide(x);
        }
        /**
        * Returns the division of this number and another.
        */
        public Field.Member divide(final Field.Member x) {
                if(x instanceof Complex)
                        return divide((Complex)x);
                else if(x instanceof MathDouble)
                        return divide(((MathDouble)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the division of this complex number by another.
        * @param z a complex number.
        * @exception ArithmeticException If divide by zero.
        */
        public Complex divide(final Complex z) {
                final double denominator,real,imag,a;             
                if(Math.abs(z.re)<Math.abs(z.im)) {
                        a=z.re/z.im;
                        denominator=z.re*a+z.im;
                        real=re*a+im;
                        imag=im*a-re;
                } else {
                        a=z.im/z.re;
                        denominator=z.re+z.im*a;
                        real=re+im*a;
                        imag=im-re*a;
                }
                return new Complex(real/denominator,imag/denominator);
        }
        /**
        * Returns the division of this complex number by a scalar.
        * @param x a real number.
        * @exception ArithmeticException If divide by zero.
        */
        public Complex divide(final double x) {
                return new Complex(re/x,im/x);
        }

// POWER

        /**
        * Returns this complex number raised to the power of another.
        * @param z a complex number.
        */
        public Complex pow(final Complex z) {
                final double thisMod=mod();
                final double thisArg=arg();
                final double r=Math.pow(thisMod,z.re)/Math.exp(thisArg*z.im);
                final double a=thisArg*z.re+Math.log(thisMod)*z.im;
                return polar(r,a);
        }
        /**
        * Returns this complex number raised to the power of a scalar.
        * @param x a real number.
        */
        public Complex pow(final double x) {
                return polar(Math.pow(mod(),x),arg()*x);
        }
        /**
        * Returns the square of this complex number.
        */
        public Complex sqr() {
                return new Complex(re*re-im*im,2.0*re*im);
        }
        /**
        * Returns the square root of this complex number.
        */
        public Complex sqrt() {
                return polar(Math.sqrt(mod()),arg()/2.0);
        }
        private static Complex sqrt(final double real,final double imag) {
                return polar(Math.sqrt(mod(real,imag)),arg(real,imag)/2.0);
        }

//===========
// FUNCTIONS
//===========

// EXP

        /**
        * Returns the exponential number e (2.718...) raised to the power of a complex number.
        * @param z a complex number.
        */
        public static Complex exp(final Complex z) {
                return new Complex(
                        Math.exp(z.re)*Math.cos(z.im),
                        Math.exp(z.re)*Math.sin(z.im)
                );
        }

// LOG

        /**
        * Returns the natural logarithm (base e) of a complex number.
        * @param z a complex number.
        */
        public static Complex log(final Complex z) {
                return new Complex(Math.log(z.mod()),z.arg());
        }
        private final static Complex log(final double real,final double imag) {
                return new Complex(Math.log(mod(real,imag)),arg(real,imag));
        }
        private final static Complex log_2(final double real,final double imag) {
                return new Complex(Math.log(mod(real,imag))/2.0,arg(real,imag)/2.0);
        }
        private final static Complex log_2I(final double real,final double imag) {
                return new Complex(arg(real,imag)/2.0,-Math.log(mod(real,imag))/2.0);
        }
        private final static Complex log_2IplusPI_2(final double real,final double imag) {
                return new Complex((arg(real,imag)+Math.PI)/2.0,-Math.log(mod(real,imag))/2.0);
        }

// SIN

        /**
        * Returns the trigonometric sine of a complex angle.
        * @param z an angle that is measured in radians.
        */
        public static Complex sin(final Complex z) {
                return new Complex(
                        Math.sin(z.re)*ExtraMath.cosh(z.im),
                        Math.cos(z.re)*ExtraMath.sinh(z.im)
                );
        }

// COS

        /**
        * Returns the trigonometric cosine of a complex angle.
        * @param z an angle that is measured in radians.
        */
        public static Complex cos(final Complex z) {
                return new Complex(
                        Math.cos(z.re)*ExtraMath.cosh(z.im),
                       -Math.sin(z.re)*ExtraMath.sinh(z.im)
                );
        }

// TAN

        /**
        * Returns the trigonometric tangent of a complex angle.
        * @param z an angle that is measured in radians.
        */
        public static Complex tan(final Complex z) {
                final double sinRe=Math.sin(z.re);
                final double cosRe=Math.cos(z.re);
                final double sinhIm=ExtraMath.sinh(z.im);
                final double coshIm=ExtraMath.cosh(z.im);
                final double denom=cosRe*cosRe*coshIm*coshIm+sinRe*sinRe*sinhIm*sinhIm;
                return new Complex(sinRe*cosRe/denom,sinhIm*coshIm/denom);
        }

// SINH

        /**
        * Returns the hyperbolic sine of a complex number.
        * @param z a complex number.
        */
        public static Complex sinh(final Complex z) {
                return new Complex(
                        ExtraMath.sinh(z.re)*Math.cos(z.im),
                        ExtraMath.cosh(z.re)*Math.sin(z.im)
                );
        }

// COSH

        /**
        * Returns the hyperbolic cosine of a complex number.
        * @param z a complex number.
        */
        public static Complex cosh(final Complex z) {
                return new Complex(
                        ExtraMath.cosh(z.re)*Math.cos(z.im),
                        ExtraMath.sinh(z.re)*Math.sin(z.im)
                );
        }

// TANH

        /**
        * Returns the hyperbolic tangent of a complex number.
        * @param z a complex number.
        */
        public static Complex tanh(final Complex z) {
                final double sinhRe=ExtraMath.sinh(z.re);
                final double coshRe=ExtraMath.cosh(z.re);
                final double sinIm=Math.sin(z.im);
                final double cosIm=Math.cos(z.im);
                final double denom=coshRe*coshRe*cosIm*cosIm+sinhRe*sinhRe*sinIm*sinIm;
                return new Complex(sinhRe*coshRe/denom,sinIm*cosIm/denom);
        }

// INVERSE SIN

        /**
        * Returns the arc sine of a complex number, in the range of
        * (-<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2,
        * -<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">).
        * @param z a complex number.
        */
        public static Complex asin(final Complex z) {
                if(z.equals(ONE))
                        return ComplexField.PI_2;
                else if(z.equals(ComplexField.MINUS_ONE))
                        return ComplexField.MINUS_PI_2;
                else {
                        // atan(z/sqrt(1-z*z))
                        final Complex root=sqrt(1.0-z.re*z.re+z.im*z.im,-2.0*z.re*z.im);
                        final double zModSqr=z.re*z.re+z.im*z.im;
                        final double rootModSqr=root.re*root.re+root.im*root.im;
                        final double denom=rootModSqr+zModSqr+2.0*(root.re*z.im-root.im*z.re);
                        return log_2I((rootModSqr-zModSqr)/denom,2.0*(root.re*z.re+root.im*z.im)/denom);
                }
        }

// INVERSE COS

        /**
        * Returns the arc cosine of a complex number, in the range of
        * (0.0 through <img border=0 alt="pi" src="doc-files/pi.gif">,
        * 0.0 through <img border=0 alt="infinity" src="doc-files/infinity.gif">).
        * @param z a complex number.
        */
        public static Complex acos(final Complex z) {
                if(z.equals(ONE))
                        return ZERO;
                else if(z.equals(ComplexField.MINUS_ONE))
                        return ComplexField.PI;
                else {
                        // atan(-z/sqrt(1-z*z))+PI/2
                        final Complex root=sqrt(1.0-z.re*z.re+z.im*z.im,-2.0*z.re*z.im);
                        final double zModSqr=z.re*z.re+z.im*z.im;
                        final double rootModSqr=root.re*root.re+root.im*root.im;
                        final double denom=rootModSqr+zModSqr+2.0*(root.im*z.re-root.re*z.im);
                        return log_2IplusPI_2((rootModSqr-zModSqr)/denom,-2.0*(root.re*z.re+root.im*z.im)/denom);
                }
        }

// INVERSE TAN

        /**
        * Returns the arc tangent of a complex number, in the range of
        * (-<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2,
        * -<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">).
        * @param z a complex number.
        */
        public static Complex atan(final Complex z) {
                // -i atanh(iz) = -i/2 log((1+iz)/(1-iz))
                final double modSqr=z.re*z.re+z.im*z.im;
                final double denom=1.0+modSqr+2.0*z.im;
                return log_2I((1.0-modSqr)/denom,2.0*z.re/denom);
        }

// INVERSE SINH

        /**
        * Returns the arc hyperbolic sine of a complex number, in the range of
        * (-<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">,
        * -<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2).
        * @param z a complex number.
        */
        public static Complex asinh(final Complex z) {
                if(z.equals(I))
                        return ComplexField.PI_2_I;
                else if(z.equals(ComplexField.MINUS_I))
                        return ComplexField.MINUS_PI_2_I;
                else {
                        // log(z+sqrt(z*z+1))
                        final Complex root=sqrt(z.re*z.re-z.im*z.im+1.0,2.0*z.re*z.im);
                        return log(z.re+root.re,z.im+root.im);
                }
        }

// INVERSE COSH

        /**
        * Returns the arc hyperbolic cosine of a complex number, in the range of
        * (0.0 through <img border=0 alt="infinity" src="doc-files/infinity.gif">,
        * 0.0 through <img border=0 alt="pi" src="doc-files/pi.gif">).
        * @param z a complex number.
        */
        public static Complex acosh(final Complex z) {
                if(z.equals(ONE))
                        return ZERO;
                else if(z.equals(ComplexField.MINUS_ONE))
                        return ComplexField.PI_I;
                else {
                        // log(z+sqrt(z*z-1))
                        final Complex root=sqrt(z.re*z.re-z.im*z.im-1.0,2.0*z.re*z.im);
                        return log(z.re+root.re,z.im+root.im);
                }
        }

// INVERSE TANH

        /**
        * Returns the arc hyperbolic tangent of a complex number, in the range of
        * (-<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">,
        * -<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2).
        * @param z a complex number.
        */
        public static Complex atanh(final Complex z) {
                // 1/2 log((1+z)/(1-z))
                final double modSqr=z.re*z.re+z.im*z.im;
                final double denom=1.0+modSqr-2.0*z.re;
                return log_2((1.0-modSqr)/denom,2.0*z.im/denom);
        }
}

