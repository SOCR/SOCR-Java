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
/* http://java.sun.com/products/plugin/1.4.1/demos/applets/SortDemo/QSortAlgorithm.java */
/*
 * @(#)QSortAlgorithm.java	1.8 02/06/13
 */

/**
 * A quick sort demonstration algorithm
 * SortAlgorithm.java
 *
 * @author James Gosling
 * @author Kevin A. Smith
 * @version 	@(#)QSortAlgorithm.java	1.3, 29 Feb 1996
 */

/*************************************************************************/
/* modified for SOCR analyses Wilcoxon Rank Sum Test. annieche 20051203. */
package edu.ucla.stat.SOCR.util;

import edu.ucla.stat.SOCR.analyses.data.DataCase;
import java.util.HashMap;

public class QSortAlgorithm extends SortAlgorithm
{
    /**
     * A version of pause() that makes it easier to ensure that we pause
     * exactly the right number of times.
     */
	private static HashMap<String, String> tieMap = null;
	private static HashMap<String, String> completeMap = null;
	private static boolean hasTie;
	private static int maxNumberTies = 1;
	private static int nTotal = 0;

	// for doing the MannWhitneyWilcoxon test.
	// (it's weird to use static here. but this was added long time after this class is written,
	// to add Mann Whitney Wilcoxon. should have done a better design)

	public QSortAlgorithm() {
		super();
	}
    private boolean pauseTrue(int lo, int hi) throws Exception {
		//super.pause(lo, hi);
		//////////System.out(" pauseTrue " + lo + " " + hi);
		return true;
    }

   /** This is a generic version of C.A.R Hoare's Quick Sort
    * algorithm.  This will handle arrays that are already
    * sorted, and arrays with duplicate keys.<BR>
    *
    * If you think of a one dimensional array as going from
    * the lowest index on the left to the highest index on the right
    * then the parameters to this function are lowest index or
    * left and highest index or right.  The first time you call
    * this function it will be with the parameters 0, a.length - 1.
    *
    * @param a       an integer array
    * @param lo0     left boundary of array partition
    * @param hi0     right boundary of array partition
    */

	void QuickSort(double array[], int lo0, int hi0) throws Exception // does not say what kind of Exception and why?????
	{
		int lo = lo0;
		int hi = hi0;
		double mid;

		//for (int i = 0; i < array.length; i++)
		//		////////System.out(array[i]);

		if ( hi0 > lo0)
		{

			/* Arbitrarily establishing partition element as the midpoint of
			* the array.
			*/
			mid = array[ ( lo0 + hi0 ) / 2 ];

			// loop through the array until indices cross
			while( lo <= hi )
			{
				/* find the first element that is greater than or equal to
				* the partition element starting from the left Index.
				*/
				while( ( lo < hi0 ) && pauseTrue(lo0, hi0) && ( array[lo] < mid ))
					++lo;

				/* find an element that is smaller than or equal to
				* the partition element starting from the right Index.
				*/
				while( ( hi > lo0 ) && pauseTrue(lo0, hi0) && ( array[hi] > mid ))
					--hi;

				// if the indexes have not crossed, swap
				if( lo <= hi )
				{
					swap(array, lo, hi);
					++lo;
					--hi;
				}
			}

			/* If the right index has not reached the left side of array
			* must now sort the left partition.
			*/
			if( lo0 < hi )
				QuickSort( array, lo0, hi );

			/* If the left index has not reached the right side of array
			* must now sort the right partition.
			*/
			if( lo < hi0 )
				QuickSort( array, lo, hi0 );

		}
	}

	void QuickSort(DataCase array[], int lo0, int hi0) throws Exception
	{
		int lo = lo0;
		int hi = hi0;
		DataCase mid;

		//for (int i = 0; i < array.length; i++)
		//		////////System.out(array[i]);

		if ( hi0 > lo0)
		{

			/* Arbitrarily establishing partition element as the midpoint of
			* the array.
			*/
			mid = array[ ( lo0 + hi0 ) / 2 ];

			// loop through the array until indices cross
			while( lo <= hi )
			{
				/* find the first element that is greater than or equal to
				* the partition element starting from the left Index.
				*/
				while( ( lo < hi0 ) && pauseTrue(lo0, hi0) && ( array[lo].getValue() < mid.getValue() ))
					++lo;

				/* find an element that is smaller than or equal to
				* the partition element starting from the right Index.
				*/
				while( ( hi > lo0 ) && pauseTrue(lo0, hi0) && ( array[hi].getValue() > mid.getValue() ))
					--hi;

				// if the indexes have not crossed, swap
				if( lo <= hi )
				{
					swap(array, lo, hi);
					++lo;
					--hi;
				}
			}

			/* If the right index has not reached the left side of array
			* must now sort the left partition.
			*/
			if( lo0 < hi )
				QuickSort( array, lo0, hi );

			/* If the left index has not reached the right side of array
			* must now sort the right partition.
			*/
			if( lo < hi0 )
				QuickSort( array, lo, hi0 );

		}
	}

