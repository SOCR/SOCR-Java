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
 * This class models the Arc-Sine distribution on a specified interval. 
 * <a href="http://www.slashbin.be/math/rangen/doc/node12.html">
 * http://www.slashbin.be/math/rangen/doc/node12.html </a>.
 */
public class ArcSineDistribution extends Distribution {

    private double c=1/Math.PI;

    /**
     * @uml.property name="minValue"
     */
    private double minValue;

    /**
     * @uml.property name="maxValue"
     */
    private double maxValue;

    /**
     * This general constructor creates a new ArcSine distribution on a
     * specified interval and with a specified orientation.
     */
    public ArcSineDistribution(double a, double b) {
        setParameters(a, b);
        name = "ArcSine Distribution";
    }

    /**
     * This default constructor creates a new ArcSine distribution on the
     * interval (0, 1) with positive slope
     */
    public ArcSineDistribution() {
        this(0, 1);
    }

    public void initialize() {
        createValueSetter("Min", CONTINUOUS, 0, 10, 0);
        createValueSetter("Max", CONTINUOUS, 0, 10, 1);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /**
     * This method sets the parameters: the minimum value, maximum value, and
     * orientation.
     */
    public void setParameters(double a, double b) {
        minValue = a;
        maxValue = b;
        if (minValue >= maxValue) maxValue = minValue + 1;

        double stepSize = Math.pow(10, -2 - Math.log(maxValue - minValue)/Math.log(10));
        super.setParameters(minValue, maxValue, stepSize, CONTINUOUS);
        //Compute normalizing constant
        //c = (maxValue - minValue) * (maxValue - minValue);
    }

    //**This method computes the density.*/
    public double getDensity(double x) {
        if (minValue < x && x < maxValue) {
            return c/Math.sqrt((x-minValue)*(maxValue-x));
        } else return 0;
    }

    /** This method computes the maximum value of the getDensity function. */
    public double getMaxDensity() {
        return getDensity(minValue+0.001);
    }

    /** This method computes the mean. */
    public double getMean() {
        return (minValue + maxValue)/2;
    }

    /**
     * This method returns the minimum value.
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

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://www.slashbin.be/math/rangen/doc/node12.html");
    }
}

