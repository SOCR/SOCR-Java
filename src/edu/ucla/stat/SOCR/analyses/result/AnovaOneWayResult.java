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

import java.text.DecimalFormat;
import java.util.HashMap;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

public class AnovaOneWayResult extends LinearModelResult{

	public static final String PREDICTED = "PREDICTED";
	public static final String RESIDUALS = "RESIDUALS";

	public static final String DF_TOTAL = "DF_TOTAL";
	public static final String DF_ERROR = "DF_ERROR";
	public static final String DF_MODEL = "DF_MODEL";
	public static final String RSS_ERROR = "RSS_ERROR";
	public static final String RSS_MODEL = "RSS_MODEL";
	public static final String RSS_TOTAL = "RSS_TOTAL";
	public static final String MSS_ERROR = "MSS_ERROR";
	public static final String MSS_MODEL = "MSS_MODEL";
	public static final String F_VALUE = "F_VALUE";
	public static final String P_VALUE = "P_VALUE";

	public static final String SORTED_RESIDUALS = "SORTED_RESIDUALS";
	public static final String SORTED_RESIDUALS_INDEX = "SORTED_RESIDUALS_INDEX";
	public static final String SORTED_STANDARDIZED_RESIDUALS = "SORTED_STANDARDIZED_RESIDUALS";
	public static final String SORTED_NORMAL_QUANTILES = "SORTED_NORMAL_QUANTILES";

	public static final String BOX_PLOT_ROW_SIZE = "BOX_PLOT_ROW_SIZE"; // row size fo r box plot
	public static final String BOX_PLOT_COLUMN_SIZE = "BOX_PLOT_COLUMN_SIZE"; // row size fo r box plot

	public static final String BOX_PLOT_RESPONSE_NAME = "BOX_PLOT_RESPONSE_NAME"; // box plot
	public static final String BOX_PLOT_RESPONSE_VALUE = "BOX_PLOT_RESPONSE_VALUE"; // box plot
	public static final String BOX_PLOT_FACTOR_NAME= "BOX_PLOT_FACTOR_NAME"; // box plot
	
	/*********************** Constructors ************************/

	public AnovaOneWayResult(HashMap<String,Object> texture) {
		super(texture);
	}
	public AnovaOneWayResult (HashMap<String,Object> texture, 
			HashMap<String,Object> graph) {
		super(texture, graph);
	}

	
	/*********************** Accessors ************************/

	/** Corrected total degrees of freedom. */
	public int getDFTotal() {
		return ((Integer)texture.get(DF_TOTAL)).intValue();
	}

	/** Error (within) degrees of freedom. */
	public int getDFError() {
		return ((Integer)texture.get(DF_ERROR)).intValue();
	}

	/** Model (between) degrees of freedom. */
	public int getDFModel() {
		return ((Integer)texture.get(DF_MODEL)).intValue();
	}

	/** Corrected total mean sum of squares. */

	
	public double getRSSTotal() {
		return ((Double)texture.get(RSS_TOTAL)).doubleValue();
	}
	
	/** Error (within) mean sum of squares. */
	
	public double getRSSError() {
		return ((Double)texture.get(RSS_ERROR)).doubleValue();
	}

	/** Model (between) mean sum of squares. */
	
	public double getRSSModel() {
		return ((Double)texture.get(RSS_MODEL)).doubleValue();
	}

	/** Corrected total residual sum of squares. */
	public double getMSSError() {
		return ((Double)texture.get(MSS_ERROR)).doubleValue();
	}
	
	
	public double getMSSModel() {
		return ((Double)texture.get(MSS_MODEL)).doubleValue();
	}
	
	public double getPValue() {
		return ((Double)texture.get(P_VALUE)).doubleValue();
	}
	
	
	public double getFValue() {
		return ((Double)texture.get(F_VALUE)).doubleValue();
	}
	

	public double getRSquare() {
		return ((Double)texture.get(R_SQUARE)).doubleValue();
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
	/** Number of groups in the independent variable. */
	public int getFactorGroupNumber() {
		return ((Integer)texture.get(BOX_PLOT_ROW_SIZE)).intValue();
	}
	public String[] getFactorGroupNames() {
		return (String[])texture.get(BOX_PLOT_FACTOR_NAME);
	}
	public double[][][] getBoxPlotResponseValues() {
		return (double[][][])texture.get(BOX_PLOT_RESPONSE_VALUE);
	}
}
