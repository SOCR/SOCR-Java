/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.util;

/** Matrix is a *lightweight* set of linear
 algebra algorithms that work on double precision
 (real) dense arrays. Efficiency is generally traded
 for compactness and ease of use. The small set of
 algorithms was selected for wide applicability,
 particularly in statistical applications.
 A nice parsing constructor can generate
 a matrix from flexibly formatted text input.

 Note that "Matrix" represents an arbitrary array
 class including row and column vectors.*/

import java.util.Vector;

import javax.swing.JTable;

public class Matrix {

  public int rows;
  public int columns;
  public double[][] element;	// the array containing the matrix

  public Matrix() {
    // Create a zero 1x1 matrix
    rows = 1;
    columns = 1;
    element = new double[rows][columns];
  }

  public Matrix(int r, int c) {
    //  creates an empty r by c matrix
    rows = r;
    columns = c;
    element = new double[rows][columns];
  }


  public Matrix(JTable dataTable) {
    	//  creates a matrix from a data-table
    	rows = dataTable.getRowCount();
    	columns = dataTable.getColumnCount();
    	element = new double[rows][columns];

	for (int k = 0; k< rows; k++)
	{  for (int i = 0; i < columns; i++)
	   {	element[k][i] =
		  (Double.valueOf((String)(dataTable.getValueAt(k, i)))).doubleValue();
	    }
	 }
  }


  public Matrix(double d) {
    //  creates a 1x1 matrix of double d
    rows = 1;
    columns = 1;
    element = new double[1][1];
    element[0][0] = d;
  }

  public Matrix(int r, int c, double fill) {
    //  creates an  r by c matrix with entries 'fill'
    rows = r;
    columns = c;
    element = new double[rows][columns];
    int i, j;
    for (i=0; i<rows; i++) {
      for (j=0; j<columns; j++) {
	element[i][j] = fill;
      }
    }
  }

  public Matrix(int r,int c, double[][] fill) {
    //  creates an  r by c matrix with entries 'fill'
    rows = r;
    columns = c;
    element = new double[rows][columns];
    int i, j;
    for (i=0; i<rows; i++) {
      for (j=0; j<columns; j++) {
	element[i][j] = fill[i][j];
      }
    }
  }


  public Matrix(Matrix m) {
    //  creates a new replicate of m
    rows = m.rows;
    columns = m.columns;
    element = new double[rows][columns];
    int i, j;
    for (i=0; i<rows; i++) {
      for (j=0; j<columns; j++) {
	element[i][j] = m.element[i][j];
      }
    }
  }

  public Matrix(int r, int c, char code) {
    // contsructor: creates an  r by c special matrix
    rows = r;
    columns = c;
    element = new double[rows][columns];
    int i, j;
    if ((code == 'i') || (code == 'I')){
      // make an identity matrix
      for (i = 0; i < r; i++) {
	if (i < c) {
	  element[i][i] = 1;
	}
      }
    }
    else if ((code == 'h') || (code == 'H')){
      // make a Hilbert matrix
      for (i = 0; i < r; i++) {
	for (j=0; j<c; j++) {
	  element[i][j] = 1/((double)i+(double)j+1);
	}
      }
    }
    else if ((code == 'r') || (code == 'R')){
      // make a random matrix with entries uniform in [0, 1]
      for (i = 0; i < r; i++) {
	for (j=0; j<c; j++) {
	  element[i][j] = Math.random();
	}
      }
    }
  }

