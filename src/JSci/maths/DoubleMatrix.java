package JSci.maths;

import JSci.GlobalSettings;
import JSci.maths.groups.AbelianGroup;
import JSci.maths.algebras.*;
import JSci.maths.fields.*;

/**
* The DoubleMatrix class provides an object for encapsulating double matrix algebras.
* @version 2.2
* @author Mark Hale
*/
public class DoubleMatrix extends Matrix {
        /**
        * Storage format identifier.
        */
        protected final static int ARRAY_2D=1;
        /**
        * Array containing the elements of the matrix.
        */
        protected double matrix[][];
        /**
        * Constructs a matrix.
        */
        protected DoubleMatrix(final int rows,final int cols,final int storeID) {
                super(rows,cols);
                storageFormat=storeID;
        }
        /**
        * Constructs an empty matrix.
        * @param rows the number of rows
        * @param cols the number of columns
        */
        public DoubleMatrix(final int rows,final int cols) {
                this(rows,cols,ARRAY_2D);
                matrix=new double[rows][cols];
        }
        /**
        * Constructs a matrix by wrapping an array.
        * @param array an assigned value
        */
        public DoubleMatrix(final double array[][]) {
                this(array.length,array[0].length,ARRAY_2D);
                matrix=array;
        }
        /**
        * Constructs a matrix from an array of vectors (columns).
        * @param array an assigned value
        */
        public DoubleMatrix(final DoubleVector array[]) {
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
        * Compares two double matrices for equality.
        * @param m a double matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof DoubleMatrix) &&
                numRows==((DoubleMatrix)m).rows() && numCols==((DoubleMatrix)m).columns()) {
                        final DoubleMatrix dm=(DoubleMatrix)m;
                        for(int j,i=0;i<numRows;i++) {
                                for(j=0;j<numCols;j++) {
                                        if(Math.abs(matrix[i][j]-dm.getElement(i,j))>GlobalSettings.ZERO_TOL)
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
        * Converts this matrix to an integer matrix.
        * @return an integer matrix
        */
        public IntegerMatrix toIntegerMatrix() {
                final int ans[][]=new int[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                ans[i][j]=Math.round((float)matrix[i][j]);
                }
                return new IntegerMatrix(ans);
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex matrix
        */
        public ComplexMatrix toComplexMatrix() {
                return new ComplexMatrix(matrix,new double[numRows][numCols]);
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public double getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols)
                        return matrix[i][j];
                else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Sets the value of an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @param x a number
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public void setElement(final int i, final int j, final double x) {
                if(i>=0 && i<numRows && j>=0 && j<numCols)
                        matrix[i][j]=x;
                else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public double infNorm() {
                double result=0.0,tmpResult;
                for(int j,i=0;i<numRows;i++) {
                        tmpResult=0.0;
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
                double result=0.0;
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
                final double array[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=-matrix[i][0];
                        for(j=1;j<numCols;j++)
                                array[i][j]=-matrix[i][j];
                }
                return new DoubleMatrix(array);
        }

// ADDITION

        /**
        * Returns the addition of this matrix and another.
        */
        public AbelianGroup.Member add(final AbelianGroup.Member m) {
                if(m instanceof DoubleMatrix)
                        return add((DoubleMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a double matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleMatrix add(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=matrix[i][0]+m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=matrix[i][j]+m.getElement(i,j);
                                        }
                                        return new DoubleMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleMatrix rawAdd(final DoubleMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double array[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=matrix[i][0]+m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=matrix[i][j]+m.matrix[i][j];
                        }
                        return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this matrix by another.
        */
        public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
                if(m instanceof DoubleMatrix)
                        return subtract((DoubleMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m a double matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleMatrix subtract(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=matrix[i][0]-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=matrix[i][j]-m.getElement(i,j);
                                        }
                                        return new DoubleMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleMatrix rawSubtract(final DoubleMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double array[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=matrix[i][0]-m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=matrix[i][j]-m.matrix[i][j];
                        }
                        return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this matrix by a scalar.
        */
        public Module.Member scalarMultiply(Ring.Member x) {
                if(x instanceof MathDouble)
                        return scalarMultiply(((MathDouble)x).value());
                else if(x instanceof MathInteger)
                        return scalarMultiply(((MathInteger)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x a double.
        * @return a double matrix.
        */
        public DoubleMatrix scalarMultiply(final double x) {
                final double array[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=x*matrix[i][0];
                        for(j=1;j<numCols;j++)
                                array[i][j]=x*matrix[i][j];
                }
                return new DoubleMatrix(array);
        }

// SCALAR DIVISON

        /**
        * Returns the division of this matrix by a scalar.
        */
        public VectorSpace.Member scalarDivide(Field.Member x) {
                if(x instanceof MathDouble)
                        return scalarDivide(((MathDouble)x).value());
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the division of this matrix by a scalar.
        * @param x a double.
        * @return a double matrix.
        */
        public DoubleMatrix scalarDivide(final double x) {
                final double array[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=matrix[i][0]/x;
                        for(j=1;j<numCols;j++)
                                array[i][j]=matrix[i][j]/x;
                }
                return new DoubleMatrix(array);
        }

// SCALAR PRODUCT

        /**
        * Returns the scalar product of this matrix and another.
        * @param m a double matrix.
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public double scalarProduct(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawScalarProduct(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        double ans=0.0;
                                        for(int j,i=0;i<numRows;i++) {
                                                ans+=matrix[i][0]*m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        ans+=matrix[i][j]*m.getElement(i,j);
                                        }
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private double rawScalarProduct(final DoubleMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        double ans=0.0;
                        for(int j,i=0;i<numRows;i++) {
                                ans+=matrix[i][0]*m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        ans+=matrix[i][j]*m.matrix[i][j];
                        }
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// MATRIX MULTIPLICATION

        /**
        * Returns the multiplication of a vector by this matrix.
        * @param v a double vector.
        * @exception DimensionException If the matrix and vector are incompatible.
        */
        public DoubleVector multiply(final DoubleVector v) {
                if(numCols==v.dimension()) {
                        final double array[]=new double[numRows];
                        for(int j,i=0;i<numRows;i++) {
                                array[i]=matrix[i][0]*v.getComponent(0);
                                for(j=1;j<numCols;j++)
                                        array[i]+=matrix[i][j]*v.getComponent(j);
                        }
                        return new DoubleVector(array);
                } else
                        throw new DimensionException("Matrix and vector are incompatible.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        */
        public Ring.Member multiply(final Ring.Member m) {
                if(m instanceof DoubleMatrix)
                        return multiply((DoubleMatrix)m);
                else
                        throw new IllegalArgumentException("Member class not recognised by this method.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double matrix
        * @return a DoubleMatrix or a DoubleSquareMatrix as appropriate
        * @exception MatrixDimensionException If the matrices are incompatible.
        */
        public DoubleMatrix multiply(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final double array[][]=new double[numRows][m.columns()];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<m.columns();k++) {
                                                        array[j][k]=matrix[j][0]*m.getElement(0,k);
                                                        for(n=1;n<numCols;n++)
                                                                array[j][k]+=matrix[j][n]*m.getElement(n,k);
                                                }
                                        }
                                        if(numRows==m.columns())
                                                return new DoubleSquareMatrix(array);
                                        else
                                                return new DoubleMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private DoubleMatrix rawMultiply(final DoubleMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        final double array[][]=new double[numRows][m.numCols];
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<m.numCols;k++) {
                                        array[j][k]=matrix[j][0]*m.matrix[0][k];
                                        for(n=1;n<numCols;n++)
                                                array[j][k]+=matrix[j][n]*m.matrix[n][k];
                                }
                        }
                        if(numRows==m.numCols)
                                return new DoubleSquareMatrix(array);
                        else
                                return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }

// DIRECT SUM

        /**
        * Returns the direct sum of this matrix and another.
        */
        public DoubleMatrix directSum(final DoubleMatrix m) {
                final double array[][]=new double[numRows+m.numRows][numCols+m.numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                array[i][j]=matrix[i][j];
                }
                for(int j,i=0;i<m.numRows;i++) {
                        for(j=0;j<m.numCols;j++)
                                array[i+numRows][j+numCols]=m.getElement(i,j);
                }
                return new DoubleMatrix(array);
        }

// TENSOR PRODUCT

        /**
        * Returns the tensor product of this matrix and another.
        */
        public DoubleMatrix tensorProduct(final DoubleMatrix m) {
                final double array[][]=new double[numRows*m.numRows][numCols*m.numCols];
                for(int i=0;i<numRows;i++) {
                        for(int j=0;j<numCols;j++) {
                                for(int k=0;k<m.numRows;j++) {
                                        for(int l=0;l<m.numCols;l++)
                                                array[i*m.numRows+k][j*m.numCols+l]=matrix[i][j]*m.getElement(k,l);
                                }
                        }
                }
                return new DoubleMatrix(array);
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return a double matrix
        */
        public Matrix transpose() {
                final double array[][]=new double[numCols][numRows];
                for(int j,i=0;i<numRows;i++) {
                        array[0][i]=matrix[i][0];
                        for(j=1;j<numCols;j++)
                                array[j][i]=matrix[i][j];
                }
                return new DoubleMatrix(array);
        }

// MAP ELEMENTS

        /**
        * Applies a function on all the matrix elements.
        * @param f a user-defined function
        * @return a double matrix
        */
        public DoubleMatrix mapElements(final Mapping f) {
                final double array[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        array[i][0]=f.map(matrix[i][0]);
                        for(j=1;j<numCols;j++)
                                array[i][j]=f.map(matrix[i][j]);
                }
                return new DoubleMatrix(array);
        }
}

