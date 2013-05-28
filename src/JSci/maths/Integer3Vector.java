package JSci.maths;

/**
* The Integer3Vector class encapsulates integer 3-vectors.
* @version 2.0
* @author Mark Hale
*/
public class Integer3Vector extends IntegerVector {
        /**
        * Constructs an empty 3-vector.
        */
        public Integer3Vector() {
                super(3);
        }
        /**
        * Constructs a vector by wrapping an array.
        * @param array an assigned value
        */
        public Integer3Vector(final int array[]) {
                super(array);
        }
        /**
        * Constructs a 3-vector.
        * @param x x coordinate
        * @param y y coordinate
        * @param z z coordinate
        */
        public Integer3Vector(final int x,final int y,final int z) {
                super(3);
                vector[0]=x;
                vector[1]=y;
                vector[2]=z;
        }
        /**
        * Compares two integer vectors for equality.
        * @param a an integer 3-vector
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof Integer3Vector)) {
                        final Integer3Vector iv=(Integer3Vector)a;
                        return (vector[0]==iv.vector[0] && vector[1]==iv.vector[1] &&
                                vector[2]==iv.vector[2]);
                } else
                        return false;
        }
        /**
        * Returns a comma delimited string representing the value of this vector.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(5);
                buf.append(vector[0]).append(',').append(vector[1]).append(',').append(vector[2]);
                return buf.toString();
        }
        /**
        * Returns a hashcode for this vector.
        */
        public int hashCode() {
                return (int)Math.exp(norm());
        }
        /**
        * Converts this 3-vector to a double 3-vector.
        * @return a double 3-vector
        */
        public DoubleVector toDoubleVector() {
                return new Double3Vector(vector[0],vector[1],vector[2]);
        }
        /**
        * Converts this 3-vector to a complex 3-vector.
        * @return a complex 3-vector
        */
        public ComplexVector toComplexVector() {
                final double arrayRe[]={vector[0],vector[1],vector[2]};
                return new Complex3Vector(arrayRe,new double[3]);
        }
        /**
        * Returns a component of this vector.
        * @param n index of the vector component
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public int getComponent(final int n) {
                if(n>=0 && n<3)
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
                if(n>=0 && n<3)
                        vector[n]=x;
                else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Returns the l<sup>n</sup>-norm.
        */
        public double norm(final int n) {
                final double answer=Math.pow(vector[0],n)+Math.pow(vector[1],n)+Math.pow(vector[2],n);
                return Math.pow(answer,1.0/n);
        }
        /**
        * Returns the l<sup>2</sup>-norm (magnitude).
        */
        public double norm() {
                final int answer=vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2];
                return Math.sqrt(answer);
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public double infNorm() {
                int infNorm=vector[0];
                if(vector[1]>infNorm)
                        infNorm=vector[1];
                if(vector[2]>infNorm)
                        infNorm=vector[2];
                return infNorm;
        }

//============
// OPERATIONS
//============

// ADDITION

        /**
        * Returns the addition of this vector and another.
        * @param v an integer 3-vector
        */
        public Integer3Vector add(final Integer3Vector v) {
                return new Integer3Vector(
                        vector[0]+v.vector[0],
                        vector[1]+v.vector[1],
                        vector[2]+v.vector[2]
                );
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        * @param v an integer 3-vector
        */
        public Integer3Vector subtract(final Integer3Vector v) {
                return new Integer3Vector(
                        vector[0]-v.vector[0],
                        vector[1]-v.vector[1],
                        vector[2]-v.vector[2]
                );
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this vector by a scalar.
        * @param x an integer
        * @return an integer 3-vector
        */
        public IntegerVector scalarMultiply(final int x) {
                return new Integer3Vector(x*vector[0],x*vector[1],x*vector[2]);
        }

// SCALAR PRODUCT

        /**
        * Returns the scalar product of this vector and another.
        * @param v an integer 3-vector
        */
        public int scalarProduct(final Integer3Vector v) {
                return vector[0]*v.vector[0]+vector[1]*v.vector[1]+vector[2]*v.vector[2];
        }

// VECTOR PRODUCT

        /**
        * Returns the vector product of this vector and another.
        * @param v an integer 3-vector
        */
        public Integer3Vector multiply(final Integer3Vector v) {
                return new Integer3Vector(
                        vector[1]*v.vector[2]-v.vector[1]*vector[2],
                        vector[2]*v.vector[0]-v.vector[2]*vector[0],
                        vector[0]*v.vector[1]-v.vector[0]*vector[1]
                );
        }
}

