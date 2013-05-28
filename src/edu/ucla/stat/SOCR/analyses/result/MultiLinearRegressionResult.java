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
import edu.ucla.stat.SOCR.util.AnalysisUtility;

public class MultiLinearRegressionResult extends LinearModelResult {
	public static final String VARIABLE_LIST = "VARIABLE_LIST";
	public static final String BETA = "BETA"; // an array of double that holds beta esitmates
	public static final String BETA_SE = "BETA_SE";
	public static final String DF_ERROR = "";

	public static final String BETA_T_STAT = "BETA_T_STAT";
	public static final String BETA_P_VALUE = "BETA_P_VALUE";

	public static final String PREDICTED = "PREDICTED";
	public static final String RESIDUALS = "RESIDUALS";
	public static final String SORTED_RESIDUALS = "SORTED_RESIDUALS";
	public static final String SORTED_RESIDUALS_INDEX = "SORTED_RESIDUALS_INDEX";
	public static final String SORTED_NORMAL_QUANTILES = "SORTED_NORMAL_QUANTILES";

	/*********************** Constructors ************************/
	public MultiLinearRegressionResult(HashMap<String,Object> texture) {
		super(texture);
	}
	public MultiLinearRegressionResult(HashMap<String,Object> texture, 
			HashMap<String,Object> graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	public String[] getVariableList() {
		return ((String[])texture.get(VARIABLE_LIST));
	}
	public double[] getBeta() {
		return (double[])texture.get(BETA);
	}
	public double[] getBetaSE() {
		return (double[])texture.get(BETA_SE);
	}

	/** get the degrees of freedom, df = sample size - number of parameters (including intercept). */
	public int getDF() {
		return ((Integer)texture.get(DF_ERROR)).intValue();
	}

	public double[] getBetaTStat() {
		return ((double[])texture.get(BETA_T_STAT));
	}
	/** the reason using String[] instead of double[] is that sometimes we have characters in the results e.g. " < 10E-10 " and R especially likes doing those things. */

	public double[] getBetaPValue() {
		return ((double[])texture.get(BETA_P_VALUE));
	}
	public double[] getFValue() {
		return ((double[])texture.get(F_VALUE));
	}
	public double[] getResiduals() {
		return ((double[])texture.get(RESIDUALS));
	}
	public double[] getPredicted() {
		return ((double[])texture.get(PREDICTED));
	}
	public double[] getSortedResiduals() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_RESIDUALS);
	}
	public double[] getSortedStandardizedResiduals() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_STANDARDIZED_RESIDUALS);
	}
	public int[] getSortedResidualsIndex() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (int[])resultHashMap.get(SORTED_RESIDUALS_INDEX);
	}
	public double[] getSortedNormalQuantiles() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_NORMAL_QUANTILES);
	}
	public double[] getSortedStandardizedNormalQuantiles() {
		double[] residuals = this.getResiduals();
		HashMap<String,Object> resultHashMap = 
			AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
		return (double[])resultHashMap.get(SORTED_STANDARDIZED_NORMAL_QUANTILES);
	}

	public double getRSquare() {
		return ((Double)texture.get(R_SQUARE)).doubleValue();
	}
}
