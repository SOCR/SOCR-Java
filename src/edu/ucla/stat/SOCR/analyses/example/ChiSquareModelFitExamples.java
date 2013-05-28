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
public class ChiSquareModelFitExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	public JTable dataTable;
	private final String DOT = ".";
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, false, false, false, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/

	public  ChiSquareModelFitExamples() {
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  ChiSquareModelFitExamples(int analysisType, int exampleID) {


		switch (exampleID) {
			case ExampleData.CHI_SQUARE_MODEL_FIT_1: {

				// rice page 241 using poisson model.
				double[] obs = {18, 28, 56, 105, 126, 146, 164, 161, 123, 101, 74, 53, 23, 15, 9, 5};
				double[] exp = {12.2, 27, 56.5, 94.9, 132.7, 159.1, 166.9, 155.6, 130.6, 99.7, 69.7, 45, 27, 15.1, 7.9, 7.1};

				try {
					double sum = AnalysisUtility.sum(obs);

					System.out.println("ChiSquareModelFitExamples sum = " + sum);
				} catch (Exception e) {
				}
				int sampleSize = obs.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "OBS";
				columnNames[1] = "EXP";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = obs[i] + "";
					example[i][1] = exp[i] + "";
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
