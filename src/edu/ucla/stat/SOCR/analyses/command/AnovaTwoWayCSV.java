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

public class AnovaTwoWayCSV {
	private static final String MISSING_MARK = ".";
	String INTERACTIONON	= "INTERACTIONON";
	String INTERACTIONOFF	= "INTERACTIONOFF";
	public final static String INTERACTION_SWITCH	= "INTERACTION";
	
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
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.AnovaTwoWayCSV fileName1.txt fileName2.txt\n");

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
		String varHeader3 =null;
		
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> list3 = new ArrayList<String>();
		String line = null;


		StringTokenizer st = null;
		//int k = 0;
		String input1 = null;
		String input2 = null;
		String input3 = null;
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
				varHeader3=st.nextToken().trim();
			
			}
			while ( (line = bReader.readLine()) != null) {
				//System.out.println(line);

				// only takes two rows and ignore the rest.
					st = new StringTokenizer(line, ",; \t");
					try {
						input1 = st.nextToken().trim();
						input2 = st.nextToken().trim();
						input3 = st.nextToken().trim();
						
						if (!input1.equalsIgnoreCase(MISSING_MARK) && !input2.equalsIgnoreCase(MISSING_MARK)&& !input3.equalsIgnoreCase(MISSING_MARK)) {
							list1.add(input1);
							list2.add(input2);
							list3.add(input3);
							sampleSize++;

						}
					} catch (NoSuchElementException e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Two Way ANOVA"));
						return;
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Two Way ANOVA"));
						return;
					}


			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	/*	System.out.println("Sample size = " + sampleSize);
		for (int i = 0; i < sampleSize; i++) {
			System.out.println("Input case " + (i+1) + " : " + list1.get(i) + "  " + list2.get(i));
		}
*/
		double[] yData = new double[sampleSize];
		String[] jData = new String[sampleSize];
		String[] iData = new String[sampleSize];

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
			iData[i] = (String)list3.get(i);
		}


		for (int i = 0; i < sampleSize; i++) {
			//System.out.println("Input case " + (i+1) + " : " + yData[i] + "  " + jData[i]+ "  " + iData[i]);
		}

		
		boolean interactionOn = false;	
		
		Data data = new Data();
		data.setParameter(AnalysisType.ANOVA_TWO_WAY, INTERACTION_SWITCH, Boolean.valueOf(interactionOn));
		
		/***************** X is 2, Y is 1 *****************/
		data.appendX("I", iData, DataType.FACTOR);
		data.appendX("J", jData, DataType.FACTOR);
		data.appendY("Y", yData, DataType.QUANTITATIVE);
		AnovaTwoWayResult result = null;

		try {
			result = (AnovaTwoWayResult)data.getAnalysis(AnalysisType.ANOVA_TWO_WAY);
		} catch (Exception e) {
			//e.printStackTrace();
		}


		/***************************************************/
		String[] varList = null;
		int dfCTotal = 0, dfError = 0, dfModel = 0;
		double rssModel = 0, rssError = 0;
		double mssModel = 0, mssError = 0;
		double rssTotal = 0, fValue = 0;
		double pValue = 0, rSquare = 0;
		double[] residuals = new double[sampleSize];
		double[] predicted = new double[sampleSize];
		String rSquareString = null;
		String rssModelString=null , rssErrorString = null;
		String mssModelString=null, mssErrorString=null;
		String rssTotalString=null, fValueString=null;
		
		int[] dfGroup = null;
		double[] rssGroup = null, mseGroup = null;
		double[] fValueGroup = null;
		double[] pValueGroup = null;
		String[] rssGroupString = null, mseGroupString = null;
		String[] fValueGroupString = null;
		String[] pValueGroupString = null;
		 double[] sortedResiduals = null;
		 double[] sortedStandardizedResiduals = null;
		 int[] sortedResidualsIndex = null;
		 double[] sortedNormalQuantiles = null;
		 double[] sortedStandardizedNormalQuantiles = null;
		 double[][][] yValue = null;
		  int seriesCount = 0; // box plot row size
			 int categoryCount = 1; // box plot column size, which is 1 for Anova 1 way.
			 String[] seriesName = null;
			 String[] categoryName = null;
		
		if(result==null)
		{System.out.println("No result");
			return;}
		
		try {
			varList = result.getVariableList();//(ArrayList)(result.getTexture().get(AnovaTwoWayResult.VARIABLE_LIST));
		} catch (Exception e) {
			////System.out.println("varList Exception " + e);
			//showError("\nException  = " + e);
		}
		try {
			dfCTotal = result.getDFTotal();//((Integer)result.getTexture().get(AnovaTwoWayResult.DF_TOTAL)).intValue();
		} catch (Exception e) {
			////System.out.println("in gui dfCTotal e = " + e);
		}


		try {
			dfError = result.getDFError();//((Integer)result.getTexture().get(AnovaTwoWayResult.DF_ERROR)).intValue();
		} catch (Exception e) {
			////System.out.println("in gui dfError e = " + e);
		}


		try {

			dfModel= result.getDFModel();//((Integer)result.getTexture().get(AnovaTwoWayResult.DF_MODEL)).intValue();
		} catch (Exception e) {
			////System.out.println("in gui dfModel e = " + e);
		}


		try {
			rssModel= result.getRSSModel();//((String)result.getTexture().get(AnovaTwoWayResult.RSS_MODEL));
		} catch (Exception e) {
			////System.out.println("in gui rssModel e = " + e);
		}

		try {
			rssError = result.getRSSError();//((String)result.getTexture().get(AnovaTwoWayResult.RSS_ERROR));

		} catch (Exception e) {
			////System.out.println("in gui rssError e = " + e);
		}

		try {
			mssModelString = result.getFormattedDouble(result.getMSSModel());//((String)result.getTexture().get(AnovaTwoWayResult.MSS_MODEL));
		} catch (Exception e) {
			////System.out.println("in gui mssModel e = " + e);
		}

		try {
			mssError = result.getMSSError();//((String)result.getTexture().get(AnovaTwoWayResult.MSS_ERROR));
			////System.out.println("in gui mssError = " + mssError);
		} catch (Exception e) {
			//showError("\nException = " + e);
		}
 		try {
			rssTotal = result.getRSSTotal();//((String)result.getTexture().get(AnovaTwoWayResult.RSS_TOTAL));

		} catch (Exception e) {
			////System.out.println("in gui rssTotal e = " + e);
		}

		try {
			//((String)result.getTexture().get(AnovaTwoWayResult.F_VALUE));
			fValue= result.getFValue();
		} catch (Exception e) {
			////System.out.println("in gui fValue e = " + e);
		}
		
		try {
			//pValue = ((String)result.getTexture().get(AnovaTwoWayResult.P_VALUE));
			pValue = result.getPValue();
		} catch (Exception e) {
			System.out.println("in gui pValue e = " + e);
		}
		

		try {
			dfGroup = result.getDFGroup();//((int[])result.getTexture().get(AnovaTwoWayResult.DF_GROUP));

		} catch (Exception e) {
			////System.out.println("in gui dfGroup e = " + e);
		}

		try {
			rssGroup = result.getRSSGroup();//((String[])result.getTexture().get(AnovaTwoWayResult.RSS_GROUP));

		} catch (Exception e) {
			////System.out.println("in gui rssGroup e = " + e);
		}
		try {
			mseGroup = result.getMSEGroup();//((String[])result.getTexture().get(AnovaTwoWayResult.MSE_GROUP));

		} catch (Exception e) {
			////System.out.println("in gui mseGroup e = " + e);
		}

		try {
			fValueGroup= result.getFValueGroup();//((String[])result.getTexture().get(AnovaTwoWayResult.F_VALUE_GROUP));

		} catch (Exception e) {
			//showError("\nException = " + e);
		}

		try {
			pValueGroup = result.getPValueGroup();//((String[])result.getTexture().get(AnovaTwoWayResult.P_VALUE_GROUP));

		} catch (Exception e) {
			System.out.println("pValueGroup e = " + e);
		}

		try {
			predicted = result.getPredicted();//(double[])(result.getTexture().get(AnovaTwoWayResult.PREDICTED));
		} catch (Exception e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			residuals = result.getResiduals();//(double[])(result.getTexture().get(AnovaTwoWayResult.RESIDUALS));
		} catch (Exception e) {
			//showError("NullPointerException  = " + e);
		}

		//HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);

		try {
			sortedResiduals = result.getSortedResiduals();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_RESIDUALS);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedResiduals = result.getSortedStandardizedResiduals();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_STANDARDIZED_RESIDUALS);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedResidualsIndex = result.getSortedResidualsIndex();//(int[])residualMap.get(AnovaTwoWayResult.SORTED_RESIDUALS_INDEX);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedNormalQuantiles = result.getSortedNormalQuantiles();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_NORMAL_QUANTILES);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}
		try {
			sortedStandardizedNormalQuantiles = result.getSortedStandardizedNormalQuantiles();//(double[])residualMap.get(AnovaTwoWayResult.SORTED_STANDARDIZED_NORMAL_QUANTILES);
		} catch (Exception e) {
			//showError("\nNullPointerException  = " + e);
		}


		try {
			seriesCount = result.getBoxPlotSeriesCount();//((Integer)(result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE))).intValue();
			//Integer.parseInt((String)result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE));
		} catch (Exception e) {
			//showError("\nException = " + e);
			e.printStackTrace();
		}
		try {
			categoryCount = result.getBoxPlotCategoryCount();//((Integer)(result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_COLUMN_SIZE))).intValue();
		} catch (Exception e) {
			//showError("\nException = " + e);
			e.printStackTrace();
		}

		try {
			seriesName = result.getBoxPlotSeriesName();//((String[])result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_FACTOR_NAME));
			for (int i = 0; i < seriesName.length; i++) {
				//////System.out.println("------>seriesName = " + seriesName[i]);
				}

		} catch (Exception e) {
			//////System.out.println(" seriesName Exception " + e);
			//showError("\nException = " + e);
		}
		try {
			categoryName = result.getBoxPlotCategoryName();//((String[])result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_COLUMN_FACTOR_NAME));

			for (int i = 0; i < categoryName.length; i++) {
					//////System.out.println("------>categoryName = " + categoryName[i]);
				}
		} catch (Exception e) {
			////System.out.println("categoryName Exception " + e);
			//showError("\nException = " + e);
		}
		try {
			yValue = result.getBoxPlotResponseValue();//((double[][][])result.getTexture().get(AnovaTwoWayResult.BOX_PLOT_RESPONSE_VALUE));

		} catch (Exception e) {
			////System.out.println("yValue Exception " + e);
		}
		try {
			rSquare= result.getRSquare();//((String)result.getTexture().get(AnovaOneWayResult.P_VALUE));

		} catch (NullPointerException e) {
			System.out.println("rSquare Exception = " + e);
		}


		//System.out.println("\n\tIndependent Variable = " + independentHeader );
		//System.out.println("\n\tDependent Variable  = " + dependentHeader + " \n" );

		System.out.println("\n\tResults of Two-Way Analysis of Variance:");
		//System.out.println(+ dependentHeader + " \n" );
		if(header)
		{	
			System.out.println("\n\tDependent Variable :" +varHeader1);
			System.out.println("\n\tIndependent Variable 1: " +varHeader2);
			System.out.println("\n\tIndependent Variable 2: " +varHeader3);
		}	
		
		System.out.println("\n");
		System.out.println("\n\t*** Two-Way Analysis of Variance Results ***\n");
	
			
		for (int i = 0; i < varList.length; i++) {
			//System.out.println("\tDEGREES OF FREEDOM = "+ dfGroup[i] + "\n\tRSS = " + rssGroup[i] + "\n\tMSE = " + mseGroup[i] + "\n\tF-VALUE = " + fValueGroup[i]+"\n\tP-VALUE = " + pValueGroup[i]);

			//fDistribution = new FisherDistribution(dfModel, dfError);
			//fValue = (mseGroup[i]/mssError);
			//pValue = (1 - fDistribution.getCDF(fValue)) + "";

			//System.out.println("\n\tDegrees of Freedom = "+ dfGroup[i] + "\n\tResidual Sum of Squares = " + rssGroupString[i] + "\n\tMean Square Error = " + mseGroupString[i] + "\n\tF-Value = " + fValueGroupString[i] + "\n\tP-Value = " + AnalysisUtility.enhanceSmallNumber(pValueGroup[i]));
			System.out.println("\n\tDegrees of Freedom = "+ dfGroup[i]);
			System.out.println("\n\tResidual Sum of Squares = " + rssGroup[i]);
			System.out.println("\n\tMean Square Error = " + mseGroup[i]);
			System.out.println("\n\tF-Value = " + fValueGroup[i]);
			System.out.println("\n\tP-Value = " + pValueGroup[i]);
			System.out.println("\n");
		}

		System.out.println("\n\n\tResidual:");
		System.out.println("\tDegrees of Freedom = " + dfError);
		System.out.println("\n\tResidual Sum of Squares = " + rssError);
		System.out.println("\n\tMean Square Error = " + mssError);
		System.out.println("\n\tF-Value = " + fValue);
		System.out.println("\n\tP-Value = " + pValue);

		System.out.println("\n\n\tR-Square = " + rSquare);

		System.out.println("\n");
	}
}

