package JSci.maths;

import JSci.GlobalSettings;

/**
* The Double3Vector class encapsulates double 3-vectors.
* @version 2.0
* @author Mark Hale
*/
public class Double3Vector extends DoubleVector {
        /**
        * Constructs an empty 3-vector.
        */
        public Double3Vector() {
                super(3);
        }
        /**
        * Constructs a vector by wrapping an array.
        * @param array an assigned value
        */
        public Double3Vector(final double array[]) {
                super(array);
        }
        /**
        * Constructs a 3-vector.
        * @param x x coordinate
        * @param y y coordinate
        * @param z z coordinate
        */
        public Double3Vector(final double x,final double y,final double z) {
                super(3);
                vector[0]=x;
                vector[1]=y;
                vector[2]=z;
        }
        /**
        * Compares two double vectors for equality.
        * @param a a double 3-vector
        */
        public boolean equals(Object a) {
                if(a!=null && (a instanceof Double3Vector)) {
                        final Double3Vector dv=(Double3Vector)a;
                        return (Math.abs(vector[0]-dv.vector[0])<=GlobalSettings.ZERO_TOL &&
                                Math.abs(vector[1]-dv.vector[1])<=GlobalSettings.ZERO_TOL &&
                                Math.abs(vector[2]-dv.vector[2])<=GlobalSettings.ZERO_TOL);
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
        * Converts this 3-vector to an integer 3-vector.
        * @return an integer 3-vector
        */
        public IntegerVector toIntegerVector() {
                return new Integer3Vector(
                        Math.round((float)vector[0]),
                        Math.round((float)vector[1]),
                        Math.round((float)vector[2])
                );
        }
        /**
        * Converts this 3-vector to a complex 3-vector.
        * @return a complex 3-vector
        */
        public ComplexVector toComplexVector() {
                return new Complex3Vector(vector,new double[3]);
        }
        /**
        * Returns a component of this vector.
        * @param n index of the vector component
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public double getComponent(final int n) {
                if(n>=0 && n<3)
                        return vector[n];
                else
                        throw new VectorDimensionException(getInvalidComponentMsg(n));
        }
        /**
        * Sets the value of a component of this vector.
        * @param n index of the vector component
        * @param x a number
        * @exception VectorDimensionException If attempting to access an invalid component.
        */
        public void setComponent(final int n, final double x) {
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
                final double answer=vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2];
                return Math.sqrt(answer);
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public double infNorm() {
                double infNorm=vector[0];
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
        * @param v a double 3-vector
        */
        public Double3Vector add(final Double3Vector v) {
                return new Double3Vector(
                        vector[0]+v.vector[0],
                        vector[1]+v.vector[1],
                        vector[2]+v.vector[2]
                );
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this vector by another.
        * @param v a double 3-vector
        */
        public Double3Vector subtract(final Double3Vector v) {
                return new Double3Vector(
                        vector[0]-v.vector[0],
                        vector[1]-v.vector[1],
                        vector[2]-v.vector[2]
                );
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this vector by a scalar.
        * @param x a double
        * @return a double 3-vector
        */
        public DoubleVector scalarMultiply(final double x) {
                return new Double3Vector(x*vector[0],x*vector[1],x*vector[2]);
        }

// SCALAR DIVISION

        /**
        * Returns the division of this vector by a scalar.
        * @param x a double
        * @return a double 3-vector
        * @exception ArithmeticException If divide by zero.
        */
        public DoubleVector scalarDivide(final double x) {
                return new Double3Vector(vector[0]/x,vector[1]/x,vector[2]/x);
        }

// SCALAR PRODUCT

        /**
        * Returns the scalar product of this vector and another.
        * @param v a double 3-vector
        */
        public double scalarProduct(final Double3Vector v) {
                return vector[0]*v.vector[0]+vector[1]*v.vector[1]+vector[2]*v.vector[2];
        }

// VECTOR PRODUCT

        /**
        * Returns the vector product of this vector and another (so(3) algebra).
        * @param v a double 3-vector
        */
        public Double3Vector multiply(final Double3Vector v) {
                return new Double3Vector(
                        vector[1]*v.vector[2]-v.vector[1]*vector[2],
                        vector[2]*v.vector[0]-v.vector[2]*vector[0],
                        vector[0]*v.vector[1]-v.vector[0]*vector[1]
                );
        }

// MAP COMPONENTS

        /**
        * Applies a function on all the vector components.
        * @param f a user-defined function
        * @return a double 3-vector
        */
        public DoubleVector mapComponents(final Mapping f) {
                return new Double3Vector(f.map(vector[0]),f.map(vector[1]),f.map(vector[2]));
        }
}

