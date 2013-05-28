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
public class ClusteringExamples extends ExampleData {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
	// system-independent line terminator
	public JTable dataTable;
	private final String DOT = ".";
	private static String dataSource = "http://wiki.stat.ucla.edu/socr/index.php/SOCR_Data_Oct2009_ID_NI";
	public static boolean[] availableExamples = new boolean[]{true, false, false, false, false, false, false, false, false, false};

	/** Constructor method for simple data generation for regression/correlation tests*/
	public  ClusteringExamples() {
		this.dataTable = getRandomExample();
	}

	/** Constructor method for sampel data for ANOVA analysis.*/

	public  ClusteringExamples(int analysisType, int exampleID) {
		//System.out.println("MultiLinearRegressionExamples exampleID " + exampleID);

		switch (exampleID) {
			case NULL_EXAMPLE : {
				this.getNullExample();
				break;
			}
			case ExampleData.Clustering_1: {
				String[] zeroth = {"Homo_sapiens","Pan ", "Gorilla", "Pongo", "Hylobates","Macaca_fuscata","Macaca_mulatta", "Macaca_fascicular", "Macaca_sylvanus", "Saimiri_sciureus","Tarsius_syrichta", "Lemur_catta"};

                                double[] first = {0,89,104,161,182,232,233,249,256,273,322,308};

				double[] second = {89,0,106,171,189,243,251,268,249,284,321,309 };

				double[] third ={104,106,0,166,189,237,235,262,244,271,314,293 };

				double[] fourth = {161,171,166,0,188,244,247,262,241,284,303,293 };
                                
                                double[] fifth = {182,189,189,188,0,247,239,257,242,269,309,296};
                                
                                double[] sixth = {232,243,237,244,247,0,36,84,124,289,314,282 };
                                
                                double[] seventh = {233,251,235,247,239,36,0,93,120,293,316,289 };
                                
                                double[] eighth = {249,268,262,262,257,84,93,0,123,287,311,298};
                                
                                double[] ninth = {256,249,244,241,242,124,120,123,0,287,319,287};
                                
                                double[] tenth = {273,284,271,284,269,289,293,287,287,0,320,285};
                                
                                double[] eleventh = {322,321,314,303,309,314,316,311,319,320,0,252 };
                                
                                double[] twelfth = {308,309,293,293,296,282,289,298,287,285,252,0};
                                

				int sampleSize = 12;
				int varSize = 13;
				example = new String[13][12];
				columnNames = new String[12];

				columnNames[0] = "C1";
				columnNames[1] = "C2";
				columnNames[2] = "C3";
				columnNames[3] = "C4";
				columnNames[4] = "C5";
                                columnNames[5] = "C6";
				columnNames[6] = "C7";
				columnNames[7] = "C8";
				columnNames[8] = "C9";
				columnNames[9] = "C10";
                                columnNames[10] = "C11";
				columnNames[11] = "C12";

				for (int i = 0; i < 12; i++) {
                                        example[0][i] = zeroth[i] + "";

                                        example[1][i] = first[i] + "";
					
					example[2][i] = second[i] + "";
					
					example[3][i] = third[i] + "";
					
					example[4][i] = fourth[i] + "";
				
					example[5][i] = fifth[i] + "";
					
                                        example[6][i] = sixth[i] + "";
				
                                        example[7][i] = seventh[i] + "";
				
					example[8][i] = eighth[i] + "";
					
					example[9][i] = ninth[i] + "";
					
					example[10][i] = tenth[i] + "";
					
					example[11][i] = eleventh[i] + "";
					
                                        example[12][i] = twelfth[i] + "";
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
