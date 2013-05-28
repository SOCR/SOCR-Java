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
 * This class models the Half-Normal distribution with specified starting and SD
 * parameters. A normal distribution with mean (starting) and standard deviation
 * SD limited to the domain [0:Infinity]. <a
 * href="http://mathworld.wolfram.com/Half-NormalDistribution.html">
 * http://mathworld.wolfram.com/Half-NormalDistribution.html </a>.
 */
public class HalfNormalDistribution extends Distribution {
    //Paramters
    public final static double C = Math.sqrt(2 * Math.PI);

    /**
     * @uml.property name="mu"
     */
    private double mu;

    /**
     * @uml.property name="sigma"
     */
    private double sigma;

    private double cSigma;

    /**
     * This general constructor creates a new Half-Normal distribution with
     * specified parameter values
     */
    public HalfNormalDistribution(double mu, double sigma) {
        setParameters(mu, sigma);
    }

    /** This default constructor creates a new standard normal distribution */
    public HalfNormalDistribution() {
        this(0, 1);
        name = "Half-Normal Distribution";

    }

    public void initialize() {
        createValueSetter("Left", CONTINUOUS, -10, 10);
        createValueSetter("Sigma", CONTINUOUS, 0, 10);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** This method sets the parameters */
    public void setParameters(double m, double s) {
        double lower, upper, width;
        //Correct for invalid sigma
        if (s < 0) s = 1;
        mu = m;
        sigma = s;
        cSigma = C * sigma;
        upper = mu + 4 * sigma;
        lower = mu - sigma;
        width = (upper - lower) / 100;
        super.setParameters(lower, upper, width, CONTINUOUS);
    }

    /** This method defines the getDensity function */
    public double getDensity(double x) {
        double z = (x - mu) / sigma;
        if (x >= mu) return 2 * Math.exp(-z * z / 2) / cSigma;
        else return 0;
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return getDensity(mu);
    }

    /** This method simulates a value from the distribution */
    public double simulate() {
        double r = Math.sqrt(-2 * Math.log(Math.random()));
        double theta = 2 * Math.PI * Math.random();
        return mu + sigma * r * Math.abs(Math.cos(theta));
    }

    /**
     * This method returns the location parameter
     * 
     * @uml.property name="mu"
     */
    public double getMu() {
        return mu;
    }

    /** This method sets the location parameter */
    public void setMu(double m) {
        setParameters(m, sigma);
    }

    /**
     * This method gets the scale parameter
     * 
     * @uml.property name="sigma"
     */
    public double getSigma() {
        return sigma;
    }

    /** This method sets the scale parameter */
    public void setSigma(double s) {
        setParameters(mu, s);
    }

    /** This method computes the cumulative distribution function */
    public double getCDF(double x) {
        double z = (x - mu) / sigma;
        double StdNormalCDF = 0;
        if (z >= 0) StdNormalCDF = 0.5 + 0.5 * gammaCDF(z * z / 2, 0.5);
        else StdNormalCDF = 0.5 - 0.5 * gammaCDF(z * z / 2, 0.5);

        if (x >= mu) return 2 * (StdNormalCDF - 0.5);
        else return 0;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://mathworld.wolfram.com/Half-NormalDistribution.html");
    }

}
