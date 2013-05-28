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

public class TwoIndependentT implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;

	private String type = "TwoIndependentT";

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		////System.out.println("Analysis Type = " + analysisType);

		if (analysisType != AnalysisType.TWO_INDEPENDENT_T)
			throw new WrongAnalysisException();
		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		double x[] = null;
		double y[] = null;
		while (iterator.hasNext()) { //once.
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				////System.out.println(cls.getName());
			} catch (Exception e) {}
			Column xColumn = (Column) xMap.get(keys);
			////System.out.println(xColumn.getDataType());
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}

			x = xColumn.getDoubleArray();
			for (int i = 0; i < x.length; i++) {
				////System.out.println(x[i]);
			}
		}

		keySet = yMap.keySet();
		iterator = keySet.iterator();
		
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
				////System.out.println(cls.getName());
			} catch (Exception e) {}

			//////System.out.println(xColumn.getDataType());
		}
		Column yColumn = (Column) yMap.get(keys);
		////System.out.println(yColumn.getDataType());
		String yDataType = yColumn.getDataType();

		if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
			throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
		}

		y = yColumn.getDoubleArray();
		for (int i = 0; i < y.length; i++) {
			////System.out.println(y[i]);
		}
		////System.out.println("In Linear Regression result = " + test(x, y));
		return test(x, y);
	}

	// test means t - test
	private TwoIndependentTResult test(double[] x, double[] y) throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		TwoIndependentTResult result = new TwoIndependentTResult(texture);
		int sampleSizeX = x.length;
		int sampleSizeY = y.length;
		double meanX = AnalysisUtility.mean(x);
		double meanY = AnalysisUtility.mean(y);


		double sampleVarX=  AnalysisUtility.sampleVariance(x);
		double sampleVarY=  AnalysisUtility.sampleVariance(y);

		double sdX = Math.sqrt(sampleVarX);
		double sdY = Math.sqrt(sampleVarY);
		double termX = sampleVarX / sampleSizeX;
		double termY = sampleVarY / sampleSizeY;

		//tStat = Math.abs(tStat);

		////System.out.println("--------------tStat = " + tStat);
		double dfNumerator = ((termX + termY) * (termX +termY));
		double dfDenomenator =  ( ((termX*termX)/(sampleSizeX - 1)) +((termY*termY)/(sampleSizeY - 1)));
		double dfAdjusted = (dfNumerator/dfDenomenator); // that complicated formula
		int df = (int) (sampleSizeX + sampleSizeY - 2);
		double pooledSampleVar = ( (sampleSizeX - 1) * sampleVarX + (sampleSizeY - 1) * sampleVarY ) / df;
		double pooledSampleSD = Math.sqrt(pooledSampleVar);
		double n = sampleSizeX;
		double m = sampleSizeY;
		double tStatPooled = (meanX - meanY) / (pooledSampleSD * (Math.sqrt(1/n + 1/m)));

		// I got my formula of unpooled from http://www.uvm.edu/~dhowell/gradstat/psych340/Lectures/t-tests/Class15.html

		double unpooledSampleVar = sampleVarX / sampleSizeX  +  sampleVarY / sampleSizeY;

		double unpooledSampleSE = Math.sqrt(unpooledSampleVar);
		double tStatUnpooled = (meanX - meanY) / unpooledSampleSE;

		StudentDistribution tDistribution = new StudentDistribution(df);

		double pValueOneSidedPooled = (1 - tDistribution.getCDF(Math.abs(tStatPooled)));
		double pValueTwoSidedPooled = 2 * (1 - tDistribution.getCDF(Math.abs(tStatPooled)));

		double pValueOneSidedUnpooled = (1 - tDistribution.getCDF(Math.abs(tStatUnpooled)));
		double pValueTwoSidedUnpooled = 2 * (1 - tDistribution.getCDF(Math.abs(tStatUnpooled)));

		texture.put((String)TwoIndependentTResult.MEAN_X, new Double(meanX));
		texture.put((String)TwoIndependentTResult.MEAN_Y, new Double(meanY));

		texture.put((String)TwoIndependentTResult.SAMPLE_VAR_X, new Double(sampleVarX));
		texture.put((String)TwoIndependentTResult.SAMPLE_VAR_Y, new Double(sampleVarY));

		texture.put((String)TwoIndependentTResult.SAMPLE_SD_X, new Double(sdX));
		texture.put((String)TwoIndependentTResult.SAMPLE_SD_Y, new Double(sdY));

		texture.put((String)TwoIndependentTResult.POOLED_SAMPLE_VAR, new Double(pooledSampleVar));
		texture.put((String)TwoIndependentTResult.POOLED_SAMPLE_SD, new Double(pooledSampleSD));

		texture.put((String)TwoIndependentTResult.T_STAT_POOLED, new Double(tStatPooled));
		texture.put((String)TwoIndependentTResult.T_STAT_UNPOOLED, new Double(tStatUnpooled));

		texture.put((String)Result.DF, new Integer(df));
		texture.put((String)TwoIndependentTResult.DF_ADJUSTED, new Double(dfAdjusted));
		texture.put((String)TwoIndependentTResult.P_VALUE_ONE_SIDED_POOLED, new Double(pValueOneSidedPooled));
		texture.put((String)TwoIndependentTResult.P_VALUE_TWO_SIDED_POOLED, new Double(pValueTwoSidedPooled));

		texture.put((String)TwoIndependentTResult.P_VALUE_ONE_SIDED_UNPOOLED, new Double(pValueOneSidedUnpooled));
		texture.put((String)TwoIndependentTResult.P_VALUE_TWO_SIDED_UNPOOLED, new Double(pValueTwoSidedUnpooled));

		return result;
	}
}
