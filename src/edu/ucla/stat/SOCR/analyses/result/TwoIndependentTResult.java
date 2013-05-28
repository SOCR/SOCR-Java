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

public class TwoIndependentTResult extends ParametricTestResult {

	public static final String MEAN_X = "MEAN_X";
	public static final String MEAN_Y = "MEAN_Y";

	public static final String SAMPLE_VAR_X = "SAMPLE_VAR_X";
	public static final String SAMPLE_VAR_Y = "SAMPLE_VAR_Y";

	public static final String SAMPLE_SD_X = "SAMPLE_SD_X";
	public static final String SAMPLE_SD_Y = "SAMPLE_SD_Y";

	public static final String POOLED_SAMPLE_VAR = "POOLED_SAMPLE_VAR";
	public static final String POOLED_SAMPLE_SD = "POOLED_SAMPLE_SD";

	public static final String T_STAT_POOLED = "T_STAT_POOLED";
	public static final String T_STAT_UNPOOLED = "T_STAT_UNPOOLED";
	public static final String P_VALUE_ONE_SIDED_POOLED   = "P_VALUE_ONE_SIDED_POOLED";
	public static final String P_VALUE_TWO_SIDED_POOLED   = "P_VALUE_TWO_SIDED_POOLED";
	public static final String P_VALUE_ONE_SIDED_UNPOOLED = "P_VALUE_ONE_SIDED_UNPOOLED";
	public static final String P_VALUE_TWO_SIDED_UNPOOLED = "P_VALUE_TWO_SIDED_UNPOOLED";

	public static final String T_STAT = "T_STAT";
	public static final String DF = "DF";
	public static final String DF_ADJUSTED = "DF_ADJUSTED";

	/*********************** Constructors ************************/

	public TwoIndependentTResult(HashMap texture) {
		super(texture);
	}
	public TwoIndependentTResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	public double getMeanX() {
		return ((Double)texture.get(MEAN_X)).doubleValue();
	}
	public double getMeanY() {
		return ((Double)texture.get(MEAN_Y)).doubleValue();
	}
	public double getSampleVarianceX() {
		return ((Double)texture.get(SAMPLE_VAR_X)).doubleValue();
	}
	public double getSampleVarianceY() {
		return ((Double)texture.get(SAMPLE_VAR_Y)).doubleValue();
	}
	public double getgetSampleSDX() {
		return ((Double)texture.get(SAMPLE_SD_X)).doubleValue();
	}
	public double getgetSampleSDY() {
		return ((Double)texture.get(SAMPLE_SD_Y)).doubleValue();
	}
	public double getPoolSampleVariance() {
		return ((Double)texture.get(POOLED_SAMPLE_VAR)).doubleValue();
	}
	public double getPoolSampleSD() {
		return ((Double)texture.get(POOLED_SAMPLE_SD)).doubleValue();
	}
	public int getDF() {
		return ((Integer)texture.get(DF)).intValue();
	}
	public double getDFAdjusted() {
		return ((Double)texture.get(DF_ADJUSTED)).doubleValue();
	}

	public double getTStatPooled() {
		return ((Double)texture.get(T_STAT_POOLED)).doubleValue();
	}
	public double getTStatUnpooled() {
		return ((Double)texture.get(T_STAT_UNPOOLED)).doubleValue();
	}

	public double getPValueOneSidedPooled() {
		return ((Double)texture.get(P_VALUE_ONE_SIDED_POOLED)).doubleValue();
	}

	public double getPValueTwoSidedPooled() {
		return ((Double)texture.get(P_VALUE_TWO_SIDED_POOLED)).doubleValue();
	}
	public double getPValueOneSidedUnpooled() {
		return ((Double)texture.get(P_VALUE_ONE_SIDED_UNPOOLED)).doubleValue();
	}

	public double getPValueTwoSidedUnpooled() {
		return ((Double)texture.get(P_VALUE_TWO_SIDED_UNPOOLED)).doubleValue();
	}
}
