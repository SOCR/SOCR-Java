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
// general linear model, is a parent class of all the linear model results.

package edu.ucla.stat.SOCR.analyses.result;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ParametricTestResult extends Result{
	protected DecimalFormat dFormat;

	/********** BEGIN RESIDUALS FROM MODELING RESULTS: MOSTLY FOR PLOTTING **********/
	public static final String SORTED_RESIDUALS = "SORTED_RESIDUALS";
	public static final String SORTED_RESIDUALS_INDEX = "SORTED_RESIDUALS_INDEX";
	public static final String SORTED_NORMAL_QUANTILES = "SORTED_NORMAL_QUANTILES";
	public static final String SORTED_STANDARDIZED_RESIDUALS = "SORTED_STANDARDIZED_RESIDUALS";
	public static final String SORTED_STANDARDIZED_NORMAL_QUANTILES = "SORTED_STANDARDIZED_NORMAL_QUANTILES";
	/********** END RESIDUALS FROM MODELING RESULTS: MOSTLY FOR PLOTTING **********/

	public ParametricTestResult(HashMap texture) {
		super(texture);
	}
	public ParametricTestResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	public void setDecimalFormat(DecimalFormat format){
		dFormat= format;
	}
	
	public String[] getFormattedGroup(double[] d) {
		int length = d.length;
		String[] s = new String[length];
		
		for (int i=0;i< length; i++)
			s[i]=dFormat.format(d[i]);
		
		return s;
	}
	
//	 first index is for row, second index for column
	public String[][] getFormattedGroupArray(double[][] d) {
		
		int row = d.length;
		
		int col = d[0].length;
		
		//System.out.println(" ParametricTestResults :row 1st index="+row+" col 2nd index="+col);
		
		String[][] s = new String[row][col];
		
		for (int i=0;i< row; i++)
			for (int j=0;j< col; j++)
			s[i][j]=dFormat.format(d[i][j]);
		
		return s;
	}
	public String getFormattedDouble(double d) {
		return dFormat.format(d);
	}
	
}
