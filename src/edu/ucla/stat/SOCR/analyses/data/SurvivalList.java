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
package edu.ucla.stat.SOCR.analyses.data;

import java.util.*;
import edu.ucla.stat.SOCR.util.*;

public class SurvivalList extends DataCase {
	//public static final int CENSORED_CONSTANT = 0;
	//public static final int DEAD_CONSTANT = 1;

	protected SurvivalObject[] objectArray;
	protected String groupName;
	protected int count = 0;
	protected ArrayList<SurvivalObject> objectList = new ArrayList<SurvivalObject>();
	//private TreeMap sortedList = new TreeMap();
	private TreeSet<Double> sortedTimeSet = new TreeSet<Double>(); // this is correct.
	private TreeMap<Double,SurvivalSingleList> timeSetLabelMap = 
		new TreeMap<Double,SurvivalSingleList>();
	private int numberNewList = 0;
	private int totalNumberList = 0;
	private double maxTime = 0; // those who die, no censored.
	private double minTime = Double.POSITIVE_INFINITY;

	private int numberGroups;

	private double[] survivalTimeArray = null;
	private int[] survivalAtRiskArray = null;
	private double[] survivalRateArray = null;
	private double[] upperCIArray = null;
	private double[] lowerCIArray = null;
	private double[] survivalSEArray = null;

	private ArrayList<Double> survivalTimeList = null;
	private ArrayList<Double> survivalRateList = null;
	private ArrayList<Integer> survivalAtRiskList = null;
	private ArrayList<Double> upperCIList = null;
	private ArrayList<Double> lowerCIList = null;
	private ArrayList<Double> survivalSEList = null;

	private String timeList = "";

	//public SurvivalList() {

	//}
	public SurvivalList(String groupName) {
		this.groupName = groupName;
		survivalTimeList = new ArrayList<Double>();
		survivalRateList = new ArrayList<Double>();
		upperCIList = new ArrayList<Double>();
		lowerCIList = new ArrayList<Double>();
		survivalAtRiskList = new ArrayList<Integer>();
		survivalSEList = new ArrayList<Double>();
	}
	public void add(SurvivalObject object) {
		//////////System.out.println("survivalList name = " + groupName + ", count = " + count);
		objectList.add(count, object);
		sortedTimeSet.add(new Double(object.getTime()));
		addNumberAtRisk(object);
		count++;
	}
	public void addNumberAtRisk(SurvivalObject object) {
		SurvivalSingleList tempList = (SurvivalSingleList)timeSetLabelMap.get(new Double(object.getTime())); // temp var.
		int temp = 0;

		//if (object.getCensor() == 1) {
			if (tempList!= null) {
				tempList.add(object);
				//////////System.out.println("when object.getTime() = " + object.getTime() + ", timeCount.intValue() = " + timeCount.intValue());
			}
			else {
				tempList = new SurvivalSingleList(groupName + " " + object.getTime());
				tempList.add(object);
				timeSetLabelMap.put(new Double(object.getTime()), tempList);
				//////////System.out.println("when object.getTime() = " + object.getTime() + ", timeCount.intValue() = " + timeCount.intValue());
				numberNewList++;
			}
		totalNumberList++;
		//}
	}

	public void rearrangeNumberAtRisk() {
		// take timeSetLabelMap which is alreay complete. this means rearrangeNumberAtRisk method is run after all the data are added into hashmap.

		if (!sortedTimeSet.isEmpty()) {
			Iterator<Double> iterator = sortedTimeSet.iterator();
			double timeLabel = 0;
			int timeCount = 0;
			Double key = null;
			//Double prevKey = null; // previousKey
			SurvivalSingleList currentSingleList = null;
			SurvivalSingleList prevSingleList = null;

			while(iterator.hasNext()) {
				key = (Double)iterator.next();
				timeLabel = key.doubleValue();
				if (timeSetLabelMap.containsKey(key)) {
					currentSingleList = (SurvivalSingleList) timeSetLabelMap.get(key);
					currentSingleList.examList();
					if (timeLabel == getMinTime()) { // this must be the first one because it's treemap.
						//prevKey = key;
						prevSingleList = currentSingleList;
					}
					else {

						if (currentSingleList.allAreCensored()) {
							for (int j = 0; j < currentSingleList.getSize(); j++) {
								prevSingleList.add((SurvivalObject)currentSingleList.getObjectList().get(j));
								currentSingleList.setSize(currentSingleList.getSize()+1);
							}
							timeSetLabelMap.remove(key);
						}
						else {
							prevSingleList = currentSingleList;
						}
					}
					//////////System.out.println("timeSetLabelMap key = " + key + ", currentSingle.size = " + currentSingle.getSize());
					//currentSingleList.printList();
				}
			}
		}
	}
	public double getMaxTimeAll() {
		if (maxTime == 0) {
			int size = objectList.size();
			objectArray = new SurvivalObject[size];

			for (int i = 0; i < size; i++) {
				objectArray[i] = (SurvivalObject)objectList.get(i);
				if (maxTime < objectArray[i].getTime()) {
					maxTime = objectArray[i].getTime();
				}
			}

			return maxTime;
		} else {
			return maxTime;
		}
	}
	private double getMaxTime() { // only the non-censored
		if (maxTime == 0) {
			int size = objectList.size();
			objectArray = new SurvivalObject[size];

			for (int i = 0; i < size; i++) {
				objectArray[i] = (SurvivalObject)objectList.get(i);
				if (maxTime < objectArray[i].getTime() && objectArray[i].getCensor() != SurvivalObject.CENSORED_CONSTANT) {
					maxTime = objectArray[i].getTime();
				}
			}

			return maxTime;
		} else {
			return maxTime;
		}
	}

