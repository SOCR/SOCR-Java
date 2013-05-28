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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import edu.ucla.stat.SOCR.distributions.FisherDistribution;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class AnovaOneWay implements Analysis {
	private final static int NUMBER_OF_Y_VAR = 1;
	private final static int MAX_GROUP_SIZE = 1;
	private String type = "AnovaOneWay";
	private final static String X_DATA_TYPE = DataType.FACTOR;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;

	//private HashMap resultMap = null;
	private static final int NUMBER_VAR = 2; // dependent and independent so there are 2

	// these variables are for box plot
	int boxPlotRowSize = 0; // seriesCount generated for box plot
	int boxPlotColSize = 1; // use 1 for one way anova, 2 for two way, etc.
	int boxPlotColIndex = 0; // use 1 for one way anova, 2 for two way, etc.

	double[][][] yValueBox = null;
	//String[][][] yValueStringBox = null;

	String[] boxPlotFactorName = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {

		Result result = null;

		if (analysisType != AnalysisType.ANOVA_ONE_WAY )
			throw new WrongAnalysisException();

		HashMap<String, Object> xMap = data.getMapX();
		HashMap<String, Object> yMap = data.getMapY();

		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		ArrayList<String[]> x = new ArrayList<String[]>();
		double y[] = null;
		int xIndex = 0;

		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
			} catch (Exception e) {}
			Column xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException("\nx data type MUST be " +
						X_DATA_TYPE + " but the input is of type " + xDataType);
			}

			String xVector[] = xColumn.getStringArray();


			x.add(xIndex, xVector);
			xIndex++;
		}


		keySet = yMap.keySet();
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
			} catch (Exception e) {}
		}
		Column yColumn = (Column) yMap.get(keys);
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException("\ny data type MUST be "+Y_DATA_TYPE+" but the input is of type " + yDataType);
		}
		y = yColumn.getDoubleArray();

		return regression(x, y);
	}


	private AnovaOneWayResult regression(ArrayList<String[]> x, double[] y) throws DataIsEmptyException {

		HashMap<String,Object> texture = new HashMap<String,Object>();

		AnovaOneWayResult result = new AnovaOneWayResult(texture);

		int sampleSize = y.length;
		int numberOfX = x.size();
		int xLength = 0;
		String[][] xString = new String[numberOfX][sampleSize];
		double[][] vectorY = new double[sampleSize][NUMBER_OF_Y_VAR];

		int indexFactor = 0;
		int totalNumCol = 0;
		//ArrayList listVarX = new ArrayList();
		xLength = ((String[])(x.get(indexFactor))).length;
		if (sampleSize != xLength ) {
			return null;
		}
		xString[indexFactor] = (String[]) x.get(indexFactor);

		HashSet<String> xSet = new HashSet<String>();

		for (int i = 0; i < xString[0].length; i++) {
			xSet.add(xString[0][i]);
		}

		Iterator<String> xIterator = xSet.iterator();
		HashMap<String,ArrayList<String>> xMap = 
			new HashMap<String,ArrayList<String>>();
		while (xIterator.hasNext()) {
			xMap.put((String)xIterator.next(), new ArrayList<String>());
		}
		xIterator = null; // will reuse this object.

		// might not be the optimal way to do it.

		Set<String> xKeySet = xMap.keySet();
		xIterator = xKeySet.iterator();
		String xKey = null;
		int groupSize = 0;
		while (xIterator.hasNext()) {
			xKey = (String)xIterator.next();
			for (int i = 0; i < y.length; i++) {
				if (xKey.equals(xString[0][i]) ){
					( (ArrayList<String>) xMap.get(xKey) ).add(groupSize, 
							new String(y[i]+""));
					groupSize++;
				}
			}
			groupSize = 0;
		}



		int numberColumns = numberOfX + 1;

		byte[][] dummy = AnalysisUtility.getDummyMatrix(xString[indexFactor]);
		int dummyLength = dummy[0].length;

		double[][] varX = new double[sampleSize][dummyLength + 1];

		for (int i = 0; i < sampleSize; i++) {
			varX[i][0] = 1; // when col = 0, first column is all one.
			vectorY[i][0] = y[i];
		}

		for (int j = 0; j < varX[0].length; j++) {
			for (int i = 0; i < sampleSize; i++) {
				if (j !=0)
					varX[i][j] = dummy[i][j-1];
			}
		}

		numberColumns = varX[indexFactor].length;
		totalNumCol = numberColumns;

		double meanY = AnalysisUtility.mean(y);
		Matrix matrixY = new Matrix(sampleSize, 1, vectorY);

		Matrix matrixX = new Matrix(sampleSize, totalNumCol, varX);
		// sampleSize by numberColumns
		Matrix xTranspose = matrixX.transpose(); // numberColumns by sampleSize

		Matrix beta = new Matrix(totalNumCol, 1);
		Matrix productXX = new Matrix(totalNumCol, totalNumCol);
		productXX = Matrix.multiply(xTranspose, matrixX);


		for (int i = 0; i < totalNumCol; i++ ) {
			for (int j = 0; j < totalNumCol; j++ ) {
				//System.out.println("AnovaOneWay " + "productXX["+i+"]["+j+"] = " + productXX.element[i][j]);
			}
		}
		Matrix inverseProductXX = new Matrix(totalNumCol, totalNumCol);
		inverseProductXX = AnalysisUtility.inverse(productXX);


		for (int i = 0; i < totalNumCol; i++ ) {
			////System.out.println("AnovaOneWay " + "---------------------------");
			for (int j = 0; j < totalNumCol; j++ ) {
				////System.out.println("AnovaOneWay " + "inverseProductXX[" + i + "][" + j + "] = " + inverseProductXX.element[i][j]);
			}
		}


		for (int i = 0; i < matrixY.rows; i++ ) {
			for (int j = 0; j < matrixY.columns; j++ ) {
				//System.out.println("AnovaOneWay " + "matrixY[" + i + "][" + j + "] = " + matrixY.element[i][j]);
			}
		}


 		beta = Matrix.multiply(inverseProductXX, Matrix.multiply(xTranspose, matrixY));

		double betaEntry[][] = new double[totalNumCol][1];
		betaEntry = beta.element;

		Matrix hat = Matrix.multiply(Matrix.multiply(matrixX, inverseProductXX), xTranspose);

		Matrix predictedY = Matrix.multiply(hat, matrixY);
		double[][] predictedYEntry = new double[sampleSize][1];
		double[] predicted = new double[sampleSize];
		double[] residuals = new double[sampleSize];

		predictedYEntry = predictedY.element;

		for (int i = 0; i < predictedYEntry.length; i++) {
			//System.out.println("AnovaOneWay " + "predictedY["+i+"] = " + predictedYEntry[i][0]);
			predicted[i] = predictedYEntry[i][0];
			residuals[i] = y[i] - predicted[i];
		}

		//Matrix residualError = Matrix.subtract(matrixY, predictedY);
		//double residualEntry[][] = new double[sampleSize][1];
		//residualEntry = residualError.element;

		Matrix yAvg = new Matrix(sampleSize, 1, meanY);

		Matrix residualModel = Matrix.subtract(predictedY, yAvg);
		double rssModel = 0;
		// Model part (use predicted Y as Y)
		for (int i = 0; i < sampleSize; i++ ) {
			rssModel += residualModel.element[i][0] * residualModel.element[i][0];
		}

		// Error part
		double rssError = 0;
		for (int i = 0; i < sampleSize; i++ ) {
			rssError += residuals[i] * residuals[i];
		}
		int numberGroups = numberColumns;//totalNumCol - 1;
		int dfCorrectedTotal = sampleSize - 1; 		// n - 1
		int dfError = sampleSize - numberGroups; 	// n - p
		int dfModel = numberGroups - 1; 			// number of rows - 1
		double estVarModel = rssModel / dfModel;
		double estVarError = rssError / dfError;
		double rssTotal = rssModel + rssError;

		FisherDistribution fDistribution = new FisherDistribution(dfModel, dfError);

	/*	System.out.println("1Way: estVarModel="+estVarModel);
		System.out.println("1Way: estVarError="+estVarError);
		System.out.println("1Way: rssModel = "+rssModel);
		System.out.println("1Way: rssError =" +rssError);
		System.out.println("1Way: dfModel =" +dfModel);
		System.out.println("1Way: dfError =" +dfError);*/
		double fValue = estVarModel/estVarError;
		double pValue = (1 - fDistribution.getCDF(Math.abs(fValue)));;
		/*System.out.println("1Way: fValue =" +fValue);
		System.out.println("1Way: pValue =" +pValue);*/
		
		yValueBox = new double[numberGroups][boxPlotColSize][];

		boxPlotFactorName = new String[numberGroups];//[boxPlotColSize];
		// xMap has the y data, the group names (index) are the key.
		xIterator = null;

		xKeySet = xMap.keySet();
		xIterator = xKeySet.iterator();
		xKey = null;
		Object[] yValueArray = null;

		int xKeyInt = 0;


		ArrayList<String> xArrayList = null;
		int groupIndex = 0;
		while (xIterator.hasNext()) {
			xKey = (String)xIterator.next();

			xArrayList = ( (ArrayList<String>) xMap.get(xKey) );

			xKeyInt++;

			int listSize = xArrayList.size();

			yValueBox[groupIndex][boxPlotColIndex] = new double[listSize];

			////System.out.println("AnovaOneWay " + "New double size = " + yValueBox[groupIndex][boxPlotColIndex].length);
			for (int j = 0; j < xArrayList.size(); j++) {
				yValueBox[groupIndex][boxPlotColIndex][j] = Double.parseDouble( (String) xArrayList.get(j) );
			}

			// need to take care of String cat names. so it may not be minus 1 in general.

			boxPlotFactorName[xKeyInt-1] = xKey; //[boxPlotColIndex] = xKey;

			groupIndex++;
		} // end while

		// 4 lines of code to get the sorted residuals for normal qq plot.
		HashMap<String,Object> residualMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);
		double[] sortedResiduals = (double[])residualMap.get(LinearModelResult.SORTED_RESIDUALS);
		int[] sortedResidualsIndex = (int[])residualMap.get(LinearModelResult.SORTED_RESIDUALS_INDEX);
		double[] sortedNormalQuantiles = (double[])residualMap.get(LinearModelResult.SORTED_NORMAL_QUANTILES);

		double varPredicted = AnalysisUtility.sampleVariance(predicted);
		double varResiduals = AnalysisUtility.sampleVariance(residuals);
		double rSquare = varPredicted / (varResiduals + varPredicted);

		texture.put(AnovaOneWayResult.BOX_PLOT_ROW_SIZE, new Integer(numberGroups));
		texture.put(AnovaOneWayResult.BOX_PLOT_FACTOR_NAME, boxPlotFactorName);
		texture.put(AnovaOneWayResult.BOX_PLOT_RESPONSE_VALUE, yValueBox);

		texture.put(AnovaOneWayResult.PREDICTED, predicted);
		texture.put(AnovaOneWayResult.RESIDUALS, residuals);
		texture.put(AnovaOneWayResult.DF_TOTAL, new Integer(dfCorrectedTotal));
		texture.put(AnovaOneWayResult.DF_ERROR, new Integer(dfError));
		texture.put(AnovaOneWayResult.DF_MODEL, new Integer(dfModel));
		texture.put(AnovaOneWayResult.RSS_ERROR, new Double(rssError));
		texture.put(AnovaOneWayResult.RSS_MODEL, new Double(rssModel));
		texture.put(AnovaOneWayResult.MSS_ERROR, new Double(estVarError));
		texture.put(AnovaOneWayResult.MSS_MODEL, new Double(estVarModel));
		texture.put(AnovaOneWayResult.RSS_TOTAL, new Double(rssTotal));
		texture.put(AnovaOneWayResult.F_VALUE, new Double(fValue));
		texture.put(AnovaOneWayResult.P_VALUE, new Double(pValue));
		texture.put(AnovaOneWayResult.R_SQUARE, new Double(rSquare));
		texture.put(Result.JAVA_ERROR, "TEST TEST");

		texture.put(AnovaOneWayResult.SORTED_RESIDUALS, sortedResiduals);
		texture.put(AnovaOneWayResult.SORTED_RESIDUALS_INDEX, sortedResidualsIndex);
		texture.put(AnovaOneWayResult.SORTED_NORMAL_QUANTILES, sortedNormalQuantiles);
		//System.out.println("model.AnovaOneWay result done result = " + result);

		return result;

	}
	public static void main(String args[]) {
		anovaOneWayTest();
	}
	static void anovaOneWayTest() {
		Data data = new Data();
		String[] group = {"1","1","1","1","1","1", "2","2","2","2","2","2","2","2", "3","3","3","3","3"};

		double[] score = {93,67,77,92,97,62, 136,120,115,104,115,121,102,130, 198,217,209,221,190};
		//String[] group = {"1","2","3","3"};
		//double[] score = {3.2, 4.2,1.3, 6.7};

		//String[] group = {"1","2","3","3","4","4"};
		//double[] score = {3.2, 4.2,1.3, 6.7,2.4,5.6};

		data.appendX("GROUP", group, DataType.FACTOR);
		data.appendY("Y_VALUE", score, DataType.QUANTITATIVE);
		int dfCTotal = 0, dfError = 0, dfModel = 0;
		double rssModel = 0, rssError = 0;
		double mssModel = 0, mssError = 0;
		double rssTotal = 0, fValue = 0;
		double pValue = 0;
		try {

			AnovaOneWayResult result = (AnovaOneWayResult)data.getAnalysis(AnalysisType.ANOVA_ONE_WAY);
			try {
				dfCTotal = result.getDFTotal();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_TOTAL))).intValue();
			} catch (Exception e) {
			}

			try {
				dfError = result.getDFError();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_ERROR))).intValue();
			} catch (Exception e) {
			}


			try {

				dfModel= result.getDFModel();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_MODEL))).intValue();
			} catch (Exception e) {
			}

			try {
				rssModel= result.getRSSModel();//((String)result.getTexture().get(AnovaOneWayResult.RSS_MODEL));
			} catch (NullPointerException e) {
			}


			try {
				rssError = result.getRSSError();//((String)result.getTexture().get(AnovaOneWayResult.RSS_ERROR));
			} catch (NullPointerException e) {
			}

			try {
				mssModel = result.getMSSModel();//((String)result.getTexture().get(AnovaOneWayResult.MSS_MODEL));
			} catch (NullPointerException e) {
			}

			try {
				mssError = result.getMSSError();//((String)result.getTexture().get(AnovaOneWayResult.MSS_ERROR));

			} catch (NullPointerException e) {
			}

			try {
				rssTotal = result.getRSSTotal();//((String)result.getTexture().get(AnovaOneWayResult.RSS_TOTAL));

			} catch (NullPointerException e) {
			}

			try {
				fValue = result.getFValue();//((String)result.getTexture().get(AnovaOneWayResult.F_VALUE));

			} catch (NullPointerException e) {
			}

			try {
				pValue = result.getPValue();//((String)result.getTexture().get(AnovaOneWayResult.P_VALUE));

			} catch (NullPointerException e) {
			}


		} catch (Exception e) {
		}
	}
}
