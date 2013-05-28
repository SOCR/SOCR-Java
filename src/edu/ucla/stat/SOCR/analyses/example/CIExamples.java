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
public class CIExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	public static final short NULL_EXAMPLE = 0;
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, false, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  CIExamples() {
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  CIExamples(int analysisType, int exampleID) {

		//System.out.println("call CIExamples");
		switch (exampleID) {
			case NULL_EXAMPLE : { // and for TWO_INDEPENDENT_KW_T.
			}

			case ExampleData.CI_1:  {
				// example from R library(MASS) InspectorSpray
				double[] a = {0.08, 0.10, 0.15, 0.17, 0.24, 0.34, 0.38, 0.42, 0.49, 0.50, 0.70, 0.94, 0.95, 1.26, 1.37, 1.55, 1.75, 3.20, 6.98, 50.57};
				double[] b = {0.11, 0.18, 0.23, 0.51, 1.19, 1.30, 1.32, 1.73, 2.06, 2.16, 2.37, 2.91, 4.50, 4.51, 4.66, 14.68, 14.82, 27.44, 39.41, 41.04};

				int sampleSize = Math.max(a.length, b.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "C1";
				columnNames[1] = "C2";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = a[i] + "";
					example[i][1] = b[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}


			case ExampleData.CI_2:  {
				// see end of class for example web link
				int sampleSize = Math.max(a.length, Math.max(b.length, c.length));
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "C1";
				columnNames[1] = "C2";
				columnNames[2] = "C3";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = a[i] + "";
					example[i][1] = b[i] + "";
					example[i][2] = c[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.CI_3: {
				// R's example: oats, using catagorical variable V
					double[] v1={111,130,157,174,61,91,97,100,68,64,112,86,74,89,81,122,62,90,100,116,53,74,118,113};
					double[] v2={117,114,161,141,70,108,126,149,60,102,89,96,64,103,132,133,80,82,94,126,89,82,86,104};
					double[] v3={105,140,118,156,96,124,121,144,89,129,132,124,70,89,104,117,63,70,109,99,97,99,119,121};
					int sampleSize = v1.length;

					int varSize = 3;
					example = new String[sampleSize][varSize];
					columnNames = new String[varSize];
					columnNames[0] = "C1";
					columnNames[1] = "C2";
					columnNames[2] = "C3";


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
	private final static double[] a=new double[]{11.78,
		7.50,
		1.46,
		4.88,
		1.69,
		3.24,
		2.30,
		8.40,
		16.08,
		6.65		
	};
	private final static double[] b=new double[]{0.39,
		18.66,
		15.79,
		19.38,
		10.56,
		18.35,
		4.49,
		13.42,
		16.13,
		11.17};
	private final static double[] c=new double[]{0.48,
		5.14,
		18.15,
		10.05,
		10.03,
		19.39,
		17.56,
		18.96,
		17.03,
		16.64};
}
