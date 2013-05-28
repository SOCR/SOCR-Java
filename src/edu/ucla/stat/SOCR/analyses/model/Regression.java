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
package edu.ucla.stat.SOCR.analyses.model;

import java.util.*;
import edu.ucla.stat.SOCR.analyses.exception.*;

public class Regression{

	public static double mean(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double total = 0;
		for (int i = 0; i < sampleSize; i++) {
			total = total + data[i];
		}
		return total/sampleSize;

	}

	public static double[] diff(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double mean = mean(data);
		double[] diff = new double[sampleSize];
		for (int i = 0; i < sampleSize; i++) {
			diff[i] = data[i] - mean;
			//System.out.println(diff[i]);
		}
		return diff;

	}
	public static double sumOfSquares(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		double mean = mean(data);
		double sum = 0;
		for (int i = 0; i < sampleSize; i++) {
			sum = sum + (data[i] - mean) * (data[i] - mean) ;

		}
		return sum;

	}
	public static double sampleVariance(double[] data) throws DataIsEmptyException {
		int sampleSize = data.length;
		if (sampleSize <= 0) throw new DataIsEmptyException("There is no data.");
		return sumOfSquares(data) / (sampleSize - 1);


	}
	public static double sampleCovariance(double[] dataX, double[] dataY) throws DataIsEmptyException {
		int sampleSizeX = dataX.length;
		int sampleSizeY = dataY.length;
		if ((sampleSizeX <= 0) || (sampleSizeY <= 0) || (sampleSizeX != sampleSizeY))
			throw new DataIsEmptyException("There is no data or uneven X and Y.");
		double meanX = mean(dataX);
		double meanY = mean(dataY);
		double sum = 0;
		for (int i = 0; i < sampleSizeX; i++) {
			sum = sum + (dataX[i] - meanX) * (dataY[i] - meanX) ;

		}
		return sum/(sampleSizeX - 1) ;

	}
	public static LinkedHashSet<String> levelFactor(String[] x) {
		//byte level = 0;
		int length = x.length;
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		for (int i = 1; i < length; i++) {
			//String temp = (x[i]+"").toString();
			set.add(x[i]);
		}
		return set;
	}


	//public static adjustToMean(double[] data) throws DataIsEmptyException {
	//}
}
