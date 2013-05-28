package JSci.maths;

import JSci.GlobalSettings;
import JSci.maths.algebras.*;
import JSci.maths.fields.*;
import JSci.maths.groups.AbelianGroup;

/**
* The ComplexVector class encapsulates vectors containing complex numbers.
* @version 2.2
* @author Mark Hale
*/
public class ComplexVector extends MathVector implements HilbertSpace.Member {
        /**
        * Storage format identifier.
        */
        protected final static int ARRAY_1D=1;
        /**
        * Arrays containing the components of the vector.
        */
        protected double vectorRe[],vectorIm[];
        protected ComplexVector(final int dim,final int storeID) {
                super(dim);
                storageFormat=storeID;
        }
        /**
        * Constructs an empty vector.
        * @param dim the dimension of the vector.
        */
        public ComplexVector(final int dim) {
                this(dim,ARRAY_1D);
                vectorRe=new double[dim];
                vectorIm=new double[dim];
        }
        /**
        * Constructs a vector by wrapping two arrays.
        * @param real an array of real values
        * @param imag an array of imaginary values
        */
        public ComplexVector(final double real[],final double imag[]) {
                this(real.length,ARRAY_1D);
                vectorRe=real;
                vectorIm=imag;
        }
        /**
        * Constructs a vector from an array.
        * @param array an assigned value
        */
        public ComplexVector(final Complex array[]) {
                this(array.length);
                for(int i=0;i<N;i++) {
                        vectorRe[i]=array[i].real();
                        vectorIm[i]=array[i].imag();
                }
        }
        /**
        * Compares two complex vectors for equality.
        * @param a a complex vector
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof ComplexVector) && N==((ComplexVector)a).N) {
                        final ComplexVector cv=(ComplexVector)a;
                        for(int i=0;i<N;i++) {
                                if(!cv.getComponent(i).equals(vectorRe[i],vectorIm[i]))
                                        return false;
                        }
                        return true;
                } else
                        return false;
        }
        /**
        * Returns a comma delimited string representing the value of this vector.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(N);
                int i;
                for(i=0;i<N-1;i++) {
                        buf.append(Complex.toString(vectorRe[i],vectorIm[i]));
                        buf.append(',');
                }
                buf.append(Complex.toString(vectorRe[i],vectorIm[i]));
                return buf.toString();
        }
        /**
        * Returns a hashcode for this vector.
        */
        public int hashCode() {
                return (int)Math.exp(norm());
        }
        /**
        * Returns the real part of this complex vector.
        */
        public DoubleVector real() {
                return new DoubleVector(vectorRe);
        }
        /**
        * Returns the imaginary part of this complex vector.
        */
        public DoubleVector imag() {
                return new DoubleVector(vectorIm);
        }
        /**
        * Returns a component of this vector.
        * @param n index of the vector component
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public Complex getComponent(final int n) {
                if(n>=0 && n<N)
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
                if(n>=0 && n<N) {
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
                if(n>=0 && n<N) {
                        vectorRe[n]=x;
                        vectorIm[n]=y;
                } else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Returns the l<sup>2</sup>-norm (magnitude).
        */
        public double norm() {
                double answer=vectorRe[0]*vectorRe[0]+vectorIm[0]*vectorIm[0];
                for(int i=1;i<N;i++)
                        answer+=vectorRe[i]*vectorRe[i]+vectorIm[i]*vectorIm[i];
                return Math.sqrt(answer);
        }
        /**
        * Makes the norm of this vector equal to 1.
        */
        public void normalize() {
                final double norm=norm();
                for(int i=0;i<N;i++) {
                        vectorRe[i]/=norm;
                        vectorIm[i]/=norm;
                }
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        */
        public double infNorm() {
                double infNorm=vectorRe[0]*vectorRe[0]+vectorIm[0]*vectorIm[0];
                double mod;
                for(int i=1;i<N;i++) {
                        mod=vectorRe[i]*vectorRe[i]+vectorIm[i]*vectorIm[i];
                        if(mod>infNorm)
                                infNorm=mod;
                }
                return Math.sqrt(infNorm);
        }

//============
// OPERATIONS
//============

        /**
        * Returns the negative of this vector.
        */
        public AbelianGroup.Member negate() {
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                arrayRe[0]=-vectorRe[0];
                arrayIm[0]=-vectorIm[0];
                for(int i=1;i<N;i++) {
                        arrayRe[i]=-vectorRe[i];
                        arrayIm[i]=-vectorIm[i];
                }
                return new ComplexVector(arrayRe,arrayIm);
        }

// COMPLEX CONJUGATE

        /**
        * Returns the complex conjugate of this vector.
        */
        public ComplexVector conjugate() {
                final double arrayIm[]=new double[N];
                arrayIm[0]=-vectorIm[0];
                for(int i=1;i<N;i++)
                        arrayIm[i]=-vectorIm[i];
                return new ComplexVector(vectorRe,arrayIm);
        }

// ADDITION

        /**
        * Returns the addition of this vector and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member v) {
                if(v instanceof ComplexVector)
                        return add((ComplexVector)v);
                else if(v instanceof DoubleVector)
                        return add((DoubleVector)v);
                else if(v instanceof IntegerVector)
                        return add((IntegerVector)v);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this vector and another.
        * @param v a complex vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public ComplexVector add(final ComplexVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawAdd(v);
                        default: 
                                if(N==v.N) {
                                        final double arrayRe[]=new double[N];
                                        final double arrayIm[]=new double[N];
                                        arrayRe[0]=vectorRe[0]+v.getComponent(0).real();
                                        arrayIm[0]=vectorIm[0]+v.getComponent(0).imag();
                                        for(int i=1;i<N;i++) {
                                                arrayRe[i]=vectorRe[i]+v.getComponent(i).real();
                                                arrayIm[i]=vectorIm[i]+v.getComponent(i).imag();
                                        }
                                        return new ComplexVector(arrayRe,arrayIm);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private ComplexVector rawAdd(final ComplexVector v) {
                if(N==v.N) {
                        final double arrayRe[]=new double[N];
                        final double arrayIm[]=new double[N];
                        arrayRe[0]=vectorRe[0]+v.vectorRe[0];
                        arrayIm[0]=vectorIm[0]+v.vectorIm[0];
                        for(int i=1;i<N;i++) {
                                arrayRe[i]=vectorRe[i]+v.vectorRe[i];
                                arrayIm[i]=vectorIm[i]+v.vectorIm[i];
                        }
                        return new ComplexVector(arrayRe,arrayIm);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }
        /**
        * Returns the addition of this vector and another.
        * @param v a double vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public ComplexVector add(final DoubleVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawAdd(v);
                        default: 
                                if(N==v.N) {
                                        final double arrayRe[]=new double[N];
                                        arrayRe[0]=vectorRe[0]+v.getComponent(0);
                                        for(int i=1;i<N;i++)
                                                arrayRe[i]=vectorRe[i]+v.getComponent(i);
                                        return new ComplexVector(arrayRe,vectorIm);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private ComplexVector rawAdd(final DoubleVector v) {
                if(N==v.N) {
                        final double arrayRe[]=new double[N];
                        arrayRe[0]=vectorRe[0]+v.vector[0];
                        for(int i=1;i<N;i++)
                                arrayRe[i]=vectorRe[i]+v.vector[i];
                        return new ComplexVector(arrayRe,vectorIm);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }
        /**
        * Returns the addition of this vector and another.
        * @param v an integer vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public ComplexVector add(final IntegerVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawAdd(v);
                        default: 
                                if(N==v.N) {
                                        final double arrayRe[]=new double[N];
                                        arrayRe[0]=vectorRe[0]+v.getComponent(0);
                                        for(int i=1;i<N;i++)
                                                arrayRe[i]=vectorRe[i]+v.getComponent(i);
                                        return new ComplexVector(arrayRe,vectorIm);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private ComplexVector rawAdd(final IntegerVector v) {
                if(N==v.N) {
                        final double arrayRe[]=new double[N];
                        arrayRe[0]=vectorRe[0]+v.vector[0];
                        for(int i=1;i<N;i++)
                                arrayRe[i]=vectorRe[i]+v.vector[i];
                        return new ComplexVector(arrayRe,vectorIm);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member v) {
                if(v instanceof ComplexVector)
                        return subtract((ComplexVector)v);
                else if(v instanceof DoubleVector)
                        return subtract((DoubleVector)v);
                else if(v instanceof IntegerVector)
                        return subtract((IntegerVector)v);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v a complex vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public ComplexVector subtract(final ComplexVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawSubtract(v);
                        default: 
                                if(N==v.N) {
                                        final double arrayRe[]=new double[N];
                                        final double arrayIm[]=new double[N];
                                        arrayRe[0]=vectorRe[0]-v.getComponent(0).real();
                                        arrayIm[0]=vectorIm[0]-v.getComponent(0).imag();
                                        for(int i=1;i<N;i++) {
                                                arrayRe[i]=vectorRe[i]-v.getComponent(i).real();
                                                arrayIm[i]=vectorIm[i]-v.getComponent(i).imag();
                                        }
                                        return new ComplexVector(arrayRe,arrayIm);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private ComplexVector rawSubtract(final ComplexVector v) {
                if(N==v.N) {
                        final double arrayRe[]=new double[N];
                        final double arrayIm[]=new double[N];
                        arrayRe[0]=vectorRe[0]-v.vectorRe[0];
                        arrayIm[0]=vectorIm[0]-v.vectorIm[0];
                        for(int i=1;i<N;i++) {
                                arrayRe[i]=vectorRe[i]-v.vectorRe[i];
                                arrayIm[i]=vectorIm[i]-v.vectorIm[i];
                        }
                        return new ComplexVector(arrayRe,arrayIm);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v a double vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public ComplexVector subtract(final DoubleVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawSubtract(v);
                        default: 
                                if(N==v.N) {
                                        final double arrayRe[]=new double[N];
                                        arrayRe[0]=vectorRe[0]-v.getComponent(0);
                                        for(int i=1;i<N;i++)
                                                arrayRe[i]=vectorRe[i]-v.getComponent(i);
                                        return new ComplexVector(arrayRe,vectorIm);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private ComplexVector rawSubtract(final DoubleVector v) {
                if(N==v.N) {
                        final double arrayRe[]=new double[N];
                        arrayRe[0]=vectorRe[0]-v.vector[0];
                        for(int i=1;i<N;i++)
                                arrayRe[i]=vectorRe[i]-v.vector[i];
                        return new ComplexVector(arrayRe,vectorIm);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v an integer vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public ComplexVector subtract(final IntegerVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawSubtract(v);
                        default: 
                                if(N==v.N) {
                                        final double arrayRe[]=new double[N];
                                        arrayRe[0]=vectorRe[0]-v.getComponent(0);
                                        for(int i=1;i<N;i++)
                                                arrayRe[i]=vectorRe[i]-v.getComponent(i);
                                        return new ComplexVector(arrayRe,vectorIm);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private ComplexVector rawSubtract(final IntegerVector v) {
                if(N==v.N) {
                        final double arrayRe[]=new double[N];
                        arrayRe[0]=vectorRe[0]-v.vector[0];
                        for(int i=1;i<N;i++)
                                arrayRe[i]=vectorRe[i]-v.vector[i];
                        return new ComplexVector(arrayRe,vectorIm);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this vector by a scalar.
        */
        public Module.Member scalarMultiply(Ring.Member x) {
                if(x instanceof Complex)
                        return scalarMultiply((Complex)x);
                else if(x instanceof MathDouble)
                        return scalarMultiply(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return scalarMultiply(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this vector by a scalar.
        * @param z a complex number
        */
        public ComplexVector scalarMultiply(final Complex z) {
                final double real=z.real();
                final double imag=z.imag();
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                arrayRe[0]=vectorRe[0]*real-vectorIm[0]*imag;
                arrayIm[0]=vectorRe[0]*imag+vectorIm[0]*real;
                for(int i=1;i<N;i++) {
                        arrayRe[i]=vectorRe[i]*real-vectorIm[i]*imag;
                        arrayIm[i]=vectorRe[i]*imag+vectorIm[i]*real;
                }
                return new ComplexVector(arrayRe,arrayIm);
        }
        /**
        * Returns the multiplication of this vector by a scalar.
        * @param x a double
        */
        public ComplexVector scalarMultiply(final double x) {
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                arrayRe[0]=x*vectorRe[0];
                arrayIm[0]=x*vectorIm[0];
                for(int i=1;i<N;i++) {
                        arrayRe[i]=x*vectorRe[i];
                        arrayIm[i]=x*vectorIm[i];
                }
                return new ComplexVector(arrayRe,arrayIm);
        }

// SCALAR DIVISION

        /**
        * Returns the division of this vector by a scalar.
        */
        public VectorSpace.Member scalarDivide(Field.Member x) {
                if(x instanceof Complex)
                        return scalarDivide((Complex)x);
                else if(x instanceof MathDouble)
                        return scalarDivide(((MathDouble)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the division of this vector by a scalar.
        * @param z a complex number
        * @exception ArithmeticException If divide by zero.
        */
        public ComplexVector scalarDivide(final Complex z) {
                final double real=z.real();
                final double imag=z.imag();
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                final double a,denom;
                if(Math.abs(real)<Math.abs(imag)) {
                        a=real/imag;
                        denom=real*a+imag;
                        for(int i=0;i<N;i++) {
                                arrayRe[i]=(vectorRe[i]*a+vectorIm[i])/denom;
                                arrayIm[i]=(vectorIm[i]*a-vectorRe[i])/denom;
                        }
                } else {
                        a=imag/real;
                        denom=real+imag*a;
                        for(int i=0;i<N;i++) {
                                arrayRe[i]=(vectorRe[i]+vectorIm[i]*a)/denom;
                                arrayIm[i]=(vectorIm[i]-vectorRe[i]*a)/denom;
                        }
                }
                return new ComplexVector(arrayRe,arrayIm);
        }
        /**
        * Returns the division of this vector by a scalar.
        * @param x a double
        * @exception ArithmeticException If divide by zero.
        */
        public ComplexVector scalarDivide(final double x) {
                final double arrayRe[]=new double[N];
                final double arrayIm[]=new double[N];
                arrayRe[0]=vectorRe[0]/x;
                arrayIm[0]=vectorIm[0]/x;
                for(int i=1;i<N;i++) {
                        arrayRe[i]=vectorRe[i]/x;
                        arrayIm[i]=vectorIm[i]/x;
                }
                return new ComplexVector(arrayRe,arrayIm);
        }

// SCALAR PRODUCT

        /**
        * Returns the scalar product of this vector and another.
        */
        public Complex scalarProduct(HilbertSpace.Member v) {
                if(v instanceof ComplexVector)
                        return scalarProduct((ComplexVector)v);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the scalar product of this vector and another.
        * @param v a complex vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public Complex scalarProduct(final ComplexVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawScalarProduct(v);
                        default: 
                                if(N==v.N) {
                                        Complex comp=v.getComponent(0);
                                        double real=vectorRe[0]*comp.real()+vectorIm[0]*comp.imag();
                                        double imag=vectorIm[0]*comp.real()-vectorRe[0]*comp.imag();
                                        for(int i=1;i<N;i++) {
                                                comp=v.getComponent(i);
                                                real+=vectorRe[i]*comp.real()+vectorIm[i]*comp.imag();
                                                imag+=vectorIm[i]*comp.real()-vectorRe[i]*comp.imag();
                                        }
                                        return new Complex(real,imag);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private Complex rawScalarProduct(final ComplexVector v) {
                if(N==v.N) {
                        double real=vectorRe[0]*v.vectorRe[0]+vectorIm[0]*v.vectorIm[0];
                        double imag=vectorIm[0]*v.vectorRe[0]-vectorRe[0]*v.vectorIm[0];
                        for(int i=1;i<N;i++) {
                                real+=vectorRe[i]*v.vectorRe[i]+vectorIm[i]*v.vectorIm[i];
                                imag+=vectorIm[i]*v.vectorRe[i]-vectorRe[i]*v.vectorIm[i];
                        }
                        return new Complex(real,imag);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }

// MAP COMPONENTS

        /**
        * Applies a function on all the vector components.
        * @param f a user-defined function
        * @return a complex vector
        */
        public ComplexVector mapComponents(final ComplexMapping f) {
                final Complex array[]=new Complex[N];
                array[0]=f.map(vectorRe[0],vectorIm[0]);
                for(int i=1;i<N;i++)
                        array[i]=f.map(vectorRe[i],vectorIm[i]);
                return new ComplexVector(array);
        }
}

