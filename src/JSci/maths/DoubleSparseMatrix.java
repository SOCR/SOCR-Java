package JSci.maths;

import JSci.GlobalSettings;

/**
* The DoubleSparseMatrix class provides an object for encapsulating sparse matrices.
* Uses compressed row storage.
* @version 1.2
* @author Mark Hale
*/
public final class DoubleSparseMatrix extends DoubleMatrix {
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
        * @param rows the number of rows
        * @param cols the number of columns
        */
        public DoubleSparseMatrix(final int rows, final int cols) {
                super(rows,cols,SPARSE);
                matrix=new double[1][0];
                colPos=new int[0];
                this.rows=new int[numRows+1];
        }
        /**
        * Constructs a matrix from an array.
        * @param array an assigned value
        */
        public DoubleSparseMatrix(final double array[][]) {
                super(array.length,array[0].length,SPARSE);
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
        * Compares two double sparse matrices for equality.
        * @param m a double matrix
        */
        public boolean equals(Object m) {
                if(m!=null && (m instanceof DoubleSparseMatrix) &&
                numRows==((DoubleSparseMatrix)m).numRows && numCols==((DoubleSparseMatrix)m).numCols) {
                        final DoubleSparseMatrix dsm=(DoubleSparseMatrix)m;
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
        * @return an integer matrix
        */
        public IntegerMatrix toIntegerMatrix() {
                final int ans[][]=new int[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                ans[i][j]=Math.round((float)getElement(i,j));
                }
                return new IntegerMatrix(ans);
        }
        /**
        * Converts this matrix to a complex matrix.
        * @return a complex matrix
        */
        public ComplexMatrix toComplexMatrix() {
                final double arrayRe[][]=new double[numRows][numCols];
                for(int j,i=0;i<numRows;i++) {
                        for(j=0;j<numCols;j++)
                                arrayRe[i][j]=getElement(i,j);
                }
                return new ComplexMatrix(arrayRe,new double[numRows][numCols]);
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
                        case SPARSE: return add((DoubleSparseMatrix)m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)+m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)+m.getElement(i,j);
                                        }
                                        return new DoubleMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleMatrix rawAdd(final DoubleMatrix m) {
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
                        return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a double sparse matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSparseMatrix add(final DoubleSparseMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        DoubleSparseMatrix ans=new DoubleSparseMatrix(numRows,numCols);
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
                        case SPARSE: return subtract((DoubleSparseMatrix)m);
                        default: 
                                if(numRows==m.rows() && numCols==m.columns()) {
                                        final double array[][]=new double[numRows][numCols];
                                        for(int j,i=0;i<numRows;i++) {
                                                array[i][0]=getElement(i,0)-m.getElement(i,0);
                                                for(j=1;j<numCols;j++)
                                                        array[i][j]=getElement(i,j)-m.getElement(i,j);
                                        }
                                        return new DoubleMatrix(array);
                                } else
                                        throw new MatrixDimensionException("Matrices are different sizes.");
                }
        }
        private DoubleMatrix rawSubtract(final DoubleMatrix m) {
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
                        return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Matrices are different sizes.");
        }
        /**
        * Returns the addition of this matrix and another.
        * @param m a double sparse matrix
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public DoubleSparseMatrix subtract(final DoubleSparseMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
                        DoubleSparseMatrix ans=new DoubleSparseMatrix(numRows,numCols);
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
                final DoubleSparseMatrix ans=new DoubleSparseMatrix(numRows,numCols);
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
                        case SPARSE: return scalarProduct((DoubleSparseMatrix)m);
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
        * @param m a double sparse matrix.
        * @exception MatrixDimensionException If the matrices are different sizes.
        */
        public double scalarProduct(final DoubleSparseMatrix m) {
                if(numRows==m.numRows && numCols==m.numCols) {
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
                        case SPARSE: return multiply((DoubleSparseMatrix)m);
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
                                        for(n=rows[j];n<rows[j+1];n++)
                                                array[j][k]+=matrix[0][n]*m.matrix[colPos[n]][k];
                                }
                        }
                        if(numRows==m.numCols)
                                return new DoubleSquareMatrix(array);
                        else
                                return new DoubleMatrix(array);
                } else
                        throw new MatrixDimensionException("Incompatible matrices.");
        }
        /**
        * Returns the multiplication of this matrix and another.
        * @param m a double sparse matrix
        * @exception MatrixDimensionException If the matrices are incompatible.
        */
        public DoubleMatrix multiply(final DoubleSparseMatrix m) {
                if(numCols==m.numRows) {
                        int n,k;
                        double tmp;
                        DoubleMatrix ans;
                        if(numRows==m.numCols)
                                ans=new DoubleSparseSquareMatrix(numRows);
                        else
                                ans=new DoubleSparseMatrix(numRows,m.numCols);
                        for(int j=0;j<ans.numRows;j++) {
                                for(k=0;k<ans.numCols;k++) {
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
                final DoubleSparseMatrix ans=new DoubleSparseMatrix(numCols,numRows);
                for(int j,i=0;i<numRows;i++) {
                        ans.setElement(0,i,getElement(i,0));
                        for(j=1;j<numCols;j++)
                                ans.setElement(j,i,getElement(i,j));
                }
                return ans;
        }

// MAP ELEMENTS

        /**
        * Applies a function on all the matrix elements.
        * @param f a user-defined function
        * @return a double sparse matrix
        */
        public DoubleMatrix mapElements(final Mapping f) {
                final DoubleSparseMatrix ans=new DoubleSparseMatrix(numRows,numCols);
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