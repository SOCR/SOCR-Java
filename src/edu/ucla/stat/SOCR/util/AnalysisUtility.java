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

import java.util.*;
import java.math.BigInteger;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
//import edu.ucla.stat.SOCR.distributions.ContinuousUniformDistribution;
import edu.ucla.stat.SOCR.distributions.BetaDistribution;

import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.data.DataCase;
import edu.ucla.stat.SOCR.analyses.result.LinearModelResult;


public class AnalysisUtility {
	public final static double PRECISION_TOLERANCE = 0.000000000000001;
	public final static String closeToZero = " < 1E-15";
	public final static double HIGH_CORRELATION = 1 - PRECISION_TOLERANCE;
	public final static int NUMBER_OF_DUMMY_DIGITS = 100;

	private boolean wholeRowIsZero = false;

	/*******************************************************************/
	/********************* Summary Statistics **************************/
	/*******************************************************************/
	public static double sum(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double total = 0;
		for (int i = 0; i < sampleSize; i++) {

			total = total + data[i];
		}

		return total;

	}

	public static BigInteger product(BigInteger[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		BigInteger product = new BigInteger(1 + "");
		for (int i = 0; i < sampleSize; i++) {
			//System.out.println("BigInteger data[i] = " + data[i]);

			product = product.multiply(data[i]);
		}

		return product;

	}

	public static double product(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double product = 1;
		for (int i = 0; i < sampleSize; i++) {

			product = product * data[i];
		}

		return product;

	}

	public static double mean(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double total = 0;
		for (int i = 0; i < sampleSize; i++) {

			total = total + data[i];
		}

		return total/sampleSize;

	}

	public static double[] diff(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double mean = mean(data);
		double[] diff = new double[sampleSize];
		for (int i = 0; i < sampleSize; i++) {
			diff[i] = data[i] - mean;
			////////System.out.println(diff[i]);
		}
		return diff;

	}
	public static double sumOfSquares(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double mean = mean(data);
		//////////System.out.println("mean(data) = " + mean(data));
		double sum = 0;
		for (int i = 0; i < sampleSize; i++) {
			sum = sum + (data[i] - mean) * (data[i] - mean) ;

		}
		return sum;

	}
	public static double sampleVariance(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		return sumOfSquares(data) / (sampleSize - 1);
	}

	public static double variance(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		return sumOfSquares(data) / (sampleSize);
	}

	public static double meanSquaredError(double[] data, int degreesFreedom) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");

		////////System.out.println("sumOfSquares(data) = " + sumOfSquares(data));
		return sumOfSquares(data) / degreesFreedom;
	}

	public static double sampleCovariance(double[] dataX, double[] dataY) throws DataIsEmptyException {
		int sampleSizeX = dataX.length;
		int sampleSizeY = dataY.length;
		if ((sampleSizeX <= 0) || (sampleSizeY <= 0) || (sampleSizeX != sampleSizeY))
			throw new DataIsEmptyException("There is no data or uneven X and Y.");
		double meanX = mean(dataX);
		double meanY = mean(dataY);
		double sum = 0;
		for (int i = 0; i < sampleSizeX; i++) {
			sum = sum + (dataX[i] - meanX) * (dataY[i] - meanX) ;

		}
		return sum/(sampleSizeX - 1) ;

	}
	public static double sampleCorrelation(double[] dataX, double[] dataY) throws DataIsEmptyException {
		int sampleSizeX = dataX.length;
		int sampleSizeY = dataY.length;
		if ((sampleSizeX <= 0) || (sampleSizeY <= 0) || (sampleSizeX != sampleSizeY))
			throw new DataIsEmptyException("There is no data or uneven X and Y.");
		return sampleCovariance(dataX, dataY) / ( Math.sqrt(sampleVariance(dataX)) * Math.sqrt(sampleVariance(dataY)) );

	}

	/*******************************************************************/
	/***************** Residual, Quantiles Etc. ************************/
	/*******************************************************************/

	public static double[] getNormalQuantile(int m) { // m = length of data
		// the way of calculation is adapted from R's ppoints & qnorm
		NormalDistribution nd = new NormalDistribution(0, 1);

		double a = (m <= 10)?(3/8):(.5);
		double[] answer = new double[m];
		for (int i = 0; i < m; i++) {
			answer[i] = nd.getQuantile(((i+1) - a)/(m + (1-a)-a));
		}
		return answer;
	}

	public static double[] getStandardNormalQuantile(int m) {
		return getNormalQuantile(m);
	}

