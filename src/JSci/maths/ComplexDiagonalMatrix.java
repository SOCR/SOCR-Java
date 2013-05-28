

package JSci.maths;

/**
* The ComplexDiagonalMatrix class provides an object for encapsulating diagonal matrices containing complex numbers.
* Uses compressed diagonal storage.
* @version 2.2
* @author Mark Hale
*/
public final class ComplexDiagonalMatrix extends ComplexTridiagonalMatrix {
        /**
        * Storage format identifier.
        */
        protected final static int DIAGONAL=4;
        /**
        * Constructs a matrix.
        */
        protected ComplexDiagonalMatrix(final int size,final int storeID) {
                super(size,storeID);
        }
        /**
        * Constructs an empty matrix.
        * @param size the number of rows/columns
        */
        public ComplexDiagonalMatrix(final int size) {
                this(size,DIAGONAL);
                matrixRe=new double[1][size];
                matrixIm=new double[1][size];
                diagRe=matrixRe[0];
                diagIm=matrixIm[0];
        }
        /**
        * Constructs a matrix from an array.
        * @param array an assigned value
        * @exception MatrixDimensionException If the array is not square.
        */
        public ComplexDiagonalMatrix(final Complex array[][]) {
                this(array.length);
                if(array.length==array[0].length) {
                        for(int i=0;i<numRows;i++) {
                                diagRe[i]=array[i][i].real();
                                diagIm[i]=array[i][i].imag();
                        }
                } else {
                        matrixRe=null;
                        matrixIm=null;
                        throw new MatrixDimensionException("Array must be square.");
                }
        }
        /**
        * Constructs a matrix by wrapping two arrays containing the diagonal elements.
        * @param arrayRe an array of real values
        * @param arrayIm an array of imaginary values
        */
        public ComplexDiagonalMatrix(final double arrayRe[],final double arrayIm[]) {
                this(arrayRe.length,DIAGONAL);
                matrixRe=new double[1][];
                matrixIm=new double[1][];
                matrixRe[0]=arrayRe;
                matrixIm[0]=arrayIm;
                diagRe=matrixRe[0];
                diagIm=matrixIm[0];
        }
        /**
        * Constructs a matrix from an array containing the diagonal elements.
        * @param array an assigned value
        */
        public ComplexDiagonalMatrix(final Complex array[]) {
                this(array.length);
                diagRe[0]=array[0].real();
                diagIm[0]=array[0].imag();
                for(int i=1;i<array.length;i++) {
                        diagRe[i]=array[i].real();
                        diagIm[i]=array[i].imag();
                }
        }
        /**
        * Creates an identity matrix.
        * @param size the number of rows/columns
        */
        public static ComplexDiagonalMatrix identity(final int size) {
                final double arrayRe[]=new double[size];
                final double arrayIm[]=new double[size];
                for(int i=0;i<size;i++)
                        arrayRe[i]=1.0;
                return new ComplexDiagonalMatrix(arrayRe,arrayIm);
        }
        /**
        * Compares two complex diagonal matrices for equality.
        * @param m a complex diagonal matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof ComplexDiagonalMatrix) &&
                numRows==((ComplexDiagonalMatrix)m).rows()) {
                        final ComplexDiagonalMatrix cdm=(ComplexDiagonalMatrix)m;
                        for(int i=0;i<numRows;i++) {
                                if(!cdm.getElement(i,i).equals(diagRe[i],diagIm[i]))
                                        return false;
                        }
                        return true;
                } else
                        return false;
        }
        /**
        * Returns the real part of this complex matrix.
        * @return a double diagonal matrix
        */
        public DoubleMatrix real() {
                return new DoubleDiagonalMatrix(diagRe);
        }
        /**
        * Returns the imaginary part of this complex matrix.
        * @return a double diagonal matrix
        */
        public DoubleMatrix imag() {
                return new DoubleDiagonalMatrix(diagIm);
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public Complex getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(i==j)
                                return new Complex(diagRe[i],diagIm[i]);
                        else
                                return Complex.ZERO;
                } else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Sets the value of an element of the matrix.
        * Should only be used to initialise this matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @param z a complex number
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public void setElement(final int i, final int j, final Complex z) {
                if(i>=0 && i<numRows && j>=0 && j<numCols && i==j) {
                        diagRe[i]=z.real();
                        diagIm[i]=z.imag();
                } else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Sets the value of an element of the matrix.
        * Should only be used to initialise this matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @param x the real part of a complex number
        * @param y the imaginary part of a complex number
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public void setElement(final int i, final int j, final double x, final double y) {
                if(i>=0 && i<numRows && j>=0 && j<numCols && i==j) {
                        diagRe[i]=x;
                        diagIm[i]=y;
                } else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Returns true if this matrix is hermitian.
        */
        public boolean isHermitian() {
                return this.equals(this.conjugate());
        }
        /**
        * Returns true if this matrix is unitary.
        */
        public boolean isUnitary() {
                return this.multiply(this.conjugate()).equals(identity(numRows));
        }
        /**
        * Returns the determinant.
        */
        public Complex det() {
                double tmp;
                double detRe=diagRe[0];
                double detIm=diagIm[0];
                for(int i=1;i<numRows;i++) {
                        tmp=detRe*diagRe[i]-detIm*diagIm[i];
                        detIm=detIm*diagRe[i]+detRe*diagIm[i];
                        detRe=tmp;
                }
                return new Complex(detRe,detIm);
        }
        /**
        * Returns the trace.
        */
        public Complex trace() {
                double trRe=diagRe[0];
                double trIm=diagIm[0];
                for(int i=1;i<numRows;i++) {
                        trRe+=diagRe[i];
                        trIm+=diagIm[i];
                }
                return new Complex(trRe,trIm);
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        * @author Taber Smith
        */
        public double infNorm() {
                double result=diagRe[0]*diagRe[0]+diagIm[0]*diagIm[0];
                double tmpResult;
                for(int i=1;i<numRows;i++) {
                        tmpResult=diagRe[i]*diagRe[i]+diagIm[i]*diagIm[i];
                        if(tmpResult>result)
                                result=tmpResult;
                }
                return Math.sqrt(result);
        }
        /**
        * Returns the Frobenius (l<sup>2</sup>) norm.
        * @author Taber Smith
        */
        public double frobeniusNorm() {
                double result=diagRe[0]*diagRe[0]+diagIm[0]*diagIm[0];
                for(int i=1;i<numRows;i++)
                        result+=diagRe[i]*diagRe[i]+diagIm[i]*diagIm[i];
                return Math.sqrt(result);
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
        * @param m a complex matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexMatrix add(final ComplexMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=m.getElement(i,0);
                                                arrayRe[i][0]=elem.real();
                                                arrayIm[i][0]=elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=m.getElement(i,j);
                                                        arrayRe[i][j]=elem.real();
                                                        arrayIm[i][j]=elem.imag();
                                                }
                                        }
                                        for(int i=0;i<numRows;i++) {
                                                arrayRe[i][i]+=diagRe[i];
                                                arrayIm[i][i]+=diagIm[i];
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexSquareMatrix rawAdd(final ComplexMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double arrayRe[][]=new double[numRows][numCols];
                        final double arrayIm[][]=new double[numRows][numCols];
                        for(int i=0;i<numRows;i++) {
                                System.arraycopy(m.matrixRe[i],0,arrayRe[i],0,numCols);
                                System.arraycopy(m.matrixIm[i],0,arrayIm[i],0,numCols);
                        }
                        for(int i=0;i<numRows;i++) {
                                arrayRe[i][i]+=diagRe[i];
                                arrayIm[i][i]+=diagIm[i];
                        }
                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a complex square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexSquareMatrix add(final ComplexSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawAdd(m);
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=m.getElement(i,0);
                                                arrayRe[i][0]=elem.real();
                                                arrayIm[i][0]=elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=m.getElement(i,j);
                                                        arrayRe[i][j]=elem.real();
                                                        arrayIm[i][j]=elem.imag();
                                                }
                                        }
                                        for(int i=0;i<numRows;i++) {
                                                arrayRe[i][i]+=diagRe[i];
                                                arrayIm[i][i]+=diagIm[i];
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a complex tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexTridiagonalMatrix add(final ComplexTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawAddTridiagonal(m);
                        case DIAGONAL: return rawAddDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                                        Complex elem=m.getElement(0,0);
                                        ans.matrixRe[1][0]=diagRe[0]+elem.real();
                                        ans.matrixIm[1][0]=diagIm[0]+elem.imag();
                                        elem=m.getElement(0,1);
                                        ans.matrixRe[2][0]=elem.real();
                                        ans.matrixIm[2][0]=elem.imag();
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                elem=m.getElement(i,i-1);
                                                ans.matrixRe[0][i]=elem.real();
                                                ans.matrixIm[0][i]=elem.imag();
                                                elem=m.getElement(i,i);
                                                ans.matrixRe[1][i]=diagRe[i]+elem.real();
                                                ans.matrixIm[1][i]=diagIm[i]+elem.imag();
                                                elem=m.getElement(i,i+1);
                                                ans.matrixRe[2][i]=elem.real();
                                                ans.matrixIm[2][i]=elem.imag();
                                        }
                                        elem=m.getElement(mRow,mRow-1);
                                        ans.matrixRe[0][mRow]=elem.real();
                                        ans.matrixIm[0][mRow]=elem.imag();
                                        elem=m.getElement(mRow,mRow);
                                        ans.matrixRe[1][mRow]=diagRe[mRow]+elem.real();
                                        ans.matrixIm[1][mRow]=diagIm[mRow]+elem.imag();
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexTridiagonalMatrix rawAddTridiagonal(final ComplexMatrix m) {
                if(numRows==m.numRows) {
                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(numRows);
                        System.arraycopy(m.matrixRe[0],0,ans.matrixRe[0],0,m.matrixRe[0].length);
                        System.arraycopy(m.matrixIm[0],0,ans.matrixIm[0],0,m.matrixIm[0].length);
                        System.arraycopy(m.matrixRe[2],0,ans.matrixRe[2],0,m.matrixRe[2].length);
                        System.arraycopy(m.matrixIm[2],0,ans.matrixIm[2],0,m.matrixIm[2].length);
                        ans.matrixRe[1][0]=diagRe[0]+m.matrixRe[1][0];
                        ans.matrixIm[1][0]=diagIm[0]+m.matrixIm[1][0];
                        for(int i=1;i<numRows;i++) {
                                ans.matrixRe[1][i]=diagRe[i]+m.matrixRe[1][i];
                                ans.matrixIm[1][i]=diagIm[i]+m.matrixIm[1][i];
                        }
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a complex diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexDiagonalMatrix add(final ComplexDiagonalMatrix m) {
                return rawAddDiagonal(m);
        }
        private ComplexDiagonalMatrix rawAddDiagonal(final ComplexMatrix m) {
                if(numRows==m.numRows) {
                        final double arrayRe[]=new double[numRows];
                        final double arrayIm[]=new double[numRows];
                        arrayRe[0]=diagRe[0]+m.matrixRe[0][0];
                        arrayIm[0]=diagIm[0]+m.matrixIm[0][0];
                        for(int i=1;i<numRows;i++) {
                                arrayRe[i]=diagRe[i]+m.matrixRe[0][i];
                                arrayIm[i]=diagIm[i]+m.matrixIm[0][i];
                        }
                        return new ComplexDiagonalMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this matrix by another.
        * @param m a complex matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexMatrix subtract(final ComplexMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=m.getElement(i,0);
                                                arrayRe[i][0]=-elem.real();
                                                arrayIm[i][0]=-elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=m.getElement(i,j);
                                                        arrayRe[i][j]=-elem.real();
                                                        arrayIm[i][j]=-elem.imag();
                                                }
                                        }
                                        for(int i=0;i<numRows;i++) {
                                                arrayRe[i][i]+=diagRe[i];
                                                arrayIm[i][i]+=diagIm[i];
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexSquareMatrix rawSubtract(final ComplexMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double arrayRe[][]=new double[numRows][numCols];
                        final double arrayIm[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                arrayRe[i][0]=-m.matrixRe[i][0];
                                arrayIm[i][0]=-m.matrixIm[i][0];
                                for(j=1;j<numCols;j++) {
                                        arrayRe[i][j]=-m.matrixRe[i][j];
                                        arrayIm[i][j]=-m.matrixIm[i][j];
                                }
                        }
                        for(int i=0;i<numRows;i++) {
                                arrayRe[i][i]+=diagRe[i];
                                arrayIm[i][i]+=diagIm[i];
                        }
                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m a complex square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexSquareMatrix subtract(final ComplexSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                if(numRows==m.rows()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=m.getElement(i,0);
                                                arrayRe[i][0]=-elem.real();
                                                arrayIm[i][0]=-elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=m.getElement(i,j);
                                                        arrayRe[i][j]=-elem.real();
                                                        arrayIm[i][j]=-elem.imag();
                                                }
                                        }
                                        for(int i=0;i<numRows;i++) {
                                                arrayRe[i][i]+=diagRe[i];
                                                arrayIm[i][i]+=diagIm[i];
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m a complex tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexTridiagonalMatrix subtract(final ComplexTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        case DIAGONAL: return rawSubtractDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                                        Complex elem=m.getElement(0,0);
                                        ans.matrixRe[1][0]=diagRe[0]-elem.real();
                                        ans.matrixIm[1][0]=diagIm[0]-elem.imag();
                                        elem=m.getElement(0,1);
                                        ans.matrixRe[2][0]=-elem.real();
                                        ans.matrixIm[2][0]=-elem.imag();
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                elem=m.getElement(i,i-1);
                                                ans.matrixRe[0][i]=-elem.real();
                                                ans.matrixIm[0][i]=-elem.imag();
                                                elem=m.getElement(i,i);
                                                ans.matrixRe[1][i]=diagRe[i]-elem.real();
                                                ans.matrixIm[1][i]=diagIm[i]-elem.imag();
                                                elem=m.getElement(i,i+1);
                                                ans.matrixRe[2][i]=-elem.real();
                                                ans.matrixIm[2][i]=-elem.imag();
                                        }
                                        elem=m.getElement(mRow,mRow-1);
                                        ans.matrixRe[0][mRow]=-elem.real();
                                        ans.matrixIm[0][mRow]=-elem.imag();
                                        elem=m.getElement(mRow,mRow);
                                        ans.matrixRe[1][mRow]=diagRe[mRow]-elem.real();
                                        ans.matrixIm[1][mRow]=diagIm[mRow]-elem.imag();
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexTridiagonalMatrix rawSubtractTridiagonal(final ComplexMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                        ans.matrixRe[1][0]=diagRe[0]-m.matrixRe[1][0];
                        ans.matrixIm[1][0]=diagIm[0]-m.matrixIm[1][0];
                        ans.matrixRe[2][0]=-m.matrixRe[2][0];
                        ans.matrixIm[2][0]=-m.matrixIm[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.matrixRe[0][i]=-m.matrixRe[0][i];
                                ans.matrixIm[0][i]=-m.matrixIm[0][i];
                                ans.matrixRe[1][i]=diagRe[i]-m.matrixRe[1][i];
                                ans.matrixIm[1][i]=diagIm[i]-m.matrixIm[1][i];
                                ans.matrixRe[2][i]=-m.matrixRe[2][i];
                                ans.matrixIm[2][i]=-m.matrixIm[2][i];
                        }
                        ans.matrixRe[0][mRow]=-m.matrixRe[0][mRow];
                        ans.matrixIm[0][mRow]=-m.matrixIm[0][mRow];
                        ans.matrixRe[1][mRow]=diagRe[mRow]-m.matrixRe[1][mRow];
                        ans.matrixIm[1][mRow]=diagIm[mRow]-m.matrixIm[1][mRow];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the subtraction of this matrix by another.
        * @param m a complex diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexDiagonalMatrix subtract(final ComplexDiagonalMatrix m) {
                return rawSubtractDiagonal(m);
        }
        private ComplexDiagonalMatrix rawSubtractDiagonal(final ComplexMatrix m) {
                if(numRows==m.numRows) {
                        final double arrayRe[]=new double[numRows];
                        final double arrayIm[]=new double[numRows];
                        arrayRe[0]=diagRe[0]-m.matrixRe[0][0];
                        arrayIm[0]=diagIm[0]-m.matrixIm[0][0];
                        for(int i=1;i<numRows;i++) {
                                arrayRe[i]=diagRe[i]-m.matrixRe[0][i];
                                arrayIm[i]=diagIm[i]-m.matrixIm[0][i];
                        }
                        return new ComplexDiagonalMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLY

        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param z a complex number
        * @return a complex diagonal matrix
        */
        public ComplexMatrix scalarMultiply(final Complex z) {
                final double real=z.real();
                final double imag=z.imag();
                final double arrayRe[]=new double[numRows];
                final double arrayIm[]=new double[numRows];
                arrayRe[0]=real*diagRe[0]-imag*diagIm[0];
                arrayIm[0]=imag*diagRe[0]+real*diagIm[0];
                for(int i=1;i<numRows;i++) {
                        arrayRe[i]=real*diagRe[i]-imag*diagIm[i];
                        arrayIm[i]=imag*diagRe[i]+real*diagIm[i];
                }
                return new ComplexDiagonalMatrix(arrayRe,arrayIm);
        }
        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x a double
        * @return a complex diagonal matrix
        */
        public ComplexMatrix scalarMultiply(final double x) {
                final double arrayRe[]=new double[numRows];
                final double arrayIm[]=new double[numRows];
                arrayRe[0]=x*diagRe[0];
                arrayIm[0]=x*diagIm[0];
                for(int i=1;i<numRows;i++) {
                        arrayRe[i]=x*diagRe[i];
                        arrayIm[i]=x*diagIm[i];
                }
                return new ComplexDiagonalMatrix(arrayRe,arrayIm);
        }

// MATRIX MULTIPLICATION

        /**
        * Returns the multiplication of a vector by this matrix.
        * @param v a complex vector
        * @exception DimensionException If the matrix and vector are incompatible.
        */
        public ComplexVector multiply(final ComplexVector v) {
                if(numCols==v.dimension()) {
                        final double arrayRe[]=new double[numRows];
                        final double arrayIm[]=new double[numRows];
                        Complex comp=v.getComponent(0);
                        arrayRe[0]=(diagRe[0]*comp.real()-diagIm[0]*comp.imag());
                        arrayIm[0]=(diagIm[0]*comp.real()+diagRe[0]*comp.imag());
                        for(int i=1;i<numRows;i++) {
                                comp=v.getComponent(i);
                                arrayRe[i]=(diagRe[i]*comp.real()-diagIm[i]*comp.imag());
                                arrayIm[i]=(diagIm[i]*comp.real()+diagRe[i]*comp.imag());
                        }
                        return new ComplexVector(arrayRe,arrayIm);
                } else
                        throw new DimensionException("Matrix and vector are incompatible.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a complex matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexMatrix multiply(final ComplexMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                if(numCols==m.rows()) {
                                        final double arrayRe[][]=new double[numRows][m.columns()];
                                        final double arrayIm[][]=new double[numRows][m.columns()];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=m.getElement(i,0);
                                                arrayRe[i][0]=(diagRe[i]*elem.real()-diagIm[i]*elem.imag());
                                                arrayIm[i][0]=(diagIm[i]*elem.real()+diagRe[i]*elem.imag());
                                                for(j=1;j<m.columns();j++) {
                                                        elem=m.getElement(i,j);
                                                        arrayRe[i][j]=(diagRe[i]*elem.real()-diagIm[i]*elem.imag());
                                                        arrayIm[i][j]=(diagIm[i]*elem.real()+diagRe[i]*elem.imag());
                                                }
                                        }
                                        return new ComplexMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexMatrix rawMultiply(final ComplexMatrix m) {
                if(numCols==m.numRows) {
                        final double arrayRe[][]=new double[numRows][m.numCols];
                        final double arrayIm[][]=new double[numRows][m.numCols];
                        for(int j,i=0;i<numRows;i++) {
                                arrayRe[i][0]=(diagRe[i]*m.matrixRe[i][0]-diagIm[i]*m.matrixIm[i][0]);
                                arrayIm[i][0]=(diagIm[i]*m.matrixRe[i][0]+diagRe[i]*m.matrixIm[i][0]);
                                for(j=1;j<m.numCols;j++) {
                                        arrayRe[i][j]=(diagRe[i]*m.matrixRe[i][j]-diagIm[i]*m.matrixIm[i][j]);
                                        arrayIm[i][j]=(diagIm[i]*m.matrixRe[i][j]+diagRe[i]*m.matrixIm[i][j]);
                                }
                        }
                        return new ComplexMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a complex square matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexSquareMatrix multiply(final ComplexSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                if(numCols==m.rows()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=m.getElement(i,0);
                                                arrayRe[i][0]=(diagRe[i]*elem.real()-diagIm[i]*elem.imag());
                                                arrayIm[i][0]=(diagIm[i]*elem.real()+diagRe[i]*elem.imag());
                                                for(j=1;j<numCols;j++) {
                                                        elem=m.getElement(i,j);
                                                        arrayRe[i][j]=(diagRe[i]*elem.real()-diagIm[i]*elem.imag());
                                                        arrayIm[i][j]=(diagIm[i]*elem.real()+diagRe[i]*elem.imag());
                                                }
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexSquareMatrix rawMultiply(final ComplexSquareMatrix m) {
                if(numCols==m.numRows) {
                        final double arrayRe[][]=new double[numRows][numCols];
                        final double arrayIm[][]=new double[numRows][numCols];
                        for(int j,i=0;i<numRows;i++) {
                                arrayRe[i][0]=(diagRe[i]*m.matrixRe[i][0]-diagIm[i]*m.matrixIm[i][0]);
                                arrayIm[i][0]=(diagIm[i]*m.matrixRe[i][0]+diagRe[i]*m.matrixIm[i][0]);
                                for(j=1;j<numCols;j++) {
                                        arrayRe[i][j]=(diagRe[i]*m.matrixRe[i][j]-diagIm[i]*m.matrixIm[i][j]);
                                        arrayIm[i][j]=(diagIm[i]*m.matrixRe[i][j]+diagRe[i]*m.matrixIm[i][j]);
                                }
                        }
                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a complex tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexSquareMatrix multiply(final ComplexTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        case DIAGONAL: return rawMultiplyDiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(numCols==m.rows()) {
                                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                                        Complex elem=m.getElement(0,0);
                                        ans.matrixRe[1][0]=(diagRe[0]*elem.real()-diagIm[0]*elem.imag());
                                        ans.matrixIm[1][0]=(diagIm[0]*elem.real()+diagRe[0]*elem.imag());
                                        elem=m.getElement(0,1);
                                        ans.matrixRe[2][0]=(diagRe[0]*elem.real()-diagIm[0]*elem.imag());
                                        ans.matrixIm[2][0]=(diagIm[0]*elem.real()+diagRe[0]*elem.imag());
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                elem=m.getElement(i,i-1);
                                                ans.matrixRe[0][i]=(diagRe[i]*elem.real()-diagIm[i]*elem.imag());
                                                ans.matrixIm[0][i]=(diagIm[i]*elem.real()+diagRe[i]*elem.imag());
                                                elem=m.getElement(i,i);
                                                ans.matrixRe[1][i]=(diagRe[i]*elem.real()-diagIm[i]*elem.imag());
                                                ans.matrixIm[1][i]=(diagIm[i]*elem.real()+diagRe[i]*elem.imag());
                                                elem=m.getElement(i,i+1);
                                                ans.matrixRe[2][i]=(diagRe[i]*elem.real()-diagIm[i]*elem.imag());
                                                ans.matrixIm[2][i]=(diagIm[i]*elem.real()+diagRe[i]*elem.imag());
                                        }
                                        elem=m.getElement(mRow,mRow-1);
                                        ans.matrixRe[0][mRow]=(diagRe[mRow]*elem.real()-diagIm[mRow]*elem.imag());
                                        ans.matrixIm[0][mRow]=(diagIm[mRow]*elem.real()+diagRe[mRow]*elem.imag());
                                        elem=m.getElement(mRow,mRow);
                                        ans.matrixRe[1][mRow]=(diagRe[mRow]*elem.real()-diagIm[mRow]*elem.imag());
                                        ans.matrixIm[1][mRow]=(diagIm[mRow]*elem.real()+diagRe[mRow]*elem.imag());
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                        }
        }
        private ComplexSquareMatrix rawMultiplyTridiagonal(final ComplexMatrix m) {
                int mRow=numRows;
                if(numCols==m.numRows) {
                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                        ans.matrixRe[1][0]=(diagRe[0]*m.matrixRe[1][0]-diagIm[0]*m.matrixIm[1][0]);
                        ans.matrixIm[1][0]=(diagIm[0]*m.matrixRe[1][0]+diagRe[0]*m.matrixIm[1][0]);
                        ans.matrixRe[2][0]=(diagRe[0]*m.matrixRe[2][0]-diagIm[0]*m.matrixIm[2][0]);
                        ans.matrixIm[2][0]=(diagIm[0]*m.matrixRe[2][0]+diagRe[0]*m.matrixIm[2][0]);
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.matrixRe[0][i]=(diagRe[i]*m.matrixRe[0][i]-diagIm[i]*m.matrixIm[0][i]);
                                ans.matrixIm[0][i]=(diagIm[i]*m.matrixRe[0][i]+diagRe[i]*m.matrixIm[0][i]);
                                ans.matrixRe[1][i]=(diagRe[i]*m.matrixRe[1][i]-diagIm[i]*m.matrixIm[1][i]);
                                ans.matrixIm[1][i]=(diagIm[i]*m.matrixRe[1][i]+diagRe[i]*m.matrixIm[1][i]);
                                ans.matrixRe[2][i]=(diagRe[i]*m.matrixRe[2][i]-diagIm[i]*m.matrixIm[2][i]);
                                ans.matrixIm[2][i]=(diagIm[i]*m.matrixRe[2][i]+diagRe[i]*m.matrixIm[2][i]);
                        }
                        ans.matrixRe[0][mRow]=(diagRe[mRow]*m.matrixRe[0][mRow]-diagIm[mRow]*m.matrixIm[0][mRow]);
                        ans.matrixIm[0][mRow]=(diagIm[mRow]*m.matrixRe[0][mRow]+diagRe[mRow]*m.matrixIm[0][mRow]);
                        ans.matrixRe[1][mRow]=(diagRe[mRow]*m.matrixRe[1][mRow]-diagIm[mRow]*m.matrixIm[1][mRow]);
                        ans.matrixIm[1][mRow]=(diagIm[mRow]*m.matrixRe[1][mRow]+diagRe[mRow]*m.matrixIm[1][mRow]);
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a complex diagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexDiagonalMatrix multiply(final ComplexDiagonalMatrix m) {
                return rawMultiplyDiagonal(m);
        }
        private ComplexDiagonalMatrix rawMultiplyDiagonal(final ComplexMatrix m) {
                if(numCols==m.numRows) {
                        final double arrayRe[]=new double[numRows];
                        final double arrayIm[]=new double[numRows];
                        arrayRe[0]=(diagRe[0]*m.matrixRe[0][0]-diagIm[0]*m.matrixIm[0][0]);
                        arrayIm[0]=(diagIm[0]*m.matrixRe[0][0]+diagRe[0]*m.matrixIm[0][0]);
                        for(int i=1;i<numRows;i++) {
                                arrayRe[i]=(diagRe[i]*m.matrixRe[0][i]-diagIm[i]*m.matrixIm[0][i]);
                                arrayIm[i]=(diagIm[i]*m.matrixRe[0][i]+diagRe[i]*m.matrixIm[0][i]);
                        }
                        return new ComplexDiagonalMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// INVERSE

        /**
        * Returns the inverse of this matrix.
        * @return a complex diagonal matrix
        */
        public ComplexSquareMatrix inverse() {
                final double arrayRe[]=new double[numRows];
                final double arrayIm[]=new double[numRows];
                double denom=diagRe[0]*diagRe[0]+diagIm[0]*diagIm[0];
                arrayRe[0]=diagRe[0]/denom;
                arrayIm[0]=-diagIm[0]/denom;
                for(int i=1;i<numRows;i++) {
                        denom=diagRe[i]*diagRe[i]+diagIm[i]*diagIm[i];
                        arrayRe[i]=diagRe[i]/denom;
                        arrayIm[i]=-diagIm[i]/denom;
                }
                return new ComplexDiagonalMatrix(arrayRe,arrayIm);
        }

// HERMITIAN ADJOINT

        /**
        * Returns the hermitian adjoint of this matrix.
        * @return a complex diagonal matrix
        */
        public ComplexMatrix hermitianAdjoint() {
                return conjugate();
        }

// CONJUGATE

        /**
        * Returns the complex conjugate of this matrix.
        * @return a complex diagonal matrix
        */
        public ComplexMatrix conjugate() {
                final double arrayIm[]=new double[numRows];
                arrayIm[0]=-diagIm[0];
                for(int i=1;i<numRows;i++)
                        arrayIm[i]=-diagIm[i];
                return new ComplexDiagonalMatrix(diagRe,arrayIm);
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return a complex diagonal matrix
        */
        public Matrix transpose() {
                return this;
        }

// LU DECOMPOSITION

        /**
        * Returns the LU decomposition of this matrix.
        * @return an array with [0] containing the L-matrix and [1] containing the U-matrix.
        */
        public ComplexSquareMatrix[] luDecompose(int pivot[]) {
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
                LU=new ComplexDiagonalMatrix[2];
                LU[0]=identity(numRows);
                LU[1]=this;
                LUpivot=new int[pivot.length];
                System.arraycopy(pivot,0,LUpivot,0,pivot.length);
                return LU;
        }

// MAP ELEMENTS

        /**
        * Applies a function on all the matrix elements.
        * @param f a user-defined function
        * @return a complex diagonal matrix
        */
        public ComplexMatrix mapElements(final ComplexMapping f) {
                final Complex array[]=new Complex[numRows];
                array[0]=f.map(diagRe[0],diagIm[0]);
                for(int i=1;i<numRows;i++)
                        array[i]=f.map(diagRe[i],diagIm[i]);
                return new ComplexDiagonalMatrix(array);
        }
}

