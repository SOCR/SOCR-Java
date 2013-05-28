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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.ChiSquareContingencyTableResult;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentKruskalWallisResult;
import edu.ucla.stat.SOCR.distributions.*;

public class ChiSquareContingencyTableCSV {
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
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.TwoIndependentText fileName1.txt fileName2.txt\n");
		}
		String row="2", col="2", alpha_string="0.05";
		
	
		
		if (!filesLoaded) {
			return;
		}
		if (args.length==2)
		{
		if (args[1].equals("-h"))
			header=true;
		}
	
		if (args.length==3)
		{
			if (args[1].equals("-a"))
				alpha_string=args[2];
			else
			{
				row=args[1];
				col=args[2];
			}
		}
		if (args.length==4)
		{
			if (args[2].equals("-a"))
				alpha_string=args[3];
			else
			{
				row=args[2];
				col=args[3];
			}
		}
		if (args.length==5)
		{
			if (args[2].equals("-a"))
				alpha_string=args[3];

			row=args[3];
			col=args[4];
			
		}
	
	
	int rowNumber=2;
	int colNumber=2;
	rowNumber=(Double.valueOf((String)row)).intValue();
	colNumber=(Double.valueOf((String)col)).intValue();
	
	if (rowNumber<=1||colNumber<=1)
	{
		System.out.println("Error! Row number and column number are at least 2.");
		return;
	}
	
		 
		StringTokenizer st = null;
		String[] input = new String [rowNumber];
	
		ArrayList[] xList = new ArrayList [rowNumber];
		double[][] observed = new double[rowNumber][colNumber];
		double[][] temp = new double[colNumber][rowNumber];
		
		
		String[] rowNames = new String[rowNumber];
		String[] colNames = new String[colNumber];
		
		String[] rowNamesData = new String[rowNumber];
		String[] colNamesData = new String[colNumber];

		for (int i = 0; i < xList.length; i++) 
			xList[i] = new ArrayList<String>();
		//ArrayList yList = new ArrayList();
		String line = null;
		
		//System.out.println("\nData File 2: " + fileName2);
		//int colNumber = yList.size();
		//	double[][] observed = new double[rowNumber][yLength];
		//System.out.println("\nyLength = " + yLength);
		
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName1));
			//StringBuffer sb = new StringBuffer();

			while ( (line = bReader.readLine()) != null) {
				//System.out.println(line);

				// only takes two rows and ignore the rest.
					st = new StringTokenizer(line, ",; \t" );
					try {
						
						for (int k=0;k<rowNumber;k++)
						{
							input[k] = st.nextToken().trim();
					
						if (!input[k].equalsIgnoreCase(MISSING_MARK) ) {
						xList[k].add(input[k]);
						}	
						}
						
					} catch (NoSuchElementException e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Chi Square Contingency Table"));
						return;
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Chi Square Contingency Table"));
						return;
					}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		colNumber=xList[1].size();
		//System.out.println("Sample size = " + colNumber);
	//	for (int i = 0; i < rowNumber; i++) {
			//for(int j=0;j<colNumber;j++)
			//	System.out.println("Input case " + (i+1) + " : " + xList[i].get(j));
		//}
		 // for each independent variable
			//resultPanelTextArea.append("\nindependentIndex(ordered)  = "  + varIndexList[index] + " \n" );
			////System.out.println("\nvarIndexList[index] = " +varIndexList[index]);
		double[] xData = null;
		String rowMessage = "Enter number of rows. Default is 2.";
		String colMessage = "Enter number of columns. Default is 2.";

		String msgWarning = "You didn't enter a number. Default 2 will be used. \nClick on CALCULATE if you'd like to change it.";
		
	/*	String tempHeader[]=new String[rowNumber];
		tempHeader[0]="A";
		tempHeader[1]="B";
		tempHeader[2]="C";
		tempHeader[3]="D";*/
		//tempHeader[4]="E";
		
		
		
		double alpha = 0.05;

		////System.out.println("in gui alpha = alph" + alpha);

		try {
			alpha=(Double.valueOf((String)alpha_string)).doubleValue();
			
			} catch (Exception e) {
			
			////////////////System.out.println("in gui use random data Exception alpha = " + alpha);
		}
		
		
		Data data = new Data();
		for (int i = 0; i < rowNumber; i++) {
			xData = new double[colNumber];
			
			for(int j=0;j<colNumber;j++)
			{
			try {
				xData[j] = (Double.valueOf((String)xList[i].get(j))).doubleValue();
				//resultPanelTextArea.append("  X = "+xList.get(i) );
				//System.out.println("i = " + i + " j = " + j + " X = " +xList[i].get(j));
				} catch (NumberFormatException e) {
					System.out.println("Line " + (j+1) + " is not in correct numerical format.");
					return;
				}
	
			}
			temp[i]=xData;
			
			
		}
		if (!header)
		{
			for (int i = 0; i < rowNumber; i++) {
				rowNames[i]="R"+i;
			}
			for (int i = 0; i < colNumber; i++) {
				colNames[i]="C"+i;
			}
		}
		for (int i = 0; i < rowNumber; i++) {
			for(int j=0;j<colNumber;j++)
			{
				observed[i][j]=temp[j][i];
			}
		}
		data.setParameter(AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.SIGNIFICANCE_LEVEL, alpha + "");
		data.setInput(AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.OBSERVED_DATA, observed);
		data.setInput(AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.ROW_NAMES, rowNamesData);
		data.setInput(AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE, edu.ucla.stat.SOCR.analyses.model.ChiSquareContingencyTable.COL_NAMES, colNamesData);

		/***************************************************/
		ChiSquareContingencyTableResult result = null;
		try {
			////////////System.out.println("TwoIndependentKruskalWallisResult result start" );

			result = (ChiSquareContingencyTableResult)data.getAnalysis(AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE);
			//////////System.out.println("TwoIndependentKruskalWallisResult applet begin result = " + result);

		} catch (Exception e) {
			//////////System.out.println("TwoIndependentKruskalWallisResult applet exception = " + e);
		}
		if (result==null)
			return;
		/*******************************************************/
		//////////System.out.println("FINISH try result = " + result);
		// Retreive the data from Data Object using HashMap

	
		double[][] expected = null;
		int df = 0;
		int[] rowSum = null;
		int[] colSum = null;
		int grandTotal = 0;
		double chiStat = 0;

		try {
			chiStat = result.getPearsonChiSquareStat();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}
		try {
			df = result.getDF();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}
		try {
			grandTotal = result.getGrandTotal();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}

		try {
			expected = result.getExpectedData();
		} catch (Exception e) {
			////////////////System.out.println("Exception = " + e);
		}

		try {
			rowSum = result.getRowSum();
		} catch (Exception e) {
			//////System.out.println("Exception = " + e);
		}
		try {
			colSum = result.getColSum();
		} catch (Exception e) {
			////////////////System.out.println("Exception = " + e);
		}
	//	int NumberDigitKept = 2;
		//String[][] expectedTruncated = AnalysisUtility.truncateDigits(expected, NumberDigitKept);
		//String[][] expectedTruncated = result.getFormattedGroupArray(expected);

		System.out.println("\tResults of Chi-Square Test for Independent or Homogeneity\n" );
		//System.out.println("\n\tVariable  = " + dependentHeader + " \n" );
		//System.out.println("\n\tGroup " + valueList[0] + ": \tFrequency = " + sampleProportion[0]);

		System.out.println("\n\tNumber of Rows = " + rowNumber);
		System.out.println("\n\tNumber of Columns = " + colNumber);


		System.out.println("\n\n\n\t");

		for (int j = 0; j < colNumber; j++) {
			System.out.println("\t" + colNames[j]);
		}

		System.out.println("\tRow Total");
		System.out.println("\n\t--------------------------------");

		for (int i = 0; i < rowNumber; i++) {

			System.out.println("\n\t" + rowNames[i]);
			for (int j = 0; j < colNumber; j++) {

				System.out.print("\t" + observed[i][j]);
				System.out.print("  (" + expected[i][j] + ")");
			}
			System.out.println("\t" + rowSum[i]);

			System.out.println("\n");
			}

		System.out.println("\n\t--------------------------------");
		System.out.println("\n\tCol Total");

		for (int j = 0; j < colNumber; j++) {
			System.out.println("\t" + colSum[j]);
		}

		System.out.println("\t" + grandTotal + "\n");
		System.out.println("\n\n\tDegrees of Freedom = " + df);

		System.out.println("\n\n\tPearson Chi-Square Statistics = " + chiStat);
		double pValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(df)).getCDF(chiStat);
		System.out.println("\n\n\tP-Value = " + pValue);
	}
}
