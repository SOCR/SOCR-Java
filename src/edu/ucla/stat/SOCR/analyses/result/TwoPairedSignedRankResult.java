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

public class TwoPairedSignedRankResult extends NonParametricTestResult {
	public static final String MEAN_W = "MEAN_W";
	public static final String VAR_W = "VAR_W";
	public static final String W_STAT = "W_STAT";
	public static final String Z_SCORE = "Z_SCORE";
	public static final String P_VALUE = "P_VALUE";
	public TwoPairedSignedRankResult(HashMap texture) {
		super(texture);
	}
	public TwoPairedSignedRankResult(HashMap texture, HashMap graph) {
		super(texture, graph);
	}
	/*********************** Accessors ************************/

	/** mean of W. */
	public double getMeanW() {
		return ((Double)texture.get(MEAN_W)).doubleValue();
	}
	public double getVarianceW() {
		return ((Double)texture.get(VAR_W)).doubleValue();
	}
	public double getWStat() {
		return ((Double)texture.get(W_STAT)).doubleValue();
	}
	public double getZScore() {
		return ((Double)texture.get(Z_SCORE)).doubleValue();
	}
	public double getPValueOneSided() {
		return ((Double)texture.get(P_VALUE_ONE_SIDED)).doubleValue();
	}
	public double getPValueTwoSided() {
		return ((Double)texture.get(P_VALUE_TWO_SIDED)).doubleValue();
	}
}
