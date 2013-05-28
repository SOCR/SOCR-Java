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
package edu.ucla.stat.SOCR.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class Frequency {
	private double[] data = null;
	private int[] freq = null;
	private int freqSize = 0;
	private int sampleSize = 0;
	private HashMap<String,Integer> map = new HashMap<String,Integer>();
	private HashSet<String> set = new HashSet<String>();
	private double maxRelFreq = -1;

	public Frequency(double[] data) {
		this.data = data;
		sampleSize = data.length;
		for (int i = 0; i < sampleSize; i++) {
			add(data[i]);
		}
	}

	public void add(double newMember) {
		//Double.parseDouble((String)yList.get(i));
		int count = 0;
		String id = newMember + "";
		//////System.out.println("add id = " + id);
		if (map.get(id) == null) {
			map.put(id, new Integer(1));
			set.add(id);
			freqSize++;
		}
		else {
			////////System.out.println("add else map.get(id)= " + (String)map.get(id));

			count = ((Integer)map.get(id)).intValue();
			count++;
			map.put(id, new Integer(count));

		}
	}
	public HashMap<String,Integer> getMap() {
		return map;
	}
	public HashSet<String> getSet() {
		return set;
	}
	public int getFreqSize() {
		return freqSize;
	}
	public int[] getFreqArray() {
		return freq;
	}
	public int[] getRelFreqArray() {
		return freq;
	}
	public int getFrequency(double input) {
		String id = input + "";
		try {
			return ((Integer)map.get(id)).intValue();  
			// OLD:    Integer.parseInt((String)map.get(id));
		} catch (Exception e) {
			return -1;
		}

	}

	public void computeFrequency() {
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		int freqSize = map.size();
		int dataCount = -1;
		double dataCountDouble = 0;
		double sampleSizeDouble = sampleSize;

		String id = null;
		while (iterator.hasNext()) {
			id = (String)iterator.next();
			dataCount = ((Integer)map.get(id)).intValue();
			dataCountDouble = dataCount;
			maxRelFreq = Math.max(maxRelFreq, dataCountDouble/sampleSizeDouble);
			////System.out.println("printFrequency data = " + id + ", count = " + dataCount);
			////System.out.println("printFrequency dataCountDouble/sampleSizeDouble = " +dataCountDouble/sampleSizeDouble);

		}

	}

	public void printData() {
		//Set keySet = map.keySet();
		//Iterator iterator = keySet.iterator();

		String keys = "";
		String id = null;
		int count = -1;
		for (int i = 0; i < data.length; i++) {
			id = data[i] + "";
			count = ((Integer)map.get(id)).intValue();
			//////System.out.println("data[" + i + "] = " + id + ", count = " + count);
		}

		//while (iterator.hasNext()) {

		//	keys = (String) iterator.next();


	}
	public double getMaxRelFreq() {
		return this.maxRelFreq;
	}
	public static void main(String[] args) {
		double[] test = {1, 1.000, 2.000, 2, 2, 3, 4, 5, 6};
		Frequency f = new Frequency(test);
		f.computeFrequency();


	}
}
