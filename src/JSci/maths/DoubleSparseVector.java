package JSci.maths;

import JSci.GlobalSettings;

/**
* The DoubleSparseVector class encapsulates sparse vectors.
* Uses Morse-coding.
* @author Daniel Lemire
* @author Alain Beliveau
*/
public final class DoubleSparseVector extends DoubleVector {
        /**
        * Storage format identifier.
        */
        protected final static int SPARSE=2;
        /**
        * Sparse indexing data.
        * Contains the component positions of each element,
        * e.g. <code>pos[n]</code> is the component position
        * of the <code>n</code>th element
        * (the <code>pos[n]</code>th component is stored at index <code>n</code>).
        */
        private int pos[];
        /**
        * Constructs an empty vector.
        * @param dim the dimension of the vector.
        */
        public DoubleSparseVector(final int dim) {
                super(dim,SPARSE);
                vector=new double[0];
                pos=new int[0];
        }
        /**
        * Constructs a vector from an array.
        */
        public DoubleSparseVector(double array[]) {
                super(array.length,SPARSE);
                int n=0;
                for(int i=0;i<N;i++) {
                        if(array[i]!=0.0)
                                n++;
                }
                vector=new double[n];
                pos=new int[n];
                n=0;
                for(int i=0;i<N;i++) {
                        if(array[i]!=0.0) {
                                vector[n]=array[i];
                                pos[n]=i;
                                n++;
                        }
                }
        }
        /**
        * Finalize.
        * @exception Throwable Any that occur.
        */
        protected void finalize() throws Throwable {
                pos=null;
                super.finalize();
        }
        /**
        * Compares two vectors for equality.
        * @param a a double sparse vector
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof DoubleSparseVector) && N==((DoubleSparseVector)a).N) {
                        DoubleSparseVector v=(DoubleSparseVector)a;
                        if(pos.length!=v.pos.length)
                                return false;
                        if(Math.abs(getComponent(0)-v.getComponent(0))>GlobalSettings.ZERO_TOL)
                                return false;
                        for(int i=1;i<N;i++) {
                                if(Math.abs(getComponent(i)-v.getComponent(i))>GlobalSettings.ZERO_TOL)
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
                StringBuffer buf=new StringBuffer(N);
                int i;
                for(i=0;i<N-1;i++) {
                        buf.append(getComponent(i));
                        buf.append(',');
                }
                buf.append(getComponent(i));
                return buf.toString();
        }
        /**
        * Returns a component of this vector.
        * @param n index of the vector component
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public double getComponent(int n) {
                if(n<0 || n>=N)
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
                for(int k=0;k<pos.length;k++) {
                        if(pos[k]==n)
                                return vector[k];
                }
                return 0.0;
        }
        /**
        * Sets the value of a component of this vector.
        * @param n index of the vector component
        * @param x a number
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public void setComponent(int n,double x) {
                if(n<0 || n>=N)
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
                if(Math.abs(x)<=GlobalSettings.ZERO_TOL)
                        return;
                for(int k=0;k<pos.length;k++) {
                        if(n==pos[k]) {
                                vector[k]=x;
                                return;
                        }
                }
                int newPos[]=new int[pos.length+1];
                double newVector[]=new double[vector.length+1];
                System.arraycopy(pos,0,newPos,0,pos.length);
                System.arraycopy(vector,0,newVector,0,pos.length);
                newPos[pos.length]=n;
                newVector[vector.length]=x;
                pos=newPos;
                vector=newVector;
        }
        /**
        * Returns the l<sup>2</sup>-norm (magnitude).
        */
        public double norm() {
                return Math.sqrt(sumSquares());
        }
        /**
        * Makes the norm of this vector equal to 1.
        */
        public void normalize() {
                final double norm=norm();
                for(int i=0;i<pos.length;i++)
                        vector[i]/=norm;
        }
        /**
        * Returns the sum of the squares of the components.
        */
        public double sumSquares() {
                double norm=0.0;
  		for(int k=0;k<pos.length;k++)
  			norm+=vector[k]*vector[k];
  		return norm;
        }
        /**
        * Returns the mass.
        */
        public double mass() {
                double mass=0.0;
  		for(int k=0;k<pos.length;k++)
  			mass+=vector[k];
  		return mass;
        }

//============
// OPERATIONS
//============

// ADDITION