  public Matrix(String s) {
    /* Creates a matrix object
       and parses the string into the element array.
       The string format can be like Matlab or typical
       delimited ascii data as follows:
       columns separated by tabs, commas, or sapces
       rows separated by newline or semicolon
       short rows filled with zeros to make rectangular
    */

    Vector<Vector<String>> row = new Vector<Vector<String>>();	// data will be assembled into these vectors
    Vector<String> col = new Vector<String>();	// and then transferred
                                // into the array element[][].
    s = s + " ;";
    int i = s.length();
    int j;
    int rowCounter = 0;
    int colCounter = 0;
    String sData = new String(); // will hold each element during parsing
    Double fl;
    char sChar;
    for (j = 0; j<i; j++) {
      sChar = s.charAt(j);
      // check for a delimiter...
      if ( (sChar==' ')|| (sChar==',')|| ( (int) sChar==9)||
	   (sChar == ';')||( (int) sChar == 13)||( (int) sChar == 10) ) {
	fl = new Double(0);
	// See if the string in sData represents a number...
	try {
	  boolean testSpace = true;
	  int ii;
	  for(ii=0;ii<sData.length();ii++){
	    testSpace=testSpace&&(sData.charAt(ii)==' '); }
	  if(testSpace==false){
	    fl = new Double(sData);
	    col.addElement(sData); }	// append column element as string
	  sData = new String();	// wipe out contents of string
	}
	catch (Exception e) {
	// non-numeric stuff...
	  sData = new String();	// wipe out contents of string
	}
	if ( ( (sChar == ';')||( (int) sChar == 13)||( (int) sChar == 10) ) &&
	     !col.isEmpty() ) {
	  row.addElement(col); // append row (i.e., vector of column elements)
	  rowCounter = rowCounter + 1;
	  sData = new String();		// wipe out contents of string
	  colCounter = col.size();
	  col = new Vector<String>();	// wipe out the column vector
	  /* an interesting Java (1.02) note: use new Vector() method to
	     force the contents of this vector to be explicitly copied
	     into the row vector. The removeAllElements method will not
	     work in this situation (try it!).
	  */
	}
      }
      // build up data...
      else {
	if ((Character.isDigit(sChar))||(sChar=='.')||(sChar=='-')) {
	  // allow only digit and decimal point characters
	  sData = sData + sChar;	// append to string
	}
      }

    }
    rows = rowCounter;
    columns = colCounter;
    element = new double[rows][columns];
    col = new Vector<String>();
    Double d = new Double(0);
    for (j = 0; j<rows; j++) {
    	col = (Vector<String>) row.elementAt(j);
    	for (i = 0; i<col.size(); i++) {
    		d = new Double((String)col.elementAt(i));
    		element[j][i] = d.doubleValue();
      }	
    }
  }

  public Matrix transpose() {
    // returns the transpose of this matrix object
    Matrix t = new Matrix(columns, rows);
    int i, j;
    for (i = 0; i<rows; i++) {
      for (j = 0; j<columns; j++) {
	t.element[j][i] = this.element[i][j];
      }
    }
    return t;
  }

  public Matrix diag() {
    /* Returns the diagonal of this matrix (as a vector), or
       if given a vector, returns a diagonal matrix
    */
    Matrix t = new Matrix(rows, 1);
    if(columns==1){
      t = new Matrix(rows, rows);
    }
    int i, k, j;
    for (i = 0; i<rows; i++) {
      if(columns>1){
	k=0; j=i;
      }
      else{
	k=i; j=0;
      }
      t.element[i][k] = this.element[i][j];
    }
    return t;
  }

  public static Matrix add(Matrix m1, Matrix m2){
    // Return the matrix m = m1 + m2
    Matrix m=new Matrix(m1.rows,m1.columns);
    if ((m1.rows == m2.rows)&&(m1.columns==m2.columns)) {
      int i,j;
      for (i=0; i<m.rows; i++) {
	for (j=0; j<m.columns; j++) {
	  m.element[i][j] = m1.element[i][j] + m2.element[i][j];
	}
      }
    }
    return m;
  }

  public double[] getColumn(int j) {
  double[] t = new double[this.rows];
  for(int i=0;i< this.rows;i++)
      t[i] = this.element[i][j];

  return t;

  }



  public static Matrix subtract(Matrix m1, Matrix m2){
    // Return the matrix m = m1 - m2
    Matrix m=new Matrix(m1.rows,m1.columns);
    if ((m1.rows == m2.rows)&&(m1.columns==m2.columns)) {
      int i,j;
      for (i=0; i<m.rows; i++) {
	for (j=0; j<m.columns; j++) {
	  m.element[i][j] = m1.element[i][j] - m2.element[i][j];
	}
      }
    }
    return m;
  }

  public static Matrix multiply(double d, Matrix m1){
    // Return the matrix m = d*m1
    Matrix m=new Matrix(m1.rows,m1.columns);
    int i,j;
    for (i=0; i<m.rows; i++) {
      for (j=0; j<m.columns; j++) {
	m.element[i][j] = d * m1.element[i][j];
      }
    }
    return m;
  }

