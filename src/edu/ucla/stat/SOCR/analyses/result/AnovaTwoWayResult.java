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

public class AnovaTwoWayResult extends AnovaOneWayResult{
	public static final String VARIABLE_LIST = "VARIABLE_LIST";
	public static final String DF_MODEL_GROUP = "DF_MODEL_GROUP";
	public static final String DF_ERROR_GROUP = "DF_ERROR_GROUP";
	public static final String RSS_GROUP = "RSS_GROUP";
	public static final String MSE_GROUP = "MSE_GROUP";
	public static final String F_VALUE_GROUP = "F_VALUE_GROUP";
	public static final String P_VALUE_GROUP = "P_VALUE_GROUP";

	public static final String BOX_PLOT_ROW_SIZE = "BOX_PLOT_ROW_SIZE";
	public static final String BOX_PLOT_COLUMN_SIZE = "BOX_PLOT_COLUMN_SIZE";
	public static final String BOX_PLOT_ROW_FACTOR_NAME = "BOX_PLOT_ROW_FACTOR_NAME";
	public static final String BOX_PLOT_COLUMN_FACTOR_NAME = "BOX_PLOT_COLUMN_FACTOR_NAME";

	public static final String BOX_PLOT_ROW_NAME = "BOX_PLOT_ROW_NAME"; // row size fo r box plot
	public static final String BOX_PLOT_COLUMN_NAME = "BOX_PLOT_COLUMN_NAME"; // column size fo r box plot

	/*********************** Constructors ************************/
	public AnovaTwoWayResult(HashMap<String,Object> texture) {
		super(texture);
	}
	public AnovaTwoWayResult (HashMap<String,Object> texture, 
			HashMap<String,Object> graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	public String[] getVariableList() {
		return ((String[])texture.get(VARIABLE_LIST));
	}
	public int[] getDFGroup() {
		return ((int[])texture.get(DF_MODEL_GROUP));
	}

	public double[] getRSSGroup() {
		return ((double[])texture.get(RSS_GROUP));
	}
	
	public double[] getMSEGroup() {
		return ((double[])texture.get(MSE_GROUP));
	}
	
	
	public double[] getFValueGroup() {
		return ((double[])texture.get(F_VALUE_GROUP));
	}
	public double[] getPValueGroup() {
		return ((double[])texture.get(P_VALUE_GROUP));
	}
	public double[] getFValueArray() {
		return ((double[])texture.get(F_VALUE));
	}
	public double[] getResiduals() {
		return ((double[])texture.get(RESIDUALS));
	}
	public double[] getPredicted() {
		return ((double[])texture.get(PREDICTED));
	}
	public int getBoxPlotSeriesCount() {
		return ((Integer)(texture.get(BOX_PLOT_ROW_SIZE))).intValue();
	}
	public int getBoxPlotCategoryCount() {
		return ((Integer)(texture.get(BOX_PLOT_COLUMN_SIZE))).intValue();
	}
	public String[] getBoxPlotSeriesName() {
		return ((String[])(texture.get(BOX_PLOT_ROW_NAME)));
	}
	public String[] getBoxPlotCategoryName() {
		return ((String[])(texture.get(BOX_PLOT_COLUMN_NAME)));
	}
	public double[][][] getBoxPlotResponseValue() {
		return ((double[][][])texture.get(BOX_PLOT_RESPONSE_VALUE));
	}
	public double getRSquare() {
		return ((Double)texture.get(R_SQUARE)).doubleValue();
	}
}
