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

import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.*;

// a list of probabilities, for n catagories, double[] dimension of n - 1,
// the last one is comptued by 1 - sum of the double[] entries.

public class ProbabilityList {
	private double[] prob;
	private int probLength;
	public ProbabilityList(double[] input) throws DataException{
		if (input == null || input.length == 0) {
			throw new DataException("input is null or of length 0.");
		}
		probLength = input.length + 1;
		double complement = 0;
		for (int i = 0; i < input.length; i++) {
			complement += input[i];
			//System.out.println(" input[i] = " +  input[i]);
			//System.out.println("complement = " + complement);

			if (complement > 1) {
				throw new DataException("sum of probabilities larger than 1.");
			}
		}
		prob = new double[probLength];
		for (int i = 0; i < input.length; i++) {
			prob[i] = input[i];
			System.out.println("prob["+i+"] = " + prob[i]);
		}
		prob[input.length] = 1 - complement;
		System.out.println("prob[input.length] = " + prob[input.length]);


	}


	public static void main(String[] args) {
		double[] test = {.3, .3, .1};
		try {
			new ProbabilityList(test);
		} catch (Exception e) {
			System.out.println("e = " + e);
		}

	}
}
