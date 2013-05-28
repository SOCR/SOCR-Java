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
// this class is written for Wilcoxon Rank Sum Test and Signed Rank Test.
// annieche 20051203

// java edu.ucla.stat.SOCR.analyses.data.Data


package edu.ucla.stat.SOCR.analyses.data;

public class DataCase {
	protected double value; 	// should never be changed
	protected String group; 	// should never be changed
	protected double rank;	// to be set after sorting
	protected double flignerKilleenNormalQuantile;
	protected int index;
	boolean isPositive = false;
	//short groupNum;

	public DataCase() { // useless.
	}

	public DataCase(double value, int index) {
		this.value = value;
		this.index = index;
	}
	public DataCase(double value, String group) {
		this.value = value;
		this.group = group;
	}

	public DataCase(double value, double rank, String group) {
		this.value = value;
		this.rank = rank;
		this.group = group;
	}
	public void setValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setFlignerKilleenNormalQuantile(double quantile) {
		this.flignerKilleenNormalQuantile = quantile;
	}

	public double getFlignerKilleenNormalQuantile() {
		return flignerKilleenNormalQuantile;
	}


	public String getGroup() {
		return group;
	}
	public void setRank(double rank) {
		this.rank = rank;
	}
	public double getRank() {
		return rank;
	}
	public int getIndex() {
		return index;
	}
	public void setSign(boolean isPositive) {
		this.isPositive = isPositive;
	}
	public boolean getSign() {
		return isPositive;
	}

	public static double[] getValueArray(DataCase[] dataCase) {
		int length = dataCase.length;
		double[] answer = new double[length];
		for (int i = 0; i < length; i++) {
			answer[i] = dataCase[i].getValue();
		}
		return answer;
	}

	public static int[] getIndexArray(DataCase[] dataCase) {
		int length = dataCase.length;
		int[] answer = new int[length];
		for (int i = 0; i < length; i++) {
			answer[i] = dataCase[i].getIndex();
		}
		return answer;
	}
	public static DataCase[] toDataCaseArray(double[] doubleArray) {
		int arrayLength = doubleArray.length;
		int[] originalRank = new int[arrayLength]; // could serve as original index when sorting
		DataCase[] dataCase = new DataCase[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			originalRank[i] = i + 1;
			dataCase[i] = new DataCase(doubleArray[i], originalRank[i]);
		}
		return dataCase;

	}
	public static void main(String args[]) {
		double[] test = new double[] {1,2,3,5,6,4,3};
		DataCase[] dataCase = DataCase.toDataCaseArray(test);
		for (int i = 0; i < test.length; i++) {
			////System.out.println(dataCase[i].getIndex());
		}
	}
}