	public static double[] getGenericNormalQuantile(double mean, double sd,int m) { // m = length of data
		// the way of calculation is adapted from R's ppoints & qnorm
		NormalDistribution nd = new NormalDistribution(mean, sd * sd);

		double a = (m <= 10)?(3/8):(.5);
		double[] answer = new double[m];
		for (int i = 0; i < m; i++) {
			answer[i] = nd.getQuantile(((i+1) - a)/(m + (1-a)-a));
		}
		return answer;
	}

	/* added annieche 20060328. */
	public static double[] getQuantileArray(double[] input) {
		/*
		the way of calculation is adapted from NIST. http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/quantile.htm
		1. use r.
			X are the observations sorted in ascending order
			NI1 = INT(q*(n+1))
			NI2 = NI1 + 1
			r = q*(n+1) - INT(q*(n+1))
		2. Herrell-Davis estimate.
			Sort the X in ascending order.

			A = (n+1)*q - 1

			B = (n+1)*(1 - q) - 1

			Wi = BETCDF(i/n,A,B) - BETCDF((i-1)/n,A,B) where BETCDF is the beta cumulative distribution function with shape parameters A and B.


			Note: The computations for A and B were modified 2/2003 to:

			A = (n+1)*q
			B = (n+1)*(1 - q)

		*/

		QSortAlgorithm quick = new QSortAlgorithm();
		int total = 100;
		double factor = 0.01;
		double n = input.length;
		double ni1 = 0, ni2 = 0, r = 0;
		double a = 0, b = 0;
		double temp = 0;
		double[] result = new double[total+1];

		double[] w = new double[(int)n];
		BetaDistribution betad = null;

		/**
		for (int i = 0; i < n; i++) {
			if (input[i] <0 )
				return null;
		}
		**/

		double qPercent = 0;
		result[0] = input[0]; // min
		result[total] = input[(int)n-1];	// max

		try {
			quick.sort(input); // input is now a new sorted double array.

			for (int q = 1; q < total; q++) {
				qPercent = q * factor;
				a = (n+1)*qPercent - 1;
				b = (n+1)*(1 - qPercent) - 1;
				////////System.out.println("  a = " + a + ";                        b = " + b );
				temp = 0;
				for (int i = 1; i < n; i++) {
					betad = new BetaDistribution(a, b);

					w[i] = betad.getCDF((double)(i+1)/n) - betad.getCDF( (double) (i)/n );
					temp = temp + w[i] * input[i];


				}
				result[q] = temp;
				///////System.out.println(q + "th quantile = " + result[q]);
				temp = 0;
			}
		} catch (Exception e) { // from QSortAlgorithm
			////////System.out.println(e.toString());
		}


		/*
		for (int q = 0; q < total; q++) {
			qPercent = q * factor;
			//////System.out.println(q + " qPercent  = " + qPercent);
			ni1 = (int) ( qPercent * (n + 1) );

			ni2 = ni1 + 1;
			r = qPercent *(n + 1) - (int)(qPercent *(n + 1));
			//////System.out.println("q = " + q +";  ni1 = " +(int)ni1 + ";  ni2 = " +(int)ni2 + ";  r = " + r);
			try {
				result[q] = (1 - r ) * input[(int)(ni1-1)] + r * input[(int)(ni2-1)] ;
			} catch (Exception e) {
				//////System.out.println(e.toString());
			}
		}
		*/
		////////System.out.println("getQuantileArray:");
		/*for (int q = 0; q < total+1; q++) {
			//////System.out.println((q) + "th quantile = " + result[q]);
		}*/

		return result;
	}

/*
	public static DataCase[] getResidualNormalScores(double[] residual) throws DataIsEmptyException {
		DataCase[] sortedResidual = getSortedRedidual(residual);
		DataCase[] normalScores = (DataCase[])sortedResidual.clone();
		double[] standardizedResidual = getStandardizedResidual(sortedResidual);

		NormalDistribution nd = new NormalDistribution(0, 1);
		for (int i = 0; i < residual.length; i++) {
				// nd.getDensity gives dnorm (density of normal, i.e. the "height" (same as dnorm in R)
				// nd.getCDF gives pnorm (cummulative probablilty, i.e. the "area" to its left (same as pnorm in R)
				normalScores[i].setValue(nd.getQuantile(standardizedResidual[i]));

				////////System.out.println("normal scores["+i+"] = " + normalScores[i].getValue() + "          " + normalScores[i].getIndex());


		}
		return null;
	}
*/