  public static Matrix multiply(Matrix m1, Matrix m2){
    /* Matrix-Matrix or Matrix-vector product
       returns m=m1*m2
       m1 can be a 1x1 Matrix for scalar-Matrix product
    */
    Matrix m = new Matrix(0);
    if (m1.columns == m2.rows) {
      // matrix product
      double sum = 0;
      int k = 0;
      m = new Matrix(m1.rows,m2.columns);
      int i,j;
      for (i=0; i<m.rows; i++) {
	for (k=0; k<m2.columns; k++) {
	  for (j=0; j<m1.columns; j++) {
	    sum = sum + m1.element[i][j] * m2.element[j][k];
	  }
	  m.element[i][k] = sum;
	  sum = 0;
	}
      }
    }
    else if ((m1.columns == 1)&&(m1.rows == 1)) {
      // scalar-vector product
      m = new Matrix(m2.rows,m2.columns);
      int i,j;
      for (i=0; i<m.rows; i++) {
	for (j=0; j<m.columns; j++) {
	  m.element[i][j] = m1.element[0][0] * m2.element[i][j];
	}
      }
    }
    return m;
  }

  public static Matrix divide(Matrix m1, Matrix m2) {
    /* Returns m1/m2. If m2 is a 1x1 matrix, then this is
       just matrix/scalar. If m2 is a square, invertible
       matrix and m1 is a vector (a matrix with one column),
       divide returns inverse(m2)*m1, using the
       Householder QR algorithm.
    */
    Matrix m = new Matrix(0);
    if ((m2.columns == 1)&&(m2.rows == 1)) {
      // vector-scalar division
      m=new Matrix(m1.rows,m1.columns);
      int i,j;
      for (i=0; i<m.rows; i++) {
	for (j=0; j<m.columns; j++) {
	  m.element[i][j] = m1.element[i][j] / m2.element[0][0];
	}
      }
    }
    else if ((m2.columns == m2.rows)&&
	     (m1.columns == 1)&&(m1.rows == m2.rows)) {
      /* Solve a general, dense, non-singular linear
	 system Ax=b via QR, where A=m2, b=m1, and x is returned. */
      m=new Matrix(m2.rows,1);
      Matrix Q=m2.Q();
      Matrix R=m2.R();
      Matrix b=multiply(Q.transpose(),m1);
      int i,j;
      double sum = 0;
      m.element[m.rows-1][0] =
	b.element[m.rows-1][0]/R.element[m.rows-1][m.rows-1];
      i=m.rows-1;
      while(i >= 0) {
	sum = 0;
	j = m.rows-1;
	while(j>=i+1) {
	  sum = sum + R.element[i][j]*m.element[j][0];
	  j--;
	}
	m.element[i][0] = (b.element[i][0]-sum)/R.element[i][i];
	i--;
      }
    }
    return m;
  }

/* 	added 20050903 to do inverse of a matrix. by annieche
	Gauss-Jordan eleimination was used.
*/
// Perhaps add a function to check singularity?
	public static Matrix inverse(Matrix matrix) {
		// matrix is the eliminated matrix from G-J, composed of Identidy and inverse
		matrix = eliminationGaussJordan(matrix);
		int nrow = matrix.rows;
		int ncol = matrix.columns;
		//System.out.println("ROW = " + nrow);
		//System.out.println("COL = " + ncol);
		if (2 * nrow != ncol)
			return null; // or would it be better to throw Exception?
		Matrix result = new Matrix(nrow, nrow); //squared

		for (int i = 0; i < nrow; i++) {
			for (int j = nrow; j < ncol; j++) {
				result.element[i][j-nrow] = matrix.element[i][j];
			}
		}
		return result;

	}

