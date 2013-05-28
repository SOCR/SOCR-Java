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
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.*;

/** Example data generator for ANOVA & other statistical applets. */
public class SimpleLinearRegressionExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, true, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  SimpleLinearRegressionExamples() {
		/*
		StringBuffer f3 = new StringBuffer();

		//now let's generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		//System.out.println("SimpleLinearRegressionExamples randome data size = " + size);
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

	public  SimpleLinearRegressionExamples(int analysisType, int exampleID) {
		//System.out.println("SimpleLinearRegressionExamples exampleID " + exampleID);

		switch (exampleID) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}
			case ExampleData.SIMPLE_LINEAR_REGRESSION_1: {


				double[] e1966 = {82, 97, 93, 75, 87, 130, 125, 126, 104};
				double[] e1976 = {85, 98, 102, 80, 90, 135, 135, 131, 110};


				int sampleSize = e1966.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "X";
				columnNames[1] = "Y";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = e1966[i] + "";
					example[i][1] = e1976[i] + "";
				}


				dataTable = new JTable(example,  columnNames);
				break;
			}
			case ExampleData.SIMPLE_LINEAR_REGRESSION_2: {
				// Rice, page 525
				double[] pressure = {25366,25356,25336,25256,25267,25306,25237,25267,25138,25148,25143,24731,24751,24771,24424,24444,24419,24117,24102,24092,25202,25157,25157};
				double[] temperature = {20.8,20.9,21,21.9,22.1,22.1,22.4,22.5,24.8,24.8,25,34,34,34.1,42.7,42.7,42.7,49.9,50.1,50.1,22.5,23.1,23};
				int sampleSize = pressure.length;
				int varSize = 2;

				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "pres";
				columnNames[1] = "temp";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = pressure[i] + "";
					example[i][1] = temperature[i] + "";
				}



				dataTable = new JTable(example, columnNames);
				//dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				//dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				break;
			}
			case ExampleData.SIMPLE_LINEAR_REGRESSION_3:  { // and for TWO_PAIRED_T.
				double[] m = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
				double[] h = {60,94,91,81,80,92,74,89,96,87,86,94,94,94,79,30,92,82,94,78};
				double[] f = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};



				int sampleSize = m.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "M";
				columnNames[1] = "H";
				columnNames[2] = "F";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = m[i] + "";
					example[i][1] = h[i] + "";
					example[i][2] = f[i] + "";
				}
				dataTable = new JTable(example, columnNames);
				break;
			}

			case ExampleData.SIMPLE_LINEAR_REGRESSION_4 : {

				double[] dist = {2.5,6.0,6.0,7.5,8.0,8.0,16.0,6.0,5.0,6.0,28.0,5.0,9.5,6.0,4.5,10.0,14.0,3.0,4.5,5.5,3.0,3.5,6.0,2.0,3.0,4.0,6.0,5.0,6.5,5.0,10.0,6.0,18.0,4.5,20.0};

 				double[] climb = {650,2500,900,800,3070,2866,7500,800,800,650,2100,2000,2200,500,1500,3000,2200,350,1000,600,300,1500,2200,900,600,2000,800,950,1750,500,4400,600,5200,850,5000};

 				double[] time = {16.083,48.350,33.650,45.600,62.267,73.217,204.617,36.367,29.750,39.750,192.667,43.050,65.000,44.133,26.933,72.250,98.417,78.650,17.417,32.567,15.950,27.900,47.633,17.933,18.683,26.217,34.433,28.567,50.500,20.950,85.583,32.383,170.250,28.100,159.833};

				int sampleSize = dist.length;
				int varSize = 3;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "dist";
				columnNames[1] = "climb";
				columnNames[2] = "time";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = dist[i] + "";
					example[i][1] = climb[i] + "";
					example[i][2] = time[i] + "";
				}



				dataTable = new JTable(example, columnNames);
				break;
			}

			case ExampleData.SIMPLE_LINEAR_REGRESSION_5: {
				// rice page 517
				double[] d = {.34, .29, .28, .42, .29, .41, .76, .73, .46, .4};
				double[] r = {.636, .319, .734, 1.327, .487, .924, 7.35, 5.89, 1.979, 1.124};

				int sampleSize = d.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];

				columnNames[0]= "depth";
				columnNames[1] = "rate";

				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = d[i] + "";
					example[i][1] = r[i] + "";
				}

				dataTable = new JTable(example,  columnNames);
				break;
			}
	}
}
	static void simpleLinearRegressionTest1() {
		Data data = new Data();
		// this example is from Computational Statistics by Jennrich.
		double[] m = {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] h = {60,94,91,81,80,92,74,89,96,87,86,94,94,94,79,30,92,82,94,78};
		double[] f = {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};

		//String[] a = new String[]{"68", "49","60","68","97"};
		//String[] b = new String[]{"75", "63","57","88","88"};

		data.appendX("midterm", h, DataType.QUANTITATIVE);
		data.appendY("final", f, DataType.QUANTITATIVE);
		//data.appendY("homework", h, DataType.QUANTITATIVE);

		//data.appendX("midterm", a, DataType.QUANTITATIVE);
		//data.appendY("final", b, DataType.QUANTITATIVE);

		try {
			Result result = data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);
			// Result.getTexture() returns a HashMap that holds some result data.
			HashMap texture = result.getTexture();

		} catch (Exception e) {
			////////////////////System.out.println("Data " + e.toString());
		}
	}


	static void simpleLinearRegressionTest2() {
		Data data = new Data();
		// this example is from Computational Statistics by Jennrich.
		double[] p = {25366,25356,25336,25256,25267,25306,25237,25267,25138,25148,15243,14731,24751,24771,24424,24444,24419,24117,24102,24092,25202,25157,25157};
		double[] t = {20.8,20.9,21,21.9,22.1,22.1,22.4,22.5,24.8,24.8,25,34,34.1,42.7,42.7,49.9,50.1,50.1,22.5,23.1,23};

		//String[] a = new String[]{"68", "49","60","68","97"};
		//String[] b = new String[]{"75", "63","57","88","88"};

		data.appendX("pressure", p, DataType.QUANTITATIVE);
		data.appendY("temperature", t, DataType.QUANTITATIVE);
		//data.appendY("homework", h, DataType.QUANTITATIVE);

		//data.appendX("midterm", a, DataType.QUANTITATIVE);
		//data.appendY("final", b, DataType.QUANTITATIVE);

		try {
			Result result = data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);
			// Result.getTexture() returns a HashMap that holds some result data.
			HashMap texture = result.getTexture();

		} catch (Exception e) {
			////////////////////System.out.println("Data " + e.toString());
		}
	}
	static void simpleLinearRegressionTest3() {
		Data data = new Data();
		// this example is from Computational Statistics by Jennrich.
		double[] e1966 = {82, 97, 93, 75, 87, 130, 125, 126, 104};
		double[] e1976 = {85, 98, 102, 80, 90, 135, 135, 131, 110};


		data.appendX("e1966", e1966, DataType.QUANTITATIVE);
		data.appendY("e1976", e1976, DataType.QUANTITATIVE);

		try {
			Result result = data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);
			// Result.getTexture() returns a HashMap that holds some result data.
			HashMap texture = result.getTexture();

		} catch (Exception e) {
			////////////////////System.out.println("Data " + e.toString());
		}
	}

	static void simpleLinearRegressionTest4() {
		Data data = new Data();
		// this example is from Computational Statistics by Jennrich.
		double[] d = {.34, .29, .28, .42, .29, .41, .76, .73, .46, .4};
		double[] r = {.636, .319, .734, 1.327, .487, .924, 7.35, 5.89, 1.979, 1.124};


		data.appendX("DEPTH", d, DataType.QUANTITATIVE);
		data.appendY("FLOW RATE", r, DataType.QUANTITATIVE);

		try {
			Result result = data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);
			// Result.getTexture() returns a HashMap that holds some result data.
			HashMap texture = result.getTexture();

		} catch (Exception e) {
			////////////////////System.out.println("Data " + e.toString());
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
		simpleLinearRegressionTest4();
	}

}
