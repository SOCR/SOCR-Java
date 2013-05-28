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
 * A Java implementation of the Error distribution with specified Location, Scale and
 * Shape parameters <a href="http://en.wikipedia.org/wiki/Exponential_power_distribution">
 * http://en.wikipedia.org/wiki/Exponential_power_distribution</a>.
 */
public class ErrorDistribution extends Distribution {

    /**
     * @uml.property name="Location"
     */
    //Parameters
    private double location;

    /**
     * @uml.property name="Scale"
     */
    //Parameters
    private double scale;

    /**
     * @uml.property name="Shape"
     */
    //Parameters
    private double shape;

    //Constant scaling parameter
    private double NormalizingConst;

    /**
     * Default constructor: creates an Error distribution with location, scale and shape
     * parameters equal to 1.
     */
    public ErrorDistribution() {
        name = "Error Distribution";
        setParameters(1, 1, 1);
    }

    /**
     * General constructor: creates an Error distribution with 
     * location(a), scale(b) and shape(c) parameters.
     */
    public ErrorDistribution(double a, double b, double c) {
        setParameters(a, b, c);
    }

    /**
     * Constructor: Creates a new Error distribution from a series of observations by parameter estimation.
     */
    public ErrorDistribution(double[] distData) {
        paramEstimate(distData);
    }

    /**
     * Constructor: Creates a new Error distribution from a series of observations by parameter estimation.
     */
    public ErrorDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    /**
     * Class initialization.
     */
    public void initialize() {
        createValueSetter("Location", CONTINUOUS, -10, 10, 0);
        createValueSetter("Scale", CONTINUOUS, 0, 10, 1);
        createValueSetter("Shape", DISCRETE, 0, 5, 1);
        setParameters(0, 1, 1);
    }

    public void valueChanged() {
        setParameters(getValueSetter(0).getValue(), getValueSetter(1).getValue(), getValueSetter(2).getValue());
    }

    /**
     * Set the parameters, compute the normalizing constant NormalizingConst, and specifies the
     * interval and partition
     */
    public void setParameters(double a, double b, double c) {
        double lower, upper;
        //Correct parameters that are out of bounds
        if (b <= 0) b = 1;
        if (c <= 0) c = 1;
        //Assign parameters
        location = a;
        scale = b;
        shape = c;
        //Compute the normalizing constant
        NormalizingConst = scale * Math.pow(2, shape/2 +1) * gamma(1+shape/2);
        //Specifiy the interval and partiton
        upper = getMean() + 5 * getSD();
        lower = getMean() - 5 * getSD();
        super.setParameters(lower, upper, 0.01, CONTINUOUS);
    }

    /** Sets the Location parameter */
    public void setLocation(double a) {
        setParameters(a, scale, shape);
    }

    /** Sets the Scale parameter */
    public void setScale(double b) {
        if (b>0) setParameters(location, b, shape);
        else setParameters(location, 1.0, shape);
    }

    /** Sets the Shape parameter */
    public void setShape(double c) {
        if (c>0) setParameters(location, scale, c);
        else setParameters(location, scale, 1.0);
    }

    /**
     * Get the Location paramter
     * @uml.property name="location"
     */
    public double getLocation() {
        return location;
    }    
    /**
     * Get the scale paramter
     * @uml.property name="scale"
     */
    public double getScale() {
        return scale;
    }
    /**
     * Get the shape parameter
     * @uml.property name="shape"
     */
    public double getShape() {
        return shape;
    }

    /** Define the Error getDensity function */
    public double getDensity(double x) {
        return (Math.exp( -Math.pow(Math.abs(x-location)/scale, 2/shape)/2.0))/NormalizingConst;
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return getDensity(getMode());
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return location;
    }

    /** Compute the Mode in closed form */
    public double getMode() {
        return location;
    }

    /** 
     * Overwrites the method in distribution for estimating parameters 
	 * By assuming that the shape parameter is known, the location and scale parameters could
	 * be easily obtained by using the maximum likelihood estimation method. The estimate of
	 * the shape parameter p is an open problem, so far. See this paper for an idea of how to implement a
 	 * numerical scheme for estimation of the Shape parameter:
	 * http://www.jstatsoft.org/v12/i04/  
	 */
    public void paramEstimate(double[] distData) {
        double location, scale, shape;
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        // MOM estimates of scale and shape a,b from sample statistics
        int size = distData.length;
        double deno = 0;
        for (int i = 0; i<size; i++)
        	deno += Math.abs(distData[i]-sMean);
        deno = deno*deno;
        // This is the first order of the itterative approx, accorting to equ. (22) of paper referenced above!
        shape = (sVar*(size-1)*size)/deno -1;	
        scale = Math.sqrt(sVar*gamma(shape/2)/(Math.pow(2, shape)*gamma(3*shape/2)));
        setParameters(sMean, scale, shape);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return (Math.pow(2, shape)*scale*scale*gamma(3*shape/3)/gamma(shape/2));
    }

    /** Compute the variance in closed form */
    public double getSD() {
        return Math.sqrt(getVariance());
    }

    /**
     * Compute the cumulative distribution function. The CDF is NOT known in closed form!!!
     *
    public double getCDF(double x) {
      	double cdf = 0;
		for (int i=0; i<shape; i++)
			cdf+=Math.pow(x/scale, i)/factorial(i);
		return (1-Math.exp(-x/scale)*cdf);
    }
    *****/

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/ErrorDistribution.html");
    }
}
