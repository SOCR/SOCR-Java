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

import edu.ucla.stat.SOCR.core.*;

/** This class models the logistic distribution */
public class LogisticDistribution extends Distribution {

	   //Paramters
    /**
     * @uml.property name="mu"
     */
    private double mu;

    /**
     * @uml.property name="sigma"
     */
    private double sigma;

    /**
     * This general constructor creates a new Logistic distribution with specified
     * parameter values
     */
    public LogisticDistribution(double mu, double sigma) {
        setParameters(mu, sigma);
        name = "Logistic Distribution";
    }

    /** This default constructor creates a new logsitic distribution */
    public LogisticDistribution() {
        super.setParameters(-7, 7, 0.14, CONTINUOUS);
        name = "Logistic Distribution";
    }
    
    public LogisticDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public LogisticDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public void initialize() { 
        createValueSetter("Mean", CONTINUOUS, -200, 200);
        createValueSetter("Standard Deviation", CONTINUOUS, 0, 100, 1);
    }
    
    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    public void paramEstimate(double[] distData) {
        double _mu = getMean(distData);
        double _sigma = 3*Math.sqrt(getVariance(distData))/Math.PI;
        setParameters(_mu, _sigma);
    }

    /** This method sets the parameters */
    public void setParameters(double m, double s) { // m = mean and s = STD.
		double lower, upper, width;
			//Correct for invalid sigma
		if (s <= 0)
		{	s = 1;
			try {
				ValueSetter valueSetter = getValueSetter(1);
				getValueSetter(1).setValue(1);
			} catch (Exception e) {
			}
		}
		else {
			try {
				ValueSetter valueSetter = getValueSetter(1);
			} catch (Exception e) {
			}
		}
		mu = m;
		sigma = s;
		upper = mu + 9 * sigma;
		lower = mu - 9 * sigma;
		width = (upper - lower) / 100;
		super.setParameters(lower, upper, width, CONTINUOUS);
		super.setMGFParameters();
		name = "Logistic ("+mu+", "+sigma+") Distribution";
    }

    public double getMean(double[] distData) {
		double sumX = 0;
		if (distData.length == 0) return (0);
		else sumX = distData[0];
		for (int i = 1; i < distData.length; i++)
			sumX += distData[i];
		return (sumX / distData.length);
    }

    public double getVariance(double[] distData) {
		double mean = getMean(distData);
		double sumX2;
		if (distData.length == 0) return (0);
		else sumX2 = distData[0] * distData[0];
		for (int i = 1; i < distData.length; i++)
			sumX2 += distData[i] * distData[i];
		double result = (-mean * mean + sumX2 / distData.length);
		return result;
    }

    /** This method computes the getDensity function */
    public double getDensity(double x) {
        double e = Math.exp(-(x-mu)/sigma);
        return e / (sigma*(1 + e) * (1 + e));
    }

    /** This method computes the maximum value of the getDensity function */
    public double getMaxDensity() {
        return getDensity(mu);
    }

    /** This method computes the cumulative distribution function */
    public double getCDF(double x) {
        double e = Math.exp(-(x-mu)/sigma);
        return 1 / (1 + e);
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t)
    {
    	return Math.PI*sigma*t*Math.exp(mu*t)/Math.sin(Math.PI*sigma*t);
    }

    /** This method comptues the getQuantile function */
    public double getQuantile(double p) {
        return (mu + sigma*Math.log(p / (1 - p)));
    }

    /** This method returns the mean */
    public double getMean() {
        return mu;
    }

    /** This method computes the variance */
    public double getVariance() {
        return sigma*sigma*Math.PI*Math.PI/3;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Logistic_distribution");
    }

}

