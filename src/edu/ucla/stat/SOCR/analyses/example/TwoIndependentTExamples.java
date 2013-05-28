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
public class TwoIndependentTExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, true, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  TwoIndependentTExamples() {
		this.dataTable = getRandomExample();
	}


	/** Constructor method for sampel data for ANOVA analysis.*/

	public  TwoIndependentTExamples(int analysisType, int exampleID) {
		this.dataTable = getExample(exampleID);

	}
	public JTable getExample(int exampleID) {

		//System.out.println("TWO_INDEPENDENT_TExamples exampleID " + exampleID);

		switch (exampleID) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}

			case ExampleData.TWO_INDEPENDENT_T_1: { // and for TWO_PAIRED_T.
				// rice page 390
				double[] a = {79.98, 80.04, 80.02, 80.04, 80.03, 80.03, 80.04, 79.97, 80.05, 80.03, 80.02, 80.00, 80.02};
				double[] b = {80.02, 79.94, 79.98, 79.97, 79.97, 80.03, 79.95, 79.97};

				int sampleSize = Math.max(a.length, b.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";


				for (int i = 0; i < sampleSize; i++) {
					try {
						example[i][0] = a[i] + "";
					} catch (Exception e) {
						example[i][0] = "";
					}
					try {
						example[i][1] = b[i] + "";
					} catch (Exception e) {
						example[i][1] = "";
					}

				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.TWO_INDEPENDENT_T_2:  {
				// jennrich page 38
				double[] y1 = {57, 63, 66, 60, 58, 64, 62, 65};
				double[] y2 = {68, 71, 67, 72, 64, 69, 70, 75, 65, 73};

				int sampleSize = Math.max(y1.length, y2.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "Y1";
				columnNames[1] = "Y2";


				for (int i = 0; i < sampleSize; i++) {
				try {
						example[i][0] = y1[i] + "";
					} catch (Exception e) {
						example[i][0] = "";
					}
					try {
						example[i][1] = y2[i] + "";
					} catch (Exception e) {
						example[i][1] = "";
					}				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.TWO_INDEPENDENT_T_3 : {
				// rice p 405
				double[] y1 = {7.5, 19, 11.5, 19, 15.5,  15.5, 19, 4.5, 21, 15., 11.5, 9, 11.5};
				double[] y2 = {11.5, 1, 7.5, 4.5, 4.5, 15.5, 2, 4.5};

				int sampleSize = Math.max(y1.length, y2.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";


				for (int i = 0; i < sampleSize; i++) {
					try {
						example[i][0] = y1[i] + "";
					} catch (Exception e) {
						example[i][0] = "";
					}
					try {
						example[i][1] = y2[i] + "";
					} catch (Exception e) {
						example[i][1] = "";
					}				}
				dataTable = new JTable(example,  columnNames);

		    		break;
		    	}

			case ExampleData.TWO_INDEPENDENT_T_4: {
				// rice p 427
				double[] y1 = {3.03, 5.53, 5.6, 9.3, 9.92, 12.51, 12.95, 15.21, 16.04, 16.84};
				double[] y2 = {3.19, 4.26, 4.47, 4.53, 4.67, 4.69, 12.78, 6.79, 9.37, 12.75};

				int sampleSize = Math.max(y1.length, y2.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";


				for (int i = 0; i < sampleSize; i++) {
					try {
						example[i][0] = y1[i] + "";
					} catch (Exception e) {
						example[i][0] = "";
					}
					try {
						example[i][1] = y2[i] + "";
					} catch (Exception e) {
						example[i][1] = "";
					}				}
				dataTable = new JTable(example,  columnNames);

		    		break;
		    	}
			case ExampleData.TWO_INDEPENDENT_T_5: {
				// jennrich page 49
				double[] a = {14, 29, 13, 18, 30, 30, 15, 13, 26, 19, 14, 25, 19};
				double[] b = {17, 11, 16, 14, 17, 10, 10, 15, 12, 20, 15, 13, 14, 15, 19};

				int sampleSize = Math.max(a.length, b.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";


				for (int i = 0; i < sampleSize; i++) {
					try {
						example[i][0] = a[i] + "";
					} catch (Exception e) {
						example[i][0] = "";
					}
					try {
						example[i][1] = b[i] + "";
					} catch (Exception e) {
						example[i][1] = "";
					}				}
				dataTable = new JTable(example,  columnNames);

		    		break;

			}

		}
		return dataTable;
	}


	public JTable getRandomExample() {
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
		return dataTable;
	}
	/** returns a JTable object containing the Example Data */
	public JTable getExample() {
		return dataTable;
	 }
	public static String getExampleSource() {
		return dataSource;
	}
}