	public static Matrix eliminationGaussJordan(Matrix matrixInput) {
		int nrow = matrixInput.rows;
		int ncol = matrixInput.columns;
		int ncolNew = 	2 * ncol;

		Matrix matrix = new Matrix(nrow, ncolNew, 0);
		for (int i = 0; i < nrow; i++) {
			for (int j = 0; j < ncolNew; j++) {
				if (j < ncol)
					matrix.element[i][j] = matrixInput.element[i][j];
				else if (i == (j - ncol) )
					matrix.element[i][j] = 1;
			}
		}

		double max_val = 0;
		int max_ind = -1;
		double[] tempRow = new double[ncol];
		double tempEntry = 0;
		int i = 0;
		int j = 0;
		double tempMultiple = 0;
		double tempPivot = 0;
		while (i < nrow && j < ncolNew) {
				max_val = Math.abs(matrix.element[i][j]);
				max_ind = i;

				for (int k = i+1; k < nrow; k++) {
					if (Math.abs(matrix.element[k][j]) > max_val) {
						max_val = Math.abs(matrix.element[k][j]);
						max_ind = k;
					} // end if
				} // end for k

				if (max_val != 0) {
					// switch rows
					tempRow = matrix.element[i];
					matrix.element[i] = matrix.element[max_ind];
					matrix.element[max_ind] = tempRow;
					tempRow = null;
				//	System.out.println("After switch " + matrix.element[i][j] + " and " + matrix.element[max_ind][j]);
					tempPivot = matrix.element[max_ind][j];
					// the next divide row i by max_val
					for (int p = 0; p < ncolNew; p++) {
						matrix.element[i][p] = matrix.element[i][p] / max_val;
				//		System.out.println("After divide " + matrix.element[i][p]);
					} // finish dividing
					tempPivot = matrix.element[i][j];
					if (tempPivot < 0)
						for (int p = 0; p < ncolNew; p++) {								matrix.element[i][p] = -matrix.element[i][p] ;
				//		System.out.println("After Change sign " + matrix.element[i][p]);
						} // end for p

				//	print(matrix);
				//	System.out.println("============================================");
					for (int u = 0; u < nrow; u++) {
						if ( u != i) {
							tempMultiple = matrix.element[u][j];
							for (int v = 0; v < ncolNew; v++) {

				//				System.out.println("Now j is : " + j + " current entry = " +(matrix.element[i][v]) + " original = " + (matrix.element[i][v]) + " tempMultiple = " + tempMultiple );

								matrix.element[u][v] = matrix.element[u][v] -
								(matrix.element[i][v]) * tempMultiple;

							} // end for v
						} // end if
				//		print(matrix);
					} // end for u
				//	print(matrix);
					i++;
				} // end if max_val not 0

			j++;
		}

		return matrix;
  	}


  public Matrix sub(int r1, int r2, int c1, int c2) {
    // returns the submatrix (r1:r2,c1:c2) (Moeler notation)
    // requires r2>=r1, c2>=c1
    Matrix A = new Matrix(r2 - r1 + 1, c2 - c1 + 1);
    int i, j;
    for (i = r1; i<=r2; i++) {
      for (j = c1; j<=c2; j++) {
	A.element[i - r1][j - c1] = this.element[i][j];
      }
    }
    return A;
  }

  public Matrix appendCols(Matrix x){
    // append the column vectors in x to this matrix
    Matrix M=new Matrix(rows,columns+x.columns);
    int i,j;
    for(i=0;i<rows;i++){
      for(j=0;j<columns;j++){
	M.element[i][j]=this.element[i][j];
      }
      for(j=0;j<x.columns;j++){
	M.element[i][columns+j]=x.element[i][j];
      }
    }
    return M;
  }

  public Matrix appendRows(Matrix x){
    // append the row vectors in x to this matrix
    Matrix M=new Matrix(rows+x.rows,columns);
    int i,j;
    for(i=0;i<columns;i++){
      for(j=0;j<rows;j++){
	M.element[j][i]=this.element[j][i];
      }
      for(j=0;j<x.rows;j++){
	M.element[rows+j][i]=x.element[j][i];
      }
    }
    return M;
  }

  public Matrix flipud(){
    // flip this matrix vertically
    Matrix M=new Matrix(rows,columns);
    int k,j;
    for(k=0;k<rows;k++){
      for(j=0;j<columns;j++){
	M.element[rows-k-1][j]=this.element[k][j];
      }
    }
    return M;
  }

  public Matrix fliplr(){
    // flip this matrix horizontally
    Matrix M=new Matrix(rows,columns);
    int k,j;
    for(k=0;k<rows;k++){
      for(j=0;j<columns;j++){
	M.element[k][columns-j-1]=this.element[k][j];
      }
    }
    return M;
  }

