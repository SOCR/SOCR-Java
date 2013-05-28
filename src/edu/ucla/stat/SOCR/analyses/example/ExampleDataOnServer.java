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

package edu.ucla.stat.SOCR.analyses.example;

import java.lang.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.StringTokenizer;
//import edu.ucla.stat.SOCR.analyses.data.DataFrame;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.model.*;

/** Example data generator for ANOVA & other statistical applets. */
public class ExampleDataOnServer {
	public String[][] example = new String[1][1];	// the example data
	public String[] columnNames = new String[1];
	String newln = System.getProperty("line.separator");
		 // system-independent line terminator
	public JTable dataTable;
	private URL dataFileURL;
	// need to write bunch of file int for the cases.

	/** Constructor method for simple data generation for regression/correlation tests*/

	public  ExampleDataOnServer() {
	}
/*
	private void test() {

			Data data = RandomExample.simpleRandomRegression(10);
			int sampleSize = data.getSampleSize();
			example = new String[sampleSize][2];
			columnNames = new String[2];

			columnNames[0]="X";
			columnNames[1] = "Y";

				byte result[];
				String fileIn  = ServletConstant.HOST_NAME + "SOCR/test.txt";
				//System.out.println("FILE NAME IN = " + fileIn);
				example = new String[100][20];
				columnNames = new String[20];

				try {
					dataFileURL = new URL(fileIn);

    				//System.out.println("URL to String = " + dataFileURL.toString());
					////System.out.println("Byte Data path" + classFile.toString());
					InputStream ips = dataFileURL.openStream();
					//DataInputStream in = dataFileURL.openStream();
					//System.out.println("ips = " + ips);
					FileInputStream fi = new FileInputStream(dataFileURL.toString());
					DataInputStream in = new DataInputStream(ips);
					////System.out.println("fi = " + fi);
					result = new byte[ips.available()];
					fi.read(result);
					int line = ips.read(result);
					//System.out.println("line = " + line);
					//return result;

				} catch (Exception e) {
					e.printStackTrace();
				}
			dataTable = new JTable(example, columnNames);
	}
*/

	 public static void main(String args[]) {

	 	//ExampleDataOnServer example = new ExampleDataOnServer();
	 	//example.test();
     	}
}
