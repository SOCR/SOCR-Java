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
package edu.ucla.stat.SOCR.analyses.result;

import java.util.HashMap;

public class FisherExactResult extends ParametricTestResult {

	public static final String EXPECTED_DATA = "EXPECTED_DATA";

	public static final String PEARSON_CHI_SQUARE = "PEARSON_CHI_SQUARE";
	public static final String DF = "DF";

	public static final String ROW_NAMES = "ROW_NAMES";
	public static final String COL_NAMES = "COL_NAMES";
	public static final String ROW_SUM = "ROW_SUM";
	public static final String COL_SUM = "COL_SUM";
	public static final String GRAND_TOTAL = "GRAND_TOTAL";
	public static final String P_VALUE_CUTOFF = "P_VALUE_CUTOFF";
	public static final String P_VALUE_CUTOFF_1_TAIL = "P_VALUE_CUTOFF_1_TAIL";
	public static final String P_VALUE_CUTOFF_2_TAIL = "P_VALUE_CUTOFF_2_TAIL";

	public static final double  INVALID_P_VALUE_CUTOFF = -1;
	public static final double  INVALID_P_VALUE_CUTOFF_1_TAIL = -1;
	public static final double  INVALID_P_VALUE_CUTOFF_2_TAIL = -1;

	/*********************** Constructors ************************/
	public FisherExactResult(HashMap texture) {
		super(texture);
	}
	public FisherExactResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}

	/*********************** Accessors ************************/
	public double[][] getExpectedData() { // get sample mean of original dataset
		return ((double[][] )texture.get(EXPECTED_DATA));
	}

	public int getDF() {
		return ((Integer)texture.get(DF)).intValue();
	}
	public String[] getRowNames() {
		return ((String[])texture.get(ROW_NAMES));
	}
	public String[] getColNames() {
		return ((String[])texture.get(COL_NAMES));
	}

	public int[] getRowSum() {
		return ((int[])texture.get(ROW_SUM));
	}
	public int[] getColSum() {
		return ((int[])texture.get(COL_SUM));
	}
	public double getPCutoff() {
		return ((Double)texture.get(P_VALUE_CUTOFF)).doubleValue();
	}
	
	public double get1TailPCutoff() {
		return ((Double)texture.get(P_VALUE_CUTOFF_1_TAIL)).doubleValue();
	}

	public double get2TailPCutoff() {
		return ((Double)texture.get(P_VALUE_CUTOFF_2_TAIL)).doubleValue();
	}
	
	public int getGrandTotal() {
		return ((Integer)texture.get(GRAND_TOTAL)).intValue();
	}
	public double getPearsonChiSquareStat() {
		return ((Double)texture.get(PEARSON_CHI_SQUARE)).doubleValue();
	}
}
