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
 * This class models the Benford distribution with parameters m
 * (population size), n (sample size), and r (number of type 1 objects). <a
 * http://en.wikipedia.org/wiki/Benford's_law">
 * http://en.wikipedia.org/wiki/Benford's_law </a>.
 */
public class BenfordDistribution extends Distribution {

    /**
     * @uml.property name="log-base parameter"
     */
    private int logBase;

    /**
     * General constructor: creates a new Benford distribution with
     * specified value of the log-base parameter
     */
    public BenfordDistribution(int b) {
        setParameters(b);
    }

    /**
     * Default constructor: creates a new Benford distribuiton with
     * parameters b=10
     */
    public BenfordDistribution() {
        this(10);
        name = "Benford Distribution";
    }

    public void initialize() {
        createValueSetter("Log-base parameter", DISCRETE, 0, 100);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        setParameters(v1);
    }

    /** Set the parameters of the distribution */
    public void setParameters(int b) {
        //Correct for invalid parameters
        if (b < 2) b = 2;
        //Assign parameter values
        logBase = b;
        super.setParameters(1, logBase - 1, 1, DISCRETE);
    }

    /** Density function */
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        if (k<1 || k>=logBase) return 0;
        else return Math.log(1+1.0/k)/Math.log(logBase);
    }

    /** Maximum value of the getDensity function */
    public double getMaxDensity() {
        return getDensity(1);
    }

    /** Mean --
    public double getMean() {
        return (double) sampleSize * type1Size / populationSize;
    }
    *****/

    /** Variance --
    public double getVariance() {
        return (double) sampleSize * type1Size * (populationSize - type1Size)
                * (populationSize - sampleSize)
                / (populationSize * populationSize * (populationSize - 1));
    }
    *****/

    /** Set population size */
    public void setLogBase(int b) {
        setParameters(b);
    }

    /**
     * Get population size
     * 
     * @uml.property name="logBase parameter"
     */
    public int getLogBase() {
        return logBase;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://en.wikipedia.org/wiki/Benford's_law");
    }
}

