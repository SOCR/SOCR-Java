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
public class TwoPairSignedRankCSV {
	
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
		double meanX=0;
		double meanY=0;
		try { 	meanX = AnalysisUtility.mean(data2);
			meanY = AnalysisUtility.mean(data1);
		}
		catch (Exception e)
		{	//throw new DataIsEmptyException();
		}

		////System.out.println("X mean = " + meanX);
		////System.out.println("Y mean = " + meanY);
		double diff[] = new double[length1];

		int lengthCombo = 0;
		for (int i = 0; i < length1; i++) {
			diff[i] = data1[i] - data2[i];
			if (diff[i] != 0) {
				lengthCombo++;
			}
			////System.out.println("diff["+i+"] = " + diff[i]);
		}
		TwoPairedSignedRankResult result = null;
		double expW = 0, wStat = 0, varW = 0, zScore = 0;
		double pValueOneSided = 0, pValueTwoSided = 0;
		try {
			result = data.modelTwoPairedSignedRank(data2, data1);
		} catch (Exception e) {
			////System.out.println(e);
		}
		try {
			expW = result.getMeanW();
		} catch (NullPointerException e) {
			////System.out.println(e);
		}
		try {
			varW = result.getVarianceW();
		} catch (NullPointerException e) {
			////System.out.println(e);
		}
		try {
			wStat = result.getWStat();
		} catch (Exception e) {
			System.out.println("wStat Exception = "  + e);
		}
		try {
			zScore = result.getZScore();
		} catch (NullPointerException e) {
			////System.out.println(e);
		}
		try {
			pValueOneSided = result.getPValueOneSided();
		} catch (NullPointerException e) {
			////System.out.println(e);
		}
		try {
			pValueTwoSided = result.getPValueTwoSided();
		} catch (NullPointerException e) {
			////System.out.println(e);
		}
		if (result==null)
			return;
		
	
		
		System.out.println("\n"); //clear first
		if(header)
		{
			System.out.println("\n\tVariable 1 = " + varHeader1 + " \n" );
			System.out.println("\n\tVariable 2 = " + varHeader2 + " \n" );
		}
		System.out.println("\n\tResults of Two Paired Sample Wilcoxon Signed Rank Test:\n" );
		
		System.out.println("\n\tWilcoxon Signed-Rank Statistic = " +
				wStat+"\n");

		System.out.println("\n\tE(W+), Wilcoxon Signed-Rank Score = "+expW+"\n");
		System.out.println("\n\tVar(W+), Variance of Score = "+varW+"\n");
		System.out.println("\n\tWilcoxon Signed-Rank Z-Score = "+zScore+"\n");
		System.out.println("\n\tOne-Sided P-Value = "+pValueOneSided+"\n");
		System.out.println("\n\tTwo-Sided P-Value = "+pValueTwoSided+"\n");
			
	}
}

