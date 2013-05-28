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

public class NormalPowerResult extends ParametricTestResult {
	public final static String HYPOTHESIS_TYPE_LT = "HYPOTHESIS_TYPE_LT"; // less then
	public final static String HYPOTHESIS_TYPE_GT = "HYPOTHESIS_TYPE_GT"; // greater then
	public final static String HYPOTHESIS_TYPE_NE = "HYPOTHESIS_TYPE_NE"; // not equal.
	public static final String MEAN_PLOT_POINTS = "MEAN_PLOT_POINTS";
	public static final String POWER_PLOT_POINTS = "POWER_PLOT_POINTS";
	public static final String MULTIPLE_MEAN_PLOT_POINTS = "MULTIPLE_MEAN_PLOT_POINTS";
	public static final String MULTIPLE_POWER_PLOT_POINTS = "MULTIPLE_POWER_PLOT_POINTS";
	public static final String POWER = "POWER";
	public static final String SAMPLE_SIZE = "SAMPLE_SIZE";
	public static final String ALPHA = "ALPHA";
	public static final String MEAN_NULL = "MEAN_NULL";
	public static final String MEAN_ALTERNATIVE = "MEAN_ALTERNATIVE";
	public static final String HYPOTHESIS_TYPE = "HYPOTHESIS_TYPE";
	public static final String Z_SCORE = "Z_SCORE";
	public static final String PLOT_DESCRIPTION = "PLOT_DESCRIPTION";

	/*********************** Constructors ************************/
	public NormalPowerResult(HashMap texture) {
		super(texture);
	}
	public NormalPowerResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	public double[] getMeanPlotPoints() {
		double[] result = ((double[])texture.get(MEAN_PLOT_POINTS));

		for (int i = 0; i < result.length; i++) {
			//System.out.println("result getMeanPlotPoints result["+i+"] = " + result[i]);
		}
		return result;
	}
	public double[] getPowerPlotPoints() {
		double[] result = ((double[])texture.get(POWER_PLOT_POINTS));

		//for (int i = 0; i < result.length; i++) {
		//	//System.out.println("result getPowerPlotPoints result["+i+"] = " + result[i]);
		//}
		return result;
	}

	public double[][] getMultipleMeanPlotPoints() {
		double[][] result = ((double[][])texture.get(MULTIPLE_MEAN_PLOT_POINTS));
		//System.out.println("gui.NormalPower multipleMeanPlotPoints.length = " + result.length + ", multipleMeanPlotPoints[0].length = " + result[0].length);
		return result;
	}
	public double[][] getMultiplePowerPlotPoints() {
		double[][] result = ((double[][])texture.get(MULTIPLE_POWER_PLOT_POINTS));
		//System.out.println("gui.NormalPower getMultiplePowerPlotPoints.length = " + result.length + ", getMultiplePowerPlotPoints[0].length = " + result[0].length);

		return result;
	}
	public double getPower() {
		return ((Double)texture.get(POWER)).doubleValue();
	}
	public int getSampleSize() {
		return ((Integer)texture.get(SAMPLE_SIZE)).intValue();
	}
	public double getSampleMean() {
		return ((Double)texture.get(SAMPLE_MEAN)).doubleValue();
	}
	public double getSampleVariance() {
		return ((Double)texture.get(SAMPLE_VAR)).doubleValue();
	}
	public int getDF() {
		return ((Integer)texture.get(DF)).intValue();
	}
	public double getZScore() {
		return ((Double)texture.get(Z_SCORE)).doubleValue();
	}
	public double getPValue() {
		return ((Double)texture.get(P_VALUE)).doubleValue();
	}
	public String getResultHypothesisType() {
		return (String)texture.get(HYPOTHESIS_TYPE);
	}
	public String getPlotDescription() {

		String result = (String)texture.get(PLOT_DESCRIPTION);
		return result;
	}
}
