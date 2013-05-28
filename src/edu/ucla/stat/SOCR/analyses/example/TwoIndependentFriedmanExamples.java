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
public class TwoIndependentFriedmanExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	public final int NULL_EXAMPLE = 0;
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, false, false, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  TwoIndependentFriedmanExamples() {
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  TwoIndependentFriedmanExamples(int analysisType, int exampleID) {
		//System.out.println("call TwoIndependentFriedmanExamples");

		switch (exampleID) {
			case NULL_EXAMPLE : { // and for TWO_INDEPENDENT_KW_T.
			}

			case ExampleData.TWO_INDEPENDENT_FRIEDMAN_1: { // and for TWO_INDEPENDENT_KW_T.


				// conover page 298
				double[] a = {5,1,16,5,10,19,10};
				double[] b = {4,3,12,4,9,18,7};
				double[] c = {7,1,22,3,7,18,6};
				double[] d = {10,0,22,5,13,37,8};

				double[] e = {12,2,35,4,10,58,7};

				// conover page 298


				int sampleSize = a.length;
				int varSize = 5;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0] = "A";
				columnNames[1] = "B";
				columnNames[2] = "C";
				columnNames[3] = "D";
				columnNames[4] = "E";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = a[i] + "";
					example[i][1] = b[i] + "";
					example[i][2] = c[i] + "";
					example[i][3] = d[i] + "";
					example[i][4] = e[i] + "";

				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}
			case ExampleData.TWO_INDEPENDENT_FRIEDMAN_2: { // and for TWO_INDEPENDENT_KW_T.

				// rice, page 470.

				double[] a = {5,6,7,6,3, 7,7,1,5,4};
				double[] b = {7,5,6,7,4,3,5,2,3,7};
				double[] c = {1,1,4,1,2,1,1,5,2,5};
				double[] d = {6,2,2,4,5,5,2,3,4,2};

				double[] e = {3.5,3,1,3,1,2,3,7,6,1};
				double[] f = {2,7,5,2,7,4,6,6,7,3};
				double[] g = {3.5,4,3,5,6,6,4,4,1,6};



				int sampleSize = a.length;
				int varSize = 7;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
/*
				columnNames[0] = "NoDrug";
				columnNames[1] = "Placebo";
				columnNames[2] = "Papaverine";
				columnNames[3] = "Morphine";
				columnNames[4] = "AP";
				columnNames[5] = "PB";
				columnNames[6] = "TN";
*/

				columnNames[0] = "A";
				columnNames[1] = "B";
				columnNames[2] = "C";
				columnNames[3] = "D";
				columnNames[4] = "E";
				columnNames[5] = "F";
				columnNames[6] = "G";



				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = a[i] + "";
					example[i][1] = b[i] + "";
					example[i][2] = c[i] + "";
					example[i][3] = d[i] + "";
					example[i][4] = e[i] + "";
					example[i][5] = f[i] + "";
					example[i][6] = g[i] + "";

				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			/*

			case ExampleData.TWO_INDEPENDENT_KW_3 : {
				// R's example: oats, using catagorical variable B
					double[] y1={111,130,157,174,117,114,161,141,105,140,118,156};
					double[] y2={61,91,97,100,70,108,126,149,96,124,121,144};
					double[] y3={68,64,112,86,60,102,89,96,89,129,132,124};
					double[] y4={74,89,81,122,64,103,132,133,70,89,104,117};
					double[] y5={62,90,100,116,80,82,94,126,63,70,109,99};
					double[] y6={53,74,118,113,89,82,86,104,97,99,119,121};

					int sampleSize = y1.length;
					int varSize = 6;
					example = new String[sampleSize][varSize];
					columnNames = new String[varSize];
					columnNames[0] = "I";
					columnNames[1] = "II";
					columnNames[2] = "III";
					columnNames[3] = "IV";
					columnNames[4] = "V";
					columnNames[5] = "VI";


					for (int i = 0; i < sampleSize; i++) {
						example[i][0] = y1[i] + "";
						example[i][1] = y2[i] + "";
						example[i][2] = y3[i] + "";
						example[i][3] = y4[i] + "";
						example[i][4] = y5[i] + "";
						example[i][5] = y6[i] + "";
					}
					dataTable = new JTable(example,  columnNames);

					break;
			}

			case ExampleData.TWO_INDEPENDENT_KW_4: {
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
			case ExampleData.TWO_INDEPENDENT_KW_5: {

			}
*/
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
