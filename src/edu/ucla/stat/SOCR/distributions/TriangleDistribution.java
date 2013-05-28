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
 * This class models the Triangular distribution on a specified interval. If (X,
 * Y) is Uniformly distributed on a triangular region, then X and Y have
 * triangular distribuitons. <a
 * href="http://mathworld.wolfram.com/TriangularDistribution.html">
 * http://mathworld.wolfram.com/TriangularDistribution.html </a>.
 */
public class TriangleDistribution extends Distribution {

    /**
     * @uml.property name="orientation"
     */
    private int orientation;

    private double c;

    /**
     * @uml.property name="minValue"
     */
    private double minValue;

    /**
     * @uml.property name="maxValue"
     */
    private double maxValue;

    public final static int UP = 0;
    public final static int DOWN = 1;

    /**
     * This general constructor creates a new triangle distribution on a
     * specified interval and with a specified orientation.
     */
    public TriangleDistribution(double a, double b, int i) {
        setParameters(a, b, i);
    }

    /**
     * This default constructor creates a new triangle distribution on the
     * interval (0, 1) with positive slope
     */
    public TriangleDistribution() {
        this(0, 1, UP);

        name = "Triangle Distribution";
    }

    public void initialize() {
        createValueSetter("Min", CONTINUOUS, 0, 5);
        createValueSetter("Max", CONTINUOUS, 1, 10);
        //orientation
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2, UP);
    }

    /**
     * This method sets the parameters: the minimum value, maximum value, and
     * orientation.
     */
    public void setParameters(double a, double b, int i) {
        minValue = a;
        maxValue = b;
        if (minValue >= maxValue) maxValue = minValue + 1;

        orientation = i;
        double stepSize = (maxValue - minValue) / 100;
        super.setParameters(minValue, maxValue, stepSize, CONTINUOUS);
        //Compute normalizing constant
        c = (maxValue - minValue) * (maxValue - minValue);
    }

    //**This method computes the density.*/
    public double getDensity(double x) {
        if (minValue <= x & x <= maxValue) {
            if (orientation == UP) return 2 * (x - minValue) / c;
            else return 2 * (maxValue - x) / c;
        } else return 0;
    }

    /** This method computes the maximum value of the getDensity function. */
    public double getMaxDensity() {
        double mode;
        if (orientation == UP) mode = maxValue;
        else mode = minValue;
        return getDensity(mode);
    }

    /** This method computes the mean. */
    public double getMean() {
        if (orientation == UP) return minValue / 3 + 2 * maxValue / 3;
        else return 2 * minValue / 3 + maxValue / 3;
    }

    /** This method computes the variance. */
    public double getVariance() {
        return (maxValue - minValue) * (maxValue - minValue) / 18;
    }

    /**
     * This method returns the minimum value.
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

    /**
     * This method returns the orientation.
     * 
     * @uml.property name="orientation"
     */
    public int getOrientation() {
        return orientation;
    }

    /** This method simulates a value from the distribution. */
    public double simulate() {
        double u = minValue + (maxValue - minValue) * Math.random();
        double v = minValue + (maxValue - minValue) * Math.random();
        if (orientation == UP) return Math.max(u, v);
        else return Math.min(u, v);
    }

    /** This method computes the cumulative distribution function. */
    public double getCDF(double x) {
        if (orientation == UP) return (x - minValue) * (x - minValue) / c;
        else return 1 - (maxValue - x) * (maxValue - x) / c;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://mathworld.wolfram.com/TriangularDistribution.html");
    }

}

