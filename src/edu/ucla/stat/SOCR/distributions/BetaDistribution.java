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
 * A Java implmentation of the beta distribution with specified left(alpha) and
 * right(beta) parameters <a
 * href="http://mathworld.wolfram.com/BetaDistribution.html">
 * http://mathworld.wolfram.com/BetaDistribution.html </a>.
 */
public class BetaDistribution extends Distribution {

    /**
     * @uml.property name="left"
     */
    //Parameters
    private double left;

    /**
     * @uml.property name="right"
     */
    //Parameters
    private double right;

    //Parameters
    private double c;

    /**
     * Default constructor: creates a beta distribution with left and right
     * parameters equal to 1
     */
    public BetaDistribution() {
        name = "Beta Distribution";
    }

    public BetaDistribution(double a, double b) {

        setParameters(a, b);
    }

    public BetaDistribution(double[] distData) {
        paramEstimate(distData);
    }
    public BetaDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }


    public void initialize() {
        createValueSetter("Alpha", CONTINUOUS, 0, 10, 1);
        createValueSetter("Beta", CONTINUOUS, 0, 10, 1);
        setParameters(1, 1);
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
        if (a <= 0) a = 1;
        if (b <= 0) b = 1;
        //Assign parameters
        left = a;
        right = b;
        //Compute the normalizing constant
        c = logGamma(left + right) - logGamma(left) - logGamma(right);
        //Specifiy the interval and partiton
        super.setParameters(0, 1, 0.01, CONTINUOUS);
    }

    /** Sets the left parameter */
    public void setLeft(double a) {
        setParameters(a, right);
    }

    /** Sets the right parameter */
    public void setRight(double b) {
        setParameters(left, b);
    }

    /**
     * Get the left paramter
     *
     * @uml.property name="left"
     */
    public double getLeft() {
        return left;
    }

    /**
     * Get the right parameter
     *
     * @uml.property name="right"
     */
    public double getRight() {
        return right;
    }

    /** Define the beta getDensity function */
    public double getDensity(double x) {
        if ((x < 0) | (x > 1)) return 0;
        else if ((x == 0) & (left == 1)) return right;
        else if ((x == 0) & (left < 1)) return Double.POSITIVE_INFINITY;
        else if ((x == 0) & (left > 1)) return 0;
        else if ((x == 1) & (right == 1)) return left;
        else if ((x == 1) & (right < 1)) return Double.POSITIVE_INFINITY;
        else if ((x == 1) & (right > 1)) return 0;
        else return Math.exp(c + (left - 1) * Math.log(x) + (right - 1)
                * Math.log(1 - x));
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        double mode;
        if (left < 1) mode = 0.01;
        else if (right <= 1) mode = 0.99;
        else mode = (left - 1) / (left + right - 2);
        return getDensity(mode);
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return left / (left + right);
    }

    /** overwrites the method in distribution for estimating parameters */
    public void paramEstimate(double[] distData) {
        double alpha, beta;
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        // estimate left and right a,b from sample statistics
        double A = minDouble(distData);
        double B = maxDouble(distData);
        double q = (sMean - A) / (B - A);
        //alpha = ( (B-A)*(B-A)*(1-q)*q*q/sVar) - 1;
        //beta = alpha*(1-q)/q;
        beta = (1 - sMean) * (1 - sMean) * sMean / sVar - (1 - sMean);
        alpha = sMean * beta / (1 - sMean);
        setParameters(alpha, beta);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return left * right
                / ((left + right) * (left + right) * (left + right + 1));
    }

    /**
     * Compute the cumulative distribution function. The beta CDF is built into
     * the superclass Distribution
     */
    public double getCDF(double x) {
        return betaCDF(x, left, right);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/BetaDistribution.html");
    }

    public double maxDouble(double[] X) {
        double iMax = X[0];
        for (int i = 0; i < X.length; i++) {
            if (X[i] > iMax) iMax = X[i];
        }
        return iMax;
    }

    public double minDouble(double[] X) {
        double iMin = X[0];
        for (int i = 0; i < X.length; i++) {
            if (X[i] < iMin) iMin = X[i];
        }
        return iMin;
    }

}

