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

import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.Matrix;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeSet;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class AnovaTwoWay implements Analysis {
	private final static int NUMBER_OF_Y_VAR = 1;
	private final static int PRESET_NUMBER_GROUPS = 9;
	private final static String X_DATA_TYPE = DataType.FACTOR;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	private final static int NUMBER_OF_DUMMY_DIGITS = AnalysisUtility.NUMBER_OF_DUMMY_DIGITS;
	// be determine by number needed for dummy digits (use R's model.matrix(m) to see.
	// we'll need more if we want 25 groups.
	
	private final static String INTERACTION_SWITCH	= "INTERACTION";
	private boolean interactionOn = false;

	private String type = "AnovaTwoWay";
	private HashMap resultMap = null;
	private String[] varNames = null;
	ArrayList<String> varNameList = new ArrayList<String>();
	String[] varNameArray = null;
	private int varLength = 0;
	int[] dfModelGroup = new int[PRESET_NUMBER_GROUPS];
	int[] dfErrorGroup = new int[PRESET_NUMBER_GROUPS];
	int dfErrorTwoWay = 0;
	double[] rssGroup = new double[PRESET_NUMBER_GROUPS];
	double[] mseModelGroup = new double[PRESET_NUMBER_GROUPS];
	double[] mseErrorGroup = new double[PRESET_NUMBER_GROUPS];
	double[] fValueGroup = new double[PRESET_NUMBER_GROUPS];

	double[] pValueGroup = new double[PRESET_NUMBER_GROUPS];


	// these variables are for box plot
	int boxPlotFactorSize = 0; // a space holder for either row or column, determine by the factor order of hashmap.
	int boxPlotRowSize = 0; // seriesCount generated for box plot
	int boxPlotColSize = 1; // use 1 for one way anova, 2 for two way, etc.
	//int colIndexBox = 0; // use 1 for one way anova, 2 for two way, etc.

	double[][][] boxPlotYValue = null;
	//double[][][] yValue = null;

	String[] boxPlotFactorName = null;
	String[] boxPlotRowFactorName = null;
	String[] boxPlotColFactorName = null;
	double estVarModel;
	double estVarError;

	double grandAvg = 0;
	int sampleSize;
	double[][] predictedBetweenArray;
	int varCount = 0; // index for predictedBetweenArray

	HashMap<String,Object> texture = new HashMap<String,Object>();
	
	double rssError;
	double rssModel;
	double[] predicted; 
	double[] residuals;
	
	
	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		

		Result result = null;
	
		interactionOn = ((Boolean)data.getParameter(analysisType, INTERACTION_SWITCH)).booleanValue();
		//System.out.println("In AnovaTwoWay analyze "+"model.AnovaTwoWay interactionOn="+interactionOn);
		
		//System.out.println("analysisType = " + analysisType);
		//System.out.println("AnalysisType.ANOVA_TWO_WAY = " + AnalysisType.ANOVA_TWO_WAY);
		if (analysisType != AnalysisType.ANOVA_TWO_WAY)
			throw new WrongAnalysisException();
		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = null;

		Result resultBetween = null;
		keySet = yMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		double y[] = null;
		String keys = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				//System.out.println("AnovaTwoWay cls.getName = " +cls.getName());
			} catch (Exception e) {
				//System.out.println("AnovaTwoWay Exception = " +e);
			}
		}
		Column yColumn = (Column) yMap.get(keys);
		//System.out.println("AnovaTwoWay yColumn.getDataType " + yColumn.getDataType());

		y = yColumn.getDoubleArray();
		//System.out.println("AnovaTwoWay  y.length = "+y.length );
		//System.out.println("AnovaOneWay " + yColumn.getDataType());
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
		}

		double[] predictedBetween = null;
		predictedBetweenArray = new double[2][]; // 2 for 2-way anova.
		varCount = 0; // index for predictedBetweenArray
		keySet = xMap.keySet();
		iterator = keySet.iterator();

		ArrayList<Object> x = new ArrayList<Object>();
		//System.out.println("AnovaTwoWay  xMap.length = "+xMap.size() );

		int xIndex = 0;
		/*
		int[] dfModelGroup = new int[PRESET_NUMBER_GROUPS];
		double[] rssGroup = new double[PRESET_NUMBER_GROUPS];
		double[] mseGroup = new double[PRESET_NUMBER_GROUPS];
		double[] fValueGroup = new double[PRESET_NUMBER_GROUPS];
		double[] pValueGroup = new double[PRESET_NUMBER_GROUPS];
		*/
		boolean useColumn = false;
		Column xColumn = null;
		int dfCTotal = 0, dfError = 0, dfModel = 0;
		double mssModel = 0;
		double mssError = 0;
		double rssTotal = 0, fValue = 0;
		double pValue = 0;
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			if (xIndex == 0) {
				useColumn = true;
			}
			else {
				useColumn = false;
			}
			keys = (String) iterator.next();
			//System.out.println("AnovaTwoWay keys "+keys);

			try {
				Class cls = keys.getClass();
			} catch (Exception e) {
				//System.out.println("AnovaTwoWay Class Exception = " + e);
			}
			xColumn = (Column) xMap.get(keys);
			//System.out.println("AnovaTwoWay xColumn.getDataType() "+xColumn.getDataType());
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}

			String xVector[] = xColumn.getStringArray();
			// x is an ArrayList, key is index, Object is an x vector
			// if you have x1 and x2, then key = 1 points to x1, key = 2 points to x2.

			data = new Data();
			data.appendX(keys, xVector, DataType.FACTOR);
			data.appendY("Y", y, DataType.QUANTITATIVE);
			//System.out.println("varLength = " + varLength);
			try {
				resultBetween = data.getAnalysis(AnalysisType.ANOVA_ONE_WAY);
				predictedBetween = (double[])(resultBetween.getTexture().get(AnovaTwoWayResult.PREDICTED));
				predictedBetweenArray[varCount] = predictedBetween;

				/*for (int i = 0; i < predictedBetween.length; i++) {
					//System.out.println("varCount = " + varCount + ", AnovaTwoWay PREDICTED["+i+"] = " + predictedBetween[i]);

				}*/
				varCount++;
			} catch (Exception e) {
				//System.out.println("AnovaTwoWay resultBetween Exception = " + e);
			}


			//int dfCTotal = 0, dfError = 0, dfModel = 0;
			//double rssModel = 0, mssModel = 0;
			//double rssError = 0, mssError = 0;
			//double rssTotal = 0, fValue = 0;
			//String pValue = null;
			try {
				dfCTotal = ((Integer)(resultBetween.getTexture().get(AnovaTwoWayResult.DF_TOTAL))).intValue();
				//dfModel= ((Integer)(result.getTexture().get(Result.DF_MODEL))).intValue();

			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay dfCTotal Exception = " + e);

			}
			try {
				dfError = ((Integer)(resultBetween.getTexture().get(AnovaTwoWayResult.DF_ERROR))).intValue();
				dfErrorGroup[xIndex] = dfError;
			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay dfError Exception = " + e);

			}

			try {
				dfModel= ((Integer)(resultBetween.getTexture().get(AnovaTwoWayResult.DF_MODEL))).intValue();
				dfModelGroup[xIndex] = dfModel;
			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay dfModel Exception = " + e);

			}

			try {
				rssModel= ((Double)resultBetween.getTexture().get(AnovaTwoWayResult.RSS_MODEL)).doubleValue();
				rssGroup[xIndex] = rssModel;
			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay rssModel Exception = " + e);

			}

			try {
				rssError = ((Double)resultBetween.getTexture().get(AnovaTwoWayResult.RSS_ERROR)).doubleValue();
  
			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay rssError Exception = " + e);

			}

			try {
				mssModel = ((Double)resultBetween.getTexture().get(AnovaTwoWayResult.MSS_MODEL)).doubleValue();
				mseModelGroup[xIndex] = mssModel;
				//System.out.println("mssModel = " + mssModel);
			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay mssModel Exception = " + e);

			}

			try {
				mssError = ((Double)resultBetween.getTexture().get(AnovaTwoWayResult.MSS_ERROR)).doubleValue();
				mseErrorGroup[xIndex] = mssError;

				//System.out.println("mssError = " + mssError);

			} catch (NullPointerException e) {

				//System.out.println("AnovaTwoWay mssError Exception = " + e);
			}

			try {
				rssTotal = ((Double)resultBetween.getTexture().get(AnovaTwoWayResult.RSS_TOTAL)).doubleValue();


			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay rssTotal Exception = " + e);

			}
