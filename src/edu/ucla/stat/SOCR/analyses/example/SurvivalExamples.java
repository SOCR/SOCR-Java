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
public class SurvivalExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, true, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  SurvivalExamples() {
		/*
		StringBuffer f3 = new StringBuffer();

		//now let's generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		example = new String[2][size];
		columnNames = new String[2];

		columnNames[0]="Y";
		columnNames[1] = "X";

		double b = Math.random()*2;
		double c = Math.random()*.05;
		double d = Math.random()*0.05-0.025;
		double e = Math.random()*0.05-0.025;
		double coeff = Math.random()*3+1;
		double x = 0;
		double y = 0;
		double yy = 0;
		double center2 = 0;
		double spread2 = 0;
		Double exampleID = new Double(0);

		for (i = 0; i < size*coeff; i=i+(int)coeff) {
			 x = Math.abs(Math.random()*10-5 + Math.pow(c*i, 2) + (b+Math.random()*0.5-0.25)*i);
			 y = Math.abs(i + Math.random()*4-2);
			 yy = y + e*Math.pow(x, 2) + d*Math.pow(x, 3);
			 example[0][(int)(i/coeff)] = String.valueOf(x);
			 exampleID = new Double(center2 + Math.random() * spread2);
			 example[1][(int)(i/coeff)] = String.valueOf(yy);
		}
		dataTable = new JTable(example,  columnNames);
		*/
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  SurvivalExamples(int analysisType, int exampleID) {

		switch (exampleID) {
			case ExampleData.SURVIVAL_1: { // from R's example gehan (library(MASS))
				example = new String[42][3];
				columnNames = new String[3];

				columnNames[0]= " Time ";
				columnNames[1] = " Censor";
				columnNames[2] = " Group " ;

				example[0][0] = "1"; example[0][1] = "1"; example[0][2] = "control";
				example[1][0] = "10"; example[1][1] = "1"; example[1][2] = "6-MP";
				example[2][0] = "22"; example[2][1] = "1"; example[2][2] = "control";
				example[3][0] = "7"; example[3][1] = "1"; example[3][2] = "6-MP";
				example[4][0] = "3"; example[4][1] = "1"; example[4][2] = "control";

				example[5][0] = "32"; example[5][1] = "0"; example[5][2] = "6-MP";
				example[6][0] = "12"; example[6][1] = "1"; example[6][2] = "control";
				example[7][0] = "23"; example[7][1] = "1"; example[7][2] = "6-MP";
				example[8][0] = "8";  example[8][1] = "1"; example[8][2] = "control";
				example[9][0] = "22"; example[9][1] = "1"; example[9][2] = "6-MP";

				example[10][0] = "17"; example[10][1] = "1"; example[10][2] = "control";
				example[11][0] = "6"; example[11][1] = "1"; example[11][2] = "6-MP";
				example[12][0] = "2"; example[12][1] = "1"; example[12][2] = "control";
				example[13][0] = "16"; example[13][1] = "1"; example[13][2] = "6-MP";
				example[14][0] = "11"; example[14][1] = "1"; example[14][2] = "control";

				example[15][0] = "34"; example[15][1] = "0"; example[15][2] = "6-MP";
				example[16][0] = "8"; example[16][1] = "1"; example[16][2] = "control";
				example[17][0] = "32"; example[17][1] = "0"; example[17][2] = "6-MP";
				example[18][0] = "12"; example[18][1] = "1"; example[18][2] = "control";
				example[19][0] = "25"; example[19][1] = "0"; example[19][2] = "6-MP";

				example[20][0] = "2"; example[20][1] = "1"; example[20][2] = "control";
				example[21][0] = "11"; example[21][1] = "0"; example[21][2] = "6-MP";
				example[22][0] = "5"; example[22][1] = "1"; example[22][2] = "control";
				example[23][0] = "20"; example[23][1] = "0"; example[23][2] = "6-MP";
				example[24][0] = "4"; example[24][1] = "1"; example[24][2] = "control";

				example[25][0] = "19"; example[25][1] = "0"; example[25][2] = "6-MP";
				example[26][0] = "15"; example[26][1] = "1"; example[26][2] = "control";
				example[27][0] = "6"; example[27][1] = "1"; example[27][2] = "6-MP";
				example[28][0] = "8"; example[28][1] = "1"; example[28][2] = "control";
				example[29][0] = "17"; example[29][1] = "0"; example[29][2] = "6-MP";

				example[30][0] = "23"; example[30][1] = "1"; example[30][2] = "control";
				example[31][0] = "35"; example[31][1] = "0"; example[31][2] = "6-MP";
				example[32][0] = "5"; example[32][1] = "1"; example[32][2] = "control";
				example[33][0] = "6"; example[33][1] = "1"; example[33][2] = "6-MP";
				example[34][0] = "11"; example[34][1] = "1"; example[34][2] = "control";

				example[35][0] = "13"; example[35][1] = "1"; example[35][2] = "6-MP";
				example[36][0] = "4"; example[36][1] = "1"; example[36][2] = "control";
				example[37][0] = "9"; example[37][1] = "0"; example[37][2] = "6-MP";
				example[38][0] = "1"; example[38][1] = "1"; example[38][2] = "control";
				example[39][0] = "6"; example[39][1] = "0"; example[39][2] = "6-MP";

				example[40][0] = "8"; example[40][1] = "1"; example[40][2] = "control";
				example[41][0] = "10"; example[41][1] = "0"; example[41][2] = "6-MP";


				dataTable = new JTable(example,  columnNames);
				break;
			}

			case ExampleData.SURVIVAL_2:  { // and for SURVIVAL_T.
				double[] time = {306,455,1010,210,883,1022,310,361,218,166,170,654,728,71,567,144,613,707,61,88,301,81,624,371,394,520,574,118,390,12,473,26,533,107,53,122,814,965,93,731,460,153,433,145,583,95,303,519,643,765,735,189,53,246,689,65,5,132,687,345,444,223,175,60,163,65,208,821,428,230,840,305,11,132,226,426,705,363,11,176,791,95,196,167,806,284,641,147,740,163,655,239,88,245,588,30,179,310,477,166,559,450,364,107,177,156,529,11,429,351,15,181,283,201,524,13,212,524,288,363,442,199,550,54,558,207,92,60,551,543,293,202,353,511,267,511,371,387,457,337,201,404,222,62,458,356,353,163,31,340,229,444,315,182,156,329,364,291,179,376,384,268,292,142,413,266,194,320,181,285,301,348,197,382,303,296,180,186,145,269,300,284,350,272,292,332,285,259,110,286,270,81,131,225,269,225,243,279,276,135,79,59,240,202,235,105,224,239,237,173,252,221,185,92,13,222,192,183,211,175,197,203,116,188,191,105,174,177};
				//status: 1=censored, 2=dead change to 0 = censored, 1 = dead.
				String[] status = {"1","1","0","1","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","0","1","1","0","1","1","1","1","1","1","1","1","1","1","1","0","1","0","1","1","1","0","1","1","1","1","1","0","1","1","1","1","1","0","1","1","1","1","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","0","0","1","1","1","0","1","0","1","1","1","1","1","0","1","1","0","0","1","1","1","1","1","0","0","1","1","1","0","1","1","0","0","1","0","1","0","0","1","1","1","1","0","1","1","0","0","0","1","1","1","0","0","0","1","0","0","0","1","0","1","1","1","1","1","0","1","0","0","0","0","1","1","1","0","0","0","1","0","1","0","0","0","0","0","0","1","0","0","1","0","0","0","0","1","0","0","0","0","0"};
				String[] sex = {"1","1","1","1","1","1","2","2","1","1","1","2","2","1","1","1","1","1","2","1","1","2","1","1","1","2","1","1","1","1","2","1","1","2","1","2","1","2","1","2","1","2","2","2","1","2","1","1","1","2","2","1","1","1","1","1","2","1","2","2","2","1","1","2","1","1","2","2","1","1","1","2","1","1","2","2","2","2","1","1","1","1","1","2","1","1","2","1","2","1","1","1","1","2","2","1","1","1","1","2","2","2","1","1","1","1","2","1","1","2","1","1","1","2","2","1","1","1","1","1","1","2","2","1","1","1","1","1","2","2","2","1","1","2","1","2","2","1","1","1","2","1","1","2","1","2","1","1","1","2","1","1","2","2","1","1","2","1","1","2","2","2","1","1","1","2","2","1","1","1","1","2","1","2","1","2","1","2","2","2","1","1","2","2","2","2","2","1","1","1","1","1","1","1","1","1","2","1","2","1","2","1","2","2","2","1","2","2","1","2","2","1","1","2","1","1","2","1","2","2","1","2","1","1","1","2","1","2"};

				int sampleSize = sex.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "time";
				columnNames[1] = "status";
				columnNames[2] = "sex";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = time[i] + "";
					example[i][1] = status[i];
					example[i][2] = sex[i];
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.SURVIVAL_3 : {
				double[] time={8,16,23,13,22,28,447,318,30,12,24,245,7,9,511,30,53,196,15,154,7,333,141,8,96,38,149,70,536,25,17,4,185,177,292,114,22,159,15,108,152,562,402,24,13,66,39,46,12,40,113,201,132,156,34,30,2,25,130,26,27,58,5,43,152,30,190,5,119,8,54,16,6,78,63,8};
				String[] status={"1","1","1","0","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","0","1","1","0","0","1","0","1","0","1","1","1","1","0","0","1","0","1","1","1","0","1","1","1","0","1","1","0","1","1","1","1","1","1","1","1","1","1","1","0","1","1","1","1","0","1","1","0","0","0","1","1","0"};
				String[] sex={"1","1","2","2","1","1","2","2","1","1","2","2","1","1","2","2","2","2","1","1","2","2","2","2","2","2","2","2","2","2","1","1","2","2","2","2","2","2","2","2","1","1","2","2","2","2","2","2","1","1","2","2","2","2","2","2","1","1","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","1","1"};
				double[] age={28,28,48,48,32,32,31,32,10,10,16,17,51,51,55,56,69,69,51,52,44,44,34,34,35,35,42,42,17,17,60,60,60,60,43,44,53,53,44,44,46,47,30,30,62,63,42,43,43,43,57,58,10,10,52,52,53,53,54,54,56,56,50,51,57,57,44,45,22,22,42,42,52,52,60,60};
				String[] disease={"Other","Other","GN","GN","Other","Other","Other","Other","Other","Other","Other","Other","GN","GN","GN","GN","AN","AN","GN","GN","AN","AN","Other","Other","AN","AN","AN","AN","Other","Other","AN","AN","Other","Other","Other","Other","GN","GN","Other","Other","PKD","PKD","Other","Other","AN","AN","AN","AN","AN","AN","AN","AN","GN","GN","AN","AN","GN","GN","GN","GN","AN","AN","AN","AN","PKD","PKD","GN","GN","Other","Other","Other","Other","PKD","PKD","PKD","PKD"};
				double[] frail={2.3,2.3,1.9,1.9,1.2,1.2,0.5,0.5,1.5,1.5,1.1,1.1,3.0,3.0,0.5,0.5,0.7,0.7,0.4,0.4,0.6,0.6,1.2,1.2,1.4,1.4,0.4,0.4,0.4,0.4,1.1,1.1,0.8,0.8,0.8,0.8,0.5,0.5,1.3,1.3,0.2,0.2,0.6,0.6,1.7,1.7,1.0,1.0,0.7,0.7,0.5,0.5,1.1,1.1,1.8,1.8,1.5,1.5,1.5,1.5,1.7,1.7,1.3,1.3,2.9,2.9,0.7,0.7,2.2,2.2,0.7,0.7,2.1,2.1,1.2,1.2};

				int sampleSize = sex.length;
				int varSize = 6;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				//System.out.println("time.length = " + time.length );
				//System.out.println("status.length = " + status.length );
				//System.out.println("sex.length = " + sex.length );
				//System.out.println("age.length = " + age.length );
				//System.out.println("disease.length = " + disease.length );
				//System.out.println("frail.length = " + frail.length );


				columnNames[0] = "time";
				columnNames[1] = "status";
				columnNames[4] = "sex";
				columnNames[3] = "age";
				columnNames[2] = "disease";
				columnNames[5] = "frail";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = time[i] + "";
					example[i][1] = status[i];
					example[i][4] = sex[i];
					example[i][3] = age[i] + "";
					example[i][2] = disease[i];
					example[i][5] = frail[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
		    	}

			case ExampleData.SURVIVAL_4: {
				double[] litter={1,1,1,2,2,2,3,3,3,4,4,4,5,5,5,6,6,6,7,7,7,8,8,8,9,9,9,10,10,10,11,11,11,12,12,12,13,13,13,14,14,14,15,15,15,16,16,16,17,17,17,18,18,18,19,19,19,20,20,20,21,21,21,22,22,22,23,23,23,24,24,24,25,25,25,26,26,26,27,27,27,28,28,28,29,29,29,30,30,30,31,31,31,32,32,32,33,33,33,34,34,34,35,35,35,36,36,36,37,37,37,38,38,38,39,39,39,40,40,40,41,41,41,42,42,42,43,43,43,44,44,44,45,45,45,46,46,46,47,47,47,48,48,48,49,49,49,50,50,50};
				double[] time={101,49,104,104,102,104,104,104,104,77,97,79,89,104,104,88,96,104,104,94,77,96,104,104,82,77,104,70,104,77,89,91,90,91,70,92,39,45,50,103,69,91,93,104,103,85,72,104,104,63,104,104,104,74,81,104,69,67,104,68,104,104,104,104,104,104,104,83,40,87,104,104,104,104,104,89,104,104,78,104,104,104,81,64,86,55,94,34,104,54,76,87,74,103,73,84,102,104,80,80,104,73,45,79,104,94,104,104,104,104,104,104,101,94,76,84,78,80,81,76,72,95,104,73,104,66,92,104,102,104,98,73,55,104,104,49,83,77,89,104,104,88,79,99,103,91,104,104,104,79};

				String[] status={"0","1","0","0","0","0","0","0","0","0","0","0","0","0","0","1","1","0","1","0","1","1","0","0","0","0","0","1","0","0","1","0","0","0","0","0","1","0","1","1","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","1","0","1","0","0","0","0","0","0","0","0","1","0","0","0","0","0","0","0","0","0","0","0","0","0","1","1","1","1","0","1","0","1","0","0","0","1","1","1","1","0","0","1","0","0","1","0","0","1","0","0","0","0","0","0","1","0","0","1","1","1","1","0","1","0","0","1","0","1","1","0","1","0","0","0","0","0","0","0","0","0","1","0","0","0","0","0","1","0","0","0","0","1"};

				String[] rx={"1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0","1","0","0"};
				int sampleSize = rx.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "time";
				columnNames[1] = "status";
				columnNames[2] = "rx";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = time[i] + "";
					example[i][1] = status[i];
					example[i][2] = rx[i];
					}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}
			case ExampleData.SURVIVAL_5: {
				double[] time={9,13,13,18,23,28,31,34,45,48,161,5,5,8,8,12,16,23,27,30,33,43,45};
				String[] status={ "1", "1", "0", "1", "1", "0", "1", "1", "0", "1", "0", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1"};
				String[] x={"M","M","M","M","M","M","M","M","M","M","M","NM","NM","NM","NM","NM","NM","NM","NM","NM","NM","NM","NM"};

				int sampleSize = x.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "time";
				columnNames[1] = "status";
				columnNames[2] = "x";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = time[i] + "";
					example[i][1] = status[i];
					example[i][2] = x[i];
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
}