	public static HashMap<String,Object> getResidualNormalQuantiles(double[] residual, int  degreesFreedom) {
		DataCase[] dataCase = getSortedRedidual(residual);
		int[] residualIndex = DataCase.getIndexArray(dataCase);
		double[] sortedResiduals = DataCase.getValueArray(dataCase);
		double[] sortedStandardizedResiduals = null;
		double[] normalQuantiles = getNormalQuantile(residual.length);
		double[] standardizedNormalQuantiles = null;

		HashMap<String,Object> residualMap = new HashMap<String,Object>();
		try {
			sortedStandardizedResiduals = getStandardizedResidual(sortedResiduals, degreesFreedom);
			residualMap.put(LinearModelResult.SORTED_STANDARDIZED_RESIDUALS, sortedStandardizedResiduals);


		} catch (DataIsEmptyException e) {
			////////System.out.println(e.toString());
		}

		residualMap.put(LinearModelResult.SORTED_RESIDUALS, sortedResiduals);
		residualMap.put(LinearModelResult.SORTED_RESIDUALS_INDEX, residualIndex);
		residualMap.put(LinearModelResult.SORTED_NORMAL_QUANTILES, normalQuantiles);
		residualMap.put(LinearModelResult.SORTED_STANDARDIZED_NORMAL_QUANTILES, normalQuantiles);

		return residualMap;
	}


	public static DataCase[] getSortedRedidual(double[] residual) {
		//double[] testArray = new double[] {1,2,3,5,4,2};
		DataCase[] dataCase = DataCase.toDataCaseArray(residual);
		int arrayLength = residual.length;
		QSortAlgorithm quick = new QSortAlgorithm();
		try {
			quick.sort(dataCase);
		} catch (Exception e) {
			////////System.out.println(e.toString());
		}
		for (int i = 0; i < arrayLength; i++) {
			//////////System.out.println(dataCase[i].getValue() + " " + dataCase[i].getIndex());
		}
		return dataCase;

	}
	public static double[] getStandardizedResidual(DataCase[] residual, int df) throws DataIsEmptyException{
		if (residual == null || residual.length == 0) {
				throw new DataIsEmptyException("DataCase array is null or of length 0.");
		}
		double[] residualDouble = new double[residual.length];
		double[] standardizedResidual = new double[residual.length];
		for (int i = 0; i < residual.length; i++) {
			standardizedResidual[i] = residual[i].getValue();
		}
		standardizedResidual = getStandardizedResidual(standardizedResidual, df);


		return standardizedResidual;
	}


/* NOTE:
	1. degreesFreedom = length_of_residual - 2 for SimpleLinearRegression.
	2. degreesFreedom = length_of_residual - 1 - number of independent_variable for MultiLinearRegression.
	3. degreesFreedom = length_of_residual + 1 - level_of_factor for AnovaOneWay.
	4. degreesFreedom = length_of_residual - (level_of_factor_one - 1) * (level_of_factor_one - 2) for AnovaTwoWay.

*/
	public static double[] getStandardizedResidual(double[] residual, int degreesFreedom) throws DataIsEmptyException{
		//double sampleVariance = sampleVariance(residual);
		//double sampleSD = Math.sqrt(sampleVariance);


		double mse = meanSquaredError(residual, degreesFreedom);
		//ystem.out.println("MSE = " + mse + " degreesFreedom = " + degreesFreedom);
		double sd = Math.sqrt(mse);
		////System.

		double[] standardizedResidual = new double[residual.length];
		for (int i = 0; i < residual.length; i++) {
			standardizedResidual[i] = residual[i]/sd;
		}
		return standardizedResidual;
	}


	/*******************************************************************/
	/*********************  Matrix Operations **************************/
	/*******************************************************************/

	// levelFactorTree sorts the factors (names in String) into a Tree.
	public static TreeSet<String> levelFactorTree(String[] x) {
		int length = x.length;
		TreeSet<String> set = new TreeSet<String>();
		//change from i = 1 to i = 0. i = 1 caused problem. (probably typo). annie 20070429.
		for (int i = 0; i < length; i++) {
			//String temp = (x[i]+"").toString();
			set.add(x[i]);
			////System.out.println("x[i] = " + x[i]);
		}
		return set;
	}

	// i = 1... might cause bug?? but this method is not called by any caller.
	public static LinkedHashSet<String> levelFactor(String[] x) {
		//byte level = 0;
		int length = x.length;
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		for (int i = 1; i < length; i++) {
			//String temp = (x[i]+"").toString();
			set.add(x[i]);
		}
		return set;
	}


