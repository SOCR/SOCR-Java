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
 * Gamma distribution with a specified shape parameter and scale parameter. <a
 * href="http://en.wikipedia.org/wiki/Gamma_distribution">
 * http://en.wikipedia.org/wiki/Gamma_distribution </a>.
 */
public class GammaDistribution extends Distribution {

    /**
     * @uml.property name="shape"
     */
    //Parameters
    private double shape;

    /**
     * @uml.property name="scale"
     */
    //Parameters
    private double scale;

    //Parameters
    private double c;

    public GammaDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public GammaDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            rData[i] = (double) distData[i];

        paramEstimate(rData);
    }

    /**
     * General Constructor: creates a new gamma distribution with shape
     * parameter k and scale parameter b
     */
    public GammaDistribution(double k, double b) {
        setParameters(k, b);
    }

    /**
     * Default Constructor: creates a new gamma distribution with shape
     * parameter 1 and scale parameter 1
     */
    public GammaDistribution() {
        this(1, 1);
        name = "Gamma Distribution";
    }

    public void initialize() {
        createValueSetter("Shape", CONTINUOUS, 0, 100, 2);
        createValueSetter("Scale", CONTINUOUS, 0, 100, 2);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** Set parameters and assign the default partition */
    public void setParameters(double k, double b) {
        double upperBound;
        //Correct invalid parameters
        if (k <= 0) { 
        	k = 0.1;
        	//getValueSetter(0).setValue(k);
        }
        if (b <= 0) { 
        	b = 0.1;
        	//getValueSetter(1).setValue(b);
        }
        shape = k;
        scale = b;
        //Normalizing constant
        c = shape * Math.log(scale) + logGamma(shape);
        //Assign default partition:
        upperBound = getMean() + 4 * getSD();
        super.setParameters(0, upperBound, 0.01 * upperBound, CONTINUOUS);
        super.setMGFParameters(0,1.0/scale, 1000.0);
    }

    /**
     * Get shape parameters
     *
     * @uml.property name="shape"
     */
    public double getShape() {
        return shape;
    }

    /**
     * Get scale parameters
     *
     * @uml.property name="scale"
     */
    public double getScale() {
        return scale;
    }

    /** Density function */
    public double getDensity(double x) {
        if (x < 0) return 0;
        else if (x == 0 & shape < 1) return Double.POSITIVE_INFINITY;
        else if (x == 0 & shape == 1) return Math.exp(-c);
        else if (x == 0 & shape > 1) return 0;
        else return Math.exp(-c + (shape - 1) * Math.log(x) - x / scale);
    }

    /** Maximum value of getDensity function */
    public double getMaxDensity() {
        double mode;
        if (shape < 1) mode = 0.01;
        else mode = scale * (shape - 1);
        return getDensity(mode);
    }

    /** Mean */
    public double getMean() {
        return shape * scale;
    }

    /** Variance */
    public double getVariance() {
        return shape * scale * scale;
    }

    /** Cumulative distribution function */
    public double getCDF(double x) {
        return gammaCDF(x / scale, shape);
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) throws ParameterOutOfBoundsException
    {
    	if (t >= 1/scale)
    		throw new ParameterOutOfBoundsException("Parameter t must be less than 1/scale");
    	else
    		return Math.pow(1-scale*t, -shape);
    }

    /** Simulate a value */
    public double simulate() {
        /*
         * If shape parameter k is an integer, simulate as the k'th arrival time
         * in a Poisson proccess
         */
        System.out.println("GammaDistribution simulate shape = " + shape);

        if (shape == Math.rint(shape)) {
            double sum = 0;
            for (int i = 1; i <= shape; i++) {
                sum = sum - scale * Math.log(1 - Math.random());
            }
            return sum;
        } else return super.simulate();
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Gamma_distribution");
    }

    /**
     * @uml.property name="shape"
     */
    public void paramEstimate(double[] distData) {
        //rate = 1/sampleMean(distData);
        //setRate(rate);
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        scale = sVar / sMean;
        shape = sMean / scale;
        setParameters(scale, shape);
    }

}

