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
 * A Java implmentation of the Muth distribution with specified left(alpha) and
 * right(Muth) parameters <a
 * http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/mutpdf.htm">
 * http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/mutpdf.htm </a>.
 */
public class MuthDistribution extends Distribution {

    /**
     * @uml.property name="beta"
     */
    //Parameters
    private double beta;

    /**
     * @uml.property name="location"
     */
    //Parameters
    private double location;

    /**
     * @uml.property name="scale"
     */
    //Parameters
    private double scale;

    /**
     * Default constructor: creates a Muth distribution with left and right
     * parameters equal to 1
     */
    public MuthDistribution() {
        this(0.5, 0, 1);
    }

    public MuthDistribution(double b, double l, double s) {
        name = "Muth Distribution";
        setParameters(b, l, s);
    }

    public void initialize() {
        createValueSetter("Shape Parameter (beta)", CONTINUOUS, 0, 1, 1);
        createValueSetter("Location", CONTINUOUS, -10, 10, 0);
        createValueSetter("Scale", CONTINUOUS, 0, 10, 1);
        setParameters(1, 0, 1);
    }

    public void valueChanged() {
        setParameters(getValueSetter(0).getValue(), 
        		getValueSetter(1).getValue(), 
        		getValueSetter(2).getValue());
    }

    /**
     * Set the parameters
     */
    public void setParameters(double b, double l, double s) {
        double lower, upper, step;
        //Correct parameters that are out of bounds
        if (b < 0 || b > 1) b = 0.5;
        if (s <= 0) s = 1;
 
        //Assign parameters
        beta = b;
        location = l;
        scale = s;
        
        //Specifiy the interval and partiton
        super.setParameters(location, location + 5*scale, 0.1, CONTINUOUS);
    }

    /** Sets the beta parameter */
    public void setBeta(double b) {
        setParameters(b, location, scale);
    }

    /** Sets the Location parameter */
    public void setLocation(double l) {
        setParameters(beta, l, scale);
    }

    /** Sets the Scale parameter */
    public void setScale(double s) {
        setParameters(beta, location, s);
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
     * @uml.property name="location"
     */
    public double getLocation() {
        return location;
    }

    /**
     * Get the right parameter
     * @uml.property name="scale"
     */
    public double getScale() {
        return scale;
    }

    /** Define the Muth getDensity function */
    public double getDensity(double x) {
        if (x <= location ) return 0;
        else return getStdDensity((x-location)/scale) / scale;
    }

    /** Define the Standard Muth Density function */
    public double getStdDensity(double x) {
        if (x <= 0 ) return 0;
        else return (Math.exp(beta*x) -beta) * 
        		Math.exp(-(1.0/beta) * (Math.exp(beta*x) - 1) + beta*x);
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
     * Compute the cumulative distribution function. The Muth CDF is built into
     * the superclass Distribution
     --
    public double getCDF(double x) {
        return MuthCDF(x, left, right);
    }
    ***/

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/mutpdf.htm");
    }
}

