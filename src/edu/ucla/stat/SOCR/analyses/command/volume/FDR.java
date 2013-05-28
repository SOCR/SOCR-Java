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
package edu.ucla.stat.SOCR.analyses.command.volume;
import java.io.*;
import java.util.*;

import edu.ucla.stat.SOCR.util.QSortAlgorithm;

/*
 * This class provides a tester/driver for the FDR class
 * 
  * Documentation: 
 * 	http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume1Sample_T_test
 * 
 */

public class FDR {
	double pValueThreshold, nonParamThreshold;
	protected double [] data;
	
	/**
	* Create a new FDR object.
	*
	* @param p a double array, the vector of p-values
	* @param q a double, the False Discovery Rate level (e.g., 0.05)
	*/
	public FDR(double[] p, double q) {
		data = new double [p.length];
		for (int i=0; i<data.length; i++) data[i] = p[i];
		
		QSortAlgorithm quick = new QSortAlgorithm();
		try { 
			quick.sort(p);
		} catch (Exception e) {
			System.out.println("FDR QuickSort Exception e = " + e);
		}
			
		int v = p.length;
		double cvn = 0;
		for (int i=0; i<v; i++) {
			cvn += 1.0/(i+1);
		}

		double cvid = 1;

		double[] test1 = new double[v];
		double[] test2 = new double[v];
		//System.out.println("Index\t Data\t Test1\t Test2\n");
		for (int i=0; i<v; i++) {
			test1[i] = (i+1)/(double)v*q/cvid;
			test2[i] = (i+1)/(double)v*q/cvn;
			//System.out.println(i+"\t"+data[i]+"\t"+test1[i]+"\t"+test2[i]);
		}
		
		int ixt = findlemax(p, test1);
		if (ixt != -1) pValueThreshold = p[ixt];
		int ixn = findlemax(p, test2);
		if (ixn != -1) nonParamThreshold = p[ixn];
	}


	/**
	* @return p-value threshold based on independence or positive dependence
	*/
	public double getThreshold() {
		return pValueThreshold;
	}

	/**
	* @return Thresholded Array using threshold based on independence or positive dependence
	*/
	public double[] getThresholdedArray() {
		double threshold = getThreshold();
		double [] thresholdedData = new double [data.length];
		for (int i=0; i<thresholdedData.length; i++) thresholdedData[i] = data[i];
		
		for (int i=0; i<thresholdedData.length; i++) {
			//if (thresholdedData[i]==0 || Float.isNaN((float)thresholdedData[i]) || 
			//		Float.isInfinite((float)thresholdedData[i])) thresholdedData[i] = 0.0;
			//else 
			if (thresholdedData[i]>threshold) thresholdedData[i] = 1;
		}
		return thresholdedData;
	}

	/**
	* @return Nonparametric p-value threshold
	*/
	public double getNThreshold() {
		return nonParamThreshold;
	}

	/**
	* @return Thresholded Array using Nonparametric p-value threshold
	*/
	public double[] getThresholdedArrayNP() {
		double threshold = getNThreshold();
		double [] thresholdedDataNP = new double [data.length];
		for (int i=0; i<thresholdedDataNP.length; i++) thresholdedDataNP[i] = data[i];
		
		for (int i=0; i<thresholdedDataNP.length; i++)
			if (thresholdedDataNP[i]>threshold) thresholdedDataNP[i] = 1;

		return thresholdedDataNP;
	}


	int findlemax(double[] a, double[] b) {
		final int len = a.length;
		int ix = -1;
		
		//System.out.println("findlemax!!!!\nIndex\t Data\t Test\t ix\n");
		for (int i=0; i<len; i++) {
			if (a[i] <= b[i]) ix=i;
			//System.out.println(i+"\t"+a[i]+"\t"+b[i]+"\t"+ix);
		}
		return ix;
	}
}
