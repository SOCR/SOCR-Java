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
import java.util.*;

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataCase;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentFriedmanResult;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentWilcoxonResult;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
public class TwoIndependentFriedmanCSV {
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
		int independentLength=5;
		String length="5";//default nuumber of variabel is 5
		//String[] groupsnumber=new String[5];
		//
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
		
		
		//String varHeader1 =null;
		StringTokenizer st = null;
	
		String[] input = new String [independentLength];
		int xLength=0;
		String[] varHeader =new String [independentLength];
		
		ArrayList[] xList = new ArrayList [independentLength];
		double[][] xDataArray = new double[independentLength][xLength];
		
		for (int i = 0; i < xList.length; i++) 
			xList[i] = new ArrayList<String>();
		//ArrayList yList = new ArrayList();
		String line = null;
		boolean read=true;
		if (header)
			read=false;


		

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
		System.out.println("Sample size = " + xLength);
	/*	for (int i = 0; i < independentLength; i++) {
			for(int j=0;j<xLength;j++)
				System.out.println("Input case " + (i+1) + " : " + xList[i].get(j));
		}*/
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
		TwoIndependentFriedmanResult result;
		result = null;
		String className = null;


		try {
			//System.out.println("TwoIndependentFriedmanResult result start" );

			result = (TwoIndependentFriedmanResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_FRIEDMAN);
			////System.out.println("TwoIndependentFriedmanResult applet begin result = " + result);

		} catch (Exception e) {
			//System.out.println("TwoIndependentFriedmanResult applet exception = " + e);
		}
		if (result==null)
			return;
		

		String[] groupNames = null;


		double grandMean = 0, grandTotal = 0;
		double[] groupMean = null, groupTotal = null;
		int df = 0, sampleSize = 0, singleGroupSize = 0;
		double pValue = 0, sumSq = 0;
		double chiStat = 0;
		DataCase[][] rankArray = null;
		DataCase[][] dataArray = null;

		try {
			rankArray= result.getRankArray();
		} catch (Exception e) {
		}

		try {
			chiStat= result.getChiStat();
		} catch (Exception e) {
		}

		try {
			singleGroupSize= result.getSingleGroupSize();
		} catch (Exception e) {
		}

		try {
			groupNames= result.getGroupNameList();
		} catch (Exception e) {
		}

		try {
			//////System.out.println("result  = " + result);

			grandMean = result.getGrandMean();
		} catch (Exception e) {
			//////System.out.println("sampleSize Exception  = " + e);
		}
		try {

			sampleSize = result.getSampleSize();
		} catch (Exception e) {
			//////System.out.println("sampleSize Exception  = " + e);
		}		try {
			groupTotal = result.getGroupTotal();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		try {
			groupMean = result.getGroupMean();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}

		try {
			pValue = result.getPValue();
		} catch (NullPointerException e) {
			//showError("NullPointerException  = " + e);
		}
		//System.out.println("guipValue = " + pValue);

		try {
			sumSq = result.getSumSquares();
		} catch (NullPointerException e) {
		}

		try {
			df = result.getDF();
		} catch (NullPointerException e) {
		}
		try {
			dataArray = result.getDataArray();
		} catch (NullPointerException e) {
		}
		try {
			rankArray = result.getRankArray();
		} catch (NullPointerException e) {
		}

		System.out.println("\tResult of  Friedmen's Test:\n" );
		System.out.print("\tGroups Included = ");
		for (int i = independentLength-1; i >=0; i--) {
			
			System.out.print("  "  + varHeader[i]);// + " original index = " + varIndex );
		
		}
		
		int groupLength = groupNames.length;
		System.out.println("\n\n\tTotal Number of Cases = " + sampleSize);

		System.out.println("\n\tNumber of Groups = " + groupLength);
		System.out.println("\n\tGroup Size = " + singleGroupSize);

		System.out.println("\n\n\t");


		for (int i = 0; i <groupLength; i++) {
			System.out.print(" " + groupNames[i]+" \t");
		}
		String[] blockName = new String[singleGroupSize];

		int numberDigitKept = 2;
		//String avgTruncated = null; //AnalysisUtility.truncateDigits(expected, NumberDigitKept);

		for (int j = 0; j < singleGroupSize; j++ ) {

			blockName[j] = (j + 1) + "";
			System.out.print("\n "+blockName[j] + "\t");

			for (int i = 0; i <groupLength; i++) {
				System.out.print(dataArray[i][j].getValue() + " (");

				System.out.print(AnalysisUtility.truncateDigits(rankArray[i][j].getValue(), numberDigitKept) + ")\t");

				//System.out.println("\n\n\tAverage"+groupMean[i] +"\t");
			}
			//System.out.println("\n" );
		}
		System.out.println("\n\tAverage " );
		for (int i = 0; i <groupLength; i++) {
			System.out.print(AnalysisUtility.truncateDigits(groupMean[i], numberDigitKept) +"\t");
		}

		System.out.println("\n\n\n\tGrand Average = " + grandMean);
		System.out.println("\n\tDegrees of Freedom = " + df);
		System.out.println("\n\tSum of Squares = " + sumSq);
		System.out.println("\n\tChi-Square Statistics = " + chiStat);
		System.out.println("\n\tP-Value = " + pValue);

		System.out.println("\n" );

	}
}
