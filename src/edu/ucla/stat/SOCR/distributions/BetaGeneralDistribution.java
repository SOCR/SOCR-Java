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

import java.util.*;

import edu.ucla.stat.SOCR.core.*;

/**
 * A Java implmentation of the (General) Beta Distribution with specified:
 * left(alpha) and right(beta) parameters AND LIMITS A and B. <a
 * href="http://mathworld.wolfram.com/BetaDistribution.html">
 * http://mathworld.wolfram.com/BetaDistribution.html </a>.
 */

public class BetaGeneralDistribution extends Distribution {

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
     * @uml.property name="a"
     */
    // constatnt
    private double A;

    /**
     * @uml.property name="b"
     */
    // constatnt
    private double B;

    public BetaGeneralDistribution(double a, double b, double A, double B) {

        setParameters(a, b, A, B);
    }

    public BetaGeneralDistribution(double[] distData) {
        paramEstimate(distData);

    }

    public BetaGeneralDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public void initialize() {
        createValueSetter("Alpha", CONTINUOUS, 0, 10, 1); // iv: initial value
        createValueSetter("Beta", CONTINUOUS, 0, 10, 1);
        createValueSetter("A_Limit", CONTINUOUS, 0, 10, 0);
        createValueSetter("B_Limit", CONTINUOUS, 1, 11, 1);
        setParameters(1.0, 1.0, 0.0, 1.0);
    }

    public void valueChanged(Observable o, Object arg) {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        double v4 = getValueSetter(3).getValue();
        if (!(v4 > v3)) {
            if (arg == getValueSetter(2)) { //A-Limit, v3 modified
                v4 = v3 + 1.0;
                getValueSetter(3).setValue(v4);
            } else if (arg == getValueSetter(3)) { //B-Limit, v4 modified
                v3 = v4 - 1.0;
                getValueSetter(2).setValue(v3);
            }
            return;
        }

        setParameters(v1, v2, v3, v4);
    }

    /**
     * Default constructor: creates a beta distribution with left and right
     * parameters equal to 1
     */
    public BetaGeneralDistribution() {
        name = "Beta (Generalized) Distribution";
    }

    /**
     * Set the parameters, compute the normalizing constant c, and specifies the
     * interval and partition
     */
    public void setParameters(double a, double b, double _A, double _B) {
        double lower, upper, step;

        //Correct parameters that are out of bounds
        if (a <= 0) a = 1;
        if (b <= 0) b = 1;
        left = a;
        right = b;

        // COrrect limit assignment
        if (_B <= _A) {
            A = _A;
            B = A + 1;
        } else {
            A = _A;
            B = _B;
        }

        //Compute LOG of the normalizing constant
        c = logGamma(left + right) - (logGamma(left) + logGamma(right))
                - Math.log(B - A);

        //Specifiy the interval and partiton
        super.setParameters(A, B, 0.01 * (B - A), CONTINUOUS);
    }

    /** --------------------------------SET------------------------------- */
    /** Sets the left parameter */
    public void setLeft(double a) {
        setParameters(a, right, A, B);
    }

    /** Sets the right parameter */
    public void setRight(double b) {
        setParameters(left, b, A, B);
    }

    /** Sets the left LIMIT */
    public void setLeftLimit(double _leftLimit) {
        setParameters(left, right, _leftLimit, B);
    }

    /** Sets the right LIMIT */
    public void setRightLimit(double _rightLimit) {
        setParameters(left, right, A, _rightLimit);
    }

    /**
     * ----------------------------------GET--------------------------
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

    /**
     * Get the left LIMIT
     *
     * @uml.property name="a"
     */
    public double getLeftLimit() {
        return A;
    }

    /**
     * Get the right LIMIT
     *
     * @uml.property name="b"
     */
    public double getRightLimit() {
        return B;
    }

    /** Define the beta getDensity function */
    public double getDensity(double x) {
        if ((x < A) || (x > B)) return 0;
        else if ((x == A) && (left == 1)) return right / (B - A);
        else if ((x == A) && (left < 1)) return Double.POSITIVE_INFINITY;
        else if ((x == A) && (left > 1)) return 0;
        else if ((x == B) && (right == 1)) return left / (B - A);
        else if ((x == B) && (right < 1)) return Double.POSITIVE_INFINITY;
        else if ((x == B) && (right > 1)) return 0;
        else return Math.exp(c + (left - 1) * Math.log((x - A) / (B - A))
                + (right - 1) * Math.log((B - x) / (B - A)));
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return getDensity(getMode());
    }