	private void swap(double a[], int i, int j)
	{
		double T;
		T = a[i];
		a[i] = a[j];
		a[j] = T;

	}
	private void swap(DataCase[] dataCase, int i, int j)
	{
		DataCase tempDataCase;
		tempDataCase = dataCase[i];
		dataCase[i] = dataCase[j];
		dataCase[j] = tempDataCase;
	}

	public void sort(double a[]) throws Exception
	{
		QuickSort(a, 0, a.length - 1);
	}
	public void sort(DataCase a[]) throws Exception
	{
		QuickSort(a, 0, a.length - 1);
	}

	public static double min(double a[]) throws Exception
	{
		QSortAlgorithm quick = new QSortAlgorithm();
		quick.sort(a);
		return a[0];
	}
	public static double max(double a[]) throws Exception
	{
		QSortAlgorithm quick = new QSortAlgorithm();
		quick.sort(a);
		return a[a.length-1];
	}


	/*
   public static void main(String args[]) {
   // example from Rice page 390. 405
   		int nA = 13; int nB = 8;

   		//DataCase dataCase = new DataCase(1, "A");

   		DataCase[] groupA = new DataCase[nA];
   		DataCase[] groupB = new DataCase[nB];
		String a = "A";
		String b = "B";

		groupA[0] = new DataCase(79.98, a);
		groupA[1] = new DataCase(80.04, a);
		groupA[2] = new DataCase(80.02, a);
		groupA[3] = new DataCase(80.04, a);
  		groupA[4] = new DataCase(80.03, a);
		groupA[5] = new DataCase(80.03, a);
		groupA[6] = new DataCase(80.04, a);
		groupA[7] = new DataCase(79.97, a);
		groupA[8] = new DataCase(80.05, a);
		groupA[9] = new DataCase(80.03, a);
		groupA[10] = new DataCase(80.02, a);
		groupA[11] = new DataCase(80.00, a);
		groupA[12] = new DataCase(80.02, a);
		//groupA[13] = new DataCase(80.05, a);

		groupB[0] = new DataCase(80.02, b);
		groupB[1] = new DataCase(79.94, b);
		groupB[2] = new DataCase(79.98, b);
		groupB[3] = new DataCase(79.97, b);
		groupB[4] = new DataCase(79.97, b);
		groupB[5] = new DataCase(80.03, b);
		groupB[6] = new DataCase(79.95, b);
		groupB[7] = new DataCase(79.97, b);
   	*/
	public static DataCase[] rankCombinedLists(DataCase[] input) {

		DataCase[][] groups = new DataCase[1][];
		groups[0] = input;
		int groupLength = groups.length;
		int[] nGroup = new int[groupLength]; // holds the number of elements in each group.

		int totalN = 0;
		for (int i = 0; i < groupLength; i++) {
   			nGroup[i] = groups[i].length;
			////System.out.println("i = " + i + ", nGroup["+i+"] = " + nGroup[i]);
			totalN += nGroup[i];
   		}
   		//nTotal = totalN;
   		DataCase[] combo = new DataCase[groupLength];
   		// note that groups already have group names in there.
   		int count = 0;

		for (int i = 0; i < groups.length; i++) {
			for (int j = 0; j < groups[i].length; j++) {
				combo[count] = groups[i][j];
				count++;
			}

		}

		return rankList(combo);
   	}


