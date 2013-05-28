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

public class TwoIndependentTest {
	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		String fileName2 = null;//"C:\\STAT\\SOCR\\test\\dataTwo.txt\\";
		boolean filesLoaded = false;
		boolean header=false;
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");
		
		try {
			fileName1 = args[0];
			fileName2 = args[1];
			filesLoaded = true;
		} catch (Exception e) {
			//System.out.println("\nType in two file names like this:");
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.TwoIndependentTest fileName1.txt fileName2.txt\n");


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
		TwoIndependentTResult result = null;
		try {
			result = (TwoIndependentTResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_T);
		} catch (Exception e) {
		}


		/***************************************************/

		int df = 0;
		double dfAdjusted = 0;
		double meanX = 0, meanY = 0, varX = 0, varY = 0, sdX = 0, sdY = 0;
		double poolSampleVar = 0, poolSampleSD = 0;
		double tStatPooled = 0;
		double pValueOneSidedPooled = 0, pValueTwoSidedPooled = 0;
		try {
			df = result.getDF();
		} catch (Exception e) {
		}
		try {
			dfAdjusted = result.getDFAdjusted();
		} catch (Exception e) {

		}

		try {
			meanX = result.getMeanX();
		} catch (Exception e) {

		}
		try {
			meanY = result.getMeanY();
		} catch (Exception e) {

		}
		try {
			varX = result.getSampleVarianceX();
		} catch (Exception e) {

		}
		try {
			varY = result.getSampleVarianceY();
		} catch (Exception e) {

		}
		try {
			sdX = result.getgetSampleSDX();
		} catch (Exception e) {

		}
		try {
			sdY = result.getgetSampleSDY();
		} catch (Exception e) {
		}

		/********************** POOLED **********************/

		try {
			poolSampleVar = result.getPoolSampleVariance();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");

		}

		try {
			poolSampleSD = result.getPoolSampleSD();
		} catch (Exception e) {

		}
		try {
			tStatPooled = result.getTStatPooled();
		} catch (Exception e) {

		}
		try {
			pValueOneSidedPooled = result.getPValueOneSidedPooled();
		} catch (Exception e) {

		}
		try {
			pValueTwoSidedPooled = result.getPValueTwoSidedPooled();
		} catch (Exception e) {

		}

		System.out.println("\n\n\t*************POOLED***********************" );
		System.out.println("\n\n\tResult of Two Independent Sample T-Test:\n" );

		System.out.println("\n\tVariable 1 = " + varHeader1);
		System.out.println("\n\tSample Size = " + data1.length );
		System.out.println("\n\tSample Mean = " + meanY);
		System.out.println("\n\tSample Variance = " + varY);
		System.out.println("\n\tSample SD = " + sdY);

		System.out.println("\n\n\tVariable 2 = " + varHeader2);
		System.out.println("\n\tSample Size = " + data2.length);
		System.out.println("\n\tSample Mean = " + meanX);
		System.out.println("\n\tSample Variance = " + varX);
		System.out.println("\n\tSample SD = " + sdX);

		System.out.println("\n\n\tDegrees of Freedom = " + df);
		System.out.println("\n\tPooled Sample Variance = " + poolSampleVar);
		System.out.println("\n\tPooled Sample SD = " + poolSampleSD);

		System.out.println("\n\tT-Statistics = " + tStatPooled);
		System.out.println("\n\tOne-Sided P-Value = " + AnalysisUtility.enhanceSmallNumber(pValueOneSidedPooled));
		System.out.println("\n\tTwo-Sided P-Value = " + AnalysisUtility.enhanceSmallNumber(pValueTwoSidedPooled));


	
		/********************** UNPOOLED **********************/
		double tStatUnpooled = 0;
		double pValueOneSidedUnpooled = 0, pValueTwoSidedUnpooled = 0;

		try {

			tStatUnpooled = result.getTStatUnpooled();
			////System.out.println("tStatUnpooled = " + tStatUnpooled);
		} catch (Exception e) {
			//System.out.println("tStatUnpooled Exception = " + e);
		}
		try {
			pValueOneSidedUnpooled = result.getPValueOneSidedUnpooled();
		} catch (Exception e) {
			//System.out.println(e);

		}
		try {
			pValueTwoSidedUnpooled = result.getPValueTwoSidedUnpooled();
		} catch (Exception e) {
			//System.out.println(e);

		}


		System.out.println("\n\n\t************UNPOOLED***********************" );
		
		System.out.println("\n\tT-Statistics  = " + tStatUnpooled);
		System.out.println("\n\tOne-Sided P-Value  = " + pValueOneSidedUnpooled);
		System.out.println("\n\tTwo-Sided P-Value  = " + pValueTwoSidedUnpooled);

	}
}