/*
			try {
				fValue = ((Double)resultBetween.getTexture().get(AnovaTwoWayResult.F_VALUE)).doubleValue();
				//fValue = mssError/estVarError;
				//System.out.println("mssModel = " + mssModel);
				//System.out.println("mssError = " + mssError);
				//System.out.println("estVarError = " + estVarError);
				//System.out.println("estVarError = " + estVarError);
				fValue = mssModel / mssError;
				fValueGroup[xIndex] = fValue;

			} catch (NullPointerException e) {

			}

			try {
				//pValue = ((String)resultBetween.getTexture().get(AnovaTwoWayResult.P_VALUE));
				FisherDistribution fDistribution = new FisherDistribution(dfModel, dfError);

				pValue = (1 - fDistribution.getCDF(fValue)) + "";

				pValueGroup[xIndex] = pValue;

			} catch (NullPointerException e) {

			}
*/
			//System.out.println("AnovaTwoWay start boxPlotRowSize");
			try {
				boxPlotFactorSize = ((Integer)(resultBetween.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE))).intValue();
				if (xIndex == 0) { // column
				//if (useColumn) { // column
					boxPlotColSize = boxPlotFactorSize;
				}
				else { // row
					boxPlotRowSize = boxPlotFactorSize;
				}
			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay boxPlotFactorSize Exception = " + e);
			}

			try {
				boxPlotFactorName = ((String[])resultBetween.getTexture().get(AnovaOneWayResult.BOX_PLOT_FACTOR_NAME));
				if (xIndex == 0) { // column
				//if (useColumn) {
					boxPlotColFactorName = boxPlotFactorName;
					for (int i = 0; i < boxPlotFactorName.length; i++) {
						//System.out.println("------>boxPlotColFactorName = " + boxPlotColFactorName[i]);
					}
				}
				else { // row
					boxPlotRowFactorName = boxPlotFactorName;
					for (int i = 0; i < boxPlotFactorName.length; i++) {
						//System.out.println("------>boxPlotRowFactorName = " + boxPlotRowFactorName[i]);
					}
				}

			} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay boxPlotFactorName Exception = " + e);
			}
			if (useColumn) {
//				for (int i = 0; i < boxPlotRowSize; i++){
					try {
						boxPlotYValue = ((double[][][])resultBetween.getTexture().get(AnovaTwoWayResult.BOX_PLOT_RESPONSE_VALUE));
						//for (int i = 0; i < double.length; i++) {
//						//System.out.println("yValue = " + boxPlotYValue[i][0][
						//}
					} catch (NullPointerException e) {
				//System.out.println("AnovaTwoWay useColumn Exception = " + e);
					}
//				}
			}
			double rss = AnalysisUtility.sumOfSquares(predictedBetween);

			x.add(xIndex, xVector);
			varNameList.add(xIndex, keys);
			xIndex++;
			data = null;
			resultBetween = null;

		}
		varLength = varNameList.size();
		varNameArray = new String[varLength];
		

		//for (int i = 0; i <= xIndex; i++) {

		
		
		if (interactionOn){
			varNameArray = new String[varLength+1];
			
			for (int i = 0; i < varLength; i++) {
				varNameArray[i]= (String)varNameList.get(i);
			}
			
			varNameArray[varLength]	=  "Interaction "+varNameArray[0]+":"+   varNameArray[1];   
			//System.out.println(varNameArray[varLength]);
		}else{		
			for (int i = 0; i < varLength; i++) {
				varNameArray[i]= (String)varNameList.get(i);
			}
		}
		
		////System.out.println("In Multi Linear Regression result = regression(x, y)");
		////System.out.println("fValue count = " + count);
		AnovaTwoWayResult anovaTwoWayResult = this.regression(x, y);
		
		for (int i = 0; i < count; i++) {
			try {
				//fValue = ((Double)resultBetween.getTexture().get(AnovaTwoWayResult.F_VALUE)).doubleValue();
				fValue = mseModelGroup[i]/estVarError;
				fValueGroup[i] = fValue;

			} catch (NullPointerException e) {
			}

			try {
				//pValue = ((String)resultBetween.getTexture().get(AnovaTwoWayResult.P_VALUE));
				dfModel = dfModelGroup[i];
				dfError = dfErrorGroup[i];

				//////////System.out.println("dfModel = " + dfModelGroup[i]);
				//////////System.out.println("dfError = " + dfErrorGroup[i]);

				FisherDistribution fDistribution = new FisherDistribution(dfModel, dfErrorTwoWay);
				////////System.out.println("dfModel = " + dfModel);
				////////System.out.println("dfError = " + dfErrorTwoWay);


				/******* pValue seems incorrect *********/
				pValue = (1 - fDistribution.getCDF(fValueGroup[i]));

				pValueGroup[i] = pValue;
				//System.out.println("pValue pValue = " + pValue);

			} catch (NullPointerException e) {

				//System.out.println("AnovaTwoWay pValue Exception = " + e);
			}
		}
		texture.put(AnovaTwoWayResult.F_VALUE_GROUP, fValueGroup);
		texture.put(AnovaTwoWayResult.P_VALUE_GROUP, pValueGroup);

		//System.out.println("ending AnovaTwoWay.analyze");
	//	Exception e = new Exception();
	//	e.printStackTrace();
		return anovaTwoWayResult;


	}



	private AnovaTwoWayResult regression(ArrayList<Object> x, double[] y) 
		throws DataIsEmptyException {
		
		//System.out.println("In AnovaTwoWay regression start");
		
		//System.out.println("AnovaTwoWay start method regression ");
		//HashMap texture = new HashMap();
		grandAvg = AnalysisUtility.mean(y);

		////System.out.println("AnovaTwoWay grandAvg = " + grandAvg);
		AnovaTwoWayResult result = new AnovaTwoWayResult(texture);

		sampleSize = y.length;
		int numberOfX = x.size();
		//////System.out.println("AnovaTwoWay doAnalysis x.size = " + x.size());

		int xLength = 0;
		//////System.out.println("AnovaTowWay doAnalysis numberOfX = " + numberOfX);
		//////System.out.println("AnovaTowWay doAnalysis sampleSize = " + sampleSize);
		//////System.out.println("AnovaTowWay doAnalysis NUMBER_OF_Y_VAR = " + NUMBER_OF_Y_VAR);
		String[][] xString = new String[numberOfX][sampleSize];
		

		int xIndex = 0;
		int totalNumCol = 0;
		//////System.out.println("AnovaTwoWay doAnalysis NUMBER OF X = " + numberOfX);
		ArrayList listVarX = new ArrayList();
		xLength = ((String[])(x.get(xIndex))).length;
		//////System.out.println("AnovaTowWay doAnalysis xLength = " + xLength);

		//////System.out.println("AnovaTowWay doAnalysis sampleSize != xLength = " + (sampleSize != xLength));

		if (sampleSize != xLength ) {
			return null;
		}

		byte[][] dummy = null;
	//	double[][] dummyTotal = null;

		byte[][] dummyTemp = new byte[sampleSize][NUMBER_OF_DUMMY_DIGITS * numberOfX + 1];
		int increment = 0;
		int numberColumns = 0;
		int dummyLength1=1, dummyLength2=1;
		
		boolean varIsColumn = true; // somehow the hashmap provide column first then row.
		for (xIndex = 0; xIndex < varLength; xIndex++) {
				xString[xIndex] = (String[]) x.get(xIndex);
				//System.out.println("AnovaTowWay doAnalysis xString.length = " + xString[0].length);

			for (int i = 0; i < xString[0].length; i++) {
				//System.out.println("AnovaTwoWay doAnalysis xString = " + xString[0][i]);
			}

			dummy = AnalysisUtility.getDummyMatrix(xString[xIndex]);
			int dummyLength = dummy[0].length;

		/*	System.out.println("in AnovaTwoWay:");
				for (int i = 0; i < sampleSize; i++) {
				for (int j = 0; j < dummy[0].length; j++) {
					System.out.print("d[" + i + "][" + j +"] = "+dummy[i][j]+" ");
					}
					System.out.println();
				}*/
				
			// the inverse of i and j in position is due to the wierdness of AnalysisUtility.getDummyMatrix, sigh.
			for (int j = 0; j < dummy[0].length; j++) {
				for (int i = 0; i < sampleSize; i++) {
				//	System.out.println("dummyLength + increment * j = " + dummyLength + increment * j);
					//dummyTemp[i][dummyLength + increment * j] = dummy[i][j];
					//System.out.println("dummy[" + i + "][" + j +"] = "+dummy[i][j]);
					dummyTemp[i][1+increment + j] = dummy[i][j];  //changed by jenny
				}
			}
			//System.out.println("AnovaTwoWay increment before increment = increment + dummyLength = "+ increment);
			increment = increment + dummyLength;
			if (xIndex==0)
				dummyLength1 = dummyLength;
			if (xIndex==1)
				dummyLength2 = dummyLength;
			//System.out.println("AnovaTwoWay increment afer change = "+ increment);

		}
		
		numberColumns = increment + 1; // add 1 for the first column
	/*	System.out.println("in AnovaTwoWay:");
		for (int i = 0; i < sampleSize; i++) {
		for (int j = 0; j < dummyTemp[0].length; j++) {
			System.out.print("dtemp[" + i + "][" + j +"] = "+dummyTemp[i][j]+" ");
			}
			System.out.println();
		}*/
		
		
		int[][] dummyInteraction= null;
		if (interactionOn){
			dummyInteraction = new int[sampleSize][dummyLength1*dummyLength2];
			for (int k=0; k<dummyLength2; k++)
				for (int j=0; j<dummyLength1; j++)				
					for (int i=0; i<sampleSize; i++){
						//System.out.print("dummyTemp["+i+"]["+(j+1)+"]="+dummyTemp[i][j+1]+",");
					//	System.out.print("dummyTemp["+i+"]["+(dummyLength1+k+1)+"]="+dummyTemp[i][dummyLength1+k+1]+",");
						
						dummyInteraction[i][j*dummyLength2+k]=
							(int)dummyTemp[i][j+1]*(int)dummyTemp[i][dummyLength1+k+1];
					//	System.out.println("dInt["+ i + "][" + (j*dummyLength1+k) +"] ="+dummyInteraction[i][j*dummyLength1+k]+" ");
					}
		}
		
		
		//////System.out.println("AnovaTwoWay numberColumns = "+ numberColumns);

	/*	dummyTotal = new double[sampleSize][numberColumns];
		for (int i = 0; i < sampleSize; i++) {
			for (int j = 0; j < increment; j++) {
				if (j == 0) {
					dummyTotal[i][j] = 1;
				}
				else {
					dummyTotal[i][j] = dummyTemp[i][j];
				}
			}

		}
		
		System.out.println("in AnovaTwoWay dummyTotal:");
		for (int i = 0; i < sampleSize; i++) {
		for (int j = 0; j < increment; j++) {
			System.out.print("dt[" + i + "][" + j +"] = "+dummyTotal[i][j]+" ");
			}
			System.out.println();
		}*/
		
		double[][] varX = new double[sampleSize][numberColumns];
	
		for (int i = 0; i < sampleSize; i++) {
			varX[i][0] = 1; // when col = 0, first column is all one.
		}

		for (int j = 0; j < varX[0].length; j++) {
			for (int i = 0; i < sampleSize; i++) {
				if (j !=0)
					varX[i][j] = dummyTemp[i][j];
				//System.out.println("AnovaTwoWay doAnalysis varX["+i+"]["+j+"] = "+varX[i][j]);
			}
			//System.out.println("");
		}
		
		
		numberColumns = varX[xIndex].length;
		totalNumCol = numberColumns;
		
		
		predicted = new double[sampleSize];
		residuals = new double[sampleSize];
	
		
		double rssErrorWithoutInteraction=0;		
		double rssErrorInteraction=0;
		double rssResiduals=0;

		doRssError(totalNumCol, varX,y);
		rssErrorWithoutInteraction= rssError;
		rssResiduals = rssError;
	
		if (interactionOn){
			varX = new double[sampleSize][numberColumns+dummyInteraction[0].length];
			for (int i = 0; i < sampleSize; i++) {
				varX[i][0] = 1; // when col = 0, first column is all one.
				//System.out.println("Y["+i+"]" 	+ y[i]);
			}
			

			for (int j = 0; j < varX[0].length; j++) {
				for (int i = 0; i < sampleSize; i++) {
					if (j !=0)
						varX[i][j] = dummyTemp[i][j];
					//System.out.println("AnovaTwoWay doAnalysis varX["+i+"]["+j+"] = "+varX[i][j]);
				}
				//System.out.println("");
			}

			for (int j = 0; j < dummyInteraction[0].length; j++) {
				for (int i = 0; i < sampleSize; i++) {
						varX[i][j+numberColumns] = dummyInteraction[i][j];
					//System.out.println("AnovaTwoWay doAnalysis varX["+i+"]["+j+"] = "+varX[i][j]);
				}
				//System.out.println("");
			}	
			
			
			totalNumCol = varX[0].length;
			
			rssError=0;
			doRssErrorInteraction(totalNumCol, varX, y);
			rssResiduals= rssError;
		//	System.out.println("rssError="+ rssError);
			rssErrorInteraction= rssErrorWithoutInteraction-rssError;
		//	System.out.println("rssErrorInteraction="+ rssErrorInteraction);
			
         //	two way, count=2    group[2] value are for i:j interaction
			dfModelGroup[2]= dfModelGroup[0]*dfModelGroup[1];
		
			rssGroup[2] = rssErrorInteraction;
			
			mseModelGroup[2]=rssErrorInteraction/dfModelGroup[2];
			
			int dfError = sampleSize - totalNumCol;
			int dfModel = totalNumCol - 1; 	
			estVarModel = rssModel / dfModel;
			estVarError = rssErrorInteraction/ dfError;
			double fValue = estVarModel/estVarError;
			FisherDistribution fDistribution = new FisherDistribution(dfModel, dfError);
			double pValue = (1 - fDistribution.getCDF(fValue));
			pValueGroup[2]=pValue;
			fValueGroup[2]=fValue;
		}
	
		
	
		int numberGroups = totalNumCol;
		int dfCorrectedTotal = sampleSize - 1; 		// n - 1
		int dfError = sampleSize - numberGroups; 	// n - p
		int dfModel = numberGroups - 1; 			// number of rows - 1
		this.estVarModel = rssModel / dfModel;
		this.estVarError = rssResiduals/ dfError;
		double rssTotal = rssModel + rssResiduals;
		dfErrorTwoWay = dfError;
		FisherDistribution fDistribution = new FisherDistribution(dfModel, dfError);
		//System.out.println("two-way model dfModel = " + dfModel + ", dfError = " + dfError);
		double fValue = estVarModel/estVarError; // MS_between / MS_within
		//System.out.println("estVarModel = " + estVarModel + ", estVarError = " +estVarError);
		double pValue = (1 - fDistribution.getCDF(fValue));
	
		HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);
		double[] sortedResiduals = (double[])residualMap.get(LinearModelResult.SORTED_RESIDUALS);
		int[] sortedResidualsIndex = (int[])residualMap.get(LinearModelResult.SORTED_RESIDUALS_INDEX);
		double[] sortedNormalQuantiles = (double[])residualMap.get(LinearModelResult.SORTED_NORMAL_QUANTILES);

		double varPredicted = AnalysisUtility.sampleVariance(predicted);
		double varResiduals = AnalysisUtility.sampleVariance(residuals);
		double rSquare = varPredicted / (varResiduals + varPredicted);
		
	/*	for (int i=0; i<dfModelGroup.length; i++)
			System.out.println("dfModelGroup:"+i+"="+dfModelGroup[i]);
		
		for (int i=0; i<rssGroup.length; i++)
			System.out.println("rssGroup:"+i+"="+rssGroup[i]);
		
		for (int i=0; i<mseModelGroup.length; i++)
			System.out.println("mesModelGroup:"+i+"="+mseModelGroup[i]);*/

		texture.put(AnovaTwoWayResult.VARIABLE_LIST, varNameArray);
		texture.put(AnovaTwoWayResult.DF_MODEL_GROUP, dfModelGroup);
		texture.put(AnovaTwoWayResult.RSS_GROUP, rssGroup);
		texture.put(AnovaTwoWayResult.MSE_GROUP, mseModelGroup);
		//texture.put(AnovaTwoWayResult.F_VALUE_GROUP, fValueGroup);
		//texture.put(AnovaTwoWayResult.P_VALUE_GROUP, pValueGroup);

		texture.put(AnovaTwoWayResult.PREDICTED, predicted);
		texture.put(AnovaTwoWayResult.RESIDUALS, residuals);
		texture.put(AnovaTwoWayResult.DF_TOTAL, new Integer(dfCorrectedTotal));
		texture.put(AnovaTwoWayResult.DF_ERROR, new Integer(dfError));
		texture.put(AnovaTwoWayResult.DF_MODEL, new Integer(dfModel));
		
		texture.put(AnovaTwoWayResult.RSS_ERROR, new Double(rssResiduals)); // rssError
		texture.put(AnovaTwoWayResult.RSS_MODEL, new Double(rssModel));
		texture.put(AnovaTwoWayResult.MSS_ERROR, new Double(estVarError));
		texture.put(AnovaTwoWayResult.MSS_MODEL, new Double(estVarModel));
		texture.put(AnovaTwoWayResult.RSS_TOTAL, new Double(rssTotal));
		texture.put(AnovaTwoWayResult.F_VALUE, new Double(fValue ));
		texture.put(AnovaTwoWayResult.P_VALUE, new Double(pValue));
		texture.put(AnovaTwoWayResult.R_SQUARE, new Double(rSquare));

		//System.out.println("AnovaTwoWay rssError " + rssError);
		//System.out.println("AnovaTwoWay boxPlotColSize " + boxPlotColSize);

		texture.put(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE, new Integer(boxPlotRowSize));
		texture.put(AnovaTwoWayResult.BOX_PLOT_COLUMN_SIZE, new Integer(boxPlotColSize));
		texture.put(AnovaTwoWayResult.BOX_PLOT_ROW_NAME, boxPlotRowFactorName);
		texture.put(AnovaTwoWayResult.BOX_PLOT_COLUMN_NAME, boxPlotColFactorName);

		texture.put(AnovaTwoWayResult.BOX_PLOT_RESPONSE_VALUE, boxPlotYValue);


		texture.put(AnovaTwoWayResult.SORTED_RESIDUALS, sortedResiduals);
		texture.put(AnovaTwoWayResult.SORTED_RESIDUALS_INDEX, sortedResidualsIndex);
		texture.put(AnovaTwoWayResult.SORTED_NORMAL_QUANTILES, sortedNormalQuantiles);

	//	System.out.println("In AnovaTwoWay regression end");
		return result;

	}
	
	protected void doRssError(int totalNumCol, double[][] varX,  double[] y){
		//////System.out.println("AnovaTwoWay doAnalysis numberColumns = " + numberColumns);
		
		/*System.out.println("in AnovaTwoWay varX : totalNumCol= "+totalNumCol);
		for (int i = 0; i < sampleSize; i++) {
		for (int j = 0; j < varX[0].length; j++) {
			System.out.print("varX[" + i + "][" + j +"] = "+varX[i][j]+" ");
			}
			System.out.println();
		}*/
		
		double[][] vectorY = new double[sampleSize][NUMBER_OF_Y_VAR];
		for (int i = 0; i < sampleSize; i++) {
			vectorY[i][0] = y[i];
		}
		
		double meanY=0;
		try{
			meanY= AnalysisUtility.mean(y);
		}catch(Exception e){
			
		}
		Matrix matrixY = new Matrix(sampleSize, 1, vectorY);
		//System.out.println("print martrixY");
		//Matrix.print(matrixY);
		Matrix matrixX = new Matrix(sampleSize, totalNumCol, varX);
		//System.out.println("print martrixX");
		//Matrix.print(matrixX);
		// sampleSize by numberColumns
		Matrix xTranspose = matrixX.transpose(); // numberColumns by sampleSize
		/*
		Matrix identityMatrix = new Matrix(numberColumns, numberColumns, 'I');

		for (int i = 0; i < numberColumns; i++ ) {
			for (int j = 0; j < numberColumns; j++ ) {
				if (i == j)
					identityMatrix.element[i][j] = 2;
				//System.out.println("IM[i][j]" + identityMatrix.element[i][j]);
			}
		}
		*/

		Matrix beta = new Matrix(totalNumCol, 1);
		Matrix productXX = new Matrix(totalNumCol, totalNumCol);
		productXX = Matrix.multiply(xTranspose, matrixX);

		Matrix inverseProductXX = new Matrix(totalNumCol, totalNumCol);

		inverseProductXX = AnalysisUtility.inverse(productXX);

 		beta = Matrix.multiply(inverseProductXX, Matrix.multiply(xTranspose, matrixY));
 		//System.out.println("beta = ");
 		//Matrix.print(beta);
 		
		double betaEntry[][] = new double[totalNumCol][1];
		betaEntry = beta.element;

		Matrix hat = Matrix.multiply(Matrix.multiply(matrixX, inverseProductXX), xTranspose);

		Matrix predictedY = Matrix.multiply(hat, matrixY);

		double[][] predictedYEntry = new double[sampleSize][1];
		

		predictedYEntry = predictedY.element;
	//	System.out.println("varLength = " + varLength);
	//System.out.println("predictedYEntry.length = " + predictedYEntry.length);

		//for (int i = 0; i < predictedYEntry.length; i++) {
		for (int i = 0; i < sampleSize; i++) {
			//predicted[i] = predictedYEntry[i][0];
			if (varLength == 2) {
				predicted[i] = predictedBetweenArray[0][i] + predictedBetweenArray[1][i] - grandAvg;
			} else if (varLength == 1) {
				predicted[i] = predictedBetweenArray[0][i] - grandAvg;

			}

			residuals[i] = y[i] - predicted[i];
			//System.out.println("AnovaTwoWay predictedBetweenArray[0]["+i+"] = "+ predictedBetweenArray[0][i] + ",grandAvg = "+ grandAvg);
			//System.out.println("AnovaTwoWay predictedBetweenArray[1]["+i+"] = "+ predictedBetweenArray[1][i] + ",grandAvg = "+ grandAvg);
			//System.out.println("AnovaTwoWay predicted["+i+"] = "+ predicted[i] + ", residuals["+i+"] = "+ residuals[i]);
		}
		//	System.out.println("mean(residuals) = "+ AnalysisUtility.mean(residuals));
		//	System.out.println("mean(predicted) = "+ AnalysisUtility.mean(predicted));

		//Matrix residualError = Matrix.subtract(matrixY, predictedY);
		//double residualEntry[][] = new double[sampleSize][1];
		//residualEntry = residualError.element;

		Matrix yAvg = new Matrix(sampleSize, 1, meanY);

		Matrix residualModel = Matrix.subtract(predictedY, yAvg);
		rssModel = 0;


		// Model part (use predicted Y as Y)
		for (int i = 0; i < sampleSize; i++ ) {
			rssModel += residualModel.element[i][0] * residualModel.element[i][0];
			//System.out.println("residualModel " + residualModel.element[i][0]);
		}

		// Error part
		rssError = 0;
		for (int i = 0; i < sampleSize; i++ ) {
			rssError += residuals[i] * residuals[i];
			//System.out.println("residualError " + residuals[i]);
		}
		
	}
	
	protected void doRssErrorInteraction(int totalNumCol, double[][] varX, double[] y){
		//////System.out.println("AnovaTwoWay doAnalysis numberColumns = " + numberColumns);
		
	/*	System.out.println("in AnovaTwoWay doRssErrorInteraction varX : totalNumCol= "+totalNumCol);
		for (int i = 0; i < sampleSize; i++) {
		for (int j = 0; j < varX[0].length; j++) {
			System.out.print("varX[" + i + "][" + j +"] = "+varX[i][j]+" ");
			}
			System.out.println();
		}*/
		
		double[][] vectorY = new double[sampleSize][NUMBER_OF_Y_VAR];
		for (int i = 0; i < sampleSize; i++) {
			vectorY[i][0] = y[i];
		}
		
		double meanY=0;
		try{
			meanY= AnalysisUtility.mean(y);
		}catch(Exception e){
			
		}
		Matrix matrixY = new Matrix(sampleSize, 1, vectorY);
		//System.out.println("print martrixY");
		//Matrix.print(matrixY);
		Matrix matrixX = new Matrix(sampleSize, totalNumCol, varX);
		//System.out.println("print martrixX");
		//Matrix.print(matrixX);
		// sampleSize by numberColumns
		Matrix xTranspose = matrixX.transpose(); // numberColumns by sampleSize

		Matrix beta = new Matrix(totalNumCol, 1);
		Matrix productXX = new Matrix(totalNumCol, totalNumCol);
		productXX = Matrix.multiply(xTranspose, matrixX);

		Matrix inverseProductXX = new Matrix(totalNumCol, totalNumCol);

		inverseProductXX = AnalysisUtility.inverse(productXX);

 		beta = Matrix.multiply(inverseProductXX, Matrix.multiply(xTranspose, matrixY));
		//System.out.println("beta = ");
 		//Matrix.print(beta);
 		
		
		Matrix predictedInteraction = Matrix.multiply(matrixX, beta);
		
		double rssResiduals=0;
		double[][] predictedInteractionEntry= predictedInteraction.element;
		
		for (int i=0 ;i<predictedInteraction.rows; i++)
			rssResiduals += (y[i]-predictedInteractionEntry[i][0])*(y[i]-predictedInteractionEntry[i][0]);
		
		rssError = rssResiduals;
		/*********/
		
		Matrix yAvg = new Matrix(sampleSize, 1, meanY);
		Matrix residualModel = Matrix.subtract(predictedInteraction, yAvg);
		rssModel = 0;
		// Model part (use predicted Y as Y)
		for (int i = 0; i < sampleSize; i++ ) {
			rssModel += residualModel.element[i][0] * residualModel.element[i][0];
			//System.out.println("residualModel " + residualModel.element[i][0]);
		}
		//texture.put(AnovaTwoWayResult.RSS_ERROR, new Double(rssResiduals));
	}		
}