	public static DataCase[] rankCombinedLists(DataCase[][] groups, String[] groupNames) {
		int groupLength = groups.length;
		int[] nGroup = new int[groupLength]; // holds the number of elements in each group.

		int totalN = 0;
		for (int i = 0; i < groupLength; i++) {
   			nGroup[i] = groups[i].length;
			////System.out.println("i = " + i + ", nGroup["+i+"] = " + nGroup[i]);
			totalN += nGroup[i];
   		}
   		nTotal = totalN;
   		DataCase[] combo = new DataCase[totalN];
   		// note that groups already have group names in there.
   		int count = 0;
		for (int i = 0; i < groups.length; i++) {
			for (int j = 0; j < groups[i].length; j++) {
				combo[count] = groups[i][j];
				count++;
			}

		}
 		/*int nA = groupA.length;
 		int nB = groupB.length;
 		String nameA = groupA[0].getGroup();
 		String nameB = groupB[0].getGroup();


   		DataCase[] combo = new DataCase[nA + nB];
   		////////System.out("combo length = " + combo.length);
   		for (int i = 0; i < nA; i++) {
   			combo[i] = groupA[i];
   		}

   		for (int i = nA; i < nA + nB; i++) {
   			combo[i] = groupB[i - nA];
   		}
   		return rankList(combo);
		*/
		return rankList(combo);
   	}


	public static DataCase[] rankCombinedListsAssignQuantile(DataCase[][] groups, String[] groupNames, String distributionType) {
		int groupLength = groups.length;
		int[] nGroup = new int[groupLength]; // holds the number of elements in each group.

		int totalN = 0;
		for (int i = 0; i < groupLength; i++) {
   			nGroup[i] = groups[i].length;
			////System.out.println("i = " + i + ", nGroup["+i+"] = " + nGroup[i]);
			totalN += nGroup[i];
   		}
   		nTotal = totalN;
   		DataCase[] combo = new DataCase[totalN];
   		// note that groups already have group names in there.
   		int count = 0;
		for (int i = 0; i < groups.length; i++) {
			for (int j = 0; j < groups[i].length; j++) {
				combo[count] = groups[i][j];
				count++;
			}

		}
		return rankListAssignQuantile(combo, distributionType);
   	}

	public static DataCase[] rankCombinedLists(DataCase[] groupA, DataCase[] groupB) {
 		int nA = groupA.length;
 		int nB = groupB.length;
 		String nameA = groupA[0].getGroup();
 		String nameB = groupB[0].getGroup();


   		DataCase[] combo = new DataCase[nA + nB];
   		////////System.out("combo length = " + combo.length);
   		for (int i = 0; i < nA; i++) {
   			combo[i] = groupA[i];
   		}

   		for (int i = nA; i < nA + nB; i++) {
   			combo[i] = groupB[i - nA];
   		}
   		return rankList(combo);

   	}
   	/*
 	public static DataCase[] sortDoubleArray(double[] obs, int[] groups) {
		ArrayList list = new ArrayList()
		for (int i = 0; i < obs.length; i++) {
			list.add(
		}
 		int nA = groupA.length;
 		int nB = groupB.length;
 		String nameA = groupA[0].getGroup();
 		String nameB = groupB[0].getGroup();


   		DataCase[] combo = new DataCase[nA + nB];
   		////////System.out("combo length = " + combo.length);
   		for (int i = 0; i < nA; i++) {
   			combo[i] = groupA[i];
   		}

   		for (int i = nA; i < nA + nB; i++) {
   			combo[i] = groupB[i - nA];
   		}
   		return rankList(combo);

   	}*/

