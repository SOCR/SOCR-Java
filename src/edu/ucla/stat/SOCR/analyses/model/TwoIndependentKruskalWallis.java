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
// this is actually for multi groups. not just two groups.


package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.distributions.ChiSquareDistribution;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.data.DataCase;


public class TwoIndependentKruskalWallis implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	private static double alpha = 0.05;
	private static double global_t;
	private static double global_cp;
	private static int global_df;
	
	private String type = "TwoIndependentKruskalWallis";
	private TreeSet<String> varNameList = new TreeSet<String>();

	private ArrayList<Object> varValueList = new ArrayList<Object>();

	private boolean approx = false; // use approximation or exact, see Conover page 231.

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		////////System.out.println("In TwoIndependentKruskalWallis analyze ");

		Result result = null;
		//////////////System.out.println("analysisType = " + analysisType);
		//////System.out.println("AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS = " + AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS);
		if (analysisType != AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS)
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
		int xIndex = 0;
		Column xColumn = null;

		while (iterator.hasNext()) {

			keys = (String) iterator.next();
			//////System.out.println("TwoIndependentKruskalWallis keys "+keys);
			try {
				Class cls = keys.getClass();
				////////////////////////System.out.println(cls.getName());
			} catch (Exception e) {
				//////System.out.println("TwoIndependentKruskalWallis Exception = " + e);
			}
			xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			double xVector[] = xColumn.getDoubleArray();
			//////System.out.println("TwoIndependentKruskalWallis  xVector.length = "+xVector.length );

			for (int i = 0; i < xVector.length; i++) {
				//System.out.println("i = " + i + " xVector["+i+"] = " +xVector[i]);
			}
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
		////////System.out.println("TwoIndependentKruskalWallis xLength = "+xLength);
		Iterator<String> it = varNameList.iterator();
		for (int i = 0; i < xLength; i++) {
			//groupNames[i] = (String)varNameList.get(i);
			//groupNames[i] = (String)varNameList.get(i + "");
			groupNames[i] = (String)it.next();

			//////System.out.println("TwoIndependentKruskalWallis groupNames["+i+"] = " + groupNames[i]);

			xVector = (double[])varValueList.get(i);
			//////////////System.out.println("TwoIndependentKruskalWallis xVector.length = " + xVector.length);
			group[i] = new DataCase[xVector.length];
			for (int j = 0; j < xVector.length; j++) {
				//////////////System.out.println("TwoIndependentKruskalWallis groupNames["+i+"] = " + groupNames[i] + ", " + xVector[j]);
				group[i][j] = new DataCase(xVector[j], groupNames[i]);

			}
			//////System.out.println("continue for");
		}

		KruskalWallisTest kruskalWallisTest = new KruskalWallisTest(group, groupNames);
		DataCase[] combo = kruskalWallisTest.getRankedArray();
		int nTotal = kruskalWallisTest.getTotalCount();
		StringBuffer dataAndRank = new StringBuffer();
		StringBuffer[] dataAndRankSeparate = new StringBuffer[varListLength];
		for (int j = 0; j < varListLength; j++) {
			dataAndRankSeparate[j] = new StringBuffer();
		}
		byte[] listCount = new byte[varListLength];

		String dataAndRankString = null;
		String[] dataAndRankStringSeparate = new String[varListLength];
		double[] rankSum = new double[xLength];
		//double[][] rankSumSeparated = new double[combo.length][];

		int[] groupCount = new int[xLength];

		double rankSquared = 0; // for S * S, See Conover page 230.
		double s2 = 0;
		double tStat = 0, tStatApprox = 0;
		//////System.out.println("varListLength = " + varListLength);
		//////System.out.println("combo.length = " + combo.length);
		for (int i = 0; i < combo.length; i++) {

			//////////System.out.println("combo["+i+"] value = " + combo[i].getValue() + ", rank = " +combo[i].getRank());
			dataAndRank.append(combo[i].getValue() + "(" +combo[i].getRank() + "), ");
			rankSquared += combo[i].getRank() * combo[i].getRank();

			for (int j = 0; j < varListLength; j++) {
				//////////System.out.println(groupNames[j] + ", " + combo[i].getGroup() + ": " + combo[i].getValue() + "(" +combo[i].getRank() + "), ");
				if (combo[i].getGroup().equalsIgnoreCase(groupNames[j])) {
					if (listCount[j] % 5 == 0) {
						dataAndRankSeparate[j].append("\n\t"+combo[i].getValue() + "(" +combo[i].getRank() + "), ");
					} else {
						dataAndRankSeparate[j].append(combo[i].getValue() + "(" +combo[i].getRank() + "), ");
					}
					listCount[j]++;
					////////////System.out.println(groupNames[j] + ": " + combo[i].getValue() + "(" +combo[i].getRank());
					//rankSquared += combo[i].getRank() * combo[i].getRank();
				}
			}
		}

		dataAndRankString = dataAndRank.toString();
		dataAndRankString = dataAndRankString.substring(0, dataAndRankString.length()-2) + ".";
		//////////System.out.println("dataAndRankString = " + dataAndRankString);
		for (int i = 0; i < varListLength; i++) {
			dataAndRankStringSeparate[i] = dataAndRankSeparate[i].toString();
			dataAndRankStringSeparate[i] = dataAndRankStringSeparate[i].substring(0, 
					dataAndRankStringSeparate[i].length()-2) + ".";
			//System.out.println(groupNames[i]  + " dataAndRankSeparate["+i+"] = " + dataAndRankSeparate[i]);
		}

		//System.out.println(dataAndRankString);
		double nTotalD = (double)nTotal;
		double s2Approx = nTotalD * (nTotalD + 1)/12; // if tie is ignored.

		s2 = (1/(nTotalD-1)) * (rankSquared - (nTotalD * (nTotalD+1) * (nTotalD+1)/4));

		for (int j = 0; j < nTotal; j++) {

			for (int i = 0; i < xLength; i++) {
				if (combo[j].getGroup().equalsIgnoreCase(groupNames[i])) {
					groupCount[i]++;
					rankSum[i] += combo[j].getRank();
				}
			}
		}
		////////System.out.println("nTotal = " + nTotal);
		////////System.out.println("xLength = " + xLength);

		double tempSum = 0;
		for (int i = 0; i < xLength; i++) {
			try {
				tempSum += rankSum[i] * rankSum[i] / (double)groupCount[i];
				////////System.out.println("groupNames["+i+"] = rankSum["+i+"] = " + rankSum[i] + ", " + groupCount[i]);
			} catch (Exception e) {
				////////System.out.println("rankSum Exception = " + e);

			}
		}

		tStat = (1/s2) * (tempSum - nTotalD * (nTotalD + 1) * (nTotalD + 1) / 4);
		tStatApprox = (1/s2Approx) * (tempSum - nTotalD * (nTotalD + 1) * (nTotalD + 1) / 4);
		//(12/nTotalD/(nTotalD+1)) * tempSum - 3 * (nTotalD+1);
		//StudentDistribution sd = new StudentDistribution(nTotal-xLength); // degrees of freedom is the parameter.
		int df = nTotal-xLength;
		//////System.out.println("df = " + df);
		//////System.out.println("df = " + df);

		double cp = getCriticalPoint(alpha, df);
		global_cp = cp;
		global_df = df;
		
		//////System.out.println("criticalPointLookUp.length = " + criticalPointLookUp.length);
		//////System.out.println("cp = " + cp);
		double compareterm = 0;
		double absTerm = 0;

		double compareTermPortion = s2 * (nTotalD - 1 - tStat) / (nTotalD - xLength);
		double compareTermPortionApprox = s2Approx * (nTotalD - 1 - tStatApprox) / (nTotalD - xLength);

		double a = AnalysisUtility.factorial(xLength);
		double b = AnalysisUtility.factorial(2); // choose 2 groups
		double c = AnalysisUtility.factorial(xLength-2);

		double numberCombonation = a / b / c;
		int infoLength = (int)numberCombonation;
		int numberDigitsKept = 4;
		////////////System.out.println("infoLength = " + infoLength);
		String[] multipleComparisonInfo = new String[infoLength];
		String multipleComparisonHeader = null;//
		if (!approx) {
			multipleComparisonHeader = "|Ri/ni - Rj/nj|\t" + AnalysisUtility.truncateDigits(cp,  numberDigitsKept) + 
			" * sqrt(" + AnalysisUtility.truncateDigits(compareTermPortion,  numberDigitsKept) + ") * sqrt(1/ni + 1/nj)";
		} else {
			multipleComparisonHeader = multipleComparisonHeader = "|Ri/ni - Rj/nj|\t" + 
			AnalysisUtility.truncateDigits(cp,  numberDigitsKept) + " * sqrt(" + 
			AnalysisUtility.truncateDigits(compareTermPortionApprox,  numberDigitsKept) + ") * sqrt(1/ni + 1/nj)";
		}
		int infoIndex = 0;

		for (int i = 0; i < xLength-1; i++) {
			for (int j = i+1; j < xLength; j++) {
					//////System.out.println("model for loop i = " + i + ", j = " + j + ", infoIndex = " + infoIndex);
					absTerm = Math.abs(rankSum[i]/(double)groupCount[i]- rankSum[j]/(double)groupCount[j]);
					////System.out.println("i=" + i + ",j=" + j + ",compareTermPortion="+compareTermPortion);
					if (!approx) {
						compareterm = cp * Math.sqrt(compareTermPortion * (1/(double)groupCount[i] + 
								1/(double)groupCount[j]));
					} else {
						compareterm = cp * Math.sqrt(compareTermPortionApprox * (1/(double)groupCount[i] + 
								1/(double)groupCount[j]));
					}
					multipleComparisonInfo[infoIndex] = ("Group " + groupNames[i] + " vs. Group " + groupNames[j] + 
							": \t" +AnalysisUtility.truncateDigits(absTerm, numberDigitsKept) + 
							"\t" + ((absTerm>compareterm)?">":"<") +"\t" + 
							AnalysisUtility.truncateDigits(compareterm, numberDigitsKept) +
							"\t" + ((absTerm>compareterm)?"(significant group differences (alpha=0.05)) ":""));

					infoIndex++;
			}
		}


		HashMap<String,Object> texture = new HashMap<String,Object>();
		int sampleSize = combo.length;
		int numberOfX = x.size();
		int numberColumns = numberOfX + 1;

		global_t= tStat;
		
		int dfChiSq = groupNames.length - 1;
		double chiSquarePValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(
				dfChiSq)).getCDF(tStat);
		////System.out.println("chiSquarePValue = " + chiSquarePValue);

		texture.put(TwoIndependentKruskalWallisResult.SAMPLE_SIZE, sampleSize + "");
		texture.put(TwoIndependentKruskalWallisResult.GROUP_NAME_LIST, groupNames);
		texture.put(TwoIndependentKruskalWallisResult.RANK_SUM_LIST, rankSum);
		texture.put(TwoIndependentKruskalWallisResult.T_STAT, new Double(tStat));
		texture.put(TwoIndependentKruskalWallisResult.P_VALUE, new Double(chiSquarePValue));
		texture.put(TwoIndependentKruskalWallisResult.CRITICAL_VALUE, new Double(cp));

		texture.put(TwoIndependentKruskalWallisResult.DATA_RANK_LIST, dataAndRankString);
		texture.put(TwoIndependentKruskalWallisResult.DATA_RANK_SEPARATE_LIST, dataAndRankStringSeparate);
		texture.put(TwoIndependentKruskalWallisResult.GROUP_COUNT, groupCount);
		texture.put(TwoIndependentKruskalWallisResult.S_SQUARED, new Double(s2));
		texture.put(TwoIndependentKruskalWallisResult.MULTIPLE_COMPARISON_INFO, multipleComparisonInfo);
		texture.put(TwoIndependentKruskalWallisResult.MULTIPLE_COMPARISON_HEADER, multipleComparisonHeader);
		texture.put(TwoIndependentKruskalWallisResult.DF, (nTotal - xLength) + "");
		result = new TwoIndependentKruskalWallisResult(texture);
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
				cp = criticalPointLookUp[df-1]; 
			// use df-1 as the array index. indexp = 0 for DF = 1, index = 1 for DF = 2, etc.
		}
		//////System.out.println("getCriticalPoint cp = " + cp);
		return cp;
	}
	
	public static double getPValue() {
		double result=1-(new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(global_df)).getCDF(global_t);
		if (result>=0 && result<=1) return (result);
		else if (result < 0) return 0.0;
		else return 1.0;
	}
	
	// from DF = 1 to DF = 40.
	private static double[] criticalPointLookUp = {12.7062047361747,  4.30265272974946,  
		3.18244630528371,  2.77644510519779,  2.57058183563631,  2.44691185114497,  
		2.36462425159278,  2.30600413520417,  2.26215716279820,  2.22813885198627,  
		2.20098516009164,  2.17881282966723,  2.16036865646279,  2.14478668791780,  
		2.13144954555978,  2.11990529922125,  2.10981557783332,  2.10092204024104,  
		2.09302405440831,  2.08596344726586,  2.07961384472768,  2.07387306790403,  
		2.06865761041905,  2.06389856162803,  2.05953855275330,  2.05552943864287,  
		2.05183051648029,  2.04840714179525,  2.04522964213270,  2.04227245630124,  
		2.03951344639641,  2.0369333434601, 2.03451529744934,  2.03224450931772,  
		2.03010792825034,  2.02809400098045,  2.02619246302911,  2.02439416391197,  
		2.02269092003676,  2.02107539030627
	};
}
