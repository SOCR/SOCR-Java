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
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.data.DataCase;


public class TwoIndependentFriedman implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	private static double alpha = 0.05;

	private String type = "TwoIndependentFriedman";
	//	private String[] varNames = null;
	private TreeSet<String> varNameList = new TreeSet<String>();
	private ArrayList<Object> varValueList = new ArrayList<Object>();

	private boolean approx = false; // use approximation or exact, see Conover page 231.

	private String[] varList = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		////////System.out.println("In TwoIndependentFriedman analyze ");

		Result result = null;
		if (analysisType != AnalysisType.TWO_INDEPENDENT_FRIEDMAN)
			throw new WrongAnalysisException();
		TreeMap<String,Object> xMap = data.getTreeX();
		//double sigLevel = data.getSignificanceLevel();
		//HashMap yMap = data.getMapY();
		if (xMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		ArrayList<Object> x = new ArrayList<Object>();
		//double y[] = null;
		int xIndex = 0;
		Column xColumn = null;

		while (iterator.hasNext()) {

			keys = (String) iterator.next();
			//////System.out.println("TwoIndependentFriedman keys "+keys);
			try {
				Class cls = keys.getClass();
				//////////System.out.println(cls.getName());
			} catch (Exception e) {
				//////System.out.println("TwoIndependentFriedman Exception = " + e);
			}
			xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			double xVector[] = xColumn.getDoubleArray();
			//////System.out.println("TwoIndependentFriedman  xVector.length = "+xVector.length );

			for (int i = 0; i < xVector.length; i++) {
				//////System.out.println("TwoIndependentFriedman xVector["+i+"] = " +xVector[i]);
			}
			// x is an ArrayList, key is index, Object is an x vector
			// if you have x1 and x2, then key = 1 points to x1, key = 2 points to x2.
			x.add(xIndex, xVector);

			varNameList.add(keys);
			varValueList.add(xIndex, xVector);

			xIndex++;
		}

		//int varListLength = varNameList.size();
		int xLength = xIndex;
		//////System.out.println("xLength = " + xLength);
		
		DataCase[][] group = new DataCase[xLength][];
		String[] groupNames = new String[xLength];
		double[] xVector = null;
		Iterator<String> it = varNameList.iterator();
		for (int i = 0; i < xLength; i++) {
			groupNames[i] = (String)it.next();
			xVector = (double[])varValueList.get(i);
			group[i] = new DataCase[xVector.length];
			for (int j = 0; j < xVector.length; j++) {
				group[i][j] = new DataCase(xVector[j], groupNames[i]);
				////System.out.println("TwoIndependentFriedman groupNames["+i+"] = "  + groupNames[i] + " group["+i+"]["+j+"] = " + group[i][j].getValue());
			}
			////System.out.println("");
		}

		FriedmanTest friedmanTest = new FriedmanTest(group, groupNames);
		DataCase[][] resultEntry = friedmanTest.getResultTable();
		double[] rankGroupAverage = friedmanTest.getRankGroupAverage();
		double[] rankGroupSum = friedmanTest.getRankGroupSum();

		double rankGrandAverage = friedmanTest.getRankGrandAverage();

		double singleGroupSize = friedmanTest.getSingleGroupSize();
		//System.out.println("getRankGrandAverage = " + rankGrandAverage);

		double sumSq = 0;
		int numberGroups = group.length;
		for (int i = 0; i < numberGroups; i++) {
			sumSq += ( (rankGroupAverage[i] - rankGrandAverage) * (rankGroupAverage[i] - rankGrandAverage));
		}

		//System.out.println("sumSq = " + sumSq);
		double chiSquareStat = 12 * singleGroupSize * sumSq / numberGroups / (numberGroups + 1);
		//System.out.println("chiSquareStat = " + chiSquareStat);

		HashMap<String,Object> texture = new HashMap<String,Object>();
		int sampleSize = (int) (singleGroupSize * numberGroups);
		int df = (int)numberGroups - 1;
		double pValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(
				df)).getCDF(chiSquareStat);
		//System.out.println("df = " + df);
		//System.out.println("pValue = " + pValue);
		//System.out.println("sampleSize = " + sampleSize);


		texture.put(TwoIndependentFriedmanResult.P_VALUE, new Double(pValue));

		texture.put(TwoIndependentFriedmanResult.SAMPLE_SIZE, new Integer(sampleSize));
		texture.put(TwoIndependentFriedmanResult.GROUP_NAME_LIST, groupNames);
		texture.put(TwoIndependentFriedmanResult.DF, new Integer(df));
		texture.put(TwoIndependentFriedmanResult.RANK_ARRAY, resultEntry);
		texture.put(TwoIndependentFriedmanResult.DATA_ARRAY, group);
		texture.put(TwoIndependentFriedmanResult.GRAND_RANK_MEAN, new Double(rankGrandAverage));
		texture.put(TwoIndependentFriedmanResult.GROUP_RANK_TOTAL, rankGroupSum);
		texture.put(TwoIndependentFriedmanResult.GROUP_RANK_MEAN, rankGroupAverage);
		texture.put(TwoIndependentFriedmanResult.CHI_STAT, new Double(chiSquareStat));
		texture.put(TwoIndependentFriedmanResult.SUM_SQUARES, new Double(sumSq));
		texture.put(TwoIndependentFriedmanResult.SINGLE_GROUP_SIZE, new Integer((int)singleGroupSize));

		result = new TwoIndependentFriedmanResult(texture);

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
