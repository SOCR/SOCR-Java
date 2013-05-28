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

import edu.ucla.stat.SOCR.distributions.NormalDistribution;
import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class PrincipalComponentAnalysis implements Analysis {
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	public final static String TEST_MEAN = "TEST_MEAN";
        public final static String TEST_VARIANCE = "TEST_VARIANCE";

	private String type = "PrincipalComponentAnalysis";
	private HashMap resultMap = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		//////System.out.println("Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.ONE_Z)
			throw new WrongAnalysisException();

		//HashMap xMap = data.getMapX();
		HashMap<String,Object> yMap = data.getMapY();

		TreeMap<String, Object> storage = data.getStorage();
		if (yMap == null)
			throw new WrongAnalysisException();
		//Column[] xColumn = new Column();
		Set<String> keySet = yMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		//Column xColumn[] = new Column[];
		//////System.out.println("In linear iterator.hasNext() = " + iterator.hasNext());
		String keys = "";
		//double y[] = null;
		double y[] = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			//////System.out.println("Analysis while keys = " + keys);


			try {
				Class cls = keys.getClass();
				//////System.out.println(cls.getName());
			} catch (Exception e) {}
			Column yColumn = (Column) yMap.get(keys);
			//////System.out.println(yColumn.getDataType());
			String yDataType = yColumn.getDataType();

			if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			//////System.out.println("Analysis Type yxColumn = " + yColumn);

			y = yColumn.getDoubleArray();
			//////System.out.println("Analysis Type y = " + y);

			for (int i = 0; i < y.length; i++) {
				//////System.out.println(y[i]);
			}
		}

		double testMean = Double.parseDouble((String)data.getParameter(analysisType, TEST_MEAN));
                

		//System.out.println("in model pack testMean = " + testMean);

		return regression(y, testMean);
	}

	private OneZResult regression(double[] input, double testMean) throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		OneZResult result = new OneZResult(texture);

		// do confidence interval for sample mean.

		int sampleSize = input.length;
		if (sampleSize <= 0)
			throw new DataIsEmptyException();

		double[] diff = new double[sampleSize];
		//if (testMean != 0) {
			for (int i = 0; i < sampleSize; i++) {
				diff[i] = input[i] - testMean;
			}
		//}

		double sampleMeanInput = AnalysisUtility.mean(input);
		double sampleMeanDiff = AnalysisUtility.mean(diff);     


		double sampleVarDiff=  AnalysisUtility.sampleVariance(diff); // sample variance

		double zStat = (sampleMeanInput-edu.ucla.stat.SOCR.analyses.gui.OneZ.testMean)/Math.sqrt(edu.ucla.stat.SOCR.analyses.gui.OneZ.testVariance/(sampleSize));
                
		NormalDistribution zDistribution = new NormalDistribution(edu.ucla.stat.SOCR.analyses.gui.OneZ.testMean, Math.sqrt(edu.ucla.stat.SOCR.analyses.gui.OneZ.testVariance));

                double pValueOneSided;
                
                if (zStat >= 0)
                    pValueOneSided = (1 - zDistribution.getCDF(Math.abs(zStat*Math.sqrt(edu.ucla.stat.SOCR.analyses.gui.OneZ.testVariance)+edu.ucla.stat.SOCR.analyses.gui.OneZ.testMean)));
                else
                    pValueOneSided = zDistribution.getCDF(Math.abs(zStat*Math.sqrt(edu.ucla.stat.SOCR.analyses.gui.OneZ.testVariance)+edu.ucla.stat.SOCR.analyses.gui.OneZ.testMean));
                
		double pValueTwoSided = 2 * pValueOneSided;

		texture.put(OneZResult.SAMPLE_MEAN_INPUT, new Double(sampleMeanInput));
		texture.put(OneZResult.SAMPLE_MEAN, new Double(sampleMeanDiff));
		texture.put(OneZResult.SAMPLE_VAR, new Double(sampleVarDiff));
		texture.put(OneZResult.SAMPLE_SIZE, new Double(sampleSize));
		texture.put(OneZResult.P_VALUE_ONE_SIDED, new Double(pValueOneSided));
		texture.put(OneZResult.P_VALUE_TWO_SIDED, new Double(pValueTwoSided));
		texture.put(OneZResult.Z_STAT, new Double(zStat));

		return result;

	}
}
