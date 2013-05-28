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

import edu.ucla.stat.SOCR.analyses.exception.DataException;

public class RegressionLine {
	private double intercept;
	private double slope;
	private double sampleVarX;
	private double sampleVarY;
	private double sampleCov;
	private double[] x;
	private double[] y;
	// x is the independent variable.
	// y is the dependent variable.
	// DataException is thrown when x and/or y is null. (by AnalysisUtility)
	public RegressionLine(double[] x, double[] y) throws DataException {
		this.x = x;
		this.y = y;

		sampleVarX = AnalysisUtility.sampleVariance(x);
		sampleCov = AnalysisUtility.sampleCovariance(x, y);
		slope = sampleCov / sampleVarX;
		intercept = AnalysisUtility.mean(y) - slope * AnalysisUtility.mean(x);

	}
	public double getIntercept(){
		return this.intercept;
	}
	public double getSlope() {
		return this.slope;
	}
	// test data from Dr. Jennrich's book.
	public static void main(String args[]) {
		double[] x = new double[] {68,49,60,68,97,82,59,50,73,39,71,95,61,72,87,40,66,58,58,77};
		double[] y = new double[] {75,63,57,88,88,79,82,73,90,62,70,96,76,75,85,40,74,70,75,72};
		RegressionLine line = null;

		// note that X goes first, Y goes econd.
		try {
			line = new RegressionLine(x, y);
			System.out.println("Intercept = " + line.getIntercept());
			System.out.println("Slope = " + line.getSlope());
		} catch (Exception e) {
		}
	}
}


