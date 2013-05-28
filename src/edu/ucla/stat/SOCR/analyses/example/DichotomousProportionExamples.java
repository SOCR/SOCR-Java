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
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

/** Example data generator for ANOVA & other statistical applets. */
public class DichotomousProportionExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, false, false, false, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/

	public  DichotomousProportionExamples() {
	}

	public  DichotomousProportionExamples(int analysisType, int exampleID) {
		//System.out.println("in DichotomousProportionExamples");

		switch (exampleID) {
			case ExampleData.DICHOTOMOUS_PROPORTION_1: {

				//String[] x = new String[500];

				int sampleSize = 500;
				int varSize = 1;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "X";

				for (int i = 0; i < 17; i++) {
					example[i][0] = "1";
				}

				for (int i = 17; i < sampleSize; i++) {
					example[i][0]= "0";
				}

				dataTable = new JTable(example,  columnNames);

		    		break;
			}
		}
	}

	/** returns a JTable object containing the Example Data */
	public JTable getExample() {
		//System
		return dataTable;
	 }
	public static String getExampleSource() {
		return dataSource;
	}
}
