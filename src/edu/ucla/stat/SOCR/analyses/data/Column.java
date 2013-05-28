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

import java.util.ArrayList;
// the data type of Column is actually array of primaries or Object
// But this is hidden from the caller.
// USE THIS SO EVEN ArrayList is replaced by another data structure, only need to mofify this file.
public class Column extends java.util.ArrayList {
	private String name = null; // name of the Column such as age, gender, etc.
	private int length;
	private String dataType; // such as quantitative, ordinal, etc.
	//private Object dataPrimaryArray; // THIS IS ACTUALLY primary array
	//private Object[] dataObjectArray;// THIS IS ACTUALLY Object array
	//private byte[] factorArray;
	private double[] doubleArray;
	private int[] intArray;
	private String[] stringArray; // declare string separately for create new var.
	private Object[] objectArray;


	// this will take array of primaries
	public Column(String[] data, String dataType) {
		this.dataType = dataType;
		this.stringArray = data;

	}
	public Column(double[] data, String dataType) {
		this.dataType = dataType;
		this.doubleArray = data;

	}
	//public Column(int[] data, String dataType) {
	//	this.dataType = dataType;
	//	this.intArray = data;
	//}
	// this constructor will take array of Objects such as String
	public Column(Object[] data, String dataType) {
		this.dataType = dataType;
		this.objectArray = data;

	}
	public String getDataType() {
		return dataType;
	}
	public String[] getStringArray() {
		return stringArray;
	}
	public double[] getDoubleArray() {
		return doubleArray;
	}
	public Object[] getObjectArray() {
		return objectArray;

	}

/*

	public static Column objectArrayToColumn(Object[] array) {
	}
	public static Column intArrayToColumn(int[] array) {
	}



	public Column(long[] data) {
		// change data to ArrayList
	}
	public Column(double[] data) {
			// change data to ArrayList
	}
	public Column(float[] data) {
			// change data to ArrayList
	}
	public Column(char[] data) {
			// change data to ArrayList
	}
	public Column(Object[] data) {
				// change data to ArrayList
	}
*/

	public void setName() {

		//this.name = name;
	}
	public String getName() {
		return this.name;
	}
	/*
	public int getLength() {
		int primaryLength = dataPrimaryArray.length();
		int objectLength = dataObjectArray.length();
		return (primaryLength>objectLength)?primaryLength:objectLength;
		//return column.size();
	}*/
	public boolean isEmpty() {
		return false;
		//return column.isEmpty();
	}


}
