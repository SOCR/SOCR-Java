package JSci.maths;

/**
* The IntegerTridiagonalMatrix class provides an object for encapsulating tridiagonal matrices containing integers.
* Uses compressed diagonal storage.
* @version 2.2
* @author Mark Hale
*/
public class IntegerTridiagonalMatrix extends IntegerSquareMatrix {
//
// example: element storage for a 3x3 tridiagonal matrix
//
// matrix[1][0] matrix[2][0]
// matrix[0][1] matrix[1][1] matrix[2][1]
//              matrix[0][2] matrix[1][2]
//
        /**
        * Storage format identifier.
        */
        protected final static int TRIDIAGONAL=3;
        /**
        * Tridiagonal data.
        */
        protected int ldiag[];
        protected int diag[];
        protected int udiag[];
        /**
        * Constructs a matrix.
        */
        protected IntegerTridiagonalMatrix(final int size,final int storeID) {
                super(size,storeID);
        }
        /**
        * Constructs an empty matrix.
        * @param size the number of rows/columns
        */
        public IntegerTridiagonalMatrix(final int size) {
                this(size,TRIDIAGONAL);
                matrix=new int[3][size];
                ldiag=matrix[0];
                diag=matrix[1];
                udiag=matrix[2];
        }
        /**
        * Constructs a matrix from an array.
        * Any non-tridiagonal elements in the array are ignored.
        * @param array an assigned value
        * @exception MatrixDimensionException If the array is not square.
        */
        public IntegerTridiagonalMatrix(final int array[][]) {
                this(array.length);
                if(array.length==array[0].length) {
                        diag[0]=array[0][0];
                        udiag[0]=array[0][1];
                        int i=1;
                        for(;i<array.length-1;i++) {
                                ldiag[i]=array[i][i-1];
                                diag[i]=array[i][i];
                                udiag[i]=array[i][i+1];
                        }
                        ldiag[i]=array[i][i-1];
                        diag[i]=array[i][i];
                } else {
                        matrix=null;
                        throw new MatrixDimensionException("The array is not square.");
                }
        }
        /**
        * Compares two integer tridiagonal matrices for equality.
        * @param m a integer tridiagonal matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof IntegerTridiagonalMatrix) &&
                numRows==((IntegerTridiagonalMatrix)m).rows()) {
                        final IntegerTridiagonalMatrix itm=(IntegerTridiagonalMatrix)m;
                        if(diag[0]!=itm.getElement(0,0))
                                return false;
                        if(udiag[0]!=itm.getElement(0,1))
                                return false;
                        int i=1;
                        for(;i<numRows-1;i++) {
                                if(ldiag[i]!=itm.getElement(i,i-1))
                                        return false;
                                if(diag[i]!=itm.getElement(i,i))
                                        return false;
                                if(udiag[i]!=itm.getElement(i,i+1))
                                        return false;
                        }
                        if(ldiag[i]!=itm.getElement(i,i-1))
                                return false;
                        if(diag[i]!=itm.getElement(i,i))
                                return false;
                        return true;
                } else {
                        return false;
                }
        }
        /**
        * Returns a string representing this matrix.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(5*rows()*columns());
                for(int j,i=0;i<rows();i++) {
                        for(j=0;j<columns();j++) {
                                buf.append(getElement(i,j));
                                buf.append(' ');
                        }
                        buf.append('\n');
                }
                return buf.toString();
        }
        /**
        * Converts this matrix to a double matrix.
        * @return a double tridiagonal matrix
        */
        public DoubleMatrix toDoubleMatrix() {
                final DoubleTridiagonalMatrix m=new DoubleTridiagonalMatrix(numRows);
                m.diag[0]=diag[0];
                m.udiag[0]=udiag[0];
                int i=1;
                for(;i<numRows-1;i++) {
                        m.ldiag[i]=ldiag[i];
                        m.diag[i]=diag[i];
                        m.udiag[i]=udiag[i];
                }
                m.ldiag[i]=ldiag[i];
                m.diag[i]=diag[i];
                return m;
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex tridiagonal matrix
        */
        public ComplexMatrix toComplexMatrix() {
                final ComplexTridiagonalMatrix m=new ComplexTridiagonalMatrix(numRows);
                m.diagRe[0]=diag[0];
                m.udiagRe[0]=udiag[0];
                int i=1;
                for(;i<numRows-1;i++) {
                        m.ldiagRe[i]=ldiag[i];
                        m.diagRe[i]=diag[i];
                        m.udiagRe[i]=udiag[i];
                }
                m.ldiagRe[i]=ldiag[i];
                m.diagRe[i]=diag[i];
                return m;
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public int getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(j>=i-1 && j<=i+1)
                                return matrix[j-i+1][i];
                        else
                                return 0;
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
        public void setElement(final int i, final int j, final int x) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(j>=i-1 && j<=i+1)
                                matrix[j-i+1][i]=x;
                        else
                                throw new MatrixDimensionException(getInvalidElementMsg(i,j));
                } else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Returns true if this matrix is symmetric.
        */
        public boolean isSymmetric() {
                if(ldiag[1]!=udiag[0])
                        return false;
                for(int i=1;i<numRows-1;i++) {
                        if(ldiag[i+1]!=udiag[i])
                                return false;
                }
                return true;
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
                int result=Math.abs(diag[0])+Math.abs(udiag[0]);
                int tmpResult;
                int i=1;
                for(;i<numRows-1;i++) {
                        tmpResult=Math.abs(ldiag[i])+Math.abs(diag[i])+Math.abs(udiag[i]);
                        if(tmpResult>result)
                                result=tmpResult;
                }
                tmpResult=Math.abs(ldiag[i])+Math.abs(diag[i]);
                if(tmpResult>result)
                        result=tmpResult;
                return result;
        }
        /**
        * Returns the Frobenius (l<sup>2</sup>) norm.
        * @author Taber Smith
        */
        public double frobeniusNorm() {
                int result=diag[0]*diag[0]+udiag[0]*udiag[0];
                int i=1;
                for(;i<numRows-1;i++)
                        result+=ldiag[i]*ldiag[i]+diag[i]*diag[i]+udiag[i]*udiag[i];
                result+=ldiag[i]*ldiag[i]+diag[i]*diag[i];
                return Math.sqrt(result);
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
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)+m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)+m.getElement(i,j);
                                        }
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
                        array[0][0]+=diag[0];
                        array[0][1]+=udiag[0];
                        int n=numCols-1;
                        for(int i=1;i<n;i++) {
                                array[i][i-1]+=ldiag[i];
                                array[i][i]+=diag[i];
                                array[i][i+1]+=udiag[i];
                        }
                        array[n][n-1]+=ldiag[n];
                        array[n][n]+=diag[n];
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
                        default: 
                                if(numRows==m.rows()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)+m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)+m.getElement(i,j);
                                        }
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
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
                                        ans.diag[0]=diag[0]+m.getElement(0,0);
                                        ans.udiag[0]=udiag[0]+m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans.ldiag[i]=ldiag[i]+m.getElement(i,i-1);
                                                ans.diag[i]=diag[i]+m.getElement(i,i);
                                                ans.udiag[i]=udiag[i]+m.getElement(i,i+1);
                                        }
                                        ans.ldiag[mRow]=ldiag[mRow]+m.getElement(mRow,mRow-1);
                                        ans.diag[mRow]=diag[mRow]+m.getElement(mRow,mRow);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerTridiagonalMatrix rawAddTridiagonal(final IntegerMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
                        ans.diag[0]=diag[0]+m.matrix[1][0];
                        ans.udiag[0]=udiag[0]+m.matrix[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.ldiag[i]=ldiag[i]+m.matrix[0][i];
                                ans.diag[i]=diag[i]+m.matrix[1][i];
                                ans.udiag[i]=udiag[i]+m.matrix[2][i];
                        }
                        ans.ldiag[mRow]=ldiag[mRow]+m.matrix[0][mRow];
                        ans.diag[mRow]=diag[mRow]+m.matrix[1][mRow];
                        return ans;
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
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)-m.getElement(i,j);
                                        }
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private IntegerSquareMatrix rawSubtract(final IntegerMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final int array[][]=new int[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=getElement(i,0)-m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=getElement(i,j)-m.matrix[i][j];
                        }
                        return new IntegerSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m an integer square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerSquareMatrix subtract(final IntegerSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final int array[][]=new int[numRows][numRows];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)-m.getElement(i,j);
                                        }
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m an integer tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerTridiagonalMatrix subtract(final IntegerTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
                                        ans.diag[0]=diag[0]-m.getElement(0,0);
                                        ans.udiag[0]=udiag[0]-m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans.ldiag[i]=ldiag[i]-m.getElement(i,i-1);
                                                ans.diag[i]=diag[i]-m.getElement(i,i);
                                                ans.udiag[i]=udiag[i]-m.getElement(i,i+1);
                                        }
                                        ans.ldiag[mRow]=ldiag[mRow]-m.getElement(mRow,mRow-1);
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
                        ans.udiag[0]=udiag[0]-m.matrix[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.ldiag[i]=ldiag[i]-m.matrix[0][i];
                                ans.diag[i]=diag[i]-m.matrix[1][i];
                                ans.udiag[i]=udiag[i]-m.matrix[2][i];
                        }
                        ans.ldiag[mRow]=ldiag[mRow]-m.matrix[0][mRow];
                        ans.diag[mRow]=diag[mRow]-m.matrix[1][mRow];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x an integer
        * @return an integer tridiagonal matrix
        */
        public IntegerMatrix scalarMultiply(final int x) {
                int mRow=numRows;
                final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(mRow);
                ans.diag[0]=x*diag[0];
                ans.udiag[0]=x*udiag[0];
                mRow--;
                for(int i=1;i<mRow;i++) {
                        ans.ldiag[i]=x*ldiag[i];
                        ans.diag[i]=x*diag[i];
                        ans.udiag[i]=x*udiag[i];
                }
                ans.ldiag[mRow]=x*ldiag[mRow];
                ans.diag[mRow]=x*diag[mRow];
                return ans;
        }

