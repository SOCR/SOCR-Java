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

import edu.ucla.stat.SOCR.modeler.NormalFit_Modeler;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.core.Distribution;

import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException;

import java.util.HashMap;


public class RandomGenerator {
	// reserve int 0 to 99 for continuous distributions.
	// reserve int 100 to 199 for discrete distributions.

	public static final int NORMAL = 0;
	public static final int POISSON = 100;

	// double[] input -- is the raw data.
	// int distribution -- is the distribution you think input follows. (your guess)
	public static double[] getGeneratedArray(double[] input, int distribution) throws DataIsEmptyException { // throws exception if the input is null.

		double mean = AnalysisUtility.mean(input);
		//double sampleVar = AnalysisUtility.sampleVariance(input);
		double sampleVar = AnalysisUtility.variance(input);
		int sampleCount = input.length;
		Distribution distGenerator = null;
		double[] dat = new double[sampleCount];

		switch (distribution) {
			case(NORMAL): {
				distGenerator = new edu.ucla.stat.SOCR.distributions.NormalDistribution(mean,Math.sqrt(sampleVar));
				for(int i = 0; i < sampleCount;i++)
					dat[i] = distGenerator.simulate();
			//	printStatistics(dat, distGenerator);
				break;
			}
			case(POISSON): {
				distGenerator = new edu.ucla.stat.SOCR.distributions.PoissonDistribution(mean);
				for(int i = 0; i < sampleCount;i++)
					dat[i] = distGenerator.simulate();
			//	printStatistics(dat, distGenerator);
				break;
			}

			default: {
				dat = null; // means the distribution was not specified by the user and therefore nothing can be provided.
			}
		}
		return dat;

	}
	private static void printStatistics(double[] dat, Distribution distGenerator) {
		try {
			System.out.println("distGenerator.getMean         = " + distGenerator.getMean());
			System.out.println("distGenerator.getVariance     = " + distGenerator.getVariance());

			System.out.println("simulated.mean                = " + AnalysisUtility.mean(dat));
			System.out.println("simulated.variance            = " + AnalysisUtility.variance(dat));
		} catch (Exception e) {
		}
	}
	public static void main(String args[]) {
		//double[] input = new double[] {-0.4373311279109722,1.0438559366194091,-1.1863006719793074,2.825198963598562,0.42872269398014207,2.0210135812262946,.8014432824000435,-0.0595595607316326,0.9884208939399594,1.1231446455397573};
		NormalFit_Modeler dummy = new NormalFit_Modeler();
		int sampleSize = 200;
		double[] input = dummy.generateSamples(sampleSize);
		try {
			double[] numbers = RandomGenerator.getGeneratedArray(input, POISSON);

			//for (int i = 0; i < input.length; i++) {
			//	System.out.println(numbers[i]);
			//}
		} catch (DataIsEmptyException e) {
		}

	}
}
