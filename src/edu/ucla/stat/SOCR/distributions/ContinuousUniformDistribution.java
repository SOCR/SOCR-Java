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
 * This class models the uniform distribution on a specified interval. <a
 * href="http://mathworld.wolfram.com/UniformDistribution.html">
 * http://mathworld.wolfram.com/UniformDistribution.html </a>.
 */
public class ContinuousUniformDistribution extends Distribution {

    /**
     * @uml.property name="minValue"
     */
    private double minValue;

    /**
     * @uml.property name="maxValue"
     */
    private double maxValue;

    /**
     * This general constructor creates a new uniform distribution on a
     * specified interval.
     */
    public ContinuousUniformDistribution(double a, double b) {
        setParameters(a, b);
    }

    /** This default constructor creates a new uniform distribuiton on (0, 1). */
    public ContinuousUniformDistribution() {
        this(0, 1);
        name = "Continuous Uniform Distribution";
    }

    public void initialize() {
        createValueSetter("LeftEnd-Limit", CONTINUOUS, -10, 10);
        createValueSetter("RightEnd-Limit", CONTINUOUS, -9, 11);
    }

    public void valueChanged(Observable o, Object arg) {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        if (!(v2 > v1)) {
            if (arg == getValueSetter(0)) {
                v2 = v1 + 1.0;
                getValueSetter(1).setValue(v2);
            } else {
                v1 = v2 - 1.0;
                getValueSetter(0).setValue(v1);
            }
            return;
        }
        setParameters(v1, v2);
    }

    /**
     * This method sets the parameters: the minimum and maximum values of the
     * interval.
     */
    public void setParameters(double a, double b) {
        if (a >= b) b=a+1;
        minValue = a;
        maxValue = b;
        double step = 0.01 * (maxValue - minValue);
        super.setParameters(minValue, maxValue, step, CONTINUOUS);
        super.setMGFParameters(0.0, 5.0, 100);
    }

    /** This method computes the density function. */
    public double getDensity(double x) {
        if (minValue <= x & x <= maxValue) return 1 / (maxValue - minValue);
        else return 0;
    }

    /** This method computes the maximum value of the getDensity function. */
    public double getMaxDensity() {
        return 1 / (maxValue - minValue);
    }

    /** This method computes the mean. */
    public double getMean() {
        return (minValue + maxValue) / 2;
    }

    /** This method computes the variance. */
    public double getVariance() {
        return (maxValue - minValue) * (maxValue - minValue) / 12;
    }

    /** This method computes the cumulative distribution function. */
    public double getCDF(double x) {
        if (x < minValue) return 0;
        else if (x >= maxValue) return 1;
        else return (x - minValue) / (maxValue - minValue);
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. 
     */
    public double getMGF(double t) 
    {
    	return (Math.exp(maxValue*t)-Math.exp(minValue*t))/(t*(maxValue-minValue));
    }

    /** This method computes the getQuantile function. */
    public double getQuantile(double p) {
        if (p < 0) p = 0;
        else if (p > 1) p = 1;
        return minValue + (maxValue - minValue) * p;
    }

    /**
     * This method gets the minimum value.
     * 
     * @uml.property name="minValue"
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * This method returns the maximum value.
     * 
     * @uml.property name="maxValue"
     */
    public double getMaxValue() {
        return maxValue;
    }

    /** This method simulates a value from the distribution. */
    public double simulate() {
        return minValue + Math.random() * (maxValue - minValue);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/UniformDistribution.html");
    }

}

