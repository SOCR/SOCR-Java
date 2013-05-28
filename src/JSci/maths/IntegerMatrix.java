package JSci.maths;

import JSci.maths.groups.AbelianGroup;
import JSci.maths.algebras.*;
import JSci.maths.fields.*;

/**
* The IntegerMatrix class provides an object for encapsulating matrices containing integers.
* @version 2.1
* @author Mark Hale
*/
public class IntegerMatrix extends Matrix {
        /**
        * Storage format identifier.
        */
        protected final static int ARRAY_2D=1;
        /**
        * Array containing the elements of the matrix.
        */
        protected int matrix[][];
        /**
        * Constructs a matrix.
        */
        protected IntegerMatrix(final int rows,final int cols,final int storeID) {
                super(rows,cols);
                storageFormat=storeID;
        }
        /**
        * Constructs an empty matrix.
        * @param rows the number of rows
        * @param cols the number of columns
        */
        public IntegerMatrix(final int rows,final int cols) {
                this(rows,cols,ARRAY_2D);
                matrix=new int[rows][cols];
        }
        /**
        * Constructs a matrix by wrapping an array.
        * @param array an assigned value
        */
        public IntegerMatrix(final int array[][]) {
                this(array.length,array[0].length,ARRAY_2D);
                matrix=array;
        }
        /**
        * Constructs a matrix from an array of vectors (columns).
        * @param array an assigned value
        */
        public IntegerMatrix(final IntegerVector array[]) {
                this(array[0].dimension(),array.length);
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                matrix[i][j]=array[j].getComponent(i);
                }
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
        * Compares two integer matrices for equality.
        * @param m an integer matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof IntegerMatrix) &&
                numRows==((IntegerMatrix)m).rows() && numCols==((IntegerMatrix)m).columns()) {
                        final IntegerMatrix im=(IntegerMatrix)m;
                        for(int j,i=0;i<numRows;i++) {
                                for(j=0;j<numCols;j++) {
                                        if(matrix[i][j]!=im.getElement(i,j)) {
                                                return false;
                                        }
                                }
                        }
                        return true;
                } else {
                        return false;
                }
        }
        /**
        * Returns a string representing this matrix.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(numRows*numCols);
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++) {
                                buf.append(matrix[i][j]);
                                buf.append(' ');
                        }
                        buf.append('\n');
                }
                return buf.toString();
        }
        /**
        * Returns a hashcode for this matrix.
        */
        public int hashCode() {
                return (int)Math.exp(infNorm());
        }
        /**
        * Converts this matrix to a double matrix.
        * @return a double matrix
        */
        public DoubleMatrix toDoubleMatrix() {
                final double ans[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                ans[i][j]=matrix[i][j];
                }
                return new DoubleMatrix(ans);
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex matrix
        */
        public ComplexMatrix toComplexMatrix() {
                final double arrayRe[][]=new double[numRows][numCols];
                final double arrayIm[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                arrayRe[i][j]=matrix[i][j];
                }
                return new ComplexMatrix(arrayRe,arrayIm);
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public int getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols)
                        return matrix[i][j];
                else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Sets the value of an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @param x an integer
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public void setElement(final int i, final int j, final int x) {
                if(i>=0 && i<numRows && j>=0 && j<numCols)
                        matrix[i][j]=x;
                else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public int infNorm() {
                int result=0,tmpResult;
                for(int j,i=0;i<numRows;i++) {
                        tmpResult=0;
                        for(j=0;j<numCols;j++)
                                tmpResult+=Math.abs(matrix[i][j]);
                        if(tmpResult>result)
                                result=tmpResult;
                }
                return result;
        }
        /**
        * Returns the Frobenius (l<sup>2</sup>) norm.
        */
        public double frobeniusNorm() {
                double result=0;
                for(int j,i=0;i<numRows;i++)
                        for(j=0;j<numCols;j++)
                                result=ExtraMath.hypot(result,matrix[i][j]);
                return result;
        }

//============
// OPERATIONS
//============

        /**
        * Returns the negative of this matrix.
        */
        public AbelianGroup.Member negate() {
                final int array[][]=new int[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=-matrix[i][0];
                        for(j=1;j<numCols;j++)
                                array[i][j]=-matrix[i][j];
                }
                return new IntegerMatrix(array);
        }

// ADDITION

        /**
        * Returns the addition of this matrix and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member m) {
                if(m instanceof IntegerMatrix)
                        return add((IntegerMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m an integer matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerMatrix add(final IntegerMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=matrix[i][0]+m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=matrix[i][j]+m.getElement(i,j);
                                        }
                                        return new IntegerMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerMatrix rawAdd(final IntegerMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final int array[][]=new int[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=matrix[i][0]+m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=matrix[i][j]+m.matrix[i][j];
                        }
                        return new IntegerMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this matrix by another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
                if(m instanceof IntegerMatrix)
                        return subtract((IntegerMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m an integer matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerMatrix subtract(final IntegerMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=matrix[i][0]-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=matrix[i][j]-m.getElement(i,j);
                                        }
                                        return new IntegerMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerMatrix rawSubtract(final IntegerMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final int array[][]=new int[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=matrix[i][0]-m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=matrix[i][j]-m.matrix[i][j];
                        }
                        return new IntegerMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this matrix by a scalar.
        */
        public Module.Member scalarMultiply(Ring.Member x) {
                if(x instanceof MathInteger)
                        return scalarMultiply(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x an integer
        * @return an integer matrix
        */
        public IntegerMatrix scalarMultiply(final int x) {
                final int array[][]=new int[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=x*matrix[i][0];
                        for(j=1;j<numCols;j++)
                                array[i][j]=x*matrix[i][j];
                }
                return new IntegerMatrix(array);
        }

// SCALAR DIVISON

        /**
        * Returns the division of this matrix by a scalar.
        */
        public VectorSpace.Member scalarDivide(Field.Member x) {
                throw new IllegalArgumentException("Member class not recognised by this method.");
        }

// MATRIX MULTIPLICATION

        /**
        * Returns the multiplication of a vector by this matrix.
        * @param v an integer vector
        * @exception DimensionException If the matrix and vector are incompatible.
        */
        public IntegerVector multiply(final IntegerVector v) {
                if(numCols==v.dimension()) {
                        final int array[]=new int[numRows];
                        for(int j,i=0;i<numRows;i++) {
                                array[i]=matrix[i][0]*v.getComponent(0);
                                for(j=1;j<numCols;j++)
                                        array[i]+=matrix[i][j]*v.getComponent(j);
                        }
                        return new IntegerVector(array);
                } else
                        throw new DimensionException("Matrix and vector are incompatible.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        */
        public Ring.Member multiply(final Ring.Member m) {
                if(m instanceof IntegerMatrix)
                        return multiply((IntegerMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m an integer matrix
        * @return an IntegerMatrix or a IntegerSquareMatrix as appropriate
        * @exception MatrixDimensionException If the matrices are incompatible.
        */
        public IntegerMatrix multiply(final IntegerMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final int array[][]=new int[numRows][m.columns()];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<m.columns();k++) {
                                                        array[j][k]=matrix[j][0]*m.getElement(0,k);
                                                        for(n=1;n<numCols;n++)
                                                                array[j][k]+=matrix[j][n]*m.getElement(n,k);
                                                }
                                        }
                                        if(numRows==m.columns())
                                                return new IntegerSquareMatrix(array);
                                        else
                                                return new IntegerMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private IntegerMatrix rawMultiply(final IntegerMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        final int array[][]=new int[numRows][m.numCols];
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<m.numCols;k++) {
                                        array[j][k]=matrix[j][0]*m.matrix[0][k];
                                        for(n=1;n<numCols;n++)
                                                array[j][k]+=matrix[j][n]*m.matrix[n][k];
                                }
                        }
                        if(numRows==m.numCols)
                                return new IntegerSquareMatrix(array);
                        else
                                return new IntegerMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return an integer matrix
        */
        public Matrix transpose() {
                final int array[][]=new int[numCols][numRows];
                for(int j,i=0;i<numRows;i++) {
                        array[0][i]=matrix[i][0];
                        for(j=1;j<numCols;j++)
                                array[j][i]=matrix[i][j];
                }
                return new IntegerMatrix(array);
        }
}

