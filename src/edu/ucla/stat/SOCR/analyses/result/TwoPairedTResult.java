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

public class TwoPairedTResult extends NonParametricTestResult {
	public static final String MEAN_X = "MEAN_X";
	public static final String MEAN_Y = "MEAN_Y";
	public static final String MEAN_DIFF = "MEAN_DIFF";

	public static final String SAMPLE_VAR = "SAMPLE_VAR";
	public static final String T_STAT = "T_STAT";
	public static final String DF = "DF";
	public static final String P_VALUE = "P_VALUE";


 	/*********************** Constructors ************************/

	public TwoPairedTResult(HashMap texture) {
		super(texture);
	}
	public TwoPairedTResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}
	/*********************** Accessors ************************/
	public double getMeanX() {
		return ((Double)texture.get(MEAN_X)).doubleValue();
	}
	public double getMeanY() {
		return ((Double)texture.get(MEAN_Y)).doubleValue();
	}
	public double getMeanDifference() {
		return ((Double)texture.get(MEAN_DIFF)).doubleValue();
	}

	public double getSampleVariance() {
		return ((Double)texture.get(SAMPLE_VAR)).doubleValue();
	}
	public int getDF() {
		return ((Integer)texture.get(DF)).intValue();
	}
	public double getTStat() {
		return ((Double)texture.get(T_STAT)).doubleValue();
	}
	public String getPValue() { // this should be deprecated and replaced by getPValueOneSided and  getPValueTwoSided. annie chu 20070808.
		return ((String)texture.get(P_VALUE));
	}
	public double getPValueOneSided() {
		return ((Double)texture.get(P_VALUE_ONE_SIDED)).doubleValue();
	}
	public double getPValueTwoSided() {
		return ((Double)texture.get(P_VALUE_TWO_SIDED)).doubleValue();
	}

}
