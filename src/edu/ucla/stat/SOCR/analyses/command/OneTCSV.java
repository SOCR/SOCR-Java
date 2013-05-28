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
import java.io.BufferedReader;
import java.io.FileReader;


import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.OneTResult;

public class OneTCSV {
	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		boolean filesLoaded = false;
		boolean header=false;
		boolean any_test_mean=false;
		String str_test_mean=null; 
		
		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");

		try {
			fileName1 = args[0];

			filesLoaded = true;
		} catch (Exception e) {
			//System.out.println("\nType in two file names like this:");
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.TwoIndependentText fileName1.txt fileName2.txt\n");


		}
		if (args.length>=2){
			if (args[1].equals("-h"))
				header=true;		
			else 
			{
				str_test_mean=args[1];
				any_test_mean=true;
			}
			
			if(args.length==3)
			{
				str_test_mean=args[2];
				any_test_mean=true;
			}
		}
		
		String varHeader1 =null;
		
		if (!filesLoaded) {
			return;
		}
		ArrayList<String> list1 = new ArrayList<String>();
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

	
	
		int length1 = list1.size();
		
		double[] data1 = new double[length1];
	

		//System.out.println("\nlength1 = " + length1);

	

		for (int i = 0; i < length1; i++) {
			try {
				data1[i] = (Double.valueOf((String)list1.get(i))).doubleValue();
			} catch (NumberFormatException e) {
				System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
			}
		}
		double testMean = 0; 
		
		try
		{
			if (any_test_mean)
				testMean=(Double.valueOf((String)str_test_mean)).doubleValue();
		}catch(Exception e){
		 
		}
		
			
			
		Data data = new Data();
		
		/*****************  Y is 1 *****************/
		data.appendY("Y", data1, DataType.QUANTITATIVE);
		data.setParameter(AnalysisType.ONE_T, edu.ucla.stat.SOCR.analyses.model.OneT.TEST_MEAN, testMean + "");

		/***************************************************/

		OneTResult result = null;
		try {
			result = (OneTResult)data.getAnalysis(AnalysisType.ONE_T);
		} catch (Exception e) {
	////////System.out.println(e);
			
		}
		int df = 0;
		double sampleMeanInput = 0, sampleMeanDiff = 0, sampleVar = 0;
		double tStat = 0, pValueOneSided = 0, pValueTwoSided = 0;
		double t_stat=0, p_value=0;
	
		

		try {
			sampleMeanInput = result.getSampleMeanInput();//((Double)result.getTexture().get(OneTResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}


		try {
			sampleMeanDiff = result.getSampleMean();//((Double)result.getTexture().get(OneTResult.SAMPLE_MEAN)).doubleValue();
		} catch (NullPointerException e) {
			//////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			sampleVar = result.getSampleVariance();//((Double)result.getTexture().get(OneTResult.SAMPLE_VAR)).doubleValue();
		} catch (NullPointerException e) {
			//////////System.out.println("NullPointerException = " + e);

		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			df = result.getDF();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (NullPointerException e) {
			//////////System.out.println("NullPointerException = " + e);
		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			tStat = result.getTStat();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			pValueOneSided = result.getPValueOneSided();//((Integer)result.getTexture().get(OneTResult.DF)).intValue();
		} catch (Exception e) {
			//////////System.out.println("Exception = " + e);
		}
		try {
			pValueTwoSided = result.getPValueTwoSided();//((String)result.getTexture().get(TwoPairedTResult.P_VALUE));
		} catch (Exception e) {
			////System.out.println("pValueTwoSided e = " + e);
		}
		
		System.out.println("\n"); //clear first
		System.out.println("\n\tSample size = " + length1 + " \n" );
		System.out.println("\n\tVariable name: "+varHeader1);
		
		
		System.out.println("\n\tTest against " + testMean + " \n" );


		System.out.println("\n\tResult of One Sample T-Test:\n" );


		System.out.println("\n\tSample Mean of Difference        = " + sampleMeanDiff);
		System.out.println("\n\n\tSample Variance     = " + sampleVar);

		System.out.println("\n\tStandard Error     = " + Math.sqrt(sampleVar / length1));
		System.out.println("\n\tDegrees of Freedom  = " + df);
		
		System.out.println("\n\tT-Statistics             = " + tStat);
		System.out.println("\n\tOne-Sided P-Value = " + pValueOneSided);
		System.out.println("\n\tTwo-Sided P-Value = " + pValueTwoSided);
	
		}
	

	}

