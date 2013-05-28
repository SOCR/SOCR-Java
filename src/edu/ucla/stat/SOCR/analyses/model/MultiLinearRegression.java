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
package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;


public class MultiLinearRegression implements Analysis {
	private final static int NUMBER_OF_Y_VAR = 1;
	private final static String INTERCEPT = "INTERCEPT";
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;

	private String type = "MultiLinearRegression";
	private ArrayList<String> varNameList = new ArrayList<String>();
	private String[] varList = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		//////////////System.out.println("In MultiLinearRegression analyze ");

		if (analysisType != AnalysisType.MULTI_LINEAR_REGRESSION)
			throw new WrongAnalysisException();
		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		ArrayList<Object> x = new ArrayList<Object>();
		double y[] = null;
		int xIndex = 0;
		varNameList.add(0, INTERCEPT);
		Column xColumn = null;
		while (iterator.hasNext()) {

			keys = (String) iterator.next();
			//////////////System.out.println("MultiLinearRegression keys "+keys);
			try {
				Class cls = keys.getClass();
				//////////////System.out.println(cls.getName());
			} catch (Exception e) {
				//////////////System.out.println("MultiLinearRegression Exception = " + e);
			}
			xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			double xVector[] = xColumn.getDoubleArray();
			//////////////System.out.println("MultiLinearRegression  xVector.length = "+xVector.length );

			for (int i = 0; i < xVector.length; i++) {
				//////////////System.out.println("MultiLinearRegression xVector["+i+"] = " +xVector[i]);
			}
			// x is an ArrayList, key is index, Object is an x vector
			// if you have x1 and x2, then key = 1 points to x1, key = 2 points to x2.
			x.add(xIndex, xVector);
			xIndex++;
			varNameList.add(xIndex, keys);
		}
		//////////////System.out.println("MultiLinearRegression xIndex = "+xIndex);

