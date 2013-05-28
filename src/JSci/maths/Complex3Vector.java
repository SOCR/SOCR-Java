package JSci.maths;

import JSci.GlobalSettings;

/**
* The Complex3Vector class encapsulates complex 3-vectors.
* @version 2.2
* @author Mark Hale
*/
public class Complex3Vector extends ComplexVector {
        /**
        * Constructs an empty 3-vector.
        */
        public Complex3Vector() {
                super(3);
        }
        /**
        * Constructs a 3-vector by wrapping two arrays.
        * @param real an array of real values
        * @param imag an array of imaginary values
        */
        public Complex3Vector(final double real[],final double imag[]) {
                super(real,imag);
        }
        /**
        * Constructs a 3-vector.
        * @param x x coordinate
        * @param y y coordinate
        * @param z z coordinate
        */
        public Complex3Vector(final Complex x,final Complex y,final Complex z) {
                super(3);
                vectorRe[0]=x.real();
                vectorIm[0]=x.imag();
                vectorRe[1]=y.real();
                vectorIm[1]=y.imag();
                vectorRe[2]=z.real();
                vectorIm[2]=z.imag();
        }
        /**
        * Compares two complex vectors for equality.
        * @param a a complex 3-vector
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof Complex3Vector)) {
                        final Complex3Vector cv=(Complex3Vector)a;
                        return Math.abs(vectorRe[0]-cv.vectorRe[0])<=GlobalSettings.ZERO_TOL &&
                                Math.abs(vectorIm[0]-cv.vectorIm[0])<=GlobalSettings.ZERO_TOL &&
                                Math.abs(vectorRe[1]-cv.vectorRe[1])<=GlobalSettings.ZERO_TOL &&
                                Math.abs(vectorIm[1]-cv.vectorIm[1])<=GlobalSettings.ZERO_TOL &&
                                Math.abs(vectorRe[2]-cv.vectorRe[2])<=GlobalSettings.ZERO_TOL &&
                                Math.abs(vectorIm[2]-cv.vectorIm[2])<=GlobalSettings.ZERO_TOL;
                } else
                        return false;
        }
        /**
        * Returns a comma delimited string representing the value of this vector.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(5);
                buf.append(Complex.toString(vectorRe[0],vectorIm[0])).append(',')
                .append(Complex.toString(vectorRe[1],vectorIm[1])).append(',')
                .append(Complex.toString(vectorRe[2],vectorIm[2]));
                return buf.toString();
        }
        /**
        * Returns a hashcode for this vector.
        */
        public int hashCode() {
                return (int)Math.exp(norm());
        }
        /**
        * Returns the real part of this complex 3-vector.
        */
        public DoubleVector real() {
                return new Double3Vector(vectorRe);
        }
        /**
        * Returns the imaginary part of this complex 3-vector.
        */
        public DoubleVector imag() {
                return new Double3Vector(vectorIm);
        }
        /**
        * Returns a component of this vector.
        * @param n index of the vector component
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public Complex getComponent(final int n) {
                if(n>=0 && n<3)
                        return new Complex(vectorRe[n],vectorIm[n]);
                else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Sets the value of a component of this vector.
        * Should only be used to initialise this vector.
        * @param n index of the vector component
        * @param z a complex number
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public void setComponent(final int n, final Complex z) {
                if(n>=0 && n<3) {
                        vectorRe[n]=z.real();
                        vectorIm[n]=z.imag();
                } else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Sets the value of a component of this vector.
        * Should only be used to initialise this vector.
        * @param n index of the vector component
        * @param x the real part of a complex number
        * @param y the imaginary part of a complex number
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public void setComponent(final int n, final double x, final double y) {
                if(n>=0 && n<3) {
                        vectorRe[n]=x;
                        vectorIm[n]=y;
                } else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Returns the l<sup>2</sup>-norm (magnitude).
        */
        public double norm() {
                return Math.sqrt(vectorRe[0]*vectorRe[0]+vectorIm[0]*vectorIm[0]
                +vectorRe[1]*vectorRe[1]+vectorIm[1]*vectorIm[1]
                +vectorRe[2]*vectorRe[2]+vectorIm[2]*vectorIm[2]);
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        */
        public double infNorm() {
                double infNorm=vectorRe[0]*vectorRe[0]+vectorIm[0]*vectorIm[0];
                double mod=vectorRe[1]*vectorRe[1]+vectorIm[1]*vectorIm[1];
                if(mod>infNorm)
                        infNorm=mod;
                mod=vectorRe[2]*vectorRe[2]+vectorIm[2]*vectorIm[2];
                if(mod>infNorm)
                        infNorm=mod;
                return Math.sqrt(infNorm);
        }

//============
// OPERATIONS
//============

// COMPLEX CONJUGATE

        /**
        * Returns the complex conjugate of this vector.
        * @return a complex 3-vector
        */
        public ComplexVector conjugate() {
                final double arrayIm[]=new double[3];
                arrayIm[0]=-vectorIm[0];
                arrayIm[1]=-vectorIm[1];
                arrayIm[2]=-vectorIm[2];
                return new Complex3Vector(vectorRe,arrayIm);
        }

// ADDITION

        /**
        * Returns the addition of this vector and another.
        * @param v a complex 3-vector
        */
        public Complex3Vector add(final Complex3Vector v) {
                final double arrayRe[]=new double[3];
                final double arrayIm[]=new double[3];
                arrayRe[0]=vectorRe[0]+v.vectorRe[0];
                arrayIm[0]=vectorIm[0]+v.vectorIm[0];
                arrayRe[1]=vectorRe[1]+v.vectorRe[1];
                arrayIm[1]=vectorIm[1]+v.vectorIm[1];
                arrayRe[2]=vectorRe[2]+v.vectorRe[2];
                arrayIm[2]=vectorIm[2]+v.vectorIm[2];
                return new Complex3Vector(arrayRe,arrayIm);
        }
        /**
        * Returns the addition of this vector and another.
        * @param v a double 3-vector
        */
        public Complex3Vector add(final Double3Vector v) {
                final double arrayRe[]=new double[3];
                arrayRe[0]=vectorRe[0]+v.vector[0];
                arrayRe[1]=vectorRe[1]+v.vector[1];
                arrayRe[2]=vectorRe[2]+v.vector[2];
                return new Complex3Vector(arrayRe,vectorIm);
        }
        /**
        * Returns the addition of this vector and another.
        * @param v an integer 3-vector
        */
        public Complex3Vector add(final Integer3Vector v) {
                final double arrayRe[]=new double[3];
                arrayRe[0]=vectorRe[0]+v.vector[0];
                arrayRe[1]=vectorRe[1]+v.vector[1];
                arrayRe[2]=vectorRe[2]+v.vector[2];
                return new Complex3Vector(arrayRe,vectorIm);
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        * @param v a complex 3-vector
        */
        public Complex3Vector subtract(final Complex3Vector v) {
                final double arrayRe[]=new double[3];
                final double arrayIm[]=new double[3];
                arrayRe[0]=vectorRe[0]-v.vectorRe[0];
                arrayIm[0]=vectorIm[0]-v.vectorIm[0];
                arrayRe[1]=vectorRe[1]-v.vectorRe[1];
                arrayIm[1]=vectorIm[1]-v.vectorIm[1];
                arrayRe[2]=vectorRe[2]-v.vectorRe[2];
                arrayIm[2]=vectorIm[2]-v.vectorIm[2];
                return new Complex3Vector(arrayRe,arrayIm);
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v a double 3-vector
        */
        public Complex3Vector subtract(final Double3Vector v) {
                final double arrayRe[]=new double[3];
                arrayRe[0]=vectorRe[0]-v.vector[0];
                arrayRe[1]=vectorRe[1]-v.vector[1];
                arrayRe[2]=vectorRe[2]-v.vector[2];
                return new Complex3Vector(arrayRe,vectorIm);
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v an integer 3-vector
        */
        public Complex3Vector subtract(final Integer3Vector v) {
                final double arrayRe[]=new double[3];
                arrayRe[0]=vectorRe[0]-v.vector[0];
                arrayRe[1]=vectorRe[1]-v.vector[1];
                arrayRe[2]=vectorRe[2]-v.vector[2];
                return new Complex3Vector(arrayRe,vectorIm);
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this vector by a scalar.
        * @param z a complex number
        * @return a complex 3-vector
        */
        public ComplexVector scalarMultiply(final Complex z) {
                final double real=z.real();
                final double imag=z.imag();
                final double arrayRe[]=new double[3];
                final double arrayIm[]=new double[3];
                arrayRe[0]=vectorRe[0]*real-vectorIm[0]*imag;
                arrayIm[0]=vectorRe[0]*imag+vectorIm[0]*real;
                arrayRe[1]=vectorRe[1]*real-vectorIm[1]*imag;
                arrayIm[1]=vectorRe[1]*imag+vectorIm[1]*real;
                arrayRe[2]=vectorRe[2]*real-vectorIm[2]*imag;
                arrayIm[2]=vectorRe[2]*imag+vectorIm[2]*real;
                return new Complex3Vector(arrayRe,arrayIm);
        }
        /**
        * Returns the multiplication of this vector by a scalar.
        * @param x a double
        * @return a complex 3-vector
        */
        public ComplexVector scalarMultiply(final double x) {
                final double arrayRe[]=new double[3];
                final double arrayIm[]=new double[3];
                arrayRe[0]=x*vectorRe[0];
                arrayIm[0]=x*vectorIm[0];
                arrayRe[1]=x*vectorRe[1];
                arrayIm[1]=x*vectorIm[1];
                arrayRe[2]=x*vectorRe[2];
                arrayIm[2]=x*vectorIm[2];
                return new Complex3Vector(arrayRe,arrayIm);
        }

// SCALAR DIVISION

        /**
        * Returns the division of this vector by a scalar.
        * @param z a complex number
        * @return a complex 3-vector
        * @exception ArithmeticException If divide by zero.
        */
        public ComplexVector scalarDivide(final Complex z) {
                final double real=z.real();
                final double imag=z.imag();
                final double arrayRe[]=new double[3];
                final double arrayIm[]=new double[3];
                final double a,denom;
                if(Math.abs(real)<Math.abs(imag)) {
                        a=real/imag;
                        denom=real*a+imag;
                        arrayRe[0]=(vectorRe[0]*a+vectorIm[0])/denom;
                        arrayIm[0]=(vectorIm[0]*a-vectorRe[0])/denom;
                        arrayRe[1]=(vectorRe[1]*a+vectorIm[1])/denom;
                        arrayIm[1]=(vectorIm[1]*a-vectorRe[1])/denom;
                        arrayRe[2]=(vectorRe[2]*a+vectorIm[2])/denom;
                        arrayIm[2]=(vectorIm[2]*a-vectorRe[2])/denom;
                } else {
                        a=imag/real;
                        denom=real+imag*a;
                        arrayRe[0]=(vectorRe[0]+vectorIm[0]*a)/denom;
                        arrayIm[0]=(vectorIm[0]-vectorRe[0]*a)/denom;
                        arrayRe[1]=(vectorRe[1]+vectorIm[1]*a)/denom;
                        arrayIm[1]=(vectorIm[1]-vectorRe[1]*a)/denom;
                        arrayRe[2]=(vectorRe[2]+vectorIm[2]*a)/denom;
                        arrayIm[2]=(vectorIm[2]-vectorRe[2]*a)/denom;
                }
                return new Complex3Vector(arrayRe,arrayIm);
        }
        /**
        * Returns the division of this vector by a scalar.
        * @param x a double
        * @return a complex 3-vector
        * @exception ArithmeticException If divide by zero.
        */
        public ComplexVector scalarDivide(final double x) {
                final double arrayRe[]=new double[3];
                final double arrayIm[]=new double[3];
                arrayRe[0]=vectorRe[0]/x;
                arrayIm[0]=vectorIm[0]/x;
                arrayRe[1]=vectorRe[1]/x;
                arrayIm[1]=vectorIm[1]/x;
                arrayRe[2]=vectorRe[2]/x;
                arrayIm[2]=vectorIm[2]/x;
                return new Complex3Vector(arrayRe,arrayIm);
        }

// SCALAR PRODUCT

        /**
        * Returns the scalar product of this vector and another.
        * @param v a complex 3-vector
        */
        public Complex scalarProduct(final Complex3Vector v) {
                return new Complex(
                        vectorRe[0]*v.vectorRe[0]+vectorIm[0]*v.vectorIm[0]
                        +vectorRe[1]*v.vectorRe[1]+vectorIm[1]*v.vectorIm[1]
                        +vectorRe[2]*v.vectorRe[2]+vectorIm[2]*v.vectorIm[2],
                        vectorIm[0]*v.vectorRe[0]-vectorRe[0]*v.vectorIm[0]
                        +vectorIm[1]*v.vectorRe[1]-vectorRe[1]*v.vectorIm[1]
                        +vectorIm[2]*v.vectorRe[2]-vectorRe[2]*v.vectorIm[2]
                );
        }

// VECTOR PRODUCT

        /**
        * Returns the vector product of this vector and another.
        * @param v a complex 3-vector
        */
        public Complex3Vector multiply(final Complex3Vector v) {
                final double arrayRe[]=new double[3];
                final double arrayIm[]=new double[3];
                arrayRe[0]=vectorRe[1]*v.vectorRe[2]-vectorIm[1]*v.vectorIm[2]-vectorRe[2]*v.vectorRe[1]+vectorIm[2]*v.vectorIm[1];
                arrayIm[0]=vectorRe[1]*v.vectorIm[2]+vectorIm[1]*v.vectorRe[2]-vectorRe[2]*v.vectorIm[1]-vectorIm[2]*v.vectorRe[1];
                arrayRe[1]=vectorRe[2]*v.vectorRe[0]-vectorIm[2]*v.vectorIm[0]-vectorRe[0]*v.vectorRe[2]+vectorIm[0]*v.vectorIm[2];
                arrayIm[1]=vectorRe[2]*v.vectorIm[0]+vectorIm[2]*v.vectorRe[0]-vectorRe[0]*v.vectorIm[2]-vectorIm[0]*v.vectorRe[2];
                arrayRe[2]=vectorRe[0]*v.vectorRe[1]-vectorIm[0]*v.vectorIm[1]-vectorRe[1]*v.vectorRe[0]+vectorIm[1]*v.vectorIm[0];
                arrayIm[2]=vectorRe[0]*v.vectorIm[1]+vectorIm[0]*v.vectorRe[1]-vectorRe[1]*v.vectorIm[0]-vectorIm[1]*v.vectorRe[0];
                return new Complex3Vector(arrayRe,arrayIm);
        }

// MAP COMPONENTS

        /**
        * Applies a function on all the vector components.
        * @param f a user-defined function
        * @return a complex 3-vector
        */
        public ComplexVector mapComponents(final ComplexMapping f) {
                return new Complex3Vector(
                        f.map(vectorRe[0],vectorIm[0]),
                        f.map(vectorRe[1],vectorIm[1]),
                        f.map(vectorRe[2],vectorIm[2])
                );
        }
}

