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

import java.text.DecimalFormat;
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

public class KolmogorovSmirnoffCSV {
	private static final String MISSING_MARK = ".";

	public static void main(String[] args) {
		//FileHelper fileHelper1 = new FileHelper();
		//fileHelper.openReader("data1.txt");
		String fileName1 = null;//"C:\\STAT\\SOCR\\test\\FK.txt";
		boolean header=false;
		boolean filesLoaded = false;

		System.out.println(
		"Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");

		try {
			fileName1 = args[0];

			filesLoaded = true;
		} catch (Exception e) {
			//System.out.println("\nType in two file names like this:");
			//System.out.println("java edu.ucla.stat.SOCR.analyses.command.AnovaOneWayCSV fileName1.txt fileName2.txt\n");

			e.printStackTrace();
		}
		if (args.length>=2){
			if (args[1].equals("-h"))
				header=true;		
		}
		if (!filesLoaded) {
			return;
		}
	
		String[] varHeaders = null;
		
		ArrayList<ArrayList<String>>lists = new ArrayList<ArrayList<String>>();
		
		
		String line = null;


		StringTokenizer st = null;
	
		int sampleSize = 0;
		
		int columnCount=0;
		
		try {
			BufferedReader bReader  = new BufferedReader(new FileReader(fileName1));
			//StringBuffer sb = new StringBuffer();
	
			if (header)
			{
				line = bReader.readLine();
				st = new StringTokenizer(line, ",; \t");
			
				columnCount = st.countTokens();
			//	System.out.println("columnCount= " + columnCount);
				varHeaders = new String[columnCount];
				
				for (int i=0; i<columnCount; i++)
					varHeaders[i]=st.nextToken().trim();
			}
			
			while ( (line = bReader.readLine()) != null) {
			//	System.out.println(line);

					st = new StringTokenizer(line, ",; \t");
					columnCount = st.countTokens();
					
					ArrayList<String> list = new ArrayList<String>();
					
					try {
						
						while( st.hasMoreTokens()){
							
							String input = st.nextToken().trim();
							if (!input.equalsIgnoreCase(MISSING_MARK)) {
								list.add(input);
							}
						}
						sampleSize++;	
					}  catch (Exception e) {
					
						e.printStackTrace();
						//System.out.println(Utility.getErrorMessage("One Way ANOVA"));
						return;
					}
					lists.add(list);

			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		System.out.println("Sample size = " + sampleSize);
		/*for (int i = 0; i < sampleSize; i++) {
			System.out.print("Input case " + (i+1) + " : ");
			for (int j=0 ;j<columnCount; j++)
				System.out.print(lists.get(i).get(j) +",");
			System.out.println();
		}*/

		Data data = new Data();
		double[] xData = new double[sampleSize];

		for (int i=0; i<columnCount; i++){
			xData = new double[sampleSize];
			for (int j = 0; j < sampleSize; j++) {
				try {
						xData[j] = (Double.valueOf((String)(lists.get(j)).get(i))).doubleValue();
				} catch (NumberFormatException e) {
					System.out.println("Line " + (i+1) + " is not in correct numerical format.");
				return;
				}
			}
			
			if(i==0)
				data.appendY("Y", xData, DataType.QUANTITATIVE);
			if(i==1)
				data.appendX("X", xData, DataType.QUANTITATIVE);
		}

	
		KolmogorovSmirnoffResult result = null;

		
		try {
			result = (KolmogorovSmirnoffResult)data.getAnalysis(AnalysisType.KOLMOGOROV_SMIRNOFF);
		} catch (Exception e) {
			e.printStackTrace();
		}


		/***************************************************/
		/*******************************************************/
		
		
		
		// Retreive the data from Data Object using HashMap
		double dStat = -1;
		double[] diff = null;
		double[] absDiff = null;

		//double[] sortedX1 = null, sortedX2 = null, y1 = null, y2 = null;
		//double[] logX1 = null, logX2 = null;

		double mean1 = 0, mean2 = 0, sd1 = 0, sd2 = 0, median1 = 0, median2 = 0;
		double q1x1 = 0, q3x1 = 0, q1x2 = 0, q3x2 = 0;

		double[] ci95X1 = null, ci95X2 = null;
		String lowerOutlier1 = null, lowerOutlier2 = null, upperOutlier1 = null, upperOutlier2 = null;
	
		double[] sortedX1 = null, sortedX2 = null, y1 = null, y2 = null;
		
		double[] logX1 = null, logX2 = null;
		
		DecimalFormat dFormat = new DecimalFormat("#.00000");
		result.setDecimalFormat(dFormat);

		try {
			sortedX1 = result.getVariable1X();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}


		try {
			sortedX2 = result.getVariable2X();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}

		try {
			y1 = result.getVariable1Y();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}

		try {
			y2 = result.getVariable2Y();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}


		try {
			logX1 = result.getVariable1LogX();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}

		try {
			logX2 = result.getVariable2LogX();
		} catch (Exception e) {
			//System.out.println("Excepion " +e + "");
		}
		try {
			diff = result.getDiff();
		} catch (Exception e) {
			//System.out.println("diff Excepion " +e + "");
		}
		try {
			absDiff = result.getAbsDiff();
		} catch (Exception e) {
			//System.out.println("absDiff Excepion " +e + "");
		}


		try {
			dStat = result.getDStat();
		} catch (Exception e) {
			//System.out.println("dStat Excepion " +e + "");
		}
		////System.out.println("gui dstat= " + dStat);

		try {
			mean1 = result.getMean1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			mean2 = result.getMean2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			sd1 = result.getSD1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_X)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			sd2 = result.getSD2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}


		try {
			median1 = result.getMedian1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			median2 = result.getMedian2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			q1x1 = result.getFirstQuartile1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			q1x2 = result.getFirstQuartile2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}

		try {
			q3x1 = result.getThirdQuartile1();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}
		try {
			q3x2 = result.getThirdQuartile2();//((Double)result.getTexture().get(TwoIndependentTResult.MEAN_Y)).doubleValue();
		} catch (Exception e) {
			//showError("Excepion2 " +e + "");
		}


		try {
			ci95X1 = result.getCI95X1();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}

		try {
			ci95X2 = result.getCI95X2();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			lowerOutlier1 = result.getBoxLowerOutlier1();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			lowerOutlier2 = result.getBoxLowerOutlier2();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			upperOutlier1 = result.getBoxUpperOutlier1();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			upperOutlier2 = result.getBoxUpperOutlier2();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		
		String zFormula = null;
		double z = 0, prob = 0;
		try {
			zFormula = result.getZFormula();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}

		try {
			z = result.getZStat();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		try {
			prob = result.getProb();
		} catch (Exception e) {
			//System.out.println(" Excepion " +e + "");
		}
		

		
		/**************************************************************/
		System.out.println("\n\tVariable 1 = " + varHeaders[0]);

		System.out.println("\n\tVariable 2 = " + varHeaders[1]);

		System.out.println("\n\n\tKolmogorov-Smirnoff Test Result:\n" );

		System.out.println("\n\tVariable 1 = " + varHeaders[0]);
		System.out.println("\n\tSample Size = " + sampleSize);
		System.out.println("\n\tSample Mean = " + result.getFormattedDouble(mean1));
		System.out.println("\n\tSample SD = " + result.getFormattedDouble(sd1));
		System.out.println("\n\t95% CI of the mean = (" + result.getFormattedDouble(ci95X1[0]) + ", " + result.getFormattedDouble(ci95X1[1]) +
		")");
		System.out.println("\n\tFirst Quartile = " + result.getFormattedDouble(q1x1));
		System.out.println("\n\tMedian = " + result.getFormattedDouble(median1));
		System.out.println("\n\tThird Quartile = " + result.getFormattedDouble(q1x1));
		System.out.println("\n\tBox plot outliers below Q1 - 1.5IQR: " + lowerOutlier1);
		System.out.println("\n\tBox plot outliers above Q3 - 1.5IQR: " + upperOutlier1);

		System.out.println("\n\n\tVariable 2 = " + varHeaders[1]);
		System.out.println("\n\tSample Size = " + sampleSize);
		System.out.println("\n\tSample Mean = " + result.getFormattedDouble(mean2));
		System.out.println("\n\tSample SD = " + result.getFormattedDouble(sd2));
		System.out.println("\n\t95% CI of the mean = (" + result.getFormattedDouble(ci95X2[0]) + ", " + result.getFormattedDouble(ci95X2[1]) +
		")");
		System.out.println("\n\tFirst Quartile = " + result.getFormattedDouble(q1x2));
		System.out.println("\n\tMedian = " + result.getFormattedDouble(median2));
		System.out.println("\n\tThird Quartile = " + result.getFormattedDouble(q1x2));


		System.out.println("\n\tBox plot outliers below Q1 - 1.5IQR: " + lowerOutlier2);
		System.out.println("\n\tBox plot outliers above Q3 - 1.5IQR: " + upperOutlier2);


		if (dStat != -1) {
			System.out.println("\n\n\tKolmogorov-Smirnoff Test:");
			System.out.println("\n\tD-Statistics = " + result.getFormattedDouble(dStat));
			System.out.println("\n\t" + zFormula + "\tz = " + result.getFormattedDouble(z));
			System.out.println("\n\tCDF(" + result.getFormattedDouble(dStat) + ") = " + result.getFormattedDouble(prob));
			System.out.println("\n\t1 - CDF(" + result.getFormattedDouble(dStat) + ") = " + result.getFormattedDouble((1-prob)));
		}

	}
}
