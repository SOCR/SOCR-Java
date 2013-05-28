package JSci.maths;

/**
* The IntegerDiagonalMatrix class provides an object for encapsulating diagonal matrices containing integers.
* Uses compressed diagonal storage.
* @version 2.2
* @author Mark Hale
*/
public final class IntegerDiagonalMatrix extends IntegerTridiagonalMatrix {
        /**
        * Storage format identifier.
        */
        protected final static int DIAGONAL=4;
        /**
        * Constructs a matrix.
        */
        protected IntegerDiagonalMatrix(final int size,final int storeID) {
                super(size,storeID);
        }
        /**
        * Constructs an empty matrix.
        * @param size the number of rows/columns
        */
        public IntegerDiagonalMatrix(final int size) {
                this(size,DIAGONAL);
                matrix=new int[1][size];
                diag=matrix[0];
        }
        /**
        * Constructs a matrix from an array.
        * @param array an assigned value
        * @exception MatrixDimensionException If the array is not square.
        */
        public IntegerDiagonalMatrix(final int array[][]) {
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
        public IntegerDiagonalMatrix(final int array[]) {
                this(array.length,DIAGONAL);
                matrix=new int[1][];
                matrix[0]=array;
                diag=matrix[0];
        }
        /**
        * Creates an identity matrix.
        * @param size the number of rows/columns
        */
        public static IntegerDiagonalMatrix identity(final int size) {
                final int array[]=new int[size];
                for(int i=0;i<size;i++)
                        array[i]=1;
                return new IntegerDiagonalMatrix(array);
        }
        /**
        * Compares two integer diagonal matrices for equality.
        * @param m a integer diagonal matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof IntegerDiagonalMatrix) &&
                numRows==((IntegerDiagonalMatrix)m).rows()) {
                        final IntegerDiagonalMatrix idm=(IntegerDiagonalMatrix)m;
                        for(int i=0;i<numRows;i++) {
                                if(diag[i]!=idm.getElement(i,i))
                                        return false;
                        }
                        return true;
                } else
                        return false;
        }
        /**
        * Converts this matrix to a double matrix.
        * @return a double diagonal matrix
        */
        public DoubleMatrix toDoubleMatrix() {
                final double array[]=new double[numRows];
                for(int i=0;i<numRows;i++)
                        array[i]=diag[i];
                return new DoubleDiagonalMatrix(array);
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex diagonal matrix
        */
        public ComplexMatrix toComplexMatrix() {
                final double arrayRe[]=new double[numRows];
                final double arrayIm[]=new double[numRows];
                for(int i=0;i<numRows;i++)
                        arrayRe[i]=diag[i];
                return new ComplexDiagonalMatrix(arrayRe,arrayIm);
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public int getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(i==j)
                                return diag[i];
                        else
                                return 0;
                } else
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
        public int det() {
                int det=diag[0];
                for(int i=1;i<numRows;i++)
                        det*=diag[i];
                return det;
        }
        /**
        * Returns the trace.
        */
        public int trace() {
                int tr=diag[0];
                for(int i=1;i<numRows;i++)
                        tr+=diag[i];
                return tr;
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public int infNorm() {
                int result=Math.abs(diag[0]);
                int tmpResult;
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

//============
// OPERATIONS
//============

// ADDITION

        /**
        * Returns the addition of this matrix and another.
        * @param m an integer matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerMatrix add(final IntegerMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerSquareMatrix rawAdd(final IntegerMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final int array[][]=new int[numRows][numCols];
                        for(int i=0;i<numRows;i++)
                                System.arraycopy(m.matrix[i],0,array[i],0,numRows);
                        for(int i=0;i<numCols;i++)
                                array[i][i]+=diag[i];
                        return new IntegerSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m an integer square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerSquareMatrix add(final IntegerSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m an integer tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerTridiagonalMatrix add(final IntegerTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
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
        private IntegerTridiagonalMatrix rawAddTridiagonal(final IntegerMatrix m) {
                if(numRows==m.numRows) {
                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(numRows);
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
        * @param m an integer diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerDiagonalMatrix add(final IntegerDiagonalMatrix m) {
                return rawAddDiagonal(m);
        }
        private IntegerDiagonalMatrix rawAddDiagonal(final IntegerMatrix m) {
                if(numRows==m.numRows) {
                        final int array[]=new int[numRows];
                        array[0]=diag[0]+m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]+m.matrix[0][i];
                        return new IntegerDiagonalMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this matrix and another.
        * @param m an integer matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerMatrix subtract(final IntegerMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=-m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerSquareMatrix rawSubtract(final IntegerMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final int array[][]=new int[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=-m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=-m.matrix[i][j];
                        }
                        for(int i=0;i<numRows;i++)
                                array[i][i]+=diag[i];
                        return new IntegerSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the subtraction of this matrix and another.
        * @param m an integer square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerSquareMatrix subtract(final IntegerSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                       array[i][j]=-m.getElement(i,j);
                                        }
                                        for(int i=0;i<numRows;i++)
                                                array[i][i]+=diag[i];
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the subtraction of this matrix and another.
        * @param m an integer tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerTridiagonalMatrix subtract(final IntegerTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
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
        private IntegerTridiagonalMatrix rawSubtractTridiagonal(final IntegerMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
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
        * @param m an integer diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerDiagonalMatrix subtract(final IntegerDiagonalMatrix m) {
                return rawSubtractDiagonal(m);
        }
        private IntegerDiagonalMatrix rawSubtractDiagonal(final IntegerMatrix m) {
                if(numRows==m.numRows) {
                        final int array[]=new int[numRows];
                        array[0]=diag[0]-m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]-m.matrix[0][i];
                        return new IntegerDiagonalMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x an integer
        * @return an integer diagonal matrix
        */
        public IntegerMatrix scalarMultiply(final int x) {
                final int array[]=new int[numRows];
                array[0]=x*diag[0];
                for(int i=1;i<numRows;i++)
                        array[i]=x*diag[i];
                return new IntegerDiagonalMatrix(array);
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
                        array[0]=diag[0]*v.getComponent(0);
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]*v.getComponent(i);
                        return new IntegerVector(array);
                } else
                        throw new DimensionException("Matrix and vector are incompatible.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m an integer matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerMatrix multiply(final IntegerMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                if(numCols==m.rows()) {
                                        final int array[][]=new int[numRows][m.columns()];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=diag[i]*m.getElement(i,0);
                                                for(j=1;j<m.columns();j++)
                                                        array[i][j]=diag[i]*m.getElement(i,j);
                                        }
                                        return new IntegerMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerMatrix rawMultiply(final IntegerMatrix m) {
                if(numCols==m.numRows) {
                        final int array[][]=new int[numRows][m.numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=diag[i]*m.matrix[i][0];
                                for(j=1;j<m.numCols;j++)
                                        array[i][j]=diag[i]*m.matrix[i][j];
                        }
                        return new IntegerMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m an integer square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerSquareMatrix multiply(final IntegerSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                if(numCols==m.rows()) {
                                        final int array[][]=new int[numRows][m.columns()];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=diag[i]*m.getElement(i,0);
                                                for(j=1;j<m.columns();j++)
                                                        array[i][j]=diag[i]*m.getElement(i,j);
                                        }
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerSquareMatrix rawMultiply(final IntegerSquareMatrix m) {
                if(numCols==m.numRows) {
                        final int array[][]=new int[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=diag[i]*m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=diag[i]*m.matrix[i][j];
                        }
                        return new IntegerSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m an integer tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerSquareMatrix multiply(final IntegerTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
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
        private IntegerSquareMatrix rawMultiplyTridiagonal(final IntegerMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
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
        * @param m an integer diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerDiagonalMatrix multiply(final IntegerDiagonalMatrix m) {
                return rawMultiplyDiagonal(m);
        }
        private IntegerDiagonalMatrix rawMultiplyDiagonal(final IntegerMatrix m) {
                if(numCols==m.numRows) {
                        final int array[]=new int[numRows];
                        array[0]=diag[0]*m.matrix[0][0];
                        for(int i=1;i<numRows;i++)
                                array[i]=diag[i]*m.matrix[0][i];
                        return new IntegerDiagonalMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return an integer diagonal matrix
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
                LU[0]=DoubleDiagonalMatrix.identity(numRows);
                LU[1]=(DoubleDiagonalMatrix)this.toDoubleMatrix();
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
}

