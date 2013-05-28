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

public class KruskalWallisTest extends QSortAlgorithm {
	private HashMap tieMap = null;
	private HashMap completeMap = null;
	private DataCase[] combo = null;
	private boolean hasTie = false;
	private int maxNumberTies;
	public KruskalWallisTest(DataCase[][] groups, String[] groupNames) {
		combo = QSortAlgorithm.rankCombinedLists(groups, groupNames);
		tieMap = QSortAlgorithm.getTieMap();
		completeMap = QSortAlgorithm.getCompleteMap();
		hasTie = QSortAlgorithm.hasTie();
		maxNumberTies =  QSortAlgorithm.getMaxNumberTies();
	}

	public DataCase[] getRankedArray() {
		return combo;
	}
	public HashMap getTieArray() {
		return tieMap;
	}
	public HashMap getCompleteArray() {
		return completeMap;
	}

	//public void setHasTie(boolean input) {
	//	hasTie = input;
	//}
	public boolean getHasTie() {
		//if (tieMap.size() > 0)
		//	hasTie = true;
		//else
		//	hasTie = false;
		return hasTie;
	}
	public int getMaxNumebrTie() {
		//if (tieMap.size() > 0)
		//	hasTie = true;
		//else
		//	hasTie = false;
		return maxNumberTies;
	}

}
