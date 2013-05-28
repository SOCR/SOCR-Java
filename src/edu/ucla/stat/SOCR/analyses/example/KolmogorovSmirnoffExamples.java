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
package edu.ucla.stat.SOCR.analyses.example;

import java.lang.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
//import edu.ucla.stat.SOCR.analyses.data.DataFrame;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

/** Example data generator for ANOVA & other statistical applets. */
public class KolmogorovSmirnoffExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	public static final short NULL_EXAMPLE = 0;
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  KolmogorovSmirnoffExamples() {
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  KolmogorovSmirnoffExamples(int analysisType, int exampleID) {

		System.out.println("call KolmogorovSmirnoffExamples");
		switch (exampleID) {
			case NULL_EXAMPLE : { // and for TWO_INDEPENDENT_KW_T.
			}

			case ExampleData.KS_1:  {
				// example from R library(MASS) InspectorSpray
				double[] a = {0.08, 0.10, 0.15, 0.17, 0.24, 0.34, 0.38, 0.42, 0.49, 0.50, 0.70, 0.94, 0.95, 1.26, 1.37, 1.55, 1.75, 3.20, 6.98, 50.57};
				double[] b = {0.11, 0.18, 0.23, 0.51, 1.19, 1.30, 1.32, 1.73, 2.06, 2.16, 2.37, 2.91, 4.50, 4.51, 4.66, 14.68, 14.82, 27.44, 39.41, 41.04};

				int sampleSize = Math.max(a.length, b.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = a[i] + "";
					example[i][1] = b[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}


			case ExampleData.KS_2:  {
				// see end of class for example web link
				int sampleSize = Math.max(ht.length, wt.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "Height";
				columnNames[1] = "Weight";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = ht[i] + "";
					example[i][1] = wt[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.KS_3:  {
				// see end of class for example web link
				int sampleSize = Math.max(calories.length, sodium.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "Calories";
				columnNames[1] = "Sodium";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = calories[i] + "";
					example[i][1] = sodium[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.KS_4: {
				// R's example: oats, using catagorical variable V
					double[] v1={111,130,157,174,61,91,97,100,68,64,112,86,74,89,81,122,62,90,100,116,53,74,118,113};
					double[] v2={117,114,161,141,70,108,126,149,60,102,89,96,64,103,132,133,80,82,94,126,89,82,86,104};
					double[] v3={105,140,118,156,96,124,121,144,89,129,132,124,70,89,104,117,63,70,109,99,97,99,119,121};
					int sampleSize = v1.length;

					int varSize = 3;
					example = new String[sampleSize][varSize];
					columnNames = new String[varSize];
					columnNames[0] = "Vict";
					columnNames[1] = "Gold";
					columnNames[2] = "Marv";


					for (int i = 0; i < sampleSize; i++) {
						example[i][0] = v1[i] + "";
						example[i][1] = v2[i] + "";
						example[i][2] = v3[i] + "";
					}
					dataTable = new JTable(example,  columnNames);

					break;
			}
		}
	}

	/** returns a JTable object containing the Example Data */
	public JTable getExample() {
		return dataTable;
	 }
	public static String getExampleSource() {
		return dataSource;
	}

	/**************************** Ivo's SOCR examples ***********************************/
	// Next example:
	// See (http://wiki.stat.ucla.edu/socr/index.php/SOCR_Data_Dinov_020108_HeightsWeights)
	private final static double[] ht=new double[]{112.99254,136.48728,153.02694,142.33536,144.29707,123.30237,141.49469,136.46232,112.37228,120.66716,127.45159,114.14302,125.61071,122.46184,116.08661,139.99746,129.50230,142.97334,137.90245,124.04486,141.28066,143.53922,97.90191,129.50270,141.85006,129.72442,142.42351,131.55021,108.33236,113.89222,103.30160,120.75357,125.78863,136.22255,140.10147,128.74865,141.79942,121.23191,131.34782,106.71149,124.35977,124.85910,139.67108,137.36956,106.44989,128.76394,145.68372,116.81903,143.62153,134.93245,147.02193,126.32850,125.48391,115.70836,123.48924,147.89261,155.89869,128.07423,119.37010,133.81478,128.73249,137.54525,129.76040,128.82401,135.31654,109.61134,142.46836,132.74904,103.52753,124.72992,129.31366,134.01754,140.39691,102.83512,128.52138,120.29908,138.60365,132.95739,115.62326,122.52397,134.62544,121.89864,155.37673,128.94180,129.10132,139.47328,140.89005,131.59158,121.12320,131.51267,136.54794,141.48959,140.61040,112.14128,133.45702,131.80005,120.02848,123.09718,128.14320,115.47589,102.09269,130.35301,134.18419,98.64133,114.55992,123.49170,123.04799,126.47720,128.41699,127.19415,122.05623,127.60639,131.64235,111.89552,122.03900,128.55469,132.67923,136.06321,115.94034,136.90406,119.88039,109.00551,128.27046,135.29132,106.85577,123.29393,109.51431,119.30871,140.24018,133.98409,132.58066,130.69884,115.56370,123.79414,128.14275,135.96455,116.62726,126.82406,151.39126,130.40216,136.20683,113.39893,125.32874,127.58456,107.15636,116.45876,133.84022,112.89014,130.75683,137.75714,125.40363,138.46587,120.81838,140.15392,136.73969,106.11395,158.95616,108.78684,138.77581,115.91359,146.29220,109.87647,139.04993,119.90013,128.30690,127.24279,115.23060,124.79754,126.95114,111.27111,122.60890,124.20843,124.64532,119.51695,139.29827,104.82651,123.04245,118.89232,121.49386,119.24880,135.02389,116.22802,109.17306,124.22372,141.16448,129.15014,127.86927,120.92439,127.64657,101.46934,144.99271,110.95232,132.86248,146.33852,145.58944,120.84314,115.78126,128.30192,127.47182,127.87615};
	private final static double[] wt=new double[]{65.78331,71.51521,69.39874,68.21660,67.78781,68.69784,69.80204,70.01472,67.90265,66.78236,66.48769,67.62333,68.30248,67.11656,68.27967,71.09160,66.46100,68.64927,71.23033,67.13118,67.83379,68.87881,63.48115,68.42187,67.62804,67.20864,70.84235,67.49434,66.53401,65.44098,69.52330,65.81320,67.81630,70.59505,71.80484,69.20613,66.80368,67.65893,67.80701,64.04535,68.57463,65.18357,69.65814,67.96731,65.98088,68.67249,66.88088,67.69868,69.82117,69.08817,69.91479,67.33182,70.26939,69.10344,65.38356,70.18447,70.40617,66.54376,66.36418,67.53700,66.50418,68.99958,68.30355,67.01255,70.80592,68.21951,69.05914,67.73103,67.21568,67.36763,65.27033,70.84278,69.92442,64.28508,68.24520,66.35708,68.36275,65.47690,69.71947,67.72554,68.63941,66.78405,70.05147,66.27848,69.20198,69.13481,67.36436,70.09297,70.17660,68.22556,68.12932,70.24256,71.48752,69.20477,70.06306,70.55703,66.28644,63.42577,66.76711,68.88741,64.87434,67.09272,68.34761,65.61073,67.75551,68.02120,67.66193,66.31460,69.43706,63.83624,67.72277,70.05098,70.18602,65.94588,70.00700,68.61129,68.80817,69.76212,65.45539,68.82534,65.80030,67.21474,69.42021,68.94396,67.94150,65.62506,66.49607,67.92809,68.89415,70.24100,68.26623,71.23161,69.09747,64.39693,71.09585,68.21868,65.91721,67.43690,73.90107,69.98149,69.51862,65.18437,68.00869,68.33840,65.18417,68.26209,68.56865,64.49675,68.71053,68.89148,69.54011,67.39964,66.47521,66.01217,72.44434,64.12642,70.98112,67.50124,72.01515,65.31143,67.07509,64.39148,69.37003,68.37921,65.31018,67.13690,68.39468,66.29180,67.18660,65.99156,69.43393,67.97463,67.76133,65.27864,73.83364,66.81312,66.89411,65.73568,65.98283,66.58396,67.11294,65.87481,66.78067,68.73577,66.22666,65.95968,68.58372,66.59347,66.96574,68.08015,70.19025,65.52149,67.45905,67.40985,69.66158,65.79799,66.10558,68.23987,68.02403,71.39044};
	// next example: see http://wiki.stat.ucla.edu/socr/index.php/SOCR_012708_ID_Data_HotDogs
	private final static double[] calories=new double[]{186,181,176,149,184,190,158,139,175,148,152,111,141,153,190,157,131,149,135,132,173,191,182,190,172,147,146,139,175,136,179,153,107,195,135,140,138,129,132,102,106,94,102,87,99,107,113,135,142,86,143,152,146,144};
	private final static double[] sodium=new double[]{495,477,425,322,482,587,370,322,479,375,330,300,386,401,645,440,317,319,298,253,458,506,473,545,496,360,387,386,507,393,405,372,144,511,405,428,339,430,375,396,383,387,542,359,357,528,513,426,513,358,581,588,522,545};

}