  public Matrix permute(int a1, int a2, char c) {
    /*	Returns a permuted matrix according  code c
	where c is in {'c', 'r'} for columns or rows and
	a1, a2 represent the columns/rows to swap
    */
    Matrix p = new Matrix(this);
    int i, j;
    if (c == 'r') {
      for (i=0; i<columns; i++) {
	p.element[a1][i] = this.element[a2][i];
	p.element[a2][i] = this.element[a1][i];
      }
    }
    else if (c == 'c') {
      for (i=0; i<rows; i++) {
	p.element[i][a1] = this.element[i][a2];
	p.element[i][a2] = this.element[i][a1];
      }
    }
    return p;
  }

  public double norm() {
    /* returns the Frobenius norm (Matrix), or Euclidean norm (Vector)
       This is the default norm for a Matrix object. Use the Norm
       class for different norms.
    */
    double l = 0;
    int i, j;
    for (i = 0; i<rows; i++) {
      for (j = 0; j<columns; j++) {
	l = l + this.element[i][j] * this.element[i][j];
      }
    }
    l = Math.pow(l, 0.5);
    return l;
  }

  public double max() {
    /* returns the most positive element of the matrix or vector */
    double m = this.element[0][0];
    int i,j;
    for (i = 0; i<rows; i++) {
      for (j = 0; j<columns; j++) {
	if(this.element[i][j] > m){
	  m = this.element[i][j];
	}
      }
    }
    return m;
  }

  public double sum() {
    /* returns the sum of all the elements in the matrix or vector
     */
    double s = 0;
    int i, j;
    for (i = 0; i<rows; i++) {
      for (j = 0; j<columns; j++) {
	s = s + this.element[i][j];
      }
    }
    return s;
  }

  public double average() {
    // returns the average of all the elements in the matrix or vector
    double s = 0;
    int i, j;
    for (i = 0; i<rows; i++) {
      for (j = 0; j<columns; j++) {
	s = s + this.element[i][j];
      }
    }
    return s/(columns*rows);
  }

  public double sumSquares() {
    // returns the sum of the squares
    // of all the elements in the matrix or vector
    double s = 0;
    int i, j;
    for (i = 0; i<rows; i++) {
      for (j = 0; j<columns; j++) {
	s = s + Math.pow(this.element[i][j],2);
      }
    }
    return s;
  }

  public Matrix  Q() {
    /*	returns the 'Q' in the QR-decomposition of this matrix object
	using Householder reflections, without column pivoting
    */
    Matrix P = new Matrix(rows, rows, 'I');
    Matrix A = new Matrix(this);
    Matrix AA, PP;
    int i, j;
    Matrix v;

    for(j = 0; j<columns; j++) {
      v = A.sub(0,A.rows-1, j, j);
      if (j>0) {
	for (i = 0; i<j; i++) {
	  v.element[i][0] = 0;
	}
      }
      v.element[j][0] = v.element[j][0] + v.norm() * sign(v.element[j][0]);
      double r = (double) -2 / (v.norm() * v.norm() );
      AA = new Matrix(A);
      A = multiply(v.transpose(),A);
      A = multiply(v,A);
      A = multiply(r,A);
      A = add(AA,A);
      PP = new Matrix(P);
      P = multiply(v.transpose(),P);
      P = multiply(v,P);
      P = multiply(r,P);
      P = add(PP,P);
    }
    return P.transpose();
  }

  public Matrix  R() {
    /*	returns the 'R' in the QR-decomposition of this matrix object
	using Householder reflections, without column pivoting
    */
    Matrix P = new Matrix(rows, rows, 'I');
    Matrix A = new Matrix(this);
    Matrix AA, PP;
    int i, j;
    Matrix v;

    for(j = 0; j<columns; j++) {
      v = A.sub(0,A.rows-1, j, j);
      if (j>0) {
	for (i = 0; i<j; i++) {
	  v.element[i][0] = 0;
	}
      }
      v.element[j][0] = v.element[j][0] + v.norm() * sign(v.element[j][0]);
      double r = (double) -2 / (v.norm() * v.norm() );
      AA = new Matrix(A);
      A = multiply(v.transpose(),A);
      A = multiply(v,A);
      A = multiply(r,A);
      A = add(AA,A);
      PP = new Matrix(P);
      P = multiply(v.transpose(),P);
      P = multiply(v,P);
      P = multiply(r,P);
      P = add(PP,P);
    }
    return A;
  }