   	public static DataCase[] rankList(DataCase[] combo) {
		/*
     	for (int i = 0; i < combo.length ; i++) {
   			//////////System.out("combo["+i+"] = " + combo[i].getValue() + " " + combo[i].getGroup());
   		}
   		*/
   		//double[] array = {80.02, 79.98, 80.05, 79.97, 80.02, 232.5, 432, 79.95, 102, 233};
   		int rank = 1;
   		double tempRankSum = 0;
   		double tempRankAvg = 0;
   		int tempCount = 0;
   		double current = 0;
   		double prev = 0;
   		int start = 0;
   		int count = 0;

   		double sumSmallerSample = 0;
   		double rPrime = 0;
   		double rStar = 0;
   		int sampleSizeSmaller = 0;

   		try {
			QSortAlgorithm quick = new QSortAlgorithm();
			quick.sort(combo);
			////////System.out("\nAfter Sort");

			combo[0].setRank(rank);

			for (int i = 0; i < combo.length; i++) {
				combo[i].setRank(rank);
				rank++;
				////////System.out(combo[i].getValue() + " " + combo[i].getGroup() + " " + combo[i].getRank());
			}
			// after setting the ranks for all cases, reset those of equal values to the average of ranks.
			current = -1; //combo[0].getValue();
			////////System.out("");//\nCurrent = " + current);
			count = 1;
			double[] avgRank = new double[combo.length];
			tempRankSum = combo[0].getRank();
			boolean avgLastCase = false;

			tieMap = new HashMap<String, String>();
			completeMap = new HashMap<String, String>();
			completeMap.put(combo[0].getValue() + "", "RANK = 1.0, COUNT = 1");
			// this initial value is needed, but if the first one has a tie,
			// the value of HashMap's element will be overwitten in the following loop --
			// the beauty of HashMap.

			for (int i = 1; i < combo.length; i++) {
				//////////System.out("I = " + i + " combo["+i+"] = " + combo[i].getValue() + " tempRankSum = " + tempRankSum);
				prev = combo[i - 1].getValue();
				try {
					current = combo[i].getValue();
				} catch (ArrayIndexOutOfBoundsException e) {
					////////System.out("Caught ArrayIndexOutOfBoundsException = " + e);
				}
					if ( Math.abs(current - prev) < AnalysisUtility.PRECISION_TOLERANCE ) {
							if (i == combo.length - 1)
								avgLastCase = true;
							//////////System.out("\t\t\t\tIn IF i = " + i + " value = " + combo[i].getValue());
							try {
								tempRankSum = tempRankSum + (int) combo[i].getRank();
								avgRank[i] = tempRankSum;
								count++;
							} catch (ArrayIndexOutOfBoundsException e) {
								////////System.out("Caught ArrayIndexOutOfBoundsException = " + e);
							}
					}
					else  {
						//////////System.out("\t\t\t\tIn ELSE i = " + i + " " + " value = " + combo[i].getValue());
						avgRank[i] = combo[i].getRank();
						try {
							tempRankAvg = tempRankSum / count;
							//////System.out.println("COUNT = " + count + " RANK = " + tempRankSum + " AVG = " + tempRankAvg);
							if (count > 1) {
								hasTie = true;
								if (maxNumberTies < count) {
									maxNumberTies = count;
								}
								tieMap.put(combo[i].getValue() + "", "RANK = " + avgRank[i] + ", COUNT = " + count);
							}
							completeMap.put(combo[i].getValue() + "", "RANK = " + avgRank[i] + ", COUNT = " + count);

							for (int j = i; j > i - count; j--) {
								//////////System.out("J = " + j);
								avgRank[j - 1] = tempRankAvg;
								// i has the spirit of "next". So -1.
							}


							count = 1; // reset
							tempRankSum = combo[i].getRank();
							//////////System.out("");

						} catch (ArithmeticException e) {
							////////System.out("ArithmeticException = " + e.toString());
							continue;
							// when count it 0
						}

					}

			} // for loop
			//////System.out.println("When done, avgLastCase = " + avgLastCase + " Count = " + count);
			//////System.out.println("When done, tempRankSum = " + tempRankSum );
			int lastIndex = combo.length - 1;
			if (avgLastCase) {
				tempRankAvg = tempRankSum / count;
				for (int j = lastIndex; j > lastIndex - count; j--) {
					//////System.out.println("J = " + j);
					avgRank[j] = tempRankAvg;
					// j has the spirit of "current". So no -1.
				}
			}

			for (int i = 0; i < combo.length; i++) {
				////System.out.println("AVG RANK["+i+"] = " + avgRank[i]);
				combo[i].setRank(avgRank[i]);
			}


			/*////////System.out("\nAfter assigning ranks");
			for (int i = 0; i < combo.length; i++) {
				////////System.out(combo[i].getValue() + " " + combo[i].getGroup() + " " + combo[i].getRank());
			}*/
			/*
			if (nA < nB) {
				sampleSizeSmaller = nA;
				for (int i = 0; i < combo.length; i++) {
					if ( (combo[i].getGroup()).equals(nameA)) {
						sumSmallerSample = sumSmallerSample + combo[i].getRank();
					}
				}

			}
			else {
				sampleSizeSmaller = nB;
				for (int i = 0; i < combo.length; i++) {
					if ( (combo[i].getGroup()).equals(nameB)) {
						sumSmallerSample = sumSmallerSample + combo[i].getRank();
					}
				}
			}

			rPrime = sampleSizeSmaller * (nA + nB + 1) - sumSmallerSample;
			rStar = Math.min(rPrime, sumSmallerSample);
			////////System.out("sumSmallerSample = " + sumSmallerSample);
			////////System.out("rPrime = " + rPrime);
			////////System.out("rStar = " + rStar);
			*/

		     	for (int i = 0; i < combo.length ; i++) {
		   			//System.out.println("combo["+i+"] = " + combo[i].getValue() + " " + combo[i].getRank()  + " " + combo[i].getGroup());
		   		}

		} catch (Exception e) { // QSortAlgorithm throws some Exception
			e.printStackTrace();
		}
		return combo;
   }


