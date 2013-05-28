

package JSci.maths;

/**
* The ComplexTridiagonalMatrix class provides an object for encapsulating tridiagonal matrices containing complex numbers.
* Uses compressed diagonal storage.
* @version 2.2
* @author Mark Hale
*/
public class ComplexTridiagonalMatrix extends ComplexSquareMatrix {
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
        protected double ldiagRe[],ldiagIm[];
        protected double diagRe[],diagIm[];
        protected double udiagRe[],udiagIm[];
        /**
        * Constructs a matrix.
        */
        protected ComplexTridiagonalMatrix(final int size,final int storeID) {
                super(size,storeID);
        }
        /**
        * Constructs an empty matrix.
        * @param size the number of rows/columns
        */
        public ComplexTridiagonalMatrix(final int size) {
                this(size,TRIDIAGONAL);
                matrixRe=new double[3][size];
                matrixIm=new double[3][size];
                ldiagRe=matrixRe[0];
                ldiagIm=matrixIm[0];
                diagRe=matrixRe[1];
                diagIm=matrixIm[1];
                udiagRe=matrixRe[2];
                udiagIm=matrixIm[2];
        }
        /**
        * Constructs a matrix from an array.
        * Any non-tridiagonal elements in the array are ignored.
        * @param array an assigned value
        * @exception MatrixDimensionException If the array is not square.
        */
        public ComplexTridiagonalMatrix(final Complex array[][]) {
                this(array.length);
                if(array.length==array[0].length) {
                        diagRe[0]=array[0][0].real();
                        diagIm[0]=array[0][0].imag();
                        udiagRe[0]=array[0][1].real();
                        udiagIm[0]=array[0][1].imag();
                        int i=1;
                        for(;i<array.length-1;i++) {
                                ldiagRe[i]=array[i][i-1].real();
                                ldiagIm[i]=array[i][i-1].imag();
                                diagRe[i]=array[i][i].real();
                                diagIm[i]=array[i][i].imag();
                                udiagRe[i]=array[i][i+1].real();
                                udiagIm[i]=array[i][i+1].imag();
                        }
                        ldiagRe[i]=array[i][i-1].real();
                        ldiagIm[i]=array[i][i-1].imag();
                        diagRe[i]=array[i][i].real();
                        diagIm[i]=array[i][i].imag();
                } else {
                        matrixRe=null;
                        matrixIm=null;
                        throw new MatrixDimensionException("The array is not square.");
                }
        }
        /**
        * Compares two complex tridiagonal matrices for equality.
        * @param m a complex tridiagonal matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof ComplexTridiagonalMatrix) &&
                numRows==((ComplexTridiagonalMatrix)m).rows()) {
                        final ComplexTridiagonalMatrix ctm=(ComplexTridiagonalMatrix)m;
                        if(!ctm.getElement(0,0).equals(diagRe[0],diagIm[0]))
                                return false;
                        if(!ctm.getElement(0,1).equals(udiagRe[0],udiagIm[0]))
                                return false;
                        int i=1;
                        for(;i<numRows-1;i++) {
                                if(!ctm.getElement(i,i-1).equals(ldiagRe[i],ldiagIm[i]))
                                        return false;
                                if(!ctm.getElement(i,i).equals(diagRe[i],diagIm[i]))
                                        return false;
                                if(!ctm.getElement(i,i+1).equals(udiagRe[i],udiagIm[i]))
                                        return false;
                        }
                        if(!ctm.getElement(i,i-1).equals(ldiagRe[i],ldiagIm[i]))
                                return false;
                        if(!ctm.getElement(i,i).equals(diagRe[i],diagIm[i]))
                                return false;
                        return true;
                } else
                        return false;
        }
        /**
        * Returns a string representing this matrix.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(5*rows()*columns());
                for(int j,i=0;i<rows();i++) {
                        for(j=0;j<columns();j++) {
                                buf.append(getElement(i,j).toString());
                                buf.append(' ');
                        }
                        buf.append('\n');
                }
                return buf.toString();
        }
        /**
        * Returns the real part of this complex matrix.
        * @return a double tridiagonal matrix
        */
        public DoubleMatrix real() {
                final DoubleTridiagonalMatrix m=new DoubleTridiagonalMatrix(numRows);
                m.matrix[1][0]=diagRe[0];
                m.matrix[2][0]=udiagRe[0];
                int i=1;
                for(;i<numRows-1;i++) {
                        m.matrix[0][i]=ldiagRe[i];
                        m.matrix[1][i]=diagRe[i];
                        m.matrix[2][i]=udiagRe[i];
                }
                m.matrix[0][i]=ldiagRe[i];
                m.matrix[1][i]=diagRe[i];
                return m;
        }
        /**
        * Returns the imaginary part of this complex matrix.
        * @return a double tridiagonal matrix
        */
        public DoubleMatrix imag() {
                final DoubleTridiagonalMatrix m=new DoubleTridiagonalMatrix(numRows);
                m.matrix[1][0]=diagIm[0];
                m.matrix[2][0]=udiagIm[0];
                int i=1;
                for(;i<numRows-1;i++) {
                        m.matrix[0][i]=ldiagIm[i];
                        m.matrix[1][i]=diagIm[i];
                        m.matrix[2][i]=udiagIm[i];
                }
                m.matrix[0][i]=ldiagIm[i];
                m.matrix[1][i]=diagIm[i];
                return m;
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public Complex getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(j>=i-1 && j<=i+1)
                                return new Complex(matrixRe[j-i+1][i],matrixIm[j-i+1][i]);
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
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(j>=i-1 && j<=i+1) {
                                matrixRe[j-i+1][i]=z.real();
                                matrixIm[j-i+1][i]=z.imag();
                        } else
                                throw new MatrixDimensionException(getInvalidElementMsg(i,j));
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
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(j>=i-1 && j<=i+1) {
                                matrixRe[j-i+1][i]=x;
                                matrixIm[j-i+1][i]=y;
                        } else
                                throw new MatrixDimensionException(getInvalidElementMsg(i,j));
                } else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
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
                double result=Math.sqrt(diagRe[0]*diagRe[0]+diagIm[0]*diagIm[0])+Math.sqrt(udiagRe[0]*udiagRe[0]+udiagIm[0]*udiagIm[0]);
                double tmpResult;
                int i=1;
                for(;i<numRows-1;i++) {
                        tmpResult=Math.sqrt(ldiagRe[i]*ldiagRe[i]+ldiagIm[i]*ldiagIm[i])+Math.sqrt(diagRe[i]*diagRe[i]+diagIm[i]*diagIm[i])+Math.sqrt(udiagRe[i]*udiagRe[i]+udiagIm[i]*udiagIm[i]);
                        if(tmpResult>result)
                                result=tmpResult;
                }
                tmpResult=Math.sqrt(ldiagRe[i]*ldiagRe[i]+ldiagIm[i]*ldiagIm[i])+Math.sqrt(diagRe[i]*diagRe[i]+diagIm[i]*diagIm[i]);
                if(tmpResult>result)
                        result=tmpResult;
                return result;
        }
        /**
        * Returns the Frobenius (l<sup>2</sup>) norm.
        * @author Taber Smith
        */
        public double frobeniusNorm() {
                double result=diagRe[0]*diagRe[0]+diagIm[0]*diagIm[0]+
                        udiagRe[0]*udiagRe[0]+udiagIm[0]*udiagIm[0];
                int i=1;
                for(;i<numRows-1;i++) {
                        result+=ldiagRe[i]*ldiagRe[i]+ldiagIm[i]*ldiagIm[i]+
                                diagRe[i]*diagRe[i]+diagIm[i]*diagIm[i]+
                                udiagRe[i]*udiagRe[i]+udiagIm[i]*udiagIm[i];
                }
                result+=ldiagRe[i]*ldiagRe[i]+ldiagIm[i]*ldiagIm[i]+
                        diagRe[i]*diagRe[i]+diagIm[i]*diagIm[i];
                return Math.sqrt(result);
        }
        /**
        * Returns the operator norm.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public double operatorNorm() throws MaximumIterationsExceededException {
                return Math.sqrt(ArrayMath.max(LinearMath.eigenvalueSolveHermitian((ComplexTridiagonalMatrix)(this.hermitianAdjoint().multiply(this)))));
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
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=getElement(i,0).add(m.getElement(i,0));
                                                arrayRe[i][0]=elem.real();
                                                arrayIm[i][0]=elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=getElement(i,j).add(m.getElement(i,j));
                                                        arrayRe[i][j]=elem.real();
                                                        arrayIm[i][j]=elem.imag();
                                                }
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexSquareMatrix rawAdd(final ComplexMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        final double arrayRe[][]=new double[numRows][numRows];
                        final double arrayIm[][]=new double[numRows][numRows];
                        for(int i=0;i<numRows;i++) {
                                System.arraycopy(m.matrixRe[i],0,arrayRe[i],0,numRows);
                                System.arraycopy(m.matrixIm[i],0,arrayIm[i],0,numRows);
                        }
                        arrayRe[0][0]+=diagRe[0];
                        arrayIm[0][0]+=diagIm[0];
                        arrayRe[0][1]+=udiagRe[0];
                        arrayIm[0][1]+=udiagIm[0];
                        int n=numCols-1;
                        for(int i=1;i<n;i++) {
                                arrayRe[i][i-1]+=ldiagRe[i];
                                arrayIm[i][i-1]+=ldiagIm[i];
                                arrayRe[i][i]+=diagRe[i];
                                arrayIm[i][i]+=diagIm[i];
                                arrayRe[i][i+1]+=udiagRe[i];
                                arrayIm[i][i+1]+=udiagIm[i];
                        }
                        arrayRe[n][n-1]+=ldiagRe[n];
                        arrayIm[n][n-1]+=ldiagIm[n];
                        arrayRe[n][n]+=diagRe[n];
                        arrayIm[n][n]+=diagIm[n];
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
                        default: 
                                if(numRows==m.rows()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=getElement(i,0).add(m.getElement(i,0));
                                                arrayRe[i][0]=elem.real();
                                                arrayIm[i][0]=elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=getElement(i,j).add(m.getElement(i,j));
                                                        arrayRe[i][j]=elem.real();
                                                        arrayIm[i][j]=elem.imag();
                                                }
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
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                                        Complex elem=m.getElement(0,0);
                                        ans.diagRe[0]=diagRe[0]+elem.real();
                                        ans.diagIm[0]=diagIm[0]+elem.imag();
                                        elem=m.getElement(0,1);
                                        ans.udiagRe[0]=udiagRe[0]+elem.real();
                                        ans.udiagIm[0]=udiagIm[0]+elem.imag();
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                elem=m.getElement(i,i-1);
                                                ans.ldiagRe[i]=ldiagRe[i]+elem.real();
                                                ans.ldiagIm[i]=ldiagIm[i]+elem.imag();
                                                elem=m.getElement(i,i);
                                                ans.diagRe[i]=diagRe[i]+elem.real();
                                                ans.diagIm[i]=diagIm[i]+elem.imag();
                                                elem=m.getElement(i,i+1);
                                                ans.udiagRe[i]=udiagRe[i]+elem.real();
                                                ans.udiagIm[i]=udiagIm[i]+elem.imag();
                                        }
                                        elem=m.getElement(mRow,mRow-1);
                                        ans.ldiagRe[mRow]=ldiagRe[mRow]+elem.real();
                                        ans.ldiagIm[mRow]=ldiagIm[mRow]+elem.imag();
                                        elem=m.getElement(mRow,mRow);
                                        ans.diagRe[mRow]=diagRe[mRow]+elem.real();
                                        ans.diagIm[mRow]=diagIm[mRow]+elem.imag();
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexTridiagonalMatrix rawAddTridiagonal(final ComplexMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                        ans.diagRe[0]=diagRe[0]+m.matrixRe[1][0];
                        ans.diagIm[0]=diagIm[0]+m.matrixIm[1][0];
                        ans.udiagRe[0]=udiagRe[0]+m.matrixRe[2][0];
                        ans.udiagIm[0]=udiagIm[0]+m.matrixIm[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.ldiagRe[i]=ldiagRe[i]+m.matrixRe[0][i];
                                ans.ldiagIm[i]=ldiagIm[i]+m.matrixIm[0][i];
                                ans.diagRe[i]=diagRe[i]+m.matrixRe[1][i];
                                ans.diagIm[i]=diagIm[i]+m.matrixIm[1][i];
                                ans.udiagRe[i]=udiagRe[i]+m.matrixRe[2][i];
                                ans.udiagIm[i]=udiagIm[i]+m.matrixIm[2][i];
                        }
                        ans.ldiagRe[mRow]=ldiagRe[mRow]+m.matrixRe[0][mRow];
                        ans.ldiagIm[mRow]=ldiagIm[mRow]+m.matrixIm[0][mRow];
                        ans.diagRe[mRow]=diagRe[mRow]+m.matrixRe[1][mRow];
                        ans.diagIm[mRow]=diagIm[mRow]+m.matrixIm[1][mRow];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SUBTRACTION

        /**
        * Returns the subtraction of this matrix and another.
        * @param m a complex matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexMatrix subtract(final ComplexMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawSubtract(m);
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        default: 
                                if(numRows==m.rows() && numRows==m.columns()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=getElement(i,0).subtract(m.getElement(i,0));
                                                arrayRe[i][0]=elem.real();
                                                arrayIm[i][0]=elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=getElement(i,j).subtract(m.getElement(i,j));
                                                        arrayRe[i][j]=elem.real();
                                                        arrayIm[i][j]=elem.imag();
                                                }
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
                        Complex elem;
                        for(int j,i=0;i<numRows;i++) {
                                elem=getElement(i,0);
                                arrayRe[i][0]=elem.real()-m.matrixRe[i][0];
                                arrayIm[i][0]=elem.imag()-m.matrixIm[i][0];
                                for(j=1;j<numCols;j++) {
                                        elem=getElement(i,j);
                                        arrayRe[i][j]=elem.real()-m.matrixRe[i][j];
                                        arrayIm[i][j]=elem.imag()-m.matrixIm[i][j];
                                }
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
                        default: 
                                if(numRows==m.rows()) {
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j,i=0;i<numRows;i++) {
                                                elem=getElement(i,0).subtract(m.getElement(i,0));
                                                arrayRe[i][0]=elem.real();
                                                arrayIm[i][0]=elem.imag();
                                                for(j=1;j<numCols;j++) {
                                                        elem=getElement(i,j).subtract(m.getElement(i,j));
                                                        arrayRe[i][j]=elem.real();
                                                        arrayIm[i][j]=elem.imag();
                                                }
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the subtraction of this matrix and another.
        * @param m a complex tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexTridiagonalMatrix subtract(final ComplexTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawSubtractTridiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(mRow==m.rows()) {
                                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                                        Complex elem=m.getElement(0,0);
                                        ans.diagRe[0]=diagRe[0]-elem.real();
                                        ans.diagIm[0]=diagIm[0]-elem.imag();
                                        elem=m.getElement(0,1);
                                        ans.udiagRe[0]=udiagRe[0]-elem.real();
                                        ans.udiagIm[0]=udiagIm[0]-elem.imag();
                                        mRow--;
                                        for(int i=1;i<mRow;i++) {
                                                elem=m.getElement(i,i-1);
                                                ans.ldiagRe[i]=ldiagRe[i]-elem.real();
                                                ans.ldiagIm[i]=ldiagIm[i]-elem.imag();
                                                elem=m.getElement(i,i);
                                                ans.diagRe[i]=diagRe[i]-elem.real();
                                                ans.diagIm[i]=diagIm[i]-elem.imag();
                                                elem=m.getElement(i,i+1);
                                                ans.udiagRe[i]=udiagRe[i]-elem.real();
                                                ans.udiagIm[i]=udiagIm[i]-elem.imag();
                                        }
                                        elem=m.getElement(mRow,mRow-1);
                                        ans.ldiagRe[mRow]=ldiagRe[mRow]-elem.real();
                                        ans.ldiagIm[mRow]=ldiagIm[mRow]-elem.imag();
                                        elem=m.getElement(mRow,mRow);
                                        ans.diagRe[mRow]=diagRe[mRow]-elem.real();
                                        ans.diagIm[mRow]=diagIm[mRow]-elem.imag();
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private ComplexTridiagonalMatrix rawSubtractTridiagonal(final ComplexMatrix m) {
                int mRow=numRows;
                if(mRow==m.numRows) {
                        final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                        ans.diagRe[0]=diagRe[0]-m.matrixRe[1][0];
                        ans.diagIm[0]=diagIm[0]-m.matrixIm[1][0];
                        ans.udiagRe[0]=udiagRe[0]-m.matrixRe[2][0];
                        ans.udiagIm[0]=udiagIm[0]-m.matrixIm[2][0];
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                ans.ldiagRe[i]=ldiagRe[i]-m.matrixRe[0][i];
                                ans.ldiagIm[i]=ldiagIm[i]-m.matrixIm[0][i];
                                ans.diagRe[i]=diagRe[i]-m.matrixRe[1][i];
                                ans.diagIm[i]=diagIm[i]-m.matrixIm[1][i];
                                ans.udiagRe[i]=udiagRe[i]-m.matrixRe[2][i];
                                ans.udiagIm[i]=udiagIm[i]-m.matrixIm[2][i];
                        }
                        ans.ldiagRe[mRow]=ldiagRe[mRow]-m.matrixRe[0][mRow];
                        ans.ldiagIm[mRow]=ldiagIm[mRow]-m.matrixIm[0][mRow];
                        ans.diagRe[mRow]=diagRe[mRow]-m.matrixRe[1][mRow];
                        ans.diagIm[mRow]=diagIm[mRow]-m.matrixIm[1][mRow];
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param z a complex number
        * @return a complex tridiagonal matrix
        */
        public ComplexMatrix scalarMultiply(final Complex z) {
                final double real=z.real();
                final double imag=z.imag();
                int mRow=numRows;
                final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                ans.diagRe[0]=real*diagRe[0]-imag*diagIm[0];
                ans.diagIm[0]=imag*diagRe[0]+real*diagIm[0];
                ans.udiagRe[0]=real*udiagRe[0]-imag*udiagIm[0];
                ans.udiagIm[0]=imag*udiagRe[0]+real*udiagIm[0];
                mRow--;
                for(int i=1;i<mRow;i++) {
                        ans.ldiagRe[i]=real*ldiagRe[i]-imag*ldiagIm[i];
                        ans.ldiagIm[i]=imag*ldiagRe[i]+real*ldiagIm[i];
                        ans.diagRe[i]=real*diagRe[i]-imag*diagIm[i];
                        ans.diagIm[i]=imag*diagRe[i]+real*diagIm[i];
                        ans.udiagRe[i]=real*udiagRe[i]-imag*udiagIm[i];
                        ans.udiagIm[i]=imag*udiagRe[i]+real*udiagIm[i];
                }
                ans.ldiagRe[mRow]=real*ldiagRe[mRow]-imag*ldiagIm[mRow];
                ans.ldiagIm[mRow]=imag*ldiagRe[mRow]+real*ldiagIm[mRow];
                ans.diagRe[mRow]=real*diagRe[mRow]-imag*diagIm[mRow];
                ans.diagIm[mRow]=imag*diagRe[mRow]+real*diagIm[mRow];
                return ans;
        }
        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x a double
        * @return a complex tridiagonal matrix
        */
        public ComplexMatrix scalarMultiply(final double x) {
                int mRow=numRows;
                final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                ans.diagRe[0]=x*diagRe[0];
                ans.diagIm[0]=x*diagIm[0];
                ans.udiagRe[0]=x*udiagRe[0];
                ans.udiagIm[0]=x*udiagIm[0];
                mRow--;
                for(int i=1;i<mRow;i++) {
                        ans.ldiagRe[i]=x*ldiagRe[i];
                        ans.ldiagIm[i]=x*ldiagIm[i];
                        ans.diagRe[i]=x*diagRe[i];
                        ans.diagIm[i]=x*diagIm[i];
                        ans.udiagRe[i]=x*udiagRe[i];
                        ans.udiagIm[i]=x*udiagIm[i];
                }
                ans.ldiagRe[mRow]=x*ldiagRe[mRow];
                ans.ldiagIm[mRow]=x*ldiagIm[mRow];
                ans.diagRe[mRow]=x*diagRe[mRow];
                ans.diagIm[mRow]=x*diagIm[mRow];
                return ans;
        }

