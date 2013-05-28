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
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.*;

/** Example data generator for ANOVA & other statistical applets. */
public class AnovaOneWayExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	private static String dataSource = "";
	static public boolean[] availableExamples = new boolean[]{true, true, true, true, true, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  AnovaOneWayExamples() {
		/*
		StringBuffer f3 = new StringBuffer();

		//now let's generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		//System.out.println("AnovaOneWayExamples randome data size = " + size);
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

	public  AnovaOneWayExamples(int analysisType, int exampleID) {

		switch (exampleID) {
			case NULL_EXAMPLE : { // and for TWO_PAIRED_T.
				this.getNullExample();
				break;

			}

			case ExampleData.ANOVA_ONE_WAY_1: { // and for TWO_PAIRED_T.
				String[] group = {"1","1","1","1","1","1", "2","2","2","2","2","2","2","2", "3","3","3","3","3"};
				double[] x = {93,67,77,92,97,62, 136,120,115,104,115,121,102,130, 198,217,209,221,190};



		    		//"An example two-way ANOVA with replication" + newln +
				//"Dependent  Factor A  Factor B " + newln +
				// Dr Jennrich's book page 199.

				int sampleSize = group.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]="Y";
				columnNames[1] = "J";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = group[i];
					example[i][0] = x[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.ANOVA_ONE_WAY_2:  { // and for TWO_PAIRED_T.
				String[] group = {"1","1","1","1","1","1","1", "2","2","2","2","2", "3","3","3","3","3","3"};
				double[] x = {42,22,30,17,24, 23, 28, 41, 37, 40, 48, 21, 47, 55, 37, 30, 53, 55};



		    		//"An example two-way ANOVA with replication" + newln +
				//"Dependent  Factor A  Factor B " + newln +
				// Dr Jennrich's book page 199.

				int sampleSize = group.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0]="Y";
				columnNames[1] = "J";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = group[i];
					example[i][0] = x[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;			}

			case ExampleData.ANOVA_ONE_WAY_3 : {
				String[] group = {"1","1","1","1","1", "2","2","2","2","2", "3","3","3","3","3","4","4","4","4","4"};
				double[] x = {279, 338, 334, 198, 303, 378, 275, 412, 265, 286, 172, 335, 335, 282, 250, 381, 346, 340, 471, 318};




		    		//"An example two-way ANOVA with replication" + newln +
				//"Dependent  Factor A  Factor B " + newln +
				// Dr Jennrich's book page 199.

				int sampleSize = group.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]="Y";
				columnNames[1] = "J";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = group[i];
					example[i][0] = x[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.ANOVA_ONE_WAY_4: {

				String[] group = {"1","1","1","1","1","1","1","1","1","1","2","2","2","2","2", "2","2","2","2","2", "3","3","3","3","3","3","3","3","3","3", "4","4","4","4","4", "4","4","4","4","4", "5", "5","5","5","5","5", "5","5","5","5", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "7", "7","7","7","7","7", "7","7","7","7" };

				double[] x = {4.13, 4.07, 4.04, 4.07, 4.05, 4.04, 4.02, 4.06, 4.10, 4.04, 3.86, 3.85, 4.08, 4.11, 4.08, 4.01, 4.02, 4.04, 3.97, 3.95, 4, 4.02, 4.01, 4.01, 4.08, 4.01, 4.02, 4.04, 3.97, 3.95, 3.88, 3.88, 3.91, 3.95, 3.92, 3.97, 3.92, 3.90, 3.97, 3.9, 4.02, 3.95, 4.02, 3.89, 3.91, 4.01, 3.89, 3.89, 3.99, 4, 4.02, 3.86, 3.96, 3.97, 4, 3.82, 3.98, 3.99, 4.02, 3.93, 4, 4.02, 4.03, 4.04, 4.1, 3.81, 3.91, 3.96, 4.05, 4.06};
				int sampleSize = group.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]="Y";
				columnNames[1] = "J";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = group[i];
					example[i][0] = x[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}
			case ExampleData.ANOVA_ONE_WAY_5: {

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
		}

	}
	// Jennrich page 199
	static void anovaOneWayTest1() {
		Data data = new Data();
		//String[] group = {"1","1","1","1","1","1", "2","2","2","2","2","2","2","2", "3","3","3","3","3"};

		//double[] score = {93,67,77,92,97,62, 136,120,115,104,115,121,102,130, 198,217,209,221,190};
		//String[] group = {"1","1","1","1","1","1", "2","2","2","2","2","2","2","2"};

		//double[] score = {93,67,77,92,97,62, 136,120,115,104,115,121,102,130};

		//String[] group = {"1","1","1","1","1","1","1","1","1","1","2","2","2","2","2", "2","2","2","2","2", "3","3","3","3","3","3","3","3","3","3", "4","4","4","4","4", "4","4","4","4","4", "5", "5","5","5","5","5", "5","5","5","5","6", "6", "6", "6", "6", "6", "6", "6", "6", "6"};

		//double[] x = {4.13, 4.07, 4.04, 4.07, 4.05, 4.04, 4.02, 4.06, 4.10, 4.04,3.86, 3.85, 4.08, 4.11, 4.08, 4.01, 4.02, 4.04, 3.97, 3.95,4, 4.02, 4.01, 4.01, 4.08, 4.01, 4.02, 4.04, 3.97, 3.95,3.88, 3.88, 3.91, 3.95, 3.92, 3.97, 3.92, 3.90, 3.97, 3.9,4.02, 3.95, 4.02, 3.89, 3.91, 4.01, 3.89, 3.89, 3.99, 4,4.02, 3.86, 3.96, 3.97, 4, 3.82, 3.98, 3.99, 4.02, 3.93};

		String[] eth = {"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N"};

		String[] sex = {"M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"};

		String[] age = {"F0", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F0", "F0", "F0", "F0", "F0", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F1", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F2", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3", "F3"};

		String[] learn = {"SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "AL", "AL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "SL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL", "AL"};

		double[] days={2,11,14,5,5,13,20,22,6,6,15,7,14,6,32,53,57,14,16,16,17,40,43,46,8,23,23,28,34,36,38,3,5,11,24,45,5,6,6,9,13,23,25,32,53,54,5,5,11,17,19,8,13,14,20,47,48,60,81,2,0,2,3,5,10,14,21,36,40,6,17,67,0,0,2,7,11,12,0,0,5,5,5,11,17,3,4,22,30,36,8,0,1,5,7,16,27,0,30,10,14,27,41,69,25,10,11,20,33,5,7,0,1,5,5,5,5,7,11,15,5,14,6,6,7,28,0,5,14,2,2,3,8,10,12,1,1,9,22,3,3,5,15,18,22,37};



		data.appendX("J", learn, DataType.FACTOR);
		data.appendY("DAYS", days, DataType.QUANTITATIVE);
		try {
			Result result = data.getAnalysis(AnalysisType.ANOVA_ONE_WAY);
		} catch (Exception e) {
			//////////////////////System.out.println("Data " + e.toString());
		}
	}

	/** returns a JTable object containing the Example Data */
	public JTable getExample() {
		return dataTable;
	 }
	public static String getExampleSource() {
		return dataSource;
	}
	public static void main(String args[]) {
		anovaOneWayTest1();
	}
}
