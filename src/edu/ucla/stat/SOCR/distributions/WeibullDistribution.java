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
 * This class models the Weibull distribution with specified shape and scale
 * parameters. <a href="http://mathworld.wolfram.com/WeibullDistribution.html">
 * http://mathworld.wolfram.com/WeibullDistribution.html </a>.
 */
public class WeibullDistribution extends Distribution {

    /**
     * @uml.property name="shape"
     */
    //Variables
    double shape;

    /**
     * @uml.property name="scale"
     */
    //Variables
    double scale;

    //Variables
    double c;

    /**
     * This general constructor creates a new Weibull distribution with spcified
     * shape and scale parameters
     */
    public WeibullDistribution(double k, double b) {
        setParameters(k, b);
    }

    /**
     * This default constructor creates a new Weibull distribution with shape
     * parameter 1 and scale parameter 1
     */
    public WeibullDistribution() {
        this(1, 1);

        name = "Weibull Distribution";
    }

    public void initialize() {
        createValueSetter("Shape", CONTINUOUS, 0, 10);
        createValueSetter("Scale", CONTINUOUS, 0, 10);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /**
     * This method sets the shape and scale parameter. The normalizing constant
     * is computed and the default interval defined
     */
    public void setParameters(double k, double b) {
        double upper, width;
        if (k <= 0) k = 1;
        if (b <= 0) b = 1;
        //Assign parameters
        shape = k;
        scale = b;
        //Compute normalizing constant
        c = shape / Math.pow(scale, shape);
        //Define interval
        upper = Math.ceil(getMean() + 4 * getSD());
        width = upper / 100;
        super.setParameters(0, upper, width, CONTINUOUS);
    }

    /** This method compues teh denstiy function */
    public double getDensity(double x) {
        return c * Math.pow(x, shape - 1) * Math.exp(-Math.pow(x / scale, shape));
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        double mode;
        if (shape < 1) mode = getDomain().getLowerValue();
        else mode = scale * Math.pow((shape - 1) / shape, 1 / shape);
        return getDensity(mode);
    }

    /** The method returns the mean */
    public double getMean() {
        return scale * gamma(1 + 1 / shape);
    }

    /** This method returns the variance */
    public double getVariance() {
        double mu = getMean();
        return scale * scale * gamma(1 + 2 / shape) - mu * mu;
    }

    /** This method computes the cumulative distribution function */
    public double getCDF(double x) {
        return 1 - Math.exp(-Math.pow(x / scale, shape));
    }

    /** This method returns the getQuantile function */
    public double getQuantile(double p) {
        return scale * Math.pow(-Math.log(1 - p), 1 / shape);
    }

    /** This method computes the failure rate function */
    public double getFailureRate(double x) {
        return shape * Math.pow(x, shape - 1) / Math.pow(scale, shape);
    }

    /**
     * This method returns the shape parameter
     * 
     * @uml.property name="shape"
     */
    public double getShape() {
        return shape;
    }

    /** This method sets the shape parameter */
    public void setShape(double k) {
        setParameters(k, scale);
    }

    /**
     * This method returns the scale parameter
     * 
     * @uml.property name="scale"
     */
    public double getScale() {
        return scale;
    }

    /** This method sets the shape parameter */
    public void setScale(double b) {
        setParameters(shape, b);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/WeibullDistribution.html");
    }

}

