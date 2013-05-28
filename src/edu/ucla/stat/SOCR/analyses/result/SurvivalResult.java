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

public class SurvivalResult extends AnalysisResult {

	public static final String SURVIVAL_TIME = "SURVIVAL_TIME";
	public static final String SURVIVAL_RATE = "SURVIVAL_RATE";
	public static final String SURVIVAL_RATE_UPPER_CI = "SURVIVAL_RATE_UPPER_CI";
	public static final String SURVIVAL_RATE_LOWER_CI = "SURVIVAL_RATE_LOWER_CI";
	public static final String SURVIVAL_AT_RISK = "SURVIVAL_AT_RISK";
	public static final String SURVIVAL_SE = "SURVIVAL_SE";
	public static final String SURVIVAL_GROUP_NAMES = "SURVIVAL_GROUP_NAMES";
	public static final String SURVIVAL_TIME_LIST = "SURVIVAL_TIME_LIST";
	public static final String SURVIVAL_CENSORED_TIME = "SURVIVAL_CENSORED_TIME";
	public static final String SURVIVAL_CENSORED_RATE = "SURVIVAL_CENSORED_RATE";
	public static final String MAX_TIME = "MAX_TIME";
	public static final String NUMBER_CENCORED = "NUMBER_CENCORED";


	/*********************** Constructors ************************/
	public SurvivalResult(HashMap texture) {
		super(texture);
	}
	public SurvivalResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	// for these arrays: first index is for treatment groups, second for patient data.
	public double[][] getSurvivalTime() {
		return ((double[][])texture.get(SURVIVAL_TIME));
	}
	public double[][] getSurvivalRate() {
		return ((double[][])texture.get(SURVIVAL_RATE));
	}
	public double[][] getSurvivalUpperCI() {
		return ((double[][])texture.get(SURVIVAL_RATE_UPPER_CI));
	}
	public double[][] getSurvivalLowerCI() {
		return ((double[][])texture.get(SURVIVAL_RATE_LOWER_CI));
	}
	public int[][] getSurvivalAtRisk() {
		////System.out.println("result getSurvivalAtRisk is called");
		return ((int[][])texture.get(SURVIVAL_AT_RISK));
	}
	public double[][] getSurvivalSE() {
		return ((double[][])texture.get(SURVIVAL_SE));
	}
	public String[] getSurvivalGroupNames() {
		return ((String[])texture.get(SURVIVAL_GROUP_NAMES));
	}
	public String getSurvivalTimeList() {
		return ((String)texture.get(SURVIVAL_TIME_LIST));
	}
	public double[][] getCensoredTimeArray() {
		return ((double[][])texture.get(SURVIVAL_CENSORED_TIME));
	}

	public double[][] getCensoredRateArray() {
		return ((double[][])texture.get(SURVIVAL_CENSORED_RATE));
	}
	public double[] getMaxTime() {
		return ((double[])texture.get(MAX_TIME));
	}
	public String getNumberCensored() {
		return ((String)texture.get(NUMBER_CENCORED));
		//return ((Integer)texture.get(NUMBER_CENCORED)).intValue();
	}

}