	public static byte[][] getDummyMatrix(String[] groups) {
		/*System.out.println("AnalysisUtility getDummyMatrix");
		for (int i = 0; i < groups.length; i++) {
			System.out.println("groups[indexFactor] " + groups[i]);

		}*/

		int sampleSize = groups.length;
		TreeSet<String> set = levelFactorTree(groups); // this will sort the factors
		//TreeMap <String, Object>dummyMap = new TreeMap<String, Object>();
		int numberGroups = set.size();
		
	/*	System.out.println("NUMBER OF GROUPS " + numberGroups);
		System.out.println("sampleSize " + sampleSize);
*/
		Iterator<String> iterator = set.iterator();
		String[] setS = new String[numberGroups];
		int index = 0;
		while (iterator.hasNext()) {
			setS[index] = (String) iterator.next();
			//System.out.println("String Factor["+index+"] = "+ setS[index]);
			index ++;

		//	dummyMap.put( (String)(iterator.next()), new byte[sampleSize]);
		}
		
//		use g to store groups in integer
		int[] g = new int[sampleSize];
		for (int i=0; i<sampleSize; i++){
			for (int j=0; j<numberGroups; j++){
				if (groups[i].equals(setS[j])){
					g[i]=j+1;
					break;
				}
			}
		}
		
		// methods for "dummies" -_-
		// do this before I think of a better way to do it.
		byte[][] dummy=null; //= new byte[][];
		//System.out.println("Set S length = " + setS.length);

		//Exception e = new Exception();
	//	e.printStackTrace();
	/*	if (numberGroups<=7){
		System.out.println("Annie's matrix");
		for (int i = 0; i < sampleSize; i++) {
			for (int j = 0; j < dummy[0].length; j++) {
					System.out.print("d[" + i + "][" + j +"] = "+dummy[i][j]+" ");
				}
				System.out.println();
			}
		System.out.println("Annie's matrix-end");
		}*/
		
		//System.out.println("Jenny's matrix");
		
		//System.out.println("sampleSize="+sampleSize+ " numberGroups" +numberGroups);
		
		/* change made Feb 21 2008, caompared with Annie's hard coded dummy Matrix, see commented code above*/	
		dummy = new byte[sampleSize][numberGroups-1];
		for (int i= 0; i< sampleSize; i++) {
			//System.out.print("i="+i+",");
			for (int k=0; k<numberGroups; k++){	
			//	System.out.print("k="+k+",");
				if (groups[i].equalsIgnoreCase(setS[k])){
				//	System.out.print("groups["+i+"]=setS["+k+"]="+groups[i]+" ");
					for (int s=0; s<numberGroups-1; s++){
					//	System.out.println("s="+s);
						if (k==0){
							//System.out.println("setting dummy[" + i + "][" + s +"] =0  since k==0");
							dummy[i][s]=0;
						}
						else{
							//if((Integer.parseInt(groups[i])-s)==2){
							if((g[i]-s)==2){
								dummy[i][s]=1;
							//	System.out.println("setting dummy[" + i + "][" + s +"] =1");
							}
							else {
								dummy[i][s]=0;
								//System.out.println("setting dummy[" + i + "][" + s +"] =0");
							}
						}
					}//s
				}//if
			}//k 
		}//i
    /*	System.out.println("Jenny's matrix");
		for (int i = 0; i < sampleSize; i++) {
			for (int j = 0; j < dummy[0].length; j++) {
					System.out.print("d[" + i + "][" + j +"] = "+dummy[i][j]+" ");
				}
				System.out.println();
			}
		System.out.println("Jenny's matrix-end");
		*/
	
		
		//return dummyMap;
		//return null;
		return dummy;
	}

/* 	added 20050903 to do inverse of a matrix. by annieche
	Gauss-Jordan eleimination was used.
*/

