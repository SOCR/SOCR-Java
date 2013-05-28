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
// the signed rank test is also known as the wilcoxon signed tank test. (Rice, page 413)
// note that the test itself is different from the two paired sample sign test (see Rice page 334, 426).
// for both tests, paired sample is used. but the method to determined the results have different calculation.


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
public class TwoIndependentWilcoxonExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	private static String dataSource = "";
	public static boolean[] availableExamples = TwoIndependentTExamples.availableExamples;

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  TwoIndependentWilcoxonExamples() {
		this.dataTable = (new TwoIndependentTExamples()).getRandomExample();
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  TwoIndependentWilcoxonExamples(int analysisType, int exampleID) {

		switch (exampleID) {
			default: {
				this.dataTable = (new TwoIndependentTExamples(analysisType, exampleID)).getExample(exampleID);
				break;
			}
			case ExampleData.TWO_INDEPENDENT_WILCOXON_6: {
				double[] a = {1,2,3,5};
				double[] b = {4,6,7,8,9};

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

			case ExampleData.TWO_INDEPENDENT_WILCOXON_7: {
				// from Conover p-218
				//double[] a = {14.8,7.3,5.6,6.3,9,4.2,  10.6,12.5,12.9,16.1,11.4,2.7};
				//double[] b = {12.7,14.2,12.6,2.1,17.7,11.8,  16.9,7.9,16,10.6,5.6,5.6,  7.6,11.3,8.3,6.7,3.6,1,  2.4,6.4,9.1,6.7,18.6,3.2,  6.2,6.1,15.3,10.6,1.8,5.9,   9.9,10.6,14.8,5,2.6,4};

				// from Hoel  -- Introduciont to mathematical statistics.
				double[] a = {8.2,10.4,10.6,11.5,12.6,12.9,13.3,   13.3,13.4,13.4, 13.6, 13.8,14,14,   14.1,14.2,14.6,14.7,14.9,15,   15.4,15.6,15.9,16,    16.2,16.3,17.2,17.4,   17.7, 18.1};
				double[] b = {9.9,12.3,13.2,   13.3,13.6,13.7,    14.1,14.5,14.5,14.6,    15.1,15.1,15.7,15.7,15.8,16,     16.1,16.3,16.7,16.8,16.8,17,    17.7,17.9,18,18,18.8, 18.8,20.4,21.5};

				int sampleSize = Math.max(a.length, b.length);
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "X";
				columnNames[1] = "Y";


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

	}

	/** returns a JTable object containing the Example Data */
	public JTable getExample() {
		return dataTable;
	 }
	public static String getExampleSource() {
		return dataSource;
	}
}