  public Vector<Matrix>  qr() {
    /*	returns the QR-decomposition of this matrix object
	using Householder reflections, without column pivoting
	The results is a (Java) vector containing {Q, R}
    */
    Vector<Matrix> result = new Vector<Matrix>();
    Matrix P = new Matrix(rows, rows, 'I');
    Matrix A = new Matrix(this);
    Matrix AA, PP;
    int i, j;
    Matrix v;

    for(j = 0; j<columns; j++) {
      v = A.sub(0,A.rows-1, j, j);
      if (j>0) {
	for (i = 0; i<j; i++) {
	  v.element[i][0] = 0;
	}
      }
      v.element[j][0] = v.element[j][0] + v.norm() * sign(v.element[j][0]);
      double r = (double) -2 / (v.norm() * v.norm() );
      AA = new Matrix(A);
      A = multiply(v.transpose(),A);
      A = multiply(v,A);
      A = multiply(r,A);
      A = add(AA,A);
      PP = new Matrix(P);
      P = multiply(v.transpose(),P);
      P = multiply(v,P);
      P = multiply(r,P);
      P = add(PP,P);
    }
    result.addElement(A);      // R (at element 0)
    result.addElement(P.transpose());	// Q (at element 1)
    return result;
  }

  public Vector<Matrix>  toHess() {
    /*	makes the matrix upper Hessenberg via Householder reflections
	returns  {P, H} s.t. P' * this * P = H and H is upper Hessenberg
	and P' * P = I.
    */
    Vector<Matrix> result = new Vector<Matrix>();	// the result
    Matrix P = new Matrix(rows, columns, 'I');
    Matrix I = new Matrix(rows, columns, 'I');
    Matrix A = new Matrix(this);
    int i, j, k;
    Matrix v;

    for(j = 0; j<columns-2; j++) {
      v = new Matrix(rows, 1);
      v.element[j][0] = 1;
      v = multiply(A,v); // get the j-th column
      for (i=0; i<(j+1); i++) {
	v.element[i][0] = 0;
      }
      v.element[j+1][0] = v.element[j+1][0] +
	v.norm() * sign(v.element[j+1][0]);

      double r = (double) -2 / (v.norm() * v.norm());
      v = multiply(v,v.transpose());
      v = multiply(r,v);
      v = add(I,v);
      P = multiply(P,v);
      A = multiply(P.transpose(),this);
      A = multiply(A,P);
    }

    result.addElement(P);   // the orthogonal transformation
    result.addElement(A);   // the upper-Hessenberg form
    return result;
  }

  public Vector<Matrix>  gepp() {

    /*	returns the LU decomposition of a matrix using the Gauss
	transform. This algorithm returns 3 matrices as follows:
	{P, L, U} such that LU = PA. This algorithm performs partial
	pivoting.
	Written 3-March, 1997 by Bryan Lewis
    */
    Vector<Matrix> v = new Vector<Matrix>();	// the result
    Matrix P = new Matrix(rows, columns, 'I');
    // P will track the permutations
    Matrix L = new Matrix(rows, columns, 'I');	// the lower triangle
    Matrix U = this;	// this matrix to be transformed to upper triangular
    Matrix G = new Matrix(rows, columns, 'I');
    // temporary Gauss transform matrix
    int i, j, k, p;
    double d;

    for (j = 0; j < columns-1; j++) {
      // start of parital pivot code:
      d = Math.abs(U.element[j][j]);
      p = j;
      for (i = j+1; i < rows; i++) {
	if (Math.abs(U.element[i][j]) > d) {
	  // System.out.println(U.element[i][j] +", "+i);
	  d = Math.abs(U.element[i][j]);
	  p = i;
	}
      }
      if (p > j) {
	U = U.permute(j, p, 'r');
	P = P.permute(j, p, 'r'); // don't forget to track permutations
      }
      // end of partial pivot code.
      for (i = j+1; i < rows; i++) {
	if (U.element[j][j]!=0) {
	  G.element[i][j] = -U.element[i][j]/U.element[j][j];
	}
      }
      U = multiply(G, U);
      L = L.permute(j, p, 'r');
      for (k = 0; k < j; k++) {
	L.element[k][j] = 0;
      }
      L.element[j][j] = 1;
      for (k = j+1; k < rows; k++) {
	L.element[k][j] = -G.element[k][j];
	G.element[k][j] = 0;
      }

    }
    for (k = 0; k < rows; k++) {
      L.element[k][columns-1] = 0;
    }
    L.element[rows-1][columns-1] = 1;

    v.addElement(P);
    v.addElement(L);
    v.addElement(U);
    return v;
  }

