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
public class FlignerKilleenExamples extends ExampleData {
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
	public  FlignerKilleenExamples() {
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  FlignerKilleenExamples(int analysisType, int exampleID) {

		//System.out.println("call FlignerKillenExamples");
		switch (exampleID) {
			case NULL_EXAMPLE : { // and for TWO_INDEPENDENT_KW_T.
			}

			case ExampleData.FK_1:  {
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

			case ExampleData.FK_2:  {
				// example from R MASS package.
				double[] a = new double[]{10,7,20,14,14,12,10,23,17,20,14,13};
				double[] b = new double[]{11,17,21,11,16,14,17,17,19,21,7,13};
				double[] c = new double[]{0,1,7,2,3,1,2,1,3,0,1,4};
				double[] d = new double[]{3,5,12,6,4,3,5,5,5,5,2,4};
				double[] e = new double[]{3,5,3,5,3,6,1,1,3,2,6,4};
				double[] f = new double[]{11,9,15,22,15,16,13,10,26,26,24,13};

				int sampleSize = Math.max(f.length, Math.max(e.length, Math.max(d.length, Math.max(c.length, Math.max(a.length, b.length)))));
				int varSize = 6;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				columnNames[0] = "A";
				columnNames[1] = "B";
				columnNames[2] = "C";
				columnNames[3] = "D";
				columnNames[4] = "E";
				columnNames[5] = "F";


				for (int i = 0; i < sampleSize; i++) {
					try {
						example[i][0] = a[i] + "";
					} catch (Exception ex) {
						example[i][0] = "";
					}
					try {
						example[i][1] = b[i] + "";
					} catch (Exception ex) {
						example[i][1] = "";
					}
					try {
						example[i][2] = c[i] + "";
					} catch (Exception ex) {
						example[i][2] = "";
					}
					try {
						example[i][3] = d[i] + "";
					} catch (Exception ex) {
						example[i][3] = "";
					}
					try {
						example[i][4] = e[i] + "";
					} catch (Exception ex) {
						example[i][4] = "";
					}
					try {
						example[i][5] = f[i] + "";
					} catch (Exception ex) {
						example[i][5] = "";
					}

				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}
			case ExampleData.FK_3:  {
				// example from R MASS package.
				double[] year = new double[]{1981,1982,1983,1984,1985,1986,1987,1988,1989,1990,1991,1992,1993,1994,1995,1996,1997,1998,1999,2000,2001,2002,2003,2004,2005,2006};
				double[] electricity = new double[]{31.552,36.006,37.184,38.600,38.975,40.223,40.022,40.195,40.828,41.663,43.226,44.501,46.959,48.200,48.874,48.538,49.245,46.401,45.061,45.207,47.472,47.868,47.663,49.159,50.847,57.223};
				double[] fuel = new double[]{1.150,1.254,1.194,1.122,1.078,1.126,0.817,0.890,0.883,1.259,1.235,0.985,0.969,0.919,0.913,1.007,1.136,0.966,0.834,1.189,1.509,1.123,1.396,1.508,1.859,2.418};
				double[] bananas = new double[]{0.343,0.364,0.332,0.344,0.350,0.337,0.374,0.394,0.429,0.438,0.428,0.426,0.440,0.503,0.463,0.497,0.473,0.489,0.490,0.500,0.509,0.526,0.512,0.485,0.490,0.505};
				double[] tomatoes = new double[]{0.792,0.763,0.726,0.854,0.697,1.104,0.871,0.797,1.735,0.912,0.936,1.141,1.604,1.323,1.103,1.213,1.452,1.904,1.443,1.414,1.451,1.711,1.472,1.660,2.162,1.621};
				double[] oj = new double[]{1.141,1.465,1.418,1.408,1.685,1.756,1.512,1.638,1.868,1.817,2.005,1.879,1.677,1.674,1.583,1.577,1.737,1.601,1.753,1.823,1.863,1.876,1.848,1.957,1.872,1.853};
				double[] beef = new double[]{1.856,1.794,1.756,1.721,1.711,1.662,1.694,1.736,1.806,1.907,1.996,1.926,1.970,1.892,1.847,1.799,1.850,1.818,1.834,1.903,2.037,2.152,2.131,2.585,2.478,2.607};
				double[] gas = new double[]{1.269,1.341,1.214,1.200,1.145,1.190,0.868,0.947,0.944,1.090,1.304,1.135,1.182,1.109,1.190,1.186,1.318,1.186,1.031,1.356,1.525,1.209,1.557,1.635,1.866,2.359};

				int sampleSize = gas.length;
				int varSize = 8;
				example = new String[sampleSize][varSize];
				columnNames = new String[varSize];
				// all are of the same length
				columnNames[0] = "Year";
				columnNames[1] = "Electricity";
				columnNames[2] = "Fuel Oil";
				columnNames[3] = "Bananas";
				columnNames[4] = "Tomatoes";
				columnNames[5] = "Orange Juice";
				columnNames[6] = "Beef";
				columnNames[7] = "Gasoline";


				for (int i = 0; i < sampleSize; i++) {
					try {
						example[i][0] = year[i] + "";
					} catch (Exception ex) {
						example[i][0] = "";
					}
					try {
						example[i][1] = electricity[i] + "";
					} catch (Exception ex) {
						example[i][1] = "";
					}
					try {
						example[i][2] = fuel[i] + "";
					} catch (Exception ex) {
						example[i][2] = "";
					}
					try {
						example[i][3] = bananas[i] + "";
					} catch (Exception ex) {
						example[i][3] = "";
					}
					try {
						example[i][4] = tomatoes[i] + "";
					} catch (Exception ex) {
						example[i][4] = "";
					}
					try {
						example[i][5] = oj[i] + "";
					} catch (Exception ex) {
						example[i][5] = "";
					}

					try {
						example[i][6] = beef[i] + "";
					} catch (Exception ex) {
						example[i][6] = "";
					}
					try {
						example[i][7] = gas[i] + "";
					} catch (Exception ex) {
						example[i][7] = "";
					}
				}
				dataTable = new JTable(example,  columnNames);

		    		break;
			}

			case ExampleData.FK_4:  {
				example = new String[30][6];
				columnNames = new String[6];
				int sampleSize = 30;

				example = new String[sampleSize][6];

				/*
				columnNames[0]= "Date(YYYY/MM/DD)";
				columnNames[1] = "Time";
				columnNames[2]= "Latitude";
				columnNames[3] = "Longitude";
				columnNames[4]= "Depth";
				columnNames[5] = "Mag";
				columnNames[6]= "Magt";
				columnNames[7] = "Nst";
				columnNames[8]= "Gap";
				columnNames[9] = "Clo";
				columnNames[10]= "RMS";
				columnNames[11] = "SRC";
				columnNames[12] = "EventID";
				*/
				columnNames[0]= "Depth";
				columnNames[1] = "Mag";
				columnNames[2]= "Nst";
				columnNames[3] = "Gap";
				columnNames[4]= "Clo";
				columnNames[5] = "RMS";
				// first 30 events of the earthquake data

				example[0][0]="0.22"; example[0][1]="5.60"; example[0][2]="38"; example[0][3]="104"; example[0][4]="52"; example[0][5]="0.22";
				example[1][0]="5.14"; example[1][1]="5.70"; example[1][2]="53"; example[1][3]="139"; example[1][4]="58"; example[1][5]="0.22";
				example[2][0]="4.18"; example[2][1]="5.10"; example[2][2]="10"; example[2][3]="128"; example[2][4]="6"; example[2][5]="0.06";
				example[3][0]="5.48"; example[3][1]="5.20"; example[3][2]="51"; example[3][3]="61"; example[3][4]="4"; example[3][5]="0.13";
				example[4][0]="23.48";example[4][1]="5.30"; example[4][2]="15"; example[4][3]="176"; example[4][4]="5"; example[4][5]="0.04";
				example[5][0]="4.97"; example[5][1]="5.70"; example[5][2]="24"; example[5][3]="116"; example[5][4]="14"; example[5][5]="0.07";
				example[6][0]="2.03"; example[6][1]="5.10"; example[6][2]="7"; example[6][3]="102"; example[6][4]="2"; example[6][5]="0.06";
				example[7][0]="1.82"; example[7][1]="5.20"; example[7][2]="7"; example[7][3]="146"; example[7][4]="2"; example[7][5]="0.03";
				example[8][0]="5.00"; example[8][1]="5.00"; example[8][2]="13"; example[8][3]="319"; example[8][4]="91"; example[8][5]="0.15";
				example[9][0]="41.86";example[9][1]="6.30"; example[9][2]="13"; example[9][3]="318"; example[9][4]="73"; example[9][5]="0.09";
				example[10][0]="5.00";  example[10][1]="5.18"; example[10][2]="29"; example[10][3]="174"; example[10][4]="60"; example[10][5]="0.20";
				example[11][0]="18.01"; example[11][1]="5.10"; example[11][2]="142"; example[11][3]="89"; example[11][4]="54"; example[11][5]="0.40";
				example[12][0]="102.97"; example[12][1]="5.07"; example[12][2]="13"; example[12][3]="317"; example[12][4]="123"; example[12][5]="0.17";
				example[13][0]="23.75"; example[13][1]="5.20"; example[13][2]="15"; example[13][3]="236"; example[13][4]="20"; example[13][5]="0.08";
				example[14][0]="8.82"; example[14][1]="5.30"; example[14][2]="44"; example[14][3]="77"; example[14][4]="20"; example[14][5]="0.13";
				example[15][0]="5.00"; example[15][1]="5.34"; example[15][2]="10"; example[15][3]="318"; example[15][4]="85"; example[15][5]="0.13";
				example[16][0]="8.62"; example[16][1]="5.80"; example[16][2]="90"; example[16][3]="97"; example[16][4]="6"; example[16][5]="0.06";
				example[17][0]="5.00"; example[17][1]="5.00"; example[17][2]="12"; example[17][3]="139"; example[17][4]="52"; example[17][5]="0.06";
				example[18][0]="14.79"; example[18][1]="5.80"; example[18][2]="76"; example[18][3]="69"; example[18][4]="5"; example[18][5]="0.13";
				example[19][0]="6.90"; example[19][1]="5.10"; example[19][2]="10"; example[19][3]="149"; example[19][4]="3"; example[19][5]="0.21";
				example[20][0]="14.43"; example[20][1]="5.40"; example[20][2]="73"; example[20][3]="102"; example[20][4]="9"; example[20][5]="0.10";
				example[21][0]="9.27"; example[21][1]="5.10"; example[21][2]="30"; example[21][3]="288"; example[21][4]="69"; example[21][5]="0.10";
				example[22][0]="9.01"; example[22][1]="6.10"; example[22][2]="12"; example[22][3]="159"; example[22][4]="1"; example[22][5]="0.15";
				example[23][0]="8.67"; example[23][1]="6.00"; example[23][2]="44"; example[23][3]="55"; example[23][4]="7"; example[23][5]="1.35";
				example[24][0]="14.20"; example[24][1]="6.10"; example[24][2]="7"; example[24][3]="191"; example[24][4]="4"; example[24][5]="0.10";
				example[25][0]="6.95"; example[25][1]="5.70"; example[25][2]="5"; example[25][3]="182"; example[25][4]="12"; example[25][5]="0.04";
				example[26][0]="16.56"; example[26][1]="5.00"; example[26][2]="6"; example[26][3]="92"; example[26][4]="1"; example[26][5]="0.02";
				example[27][0]="9.33"; example[27][1]="5.10"; example[27][2]="11"; example[27][3]="204"; example[27][4]="6"; example[27][5]="0.07";
				example[28][0]="4.40"; example[28][1]="5.70"; example[28][2]="13"; example[28][3]="211"; example[28][4]="9"; example[28][5]="0.03";
				example[29][0]="16.09"; example[29][1]="6.20";example[29][2]="20";example[29][3]="183";example[29][4]="5";example[29][5]="0.12";

				dataTable = new JTable(example,  columnNames);
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

}
