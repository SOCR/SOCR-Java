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
 * This class models the Lomax distribution (Pareto-distribution of hte second-kind)
 * with a specified parameters (alpha=shape1; gamma=shape2). <a
 * http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/pa2pdf.htm">
 * http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/pa2pdf.htm</a>.
 */
public class LomaxDistribution extends Distribution {

    /**
     * @uml.property name="parameter1"
     */
    //Variable: alpha=shape1
    private double parameter1;

    /**
     * @uml.property name="parameter2"
     */
    //Variable: gamma=shape2
    private double parameter2;

    /**
     * This general constructor creates a new Lomax distribuiton with specified
     * parameters, alpha and gamma
     */
    public LomaxDistribution(double a, double g) {
        setParameter(a, g);
        name = "Lomax Distribution";
    }

    /**
     * The default constructor creates a new Lomax distribution 
     * with parameters (a=1, gamma)
     */
    public LomaxDistribution(double g) {
        this(1, g);
    }

    /**
     * The default constructor creates a new Lomax distribution 
     * with parameters (1, 1)
     */
    public LomaxDistribution() {
        this(1, 1);
    }

    public void initialize() {
        createValueSetter("Shape-Parameter1: (Alpha)", CONTINUOUS, 0, 10);
        createValueSetter("Shape-Parameter2: (Gamma)", CONTINUOUS, 0, 10);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameter(v1, v2);
    }

    /** This method sets the parameter and computes the default interval */
    public void setParameter(double a, double g) {
        if (a <= 0) a = 1;
        parameter1 = a;
        if (g <= 0) g = 1;
        parameter2 = g;

        double upper = parameter1*(20/parameter2);
        double width = (upper) / 100;
        super.setParameters(0, upper, width, CONTINUOUS);
    }

    /*
     * @uml.property name="parameter1"
     * @uml.property name="parameter2"
     */
    public double[] getParameters() {
        return new double[] { parameter1, parameter2 };
    }

    /**
     * @uml.property name="parameter1"
     */
    public double getParameter1() {
        return parameter1;
    }

    /**
     * @uml.property name="parameter2"
     */
    public double getParameter2() {
        return parameter2;
    }

    /** This method computes the getDensity function */
    public double getDensity(double x) {
        if (x < 0) return 0;
        else return ((parameter2 * Math.pow(parameter1, parameter2)) / 
        		Math.pow(x+parameter1, parameter2 + 1));
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return parameter2 / parameter1;
    }

    /** This method computes the mean */
    public double getMean() {
        if (parameter2 > 1) return (parameter2 * parameter1 / (parameter2 - 1));
        else return Double.POSITIVE_INFINITY;
    }

    /** This method computes the variance */
    public double getVariance() {
    	if (parameter2 == 1 || parameter2 == 2) return Double.POSITIVE_INFINITY;
    	else if (parameter2 > 2) return (parameter2 * Math.pow(parameter1, 2) /
    			((parameter2 - 1) * (parameter2 - 1) * (parameter2 - 2)));
        else return Double.NaN;
    }

    /** This method comptues the cumulative distribution function */
    public double getCDF(double x) {
        if (x>=0) return (1 - Math.pow(parameter1, parameter2)/
        				Math.pow(x+parameter1, parameter2));
        else return 0.0;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/pa2pdf.htm");
    }
}

