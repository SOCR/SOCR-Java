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

public class TwoPairedTCSV {
	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		String fileName2 = null;//"C:\\STAT\\SOCR\\test\\dataTwo.txt\\";
		boolean header=false;
		boolean filesLoaded = false;
		
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
		String varHeader1 =null;
		String varHeader2 =null;
		
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
		if (length1 != length2)
		{   	////System.out.println("Unequal sample-sizes are not " +
			//	"allowed for paired tests, xLength = "+ xLength +
			//	"; yLength = " + yLength+"!");
			System.out.println("Unequal sample-sizes are not "+
				"allowed for paired tests, xLength = "+length2 +
				"; yLength = " + length1+"!");
			return;
		 }
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
		data.appendX("X", data2, DataType.QUANTITATIVE);
		data.appendY("Y", data1, DataType.QUANTITATIVE);
	
		/***************************************************/

		int df = 0;
		double meanX = 0, meanY = 0, meanDiff = 0;
		double sampleVar = 0;
		double tStat = 0;
		double pValueOneSided = 0, pValueTwoSided = 0;
		TwoPairedTResult result = null;

		try {
			result = (TwoPairedTResult)data.getAnalysis(AnalysisType.TWO_PAIRED_T);
		} catch (Exception e) {
		}
		
		try {
			df = result.getDF();//((Integer)result.getTexture().get(TwoPairedTResult.DF)).intValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanX = result.getMeanX();//((Double)result.getTexture().get(TwoPairedTResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanY = result.getMeanY();//((Double)result.getTexture().get(TwoPairedTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			meanDiff = result.getMeanDifference();//((Double)result.getTexture().get(TwoPairedTResult.MEAN_DIFF)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			sampleVar = result.getSampleVariance();//((Double)result.getTexture().get(TwoPairedTResult.SAMPLE_VAR)).doubleValue();
		} catch (Exception e) {
			//
		}
		try {
			tStat = result.getTStat();//((Double)result.getTexture().get(TwoPairedTResult.T_STAT)).doubleValue();
		} catch (Exception e) {
			//
		}
		try {
			pValueOneSided = result.getPValueOneSided();//((String)result.getTexture().get(TwoPairedTResult.P_VALUE));
		} catch (Exception e) {
			//
		}
		try {
			pValueTwoSided = result.getPValueTwoSided();//((String)result.getTexture().get(TwoPairedTResult.P_VALUE));
		} catch (Exception e) {
			//System.out.println("pValueTwoSided e = " + e);
		}
	if(header)
	{	
		System.out.println("Variable 1:" +varHeader1);
		System.out.println("Variable 2:" +varHeader2);
	}	
	System.out.println("\n\tSample size = " + length1 );

	System.out.println("\n\n\tResults of Two Paired Sample T-Test:\n" );

	if(header)
	{
		System.out.println("\n\tLet Difference = "+varHeader1+" - "+varHeader2);
		System.out.println("\n\n\tMean of "+varHeader1+" = " + meanY);
		System.out.println("\n\tMean of "+varHeader2+" = " + meanX);
	}	
	else
	{
		System.out.println("\n\tLet Difference = Variable1 - Variable2");
		System.out.println("\n\n\tMean of Variable 1 = " + meanY);
		System.out.println("\n\tMean of Variable 2 = " + meanX);
	}
	System.out.println("\n\tMean of Difference  = " + meanDiff);


	System.out.println("\n\tVariance of Difference  = " + sampleVar);
	System.out.println("\n\tStandard Error of Difference  = " + sampleVar / Math.sqrt(length1));


	System.out.println("\n\tDegrees of Freedom = " + df);

	System.out.println("\n\tT-Statistics             = " + tStat);
	System.out.println("\n\tOne-Sided P-Value = " + pValueOneSided);
	System.out.println("\n\tTwo-Sided P-Value = " + pValueTwoSided);

	
	}
}
