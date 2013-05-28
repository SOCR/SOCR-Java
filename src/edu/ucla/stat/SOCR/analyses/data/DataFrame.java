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

import java.util.Set;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.Set;

public class DataFrame {
	String[] header;
	String[][] data;
	static int casePosition = 0;
	static int varLength = 0;
	static int sampleSize = 0;
	public DataFrame(String headerRow, int sampleSize) {
     	StringTokenizer st = new StringTokenizer(headerRow.trim());
     	int varPosition = 0;
     	int length = 0;
     	while (st.hasMoreTokens()) {
			st.nextToken();
     		varLength++;
     	}
     	header = new String[varLength];
     	st = new StringTokenizer(headerRow.trim());
     	while (st.hasMoreTokens()) {
		     header[varPosition] = st.nextToken();
		     varPosition++;
     	}
     	data = new String[varLength][sampleSize];

	}

	public void addRow(String row) {
     	StringTokenizer st = new StringTokenizer(row.trim());
     	int varPosition = 0;
     	//data[varPosition] = new String[sampleSize];
     	while (st.hasMoreTokens()) {
     		data[varPosition][casePosition] = st.nextToken();
     		varPosition++;
     	}
     	casePosition++;
	}
	public String[] getHeader() {
		return header;
	}
	public String[][] getData() {
		return data;
	}
	public int getSampleEize() {
		return sampleSize;
	}
	public static void main(String args[]) {
		int currentSampleSize = 10;
		DataFrame dataFrame = new DataFrame("sex age edu ", currentSampleSize);
		String[] currentHeader = dataFrame.getHeader();
		for (int i = 0; i < currentHeader.length; i++) {
			//System.out.println("this line = " + currentHeader[i]);
		}
		//System.out.println(currentHeader.length);
	}
}
