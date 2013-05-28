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
import java.io.*;

import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.model.*;

import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.analyses.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.command.Utility;
import edu.ucla.stat.SOCR.util.*;

public class SimpleRegressionCSV {
	
	
	private static final String MISSING_MARK = ".";

	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
	
		boolean filesLoaded = false;
		boolean header=false;
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");

		try {
			fileName1 = args[0];
			filesLoaded = true;
		} catch (Exception e) {
			//System.out.println("\nType in two file names like this:");
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.SimpleLinearRegression fileName1.txt fileName2.txt\n");


		}
		if (args.length>=2){
			if (args[1].equals("-h"))
				header=true;		
		}
		
		if (!filesLoaded) {
			return;
		}
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		String line = null;
		String varHeader1 =null;
		String varHeader2 =null;
		

		StringTokenizer st = null;
		String input1 = null;
		String input2 = null;
		int sampleSize = 0;
		boolean read=false;
		boolean read1=false;
		
		
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName1));
			//StringBuffer sb = new StringBuffer();

			while ( (line = bReader.readLine()) != null) {
				//System.out.println(line);
				
				// only takes two rows and ignore the rest.
					st = new StringTokenizer(line, ",; \t" );
					try {
						input1 = st.nextToken().trim();
						input2 = st.nextToken().trim();

						if ((header)&&(!read))
						{
							varHeader1=input1;
							varHeader2=input2;
							read=true;
							read1=true;
						}
						
						if (!read1 && !input1.equalsIgnoreCase(MISSING_MARK) && !input2.equalsIgnoreCase(MISSING_MARK)) {
							
							list1.add(input1);
							list2.add(input2);
							sampleSize++;
						}
						if (read)
							read1=false;
					} catch (NoSuchElementException e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Simple Linear Regression"));
						return;
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Simple Linear Regression"));
						return;
					}


			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		//System.out.println("Sample size = " + sampleSize);
		//for (int i = 0; i < sampleSize; i++) {
		//	System.out.println("Input case " + (i+1) + " : " + list1.get(i) + "  " + list2.get(i));
		//}

		double[] yData = new double[sampleSize];
		double[] jData = new double[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			try {
				yData[i] = (Double.valueOf((String)list1.get(i))).doubleValue();
			} catch (NumberFormatException e) {
				System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
			}
		}
		for (int i = 0; i < sampleSize; i++) {
			try {
				jData[i] = (Double.valueOf((String)list2.get(i))).doubleValue();
			} catch (NumberFormatException e) {
				System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
			}
		}


		for (int i = 0; i < sampleSize; i++) {
			//System.out.println("Input case " + (i+1) + " : " + yData[i] + "  " + jData[i]);
		}



		Data data = new Data();

		/***************** X is 2, Y is 1 *****************/
		data.appendX("J", jData, DataType.QUANTITATIVE);
		data.appendY("Y", yData, DataType.QUANTITATIVE);
		SimpleLinearRegressionResult result = null;

		try {
			result = (SimpleLinearRegressionResult)data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);
		} catch (Exception e) {
			//e.printStackTrace();
		}


		/***************************************************/
		
		 double[] predicted = null;
		 double[] predictedUpper = null;
		 double[] predictedLower = null;
		 double[] varPredict = null;
		 double[] sdPredict = null;

		 double[] residuals = null;
		 /*double[] sortedResiduals = null;
		 double[] sortedStandardizedResiduals = null;
		 int[] sortedResidualsIndex = null;
		 double[] sortedNormalQuantiles = null;
		 double[] sortedStandardizedNormalQuantiles = null;*/
		 double slope, intercept;
		 double beta = 0, alpha = 0, meanX = 0, meanY = 0, sdAlpha = 0, sdBeta = 0;
			double tStatAlpha = 0, tStatBeta = 0;
			double corrXY = 0, rSquare = 0;
			double pvAlpha = 0, pvBeta = 0;
			//String betaName = null;
		residuals = new double[sampleSize];
		predicted = new double[sampleSize];
		predictedUpper = new double[sampleSize];
		predictedLower = new double[sampleSize];
		varPredict = new double[sampleSize];
		sdPredict = new double[sampleSize];

		try {
			residuals = result.getResiduals();

		} catch (NullPointerException e) {
			//////System.out.println("residuals Exception " + e);
		}
		try {
			predicted = result.getPredicted();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			varPredict = result.getVarPredict();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			sdPredict = result.getSDPredict();
		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			predictedUpper = result.getPredictedUpperBound();

		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}
		try {
			predictedLower = result.getPredictedLowerBound();

		} catch (NullPointerException e) {
			////System.out.println("predicted Exception " + e);
		}

		try {
			beta = result.getBeta();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		/*try {
			betaName = result.getBetaName();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}*/
		////////System.out.println("SimpleLinearRegression betaName = " + betaName);
		try {
			alpha = result.getAlpha();
		} catch (NullPointerException e) {
		}

		try {
			meanX = result.getMeanX();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		try {
			meanY = result.getMeanY();//((Double)
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		try {
			sdAlpha = result.getAlphaSE();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sdBeta = result.getBetaSE();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_SE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			tStatAlpha =  result.getAlphaTStat();//((Double)result.getTexture().get(SimpleLinearRegressionResult.ALPHA_T_STAT)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			tStatBeta = result.getBetaTStat();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_T_STAT)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			pvAlpha = result.getAlphaPValue();//((Double)result.getTexture().get(SimpleLinearRegressionResult.ALPHA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			pvBeta = result.getBetaPValue();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			corrXY = result.getCorrelationXY();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			rSquare = result.getRSquare();//((Double)result.getTexture().get(SimpleLinearRegressionResult.BETA_P_VALUE)).doubleValue();
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - 2);
/*
		try {
			sortedResiduals = result.getSortedResiduals();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedResiduals = result.getSortedStandardizedResiduals();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_STANDARDIZED_RESIDUALS);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}

		try {
			sortedResidualsIndex = result.getSortedResidualsIndex();//(int[])residualMap.get(SimpleLinearRegressionResult.SORTED_RESIDUALS_INDEX);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedNormalQuantiles = result.getSortedNormalQuantiles();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();//(double[])residualMap.get(SimpleLinearRegressionResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);
		} catch (NullPointerException e) {
			//showError("\nNullPointerException  = " + e);
		}
		
		*/
		
		slope = beta;
		intercept = alpha;

		if (!header)
		{
			varHeader1="Variable 1";
			varHeader2="Variable 2";
			
		}
		
		
		System.out.println("\tSample Size = " + sampleSize + " \n" );
		System.out.println("\n\tDependent Variable = " + varHeader2 + " \n" );
		System.out.println("\n\tIndependent Variable = "  + varHeader1 + " \n" );
		System.out.println("\n\tSimple Linear Regression Results:\n" );

		System.out.println("\n\tMean of " + varHeader1 + " = " + meanX);
		System.out.println("\n\tMean of " +   varHeader2 + " = " + meanY);


		System.out.println("\n\n\tRegression Line:\n\t\t" + varHeader2 + " = " + alpha);
		if (beta >= 0) {
			System.out.println(" + " + beta + "   " + varHeader1);
		}
		else {
			System.out.println(" " + beta + "   " + varHeader1);
		}


		System.out.println("\n\n\tCorrelation(" + varHeader1 + ", " + varHeader2 + ") = " + corrXY);
		
		System.out.println("\n\tR-Square = " + rSquare);
		System.out.println("\n\n");


		System.out.println("\n\tIntercept: ");
		System.out.println("\n\t\tParameter Estimate: " + alpha);
		System.out.println("\n\t\tStandard Error:     " +sdAlpha);
		System.out.println("\n\t\tT-Statistics:        " + tStatAlpha);
		/*
		if (pvAlpha==0.0)) {
			System.out.println("\n\t\tP-Value:            <2E-16");
		}
		else {
			System.out.println("\n\t\tP-Value:            " + pvAlpha);
		}
		*/
		//System.out.println("\n\t\tP-Value:            " + AnalysisUtility.enhanceSmallNumber(pvAlpha));
		System.out.println("\n\t\tP-Value:            " + pvAlpha);


		System.out.println("\n");
		System.out.println("\n\tSlope: ");
		System.out.println("\n\t\tParameter Estimate: " + beta);
		System.out.println("\n\t\tStandard Error:     " +sdBeta);
		System.out.println("\n\t\tT-Statistics:        " + tStatBeta);
		/*if (pvBeta.equals("0.0")) {
			System.out.println("\n\t\tP-Value:            <2E-16");
		}
		else {
		*/
		//System.out.println("\n\t\tP-Value:            " + AnalysisUtility.enhanceSmallNumber(pvBeta));
		System.out.println("\n\t\tP-Value:            " + pvBeta);

		/*
		System.out.println("\nSLOPE             = " + beta);
		System.out.println("\nSTANDARD ERROR of INTERCEPT      = " + sdAlpha);
		System.out.println("\nSTANDARD ERROR of SLOPE       = " + sdBeta);
		System.out.println("\nT-STAT of ALPHA  = " + tStatAlpha);
		System.out.println("\nT_STAT of BETA   = " + tStatBeta);
		System.out.println("\nP-VALUE of ALPHA = " + pvAlpha);
		System.out.println("\nP-VALUE of BETA  = " + pvBeta);
		*/
	
		
		System.out.println("\n\n\t" + varHeader1 + "\t" + varHeader2 + "\tPredicted\tResidual");
		
		
		/*	int NumberDigitKept = 3;
		String[] xTruncated = AnalysisUtility.truncateDigits(xData, NumberDigitKept);
		String[] yTruncated = AnalysisUtility.truncateDigits(yData, NumberDigitKept);
		String[] predictedTruncated = AnalysisUtility.truncateDigits(predicted, NumberDigitKept);
		String[] residualTruncated = AnalysisUtility.truncateDigits(residuals, NumberDigitKept);*/

		for (int i = 0; i < sampleSize; i++) {
			try {
				System.out.println("\n\t" + jData[i] + "\t" + yData[i]+ "\t" +predicted[i] + "\t" + residuals[i] );
			} catch (Exception e) {
			}
		}
	}
}
