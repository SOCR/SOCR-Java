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

public class TwoIndependentWilcoxonResult extends NonParametricTestResult {
	public static final String MEAN_X = "MEAN_X";
	public static final String MEAN_Y = "MEAN_Y";

	public static final String SAMPLE_VAR = "SAMPLE_VAR";
	public static final String T_STAT = "T_STAT";
	public static final String DF = "DF";
	public static final String P_VALUE = "P_VALUE";
	public static final String RANK_SUM_SMALL = "RANK_SUM_SMALL"; // rank sum of the smaller sample
	public static final String RANK_SUM_LARGE = "RANK_SUM_LARGE"; // rank sum of the larger sample
	public static final String U_STAT_SMALL = "U_STAT_SMALL";
	public static final String U_STAT_LARGE = "U_STAT_LARGE";
	public static final String GROUP_NAME_SMALL = "GROUP_NAME_SMALL";
	public static final String GROUP_NAME_LARGE = "GROUP_NAME_LARGE";
	//public boolean
	public static final String MEAN_U = "MEAN_U";
	public static final String VAR_U = "VAR_U";
	public static final String Z_SCORE = "Z_SCORE";
	public static final String IS_LARGE_SAMPLE = "IS_LARGE_SAMPLE";
	//public static final String U_FORMULA = "U_FORMULA";
	public static final String DATA_SUMMARY_1 = "DATA_SUMMARY_1";
	public static final String DATA_SUMMARY_2 = "DATA_SUMMARY_2";
	public static final String U_STAT_EXPECTATION_FORMULA = "U_STAT_EXPECTATION_FORMULA";
	public static final String U_STAT_VARIANCE_FORMULA = "U_STAT_VARIANCE_FORMULA";

	/*********************** Constructors ************************/

	public TwoIndependentWilcoxonResult(HashMap texture) {
		super(texture);
	}
	public TwoIndependentWilcoxonResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}


	/*********************** Accessors ************************/
	public double getMeanX() {
		return ((Double)texture.get(MEAN_X)).doubleValue();
	}
	public double getMeanY() {
		return ((Double)texture.get(MEAN_Y)).doubleValue();
	}
	public boolean isLargeSample() {
		return ((Boolean)texture.get(IS_LARGE_SAMPLE)).booleanValue();
	}
	/** get MeanU when the sample is large sample. */
	public double getMeanU() {
		return ((Double)texture.get(MEAN_U)).doubleValue();
	}
	/** get VarianceU when the sample is large sample. */
	public double getVarianceU() {
		return ((Double)texture.get(VAR_U)).doubleValue();
	}
	/** get MeanU when the sample is large sample. */
	public double getZScore() {
		return ((Double)texture.get(Z_SCORE)).doubleValue();
	}
	/** get MeanU when using the exact method. */
	public double getPValueOneSided() {
		return ((Double)texture.get(P_VALUE_ONE_SIDED)).doubleValue();
	}
	public double getPValueTwoSided() {
		return ((Double)texture.get(P_VALUE_TWO_SIDED)).doubleValue();
	}

	/** get the sum of the ranks of the smaller group. */
	public double getRankSumSmallerGroup() {
		return ((Double)texture.get(RANK_SUM_SMALL)).doubleValue();
	}

	/** get the sum of the ranks of the larger group. */
	public double getRankSumLargerGroup() {
		return ((Double)texture.get(RANK_SUM_LARGE)).doubleValue();
	}

	/** get the U statistic of the smaller group. */
	public double getUStatSmallerGroup() {
		return ((Double)texture.get(U_STAT_SMALL)).doubleValue();
	}
	/** get the U statistic of the larger group. */
	public double getUStatLargerGroup() {
		return ((Double)texture.get(U_STAT_LARGE)).doubleValue();
	}

	public String getGroupNameSmall() {
		return (String)texture.get(GROUP_NAME_SMALL);
	}
	public String getGroupNameLarge() {
		return (String)texture.get(GROUP_NAME_LARGE);
	}
	public String getUMeanFormula() {
		return (String)texture.get(U_STAT_EXPECTATION_FORMULA);
	}
	public String getUVarianceFormula() {
		return (String)texture.get(U_STAT_VARIANCE_FORMULA);
	}

	public String getDataSummaryOfGroup1() {
		return (String)texture.get(DATA_SUMMARY_1);
	}
	public String getDataSummaryOfGroup2() {
		return (String)texture.get(DATA_SUMMARY_2);
	}
}