  public double leig(double p) {
    /*	Elementary QR  method method to find the spectral radius of
	a positive valued matrix. Parameter p = precision desired.
	For example, if A is a Matrix of  positive real numbers, then
	A.leig(0.01) returns the largest eigenvalue to at least two
	digits of accuracy.
    */
    Vector<Matrix> qr;
    Matrix Q = new Matrix(rows, columns);
    Matrix R = new Matrix(rows, columns);
    Matrix A = new Matrix(this);	// initialized
    int i = 1;
    int maxIter = 200-this.rows;
    if(maxIter<25){maxIter=25;} // set up a maximum iteration count
    double v = 99;		// temporary result
    double res = 99;	// residual
    while((i<maxIter)&&(res>p)) {
      qr = A.qr();
      Q = (Matrix)qr.elementAt(1);
      R = (Matrix)qr.elementAt(0);
      A = multiply(R,Q);
      i++;
      res = Math.abs(A.element[0][0] - v);
      v = A.element[0][0];
    }
    return A.element[0][0];
  }

  public String toString(int d) {
    /*Return a string representation of this matrix with 'd'
      displayed digits*/
    String newln = System.getProperty("line.separator");
    String outPut = new String();
    String num = new String();
    int i, j;
    for (i=0; i<this.rows; i++) {
      for (j=0; j<this.columns; j++) {
	Numeric x = new Numeric(this.element[i][j]);
	num = x.toString(d);
	outPut = outPut + num + (char) 9;
      }
      outPut = outPut + newln;
    }
    return outPut;
  }

  public String toString() {
    /*Return a string representation of this matrix with
      6 displayed digits*/
    String outPut = this.toString(6);
    return outPut;
  }

  public Matrix  sort() {
    /* Sort this object in the following way:
       1. If this is a column or row vector, sort the vector
          and return a two column Matrix containing the sorted
	  vector in the first column, and the permutation indices
	  in the 2nd column.
       2. If this is a rectangular Matrix, sort the rows according
          to the entries in the first column.
    */
    Matrix R = new Matrix();
    int i,k;
    double d=Math.max(columns,rows);
    int lngth = (int)d;
    double a[] = new double[lngth];
    int indx[] = new int[lngth];

    if((rows == 1)||(columns==1)) {
      a = new double[lngth];	// contains the data for sorting
      indx = new int[lngth];	// index array
      for (i=0; i<lngth; i++) {
	if(rows<columns){
	  a[i] = element[0][i];}
	else {
	  a[i] = element[i][0];}
	indx[i] = i + 1;
      }
      qsort(a, indx, 0, lngth - 1);
      R = new Matrix(lngth, 2);
      for (i=0; i<lngth; i++) {
	R.element[i][0] = a[i];
	R.element[i][1] = indx[i];
      }
    }
    else if (columns > 1) {
      // sort the array by the first column
      a = new double[rows];	// contains the data for sorting
      indx = new int[rows];	// index array
      for (i=0; i<rows; i++) {
	a[i] = element[i][0];
	indx[i] = i + 1;
      }
      qsort(a, indx, 0, rows - 1);
      R = new Matrix(rows, columns);
      for(i=0; i<rows; i++) {
	for(k=0; k<columns; k++){
	  R.element[i][k] = element[indx[i]-1][k];
	}
      }
    }
    return R;
  }

  public Matrix  order() {
    /*	Sorts this vector, returning a ranked vector,
	(here, 'vector' indicates a 1-d Matrix, not the Java class Vector).
	'order' is used so as to not confuse with the usual definition of rank
    */
    Matrix S = this.sort();
    Matrix y = new Matrix(S.rows, 1);
    double[] v = new double[S.rows];
    // the only trick here is to handle ties!
    int i = 0, k, j, l;
    while (i<S.rows){
      j = 0; l = 0;
      for(k=i;k<S.rows;k++){
	if(S.element[k][0]==S.element[i][0]) {
	  j = j + 1;
	  l = l + k +1;
	}
      }
      for(k=0;k<j;k++){
	v[i+k]=(double)(((double)(l))/(double)j);
      }
      i = i + j;
    }
    // now unsort v and return it...
    for (i=0;i<S.rows; i++){
      y.element[(int)S.element[i][1]-1][0]=v[i];
    }
    return y;
  }

