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
/* annieche 200608.
One-sided normal with known variance power computation
*/

package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.example.*;

public class DichotomousProportion implements Analysis {
	private final static String X_DATA_TYPE = DataType.FACTOR;
	public final static String SIGNIFICANCE_LEVEL = "SIGNIFICANCE_LEVEL";
	private static int outcomeCount;
	private String type = "DichotomousProportion";

	private static double alpha;
	private static double z;


	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataException {
		Result result = null;
		////System.out.println("Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.DICHOTOMOUS_PROPORTION)
			throw new WrongAnalysisException();

		HashMap<String,Object> xMap = data.getMapX();
		//HashMap yMap = data.getMapY();
		alpha = Double.parseDouble((String)data.getParameter(analysisType, SIGNIFICANCE_LEVEL));

		////System.out.println("alpha = " + alpha);
		////System.out.println("xMap = " + xMap);

		if (xMap == null)
			throw new WrongAnalysisException();

		Set<String> keySet = xMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		String keys = "";
		ArrayList<String[]> x = new ArrayList<String[]>();
		//double y[] = null;
		int xIndex = 0;
		String[] xVector = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			////System.out.println("AnovaOneWay keys = "+keys);
			try {
				Class cls = keys.getClass();
				//////////System.out.println("AnovaOneWay cls = " + cls.getName());
			} catch (Exception e) {}
			Column xColumn = (Column) xMap.get(keys);
			String xDataType = xColumn.getDataType();
			//////////System.out.println("AnovaOneWay xColumn.getDataType = " + xColumn.getDataType());

			if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
				//ystem.out.println("AnovaOneWay xDataType != DataType.FACTOR");
				throw new WrongAnalysisException("\nx data type MUST be " + X_DATA_TYPE + " but the input is of type " + xDataType);
			}

			xVector= xColumn.getStringArray();

