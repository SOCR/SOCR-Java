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
 * A Java implementation of the (Two-Sided POwer (TSP) Distribution with specified:
 * left, right, mean and power parameters <a
 * href="http://www.springerlink.com/content/u71g0104356x70u1/">
 * http://www.springerlink.com/content/u71g0104356x70u1/ </a>.
 */

public class TwoSidedPowerDistribution extends Distribution {

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
     * @uml.property name="med"
     */
    private double med;

    /**
     * @uml.property name="power"
     */
    private double power;

    public TwoSidedPowerDistribution(double l, double r, double m, double p) {
    	name = "Two-Sided Power Distribution";
        setParameters(l, r, m, p);
    }

    public void initialize() {
        createValueSetter("Left Limit", CONTINUOUS, -10, 10, -1); // iv: initial value
        createValueSetter("Right Limit", CONTINUOUS, -10, 10, 1);
        createValueSetter("Mode", CONTINUOUS, -10, 10, 0);
        createValueSetter("Power", CONTINUOUS, 0, 11, 1);
        setParameters(-1.0, 1.0, 0.0, 1.0);
    }

    public void valueChanged(Observable o, Object arg) {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        double v4 = getValueSetter(3).getValue();
        if (!(v2 > v1)) {
            if (arg == getValueSetter(0)) { //A-Limit, v2 modified
                v2 = v1 + 1.0;
                getValueSetter(1).setValue(v2);
            } else if (arg == getValueSetter(1)) { //B-Limit, v1 modified
                v1 = v2 - 1.0;
                getValueSetter(0).setValue(v1);
            }
            return;
        }
        if (v3 <= v1 || v3 >= v2) {
            getValueSetter(2).setValue(
            	(getValueSetter(0).getValue()+getValueSetter(1).getValue())/2);
            return;
        }
        setParameters(v1, v2, v3, v4);
    }

    /**
     * Default constructor: creates a beta distribution with left and right
     * parameters equal to 1
     */
    public TwoSidedPowerDistribution() {
        this(-1, 1, 0, 1);
    }

    /**
     * Set the parameters, compute the normalizing constant c, and specifies the
     * all 4 parameters
     * @uml.property name="left"
     * @uml.property name="right"
     * @uml.property name="med"
     * @uml.property name="power"
     */
    public void setParameters(double l, double r, double m, double p) {
        double lower, upper, step;

        //Correct parameters that are out of bounds
        if (l >= r) r = l +1;
        if (l >=m || m >= r) m = (l+r)/2;
        if (p <= 0) p = 1;
        left = l;
        right = r;
        med = m;
        power = p;

        //Compute the normalizing constant
        c = power/(right -left);

        //Specifiy the interval and partiton
        super.setParameters(left, right, 0.01 * (right - left), CONTINUOUS);
    }

    /** --------------------------------SET------------------------------- */
    /** Sets the left parameter */
    public void setLeft(double l) {
        setParameters(l, right, med, power);
    }

    /** Sets the right parameter */
    public void setRight(double r) {
        setParameters(left, r, med, power);
    }

    /** Sets the med parameter */
    public void setMed(double m) {
        setParameters(left, right, m, power);
    }

    /** Sets the power */
    public void setPower(double p) {
        setParameters(left, right, med, p);
    }

    /**
     * ----------------------------------GET--------------------------
     * @uml.property name="left"
     */
    public double getLeft() {
        return left;
    }

    /**
     * Get the right parameter
     * @uml.property name="right"
     */
    public double getRight() {
        return right;
    }

    /**
     * Get the left LIMIT
     * @uml.property name="med"
     */
    public double getMed() {
        return med;
    }

    /**
     * Get the right LIMIT
     * @uml.property name="power"
     */
    public double getPower() {
        return power;
    }

    /** Define the TSP Density function */
    public double getDensity(double x) {
        if ((x <= left) || (x >= right)) return 0;
        else if (x > left && x < med) return c*Math.pow((x-left)/(med-left), power-1);
        else return c*Math.pow((right-x)/(right-med), power-1);
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return getDensity(getMode());
    }

    /** Compute the Mode */
    public double getMode() {
        double mode;
        if (power >1) mode = med;
        else if (power==1) mode = 1.0/(right-left);
        else mode = left+0.05;
        return mode;
    }

    public double getMedian() {
        return getMode();
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return (left + right + (power-1)*med) / (power+1);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return (right - left) * (right - left) / ((power+1)*(power+1));
    }

    /** HOW SHOULD THESE PARAMETERS be estimated????? */
    public void paramEstimate(double[] distData) {
        double alpha, beta;
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        //setParameters(lt, rt, me, pow);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://www.springerlink.com/content/u71g0104356x70u1/");
    }
}