    /** Compute the Generalized Beta Median==Mode */
    public double getMode() {
        double mode;
        if (left < 1) mode = A + 0.01 * (B - A);
        else if (right <= 1) mode = B - 0.01 * (B - A);
        else mode = A + ((B - A) * (left - 1)) / (left + right - 2);
        return mode;
    }

    public double getMedian() {
        return getMode();
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return A + ((B - A) * left) / (left + right);
    }

    /** overwrites the method in distribution for estimating parameters */
    public void paramEstimate(double[] distData) {
        double alpha, beta;
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);

        if (distData.length < 2) {
            setParameters(1.0, 1.0, 1.0, 1.0);
            return;
        }

        /***********************************************************************
         * estimate the domain/support of the Beta(a,b,A,B) double A =
         * distData[0]; // Min, left end, of support region double B =
         * distData[0]; // Max, right end. for (int i=0; i <distData.length();
         * i++) { if (A > distData[i]) A = distData[i]; if (B < distData[i]) B =
         * distData[i]; }
         **********************************************************************/

        // estimate left and right a,b parameters and the Left(A) & Right(B)
        // limits
        //	from sample statistics
        double A = minDouble(distData);
        double B = maxDouble(distData);
        double C = (sMean - A) / (B - A);
        //double q = (sMean - A)/(B-A);
        //alpha = ( (B-A)*(B-A)*(1-q)*q*q/sVar) - 1;
        //beta = alpha*(1-q)/q;

        beta = C - 1 + (B - A) * (B - A) * C * (1 - C) * (1 - C) / sVar;
        alpha = beta * C / (1 - C);

        setParameters(alpha, beta, A, B);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return ((B - A) * (B - A) * left * right)
                / ((left + right) * (left + right) * (left + right + 1));
    }

    /***************************************************************************
     * ******** NEED TO DOUBLE CHECK THESE EXTENSIONS OF THE STANDARD BETA CDF
     * CALCULATIONS ********** TO THE GENERAL Beta(alpha, beta, A, B)
     * ************************************* *Compute the cumulative
     * distribution function. The beta CDF is built into the superclass
     * Distribution but is overwritten here!!! ** public double getCDF(double
     * x){ return betaCDF(x, left, right, A, B); } *The method computes the beta
     * CDF.** public static double betaCDF(double x, double a, double b, double
     * A, double B){ double bt; if ((x == A) | (x == B)) bt = 0; else bt =
     * Math.exp(logGamma(a + b) - logGamma(a) - logGamma(b) - Math.log(B-A) + a *
     * Math.log((x-A)/(B-A)) + b * Math.log((B-x)/(B-A))); if (x < A + ((B-A)*(a +
     * 1)) / (a + b + 2)) return bt * betaCF(x, a, b, A, B) / a; else return 1 -
     * bt * betaCF((B - x)/(B-A), b, a, A, B) / b; } *This method computes a
     * beta continued fractions function that is used in the calculations of the
     * beta CDF. See Numerical Recepies in C for incomplete Beta function and
     * the continuous fraction representation. ** private static double
     * betaCF(double x, double a, double b, double A, double B){ int maxit =
     * 100; double eps = 0.0000003, am = 1, bm = 1, az = 1, qab = a + b, qap = a +
     * 1, qam = a - 1, bz = 1 - qab * x / qap, tem, em, d, bpp, bp, app, aOld,
     * ap; for (int m = 1; m <= maxit; m++){ em = m; tem = em + em; d = em * (b -
     * m) * x / ((qam + tem) * (a + tem)); ap = az + d * am; bp = bz + d * bm; d =
     * -(a + em) *(qab + em) * x / ((a + tem) * (qap + tem)); app = ap + d * az;
     * bpp = bp + d * bz; aOld = az; am = ap / bpp; bm = bp / bpp; az = app /
     * bpp; bz = 1; if (Math.abs(az - aOld) < eps * Math.abs(az)) break; }
     * return az; } END OF CALCULATION FOR COMPUTING GBD ******************
     **************************************************************************/

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

