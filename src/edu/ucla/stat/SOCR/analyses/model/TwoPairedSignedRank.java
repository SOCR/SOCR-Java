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
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

public class TwoPairedSignedRank implements Analysis {
	private final static String X_DATA_TYPE = DataType.QUANTITATIVE;
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;

	private String type = "TwoPairedSignedRank";
	private HashMap<String,Object> resultMap = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		//////System.out("Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.TWO_PAIRED_SIGNED_RANK)
			throw new WrongAnalysisException();
		HashMap<String,Object> xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();
		if (xMap == null || yMap == null)
			throw new WrongAnalysisException();
		//Column[] xColumn = new Column();
		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		String keys = "";
		double x[] = null;
		double y[] = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
			} catch (Exception e) {}
			Column xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			x = xColumn.getDoubleArray();
		}

		keySet = yMap.keySet();
		iterator = keySet.iterator();
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			try {
				Class cls = keys.getClass();
			} catch (Exception e) {}
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
		//////System.out.println("In Linear Regression result = " + test(x, y));
		return regression(x, y);
	}

	private TwoPairedSignedRankResult regression(double[] x, double[] y) 
			throws DataIsEmptyException {
		
		HashMap<String,Object> texture = new HashMap<String,Object>();
		TwoPairedSignedRankResult result = new TwoPairedSignedRankResult(texture);
		double[] diff = new double[x.length];
		
		double zScore=0, wStat=0, expW=0, varW=0;
		NormalDistribution nDistribution = new NormalDistribution();
		
		for (int i = 0; i < x.length; i++) {
			diff[i] = y[i] - x[i];
			////System.out.println("In model class diff["+i+"] = " + diff[i]);
		}

		//for (int i = 0; i < yLength; i++)
		//	y[i] = Double.parseDouble((String)yList.get(i));
		//for (int i = 0; i < xLength; i++)
		//	x[i] = Double.parseDouble((String)xList.get(i));

		//============================================
		int sampleSizeX = x.length;
		int sampleSizeY = y.length;
		if ((sampleSizeX == 0) || (sampleSizeY == 0) || (sampleSizeX != sampleSizeY))
		{	System.err.println("\n\nError - Sample-Sizes are NOT equal!\n\n");
			//resultPanelTextArea.append("Error - Sample-Sizes are NOT equal!\n");
		}
			//throw new DataIsEmptyException();

		double meanX=0;
		double meanY=0;
		try { 	meanX = AnalysisUtility.mean(x);
				meanY = AnalysisUtility.mean(y);
		} catch (Exception e) {
			//throw new DataIsEmptyException();
			System.err.println("Exception: "+e);
		}

		////System.out.println("X mean = " + meanX);
		////System.out.println("Y mean = " + meanY);
		//double diff[] = new double[sampleSizeX];

		int lengthCombo = 0;
		for (int i = 0; i < sampleSizeX; i++) {
			diff[i] = y[i] - x[i];

			if (diff[i] != 0) {
				lengthCombo++;
			}
			////System.out.println("diff["+i+"] = " + diff[i]);
		}

		////System.out.println("lengthCombo = " + lengthCombo);
		
		if (lengthCombo>0) {
			DataCase[] diffList = new DataCase[lengthCombo];
			DataCase[] diffListAbs = new DataCase[lengthCombo];
			boolean positive = true;
			boolean negative = false;

			int increment = 0;
			double tempValue = 0;
			for (int i = 0; i < diff.length; i++) {
				if (diff[i] != 0) {
					tempValue = diff[i];

					diffList[increment] = new DataCase(tempValue, null);
					diffListAbs[increment] = new DataCase(Math.abs(tempValue), null);

					if (diff[i] > 0) {
						diffListAbs[increment].setSign(positive);
					}
					else {
						diffListAbs[increment].setSign(negative);
					}

					////System.out.println("diffList["+increment+"] = " + diffListAbs[increment].getValue() + " isPositive = " + diffListAbs[increment].getSign());
					increment++;
				}
			}

			DataCase[] combo = QSortAlgorithm.rankList(diffListAbs);
			wStat = 0;
			int lenList = combo.length;
			for (int i = 0; i < lenList; i++) {
				////System.out.println("In model combo["+i+"] = " + combo[i].getValue());
				if ( combo[i].getSign() ) {
					//////System.out.println("add this to wStat: " +
					//	combo[i].getRank());
					wStat = wStat + combo[i].getRank();
					//////System.out.println("In model wStat = " + wStat);
				}
			}

			//System.out.println("lenList = " + lenList);
			expW = (double)lenList * ((double)lenList + 1) / 4; // page 414 Rice book.
			varW = (double)lenList * ((double)lenList + 1) * (2 * (double)lenList + 1) / 24;
			//System.out.println("expW = " + expW);
			//System.out.println("varW = " + varW);


			zScore = (wStat - expW) / Math.sqrt(varW);
		} else {	//if (lengthCombo<=0)
			wStat = 0;
			expW=0;
			varW=0;
		}
		
		double pValueOneSided = 0, pValueTwoSided = 0;
		if (zScore < 0) {
			pValueOneSided =  (nDistribution.getCDF(zScore));
		} else {
			pValueOneSided =  (1-nDistribution.getCDF(zScore));
		}

		if (pValueOneSided<0) 			pValueOneSided = 0;
		else if (pValueOneSided>0.5)	pValueOneSided = 0.5;
		
		pValueTwoSided = 2 * pValueOneSided;
		////System.out.println("In model class expW = " + expW);
		////System.out.println("In model class varW = " + varW);
		////System.out.println("In model class wStat = " + wStat);
		//System.out.println("In model class zScore = " + zScore);
		////System.out.println("In model class nDistribution.getCDF(zScore) = " + nDistribution.getCDF(zScore));

		//System.out.println("In model class pValue = " + pValue);

		texture.put(TwoPairedSignedRankResult.MEAN_W, new Double(expW));
		texture.put(TwoPairedSignedRankResult.VAR_W, new Double(varW));
		texture.put(TwoPairedSignedRankResult.W_STAT, new Double(wStat));

		texture.put(TwoPairedSignedRankResult.Z_SCORE, new Double(zScore));
		texture.put(TwoPairedSignedRankResult.P_VALUE_ONE_SIDED, new Double(pValueOneSided));
		texture.put(TwoPairedSignedRankResult.P_VALUE_TWO_SIDED, new Double(pValueTwoSided));

		return result;
	}
}
