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
 * This class models the Fisher F distribution with a spcified number of degrees
 * of freedom in the numerator and denominator. <a
 * href="http://mathworld.wolfram.com/F-Distribution.html">
 * http://mathworld.wolfram.com/F-Distribution.html </a>.
 */
public class FisherDistribution extends Distribution {
    private int nDegrees;
    private int dDegrees;
    private double c;

    /**
     * This general constructor creates a new Fisher distribution with a
     * specified number of degrees of freedom in the numerator and denominator
     */
    public FisherDistribution(int n, int d) {
        setParameters(n, d);
    }

    /**
     * This default constructor creates a new Fisher distribution with 5 degrees
     * of freedom in numerator and denominator
     */
    public FisherDistribution() {
        this(5, 5);
        name = "Fisher's F Distribution";
    }

    public void initialize() {
        createValueSetter("DF Numerator", DISCRETE, 0, 40);
        createValueSetter("DF Denomenator", DISCRETE, 0, 40);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        int v2 = getValueSetter(1).getValueAsInt();
        setParameters(v1, v2);
    }

    /**
     * This method sets the parameters, the degrees of freedom in the numerator
     * and denominator. Additionally, the normalizing constant and default
     * interval are computed
     */
    public void setParameters(int n, int d) {
        double upper, width;
        //Correct invalid parameters
        if (n < 1) n = 1;
        if (d < 1) d = 1;
        nDegrees = n;
        dDegrees = d;
        //Compute normalizing constant
        c = logGamma(0.5 * (nDegrees + dDegrees)) - logGamma(0.5 * nDegrees)
                - logGamma(0.5 * dDegrees) + 0.5 * nDegrees
                * (Math.log(nDegrees) - Math.log(dDegrees));
        //Compute interval
        if (dDegrees <= 4) upper = 20;
        else upper = getMean() + 4 * getSD();
        width = 0.01 * upper;
        super.setParameters(0, upper, width, CONTINUOUS);
    }

    /** This method computes the denisty function */
    public double getDensity(double x) {
        if (x < 0) return 0;
        else if (x == 0 & nDegrees == 1) return Double.POSITIVE_INFINITY;
        else return Math.exp(c + (0.5 * nDegrees - 1) * Math.log(x) - 0.5
                * (nDegrees + dDegrees) * Math.log(1 + nDegrees * x / dDegrees));
    }

    /** This method computes the maximum value of the getDensity function */
    public double getMaxDensity() {
        double mode;
        if (nDegrees <= 2) mode = getDomain().getLowerValue(); // Density is maximal at Zero!!!
        else mode = (double) ((nDegrees - 2) * dDegrees)
                / (nDegrees * (dDegrees + 2));
        return getDensity(mode);
    }

    /** This method returns the mean */
    public double getMean() {
        if (dDegrees <= 2) return Double.POSITIVE_INFINITY;
        else return (double) dDegrees / (dDegrees - 2);
    }

    /** This method returns the variance */
    public double getVariance() {
        if (dDegrees <= 2) return Double.NaN;
        else if (dDegrees <= 4) return Double.POSITIVE_INFINITY;
        else return 2.0 * (dDegrees / (dDegrees - 2)) * (dDegrees / (dDegrees - 2))
                * (dDegrees + nDegrees - 2) / (nDegrees * (dDegrees - 4));
    }

    /**
     * This method computes the cumulative distribution function in terms of the
     * beta CDF
     */
    public double getCDF(double x) {
        double u = dDegrees / (dDegrees + nDegrees * x);
        if (x < 0) return 0;
        else return 1 - betaCDF(u, 0.5 * dDegrees, 0.5 * nDegrees);
    }

    /** This method returns the numerator degrees of freedom */
    public double getNDegrees() {
        return nDegrees;
    }

    /** This method sets the numerator degrees of freedom */
    public void setNDegrees(int n) {
        setParameters(n, dDegrees);
    }

    /** This method gets the denominator degrees of freedom */
    public double getDDegrees() {
        return dDegrees;
    }

    /** This method sets the denominator degrees of freedom */
    public void setDDegrees(int d) {
        setParameters(nDegrees, d);
    }

    /** This method simulates a value from the distribution */
    public double simulate() {
        double U, V, Z, r, theta;
        U = 0;
        for (int i = 1; i <= dDegrees; i++) {
            r = Math.sqrt(-2 * Math.log(Math.random()));
            theta = 2 * Math.PI * Math.random();
            Z = r * Math.cos(theta);
            U = U + Z * Z;
        }
        V = 0;
        for (int j = 1; j <= dDegrees; j++) {
            r = Math.sqrt(-2 * Math.log(Math.random()));
            theta = 2 * Math.PI * Math.random();
            Z = r * Math.cos(theta);
            V = V + Z * Z;
        }
        return (U / nDegrees) / (V / dDegrees);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/F-Distribution.html");
    }

}

