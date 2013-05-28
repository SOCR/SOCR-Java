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
import edu.ucla.stat.SOCR.analyses.data.DataFrame;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

/** Example data generator for ANOVA & other statistical applets. */
public class ExampleDataRandom extends ExampleData{
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";
	private final int SAMPLE_SIZE_DEFAULT_BIG = 10;
	private final int SAMPLE_SIZE_DEFAULT_SMALL = 5;
	private final int SAMPLE_SIZE_MAX = 35;
	private int sampleSize = 0;

	/** Constructor method for simple data generation for regression/correlation tests*/
	/*

	public  ExampleDataRandom() {

		StringBuffer f3 = new StringBuffer();

		//now let's generate some data
		int i = 0;
		int size = (int) (Math.random() * 15) + 3;
		////System.out.println("size = " + size);
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
		//try {
			example = new String[2][size];
			columnNames = new String[2];

			columnNames[0]="Y";
			columnNames[1] = "X";

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
	*/
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

	CHI_SQUARE_MODEL_FIT = 61;
*/
	public  ExampleDataRandom(int index, int dummy, int sampleSize) {
		sampleSize = (int) Math.floor(SAMPLE_SIZE_MAX * Math.random());
		if (sampleSize < SAMPLE_SIZE_DEFAULT_BIG )
			sampleSize = SAMPLE_SIZE_DEFAULT_BIG;

		////System.out.println("sampleSize = " + sampleSize);
 		//dataTable = ExampleData.getNullExample();
		switch (index) {
		case AnalysisType.CI: 
		case AnalysisType.SIMPLE_LINEAR_REGRESSION : { // used for TWO_PAIRED_T too.
			//int sampleSize = sampleSize;

			example = new String[sampleSize][2];
			columnNames = new String[2];

			columnNames[0]= "X";
			columnNames[1] = "Y";
			Data data = RandomExample.simpleRandomRegression(sampleSize);

			double[] varA = data.getDoubleX(Data.INDEPENDENT_VAR);
			double[] varB  = data.getDoubleY(Data.DEPENDENT_VAR);
				for (int j = 0; j < sampleSize; j++) {
				//////System.out.println("IN EXAMPLE " + j + " " + varA[j] + " " + varB[j]);
					varA[j] = Math.abs(100 * varA[j]);
					varB[j] = Math.abs(100 * varB[j]);
					example[j][0] = ("" + varA[j]).substring(0,(""+varA[j]).indexOf(DOT));
					example[j][1] = ("" + varB[j]).substring(0,(""+varB[j]).indexOf(DOT));
				}
				dataTable = new JTable(example, columnNames);

			break;
			}
	
		case AnalysisType.ANOVA_ONE_WAY : {
  		  	//sampleSize = sampleSize-1;

  			  	example = new String[sampleSize][2];
  				columnNames = new String[2];

  				columnNames[0]="Y";
  				columnNames[1] = "J";

 			Data data = RandomExample.simpleRandomRegression(sampleSize);

 			double[] varA = data.getDoubleX(Data.DEPENDENT_VAR);
 			double[] varB  = data.getDoubleY(Data.INDEPENDENT_VAR);
 			int group1 = 1;
 			int group2 = 2;
 			int group3 = 3;
 			int group = 0;
 				for (int j = 0; j < sampleSize; j++) {
 				//////System.out.println("IN EXAMPLE " + j + " " + varA[j] + " " + varB[j]);
 				if (Math.random() > 0.3) {
 					if (varA[j] > 0.5) {
 						group = group1;
 					}
 					else {
 						group = group2;
 					}
 				} else {
					if (varA[j] <0.3) {
						group = group1;
					}
					else  if (varA[j] > 0.7) {
						group = group2;
					}
					else {
						group = group3;
					}
 				}
 					varA[j] = Math.abs(100 * varA[j]);
 					varB[j] = Math.abs(100 * varB[j]);
 					example[j][1] = "" + group;
 					example[j][0] = ("" + varA[j]).substring(0,(""+varA[j]).indexOf(DOT));
				}
  				dataTable = new JTable(example, columnNames);

  		    break;
  			}
		  case AnalysisType.ONE_T : {
 		  	//int sampleSize = sampleSize;

			example = new String[sampleSize][1];
			columnNames = new String[1];

			columnNames[0]="X";
			Data data = RandomExample.oneTRandom(sampleSize);
			////////System.out.println("ExampleDataRandom generator done");
			double[] varA = data.getDoubleX(Data.INDEPENDENT_VAR);

			for (int j = 0; j < sampleSize; j++) {
				varA[j] = Math.abs(100 * varA[j]);

				example[j][0] = ("" + varA[j]).substring(0,(""+varA[j]).indexOf(DOT));
			}
			dataTable = new JTable(example, columnNames);

  		    break;
  			}
		  case AnalysisType.NORMAL_POWER : {
 		  	//int sampleSize = 3 * sampleSize;

			example = new String[sampleSize][1];
			columnNames = new String[1];

			columnNames[0]="X";
			Data data = RandomExample.oneTRandom(sampleSize);
			////////System.out.println("ExampleDataRandom generator done");
			double[] varA = data.getDoubleX(Data.INDEPENDENT_VAR);
			int multiple = 100;
			double randomNumber = Math.random();
			/*
			if (multiple < .33) {
				multiple = 10;
			} else if (multiple < .66) {
				multiple = 100;
			} else {
				multiple = 1000;
			}*/
			for (int j = 0; j < sampleSize; j++) {
				varA[j] = Math.abs(multiple * varA[j]);

				example[j][0] = ("" + varA[j]).substring(0,(""+varA[j]).indexOf(DOT));
			}
			dataTable = new JTable(example, columnNames);

  		    break;
  			}

 		case AnalysisType.TWO_INDEPENDENT_T :
 		case AnalysisType.TWO_INDEPENDENT_WILCOXON: {
			sampleSize = (int) Math.floor(SAMPLE_SIZE_MAX * Math.random());
			if (sampleSize < SAMPLE_SIZE_DEFAULT_SMALL )
			sampleSize = SAMPLE_SIZE_DEFAULT_SMALL;
			////System.out.println("2 indepen sampleSize = " + sampleSize);
 			example = new String[sampleSize][2];
 			columnNames = new String[2];

 			columnNames[0] = "A";
 			columnNames[1] = "B";
 			Data data = RandomExample.simpleRandomRegression(sampleSize);

 			double[] varA = data.getDoubleX(Data.INDEPENDENT_VAR);
 			double[] varB  = data.getDoubleY(Data.DEPENDENT_VAR);
 				for (int j = 0; j < sampleSize; j++) {
 				//////System.out.println("IN EXAMPLE " + j + " " + varA[j] + " " + varB[j]);
 					varA[j] = 15 + Math.abs(10 * varA[j]);
 					varB[j] = 15 + Math.abs(10 * varB[j]);
 					example[j][0] = ("" + varA[j]).substring(0,(""+varA[j]).indexOf(DOT));
 					example[j][1] = ("" + varB[j]).substring(0,(""+varB[j]).indexOf(DOT));
 				}
				int randomEmpty = (int) Math.floor(0.3 * (double)sampleSize * Math.random());
				////System.out.println("randomEmpty = " + randomEmpty);
 				for (int i = sampleSize-1; i >= sampleSize-randomEmpty; i--) {
 				//for (int i = 0; i < randomEmpty; i++) {
 					example[i][1] = "";
				}
 				dataTable = new JTable(example, columnNames);

 			break;
			}
		case AnalysisType.TWO_PAIRED_T :
		case AnalysisType.TWO_PAIRED_SIGN_TEST :
		case AnalysisType.TWO_PAIRED_SIGNED_RANK : {
			sampleSize = (int) Math.floor(SAMPLE_SIZE_MAX * Math.random());
			if (sampleSize < SAMPLE_SIZE_DEFAULT_SMALL )
			sampleSize = SAMPLE_SIZE_DEFAULT_SMALL;
			////System.out.println("2 paired sampleSize = " + sampleSize);

			example = new String[sampleSize][2];
			columnNames = new String[2];

			columnNames[0]= "A";
			columnNames[1] = "B";
			Data data = RandomExample.simpleRandomRegression(sampleSize);

			double[] varA = data.getDoubleX(Data.INDEPENDENT_VAR);
			double[] varB  = data.getDoubleY(Data.DEPENDENT_VAR);
				for (int j = 0; j < sampleSize; j++) {
					varA[j] = Math.abs(100 * varA[j]);
					varB[j] = Math.abs(100 * varB[j]);
					example[j][0] = ("" + varA[j]).substring(0,(""+varA[j]).indexOf(DOT));
					example[j][1] = ("" + varB[j]).substring(0,(""+varB[j]).indexOf(DOT));
				}
				dataTable = new JTable(example, columnNames);

			break;
			}
		case AnalysisType.CHI_SQUARE_MODEL_FIT : {
			// This is directly copied from the paragraph above but we whouls write a generic method instead. annieche 20060405.
			//int sampleSize = sampleSize;

			example = new String[sampleSize][2];
			columnNames = new String[2];

			columnNames[0]= "X";
			columnNames[1] = "VY";
			Data data = RandomExample.simpleRandomRegression(sampleSize);

			double[] varA = data.getDoubleX(Data.INDEPENDENT_VAR);
			double[] varB  = data.getDoubleY(Data.DEPENDENT_VAR);
				for (int j = 0; j < sampleSize; j++) {
					varA[j] = Math.abs(100 * varA[j]);
					varB[j] = Math.abs(100 * varB[j]);
					example[j][0] = ("" + varA[j]).substring(0,(""+varA[j]).indexOf(DOT));
					example[j][1] = ("" + varB[j]).substring(0,(""+varB[j]).indexOf(DOT));
				}
				dataTable = new JTable(example, columnNames);

			break;
			}


		case AnalysisType.DICHOTOMOUS_PROPORTION : {
			int multipleSize = 20;
			int portionP = (int) (multipleSize * Math.random()) + 1;
			int portionQ = (int) (multipleSize * Math.random()) + 1;

			sampleSize = portionP + portionQ;

			example = new String[sampleSize][1];
			columnNames = new String[1];

			columnNames[0]= "X";
			//Data data = RandomExample.simpleRandomRegression(sampleSize);



			double p = (double)portionP / (double)sampleSize;
			double q = (double)portionQ / (double)sampleSize;
			//System.out.println("portionP = " + portionP);
			//System.out.println("portionQ = " + portionQ);
			//System.out.println("sampleSize = " + sampleSize);
			//System.out.println("p = " + p);
			//System.out.println("q = " + q);

			for (int i = 0; i < portionP; i++) {
				example[i][0] = "0";
			}

			for (int i = portionP; i < sampleSize; i++) {
				example[i][0] = "1";
			}
			dataTable = new JTable(example, columnNames);

			break;
			}
		  case AnalysisType.LOGISTIC_REGRESSION : {
			  example = new String[sampleSize][2];
				columnNames = new String[2];

				columnNames[0]= "Predictor";
				columnNames[1] = "Response";
				Data data = RandomExample.logisticRegression(sampleSize);

				double[] varA = data.getDoubleX(Data.INDEPENDENT_VAR);
				double[] varB  = data.getDoubleY(Data.DEPENDENT_VAR);
					for (int j = 0; j < sampleSize; j++) {
						example[j][0] = "" + varA[j];
						example[j][1] = "" + varB[j];
					}
					dataTable = new JTable(example, columnNames);

				break;
			}
		} // end switch
	}

	/** returns a JTable object containing the Example Data */
	public JTable getExample() {
		//System.out.println("ExampleDataRandom getExample dataTable = " + dataTable);

		return dataTable;
	 }

}
