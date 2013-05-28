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
public class OneZExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, true, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/

	public  OneZExamples() {
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

	public  OneZExamples(int analysisType, int exampleID) {


		switch (exampleID) {
			case ExampleData.ONEZ_1: { // and for ONE_T.

				// rice page 412
				double[] before = {25,25,27,44,30,67,53,53,52,60,28};
				double[] after = {27,29,37,56,46,82,57,80,61,59,43};

				int sampleSize = before.length;
				int varSize = 1;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "X";
				//columnNames[1] = "After";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = AnalysisUtility.truncateDigits((after[i] - before[i]), 4) + "";
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
