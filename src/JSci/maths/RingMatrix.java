package JSci.maths;

import JSci.GlobalSettings;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.algebras.*;
import JSci.maths.fields.*;

/**
* The RingMatrix class provides an object for encapsulating matrices over an arbitrary ring.
* @version 1.0
* @author Mark Hale
*/
public class RingMatrix extends Matrix {
        /**
        * Storage format identifier.
        */
        protected final static int ARRAY_2D=1;
        /**
        * Array containing the elements of the matrix.
        */
        protected Ring.Member matrix[][];
        /**
        * Constructs a matrix.
        */
        protected RingMatrix(final int rows,final int cols,final int storeID) {
                super(rows,cols);
                storageFormat=storeID;
        }
        /**
        * Constructs a matrix by wrapping an array.
        * @param array an assigned value
        */
        public RingMatrix(final Ring.Member array[][]) {
                this(array.length,array[0].length,ARRAY_2D);
                matrix=array;
        }
        /**
        * Finalize.
        * @exception Throwable Any that occur.
        */
        protected void finalize() throws Throwable {
                matrix=null;
                super.finalize();
        }
        /**
        * Compares two matrices for equality.
        * @param m a matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof RingMatrix) &&
                numRows==((RingMatrix)m).rows() && numCols==((RingMatrix)m).columns()) {
                        final RingMatrix rm=(RingMatrix)m;
                        for(int j,i=0;i<numRows;i++) {
                                for(j=0;j<numCols;j++) {
                                        if(!matrix[i][j].equals(rm.getElement(i,j)))
                                                return false;
                                }
                        }
                        return true;
                } else
                        return false;
        }
        /**
        * Returns a string representing this matrix.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(5*numRows*numCols);
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++) {
                                buf.append(matrix[i][j].toString());
                                buf.append(' ');
                        }
                        buf.append('\n');
                }
                return buf.toString();
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public Ring.Member getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols)
                        return matrix[i][j];
                else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Sets the value of an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @param r a ring element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public void setElement(final int i, final int j, final Ring.Member r) {
                if(i>=0 && i<numRows && j>=0 && j<numCols)
                        matrix[i][j]=r;
                else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }

//============
// OPERATIONS
//============

        /**
        * Returns the negative of this matrix.
        */
        public AbelianGroup.Member negate() {
                final Ring.Member array[][]=new Ring.Member[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=(Ring.Member) matrix[i][0].negate();
                        for(j=1;j<numCols;j++)
                                array[i][j]=(Ring.Member) matrix[i][j].negate();
                }
                return new RingMatrix(array);
        }

// ADDITION

        /**
        * Returns the addition of this matrix and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member m) {
                if(m instanceof RingMatrix)
                        return add((RingMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public RingMatrix add(final RingMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final Ring.Member array[][]=new Ring.Member[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=(Ring.Member) matrix[i][0].add(m.getElement(i,0));
                                for(j=1;j<numCols;j++)
                                        array[i][j]=(Ring.Member) matrix[i][j].add(m.getElement(i,j));
                        }
                        return new RingMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this matrix and another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
                if(m instanceof RingMatrix)
                        return subtract((RingMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this matrix and another.
        * @param m a matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public RingMatrix subtract(final RingMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final Ring.Member array[][]=new Ring.Member[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=(Ring.Member) matrix[i][0].subtract(m.getElement(i,0));
                                for(j=1;j<numCols;j++)
                                        array[i][j]=(Ring.Member) matrix[i][j].subtract(m.getElement(i,j));
                        }
                        return new RingMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param r a ring element.
        */
        public Module.Member scalarMultiply(Ring.Member r) {
                final Ring.Member array[][]=new Ring.Member[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=r.multiply(matrix[i][0]);
                        for(j=1;j<numCols;j++)
                                array[i][j]=r.multiply(matrix[i][j]);
                }
                return new RingMatrix(array);
        }

// SCALAR DIVISON

        /**
        * Returns the division of this matrix by a scalar.
        * @param x a field element.
        */
        public VectorSpace.Member scalarDivide(Field.Member x) {
                final Ring.Member array[][]=new Ring.Member[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=((Field.Member)matrix[i][0]).divide(x);
                        for(j=1;j<numCols;j++)
                                array[i][j]=((Field.Member)matrix[i][j]).divide(x);
                }
                return new RingMatrix(array);
        }

// MATRIX MULTIPLICATION

        /**
        * Returns the multiplication of this matrix and another.
        */
        public Ring.Member multiply(final Ring.Member m) {
                if(m instanceof RingMatrix)
                        return multiply((RingMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a matrix
        * @exception MatrixDimensionException If the matrices are incompatible.
        */
        public RingMatrix multiply(final RingMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        final Ring.Member array[][]=new Ring.Member[numRows][m.numCols];
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<m.numCols;k++) {
                                        AbelianGroup.Member g=matrix[j][0].multiply(m.getElement(0,k));
                                        for(n=1;n<numCols;n++)
                                                g=g.add(matrix[j][n].multiply(m.getElement(n,k)));
                                        array[j][k]=(Ring.Member) g;
                                }
                        }
                        return new RingMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }

// DIRECT SUM

        /**
        * Returns the direct sum of this matrix and another.
        */
        public RingMatrix directSum(final RingMatrix m) {
                final Ring.Member array[][]=new Ring.Member[numRows+m.numRows][numCols+m.numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                array[i][j]=matrix[i][j];
                }
                for(int j,i=0;i<m.numRows;i++) {
                        for(j=0;j<m.numCols;j++)
                                array[i+numRows][j+numCols]=m.getElement(i,j);
                }
                return new RingMatrix(array);
        }

// TENSOR PRODUCT

        /**
        * Returns the tensor product of this matrix and another.
        */
        public RingMatrix tensorProduct(final RingMatrix m) {
                final Ring.Member array[][]=new Ring.Member[numRows*m.numRows][numCols*m.numCols];
                for(int i=0;i<numRows;i++) {
                        for(int j=0;j<numCols;j++) {
                                for(int k=0;k<m.numRows;j++) {
                                        for(int l=0;l<m.numCols;l++)
                                                array[i*m.numRows+k][j*m.numCols+l]=matrix[i][j].multiply(m.getElement(k,l));
                                }
                        }
                }
                return new RingMatrix(array);
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return a matrix
        */
        public Matrix transpose() {
                final Ring.Member array[][]=new Ring.Member[numCols][numRows];
                for(int j,i=0;i<numRows;i++) {
                        array[0][i]=matrix[i][0];
                        for(j=1;j<numCols;j++)
                                array[j][i]=matrix[i][j];
                }
                return new RingMatrix(array);
        }
}

