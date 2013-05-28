package JSci.maths;

import JSci.maths.groups.AbelianGroup;
import JSci.maths.fields.Ring;
import JSci.maths.algebras.Module;

/**
* The IntegerVector class encapsulates vectors containing integers.
* @version 2.1
* @author Mark Hale
*/
public class IntegerVector extends MathVector {
        /**
        * Storage format identifier.
        */
        protected final static int ARRAY_1D=1;
        /**
        * Array containing the components of the vector.
        */
        protected int vector[];
        protected IntegerVector(final int dim,final int storeID) {
                super(dim);
                storageFormat=storeID;
        }
        /**
        * Constructs an empty vector.
        * @param dim the dimension of the vector.
        */
        public IntegerVector(final int dim) {
                this(dim,ARRAY_1D);
                vector=new int[dim];
        }
        /**
        * Constructs a vector by wrapping an array.
        * @param array an assigned value
        */
        public IntegerVector(final int array[]) {
                this(array.length,ARRAY_1D);
                vector=array;
        }
        /**
        * Compares two integer vectors for equality.
        * @param a an integer vector
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof IntegerVector) && N==((IntegerVector)a).N) {
                        final IntegerVector iv=(IntegerVector)a;
                        for(int i=0;i<N;i++) {
                                if(vector[i]!=iv.getComponent(i))
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
                        buf.append(vector[i]);
                        buf.append(',');
                }
                buf.append(vector[i]);
                return buf.toString();
        }
        /**
        * Returns a hashcode for this vector.
        */
        public int hashCode() {
                return (int)Math.exp(norm());
        }
        /**
        * Converts this vector to a double vector.
        * @return a double vector
        */
        public DoubleVector toDoubleVector() {
                final double array[]=new double[N];
                for(int i=0;i<N;i++)
                        array[i]=vector[i];
                return new DoubleVector(array);
        }
        /**
        * Converts this vector to a complex vector.
        * @return a complex vector
        */
        public ComplexVector toComplexVector() {
                final double arrayRe[]=new double[N];
                for(int i=0;i<N;i++)
                        arrayRe[i]=vector[i];
                return new ComplexVector(arrayRe,new double[N]);
        }
        /**
        * Returns a component of this vector.
        * @param n index of the vector component
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public int getComponent(final int n) {
                if(n>=0 && n<N)
                        return vector[n];
                else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Sets the value of a component of this vector.
        * @param n index of the vector component
        * @param x an integer
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public void setComponent(final int n, final int x) {
                if(n>=0 && n<N)
                        vector[n]=x;
                else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Returns the l<sup>n</sup>-norm.
        */
        public double norm(final int n) {
                double answer=Math.pow(vector[0],n);
                for(int i=1;i<N;i++)
                        answer+=Math.pow(vector[i],n);
                return Math.pow(answer,1.0/n);
        }
        /**
        * Returns the l<sup>2</sup>-norm (magnitude).
        */
        public double norm() {
                double answer=vector[0];
                for(int i=1;i<N;i++)
                        answer=ExtraMath.hypot(answer,vector[i]);
                return answer;
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public double infNorm() {
                int infNorm=vector[0];
                for(int i=1;i<N;i++) {
                        if(vector[i]>infNorm)
                                infNorm=vector[i];
                }
                return infNorm;
        }

//============
// OPERATIONS
//============

        /**
        * Returns the negative of this vector.
        */
        public AbelianGroup.Member negate() {
                final int array[]=new int[N];
                array[0]=-vector[0];
                for(int i=1;i<N;i++)
                        array[i]=-vector[i];
                return new IntegerVector(array);
        }

// ADDITION

        /**
        * Returns the addition of this vector and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member v) {
                if(v instanceof IntegerVector)
                        return add((IntegerVector)v);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this vector and another.
        * @param v an integer vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public IntegerVector add(final IntegerVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawAdd(v);
                        default: 
                                if(N==v.N) {
                                        final int array[]=new int[N];
                                        array[0]=vector[0]+v.getComponent(0);
                                        for(int i=1;i<N;i++)
                                                array[i]=vector[i]+v.getComponent(i);
                                        return new IntegerVector(array);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private IntegerVector rawAdd(final IntegerVector v) {
                if(N==v.N) {
                        final int array[]=new int[N];
                        array[0]=vector[0]+v.vector[0];
                        for(int i=1;i<N;i++)
                                array[i]=vector[i]+v.vector[i];
                        return new IntegerVector(array);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member v) {
                if(v instanceof IntegerVector)
                        return subtract((IntegerVector)v);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v an integer vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public IntegerVector subtract(final IntegerVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawSubtract(v);
                        default: 
                                if(N==v.N) {
                                        final int array[]=new int[N];
                                        array[0]=vector[0]-v.getComponent(0);
                                        for(int i=1;i<N;i++)
                                                array[i]=vector[i]-v.getComponent(i);
                                        return new IntegerVector(array);
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private IntegerVector rawSubtract(final IntegerVector v) {
                if(N==v.N) {
                        final int array[]=new int[N];
                        array[0]=vector[0]-v.vector[0];
                        for(int i=1;i<N;i++)
                                array[i]=vector[i]-v.vector[i];
                        return new IntegerVector(array);
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this vector by a scalar.
        */
        public Module.Member scalarMultiply(Ring.Member x) {
                if(x instanceof MathInteger)
                        return scalarMultiply(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this vector by a scalar.
        * @param x an integer
        */
        public IntegerVector scalarMultiply(final int x) {
                final int array[]=new int[N];
                array[0]=x*vector[0];
                for(int i=1;i<N;i++)
                        array[i]=x*vector[i];
                return new IntegerVector(array);
        }

// SCALAR PRODUCT

        /**
        * Returns the scalar product of this vector and another.
        * @param v an integer vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public int scalarProduct(final IntegerVector v) {
                switch(v.storageFormat) {
                        case ARRAY_1D: return rawScalarProduct(v);
                        default: 
                                if(N==v.N) {
                                        int answer=vector[0]*v.getComponent(0);
                                        for(int i=1;i<N;i++)
                                                answer+=vector[i]*v.getComponent(i);
                                        return answer;
                                } else
                                        throw new VectorDimensionException("Vectors are different sizes.");
                }
        }
        private int rawScalarProduct(final IntegerVector v) {
                if(N==v.N) {
                        int answer=vector[0]*v.vector[0];
                        for(int i=1;i<N;i++)
                                answer+=vector[i]*v.vector[i];
                        return answer;
                } else
                        throw new VectorDimensionException("Vectors are different sizes.");
        }
}

