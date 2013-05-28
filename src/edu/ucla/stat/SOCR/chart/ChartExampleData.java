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
 It's Online, Therefore, It Exists!
 ***************************************************/

package edu.ucla.stat.SOCR.chart;

import javax.swing.JTable;

import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

/** Example data generator for ANOVA & other statistical applets. */
public class ChartExampleData {
	/**
	 * @uml.property  name="example" multiplicity="(0 -1)" dimension="2"
	 */
	public String[][] example = new String[1][1];	// the example data
	/**
	 * @uml.property  name="columnNames" multiplicity="(0 -1)" dimension="1"
	 */
	public String[] columnNames = new String[1];
	/**
	 * @uml.property  name="newln"
	 */
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	/**
	 * @uml.property  name="dataTable"
	 * @uml.associationEnd  readOnly="true" multiplicity="(1 1)"
	 */
	public JTable dataTable;
	/**
	 * @uml.property  name="dOT"
	 */
	private final String DOT = ".";
	
	/**
	 * @uml.property  name="nULL_EXAMPLE"
	 */
	public final int NULL_EXAMPLE = 0;
	
	/**
	 * @uml.property  name="bOOK_EXAMPLE_SLR"
	 */
	public final int BOOK_EXAMPLE_SLR = 101;
	/**
	 * @uml.property  name="bOOK_EXAMPLE_MLR"
	 */
	public final int BOOK_EXAMPLE_MLR = 102;
	/**
	 * @uml.property  name="bOOK_EXAMPLE_ANOVA"
	 */
	public final int BOOK_EXAMPLE_ANOVA = 103;
	/**
	 * @uml.property  name="bOOK_EXAMPLE_ANCOVA"
	 */
	public final int BOOK_EXAMPLE_ANCOVA = 104;
	

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  ChartExampleData() {
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
		Double dummy = new Double(0);

		for (i = 0; i < size*coeff; i=i+(int)coeff) {
			 x = Math.abs(Math.random()*10-5 + Math.pow(c*i,2) + (b+Math.random()*0.5-0.25)*i);
			 y = Math.abs(i + Math.random()*4-2);
			 yy = y + e*Math.pow(x,2) + d*Math.pow(x,3);
			 example[0][(int)(i/coeff)] = String.valueOf(x);
			 dummy = new Double(center2 + Math.random() * spread2);
			 example[1][(int)(i/coeff)] = String.valueOf(yy);
		}
		dataTable = new JTable(example, columnNames);
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

/*
	0 ~ 9 -- not used.
	SIMPLE_LINEAR_REGRESSION = 11;
	MULTI_LINEAR_REGRESSION = 12;

	ANOVA_ONE_WAY = 21;
	ANOVA_TWO_WAY = 22;
	ANOVA_TWO_WAY_INTERACTION = 23;
	ANOVA_THREE_WAY = 24;
	ANCOVA = 29;

	ONE_T = 51;
	TWO_INDEPENDENT_T = 52;
	TWO_PAIRED_T = 53;
	TWO_INDEPENDENT_WILCOXON = 52;
	TWO_PAIRED_SIGN_TEST = 55;
	

	PEARSON_CHI_SQUARED = 61;
*/
	public  ChartExampleData(int index, int dummy) {
		//		System.out.println("ExampleData load for " + index);

		switch (index) {
			case NULL_EXAMPLE : { // and for TWO_PAIRED_T.
				example = new String[20][2];
				columnNames = new String[2];

				columnNames[0] = "C1";
				columnNames[1] = "C2";

				example[0][0] = ""; example[0][1] = "";
				example[1][0] = ""; example[1][1] = "";
				example[2][0] = ""; example[2][1] = "";
				example[3][0] = ""; example[3][1] = "";
				example[4][0] = ""; example[4][1] = "";
				example[5][0] = ""; example[5][1] = "";
				example[6][0] = ""; example[6][1] = "";
				example[7][0] = ""; example[7][1] = "";
				example[8][0] = ""; example[8][1] = "";
				example[9][0] = ""; example[9][1] = "";

				example[10][0] = ""; example[10][1] = "";
				example[11][0] = ""; example[11][1] = "";
				example[12][0] = ""; example[12][1] = "";
				example[13][0] = ""; example[13][1] = "";
				example[14][0] = ""; example[14][1] = "";

				example[15][0] = ""; example[15][1] = "";
				example[16][0] = ""; example[16][1] = "";
				example[17][0] = ""; example[17][1] = "";
				example[18][0] = ""; example[18][1] = "";
				example[19][0] = ""; example[19][1] = "";
				dataTable = new JTable(example, columnNames);
				break;
			}
		    case    ChartType.PIE_CHART:{
				example = new String[6][2];
				columnNames = new String[2];

				columnNames[0]="Name";
				columnNames[1]="Value";

				example[0][0] = "One";    example[0][1] = "43.2";
				example[1][0] = "Two";    example[1][1] = "10.0";
				example[2][0] = "Three";  example[2][1] = "27.5";
				example[3][0] = "Four";   example[3][1] = "17.5";
				example[4][0] = "Five";   example[4][1] = "11.0";
				example[5][0] = "Six";    example[5][1] = "19.4";
				
				dataTable = new JTable(example, columnNames);
				break;	
			}
		   case    ChartType.BAR_CAT_CHART:{
				example = new String[5][2];
				columnNames = new String[2];

				columnNames[0]="Category";
				columnNames[1]="Value";

				example[0][0] = "Category1";    example[0][1] = "4.0";
				example[1][0] = "Category2";    example[1][1] = "3.0";
				example[2][0] = "Category3";  example[2][1] = "-2.0";
				example[3][0] = "Category4";   example[3][1] = "3.0";
				example[4][0] = "Category5";   example[4][1] = "6.0";
				
				dataTable = new JTable(example, columnNames);
				break;	
			}
			case AnalysisType.SIMPLE_LINEAR_REGRESSION : { // and for TWO_PAIRED_T.
				example = new String[20][2];
				columnNames = new String[2];

				columnNames[0]= "VarA";
				columnNames[1] = "VarB";

				example[0][0] = "95"; example[0][1] = "97";
				example[1][0] = "104"; example[1][1] = "98";
				example[2][0] = "99"; example[2][1] = "92";
				example[3][0] = "102"; example[3][1] = "94";
				example[4][0] = "95"; example[4][1] = "93";
				example[5][0] = "106"; example[5][1] = "106";
				example[6][0] = "100"; example[6][1] = "94";
				example[7][0] = "108"; example[7][1] = "109";
				example[8][0] = "104"; example[8][1] = "102";
				example[9][0] = "97"; example[9][1] = "96";

				example[10][0] = "105"; example[10][1] = "105";
				example[11][0] = "103"; example[11][1] = "97";
				example[12][0] = "97"; example[12][1] = "100";
				example[13][0] = "101"; example[13][1] = "103";
				example[14][0] = "96"; example[14][1] = "97";

				example[15][0] = "98"; example[15][1] = "95";
				example[16][0] = "105"; example[16][1] = "105";
				example[17][0] = "106"; example[17][1] = "104";
				example[18][0] = "95"; example[18][1] = "95";
				example[19][0] = "97"; example[19][1] = "95";
				dataTable = new JTable(example, columnNames);
				break;
			}

			case BOOK_EXAMPLE_SLR: { // and for TWO_PAIRED_T.
				example = new String[20][2];
				columnNames = new String[2];

				columnNames[0]= "VarA";
				columnNames[1] = "VarB";
				example[0][0] = "68"; example[0][1] = "75";
				example[1][0] = "49"; example[1][1] = "63";
				example[2][0] = "60"; example[2][1] = "57";
				example[3][0] = "68"; example[3][1] = "88";
				example[4][0] = "97"; example[4][1] = "88";
				
				example[5][0] = "82"; example[5][1] = "79";
				example[6][0] = "59"; example[6][1] = "82";
				example[7][0] = "50"; example[7][1] = "73";
				example[8][0] = "73"; example[8][1] = "90";
				example[9][0] = "39"; example[9][1] = "62";

				example[10][0] = "71"; example[10][1] = "70";
				example[11][0] = "95"; example[11][1] = "96";
				example[12][0] = "61"; example[12][1] = "76";
				example[13][0] = "72"; example[13][1] = "75";
				example[14][0] = "87"; example[14][1] = "85";

				example[15][0] = "40"; example[15][1] = "40";
				example[16][0] = "66"; example[16][1] = "74";
				example[17][0] = "58"; example[17][1] = "70";
				example[18][0] = "58"; example[18][1] = "75";
				example[19][0] = "77"; example[19][1] = "72";
				dataTable = new JTable(example, columnNames);
				break;
			}

			case AnalysisType.TWO_INDEPENDENT_T : {
				example = new String[10][2];
				columnNames = new String[2];

				columnNames[0]= "VarA";
				columnNames[1] = "VarB";

				example[0][0]  = "95"; example[0][1] = "97";
				example[1][0]  = "104"; example[1][1] = "98";
				example[2][0]  = "99"; example[2][1] = "92";
				example[3][0]  = "102"; example[3][1] = "94";
				example[4][0]  = "95"; example[4][1] = "93";

				example[5][0]  = "106"; example[5][1] = "106";
				example[6][0]  = "100"; example[6][1] = "94";
				example[7][0]  = "108"; example[7][1] = "109";
				example[8][0]  = "104"; example[8][1] = "";
				example[9][0]  = "97"; example[9][1] = "";
				dataTable = new JTable(example, columnNames);
				break;
			}

			case AnalysisType.TWO_INDEPENDENT_WILCOXON : {
				example = new String[13][2];
				columnNames = new String[2];

				columnNames[0]= "A";
				columnNames[1] = "B";
				/* From Rice Book */
				example[0][0]  = "79.98"; example[0][1] = "80.02";
				example[1][0]  = "80.04"; example[1][1] = "79.94";
				example[2][0]  = "80.02"; example[2][1] = "79.98";
				example[3][0]  = "80.04"; example[3][1] = "79.97";
				example[4][0]  = "80.03"; example[4][1] = "79.97";

				example[5][0]  = "80.03"; example[5][1] = "80.03";
				example[6][0]  = "80.04"; example[6][1] = "79.95";
				example[7][0]  = "79.97"; example[7][1] = "79.97";
				example[8][0]  = "80.05"; example[8][1] = "";
				example[9][0]  = "80.03"; example[9][1] = "";

				example[10][0]  = "80.02"; example[10][1] = "";
				example[11][0]  = "80.00"; example[11][1] = "";
				example[12][0]  = "80.02"; example[12][1] = "";
				
				/*
				example[0][0]  = "95"; example[0][1] = "97";
				example[1][0]  = "104"; example[1][1] = "98";
				example[2][0]  = "99"; example[2][1] = "92";
				example[3][0]  = "102"; example[3][1] = "94";
				example[4][0]  = "95"; example[4][1] = "93";

				example[5][0]  = "106"; example[5][1] = "106";
				example[6][0]  = "100"; example[6][1] = "94";
				example[7][0]  = "108"; example[7][1] = "109";
				example[8][0]  = "104"; example[8][1] = "";
				example[9][0]  = "97"; example[9][1] = "";
			
				*/
				dataTable = new JTable(example, columnNames);
				break;
			}
		  case AnalysisType.ANOVA_ONE_WAY : {
			  	example = new String[19][2];
				columnNames = new String[2];


				columnNames[0]="Dependent";
				columnNames[1] = "Group";

				example[0][0]  = "98"; example[0][1] = "1";
				example[1][0]  = "104"; example[1][1] = "1";
				example[2][0]  = "99"; example[2][1] = "1";
				example[3][0]  = "102"; example[3][1] = "1";
				example[4][0]  = "97"; example[4][1] = "1";

				example[5][0]  = "106"; example[5][1] = "1";
				example[6][0]  = "100"; example[6][1] = "1";
				example[7][0]  = "108"; example[7][1] = "1";
				
				example[8][0]  = "104"; example[8][1] = "2";
				example[9][0]  = "97"; example[9][1] = "2";

				example[10][0]  = "105"; example[10][1] = "2";
				example[11][0]  = "111"; example[11][1] = "2";
				example[12][0]  = "120"; example[12][1] = "2";
				example[13][0]  = "130"; example[13][1] = "3";
				example[14][0]  = "133"; example[14][1] = "3";

				example[15][0]  = "200"; example[15][1] = "3";
				example[16][0]  = "211"; example[16][1] = "3";
				example[17][0]  = "209"; example[17][1] = "3";
				example[18][0]  = "190"; example[18][1] = "3";
			
		    		//"An example two-way ANOVA with replication" + newln +
				//"Dependent  Factor A  Factor B " + newln +
				// Dr Jennrich's book page 199.
			/* 	
			  	example = new String[19][2];
				columnNames = new String[2];


				columnNames[0]="Dependent";
				columnNames[1] = "Group";
				
				example[0][0]  = "93"; example[0][1] = "1";
				example[1][0]  = "67"; example[1][1] = "1";
				example[2][0]  = "77"; example[2][1] = "1";
				example[3][0]  = "92"; example[3][1] = "1";
				example[4][0]  = "97"; example[4][1] = "1";

				example[5][0]  = "62"; example[5][1] = "1";
				example[6][0]  = "136"; example[6][1] = "2";
				example[7][0]  = "120"; example[7][1] = "2";
				example[8][0]  = "115"; example[8][1] = "2";
				example[9][0]  = "104"; example[9][1] = "2";

				example[10][0]  = "115"; example[10][1] = "2";
				example[11][0]  = "121"; example[11][1] = "2";
				example[12][0]  = "102"; example[12][1] = "2";
				example[13][0]  = "130"; example[13][1] = "2";
				example[14][0]  = "198"; example[14][1] = "3";

				example[15][0]  = "217"; example[15][1] = "3";
				example[16][0]  = "209"; example[16][1] = "3";
				example[17][0]  = "221"; example[17][1] = "3";
				example[18][0]  = "190"; example[18][1] = "3";
				*/			
				dataTable = new JTable(example, columnNames);
				
		    break;
  			}
		  case AnalysisType.ANOVA_TWO_WAY : {
			  	//example = new String[19][2];
				//columnNames = new String[2];

			  	example = new String[10][3];
				columnNames = new String[3];

				columnNames[0]="Y";
				columnNames[1] = "I";
				columnNames[2] = "J";
/*				
				example[0][0]  = "93"; example[0][1] = "1"; example[0][2] = "1";
				example[1][0]  = "136"; example[1][1] = "1"; example[1][2] = "2";
				example[2][0]  = "198"; example[2][1] = "1"; example[2][2] = "3";
				example[3][0]  = "88"; example[3][1] = "2"; example[3][2] = "1";
				example[4][0]  = "148"; example[4][1] = "2"; example[4][2] = "2";

				example[5][0]  = "279"; example[5][1] = "2"; example[5][2] = "3";
*/			
			
				example[0][0]  = "89.01"; example[0][1] = "1"; example[0][2] = "1";
				example[1][0]  = "86.22"; example[1][1] = "1"; example[1][2] = "2";
				example[2][0]  = "98.43"; example[2][1] = "1"; example[2][2] = "3";
				example[3][0]  = "88.45"; example[3][1] = "2"; example[3][2] = "1";
				example[4][0]  = "58.66"; example[4][1] = "2"; example[4][2] = "2";

				example[5][0]  = "86.30"; example[5][1] = "2"; example[5][2] = "3";
				example[6][0]  = "86.67"; example[6][1] = "1"; example[6][2] = "3";
				example[7][0]  = "58.83"; example[7][1] = "2"; example[7][2] = "1";
				example[8][0]  = "77.32"; example[8][1] = "1"; example[8][2] = "2";
				example[9][0]  = "58.61"; example[9][1] = "2"; example[9][2] = "3";
			
/*
				example[0][0]  = "98"; example[0][1] = "1"; example[0][1] = "1";
				example[1][0]  = "104"; example[1][1] = "1"; example[0][1] = "1";
				example[2][0]  = "99"; example[2][1] = "1"; example[0][1] = "1";
				example[3][0]  = "102"; example[3][1] = "1"; example[0][1] = "1";
				example[4][0]  = "97"; example[4][1] = "1"; example[0][1] = "1";

				example[5][0]  = "106"; example[5][1] = "1"; example[0][1] = "1";
				example[6][0]  = "100"; example[6][1] = "1"; example[0][1] = "1";
				example[7][0]  = "108"; example[7][1] = "1"; example[0][1] = "1";
				
				example[8][0]  = "104"; example[8][1] = "2"; example[0][1] = "1";
				example[9][0]  = "97"; example[9][1] = "2"; example[0][1] = "1";

				example[10][0]  = "105"; example[10][1] = "2"; example[0][1] = "1";
				example[11][0]  = "111"; example[11][1] = "2"; example[0][1] = "1";
				example[12][0]  = "120"; example[12][1] = "2"; example[0][1] = "1";
				example[13][0]  = "130"; example[13][1] = "3"; example[0][1] = "1";
				example[14][0]  = "133"; example[14][1] = "3"; example[0][1] = "1";

				example[15][0]  = "200"; example[15][1] = "3"; example[0][1] = "1";
				example[16][0]  = "211"; example[16][1] = "3"; example[0][1] = "1";
				example[17][0]  = "209"; example[17][1] = "3"; example[0][1] = "1";
				example[18][0]  = "190"; example[18][1] = "3"; example[0][1] = "1";
*/			
				
				dataTable = new JTable(example, columnNames);
							
				break;
  			}

  			case AnalysisType.MULTI_LINEAR_REGRESSION: {
				//  				System.out.println("ExampleData in the case " + AnalysisType.MULTI_LINEAR_REGRESSION);

  				example = new String[20][5];
				columnNames = new String[5];
				//				System.out.println("ExampleData example " + example);
				//System.out.println("ExampleData columnNames " + columnNames);
				columnNames[0] = "X1";
				columnNames[1] = "X2";
				columnNames[2] = "X3";
				columnNames[3] = "X4";
				columnNames[4] = "X5";
				
			
				example[0][0]  = "13.8"; example[0][1] = "95"; example[0][4] = "97";
				example[1][0]  = "12.6"; example[1][1] = "104"; example[1][4] = "98";
				example[2][0]  = "11.1"; example[2][1] = "99"; example[2][4] = "92";
				example[3][0]  = "11.3"; example[3][1] = "102"; example[3][4] = "94";
				example[4][0]  = "12.2"; example[4][1] = "95"; example[4][4] = "93";

				example[5][0]  = "14.7"; example[5][1] = "106"; example[5][4] = "106";
				example[6][0]  = "11.6"; example[6][1] = "100"; example[6][4] = "94";
				example[7][0]  = "14.9"; example[7][1] = "108"; example[7][4] = "109";
				example[8][0]  = "13.6"; example[8][1] = "104"; example[8][4] = "102";
				example[9][0]  = "13.1"; example[9][1] = "97"; example[9][4] = "96";

				example[10][0]  = "11.5"; example[10][1] = "105"; example[10][4] = "105";
				example[11][0]  = "11.2"; example[11][1] = "103"; example[11][4] = "97";
				example[12][0]  = "14.2"; example[12][1] = "97"; example[12][4] = "100";
				example[13][0]  = "14.8"; example[13][1] = "101"; example[13][4] = "103";
				example[14][0]  = "13.2"; example[14][1] = "96"; example[14][4] = "97";

				example[15][0]  = "12.0"; example[15][1] = "98"; example[15][4] = "95";
				example[16][0]  = "14.8"; example[16][1] = "105"; example[16][4] = "105";
				example[17][0]  = "14.6"; example[17][1] = "106"; example[17][4] = "104";
				example[18][0]  = "12.7"; example[18][1] = "95"; example[18][4] = "95";
				example[19][0]  = "13.3"; example[19][1] = "97"; example[19][4] = "98";
				
				example[0][2]  = "6"; example[0][3] = ".66";
				example[1][2]  = "4"; example[1][3] = "4.5";
				example[2][2]  = "1"; example[2][3] = "1.1";
				example[3][2]  = "1"; example[3][3] = "1.3";
				example[4][2]  = "8"; example[4][3] = ".88";

				example[5][2]  = "2"; example[5][3] = ".2";
				example[6][2]  = "4"; example[6][3] = "4";
				example[7][2]  = "9"; example[7][3] = "9";
				example[8][2]  = "6"; example[8][3] = ".64";
				example[9][2]  = "7"; example[9][3] = "7";

				example[10][2]  = "6"; example[10][3] = ".66";
				example[11][2]  = "4"; example[11][3] = ".54";
				example[12][2]  = "4"; example[12][3] = "9.4";
				example[13][2]  = "4"; example[13][3] = "64";
				example[14][2]  = "9"; example[14][3] = "9.1";

				example[15][2]  = "0"; example[15][3] = "0.3";
				example[16][2]  = "2"; example[16][3] = "2.3";
				example[17][2]  = "2"; example[17][3] = "2.8";
				example[18][2]  = "9"; example[18][3] = "9.5";
				example[19][2]  = "7"; example[19][3] = "7.1";

				dataTable = new JTable(example, columnNames);
			break;
 			}
 			
 			case BOOK_EXAMPLE_MLR: {
  				example = new String[20][3];
				columnNames = new String[3];

				columnNames[0]="VarA";
				columnNames[1] = "VarB";
				columnNames[2] = "VarC";

				example[0][0] = "68"; example[0][1] = "60"; example[0][2] = "75";
				example[1][0] = "49"; example[1][1] = "94"; example[1][2] = "63";
				example[2][0] = "60"; example[2][1] = "91"; example[2][2] = "57";
				example[3][0] = "68"; example[3][1] = "81"; example[3][2] = "88";
				example[4][0] = "97"; example[4][1] = "80"; example[4][2] = "88";
				
				example[5][0] = "82"; example[5][1] = "92"; example[5][2] = "79";
				example[6][0] = "59"; example[6][1] = "74"; example[6][2] = "82";
				example[7][0] = "50"; example[7][1] = "89"; example[7][2] = "73";
				example[8][0] = "73"; example[8][1] = "96"; example[8][2] = "90";
				example[9][0] = "39"; example[9][1] = "87"; example[9][2] = "62";

				example[10][0] = "71"; example[10][1] = "86"; example[10][2] = "70";
				example[11][0] = "95"; example[11][1] = "94"; example[11][2] = "96";
				example[12][0] = "61"; example[12][1] = "94"; example[12][2] = "76";
				example[13][0] = "72"; example[13][1] = "94"; example[13][2] = "75";
				example[14][0] = "87"; example[14][1] = "79"; example[14][2] = "85";

				example[15][0] = "40"; example[15][1] = "50"; example[15][2] = "40";
				example[16][0] = "66"; example[16][1] = "92"; example[16][2] = "74";
				example[17][0] = "58"; example[17][1] = "82"; example[17][2] = "70";
				example[18][0] = "58"; example[18][1] = "94"; example[18][2] = "75";
				example[19][0] = "77"; example[19][1] = "78"; example[19][2] = "72";

				dataTable = new JTable(example, columnNames);

			break;
 			}
		  case AnalysisType.ONE_T : {
			  	example = new String[20][1];
				columnNames = new String[1];

				columnNames[0]= "VarA";

				example[0][0] = "22.5";
				example[1][0] = "16.3";
				example[2][0] = "19.7";
				example[3][0] = "21.0";
				example[4][0] = "7.0";
				example[5][0] = "19.0";
				example[6][0] = "21.0";
				example[7][0] = "23.6";
				example[8][0] = "20.7";
				example[9][0] = "13.5";

				example[10][0] = "16.4";
				example[11][0] = "20.1";
				example[12][0] = "21.2";
				example[13][0] = "23.7";
				example[14][0] = "13.7";
				example[15][0] = "13.9";
				example[16][0] = "19.1";
				example[17][0] = "20.4";
				example[18][0] = "23.4";
				example[19][0] = "20.0";

				dataTable = new JTable(example, columnNames);

		    break;
		       }
			case AnalysisType.TWO_PAIRED_T : { 
				example = new String[20][2];
				columnNames = new String[2];

				columnNames[0]= "VarA";
				columnNames[1] = "VarB";


				example[0][0] = "95"; example[0][1] = "97";
				example[1][0] = "104"; example[1][1] = "98";
				example[2][0] = "99"; example[2][1] = "92";
				example[3][0] = "102"; example[3][1] = "94";
				example[4][0] = "95"; example[4][1] = "93";
				example[5][0] = "106"; example[5][1] = "106";
				example[6][0] = "100"; example[6][1] = "94";
				example[7][0] = "108"; example[7][1] = "109";
				example[8][0] = "104"; example[8][1] = "102";
				example[9][0] = "97"; example[9][1] = "96";

				example[10][0] = "105"; example[10][1] = "105";
				example[11][0] = "103"; example[11][1] = "97";
				example[12][0] = "97"; example[12][1] = "100";
				example[13][0] = "101"; example[13][1] = "103";
				example[14][0] = "96"; example[14][1] = "97";

				example[15][0] = "98"; example[15][1] = "95";
				example[16][0] = "105"; example[16][1] = "105";
				example[17][0] = "106"; example[17][1] = "104";
				example[18][0] = "95"; example[18][1] = "95";
				example[19][0] = "97"; example[19][1] = "95";
				dataTable = new JTable(example, columnNames);
				break;
			}
			case AnalysisType.TWO_PAIRED_SIGN_TEST : { 
				example = new String[20][2];
				columnNames = new String[2];

				columnNames[0]= "A";
				columnNames[1] = "B";
				example[0][0] = "62"; example[0][1] = "69";
				example[1][0] = "20"; example[1][1] = "27";
				example[2][0] = "91"; example[2][1] = "91";
				example[3][0] = "27"; example[3][1] = "23";
				example[4][0] = "62"; example[4][1] = "22";

				example[5][0] = "65"; example[5][1] = "60";
				example[6][0] = "62"; example[6][1] = "23";
				example[7][0] = "13"; example[7][1] = ".98";
				example[8][0] = "70"; example[8][1] = ".86";
				example[9][0] = "10"; example[9][1] = "59";

				example[10][0] = "68"; example[10][1] = "63";
				example[11][0] = "26"; example[11][1] = "25";
				example[12][0] = "80"; example[12][1] = "82";
				example[13][0] = "61"; example[13][1] = "60";
				example[14][0] = "12"; example[14][1] = "10";

				example[15][0] = "72"; example[15][1] = "73";
				example[16][0] = "57"; example[16][1] = ".85";
				example[17][0] = "83"; example[17][1] = "81";
				example[18][0] = "60"; example[18][1] = "63";
				example[19][0] = "50"; example[19][1] = "77";
				
				// Example from Mathematical Statistics and Data Analysis, 2nd Edition, John Rice.
/*				example[0][0] = ".32"; example[0][1] = ".39";
				example[1][0] = ".40"; example[1][1] = ".47";
				example[2][0] = ".11"; example[2][1] = ".11";
				example[3][0] = ".47"; example[3][1] = ".43";
				example[4][0] = ".32"; example[4][1] = ".42";

				example[5][0] = ".35"; example[5][1] = ".30";
				example[6][0] = ".32"; example[6][1] = ".43";
				example[7][0] = ".63"; example[7][1] = ".98";
				example[8][0] = ".50"; example[8][1] = ".86";
				example[9][0] = ".60"; example[9][1] = ".79";

				example[10][0] = ".38"; example[10][1] = ".33";
				example[11][0] = ".46"; example[11][1] = ".45";
				example[12][0] = ".20"; example[12][1] = ".22";
				example[13][0] = ".31"; example[13][1] = ".30";
				example[14][0] = ".62"; example[14][1] = ".60";

				example[15][0] = ".52"; example[15][1] = ".53";
				example[16][0] = ".77"; example[16][1] = ".85";
				example[17][0] = ".23"; example[17][1] = ".21";
				example[18][0] = ".30"; example[18][1] = ".33";
				example[19][0] = ".70"; example[19][1] = ".57";

				example[20][0] = ".41"; example[20][1] = ".43";
				example[21][0] = ".53"; example[21][1] = ".49";
				example[22][0] = ".19"; example[22][1] = ".20";
				example[23][0] = ".31"; example[23][1] = ".35";
				example[24][0] = ".48"; example[24][1] = ".40";	
*/				
				dataTable = new JTable(example, columnNames);
				break;
			}
				default: {
				//  			
  				example = new String[20][5];
				columnNames = new String[5];

				columnNames[0] = "X1";
				columnNames[1] = "X2";
				columnNames[2] = "X3";
				columnNames[3] = "X4";
				columnNames[4] = "X5";
				
			
				example[0][0]  = "13.8"; example[0][1] = "95"; example[0][4] = "97";
				example[1][0]  = "12.6"; example[1][1] = "104"; example[1][4] = "98";
				example[2][0]  = "11.1"; example[2][1] = "99"; example[2][4] = "92";
				example[3][0]  = "11.3"; example[3][1] = "102"; example[3][4] = "94";
				example[4][0]  = "12.2"; example[4][1] = "95"; example[4][4] = "93";

				example[5][0]  = "14.7"; example[5][1] = "106"; example[5][4] = "106";
				example[6][0]  = "11.6"; example[6][1] = "100"; example[6][4] = "94";
				example[7][0]  = "14.9"; example[7][1] = "108"; example[7][4] = "109";
				example[8][0]  = "13.6"; example[8][1] = "104"; example[8][4] = "102";
				example[9][0]  = "13.1"; example[9][1] = "97"; example[9][4] = "96";

				example[10][0]  = "11.5"; example[10][1] = "105"; example[10][4] = "105";
				example[11][0]  = "11.2"; example[11][1] = "103"; example[11][4] = "97";
				example[12][0]  = "14.2"; example[12][1] = "97"; example[12][4] = "100";
				example[13][0]  = "14.8"; example[13][1] = "101"; example[13][4] = "103";
				example[14][0]  = "13.2"; example[14][1] = "96"; example[14][4] = "97";

				example[15][0]  = "12.0"; example[15][1] = "98"; example[15][4] = "95";
				example[16][0]  = "14.8"; example[16][1] = "105"; example[16][4] = "105";
				example[17][0]  = "14.6"; example[17][1] = "106"; example[17][4] = "104";
				example[18][0]  = "12.7"; example[18][1] = "95"; example[18][4] = "95";
				example[19][0]  = "13.3"; example[19][1] = "97"; example[19][4] = "98";
				
				example[0][2]  = "6"; example[0][3] = ".66";
				example[1][2]  = "4"; example[1][3] = "4.5";
				example[2][2]  = "1"; example[2][3] = "1.1";
				example[3][2]  = "1"; example[3][3] = "1.3";
				example[4][2]  = "8"; example[4][3] = ".88";

				example[5][2]  = "2"; example[5][3] = ".2";
				example[6][2]  = "4"; example[6][3] = "4";
				example[7][2]  = "9"; example[7][3] = "9";
				example[8][2]  = "6"; example[8][3] = ".64";
				example[9][2]  = "7"; example[9][3] = "7";

				example[10][2]  = "6"; example[10][3] = ".66";
				example[11][2]  = "4"; example[11][3] = ".54";
				example[12][2]  = "4"; example[12][3] = "9.4";
				example[13][2]  = "4"; example[13][3] = "64";
				example[14][2]  = "9"; example[14][3] = "9.1";

				example[15][2]  = "0"; example[15][3] = "0.3";
				example[16][2]  = "2"; example[16][3] = "2.3";
				example[17][2]  = "2"; example[17][3] = "2.8";
				example[18][2]  = "9"; example[18][3] = "9.5";
				example[19][2]  = "7"; example[19][3] = "7.1";

				dataTable = new JTable(example, columnNames);
			break;
 			}
	}

}

	/** returns a JTable object containing the Example Data */
	public JTable getExample() {
		return dataTable;
	 }

}