			x.add(xIndex, xVector);
			xIndex++;
		}
		outcomeCount = xIndex;
		int sampleSize = xVector.length;
		////System.out.println("sampleSize = " + sampleSize);

		String[] valueString = new String[2]; // because it's dichotomous

		HashSet<String> xSet = new HashSet<String>();

		for (int i = 0; i < xVector.length; i++) {
			xSet.add(xVector[i]);
		}

		////System.out.println("xSet.size() = " + xSet.size() );
		int groupSize = xSet.size();
		if (groupSize == 0 || groupSize > 2) {
			throw new DataException("Data have more than 2 catagories.");
		}
		if (groupSize == 1) {
			throw new DataException("Data have only 1 catagories.");
		}


		Iterator<String> xIterator = xSet.iterator();
		xMap = new HashMap<String,Object>();
		int j = 0;
		//////System.out.println("j = " + j);
		while (xIterator.hasNext()) {
			valueString[j] = (String)xIterator.next();
			j++;
		}
		//////System.out.println("j = " + j);

		////System.out.println("valueString.lenght = " + valueString.length);

		for (int i = 0; i < 2; i++) {
			//////System.out.println("valueString[j] = " + valueString[i]);

		}
		////System.out.println("valueString = " + valueString);

		return regression(xVector, valueString);
	}

	private DichotomousProportionResult regression(String[] input, String[] valueList) throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		DichotomousProportionResult result = new DichotomousProportionResult(texture);

		// do confidence interval for sample mean.

		int sampleSize = input.length;
		int portionP = 0, portionQ = 0;
		for (int i = 0; i < sampleSize; i++) {
			if (input[i].equalsIgnoreCase(valueList[0])) { // p
				portionP++;
			} else if (input[i].equalsIgnoreCase(valueList[1])) { // q
				portionQ++;
			}
		}

		int[] sampleProportion = new int[] {portionP, portionQ};
		////System.out.println("portionP = " + portionP);
		////System.out.println("portionQ = " + portionQ);
		////System.out.println("alpha = " + this.alpha);

		z = AnalysisUtility.getNormalCriticalPoint(alpha);
		////System.out.println("z = " + z);

		double sampleProportionP = (double)portionP / (double)sampleSize;
		double sampleProportionQ = (double)portionQ / (double)sampleSize;

		double adjustedProportionP = ((double)portionP + 0.5 * z * z) / ((double) sampleSize + z * z);
		double adjustedProportionQ = ((double)portionQ + 0.5 * z * z) / ((double) sampleSize + z * z);

		double sampleSEP = Math.sqrt(sampleProportionP * sampleProportionQ / (double)sampleSize);
		double sampleSEQ = sampleSEP;

		double adjustedSEP = Math.sqrt(adjustedProportionP * adjustedProportionQ / ((double)sampleSize + z * z));
		double adjustedSEQ = adjustedSEP;

		double ciWidth =  z * adjustedSEP;
		double upperP = adjustedProportionP + ciWidth;
		double lowerP = adjustedProportionP - ciWidth;
		double upperQ = adjustedProportionQ + ciWidth;
		double lowerQ = adjustedProportionQ - ciWidth;

		double ci = (100 - 100 * alpha);
		//System.out.println("ci = " + ci);
		String ciString = ci + "";
		int indexDot = ciString.indexOf(".");
		//System.out.println("ciString.substring(indexDot, indexDot+1) = " + ciString.substring(indexDot, indexDot+1));
		if (ciString.substring(indexDot+1, indexDot+2).equals("0")) {
			ciString = ciString.substring(0, indexDot);
		}
		//System.out.println("ciString = " + ciString);

		String ciTextP = ciString + "% CI = " + adjustedProportionP + " +/- " + ciWidth + "\n\t= (" + lowerP + ", " + upperP + ")";
		String ciTextQ = ciString + "% CI = " + adjustedProportionQ + " +/- " + ciWidth + "\n\t= (" + lowerQ + ", " + upperQ + ")";


		////System.out.println("sampleProportionP = " + sampleProportionP);
		////System.out.println("sampleProportionQ = " + sampleProportionQ);

		////System.out.println("adjustedProportionP = " + adjustedProportionP);
		////System.out.println("adjustedProportionQ = " + adjustedProportionQ);

		////System.out.println("sampleSEP = " + sampleSEP);
		////System.out.println("sampleSEQ = " + sampleSEQ);

		////System.out.println("adjustedSEP = " + adjustedSEP);
		////System.out.println("adjustedSEQ = " + adjustedSEQ);

		////System.out.println("ciTextP = " + ciTextP);
		////System.out.println("ciTextQ = " + ciTextQ);



		texture.put(DichotomousProportionResult.SAMPLE_PROPORTION_P, new Double(sampleProportionP));
		texture.put(DichotomousProportionResult.SAMPLE_PROPORTION_Q, new Double(sampleProportionQ));
		texture.put(DichotomousProportionResult.ADJUSTED_PROPORTION_P, new Double(adjustedProportionP));
		texture.put(DichotomousProportionResult.ADJUSTED_PROPORTION_Q, new Double(adjustedProportionQ));


		texture.put(DichotomousProportionResult.SAMPLE_SE_P, new Double(sampleSEP));
		texture.put(DichotomousProportionResult.SAMPLE_SE_Q, new Double(sampleSEQ));
		texture.put(DichotomousProportionResult.ADJUSTED_SE_P, new Double(adjustedSEP));
		texture.put(DichotomousProportionResult.ADJUSTED_SE_Q, new Double(adjustedSEQ));

		texture.put(DichotomousProportionResult.CI_WIDTH, new Double(ciWidth));
		texture.put(DichotomousProportionResult.LOWER_P, new Double(lowerP));
		texture.put(DichotomousProportionResult.UPPER_P, new Double(upperP));
		texture.put(DichotomousProportionResult.LOWER_Q, new Double(lowerQ));
		texture.put(DichotomousProportionResult.UPPER_Q, new Double(upperQ));
		texture.put(DichotomousProportionResult.CI_STRING, new String(ciString));
		
		texture.put(DichotomousProportionResult.CI_TEXT_P, new String(ciTextP));

		texture.put(DichotomousProportionResult.CI_TEXT_Q, new String(ciTextQ));
		texture.put(DichotomousProportionResult.VALUE_LIST, valueList);

		texture.put(DichotomousProportionResult.SAMPLE_PROPORTION, sampleProportion);
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
