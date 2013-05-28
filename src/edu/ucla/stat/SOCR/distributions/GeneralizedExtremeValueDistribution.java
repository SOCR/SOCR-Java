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
 It s Online, Therefore, It EZetasts! 
****************************************************/

package edu.ucla.stat.SOCR.distributions;

import java.util.Observable;

import edu.ucla.stat.SOCR.core.*;

/**
 * This class models the Generalized-Extreme-Value (GEV) Distribution  with specified 3 parameters
 * (location, scale, shape):
 * 
 * The generalized extreme value distribution (GEV) is a family of continuous probability distributions 
 * developed within extreme value theory to combine the Gumbel, FrEchet and Weibull families 
 * also known as type I, II and III extreme value distributions. 
 * 
 * By the extreme value theorem, the GEV distribution is the limit distribution of properly 
 * normalized maZetama of a sequence of independent and identically distributed random variables. 
 * Because of this, the GEV distribution is used as an approZetamation to model the maZetama of long 
 * (finite) sequences of random variables.
 * 
 * http://en.wikipedia.org/wiki/Generalized_extreme_value_distribution
 * http://www.aainformatics.co.uk/Finance/mc/topic9.html
 */

public class GeneralizedExtremeValueDistribution extends Distribution {
    public static double EulerConstant = 0.5772156649015328606065120900824024310421;
	
	//Parameters
    /**
     * @uml.property name="mu (location)"
     */
    private double mu;

    /**
     * @uml.property name="sigma (scale)"
     */
    private double sigma;

    /**
     * @uml.property name="zeta (shape, tail index parameter)"
     */
    private double zeta;

    //NormalDistribution ND = new NormalDistribution(0,1);
    
    /**
     * This general constructor creates a new GEV GeneralizedExtremeValueDistribution with
     * specified parameters
     * @param mu = location mean
     * @param sigma = scale SD
     * @param zeta = shape skewness
     */
    public GeneralizedExtremeValueDistribution(double _mu, double _sigma, double _zeta) {
        setParameters(_mu, _sigma, _zeta);
    }

    /**
     * Default constructor: creates a beta distribution with mu and sigma
     * parameters equal to 1
     */
    public GeneralizedExtremeValueDistribution() {
    	this(1.0, 1.0, 0);
    	name = "Generalized Extreme Value Distribution";
    }

    public GeneralizedExtremeValueDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public GeneralizedExtremeValueDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            rData[i] = (double) distData[i];
        paramEstimate(rData);
    }

    public void initialize() {
        createValueSetter("Mu (location)", CONTINUOUS, -10, 10, 1); // iv: initial value
        createValueSetter("Sigma (scale)", CONTINUOUS, 0, 10, 1);
        createValueSetter("zeta (shape)", CONTINUOUS, -10, 10, 0);
        setParameters(1.0, 1.0, 0.0);
    }

    public void valueChanged(Observable o, Object arg) {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        setParameters(v1, v2, v3);
    }

    /** This method sets the parameters, computes the default interval 
     * @param _zeta = location
     * @param _lambda = scale
     * @param _gamma = shape
     * @param _delta = shape
     */
    public void setParameters(double _mu, double _sigma, double _zeta) {
        //Correct parameters that are out of bounds
        if (_sigma <= 0) 	_sigma = 1.0;
        mu = _mu;
        sigma = _sigma;
        zeta = _zeta;
        double lowerBound;
        double upperBound;
        
        /***/
        if (zeta > 0) {
        	lowerBound = mu-sigma/zeta;
        	if (zeta >1) upperBound = mu+20*Math.exp(zeta)*sigma; // Double.MAX_VALUE;
        	else upperBound = mu+20*sigma; // Double.MAX_VALUE;
        } else if (zeta==0) {
        	lowerBound = mu-20*sigma;	// Double.MIN_VALUE;
        	upperBound = mu+20*sigma;	// Double.MAX_VALUE;
        } else {
        	if (zeta < -1) lowerBound = mu-20*Math.exp(-zeta)*sigma;	// Double.MIN_VALUE;
        	else lowerBound = mu-20*sigma;	// Double.MIN_VALUE;
        	upperBound = mu-sigma/zeta;
        }
        /***
        lowerBound = mu-5*sigma;
    	upperBound = mu+5*sigma;
    	***/
        //System.err.println("lowerBound=" + lowerBound+"\t upperBound="+upperBound);
        super.setParameters(lowerBound, upperBound, 0.01 * (upperBound-lowerBound), CONTINUOUS);
    }

    /** This method gets the 3 parameters 
     */
    public double[] getParameters() {
        return new double[] {this.mu, this.sigma, this.zeta};
    }

    /**
     * Estimate the 3 GEV Distribution parameters
     * @param distData = an array of sample data (loss)
     */
    public void paramEstimate(double[] distData) {
    	double _mu=0.0, _sigma=0.0, _zeta=0.0;
    	
        for (int i = 0; i < distData.length; i++) _mu += distData[i];
        _mu /= (double) distData.length;

        for (int i = 0; i < distData.length; i++) 
        	_sigma += (distData[i] - _mu) * (distData[i] - _mu);
        _sigma = Math.sqrt(_sigma / (double) (distData.length - 1));

        double[] lnloss = new double[distData.length + 1];
        for (int i = 0; i < distData.length; i++) lnloss[i + 1] = Math.log(distData[i]);

        double[] teta = new double[distData.length + 1];
        for (int k = 2; k <= distData.length; k++) {
        	teta[k] = 0;
        	for (int j = 1; j <= k - 1; j++) teta[k] += lnloss[j];
        	teta[k] /= (double) (k - 1);
        	teta[k] -= lnloss[k];
        }
        double et = 0.0;
        for (int i = 1; i <= distData.length; i++) et += teta[i];
        _zeta = et / (double) (distData.length - 1);

        setParameters(_mu, _sigma, _zeta);
    }
    
    /**
     * @uml.property name="mu (location)"
     */
    public double getMu() {
        return mu;
    }
    /** This method sets mean */
    public void setMu(double _mu) {
        setParameters(_mu, this.sigma, this.zeta);
    }

    /**
     * Get sigma
     * @uml.property name="sigma (scale)"
     */
    public double getSigma() {
        return sigma;
    }
    /** This method sets Sigma */
    public void setSigma(double _sigma) {
    	setParameters(this.mu, _sigma, this.zeta);
    }

    /**
     * Get  zeta
     * @uml.property name="zeta (shape)"
     */
    public double getZeta() {
        return zeta;
    }
    /** This method sets zeta */
    public void setZeta(double _zeta) {
    	setParameters(this.mu, this.sigma, _zeta);
    }

    /** Define the GEV getDensity function 
     * @param x value to evaluate the Density at
     */
    public double getDensity(double x) {  
        double density = 0.0; 
        double z = (x - mu) / sigma;
        if (zeta != 0) {        
        	 density = (1.0 / sigma) * Math.exp(-Math.pow((1 + zeta * z), -1.0 / zeta)) * 
        	 		Math.pow((1 + zeta * z), -1.0 / zeta - 1.0);
        } else {
        	density = (1.0 / sigma) * Math.exp(-z - Math.exp(-z));
        }
        if (density <0) density=0.0;
        return density;
    }


    /** This method computes the cumulative distribution function 
     * @param x = value to evaluate the CDF at
     * 
     * http://www.aainformatics.co.uk/Finance/mc/topic9.html
     */
    public double getCDF(double x) {
        double cdfValue;
        double z = (x - mu) / sigma;
        if (zeta != 0) {
        	cdfValue = Math.exp(-Math.pow((1 + zeta * z), -1.0 / zeta));
        } else {
        	cdfValue = Math.exp(-Math.exp(-z));
        }
        return cdfValue;
    }

    /** This method returns the mean
     * http://en.wikipedia.org/wiki/Generalized_extreme_value_distribution
     */
    public double getMean () {
    	if (zeta >0 && zeta <1) 
    		return (mu + sigma*(gamma(1-zeta)-1)/zeta);
    	else if (zeta==0) 
    		return (mu+sigma*EulerConstant);
    	else return (super.getMean());
    }

    /** This method returns the median
     * http://en.wikipedia.org/wiki/Generalized_extreme_value_distribution
     */
    public double getMedian() {
    	if (zeta != 0) 
    		return (mu + sigma*(Math.pow(Math.log(2), -zeta)-1)/zeta);
    	else if (zeta==0) 
    		return (mu-sigma*Math.log(Math.log(2)));
    	else return (super.getMedian());
    }

    /** This method returns the Mode
     * http://en.wikipedia.org/wiki/Generalized_extreme_value_distribution
     */
    public double getMode() {
      	if (zeta != 0) 
    		return (mu + sigma*(Math.pow(1+zeta, -zeta)-1)/zeta);
    	else if (zeta==0) 
    		return mu;
    	else return getMean();
    }

    /** This method returns the Variance
     * http://en.wikipedia.org/wiki/Generalized_extreme_value_distribution
     */
    public double getVariance () {
      	if (zeta >0 && zeta <0.5) 
    		return (sigma*sigma*(gamma(1-2*zeta)-
    				Math.pow(gamma(1-zeta),2))/(zeta*zeta));
    	else if (zeta==0) 
    		return (sigma*sigma*Math.PI*Math.PI/6);
    	else return (super.getVariance());
    }

    /** This method returns the SD
     * http://en.wikipedia.org/wiki/Generalized_extreme_value_distribution
     */
    public double getSD() {
    	return Math.sqrt(getVariance());
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Generalized_extreme_value_distribution");
    }
}