	public static Matrix inverse(Matrix matrix) {
		// matrix is the eliminated matrix from G-J, composed of Identidy and inverse
		matrix = eliminationGaussJordan(matrix);
		int nrow = matrix.rows;
		int ncol = matrix.columns;
		////////System.out.println("ROW = " + nrow);
		////////System.out.println("COL = " + ncol);
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
				////////System.out.println("i = " + i + " j = " + j + "  max_val = " + max_val +  "   max_ind = " + max_ind);

				for (int k = i+1; k < nrow; k++) {
					if (Math.abs(matrix.element[k][j]) > max_val) {
						max_val = Math.abs(matrix.element[k][j]);
						max_ind = k;
					} // end if
				} // end for k
				////////System.out.println("i = " + i + " j = " + j + "  NEW max_val = " + max_val +  "   NEW max_ind = " + max_ind);

				if (max_val != 0) {
					// switch rows
					tempRow = matrix.element[i];
					matrix.element[i] = matrix.element[max_ind];
					matrix.element[max_ind] = tempRow;
					tempRow = null;
					////////System.out.println("After switch " + matrix.element[i][j] + " and " + matrix.element[max_ind][j]);
					tempPivot = matrix.element[max_ind][j];
					// the next divide row i by max_val
					for (int p = 0; p < ncolNew; p++) {
						matrix.element[i][p] = matrix.element[i][p] / max_val;
						//////////System.out.println("After divide " + matrix.element[i][p]);
					} // finish dividing
					tempPivot = matrix.element[i][j];
					if (tempPivot < 0)
						for (int p = 0; p < ncolNew; p++) {								matrix.element[i][p] = -matrix.element[i][p] ;
						//////////System.out.println("After Change sign " + matrix.element[i][p]);
						} // end for p

				//	print(matrix);
				//	////////System.out.println("============================================");
					for (int u = 0; u < nrow; u++) {
						if ( u != i) {
							tempMultiple = matrix.element[u][j];
							for (int v = 0; v < ncolNew; v++) {

								//////////System.out.println("Now j is : " + j + " current entry = " +(matrix.element[i][v]) + " original = " + (matrix.element[i][v]) + " tempMultiple = " + tempMultiple );

								matrix.element[u][v] = matrix.element[u][v] -
								(matrix.element[i][v]) * tempMultiple;

							} // end for v
						} // end if
						//printMatrix(matrix);
					} // end for u
					//printMatrix(matrix);
					i++;
				} // end if max_val not 0

			j++;
		}
		//printMatrix(matrix);
		return matrix;
  	}

	// copy from util.Matrix --print for testing only. method name changed.
	private static void printMatrix(Matrix matrix) {
		////////System.out.println("----- Print the Matrix -----");
		////////System.out.println("----- matrix.rows    = "+ matrix.rows);
		////////System.out.println("----- matrix.columns = " + matrix.columns);

		for (int i = 0; i < matrix.rows; i++ ) {
			////////System.out.println("-----------------");
				for (int j = 0; j < matrix.columns; j++ ) {
					////////System.out.println("MATRIX["+i+"]["+j+"] = " + matrix.element[i][j]);
				}
			}

	}
	/*******************************************************************/
	/********************* Factorials         **************************/
	/*******************************************************************/

	// result copied from someone's web page:
	// http://www.leepoint.net/notes-java/data/numbers/60factorial.html
	// numbers/Factorial.java - Computes factorial
	// Fred Swartz - 2003-11-02


	public static BigInteger factorialBigInt(int input) {
		BigInteger output;
		switch (input) {
			case 0: { output = new BigInteger("1"); break; }
			case 1: { output = new BigInteger("1"); break; }
			case 2: { output = new BigInteger("2"); break; }
			case 3: { output = new BigInteger("6"); break; }
			case 4: { output = new BigInteger("24"); break; }
			case 5: { output = new BigInteger("120"); break; }
			case 6: { output = new BigInteger("720"); break; }
			case 7: { output = new BigInteger("5040"); break; }
			case 8: { output = new BigInteger("40320"); break; }
			case 9: { output = new BigInteger("362880"); break; }
			case 10: { output = new BigInteger("3628800"); break; }
			case 11: { output = new BigInteger("39916800"); break; }
			case 12: { output = new BigInteger("479001600"); break; }
			case 13: { output = new BigInteger("6227020800"); break; }
			case 14: { output = new BigInteger("87178291200"); break; }
			case 15: { output = new BigInteger("1307674368000"); break; }
			case 16: { output = new BigInteger("20922789888000"); break; }
			case 17: { output = new BigInteger("355687428096000"); break; }
			case 18: { output = new BigInteger("6402373705728000"); break; }
			case 19: { output = new BigInteger("121645100408832000"); break; }
			//case 20: { output = new BigInteger("2432902008176640000"); break; }
			default: { output = (new BigInteger(input+"")).multiply(factorialBigInt(input - 1)); }
		}
		return output;
	}

	public static int factorial(int input) {
		int output;
		switch (input) {
			case 0: { output = 1; break; }
			case 1: { output = 1; break; }
			case 2: { output = 2; break; }
			case 3: { output = 6; break; }
			case 4: { output = 24; break; }
			case 5: { output = 120; break; }
			case 6: { output = 720; break; }
			case 7: { output = 5040; break; }
			case 8: { output = 40320; break; }
			case 9: { output = 362880; break; }
			case 10: { output = 3628800; break; }
			case 11: { output = 39916800; break; }
			case 12: { output = 479001600; break; }
			case 13: { output = 13 * 479001600; break; }
			case 14: { output = 14 * 13 * 479001600; break; }
			case 15: { output = 15 * 14 * 13 * 479001600; break; }
			case 16: { output = 16 * 15 * 14 * 13 * 479001600; break; }
			case 17: { output = 17 * 16 * 15 * 14 * 13 * 479001600; break; }

			default: { output = 0; }
		}
		return output;
	}

	public static double sumPossitiveIntegerSequencePartial(int start, int end) {
		double largeSum = .5 * end * (end + 1);
		double smallSum = .5 * start * (start + 1);
		////System.out.println("sumPossitiveIntegerSequencePartial start = " + start);
		////System.out.println("sumPossitiveIntegerSequencePartial end = " + end);

		return largeSum - smallSum;

	}
	public static double sumPossitiveIntegerSequenceFromOne(int end) {

		return .5 * end * (end + 1);

	}
