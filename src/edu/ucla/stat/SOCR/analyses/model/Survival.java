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
// annie che 20061110.

// using Kaplan-Meier method.

package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import java.util.*;
import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.analyses.result.SurvivalResult;

public class Survival implements Analysis {
	private final static String Y_DATA_TYPE = DataType.QUANTITATIVE;

	private String type = "OneT";
	private double[] time = null;
	private byte[] censor = null;
	private String[] treat = null;

	private double[][] survivalTimeArray2D = null;
	private double[][] survivalRateArray2D = null;
	private double[][] survivalSEArray2D = null;
	private double[][] upperCIArray2D = null;
	private double[][] lowerCIArray2D = null;
	private double[][] censoredArray2D = null;
	private double[][] censoredRateArray2D = null;

	private double[] maxTimeArray2D = null;

	private int[][] atRiskArray2D = null;
	private String timeList = null;
	public Survival(double[] time, byte[] censor, String[] treat) {
		this.time = time;
		this.censor = censor;
		this.treat = treat;

	}

	public String getAnalysisType() {
		return type;
	}

	// analyze not used.
	public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
		Result result = null;
		//////System.out.println("Analysis Type = " + analysisType);
		if (analysisType != AnalysisType.SURVIVAL)
			throw new WrongAnalysisException();
		return result;
	}


 //public static void main(String[] args) {
	public SurvivalResult getSurvivalResult() {
		HashSet<String> treatNameSet = new HashSet<String>();
		//////System.out.println("model treat.length = " + treat.length);

		for (int i = 0; i < treat.length; i++) {
			treatNameSet.add(treat[i]);
			//////System.out.println("model treat["+i+"] = " + treat[i]);

		}
		String[] groupNames = new String[treatNameSet.size()];
		//////System.out.println("model treatNameSet.size() = " +treatNameSet.size());

		Iterator<String> iterator = treatNameSet.iterator();
		int k = 0;
		while(iterator.hasNext()) {
			groupNames[k] = (String)iterator.next();
			//////System.out.println("groupNames["+k+"] = " + groupNames[k]);
			k++;
		}

		HashMap<String,Object> texture = new HashMap<String,Object>();
		SurvivalResult result = new SurvivalResult(texture);

		int numberGroups = treatNameSet.size();

		SurvivalList[] survList = new SurvivalList[numberGroups];
		for (int i = 0; i < numberGroups; i++) {
			survList[i] = new SurvivalList(groupNames[i]);
		}

		survivalTimeArray2D = new double[numberGroups][];
		survivalRateArray2D = new double[numberGroups][];
		upperCIArray2D = new double[numberGroups][];
		lowerCIArray2D = new double[numberGroups][];
		atRiskArray2D = new int[numberGroups][];
		survivalSEArray2D = new double[numberGroups][];
		maxTimeArray2D = new double[numberGroups];

		// Why is this not working??????
		// ArrayList<Double>[] censoredArrayList = new ArrayList<Double>[numberGroups];
		ArrayList[] censoredArrayList = new ArrayList[numberGroups];
		
		StringBuffer listBuffer = new StringBuffer();

		for (int i = 0; i < time.length; i++) {
			if (i % edu.ucla.stat.SOCR.analyses.gui.Analysis.SURVIVAL_LIST_LENGTH == 0)
				listBuffer.append("\n\t");
			listBuffer.append(time[i]);
			if (censor[i] == SurvivalObject.CENSORED_CONSTANT) {
				listBuffer.append("+");
			}
			listBuffer.append("     ");

		}
		timeList = listBuffer.toString();
		int[] censorCount = new int[numberGroups];
		int censorIndex = 0;
		int numberCensored = 0;
		for (int j = 0; j < numberGroups; j++) {
			censoredArrayList[j] = new ArrayList<Double>();
			for (int i = 0; i < time.length; i++) {
				censorIndex = 0;
				if (censor[i] == SurvivalObject.CENSORED_CONSTANT && 
						treat[i].equalsIgnoreCase(groupNames[j])) {
					censoredArrayList[j].add(censorIndex, new Double(time[i]));
					//////System.out.println("time["+i+"] = " + time[i] + " censor = " + censor[i]);
					//////System.out.println("censoredArrayList["+j+"] = " + censoredArrayList[j]);
					censorCount[j]++;
					censorIndex++;
					numberCensored++;
				}
			}
		}
		//for (int i = 0; i < censorCount.length; i++) {
			//////System.out.println("censorCount["+i+"] = " + censorCount[i]);
		//}
		censoredArray2D = new double[numberGroups][];
		censoredRateArray2D = new double[numberGroups][];
		for (int j = 0; j < numberGroups; j++) {
			////////System.out.println("start for j");
			if (censorCount[j]>0) {
				////////System.out.println("j = " + j + " censorCount["+j+"] = " + censorCount[j]);
				censoredArray2D[j] = new double[censorCount[j]];

				////////System.out.println("start for j = " +j + " time.length = "+ time.length );
				for (int i = 0; i < censorCount[j]; i++) {
					censoredArray2D[j][i] = ((Double)censoredArrayList[j].get(i)).doubleValue();
					//////////System.out.println("censoredArray2D["+j+"]["+i + "] = " + censoredArray2D[j][i]); 
					// j = group, i = patient

				}
				censoredRateArray2D[j] = new double[censorCount[j]];

			}
			else {
				censoredArray2D[j] = new double[censorCount[j]];

				censoredRateArray2D[j] = new double[censorCount[j]];
				// to avoid NullPointerException.
			}
		}
		
		for (int j = 0; j < numberGroups; j++) {
				QSortAlgorithm quickTest = new QSortAlgorithm();

				try {
					quickTest.sort(censoredArray2D[j]);
				} catch (Exception e) {
					// shouldn't be a problem here.
				}
				for (int i = 0; i < censorCount[j]; i++) {
				if (censorCount[j]>0) {
					////////System.out.println("after sort censoredArray2D["+j+"] = " + 
					//censoredArray2D[j][i]);
				}
			}
		}

		for (int i = 0; i < time.length; i++) {
			for (int j = 0; j < numberGroups; j++) {
				//////////System.out.println("before if i = " + i + ", j = " + 
				// j +", groupNames["+j+"] = " + groupNames[j]);

				if (treat[i].equals(groupNames[j])) {
					//////System.out.println("in if i = " + i + ", j = " + j);
					survList[j].add(new SurvivalObject(time[i], censor[i], i));
				}
			}
			////////System.out.print("");
		}

		for (int j = 0; j < numberGroups; j++) {
			survList[j].printList();
			////////System.out.println("");
		}

		QSortAlgorithm quick = new QSortAlgorithm();
		for (int i = 0; i < numberGroups; i++) {
			//////////System.out.println("survList[0] instanceof DataCase = " + 
			//(survList[i] instanceof DataCase));
		}


		try {
			for (int j = 0; j < numberGroups; j++) {

				quick.sort(survList[j].getObjectArray());
				survList[j].getSortedTimeSet();
				survList[j].rearrangeNumberAtRisk();
				survList[j].printSortedTimeSet();
				survList[j].listSurvivalRate();
				survivalTimeArray2D[j] = survList[j].getSurvivalTimeArray();
				survivalRateArray2D[j] = survList[j].getSurvivalRateArray();
				upperCIArray2D[j] = survList[j].getUpperCIArray();
				lowerCIArray2D[j] = survList[j].getLowerCIArray();

				maxTimeArray2D[j] = survList[j].getMaxTimeAll();
				atRiskArray2D[j] = survList[j].getSurvivalAtRiskArray();
				survivalSEArray2D[j] = survList[j].getSurvivalSEArray();
				//////////System.out.println("now for");
				for (int i = 0; i < censorCount[j]; i++) {
					//////////System.out.println("now for i = " + i + " time.length = " + 
					// time.length);
					for (int a = 0; a < survivalRateArray2D[j].length; a++) {
						//////////System.out.println("now for j = " + j + " i = " + 
						//i + " a = " + a + ", censoredArray2D["+j+"].length = " + censoredArray2D[j].length);
						if ((censoredArray2D[j].length > 0) && 
								(censoredArray2D[j][i] >= survivalTimeArray2D[j][a])) {
							censoredRateArray2D[j][i] = survivalRateArray2D[j][a];
							//////////System.out.println("censoredRateArray2D = " + 
							//censoredRateArray2D);
						}
					}
				}
			}

			texture.put(SurvivalResult.SURVIVAL_TIME, survivalTimeArray2D);
			texture.put(SurvivalResult.SURVIVAL_RATE, survivalRateArray2D);
			texture.put(SurvivalResult.SURVIVAL_RATE_UPPER_CI, upperCIArray2D);
			texture.put(SurvivalResult.SURVIVAL_RATE_LOWER_CI, lowerCIArray2D);
			texture.put(SurvivalResult.SURVIVAL_AT_RISK, atRiskArray2D);
			texture.put(SurvivalResult.SURVIVAL_SE, survivalSEArray2D);
			texture.put(SurvivalResult.SURVIVAL_GROUP_NAMES, groupNames);
			texture.put(SurvivalResult.SURVIVAL_TIME_LIST, timeList);
			texture.put(SurvivalResult.NUMBER_CENCORED, numberCensored + "");

			texture.put(SurvivalResult.SURVIVAL_CENSORED_TIME, censoredArray2D);
			texture.put(SurvivalResult.SURVIVAL_CENSORED_RATE, censoredRateArray2D);
			texture.put(SurvivalResult.MAX_TIME, maxTimeArray2D);

		} catch (Exception e) {
			////////System.out.println("model.Survival e = " + e);
		}

		return result;
   }
}
