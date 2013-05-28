package JSci.maths;

/**
* The linear math library.
* This class cannot be subclassed or instantiated because all methods are static.
* @version 2.2
* @author Mark Hale
*/
public final class LinearMath extends AbstractMath {
        private LinearMath() {}

// LINEAR SYSTEM

        /**
        * Solves the linear system Mx=v.
        * @param M a double square matrix.
        * @param v a double vector.
        * @return the double vector x.
        */
        public static DoubleVector solve(final DoubleSquareMatrix M,final DoubleVector v) {
                final int n=v.dimension();
                final double array[]=new double[n];
                final int pivot[]=new int[n+1];
                final DoubleSquareMatrix lu[]=M.luDecompose(pivot);
                int i,j;
                double sum;
        // forward substitution
                for(i=0;i<n;i++) {
                        sum=v.getComponent(pivot[i]);
                        for(j=0;j<i;j++)
                                sum-=lu[0].getElement(i,j)*array[j];
                        array[i]=sum/lu[0].getElement(i,i);
                }
        // back substitution
                for(i=n-1;i>=0;i--) {
                        sum=array[i];
                        for(j=i+1;j<n;j++)
                                sum-=lu[1].getElement(i,j)*array[j];
                        array[i]=sum/lu[1].getElement(i,i);
                }
                return new DoubleVector(array);
        }
        private static DoubleVector solveCholesky(final double m[][],final double v[]) {
                final int n=v.length;
                final double array[]=new double[n];
                final DoubleSquareMatrix lu[]=new DoubleSquareMatrix(m).choleskyDecompose();
                int i,j;
                double sum;
        // forward substitution
                for(i=0;i<n;i++) {
                        sum=v[i];
                        for(j=0;j<i;j++)
                                sum-=lu[0].getElement(i,j)*array[j];
                        array[i]=sum/lu[0].getElement(i,i);
                }
        // back substitution
                for(i=n-1;i>=0;i--) {
                        sum=array[i];
                        for(j=i+1;j<n;j++)
                                sum-=lu[1].getElement(i,j)*array[j];
                        array[i]=sum/lu[1].getElement(i,i);
                }
                return new DoubleVector(array);
        }
        /**
        * Solves the unsymmetric linear system Ax=b using the
        * Generalized Minimum Residual method (doesn't require A
        * to be nonsingular).
        * While slower than LU decomposition, it is more
        * robust and should be used with large matrices.
        * It is guaranted to converge exactly in N iterations for an
        * N by N matrix (minus some numerical errors).
        * @author Alain Beliveau
        * @author Daniel Lemire
        * @param max_iter maximum number of iterations.
	* @param tol tolerance.
        * @exception IllegalArgumentException If either the tolerance
        * or the number of iterations is not positive.
        * Also, if an unexpected error occurs.
        * @exception MaximumIterationsExceededException If it cannot
        * converge according to the given parameters.
        */
        public static DoubleVector solveGMRes(final DoubleMatrix A,final DoubleVector b,int max_iter,double tol) throws MaximumIterationsExceededException {
                if(max_iter<=0)
                        throw new IllegalArgumentException("Number of allowed iterations must be a positive integer: "+max_iter+" <= 0.");
                if(tol<0)
                        throw new IllegalArgumentException("Tolerance must be positive or zero: "+tol+" < 0.");
                final int m=A.rows();
   		double resid;
   		int i,j=1,k;
   		double[] s=new double[m+1];
   		double[][] cs=new double[m+1][2];
   		double[] rotmp=new double[2];
   		DoubleVector w;
                DoubleVector x=new DoubleVector(A.rows());
   		double normb=b.norm();
                DoubleVector r=b.subtract(A.multiply(x));
   		double beta=r.norm();
   		if(normb==0.0)
                        normb=1.0;
   		if((resid=r.norm()/normb)<= tol) {
                        tol = resid;
     			max_iter = 0;
     			throw new IllegalArgumentException("There is a bug.");
                }
   		DoubleVector[] v = new DoubleVector[m+1];
   		DoubleMatrix H = new DoubleMatrix(m+1,m);
 		while (j <= max_iter) {
     			v[0] = r.scalarMultiply(1.0 / beta);
     			for (i = 0; i < m+1; i++)
     				s[i] = 0.0;
     			s[0] = beta;
     			for (i = 0; i < m && j <= max_iter; i++, j++) {
                                w = A.multiply(v[i]);
       	        		for (k = 0; k <= i; k++) {
                                        H.matrix[k][i]=w.scalarProduct(v[k]);
         				w = w.subtract(v[k].scalarMultiply(H.matrix[k][i]));
             	       		}
       	        		H.matrix[i+1][i]=w.norm();
               			v[i+1] = w.scalarMultiply(1.0 / H.matrix[i+1][i]);
 				for (k = 0; k < i; k++) {
               				rotmp=applyPlaneRotation(H.matrix[k][i], H.matrix[k+1][i], cs[k][0], cs[k][1]);
       	        			H.matrix[k][i]=rotmp[0];
               				H.matrix[k+1][i]=rotmp[1];
                    		}
       	        		cs[i]=generatePlaneRotation(H.matrix[i][i], H.matrix[i+1][i]);
               			rotmp=applyPlaneRotation(H.matrix[i][i], H.matrix[i+1][i], cs[i][0], cs[i][1]);
       	        		H.matrix[i][i]=rotmp[0];
               			H.matrix[i+1][i]=rotmp[1];
       	        		rotmp=applyPlaneRotation(s[i], s[i+1], cs[i][0], cs[i][1]);
               			s[i]=rotmp[0];
       	        		s[i+1]=rotmp[1];
               			if ((resid = Math.abs(s[i+1]) / normb) < tol) {
         				x = update(x, i, H, s, v);
         				tol = resid;
         				max_iter = j;
         				return (x);
                 		}
                        }
                        x = update(x, m - 1, H, s, v);
                        r = b.subtract(A.multiply(x));
                        beta = r.norm();
                        if ((resid = beta / normb) < tol) {
               			tol = resid;
       	        		max_iter = j;
               			return (x);
   			}
		}
   		tol = resid;
   		throw new MaximumIterationsExceededException("(tol) "+tol+". It doesn't converge in "+max_iter+"iterations. Try raising the number of allowed iterations or raising the tolerance.");
	}
	private static double[] generatePlaneRotation(double dx, double dy) {
   		double [] cs = new double[2];
   		double temp;
   		if (dy == 0.0) {
     			cs[0] = 1.0;
     			cs[1] = 0.0;
     	 	} else if (Math.abs(dy) > Math.abs(dx)) {
     			temp = dx / dy;
     			cs[1] = 1.0 / Math.sqrt( 1.0 + temp*temp );
     			cs[0] = temp * cs[1];
              	} else {
     			temp = dy / dx;
     			cs[0] = 1.0 / Math.sqrt( 1.0 + temp*temp );
     			cs[1] = temp * cs[0];
              	}
     		return (cs);
	}
	private static double[] applyPlaneRotation(double dx, double dy, double cs, double sn) {
   		double [] dxy = new double[2];
   		dxy[0] = cs * dx + sn * dy;
   		dxy[1] = -sn * dx + cs * dy;
     		return (dxy);
	}
	private static DoubleVector update(DoubleVector x, int k, DoubleMatrix H, double [] s, DoubleVector [] v) {
   		DoubleVector y = new DoubleVector(s);
 		/************************************************
    		* Backsolve of a triangular + sub diagonal matrix:
    		************************************************/
		for (int i = k; i >= 0; i--) {
     			y.vector[i]=y.vector[i]/H.matrix[i][i];
     			for (int j = i - 1; j >= 0; j--)
       			y.vector[j]=y.vector[j]-H.matrix[j][i]*y.vector[i];
              	}
 		for (int j = 0; j <= k; j++){
     			x = x.add(v[j].scalarMultiply(y.vector[j]));
              	}
     		return(x);
	}

// LEAST SQUARES

