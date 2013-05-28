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

public class KolmogorovSmirnoffResult extends ParametricTestResult {


	public static final String VAR_1_Y = "VAR_1_Y";
	public static final String VAR_2_Y = "VAR_2_Y";
	public static final String VAR_1_X = "VAR_1_X";
	public static final String VAR_2_X = "VAR_2_X";
	public static final String VAR_1_LOG_X = "VAR_1_LOG_X";
	public static final String VAR_2_LOG_X = "VAR_2_LOG_X";
	public static final String DIFF = "DIFF";
	public static final String ABS_DIFF = "ABS_DIFF";
	public static final String D_STAT = "D_STAT";

	public static final String MEAN_1 = "MEAN_1";
	public static final String MEAN_2 = "MEAN_2";

	public static final String SD_1 = "SD_1";
	public static final String SD_2 = "SD_2";

	public static final String MEDIAN_1 = "MEDIAN_1";
	public static final String MEDIAN_2 = "MEDIAN_2";

	public static final String MAX_1 = "MAX_1";
	public static final String MAX_2 = "MAX_2";

	public static final String MIN_1 = "MIN_1";
	public static final String MIN_2 = "MIN_2";

	public static final String FIRST_QUARTILE_1 = "FIRST_QUARTILE_1";
	public static final String FIRST_QUARTILE_2 = "FIRST_QUARTILE_2";
	public static final String THIRD_QUARTILE_1 = "THIRD_QUARTILE_1";
	public static final String THIRD_QUARTILE_2 = "THIRD_QUARTILE_2";

	public static final String CI_95_1 = "CI_95_1"; // 95% CI, not unleaded gas. use double array then.
	public static final String CI_95_2 = "CI_95_2";

	public static final String AVG_ABS_DEV_MEDIAN_1 = "AVG_ABS_DEV_MEDIAN_1";
	public static final String AVG_ABS_DEV_MEDIAN_2 = "AVG_ABS_DEV_MEDIAN_2";

	public static final String BOX_LOWER_OUTLIER_STRING_1 = "BOX_LOWER_OUTLIER_STRING_1";
	public static final String BOX_LOWER_OUTLIER_STRING_2 = "BOX_LOWER_OUTLIER_STRING_2";
	public static final String BOX_UPPER_OUTLIER_STRING_1 = "BOX_UPPER_OUTLIER_STRING_1";
	public static final String BOX_UPPER_OUTLIER_STRING_2 = "BOX_UPPER_OUTLIER_STRING_2";

	public static final String NORMAL_SCORE_1 = "NORMAL_SCORE_1";
	public static final String NORMAL_SCORE_2 = "NORMAL_SCORE_2";
	public static final String LOG_NORMAL_SCORE_1 = "LOG_NORMAL_SCORE_1";
	public static final String LOG_NORMAL_SCORE_2 = "LOG_NORMAL_SCORE_2";

	public static final String Z_FORMULA = "Z_FORMULA";
	public static final String Z_STAT = "Z_STAT";

	public static final String PROB = "PROB";
	public static final String ONE_MINUS_PROB = "ONE_MINUS_PROB";
	/*********************** Constructors ************************/
	public KolmogorovSmirnoffResult(HashMap texture) {
		super(texture);
	}
	public KolmogorovSmirnoffResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/


	public double[] getVariable1Y() {
		return ((double[])texture.get(VAR_1_Y));
	}
	public double[] getVariable2Y() {
		return ((double[])texture.get(VAR_2_Y));
	}

	public double[] getVariable1X() {
		return ((double[])texture.get(VAR_1_X));
	}
	public double[] getVariable2X() {
		return ((double[])texture.get(VAR_2_X));
	}

	public double[] getVariable1LogX() {
		return ((double[])texture.get(VAR_1_LOG_X));
	}

	public double[] getVariable2LogX() {
		return ((double[])texture.get(VAR_2_LOG_X));
	}


	public double[] getDiff() {
		return ((double[])texture.get(DIFF));
	}
	public double[] getAbsDiff() {
		return ((double[])texture.get(ABS_DIFF));
	}

	public double getDStat() {
		//System.out.println("dstat = " + ((Double)texture.get(D_STAT)).doubleValue());
		return ((Double)texture.get(D_STAT)).doubleValue();

	}

	/********************************************************/

	public double getMean1() {
		return ((Double)texture.get(MEAN_1)).doubleValue();
	}
	public double getMean2() {
		return ((Double)texture.get(MEAN_2)).doubleValue();
	}

	public double getSD1() {
		return ((Double)texture.get(SD_1)).doubleValue();
	}

	public double getSD2() {
		return ((Double)texture.get(SD_2)).doubleValue();
	}


	public double getMax1() {
		return ((Double)texture.get(MAX_1)).doubleValue();
	}
	public double getMax2() {
		return ((Double)texture.get(MAX_2)).doubleValue();
	}

	public double getMin1() {
		return ((Double)texture.get(MIN_1)).doubleValue();
	}
	public double getMin2() {
		return ((Double)texture.get(MIN_2)).doubleValue();
	}


	public double[] getCI95X1() {
		return ((double[])texture.get(CI_95_1));
	}

	public double[] getCI95X2() {
		return ((double[])texture.get(CI_95_2));
	}

	public double getMedian1() {
		return ((Double)texture.get(MEDIAN_1)).doubleValue();
	}
	public double getMedian2() {
		return ((Double)texture.get(MEDIAN_2)).doubleValue();
	}

	public double getFirstQuartile1() {
		return ((Double)texture.get(FIRST_QUARTILE_1)).doubleValue();
	}
	public double getFirstQuartile2() {
		return ((Double)texture.get(FIRST_QUARTILE_2)).doubleValue();
	}
	public double getThirdQuartile1() {
		return ((Double)texture.get(THIRD_QUARTILE_1)).doubleValue();
	}
	public double getThirdQuartile2() {
		return ((Double)texture.get(THIRD_QUARTILE_2)).doubleValue();
	}


	public double[] getNormalQuantile1() {
		return ((double[])texture.get(NORMAL_SCORE_1));
	}
	public double[] getNormalQuantile2() {
		return ((double[])texture.get(NORMAL_SCORE_2));
	}


	public double[] getLogNormalQuantile1() {
		return ((double[])texture.get(LOG_NORMAL_SCORE_1));
	}
	public double[] getLogNormalQuantile2() {
		return ((double[])texture.get(LOG_NORMAL_SCORE_2));
	}
	public String getZFormula() {
		return ((String)texture.get(Z_FORMULA));
	}
	public double getZStat() {
		return ((Double)texture.get(Z_STAT)).doubleValue();
	}
	public double getProb() {
		return ((Double)texture.get(PROB)).doubleValue();
	}
	public double getOneMinusProb() {
		return ((Double)texture.get(ONE_MINUS_PROB)).doubleValue();
	}
	public String getBoxLowerOutlier1() {
		return ((String)texture.get(BOX_LOWER_OUTLIER_STRING_1));
	}
	public String getBoxLowerOutlier2() {
		return ((String)texture.get(BOX_LOWER_OUTLIER_STRING_2));
	}
	public String getBoxUpperOutlier1() {
		return ((String)texture.get(BOX_UPPER_OUTLIER_STRING_1));
	}
	public String getBoxUpperOutlier2() {
		return ((String)texture.get(BOX_UPPER_OUTLIER_STRING_2));
	}
}
