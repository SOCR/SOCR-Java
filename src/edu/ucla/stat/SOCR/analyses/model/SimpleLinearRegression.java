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
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class SimpleLinearRegression implements Analysis {
	private String type = "SimpleLinearRegression";
	private HashMap resultMap = null;
	private static final int NUMBER_VAR = 2; // dependent and independent so there are 2
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String SIGNIFICANCE_LEVEL = "SIGNIFICANCE_LEVEL";
	private double significanceLevel = 0.05;
	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		////System.out.println("Analysis Type = " + analysisType);

		if (analysisType != AnalysisType.SIMPLE_LINEAR_REGRESSION)
			throw new WrongAnalysisException();

		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();
		try {
			significanceLevel = Double.parseDouble((String)data.getParameter(analysisType, SIGNIFICANCE_LEVEL));
		} catch (Exception e) {
		}

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		////System.out.println("In linear");
		String keys = "";
		double x[] = null;
		double y[] = null;
		Column xColumn = null;
		while (iterator.hasNext()) {
			////System.out.println("model SLR IN while");

			keys = (String) iterator.next();
			////System.out.println("model SLR IN while keys = " + keys);

			try {
				Class cls = keys.getClass();
				////System.out.println(cls.getName());
			} catch (Exception e) {}
			xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}

			////System.out.println("model SLR getDataType = " + xColumn.getDataType());

			x = xColumn.getDoubleArray();
			////System.out.println("model SLR x = " + x);

			for (int i = 0; i < x.length; i++) {
				////System.out.println("model SLR x["+i+"] = "+x[i]);
			}
		}
		////System.out.println("model SLR done while");



		keySet = yMap.keySet();
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				////System.out.println(cls.getName());
			} catch (Exception e) {
				////System.out.println(e);
			}

			//////System.out.println(xColumn.getDataType());
		}
		Column yColumn = (Column) yMap.get(keys);
		////System.out.println(yColumn.getDataType());
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
		}

		y = yColumn.getDoubleArray();
		for (int i = 0; i < y.length; i++) {
			////System.out.println(y[i]);
		}
		////System.out.println("In Linear Regression result = " + regression(x, y));
		return regression(x, y);


	}

	private SimpleLinearRegressionResult regression(double[] x, double[] y) throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		SimpleLinearRegressionResult result = new SimpleLinearRegressionResult(texture);

		int sampleSize = x.length;
		int sampleSizeY = y.length;
		if ((sampleSize == 0) || (sampleSizeY == 0) || (sampleSize != sampleSizeY))
		throw new DataIsEmptyException();

		double meanX = AnalysisUtility.mean(x);
		double meanY = AnalysisUtility.mean(y);
		double sdX = Math.sqrt(AnalysisUtility.sampleVariance(x)); // sample SD
		double sdY = Math.sqrt(AnalysisUtility.sampleVariance(y));

		double covXY = AnalysisUtility.sampleCovariance(x, y);
		double corrXY = AnalysisUtility.sampleCorrelation(x, y);
		double rSquare = corrXY * corrXY;

		////System.out.println("X mean = " + meanX);
		////System.out.println("Y mean = " + meanY);
		double ssTotal = AnalysisUtility.sumOfSquares(y); // corrected total SS
		double sXX=  AnalysisUtility.sampleVariance(x);
		double sYY=  AnalysisUtility.sampleVariance(y);
		double sXY=  AnalysisUtility.sampleCovariance(x, y);
		////System.out.println("SS total of y = " + ssTotal);
		////System.out.println("S x = " + sXX);
		////System.out.println("S y = " + sYY);
		////System.out.println("S xy = " + sXY);
		double beta = sXY / sXX ;
		////System.out.println("beta = " + beta);
		double alpha = meanY - beta * meanX;
		////System.out.println("alpha = " + alpha);
		double rho = sXY / Math.sqrt(sXX * sYY);
		////System.out.println("rho = " + rho);
		////System.out.println("R-squared = " + rho * rho);


		double[] predicted = new double [sampleSize];
		double[] predictedUpper = new double [sampleSize];
		double[] predictedLower = new double [sampleSize];
		double[] varPredicted = new double [sampleSize];
		double[] sdPredicted = new double [sampleSize];

		double[] residuals = new double [sampleSize];

		int dfPredictionInterval = sampleSize - 1;
		double tCritical = AnalysisUtility.getStudentTCriticalPoint(significanceLevel, dfPredictionInterval);
		//System.out.println("pred interval dfPredictionInterval = " + dfPredictionInterval);
		////System.out.println("pred interval tStat = " + tCritical);


		// use the equation (2.22) and on, Jennrich's book, page 27.
		double ssError = 0; // residual sum square
		for (int i = 0; i < sampleSize; i++) {
			predicted[i] = alpha + beta * x[i];
			residuals[i] = y[i]- predicted[i];
			ssError = ssError + (residuals[i] * residuals[i]);
		}
		double rms = ssError / (double)(sampleSize - 2); // residual mean square = ssError / (n - 2). 2 for alpha and beta.
		double sumSqX = sXX * (sampleSize - 1);
		//System.out.println("meanX = " + meanX);
		//System.out.println(" sumSqX= " +  sumSqX);
		//System.out.println("rms = " + rms);

		
		for (int i = 0; i < sampleSize; i++) {

			varPredicted[i] = (rms / (double) (sampleSize) ) + ((x[i] - meanX) * (x[i] - meanX) * rms  / sXX);
			sdPredicted[i] = Math.sqrt(varPredicted[i]);
			predictedUpper[i] = predicted[i] + tCritical * sdPredicted[i];
			predictedLower[i] = predicted[i] - tCritical * sdPredicted[i];
		}
		
		double meanPredicted = AnalysisUtility.mean(predicted);
		double meanResiduals = AnalysisUtility.mean(residuals);
		double sdPredicted2 = Math.sqrt(AnalysisUtility.sampleVariance(predicted));
		double sdResiduals = Math.sqrt(AnalysisUtility.sampleVariance(residuals));
		
		// 4 lines of code to get the sorted residuals for normal qq plot.
		int df = residuals.length - 2; // number of data points - 2 (2 parameters for SLR)
		HashMap<String,Object> residualMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, df);

		double[] sortedResiduals = (double[])residualMap.get(LinearModelResult.SORTED_RESIDUALS);
		int[] sortedResidualsIndex = (int[])residualMap.get(LinearModelResult.SORTED_RESIDUALS_INDEX);
		//double[] sortedStandardizedResiduals = (double[])residualMap.get(LinearModelResult.SORTED_STANDARDIZED_RESIDUALS);
		double[] sortedNormalQuantiles = (double[])residualMap.get(LinearModelResult.SORTED_NORMAL_QUANTILES);
		double[] sortedStandardizedNormalQuantiles = (double[])residualMap.get(LinearModelResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);



		////System.out.println("SS Error= " + ssError);
		double estVar = ssError / (sampleSize - 2);// estimated variance of data y
		////System.out.println("estVar, sigma squared= " + estVar);
		double capSXX = sXX * (sampleSize - 1);
		double sdBeta = Math.sqrt( estVar/ capSXX);
		double sdAlpha = Math.sqrt(estVar/sampleSize + (meanX * meanX) * estVar / capSXX);
		////System.out.println("sdBeta = " + sdBeta);
		////System.out.println("sdAlpha = " + sdAlpha);
		StudentDistribution tDistribution = new StudentDistribution(sampleSize - NUMBER_VAR);
		double tStatBeta = beta / sdBeta;
		double tStatAlpha = alpha / sdAlpha;
		////System.out.println("tStatBeta = " + tStatBeta);
		////System.out.println("tStatAlpha = " + tStatAlpha);

			// Ivo added the Math.abs(tStatBeta), as the results were incorrect for
			// negative tStatBeta, i.e., negative beta estimates!
		double pBeta = 2 * (1 - tDistribution.getCDF(Math.abs(tStatBeta)));
		double pAlpha = 2 * (1 - tDistribution.getCDF(Math.abs(tStatAlpha)));
		
		
		
		////System.out.println(" pBeta = " + pBeta);
		////System.out.println(" pAlpha = " + pAlpha);

		texture.put(SimpleLinearRegressionResult.MEAN_X, new Double(meanX));
		texture.put(SimpleLinearRegressionResult.MEAN_Y, new Double(meanY));
		texture.put(SimpleLinearRegressionResult.SD_X, new Double(sdX));
		texture.put(SimpleLinearRegressionResult.SD_Y, new Double(sdY));
		texture.put(SimpleLinearRegressionResult.COVARIANCE_XY, new Double(covXY));
		texture.put(SimpleLinearRegressionResult.CORRELATION_XY, new Double(corrXY));
		texture.put(SimpleLinearRegressionResult.R_SQUARE, new Double(rSquare));

		texture.put(SimpleLinearRegressionResult.ALPHA, new Double(alpha));
		texture.put(SimpleLinearRegressionResult.BETA, new Double(beta));
		texture.put(SimpleLinearRegressionResult.ALPHA_SE, new Double(sdAlpha));
		texture.put(SimpleLinearRegressionResult.BETA_SE, new Double(sdBeta));
		texture.put(SimpleLinearRegressionResult.ALPHA_P_VALUE, new Double(pAlpha));
		texture.put(SimpleLinearRegressionResult.BETA_P_VALUE, new Double(pBeta));
		texture.put(SimpleLinearRegressionResult.ALPHA_T_STAT, new Double(tStatAlpha));
		texture.put(SimpleLinearRegressionResult.BETA_T_STAT, new Double(tStatBeta));
		texture.put(SimpleLinearRegressionResult.PREDICTED, predicted);
		texture.put(SimpleLinearRegressionResult.PREDICTED_UPPER, predictedUpper);
		texture.put(SimpleLinearRegressionResult.PREDICTED_LOWER, predictedLower);
		
		texture.put(SimpleLinearRegressionResult.VAR_PREDICT, varPredicted);
		texture.put(SimpleLinearRegressionResult.SD_PREDICT, sdPredicted);
		texture.put(SimpleLinearRegressionResult.MEAN_PREDICTED, new Double(meanPredicted));
		texture.put(SimpleLinearRegressionResult.SD_PREDICTED, new Double(sdPredicted2));
		texture.put(SimpleLinearRegressionResult.RESIDUALS, residuals);
		texture.put(SimpleLinearRegressionResult.MEAN_RESIDUALS, new Double(meanResiduals));
		texture.put(SimpleLinearRegressionResult.SD_RESIDUALS, new Double(sdResiduals));
		texture.put(SimpleLinearRegressionResult.SORTED_RESIDUALS, sortedResiduals);
		texture.put(SimpleLinearRegressionResult.SORTED_RESIDUALS_INDEX, sortedResidualsIndex);
		texture.put(SimpleLinearRegressionResult.SORTED_NORMAL_QUANTILES, sortedNormalQuantiles);

		return result;
	}

}