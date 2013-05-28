package JSci.maths;

import JSci.GlobalSettings;

/**
* The DoubleDiagonalMatrix class encapsulates diagonal matrices containing doubles.
* Uses compressed diagonal storage.
* @version 2.2
* @author Mark Hale
*/
public final class DoubleDiagonalMatrix extends DoubleTridiagonalMatrix {
        /**
        * Storage format identifier.
        */
        protected final static int DIAGONAL=4;
        /**
        * Constructs a matrix.
        */
        protected DoubleDiagonalMatrix(final int size,final int storeID) {
                super(size,storeID);
        }
        /**
        * Constructs an empty matrix.
        * @param size the number of rows/columns
        */
        public DoubleDiagonalMatrix(final int size) {
                this(size,DIAGONAL);
                matrix=new double[1][size];
                diag=matrix[0];
        }
        /**
        * Constructs a matrix from an array.
        * @param array an assigned value
        * @exception MatrixDimensionException If the array is not square.
        */
        public DoubleDiagonalMatrix(final double array[][]) {
                this(array.length);
                if(array.length==array[0].length) {
                        for(int i=0;i<numRows;i++)
                                diag[i]=array[i][i];
                } else {
                        matrix=null;
                        throw new MatrixDimensionException("Array must be square.");
                }
        }
        /**
        * Constructs a matrix by wrapping an array containing the diagonal elements.
        * @param array an assigned value
        */
        public DoubleDiagonalMatrix(final double array[]) {
                this(array.length,DIAGONAL);
                matrix=new double[1][];
                matrix[0]=array;
                diag=matrix[0];
        }
        /**
        * Creates an identity matrix.
        * @param size the number of rows/columns
        */
        public static DoubleDiagonalMatrix identity(final int size) {
                double array[]=new double[size];
                for(int i=0;i<size;i++)
                        array[i]=1.0;
                return new DoubleDiagonalMatrix(array);
        }
        /**
        * Compares two double diagonal matrices for equality.
        * @param m a double diagonal matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof DoubleDiagonalMatrix) &&
                numRows==((DoubleDiagonalMatrix)m).rows()) {
                        final DoubleDiagonalMatrix ddm=(DoubleDiagonalMatrix)m;
                        for(int i=0;i<numRows;i++) {
                                if(Math.abs(diag[i]-ddm.getElement(i,i))>GlobalSettings.ZERO_TOL)
                                        return false;
                        }
                        return true;
                } else
                        return false;
        }
        /**
        * Converts this matrix to an integer matrix.
        * @return an integer diagonal matrix
        */
        public IntegerMatrix toIntegerMatrix() {
                final int array[]=new int[numRows];
                for(int i=0;i<numRows;i++)
                        array[i]=Math.round((float)diag[i]);
                return new IntegerDiagonalMatrix(array);
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex diagonal matrix
        */
        public ComplexMatrix toComplexMatrix() {
                return new ComplexDiagonalMatrix(diag,new double[numRows]);
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public double getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(i==j)
                                return diag[i];
                        else
                                return 0.0;
                } else
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
                if(i>=0 && i<numRows && j>=0 && j<numCols && i==j)
                        diag[i]=x;
                else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Returns true if this matrix is symmetric.
        */
        public boolean isSymmetric() {
                return true;
        }
        /**
        * Returns true if this matrix is unitary.
        */
        public boolean isUnitary() {
                return this.multiply(this).equals(identity(numRows));
        }
        /**
        * Returns the determinant.
        */
        public double det() {
                double det=diag[0];
                for(int i=1;i<numRows;i++)
                        det*=diag[i];
                return det;
        }
        /**
        * Returns the trace.
        */
        public double trace() {
                double tr=diag[0];
                for(int i=1;i<numRows;i++)
                        tr+=diag[i];
                return tr;
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public double infNorm() {
                double result=Math.abs(diag[0]);
                double tmpResult;
                for(int i=1;i<numRows;i++) {
                        tmpResult=Math.abs(diag[i]);
                        if(tmpResult>result)
                                result=tmpResult;
                }
                return result;
        }
        /**
        * Returns the Frobenius (l<sup>2</sup>) norm.
        */
        public double frobeniusNorm() {
                double result=diag[0];
                for(int i=1;i<numRows;i++)
                        result=ExtraMath.hypot(result,diag[i]);
                return result;
        }
        /**
        * Returns the operator norm.
        */
        public double operatorNorm() {
                return infNorm();
        }

//============
// OPERATIONS
//============

// ADDITION

        /**
        * Returns the addition of this matrix and another.
        * @param m a double matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleMatrix add(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleSquareMatrix rawAdd(final DoubleMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double array[][]=new double[numRows][numCols];
                        for(int i=0;i<numRows;i++)
                                System.arraycopy(m.matrix[i],0,array[i],0,numRows);
                        for(int i=0;i<numRows;i++)
                                array[i][i]+=diag[i];
                        return new DoubleSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a double square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSquareMatrix add(final DoubleSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a double tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleTridiagonalMatrix add(final DoubleTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
                                        ans.diag[0]=diag[0]+m.getElement(0,0);
                                        ans.udiag[0]=m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans.ldiag[i]=m.getElement(i,i-1);
                                                ans.diag[i]=diag[i]+m.getElement(i,i);
                                                ans.udiag[i]=m.getElement(i,i+1);
                                        }
                                        ans.ldiag[mRow]=m.getElement(mRow,mRow-1);
                                        ans.diag[mRow]=diag[mRow]+m.getElement(mRow,mRow);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleTridiagonalMatrix rawAddTridiagonal(final DoubleMatrix m) {
                if(numRows==m.numRows) {
                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(numRows);
                        System.arraycopy(m.matrix[0],0,ans.ldiag,0,m.matrix[0].length);
                        System.arraycopy(m.matrix[2],0,ans.udiag,0,m.matrix[2].length);
                        ans.diag[0]=diag[0]+m.matrix[1][0];
                        for(int i=1;i<numRows;i++)
                                ans.diag[i]=diag[i]+m.matrix[1][i];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a double diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleDiagonalMatrix add(final DoubleDiagonalMatrix m) {
                return rawAddDiagonal(m);
        }
        private DoubleDiagonalMatrix rawAddDiagonal(final DoubleMatrix m) {
                if(numRows==m.numRows) {
                        final double array[]=new double[numRows];
                        array[0]=diag[0]+m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]+m.matrix[0][i];
                        return new DoubleDiagonalMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this matrix and another.
        * @param m a double matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleMatrix subtract(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=-m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleSquareMatrix rawSubtract(final DoubleMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double array[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=-m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=-m.matrix[i][j];
                        }
                        for(int i=0;i<numRows;i++)
                                array[i][i]+=diag[i];
                        return new DoubleSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the subtraction of this matrix and another.
        * @param m a double square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSquareMatrix subtract(final DoubleSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=-m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the subtraction of this matrix and another.
        * @param m a double tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleTridiagonalMatrix subtract(final DoubleTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
                                        ans.diag[0]=diag[0]-m.getElement(0,0);
                                        ans.udiag[0]=-m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans.ldiag[i]=-m.getElement(i,i-1);
                                                ans.diag[i]=diag[i]-m.getElement(i,i);
                                                ans.udiag[i]=-m.getElement(i,i+1);
                                        }
                                        ans.ldiag[mRow]=-m.getElement(mRow,mRow-1);
                                        ans.diag[mRow]=diag[mRow]-m.getElement(mRow,mRow);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleTridiagonalMatrix rawSubtractTridiagonal(final DoubleMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
                        ans.diag[0]=diag[0]-m.matrix[1][0];
                        ans.udiag[0]=-m.matrix[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.ldiag[i]=-m.matrix[0][i];
                                ans.diag[i]=diag[i]-m.matrix[1][i];
                                ans.udiag[i]=-m.matrix[2][i];
                        }
                        ans.ldiag[mRow]=-m.matrix[0][mRow];
                        ans.diag[mRow]=diag[mRow]-m.matrix[1][mRow];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m a double diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleDiagonalMatrix subtract(final DoubleDiagonalMatrix m) {
                return rawSubtractDiagonal(m);
        }
        private DoubleDiagonalMatrix rawSubtractDiagonal(final DoubleMatrix m) {
                if(numRows==m.numRows) {
                        final double array[]=new double[numRows];
                        array[0]=diag[0]-m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]-m.matrix[0][i];
                        return new DoubleDiagonalMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLY

        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x a double.
        * @return a double diagonal matrix.
        */
        public DoubleMatrix scalarMultiply(final double x) {
                final double array[]=new double[numRows];
                array[0]=x*diag[0];
                for(int i=1;i<numRows;i++)
                        array[i]=x*diag[i];
                return new DoubleDiagonalMatrix(array);
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
                        case TRIDIAGONAL: return rawScalarProductTridiagonal(m);
                        case DIAGONAL: return rawScalarProductDiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        double ans=diag[0]*m.getElement(0,0);
                                        for(int i=1;i<numRows;i++)
                                                ans+=diag[i]*m.getElement(i,i);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private double rawScalarProduct(final DoubleMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        double ans=diag[0]*m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                ans+=diag[i]*m.matrix[i][i];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the scalar product of this matrix and another.
        * @param m a double square matrix.
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public double scalarProduct(final DoubleSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawScalarProduct(m);
                        case TRIDIAGONAL: return rawScalarProductTridiagonal(m);
                        case DIAGONAL: return rawScalarProductDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        double ans=diag[0]*m.getElement(0,0);
                                        for(int i=1;i<numRows;i++)
                                                ans+=diag[i]*m.getElement(i,i);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the scalar product of this matrix and another.
        * @param m a double tridiagonal matrix.
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public double scalarProduct(final DoubleTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawScalarProductTridiagonal(m);
                        case DIAGONAL: return rawScalarProductDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        double ans=diag[0]*m.getElement(0,0);
                                        for(int i=1;i<numRows;i++)
                                                ans+=diag[i]*m.getElement(i,i);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private double rawScalarProductTridiagonal(final DoubleMatrix m) {
                if(numRows==m.numRows) {
                        double ans=diag[0]*m.matrix[1][0];
                        for(int i=1;i<numRows;i++)
                                ans+=diag[i]*m.matrix[1][i];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the scalar product of this matrix and another.
        * @param m a double diagonal matrix.
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public double scalarProduct(final DoubleDiagonalMatrix m) {
                return rawScalarProductDiagonal(m);
        }
        private double rawScalarProductDiagonal(final DoubleMatrix m) {
                if(numRows==m.numRows) {
                        double ans=diag[0]*m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                ans+=diag[i]*m.matrix[0][i];
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
                        array[0]=diag[0]*v.getComponent(0);
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]*v.getComponent(i);
                        return new DoubleVector(array);
                } else
                        throw new DimensionException("Matrix and vector are incompatible.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleMatrix multiply(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                if(numCols==m.rows()) {
                                        final double array[][]=new double[numRows][m.columns()];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=diag[i]*m.getElement(i,0);
                                                for(j=1;j<m.columns();j++)
                                                        array[i][j]=diag[i]*m.getElement(i,j);
                                        }
                                        return new DoubleMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleMatrix rawMultiply(final DoubleMatrix m) {
                if(numCols==m.numRows) {
                        final double array[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=diag[i]*m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=diag[i]*m.matrix[i][j];
                        }
                        return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSquareMatrix multiply(final DoubleSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                if(numCols==m.rows()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=diag[i]*m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=diag[i]*m.getElement(i,j);
                                        }
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleSquareMatrix rawMultiply(final DoubleSquareMatrix m) {
                if(numCols==m.numRows) {
                        final double array[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=diag[i]*m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=diag[i]*m.matrix[i][j];
                        }
                        return new DoubleSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSquareMatrix multiply(final DoubleTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
                                        ans.diag[0]=diag[0]*m.getElement(0,0);
                                        ans.udiag[0]=diag[0]*m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans.ldiag[i]=diag[i]*m.getElement(i,i-1);
                                                ans.diag[i]=diag[i]*m.getElement(i,i);
                                                ans.udiag[i]=diag[i]*m.getElement(i,i+1);
                                        }
                                        ans.ldiag[mRow]=diag[mRow]*m.getElement(mRow,mRow-1);
                                        ans.diag[mRow]=diag[mRow]*m.getElement(mRow,mRow);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                        }
        }
        private DoubleSquareMatrix rawMultiplyTridiagonal(final DoubleMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
                        ans.diag[0]=diag[0]*m.matrix[1][0];
                        ans.udiag[0]=diag[0]*m.matrix[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.ldiag[i]=diag[i]*m.matrix[0][i];
                                ans.diag[i]=diag[i]*m.matrix[1][i];
                                ans.udiag[i]=diag[i]*m.matrix[2][i];
                        }
                        ans.ldiag[mRow]=diag[mRow]*m.matrix[0][mRow];
                        ans.diag[mRow]=diag[mRow]*m.matrix[1][mRow];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleDiagonalMatrix multiply(final DoubleDiagonalMatrix m) {
                return rawMultiplyDiagonal(m);
        }
        private DoubleDiagonalMatrix rawMultiplyDiagonal(final DoubleMatrix m) {
                if(numRows==m.numRows) {
                        final double array[]=new double[numRows];
                        array[0]=diag[0]*m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]*m.matrix[0][i];
                        return new DoubleDiagonalMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// INVERSE

        /**
        * Returns the inverse of this matrix.
        * @return a double diagonal matrix
        */
        public DoubleSquareMatrix inverse() {
                final double array[]=new double[numRows];
                array[0]=1.0/diag[0];
                for(int i=1;i<numRows;i++)
                        array[i]=1.0/diag[i];
                return new DoubleDiagonalMatrix(array);
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return a double diagonal matrix
        */
        public Matrix transpose() {
                return this;
        }

// LU DECOMPOSITION

        /**
        * Returns the LU decomposition of this matrix.
        * @param pivot an empty array of length <code>rows()+1</code>
        * to hold the pivot information (null if not interested).
        * The last array element will contain the parity.
        * @return an array with [0] containing the L-matrix
        * and [1] containing the U-matrix.
        */
        public DoubleSquareMatrix[] luDecompose(int pivot[]) {
                if(LU!=null) {
                        if(pivot!=null)
                                System.arraycopy(LUpivot,0,pivot,0,pivot.length);
                        return LU;
                }
                if(pivot==null)
                        pivot=new int[numRows+1];
                for(int i=0;i<numRows;i++)
                        pivot[i]=i;
                pivot[numRows]=1;
                LU=new DoubleDiagonalMatrix[2];
                LU[0]=identity(numRows);
                LU[1]=this;
                LUpivot=new int[pivot.length];
                System.arraycopy(pivot,0,LUpivot,0,pivot.length);
                return LU;
        }

// CHOLESKY DECOMPOSITION

        /**
        * Returns the Cholesky decomposition of this matrix.
        * Matrix must be symmetric and positive definite.
        * @return an array with [0] containing the L-matrix and [1] containing the U-matrix.
        */
        public DoubleSquareMatrix[] choleskyDecompose() {
                final DoubleDiagonalMatrix lu[]=new DoubleDiagonalMatrix[2];
                final double array[]=new double[numRows];
                array[0]=Math.sqrt(diag[0]);
                for(int i=1;i<numRows;i++)
                        array[i]=Math.sqrt(diag[i]);
                lu[0]=new DoubleDiagonalMatrix(array);
                lu[1]=lu[0];
                return lu;
        }

// SINGULAR VALUE DECOMPOSITION

        /**
        * Returns the singular value decomposition of this matrix.
        * @return an array with [0] containing the U-matrix, [1] containing the S-matrix and [2] containing the V-matrix.
        */
        public DoubleSquareMatrix[] singularValueDecompose() {
                final int N=numRows;
                final int Nm1=N-1;
                final double arrayU[]=new double[N];
                final double arrayS[]=new double[N];
                final double arrayV[]=new double[N];
                for(int i=0;i<Nm1;i++) {
                        arrayU[i]=-1.0;
                        arrayS[i]=Math.abs(diag[i]);
                        arrayV[i]=diag[i]<0.0 ? 1.0 : -1.0;
                }
                arrayU[Nm1]=1.0;
                arrayS[Nm1]=Math.abs(diag[Nm1]);
                arrayV[Nm1]=diag[Nm1]<0.0 ? -1.0 : 1.0;
                final DoubleSquareMatrix svd[]=new DoubleSquareMatrix[3];
                svd[0]=new DoubleDiagonalMatrix(arrayU);
                svd[1]=new DoubleDiagonalMatrix(arrayS);
                svd[2]=new DoubleDiagonalMatrix(arrayV);
                return svd;
        }

// MAP ELEMENTS

        /**
        * Applies a function on all the matrix elements.
        * @param f a user-defined function
        * @return a double diagonal matrix
        */
        public DoubleMatrix mapElements(final Mapping f) {
                final double array[]=new double[numRows];
                array[0]=f.map(diag[0]);
                for(int i=1;i<numRows;i++)
                        array[i]=f.map(diag[i]);
                return new DoubleDiagonalMatrix(array);
        }
}

