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
// to store a list of those survival objects of the same time (same result of getTime.).

package edu.ucla.stat.SOCR.analyses.data;


import edu.ucla.stat.SOCR.util.*;
import java.util.*;

   public class SurvivalSingleList extends SurvivalList{
		private int count = 0;
		private int numberAtRisk = 0;
		private boolean allAreCensored = true;
		private int numberCensored = 0;
		private int numberDead = 0;

		//private SurvivalObject[] objectArray = null;
		//public SurvivalSingleList() {

		//}
		public SurvivalSingleList(String groupName) {
			super(groupName);

		}
		//public SurvivalSingleList(SurvivalObject[] objectArray) {
		//	count = objectArray.length;
		//	this.objectArray = objectArray;
		//}
		//public SurvivalSingleList(ArrayList objectList) {
		//	count = objectList.count();
		//	this.objectList = objectList;
		//}
		public int getSize() {
			count = objectList.size();
			return count;
		}
		public void setSize(int input) {
			count = input;
		}
		public ArrayList getObjectList() {
			return objectList;
		}
		public void add(SurvivalObject object) {
			objectList.add(object);
		}

		//public void addObject(SurvivalObject object) {
		//	objectList.add(object);
		//}
		public void examList() {
			count = objectList.size();
			listToSurvivalObjectArray();
			numberCensored = 0;
			numberDead = 0;
			allAreCensored = true;
			////System.out.println("continue examList  " + groupName + " objectList.size() = " + objectList.size() );
			////System.out.println("continue examList objectArray = " + objectArray);
			for (int i = 0; i < objectArray.length; i++) {
				if (objectArray[i].getCensor() == SurvivalObject.CENSORED_CONSTANT) {
					numberCensored++;
				}
				else {
					numberDead++;
				}

			}
			if (numberCensored == count) {
				allAreCensored = true;
			}
			else {
				allAreCensored = false;
			}
			//objectArray =
		}
		public int getNumberCensored() {
			return numberCensored;


		}
		public int getNumberDead() {
			return numberDead;
		}
		public boolean allAreCensored() {
			return allAreCensored;
		}
		public void printList() {
			super.printList();
			examList();
			//count = getSize();
			////System.out.print("count = " + count + ", getNumberCensored = " + getNumberCensored() + ", getNumberDead = " + getNumberDead() + ", allAreCensored = " + allAreCensored() );


			////System.out.println("\ngetSurvivalRate = " + getSurvivalRate());
		}
		public double getSurvivalRate() {
			return (double)numberDead / (double) count;
		}


   }