		keySet = yMap.keySet();
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				//////////////System.out.println("MultiLinearRegression cls.getName = " +cls.getName());
			} catch (Exception e) {}
		}
		Column yColumn = (Column) yMap.get(keys);
		////////////System.out.println("MultiLinearRegression " + yColumn.getDataType());
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException("\ny data type MUST be QUANTITATIVE but the input is of type " + yDataType);
		}
		y = yColumn.getDoubleArray();
		//////////////System.out.println("MultiLinearRegression  y.length = "+y.length );

		for (int i = 0; i < y.length; i++) {
			//////////////System.out.println("MultiLinearRegression y = " + y[i]);
		}
		varList = new String[varNameList.size()];
		for (int i = 0; i < varNameList.size(); i++) {
			//////////////System.out.println("MultiLinearRegression varNameList["+i+"] = " + (String)varNameList.get(i));
			varList[i] = (String)varNameList.get(i);
		}

		//////////////System.out.println("In Multi Linear Regression result = " + regression(x, y));
		return regression(x, y);
		//return null;
	}

	private MultiLinearRegressionResult regression(ArrayList<Object> x, double[] y) 
			throws DataIsEmptyException {
		//////////////System.out.println("MultiLinearRegression start method regression ");
		HashMap<String,Object> texture = new HashMap<String,Object>();

		MultiLinearRegressionResult result = new MultiLinearRegressionResult(texture);

		int sampleSize = ((double[])(x.get(0))).length;
		//////////////System.out.println("MultiLinearRegression sampleSize = " + sampleSize);

		int numberOfX = x.size();
		int numberColumns = numberOfX + 1;
		//System.out.println("MultiLinearRegression numberOfX = " + numberOfX);

		double[][] varX = new double[sampleSize][numberColumns];
		double[][] vectorY = new double[sampleSize][NUMBER_OF_Y_VAR];
		for (int i = 0; i < sampleSize; i++) {
			varX[i][0] = 1; // when col = 0, first column is all one.
			vectorY[i][0] = y[i];
			//////////////System.out.println("y["+i+"] = "+y[i]);
		}

		double[] meanX = new double[numberColumns];
		meanX[0] = 1;

		for (int j = 1; j < numberColumns; j++) {
			double[] rowX = (double[])(x.get(j-1));
			for (int i = 0; i < sampleSize; i++) {
				double entryX = rowX[i]; // the ArraList's ith Object is a double array
				varX[i][j] = entryX;  // the i,jth entry
			}
			meanX[j] = AnalysisUtility.mean(rowX);
			//////////////System.out.println("mean X [" + j+ "]= " +meanX[j]);
		}

		Matrix matrixY = new Matrix(sampleSize, 1, vectorY);

		Matrix matrixX = new Matrix(sampleSize, numberColumns, varX); 	// sampleSize by numberColumns
		Matrix xTranspose = matrixX.transpose(); 					// numberColumns by sampleSize
		Matrix identityMatrix = new Matrix(numberColumns, numberColumns, 'I');

		for (int i = 0; i < numberColumns; i++ ) {
			for (int j = 0; j < numberColumns; j++ ) {
				if (i == j)
					identityMatrix.element[i][j] = 1;
			}
		}


		Matrix beta = new Matrix(numberColumns, 1);
		Matrix productXX = new Matrix(numberColumns, numberColumns);
		productXX = Matrix.multiply(xTranspose, matrixX);


		for (int i = 0; i < numberColumns; i++ ) {
			for (int j = 0; j < numberColumns; j++ ) {
				//////////////System.out.println("productXX["+i+"]["+j+"] = " + productXX.element[i][j]);
			}
		}

		//Matrix top = new Matrix(10);
		//Matrix one = new Matrix(1);
		///Matrix product = Matrix.divide(top, one);
		////////////////System.out.println("product  " + product.element[0][0]);

		Matrix inverseProductXX = new Matrix(numberColumns, numberColumns);
		//////////////System.out.println("inverseProductXX row " + inverseProductXX.rows);
		//////////////System.out.println("inverseProductXX col " + inverseProductXX.columns);

		inverseProductXX = AnalysisUtility.inverse(productXX);

		//////////////System.out.println("inverseProductXX row " + inverseProductXX.rows);
		//////////////System.out.println("inverseProductXX col " + inverseProductXX.columns);
		double temp = 0;
		for (int i = 0; i < numberColumns; i++ ) {
			//////////////System.out.println("---------------------------");
			for (int j = 0; j < numberColumns; j++ ) {
				//////////////System.out.println("inverseProductXX[" + i + "][" + j + "] = " + inverseProductXX.element[i][j]);
				temp = inverseProductXX.element[i][j];
				//////////////System.out.println("inverseProductXX Inf? = " + (Math.abs(temp) < AnalysisUtility.PRECISION_TOLERANCE));
			}
		}

 		beta = Matrix.multiply(inverseProductXX, Matrix.multiply(xTranspose, matrixY));

		double betaEntry[][] = new double[numberColumns][1];
		double betaArray[] = new double[numberColumns]; // this part need to be trimmed
		betaEntry = beta.element;
		for (int i = 0; i < numberColumns; i++ ) {
			betaArray[i] = beta.element[i][0];
			//////////////System.out.println("Beta " + beta.element[i][0]);
		}
		Matrix hat = Matrix.multiply(Matrix.multiply(matrixX, inverseProductXX), xTranspose);

		Matrix predictedY = Matrix.multiply(hat, matrixY);
		Matrix residual = Matrix.subtract(matrixY, predictedY);
		double[] predicted = new double [sampleSize];
		double[] residuals = new double [sampleSize];
		double rss = 0;

		for (int i = 0; i < sampleSize; i++ ) {
			predicted[i] = predictedY.element[i][0];
			residuals[i] = residual.element[i][0];
			rss += residuals[i] * residuals[i];
		}
		//////////////System.out.println("Sample Size " + sampleSize + " numberColumns = "+numberColumns);
		double estVar = rss / (sampleSize - numberColumns);

		//////////////System.out.println("RSS " + rss + " estVar = "+estVar);
		Matrix estCovBeta = Matrix.multiply(estVar, inverseProductXX);
		double[] estVarBeta = new double[estCovBeta.rows];
		double[] seBeta = new double[estCovBeta.rows];

		for (int i = 0; i < estCovBeta.rows; i++ ) {
			for (int j = 0; j < estCovBeta.columns; j++ ) {
				if (i == j) {
					estVarBeta[i] = estCovBeta.element[i][j];
					seBeta[i] = Math.sqrt(estVarBeta[i]);
					//////////////System.out.println("STD estVarBeta'["+i+"]["+j+"] = " + seBeta[i]);
				}

			}
		}

		texture.put(MultiLinearRegressionResult.VARIABLE_LIST, varList);
		texture.put(MultiLinearRegressionResult.BETA, betaArray);
		texture.put(MultiLinearRegressionResult.BETA_SE, seBeta);

		int betaLength = varNameList.size();
		StudentDistribution tDistribution = new StudentDistribution(sampleSize - numberOfX - 1);

		double tStatBeta[] = new double[betaLength];
		double pValue[] = new double[betaLength];
		//String pValueS[] = new String[betaLength];

		for (int i = 0; i < betaLength; i++) {
			//////////////System.out.println("beta = "+ betaArray[i] + " SE = " + seBeta[i] );
		}
		//////////////System.out.println("");

		for (int i = 0; i < betaLength; i++) {
			tStatBeta[i] = betaArray[i] / seBeta[i];
			pValue[i] = 2 * (1 - tDistribution.getCDF(Math.abs(tStatBeta[i])));
		}

		HashMap<String,Object> residualMap = AnalysisUtility.getResidualNormalQuantiles(
				residuals, residuals.length - betaArray.length);
		double[] sortedResiduals = 
			(double[])residualMap.get(MultiLinearRegressionResult.SORTED_RESIDUALS);
		int[] sortedResidualsIndex = 
			(int[])residualMap.get(MultiLinearRegressionResult.SORTED_RESIDUALS_INDEX);
		double[] sortedNormalQuantiles = 
			(double[])residualMap.get(MultiLinearRegressionResult.SORTED_NORMAL_QUANTILES);

		for (int i = 0; i < sortedResiduals.length; i++) {
			//////////////System.out.println("Sorted Residual["+i+"] = " + sortedResiduals[i]);
			//////////////System.out.println("sortedResidualsIndex["+i+"] = " + sortedResidualsIndex[i]);
			//////////////System.out.println("sortedNormalQuantiles["+i+"] = " + sortedNormalQuantiles[i]);
		}
		double varPredicted = AnalysisUtility.sampleVariance(predicted);
		double varResiduals = AnalysisUtility.sampleVariance(residuals);
		double rSquare = varPredicted / (varResiduals + varPredicted);

		int dfError = residuals.length - betaArray.length;
		texture.put(MultiLinearRegressionResult.DF_ERROR, new Integer(dfError));

		texture.put(MultiLinearRegressionResult.R_SQUARE, new Double(rSquare));
		texture.put(MultiLinearRegressionResult.BETA_T_STAT, tStatBeta);
		texture.put(MultiLinearRegressionResult.BETA_P_VALUE, pValue);
		texture.put(MultiLinearRegressionResult.PREDICTED, predicted);
		texture.put(MultiLinearRegressionResult.RESIDUALS, residuals);
		texture.put(MultiLinearRegressionResult.SORTED_RESIDUALS, sortedResiduals);
		texture.put(MultiLinearRegressionResult.SORTED_RESIDUALS_INDEX, sortedResidualsIndex);
		texture.put(MultiLinearRegressionResult.SORTED_NORMAL_QUANTILES, sortedNormalQuantiles);

		return result;
	}
}