  // The following methods are used internally by the Matrix class:

  double sign(double d) {
    // returns the sign of the supplied double-precision argument
    double s = 1;
    if (d<0) { s = -1; }
    return s;
  }

  void qsort(double a[], int index[], int lo0, int hi0)   {
    /* Recursive Quick Sort algorithm
       a[]=input 1-d array
       index[]=output permutation array
       lo0=input lower index
       hi0=input upper index
    */
      int lo = lo0;
      int hi = hi0;
      double mid;

      if ( hi0 > lo0)
	{
	  mid = a[ ( lo0 + hi0 ) / 2 ];
	  while( lo <= hi )
	    {
	      while( ( lo < hi0 ) && ( a[lo] < mid ) )
		++lo;
	      while( ( hi > lo0 ) && ( a[hi] > mid ) )
		--hi;
	      if( lo <= hi )
		{
		  swap(a, lo, hi);
		  swap(index, lo, hi);
		  ++lo;
		  --hi;
		}
	    }
	  if( lo0 < hi )
            qsort( a,index, lo0, hi );
	  if( lo < hi0 )
            qsort( a,index, lo, hi0 );
	}
  }

  private void swap(double a[], int i, int j){
    double T;
    T = a[i];
    a[i] = a[j];
    a[j] = T;
  }
  private void swap(int a[], int i, int j){
    int T;
    T = a[i];
    a[i] = a[j];
    a[j] = T;
  }

public static void print(Matrix matrix) {
		for (int i = 0; i < matrix.rows; i++ ) {
			System.out.println("-----------------");
				for (int j = 0; j < matrix.columns; j++ ) {
					System.out.println("MATRIX["+i+"]["+j+"] = " + matrix.element[i][j]);
				}
			}

}
 public static void main(String args[]) {


	 /* testing only
		Matrix matrix = new Matrix(3, 4);

		matrix.element[0][0] = 2;
		matrix.element[0][1] = 1;
		matrix.element[0][2] = -1;
		matrix.element[0][3] = 8;

		matrix.element[1][0] = -3;
		matrix.element[1][1] = -1;
		matrix.element[1][2] = 2;
		matrix.element[1][3] = -11;

		matrix.element[2][0] = -2;
		matrix.element[2][1] = 1;
		matrix.element[2][2] = 2;
		matrix.element[2][3] = -3;
	*/

	 //(c(1,2,2,6,2,2,6,4,2)

		Matrix matrix = new Matrix(3, 3);

		matrix.element[0][0] = 1;
		matrix.element[0][1] = 3;
		matrix.element[0][2] = 3;


		matrix.element[1][0] = 2;
		matrix.element[1][1] = 2;
		matrix.element[1][2] = 4;


		matrix.element[2][0] = 2;
		matrix.element[2][1] = 2;
		matrix.element[2][2] = 2;

	  	int numberColumns = matrix.columns;
/*		Matrix inverseMatrix = new Matrix(numberColumns, numberColumns, 'I');

		for (int i = 0; i < numberColumns; i++ ) {
			for (int j = 0; j < numberColumns; j++ ) {
				if (i == j)
					identityMatrix.element[i][j] = 2;
				//System.out.println("IM[i][j]" + identityMatrix.element[i][j]);
			}
		}
*/

		Matrix inverseProductXX = new Matrix(numberColumns, numberColumns);
		System.out.println("inverseProductXX row " + inverseProductXX.rows);
		System.out.println("inverseProductXX col " + inverseProductXX.columns);

		inverseProductXX = Matrix.inverse(matrix);
		System.out.println("inverseProductXX = \n" + inverseProductXX);
		for (int i = 0; i < matrix.rows; i++ ) {
			System.out.println("\n");
			for (int j = 0; j <  matrix.columns; j++ ) {
				System.out.println("INV[i][j]" + inverseProductXX.element[i][j]);
			}
		}

  }
}