        /**
        * Fits an nth degree polynomial to data using the method of least squares.
        * @param n the degree of the polynomial.
        * @param data
        * [0][] contains the x-series,
        * [1][] contains the y-series.
        * @return a vector containing the polynomial's coefficients (in ascending degree order).
        */
        public static DoubleVector leastSquaresFit(int n,final double data[][]) {
                int i,j,nm1=n++;
                double xsum,xysum,tmp;
                final double mArray[][]=new double[n][n];
                final double vArray[]=new double[n];
                for(i=0;i<n;i++) {
                        xsum=xysum=0.0;
                        for(j=0;j<data[0].length;j++) {
                                tmp=Math.pow(data[0][j],i);
                                xsum+=tmp;
                                xysum+=tmp*data[1][j];
                        }
                        mArray[0][i]=xsum;
                        vArray[i]=xysum;
                }
                for(i=1;i<n;i++) {
                        System.arraycopy(mArray[i-1],1,mArray[i],0,nm1);
                        xsum=0.0;
                        for(j=0;j<data[0].length;j++)
                                xsum+=Math.pow(data[0][j],nm1+i);
                        mArray[i][nm1]=xsum;
                }
                return solveCholesky(mArray,vArray);
        }

// LINEAR REGRESSION

        /**
        * Fits a line to multi-dimensional data using the method of least squares.
        * @param data
        * [0...n-1][] contains the x-series' (they must be linearly uncorrelated),
        * [n][] contains the y-series.
        * @return a vector containing the coefficients.
        */
        public static DoubleVector linearRegression(final double data[][]) {
                final int y=data.length-1;
                int i,j,k;
                double xsum,xysum;
                final double mArray[][]=new double[data.length][data.length];
                final double vArray[]=new double[data.length];
                mArray[0][0]=data[0].length;
                for(j=1;j<data.length;j++) {
                        xsum=0.0;
                        for(k=0;k<data[0].length;k++)
                                xsum+=data[j-1][k];
                        mArray[0][j]=mArray[j][0]=xsum;
                }
                xysum=0.0;
                for(k=0;k<data[0].length;k++)
                        xysum+=data[y][k];
                vArray[0]=xysum;
                for(i=1;i<data.length;i++) {
                        for(j=i;j<data.length;j++) {
                                xsum=0.0;
                                for(k=0;k<data[0].length;k++)
                                        xsum+=data[i-1][k]*data[j-1][k];
                                mArray[i][j]=mArray[j][i]=xsum;
                        }
                        xysum=0.0;
                        for(k=0;k<data[0].length;k++)
                                xysum+=data[i-1][k]*data[y][k];
                        vArray[i]=xysum;
                }
                return solveCholesky(mArray,vArray);
        }

// GRAM-SCHMIDT

