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
 * This class models the Student T distribution with a specifed degrees of
 * freedom parameter. <a
 * href="http://mathworld.wolfram.com/Studentst-Distribution.html">
 * http://mathworld.wolfram.com/Studentst-Distribution.html </a>.
 */
public class StudentDistribution extends Distribution {

    /**
     * @uml.property name="degrees"
     */
    private int degrees;

    private double c;

    /**
     * This general constructor creates a new student distribution with a
     * specified degrees of freedom
     */
    public StudentDistribution(int n) {
        setDegrees(n);
    }

    /**
     * This default constructor creates a new student distribuion with 1 degree
     * of freedom
     */
    public StudentDistribution() {
        this(1);
        name = "Student's T Distribution";

    }

    public void initialize() {
        createValueSetter("Degrees of Freedom", DISCRETE, 1, 60);
    }

    public void valueChanged() {
        try {  	int v1 = getValueSetters()[0].getValueAsInt();
        		setDegrees(v1);
	  }
	  catch (Exception e) {}
    }

    /**
     * This method sets the degrees of freedom
     * 
     * @uml.property name="degrees"
     */
    public void setDegrees(int n) {
        //Correct invalid parameter
        if (n < 1) n = 1;
        //Assign parameter
        degrees = n;
        //Compute normalizing constant
        c = logGamma(0.5 * (degrees + 1)) - 0.5 * Math.log(degrees) - 0.5
                * Math.log(Math.PI) - logGamma(0.5 * degrees);
        //Compute upper bound
        double upper;
        if (n == 1) upper = 14;
        else if (n == 2) upper = 9;
        else upper = Math.ceil(getMean() + 4 * getSD());
        super.setParameters(-upper, upper, upper / 50, CONTINUOUS);
    }

    /** This method computes the getDensity function */
    public double getDensity(double x) {
        return Math.exp(c - 0.5 * (degrees + 1) * Math.log(1 + x * x / degrees));
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return getDensity(0);
    }

    /** This method returns the mean */
    public double getMean() {
        if (degrees == 1) return Double.NaN;
        else return 0;
    }

    /** This method returns the variance */
    public double getVariance() {
        if (degrees == 1) return Double.NaN;
        else if (degrees == 2) return Double.POSITIVE_INFINITY;
        else return (double) degrees / (degrees - 2);
    }

    /** This method returns the SD */
    public double getSD() {
        if (degrees == 1) return Double.NaN;
        else if (degrees == 2) return Double.POSITIVE_INFINITY;
        else return Math.sqrt(getVariance());
    }

    /**
     * This method computes the cumulative distribution function in terms of the
     * beta CDF
     */
    public double getCDF(double x) {
        double u = degrees / (degrees + x * x);
        if (x > 0) return 1 - 0.5 * betaCDF(u, 0.5 * degrees, 0.5);
        else return 0.5 * betaCDF(u, 0.5 * degrees, 0.5);
    }

    /**
     * Inverse of the cumulative Student's T distribution function.
     * @return the value X for which P(x&lt;X).
     */
     public double inverseCDF(double probability) {
		if (0<=probability && probability <=1)
               return findRoot(probability, -1.0, -6.0, 6.0); 
		   // Distribution.findRoot(double prob, double guess, double xLo, double xHi); 
	 	else if (probability <0) return 0;
		else return 1.0;  // (1 < probability)
     }

    /** This method returns the degrees of freedom */
    public double getDegrees() {
        return degrees;
    }

    /** This method simulates a value of the distribution */
    public double simulate() {
        double v, z, r, theta;
        v = 0;
        for (int i = 1; i <= degrees; i++) {
            r = Math.sqrt(-2 * Math.log(Math.random()));
            theta = 2 * Math.PI * Math.random();
            z = r * Math.cos(theta);
            v = v + z * z;
        }
        r = Math.sqrt(-2 * Math.log(Math.random()));
        theta = 2 * Math.PI * Math.random();
        z = r * Math.cos(theta);
        return z / Math.sqrt(v / degrees);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://mathworld.wolfram.com/Studentst-Distribution.html");
    }

}

