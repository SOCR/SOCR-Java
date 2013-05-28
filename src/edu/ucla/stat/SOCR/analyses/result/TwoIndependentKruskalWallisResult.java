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
package edu.ucla.stat.SOCR.analyses.result;

import java.util.HashMap;

public class TwoIndependentKruskalWallisResult extends NonParametricTestResult {
	public static final String GROUP_NAME_LIST = "GROUP_NAME_LIST";
	public static final String RANK_SUM_LIST = "RANK_SUM_LIST";
	public static final String CRITICAL_VALUE = "CRITICAL_VALUE";
	public static final String T_STAT = "T_STAT";
	public static final String P_VALUE = "P_VALUE";
	public static final String DATA_RANK_LIST = "DATA_RANK_LIST";
	public static final String DATA_RANK_SEPARATE_LIST = "DATA_RANK_SEPARATE_LIST";
	public static final String DF = "DF";
	public static final String GROUP_COUNT = "GROUP_COUNT";
	public static final String S_SQUARED = "S_SQUARED";
	public static final String MULTIPLE_COMPARISON_INFO = "MULTIPLE_COMPARISON_INFO";
	public static final String MULTIPLE_COMPARISON_HEADER = "MULTIPLE_COMPARISON_HEADER";
	public static final String SAMPLE_SIZE = "SAMPLE_SIZE";
	/*********************** Constructors ************************/

	public TwoIndependentKruskalWallisResult(HashMap texture) {
		super(texture);
	}
	public TwoIndependentKruskalWallisResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}


	/*********************** Accessors ************************/

	public String[] getGroupNameList() {
		return ((String[])texture.get(GROUP_NAME_LIST));
	}
	public double[] getRankSumList() {
		return ((double[])texture.get(RANK_SUM_LIST));
	}
	public double getTStat() {
		return ((Double)texture.get(T_STAT)).doubleValue();
	}

	public String getSampleSize() {
		String sampleSize = ((String)texture.get(SAMPLE_SIZE));
	//	System.out.println("result getSampleSize = " + sampleSize);
		return sampleSize;
	}
	public double getChiSquarePValue() {
		return ((Double)texture.get(P_VALUE)).doubleValue();
	}
	public double getCriticalValue() {
		return ((Double)texture.get(CRITICAL_VALUE)).doubleValue();
	}
	public String getDataRankInformation() {
		return ((String)texture.get(DATA_RANK_LIST));
	}
	public String[] getDataRankSepratedInformation() {
		//System.out.println("
		return ((String[])texture.get(DATA_RANK_SEPARATE_LIST));
	}

	public String getDegreesOfFreedom() {
		return ((String)texture.get(DF));
	}

	public int[] getGroupCount() {
		return ((int[])texture.get(GROUP_COUNT));
	}


	public double getSSqaured() {
		return ((Double)texture.get(S_SQUARED)).doubleValue();
	}


	public String[] getMultipleComparisonInformation() {
		return ((String[])texture.get(MULTIPLE_COMPARISON_INFO));
	}


	public String getMultipleComparisonHeader() {
		return ((String)texture.get(MULTIPLE_COMPARISON_HEADER));
	}


}
