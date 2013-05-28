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

/**
 * A Java implmentation of the FisherTippettdistribution with specified alpha &
 * beta parameters <a
 * href="http://mathworld.wolfram.com/FisherTippettDistribution.html">
 * http://mathworld.wolfram.com/FisherTippettDistribution.html </a>. Also called
 * the "Extreme Value Distribution" and "Log-Weibull distribution".
 */

public class FisherTippettDistribution extends Distribution {

    /**
     * @uml.property name="alpha" (location)
     */
    //Parameters
    private double alpha = 0.0;

    /**
     * @uml.property name="beta" (scale)
     */
    //Parameters
    private double beta = 1.0;

    public double EULER_MASCHERONI_CONSTANT=0.5772156649015328606;

    double xlo;
    double xhi;
    double coef;

    /**
     * General Constructor: creates a FisherTippettdistribution with specified
     * alpha & beta parameters
     */
    public FisherTippettDistribution(double a, double b) {
        setParameters(a, b);
    }

    /**
     * Default constructor: creates a FisherTippettdistribution with alpha &
     * beta parameters equal to 0 & 1
     */
    public FisherTippettDistribution() {
        this(0, 1);
        name = "Fisher-Tippett Distribution";
    }

    /**
     * Constructor: Creates a new Fisher-Tippett distribution from 
     * a series of observations by parameter estimation.
     */
    public FisherTippettDistribution(double[] distData) {
        paramEstimate(distData);
    }

    /**
     * Constructor: Creates a new Fisher-Tippett distribution from 
     * a series of observations by parameter estimation.
     */
    public FisherTippettDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public void initialize() {
        createValueSetter("Alpha", CONTINUOUS, 0, 5, 0);
        createValueSetter("Beta", CONTINUOUS, 1, 10, 1);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /**
     * Set the parameters, compute the normalizing constant c, and specifies the
     * interval and partition
     */
    public void setParameters(double a, double b) {
        //Correct parameters that are out of bounds
        alpha = a;
        if (b <= 0) beta = 1;
        else beta = b;

        //Specifiy the interval and partiton
        double upperBound = getMean() + 4 * getSD();
        double lowerBound = getMean() - 4 * getSD();
        super.setParameters(lowerBound, upperBound, 0.01, CONTINUOUS);
        super.setMGFParameters();
        //super.setMGFParameters(0.0, 10, 200, 0.1);
    }

    /** 
     * Overwrites the method in distribution for estimating parameters 
	 * REF: MOM estimates according to:
	 * http://en.wikipedia.org/wiki/Fisher-Tippett_distribution  
	 */
    public void paramEstimate(double[] distData) {
        double _location, _scale;
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        // MOM estimates of location (a) & scale (b) parameters from sample statistics
        
        _scale = Math.sqrt(6*sVar/(Math.PI*Math.PI));
        _location = sMean - _scale*EULER_MASCHERONI_CONSTANT;
        setParameters(_location, _scale);
    }

    /** Sets the left parameter (location) */
    public void setLocation(double a) {
        setParameters(a, beta);
    }

    public void setLeft(double a) {
        setParameters(a, beta);
    }

    /** Sets the right parameter (scale) */
    public void setScale(double b) {
        setParameters(alpha, b);
    }
 
    public void setRight(double b) {
        setParameters(alpha, b);
    }

    /**
     * Get the left paramter
     * 
     * @uml.property name="alpha"
     */
    public double getLocation() {
        return alpha;
    }

    public double getLeft() {
        return alpha;
    }

    /**
     * Get the right parameter
     * @uml.property name="beta"
     */
    public double getScale() {
        return beta;
    }
    
    public double getRight() {
        return beta;
    }

    /** Define the beta getDensity function */
    public double getDensity(double x) {
        double temp;
        temp = Math.exp(0.0 - (x - alpha) / beta);
        return (temp / (beta * Math.exp(temp)));
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return (1.0 / (beta * Math.exp(1.0)));
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return (alpha + beta * 0.57721566490153286);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return Math.PI * Math.PI * beta * beta / 6;
    }

    /**
     * Compute the cumulative distribution function. The FisherTippettCDF is
     * built into the superclass Distribution
     */
    public double getCDF(double x) {
        return (Math.exp(0 - Math.exp(0 - (x - alpha) / beta)));
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t)
    {
    	return gamma(1-beta*t)*Math.exp(alpha*t);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://mathworld.wolfram.com/FisherTippettDistribution.html");
    }

}

