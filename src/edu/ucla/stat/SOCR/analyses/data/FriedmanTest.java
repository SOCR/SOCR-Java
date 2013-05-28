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
// annieche 2006127

// mann-whitney-wilcoxon for two independent samples.

package edu.ucla.stat.SOCR.analyses.data;

import edu.ucla.stat.SOCR.util.*;
import java.util.*;

public class FriedmanTest extends QSortAlgorithm {

	private HashMap[] tieMap = null;
	private HashMap[] completeMap = null;
	private DataCase[][] combo = null;
	private boolean[] hasTie = null;
	private int[] maxNumberTies;
	private int numberGroups;

	private double[] rankGroupAverage = null;
	private double rankGrandAverage;
	private double[][] rankValues = null;
	private int singleGroupSize;
	private DataCase[][] entry = null;

	private double[] rankSum = null;

	private DataCase[][] newEntry = null;

	public FriedmanTest(DataCase[][] groups, String[] groupNames) {
		//////System.out.println("in FriedmanTest constructor ");
		numberGroups = groups.length;
		singleGroupSize =  groups[0].length;
		tieMap = new HashMap[numberGroups];
		completeMap = new HashMap[numberGroups];
		hasTie = new boolean[numberGroups];
		maxNumberTies = new int[numberGroups];
		rankGroupAverage = new double[numberGroups];
		rankValues = new double[singleGroupSize][numberGroups];
		rankSum = new double[numberGroups];

		////System.out.println("in FriedmanTest numberGroups =  " + numberGroups);
		////System.out.println("in FriedmanTest singleGroupSize =  " + singleGroupSize);

		entry = new DataCase[singleGroupSize][numberGroups];
		combo = new DataCase[singleGroupSize][numberGroups];

		for (int i = 0; i < singleGroupSize; i++) {
			for (int j = 0; j < numberGroups; j++) {
				try {
					entry[i][j] = groups[j][i];
					////System.out.println("in FriedmanTest entry["+i+"]["+j+"]  = " + entry[i][j].getValue());
				} catch (Exception e) {
					//////System.out.println(" e = " + e);
				}
			}
			//////System.out.println("");
		}

		for (int i = 0; i < singleGroupSize; i++) {
			////System.out.println("i = " + i + " combo.length = " + combo.length);

			try {
				//combo[i] = QSortAlgorithm.rankCombinedLists(entry[i]);
				combo[i] = QSortAlgorithm.rankList(entry[i]);


			} catch (Exception e) {
				////System.out.println("i = " + i + " combo e = " + e);
			}
			for (int j = 0; j < numberGroups; j++) {
				try {
					rankValues[i][j] = combo[i][j].getRank();

					////System.out.println("rankValue["+i + "][" + j + "]= " + rankValues[i][j] + " " +  combo[i][j].getValue() + " " + combo[i][j].getGroup() );
				} catch (Exception e) {
					//////System.out.println("rankValues e = " + e);
				}
			}
			/*
			tieMap[i] = QSortAlgorithm.getTieMap();
			completeMap[i] = QSortAlgorithm.getCompleteMap();
			hasTie[i] = QSortAlgorithm.hasTie();
			maxNumberTies[i] =  QSortAlgorithm.getMaxNumberTies();
			*/
		}


		newEntry = new DataCase[numberGroups][singleGroupSize];

		for (int i = 0; i < singleGroupSize; i++) {
			for (int j = 0; j < numberGroups; j++) {
				 for (int k = 0; k < numberGroups; k++) {
					if ((combo[i][k].getGroup()).equalsIgnoreCase(groupNames[j])) {
						////System.out.println("if when " + groupNames[j]);
						rankSum[j] += combo[i][k].getRank();
						newEntry[j][i] = new DataCase(combo[i][k].getValue(), combo[i][k].getRank(), groupNames[j]);
					}
				}
			}
		}


		for (int i = 0; i < singleGroupSize; i++) {
			//System.out.print("NEW ENTRY ");
			for (int j = 0; j < numberGroups; j++) {
				//System.out.print(" " + newEntry[j][i].getValue() + " " + newEntry[j][i].getRank() + " " + newEntry[j][i].getGroup() + " ");
			}
			//System.out.println("");
		}

			for (int j = 0; j < numberGroups; j++) {

				try {
					//System.out.println(groupNames[j] + " " + rankSum[j]);
					rankGroupAverage[j] = rankSum[j] / (double)singleGroupSize;
					//rankGroupAverage[j] = AnalysisUtility.mean(rankValues[j]);
				} catch (Exception e) {
					//System.out.println("j = " + j + " rankGroupAverage e = " + e);
				}
			}



		try {
			rankGrandAverage = AnalysisUtility.mean(rankGroupAverage);
		} catch (Exception e) {
				////System.out.println(" rankGrandAverage e = " + e);
		}
	}

	public DataCase[][] getResultTable() { // first index is group, second index is for case.
		return newEntry;
	}

	public double[] getRankGroupAverage() {
		return rankGroupAverage;
	}

	public double[] getRankGroupSum() {
		return rankSum;
	}


	public double getSingleGroupSize() {
		return singleGroupSize;
	}


	public double getRankGrandAverage() {
		return rankGrandAverage;
	}


	public DataCase[][] getRankedArray() {
		return combo;
	}
	public HashMap[] getTieArray() {
		return tieMap;
	}
	public HashMap[] getCompleteArray() {
		return completeMap;
	}

	//public void setHasTie(boolean input) {
	//	hasTie = input;
	//}
	public boolean[] getHasTie() {
		//if (tieMap.size() > 0)
		//	hasTie = true;
		//else
		//	hasTie = false;
		return hasTie;
	}
	public int[] getMaxNumebrTie() {
		//if (tieMap.size() > 0)
		//	hasTie = true;
		//else
		//	hasTie = false;
		return maxNumberTies;
	}

}
