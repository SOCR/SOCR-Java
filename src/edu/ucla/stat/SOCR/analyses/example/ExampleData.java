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
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import javax.swing.table.*;
//import edu.ucla.stat.SOCR.analyses.data.DataFrame;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.gui.Analysis;

/** Example data generator for ANOVA & other statistical applets. */
public class ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";

	public final static short NULL_EXAMPLE = 0;
	private static String dataSource = "";

	public final static int DEFAULT_SAMPLE_SIZE = 1000; // default, if there's no example or data.
	public final int DEFAULT_VARIABLE_SIZE = edu.ucla.stat.SOCR.analyses.gui.Analysis.DEFAULT_MAX_COLUMN_NUMBER;

	public static boolean[] availableExamples = new boolean[10];

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  ExampleData() {
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
		Double dummy = new Double(0);

		for (i = 0; i < size*coeff; i=i+(int)coeff) {
			 x = Math.abs(Math.random()*10-5 + Math.pow(c*i, 2) + (b+Math.random()*0.5-0.25)*i);
			 y = Math.abs(i + Math.random()*4-2);
			 yy = y + e*Math.pow(x, 2) + d*Math.pow(x, 3);
			 example[0][(int)(i/coeff)] = String.valueOf(x);
			 dummy = new Double(center2 + Math.random() * spread2);
			 example[1][(int)(i/coeff)] = String.valueOf(yy);
		}
		dataTable = new JTable(example,  columnNames);
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  ExampleData(int analysisType, int dummy) {
		////System.out.println("ExampleData load for " + analysisType);

		switch (analysisType) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}

			case AnalysisType.TWO_PAIRED_SIGN_TEST : {
				example = new String[20][2];
				columnNames = new String[2];
				////System.out.println("Example Data case AnalysisType.TWO_PAIRED_SIGN_TEST ");
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
				////System.out.println("ExampleData before dataTable");
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

	public void getNullExample() {
		this.example = new String[DEFAULT_SAMPLE_SIZE][DEFAULT_VARIABLE_SIZE];
		columnNames = new String[DEFAULT_VARIABLE_SIZE];
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = Analysis.DEFAULT_HEADER_1 + (i + 1);
		}
		for (int i = 0; i < DEFAULT_SAMPLE_SIZE; i++) {
			for (int j = 0; j < DEFAULT_VARIABLE_SIZE; j++) {
				example[i][j] = "";
			}

		}

		this.dataTable = new JTable(example,  columnNames);
		int columnNumber = columnNames.length;
		TableColumnModel columnModel = dataTable.getColumnModel();
		for (int i = 0; i < columnNumber; i++) {
			columnNames[i] = new String(Analysis.DEFAULT_HEADER_1+(i+1));
			////System.out.println("RESET  header columnNames[i] = " + columnNames[i]);
			columnModel.getColumn(i).setHeaderValue(columnNames[i]);
			columnModel.getColumn(i).setResizable(true);
			columnModel.getColumn(i).setPreferredWidth(300);
			columnModel.getColumn(i).setWidth(300);
			////System.out.println("columnModel.getColumn("+i+").getResizable = " + columnModel.getColumn(i).getResizable());


		}
		dataTable.setPreferredScrollableViewportSize(new Dimension(Analysis.DEFAULT_DATA_PANEL_WIDTH, Analysis.DEFAULT_DATA_PANEL_HEIGHT));

		dataTable.setGridColor(Color.LIGHT_GRAY);
		dataTable.setShowGrid(true);
		//dataTable.setTableHeader(new EditableHeader(columnModel));
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		dataTable.setCellSelectionEnabled(true);
		dataTable.setColumnSelectionAllowed(true);
		dataTable.setRowSelectionAllowed(true);

		dataTable.doLayout();


	}
	public static final char SIMPLE_LINEAR_REGRESSION_1 = 1;
	public static final char SIMPLE_LINEAR_REGRESSION_2 = 2;
	public static final char SIMPLE_LINEAR_REGRESSION_3 = 3;
	public static final char SIMPLE_LINEAR_REGRESSION_4 = 4;
	public static final char SIMPLE_LINEAR_REGRESSION_5 = 5;
	public static final char SIMPLE_LINEAR_REGRESSION_6 = 6;
	public static final char SIMPLE_LINEAR_REGRESSION_7 = 7;

	public static final char MULTI_LINEAR_REGRESSION_1 = 1; // use the same one.
	public static final char MULTI_LINEAR_REGRESSION_2 = 2;
	public static final char MULTI_LINEAR_REGRESSION_3 = 3;
	public static final char MULTI_LINEAR_REGRESSION_4 = 4;
	public static final char MULTI_LINEAR_REGRESSION_5 = 5;
	public static final char MULTI_LINEAR_REGRESSION_6 = 6;
	public static final char MULTI_LINEAR_REGRESSION_7 = 7;

	public static final char ANOVA_ONE_WAY_1 = 1;
	public static final char ANOVA_ONE_WAY_2 = 2;
	public static final char ANOVA_ONE_WAY_3 = 3;
	public static final char ANOVA_ONE_WAY_4 = 4;
	public static final char ANOVA_ONE_WAY_5 = 5;
	public static final char ANOVA_ONE_WAY_6 = 6;
	public static final char ANOVA_ONE_WAY_7 = 7;

	public static final char ANOVA_TWO_WAY_1 = 1;
	public static final char ANOVA_TWO_WAY_2 = 2;
	public static final char ANOVA_TWO_WAY_3 = 3;
	public static final char ANOVA_TWO_WAY_4 = 4;
	public static final char ANOVA_TWO_WAY_5 = 5;
	public static final char ANOVA_TWO_WAY_6 = 6;
	public static final char ANOVA_TWO_WAY_7 = 7;


	public static final char ONE_T_1 = 1;
	public static final char ONE_T_2 = 2;
	public static final char ONE_T_3 = 3;
	public static final char ONE_T_4 = 4;
	public static final char ONE_T_5 = 5;
	public static final char ONE_T_6 = 6;
	public static final char ONE_T_7 = 7;

	public static final char TWO_INDEPENDENT_T_1 = 1;
	public static final char TWO_INDEPENDENT_T_2 = 2;
	public static final char TWO_INDEPENDENT_T_3 = 3;
	public static final char TWO_INDEPENDENT_T_4 = 4;
	public static final char TWO_INDEPENDENT_T_5 = 5;
	public static final char TWO_INDEPENDENT_T_6 = 6;
	public static final char TWO_INDEPENDENT_T_7 = 7;

	public static final char TWO_PAIRED_T_1 = 1;
	public static final char TWO_PAIRED_T_2 = 2;
	public static final char TWO_PAIRED_T_3 = 3;
	public static final char TWO_PAIRED_T_4 = 4;
	public static final char TWO_PAIRED_T_5 = 5;
	public static final char TWO_PAIRED_T_6 = 6;
	public static final char TWO_PAIRED_T_7 = 7;

	public static final char TWO_INDEPENDENT_WILCOXON_1 = 1;
	public static final char TWO_INDEPENDENT_WILCOXON_2 = 2;
	public static final char TWO_INDEPENDENT_WILCOXON_3 = 3;
	public static final char TWO_INDEPENDENT_WILCOXON_4 = 4;
	public static final char TWO_INDEPENDENT_WILCOXON_5 = 5;
	public static final char TWO_INDEPENDENT_WILCOXON_6 = 6;
	public static final char TWO_INDEPENDENT_WILCOXON_7 = 7;

	public static final char TWO_INDEPENDENT_KW_1 = 1; // KW: kruskal-wallies.
	public static final char TWO_INDEPENDENT_KW_2 = 2;
	public static final char TWO_INDEPENDENT_KW_3 = 3;
	public static final char TWO_INDEPENDENT_KW_4 = 4;
	public static final char TWO_INDEPENDENT_KW_5 = 5;
	public static final char TWO_INDEPENDENT_KW_6 = 6;
	public static final char TWO_INDEPENDENT_KW_7 = 7;

	public static final char SURVIVAL_1 = 1;
	public static final char SURVIVAL_2 = 2;
	public static final char SURVIVAL_3 = 3;
	public static final char SURVIVAL_4 = 4;
	public static final char SURVIVAL_5 = 5;
	public static final char SURVIVAL_6 = 6;
	public static final char SURVIVAL_7 = 7;

	public static final char DICHOTOMOUS_PROPORTION_1 = 1;

	public static final char CHI_SQUARE_MODEL_FIT_1 = 1;
	public static final char TWO_INDEPENDENT_FRIEDMAN_1 = 1;
	public static final char TWO_INDEPENDENT_FRIEDMAN_2 = 2;

	public static final char KS_1 = 1;
	public static final char KS_2 = 2;
	public static final char KS_3 = 3;
	public static final char KS_4 = 4;
	public static final char KS_5 = 5;
	public static final char KS_6 = 6;
	public static final char KS_7 = 7;
	public static final char KS_8 = 8;

	public static final char FK_1 = 1;
	public static final char FK_2 = 2;
	public static final char FK_3 = 3;
	public static final char FK_4 = 4;
	public static final char FK_5 = 5;
	public static final char FK_6 = 6;
	public static final char FK_7 = 7;
	public static final char FK_8 = 8;
	
	public static final char CI_1 = 1;
	public static final char CI_2 = 2;
	public static final char CI_3 = 3;
	
	public static final char LOGISTIC_REGRESSION_1 = 1;
	public static final char LOGISTIC_REGRESSION_2 = 2;
	public static final char LOGISTIC_REGRESSION_3 = 3;
        
        public static final char Clustering_1 = 1; // added
        public static final char ONEZ_1 = 1; // added

/*	public static final char TWO_INDEPENDENT_PAIR_SIGN_RANK_1 = 1;
	public static final char TWO_INDEPENDENT_PAIR_SIGN_RANK_2 = 2;
	public static final char TWO_INDEPENDENT_PAIR_SIGN_RANK_3 = 3;
	public static final char TWO_INDEPENDENT_PAIR_SIGN_RANK_4 = 4;
	public static final char TWO_INDEPENDENT_PAIR_SIGN_RANK_5 = 5;
	public static final char TWO_INDEPENDENT_PAIR_SIGN_RANK_6 = 6;
	public static final char TWO_INDEPENDENT_PAIR_SIGN_RANK_7 = 7;
*/

}
