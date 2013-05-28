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
import edu.ucla.stat.SOCR.util.BesselFunction;

/**
 * This class models the Von-Mises (Circular Gaussian) distribution on [-Pi; Pi].
 * <a href="http://mathworld.wolfram.com/vonMisesDistribution.html">
 * http://mathworld.wolfram.com/vonMisesDistribution.html </a>.
 */
public class VonMisesDistribution extends Distribution {

    /**
     * @uml.property name="minValue"
     */
    private double minValue=-Math.PI;

    /**
     * @uml.property name="maxValue"
     */
    private double maxValue=Math.PI;

    /**
     * @uml.property name="mu" (center/mean)
     */
    private double mu=0;

    /**
     * @uml.property name="k" (reciprocal of Variance). Must be positive.
     */
    private double k=1;

    /**
     * This general constructor creates a new Von Mises distribution on a specified interval. 
     * The parameters mu and ? are mean and approximately the inverse of the variance, for large k. 
     * The distribution becomes very concentrated about the angle mu with large K.
     * As ? increases, the distribution approaches a normal distribution in x with 
     * mean mu and variance 1/k.
     */
    public VonMisesDistribution(double mu, double k) {
        setParameters(mu, k);
    }

    /** This default constructor creates a new Von Mises distribuiton on (0, 1). */
    public VonMisesDistribution() {
        this(0, 1);
        name = "Von Mises Distribution";
    }

    public void initialize() {
        createValueSetter("Center (mu)", CONTINUOUS, -100, 100, 0);
        createValueSetter("Reciprocal of Variance (k)", CONTINUOUS, 0, 100, 1);
    }

    public void valueChanged(Observable o, Object arg) {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        if (v2 <= 0) {	
        	v2=1;
        	getValueSetter(1).setValue(v2);
        }
        setParameters(v1, v2);
    }

    /**
     * This method sets the parameters: the minimum and maximum values of the
     * interval.
     */
    public void setParameters(double mu, double k) {
        this.mu = mu;
        if (k<=0) k=1;
        this.k = k;
        minValue = -Math.PI + mu;
        maxValue = mu + Math.PI;
        double step = 0.001 * (maxValue - minValue);
        super.setParameters(minValue, maxValue, step, CONTINUOUS);
    }

    /** This method computes the density function. */
    public double getDensity(double x) {
        if (minValue <= x && x <= maxValue) 
		return (Math.exp(k*Math.cos(x-mu))/(2*Math.PI*I0(k)));
        else return 0;
    }

    /** This method computes the maximum value of the getDensity function. */
    public double getMaxDensity() {
        return getDensity(this.mu);
    }

    /** This method computes the mean. */
    public double getMean() {
        return this.mu;
    }

    /** This method computes the variance. */
    public double getVariance() {
        return (1 - (I1(k)*I1(k))/(I0(k)*I0(k)));
    }

    /** This method Calls the Bessel Function defined in
	edu.ucla.stat.SOCR.util.BesselFunction
     */
    public double I0(double x) {
	double besselValue = 0;
	try {	besselValue = edu.ucla.stat.SOCR.util.BesselFunction.i0(x);
	} catch (Exception e) { }
	return besselValue;
    }

    /** This method Calls the Bessel Function defined in
	edu.ucla.stat.SOCR.util.BesselFunction
     */
    public double I1(double x) {
	double besselValue = 0;
	try {	besselValue = edu.ucla.stat.SOCR.util.BesselFunction.i1(x);
	} catch (Exception e) { }
	return besselValue;
    }

    /** This method computes the Standard Diviation */
    public double getSD() {
        return Math.sqrt(getVariance());
    }

    /** This method computes the cumulative distribution function.
	Not known in closed simple form: 
	http://en.wikipedia.org/wiki/Von_Mises_distribution
    public double getCDF(double x) {
     }
    */

    /**
     * This method gets the minimum value.
     * @uml.property name="minValue"
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * This method returns the maximum value.
     * @uml.property name="maxValue"
     */
    public double getMaxValue() {
        return maxValue;
    }

    /** This method simulates a value from the distribution. To Be Completed!
    public double simulate() {
        return minValue + Math.random() * (maxValue - minValue);
    }
    */

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/vonMisesDistribution.html");
    }

    //Statistical Distributions, 3rd edition (2000) by Merran Evans ...
    public void paramEstimate(double[] distData) {
    	double sum1=0, sum2=0, a, b;
    	int n = distData.length;
    	
    	//calculate a
    	for(int i = 0; i < n; i++){
    		sum1 += Math.sin(distData[i]);
    	}

    	for(int i = 0; i < n; i++){
    		sum2 += Math.cos(distData[i]);
    	}
    	
    	a = Math.atan(sum1/sum2);
    	
    	//calculate b
    	b = 1/n * Math.pow((Math.pow(sum1,2)+Math.pow(sum2, 2)),0.5);
    	
    	setParameters(a, b);
    	
    }
    
    public double getK(){
    	return k;
    }

}

