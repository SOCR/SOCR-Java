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
/*

January 2007. Annie Che <chea@stat.ucla.edu>. UCLA Statistics.

reference for example data and formula:
	Modern Applied Statistics with S. Fourth Edition
	by W. N. Venables and B. D. Ripley
	Note that the first method of hazard calculation is used here (see reference for details).
*/

package edu.ucla.stat.SOCR.analyses.example;

import java.util.HashMap;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.result.SurvivalResult;


public class SurvivalKaplanMeierExample {
	public static void main(String args[]) {

		double[] time = {1,10,22,7,3,32,12,23,8,22,17,6,2,16,11,34,8,32,12,25,2,11,5,20,4,19,15,6,8,17,23,35,5,6,11,13,4,9,1,6,8,10};
		byte[] censor = {1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,0};
		String[] treat = {"control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",   "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP"};

		try {
			Data data = new Data();
			SurvivalResult result = (SurvivalResult)data.getSurvivalResult(time, censor, treat);
			double[][] survivalTimeArray = result.getSurvivalTime();
			double[][] survivalRateArray = result.getSurvivalRate();
			double[][] upperCIArray = result.getSurvivalUpperCI();
			double[][] lowerCIArray = result.getSurvivalLowerCI();
			int[][] atRiskArray = result.getSurvivalAtRisk();
			double[][] survivalSEArray = result.getSurvivalSE();
			String timeList = result.getSurvivalTimeList();;
			double[][] survivalTime = result.getSurvivalTime();
			double[][] survivalRate = result.getSurvivalRate();
			double[][] upperCI = result.getSurvivalUpperCI();;
			double[][] lowerCI = result.getSurvivalLowerCI();
			double[][] survivalSE = result.getSurvivalSE();
			double[][] censoredTime = result.getCensoredTimeArray();;
			double[][] censoredRate = result.getCensoredRateArray();
			double[] maxTime = result.getMaxTime();
			int[][]    atRisk = result.getSurvivalAtRisk();;
			String[]   groupNames = result.getSurvivalGroupNames();

			//System.out.println("\n\nSURVIVAL TIME         = " + timeList);
			//System.out.println("\n\nTIME | NUMBER_AT_RISK | RATE | STANDARD_ERROR_OF_RATE | UPPER_CI | LOWER_CI ");
			for (int i = 0; i < survivalRate.length; i++) {
				//System.out.println("\n\nTREATMENT GROUP = " + groupNames[i]+"\n");
				for (int j = 0; j < survivalRate[i].length; j++) {
					if (upperCI[i][j] == 1) {
						//System.out.println("\n" +  survivalTime[i][j] +"\t" + atRisk[i][j] + "\t" + survivalRate[i][j] +"\t" + survivalSE[i][j] + "\t" + upperCI[i][j]+"\t\t" + lowerCI[i][j]);
					} else {
						//System.out.println("\n" +  survivalTime[i][j] +"\t" + atRisk[i][j] + "\t" + survivalRate[i][j] +"\t" + survivalSE[i][j] + "\t" + upperCI[i][j]+"\t" + lowerCI[i][j]);
					}
				}
			}

		} catch (Exception e) {
			//System.out.println(e);
		}
	}
}
