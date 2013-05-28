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
 * This class defines the Chi distribution with a specifed degrees of
 * freedom. <a href="http://en.wikipedia.org/wiki/Chi_distribution">
 * http://en.wikipedia.org/wiki/Chi_distribution </a>.
 * 
 * Perhaps this distribution should be defined in terms of Gamma or Chi-Square?
 * 
 */
public class ChiDistribution extends Distribution {

    /**
     * @uml.property name="degrees"
     */
    int degrees;

    // Normalization constant
    private double c=0;
    
    /**
     * This general constructor creates a new Chi distribuiton with a
     * specified degrees of freedom parameter
     */
    public ChiDistribution(int n) {
        setDegrees(n);
        name = "Chi Distribution";
    }

    public ChiDistribution() {
        this(1);
     }

    public ChiDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public ChiDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            rData[i] = (double) distData[i];
        paramEstimate(rData);
    }

    public void initialize() {
        createValueSetter("Degrees of Freedom", DISCRETE, 1, 20,1);
    }

    public void valueChanged() {
        setDegrees(getValueSetter(0).getValueAsInt());
    }

    /**
     * This method sets the degrees of freedom
     * @uml.property name="degrees"
     */
    public void setDegrees(int n) {
        setParameters(n);
    }

    /**
     * This method sets the degrees of freedom
     * @uml.property name="degrees"
     */
    public void setParameters(int n) {
        //Correct invalid parameter
        if (n <= 0) n = 1;
        degrees = n;
        c = Math.pow(2, (1.0*degrees)/2 - 1) * gamma((1.0*degrees)/2);
        double upperBound = getMean() + 5 * getSD();
        super.setParameters(0, upperBound, 0.01 * upperBound, CONTINUOUS);
    }

    /**
     * This method returns the degrees of freedom
     * @uml.property name="degrees"
     */
    public int getDegrees() {
        return degrees;
    }

    /** Density function */
    public double getDensity(double x) {
        if (x <= 0) return 0;
        else return Math.pow(x, degrees-1) * Math.exp(-x*x/2) / c;
    }

    /** Maximum value of getDensity function --
    public double getMaxDensity() {
        return getDensity(getMode());
    }
    ****/

    /** Mean */
    public double getMean() {
        return Math.sqrt(2) * gamma((degrees+1.0)/2) / gamma((degrees*1.0)/2);
    }

    /** Find the Mode */
    public double getMode() {
        double mode;
        if (degrees <= 1) mode = 0.01;
        else mode = Math.sqrt(degrees-1);
        return mode;
    }

    /** Variance */
    public double getVariance() {
        double mean = getMean();
        return degrees - mean*mean;
    }

    /** Cumulative distribution function **/
    public double getCDF(double x) {
        return gammaCDF(x*x/2, (1.0*degrees)/2);
    }
    
    /**
     * This method simulates a value from the distribuiton, as the sum of
     * squares of independent, standard normal distribution
     */
    public double simulate() {
        double V, Z, r, theta;
        V = 0;
        // SImulate Chi-Square first thand then take a root
        for (int i = 1; i <= degrees; i++) {
            r = Math.sqrt(-2 * Math.log(Math.random()));
            theta = 2 * Math.PI * Math.random();
            Z = r * Math.cos(theta);
            V = V + Z * Z;
        }
        return Math.sqrt(V);
    }

    /**
     * @uml.property name="shape"
     */
    public void paramEstimate(double[] distData) {
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        setDegrees( (int)(sVar + sMean*sMean));
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Chi_distribution");
    }

}

