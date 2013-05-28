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
package edu.ucla.stat.SOCR.analyses.command;

import java.text.DecimalFormat;
import java.util.*;
import java.awt.Color;
import java.io.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.model.*;

import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException;
import edu.ucla.stat.SOCR.analyses.command.Utility;
import edu.ucla.stat.SOCR.util.*;

public class FlignerKilleenCSV {
	private static final String MISSING_MARK = ".";

	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\FK.txt";
		boolean header=false;
		boolean filesLoaded = false;

		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");

		try {
			fileName1 = args[0];

			filesLoaded = true;
		} catch (Exception e) {
			//System.out.println("\nType in two file names like this:");
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.AnovaOneWayCSV fileName1.txt fileName2.txt\n");

			e.printStackTrace();
		}
		if (args.length>=2){
			if (args[1].equals("-h"))
				header=true;		
		}
		if (!filesLoaded) {
			return;
		}
	
		String[] varHeaders = null;
		
		ArrayList<ArrayList<String>>lists = new ArrayList<ArrayList<String>>();
		
		
		String line = null;


		StringTokenizer st = null;
	
		int sampleSize = 0;
		
		int columnCount=0;
		
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName1));
			//StringBuffer sb = new StringBuffer();
	
			if (header)
			{
				line = bReader.readLine();
				st = new StringTokenizer(line, ",; \t");
			
				columnCount = st.countTokens();
				System.out.println("columnCount= " + columnCount);
				varHeaders = new String[columnCount];
				
				for (int i=0; i<columnCount; i++)
					varHeaders[i]=st.nextToken().trim();
			}
			
			while ( (line = bReader.readLine()) != null) {
			//	System.out.println(line);

					st = new StringTokenizer(line, ",; \t");
					columnCount = st.countTokens();
					
					ArrayList<String> list = new ArrayList<String>();
					
					try {
						
						while( st.hasMoreTokens()){
							
							String input = st.nextToken().trim();
							if (!input.equalsIgnoreCase(MISSING_MARK)) {
								list.add(input);
							}
						}
						sampleSize++;	
					}  catch (Exception e) {
					
						e.printStackTrace();
						//System.out.println(Utility.getErrorMessage("One Way ANOVA"));
						return;
					}
					lists.add(list);

			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		System.out.println("Sample size = " + sampleSize);
	/*	for (int i = 0; i < sampleSize; i++) {
			System.out.print("Input case " + (i+1) + " : ");
			for (int j=0 ;j<columnCount; j++)
				System.out.print(lists.get(i).get(j) +",");
			System.out.println();
		}*/

		Data data = new Data();
		double[] xData = new double[sampleSize];

		for (int i=0; i<columnCount; i++){
			xData = new double[sampleSize];
			for (int j = 0; j < sampleSize; j++) {
				try {
						xData[j] = (Double.valueOf((String)(lists.get(j)).get(i))).doubleValue();
				} catch (NumberFormatException e) {
					System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
				}
			}
			data.appendX(varHeaders[i], xData, DataType.QUANTITATIVE);
		}



		FlignerKilleenResult result = null;

		
		try {
			result = (FlignerKilleenResult)data.getAnalysis(AnalysisType.FLIGNER_KILLEEN);
		} catch (Exception e) {
			//e.printStackTrace();
		}


		/***************************************************/
		/*******************************************************/
		int total = 0, df = 0; int[] groupSize = null;
		double var = 0, totalMeanScore = 0, chiStat = 0;
		double[] median = null, meanScore = null;
		double[][] normalScore = null;
		String[] groupNames = null;
		
		DecimalFormat dFormat = new DecimalFormat("#.00000");
		result.setDecimalFormat(dFormat);

		try {
			groupNames = result.getGroupNames();
		} catch (Exception e) {
			//System.out.println("sampleSize Exception  = " + e);
		}
		try {
			total = result.getTotal();
		} catch (Exception e) {
			//System.out.println("sampleSize Exception  = " + e);
		}
		try {
			df = result.getDF();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			groupSize = result.getGroupSize();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		//System.out.println(groupSize[0]);
		try {
			var = result.getVariance();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			totalMeanScore = result.getTotalMeanScore();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}


		try {
			median= result.getMedian();
		} catch (NullPointerException e) {
			System.out.println("median Exception  = " + e);
		}
		///System.out.println("meanScore[0] = " + median[0]);
		/*
		try {
			meanScore= result.getMeanScore();
		} catch (NullPointerException e) {
			System.out.println("meanScore Exception  = " + e);
		}
		*/
		try {
			df = result.getDF();
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		try {
			normalScore = result.getScore();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}

		try {
			chiStat = result.getChiStat();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		double pValue = 0;
		try {
			pValue = result.getPValue();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		

		int numberGroups = groupSize.length;
		for (int i = 0; i < numberGroups; i++) {
			System.out.println("\n\tGroup " + groupNames[i]);
			System.out.println("\n\tmedian = " + median[i]);
			System.out.println("\n");
		}
		System.out.println("\n\tTotal Size = " + total);
		System.out.println("\n\tTotal Mean Score = " + result.getFormattedDouble(totalMeanScore));
		System.out.println("\n\tTotal Variance = " + result.getFormattedDouble(var));
		System.out.println("\n\tDegrees of Freedom = " + df);
		System.out.println("\n\tChi-Square Statistic = " + result.getFormattedDouble(chiStat));
		System.out.println("\n\tP-Value = " + result.getFormattedDouble(pValue));

		
	}
}
