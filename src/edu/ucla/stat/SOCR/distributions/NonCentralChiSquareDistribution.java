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
import edu.ucla.stat.SOCR.util.*;

/**
 * This class encapsulates the Non-Central Chi-Square distribution with specified:
 * k > 0\, degrees of freedom, and \lambda > 0, non-centrality parameters
 * parameters. <a href="http://en.wikipedia.org/wiki/Noncentral_chi-square_distribution">
 * http://en.wikipedia.org/wiki/Noncentral_chi-square_distribution </a>.
 */
public class NonCentralChiSquareDistribution extends Distribution {
    /**
     * @uml.property name="DF k > 1, degrees of freedom"
     */
    private int df;

    /**
     * @uml.property name="lambda > 0, non-centrality parameter"
     */
    private double lambda;

    /**
     * This general constructor creates a new normal distribution with specified
     * parameter values
     */
    public NonCentralChiSquareDistribution(int _df, double _lambda) {
        setParameters(_df, _lambda);
    }

    /** This default constructor creates a new standard normal distribution */
    public NonCentralChiSquareDistribution() {
        this(2, 1);
    }

    public void initialize() { 
        createValueSetter("Degrees of Freedom", DISCRETE, 2, 100, 3);
        createValueSetter("Non-Centrality Parameter", CONTINUOUS, 0, 100, 1);
    }

    public void valueChanged() {
        int v1 = (int) getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** This method sets the parameters */
    public void setParameters(int _df, double _lambda) { 
    	// _df = DF and _lambda = Lambda (centrality)

		double lower, upper, width;
		//Correct for invalid sigma
		if (_df < 2) df = 2;	// DF should be >= 2
		else df = _df;
		
		if (_lambda <= 0) // 	\lambda > 0, non-centrality parameter
			{	lambda = 1; }
		else lambda = _lambda;
			
		upper = getMean() + 4 * getSD();
		lower = 0;
		width = (upper - lower) / 100;
		
		name = "Noncentral Chi-Square Distribution (df="+df+", location="+lambda+
			") Distribution";
		
		super.setParameters(lower, upper, width, CONTINUOUS);
    }

    /** This method defines the getDensity function */
    public double getDensity(double x) {
        if (x<=0) return 0;
        else {
        	int alpha = (df-2)/2;
        	double y = Math.sqrt(lambda*x);
        	return 0.5*Math.exp(-(x+lambda)/2)* Math.pow(x/lambda, (df-2.0)/4)*
        	BesselFunction.in(alpha, y);
        }
    }

    /** These methods return the mean */
    public double getMean() {
        return (df+lambda);
    }

    /** These methods return the variance */
    public double getVariance() {
        return 2*(df+2*lambda);
    }

    /** These methods return the variance */
    public double getSD() {
        return Math.sqrt(getVariance());
    }

    /**
     * This method returns the location parameter
     * @uml.property name="lambda"
     */
    public double getLambda() {
        return lambda;
    }

    /** This method sets the location parameter */
    public void setLambda(double l) {
        setParameters(df, l);
    }

    /**
     * This method gets the DF parameter
     *
     * @uml.property name="DF"
     */
    public double getDF() {
        return df;
    }

    /** This method sets the scale parameter */
    public void setDF(int _df) {
        setParameters(_df, lambda);
    }

    /** Computes the moment generating function in closed form for a
     * parameter t < 0.5 */
    public double getMGF(double t)
    {
    	if (t<0.5)
    		return Math.exp(lambda*t/(1-2*t))/Math.pow(1-2*t, (double)df/2);
    	else return 0;
    }

    /**
     * Inverse of the cumulative (General) Normal distribution function.
     * @return the value X for which P(x&lt;X).
     --
     public double inverseCDF(double probability) {
        System.err.println("probability="+probability+"\t mu="+getMu()+
        		"\tSD="+getSD()+"inverseCDF="+(getMu() + 
        		getSigma()*inverseStdNormalCDF(probability)));
    	 return getMu() + getSigma()*inverseStdNormalCDF(probability);
     }
     ***/

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Noncentral_chi-square_distribution");
    }
}

