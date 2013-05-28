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

public class FlignerKilleenResult extends ParametricTestResult {

	public static final String TOTAL = "TOTAL";
	public static final String GROUP_SIZE = "GROUP_SIZE";
	public static final String GROUP_NAME = "GROUP_NAME";
	public static final String MEAN_SCORE = "MEAN_SCORE";
	public static final String MEDIAN = "MEDIAN";
	public static final String TOTAL_MEAN_SCORE = "TOTAL_MEAN_SCORE";
	public static final String VAR = "VAR";
	public static final String SCORE = "SCORE";
	public static final String DF = "DF";
	public static final String CHI_STAT = "CHI_STAT";
	public static final String P_VALUE = "P_VALUE";


	/*********************** Constructors ************************/
	public FlignerKilleenResult(HashMap texture) {
		super(texture);
	}
	public FlignerKilleenResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	public int getTotal() {
		return ((Integer)texture.get(TOTAL)).intValue();
	}

	public int[] getGroupSize() {
		return ((int[])texture.get(GROUP_SIZE));
	}
	public String[] getGroupNames() {
		return ((String[])texture.get(GROUP_NAME));
	}

	public double[] getMedian() {
		return ((double[])texture.get(MEDIAN));
	}

	public double getTotalMeanScore() {
		return ((Double)texture.get(TOTAL_MEAN_SCORE)).doubleValue();
	}
	public double getVariance() {
		return ((Double)texture.get(VAR)).doubleValue();
	}
	public double[][] getScore() {
		return ((double[][])texture.get(SCORE));
	}

	public int getDF() {
		return ((Integer)texture.get(DF)).intValue();
	}
	public double getChiStat() {
		return ((Double)texture.get(CHI_STAT)).doubleValue();
	}
	public double getPValue() {
		return ((Double)texture.get(P_VALUE)).doubleValue();
	}


}
