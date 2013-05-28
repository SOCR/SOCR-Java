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
 * href="http://en.wikipedia.org/wiki/Inverse-gamma_distribution">
 * http://en.wikipedia.org/wiki/Inverse-gamma_distribution </a>.
 */
public class InverseGammaDistribution extends Distribution {

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

    //Parameters - normalizing constant
    private double c;

    public InverseGammaDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public InverseGammaDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            rData[i] = (double) distData[i];

        paramEstimate(rData);
    }

    /**
     * General Constructor: creates a new gamma distribution with shape
     * parameter k and scale parameter b
     */
    public InverseGammaDistribution(double a, double b) {
        setParameters(a, b);
    }

    /**
     * Default Constructor: creates a new gamma distribution with shape
     * parameter 1 and scale parameter 1
     */
    public InverseGammaDistribution() {
        this(5, 1);
        name = "Inverse-Gamma Distribution";
    }

    public void initialize() {
    	createValueSetter("Shape (alpha)", CONTINUOUS, 2, 41, 5);
        createValueSetter("Scale (beta)", CONTINUOUS, 0, 5, 1);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** Set parameters and assign the default partition 
     *  @uml.property name="shape" // a
     *  @uml.property name="scale"  // b)
     */
    public void setParameters(double a, double b) {
        double upperBound;
        //Correct invalid parameters
        if (a <= 2) a = 3;
        if (b <= 0) b = 1;
        shape = a;	// alpha=shape
        scale = b;	// beta=scale
        //Normalizing constant
        c = Math.pow(scale, shape)/gamma(shape);
        //Assign default partition:
        upperBound = getMean() + 5 * getSD();
        super.setParameters(0.00001, upperBound, 0.01 * upperBound, CONTINUOUS);
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
        if (x <= 0) return 0;
        else return c * Math.pow(x, -1-shape)*Math.exp(-scale/(x));
    }

    /** Maximum value of getDensity function --
    public double getMaxDensity() {
        return getDensity(Math.sqrt(scale/(shape+1)));
    }
    *****/

    /** Mean */
    public double getMean() {
        if (shape>1) return scale/(shape-1);
        else return Double.NaN;
    }

    /** Mode */
    public double getMode() {
        return scale/(shape+1);
    }

    /** Variance */
    public double getVariance() {
    	if (shape>2) return scale*scale/((shape-1)*(shape-1)*(shape-2));
        else return Double.NaN;
    }

    /** Cumulative distribution function --
    public double getCDF(double x) {
        return gammaCDF(scale/x, shape);
    	//return gammaCDF(scale/x, shape)/gamma(shape);
    }
	****/
    
    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Inverse-gamma_distribution");
    }

    /**
     * @uml.property name="shape"
     * @uml.property name="scale"
     */
    public void paramEstimate(double[] distData) {
        //rate = 1/sampleMean(distData);
        //setRate(rate);
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        shape = 2 + sMean*sMean / sVar;
        scale = sMean*(shape-1);
        setParameters(shape, scale);
    }

}

