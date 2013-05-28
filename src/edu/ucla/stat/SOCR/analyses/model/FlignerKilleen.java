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
// java edu.ucla.stat.SOCR.analyses.model.FlignerKilleen

// see for reference:
// http://wiki.stat.ucla.edu/socr/index.php/AP_Statistics_Curriculum_2007_NonParam_VarIndep
// (the index i part is WRONG. should be 1<=i <=N, according to Ivo.

package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;

import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.data.DataCase;
import org.jfree.data.statistics.Statistics;
import java.util.*;


public class FlignerKilleen implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	private static double alpha = 0.05;

	private String type = "FlignerKilleen";
	private HashMap<String,Object> resultMap = null;
	private String[] varNames = null;
	private TreeSet<Object> varNameList = new TreeSet<Object>();

	private ArrayList<Object> varValueList = new ArrayList<Object>();

	private boolean approx = false; // use approximation or exact, see Conover page 231.

	private String[] varList = null;

	private double[][] inputData = null;
	private double[] sampleMedian = null; // sample median of the j-th population.
	//private double[][] normalScores = null;


	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		//////////System.out.println("In FlignerKilleen analyze ");

		Result result = null;
		////////////////System.out.println("analysisType = " + analysisType);
		if (analysisType != AnalysisType.FLIGNER_KILLEEN)
			throw new WrongAnalysisException();
		//HashMap xMap = data.getMapX();
		TreeMap<String,Object> xMap = data.getTreeX();

		//double sigLevel = data.getSignificanceLevel();
		//HashMap yMap = data.getMapY();
		if (xMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		ArrayList<Object> x = new ArrayList<Object>();
		double y[] = null;
		int xIndex = 0;
		Column xColumn = null;

		while (iterator.hasNext()) {

			keys = (String) iterator.next();
			////////System.out.println("FlignerKilleen keys "+keys);
			try {
				Class cls = keys.getClass();
				//System.out.println(cls.getName());
			} catch (Exception e) {
				////////System.out.println("FlignerKilleen Exception = " + e);
			}
			xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			double xVector[] = xColumn.getDoubleArray();
			//System.out.println("FlignerKilleen  xVector.length = "+xVector.length );

			//for (int i = 0; i < xVector.length; i++) {
			//	//System.out.println("i = " + i + " xVector["+i+"] = " +xVector[i]);
			//}
			// x is an ArrayList, key is index, Object is an x vector
			// if you have x1 and x2, then key = 1 points to x1, key = 2 points to x2.
			x.add(xIndex, xVector);
			//x.add(xVector);
			//varNameList.put(keys, keys + "");

			varNameList.add(keys);
			varValueList.add(xIndex, xVector);

			xIndex++;
		}

		int varListLength = varNameList.size();
		int xLength = xIndex;
		DataCase[][] group = new DataCase[xLength][];
		String[] groupNames = new String[xLength];
		double[] xVector = null;
		inputData = new double[xLength][];
		//System.out.println("FlignerKilleen xLength = "+xLength);
		Iterator<Object> it = varNameList.iterator();
		QSortAlgorithm quick = new QSortAlgorithm();
		int totalSize = 0; // N.
		ArrayList<Object> sampleMedianList = new ArrayList<Object>();
		DataCase[][] groupMedianDiff = new DataCase[xLength][];
		//double[] meanScore = new double[varListLength]; // each sample (group) will have one mean
		sampleMedian = new double[varListLength];
		//System.out.println("xLength = " + xLength); // xLength = group size = varListLength
		for (int i = 0; i < xLength; i++) {
			//groupNames[i] = (String)varNameList.get(i);
			//groupNames[i] = (String)varNameList.get(i + "");
			groupNames[i] = (String)it.next();

			////////System.out.println("FlignerKilleen groupNames["+i+"] = " + groupNames[i]);

			xVector = (double[])varValueList.get(i);
			//System.out.println("FlignerKilleen xVector.length = " + xVector.length);
			group[i] = new DataCase[xVector.length];

			for (int j = 0; j < xVector.length; j++) {
				////System.out.println("FlignerKilleen groupNames["+i+"] = " + groupNames[i] + ", " + xVector[j]);
				group[i][j] = new DataCase(xVector[j], groupNames[i]);

			}
			inputData[i] = xVector;
			totalSize += inputData[i].length;
			sampleMedian[i] = Statistics.calculateMedian(Utility.doubleArrayToList(inputData[i]));
			//meanScore[i] /= group[i].length; // A_j_bar
			groupMedianDiff[i] = new DataCase[xVector.length];
			double currentMeanScoreSum = 0;
			for (int j = 0; j < xVector.length; j++) {
				double currentDiff = Math.abs(xVector[j] - sampleMedian[i]);
				groupMedianDiff[i][j] = new DataCase(currentDiff, groupNames[i]);
				currentMeanScoreSum += currentDiff;
			}
			//meanScore[i] = currentMeanScoreSum / xVector.length;
			try {
				quick.sort(inputData[i]);
			} catch (Exception e) {
				//System.out.println("FlignerKilleen quick.sort Exception at i = " + i);
			}
		}
		double[] absDiff = new double[totalSize]; // each sample (group) will have one mean

		NormalDistribution nd = new NormalDistribution(0, 1);
		double[] overAllScore = new double[totalSize];
		double[] arg = new double[totalSize];

		//DataCase[] sortedAll = quick.rankCombinedListsAssignQuantile(group, groupNames, QSortAlgorithm.FLIGNER_KILLEEN_NORMAL);

		DataCase[] sortedAll = quick.rankCombinedListsAssignQuantile(groupMedianDiff, groupNames, QSortAlgorithm.FLIGNER_KILLEEN_NORMAL);


		double totalMeanScore = 0; // a_bar = sum of a_N,i, ober i = 1 to N

		double[] individualScore = new double[totalSize]; // each sample (group) will have one mean
		for (int i = 0; i < totalSize; i++) {
			//System.out.println("sortedAll["+i+"] = " + sortedAll[i].getValue());
		}
		double[] groupSum = new double[varListLength];
		double[] scores = new double[totalSize];
		//totalMeanScore = AnalysisUtility.mean(sortedAll);
		for (int i = 0; i < totalSize; i++) {

			arg[i] = sortedAll[i].getFlignerKilleenNormalQuantile();
			individualScore[i] = nd.inverseCDF(arg[i]);

			totalMeanScore += individualScore[i];

			//System.out.println(sortedAll[i].getGroup() + ", " + sortedAll[i].getValue() + ", "+ arg[i] + ", " +  individualScore[i]);
			////System.out.println("Group " + sortedAllAbsDiff[i].getGroup() + ", " + sortedAllAbsDiff[i].getValue() + ", " +individualScore[i]);
			for (int j = 0; j < varListLength; j++) {
				if (sortedAll[i].getGroup().equalsIgnoreCase(groupNames[j])) {
					groupSum[j] += individualScore[i];
				}
			}
			scores[i] = sortedAll[i].getValue();

		}
		int[] groupSize = new int[varListLength];
		for (int i = 0; i < varListLength; i++) {
			groupSize[i] = inputData[i].length;
			//System.out.println("groupSize["+i+"] = "  + groupSize[i]);
		}


		double sumOfScore = AnalysisUtility.sum(scores);
		//System.out.println("sumOfScore = " + sumOfScore);

		double mssGroupSum = 0;
		for (int j = 0; j < varListLength; j++) {
			//System.out.println("Group Sum " + groupNames[j] + " = " + groupSum[j]);
			mssGroupSum += (groupSum[j] * groupSum[j]) / ((double) groupSize[j]);
		}
		//System.out.println("mssGroupSum = " + mssGroupSum);

		/**************** ADD FIRST THEN SQUARE, THEN DIVIDE BY GROUP SIZE ****************/



		totalMeanScore /= (double)totalSize;


		//System.out.println("totalSize " + totalSize);
		//System.out.println("totalMeanScore " + totalMeanScore);
		double diffSquare = 0;

		double totalDiffSqaure = 0;
		for (int i = 0; i < totalSize; i++) {
			diffSquare = (individualScore[i] - totalMeanScore);
			diffSquare = diffSquare * diffSquare;
			totalDiffSqaure += diffSquare;
			////System.out.println("individualScore["+i+"] = " + individualScore[i] + " - totalMeanScore = " + totalMeanScore + ", diffSquare = " + diffSquare);

		}
		double variance = totalDiffSqaure / ((double)totalSize - 1); // sample variance
		//System.out.println("totalDiffSqaure = " + totalDiffSqaure);
		//System.out.println("variance = " + variance);

		//System.out.println("totalMeanScore = " + totalMeanScore);

		//double chiStat = 0;
		//for (int i = 0; i < varListLength; i++) {
		//	chiStat = inputData[i].length * (meanScore[i]- totalMeanScore);
		//}

		//System.out.println("totalSize = " + totalSize);
		//System.out.println("mssGroupSum = " + mssGroupSum);

		//totalMeanScore =  0.773742939062177;
		//variance =  0.297500798842576;
		//mssGroupSum = 14.3715281936122;
		////System.out.println("totalSize = " + totalSize);
		////System.out.println("mssGroupSum = " + mssGroupSum);
		////System.out.println("totalSize * totalMeanScore * totalMeanScore = " + (totalSize * totalMeanScore * totalMeanScore));
		////System.out.println("(mssGroupSum - totalSize * totalMeanScore * totalMeanScore ) = " + (mssGroupSum - totalSize * totalMeanScore * totalMeanScore ));
		////System.out.println("mssGroupSum = " + mssGroupSum);

		/************************************** NOTE: ************************************/
		/* normal score might not be calculated the same as in R
		/* therefore, mean and var of normal scores can be different from results of R fligner.test
		/* this can make chi-sq stat look quite different if is it small (like < 1)
		*/
		double chiStat = (mssGroupSum - totalSize * totalMeanScore * totalMeanScore ) / variance;
		int degreesOfFreedom = varListLength - 1;
		//System.out.println("degreesOfFreedom = " + degreesOfFreedom+ " chiStat = " + chiStat);
		double pValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(
				degreesOfFreedom)).getCDF(chiStat);
		//System.out.println("\n\n\tP-Value = " + pValue);
		//System.out.println("meanScore[0] = " + sampleMedian[0]);

		//double chiSquarePValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(dfChiSq)).getCDF(tStat);
		//////System.out.println("chiSquarePValue = " + chiSquarePValue);
		HashMap<String,Object> texture = new HashMap<String,Object>();

		texture.put(FlignerKilleenResult.TOTAL, new Integer(totalSize));
		texture.put(FlignerKilleenResult.GROUP_SIZE, groupSize);
		texture.put(FlignerKilleenResult.GROUP_NAME, groupNames);
		//texture.put(FlignerKilleenResult.MEAN_SCORE, meanScore));
		texture.put(FlignerKilleenResult.MEDIAN, sampleMedian);
		texture.put(FlignerKilleenResult.TOTAL_MEAN_SCORE, new Double(totalMeanScore));
		texture.put(FlignerKilleenResult.VAR, new Double(variance));
		texture.put(FlignerKilleenResult.DF,  new Integer(degreesOfFreedom));
		texture.put(FlignerKilleenResult.CHI_STAT,  new Double(chiStat));
		texture.put(FlignerKilleenResult.P_VALUE,  new Double(pValue));

		result = new FlignerKilleenResult(texture);
		return result;
	}
	private static double getCriticalPoint(double alpha, int df) { // unsigned, all positive.
		// alpha = significant level, df = degrees of freedom
		// the numbers are from R code qt(1 - alpha/2, df)
		if (alpha == 1) {
			return Double.POSITIVE_INFINITY;
		}
		else if (alpha == 0) {
			return Double.NEGATIVE_INFINITY;
		}
		double cp = 0;
		double area = 1 - .5 * alpha;

		// use alpha = .05 for now.
		if (area == .975) {
			if (df >= 120) cp = 1.979930;
			else if (df >= 60) cp = 2.000298;
			else if (df >= 40) cp = 2.021075;
			else
				cp = criticalPointLookUp[df-1]; // use df-1 as the array index. indexp = 0 for DF = 1, index = 1 for DF = 2, etc.
		}
		////////System.out.println("getCriticalPoint cp = " + cp);
		return cp;
	}
	
	// from DF = 1 to DF = 40.
	private static double[] criticalPointLookUp = {
		12.7062047361747,  4.30265272974946,  3.18244630528371,  2.77644510519779,  
		2.57058183563631,  2.44691185114497,  2.36462425159278,  2.30600413520417,  
		2.26215716279820,  2.22813885198627,  2.20098516009164,  2.17881282966723,  
		2.16036865646279,  2.14478668791780,  2.13144954555978,  2.11990529922125,  
		2.10981557783332,  2.10092204024104,  2.09302405440831,  2.08596344726586,  
		2.07961384472768,  2.07387306790403,  2.06865761041905,  2.06389856162803,  
		2.05953855275330,  2.05552943864287,  2.05183051648029,  2.04840714179525,  
		2.04522964213270,  2.04227245630124,  2.03951344639641,  2.0369333434601, 
		2.03451529744934,  2.03224450931772,  2.03010792825034,  2.02809400098045,  
		2.02619246302911,  2.02439416391197,  2.02269092003676,  2.02107539030627
		};
}
