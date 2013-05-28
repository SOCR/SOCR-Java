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
 * A Java implmentation of the Gumbel distribution with specified alpha & beta
 * parameters <a href="http://mathworld.wolfram.com/GumbelDistribution.html">
 * http://mathworld.wolfram.com/GumbelDistribution.html </a>. A special case of
 * the <a href="FisherTippettDistribution.html">Fisher-Tippett </a> distribution
 * with a location parameter alpha and scale parameter beta.
 */

public class GumbelDistribution extends Distribution {

    /**
     * @uml.property name="alpha"
     */
    //Parameters - location parameter alpha 
    private double alpha = 0.0;

    /**
     * @uml.property name="beta"
     */
    //Parameters - scale parameter beta
    private double beta = 1.0;

    // Euler Mascheroni constant (also called the Euler constant):
    // http://en.wikipedia.org/wiki/Euler-Mascheroni_constant 
    double GAMMA = 0.5772156649015328606;
    
    double xlo;
    double xhi;
    double coef;

    /**
     * General Constructor: creates a Gumbel distribution with specified alpha &
     * beta parameters
     */
    public GumbelDistribution(double a, double b) {
        setParameters(a, b);
   }

    /**
     * Default constructor: creates a Gumbel distribution with alpha & beta
     * parameters equal to 0 & 1
     */
    public GumbelDistribution() {
        this(0, 1);
    }

    public void initialize() {
        createValueSetter("Location (alpha)", CONTINUOUS, -200, 200);
        createValueSetter("Scale (beta)", CONTINUOUS, 0, 20);
    }


    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /**
     * This method computed the MOM estimates of the parameters from a data series.
     */
    public void paramEstimate(double[] distData) {
        double m = getMean(distData);
        double b = Math.sqrt(6*getVariance(distData)/(Math.PI*Math.PI));
        double a = m + GAMMA*b;
        setParameters(a, b);
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

        //Specifiy the interval and partiton
        double upperBound = getMean() + 4 * getSD();
        double lowerBound = getMean() - 4 * getSD();
        super.setParameters(lowerBound, upperBound, 0.01, CONTINUOUS);
        name = "Gumbel Distribution";
        super.setMGFParameters(MINMGFXVAL,1/beta);
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
     * @uml.property name="alpha"
     */
    public double getLeft() {
        return alpha;
    }

    /**
     * Get the right parameter 
     * @uml.property name="beta"
     */
    public double getRight() {
        return beta;
    }

    /** Define the Gumpbel getDensity function */
    public double getDensity(double x) {
        return (1.0/beta) * Math.exp((x-alpha)/beta - Math.exp((x-alpha)/beta));
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return (1.0 / (beta * Math.exp(1.0)));
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return (alpha - GAMMA*beta);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return Math.PI*Math.PI*beta*beta/ 6;
    }

    /**
     * Compute the cumulative distribution function.
     */
    public double getCDF(double x) {
        return (1 - Math.exp(-Math.exp((x - alpha) / beta)));
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) throws ParameterOutOfBoundsException
    {	if (t >= 1/beta)
    		throw new ParameterOutOfBoundsException("Parameter t must be less than 1/beta");
    	else
    		return gamma(1-beta*t)*Math.exp(alpha*t);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/GumbelDistribution.html");
    }

}

