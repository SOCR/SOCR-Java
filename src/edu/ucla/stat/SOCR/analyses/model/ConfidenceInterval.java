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
package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.util.IntervalType;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeMap;

import edu.ucla.stat.SOCR.distributions.FisherDistribution;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class ConfidenceInterval implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	
	public final static String  CI_CV_INDEX = "CT_INDEX";
	public final static String  CI_CHOICE = "CI_CHOICE";
	public final static String  CI_DATA = "CI_DATA";
	public final static String  CI_N_TRAILS = "CI_N_TRAILS";
	public final static String  CI_XY_LENGTH = "CI_XY_LENGTH";
	public final static String  CI_LEFT = "CI_LEFT";
	public final static String  CI_RIGHT = "CI_RIGHT";
	public final static String  CI_KNOWN_VARIANCE = "CI_KNOWN_VARIANCE";
	private String type = "CI";
	
	private static int cvIndex;
	private static double alpha;
	private static IntervalType ci_choice;
	private int xyLength;
	private int nTrails;
	private int[] sampleSizes;
	private double[][] sampleData;
	private String[] headings;
	private double left, right, knownVariance;
	
	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataException {
		Result result = null;
	
	//	System.out.println("Modelin analyze method: Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.CI)
			throw new WrongAnalysisException();

		cvIndex = Integer.parseInt((String)data.getParameter(analysisType, CI_CV_INDEX));
		
		ci_choice =  (IntervalType)data.getParameter(analysisType, CI_CHOICE);
	
		nTrails = Integer.parseInt((String)data.getParameter(analysisType, CI_N_TRAILS));

		xyLength = Integer.parseInt((String)data.getParameter(analysisType, CI_XY_LENGTH));
		
		left= Double.parseDouble((String)data.getParameter(analysisType, CI_LEFT));
		
		right= Double.parseDouble((String)data.getParameter(analysisType, CI_RIGHT));
		
		knownVariance = Double.parseDouble((String)data.getParameter(analysisType, CI_KNOWN_VARIANCE));
		
		//System.out.println("CI knownVariance ="+knownVariance);
		
		TreeMap<String,Object> xMap = data.getTreeX();
		if (xMap == null)
			throw new WrongAnalysisException();

		sampleData = new double[nTrails][xyLength];
		headings = new String[nTrails];
		sampleSizes = new int[nTrails];
	
		
	//	System.out.println("getting data: alpha ="+alpha+" xyLength="+xyLength+" ci_choice="+ci_choice.name());

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		ArrayList<String[]> x = new ArrayList<String[]>();
		int xIndex = 0;
		String[] xVector = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			headings[xIndex]= keys;
			try {
				Class cls = keys.getClass();
				//////////System.out.println("AnovaOneWay cls = " + cls.getName());
			} catch (Exception e) {}
			Column xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();
			//////////System.out.println("CI xColumn.getDataType = " + xColumn.getDataType());

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				//System.out.println("CI xDataType != DataType.QUANTITATIVE");
				throw new WrongAnalysisException("\nx data type MUST be " + X_DATA_TYPE + " but the input is of type " + xDataType);
			}

			xVector= xColumn.getStringArray();
			//System.out.println("xVector.length="+xVector.length);
			int j=0;
			for (int i = 0; i < xVector.length; i++) {
				if(xVector[i]!=null && xVector[i].length()!=0){
					sampleData[xIndex][j]=Double.parseDouble(xVector[i]);
					j++;
				}
			}
			sampleSizes[xIndex]=j;

			xIndex++;
		}
		
		return regression();

	}

	private ConfidenceIntervalResult regression() throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		ConfidenceIntervalResult result = new ConfidenceIntervalResult(texture);

		// do confidence interval for sample mean.
		double[][] ciData= new double[nTrails][xyLength];
		double[]xBar = new double[nTrails];
		
		for (int i=0; i<nTrails; i++){
		//	System.out.println("left ="+left);
			ci_choice.updateIntervalType(sampleSizes[i], cvIndex, sampleData[i]);
			ci_choice.setCutOffValue((Double)left, (Double)right);
			ci_choice.setKnownVarianceValue(knownVariance);
			ciData[i] = ci_choice.getConfidenceIntervals(); 
			xBar[i] = ci_choice.getX_bar();
		}

		texture.put(ConfidenceIntervalResult.CV_INDEX, cvIndex);
		texture.put(ConfidenceIntervalResult.CI_DATA, ciData);
		texture.put(ConfidenceIntervalResult.X_BAR, xBar);
		texture.put(ConfidenceIntervalResult.SAMPLE_DATA, sampleData);
		texture.put(ConfidenceIntervalResult.N_TRAILS, nTrails);
			
		return result;

	}
	public static void main(String[] args) {
		/*
		// dichotomous, use P + Q = 1.
		int multipleSize = 20;
		int portionP = (int) (multipleSize * Math.random()) + 1;
		int portionQ = (int) (multipleSize * Math.random()) + 1;

		int sampleSize = portionP + portionQ;

		double p = (double)portionP / (double)sampleSize;
		double q = (double)portionQ / (double)sampleSize;
		//////System.out.println("portionP = " + portionP);
		//////System.out.println("portionQ = " + portionQ);
		//////System.out.println("sampleSize = " + sampleSize);
		//////System.out.println("p = " + p);
		//////System.out.println("q = " + q);
		*/
		int sampleSize = 500;
		int portionP = 17;

		String[] x = new String[sampleSize];

		for (int i = 0; i < portionP; i++) {
			x[i] = "A";
		}

		for (int i = portionP; i < sampleSize; i++) {
			x[i] = "B";
		}


		//////System.out.println("samplesize = " + sampleSize);
		for (int i = 0; i < sampleSize; i++) {
			//////System.out.println("x["+i+"] = "+x[i]);
		}

		Data data = new Data();
		data.appendX("X", x, DataType.FACTOR);
		data.setParameter(AnalysisType.DICHOTOMOUS_PROPORTION, DichotomousProportion.SIGNIFICANCE_LEVEL, 0.05 + "");
		Result result = null;
		DichotomousProportion test = new DichotomousProportion();
		try {
			result = (DichotomousProportionResult) test.analyze(data, AnalysisType.DICHOTOMOUS_PROPORTION);
		} catch (Exception e) {
			//////System.out.println("e = " + e);
			//throw new Exception(e);
		}
	}
}