	/*****************************************************************************/
	/* annie chu added 20081007 for fligner-killeen, use normal ******************/
	   	public static DataCase[] rankListAssignQuantile(DataCase[] combo, String distributionType) {
				   		int rank = 1;
	   		double tempRankSum = 0;
	   		double tempRankAvg = 0;
	   		int tempCount = 0;
	   		double current = 0;
	   		double prev = 0;
	   		int start = 0;
	   		int count = 0;

	   		double sumSmallerSample = 0;
	   		double rPrime = 0;
	   		double rStar = 0;
	   		int sampleSizeSmaller = 0;

	   		try {
				QSortAlgorithm quick = new QSortAlgorithm();
				quick.sort(combo);
				////////System.out("\nAfter Sort");

				combo[0].setRank(rank);

				for (int i = 0; i < combo.length; i++) {
					combo[i].setRank(rank);
					rank++;
				}
				// after setting the ranks for all cases, reset those of equal values to the average of ranks.
				current = -1; //combo[0].getValue();
				////////System.out("");//\nCurrent = " + current);
				count = 1;
				double[] avgRank = new double[combo.length];
				tempRankSum = combo[0].getRank();
				boolean avgLastCase = false;

				tieMap = new HashMap<String, String>();
				completeMap = new HashMap<String,String>();
				completeMap.put(combo[0].getValue() + "", "RANK = 1.0, COUNT = 1");
				// this initial value is needed, but if the first one has a tie,
				// the value of HashMap's element will be overwitten in the following loop --
				// the beauty of HashMap.
				int numberOfUnique = 1; // first set
				for (int i = 1; i < combo.length; i++) {
					//////////System.out("I = " + i + " combo["+i+"] = " + combo[i].getValue() + " tempRankSum = " + tempRankSum);
					prev = combo[i - 1].getValue();
					try {
						current = combo[i].getValue();
					} catch (ArrayIndexOutOfBoundsException e) {
						////////System.out("Caught ArrayIndexOutOfBoundsException = " + e);
					}
					if ( Math.abs(current - prev) < AnalysisUtility.PRECISION_TOLERANCE ) {
						// if two numbers are considered equal
						if (i == combo.length - 1)
							avgLastCase = true;
						//////////System.out("\t\t\t\tIn IF i = " + i + " value = " + combo[i].getValue());
						try {
							tempRankSum = tempRankSum + (int) combo[i].getRank();
							avgRank[i] = tempRankSum;
							count++;
						} catch (ArrayIndexOutOfBoundsException e) {
							////////System.out("Caught ArrayIndexOutOfBoundsException = " + e);
						}

					}
					else  {
						//////////System.out("\t\t\t\tIn ELSE i = " + i + " " + " value = " + combo[i].getValue());
						avgRank[i] = combo[i].getRank();
						try {
							tempRankAvg = tempRankSum / count;
							//////System.out.println("COUNT = " + count + " RANK = " + tempRankSum + " AVG = " + tempRankAvg);
							if (count > 1) {
								hasTie = true;
								if (maxNumberTies < count) {
									maxNumberTies = count;
								}
								tieMap.put(combo[i].getValue() + "", "RANK = " + avgRank[i] + ", COUNT = " + count);
								//System.out.println("put to tieMap i = " + i + " RANK = " + avgRank[i] + ", COUNT = " + count);
							}
							numberOfUnique++;


							completeMap.put(combo[i].getValue() + "", "RANK = " + avgRank[i] + ", COUNT = " + count);

							//System.out.println("put to completeMap i = " + i + " "  + combo[i].getValue()+" RANK = " + avgRank[i] + ", COUNT = " + count);
							for (int j = i; j > i - count; j--) {
								//////////System.out("J = " + j);
								avgRank[j - 1] = tempRankAvg;
								// i has the spirit of "next". So -1.
							}


							count = 1; // reset
							tempRankSum = combo[i].getRank();
							//////////System.out("");

						} catch (ArithmeticException e) {
							////////System.out("ArithmeticException = " + e.toString());
							continue;
							// when count it 0
						}

					}

				} // for loop
				/////System.out.println("numberOfUnique = " + numberOfUnique);

				//System.out.println("When done, avgLastCase = " + avgLastCase + " Count = " + count);
				//System.out.println("When done, tempRankSum = " + tempRankSum );
				int lastIndex = combo.length - 1;
				if (avgLastCase) {
					tempRankAvg = tempRankSum / count;
					for (int j = lastIndex; j > lastIndex - count; j--) {
						//////System.out.println("J = " + j);
						avgRank[j] = tempRankAvg;
						// j has the spirit of "current". So no -1.
					}
				}
				/*
				for (int i = 0; i < numberOfUnique; i++) {
					if (distributionType.equalsIgnoreCase(FLIGNER_KILLEEN_NORMAL)) {
						combo[i].setFlignerKilleenNormalQuantile(Utility.getFlignerKilleenNormalQuantile(numberOfUnique, (i+1))); // i is counted from one
						System.out.println("i = " + i + " getFlignerKilleenNormalQuantile = " + Utility.getFlignerKilleenNormalQuantile(numberOfUnique, (i+1)) );

					}
				}
				*/
				for (int i = 0; i < combo.length; i++) {
				/////System.out.println("AVG RANK["+i+"] = " + avgRank[i]);
					combo[i].setRank(avgRank[i]);
					// if more distribution is used, should set some kind of if-else...
					if (distributionType.equalsIgnoreCase(FLIGNER_KILLEEN_NORMAL)) {
						combo[i].setFlignerKilleenNormalQuantile(Utility.getFlignerKilleenNormalQuantile(combo.length, avgRank[i])); // i is counted from one

					}
				}

			/*	System.out.println("number of without ties = " + tieMap.size());
				for (int i = 0; i < combo.length ; i++) {
					System.out.println("combo["+i+"] = " + combo[i].getValue() + " " + combo[i].getRank()  + " " + combo[i].getGroup() + " rank = " + combo[i].getRank() + ", score = " + combo[i].getFlignerKilleenNormalQuantile());
				}*/

			} catch (Exception e) { // QSortAlgorithm throws some Exception
				e.printStackTrace();
			}
			return combo;
     }
     /*************************************************************************************/
	public static HashMap getTieMap() {
		return tieMap;
	}


