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
public class MultiLinearRegressionExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, true, true, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  MultiLinearRegressionExamples() {
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

	public  MultiLinearRegressionExamples(int analysisType, int exampleID) {
		//System.out.println("MultiLinearRegressionExamples exampleID " + exampleID);

		switch (exampleID) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}
			case ExampleData.MULTI_LINEAR_REGRESSION_1: {
				double[] deaths={968,43,588,640,4743,566,325,118,115,1545,1302,262,2207,1410,833,669,911,1037,1196,616,766,2120,841,648,1289,259};

				double[] drivers={158,11,91,92,952,109,167,30,35,298,203,41,544,254,150,136,147,146,46,157,255,403,189,85,234,38};

				double[] popden={64.0,0.4,12.0,34.0,100.0,17.0,518.0,226.0,12524.0,91.0,68.0,8.1,180.0,129.0,49.0,27.0,76.0,72.0,31.0,314.0,655.0,137.0,43.0,46.0,63.0,4.6};

				double[] rural={66.0,5.9,33.0,73.0,118.0,73.0,5.1,3.4,0.0,57.0,83.0,40.0,102.0,89.0,100.0,124.0,65.0,40.0,19.0,29.0,17.0,95.0,110.0,59.0,100.0,72.0};

				double[] temp={62,30,64,51,65,42,37,41,44,67,54,36,33,37,30,42,44,65,30,44,37,33,22,57,40,29};

				double[] fuel={119.0,6.2,65.0,74.0,105.0,78.0,95.0,20.0,23.0,216.0,162.0,29.0,350.0,196.0,109.0,94.0,104.0,109.0,37.0,113.0,166.0,306.0,132.0,77.0,180.0,31.0};

				int sampleSize = deaths.length;
				int varSize = 6;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "deaths";
				columnNames[1] = "drivers";
				columnNames[2] = "popden";
				columnNames[3] = "rural";
				columnNames[4] = "temp";
				columnNames[5] = "fuel";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = deaths[i] + "";
					example[i][1] = drivers[i] + "";
					example[i][2] = popden[i] + "";
					example[i][3] = rural[i] + "";
					example[i][4] = temp[i] + "";
					example[i][5] = fuel[i] + "";
				}
				dataTable = new JTable(example,  columnNames);
				break;


			}
			case ExampleData.MULTI_LINEAR_REGRESSION_2:  {
				//String[] No={
				// [1] A A A A B B B C C C D D D D E E E F F F G G G G H H H I I J J J
				//Levels: A B C D E F G H I J
				double[] SG={50.8,50.8,50.8,50.8,40.8,40.8,40.8,40.0,40.0,40.0,38.4,38.4,38.4,38.4,40.3,40.3,40.3,32.2,32.2,32.2,41.3,41.3,41.3,41.3,38.1,38.1,38.1,32.2,32.2,31.8,31.8,31.8};

				double[] VP={8.6,8.6,8.6,8.6,3.5,3.5,3.5,6.1,6.1,6.1,6.1,6.1,6.1,6.1,4.8,4.8,4.8,5.2,5.2,5.2,1.8,1.8,1.8,1.8,1.2,1.2,1.2,2.4,2.4,0.2,0.2,0.2};
				double[] V10={190,190,190,190,210,210,210,217,217,217,220,220,220,220,231,231,231,236,236,236,267,267,267,267,274,274,274,284,284,316,316,316};
				double[] EP={205,275,345,407,218,273,347,212,272,340,235,300,365,410,307,367,395,267,360,402,235,275,358,416,285,365,444,351,424,365,379,428};
				double[] Y={12.2,22.3,34.7,45.7,8.0,13.1,26.6,7.4,18.2,30.4,6.9,15.2,26.0,33.6,14.4,26.8,34.9,10.0,24.8,31.7,2.8,6.4,16.1,27.8,5.0,17.6,32.1,14.0,23.2,8.5,14.7,18.0};

				int sampleSize = Y.length;
				int varSize = 5;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "Y";
				columnNames[1] = "SG";
				columnNames[2] = "VP";
				columnNames[3] = "V10";
				columnNames[4] = "EP";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = Y[i] + "";
					example[i][1] = SG[i] + "";
					example[i][2] = VP[i] + "";
					example[i][3] = V10[i] + "";
					example[i][4] = EP[i] + "";
				}
				dataTable = new JTable(example, columnNames);
				break;

			}

			case ExampleData.MULTI_LINEAR_REGRESSION_3:  { // and for TWO_PAIRED_T.
				double[] m = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
				double[] h = {60,94,91,81,80,92,74,89,96,87,86,94,94,94,79,50,92,82,94,78};
				double[] f = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};



				int sampleSize = m.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "M";
				columnNames[1] = "H";
				columnNames[2] = "F";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = m[i] + "";
					example[i][1] = h[i] + "";
					example[i][2] = f[i] + "";
				}
				dataTable = new JTable(example, columnNames);
				break;
			}
			case ExampleData.MULTI_LINEAR_REGRESSION_4 : {

				double[] dist = {2.5,6.0,6.0,7.5,8.0,8.0,16.0,6.0,5.0,6.0,28.0,5.0,9.5,6.0,4.5,10.0,14.0,3.0,4.5,5.5,3.0,3.5,6.0,2.0,3.0,4.0,6.0,5.0,6.5,5.0,10.0,6.0,18.0,4.5,20.0};

 				double[] climb = {650,2500,900,800,3070,2866,7500,800,800,650,2100,2000,2200,500,1500,3000,2200,350,1000,600,300,1500,2200,900,600,2000,800,950,1750,500,4400,600,5200,850,5000};

 				double[] time = {16.083,48.350,33.650,45.600,62.267,73.217,204.617,36.367,29.750,39.750,192.667,43.050,65.000,44.133,26.933,72.250,98.417,78.650,17.417,32.567,15.950,27.900,47.633,17.933,18.683,26.217,34.433,28.567,50.500,20.950,85.583,32.383,170.250,28.100,159.833};

				int sampleSize = dist.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "dist";
				columnNames[1] = "climb";
				columnNames[2] = "time";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = dist[i] + "";
					example[i][1] = climb[i] + "";
					example[i][2] = time[i] + "";
				}
				dataTable = new JTable(example, columnNames);
				break;
			}


			case ExampleData.MULTI_LINEAR_REGRESSION_5: {


				double[] syct = {125,29,29,29,29,26,23,23,23,23,400,400,60,50,350,200,167,143,143,110,143,143,143,110,320,320,320,320,320,320,25,25,50,50,56,64,50,50,50,50,50,50,50,50,133,133,810,810,320,200,700,700,140,200,110,110,220,800,800,800,800,800,125,75,75,75,90,105,105,105,75,75,175,300,300,300,300,300,300,180,330,300,300,330,330,140,140,140,140,140,140,140,140,57,57,26,26,26,26,480,203,115,1100,1100,600,400,400,900,900,900,900,900,225,225,180,185,180,225,25,25,17,17,1500,1500,800,50,50,50,50,50,50,100,100,100,50,50,50,150,115,115,92,92,92,75,60,60,60,50,72,72,40,40,35,38,48,38,30,112,84,56,56,56,56,56,56,38,38,38,38,38,200,200,200,250,250,250,160,160,160,160,160,240,240,105,105,105,52,70,59,59,26,26,26,116,50,50,50,50,30,30,180,180,180,180,124,98,125,480,480};

				double[] mmin = {256,8000,8000,8000,8000,8000,16000,16000,16000,32000,1000,512,2000,4000,64,512,524,512,1000,5000,1500,3100,2300,3100,128,512,256,256,512,256,1310,1310,2620,2620,5240,5240,500,1000,2000,1000,1000,2000,2000,2000,1000,1000,512,1000,512,512,384,256,1000,1000,1000,1000,1000,256,256,256,256,256,512,2000,2000,2000,256,256,1000,2000,2000,3000,256,768,768,768,768,384,192,768,1000,1000,1000,1000,1000,2000,2000,2000,2000,2000,2000,2000,2000,4000,4000,16000,16000,8000,8000,96,1000,512,512,768,768,2000,4000,1000,512,1000,1000,2000,2000,2000,2000,2000,2000,1000,2000,2000,4000,4000,768,768,768,2000,2000,2000,2000,2000,8000,1000,1000,1000,2000,2000,2000,512,2000,2000,2000,2000,2000,4000,4000,2000,4000,4000,4000,2000,8000,8000,8000,16000,4000,8000,16000,1000,1000,1000,2000,2000,4000,4000,4000,4000,4000,8000,8000,4000,1000,1000,2000,512,512,1000,512,512,1000,1000,2000,512,512,2000,2000,2000,4000,4000,4000,8000,8000,8000,8000,2000,2000,2000,2000,4000,8000,8000,262,512,262,512,1000,1000,2000,512,1000};

				double[] mmax = {6000,32000,32000,32000,16000,32000,32000,32000,64000,64000,3000,3500,8000,16000,64,16000,2000,5000,2000,5000,6300,6200,6200,6200,6000,2000,6000,3000,5000,5000,2620,2620,10480,10480,20970,20970,2000,4000,8000,4000,8000,16000,16000,16000,12000,8000,512,5000,8000,8000,8000,2000,16000,8000,4000,12000,8000,8000,8000,8000,8000,8000,1000,8000,16000,16000,1000,2000,4000,4000,8000,8000,2000,3000,3000,12000,4500,12000,768,12000,3000,4000,16000,2000,4000,4000,4000,4000,32000,8000,32000,32000,4000,16000,24000,32000,32000,32000,16000,512,2000,6000,1500,2000,2000,4000,8000,1000,1000,4000,4000,4000,4000,4000,8000,16000,16000,4000,12000,12000,16000,16000,1000,2000,2000,4000,8000,8000,16000,16000,16000,8000,8000,8000,16000,16000,16000,4000,8000,4000,8000,8000,8000,16000,16000,16000,16000,16000,16000,8000,16000,32000,32000,32000,24000,32000,32000,1000,2000,4000,6000,8000,8000,12000,16000,8000,8000,16000,24000,16000,2000,4000,8000,4000,4000,16000,4000,2000,4000,8000,8000,1000,2000,4000,6000,8000,16000,12000,12000,16000,24000,32000,32000,8000,32000,32000,32000,32000,64000,64000,4000,4000,4000,4000,8000,8000,8000,8000,4000};

				double[] cach = {256,32,32,32,32,64,64,64,64,128,0,4,65,65,0,0,8,0,0,142,0,0,0,0,0,4,0,4,4,4,131,131,30,30,30,30,8,8,8,8,8,8,8,8,9,9,8,0,4,8,0,0,16,0,16,16,16,0,0,0,0,0,0,64,64,128,0,0,0,8,8,8,0,0,6,6,0,6,6,6,0,8,8,0,0,0,0,8,32,32,32,32,8,1,64,64,64,0,0,0,0,16,0,0,0,0,0,0,0,4,8,0,8,8,8,16,16,2,8,16,8,32,0,0,0,0,8,8,24,24,48,0,24,24,12,24,24,0,16,2,32,32,4,16,32,64,64,64,64,16,32,64,64,128,32,64,256,0,0,0,0,0,0,0,0,32,32,64,160,128,0,0,64,0,0,1,2,2,8,16,32,8,8,8,16,16,32,8,32,64,32,64,128,32,24,48,112,112,96,128,0,0,0,0,0,32,0,32,0};


				double[] chmin = {16,8,8,8,8,8,16,16,16,32,1,1,1,1,1,4,4,7,5,8,5,5,6,6,1,1,1,1,1,1,12,12,12,12,12,12,1,1,1,3,3,3,3,3,3,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,8,1,1,1,3,3,3,3,3,3,3,6,6,6,1,1,6,1,2,3,2,1,3,3,4,1,1,1,1,1,1,6,12,16,8,8,8,1,1,1,1,1,1,1,1,1,1,1,1,3,3,3,1,1,1,3,1,3,6,6,0,0,0,3,3,1,1,1,1,2,2,3,3,6,6,8,1,1,1,1,1,1,1,5,5,5,8,6,8,8,8,16,8,8,16,1,1,1,1,1,1,1,1,16,16,4,4,16,1,1,1,1,4,1,1,3,1,1,1,1,1,3,6,4,4,6,6,12,8,12,24,5,6,26,52,52,12,12,1,1,1,1,1,2,2,0,0};

				double[] chmax = {128,32,32,32,16,32,32,32,32,64,2,6,8,8,4,32,15,32,16,64,32,20,64,64,12,3,6,3,5,6,24,24,24,24,24,24,4,5,5,5,5,5,6,6,12,12,1,1,5,8,1,1,3,2,2,2,2,4,4,4,4,4,20,38,38,38,10,10,24,19,24,48,24,24,24,24,24,24,24,31,4,64,112,2,6,6,8,20,20,54,54,54,20,12,16,24,24,24,16,1,5,6,1,1,1,1,1,2,2,2,2,6,6,6,6,6,6,6,4,5,12,12,0,0,0,6,6,6,6,6,10,6,6,6,16,16,16,128,3,5,6,6,6,6,6,8,8,10,16,8,16,24,24,32,24,24,24,4,6,6,8,8,8,8,8,32,32,8,8,32,2,4,5,7,7,8,5,8,14,14,13,3,5,8,16,14,12,8,12,24,16,16,32,28,26,52,104,104,176,176,3,3,3,3,8,8,14,0,0};

				double[] perf = {198,269,220,172,132,318,367,489,636,1144,38,40,92,138,10,35,19,28,31,120,30,33,61,76,23,69,33,27,77,27,274,368,32,63,106,208,20,29,71,26,36,40,52,60,72,72,18,20,40,62,24,24,138,36,26,60,71,12,14,20,16,22,36,144,144,259,17,26,32,32,62,64,22,36,44,50,45,53,36,84,16,38,38,16,22,29,40,35,134,66,141,189,22,132,237,465,465,277,185,6,24,45,7,13,16,32,32,11,11,18,22,37,40,34,50,76,66,24,49,66,100,133,12,18,20,27,45,56,70,80,136,16,26,32,45,54,65,30,50,40,62,60,50,66,86,74,93,110,143,105,214,277,370,510,214,326,510,8,12,17,21,24,34,42,46,51,116,100,140,212,25,30,41,25,50,50,30,32,38,60,109,6,11,22,33,58,130,75,113,188,173,248,405,70,114,208,307,397,915,1150,12,14,18,21,42,46,52,67,45};

				double[] estperf = {199,253,253,253,132,290,381,381,749,1238,23,24,70,117,15,64,23,29,22,124,35,39,40,45,28,21,28,22,28,27,102,102,74,74,138,136,23,29,44,30,41,74,74,74,54,41,18,28,36,38,34,19,72,36,30,56,42,34,34,34,34,34,19,75,113,157,18,20,28,33,47,54,20,23,25,52,27,50,18,53,23,30,73,20,25,28,29,32,175,57,181,181,32,82,171,361,350,220,113,15,21,35,18,20,20,28,45,18,17,26,28,28,31,31,42,76,76,26,59,65,101,116,18,20,20,30,44,44,82,82,128,37,46,46,80,88,88,33,46,29,53,53,41,86,95,107,117,119,120,48,126,266,270,426,151,267,603,19,21,26,35,41,47,62,78,80,80,142,281,190,21,25,67,24,24,64,25,20,29,43,53,19,22,31,41,47,99,67,81,149,183,275,382,56,182,227,341,360,919,978,24,24,24,24,37,50,41,47,25};

				int sampleSize = estperf.length;
				int varSize = 8;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[2] = "syct";
				columnNames[3] = "mmin";
				columnNames[4] = "mmax";
				columnNames[5] = "cach";
				columnNames[6] = "chmin";
				columnNames[7] = "chmax";
				columnNames[0] = "perf";
				columnNames[1] = "estperf";

				for (int i = 0; i < sampleSize; i++) {
					example[i][2] = syct[i] + "";
					example[i][3] = mmin[i] + "";
					example[i][4] = mmax[i] + "";
					example[i][5] = cach[i] + "";
					example[i][6] = chmin[i] + "";
					example[i][7] = chmax[i] + "";
					example[i][0] = perf[i] + "";
					example[i][1] = estperf[i] + "";
				}
				dataTable = new JTable(example, columnNames);
				break;

			}

			case ExampleData.MULTI_LINEAR_REGRESSION_6: {

				dataTable = MultiLinearRegressionBostonExample.getBostonExample();
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
