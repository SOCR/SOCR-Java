package JSci.maths;

import JSci.GlobalSettings;

/**
* The DoubleSparseSquareMatrix class provides an object for encapsulating sparse square matrices.
* Uses compressed row storage.
* @version 1.2
* @author Mark Hale
*/
public final class DoubleSparseSquareMatrix extends DoubleSquareMatrix {
        /**
        * Storage format identifier.
        */
        protected final static int SPARSE=2;
        /**
        * Sparse indexing data.
        * Contains the column positions of each element,
        * e.g. <code>colPos[n]</code> is the column position
        * of the <code>n</code>th element.
        */
        private int colPos[];
        /**
        * Sparse indexing data.
        * Contains the indices of the start of each row,
        * e.g. <code>rows[i]</code> is the index
        * where the <code>i</code>th row starts.
        */
        private int rows[];
        /**
        * Constructs an empty matrix.
        * @param size the number of rows/columns
        */
        public DoubleSparseSquareMatrix(final int size) {
                super(size,SPARSE);
                matrix=new double[1][0];
                colPos=new int[0];
                rows=new int[numRows+1];
        }
        /**
        * Constructs a matrix from an array.
        * @param array an assigned value
        * @exception MatrixDimensionException If the array is not square.
        */
        public DoubleSparseSquareMatrix(final double array[][]) {
                super(array.length,SPARSE);
                if(array.length!=array[0].length) {
                        matrix=null;
                        colPos=null;
                        rows=null;
                        throw new MatrixDimensionException("The array is not square.");
                }
                rows=new int[numRows+1];
                int n=0;
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++) {
                                if(Math.abs(array[i][j])>GlobalSettings.ZERO_TOL)
                                        n++;
                        }
                }
                matrix=new double[1][n];
                colPos=new int[n];
                n=0;
                for(int j,i=0;i<numRows;i++) {
                        rows[i]=n;
                        for(j=0;j<numCols;j++) {
                                if(Math.abs(array[i][j])>GlobalSettings.ZERO_TOL) {
                                        matrix[0][n]=array[i][j];
                                        colPos[n]=j;
                                        n++;
                                }
                        }
                }
                rows[numRows]=n;
        }
        /**
        * Finalize.
        * @exception Throwable Any that occur.
        */
        protected void finalize() throws Throwable {
                colPos=null;
                rows=null;
                super.finalize();
        }
        /**
        * Compares two double sparse square matrices for equality.
        * @param m a double matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof DoubleSparseSquareMatrix) &&
                numRows==((DoubleSparseSquareMatrix)m).numRows) {
                        final DoubleSparseSquareMatrix dsm=(DoubleSparseSquareMatrix)m;
                        if(colPos.length!=dsm.colPos.length)
                                return false;
                        for(int i=1;i<rows.length;i++) {
                                if(rows[i]!=dsm.rows[i])
                                        return false;
                        }
                        for(int i=1;i<colPos.length;i++) {
                                if(colPos[i]!=dsm.colPos[i])
                                        return false;
                                if(Math.abs(matrix[0][i]-dsm.matrix[0][i])>GlobalSettings.ZERO_TOL)
                                        return false;
                        }
                        return true;
                } else
                        return false;
        }
        /**
        * Returns a string representing this matrix.
        */
        public String toString() {
                final StringBuffer buf=new StringBuffer(numRows*numCols);
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++) {
                                buf.append(getElement(i,j));
                                buf.append(' ');
                        }
                        buf.append('\n');
                }
                return buf.toString();
        }
        /**
        * Converts this matrix to an integer matrix.
        * @return an integer square matrix
        */
        public IntegerMatrix toIntegerMatrix() {
                final int ans[][]=new int[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                ans[i][j]=Math.round((float)getElement(i,j));
                }
                return new IntegerSquareMatrix(ans);
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex square matrix
        */
        public ComplexMatrix toComplexMatrix() {
                final double arrayRe[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                arrayRe[i][j]=getElement(i,j);
                }
                return new ComplexSquareMatrix(arrayRe,new double[numRows][numCols]);
        }
        /**
        * Returns an element of the matrix.
        * @param i row index of the element
        * @param j column index of the element
        * @exception MatrixDimensionException If attempting to access an invalid element.
        */
        public double getElement(final int i, final int j) {
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        for(int k=rows[i];k<rows[i+1];k++) {
                                if(colPos[k]==j)
                                        return matrix[0][k];
                        }
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
                if(i>=0 && i<numRows && j>=0 && j<numCols) {
                        if(Math.abs(x)<=GlobalSettings.ZERO_TOL)
                                return;
                        int k;
                        for(k=rows[i];k<rows[i+1];k++) {
                                if(colPos[k]==j) {
                                        matrix[0][k]=x;
                                        return;
                                }
                        }
                        final double oldMatrix[]=matrix[0];
                        final int oldColPos[]=colPos;
                        matrix[0]=new double[oldMatrix.length+1];
                        colPos=new int[oldColPos.length+1];
                        System.arraycopy(oldMatrix,0,matrix[0],0,rows[i]);
                        System.arraycopy(oldColPos,0,colPos,0,rows[i]);
                        for(k=rows[i];k<rows[i+1] && oldColPos[k]<j;k++) {
                                matrix[0][k]=oldMatrix[k];
                                colPos[k]=oldColPos[k];
                        }
                        matrix[0][k]=x;
                        colPos[k]=j;
                        System.arraycopy(oldMatrix,k,matrix[0],k+1,oldMatrix.length-k);
                        System.arraycopy(oldColPos,k,colPos,k+1,oldColPos.length-k);
                        for(k=i+1;k<rows.length;k++)
                                rows[k]++;
                } else
                        throw new MatrixDimensionException(getInvalidElementMsg(i,j));
        }
        /**
        * Returns the determinant.
        */
        public double det() {
                final DoubleSquareMatrix lu[]=this.luDecompose(null);
                double det=lu[1].matrix[0][0];
                for(int i=1;i<numRows;i++)
                        det*=lu[1].matrix[i][i];
                return det*LUpivot[numRows];
        }
        /**
        * Returns the trace.
        */
        public double trace() {
                double result=getElement(0,0);
                for(int i=1;i<numRows;i++)
                        result+=getElement(i,i);
                return result;
        }
        /**
        * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
        */
        public double infNorm() {
                double result=0.0,tmpResult;
                for(int j,i=0;i<numRows;i++) {
                        tmpResult=0.0;
                        for(j=rows[i];j<rows[i+1];j++)
                                tmpResult+=Math.abs(matrix[0][j]);
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
                for(int i=0;i<colPos.length;i++)
                        result+=matrix[0][i]*matrix[0][i];
                return Math.sqrt(result);
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
                        case SPARSE: return add((DoubleSparseSquareMatrix)m);
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
                        int i,j;
                        for(i=0;i<numRows;i++) {
                                for(j=rows[i];j<rows[i+1];j++)
                                        array[i][colPos[j]]=matrix[0][j];
                                array[i][0]+=m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]+=m.matrix[i][j];
                        }
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
                        case SPARSE: return add((DoubleSparseSquareMatrix)m);
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
        * @param m a double sparse matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSparseSquareMatrix add(final DoubleSparseSquareMatrix m) {
                if(numRows==m.numRows) {
                        DoubleSparseSquareMatrix ans=new DoubleSparseSquareMatrix(numRows);
                        for(int j,i=0;i<numRows;i++) {
                                for(j=0;j<numCols;j++)
                                        ans.setElement(i,j,getElement(i,j)+m.getElement(i,j));
                        }
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
                        case SPARSE: return subtract((DoubleSparseSquareMatrix)m);
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
                        int i,j;
                        for(i=0;i<numRows;i++) {
                                for(j=rows[i];j<rows[i+1];j++)
                                        array[i][colPos[j]]=matrix[0][j];
                                array[i][0]-=m.matrix[i][0];
                                for(j=1;j<numCols;j++)
                                        array[i][j]-=m.matrix[i][j];
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
                        case SPARSE: return subtract((DoubleSparseSquareMatrix)m);
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
        * Returns the addition of this matrix and another.
        * @param m a double sparse matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSparseSquareMatrix subtract(final DoubleSparseSquareMatrix m) {
                if(numRows==m.numRows) {
                        DoubleSparseSquareMatrix ans=new DoubleSparseSquareMatrix(numRows);
                        for(int j,i=0;i<numRows;i++) {
                                for(j=0;j<numCols;j++)
                                        ans.setElement(i,j,getElement(i,j)-m.getElement(i,j));
                        }
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// SCALAR MULTIPLICATION

        /**
        * Returns the multiplication of this matrix by a scalar.
        * @param x a double
        * @return a double sparse matrix
        */
        public DoubleMatrix scalarMultiply(final double x) {
                final DoubleSparseSquareMatrix ans=new DoubleSparseSquareMatrix(numRows);
                ans.matrix[0]=new double[matrix[0].length];
                ans.colPos=new int[colPos.length];
                System.arraycopy(colPos,0,ans.colPos,0,colPos.length);
                System.arraycopy(rows,0,ans.rows,0,rows.length);
                for(int i=0;i<colPos.length;i++)
                        ans.matrix[0][i]=x*matrix[0][i];
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
                        case SPARSE: return scalarProduct((DoubleSparseSquareMatrix)m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        double ans=0.0;
                                        for(int j,i=0;i<numRows;i++) {
                                                for(j=rows[i];j<rows[i+1];j++)
                                                        ans+=matrix[0][j]*m.getElement(i,colPos[j]);
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
                                for(j=rows[i];j<rows[i+1];j++)
                                        ans+=matrix[0][j]*m.matrix[i][colPos[j]];
                        }
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
                        case SPARSE: return scalarProduct((DoubleSparseSquareMatrix)m);
                        default: 
                                if(numRows==m.rows()) {
                                        double ans=0.0;
                                        for(int j,i=0;i<numRows;i++) {
                                                for(j=rows[i];j<rows[i+1];j++)
                                                        ans+=matrix[0][j]*m.getElement(i,colPos[j]);
                                        }
                                        return ans;
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        /**
        * Returns the scalar product of this matrix and another.
        * @param m a double sparse matrix.
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public double scalarProduct(final DoubleSparseSquareMatrix m) {
                if(numRows==m.numRows) {
                        double ans=0.0;
                        for(int j,i=0;i<numRows;i++) {
                                for(j=rows[i];j<rows[i+1];j++)
                                        ans+=matrix[0][j]*m.getElement(i,colPos[j]);
                        }
                        return ans;
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }

// MATRIX MULTIPLICATION

        /**
        * Returns the multiplication of a vector by this matrix.
        * @param v a double vector
        * @exception DimensionException If the matrix and vector are incompatible.
        */
        public DoubleVector multiply(final DoubleVector v) {
                if(numCols==v.dimension()) {
                        final double array[]=new double[numRows];
                        for(int j,i=0;i<numRows;i++) {
                                for(j=rows[i];j<rows[i+1];j++)
                                        array[i]+=matrix[0][j]*v.getComponent(colPos[j]);
                        }
                        return new DoubleVector(array);
                } else
                        throw new DimensionException("Matrix and vector are incompatible.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double matrix
        * @exception MatrixDimensionException If the matrices are incompatible.
        */
        public DoubleMatrix multiply(final DoubleMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case SPARSE: return multiply((DoubleSparseSquareMatrix)m);
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final double array[][]=new double[numRows][m.columns()];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<m.columns();k++) {
                                                        for(n=rows[j];n<rows[j+1];n++)
                                                                array[j][k]+=matrix[0][n]*m.getElement(colPos[n],k);
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
                                        for(n=rows[j];n<rows[j+1];n++)
                                                array[j][k]+=matrix[0][n]*m.matrix[colPos[n]][k];
                                }
                        }
                        return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double square matrix
        * @exception MatrixDimensionException If the matrices are incompatible.
        */
        public DoubleSquareMatrix multiply(final DoubleSquareMatrix m) {
                switch(m.storageFormat) {
                        case ARRAY_2D: return rawMultiply(m);
                        case SPARSE: return multiply((DoubleSparseSquareMatrix)m);
                        default: 
                                if(numCols==m.rows()) {
                                        int n,k;
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j=0;j<numRows;j++) {
                                                for(k=0;k<numCols;k++) {
                                                        for(n=rows[j];n<rows[j+1];n++)
                                                                array[j][k]+=matrix[0][n]*m.getElement(colPos[n],k);
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
                                        for(n=rows[j];n<rows[j+1];n++)
                                                array[j][k]+=matrix[0][n]*m.matrix[colPos[n]][k];
                                }
                        }
                        return new DoubleSquareMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double sparse matrix
        * @exception MatrixDimensionException If the matrices are incompatible.
        */
        public DoubleSparseSquareMatrix multiply(final DoubleSparseSquareMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        double tmp;
                        DoubleSparseSquareMatrix ans=new DoubleSparseSquareMatrix(numRows);
                        for(int j=0;j<numRows;j++) {
                                for(k=0;k<numCols;k++) {
                                        tmp=0.0;
                                        for(n=rows[j];n<rows[j+1];n++)
                                                tmp+=matrix[0][n]*m.getElement(colPos[n],k);
                                        ans.setElement(j,k,tmp);
                                }
                        }
                        return ans;
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }

// TRANSPOSE

        /**
        * Returns the transpose of this matrix.
        * @return a double sparse matrix
        */
        public Matrix transpose() {
                final DoubleSparseSquareMatrix ans=new DoubleSparseSquareMatrix(numRows);
                for(int j,i=0;i<numRows;i++) {
                        ans.setElement(0,i,getElement(i,0));
                        for(j=1;j<numCols;j++)
                                ans.setElement(j,i,getElement(i,j));
                }
                return ans;
        }

// LU DECOMPOSITION

        /**
        * Returns the LU decomposition of this matrix.
        * @param pivot an empty array of length <code>rows()+1</code>
        * to hold the pivot information (null if not interested)
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
                final double arrayL[][]=new double[numRows][numCols];
                final double arrayU[][]=new double[numRows][numCols];
                final double buf[]=new double[numRows];
                double tmp,max;
                if(pivot==null)
                        pivot=new int[numRows+1];
                for(i=0;i<numRows;i++)
                        pivot[i]=i;
                pivot[numRows]=1;
        // LU decomposition to arrayU
                for(j=0;j<numCols;j++) {
                        for(i=0;i<j;i++) {
                                tmp=getElement(pivot[i],j);
                                for(k=0;k<i;k++)
                                        tmp-=arrayU[i][k]*arrayU[k][j];
                                arrayU[i][j]=tmp;
                        }
                        max=0.0;
                        pivotrow=j;
                        for(i=j;i<numRows;i++) {
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
                                pivot[numRows]=-pivot[numRows];
                        }
                // divide by pivot
                        for(i=j+1;i<numRows;i++)
                                arrayU[i][j]/=arrayU[j][j];
                }
                // move lower triangular part to arrayL
                for(j=0;j<numCols;j++) {
                        arrayL[j][j]=1.0;
                        for(i=j+1;i<numRows;i++) {
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
                double tmp;
                final double array[][][]=new double[2][numRows][numCols];
                array[0][0][0]=array[1][0][0]=Math.sqrt(getElement(0,0));
                for(i=1;i<numRows;i++)
                        array[0][i][0]=array[1][0][i]=getElement(i,0)/array[0][0][0];
                for(j=1;j<numCols;j++) {
                        tmp=getElement(j,j);
                        for(i=0;i<j;i++)
                                tmp-=array[0][j][i]*array[0][j][i];
                        array[0][j][j]=array[1][j][j]=Math.sqrt(tmp);
                        for(i=j+1;i<numRows;i++) {
                                tmp=getElement(i,j);
                                for(k=0;k<i;k++)
                                        tmp-=array[0][j][k]*array[1][k][i];
                                array[0][i][j]=array[1][j][i]=tmp/array[1][j][j];
                        }
                }
                final DoubleSquareMatrix lu[]=new DoubleSquareMatrix[2];
                lu[0]=new DoubleSquareMatrix(array[0]);
                lu[1]=new DoubleSquareMatrix(array[1]);
                return lu;
        }

// MAP ELEMENTS

        /**
        * Applies a function on all the matrix elements.
        * @param f a user-defined function
        * @return a double sparse matrix
        */
        public DoubleMatrix mapElements(final Mapping f) {
                final DoubleSparseSquareMatrix ans=new DoubleSparseSquareMatrix(numRows);
                ans.matrix[0]=new double[matrix[0].length];
                ans.colPos=new int[colPos.length];
                System.arraycopy(colPos,0,ans.colPos,0,colPos.length);
                System.arraycopy(rows,0,ans.rows,0,rows.length);
                for(int i=0;i<colPos.length;i++)
                        ans.matrix[0][i]=f.map(matrix[0][i]);
                return ans;
        }
}

