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

package edu.uah.math.experiments;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the point estimation problem associated with the Uniform distribuiton
* on the interval [0, 1].
* 
* This class implements an exercise proposed by Gnedenko (1978; p. 194) 
* that shows that, if X1, X2, X3, . . . are IID Uniform(0, l), 
* if S = \sum_{i=1}^{n} {Xi},and if U is the minimum value of n for which S > 1 , 
* then E(U) = e (~2.7182....).
* 
* @author Ivo Dinov
* @version December 19, 2007
*/
public class Uniform_E_EstimateExperiment extends EstimateExperiment implements Serializable{
	//Variables
	private double a = 1, u, uBias, uMSE;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "U (estimate of 'e')"});
	private ContinuousUniformDistribution dist = new ContinuousUniformDistribution(0, a);
	private RandomVariable rv = new RandomVariable(dist, "X");
	private StatisticsTable uTable = new StatisticsTable("U", new String[]{"Bias", "MSE"});

	/**
	* This method initializes the experiment by calling the initialization method
	* in the superclass,and by setting up the toolbar containing the parameter scrollbar
	* and label.
	*/
	public void init(){
		super.init(rv);
		setName("Uniform E-Estimate Experiment");
		//Record Table
		recordTable.setDescription("U");
		addComponent(recordTable, 0, 1, 1, 3);
		//U Table
		uTable.setDescription("Bias and mean square error of U as an estimator of e");
		addComponent(uTable, 1, 1, 1, 1);
		stopChoice.setToolTipText("Number of Times to run the experiment of *n* trials!");
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return "\nThe SOCR Resource: www.SOCR.ucla.edu \n"
			+ "http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_Activities_Uniform_E_EstimateExperiment \n\n"
			+ "This experiment is to generate a random sample X1, X2, ..., Xn of size n from the Uniform\n"
			+ "distribution on (0, 1). The distribution density is shown in blue in the graph, and on\n"
			+ "each update, the sample density is shown in red. On each update, the following statistic\n"
			+ "is recorded: U = minimum n for which the sum S = X1+X2+...+Xn > 1\n"
			+ "The expected value of U is approximately equal to the natural number *e~2.7182....*\n"
			+ "Therefore, if we average the smallest indices k, within the sample of n, for which\n"
			+ "X1+X2+...+Xk > 1, we will estimate the E(U) and thus obtain an approximation to *e*.\n\n"
			+ "If *n* is small (e.g., 1, 2), then the estimates for *e* will be roughly integers and\n"
			+ "we may actually need to sample more than *n* Uniform variables to ensure their sum > 1.\n\n"
			+ "If *n* is large (e.g., n>9), then the *e* estimates are obtained by averaging the indices\n"
			+ "k1, k2, k3, etc. where k1+k2+k3+ ... ~= *n* and X1+X2+...+Xkl > 1. ";
	}

	/**
	* This method defines the experiment. A sample from the uniform distribution
	* is simulated and the estimate of U is computed. The empirical bias and mean
	* square error are also computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		resetSample();
		int n = getSampleSize(), runs = getTime();
		int i = 0, j=0, counter=0; 
		double S = 0.0, var=0.0;
		
		u=0;
		
		for (i=0; i<n; i++) { 
			j=0;
			while (S < 1) {
				rv.sample();
				S += rv.getValue();
				j++;
			}
			u += j;			// mean estimate
			var += j*j;		// variance estimate
			
			counter++;
			S=0;
			i += j;
		}
		
		u /= counter;	// Sample average estimates the expectation E(U) ~ e!
		var /= counter;
		
		double sd = Math.sqrt( var - u*u );
		
		uBias = Math.E - u;
		uMSE =  1.96*sd/Math.sqrt(n);
	}

	/**
	* This method updates the experiment, including the record table, the
	* statistic table, and the random variable graph.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), u});
		uTable.setDataValues(new double[]{uBias, uMSE});
	}
	
	public void graphUpdate(){
		super.update();
		
		uTable.setShowModelDistribution(showModelDistribution);

		uTable.repaint();
	}

	/**
	* This method resets the experiment.
	*/
	public void reset(){
		int n = getSampleSize();
		super.reset();
		recordTable.reset();
		uTable.reset();
		uTable.setDistributionValues(new double[]{0, 1.96/Math.sqrt(n)});
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