        /**
        * The Gram-Schmidt orthonormalization method.
        * @param vecs a set of linearly independent vectors.
        * @return a set of orthonormal vectors.
        */
        public static DoubleVector[] orthonormalize(DoubleVector vecs[]) {
                final int N=vecs.length;
                DoubleVector orthovecs[]=new DoubleVector[N];
                for(int i=0;i<N;i++) {
                        orthovecs[i]=vecs[i];
                        for(int j=0;j<i;j++)
                                orthovecs[i]=orthovecs[i].subtract(orthovecs[j].scalarMultiply(orthovecs[j].scalarProduct(vecs[i])));
                        orthovecs[i].normalize();
                }
                return orthovecs;
        }

// EIGENVALUES & EIGENVECTORS

        /**
        * This method finds the eigenvalues of a Hermitian matrix.
        * @param matrix a Hermitian matrix.
        * @return an array containing the eigenvalues.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public static double[] eigenvalueSolveHermitian(final ComplexSquareMatrix matrix) throws MaximumIterationsExceededException {
                final int n=matrix.rows();
                final double matrix2[][]=new double[2*n][2*n];
                double real,imag;
                for(int j,i=0;i<n;i++) {
                        for(j=0;j<n;j++) {
                                real=matrix.getElement(i,j).real();
                                imag=matrix.getElement(i,j).imag();
                                matrix2[i][j]=real;
                                matrix2[n+i][n+j]=real;
                                matrix2[n+i][j]=imag;
                                matrix2[i][n+j]=-imag;
                        }
                }
                final double eigenvalue2[]=new double[2*n];
                final double offdiag[]=new double[2*n];
                reduceSymmetric1_SquareToTridiagonal(matrix2,eigenvalue2,offdiag);
                System.arraycopy(offdiag,1,offdiag,0,n-1);
                offdiag[n-1]=0.0;
                eigenvalueSolveSymmetricTridiagonalMatrix(eigenvalue2,offdiag);
                final double eigenvalue[]=new double[n];
                System.arraycopy(eigenvalue2,0,eigenvalue,0,n);
                return eigenvalue;
        }
        /**
        * This method finds the eigenvalues and eigenvectors of a Hermitian matrix.
        * @param matrix a Hermitian matrix.
        * @param eigenvector an empty array of complex vectors to hold the eigenvectors.
        * All eigenvectors will be orthogonal.
        * @return an array containing the eigenvalues.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public static double[] eigenSolveHermitian(final ComplexSquareMatrix matrix,final ComplexVector eigenvector[]) throws MaximumIterationsExceededException {
                final int n=matrix.rows();
                final double matrix2[][]=new double[2*n][2*n];
                int i,j;
                double real,imag;
                for(i=0;i<n;i++) {
                        for(j=0;j<n;j++) {
                                real=matrix.getElement(i,j).real();
                                imag=matrix.getElement(i,j).imag();
                                matrix2[i][j]=real;
                                matrix2[n+i][n+j]=real;
                                matrix2[n+i][j]=imag;
                                matrix2[i][n+j]=-imag;
                        }
                }
                final double eigenvalue2[]=new double[2*n];
                final double offdiag[]=new double[2*n];
                reduceSymmetric2_SquareToTridiagonal(matrix2,eigenvalue2,offdiag);
                System.arraycopy(offdiag,1,offdiag,0,n-1);
                offdiag[n-1]=0.0;
                eigenSolveSymmetricTridiagonalMatrix(eigenvalue2,offdiag,matrix2);
                final double eigenvalue[]=new double[n];
                double arrayRe[],arrayIm[];
                for(i=0;i<n;i++) {
                        eigenvalue[i]=eigenvalue2[i];
                        arrayRe=new double[n];
                        arrayIm=new double[n];
                        for(j=0;j<n;j++) {
                                arrayRe[j]=matrix2[j][i];
                                arrayIm[j]=matrix2[j+n][i];
                        }
                        eigenvector[i]=new ComplexVector(arrayRe,arrayIm);
                }
                return eigenvalue;
        }
        /**
        * This method finds the eigenvalues of a symmetric tridiagonal matrix by the QL method.
        * It is based on the NETLIB algol/fortran procedure tql1 by Bowdler, Martin, Reinsch and Wilkinson.
        * @param matrix a double symmetric tridiagonal matrix.
        * @return an array containing the eigenvalues.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public static double[] eigenvalueSolveSymmetric(final DoubleTridiagonalMatrix matrix) throws MaximumIterationsExceededException {
                final int n=matrix.rows();
                final int nm1=n-1;
                final double eigenvalue[]=new double[n];
                final double offdiag[]=new double[n];
                for(int i=0;i<nm1;i++) {
                        eigenvalue[i]=matrix.getElement(i,i);
                        offdiag[i]=matrix.getElement(i,i+1);
                }
                eigenvalue[nm1]=matrix.getElement(nm1,nm1);
                offdiag[nm1]=0.0;
                eigenvalueSolveSymmetricTridiagonalMatrix(eigenvalue,offdiag);
                return eigenvalue;
        }
        /**
        * This method finds the eigenvalues and eigenvectors of a symmetric tridiagonal matrix by the QL method.
        * It is based on the NETLIB algol/fortran procedure tql2 by Bowdler, Martin, Reinsch and Wilkinson.
        * @param matrix a double symmetric tridiagonal matrix.
        * @param eigenvector an empty array of double vectors to hold the eigenvectors.
        * All eigenvectors will be orthogonal.
        * @return an array containing the eigenvalues.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public static double[] eigenSolveSymmetric(final DoubleTridiagonalMatrix matrix,final DoubleVector eigenvector[]) throws MaximumIterationsExceededException {
                final int n=matrix.rows();
                final int nm1=n-1;
                final double eigenvalue[]=new double[n];
                final double offdiag[]=new double[n];
                final double id[][]=new double[n][n];
                int i,j;
                for(i=0;i<nm1;i++) {
                        id[i][i]=1.0;
                        eigenvalue[i]=matrix.getElement(i,i);
                        offdiag[i]=matrix.getElement(i,i+1);
                }
                id[nm1][nm1]=1.0;
                eigenvalue[nm1]=matrix.getElement(nm1,nm1);
                offdiag[nm1]=0.0;
                eigenSolveSymmetricTridiagonalMatrix(eigenvalue,offdiag,id);
                for(i=0;i<n;i++) {
                        eigenvector[i]=new DoubleVector(n);
                        for(j=0;j<n;j++)
                                eigenvector[i].vector[j]=id[j][i];
                }
                return eigenvalue;
        }
        /**
        * This method finds the eigenvalues of a symmetric square matrix.
        * The matrix is reduced to tridiagonal form and then the QL method is applied.
        * It is based on the NETLIB algol/fortran procedure tred1/tql1 by Bowdler, Martin, Reinsch and Wilkinson.
        * @param matrix a double symmetric square matrix.
        * @return an array containing the eigenvalues.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public static double[] eigenvalueSolveSymmetric(final DoubleSquareMatrix matrix) throws MaximumIterationsExceededException {
                final int n=matrix.rows();
                final double eigenvalue[]=new double[n];
                final double offdiag[]=new double[n];
                final double array[][]=new double[n][n];
                int i,j;
                for(i=0;i<n;i++) {
                        for(j=0;j<n;j++)
                                array[i][j]=matrix.getElement(i,j);
                }
                reduceSymmetric1_SquareToTridiagonal(array,eigenvalue,offdiag);
                System.arraycopy(offdiag,1,offdiag,0,n-1);
                offdiag[n-1]=0.0;
                eigenvalueSolveSymmetricTridiagonalMatrix(eigenvalue,offdiag);
                return eigenvalue;
        }
        /**
        * This method finds the eigenvalues and eigenvectors of a symmetric square matrix.
        * The matrix is reduced to tridiagonal form and then the QL method is applied.
        * It is based on the NETLIB algol/fortran procedure tred2/tql2 by Bowdler, Martin, Reinsch and Wilkinson.
        * @param matrix a double symmetric square matrix.
        * @param eigenvector an empty array of double vectors to hold the eigenvectors.
        * All eigenvectors will be orthogonal.
        * @return an array containing the eigenvalues.
        * @exception MaximumIterationsExceededException If it takes more than 50 iterations to determine an eigenvalue.
        */
        public static double[] eigenSolveSymmetric(final DoubleSquareMatrix matrix,final DoubleVector eigenvector[]) throws MaximumIterationsExceededException {
                final int n=matrix.rows();
                final double eigenvalue[]=new double[n];
                final double offdiag[]=new double[n];
                final double transf[][]=new double[n][n];
                int i,j;
                for(i=0;i<n;i++) {
                        for(j=0;j<n;j++)
                                transf[i][j]=matrix.getElement(i,j);
                }
                reduceSymmetric2_SquareToTridiagonal(transf,eigenvalue,offdiag);
                System.arraycopy(offdiag,1,offdiag,0,n-1);
                offdiag[n-1]=0.0;
                eigenSolveSymmetricTridiagonalMatrix(eigenvalue,offdiag,transf);
                for(i=0;i<n;i++) {
                        eigenvector[i]=new DoubleVector(n);
                        for(j=0;j<n;j++)
                                eigenvector[i].vector[j]=transf[j][i];
                }
                return eigenvalue;
        }


