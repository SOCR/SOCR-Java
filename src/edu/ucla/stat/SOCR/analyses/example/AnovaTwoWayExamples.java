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
public class AnovaTwoWayExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	public final String BOOK_JENNRICH = "R.I. Jennrich,  An Introduction to Computational Statistics.";
	public final String BOOK_RICE = "J.A. Rice Mathematical Statistics and Data Analysis.";
	public final String BOOK_CONOVER = "";
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, true, true, true, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  AnovaTwoWayExamples() {
		/*
		StringBuffer f3 = new StringBuffer();

		//now let's generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		//System.out.println("AnovaTwoWayExamples randome data size = " + size);
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

	public  AnovaTwoWayExamples(int analysisType, int exampleID) {
		//System.out.println("AnovaTwoWayExamples exampleID " + exampleID);

		switch (exampleID) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}

			case ExampleData.ANOVA_TWO_WAY_1: { // and for TWO_PAIRED_T.
				String[] k = {"1","1","1","2","2","2"};
				String[] j = {"1","2","3","1","2","3"};
				double[] x = {93, 136, 198, 88, 148, 279};

/*
				String[] k = {"1","1","1","2","2","1","1","2","2","2","2","1","1","1","2","2"};
				String[] j = {"1","1","1","1","1", "2","2","2","2","2","2", "3","3","3","3","3"};
				double[] x = {20,22,22,38,29,25,32,31,29,34,35,29,32,32,49,40};
*/

				int sampleSize = x.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "Y";
				columnNames[1] = "I";
				columnNames[2] = "J";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = x[i] + "";
					example[i][1] = k[i];
					example[i][2] = j[i];
				}
				dataTable = new JTable(example,  columnNames);

		    		break;

			}

			case ExampleData.ANOVA_TWO_WAY_2:  { // and for TWO_PAIRED_T.

				String[] k = {"1", "1", "1", "1", "1", "1", "1", "1","1", "1", "1", "1", "1", "1", "1", "1",  "2",  "2",  "2",  "2",  "2",  "2",  "2",  "2", "2",  "2",  "2",  "2",  "2",  "2",  "2",  "2",   "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3",  "3"};


				String[] j = {"1", "1", "1", "1", "2", "2", "2", "2", "3",  "3", "3", "3", "4", "4", "4", "4", "1", "1", "1", "1", "2", "2", "2", "2", "3",  "3", "3", "3", "4", "4", "4", "4", "1", "1", "1", "1", "2", "2", "2", "2", "3",  "3", "3", "3", "4", "4", "4", "4"};

				double[] x = {3.1,4.5,4.6,4.3,8.2,11,8.8,7.2,4.3,4.5,6.3,7.6,4.5,7.1,6.6,6.2,
				3.6,2.9,4,2.3,9.2,9.1,4.9,12.4,4.4,3.5,3.1,4,5.6,10.2,7.1,3.8,
				2.2,2.1,1.8,2.3,3,3.7,3.8,2.9,2.3,2.5,2.4,2.2,3,3.6,3.1,3.3};

				int sampleSize = x.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "Y";
				columnNames[1] = "I";
				columnNames[2] = "J";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = x[i] + "";
					example[i][1] = k[i];
					example[i][2] = j[i];
				}
				dataTable = new JTable(example,  columnNames);

		    		break;

			}

			case ExampleData.ANOVA_TWO_WAY_3 : {
				String UF = "UF", W = "W", M = "M", C = "C", GR = "GR", D = "D";
				String S = "S", V = "V", T = "T", P = "P";

				String[] loc = {UF,UF,UF,UF,UF,W,W,W,W,W,M,M,M,M,M,C,C,C,C,C,GR,GR,GR,GR,GR,D,D,D,D,D};
				String[] var = {M,S,V,T,P,M,S,V,T,P,M,S,V,T,P,M,S,V,T,P,M,S,V,T,P,M,S,V,T,P};
				double[] y1 = {81.0,105.4,119.7,109.7,98.3,146.6,142.0,150.7,191.5,145.7,82.3,77.3,78.4,131.3,89.6,119.8,121.4,124.0,140.8,124.8,98.9,89.0,69.1,89.3,104.1,86.9,77.1,78.9,101.8,96.0};
				double[] y2 = {80.7,82.3,80.4,87.2,84.2,100.4,115.5,112.2,147.7,108.1,103.1,105.1,116.5,139.9,129.6,98.9,61.9,96.2,125.5,75.7,66.4,49.9,96.7,61.9,80.3,67.7,66.7,67.4,91.8,94.1};
				int sampleSize = y1.length;
				int varSize = 4;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "Y1";
				columnNames[1] = "Loc";
				columnNames[2] = "Var";
				columnNames[3] = "Y2";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = y1[i] + "";
					example[i][1] = loc[i]+ "";
					example[i][2] = var[i]+ "";
					example[i][3] = y2[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.ANOVA_TWO_WAY_4: {
				String[] district = {"1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","3","3","3","3","3","3","3","3","3","3","3","3","3","3","3","3","4","4","4","4","4","4","4","4","4","4","4","4","4","4","4","4"};
				String[] group = {"<1l","<1l","<1l","<1l","1-1.5l","1-1.5l","1-1.5l","1-1.5l","1.5-2l","1.5-2l","1.5-2l","1.5-2l",">2l",">2l",">2l",">2l","<1l","<1l","<1l","<1l","1-1.5l","1-1.5l","1-1.5l","1-1.5l","1.5-2l","1.5-2l","1.5-2l","1.5-2l",">2l",">2l",">2l",">2l","<1l","<1l","<1l","<1l","1-1.5l","1-1.5l","1-1.5l","1-1.5l","1.5-2l","1.5-2l","1.5-2l","1.5-2l",">2l",">2l",">2l",">2l","<1l","<1l","<1l","<1l","1-1.5l","1-1.5l","1-1.5l","1-1.5l","1.5-2l","1.5-2l","1.5-2l","1.5-2l",">2l",">2l",">2l",">2l"};
				String[] age = {"<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35","<25","25-29","30-35",">35"};
				double[] holders = {197,264,246,1680,284,536,696,3582,133,286,355,1640,24,71,99,452,85,139,151,931,149,313,419,2443,66,175,221,1110,9,48,72,322,35,73,89,648,53,155,240,1635,24,78,121,692,7,29,43,245,20,33,40,316,31,81,122,724,18,39,68,344,3,16,25,114};
				double[] claims = {38,35,20,156,63,84,89,400,19,52,74,233,4,18,19,77,22,19,22,87,25,51,49,290,14,46,39,143,4,15,12,53,5,11,10,67,10,24,37,187,8,19,24,101,3,2,8,37,2,5,4,36,7,10,22,102,5,7,16,63,0,6,8,33};

				int sampleSize = district.length;
				int varSize = 5;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "Claims";
				columnNames[1] = "District";
				columnNames[2] = "Group";
				columnNames[3] = "Age";
				columnNames[4] = "Holders";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = claims[i] + "";
					example[i][1] = district[i];
					example[i][2] = group[i];
					example[i][3] = age[i];
					example[i][4] = holders[i] + "";

				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}
			case ExampleData.ANOVA_TWO_WAY_5: {

				String[] eth = {"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N"};

				String[] sex = {"M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"};

				String[] age = {"F0", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3"};

				String[] learn = {"SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "AL", "AL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL"};

				double[] days={2,11,14,5,5,13,20,22,6,6,15,7,14,6,32,53,57,14,16,16,17,40,43,46,8,23,23,28,34,36,38,3,5,11,24,45,5,6,6,9,13,23,25,32,53,54,5,5,11,17,19,8,13,14,20,47,48,60,81,2,0,2,3,5,10,14,21,36,40,6,17,67,0,0,2,7,11,12,0,0,5,5,5,11,17,3,4,22,30,36,8,0,1,5,7,16,27,0,30,10,14,27,41,69,25,10,11,20,33,5,7,0,1,5,5,5,5,7,11,15,5,14,6,6,7,28,0,5,14,2,2,3,8,10,12,1,1,9,22,3,3,5,15,18,22,37};

				int sampleSize = days.length;
				int varSize = 5;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[1] = "Eth";
				columnNames[2] = "Sex";
				columnNames[3] = "Age";
				columnNames[4] = "Lrn";
				columnNames[0] = "Days";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = eth[i];
					example[i][2] = sex[i] + "";
					example[i][3] = age[i] + "";
					example[i][4] = learn[i] + "";
					example[i][0] = days[i] + "";

				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

		case ExampleData.ANOVA_TWO_WAY_6: {

			String[] k = {"1","1","1","2","2","1","1","2","2","2","2","1","1","1","2","2"};
			String[] j = {"1","1","1","1","1","2","2","2","2","2","2","3","3","3","3","3"};
			double[] x = {20,22,22,38,29,25,32,31,29,34,35,29,32,32,49,40};


			int sampleSize = x.length;
			int varSize = 3;
			example = new String[sampleSize][varSize];
			columnNames = new String[varSize];

			columnNames[1] = "I";
			columnNames[2] = "J";
			columnNames[0] = "Y";


			for (int i = 0; i < sampleSize; i++) {
				example[i][1] = k[i];
				example[i][2] = j[i] + "";
				example[i][0] = x[i] + "";

			}
			dataTable = new JTable(example,  columnNames);

			break;
			}

		case ExampleData.ANOVA_TWO_WAY_7: {

			String[] k = {"1","1","1","2","2","2","3","3","3"};
			String[] j = {"1","2","3","1","2","3","1","2","3"};
			double[] x = {3.97,4.24,4.44,2.36,2.61,2.82,2.76,2.75,3.01};


			int sampleSize = x.length;
			int varSize = 3;
			example = new String[sampleSize][varSize];
			columnNames = new String[varSize];

			columnNames[1] = "menu";
			columnNames[2] = "range";
			columnNames[0] = "hours";


			for (int i = 0; i < sampleSize; i++) {
				example[i][1] = k[i];
				example[i][2] = j[i] + "";
				example[i][0] = x[i] + "";

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
