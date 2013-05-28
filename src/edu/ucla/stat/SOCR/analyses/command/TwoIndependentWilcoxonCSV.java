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
public class TwoIndependentWilcoxonCSV {

	
	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		String fileName2 = null;//"C:\\STAT\\SOCR\\test\\dataTwo.txt\\";
		boolean filesLoaded = false;
		boolean header=false;
		final int SIZE_LARGE_SAMPLE = 10;
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");

		try {
			fileName1 = args[0];
			fileName2 = args[1];
			filesLoaded = true;
		} catch (Exception e) {
			//System.out.println("\nType in two file names like this:");
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.TwoIndependentText fileName1.txt fileName2.txt\n");


		}
		if (args.length>=3){
			if (args[2].equals("-h"))
				header=true;		
		}
		
		if (!filesLoaded) {
			return;
		}
		String varHeader1 ="variable 1";
		String varHeader2 ="variable 2";
	
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		String line = null;


		// read the first file
		//System.out.println("Data File 1: " + fileName1);
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName1));
			//StringBuffer sb = new StringBuffer();
			if (header)
				varHeader1=bReader.readLine();
			
			while ( (line = bReader.readLine()) != null) {
				list1.add(line);
				//System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.out.println("\nData File 2: " + fileName2);

		// read the second file
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName2));
			//StringBuffer sb = new StringBuffer();
			if (header)
				varHeader2=bReader.readLine();
			
			while ( (line = bReader.readLine()) != null) {
				list2.add(line);
				//System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int length1 = list1.size();
		int length2 = list2.size();

		double[] data1 = new double[length1];
		double[] data2 = new double[length2];


		//System.out.println("\nlength1 = " + length1);

		//System.out.println("length2 = " + length2);

		for (int i = 0; i < length1; i++) {
			try {
				data1[i] = (Double.valueOf((String)list1.get(i))).doubleValue();
			} catch (NumberFormatException e) {
				System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
		}
		}
		for (int i = 0; i < length2; i++) {
			try {
				data2[i] = (Double.valueOf((String)list2.get(i))).doubleValue();
			} catch (NumberFormatException e) {
				System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
			}
		}
		Data data = new Data();

		/***************** X is 2, Y is 1 *****************/
		data.appendX(varHeader2, data2, DataType.QUANTITATIVE);
		data.appendY(varHeader1, data1, DataType.QUANTITATIVE);
	
		/***************************************************/

		double rankSumSmall = 0, rankSumLarge = 0;
		double uStatSmall = 0, uStatLarge = 0;
		double meanY = 0, meanX = 0, tStat = 0 ;
		double meanU = 0, varU = 0, zScore = 0;
		double pValue1 = 0, pValue2 = 0;
		String groupNameSmall = "", groupNameLarge = ""; // small means small in group size
		int df = 0;
		String uFormulaExp = null, uFormulaVar = null, dataSummary1 = null, dataSummary2 = null;
		boolean isLargeSample = false;
		String whosSmaller = "";
		TwoIndependentWilcoxonResult result = null;

		/*** Ivo commented out
		 
		if (varHeader1.equals(null)&&varHeader1.equals(null))
		{
			varHeader1="variable 1";
			varHeader2="variable 2";
		}
		******/
		
		try {
			result = (TwoIndependentWilcoxonResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_WILCOXON);
		} catch (Exception e) {
		}
		
		try {
			isLargeSample = result.isLargeSample();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			meanX = result.getMeanX();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanY = result.getMeanY();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			groupNameSmall = result.getGroupNameSmall();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion3 " +e + "");
		}

		try {
			groupNameLarge = result.getGroupNameLarge();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion3 " +e + "");
		}
		try {
			rankSumSmall = result.getRankSumSmallerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion3 " +e + "");
		}
		try {
			rankSumLarge = result.getRankSumLargerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.RANK_SUM_LARGE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			uStatSmall = result.getUStatSmallerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.U_STAT_SMALL)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			uStatLarge = result.getUStatLargerGroup();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.U_STAT_LARGE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			meanU = result.getMeanU();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			varU = result.getVarianceU();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			zScore = result.getZScore();//((Double)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE)).doubleValue();
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			pValue2 = result.getPValueTwoSided();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			pValue1 = result.getPValueOneSided();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}

		try {
			uFormulaExp = result.getUMeanFormula();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			uFormulaVar = result.getUVarianceFormula();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
			} catch (Exception e) {
				//showError(e + "");
		}
		try {
			dataSummary1 = result.getDataSummaryOfGroup1();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}
		try {
			dataSummary2 = result.getDataSummaryOfGroup2();//((String)result.getTexture().get(TwoIndependentWilcoxonResult.P_VALUE));
		} catch (Exception e) {
			//showError(e + "");
		}

		if (meanX <= meanY) {
			whosSmaller =varHeader2 + " < " + varHeader1;
		}
		else {
			whosSmaller =varHeader1 + " < " + varHeader2;	
		}
		dataSummary1 = dataSummary1.substring(0, dataSummary1.length()-2);
		dataSummary2 = dataSummary2.substring(0, dataSummary2.length()-2);

		System.out.println("\n"); //clear first
		

		System.out.println("\tResults of Two Independent Sample Wilcoxon Rank Sum Test:" );
		
		if (header)
		{
			System.out.println("\n\tVariable 1 =" + varHeader1 + ". Sample Size = " + length1);
	
			System.out.println("\n\tVariable 2 =" + varHeader2 + ",. Sample Size = " + length2+ "\n" );
		}
		System.out.println("\n\n\tData (with Ranks):");
		System.out.println("\n");

		System.out.println("\n\tData of Group " + dataSummary2);
		System.out.println("\n\n\tData of Group " +dataSummary1);
		System.out.println("\n");
		
		
			if (varHeader1.equalsIgnoreCase(groupNameSmall)) {
				System.out.println("\n\tGroup "+ varHeader1 + ":\n\tSample Size = " + length1 + "\n\tMean = " + meanY + "\n\tRank Sum = " + rankSumSmall + "\n\tTest Statistics = " +  uStatSmall + "\n\t");
				System.out.println("\n\tGroup "+ varHeader2 + ":\n\tSample Size = " + length2 + "\n\tMean = " + meanX + "\n\tRank Sum = " + rankSumLarge + "\n\tTest Statistics = " +  uStatLarge + "\n\t");

			} else {
				System.out.println("\n\tGroup "+ varHeader1 + ":\n\tSample Size = " + length1 + "\n\tMean = " + meanY + "\n\tRank Sum = " + rankSumLarge + "\n\tTest Statistics = " +  uStatLarge);
				System.out.println("\n\n\tGroup "+ varHeader2 + ":\n\tSample Size = " + length2 + "\n\tMean = " + meanX + "\n\tRank Sum = " + rankSumSmall + "\n\tTest Statistics = " +  uStatSmall);
			}
			
	
		if (isLargeSample) {
			System.out.println("\n\n\tExpectation of Test Statistics = " + meanU);
			System.out.println("\n\n\tVariance of Test Statistics = " + varU);
			System.out.println("\n\n\tZ-Score " + zScore);
			System.out.println("\n\n\tOne-Sided P-Value for " + whosSmaller + ": " + pValue1);
			
				System.out.println("\n\n\tTwo-Sided P-Value for " + varHeader1 + " not equal to " + varHeader2 + ": " + pValue2);
				
				
		} else {
			System.out.println("\n\n\tOne-Sided P-Value for " + whosSmaller + ": " + pValue1);
				System.out.println("\n\n\tTwo-Sided P-Value for " + varHeader1 + " not equal to " +varHeader2 + ": " + pValue2);
				}
		System.out.println("\n\n\t*********************** Note ***********************");

		if (isLargeSample) {
			System.out.println("\n\tEither sample size > "+ SIZE_LARGE_SAMPLE + ", use large sample approximation.");

			System.out.println("\n\tUse Normal Approximation, when at least 1 sample-size is > 10.");
		}
		else {
			System.out.println("\n\tUse the exact U-test when both sample sizes are <= 10.\n"+
					"\tUse Normal Approximation, when at least 1 sample-size is > 10.\n");
		}
		if (uFormulaExp != null)
			System.out.println("\n\tFormula Used for the Expectation of the Test Statistics = " + uFormulaExp + "\n");
		if (uFormulaVar != null)
			System.out.println("\tFormula Used for the Variance of the Test Statistics = " + uFormulaVar + "\n\n");

		
		}

	
	
	}