	public static HashMap getCompleteMap() {
		return completeMap;
	}

	public static boolean hasTie() {
		return hasTie;
	}
	public static int getMaxNumberTies() {
		return maxNumberTies;
	}
	public static int getTotalCount() {
		return nTotal;
	}
/*

      public static void main(String args[]) {
      // example from Rice page 390. 405

      	int nA = 13; int nB = 8;

      	//DataCase dataCase = new DataCase(1, "A");

      	DataCase[] groupA = new DataCase[nA];
      	DataCase[] groupB = new DataCase[nB];
   		String a = "A";
   		String b = "B";

   		groupA[0] = new DataCase(79.98, a);
   		groupA[1] = new DataCase(80.04, a);
   		groupA[2] = new DataCase(80.02, a);
   		groupA[3] = new DataCase(80.04, a);
     	groupA[4] = new DataCase(80.03, a);
   		groupA[5] = new DataCase(80.03, a);
   		groupA[6] = new DataCase(80.04, a);
   		groupA[7] = new DataCase(79.97, a);
   		groupA[8] = new DataCase(80.05, a);
   		groupA[9] = new DataCase(80.03, a);
   		groupA[10] = new DataCase(80.02, a);
   		groupA[11] = new DataCase(80.00, a);
   		groupA[12] = new DataCase(80.02, a);
   		//groupA[13] = new DataCase(80.05, a);

   		groupB[0] = new DataCase(80.02, b);
   		groupB[1] = new DataCase(79.94, b);
   		groupB[2] = new DataCase(79.98, b);
   		groupB[3] = new DataCase(79.97, b);
   		groupB[4] = new DataCase(79.97, b);
   		groupB[5] = new DataCase(80.03, b);
   		groupB[6] = new DataCase(79.95, b);
		groupB[7] = new DataCase(79.97, b);
		DataCase[] combo = rankCombinedLists(groupA, groupB);

		//////////System.out("\nIn Main After assigning ranks");
		for (int i = 0; i < combo.length; i++) {
			////////System.out(combo[i].getValue() + " " + combo[i].getGroup() + " " + combo[i].getRank());
		}

	}
*/
	public static final String FLIGNER_KILLEEN_NORMAL = "FLIGNER_KILLEEN_NORMAL";


	public static void main(String[] args) {
		double[] test = {32, 34, 32, 25, 11, 20, 19, 17, 35, 9, 6, 10};
		try {
			//////System.out.println("max = " + QSortAlgorithm.max(test));
			//////System.out.println("min = " + QSortAlgorithm.min(test));
		} catch (Exception e) {
		}

	}
}