        /**
        * Returns the addition of this vector and another.
        * @param v a double vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public DoubleVector add(final DoubleVector v) {
                switch(v.storageFormat) {
                        case SPARSE: return add((DoubleSparseVector)v);
                        case ARRAY_1D: return rawAdd(v);
                        default:
                                if(N!=v.N)
                                        throw new VectorDimensionException("Vectors are different sizes.");
                                double array[]=new double[N];
                                array[0]=v.getComponent(0);
                                for(int i=1;i<N;i++)
                                        array[i]=v.getComponent(i);
                                for(int i=0;i<pos.length;i++)
                                        array[pos[i]]+=vector[i];
                                return new DoubleVector(array);
                }
        }
        private DoubleVector rawAdd(final DoubleVector v) {
                if(N!=v.N)
                        throw new VectorDimensionException("Vectors are different sizes.");
                double array[]=new double[N];
                System.arraycopy(v.vector,0,array,0,N);
                for(int i=0;i<pos.length;i++)
                        array[pos[i]]+=vector[i];
                return new DoubleVector(array);
        }
        /**
        * Returns the addition of this vector and another.
        * @param v a double sparse vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public DoubleSparseVector add(final DoubleSparseVector v) {
                if(N!=v.N)
                        throw new VectorDimensionException("Vectors are different sizes.");
                double array[]=new double[N];
                for(int i=0;i<pos.length;i++)
                        array[pos[i]]=vector[i]+v.getComponent(pos[i]);
                for(int m,i=0;i<v.pos.length;i++) {
                        m=v.pos[i];
                        array[m]=getComponent(m)+v.vector[i];
                }
                return new DoubleSparseVector(array);
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        * @param v a double vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public DoubleVector subtract(final DoubleVector v) {
                switch(v.storageFormat) {
                        case SPARSE: return subtract((DoubleSparseVector)v);
                        case ARRAY_1D: return rawSubtract(v);
                        default:
                                if(N!=v.N)
                                        throw new VectorDimensionException("Vectors are different sizes.");
                                double array[]=new double[N];
                                array[0]=-v.getComponent(0);
                                for(int i=1;i<N;i++)
                                        array[i]=-v.getComponent(i);
                                for(int i=0;i<pos.length;i++)
                                        array[pos[i]]+=vector[i];
                                return new DoubleVector(array);
                }
        }
        private DoubleVector rawSubtract(final DoubleVector v) {
                if(N!=v.N)
                        throw new VectorDimensionException("Vectors are different sizes.");
                double array[]=new double[N];
                array[0]=-v.vector[0];
                for(int i=1;i<N;i++)
                        array[i]=-v.vector[i];
                for(int i=0;i<pos.length;i++)
                        array[pos[i]]+=vector[i];
                return new DoubleVector(array);
        }
        /**
        * Returns the subtraction of this vector by another.
        * @param v a double sparse vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public DoubleSparseVector subtract(final DoubleSparseVector v) {
                if(N!=v.N)
                        throw new VectorDimensionException("Vectors are different sizes.");
                double array[]=new double[N];
                for(int i=0;i<pos.length;i++)
                        array[pos[i]]=vector[i]-v.getComponent(pos[i]);
                for(int m,i=0;i<v.pos.length;i++) {
                        m=v.pos[i];
                        array[m]=getComponent(m)-v.vector[i];
                }
                return new DoubleSparseVector(array);
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this vector by a scalar.
        * @param x a double
        */
        public DoubleVector scalarMultiply(final double x) {
                final DoubleSparseVector ans=new DoubleSparseVector(N);
                ans.vector=new double[vector.length];
                ans.pos=new int[pos.length];
                System.arraycopy(pos,0,ans.pos,0,pos.length);
                for(int i=0;i<pos.length;i++)
                        ans.vector[i]=x*vector[i];
                return ans;
        }

// SCALAR DIVISION

        /**
        * Returns the division of this vector by a scalar.
        * @param x a double
        * @exception ArithmeticException If divide by zero.
        */
        public DoubleVector scalarDivide(final double x) {
                final DoubleSparseVector ans=new DoubleSparseVector(N);
                ans.vector=new double[vector.length];
                ans.pos=new int[pos.length];
                System.arraycopy(pos,0,ans.pos,0,pos.length);
                for(int i=0;i<pos.length;i++)
                        ans.vector[i]=vector[i]/x;
                return ans;
        }

// SCALAR PRODUCT

        /**
        * Returns the scalar product of this vector and another.
        * @param v a double vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public double scalarProduct(final DoubleVector v) {
                switch(v.storageFormat) {
                        case SPARSE: return scalarProduct((DoubleSparseVector)v);
                        case ARRAY_1D: return rawScalarProduct(v);
                        default:
                                if(N!=v.N)
                                        throw new VectorDimensionException("Vectors are different sizes.");
                                double ps=0.0;
                                for(int i=0;i<pos.length;i++)
                                        ps+=vector[i]*v.getComponent(pos[i]);
                                return ps;
                }
        }
        private double rawScalarProduct(final DoubleVector v) {
                if(N!=v.N)
                        throw new VectorDimensionException("Vectors are different sizes.");
                double ps=0.0;
                for(int i=0;i<pos.length;i++)
                        ps+=vector[i]*v.vector[pos[i]];
                return ps;
        }
        /**
        * Returns the scalar product of this vector and another.
        * @param v a double sparse vector
        * @exception VectorDimensionException If the vectors are different sizes.
        */
        public double scalarProduct(final DoubleSparseVector v) {
                if(N!=v.N)
                        throw new VectorDimensionException("Vectors are different sizes.");
                double ps=0.0;
                if(pos.length<=v.pos.length) {
                        for(int i=0;i<pos.length;i++)
                                ps+=vector[i]*v.getComponent(pos[i]);
                } else {
                        for(int i=0;i<v.pos.length;i++)
                                ps+=getComponent(v.pos[i])*v.vector[i];
                }
                return ps;
        }

// TENSOR PRODUCT

        /**
        * Returns the tensor product of this vector and another.
        */
        public DoubleSparseMatrix tensorProduct(final DoubleSparseVector v) {
                DoubleSparseMatrix ans=new DoubleSparseMatrix(N,v.N);
                for(int j,i=0;i<pos.length;i++) {
                        for(j=0;j<v.pos.length;j++)
                                ans.setElement(pos[i],v.pos[j],vector[i]*v.vector[j]);
                }
                return ans;
        }

// MAP COMPONENTS

        /**
        * Applies a function on all the vector components.
        * @param f a user-defined function
        * @return a double sparse vector
        */
        public DoubleVector mapComponents(final Mapping f) {
                final DoubleSparseVector ans=new DoubleSparseVector(N);
                ans.vector=new double[vector.length];
                ans.pos=new int[pos.length];
                System.arraycopy(pos,0,ans.pos,0,pos.length);
                for(int i=0;i<pos.length;i++)
                        ans.vector[i]=f.map(vector[i]);
                return ans;
        }
}

