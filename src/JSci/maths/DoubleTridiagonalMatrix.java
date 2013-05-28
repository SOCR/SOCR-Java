package JSci.maths;

import JSci.GlobalSettings;

/**
* The DoubleTridiagonalMatrix class provides an object for encapsulating tridiagonal matrices containing doubles.
* Uses compressed diagonal storage.
* @version 2.2
* @author Mark Hale
*/
public class DoubleTridiagonalMatrix extends DoubleSquareMatrix {
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
        protected double ldiag[];
        protected double diag[];
        protected double udiag[];
        /**
        * Constructs a matrix.
        */
        protected DoubleTridiagonalMatrix(final int size,final int storeID) {
                super(size,storeID);
        }
        /**
        * Constructs an empty matrix.
        * @param size the number of rows/columns
        */
        public DoubleTridiagonalMatrix(final int size) {
                this(size,TRIDIAGONAL);
                matrix=new double[3][size];
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
        public DoubleTridiagonalMatrix(final double array[][]) {
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
        * Compares two double tridiagonal matrices for equality.
        * @param m a double tridiagonal matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof DoubleTridiagonalMatrix) &&
                numRows==((DoubleTridiagonalMatrix)m).rows()) {
                        final DoubleTridiagonalMatrix dtm=(DoubleTridiagonalMatrix)m;
                        if(Math.abs(diag[0]-dtm.getElement(0,0))>GlobalSettings.ZERO_TOL)
                                return false;
                        if(Math.abs(udiag[0]-dtm.getElement(0,1))>GlobalSettings.ZERO_TOL)
                                return false;
                        int i=1;
                        for(;i<numRows-1;i++) {
                                if(Math.abs(ldiag[i]-dtm.getElement(i,i-1))>GlobalSettings.ZERO_TOL)
                                        return false;
                                if(Math.abs(diag[i]-dtm.getElement(i,i))>GlobalSettings.ZERO_TOL)
                                        return false;
                                if(Math.abs(udiag[i]-dtm.getElement(i,i+1))>GlobalSettings.ZERO_TOL)
                                        return false;
                        }
                        if(Math.abs(ldiag[i]-dtm.getElement(i,i-1))>GlobalSettings.ZERO_TOL)
                                return false;
                        if(Math.abs(diag[i]-dtm.getElement(i,i))>GlobalSettings.ZERO_TOL)
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
        * Converts this matrix to an integer matrix.
        * @return an integer tridiagonal matrix.
        */
        public IntegerMatrix toIntegerMatrix() {
                final IntegerTridiagonalMatrix m=new IntegerTridiagonalMatrix(numRows);
                m.diag[0]=Math.round((float)diag[0]);
                m.udiag[0]=Math.round((float)udiag[0]);
                int i=1;
                for(;i<numRows-1;i++) {
                        m.ldiag[i]=Math.round((float)ldiag[i]);
                        m.diag[i]=Math.round((float)diag[i]);
                        m.udiag[i]=Math.round((float)udiag[i]);
                }
                m.ldiag[i]=Math.round((float)ldiag[i]);
                m.diag[i]=Math.round((float)diag[i]);
                return m;
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex tridiagonal matrix.
        */
        public ComplexMatrix toComplexMatrix() {
                final ComplexTridiagonalMatrix m=new ComplexTridiagonalMatrix(numRows);
                m.matrixRe[1][0]=diag[0];
                m.matrixRe[2][0]=udiag[0];
                int i=1;
                for(;i<numRows-1;i++) {
                        m.matrixRe[0][i]=ldiag[i];
                        m.matrixRe[1][i]=diag[i];
                        m.matrixRe[2][i]=udiag[i];
                }
                m.matrixRe[0][i]=ldiag[i];
                m.matrixRe[1][i]=diag[i];
                return m;
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element.
        * @param j column index of the element.
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public double getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(j>=i-1 && j<=i+1)
                                return matrix[j-i+1][i];
                        else
                                return 0.0;
                } else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Sets the value of an element of the matrix.
        * @param i row index of the element.
        * @param j column index of the element.
        * @param x a number.
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public void setElement(final int i, final int j, final double x) {
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
                double result=Math.abs(diag[0])+Math.abs(udiag[0]);
                double tmpResult;
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
                double result=diag[0]*diag[0]+udiag[0]*udiag[0];
                int i=1;
                for(;i<numRows-1;i++)
                        result+=ldiag[i]*ldiag[i]+diag[i]*diag[i]+udiag[i]*udiag[i];
                result+=ldiag[i]*ldiag[i]+diag[i]*diag[i];
                return Math.sqrt(result);
        }
        /**
        * Returns the operator norm.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public double operatorNorm() throws MaximumIterationsExceededException {
                return Math.sqrt(ArrayMath.max(LinearMath.eigenvalueSolveSymmetric((DoubleTridiagonalMatrix)(this.transpose().multiply(this)))));
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
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)+m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)+m.getElement(i,j);
                                        }
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
                        array[0][0]+=diag[0];
                        array[0][1]+=udiag[0];
                        int n=numRows-1;
                        for(int i=1;i<n;i++) {
                                array[i][i-1]+=ldiag[i];
                                array[i][i]+=diag[i];
                                array[i][i+1]+=udiag[i];
                        }
                        array[n][n-1]+=ldiag[n];
                        array[n][n]+=diag[n];
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
                        default: 
                                if(numRows==m.rows()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)+m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)+m.getElement(i,j);
                                        }
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
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
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
        private DoubleTridiagonalMatrix rawAddTridiagonal(final DoubleMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
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
        * @param m a double matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleMatrix subtract(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)-m.getElement(i,j);
                                        }
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleSquareMatrix rawSubtract(final DoubleMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double array[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                array[i][0]=getElement(i,0)-m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]=getElement(i,j)-m.matrix[i][j];
                        }
                        return new DoubleSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m a double square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSquareMatrix subtract(final DoubleSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)-m.getElement(i,j);
                                        }
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
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
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
        private DoubleTridiagonalMatrix rawSubtractTridiagonal(final DoubleMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
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
        * @param x a double.
        * @return a double tridiagonal matrix.
        */
        public DoubleMatrix scalarMultiply(final double x) {
                int mRow=numRows;
                final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
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
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows() && mRow==m.columns()) {
                                        double ans=0.0;
                                        ans+=diag[0]*m.getElement(0,0);
                                        ans+=udiag[0]*m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans+=ldiag[i]*m.getElement(i,i-1);
                                                ans+=diag[i]*m.getElement(i,i);
                                                ans+=udiag[i]*m.getElement(i,i+1);
                                        }
                                        ans+=ldiag[mRow]*m.getElement(mRow,mRow-1);
                                        ans+=diag[mRow]*m.getElement(mRow,mRow);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private double rawScalarProduct(final DoubleMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows && mRow==m.numCols) {
                        double ans=0.0;
                        ans+=diag[0]*m.matrix[0][0];
                        ans+=udiag[0]*m.matrix[0][1];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans+=ldiag[i]*m.matrix[i][i-1];
                                ans+=diag[i]*m.matrix[i][i];
                                ans+=udiag[i]*m.matrix[i][i+1];
                        }
                        ans+=ldiag[mRow]*m.matrix[mRow][mRow-1];
                        ans+=diag[mRow]*m.matrix[mRow][mRow];
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
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        double ans=0.0;
                                        ans+=diag[0]*m.getElement(0,0);
                                        ans+=udiag[0]*m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans+=ldiag[i]*m.getElement(i,i-1);
                                                ans+=diag[i]*m.getElement(i,i);
                                                ans+=udiag[i]*m.getElement(i,i+1);
                                        }
                                        ans+=ldiag[mRow]*m.getElement(mRow,mRow-1);
                                        ans+=diag[mRow]*m.getElement(mRow,mRow);
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
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        double ans=0.0;
                                        ans+=diag[0]*m.getElement(0,0);
                                        ans+=udiag[0]*m.getElement(0,1);
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                ans+=ldiag[i]*m.getElement(i,i-1);
                                                ans+=diag[i]*m.getElement(i,i);
                                                ans+=udiag[i]*m.getElement(i,i+1);
                                        }
                                        ans+=ldiag[mRow]*m.getElement(mRow,mRow-1);
                                        ans+=diag[mRow]*m.getElement(mRow,mRow);
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private double rawScalarProductTridiagonal(final DoubleMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        double ans=0.0;
                        ans+=diag[0]*m.matrix[1][0];
                        ans+=udiag[0]*m.matrix[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans+=ldiag[i]*m.matrix[0][i];
                                ans+=diag[i]*m.matrix[1][i];
                                ans+=udiag[i]*m.matrix[2][i];
                        }
                        ans+=ldiag[mRow]*m.matrix[0][mRow];
                        ans+=diag[mRow]*m.matrix[1][mRow];
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
                int mRow=numRows;
                if(numCols==v.dimension()) {
                        final double array[]=new double[mRow];
                        array[0]=diag[0]*v.getComponent(0)+udiag[0]*v.getComponent(1);
                        mRow--;
                        for(int i=1;i<mRow;i++)
                                array[i]=ldiag[i]*v.getComponent(i-1)+diag[i]*v.getComponent(i)+udiag[i]*v.getComponent(i+1);
                        array[mRow]=ldiag[mRow]*v.getComponent(mRow-1)+diag[mRow]*v.getComponent(mRow);
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
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final double array[][]=new double[numRows][m.columns()];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<m.columns();k++) {
                                                        array[j][k]=getElement(j,0)*m.getElement(0,k);
                                                        for(n=1;n<numCols;n++)
                                                                array[j][k]+=getElement(j,n)*m.getElement(n,k);
                                                }
                                        }
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
                                        array[j][k]=getElement(j,0)*m.matrix[0][k];
                                        for(n=1;n<numCols;n++)
                                                array[j][k]+=getElement(j,n)*m.matrix[n][k];
                                }
                        }
                        return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
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
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<numCols;k++) {
                                                        array[j][k]=getElement(j,0)*m.getElement(0,k);
                                                        for(n=1;n<numCols;n++)
                                                                array[j][k]+=getElement(j,n)*m.getElement(n,k);
                                                }
                                        }
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private DoubleSquareMatrix rawMultiply(final DoubleSquareMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        final double array[][]=new double[numRows][numCols];
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<numCols;k++) {
                                        array[j][k]=getElement(j,0)*m.matrix[0][k];
                                        for(n=1;n<numCols;n++)
                                                array[j][k]+=getElement(j,n)*m.matrix[n][k];
                                }
                        }
                        return new DoubleSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSquareMatrix multiply(final DoubleTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(numCols==m.rows()) {
                                        final double array[][]=new double[mRow][mRow];
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
                                        return new DoubleSquareMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private DoubleSquareMatrix rawMultiplyTridiagonal(final DoubleMatrix m) {
                int mRow=numRows;
                if(numCols==m.numRows) {
                        final double array[][]=new double[mRow][mRow];
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
                        return new DoubleSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return a double tridiagonal matrix
        */
        public Matrix transpose() {
                final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(numRows);
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
                if(LU!=null) {
                        if(pivot!=null)
                                System.arraycopy(LUpivot,0,pivot,0,pivot.length);
                        return LU;
                }
                int i,j,k,pivotrow;
                final int N=numRows;
                final double arrayL[][]=new double[N][N];
                final double arrayU[][]=new double[N][N];
                final double buf[]=new double[N];
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
                                System.arraycopy(arrayU[j],0,buf,0,j+1);
                                System.arraycopy(arrayU[pivotrow],0,arrayU[j],0,j+1);
                                System.arraycopy(buf,0,arrayU[pivotrow],0,j+1);
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
                int i,j,k;
                final int N=numRows;
                final double arrayL[][]=new double[N][N];
                final double arrayU[][]=new double[N][N];
                double tmp=Math.sqrt(diag[0]);
                arrayL[0][0]=arrayU[0][0]=tmp;
                arrayL[1][0]=arrayU[0][1]=ldiag[1]/tmp;
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

// SINGULAR VALUE DECOMPOSITION

        /**
        * Returns the singular value decomposition of this matrix.
        * Based on the code from <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a> (public domain).
        * @return an array with [0] containing the U-matrix, [1] containing the S-matrix and [2] containing the V-matrix.
        */
        public DoubleSquareMatrix[] singularValueDecompose() {
                int i,j,k;
                final int N=numRows;
                final int Nm1=N-1;
                final double array[][]=new double[N][N];
                final double arrayU[][]=new double[N][N];
                final double arrayS[]=new double[N];
                final double arrayV[][]=new double[N][N];
                final double e[]=new double[N];
                final double work[]=new double[N];
                // copy matrix
                array[0][0]=diag[0];
                array[0][1]=udiag[0];
                for(i=1;i<Nm1;i++) {
                        array[i][i-1]=ldiag[i];
                        array[i][i]=diag[i];
                        array[i][i+1]=udiag[i];
                }
                array[Nm1][Nm1-1]=ldiag[Nm1];
                array[Nm1][Nm1]=diag[Nm1];
                // reduce matrix to bidiagonal form
                for(k=0;k<Nm1;k++) {
                        // compute the transformation for the kth column
                        // compute l2-norm of kth column
                        arrayS[k]=array[k][k];
                        for(i=k+1;i<N;i++)
                                arrayS[k]=ExtraMath.hypot(arrayS[k],array[i][k]);
                        if(arrayS[k]!=0.0) {
                                if(array[k][k]<0.0)
                                        arrayS[k]=-arrayS[k];
                                for(i=k;i<N;i++)
                                        array[i][k]/=arrayS[k];
                                array[k][k]+=1.0;
                        }
                        arrayS[k]=-arrayS[k];
                        for(j=k+1;j<N;j++) {
                                if(arrayS[k]!=0.0) {
                                        // apply the transformation
                                        double t=0.0;
                                        for(i=k;i<N;i++)
                                                t+=array[i][k]*array[i][j];
                                        t/=-array[k][k];
                                        for(i=k;i<N;i++)
                                                array[i][j]+=t*array[i][k];
                                }
                                e[j]=array[k][j];
                        }
                        for(i=k;i<N;i++)
                                arrayU[i][k]=array[i][k];
                        if(k<N-2) {
                                // compute the kth row transformation
                                // compute l2-norm of kth column
                                e[k]=e[k+1];
                                for(i=k+2;i<N;i++)
                                        e[k]=ExtraMath.hypot(e[k],e[i]);
                                if(e[k]!=0.0) {
                                        if(e[k+1]<0.0)
                                                e[k]=-e[k];
                                        for(i=k+1;i<N;i++)
                                                e[i]/=e[k];
                                        e[k+1]+=1.0;
                                }
                                e[k]=-e[k];
                                if(e[k]!=0.0) {
                                        // apply the transformation
                                        for(i=k+1;i<N;i++) {
                                                work[i]=0.0;
                                                for(j=k+1;j<N;j++)
                                                        work[i]+=e[j]*array[i][j];
                                        }
                                        for(j=k+1;j<N;j++) {
                                                double t=-e[j]/e[k+1];
                                                for(i=k+1;i<N;i++)
                                                        array[i][j]+=t*work[i];
                                        }
                                }
                                for(i=k+1;i<N;i++)
                                        arrayV[i][k]=e[i];
                        }
                }
                // setup the final bidiagonal matrix of order p
                int p=N;
                arrayS[Nm1]=array[Nm1][Nm1];
                e[N-2]=array[N-2][Nm1];
                e[Nm1]=0.0;
                for(i=0;i<N;i++)
                        arrayU[i][Nm1]=0.0;
                arrayU[Nm1][Nm1]=1.0;
                for(k=N-2;k>=0;k--) {
                        if(arrayS[k]!=0.0) {
                                for(j=k+1;j<N;j++) {
                                        double t=arrayU[k][k]*arrayU[k][j];
                                        for(i=k+1;i<N;i++)
                                                t+=arrayU[i][k]*arrayU[i][j];
                                        t/=-arrayU[k][k];
                                        for(i=k;i<N;i++)
                                                arrayU[i][j]+=t*arrayU[i][k];
                                }
                                for(i=k;i<N;i++)
                                        arrayU[i][k]=-arrayU[i][k];
                                arrayU[k][k]+=1.0;
                                for(i=0;i<k-1;i++)
                                        arrayU[i][k]=0.0;
                        } else {
                                for(i=0;i<N;i++)
                                        arrayU[i][k]=0.0;
                                arrayU[k][k]=1.0;
                        }
                }
                for(k=Nm1;k>=0;k--) {
                        if(k<N-2 && e[k]!=0.0) {
                                for(j=k+1;j<N;j++) {
                                        double t=arrayV[k+1][k]*arrayV[k+1][j];
                                        for(i=k+2;i<N;i++)
                                                t+=arrayV[i][k]*arrayV[i][j];
                                        t/=-arrayV[k+1][k];
                                        for(i=k+1;i<N;i++)
                                                arrayV[i][j]+=t*arrayV[i][k];
                                }
                        }
                        for(i=0;i<N;i++)
                                arrayV[i][k]=0.0;
                        arrayV[k][k]=1.0;
                }
                final double eps=Math.pow(2.0,-52.0);
                int iter=0;
                while(p>0) {
                        int action;
                        // action = 1 if arrayS[p] and e[k-1] are negligible and k<p
                        // action = 2 if arrayS[k] is negligible and k<p
                        // action = 3 if e[k-1] is negligible, k<p, and arrayS[k], ..., arrayS[p] are not negligible (QR step)
                        // action = 4 if e[p-1] is negligible (convergence)
                        for(k=p-2;k>=-1;k--) {
                                if(k==-1)
                                        break;
                                if(Math.abs(e[k])<=eps*(Math.abs(arrayS[k])+Math.abs(arrayS[k+1]))) {
                                        e[k]=0.0;
                                        break;
                                }
                        }
                        if(k==p-2) {
                                action=4;
                        } else {
                                int ks;
                                for(ks=p-1;ks>=k;ks--) {
                                        if(ks==k)
                                                break;
                                        double t=(ks!=p ? Math.abs(e[ks]) : 0.0)+(ks!=k+1 ? Math.abs(e[ks-1]) : 0.0);
                                        if(Math.abs(arrayS[ks])<=eps*t) {
                                                arrayS[ks]=0.0;
                                                break;
                                        }
                                }
                                if(ks==k) {
                                        action=3;
                                } else if(ks==p-1) {
                                        action=1;
                                } else {
                                        action=2;
                                        k=ks;
                                }
                        }
                        k++;
                        switch(action) {
                                // deflate negligible arrayS[p]
                                case 1: {
                                        double f=e[p-2];
                                        e[p-2]=0.0;
                                        for(j=p-2;j>=k;j--) {
                                                double t=ExtraMath.hypot(arrayS[j],f);
                                                final double cs=arrayS[j]/t;
                                                final double sn=f/t;
                                                arrayS[j]=t;
                                                if(j!=k) {
                                                        f=-sn*e[j-1];
                                                        e[j-1]*=cs;
                                                }
                                                for(i=0;i<N;i++) {
                                                        t=cs*arrayV[i][j]+sn*arrayV[i][p-1];
                                                        arrayV[i][p-1]=-sn*arrayV[i][j]+cs*arrayV[i][p-1];
                                                        arrayV[i][j]=t;
                                                }
                                        }
                                        } break;
                                // split at negligible arrayS[k]
                                case 2: {
                                        double f=e[k-1];
                                        e[k-1]=0.0;
                                        for(j=k;j<p;j++) {
                                                double t=ExtraMath.hypot(arrayS[j],f);
                                                final double cs=arrayS[j]/t;
                                                final double sn=f/t;
                                                arrayS[j]=t;
                                                f=-sn*e[j];
                                                e[j]*=cs;
                                                for(i=0;i<N;i++) {
                                                        t=cs*arrayU[i][j]+sn*arrayU[i][k-1];
                                                        arrayU[i][k-1]=-sn*arrayU[i][j]+cs*arrayU[i][k-1];
                                                        arrayU[i][j]=t;
                                                }
                                        }
                                        } break;
                                // perform one QR step
                                case 3: {
                                        // calculate the shift
                                        final double scale=Math.max(Math.max(Math.max(Math.max(
                                                Math.abs(arrayS[p-1]),Math.abs(arrayS[p-2])),Math.abs(e[p-2])),
                                                Math.abs(arrayS[k])),Math.abs(e[k]));
                                        double sp=arrayS[p-1]/scale;
                                        double spm1=arrayS[p-2]/scale;
                                        double epm1=e[p-2]/scale;
                                        double sk=arrayS[k]/scale;
                                        double ek=e[k]/scale;
                                        double b=((spm1+sp)*(spm1-sp)+epm1*epm1)/2.0;
                                        double c=(sp*epm1)*(sp*epm1);
                                        double shift=0.0;
                                        if(b!=0.0 || c!=0.0) {
                                                shift=Math.sqrt(b*b+c);
                                                if(b<0.0)
                                                        shift=-shift;
                                                shift=c/(b+shift);
                                        }
                                        double f=(sk+sp)*(sk-sp)+shift;
                                        double g=sk*ek;
                                        // chase zeros
                                        for(j=k;j<p-1;j++) {
                                                double t=ExtraMath.hypot(f,g);
                                                double cs=f/t;
                                                double sn=g/t;
                                                if(j!=k)
                                                        e[j-1]=t;
                                                f=cs*arrayS[j]+sn*e[j];
                                                e[j]=cs*e[j]-sn*arrayS[j];
                                                g=sn*arrayS[j+1];
                                                arrayS[j+1]*=cs;
                                                for(i=0;i<N;i++) {
                                                        t=cs*arrayV[i][j]+sn*arrayV[i][j+1];
                                                        arrayV[i][j+1]=-sn*arrayV[i][j]+cs*arrayV[i][j+1];
                                                        arrayV[i][j]=t;
                                                }
                                                t=ExtraMath.hypot(f,g);
                                                cs=f/t;
                                                sn=g/t;
                                                arrayS[j]=t;
                                                f=cs*e[j]+sn*arrayS[j+1];
                                                arrayS[j+1]=-sn*e[j]+cs*arrayS[j+1];
                                                g=sn*e[j+1];
                                                e[j+1]*=cs;
                                                if(j<Nm1) {
                                                        for(i=0;i<N;i++) {
                                                                t=cs*arrayU[i][j]+sn*arrayU[i][j+1];
                                                                arrayU[i][j+1]=-sn*arrayU[i][j]+cs*arrayU[i][j+1];
                                                                arrayU[i][j]=t;
                                                        }
                                                }
                                        }
                                        e[p-2]=f;
                                        iter++;
                                        } break;
                                // convergence
                                case 4: {
                                        // make the singular values positive
                                        if(arrayS[k]<=0.0) {
                                                arrayS[k]=-arrayS[k];
                                                for(i=0;i<p;i++)
                                                        arrayV[i][k]=-arrayV[i][k];
                                        }
                                        // order the singular values
                                        while(k<p-1) {
                                                if(arrayS[k]>=arrayS[k+1])
                                                        break;
                                                double tmp=arrayS[k];
                                                arrayS[k]=arrayS[k+1];
                                                arrayS[k+1]=tmp;
                                                if(k<Nm1) {
                                                        for(i=0;i<N;i++) {
                                                                tmp=arrayU[i][k+1];
                                                                arrayU[i][k+1]=arrayU[i][k];
                                                                arrayU[i][k]=tmp;
                                                                tmp=arrayV[i][k+1];
                                                                arrayV[i][k+1]=arrayV[i][k];
                                                                arrayV[i][k]=tmp;
                                                        }
                                                }
                                                k++;
                                        }
                                        iter=0;
                                        p--;
                                        } break;
                        }
                }
                final DoubleSquareMatrix svd[]=new DoubleSquareMatrix[3];
                svd[0]=new DoubleSquareMatrix(arrayU);
                svd[1]=new DoubleDiagonalMatrix(arrayS);
                svd[2]=new DoubleSquareMatrix(arrayV);
                return svd;
        }

// MAP ELEMENTS

        /**
        * Applies a function on all the matrix elements.
        * @param f a user-defined function
        * @return a double tridiagonal matrix
        */
        public DoubleMatrix mapElements(final Mapping f) {
                int mRow=numRows;
                final DoubleTridiagonalMatrix ans=new DoubleTridiagonalMatrix(mRow);
                ans.diag[0]=f.map(diag[0]);
                ans.udiag[0]=f.map(udiag[0]);
                mRow--;
                for(int i=1;i<mRow;i++) {
                        ans.ldiag[i]=f.map(ldiag[i]);
                        ans.diag[i]=f.map(diag[i]);
                        ans.udiag[i]=f.map(udiag[i]);
                }
                ans.ldiag[mRow]=f.map(ldiag[mRow]);
                ans.diag[mRow]=f.map(diag[mRow]);
                return ans;
        }
}

