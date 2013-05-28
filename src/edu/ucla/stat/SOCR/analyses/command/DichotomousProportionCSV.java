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

import javax.swing.JOptionPane;

import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.model.*;

import edu.ucla.stat.SOCR.core.Distribution;
import edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException;
import edu.ucla.stat.SOCR.analyses.command.Utility;
import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.analyses.example.DichotomousProportionExamples;

public class DichotomousProportionCSV {

	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		boolean filesLoaded = false;
		boolean header=false;
		boolean any_alpha=false;
		String str_alpha=null; 	
		
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
				str_alpha=args[1];
				any_alpha=true;
			}
			
			if(args.length==3)
			{
				str_alpha=args[2];
				any_alpha=true;
			}
		}
		
		String varHeader1 =null;
		if (!filesLoaded) {
			return;
		}
	//	String varHeader1 =null;
	
	
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

		
		if (!header)
		{
			varHeader1="Variable 1";
		}
		
		int length1 = list1.size();
		double alpha = 0.05;

		
		double sampleProportionP = 0, sampleProportionQ = 0;
		double adjustedProportionP = 0, adjustedProportionQ = 0;
		double sampleSEP = 0, sampleSEQ = 0;
		double adjustedSEP = 0, adjustedSEQ = 0;
		String ciTextPString = null, ciTextQString = null;
		String[] valueList = null;
		int[] sampleProportion = null;
		
		String[] data1 = new String[length1];
	
	
		//System.out.println("\nlength1 = " + length1);

		//System.out.println("length2 = " + length2);


		for (int i = 0; i < length1; i++) {
			try {
				data1[i] = (String)list1.get(i);
				
			}catch (NumberFormatException e) {
				System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
		}
		}
		

		Data data = new Data();
		
		
		try {
			if(any_alpha)
				alpha = (Double.valueOf((String)str_alpha)).doubleValue();
			//alpha=0.05;
		} catch (Exception e) {
			alpha = 0.05;
			////////////System.out.println("in gui use random data Exception alpha = " + alpha);
		}
		data.appendX("X", data1, DataType.FACTOR);
		
		data.setParameter(AnalysisType.DICHOTOMOUS_PROPORTION, edu.ucla.stat.SOCR.analyses.model.DichotomousProportion.SIGNIFICANCE_LEVEL, alpha + "");

		DichotomousProportionResult result=null;
		try {
			result = (DichotomousProportionResult)data.getAnalysis(AnalysisType.DICHOTOMOUS_PROPORTION);
		} catch (Exception e) {
			//System.out.println(e);
		}
		try {
			valueList = (String[])result.getValueList();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		try {
			sampleProportion = (int[])result.getSampleProportion();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			sampleProportionP = result.getSampleProportionP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			////////////System.out.println("Exception = " + e);
		}

		try {
			sampleProportionQ = result.getSampleProportionQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		try {
			adjustedProportionP = result.getAdjustedProportionP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			////////////System.out.println("Exception = " + e);
		}

		try {
			adjustedProportionQ = result.getAdjustedProportionQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		try {
			sampleSEP = result.getSampleSEP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			////////////System.out.println("Exception = " + e);
		}
		try {
			sampleSEQ = result.getSampleSEQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			adjustedSEP = result.getAdjustedSEP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			adjustedSEQ = result.getAdjustedSEQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}



		try {
			ciTextPString = result.getCITextP();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}

		try {
			ciTextQString = result.getCITextQ();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		
		
		if (result==null)
			return;
		
		
		System.out.println("\n");//clear first
		System.out.println("\tSample size = " + length1 + " \n" );
		System.out.println("\n\tVariable  = " + varHeader1 + " \n" );
		System.out.println("\n\tGroup " + valueList[0] + ": \tFrequency = " + sampleProportion[0]);

		System.out.println("\n\tGroup " + valueList[1] + ": \tFrequency = " + sampleProportion[1]);

		System.out.println("\n\n\tResults of Dichotomous Proportion Test:\n" );
		System.out.println("\n\tSignificance Level = " + alpha);



		System.out.println("\n\n\t********** Without Adjustment **********"  );
		System.out.println("\n\tGroup " + valueList[0] + ": \n\tProportion = " + sampleProportionP + "\n\tStandard Error = " + sampleSEP);

		System.out.println("\n\n\tGroup " + valueList[1] + ": \n\tProportion = " + sampleProportionQ + "\n\tStandard Error = " + sampleSEQ);
		
		//String ciString = null;
	//	try {
		//	ciTextPString = result.getCiString();//((Double)result.getTexture().get(DichotomousProportionResult.SAMPLE_MEAN)).doubleValue();
		//} catch (Exception e) {
			//System.out.println("Exception = " + e);
	//	}
		
		double ciWidth=0,lowerP=0, upperP=0, lowerQ=0, upperQ=0;

		try {
			 ciWidth= result.getCiWidth();
			 lowerP= result.getLowerP();
			 upperP= result.getUpperP();
			 lowerQ= result.getLowerQ();
			 upperQ= result.getUpperQ();
		} catch (Exception e) {
			//System.out.println("Exception = " + e);
		}
		
		System.out.println("\n\n\t********** With Adjustment **********"  );
		System.out.println("\n\tGroup " + valueList[0] + ": \n\tProportion = " +
				adjustedProportionP + "\n\tStandard Error = " +adjustedSEP);
		//System.out.println("\n\t" + ciTextPString);
		System.out.println("\n\t % CI = " + adjustedProportionP + " +/- " + ciWidth + "\n\t= (" + lowerP + ", " + upperP + ")");

		System.out.println("\n\n\tGroup " + valueList[1] + ": \n\tProportion = " + adjustedProportionQ + "\n\tStandard Error = " + adjustedSEQ);
		//System.out.println("\n\t" + ciTextQString);
		System.out.println("\n\t % CI = " + adjustedProportionQ + " +/- " + ciWidth + "\n\t= (" + lowerQ + ", " + upperQ + ")");


		
	
	}
	
}
