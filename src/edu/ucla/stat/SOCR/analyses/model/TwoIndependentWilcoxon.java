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
// Wilcoxon (Mann-Whitney) Rank Sum test for two independent samples.
// annieche 20051130

package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.util.QSortAlgorithm;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import java.util.*;
import java.math.BigInteger;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class TwoIndependentWilcoxon implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;

	private String type = "TwoIndependentWilcoxon";
	private HashMap<String,Object> resultMap = null;
	private boolean isLargeSample = false;
	public static int SIZE_LARGE_SAMPLE = 10;
	private static int SIZE_BIG_INT = 1;

	private static String U_STAT_EXPECTATION_FORMULA = "(n_1 * n_2)/ 2" ;
	private static String U_STAT_VARIANCE_FORMULA = "n_1 * n_2 * (n_1 + n_2 + 1) / 12";
	private String dataSummary1 = "";
	private String dataSummary2 = "";
	private String exactComboSummary = "";

	private boolean pValueAtLeft = true;
	private	int sameSizeOfSmallerRankSum = 0;
	private	int sameSizeOfLargerRankSum = 0;

	// assuming the rank sum of group X (nameX, see below) is less than
	// the rank sum  of group Y (nameY), pValue is at left tail. the
	// Prob(rank-sum-of-X equal to rank-sum-of-X-from-computation, and under) are calculated as
	// the p-value.
	// similarly, if >, use right tail.

	/* NOTE: large sample size is set to be 10 If you'd like to
	change this to a larger number, please also modify
	edu.ucla.stat.SOCR.analyses.tools.AnalysisUtility class' method factorial().
	Otherwise the code will break.
	-- annieche 20060113
	*/

	HashMap<String,Object> texture = new HashMap<String,Object>();
	HashMap<String,Object> completeMap = new HashMap<String,Object>();
	//HashSet completeSet = new HashSet();
	String nameX = null, nameY = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		//////System.out.println("TwoIndependentWilcoxon Analysis Type = " + analysisType);

		if (analysisType != AnalysisType.TWO_INDEPENDENT_WILCOXON)
			throw new WrongAnalysisException();
		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		double x[] = null;
		double y[] = null;

		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
			} catch (Exception e) {}
			Column xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			x = xColumn.getDoubleArray();
		}
		nameX = keys;

		double combo[] = null;
		keySet = yMap.keySet();
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				//////System.out.println(cls.getName());
			} catch (Exception e) {}

			////////System.out.println(xColumn.getDataType());
		}
		nameY = keys;

		Column yColumn = (Column) yMap.get(keys);
		////////////////System.out.println(yColumn.getDataType());
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
		}
		y = yColumn.getDoubleArray();
		for (int i = 0; i < y.length; i++) {
			//////System.out.println(y[i]);
		}
		//combo[] = new double[x.length + y.length];

		//for (int i = 0; i <
		Arrays.sort(x);
		Arrays.sort(y);
		//////System.out.println("TwoIndependentWilcoxon sorted X");
		for (int i=0; i<x.length; i++){
			//////System.out.println(x[i]);
		}
		//////System.out.println("TwoIndependentWilcoxon sorted Y");

		for (int i=0; i<y.length; i++){
			//////System.out.println(y[i]);
		}

		////////System.out.println("In TwoIndependentWilcoxon result = " + doAnalysis(x, y));
		return doAnalysis(x, y);
	}
	
	// test means t - test
	private TwoIndependentWilcoxonResult doAnalysis(double[] x, double[] y) throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		TwoIndependentWilcoxonResult result = new TwoIndependentWilcoxonResult(texture);
		int nX = x.length;
		int nY = y.length;
		int nTotal = nX + nY; // total size
		/***** Determine whether use large sample approximation *****/
		if (nX > SIZE_LARGE_SAMPLE || nY > SIZE_LARGE_SAMPLE || nTotal > SIZE_LARGE_SAMPLE) {
			isLargeSample = true;
		}

		double meanX = AnalysisUtility.mean(x);
		double meanY = AnalysisUtility.mean(y);

		DataCase[] groupA = new DataCase[nX];
		DataCase[] groupB = new DataCase[nY];
		for (int i = 0; i < nX; i++) {
			groupA[i] = new DataCase(x[i], nameX);
			//////System.out.println("group A has: " + x[i]);


		}
		for (int i = 0; i < nY; i++) {
			groupB[i] = new DataCase(y[i], nameY);
			//////System.out.println("group B has: " + y[i]);

		}

		texture.put((String)TwoIndependentWilcoxonResult.MEAN_X, new Double(meanX));
		texture.put((String)TwoIndependentWilcoxonResult.MEAN_Y, new Double(meanY));

		/////DataCase[] combo = QSortAlgorithm.rankCombinedLists(groupA, groupB);
		WilcoxonRankTest wilcoxonRankTest = new WilcoxonRankTest(groupA, groupB);
		DataCase[] combo = wilcoxonRankTest.getRankedArray();

		//HashMap tieMap = wilcoxonRankTest.getTieMap(); // the keys are from combo.
		//HashMap completeMap = wilcoxonRankTest.getCompleteMap();

		double sampleSize1 = 0, sampleSize2 = 0;
		double sum1 = 0, sum2 = 0;
		String groupName1 = "";
		String groupName2 = "";
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		int iA = 0, iB = 0;
		int listLength = 5;
		// to follow the logic in Conover page 343.
		if (nX <= nY) {

			groupName1 = nameX;
			groupName2 = nameY;
			sampleSize1 = nX;
			sampleSize2 = nY;

			sb1.append(nameX + ": ");
			sb2.append(nameY + ": ");

			for (int i = 0; i < combo.length; i++) {
				if ( (combo[i].getGroup()).equals(nameX)) {
					sum1 = sum1 + combo[i].getRank();
					sb1.append(combo[i].getValue() + "(" +combo[i].getRank() + "), ");

					if (iA % listLength == listLength-1) {
						sb1.append("\n\t");
					}
					iA++;
				}
				else {
					sb2.append(combo[i].getValue() + "(" +combo[i].getRank() + "), ");
					if (iB % listLength == listLength-1) {
						sb2.append("\n\t");
					}
					iB++;
				}
			}
			sum2 = .5 * nTotal * (nTotal + 1) - sum1;
		}
		else {

			groupName1 = nameY;
			groupName2 = nameX;
			sampleSize1 = nY;
			sampleSize2 = nX;
			sb1.append(nameY + ": ");
			sb2.append(nameX + ": ");
			iA = 0; iB = 0;

			for (int i = 0; i < combo.length; i++) {
				if ( (combo[i].getGroup()).equals(nameY)) {
					sum1 = sum1 + combo[i].getRank();
					sb1.append(combo[i].getValue() + "(" +combo[i].getRank() + "), ");
					if (iA % listLength == listLength-1) {
						sb1.append("\n\t");
					}
					iA++;
				}
				else {
					sb2.append(combo[i].getValue() + "(" +combo[i].getRank() + "), ");
					if (iB % listLength == listLength-1) {
						sb2.append("\n\t");
					}
					iB++;
				}
			}
			sum2 = .5 * nTotal * (nTotal + 1) - sum1;
		}

		dataSummary1 = sb1.toString();
		dataSummary2 = sb2.toString();
		////System.out.println("model dataSummary1 = " + dataSummary1);
		////System.out.println("model dataSummary2 = " + dataSummary2);

		texture.put((String)TwoIndependentWilcoxonResult.DATA_SUMMARY_1, dataSummary1);
		texture.put((String)TwoIndependentWilcoxonResult.DATA_SUMMARY_2, dataSummary2);

		// find u statistic for both small and large samples.
		//double sum2 = .5 * nTotal * (nTotal + 1) - sum1;
		sampleSize2 = nTotal - sampleSize1;
		double uStat1 = 0, uStat2 = 0;
		// refer to smaller in sample size, not smaller u-stat or rank sum.

		uStat1 = (sampleSize1 * sampleSize2 + .5 * (sampleSize1 * (sampleSize1 + 1))) - sum1;
		uStat2 = (sampleSize1 * sampleSize2 + .5 * (sampleSize2 * (sampleSize2 + 1))) - sum2;
		// note: sum1 is rank sum 1. sum2 is rank sum 2.

		// note that group X is used as the reference here.
		if ((groupName1.equalsIgnoreCase(nameX) && sum1 <= sum2) || (groupName1.equalsIgnoreCase(nameY) && sum1 >= sum2)) {
			pValueAtLeft = true;
		}
		else {
			pValueAtLeft = false;
		}

		double tempUstat=0;
		if (groupName1.equalsIgnoreCase(nameX) || sum1 <= sum2) {
			// Swap the test stastistics to be consistent with Wikipedia protocols/recipe.
			// This does not affect the results, it just lists the test ststistics for each group
			// according to the Wikipedia protocol: http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U
			// Recommended by Rob Gould, April 02, 2010
			tempUstat = uStat1;
			uStat1 = uStat2;
			uStat2 = tempUstat;
		}
		
		double commonTerm = factorialRatio((int)sampleSize1, (int)(sampleSize1 + sampleSize2));
		//////System.out.println("groupName1 = " + groupName1 + " rank sum1  = " + sum1);
		//////System.out.println("groupName2 = " + groupName2 + " rank sum2  = " + sum2);

		double cummulativePValue = 0;

		boolean hasTie = wilcoxonRankTest.getHasTie();
		int maxNumberTies = wilcoxonRankTest.getMaxNumberTies();
		//////System.out.println("hasTie  = " + hasTie);
		//////System.out.println("pValueAtLeft = " + pValueAtLeft);

		texture.put((String)TwoIndependentWilcoxonResult.RANK_SUM_SMALL, new Double(sum1));
		texture.put((String)TwoIndependentWilcoxonResult.RANK_SUM_LARGE, new Double(sum2));
		texture.put((String)TwoIndependentWilcoxonResult.U_STAT_SMALL, new Double(uStat1));
		texture.put((String)TwoIndependentWilcoxonResult.U_STAT_LARGE, new Double(uStat2));
		texture.put((String)TwoIndependentWilcoxonResult.GROUP_NAME_SMALL, groupName1);
		texture.put((String)TwoIndependentWilcoxonResult.GROUP_NAME_LARGE, groupName2);

		// now do things differently for small and large samples.
		// large sample: use normal approximation.
		// small sample: use xact test.
		// added by annieche 20060111.
		//double meanU = .5 * nX * (nX + nY + 1);
		double meanU = .5 * nX * nY; // Rice formula page 407, wikipedia uses this too.
		/* some other refenreces use a different formula: */
		//System.out.println("meanU  = " + meanU);

		//double meanULarger = sum1 + sum2 - meanU;
		texture.put((String)TwoIndependentWilcoxonResult.IS_LARGE_SAMPLE, new Boolean(isLargeSample));
		//isLargeSample = true;
		//////System.out.println("Use Large Sample isLargeSample = " + isLargeSample);
		if (isLargeSample) {
			//////System.out.println("Use Large Sample");
			double varU = 0, zScore = 0, pValueTwoSided = 0, pValueOneSided = 0;
			double nume = nX * nY * (nX + nY + 1);
			varU = nume / 12; //N1N2(N1 + N2 + 1)/12
			//System.out.println("Use Large Sample uStat1 = " + uStat1);
			//System.out.println("Use Large Sample meanU = " + meanU);
			//System.out.println("Use Large Sample varU = " + varU);

			zScore = (uStat1 - meanU) / Math.sqrt(varU);
			NormalDistribution nDistribution = new NormalDistribution();
			if (zScore >= 0) {
				pValueTwoSided = 2 * (1 - nDistribution.getCDF(zScore));
			} else {
				pValueTwoSided = 2 * nDistribution.getCDF(zScore);

			}

			pValueOneSided = 1 - nDistribution.getCDF(Math.abs(zScore));

			if (pValueOneSided<0) 		pValueOneSided=0;
			else if (pValueOneSided>1) 	pValueOneSided=1;
			if (pValueTwoSided<0)		pValueTwoSided=0;
			else if (pValueTwoSided>1)	pValueTwoSided=1;
			
			texture.put((String)TwoIndependentWilcoxonResult.U_STAT_EXPECTATION_FORMULA, this.U_STAT_EXPECTATION_FORMULA);
			texture.put((String)TwoIndependentWilcoxonResult.U_STAT_VARIANCE_FORMULA, this.U_STAT_VARIANCE_FORMULA);

			texture.put((String)TwoIndependentWilcoxonResult.MEAN_U, new Double(meanU));
			texture.put((String)TwoIndependentWilcoxonResult.VAR_U, new Double(varU));
			texture.put((String)TwoIndependentWilcoxonResult.Z_SCORE, new Double(zScore));
			texture.put((String)Result.P_VALUE_TWO_SIDED, new Double(pValueTwoSided));
			texture.put((String)Result.P_VALUE_ONE_SIDED, new Double(pValueOneSided));

			//////System.out.println("TwoIndependentWilcoxon use large sample meanU = " + meanU);
			//////System.out.println("TwoIndependentWilcoxon use large sample varU = " + varU);
			//////System.out.println("TwoIndependentWilcoxon use large sample zScore = " + zScore);
			//////System.out.println("TwoIndependentWilcoxon use large sample pValue = " + pValue);

		}
		else {
			//double pValue = exactPValue(nX, nY, uStat1);
			double minPossible = 0;
			double maxPossible = 0;
			TreeMap<String,Object> xKey = new TreeMap<String,Object>(); // key of the groupX (supposed with smaller rank sum)
			double dec = 0, inc = 0;
			double currentValue = 0;
			TreeMap<String,Object> countMap = new TreeMap<String,Object>();
/*
			if (sum1 < sum2 && groupName1.equalsIgnoreCase(nameX)) { // x has smaller rank
				sampleSize1 = nX;
				sampleSize2 = nY;
			} else { // x has larger ran
				sampleSize2 = nX;
				sampleSize1 = nY;
			}
*/
			if (!hasTie && pValueAtLeft) {
				dec = 1;
				currentValue = Math.min(sum1, sum2);
				minPossible = AnalysisUtility.sumPossitiveIntegerSequenceFromOne((int)nX);
				//////System.out.println("Caller 1 minPossible = " + minPossible);

				int numberCombo = 0;
				while (currentValue >= minPossible) {
					xKey.put(currentValue + "", "");
					currentValue -= dec;
					numberCombo++;
				}

				cummulativePValue = findExactPValue(xKey, combo, (int)sampleSize1, (int)sampleSize2, commonTerm);
				//System.out.println("Caller 1: cummulativePValue = " + cummulativePValue);


			}
			else if (!hasTie && !pValueAtLeft) { // right tail, that is group X has larger rank sum
				inc = 1;
				currentValue = Math.max(sum1, sum2);
				maxPossible = AnalysisUtility.sumPossitiveIntegerSequencePartial((int)sampleSize1, (int)(sampleSize1 + sampleSize2));
				////////System.out.println("Caller 2 nX = " + nX);
				////////System.out.println("Caller 2 sampleSize1 = " + sampleSize1);

				//////System.out.println("Caller 2 maxPossible = " + maxPossible);

				while (currentValue <= maxPossible) {

					////////System.out.println("Caller 2 put = " + currentValue);


					xKey.put(currentValue + "", "");
					currentValue += inc;
				}
				cummulativePValue = findExactPValue(xKey, combo, (int)sampleSize2, (int)sampleSize1, commonTerm);
				// sample sizes have different order than the above case
				//System.out.println("Caller 2: sampleSize1 = " + sampleSize1 + ", sampleSize2 = " + sampleSize2 + ", cummulativePValue = " + cummulativePValue);
			}
			else if (hasTie && pValueAtLeft) {
				dec = .5;
				currentValue = Math.min(sum1, sum2);
				minPossible = AnalysisUtility.sumPossitiveIntegerSequenceFromOne((int)sampleSize1);
				//////System.out.println("Caller 3 minPossible = " + minPossible);
				while (currentValue >= minPossible) {
					//////System.out.println(" currentValue = " + currentValue);
					xKey.put(currentValue + "", "");
					currentValue -= dec;
				}
				cummulativePValue = findExactPValue(xKey, combo, (int)sampleSize1, (int)sampleSize2, commonTerm);
				//System.out.println("Caller 3: cummulativePValue = " + cummulativePValue);
			}
			else if (hasTie && !pValueAtLeft) { // right tail, that is group X has larger rank sum
				inc = .5;
				currentValue = Math.max(sum1, sum2);
				maxPossible = AnalysisUtility.sumPossitiveIntegerSequencePartial((int)sampleSize2, (int)(sampleSize1 + sampleSize2));
				while (currentValue <= maxPossible) {
					xKey.put(currentValue + "", "");
					currentValue += inc;
				}
				cummulativePValue = findExactPValue(xKey, combo, 
						(int)sampleSize2, (int)sampleSize1, commonTerm);
				// sample sizes have different order than the above case

				//System.out.println("Caller 4: cummulativePValue = " + cummulativePValue);
			}
			else {
				//System.out.println("Caller 5: cummulativePValue = " + cummulativePValue);

			}
			if (pValueAtLeft && groupName1.equalsIgnoreCase(nameX) && sum1 < sum2) {
				//cummulativePValue = 1 - cummulativePValue;
			}
			//System.out.println("Commonterm = " + commonTerm);
			//System.out.println("CummulativePValue = " + cummulativePValue);

			if (cummulativePValue<0) 		cummulativePValue=0;
			else if (cummulativePValue>0.5) cummulativePValue=0.5;
			
			texture.put(TwoIndependentWilcoxonResult.P_VALUE_ONE_SIDED, new Double(cummulativePValue));
			texture.put(TwoIndependentWilcoxonResult.P_VALUE_TWO_SIDED, new Double(2*cummulativePValue));
		}
		return result;
	}

	// test code only.
	public static void test(DataCase[] combo, int nX, int nY, String nameX, String nameY) {
		double sampleSize1 = 0;
		double sum1 = 0;

		if (nX < nY) {
			sampleSize1 = nX;
			for (int i = 0; i < combo.length; i++) {
				if ( (combo[i].getGroup()).equals(nameX)) {
					sum1 = sum1 + combo[i].getRank();
				}
			}

		}
		else {
			sampleSize1 = nY;
			for (int i = 0; i < combo.length; i++) {
				if ( (combo[i].getGroup()).equals(nameY)) {
					sum1 = sum1 + combo[i].getRank();
				}
			}
		}
		/* not used.
		double rPrime = sampleSize1 * (nX + nY + 1) - sum1;
		double rStar = Math.min(rPrime, sum1);
		////////////////System.out.println("sum1 = " + sum1);
		////////////////System.out.println("rPrime = " + rPrime);
		////////////////System.out.println("rStar = " + rStar);
		*/

	}

	private static double factorialRatio(int m, int n) {
		// finds m! * (n - m)! / n! assuming n >= m
		if (n < m) {
			//////////System.out.println("n < m");
			return 0;
		}

			//////////System.out.println("n >= m");

		int mFactorial = AnalysisUtility.factorial(m);
		int diffFactorial = AnalysisUtility.factorial(n - m);
		int nFactorial = AnalysisUtility.factorial(n);
		////////System.out.println("mFactorial = " + mFactorial);
		////////System.out.println("diffFactorial = " + diffFactorial);
		////////System.out.println("n = " + n + ", nFactorial = " + nFactorial);

		return ((double)(mFactorial) / (double)nFactorial ) * (double) diffFactorial;
	}

	private static double numberPermutation(int m, int n, double k) {
		//////////////System.out.println("numberPermutation m = " + m + ", n = " + n + ", k = " + k);
	// find Prob(k), where sample size = m and n.
		if (k < 0) { // rank sum cannot be negative.
			////////////////System.out.println("case 2");
			return 0;
		}
		else if (m <= 0 && n <= 0) { // cannot both be 0.
			////////////////System.out.println("case 1");
			return 0;
		}

		else if ( (m == 0 || n == 0) && k == 0) {
			////////////////System.out.println("case 3");
			return 1;
		}
		else if ( (m == 0 || n == 0) && k != 0) { // then k == m xor k == n.
			////////////////System.out.println("case 4");
			return 0;
		}
		else {
			double prob1 = numberPermutation(m, n - 1, k - m);
			double prob2 = numberPermutation(m - 1, n, k);

			return prob1 + prob2;
		}
	}
	
