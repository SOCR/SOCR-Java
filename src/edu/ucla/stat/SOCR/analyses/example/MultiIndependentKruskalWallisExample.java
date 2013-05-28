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
/*

january 2007. Annie Che <chea@stat.ucla.edu>. UCLA Statistics.

Source of example data:
Conover, W. J. Practical nonparametric statistics.

*/
package edu.ucla.stat.SOCR.analyses.example;

import java.util.HashMap;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentKruskalWallisResult;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;

public class MultiIndependentKruskalWallisExample {
	public static void main(String args[]) {

		double[] xA = {83, 91, 94, 89, 89, 96, 91, 92, 90};
		double[] xB = {91, 90, 81, 83, 84, 83, 88, 91, 89, 84};
		double[] xC = {101, 100, 91, 93, 96, 95, 94};
		double[] xD = {78, 82, 81, 77, 79, 81, 80, 81};


		// you'll need to instantiate a data instance first.
		Data data = new Data();

		/* dump all the columns you need to the data object using 'data.appendX' comment.
		 dump as many as you'd like.
		 but duplicate append will cause problem, so DO NOT append the same column more than once please.
		*/
		data.appendX("Column_A", xA, DataType.QUANTITATIVE);
		data.appendX("Column_B", xB, DataType.QUANTITATIVE);
		data.appendX("Column_C", xC, DataType.QUANTITATIVE);
		data.appendX("Column_D", xD, DataType.QUANTITATIVE);


		// then use the following line to get the result.
		try {
			TwoIndependentKruskalWallisResult result = (TwoIndependentKruskalWallisResult)data.getAnalysis(AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS);

               if (result != null) {
		          // Getting the model's parameter estiamtes, summary, and statistics.
				String[] groupNames = result.getGroupNameList();
				double[] rankSum = result.getRankSumList();
				double tStat = result.getTStat();
				double s2 = result.getSSqaured(); // i.e. s * s
				double cp = result.getCriticalValue();;
				String dataAndRankString = result.getDataRankInformation();;
				int[] groupCount = result.getGroupCount();;
				String df = result.getDegreesOfFreedom();;
				String[] multipleComparisonInfo = result.getMultipleComparisonInformation();;
				String multipleComparisonHeader = result.getMultipleComparisonHeader();;

				//System.out.println("\n\nSIGNIFICANCE LEVEL = 0.05");
				//System.out.println("\nDEGREES OF FREEDOM = " + df);
				//System.out.println("\nCRITICAL VALUE = " + cp);
				//System.out.println("\nT-STAITISTIC = " + tStat);
				//System.out.println("\nS * S = " + s2);
				//System.out.println("\n\nNotation: Ri -- Rank of group i; ni -- size of group i.\n");

				//System.out.println("\n" + multipleComparisonHeader + "\n");
				/*for (int i = 0; i <multipleComparisonInfo.length; i++) {
					if (multipleComparisonInfo[i] != null)
						//System.out.println("\n"+multipleComparisonInfo[i] );
				}*/

			}
		} catch (Exception e) {
			//System.out.println(e);
		}
	}
}
