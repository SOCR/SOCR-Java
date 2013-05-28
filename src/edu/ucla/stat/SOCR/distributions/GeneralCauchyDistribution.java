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
 * A Java implmentation of the General Cauchy distribution with specified alpha &
 * beta parameters. <a
 * http://en.wikipedia.org/wiki/Cauchy_distribution">
 * http://en.wikipedia.org/wiki/Cauchy_distribution </a>.
 */
public class GeneralCauchyDistribution extends Distribution {

    /**
     * @uml.property name="alpha"
     */
    //Parameters
    private double alpha = 0.0;

    /**
     * @uml.property name="beta"
     */
    //Parameters
    private double beta = 1.0;

    double xlo;
    double xhi;

    /**
     * @uml.property name="coef"
     */
    double coef;

    /**
     * General Constructor: creates a Cauchy distribution with specified alpha &
     * beta parameters
     */
    public GeneralCauchyDistribution(double a, double b) {
        setParameters(a, b);
        name = "General Cauchy Distribution ("+alpha+", "+beta+")";
    }

    /**
     * Default constructor: creates a Cauchy distribution with alpha & beta
     * parameters equal to 0 & 1
     */
    public GeneralCauchyDistribution() {
        this(0, 1);
    }

    public void initialize() {
        createValueSetter("Median", CONTINUOUS, -20, 20, 0);
        createValueSetter("Spread", CONTINUOUS, 0, 100, 1);
        setParameters(0, 1);
    }

    public void valueChanged() {
        setParameters(getValueSetter(0).getValue(), getValueSetter(1).getValue());
    }

    /**
     * Set the parameters, compute the normalizing constant c, and specifies the
     * interval and partition
     */
    public void setParameters(double a, double b) {
        double lower, upper, step;
        //Correct parameters that are out of bounds
        alpha = a;
        if (b <= 0) beta = 1;
        else beta = b;

        xlo = alpha + beta * Math.tan(0.0 - 0.48 * Math.PI);
        xhi = alpha + alpha - xlo;
        coef = 1.0 / (Math.PI * beta);

        //Specifiy the interval and partiton
        double upperBound = getMedian() + 6 * getSpread();
        double lowerBound = getMedian() - 6 * getSpread();
        super.setParameters(lowerBound, upperBound, 0.01, CONTINUOUS);
        name = "General Cauchy Distribution ("+alpha+", "+beta+")";
    }

    /** Sets the left parameter */
    public void setLeft(double a) {
        setParameters(a, beta);
    }

    /** Sets the right parameter */
    public void setRight(double b) {
        setParameters(alpha, b);
    }

    /**
     * Get the left paramter
     * 
     * @uml.property name="alpha"
     */
    public double getLeft() {
        return alpha;
    }

    /**
     * Get the right parameter
     * 
     * @uml.property name="beta"
     */
    public double getRight() {
        return beta;
    }

    /** Define the beta getDensity function */
    public double getDensity(double x) {
        double temp;
        temp = (x - alpha) / beta;
        return (coef / (1.0 + temp * temp));
    }

    /**
     * Compute the maximum getDensity
     * 
     * @uml.property name="coef"
     */
    public double getMaxDensity() {
        return (coef);
    }

    /** Compute the mean in closed form */
    public double getMedian() {
        return alpha;
    }

    /** Compute the Spread in closed form */
    public double getSpread() {
        return beta;
    }

    /**
     * Compute the cumulative distribution function. The Cauchy CDF is built
     * into the superclass Distribution
     */
    public double getCDF(double x) {
        return (0.5 + Math.atan2(x - alpha, beta) / Math.PI);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Cauchy_distribution");
    }
}

