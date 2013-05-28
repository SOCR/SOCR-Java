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
 * A Java implmentation of the Minimax distribution with specified left(alpha) and
 * right(Minimax) parameters <a
 * http://mathworld.wolfram.com/topics/ContinuousDistributions.html">
 * http://mathworld.wolfram.com/topics/ContinuousDistributions.html </a>.
 */
public class MinimaxDistribution extends Distribution {

    /**
     * @uml.property name="beta"
     */
    //Parameters
    private double beta;

    /**
     * @uml.property name="gamma"
     */
    //Parameters
    private double gamma;

    //Parameters - normalizing constant
    private double c;

    /**
     * Default constructor: creates a Minimax distribution with left and right
     * parameters equal to 1
     */
    public MinimaxDistribution() {
        this(1,2);
    }

    public MinimaxDistribution(double b, double g) {
        name = "Minimax Distribution";
        setParameters(b, g);
    }

    public void initialize() {
        createValueSetter("Shape Parameter 1 (beta)", CONTINUOUS, 0, 10, 1);
        createValueSetter("Shape Parameter 2 (gamma)", CONTINUOUS, 0, 10, 2);
        setParameters(1, 2);
    }

    public void valueChanged() {
        setParameters(getValueSetter(0).getValue(), getValueSetter(1).getValue());
    }

    /**
     * Set the parameters, compute the normalizing constant c, and specifies the
     * interval and partition
     */
    public void setParameters(double b, double g) {
        double lower, upper, step;
        //Correct parameters that are out of bounds
        if (b <= 0) b = 1;
        if (g <= 0) g = 1;
        //Assign parameters
        beta = b;
        gamma = g;
        //Compute the normalizing constant
        c = beta * gamma;
        //Specifiy the interval and partiton
        super.setParameters(0, 1, 0.01, CONTINUOUS);
    }

    /** Sets the left parameter */
    public void setBeta(double b) {
        setParameters(b, gamma);
    }

    /** Sets the right parameter */
    public void setGamma(double g) {
        setParameters(beta, g);
    }

    /**
     * Get the left paramter
     * @uml.property name="beta"
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Get the right parameter
     * @uml.property name="right"
     */
    public double getGamma() {
        return gamma;
    }

    /** Define the Minimax getDensity function */
    public double getDensity(double x) {
        if (x <= 0 || x >= 1) return 0;
        else return c * beta * gamma * Math.pow(x, beta-1) * Math.pow(1-Math.pow(x, beta), gamma-1);
    }

    /** Compute the maximum getDensity --
    public double getMaxDensity() {
        double extreme = 0.9;
        extreme = Math.pow(1.0/(1 + beta*(1-gamma)), 1.0/beta);
        return getDensity(extreme);
    }
    ***/

    /** Compute the mean in closed form --
    public double getMean() {
        return left / (left + right);
    }
   ***/

    /** Compute the variance in closed form --
    public double getVariance() {
        return left * right
                / ((left + right) * (left + right) * (left + right + 1));
    }
    ***/

    /**
     * Compute the cumulative distribution function. The Minimax CDF is built into
     * the superclass Distribution
     --
    public double getCDF(double x) {
        return MinimaxCDF(x, left, right);
    }
    ***/

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/topics/ContinuousDistributions.html");
    }
}

