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

public class ChiSquareContingencyTable implements Analysis {
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;
	public final static String OBSERVED_DATA = "OBSERVED_DATA";
	public final static String SIGNIFICANCE_LEVEL = "SIGNIFICANCE_LEVEL";
	public final static String ROW_NAMES = "ROW_NAMES";
	public final static String COL_NAMES = "COL_NAMES";
	private String[] rowNames;
	private String[] colNames;
	private String type = "ChiSquareContingencyTable";
	private HashMap resultMap = null;

	public String getAnalysisType() {
		return type;
	}
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		//System.out.println("ChiSquareContingencyTable Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.CHI_SQUARE_CONTINGENCY_TABLE)
			throw new WrongAnalysisException();

		//HashMap xMap = data.getMapX();
/*
		HashMap yMap = data.getMapY();

		TreeMap storage = data.getStorage();
		if (yMap == null)
			throw new WrongAnalysisException();
		//Column[] xColumn = new Column();
		Set keySet = yMap.keySet();
		Iterator iterator = keySet.iterator();
		//Column xColumn[] = new Column[];
		////////System.out.println("In linear iterator.hasNext() = " + iterator.hasNext());
		String keys = "";
		//double y[] = null;
		double y[] = null;
		while (iterator.hasNext()) {
			keys = (String) iterator.next();
			////////System.out.println("Analysis while keys = " + keys);


			try {
				Class cls = keys.getClass();
				////////System.out.println(cls.getName());
			} catch (Exception e) {}
			Column yColumn = (Column) yMap.get(keys);
			////////System.out.println(yColumn.getDataType());
			String yDataType = yColumn.getDataType();

			if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
				throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
			}
			////////System.out.println("Analysis Type yxColumn = " + yColumn);

			y = yColumn.getDoubleArray();
			////////System.out.println("Analysis Type y = " + y);

			for (int i = 0; i < y.length; i++) {
				////////System.out.println(y[i]);
			}
		}
*/
		double alpha = Double.parseDouble((String)data.getParameter(analysisType, SIGNIFICANCE_LEVEL));
		double[][] observed = ((double[][])data.getInput(analysisType, OBSERVED_DATA));

		rowNames = ((String[])data.getInput(analysisType, ROW_NAMES));
		colNames = ((String[])data.getInput(analysisType, COL_NAMES));
		//System.out.println("in model pack alpha = " + alpha);
		//System.out.println("in model pack observed = " + observed);
		//System.out.println("in model pack rowNames = " + colNames);
		//System.out.println("in model pack colNames = " + colNames);
		try {
			for (int i = 0; i < rowNames.length; i++) {
				//System.out.println("in model pack rowNames = " + rowNames[i]);
			}

			for (int i = 0; i < colNames.length; i++) {
				//System.out.println("in model pack colNames = " + colNames[i]);
			}
		} catch (Exception e) {
		}


		return getResult(observed, alpha);
	}

	private ChiSquareContingencyTableResult getResult(double[][] observed, double alpha) throws DataIsEmptyException {
		HashMap<String,Object> texture = new HashMap<String,Object>();
		ChiSquareContingencyTableResult result = new ChiSquareContingencyTableResult(texture);


		ContingencyTable ct = new ContingencyTable(observed);

		try {
			//ct.setExpectedProbabilities(rowP, colP);
			ct.setExpectedProbabilities();
		} catch (Exception e) {
			//System.out.println("setExpectedProbabilities e = " + e);
		}

		double pearsonChiSquare = ct.findPearsonChiSquare();
		int df = ct.getDF();
		int grandTotal = ct.getGrandTotal();

		ct.getRowNames();
		ct.getColNames();
		ct.getRowSumObserved();
		ct.getColSumObserved();
		ct.getGrandTotal();

		double[][] expected = ct.getExpected();

		int[] rowSum = ct.getRowSum();
		int[] colSum = ct.getColSum();

		texture.put(ChiSquareContingencyTableResult.EXPECTED_DATA, expected);
		texture.put(ChiSquareContingencyTableResult.ROW_NAMES, rowNames);
		texture.put(ChiSquareContingencyTableResult.COL_NAMES, colNames);
		texture.put(ChiSquareContingencyTableResult.DF, new Integer(df));

		texture.put(ChiSquareContingencyTableResult.ROW_SUM, rowSum);
		texture.put(ChiSquareContingencyTableResult.COL_SUM, colSum);
		texture.put(ChiSquareContingencyTableResult.GRAND_TOTAL, new Integer(grandTotal));
		texture.put(ChiSquareContingencyTableResult.PEARSON_CHI_SQUARE, new Double(pearsonChiSquare));

		return result;
	}
}
