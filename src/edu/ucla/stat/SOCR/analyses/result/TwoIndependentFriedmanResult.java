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
import edu.ucla.stat.SOCR.analyses.data.*;

public class TwoIndependentFriedmanResult extends NonParametricTestResult {
	public static final String GROUP_NAME_LIST = "GROUP_NAME_LIST";
	public static final String DATA_RANK_LIST = "DATA_RANK_LIST";
	public static final String DATA_RANK_SEPARATE_LIST = "DATA_RANK_SEPARATE_LIST";
	public static final String DF = "DF";
	public static final String SAMPLE_SIZE = "SAMPLE_SIZE";

	public static final String GRAND_RANK_TOTAL = "GRAND_TOTAL";
	public static final String GRAND_RANK_MEAN = "GRAND_MEAN";
	public static final String GROUP_RANK_TOTAL = "GROUP_TOTAL";
	public static final String GROUP_RANK_MEAN = "GROUP_MEAN";

	public static final String RANK_ARRAY = "RANK_ARRAY";
	public static final String CHI_STAT = "CHI_STAT";
	public static final String SUM_SQUARES = "SUM_SQUARES";
	public static final String SINGLE_GROUP_SIZE = "SINSINGLE_GROUP_SIZE";
	public static final String DATA_ARRAY = "DATA_ARRAY";

	/*********************** Constructors ************************/

	public TwoIndependentFriedmanResult(HashMap texture) {
		super(texture);
	}
	public TwoIndependentFriedmanResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}


	/*********************** Accessors ************************/

	public String[] getGroupNameList() {
		return ((String[])texture.get(GROUP_NAME_LIST));
	}

	public DataCase[][] getDataArray() {
		return ((DataCase[][])texture.get(DATA_ARRAY));
	}
	public DataCase[][] getRankArray() {
		return ((DataCase[][])texture.get(RANK_ARRAY));
	}

	public int getSampleSize() {
		return ((Integer)texture.get(SAMPLE_SIZE)).intValue();
	}
	public int getSingleGroupSize() {
		return ((Integer)texture.get(SINGLE_GROUP_SIZE)).intValue();
	}
	public int getDF() {
		return ((Integer)texture.get(DF)).intValue();
	}
	public double getSumSquares() {
		//System.out.println("result " + ((Double)texture.get(SUM_SQUARES)).doubleValue());
		return ((Double)texture.get(SUM_SQUARES)).doubleValue();
	}
	public double getPValue() {
		return ((Double)texture.get(P_VALUE)).doubleValue();
	}

	public double[] getGroupTotal() {
		return ((double[])texture.get(GROUP_RANK_TOTAL));
	}

	public double[] getGroupMean() {
		return ((double[])texture.get(GROUP_RANK_MEAN));
	}
/*
	public double getGrandTotal() {
		return ((Double)texture.get(GRAND_RANK_TOTAL)).doubleValue();
	}
*/
	public double getGrandMean() {
		return ((Double)texture.get(GRAND_RANK_MEAN)).doubleValue();
	}
	public double getChiStat() {
		return ((Double)texture.get(CHI_STAT)).doubleValue();
	}

}
