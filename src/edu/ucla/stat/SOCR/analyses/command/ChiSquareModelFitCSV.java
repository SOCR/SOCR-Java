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

public class ChiSquareModelFitCSV {
	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\dataOne.txt\\"
		String fileName2 = null;//"C:\\STAT\\SOCR\\test\\dataTwo.txt\\";
		boolean filesLoaded = false;
		boolean header=false;
		boolean any_parameter=false;
		String str_parameter=null; 
		
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
			else 
			{
				str_parameter=args[2];
				any_parameter=true;
			}
			
			if(args.length==4)
			{
				str_parameter=args[3];
				any_parameter=true;
			}
		}
		
		String varHeader1 =null;
		String varHeader2 =null;
		
		if (!filesLoaded) {
			return;
		}
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

		double chiSquareStat = 0, pValue = 0;
		double O_E=0;
	
		int numberParameters = 0; // for example, Poisson takes one, normal takes two. etc.
		try
		{
			if (any_parameter)
				numberParameters=(Double.valueOf((String)str_parameter)).intValue();
		}catch(Exception e){
		 
		}
		
		
		int df =0;
		if (numberParameters >= length2 - 2) {
			System.out.println( "Too many parameters. Default zero will be used.");
			numberParameters = 0;
			
		}
		df= length2  - numberParameters - 1;
		for (int i = 0; i < length2; i++) {
			O_E = data1[i] - data2[i];
			if (data2[i]<=0) chiSquareStat += (O_E * O_E)/(0.1);
			else chiSquareStat += (O_E * O_E)/data2[i]; // x is expected (bottom panel in mapping panel)
			//resultPanelTextArea.append("\n | i = " + i + " | X = "+x[i] + " | Y = "+y[i]);
		}

		pValue = 1- (new edu.ucla.stat.SOCR.distributions.ChiSquareDistribution(
				df)).getCDF(chiSquareStat);
	
		

		System.out.println("\n");//clear first
		System.out.println("\n\tObserved Data = " + varHeader1 + " \n" );
		System.out.println("\n\tExpected Data = " + varHeader2 + " \n" );

		
		System.out.println("\n\tChi-Square Goodness of Fit Results:\n" );
		
		System.out.println("\n\tTotal Counts = " + length2 + " \n" );
		System.out.println("\n\tNumber of Parameters = " + numberParameters + " \n" );
		System.out.println("\n\tChi-Square Goodness of Fit Results:\n" );
		
		String newln = System.getProperty("line.separator");
		String chiSquareOutput = new String("\n\n\t********** Chi-Square Statistic is: "+
				chiSquareStat + " *********\n");
		chiSquareOutput += "\n\n\t********** Chi-Square Degrees of Freedom is: "+
				length2 + " - " + numberParameters + " - 1 = " + df + " *********\n";
		chiSquareOutput += "\n\n\t********** Chi-Square p-value is: "+
		pValue + " *********\n";

		System.out.println(chiSquareOutput);
	
	}
}