/*
        //-- BigInteger solution.
        BigInteger n = BigInteger.ONE;
        for (int i=1; i<=20; i++) {
            n = n.multiply(BigInteger.valueOf(i));
            ////////System.out.println(i + "! = " + n);
        }

        //-- int solution (BAD IDEA BECAUSE ONLY WORKS TO 12).
        int fact = 1;
        for (int i=1; i<=20; i++) {
            fact = fact * i;
            ////////System.out.println(i + "! = " + fact);
        }
    }
}
*/

	/*******************************************************************/
	/*********** Precesion Determination Function **********************/
	/*******************************************************************/

	public static String enhanceSmallNumber(double original) {
		if (original < PRECISION_TOLERANCE) {
			return closeToZero;
		}
		return original + "";
	}

	public static boolean dataColinear(double[] x, double[] y) {
		boolean result = false;
		try {
			if ( Math.abs(sampleCorrelation(x,y)) > HIGH_CORRELATION) {
				////////System.out.println("correlation = " + sampleCorrelation(x,y));
				result = true;
			}
		} catch (DataIsEmptyException e) {
			////////System.out.println(e.toString());
		}
		return result;

	}
/* Test program for dummy binary
	public static void main(String args[]) {
		//String[] group = {"4","4","1","1","1","1","1","1", "2","2","2","2","2","2","2","2", "3","3","3","3","3", "5", "5"};
		//String[] group = {"1","1","1","1","1","1", "2","2","2","2","2","2","2","2"};
		String[] group = {"1","1","1", "2","2","2"};
		int sampleSize = group.length;
		/*
		TreeMap treeMap = getDummyMatrix(group);
		Set keys = treeMap.keySet();
		Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {
			////////System.out.println(iterator.next());
		}*/
