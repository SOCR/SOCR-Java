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

import edu.ucla.stat.SOCR.modeler.NormalFit_Modeler;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.exception.DataIsEmptyException;
import edu.ucla.stat.SOCR.analyses.result.Result;
import edu.ucla.stat.SOCR.analyses.model.*;


public class RandomExample {
	public static Data simpleRandomRegression(int sampleSize) {
		NormalFit_Modeler dummy = new NormalFit_Modeler();
		double[] error = dummy.generateSamples(sampleSize);
		double[] independentVar = new double[sampleSize];
		double[] dependentVar = new double[sampleSize];
		double correlation  = Math.random(); // set other fized values for testing
		double varianceDependent = Math.abs(Math.random());
		for (int i = 0; i < sampleSize; i++) {
			independentVar[i] = Math.random();
		}
		double varianceIndependent = 0;
		try {
			varianceIndependent = AnalysisUtility.sampleVariance(independentVar);
		} catch (DataIsEmptyException e) {
		}
		double alpha = Math.random();

		double beta = ( correlation * Math.sqrt(varianceDependent)) / (Math.sqrt(varianceIndependent) );
		Data data = new Data();
		data.appendX(independentVar, DataType.QUANTITATIVE);
		data.appendY(dependentVar, DataType.QUANTITATIVE);
		for (int i = 0; i < sampleSize; i++) {
			dependentVar[i] = alpha + beta * error[i];
		}
		return data;
	}
	
	public static Data logisticRegression(int sampleSize) {
		double[] independentVar = new double[sampleSize];
		double[] dependentVar = new double[sampleSize];
		Data data = new Data();
		data.appendX(independentVar, DataType.QUANTITATIVE);
		data.appendY(dependentVar, DataType.QUANTITATIVE);
		
		for (int i = 0; i < sampleSize; i++) {
			independentVar[i] = Math.random();
			dependentVar[i] = (int)(Math.random()+0.499);
		}
		return data;
	}

	public static Data anovaOneWay(int sampleSize, int numberGroups) {
		NormalFit_Modeler dummy = new NormalFit_Modeler();
		double[] error = dummy.generateSamples(sampleSize);
		String[] independentVar = new String[sampleSize];
		double[] dependentVar = new double[sampleSize];
		//double[] dependentVar = new double[sampleSize];
		double correlation  = Math.random();
		double varianceDependent = Math.abs(Math.random());
		for (int i = 0; i < sampleSize; i++) {
			dependentVar[i] = Math.random();
			independentVar[i] =  ((Math.random() > 0.5) ? 1 : 2 ) + "";
		}
		Data data = new Data();

		data.appendX(independentVar, DataType.FACTOR);
		data.appendY(dependentVar, DataType.QUANTITATIVE);

		return data;
	}

	public static Data oneTRandom(int sampleSize) {
		NormalFit_Modeler dummy = new NormalFit_Modeler();
		double[] error = dummy.generateSamples(sampleSize);
		double[] dependentVar = new double[sampleSize];
		//double[] dependentVar = new double[sampleSize];
		double correlation  = Math.random();
		double varianceDependent = Math.abs(Math.random());
		for (int i = 0; i < sampleSize; i++) {
			dependentVar[i] = Math.random();
		}
		Data data = new Data();

		data.appendX(dependentVar, DataType.QUANTITATIVE);
		return data;
	}
	public static void main(String args[]) {
		int sampleSize = 10;
		Data data = RandomExample.simpleRandomRegression(sampleSize);
		try {
			Result result = data.getAnalysis(AnalysisType.SIMPLE_LINEAR_REGRESSION);
		} catch (Exception e) {
		}
	}

}
