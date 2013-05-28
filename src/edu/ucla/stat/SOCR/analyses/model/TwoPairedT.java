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
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class TwoPairedT implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	private String type = "TwoPairedT";
	private HashMap<String,Object> resultMap = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		//////System.out.println("Analysis Type = " + analysisType);

		if (analysisType != AnalysisType.TWO_PAIRED_T)
			throw new WrongAnalysisException();
		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();
		//Column[] xColumn = new Column();
		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		//Column xColumn[] = new Column[];
		//////System.out.println("In linear");
		String keys = "";
		double x[] = null;
		double y[] = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				//////System.out.println(cls.getName());
			} catch (Exception e) {}
			Column xColumn = (Column) xMap.get(keys);
			//////System.out.println(xColumn.getDataType());
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			x = xColumn.getDoubleArray();
			for (int i = 0; i < x.length; i++) {
				//////System.out.println(x[i]);
			}
		}

		keySet = yMap.keySet();
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				//////System.out.println(cls.getName());
			} catch (Exception e) {}
		}
		Column yColumn = (Column) yMap.get(keys);
		//////System.out.println(yColumn.getDataType());
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
		}
		y = yColumn.getDoubleArray();
		for (int i = 0; i < y.length; i++) {
			//////System.out.println(y[i]);
		}
		//////System.out.println("In Linear Regression result = " + test(x, y));
		return test(x, y);
	}
	
	// test means t - test
	private TwoPairedTResult test(double[] x, double[] y) throws DataIsEmptyException {
		int sampleSizeX = x.length;
		int sampleSizeY = y.length;
		if ((sampleSizeX == 0) || (sampleSizeY == 0) || (sampleSizeX != sampleSizeY))
			throw new DataIsEmptyException();
		HashMap<String,Object> texture = new HashMap<String,Object>();
		TwoPairedTResult result = new TwoPairedTResult(texture);
		double meanX = AnalysisUtility.mean(x);
		double meanY = AnalysisUtility.mean(y);
		//System.out.println("X mean = " + meanX);
		//System.out.println("Y mean = " + meanY);
		double diff[] = new double[sampleSizeX];
		for (int i = 0; i < sampleSizeX; i++) {
			diff[i] = x[i] - y[i];
		}
		double meanDiff = AnalysisUtility.mean(diff);
		//System.out.println("meanDiff  = " + meanDiff);
		double sdDiff = Math.sqrt(AnalysisUtility.sampleVariance(diff));

		double tStat = (meanDiff * (Math.sqrt(sampleSizeX)))/ sdDiff;
		//tStat = Math.abs(tStat); // Difference, do not care about the sign.
		//meanDiff = Math.abs(meanDiff);
		//meanDiff = meanDiff;
		int df =sampleSizeX - 1;// degrees of freedom

		StudentDistribution tDistribution = new StudentDistribution(df);

		double pValueOneSided = (1 - tDistribution.getCDF(Math.abs(tStat)));
		double pValueTwoSided = 2 * pValueOneSided;
		//////System.out.println("TWO_PAIRED_T meanDiff = " + meanDiff);
		//////System.out.println("TWO_PAIRED_T sdDiff = " + sdDiff);
		//////System.out.println("TWO_PAIRED_T tStat= " + tStat);
		//////System.out.println("TWO_PAIRED_T "+Result.P_VALUE+" = " + pValue);

		texture.put(Result.MEAN_X, new Double(meanX));
		texture.put(Result.MEAN_Y, new Double(meanY));
		texture.put(Result.MEAN_DIFF, new Double(meanDiff));

		texture.put(Result.DF, new Integer(df));
		texture.put(Result.SAMPLE_VAR, new Double(sdDiff));
		texture.put(Result.T_STAT, new Double(tStat));
		texture.put(Result.P_VALUE_ONE_SIDED,  new Double(pValueOneSided));
		texture.put(Result.P_VALUE_TWO_SIDED,  new Double(pValueTwoSided));

		return result;
	}
}