        /**
        * Internal NETLIB tql1 routine.
        * @param diag output eigenvalues.
        * @author Richard Cannings
        */
        private static void eigenvalueSolveSymmetricTridiagonalMatrix(final double diag[],final double offdiag[]) throws MaximumIterationsExceededException {
                final int n=diag.length;
                final int nm1=n-1;
                int m,l,iteration,i,k;
                double s,r,p,g,f,dd,c,b;
                for(l=0;l<n;l++) {
                        iteration=0;
                        do {
                                for(m=l;m<nm1;m++) {
                                        dd=Math.abs(diag[m])+Math.abs(diag[m+1]);
                                        if(Math.abs(offdiag[m])+dd==dd)
                                                break;
                                }
                                if(m!=l) {
                                        if(iteration++==50)
                                                throw new MaximumIterationsExceededException("No convergence after 50 iterations.");
                                        g=(diag[l+1]-diag[l])/(2.0*offdiag[l]);
                                        r=Math.sqrt(g*g+1.0);
                                        g=diag[m]-diag[l]+offdiag[l]/(g+(g<0.0?-Math.abs(r):Math.abs(r)));
                                        s=c=1.0;
                                        p=0.0;
                                        for(i=m-1;i>=l;i--) {
                                                f=s*offdiag[i];
                                                b=c*offdiag[i];
                                                if(Math.abs(f)>=Math.abs(g)) {
                                                        c=g/f;
                                                        r=Math.sqrt(c*c+1.0);
                                                        offdiag[i+1]=f*r;
                                                        s=1/r;
                                                        c*=s;
                                                } else {
                                                        s=f/g;
                                                        r=Math.sqrt(s*s+1.0);
                                                        offdiag[i+1]=g*r;
                                                        c=1/r;
                                                        s*=c;
                                                }
                                                g=diag[i+1]-p;
                                                r=(diag[i]-g)*s+2.0*c*b;
                                                p=s*r;
                                                diag[i+1]=g+p;
                                                g=c*r-b;
                                        }
                                        diag[l]=diag[l]-p;
                                        offdiag[l]=g;
                                        offdiag[m]=0.0;
                                }
                        } while(m!=l);
                }
        }
        /**
        * Internal NETLIB tred1 routine.
        * @author Richard Cannings
        */
        private static void reduceSymmetric1_SquareToTridiagonal(final double matrix[][],final double diag[],final double offdiag[]) {
                final int n=diag.length;
                int i,j,k,l;
                double f,g,h,hh,scale;
                for(i=n-1;i>0;i--) {
                        l=i-1;
                        h=scale=0.0;
                        if(l>0) {
                                for(k=0;k<=l;k++)
                                        scale+=Math.abs(matrix[i][k]);
                                if(scale==0.0)
                                        offdiag[i]=matrix[i][l];
                                else {
                                        for(k=0;k<=l;k++) {
                                                matrix[i][k]/=scale;
                                                h+=matrix[i][k]*matrix[i][k];
                                        }
                                        f=matrix[i][l];
                                        g=(f>=0.0?-Math.sqrt(h):Math.sqrt(h));
                                        offdiag[i]=scale*g;
                                        h-=f*g;
                                        matrix[i][l]=f-g;
                                        f=0.0;
                                        for(j=0;j<=l;j++) {
                                                g=0.0;
                                                for(k=0;k<=j;k++)
                                                        g+=matrix[j][k]*matrix[i][k];
                                                for(k=j+1;k<=l;k++)
                                                        g+=matrix[k][j]*matrix[i][k];
                                                offdiag[j]=g/h;
                                                f+=offdiag[j]*matrix[i][j];
                                        }
                                        hh=f/(h+h);
                                        for(j=0;j<=l;j++) {
                                                f=matrix[i][j];
                                                offdiag[j]=g=offdiag[j]-hh*f;
                                                for(k=0;k<=j;k++)
                                                        matrix[j][k]-=f*offdiag[k]+g*matrix[i][k];
                                        }
                                }
                        } else
                                offdiag[i]=matrix[i][l];
                        diag[i]=h;
                }
                offdiag[0]=0.0;
                for(i=0;i<n;i++)
                        diag[i]=matrix[i][i];
        }
        /**
        * Internal NETLIB tql2 routine.
        * @param diag output eigenvalues.
        * @param transf output eigenvectors.
        * @author Richard Cannings
        */
        private static void eigenSolveSymmetricTridiagonalMatrix(final double diag[],final double offdiag[],final double transf[][]) throws MaximumIterationsExceededException {
                final int n=diag.length;
                final int nm1=n-1;
                int m,l,iteration,i,k;
                double s,r,p,g,f,dd,c,b;
                for(l=0;l<n;l++) {
                        iteration=0;
                        do {
                                for(m=l;m<nm1;m++) {
                                        dd=Math.abs(diag[m])+Math.abs(diag[m+1]);
                                        if(Math.abs(offdiag[m])+dd==dd)
                                                break;
                                }
                                if(m!=l) {
                                        if(iteration++==50)
                                                throw new MaximumIterationsExceededException("No convergence after 50 iterations.");
                                        g=(diag[l+1]-diag[l])/(2.0*offdiag[l]);
                                        r=Math.sqrt(g*g+1.0);
                                        g=diag[m]-diag[l]+offdiag[l]/(g+(g<0.0?-Math.abs(r):Math.abs(r)));
                                        s=c=1.0;
                                        p=0.0;
                                        for(i=m-1;i>=l;i--) {
                                                f=s*offdiag[i];
                                                b=c*offdiag[i];
                                                if(Math.abs(f)>=Math.abs(g)) {
                                                        c=g/f;
                                                        r=Math.sqrt(c*c+1.0);
                                                        offdiag[i+1]=f*r;
                                                        s=1/r;
                                                        c*=s;
                                                } else {
                                                        s=f/g;
                                                        r=Math.sqrt(s*s+1.0);
                                                        offdiag[i+1]=g*r;
                                                        c=1/r;
                                                        s*=c;
                                                }
                                                g=diag[i+1]-p;
                                                r=(diag[i]-g)*s+2.0*c*b;
                                                p=s*r;
                                                diag[i+1]=g+p;
                                                g=c*r-b;
                                                for(k=0;k<n;k++) {
                                                        f=transf[k][i+1];
                                                        transf[k][i+1]=s*transf[k][i]+c*f;
                                                        transf[k][i]=c*transf[k][i]-s*f;
                                                }
                                        }
                                        diag[l]=diag[l]-p;
                                        offdiag[l]=g;
                                        offdiag[m]=0.0;
                                }
                        } while(m!=l);
                }
        }
        /**
        * Internal NETLIB tred2 routine.
        * @param matrix output orthogonal transformations.
        * @author Richard Cannings
        */
        private static void reduceSymmetric2_SquareToTridiagonal(final double matrix[][],final double diag[],final double offdiag[]) {
                final int n=diag.length;
                int i,j,k,l;
                double f,g,h,hh,scale;
                for(i=n-1;i>0;i--) {
                        l=i-1;
                        h=scale=0.0;
                        if(l>0) {
                                for(k=0;k<=l;k++)
                                        scale+=Math.abs(matrix[i][k]);
                                if(scale==0.0)
                                        offdiag[i]=matrix[i][l];
                                else {
                                        for(k=0;k<=l;k++) {
                                                matrix[i][k]/=scale;
                                                h+=matrix[i][k]*matrix[i][k];
                                        }
                                        f=matrix[i][l];
                                        g=(f>=0.0?-Math.sqrt(h):Math.sqrt(h));
                                        offdiag[i]=scale*g;
                                        h-=f*g;
                                        matrix[i][l]=f-g;
                                        f=0.0;
                                        for(j=0;j<=l;j++) {
                                                matrix[j][i]=matrix[i][j]/h;
                                                g=0.0;
                                                for(k=0;k<=j;k++)
                                                        g+=matrix[j][k]*matrix[i][k];
                                                for(k=j+1;k<=l;k++)
                                                        g+=matrix[k][j]*matrix[i][k];
                                                offdiag[j]=g/h;
                                                f+=offdiag[j]*matrix[i][j];
                                        }
                                        hh=f/(h+h);
                                        for(j=0;j<=l;j++) {
                                                f=matrix[i][j];
                                                offdiag[j]=g=offdiag[j]-hh*f;
                                                for(k=0;k<=j;k++)
                                                        matrix[j][k]-=f*offdiag[k]+g*matrix[i][k];
                                        }
                                }
                        } else
                                offdiag[i]=matrix[i][l];
                        diag[i]=h;
                }
                diag[0]=offdiag[0]=0.0;
                for(i=0;i<n;i++) {
                        l=i-1;
                        if(diag[i]!=0.0) {
                                for(j=0;j<=l;j++) {
                                        g=0.0;
                                        for(k=0;k<=l;k++)
                                                g+=matrix[i][k]*matrix[k][j];
                                        for(k=0;k<=l;k++)
                                                matrix[k][j]-=g*matrix[k][i];
                                }
                        }
                        diag[i]=matrix[i][i];
                        matrix[i][i]=1.0;
                        for(j=0;j<=l;j++)
                                matrix[j][i]=matrix[i][j]=0.0;
                }
        }
}

