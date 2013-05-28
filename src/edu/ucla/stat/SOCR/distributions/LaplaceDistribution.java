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

package edu.ucla.stat.SOCR.distributions;

import java.util.List;

import org.jfree.data.statistics.Statistics;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.exception.ParameterOutOfBoundsException;

/**
 * This class defines the Laplace distribution with parameters mu & beta. Also
 * called the Double-Exponential distribution. It is the distribution of
 * differences between two independent variates with identical Exponential
 * distributions (Abramowitz and Stegun 1972, p. 930). <a
 * href="http://mathworld.wolfram.com/LaplaceDistribution.html">
 * http://mathworld.wolfram.com/LaplaceDistribution.html </a>.
 */
public class LaplaceDistribution extends Distribution {

    /**
     * @uml.property name="mu"
     */
    //Parameters
    double mu;

    /**
     * @uml.property name="beta"
     */
    //Parameters
    double beta;

    /**
     * @uml.property name="c"
     */
    //Parameters
    double c;

    /**
     * This general constructor creates a new exponential distribution with a
     * specified rate
     */
    public LaplaceDistribution(double mu, double beta) {
        setParameters(mu, beta);
    }

    /**
     * This default constructor creates a new exponential distribution with rate
     * 1
     */
    public LaplaceDistribution() {
        this(0, 1);
        name = "Laplace Distribution";
    }

    
    public void initialize() {
        createValueSetter("Mu", CONTINUOUS, -5, 5);
        createValueSetter("Beta", CONTINUOUS, 0, 20);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** Set parameters and assign the default partition */
    public void setParameters(double k, double b) {
        double lowerBound, upperBound;
        //Correct invalid parameters
        if (b <= 0) b = 1;
        mu = k;
        beta = b;
        //Normalizing constant
        c = 1 / (2 * beta);
        //Assign default partition:
        upperBound = getMean() + 4 * getSD();
        lowerBound = getMean() - 4 * getSD();
        super.setParameters(lowerBound, upperBound, 0.01 * upperBound, CONTINUOUS);
        super.setMGFParameters(super.MINMGFXVAL, 1/beta);
    }

    /**
     * Get center parameters
     * 
     * @uml.property name="mu"
     */
    public double getMu() {
        return mu;
    }

    /**
     * Get scale parameters
     * 
     * @uml.property name="beta"
     */
    public double getBeta() {
        return beta;
    }

    /** Density function */
    public double getDensity(double x) {
        return c * Math.exp(-Math.abs(x - mu) / beta);
    }

    /**
     * Maximum value of getDensity function
     * 
     * @uml.property name="c"
     */
    public double getMaxDensity() {
        return c;
    }

    /** Mean */
    public double getMean() {
        return mu;
    }

    /** Variance */
    public double getVariance() {
        return 2 * beta * beta;
    }

    /** Cumulative distribution function */
    public double getCDF(double x) {
        if (x == mu) return 0.5;
        else return (0.5) * (1 + ((x - mu) / Math.abs(x - mu))
                * (1 - Math.exp(-Math.abs(x - mu) / beta)));
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) throws ParameterOutOfBoundsException
    {
    	if (Math.abs(t) >= 1/beta)
    		throw new ParameterOutOfBoundsException("Parameter abs(t) must be less than 1/beta");
    	else
    		return (Math.exp(mu*t)/(1-(beta*beta*t*t)));
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/LaplaceDistribution.html");
    }
    
    //Statistical Distributions, 3rd edition (2000) by Merran Evans ...
    public void paramEstimate(double[] distData) {
    	double a, b, sum = 0;

    	//a = getMedian();
    	List<Double> valueList = new java.util.ArrayList<Double>();
    	for (int i=0; i<distData.length; i++){
			valueList.add(new Double(distData[i]));
    	}
    	a = Statistics.calculateMedian(valueList);
    	
    	sum = 0;
    	for(int i = 0; i < distData.length; i++){
    		double temp = Math.abs(distData[i]-a); 
    		sum += temp;
    	}
    	b = sum/distData.length;
    	setParameters(a, b);
    }
}
