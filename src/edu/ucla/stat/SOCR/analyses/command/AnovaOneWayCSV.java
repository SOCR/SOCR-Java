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
import edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException;
import edu.ucla.stat.SOCR.analyses.command.Utility;
import edu.ucla.stat.SOCR.util.*;

public class AnovaOneWayCSV {
	private static final String MISSING_MARK = ".";

	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\AnovaOneWayCSVTest1.txt";
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
		String varHeader1 =null;
		String varHeader2 =null;
		
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		String line = null;


		StringTokenizer st = null;
		//int k = 0;
		String input1 = null;
		String input2 = null;
	
		int sampleSize = 0;
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName1));
			//StringBuffer sb = new StringBuffer();
	
			if (header)
			{
				line = bReader.readLine();
				st = new StringTokenizer(line, ",; \t");
			
				
				varHeader1=st.nextToken().trim();
				varHeader2=st.nextToken().trim();
				
			
			}
			while ( (line = bReader.readLine()) != null) {
			//	System.out.println(line);

				// only takes two rows and ignore the rest.
					st = new StringTokenizer(line, ",; \t");
					
					try {
						
						input1 = st.nextToken().trim();
						input2 = st.nextToken().trim();
						
						if (!input1.equalsIgnoreCase(MISSING_MARK) && !input2.equalsIgnoreCase(MISSING_MARK)) {
							list1.add(input1);
							list2.add(input2);
							sampleSize++;
						
						}
					} catch (NoSuchElementException e) {
						
						e.printStackTrace();
						//System.out.println(Utility.getErrorMessage("One Way ANOVA"));
						return;
					} catch (Exception e) {
					
						e.printStackTrace();
						//System.out.println(Utility.getErrorMessage("One Way ANOVA"));
						return;
					}


			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		//System.out.println("Sample size = " + sampleSize);
		for (int i = 0; i < sampleSize; i++) {
			//System.out.println("Input case " + (i+1) + " : " + list1.get(i) + "  " + list2.get(i));
		}

		double[] yData = new double[sampleSize];
		String[] jData = new String[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			try {
				yData[i] = (Double.valueOf((String)list1.get(i))).doubleValue();
			} catch (NumberFormatException e) {
				System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
			}
		}
		for (int i = 0; i < sampleSize; i++) {
			
			jData[i] = (String)list2.get(i);
		}


		for (int i = 0; i < sampleSize; i++) {
			//System.out.println("Input case " + (i+1) + " : " + yData[i] + "  " + jData[i]);
		}



		Data data = new Data();

		/***************** X is 2, Y is 1 *****************/
		data.appendX("J", jData, DataType.FACTOR);
		data.appendY("Y", yData, DataType.QUANTITATIVE);
		AnovaOneWayResult result = null;

		try {
			result = (AnovaOneWayResult)data.getAnalysis(AnalysisType.ANOVA_ONE_WAY);
		} catch (Exception e) {
			//e.printStackTrace();
		}


		/***************************************************/

		int dfCTotal = 0, dfError = 0, dfModel = 0;
		double rssModel = 0, rssError = 0;
		double mssModel = 0, mssError = 0;
		double rssTotal = 0, fValue = 0;
		double pValue = 0, rSquare = 0;
		double[] residuals = new double[sampleSize];
		double[] predicted = new double[sampleSize];
		try {
			residuals = result.getResiduals();//((double[])result.getTexture().get(AnovaOneWayResult.RESIDUALS));
		} catch (NullPointerException e) {
			//////System.out.println("NullPointerException  = " + e);
		}

		try {
			predicted = result.getPredicted();//((double[])result.getTexture().get(AnovaOneWayResult.PREDICTED));
		} catch (NullPointerException e) {
			////////System.out.println("NullPointerException  = " + e);
		}




		try {
			dfCTotal = result.getDFTotal();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_TOTAL))).intValue();
		} catch (Exception e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			dfError = result.getDFError();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_ERROR))).intValue();
		} catch (Exception e) {
			//////System.out.println("\nException = " + e);
		}


		try {

			dfModel= result.getDFModel();//((Integer)(result.getTexture().get(AnovaOneWayResult.DF_MODEL))).intValue();
		} catch (Exception e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			rssModel= result.getRSSModel();//((String)result.getTexture().get(AnovaOneWayResult.RSS_MODEL));
		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}


		try {
			rssError = result.getRSSError();//((String)result.getTexture().get(AnovaOneWayResult.RSS_ERROR));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			mssModel = result.getMSSModel();//((String)result.getTexture().get(AnovaOneWayResult.MSS_MODEL));
		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			mssError = result.getMSSError();//((String)result.getTexture().get(AnovaOneWayResult.MSS_ERROR));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			rssTotal = result.getRSSTotal();//((String)result.getTexture().get(AnovaOneWayResult.RSS_TOTAL));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			fValue = result.getFValue();//((String)result.getTexture().get(AnovaOneWayResult.F_VALUE));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		try {
			pValue = result.getPValue();//((String)result.getTexture().get(AnovaOneWayResult.P_VALUE));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);


		try {
			rSquare = result.getRSquare();//((String)result.getTexture().get(AnovaOneWayResult.P_VALUE));

		} catch (NullPointerException e) {
			//////System.out.println("\nException = " + e);
		}

		//System.out.println("\n\tIndependent Variable = " + independentHeader );
		//System.out.println("\n\tDependent Variable  = " + dependentHeader + " \n" );

		System.out.println("\n\tResults of One-Way Analysis of Variance:");
		//System.out.println(+ dependentHeader + " \n" );
		if(header)
		{	
			System.out.println("\n\tDependent Variable: " +varHeader1);
			System.out.println("\n\tIndependent Variable: " +varHeader2);
		}	
		System.out.println("\n\n\tModel:");
		System.out.println("\n\tDegrees of Freedom = " + dfModel);
		System.out.println("\n\tResidual Sum of Squares = " + rssModel);
		System.out.println("\n\tMean Square Error = " + mssModel);


		System.out.println("\n\n\tError:");
		System.out.println("\n\tDegrees of Freedom = " + dfError);
		System.out.println("\n\tResidual Sum of Squares = " + rssError);
		System.out.println("\n\tMean Square Error = " + mssError);

		System.out.println("\n\n\tCorrected Total:");
		System.out.println("\n\tDegrees of Freedom = " + dfCTotal);
		System.out.println("\n\tResidual Sum of Squares = " + rssTotal);
		System.out.println("\n\n\tF-Value = " + fValue);
		System.out.println("\n\tP-Value = " + AnalysisUtility.enhanceSmallNumber(pValue));

		System.out.println("\n\n\tR-Square = " + rSquare);
		System.out.println();
	}
}