/*
	public static void main(String args[]) {
		// example from Rice page 390. 405
		int nX = 13; int nY = 8;

		//DataCase dataCase = new DataCase(1, "A");

		DataCase[] groupA = new DataCase[nX];
		DataCase[] groupB = new DataCase[nY];
		String a = "A";
		String b = "B";

		groupA[0] = new DataCase(79.98, a);
		groupA[1] = new DataCase(80.04, a);
		groupA[2] = new DataCase(80.02, a);
		groupA[3] = new DataCase(80.04, a);
		groupA[4] = new DataCase(80.03, a);
		groupA[5] = new DataCase(80.03, a);
		groupA[6] = new DataCase(80.04, a);
		groupA[7] = new DataCase(79.97, a);
		groupA[8] = new DataCase(80.05, a);
		groupA[9] = new DataCase(80.03, a);
		groupA[10] = new DataCase(80.02, a);
		groupA[11] = new DataCase(80.00, a);
		groupA[12] = new DataCase(80.02, a);
		//groupA[13] = new DataCase(80.05, a);

		groupB[0] = new DataCase(80.02, b);
		groupB[1] = new DataCase(79.94, b);
		groupB[2] = new DataCase(79.98, b);
		groupB[3] = new DataCase(79.97, b);
		groupB[4] = new DataCase(79.97, b);
		groupB[5] = new DataCase(80.03, b);
		groupB[6] = new DataCase(79.95, b);
		groupB[7] = new DataCase(79.97, b);
		DataCase[] combo = QSortAlgorithm.rankCombinedLists(groupA, groupB);

		////////////////System.out.println("\nIn Main After assigning ranks");
		for (int i = 0; i < combo.length; i++) {
			////////////////System.out.println(combo[i].getValue() + " " + combo[i].getGroup() + " " + combo[i].getRank());
		}
		test(combo, nX, nY, a, b);
	}
*/
	
	// P(U-stat >= some u)
	public static double cummulativeGE(int nX, int nY, double u) {
		int n = nX + nY;

		int lastW = 0;
		for (int i = n; i > n - nX; i--) {
			lastW = i + lastW;
		}
		double output = 0;
		double increment = u;
		//////////////System.out.println("cummulativeGE  = " + u + ", lastW = " + lastW);

		/*while (increment <= lastW) {
			output = output + exactProb(nX, nY, increment);
			increment++;
		}*/
		double prob = 0;
		do {
			if (nX <= nY) {
				prob = exactProb(nX, nY, increment);
			}
			else {
				prob = exactProb(nY, nX, increment);
			}
			//////////////System.out.println("cummulativeGE do prob = " + prob);

			output = output + prob;
			increment++;
		} while (increment <= lastW);

		//////////////System.out.println("cummulativeGE = " + output);
		return output;
	}
	// P(U-stat <= some u)
	public static double cummulativeLE(int nX, int nY, double u) {
		int n = nX + nY;
		int firstW = (int) (.5 * nX * (nX + 1));

		double output = 0;
		double decrement = u;
		//////////////System.out.println("cummulativeLE decrement = " + u + ", firstW = " + firstW);
		while (decrement >= firstW) {
			//////////////System.out.println("while cummulativeLE decrement = " + decrement + ", firstW = " + firstW);
			output = output + exactProb(nX, nY, decrement);
			decrement--;
		}
		/*
		double prob = 0;
		do{
			////////////////System.out.println("cummulativeLE do exactProb(nX, nY, decrement) = "  +exactProb(nX, nY, decrement));
			if (nX <= nY) {
				prob = exactProb(nX, nY, decrement);
			}
			else {
				prob = exactProb(nY, nX, decrement);
			}
			//////////////System.out.println("cummulativeLE do prob = " + prob);
			output = output + prob;
			decrement--;
		} while (decrement >= firstW);
*/
		//////////////System.out.println("cummulativeLE = " + output);
		return output;
	}
	public static double exactProb(int nX, int nY, double u) {
	// exact probability and assume nX is not greater than nY
	// u: u statistic
		//////////////System.out.println("exactProb nX = " + nX);
		//////////////System.out.println("exactProb nY = " + nY);
		//////////////System.out.println("exactProb u = " + u);

		int countw = 0;
		int n = nX + nY;
		int firstW = (int) (.5 * nX * (nX + 1));
		int lastW = 0;
		for (int i = n; i > n - nX; i--) {
			lastW = i + lastW;
		}

		// for loop is for test only.
/*
		for (int k = firstW; k <= lastW; k++) {
			countw = numberPermutation(nX, nY, k - firstW);
			////////////////System.out.println("P("+(k) +") = " + countw );
		}
*/

		/*if (nX > SIZE_BIG_INT || nY > SIZE_BIG_INT || n > SIZE_BIG_INT) {

			//BigInteger factorialPart =  (AnalysisUtility.factorialBigInt(nX).multiply(AnalysisUtility.factorialBigInt(nY))).divide(AnalysisUtility.factorialBigInt(n));
			double nume =  (double) (AnalysisUtility.factorial(nX) * AnalysisUtility.factorial(nY)) ;
			double deno =  (double) AnalysisUtility.factorial(n) ;

			////////////////System.out.println(" exactProb if nume = "  + nume);
			////////////////System.out.println(" exactProb if deno = "  + deno);
			double factorialPart = nume/ deno;

			//////////////System.out.println(" exactProb if factorialPart = "  + factorialPart);

			return  factorialPart;//factorialPart.intValue();
		}
		else {*/
			////////////////System.out.println(" exactProb else nX = ");
			////////////////System.out.println(" exactProb else nY = ");
			////////////////System.out.println(" exactProb else n = ");

			double a = AnalysisUtility.factorial(nX);
			double b = AnalysisUtility.factorial(nY);
			double c = AnalysisUtility.factorial(n);

			double factorialPart = (a * b) / c;

			double possibleNumPermutation = numberPermutation(nX, nY, u - firstW);
			//////////////System.out.println("possibleNumPermutation = " + possibleNumPermutation);

			//////////////System.out.println("factorialPart = " + factorialPart);

			//////////////System.out.println("numberPermutation(nX, nY, u) = " + numberPermutation(nX, nY, u - firstW)+"\n");
			return possibleNumPermutation * factorialPart ;
		//}

	}
	
	public static double exactPValue(int nX, int nY, double u) {


		int countw = 0;
		int n = nX + nY;
		int firstW = (int) (.5 * nX * (nX + 1));
		int lastW = 0;
		for (int i = n; i > n - nX; i--) {
			lastW = i + lastW;
			//////////////System.out.println("lastW = " + lastW);

		}
		double middle = (firstW + lastW) / 2;
		//////////////System.out.println("exactPValue firstW = " + firstW + ", lastW = " + lastW);

		//////////////System.out.println("exactPValue u = " + u + ", middle = " + middle);
		if (u <= middle) {
			return (cummulativeLE(nX, nY, u));

		}
		else {
			return (cummulativeGE(nX, nY, (firstW + lastW) - u ));
		}
	}

	private double findExactPValue(TreeMap<String,Object> xKey, DataCase[] combo, 
			int sampleSize1, int sampleSize2, double commonTerm){
		
		TreeMap<String,Object> countMap = new TreeMap<String,Object>();
		double cummulativePValue = 0;
		Set<String> keySet = xKey.keySet();
		Iterator<String> iterator = keySet.iterator();
		String key = "";
		int numberPossibilities = 0;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			double tempSum = 0;
			////////System.out.println("sampleSize1 = " + sampleSize1);
			////////System.out.println("sampleSize2 = " + sampleSize2);

			// slow and dirty, a better way of implementation should be done and
			// this way of computation should be temporary.
			int sampleSizeTotal = sampleSize1 + sampleSize2;
			////////System.out.println("sampleSizeTotal = " + sampleSizeTotal);

			switch (sampleSize1) {
				case 1: {
					numberPossibilities = 1;
					break;
				}
				case 2: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							tempSum = combo[i].getRank() + combo[j].getRank();
							////////System.out.println("case 2 tempSum = " + tempSum + " " + combo[i].getRank() +" " + combo[j].getRank() );
							if (tempSum == Double.parseDouble(key)) numberPossibilities++;
					} }
					break;
				}
				case 3: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							for (int k = j+1; k < sampleSizeTotal; k++) {
								tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank();
								////////System.out.println("case 3 tempSum = " + tempSum + " " + combo[i].getRank() +" " + combo[j].getRank() + " " +  combo[k].getRank() );
								if (tempSum == Double.parseDouble(key)) {
									//////System.out.println("case 3 if tempSum = " + tempSum + " " + combo[i].getRank() +" " + combo[j].getRank() + " " +  combo[k].getRank() );
									numberPossibilities++;
								}
					} } }
					break;
				}
				case 4: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							for (int k = j+1; k < sampleSizeTotal; k++) {
								for (int m = k+1; m < sampleSizeTotal; m++) {
									tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank() + combo[m].getRank();
									////////System.out.println("case 4 tempSum = " + tempSum);
									if (tempSum == Double.parseDouble(key)) {
											//////System.out.println("case 4 if tempSum = " + tempSum + " " + combo[i].getRank() + " " + combo[j].getRank() + " " +  combo[k].getRank() + " " +  combo[m].getRank() );
											numberPossibilities++;
									}
					} } } }
					break;
				}
				case 5: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							for (int k = j+1; k < sampleSizeTotal; k++) {
								for (int m = k+1; m < sampleSizeTotal; m++) {
									for (int n = m+1; n < sampleSizeTotal; n++) {
										tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank() + combo[m].getRank() + combo[n].getRank();
										////////System.out.println("case 5 if tempSum = " + tempSum);
										if (tempSum == Double.parseDouble(key))  {
											//////System.out.println("case 5 tempSum = " + tempSum + "  " + combo[i].getRank()  + " " +combo[j].getRank() + " " +  combo[k].getRank() + " " +  combo[m].getRank() + " " + combo[n].getRank());

											numberPossibilities++;
										}
					} } } } }
					break;
				}

				case 6: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							for (int k = j+1; k < sampleSizeTotal; k++) {
								for (int m = k+1; m < sampleSizeTotal; m++) {
									for (int n = m+1; n < sampleSizeTotal; n++) {
										for (int p = n+1; p < sampleSizeTotal; p++) {

											tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank() + combo[m].getRank() + combo[n].getRank() + combo[p].getRank();
											if (tempSum == Double.parseDouble(key)) {
												//////System.out.println("case 5 tempSum = " + tempSum + "  " + combo[i].getRank() + " " + combo[j].getRank() + " " +  combo[k].getRank() + " " +  combo[m].getRank() + " " +  combo[n].getRank() + " " +  combo[p].getRank());
												numberPossibilities++;
											}
					} } } } } }
					break;
				}
				case 7: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							for (int k = j+1; k < sampleSizeTotal; k++) {
								for (int m = k+1; m < sampleSizeTotal; m++) {
									for (int n = m+1; n < sampleSizeTotal; n++) {
										for (int p = n+1; p < sampleSizeTotal; p++) {
											for (int q = p+1; q < sampleSizeTotal; q++) {

											tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank() + combo[m].getRank() + combo[n].getRank() + combo[p].getRank() + combo[q].getRank();
											if (tempSum == Double.parseDouble(key)) numberPossibilities++;
					} } } } } } }
					break;
				}
				case 8: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							for (int k = j+1; k < sampleSizeTotal; k++) {
								for (int m = k+1; m < sampleSizeTotal; m++) {
									for (int n = m+1; n < sampleSizeTotal; n++) {
										for (int p = n+1; p < sampleSizeTotal; p++) {
											for (int q = p+1; q < sampleSizeTotal; q++) {
												for (int r = q+1; r < sampleSizeTotal; r++) {
												tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank() + combo[m].getRank() + combo[n].getRank() + combo[p].getRank() + combo[q].getRank() + combo[r].getRank();
												if (tempSum == Double.parseDouble(key)) numberPossibilities++;
					} } } } } } } }
					break;
				}
				case 9: {
					for (int i = 0; i < sampleSizeTotal; i++) {
						for (int j = i+1; j < sampleSizeTotal; j++) {
							for (int k = j+1; k < sampleSizeTotal; k++) {
								for (int m = k+1; m < sampleSizeTotal; m++) {
									for (int n = m+1; n < sampleSizeTotal; n++) {
										for (int p = n+1; p < sampleSizeTotal; p++) {
											for (int q = p+1; q < sampleSizeTotal; q++) {
												for (int r = q+1; r < sampleSizeTotal; r++) {
													for (int s = r+1; s < sampleSizeTotal; s++) {
													tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank() + combo[m].getRank() + combo[n].getRank() + combo[p].getRank() + combo[q].getRank() + combo[r].getRank() + combo[s].getRank();
													if (tempSum == Double.parseDouble(key)) numberPossibilities++;
					} } } } } } } } }
					break; // i need a break, man. XD
				}
				default: {
					// this should not happen because the sample size for the
					// exact method is restricted to be 10 or less for our calculation.
					// perhaps out put an error here.
				}
			} // end switch

			/*
			for (int i = 0; i < (sampleSize1 + sampleSize2); i++) {
				for (int j = i+1; j < (sampleSize1 + sampleSize2); j++) {
					for (int k = j+1; k < (sampleSize1 + sampleSize2); k++) {

						tempSum = combo[i].getRank() + combo[j].getRank() + combo[k].getRank();
						////////System.out.println("tempSum = " + tempSum);
						////////System.out.println("combo["+i+"].getRank() = " + combo[i].getRank());
						////////System.out.println("combo["+j+"].getRank() = " + combo[j].getRank());
						////////System.out.println("combo["+k+"].getRank() = " + combo[k].getRank());

						if (tempSum == Double.parseDouble(key)) {
							////////System.out.println("key = " + key + ", " + combo[i].getRank() + ", " + combo[j].getRank() + combo[k].getRank());
							numberPossibilities++;

						}
					}
				}
			}
			*/
			countMap.put(key, new Integer(numberPossibilities));
			////////System.out.println("numberPossibilities = " + numberPossibilities);
			numberPossibilities = 0;
		}

		keySet = countMap.keySet();
		iterator = keySet.iterator();
		key = "";
		int currentCount = 0;

		while (iterator.hasNext()) {
			key = (String) iterator.next();
			currentCount = ((Integer)countMap.get(key)).intValue();
			////////System.out.println("countMap key = " + key + ", " + currentCount);
			cummulativePValue += currentCount * commonTerm;
		}
		//System.out.println("PVALUE = " + cummulativePValue);
		return cummulativePValue;
	}
	
	public static void main(String args[]) {
		double singleProb = 0;
		int nX = 3;
		int nY = 4;
		//double rankSum = exactProb(5,6,15) + exactProb(5,6,16) + exactProb(5,6,17) + exactProb(5,6,18) ;//+ exactProb(3,4,15) + exactProb(3,4,16) + exactProb(3,4,17) + exactProb(3,4,18);
		//double rankSum = exactProb(2,3,4.5) +  exactProb(2,3,4) +  exactProb(2,3,3);

		//////////System.out.println("factorialRatio(2, 3, 5); = " + factorialRatio(2, 5));

		////////////////System.out.println("exactProb(2,3,4) = " + exactProb(2,3,4));
		////////////////System.out.println("exactProb(2,3,3) = " + exactProb(2,3,3));
		////////////////System.out.println("rankSum = " + rankSum);
		//////////////////System.out.println(cummulativeLE(5,6,18.0));
		////////////////System.out.println(cummulativeGE(8, 13, 52.0));
	}
}
