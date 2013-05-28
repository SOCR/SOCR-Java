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

/** Example data generator for ANOVA & other statistical applets. */
public class LogisticRegressionExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";
	private static String dataSource = "http://wiki.stat.ucla.edu/socr/index.php/SOCR_Data_Oct2009_ID_NI";
	public static boolean[] availableExamples = new boolean[]{true, false, false, false, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  LogisticRegressionExamples() {
		this.dataTable = getRandomExample();
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  LogisticRegressionExamples(int analysisType, int exampleID) {
		//System.out.println("MultiLinearRegressionExamples exampleID " + exampleID);

		switch (exampleID) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}
			case ExampleData.LOGISTIC_REGRESSION_1: {
				double[] age = {9.0842,7.9097,14.8145,7.7016,6.5572,15.7618,15.6632,16.0712,13.1362,13.3415,14.6721,14.6311,17.1253,17.6345};

				double[] dx={0,0,0,0,0,0,1,1,1,1,1,1,1,1};

				double[] sex={2,2,1,1,1,1,2,1,2,2,2,1,2,2};

				double[] iq={110,87,97,91,91,114,96,99,65,96,84,86,93,87};

				double[] tbv={1203343.63,1319736.75,1688990.25,1292640.88,1512570.63,1635918.38,1331777.25,1487885.63,1066075.13,1297326.75,1499982.88,1861990.75,1861990.75,1476890.5};

				int sampleSize = Math.max(age.length, Math.max(dx.length, Math.max(sex.length, Math.max(iq.length, tbv.length))));
				int varSize = 5;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "Age";
				columnNames[1] = "DX";
				columnNames[2] = "Sex";
				columnNames[3] = "FS_IQ";
				columnNames[4] = "TBV";

				for (int i = 0; i < sampleSize; i++) {
					try { example[i][0] = age[i] + "";} 
					catch (Exception ex) { example[i][0] = "";}
					try { example[i][0] = age[i] + "";} 
					catch (Exception ex) { example[i][0] = "";}
					try { example[i][1] = dx[i] + "";} 
					catch (Exception ex) { example[i][0] = "";}
					try { example[i][2] = sex[i] + "";} 
					catch (Exception ex) { example[i][0] = "";}
					try { example[i][3] = iq[i] + "";} 
					catch (Exception ex) { example[i][0] = "";}
					try { example[i][4] = tbv[i] + "";} 
					catch (Exception ex) { example[i][0] = "";}
				}
				dataTable = new JTable(example,  columnNames);
				break;
			}
			
		}
	}

	public JTable getRandomExample() {
		//now let's generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		example = new String[2][size];
		columnNames = new String[2];
		double x, y;
		
		columnNames[0]="Predictor";
		columnNames[1] = "Response";

		for (i = 0; i < size; i++) {
			 x = Math.random();
			 y = (int)(Math.random()+0.499);
			 example[0][i] = String.valueOf(x);
			 example[1][i] = String.valueOf(y);
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
