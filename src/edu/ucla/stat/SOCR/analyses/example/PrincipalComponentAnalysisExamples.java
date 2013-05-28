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
//import edu.ucla.stat.SOCR.analyses.data.DataFrame;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

/** Example data generator for PCA & other statistical applets. */
public class PrincipalComponentAnalysisExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";
	private static String dataSource = "http://wiki.stat.ucla.edu/socr/index.php/SOCR_Data_Oct2009_ID_NI";
	public static boolean[] availableExamples = new boolean[]{true, false, false, false, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  PrincipalComponentAnalysisExamples() {
		
	}

	/** Constructor method for sampel data for PCA analysis.*/

	public  PrincipalComponentAnalysisExamples(int analysisType, int exampleID) {
		//System.out.println("MultiLinearRegressionExamples exampleID " + exampleID);

		switch (exampleID) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}
			case ExampleData.LOGISTIC_REGRESSION_1: {
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
