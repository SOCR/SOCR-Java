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
import edu.ucla.stat.SOCR.analyses.data.DataCase;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentFriedmanResult;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentKruskalWallisResult;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

public class TwoIndependentKruskalWallisCSV {
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
		
		if (!filesLoaded) {
			return;
		}

		int independentLength=4;
		String length="4";
		boolean len=false;
		if (args.length>=2){
			if (args[1].equals("-h"))
				header=true;
			else
			{
				length=args[1];
				len=true;
			}
		
			if (args.length==3)
			{
				length=args[2];
				len=true;
			}
		}

		try
		{
			if (len)
				independentLength=(Double.valueOf((String)length)).intValue();
		}catch(Exception e){
		 
		}
		
		if (independentLength<=1)
		{
			System.out.println("Error! At least 2 groups.");
			return;
		}
		StringTokenizer st = null;
	
		String[] input = new String [independentLength];
		int xLength=0;
	
		ArrayList[] xList = new ArrayList [independentLength];
		double[][] xDataArray = new double[independentLength][xLength];
		
		for (int i = 0; i < xList.length; i++) 
			xList[i] = new ArrayList<String>();
		String line = null;
		boolean read=true;
		if (header)
			read=false;

		String[] varHeader =new String [independentLength];
		
		

		//System.out.println("\nData File 2: " + fileName2);
		
		
		//int xLength = yList.size();
		
		
	//	double[][] xDataArray = new double[independentLength][yLength];
		
		
		//System.out.println("\nyLength = " + yLength);
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName1));
			//StringBuffer sb = new StringBuffer();

			while ( (line = bReader.readLine()) != null) {
				//System.out.println(line);

				// only takes two rows and ignore the rest.
					st = new StringTokenizer(line, ",; \t" );
					try {
						
						for (int k=0;k<independentLength;k++)
						{
							input[k] = st.nextToken().trim();
							if (header&&!read)
								varHeader[k]=input[k];
							
							
							if (read&&!input[k].equalsIgnoreCase(MISSING_MARK) ) {
								xList[k].add(input[k]);
							}				
						}
						read=true;
					} catch (NoSuchElementException e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Friedman Test"));
						return;
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println(Utility.getErrorMessage("Friedman Test"));
						return;
					}


			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		xLength=xList[1].size();
		//System.out.println("Sample size = " + xLength);
	//	for (int i = 0; i < independentLength; i++) {
			//for(int j=0;j<xLength;j++)
			//	System.out.println("Input case " + (i+1) + " : " + xList[i].get(j));
		//}
		 // for each independent variable
			//resultPanelTextArea.append("\nindependentIndex(ordered)  = "  + varIndexList[index] + " \n" );
			////System.out.println("\nvarIndexList[index] = " +varIndexList[index]);
		double[] xData = null;
		if (!header)
		{
			for (int k=0;k<independentLength;k++)
				varHeader[k]="variable "+k;
		}
		Data data = new Data();
		for (int i = 0; i < independentLength; i++) {
			xData = new double[xLength];
			
			for(int j=0;j<xLength;j++)
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
			xDataArray[i]=xData;
			data.appendX(varHeader[i], xDataArray[i], DataType.QUANTITATIVE);
			
			
		}
		
	
		/***************************************************/
		TwoIndependentKruskalWallisResult result;
		result = null;
		String className = null;
		
		
		String[] groupNames = null;
		double[] rankSum = null;
		double tStat = 0, s2 = 0, cp =0;
		String dataAndRankString = null;
		String[] dataAndRankStringArray = null;

		int[] groupCount = null;
		String df = null;
		String[] multipleComparisonInfo = null;
		String multipleComparisonHeader = null;
		String sampleSize = null;
		try {
			////////////System.out.println("TwoIndependentKruskalWallisResult result start" );

			result = (TwoIndependentKruskalWallisResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS);
			//////////System.out.println("TwoIndependentKruskalWallisResult applet begin result = " + result);

		} catch (Exception e) {
			//////////System.out.println("TwoIndependentKruskalWallisResult applet exception = " + e);
		}


		/*******************************************************/
		//////////System.out.println("FINISH try result = " + result);
		// Retreive the data from Data Object using HashMap

	
		try {
			//System.out.println("result  = " + result);

			sampleSize = result.getSampleSize();
		} catch (Exception e) {
			//System.out.println("sampleSize Exception  = " + e);
		}
		try {
			groupNames = result.getGroupNameList();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			rankSum = result.getRankSumList();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			tStat = result.getTStat();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			cp = result.getCriticalValue();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}

		try {
			s2 = result.getSSqaured();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			df = result.getDegreesOfFreedom();
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}

		try {
			groupCount = result.getGroupCount();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			dataAndRankString = result.getDataRankInformation();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}

		try {
			dataAndRankStringArray = result.getDataRankSepratedInformation();
		} catch (NullPointerException e) {
			////System.out.println("dataAndRankStringArray NullPointerException  = " + e);
		}

		try {
			multipleComparisonInfo = result.getMultipleComparisonInformation();
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		try {
			multipleComparisonHeader = result.getMultipleComparisonHeader();
		} catch (NullPointerException e) {
			//showError("\nException = " + e);
		}
		if (result==null)
			return;
		
	
		System.out.println("\n"); //clear first
		System.out.println("\n\tNumber of Groups = "  + independentLength);
		
		double groupLength = groupNames.length;
		//System.out.println(dataAndRankString+"\n\n");
		System.out.println("\n\tTotal Number of Cases = " + sampleSize);


		for (int i = 0; i < dataAndRankStringArray.length; i++) {
			System.out.println("\n\n\tGroup = " + groupNames[i] + ": " + dataAndRankStringArray[i]);
		}
		System.out.println("\n\n");
		for (int i = 0; i <groupLength; i++) {
			System.out.println("\n\tGroup "+groupNames[i] + ":\tSample Size = "+ groupCount[i] + "\tRank Sum = " + rankSum[i]);


		}
		System.out.println("\n\n\tSignificance Level = 0.05");
		System.out.println("\n\tDegrees of Freedom = " + df);
		System.out.println("\n\tCritical Value = " + cp);
		System.out.println("\n\tT-Statistics = " + tStat);
		System.out.println("\n\tS * S = " + s2);
		System.out.println("\n\n\tNotation: Ri -- Rank of group i; ni -- size of group i.\n");

		System.out.println("\n\t\t\t" + multipleComparisonHeader + "\n");
		for (int i = 0; i <multipleComparisonInfo.length; i++) {
			if (multipleComparisonInfo[i] != null)
				System.out.println("\n\t"+multipleComparisonInfo[i] );


		}
		System.out.println("\n" );


	}
}