// MATRIX MULTIPLICATION

        /**
        * Returns the multiplication of a vector by this matrix.
        * @param v an integer vector
        * @exception DimensionException If the matrix and vector are incompatible.
        */
        public IntegerVector multiply(final IntegerVector v) {
                int mRow=numRows;
                if(mRow==v.dimension()) {
                        final int array[]=new int[mRow];
                        array[0]=diag[0]*v.getComponent(0)+udiag[0]*v.getComponent(1);
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                array[i]=ldiag[i]*v.getComponent(i-1)+diag[i]*v.getComponent(i)+udiag[i]*v.getComponent(i+1);
                        }
                        array[mRow]=ldiag[mRow]*v.getComponent(mRow-1)+diag[mRow]*v.getComponent(mRow);
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
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final int array[][]=new int[numRows][m.columns()];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<m.columns();k++) {
                                                        array[j][k]=getElement(j,0)*m.getElement(0,k);
                                                        for(n=1;n<numCols;n++)
                                                                array[j][k]+=getElement(j,n)*m.getElement(n,k);
                                                }
                                        }
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
                                        array[j][k]=getElement(j,0)*m.matrix[0][k];
                                        for(n=1;n<numCols;n++)
                                                array[j][k]+=getElement(j,n)*m.matrix[n][k];
                                }
                        }
                        return new IntegerMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
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
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final int array[][]=new int[numRows][numCols];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<numCols;k++) {
                                                        array[j][k]=getElement(j,0)*m.getElement(0,k);
                                                        for(n=1;n<numCols;n++)
                                                                array[j][k]+=getElement(j,n)*m.getElement(n,k);
                                                }
                                        }
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private IntegerSquareMatrix rawMultiply(final IntegerSquareMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        final int array[][]=new int[numRows][numCols];
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<numCols;k++) {
                                        array[j][k]=getElement(j,0)*m.matrix[0][k];
                                        for(n=1;n<numCols;n++)
                                                array[j][k]+=getElement(j,n)*m.matrix[n][k];
                                }
                        }
                        return new IntegerSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m an integer tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public IntegerSquareMatrix multiply(final IntegerTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final int array[][]=new int[mRow][mRow];
                                        array[0][0]=diag[0]*m.getElement(0,0)+udiag[0]*m.getElement(1,0);
                                        array[0][1]=diag[0]*m.getElement(0,1)+udiag[0]*m.getElement(1,1);
                                        array[0][2]=udiag[0]*m.getElement(1,2);
                                        if(mRow>3) {
                                                array[1][0]=ldiag[1]*m.getElement(0,0)+diag[1]*m.getElement(1,0);
                                                array[1][1]=ldiag[1]*m.getElement(0,1)+diag[1]*m.getElement(1,1)+udiag[1]*m.getElement(2,1);
                                                array[1][2]=diag[1]*m.getElement(1,2)+udiag[1]*m.getElement(2,2);
                                                array[1][3]=udiag[1]*m.getElement(2,3);
                                        }
                                        if(mRow==3) {
                                                array[1][0]=ldiag[1]*m.getElement(0,0)+diag[1]*m.getElement(1,0);
                                                array[1][1]=ldiag[1]*m.getElement(0,1)+diag[1]*m.getElement(1,1)+udiag[1]*m.getElement(2,1);
                                                array[1][2]=diag[1]*m.getElement(1,2)+udiag[1]*m.getElement(2,2);
                                        } else if(mRow>4) {
                                                for(int i=2;i<mRow-2;i++) {
                                                        array[i][i-2]=ldiag[i]*m.getElement(i-1,i-2);
                                                        array[i][i-1]=ldiag[i]*m.getElement(i-1,i-1)+diag[i]*m.getElement(i,i-1);
                                                        array[i][i]=ldiag[i]*m.getElement(i-1,i)+diag[i]*m.getElement(i,i)+udiag[i]*m.getElement(i+1,i);
                                                        array[i][i+1]=diag[i]*m.getElement(i,i+1)+udiag[i]*m.getElement(i+1,i+1);
                                                        array[i][i+2]=udiag[i]*m.getElement(i+1,i+2);
                                                }
                                        }
                                        if(mRow>3) {
                                                array[mRow-2][mRow-4]=ldiag[mRow-2]*m.getElement(mRow-3,mRow-4);
                                                array[mRow-2][mRow-3]=ldiag[mRow-2]*m.getElement(mRow-3,mRow-3)+diag[mRow-2]*m.getElement(mRow-2,mRow-3);
                                                array[mRow-2][mRow-2]=ldiag[mRow-2]*m.getElement(mRow-3,mRow-2)+diag[mRow-2]*m.getElement(mRow-2,mRow-2)+udiag[mRow-2]*m.getElement(mRow-1,mRow-2);
                                                array[mRow-2][mRow-1]=diag[mRow-2]*m.getElement(mRow-2,mRow-1)+udiag[mRow-2]*m.getElement(mRow-1,mRow-1);
                                        }
                                        mRow--;
                                        array[mRow][mRow-2]=ldiag[mRow]*m.getElement(mRow-1,mRow-2);
                                        array[mRow][mRow-1]=ldiag[mRow]*m.getElement(mRow-1,mRow-1)+diag[mRow]*m.getElement(mRow,mRow-1);
                                        array[mRow][mRow]=ldiag[mRow]*m.getElement(mRow-1,mRow)+diag[mRow]*m.getElement(mRow,mRow);
                                        return new IntegerSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private IntegerSquareMatrix rawMultiplyTridiagonal(final IntegerMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final int array[][]=new int[mRow][mRow];
                        array[0][0]=diag[0]*m.matrix[1][0]+udiag[0]*m.matrix[0][1];
                        array[0][1]=diag[0]*m.matrix[2][0]+udiag[0]*m.matrix[1][1];
                        array[0][2]=udiag[0]*m.matrix[2][1];
                        if(mRow>3) {
                                array[1][0]=ldiag[1]*m.matrix[1][0]+diag[1]*m.matrix[0][1];
                                array[1][1]=ldiag[1]*m.matrix[2][0]+diag[1]*m.matrix[1][1]+udiag[1]*m.matrix[0][2];
                                array[1][2]=diag[1]*m.matrix[2][1]+udiag[1]*m.matrix[1][2];
                                array[1][3]=udiag[1]*m.matrix[2][2];
                        }
                        if(mRow==3) {
                                array[1][0]=ldiag[1]*m.matrix[1][0]+diag[1]*m.matrix[0][1];
                                array[1][1]=ldiag[1]*m.matrix[2][0]+diag[1]*m.matrix[1][1]+udiag[1]*m.matrix[0][2];
                                array[1][2]=diag[1]*m.matrix[2][1]+udiag[1]*m.matrix[1][2];
                        } else if(mRow>4) {
                                for(int i=2;i<mRow-2;i++) {
                                        array[i][i-2]=ldiag[i]*m.matrix[0][i-1];
                                        array[i][i-1]=ldiag[i]*m.matrix[1][i-1]+diag[i]*m.matrix[0][i];
                                        array[i][i]=ldiag[i]*m.matrix[2][i-1]+diag[i]*m.matrix[1][i]+udiag[i]*m.matrix[0][i+1];
                                        array[i][i+1]=diag[i]*m.matrix[2][i]+udiag[i]*m.matrix[1][i+1];
                                        array[i][i+2]=udiag[i]*m.matrix[2][i+1];
                                }
                        }
                        if(mRow>3) {
                                array[mRow-2][mRow-4]=ldiag[mRow-2]*m.matrix[0][mRow-3];
                                array[mRow-2][mRow-3]=ldiag[mRow-2]*m.matrix[1][mRow-3]+diag[mRow-2]*m.matrix[0][mRow-2];
                                array[mRow-2][mRow-2]=ldiag[mRow-2]*m.matrix[2][mRow-3]+diag[mRow-2]*m.matrix[1][mRow-2]+udiag[mRow-2]*m.matrix[0][mRow-1];
                                array[mRow-2][mRow-1]=diag[mRow-2]*m.matrix[2][mRow-2]+udiag[mRow-2]*m.matrix[1][mRow-1];
                        }
                        mRow--;
                        array[mRow][mRow-2]=ldiag[mRow]*m.matrix[0][mRow-1];
                        array[mRow][mRow-1]=ldiag[mRow]*m.matrix[1][mRow-1]+diag[mRow]*m.matrix[0][mRow];
                        array[mRow][mRow]=ldiag[mRow]*m.matrix[2][mRow-1]+diag[mRow]*m.matrix[1][mRow];
                        return new IntegerSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return an integer tridiagonal matrix
        */
        public Matrix transpose() {
                final IntegerTridiagonalMatrix ans=new IntegerTridiagonalMatrix(numRows);
                System.arraycopy(ldiag,1,ans.udiag,0,ldiag.length-1);
                System.arraycopy(diag,0,ans.diag,0,diag.length);
                System.arraycopy(udiag,0,ans.ldiag,1,udiag.length-1);
                return ans;
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
                // exactly the same as in DoubleTridiagonalMatrix
                if(LU!=null) {
                        if(pivot!=null)
                                System.arraycopy(LUpivot,0,pivot,0,pivot.length);
                        return LU;
                }
                int i,j,k,pivotrow;
                final int N=numRows;
                final double arrayL[][]=new double[N][N];
                final double arrayU[][]=new double[N][N];
                double tmp,max;
                if(pivot==null)
                        pivot=new int[N+1];
                for(i=0;i<N;i++)
                        pivot[i]=i;
                pivot[N]=1;
        // LU decomposition to arrayU
                for(j=0;j<N;j++) {
                        for(i=0;i<j;i++) {
                                tmp=getElement(pivot[i],j);
                                for(k=0;k<i;k++)
                                        tmp-=arrayU[i][k]*arrayU[k][j];
                                arrayU[i][j]=tmp;
                        }
                        max=0.0;
                        pivotrow=j;
                        for(i=j;i<N;i++) {
                                tmp=getElement(pivot[i],j);
                                for(k=0;k<j;k++)
                                        tmp-=arrayU[i][k]*arrayU[k][j];
                                arrayU[i][j]=tmp;
                        // while we're here search for a pivot for arrayU[j][j]
                                tmp=Math.abs(tmp);
                                if(tmp>max) {
                                        max=tmp;
                                        pivotrow=i;
                                }
                        }
                // swap row j with pivotrow
                        if(pivotrow!=j) {
                                for(k=0;k<j+1;k++) {
                                        tmp=arrayU[j][k];
                                        arrayU[j][k]=arrayU[pivotrow][k];
                                        arrayU[pivotrow][k]=tmp;
                                }
                                k=pivot[j];
                                pivot[j]=pivot[pivotrow];
                                pivot[pivotrow]=k;
                                // update parity
                                pivot[N]=-pivot[N];
                        }
                // divide by pivot
                        tmp=arrayU[j][j];
                        for(i=j+1;i<N;i++)
                                arrayU[i][j]/=tmp;
                }
                // move lower triangular part to arrayL
                for(j=0;j<N;j++) {
                        arrayL[j][j]=1.0;
                        for(i=j+1;i<N;i++) {
                                arrayL[i][j]=arrayU[i][j];
                                arrayU[i][j]=0.0;
                        }
                }
                LU=new DoubleSquareMatrix[2];
                LU[0]=new DoubleSquareMatrix(arrayL);
                LU[1]=new DoubleSquareMatrix(arrayU);
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
                // exactly the same as in DoubleTridiagonalMatrix
                int i,j,k;
                final int N=numRows;
                final double arrayL[][]=new double[N][N];
                final double arrayU[][]=new double[N][N];
                double tmp;
                arrayL[0][0]=arrayU[0][0]=Math.sqrt(diag[0]);
                arrayL[1][0]=arrayU[0][1]=ldiag[1]/arrayL[0][0];
                for(j=1;j<N;j++) {
                        tmp=diag[j];
                        for(i=0;i<j;i++)
                                tmp-=arrayL[j][i]*arrayL[j][i];
                        arrayL[j][j]=arrayU[j][j]=Math.sqrt(tmp);
                        if(j+1<N) {
                                tmp=ldiag[j+1];
                                for(k=0;k<j+1;k++)
                                        tmp-=arrayL[j][k]*arrayU[k][j+1];
                                arrayL[j+1][j]=arrayU[j][j+1]=tmp/arrayU[j][j];
                                for(i=j+2;i<N;i++) {
                                        tmp=0.0;
                                        for(k=0;k<i;k++)
                                                tmp-=arrayL[j][k]*arrayU[k][i];
                                        arrayL[i][j]=arrayU[j][i]=tmp/arrayU[j][j];
                                }
                        }
                }
                final DoubleSquareMatrix lu[]=new DoubleSquareMatrix[2];
                lu[0]=new DoubleSquareMatrix(arrayL);
                lu[1]=new DoubleSquareMatrix(arrayU);
                return lu;
        }
}

