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

public class ConfidenceIntervalResult extends ParametricTestResult{

	
	public static final String SAMPLE_DATA = "SMAPLE_DATA";
	
	public static final String SAMPLE_SIZES = "SMAPLE_SIZES";
	public static final String SAMPLE_NAMES = "SMAPLE_NAMES";	
	public static final String N_TRAILS = "N_TRAILS";
	
	public static final String X_BAR = "X_BAR";
	public static final String CI_DATA = "CI_DATA";
	public static final String CV_INDEX = "CV_INDEX";
	public static final String MISS_COUNT = "MISS_COUNT";
	
	/*********************** Constructors ************************/

	public ConfidenceIntervalResult(HashMap<String,Object> texture) {
		super(texture);
	}
	public ConfidenceIntervalResult(HashMap<String,Object> texture, 
			HashMap<String,Object> graph) {
		super(texture, graph);
	}

	
	/*********************** Accessors ************************/

	public double[][] getSampleData() {
		return (double[][])texture.get(SAMPLE_DATA);
	}

	public int[] getSampleSizes() {
		return (int[])texture.get(SAMPLE_SIZES);
	}
	public double[][] getCIData() {
		return (double[][])texture.get(CI_DATA);
	}
	
	public double[] getXBar() {
		return (double[])texture.get(X_BAR);
	}
	public int getCvIndex() {
		return Integer.parseInt((String)texture.get(CV_INDEX));
	}
}
