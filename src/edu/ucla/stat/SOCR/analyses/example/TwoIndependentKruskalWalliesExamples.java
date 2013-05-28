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
public class TwoIndependentKruskalWalliesExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	public static final short NULL_EXAMPLE = 0;
	private static String dataSource = "";
	public static boolean[] availableExamples = new boolean[]{true, true, true, true, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  TwoIndependentKruskalWalliesExamples() {
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  TwoIndependentKruskalWalliesExamples(int analysisType, int exampleID) {

		//System.out.println("call TwoIndependentKruskalWalliesExamples");
		switch (exampleID) {
			case NULL_EXAMPLE : { // and for TWO_INDEPENDENT_KW_T.
			}

			case ExampleData.TWO_INDEPENDENT_KW_1: { // and for TWO_INDEPENDENT_KW_T.
				/* the following example is from Conover but we don't know if we have the permission to use it. */
				example = new String[10][4];
				columnNames = new String[4];

				columnNames[0]= "A";
				columnNames[1] = "B";
				columnNames[2]= "C";
				columnNames[3] = "D";
				example[0][0]  = "83"; example[0][1] = "91"; example[0][2]  = "101"; example[0][3] = "78";
				example[1][0]  = "91"; example[1][1] = "90"; example[1][2]  = "100"; example[1][3] = "82";
				example[2][0]  = "94"; example[2][1] = "81"; example[2][2]  = "91"; example[2][3] = "81";
				example[3][0]  = "89"; example[3][1] = "83"; example[3][2]  = "93"; example[3][3] = "77";
				example[4][0]  = "89"; example[4][1] = "84"; example[4][2]  = "96"; example[4][3] = "79";

				example[5][0]  = "96"; example[5][1] = "83"; example[5][2]  = "95"; example[5][3] = "81";
				example[6][0]  = "91"; example[6][1] = "88"; example[6][2]  = "94"; example[6][3] = "80";
				example[7][0]  = "92"; example[7][1] = "91"; example[7][2]  = "";   example[7][3] = "81";
				example[8][0]  = "90"; example[8][1] = "89"; example[8][2]  = "";   example[8][3] = "";
				example[9][0]  = "";   example[9][1] = "84"; example[9][2]  = "";   example[9][3] = "";


				dataTable = new JTable(example,  columnNames);
				break;
			}

			case ExampleData.TWO_INDEPENDENT_KW_2:  { // and for TWO_INDEPENDENT_KW_T.
				// Wu and Hamad, page 20
				double[] a = {59.8, 60, 60.8, 60.8, 59.8};
				double[] b = {59.8, 60.2, 60.4, 59.9, 60};
				double[] c = {60.7, 60.7, 60.5, 60.9, 60.3};
				double[] d = {61, 60.8, 60.6, 60.5, 60.5};

				int sampleSize = a.length;
				int varSize = 4;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";
				columnNames[2] = "C";
				columnNames[3] = "D";


				for (int i = 0; i < sampleSize; i++) {
					example[i][0] = a[i] + "";
					example[i][1] = b[i] + "";
					example[i][2] = c[i] + "";
					example[i][3] = d[i] + "";
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.TWO_INDEPENDENT_KW_3 : {
				// R's example: oats, using catagorical variable B
					double[] y1={111,130,157,174,117,114,161,141,105,140,118,156};
					double[] y2={61,91,97,100,70,108,126,149,96,124,121,144};
					double[] y3={68,64,112,86,60,102,89,96,89,129,132,124};
					double[] y4={74,89,81,122,64,103,132,133,70,89,104,117};
					double[] y5={62,90,100,116,80,82,94,126,63,70,109,99};
					double[] y6={53,74,118,113,89,82,86,104,97,99,119,121};

					int sampleSize = y1.length;
					int varSize = 6;
					example = new String[sampleSize][varSize];
					columnNames = new String[varSize];
					columnNames[0] = "I";
					columnNames[1] = "II";
					columnNames[2] = "III";
					columnNames[3] = "IV";
					columnNames[4] = "V";
					columnNames[5] = "VI";


					for (int i = 0; i < sampleSize; i++) {
						example[i][0] = y1[i] + "";
						example[i][1] = y2[i] + "";
						example[i][2] = y3[i] + "";
						example[i][3] = y4[i] + "";
						example[i][4] = y5[i] + "";
						example[i][5] = y6[i] + "";
					}
					dataTable = new JTable(example,  columnNames);

					break;
			}

			case ExampleData.TWO_INDEPENDENT_KW_4: {
				// R's example: oats, using catagorical variable V
					double[] v1={111,130,157,174,61,91,97,100,68,64,112,86,74,89,81,122,62,90,100,116,53,74,118,113};
					double[] v2={117,114,161,141,70,108,126,149,60,102,89,96,64,103,132,133,80,82,94,126,89,82,86,104};
					double[] v3={105,140,118,156,96,124,121,144,89,129,132,124,70,89,104,117,63,70,109,99,97,99,119,121};
					int sampleSize = v1.length;

					int varSize = 3;
					example = new String[sampleSize][varSize];
					columnNames = new String[varSize];
					columnNames[0] = "Vict";
					columnNames[1] = "Gold";
					columnNames[2] = "Marv";


					for (int i = 0; i < sampleSize; i++) {
						example[i][0] = v1[i] + "";
						example[i][1] = v2[i] + "";
						example[i][2] = v3[i] + "";
					}
					dataTable = new JTable(example,  columnNames);

					break;
			}
			case ExampleData.TWO_INDEPENDENT_KW_5: {

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
