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
 * This class models the Quadratic U distribution on a specified interval. <a
 * href="http://wiki.stat.ucla.edu/socr/index.php/UQuadraticDistribuionAbout">
 * http://wiki.stat.ucla.edu/socr/index.php/UQuadraticDistribuionAbout </a>.
 */
public class UQuadraticDistribution extends Distribution {

	/**
     * These are the short parameters described here: 
     * http://wiki.stat.ucla.edu/socr/index.php/UQuadraticDistribuionAbout
     */
	private double alpha=0.0, beta=0.0;
	
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
    public UQuadraticDistribution(double a, double b) {
        setParameters(a, b);
        name = "U Quadratic Distribution";
    }

    /** This default constructor creates a new U Quadratic distribuiton on (0, 1). */
    public UQuadraticDistribution() {
        this(0, 1);
        name = "U Quadratic Distribution";
    }

   /** This a constructor that creates a new U Quadratic distribuiton model
  	*  based on an array of data.
    */
    public UQuadraticDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            rData[i] = (double) distData[i];
		paramEstimate(rData);
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
        if (a < b) {
        	minValue = a;
        	maxValue = b;
        } else {
        	minValue = a;
        	maxValue = a+1;
        }
        alpha = (12.0)/((maxValue - minValue)*(maxValue - minValue)*(maxValue - minValue));
        beta = (maxValue + minValue)/2;
        
        double step = 0.01 * (maxValue - minValue);
        //System.err.println("\talpha="+alpha+"\tbeta="+beta+"\tA="+minValue+"\tmaxValue="+maxValue);
        super.setParameters(minValue, maxValue, step, CONTINUOUS);
    }

    /** This method computes the density function. */
    public double getDensity(double x) {
        if (minValue <= x & x <= maxValue) return alpha*(x-beta)*(x-beta);
        else return 0;
    }

    /** This method computes the maximum value of the getDensity function. */
    public double getMaxDensity() {
        return 3.0/((maxValue-minValue));
    }

    /** This method computes the mean. */
    public double getMean() {
        return (minValue + maxValue) / 2;
    }

    /** This method computes the median. */
    public double getMedian() {
        return (minValue + maxValue) / 2;
    }

    /** This method computes the variance. 
      * Var(X) = E(X^2) - mu^2.
     */
    public double getVariance() {
        return  (3.0/20)*Math.pow(maxValue-minValue,2);
    }

    /** This method computes the cumulative distribution function. */
    public double getCDF(double x) {
        if (x < minValue) return 0;
        else if (x >= maxValue) return 1;
        else return (alpha/3)*(Math.pow(x-beta,3)+Math.pow(beta-minValue,3));
    }

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

    /**
     * This method returns the ALPHA value.
     * @uml.property name="alpha"
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * This method returns the BETA value.
     * @uml.property name="beta"
     */
    public double getBeta() {
        return beta;
    }

    /** This method estimates the parameters of this distribution. */
   public void paramEstimate(double[] distData) {
	    double min=0, max=0;
	    for (int i = 0; i < distData.length; i++) {
	    	if (min > distData[i]) min = distData[i];
	    	if (max < distData[i]) max = distData[i];
	    }   
		setParameters(min, max);
   }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://wiki.stat.ucla.edu/socr/index.php/UQuadraticDistribuionAbout");
    }

}

