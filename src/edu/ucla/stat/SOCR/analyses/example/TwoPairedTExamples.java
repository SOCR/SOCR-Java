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
public class TwoPairedTExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	public final int NULL_EXAMPLE = 0;
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, true, true, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/

	public  TwoPairedTExamples() {
		dataTable = this.getRandomExample();
	}

	/** Constructor method for sampel data for ANOVA analysis.*/



	public  TwoPairedTExamples(int analysisType, int exampleID) {
		this.dataTable = getExample(exampleID);
	}

	public JTable getExample(int exampleID) {

		switch (exampleID) {
			default: {
				this.getNullExample();
				break;
			}

			case ExampleData.TWO_PAIRED_T_1: { // and for TWO_PAIRED_T.

				// rice page 412
				double[] before = {25,25,27,44,30,67,53,53,52,60,28};
				double[] after = {27,29,37,56,46,82,57,80,61,59,43};

				int sampleSize = before.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "Before";
				columnNames[1] = "After";
				//columnNames[2] = "Difference";



				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = before[i] + "";
					example[i][0] = after[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.TWO_PAIRED_T_2:  { // and for TWO_PAIRED_T.
				// rice, page 433
				double[] test = {676, 206, 230, 256, 280, 433, 337, 466, 497, 512,794, 428, 452, 512};
				double[] control = {88, 570, 605, 617, 653, 2913, 924, 286, 1098, 982, 2346, 321, 615, 519};

				int sampleSize = test.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "Test";
				columnNames[1] = "Control";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = test[i] + "";
					example[i][1] = control[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
		    	}

			case ExampleData.TWO_PAIRED_T_3 : {
				// boys shoes
				double[] a = {13.2,8.2,10.9,14.3,10.7,6.6,9.5,10.8,8.8,13.3};
				double[] b = {14.0,8.8,11.2,14.2,11.8,6.4,9.8,11.3,9.3,13.6};

				int sampleSize = a.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = a[i] + "";
					example[i][0] = b[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.TWO_PAIRED_T_4: {
				// rice page 431
				double[] a = {97.2,105.8,99.5,100,93.8,79.2,72,72,69.5,29.5,95.2,90.8,96.2,96.2,91};
				double[] b = {97.2,97.8,96.2,101.8,88,74,75,67.5,65.8,21.2,94.8,95.8,98,99,100.2};

				int sampleSize = a.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "Micro";
				columnNames[1] = "Hydro";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = a[i] + "";
					example[i][0] = b[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}
			case ExampleData.TWO_PAIRED_T_5: {
				// rice page 415 fish
				double[] a = {.32,.4,.11,.47,.32,.35,.32,.63,.5,.6,.38,.46,  .2,.31,.62,.52,.77,.23,.3,.7,.41,.53,.19,.31,.48};
				double[] b = {.39,.47,.11,.43,.42,.3,.43,.98,.86,.79,.33,.45,  .22,.3,.6,.53,.85,.21,.33,.57,.43,.49,.2,.35,.4};

				int sampleSize = a.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "Select";
				columnNames[1] = "Perman";


				for (int i = 0; i < sampleSize; i++) {
					example[i][1] = a[i] + "";
					example[i][0] = b[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;

			}
			case ExampleData.TWO_PAIRED_T_6: {
				// Wu and Hamad page 49
				double[] a = {.39, .84, 1.76, 3.35, 4.69, 7.7, 10.52, 10.92};
				double[] b = {0.36, 1.35, 2.56, 3.92, 5.35, 8.33, 10.7, 10.91};

				int sampleSize = a.length;
				int varSize = 2;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "MSI";
				columnNames[1] = "SIB";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = a[i] + "";
					example[i][1] = b[i] + "";
				}
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