/*
		double[][] dummy = getDummyMatrix(group);
		int dummyLength = dummy[0].length;
		//////////System.out.println("dummy.length = " + dummyLength + " sample size = " + sampleSize);
		for (int i = 0; i < sampleSize; i++) {
			for (int j = 0; j < dummyLength; j++) {
				////////System.out.println("dummy[" + i + "][" + j +"] = "+dummy[i][j]);
			}
		}
	}
*/

	//public static void main(String args[]) {
	/* test only
		double[] testArray = new double[] {1,2,3,5,4,2};
		DataCase[] dataCase = DataCase.toDataCaseArray(testArray);
		int arrayLength = testArray.length;
		QSortAlgorithm quick = new QSortAlgorithm();
		try {
			quick.sort(dataCase);
		} catch (Exception e) {
			////////System.out.println("darn!!");
		}
		for (int i = 0; i < arrayLength; i++) {
			////////System.out.println(dataCase[i].getValue() + " " + dataCase[i].getIndex());
		}
	*/
	/*
          double[] testArray = new double[] {-0.4007316, -0.9914651, -13.5968299, 12.5992684, -4.8148750, -4.8075594, 12.0036578,  8.4080471, 11.5968299, 4.0134119, -7.2021947, 4.3861004, 4.8026824, -2.8026824, -1.8099980, -18.5870758, -0.1997561, 0.6041455, 5.6041455, -8.8051209};
          for (int i = 0; i < testArray.length; i++) {
			////////System.out.println((i +1) + " " + testArray[i] );
		}
          ////////System.out.println("Length = " + testArray.length);
		try {
          	DataCase[] sortedResidual = getResidualNormalScores(testArray);
			for (int i = 0; i < sortedResidual.length; i++) {
				////////System.out.println(sortedResidual[i].getValue() + " " + sortedResidual[i].getIndex());
			}

			getStandardizedResidual(testArray);
		} catch (DataIsEmptyException e) {
		}
	*/
	/* test standardized, sorted, residuals.
		double[] testArray = //new double[] {-14.0, -4.0, 6.0, -4.0, 16.0, -17.4, -7.4, 2.6, 0.6, 21.6};
		new double[] {-4.4428706,3.9046270,2.8196412,4.5146364,-1.8328612,0.6846081, 2.5146364,0.7271010,1.2946176,-1.7903683,0.3371105,3.5571294,-4.4003777,-2.3578848,-3.4428706,-0.1378659,0.3371105,1.9896128,-3.1378659,-1.1378659};

		DataCase[] sortedResidual = getSortedRedidual(testArray);
		for (int i = 0; i < sortedResidual.length; i++) {
				////////System.out.println(sortedResidual[i].getValue());
		}
		////////System.out.println("");
		double[] standardizedResid = null;

		try {
			standardizedResid = getStandardizedResidual(sortedResidual, testArray.length-1);

			double[] answer = getNormalQuantile(20);
			for (int i = 0; i < standardizedResid.length; i++) {
					//////////System.out.println(standardizedResid[i]);
					////////System.out.println(answer[i]);
			}


		} catch (Exception e) {
		}


	*/
	// test quantiles
		//double[] testArray = {-14.0, -4.0, 6.0, -4.0, 16.0, -17.4, -7.4, 2.6, 0.6, 21.6};
		double[] testArray = {1,3,4,6,10,13,15,20,29,30};
		//double[] testArray = new double[20];//{-14.0, -4.0, 6.0, -4.0, 16.0, -17.4, -7.4, 2.6, 0.6, 21.6};
		//for (int i = 0; i < testArray.length; i ++) {
			//testArray[i] = i+1;
		//}
		////////System.out.println("testArray.length = " + testArray.length);

		/****************** CODE EXAMPLE ******************/
	//	double[] answer = getQuantileArray(testArray);


		////////System.out.println("answer = " + answer);
	//}
	public static String truncateDigits(double input, int numberDigits) {
		int indexDot = -1;
		String integerPart = null, decimalPart = null, dot = ".";
		String wholePart = null; String result = null;
		wholePart = input + "";
		indexDot = (wholePart).indexOf(dot);
		if (indexDot>=0) {
			integerPart = wholePart.substring(0, indexDot);
			try {
				decimalPart = wholePart.substring(indexDot + 1, indexDot + 1 + numberDigits);
			} catch (Exception e) {
				decimalPart = wholePart.substring(indexDot + 1, wholePart.length());

				//decimalPart = wholePart.substring(indexDot + 1, );
			}
			result = (integerPart + dot + decimalPart).trim();
		} else {
			result = (input + "").trim();
		}
		return result;
	}
	public static String[] truncateDigits(double[] input, int numberDigits) {
		String[] result = new String[input.length];
		int indexDot = -1;
		String integerPart = null, decimalPart = null, dot = ".";
		String wholePart = null;
		//int maxNumberDigit = 0;
		for (int i = 0; i < input.length; i++) {
			result[i] = truncateDigits(input[i], numberDigits);
		}
		return result;
	}

	public static String[][] truncateDigits(double[][] input, int numberDigits) {
		String[][] result = new String[input.length][];
		int indexDot = -1;
		String integerPart = null, decimalPart = null, dot = ".";
		String wholePart = null;
		//int maxNumberDigit = 0;
		for (int i = 0; i < input.length; i++) {
			result[i] = new String[input[i].length];
			for (int j = 0; j < input[i].length; j++) {

				//////System.out.println("input["+i+"]["+j+"] = " + input[i][j]);

				wholePart = input[i][j] + "";
				indexDot = (wholePart).indexOf(dot);
				if (indexDot>=0) {
					integerPart = wholePart.substring(0, indexDot);
					try {
						decimalPart = wholePart.substring(indexDot + 1, indexDot + 1 + numberDigits);
					} catch (Exception e) {
						decimalPart = wholePart.substring(indexDot + 1, wholePart.length());

						//decimalPart = wholePart.substring(indexDot + 1, );
					}
					result[i][j] = (integerPart + dot + decimalPart).trim();
				} else {
					result[i][j] = (input[i][j] + "").trim();
				}
			}
		}
		return result;
	}

	public static double getNormalCriticalPoint(double alpha) { // unsigned, all positive.
		// alpha = significant level for the Normal distribution.
		// the numbers are from R code qnorm(1 - alpha/2)
		/*	R code:
			> format(qnorm(1 - 0.1/2), digits = 15)
			[1] "1.64485362695147"
			> format(qnorm(1 - 0.05/2), digits = 15)
			[1] "1.95996398454005"
			> format(qnorm(1 - 0.01/2), digits = 15)
			[1] "2.5758293035489"
			> format(qnorm(1 - 0.001/2), digits = 15)
			[1] "3.29052673149193"
		*/

		double cp = 0;
		if (alpha == 0.1) {
			cp = 1.64485362695147;
		} else if (alpha == 0.05) {
			cp = 1.95996398454005;
		} else if (alpha == 0.01) {
			cp = 2.5758293035489;
		} else if (alpha == 0.001) {
			cp = 3.29052673149193;
		}

		return cp;
	}
	public static double getStudentTCriticalPoint(double alpha, int df) {
		return 2; // this needs to be written someday.
	}

	   /**
	    * Computes a root of the function using the Brent-Dekker method. 
	    * @param a left endpoint of initial interval
	    * @param b right endpoint of initial interval
	    * @param f the function which is evaluated
	    * @param tol accuracy goal
	    * @return double x, the root, where f(x)=0
	    */
	public static double functionRootBrentDekker (
			   double a, double b, MathFunction f, double tol) {
	      final double EPS = 0.5E-15;
	      final int MAXITER = 100;     // Maximum number of iterations
	      double c, d, e;
	      double fa, fb, fc;

	      // Special case I = [b, a]
	      if (b < a) {
	    	 double ctemp = a;   
	         a = b;   b = ctemp;
	      }

	      // Initialization
	      fa = f.evaluate (a);
	      fb = f.evaluate (b);
	      c = a;
	      fc = fa;
	      d = e = b - a;
	      tol += 2*EPS;    // in case tol is too small

	      if (Math.abs(fc) < Math.abs(fb)) {
	            a = b; b = c; c = a;
	            fa = fb; fb = fc; fc = fa;
	      }

	      for (int i = 0; i < MAXITER; i++)
	      {
	         double s, p, q, r;
	         double tol1 = tol + 4.0 * tol * Math.abs (b);
	         double xm = 0.5 * (c - b);

	         if ((Math.abs (fb) == 0.0) || (Math.abs (xm) <= tol1))
	            return b;
	         
	         if ((Math.abs (e) >= tol1) && (Math.abs (fa) > Math.abs (fb)))
	         {
	            if (a != c)
	            {
	               // Inverse quadratic interpolation
	               q = fa / fc;
	               r = fb / fc;
	               s = fb / fa;
	               p = s * (2.0 * xm * q * (q - r) - (b - a) * (r - 1.0));
	               q = (q - 1.0) * (r - 1.0) * (s - 1.0);
	            }
	            else
	            {
	               // Linear interpolation
	               s = fb / fa;
	               p = 2.0 * xm * s;
	               q = 1.0 - s;
	            }

	            // Adjust signs
	            if (p > 0.0)
	               q = -q;
	            p = Math.abs(p);

	            // Is interpolation acceptable ?
	            if (((2.0 * p) >= (3.0 * xm * q - Math.abs (tol1 * q))) || 
	            		(p >= Math.abs (0.5 * e * q))) {
	            	d = xm;
	            	e = d;
	            }
	            else {
		           e = d;
	               d = p / q;
	            }
	         }
	         else {
	            // Bisection necessary
	            d = xm;
	            e = d;
	         }

	         a = b;
	         fa = fb;
	         if (Math.abs (d) > tol1)
	            b += d;
	         else if (xm < 0.0)
	            b -= tol1;
	         else
	            b += tol1;
	         fb = f.evaluate (b);
	         if ((fb * (fc / Math.abs (fc))) > 0.0)
	         {
	            c = a;
	            fc = fa;
	            d = e = b - a;
	         }
	         else
	         {
	            a = b; b = c; c = a;
	            fa = fb; fb = fc; fc = fa;
	         }
	      }

	      return b;
	}

	public static void main(String[] args) {
		int input = 100;

		System.out.println("factorialBigInt of " + input + " = " + factorialBigInt(input));
	}
}