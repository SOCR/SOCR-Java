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

public class DichotomousProportionResult extends ParametricTestResult {

	public static final String SAMPLE_PROPORTION_P   = "SAMPLE_PROPORTION_P";
	public static final String SAMPLE_PROPORTION_Q   = "SAMPLE_PROPORTION_Q";
	public static final String ADJUSTED_PROPORTION_P = "ADJUSTED_PROPORTION_P";
	public static final String ADJUSTED_PROPORTION_Q = "ADJUSTED_PROPORTION_Q";
	public static final String SAMPLE_SE_P   = "SAMPLE_SE_P";
	public static final String SAMPLE_SE_Q   = "SAMPLE_SE_Q";
	public static final String ADJUSTED_SE_P = "ADJUSTED_SE_P";
	public static final String ADJUSTED_SE_Q = "ADJUSTED_SE_Q";

	public static final String CI_TEXT_P = "CI_TEXT_P";
	public static final String CI_TEXT_Q = "CI_TEXT_Q";
	
	public static final String CI_WIDTH = "CI_WIDTH";
	public static final String CI_STRING = "CI_STRING";
	public static final String LOWER_P = "LOWER_P";
	public static final String UPPER_P = "UPPER_P";
	public static final String LOWER_Q = "LOWER_Q";
	public static final String UPPER_Q = "UPPER_Q";
	
	public static final String VALUE_LIST = "VALUE_LIST";
	public static final String SAMPLE_PROPORTION = "SAMPLE_PROPORTION";

	/*********************** Constructors ************************/
	public DichotomousProportionResult(HashMap texture) {
		super(texture);
	}
	public DichotomousProportionResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	public double getSampleProportionP() { // get sample mean of original dataset
		return ((Double)texture.get(SAMPLE_PROPORTION_P)).doubleValue();
	}
	public double getSampleProportionQ() { // get sample mean of original dataset
		return ((Double)texture.get(SAMPLE_PROPORTION_Q)).doubleValue();
	}
	public double getAdjustedProportionP() { // get sample mean of original dataset
		return ((Double)texture.get(ADJUSTED_PROPORTION_P)).doubleValue();
	}
	public double getAdjustedProportionQ() { // get sample mean of original dataset
		return ((Double)texture.get(ADJUSTED_PROPORTION_Q)).doubleValue();
	}

	public double getSampleSEP() { // get sample mean of original dataset
		return ((Double)texture.get(SAMPLE_SE_P)).doubleValue();
	}
	public double getSampleSEQ() { // get sample mean of original dataset
		return ((Double)texture.get(SAMPLE_SE_Q)).doubleValue();
	}
	public double getAdjustedSEP() { // get sample mean of original dataset
		return ((Double)texture.get(ADJUSTED_SE_P)).doubleValue();
	}
	public double getAdjustedSEQ() { // get sample mean of original dataset
		return ((Double)texture.get(ADJUSTED_SE_Q)).doubleValue();
	}
	
	public double getCiWidth() { // get sample mean of original dataset
		return ((Double)texture.get(CI_WIDTH)).doubleValue();
	}
	public String getCiString() { // get sample mean of original dataset
		return ((String)texture.get(CI_STRING));
	}
	public double getLowerP(){ // get sample mean of original dataset
		return ((Double)texture.get(LOWER_P)).doubleValue();
	}
	public double getUpperP(){ // get sample mean of original dataset
		return ((Double)texture.get(UPPER_P)).doubleValue();
	}
	public double getLowerQ(){ // get sample mean of original dataset
		return ((Double)texture.get(LOWER_Q)).doubleValue();
	}
	public double getUpperQ(){ // get sample mean of original dataset
		return ((Double)texture.get(UPPER_Q)).doubleValue();
	}
	
	public String getCITextP() {
		return ((String)texture.get(CI_TEXT_P));
	}
	
	public String getCITextQ() {
		return ((String)texture.get(CI_TEXT_Q));
	}
	
	public String[] getValueList() {
		return ((String[])texture.get(VALUE_LIST));
	}
	public int[] getSampleProportion() {
		return ((int[])texture.get(SAMPLE_PROPORTION));
	}
}