// MATRIX MULTIPLICATION

        /**
        * Returns the multiplication of a vector by this matrix.
        * @param v a complex vector
        * @exception DimensionException If the matrix and vector are incompatible.
        */
        public ComplexVector multiply(final ComplexVector v) {
                int mRow=numRows;
                if(mRow==v.dimension()) {
                        final double arrayRe[]=new double[mRow];
                        final double arrayIm[]=new double[mRow];
                        Complex comp;
                        comp=v.getComponent(0);
                        arrayRe[0]=(diagRe[0]*comp.real()-diagIm[0]*comp.imag());
                        arrayIm[0]=(diagIm[0]*comp.real()+diagRe[0]*comp.imag());
                        comp=v.getComponent(1);
                        arrayRe[0]+=(udiagRe[0]*comp.real()-udiagIm[0]*comp.imag());
                        arrayIm[0]+=(udiagIm[0]*comp.real()+udiagRe[0]*comp.imag());
                        mRow--;
                        for(int i=1;i<mRow;i++) {
                                comp=v.getComponent(i-1);
                                arrayRe[i]=(ldiagRe[i]*comp.real()-ldiagIm[i]*comp.imag());
                                arrayIm[i]=(ldiagIm[i]*comp.real()+ldiagRe[i]*comp.imag());
                                comp=v.getComponent(i);
                                arrayRe[i]+=(diagRe[i]*comp.real()-diagIm[i]*comp.imag());
                                arrayIm[i]+=(diagIm[i]*comp.real()+diagRe[i]*comp.imag());
                                comp=v.getComponent(i+1);
                                arrayRe[i]+=(udiagRe[i]*comp.real()-udiagIm[i]*comp.imag());
                                arrayIm[i]+=(udiagIm[i]*comp.real()+udiagRe[i]*comp.imag());
                        }
                        comp=v.getComponent(mRow-1);
                        arrayRe[mRow]=(ldiagRe[mRow]*comp.real()-ldiagIm[mRow]*comp.imag());
                        arrayIm[mRow]=(ldiagIm[mRow]*comp.real()+ldiagRe[mRow]*comp.imag());
                        comp=v.getComponent(mRow);
                        arrayRe[mRow]+=(diagRe[mRow]*comp.real()-diagIm[mRow]*comp.imag());
                        arrayIm[mRow]+=(diagIm[mRow]*comp.real()+diagRe[mRow]*comp.imag());
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
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final double arrayRe[][]=new double[numRows][m.columns()];
                                        final double arrayIm[][]=new double[numRows][m.columns()];
                                        Complex elem;
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<m.columns();k++) {
                                                        elem=getElement(j,0).multiply(m.getElement(0,k));
                                                        arrayRe[j][k]=elem.real();
                                                        arrayIm[j][k]=elem.imag();
                                                        for(n=1;n<numCols;n++) {
                                                                elem=getElement(j,n).multiply(m.getElement(n,k));
                                                                arrayRe[j][k]+=elem.real();
                                                                arrayIm[j][k]+=elem.imag();
                                                        }
                                                }
                                        }
                                        return new ComplexMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private ComplexMatrix rawMultiply(final ComplexMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        final double arrayRe[][]=new double[numRows][m.numCols];
                        final double arrayIm[][]=new double[numRows][m.numCols];
                        double real,imag;
                        Complex elem;
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<m.numCols;k++) {
                                        elem=getElement(j,0);
                                        real=(m.matrixRe[0][k]*elem.real()-m.matrixIm[0][k]*elem.imag());
                                        imag=(m.matrixIm[0][k]*elem.real()+m.matrixRe[0][k]*elem.imag());
                                        for(n=1;n<numCols;n++) {
                                                elem=getElement(j,n);
                                                real+=(m.matrixRe[n][k]*elem.real()-m.matrixIm[n][k]*elem.imag());
                                                imag+=(m.matrixIm[n][k]*elem.real()+m.matrixRe[n][k]*elem.imag());
                                        }
                                        arrayRe[j][k]=real;
                                        arrayIm[j][k]=imag;
                                }
                        }
                        return new ComplexMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
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
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final double arrayRe[][]=new double[numRows][numCols];
                                        final double arrayIm[][]=new double[numRows][numCols];
                                        Complex elem;
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<numCols;k++) {
                                                        elem=getElement(j,0).multiply(m.getElement(0,k));
                                                        arrayRe[j][k]=elem.real();
                                                        arrayIm[j][k]=elem.imag();
                                                        for(n=1;n<numCols;n++) {
                                                                elem=getElement(j,n).multiply(m.getElement(n,k));
                                                                arrayRe[j][k]+=elem.real();
                                                                arrayIm[j][k]+=elem.imag();
                                                        }
                                                }
                                        }
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private ComplexSquareMatrix rawMultiply(final ComplexSquareMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        final double arrayRe[][]=new double[numRows][numCols];
                        final double arrayIm[][]=new double[numRows][numCols];
                        double real,imag;
                        Complex elem;
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<numCols;k++) {
                                        elem=getElement(j,0);
                                        real=(m.matrixRe[0][k]*elem.real()-m.matrixIm[0][k]*elem.imag());
                                        imag=(m.matrixIm[0][k]*elem.real()+m.matrixRe[0][k]*elem.imag());
                                        for(n=1;n<numCols;n++) {
                                                elem=getElement(j,n);
                                                real+=(m.matrixRe[n][k]*elem.real()-m.matrixIm[n][k]*elem.imag());
                                                imag+=(m.matrixIm[n][k]*elem.real()+m.matrixRe[n][k]*elem.imag());
                                        }
                                        arrayRe[j][k]=real;
                                        arrayIm[j][k]=imag;
                                }
                        }
                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a complex tridiagonal matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public ComplexSquareMatrix multiply(final ComplexTridiagonalMatrix m) {
                switch(m.storageFormat) {
                        case TRIDIAGONAL: return rawMultiplyTridiagonal(m);
                        default: 
                                int mRow=numRows;
                                if(numCols==m.rows()) {
                                        final double arrayRe[][]=new double[mRow][mRow];
                                        final double arrayIm[][]=new double[mRow][mRow];
                                        Complex elem1,elem2,elem3;
                                        elem1=m.getElement(0,0);elem2=m.getElement(1,0);
                                        arrayRe[0][0]=(diagRe[0]*elem1.real()-diagIm[0]*elem1.imag())+(udiagRe[0]*elem2.real()-udiagIm[0]*elem2.imag());
                                        arrayIm[0][0]=(diagIm[0]*elem1.real()+diagRe[0]*elem1.imag())+(udiagIm[0]*elem2.real()+udiagRe[0]*elem2.imag());
                                        elem1=m.getElement(0,1);elem2=m.getElement(1,1);
                                        arrayRe[0][1]=(diagRe[0]*elem1.real()-diagIm[0]*elem1.imag())+(udiagRe[0]*elem2.real()-udiagIm[0]*elem2.imag());
                                        arrayIm[0][1]=(diagIm[0]*elem1.real()+diagRe[0]*elem1.imag())+(udiagIm[0]*elem2.real()+udiagRe[0]*elem2.imag());
                                        elem1=m.getElement(1,2);
                                        arrayRe[0][2]=(udiagRe[0]*elem1.real()-udiagIm[0]*elem1.imag());
                                        arrayIm[0][2]=(udiagIm[0]*elem1.real()+udiagRe[0]*elem1.imag());
                                        if(mRow>3) {
                                                elem1=m.getElement(0,0);elem2=m.getElement(1,0);
                                                arrayRe[1][0]=(ldiagRe[1]*elem1.real()-ldiagIm[1]*elem1.imag())+(diagRe[1]*elem2.real()-diagIm[1]*elem2.imag());
                                                arrayIm[1][0]=(ldiagIm[1]*elem1.real()+ldiagRe[1]*elem1.imag())+(diagIm[1]*elem2.real()+diagRe[1]*elem2.imag());
                                                elem1=m.getElement(0,1);elem2=m.getElement(1,1);elem3=m.getElement(2,1);
                                                arrayRe[1][1]=(ldiagRe[1]*elem1.real()-ldiagIm[1]*elem1.imag())+(diagRe[1]*elem2.real()-diagIm[1]*elem2.imag())+(udiagRe[1]*elem3.real()-udiagIm[1]*elem3.imag());
                                                arrayIm[1][1]=(ldiagIm[1]*elem1.real()+ldiagRe[1]*elem1.imag())+(diagIm[1]*elem2.real()+diagRe[1]*elem2.imag())+(udiagIm[1]*elem3.real()+udiagRe[1]*elem3.imag());
                                                elem1=m.getElement(1,2);elem2=m.getElement(2,2);
                                                arrayRe[1][2]=(diagRe[1]*elem1.real()-diagIm[1]*elem1.imag())+(udiagRe[1]*elem2.real()-udiagIm[1]*elem2.imag());
                                                arrayIm[1][2]=(diagIm[1]*elem1.real()+diagRe[1]*elem1.imag())+(udiagIm[1]*elem2.real()+udiagRe[1]*elem2.imag());
                                                elem1=m.getElement(2,3);
                                                arrayRe[1][3]=(udiagRe[1]*elem1.real()-udiagIm[1]*elem1.imag());
                                                arrayIm[1][3]=(udiagIm[1]*elem1.real()+udiagRe[1]*elem1.imag());
                                        }
                                        if(mRow==3) {
                                                elem1=m.getElement(0,0);elem2=m.getElement(1,0);
                                                arrayRe[1][0]=(ldiagRe[1]*elem1.real()-ldiagIm[1]*elem1.imag())+(diagRe[1]*elem2.real()-diagIm[1]*elem2.imag());
                                                arrayIm[1][0]=(ldiagIm[1]*elem1.real()+ldiagRe[1]*elem1.imag())+(diagIm[1]*elem2.real()+diagRe[1]*elem2.imag());
                                                elem1=m.getElement(0,1);elem2=m.getElement(1,1);elem3=m.getElement(2,1);
                                                arrayRe[1][1]=(ldiagRe[1]*elem1.real()-ldiagIm[1]*elem1.imag())+(diagRe[1]*elem2.real()-diagIm[1]*elem2.imag())+(udiagRe[1]*elem3.real()-udiagIm[1]*elem3.imag());
                                                arrayIm[1][1]=(ldiagIm[1]*elem1.real()+ldiagRe[1]*elem1.imag())+(diagIm[1]*elem2.real()+diagRe[1]*elem2.imag())+(udiagIm[1]*elem3.real()+udiagRe[1]*elem3.imag());
                                                elem1=m.getElement(1,2);elem2=m.getElement(2,2);
                                                arrayRe[1][2]=(diagRe[1]*elem1.real()-diagIm[1]*elem1.imag())+(udiagRe[1]*elem2.real()-udiagIm[1]*elem2.imag());
                                                arrayIm[1][2]=(diagIm[1]*elem1.real()+diagRe[1]*elem1.imag())+(udiagIm[1]*elem2.real()+udiagRe[1]*elem2.imag());
                                        } else if(mRow>4) {
                                                for(int i=2;i<mRow-2;i++) {
                                                        elem1=m.getElement(i-1,i-2);
                                                        arrayRe[i][i-2]=(ldiagRe[i]*elem1.real()-ldiagIm[i]*elem1.imag());
                                                        arrayIm[i][i-2]=(ldiagIm[i]*elem1.real()+ldiagRe[i]*elem1.imag());
                                                        elem1=m.getElement(i-1,i-1);elem2=m.getElement(i,i-1);
                                                        arrayRe[i][i-1]=(ldiagRe[i]*elem1.real()-ldiagIm[i]*elem1.imag())+(diagRe[i]*elem2.real()-diagIm[i]*elem2.imag());
                                                        arrayIm[i][i-1]=(ldiagIm[i]*elem1.real()+ldiagRe[i]*elem1.imag())+(diagIm[i]*elem2.real()+diagRe[i]*elem2.imag());
                                                        elem1=m.getElement(i-1,i);elem2=m.getElement(i,i);elem3=m.getElement(i+1,i);
                                                        arrayRe[i][i]=(ldiagRe[i]*elem1.real()-ldiagIm[i]*elem1.imag())+(diagRe[i]*elem2.real()-diagIm[i]*elem2.imag())+(udiagRe[i]*elem3.real()-udiagIm[i]*elem3.imag());
                                                        arrayIm[i][i]=(ldiagIm[i]*elem1.real()+ldiagRe[i]*elem1.imag())+(diagIm[i]*elem2.real()+diagRe[i]*elem2.imag())+(udiagIm[i]*elem3.real()+udiagRe[i]*elem3.imag());
                                                        elem1=m.getElement(i,i+1);elem2=m.getElement(i+1,i+1);
                                                        arrayRe[i][i+1]=(diagRe[i]*elem1.real()-diagIm[i]*elem1.imag())+(udiagRe[i]*elem2.real()-udiagIm[i]*elem2.imag());
                                                        arrayIm[i][i+1]=(diagIm[i]*elem1.real()+diagRe[i]*elem1.imag())+(udiagIm[i]*elem2.real()+udiagRe[i]*elem2.imag());
                                                        elem1=m.getElement(i+1,i+2);
                                                        arrayRe[i][i+2]=(udiagRe[i]*elem1.real()-udiagIm[i]*elem1.imag());
                                                        arrayIm[i][i+2]=(udiagIm[i]*elem1.real()+udiagRe[i]*elem1.imag());
                                                }
                                        }
                                        if(mRow>3) {
                                                elem1=m.getElement(mRow-3,mRow-4);
                                                arrayRe[mRow-2][mRow-4]=(ldiagRe[mRow-2]*elem1.real()-ldiagIm[mRow-2]*elem1.imag());
                                                arrayIm[mRow-2][mRow-4]=(ldiagIm[mRow-2]*elem1.real()+ldiagRe[mRow-2]*elem1.imag());
                                                elem1=m.getElement(mRow-3,mRow-3);elem2=m.getElement(mRow-2,mRow-3);
                                                arrayRe[mRow-2][mRow-3]=(ldiagRe[mRow-2]*elem1.real()-ldiagIm[mRow-2]*elem1.imag())+(diagRe[mRow-2]*elem2.real()-diagIm[mRow-2]*elem2.imag());
                                                arrayIm[mRow-2][mRow-3]=(ldiagIm[mRow-2]*elem1.real()+ldiagRe[mRow-2]*elem1.imag())+(diagIm[mRow-2]*elem2.real()+diagRe[mRow-2]*elem2.imag());
                                                elem1=m.getElement(mRow-3,mRow-2);elem2=m.getElement(mRow-2,mRow-2);elem3=m.getElement(mRow-1,mRow-2);
                                                arrayRe[mRow-2][mRow-2]=(ldiagRe[mRow-2]*elem1.real()-ldiagIm[mRow-2]*elem1.imag())+(diagRe[mRow-2]*elem2.real()-diagIm[mRow-2]*elem2.imag())+(udiagRe[mRow-2]*elem3.real()-udiagIm[mRow-2]*elem3.imag());
                                                arrayIm[mRow-2][mRow-2]=(ldiagIm[mRow-2]*elem1.real()+ldiagRe[mRow-2]*elem1.imag())+(diagIm[mRow-2]*elem2.real()+diagRe[mRow-2]*elem2.imag())+(udiagIm[mRow-2]*elem3.real()+udiagRe[mRow-2]*elem3.imag());
                                                elem1=m.getElement(mRow-2,mRow-1);elem2=m.getElement(mRow-1,mRow-1);
                                                arrayRe[mRow-2][mRow-1]=(diagRe[mRow-2]*elem1.real()-diagIm[mRow-2]*elem1.imag())+(udiagRe[mRow-2]*elem2.real()-udiagIm[mRow-2]*elem2.imag());
                                                arrayIm[mRow-2][mRow-1]=(diagIm[mRow-2]*elem1.real()+diagRe[mRow-2]*elem1.imag())+(udiagIm[mRow-2]*elem2.real()+udiagRe[mRow-2]*elem2.imag());
                                        }
                                        mRow--;
                                        elem1=m.getElement(mRow-1,mRow-2);
                                        arrayRe[mRow][mRow-2]=(ldiagRe[mRow]*elem1.real()-ldiagIm[mRow]*elem1.imag());
                                        arrayIm[mRow][mRow-2]=(ldiagIm[mRow]*elem1.real()+ldiagRe[mRow]*elem1.imag());
                                        elem1=m.getElement(mRow-1,mRow-1);elem2=m.getElement(mRow,mRow-1);
                                        arrayRe[mRow][mRow-1]=(ldiagRe[mRow]*elem1.real()-ldiagIm[mRow]*elem1.imag())+(diagRe[mRow]*elem2.real()-diagIm[mRow]*elem2.imag());
                                        arrayIm[mRow][mRow-1]=(ldiagIm[mRow]*elem1.real()+ldiagRe[mRow]*elem1.imag())+(diagIm[mRow]*elem2.real()+diagRe[mRow]*elem2.imag());
                                        elem1=m.getElement(mRow-1,mRow);elem2=m.getElement(mRow,mRow);
                                        arrayRe[mRow][mRow]=(ldiagRe[mRow]*elem1.real()-ldiagIm[mRow]*elem1.imag())+(diagRe[mRow]*elem2.real()-diagIm[mRow]*elem2.imag());
                                        arrayIm[mRow][mRow]=(ldiagIm[mRow]*elem1.real()+ldiagRe[mRow]*elem1.imag())+(diagIm[mRow]*elem2.real()+diagRe[mRow]*elem2.imag());
                                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                                } else
                                        throw new MatrixDimensionException("Incompatible matrices.");
                }
        }
        private ComplexSquareMatrix rawMultiplyTridiagonal(final ComplexMatrix m) {
                int mRow=numRows;
                if(numCols==m.numRows) {
                        final double arrayRe[][]=new double[mRow][mRow];
                        final double arrayIm[][]=new double[mRow][mRow];
                        arrayRe[0][0]=(diagRe[0]*m.matrixRe[1][0]-diagIm[0]*m.matrixIm[1][0])+(udiagRe[0]*m.matrixRe[0][1]-udiagIm[0]*m.matrixIm[0][1]);
                        arrayIm[0][0]=(diagIm[0]*m.matrixRe[1][0]+diagRe[0]*m.matrixIm[1][0])+(udiagIm[0]*m.matrixRe[0][1]+udiagRe[0]*m.matrixIm[0][1]);
                        arrayRe[0][1]=(diagRe[0]*m.matrixRe[2][0]-diagIm[0]*m.matrixIm[2][0])+(udiagRe[0]*m.matrixRe[1][1]-udiagIm[0]*m.matrixIm[1][1]);
                        arrayIm[0][1]=(diagIm[0]*m.matrixRe[2][0]+diagRe[0]*m.matrixIm[2][0])+(udiagIm[0]*m.matrixRe[1][1]+udiagRe[0]*m.matrixIm[1][1]);
                        arrayRe[0][2]=(udiagRe[0]*m.matrixRe[2][1]-udiagIm[0]*m.matrixIm[2][1]);
                        arrayIm[0][2]=(udiagIm[0]*m.matrixRe[2][1]+udiagRe[0]*m.matrixIm[2][1]);
                        if(mRow>3) {
                                arrayRe[1][0]=(ldiagRe[1]*m.matrixRe[1][0]-ldiagIm[1]*m.matrixIm[1][0])+(diagRe[1]*m.matrixRe[0][1]-diagIm[1]*m.matrixIm[0][1]);
                                arrayIm[1][0]=(ldiagIm[1]*m.matrixRe[1][0]+ldiagRe[1]*m.matrixIm[1][0])+(diagIm[1]*m.matrixRe[0][1]+diagRe[1]*m.matrixIm[0][1]);
                                arrayRe[1][1]=(ldiagRe[1]*m.matrixRe[2][0]-ldiagIm[1]*m.matrixIm[2][0])+(diagRe[1]*m.matrixRe[1][1]-diagIm[1]*m.matrixIm[1][1])+(udiagRe[1]*m.matrixRe[0][2]-udiagIm[1]*m.matrixIm[0][2]);
                                arrayIm[1][1]=(ldiagIm[1]*m.matrixRe[2][0]+ldiagRe[1]*m.matrixIm[2][0])+(diagIm[1]*m.matrixRe[1][1]+diagRe[1]*m.matrixIm[1][1])+(udiagIm[1]*m.matrixRe[0][2]+udiagRe[1]*m.matrixIm[0][2]);
                                arrayRe[1][2]=(diagRe[1]*m.matrixRe[2][1]-diagIm[1]*m.matrixIm[2][1])+(udiagRe[1]*m.matrixRe[1][2]-udiagIm[1]*m.matrixIm[1][2]);
                                arrayIm[1][2]=(diagIm[1]*m.matrixRe[2][1]+diagRe[1]*m.matrixIm[2][1])+(udiagIm[1]*m.matrixRe[1][2]+udiagRe[1]*m.matrixIm[1][2]);
                                arrayRe[1][3]=(udiagRe[1]*m.matrixRe[2][2]-udiagIm[1]*m.matrixIm[2][2]);
                                arrayIm[1][3]=(udiagIm[1]*m.matrixRe[2][2]+udiagRe[1]*m.matrixIm[2][2]);
                        }
                        if(mRow==3) {
                                arrayRe[1][0]=(ldiagRe[1]*m.matrixRe[1][0]-ldiagIm[1]*m.matrixIm[1][0])+(diagRe[1]*m.matrixRe[0][1]-diagIm[1]*m.matrixIm[0][1]);
                                arrayIm[1][0]=(ldiagIm[1]*m.matrixRe[1][0]+ldiagRe[1]*m.matrixIm[1][0])+(diagIm[1]*m.matrixRe[0][1]+diagRe[1]*m.matrixIm[0][1]);
                                arrayRe[1][1]=(ldiagRe[1]*m.matrixRe[2][0]-ldiagIm[1]*m.matrixIm[2][0])+(diagRe[1]*m.matrixRe[1][1]-diagIm[1]*m.matrixIm[1][1])+(udiagRe[1]*m.matrixRe[0][2]-udiagIm[1]*m.matrixIm[0][2]);
                                arrayIm[1][1]=(ldiagIm[1]*m.matrixRe[2][0]+ldiagRe[1]*m.matrixIm[2][0])+(diagIm[1]*m.matrixRe[1][1]+diagRe[1]*m.matrixIm[1][1])+(udiagIm[1]*m.matrixRe[0][2]+udiagRe[1]*m.matrixIm[0][2]);
                                arrayRe[1][2]=(diagRe[1]*m.matrixRe[2][1]-diagIm[1]*m.matrixIm[2][1])+(udiagRe[1]*m.matrixRe[1][2]-udiagIm[1]*m.matrixIm[1][2]);
                                arrayIm[1][2]=(diagIm[1]*m.matrixRe[2][1]+diagRe[1]*m.matrixIm[2][1])+(udiagIm[1]*m.matrixRe[1][2]+udiagRe[1]*m.matrixIm[1][2]);
                        } else if(mRow>4) {
                                for(int i=2;i<mRow-2;i++) {
                                        arrayRe[i][i-2]=(ldiagRe[i]*m.matrixRe[0][i-1]-ldiagIm[i]*m.matrixIm[0][i-1]);
                                        arrayIm[i][i-2]=(ldiagIm[i]*m.matrixRe[0][i-1]+ldiagRe[i]*m.matrixIm[0][i-1]);
                                        arrayRe[i][i-1]=(ldiagRe[i]*m.matrixRe[1][i-1]-ldiagIm[i]*m.matrixIm[1][i-1])+(diagRe[i]*m.matrixRe[0][i]-diagIm[i]*m.matrixIm[0][i]);
                                        arrayIm[i][i-1]=(ldiagIm[i]*m.matrixRe[1][i-1]+ldiagRe[i]*m.matrixIm[1][i-1])+(diagIm[i]*m.matrixRe[0][i]+diagRe[i]*m.matrixIm[0][i]);
                                        arrayRe[i][i]=(ldiagRe[i]*m.matrixRe[2][i-1]-ldiagIm[i]*m.matrixIm[2][i-1])+(diagRe[i]*m.matrixRe[1][i]-diagIm[i]*m.matrixIm[1][i])+(udiagRe[i]*m.matrixRe[0][i+1]-udiagIm[i]*m.matrixIm[0][i+1]);
                                        arrayIm[i][i]=(ldiagIm[i]*m.matrixRe[2][i-1]+ldiagRe[i]*m.matrixIm[2][i-1])+(diagIm[i]*m.matrixRe[1][i]+diagRe[i]*m.matrixIm[1][i])+(udiagIm[i]*m.matrixRe[0][i+1]+udiagRe[i]*m.matrixIm[0][i+1]);
                                        arrayRe[i][i+1]=(diagRe[i]*m.matrixRe[2][i]-diagIm[i]*m.matrixIm[2][i])+(udiagRe[i]*m.matrixRe[1][i+1]-udiagIm[i]*m.matrixIm[1][i+1]);
                                        arrayIm[i][i+1]=(diagIm[i]*m.matrixRe[2][i]+diagRe[i]*m.matrixIm[2][i])+(udiagIm[i]*m.matrixRe[1][i+1]+udiagRe[i]*m.matrixIm[1][i+1]);
                                        arrayRe[i][i+2]=(udiagRe[i]*m.matrixRe[2][i+1]-udiagIm[i]*m.matrixIm[2][i+1]);
                                        arrayIm[i][i+2]=(udiagIm[i]*m.matrixRe[2][i+1]+udiagRe[i]*m.matrixIm[2][i+1]);
                                }
                        }
                        if(mRow>3) {
                                arrayRe[mRow-2][mRow-4]=(ldiagRe[mRow-2]*m.matrixRe[0][mRow-3]-ldiagIm[mRow-2]*m.matrixIm[0][mRow-3]);
                                arrayIm[mRow-2][mRow-4]=(ldiagIm[mRow-2]*m.matrixRe[0][mRow-3]+ldiagRe[mRow-2]*m.matrixIm[0][mRow-3]);
                                arrayRe[mRow-2][mRow-3]=(ldiagRe[mRow-2]*m.matrixRe[1][mRow-3]-ldiagIm[mRow-2]*m.matrixIm[1][mRow-3])+(diagRe[mRow-2]*m.matrixRe[0][mRow-2]-diagIm[mRow-2]*m.matrixIm[0][mRow-2]);
                                arrayIm[mRow-2][mRow-3]=(ldiagIm[mRow-2]*m.matrixRe[1][mRow-3]+ldiagRe[mRow-2]*m.matrixIm[1][mRow-3])+(diagIm[mRow-2]*m.matrixRe[0][mRow-2]+diagRe[mRow-2]*m.matrixIm[0][mRow-2]);
                                arrayRe[mRow-2][mRow-2]=(ldiagRe[mRow-2]*m.matrixRe[2][mRow-3]-ldiagIm[mRow-2]*m.matrixIm[2][mRow-3])+(diagRe[mRow-2]*m.matrixRe[1][mRow-2]-diagIm[mRow-2]*m.matrixIm[1][mRow-2])+(udiagRe[mRow-2]*m.matrixRe[0][mRow-1]-udiagIm[mRow-2]*m.matrixIm[0][mRow-1]);
                                arrayIm[mRow-2][mRow-2]=(ldiagIm[mRow-2]*m.matrixRe[2][mRow-3]+ldiagRe[mRow-2]*m.matrixIm[2][mRow-3])+(diagIm[mRow-2]*m.matrixRe[1][mRow-2]+diagRe[mRow-2]*m.matrixIm[1][mRow-2])+(udiagIm[mRow-2]*m.matrixRe[0][mRow-1]+udiagRe[mRow-2]*m.matrixIm[0][mRow-1]);
                                arrayRe[mRow-2][mRow-1]=(diagRe[mRow-2]*m.matrixRe[2][mRow-2]-diagIm[mRow-2]*m.matrixIm[2][mRow-2])+(udiagRe[mRow-2]*m.matrixRe[1][mRow-1]-udiagIm[mRow-2]*m.matrixIm[1][mRow-1]);
                                arrayIm[mRow-2][mRow-1]=(diagIm[mRow-2]*m.matrixRe[2][mRow-2]+diagRe[mRow-2]*m.matrixIm[2][mRow-2])+(udiagIm[mRow-2]*m.matrixRe[1][mRow-1]+udiagRe[mRow-2]*m.matrixIm[1][mRow-1]);
                        }
                        mRow--;
                        arrayRe[mRow][mRow-2]=(ldiagRe[mRow]*m.matrixRe[0][mRow-1]-ldiagIm[mRow]*m.matrixIm[0][mRow-1]);
                        arrayIm[mRow][mRow-2]=(ldiagIm[mRow]*m.matrixRe[0][mRow-1]+ldiagRe[mRow]*m.matrixIm[0][mRow-1]);
                        arrayRe[mRow][mRow-1]=(ldiagRe[mRow]*m.matrixRe[1][mRow-1]-ldiagIm[mRow]*m.matrixIm[1][mRow-1])+(diagRe[mRow]*m.matrixRe[0][mRow]-diagIm[mRow]*m.matrixIm[0][mRow]);
                        arrayIm[mRow][mRow-1]=(ldiagIm[mRow]*m.matrixRe[1][mRow-1]+ldiagRe[mRow]*m.matrixIm[1][mRow-1])+(diagIm[mRow]*m.matrixRe[0][mRow]+diagRe[mRow]*m.matrixIm[0][mRow]);
                        arrayRe[mRow][mRow]=(ldiagRe[mRow]*m.matrixRe[2][mRow-1]-ldiagIm[mRow]*m.matrixIm[2][mRow-1])+(diagRe[mRow]*m.matrixRe[1][mRow]-diagIm[mRow]*m.matrixIm[1][mRow]);
                        arrayIm[mRow][mRow]=(ldiagIm[mRow]*m.matrixRe[2][mRow-1]+ldiagRe[mRow]*m.matrixIm[2][mRow-1])+(diagIm[mRow]*m.matrixRe[1][mRow]+diagRe[mRow]*m.matrixIm[1][mRow]);
                        return new ComplexSquareMatrix(arrayRe,arrayIm);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }

// HERMITIAN ADJOINT

        /**
        * Returns the hermitian adjoint of this matrix.
        * @return a complex tridiagonal matrix
        */
        public ComplexMatrix hermitianAdjoint() {
                int mRow=numRows;
                final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                System.arraycopy(ldiagRe,1,ans.udiagRe,0,ldiagRe.length-1);
                System.arraycopy(diagRe,0,ans.diagRe,0,diagRe.length);
                System.arraycopy(udiagRe,0,ans.ldiagRe,1,udiagRe.length-1);
                ans.diagIm[0]=-diagIm[0];
                ans.ldiagIm[1]=-udiagIm[0];
                mRow--;
                for(int i=1;i<mRow;i++) {
                        ans.udiagIm[i-1]=-ldiagIm[i];
                        ans.diagIm[i]=-diagIm[i];
                        ans.ldiagIm[i+1]=-udiagIm[i];
                }
                ans.udiagIm[mRow-1]=-ldiagIm[mRow];
                ans.diagIm[mRow]=-diagIm[mRow];
                return ans;
        }

// CONJUGATE

        /**
        * Returns the complex conjugate of this matrix.
        * @return a complex tridiagonal matrix
        */
        public ComplexMatrix conjugate() {
                int mRow=numRows;
                final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                System.arraycopy(ldiagRe,1,ans.ldiagRe,0,ldiagRe.length-1);
                System.arraycopy(diagRe,0,ans.diagRe,0,diagRe.length);
                System.arraycopy(udiagRe,0,ans.udiagRe,1,udiagRe.length-1);
                ans.diagIm[0]=-diagIm[0];
                ans.udiagIm[0]=-udiagIm[0];
                mRow--;
                for(int i=1;i<mRow;i++) {
                        ans.ldiagIm[i]=-ldiagIm[i];
                        ans.diagIm[i]=-diagIm[i];
                        ans.udiagIm[i]=-udiagIm[i];
                }
                ans.ldiagIm[mRow]=-ldiagIm[mRow];
                ans.diagIm[mRow]=-diagIm[mRow];
                return ans;
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return a complex tridiagonal matrix
        */
        public Matrix transpose() {
                final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(numRows);
                System.arraycopy(ldiagRe,1,ans.udiagRe,0,ldiagRe.length-1);
                System.arraycopy(ldiagIm,1,ans.udiagIm,0,ldiagIm.length-1);
                System.arraycopy(diagRe,0,ans.diagRe,0,diagRe.length);
                System.arraycopy(diagIm,0,ans.diagIm,0,diagIm.length);
                System.arraycopy(udiagRe,0,ans.ldiagRe,1,udiagRe.length-1);
                System.arraycopy(udiagIm,0,ans.ldiagIm,1,udiagIm.length-1);
                return ans;
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
                int i,j,k,pivotrow;
                final int N=numRows;
                final double arrayLRe[][]=new double[N][N];
                final double arrayLIm[][]=new double[N][N];
                final double arrayURe[][]=new double[N][N];
                final double arrayUIm[][]=new double[N][N];
                final double buf[]=new double[N];
                double tmp,tmpRe,tmpIm;
                double max;
                Complex elem;
                if(pivot==null)
                        pivot=new int[N+1];
                for(i=0;i<N;i++)
                        pivot[i]=i;
                pivot[N]=1;
        // LU decomposition to arrayU
                for(j=0;j<N;j++) {
                        for(i=0;i<j;i++) {
                                elem=getElement(pivot[i],j);
                                tmpRe=elem.real();
                                tmpIm=elem.imag();
                                for(k=0;k<i;k++) {
                                        tmpRe-=(arrayURe[i][k]*arrayURe[k][j]-arrayUIm[i][k]*arrayUIm[k][j]);
                                        tmpIm-=(arrayUIm[i][k]*arrayURe[k][j]+arrayURe[i][k]*arrayUIm[k][j]);
                                }
                                arrayURe[i][j]=tmpRe;
                                arrayUIm[i][j]=tmpIm;
                        }
                        max=0.0;
                        pivotrow=j;
                        for(i=j;i<N;i++) {
                                elem=getElement(pivot[i],j);
                                tmpRe=elem.real();
                                tmpIm=elem.imag();
                                for(k=0;k<j;k++) {
                                        tmpRe-=(arrayURe[i][k]*arrayURe[k][j]-arrayUIm[i][k]*arrayUIm[k][j]);
                                        tmpIm-=(arrayUIm[i][k]*arrayURe[k][j]+arrayURe[i][k]*arrayUIm[k][j]);
                                }
                                arrayURe[i][j]=tmpRe;
                                arrayUIm[i][j]=tmpIm;
                        // while we're here search for a pivot for arrayU[j][j]
                                tmp=tmpRe*tmpRe+tmpIm*tmpIm;
                                if(tmp>max) {
                                        max=tmp;
                                        pivotrow=i;
                                }
                        }
                // swap row j with pivotrow
                        if(pivotrow!=j) {
                                System.arraycopy(arrayURe[j],0,buf,0,j+1);
                                System.arraycopy(arrayURe[pivotrow],0,arrayURe[j],0,j+1);
                                System.arraycopy(buf,0,arrayURe[pivotrow],0,j+1);
                                System.arraycopy(arrayUIm[j],0,buf,0,j+1);
                                System.arraycopy(arrayUIm[pivotrow],0,arrayUIm[j],0,j+1);
                                System.arraycopy(buf,0,arrayUIm[pivotrow],0,j+1);
                                k=pivot[j];
                                pivot[j]=pivot[pivotrow];
                                pivot[pivotrow]=k;
                                // update parity
                                pivot[N]=-pivot[N];
                        }
                // divide by pivot
                        tmpRe=arrayURe[j][j];
                        tmpIm=arrayUIm[j][j];
                        double a,denom;
                        if(Math.abs(tmpRe)<Math.abs(tmpIm)) {
                                a=tmpRe/tmpIm;
                                denom=tmpRe*a+tmpIm;
                                for(i=j+1;i<N;i++) {
                                        tmp=(arrayURe[i][j]*a+arrayUIm[i][j])/denom;
                                        arrayUIm[i][j]=(arrayUIm[i][j]*a-arrayURe[i][j])/denom;
                                        arrayURe[i][j]=tmp;
                                }
                        } else {
                                a=tmpIm/tmpRe;
                                denom=tmpRe+tmpIm*a;
                                for(i=j+1;i<N;i++) {
                                        tmp=(arrayURe[i][j]+arrayUIm[i][j]*a)/denom;
                                        arrayUIm[i][j]=(arrayUIm[i][j]-arrayURe[i][j]*a)/denom;
                                        arrayURe[i][j]=tmp;
                                }
                        }
                }
                // move lower triangular part to arrayL
                for(j=0;j<N;j++) {
                        arrayLRe[j][j]=1.0;
                        for(i=j+1;i<N;i++) {
                                arrayLRe[i][j]=arrayURe[i][j];
                                arrayLIm[i][j]=arrayUIm[i][j];
                                arrayURe[i][j]=0.0;
                                arrayUIm[i][j]=0.0;
                        }
                }
                LU=new ComplexSquareMatrix[2];
                LU[0]=new ComplexSquareMatrix(arrayLRe,arrayLIm);
                LU[1]=new ComplexSquareMatrix(arrayURe,arrayUIm);
                LUpivot=new int[pivot.length];
                System.arraycopy(pivot,0,LUpivot,0,pivot.length);
                return LU;
        }

// MAP ELEMENTS

        /**
        * Applies a function on all the matrix elements.
        * @param f a user-defined function
        * @return a complex tridiagonal matrix
        */
        public ComplexMatrix mapElements(final ComplexMapping f) {
                int mRow=numRows;
                final ComplexTridiagonalMatrix ans=new ComplexTridiagonalMatrix(mRow);
                ans.setElement(0,0,f.map(diagRe[0],diagIm[0]));
                ans.setElement(0,1,f.map(udiagRe[0],udiagIm[0]));
                mRow--;
                for(int i=1;i<mRow;i++) {
                        ans.setElement(i,i-1,f.map(ldiagRe[i],ldiagIm[i]));
                        ans.setElement(i,i,f.map(diagRe[i],diagIm[i]));
                        ans.setElement(i,i+1,f.map(udiagRe[i],udiagIm[i]));
                }
                ans.setElement(mRow,mRow-1,f.map(ldiagRe[mRow],ldiagIm[mRow]));
                ans.setElement(mRow,mRow,f.map(diagRe[mRow],diagIm[mRow]));
                return ans;
        }
}

