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
import edu.ucla.stat.SOCR.distributions.exception.ParameterOutOfBoundsException;

/**
 * A Java implmentation of the Erlang distribution with specified Scale (scale) and
 * shape (shape) parameters <a href="http://mathworld.wolfram.com/ErlangDistribution.html">
 * http://mathworld.wolfram.com/ErlangDistribution.html </a>.
 */
public class ErlangDistribution extends Distribution {

    /**
     * @uml.property name="Scale"
     */
    //Parameters
    private double scale;

    /**
     * @uml.property name="shape"
     */
    //Parameters
    private int shape;

    //Constant scaling parameter
    private double NormalizingConst;

    /**
     * Default constructor: creates an Erlang distribution with scale and shape
     * parameters equal to 1
     */
    public ErlangDistribution() {
        name = "Erlang Distribution";
    }

    /**
     * Constructor: creates an Erlang distribution with scale (a) and shape (b) parameters. 
     */
    public ErlangDistribution(double a, int b) {
        setParameters(a, b);
    }

    /**
     * Constructor: creates an Erlang distribution from a sample data series and
     * estimates the scale (a) and shape (b) parameters. 
     */
    public ErlangDistribution(double[] distData) {
        paramEstimate(distData);
    }

    /**
     * Constructor: creates an Erlang distribution from a sample data series and
     * estimates the scale (a) and shape (b) parameters. 
     */
    public ErlangDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }


    public void initialize() {
        createValueSetter("Scale", CONTINUOUS, 0, 10, 1);
        createValueSetter("Shape", DISCRETE, 1, 20, 2);
        setParameters(1, 2);
    }

    public void valueChanged() {
        setParameters(getValueSetter(0).getValue(), (int)getValueSetter(1).getValue());
    }

    /**
     * Set the parameters, compute the normalizing constant NormalizingConst, and specifies the
     * interval and partition.
     */
    public void setParameters(double a, int b) {
        double lower=0, upper;
        //Correct parameters that are out of bounds
        if (a <= 0) a = 1;
        if (b <= 0) b = 1;
        //Assign parameters
        scale = a;
        shape = b;
        //Compute the normalizing constant
        NormalizingConst = scale * factorial(shape-1);
        //Specifiy the interval and partiton
        upper = getMean() + 5 * getSD();
        super.setParameters(lower, upper, 0.01, CONTINUOUS);
        super.setMGFParameters(0.0,1.0/scale);
    }

    /** Sets the scale parameter */
    public void setScale(double a) {
        if (a>0) setParameters(a, shape);
        else setParameters(1.0, shape);
    }

    /** Sets the shape parameter */
    public void setShape(int b) {
        if (b>0) setParameters(scale, b);
        else setParameters(scale, 1);
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
    public int getShape() {
        return shape;
    }

    /** Define the Erlang getDensity function */
    public double getDensity(double x) {
        if (x < 0) return 0;
        else return (Math.exp(-x/scale) * Math.pow(x/scale, shape-1))/NormalizingConst;
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
		double mode;
        if (shape < 1) mode = 0.01;
        else mode = getMode();
        return getDensity(mode);
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return scale*shape;
    }

    /** Compute the mean in closed form */
    public double getMode() {
        return scale*(shape-1);
    }

    /** Overwrites the method in distribution for estimating parameters */
    public void paramEstimate(double[] distData) {
        double scale, beta;
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        // MOM estimates of scale and shape a,b from sample statistics
        if (sMean==0) scale=1;
        else scale = sVar/sMean;
        if (sVar==0) shape=1;
        else shape = (int)(sMean*sMean/sVar);
        setParameters(scale, shape);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return scale*scale*shape;
    }

    /** Compute the variance in closed form */
    public double getSD() {
        return scale*Math.sqrt(shape);
    }

    /**
     * Compute the cumulative distribution function. 
     */
    public double getCDF(double x) {
        double cdf = 0;
        for (int i=0; i<shape; i++) cdf+=Math.pow(x/scale, i)/factorial(i);
        return (1-Math.exp(-x/scale)*cdf);
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. 
     */
    public double getMGF(double t) throws ParameterOutOfBoundsException
    {	if (t >= 1/scale)
    		throw new ParameterOutOfBoundsException("Parameter t must be less than 1/scale");
    	else
    		return Math.pow(1-scale*t, -shape);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/ErlangDistribution.html");
    }

}