	private double getMinTime() { // only the non-censored
		if (minTime == Double.POSITIVE_INFINITY) {
			int size = objectList.size();
			objectArray = new SurvivalObject[size];

			for (int i = 0; i < size; i++) {
				objectArray[i] = (SurvivalObject)objectList.get(i);
				if (minTime> objectArray[i].getTime()) {
					minTime = objectArray[i].getTime();
				}
			}

			return minTime;
		} else {
			return minTime;
		}
	}
	public int getCount() {
		return this.count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	protected void listToSurvivalObjectArray() {
		int size = objectList.size();
		objectArray = new SurvivalObject[size];
		//1////////System.out.println("listToArray size = " + size);
		for (int i = 0; i < size; i++) {
			objectArray[i] = (SurvivalObject)objectList.get(i);
			//////////System.out.println("listToArray objectArray = " + objectArray);

		}

	}

	protected double[] listToDoubleArray(ArrayList<Double> list) {
		int size = list.size();
		double[] result = new double[size];

		//for (int i = size-1; i >= 0; i--) {
		for (int i = 0; i < size; i++) {

			result[i] = ((Double)list.get(i)).doubleValue();
			////////System.out.println("
		}
		return result;
	}
	protected int[] listToIntArray(ArrayList<Integer> list) {
		int size = list.size();
		int[] result = new int[size];

		//for (int i = size-1; i >= 0; i--) {
		for (int i = 0; i < size; i++) {

			result[i] = ((Integer)list.get(i)).intValue();
			////////System.out.println("
		}
		return result;
	}


	public double[] getSurvivalTimeArray() {
		survivalTimeArray = listToDoubleArray(survivalTimeList);
		for (int i = 0; i < survivalTimeArray.length; i++) {
			//////System.out.println("LIST:getSurvivalTimeArray survivalTimeArray["+i+"] = " + survivalTimeArray[i]);
		}
		return survivalTimeArray;
	}

	public int[] getSurvivalAtRiskArray() {
		////System.out.println("getSurvivalAtRiskArray IS CALLED");
		survivalAtRiskArray = listToIntArray(survivalAtRiskList);
		//////System.out.println("LIST:getSurvivalAtRiskArray survivalAtRiskArray.length = " + survivalAtRiskArray.length);
		for (int i = 0; i < survivalAtRiskArray.length; i++) {

			//////System.out.println("LIST:getSurvivalAtRiskArray survivalAtRiskArray["+i+"] = " + survivalAtRiskArray[i]);

		}
		return survivalAtRiskArray;
	}
	public double[] getSurvivalSEArray() {
		survivalSEArray = listToDoubleArray(survivalSEList);
		for (int i = 0; i < survivalSEArray.length; i++) {
			//////System.out.println("LIST:getSurvivalSEArray survivalSEArray["+i+"] = " + survivalSEArray[i]);
		}
		return survivalSEArray;
	}
	public double[] getSurvivalRateArray() {
		survivalRateArray = listToDoubleArray(survivalRateList);
		for (int j = 0; j < survivalRateArray.length; j++) {
			//////System.out.println("LIST:getSurvivalRateArray survivalRateArray = " + survivalRateArray[j]);
		}
		return survivalRateArray;
	}

	public double[] getUpperCIArray() {
		upperCIArray = listToDoubleArray(upperCIList);
		for (int j = 0; j < upperCIArray.length; j++) {
			//////System.out.println("LIST:getUpperCIArray upperCIArray = " + upperCIArray[j]);
		}
		return upperCIArray;
	}

	public double[] getLowerCIArray() {
		lowerCIArray = listToDoubleArray(lowerCIList);
		for (int j = 0; j < lowerCIArray.length; j++) {
			//////System.out.println("LIST:getLowerCIArray lowerCIArray = " + lowerCIArray[j]);
		}
		return lowerCIArray;
	}

	public TreeSet<Double> getSortedTimeSet() {
		////////System.out.println("getSortedTimeSet groupName = "+ groupName + ", sortedTimeSet.size()= "  + sortedTimeSet.size());
		return sortedTimeSet;
	}
	public void printSortedTimeSet() {
			//////System.out.println("printSortedTimeSet groupName = " + groupName);
			//////System.out.println("printSortedTimeSet sortedTimeSet.isEmpty() = " + sortedTimeSet.isEmpty());

		if (!sortedTimeSet.isEmpty()) {
			Iterator<Double> iterator = sortedTimeSet.iterator();
			double timeLabel = 0;
			int timeCount = 0;
			Double key = null;
			SurvivalSingleList currentSingle = null;
			while(iterator.hasNext()) {
				key = (Double)iterator.next();
				timeLabel = key.doubleValue();
				//////////System.out.println("printSortedTimeSet timeLabel = " + timeLabel);
				if (timeSetLabelMap.containsKey(key)) {
					currentSingle = (SurvivalSingleList) timeSetLabelMap.get(key);
					//////////System.out.println("timeSetLabelMap key = " + key + ", currentSingle.size = " + currentSingle.getSize());
					currentSingle.printList();
				}
			}
		}
	}
	public void listSurvivalRate() {
		// follow Ripley and Vanables' R book, chapter 13 page 356.
		if (!sortedTimeSet.isEmpty()) {

			Iterator<Double> iterator = sortedTimeSet.iterator();
			double timeLabel = 0;
			int timeCount = 0;
			Double key = null;
			SurvivalSingleList currentSingle = null;
			double currentTotal = count;
			double currentAtRisk = count;
			double currentSurvivalRate = 0;
			double cumulativeRate = 1;
			int currentNumberEvent = 0; // that is, death.
			double currentVarSurvival = 0; // for variance
			double currentSurvivalSE = 0; // for variance
			//double cumulativeVarSum = 0; //the temperary term to add to the variance.
			double currentHazard = 0; // the hazard function.
			double currentVarHazard = 0;
			double currentSEHazard = 0; // for variance
			double hazardCIUpper = 0, hazardCILower = 0;
			int i = 0;
			while(iterator.hasNext()) {
				key = (Double)iterator.next();
				timeLabel = key.doubleValue();
				if (timeSetLabelMap.containsKey(key)) {


					currentSingle = (SurvivalSingleList) timeSetLabelMap.get(key);
					currentSurvivalRate = 1 - (currentSingle.getNumberDead() / currentTotal);
					currentSurvivalRate = cumulativeRate * currentSurvivalRate;
					cumulativeRate = currentSurvivalRate;

					////////System.out.println("printSortedTimeSet currentSingle = " + currentSingle);
					currentNumberEvent = currentSingle.getNumberDead();
					currentVarHazard += currentNumberEvent / currentAtRisk / (currentAtRisk - currentNumberEvent);
					currentVarSurvival = currentVarHazard * (currentSurvivalRate * currentSurvivalRate);
					currentSurvivalSE = Math.sqrt(currentVarSurvival);
					currentHazard += currentNumberEvent / currentAtRisk;
					currentSEHazard = Math.sqrt(currentVarHazard);
					double k0 = 1.645; // this needs to be user entered.

					hazardCIUpper = currentSurvivalRate * Math.exp(k0 * currentSEHazard);
					hazardCILower = currentSurvivalRate * Math.exp(-k0 * currentSEHazard);
					if(hazardCIUpper > 1) {
						hazardCIUpper = 1;

					}
					if (hazardCILower > 1) {
						hazardCILower = 1;
					}

					survivalTimeList.add(i, key);
					survivalAtRiskList.add(i, new Integer((int)currentAtRisk));
					survivalRateList.add(i, new Double(currentSurvivalRate));
					survivalSEList.add(i, new Double(currentSurvivalSE));
					upperCIList.add(i, new Double(hazardCIUpper));
					lowerCIList.add(i, new Double(hazardCILower));

					////////System.out.println("key = " + key + ", AtRisk = " + currentAtRisk + ", S = " + currentSurvivalRate + ", SE = " + currentSurvivalSE);// + ", " + hazardCILower + ", " + hazardCIUpper);
					//////System.out.println("LIST: key = " + key + ", AtRisk = " + currentAtRisk + ", SurvivalRate = " + currentSurvivalRate + ", SE = " + currentSurvivalSE);// + ", " + hazardCILower + ", " + hazardCIUpper);
					currentAtRisk = currentTotal - currentSingle.getSize();
					currentTotal = currentAtRisk;

					//currentTotal = currentTotal - currentAtRisk;
					//////////System.out.println("timeSetLabelMap key = " + key + ", currentSingle.size = " + currentSingle.getSize());
					//currentSingle.printList();
					i++;
				}
			}
		}
	}

	public SurvivalObject[] getObjectArray() {
		listToSurvivalObjectArray();
		return objectArray;
	}

	public void printList() {
		//////////System.out.println("Print survival survList: ");

		////////System.out.print("groupName = " + groupName + ": ");
		////////System.out.print("groupName = objectList.size() " + objectList.size());
		SurvivalObject currentObject = null;
		//for (int i = 0; i < objectList.size(); i++) {
		//	currentObject = (SurvivalObject)objectList.get(i);
		//	////////System.out.println("time = " + currentObject.getTime() + ", censor = " + //currentObject.getCensor() + ", index = " + currentObject.getIndex());
		//}
		listToSurvivalObjectArray();
		for (int i = 0; i < objectList.size(); i++) {
			currentObject = objectArray[i];
			////////System.out.print("printList " + i + " time = " + currentObject.getTime() + ", censor = " + currentObject.getCensor() + ", index = " + currentObject.getIndex()+ ". ");
		}
		////////System.out.println("");
	}



	/*public void sort() {
		SurvivalObject currentObject = null;
		for (int i = 0; i < this.count; i++) {
			currentObject = (SurvivalObject)objectList.get(i);
			sortedList.put(currentObject.getTime() + "", currentObject);
			//////////System.out.println("time = " + currentObject.getTime() + ", censor = " + currentObject.getCensor() + ", index = " + currentObject.getIndex());
		}
	}*/

   // added by annie che 20061008 for testing this class.

   // move this to Survival under model.
   //public static void main(String[] args) {
   public void constructSurvivalResult() {

	//double[] treat = {0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	double[] time = {1,10,22,7,3,32,12,23,8,22,17,6,2,16,11,34,8,32,12,25,2,11,5,20,4,19,15,6,8,17,23,35,5,6,11,13,4,9,1,6,8,10};
	byte[] censor = {1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,0};
	String[] treat = {"control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",   "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP","control","6-MP","control","6-MP","control","6-MP",    "control","6-MP","control","6-MP"};

	////////System.out.println("treat.length = " + treat.length);
	int numberGroups = 2; // could use set here.
	String[] groupNames = new String[]{"6-MP","control"};
	//int[] count = new int[numberGroups];

	ArrayList[] indexOfGroup = new ArrayList[numberGroups];
	SurvivalList[] survList = new SurvivalList[] {new SurvivalList("6-MP"), new SurvivalList("control")};


	//for (int i = 0; i < numberGroups; i++) {
	//	indexOfGroup[i] = new ArrayList();
	//	survList[i] = new SurvivalList();
	//}

	for (int i = 0; i < time.length; i++) {
		//////System.out.print(time[i]);
		if (censor[i] == SurvivalObject.CENSORED_CONSTANT) {
			//////System.out.print("+");
		}
		//////System.out.print(" ");
	}
	////////System.out.println("");

	//////////System.out.println("survList.length = " + survList.length);
	//////////System.out.println("time.length = " + time.length);

	for (int i = 0; i < time.length; i++) {
		for (int j = 0; j < numberGroups; j++) {
			//////////System.out.println("before if i = " + i + ", j = " + j +", groupNames["+j+"] = " + groupNames[j]);

			if (treat[i].equals(groupNames[j])) {
				//////////System.out.println("in if i = " + i + ", j = " + j);
				survList[j].add(new SurvivalObject(time[i], censor[i], i));
				//survList[j].count++;
			}
		}
		//////System.out.print("");
	}
	for (int j = 0; j < numberGroups; j++) {
		survList[j].printList();
		////////System.out.println("");
	}

	QSortAlgorithm quick = new QSortAlgorithm();
	//////////System.out.println("survList[0] instanceof DataCase = " + (survList[0] instanceof DataCase));

	//////////System.out.println("survList[1] instanceof DataCase = " + (survList[1] instanceof DataCase));
	try {
		for (int j = 0; j < numberGroups; j++) {

			quick.sort(survList[j].getObjectArray());
			//survList[j].printSortedList();
			////////System.out.println("");
			survList[j].getSortedTimeSet();
			survList[j].rearrangeNumberAtRisk();
			survList[j].printSortedTimeSet();
			survList[j].listSurvivalRate();

			//////////System.out.println("survList[j].getMinTime() = " + survList[j].getMinTime());

		}
	} catch (Exception e) {

		////////System.out.println("e = " + e);
	}
	//////////System.out.println("test KaplanMeier");
	//aplanMeierEstimate km = new KaplanMeierEstimate(time, censor);
	//double[] estimate = km.estimate(time, censor);
	//for (int i = 0; i < estimate.length; i++)
	//	////////System.out.println("estimate["+i+"] = " + estimate[i]);

	//double [][] ci = km.confidenceInterval(0.05, time, censor);

   }
}
