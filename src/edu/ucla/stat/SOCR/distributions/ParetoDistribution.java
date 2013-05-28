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
 * This class models the Pareto distribution with a specified parameters
 * (alpha=power; theta=LeftStart). <a
 * http://en.wikipedia.org/wiki/Pareto_distribution">
 * http://en.wikipedia.org/wiki/Pareto_distribution </a>.
 */
public class ParetoDistribution extends Distribution {

    /**
     * @uml.property name="parameter1"
     */
    //Variable: alpha=power
    private double parameter1;

    /**
     * @uml.property name="parameter2"
     */
    //Variable: theta=LeftStart
    private double parameter2;

    /**
     * This general constructor creates a new Pareto distribuiton with specified
     * parameters, a and theta
     */
    public ParetoDistribution(double a, double theta) {
        setParameter(a, theta);
    }

    public ParetoDistribution(double a) {
        setParameter(a, 1);
    }

    /**
     * The default constructor creates a new Pareto distribution with parameter
     * 1
     */
    public ParetoDistribution() {
        this(1, 1);
        name = "Pareto Distribution";

    }

    public void initialize() {
        createValueSetter("Param: Alpha (power)", CONTINUOUS, 0, 10);
        createValueSetter("Param: Theta (shift)", CONTINUOUS, 0, 10);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameter(v1, v2);
    }

    /** This method sets the parameter and computes the default interval */
    public void setParameter(double a, double theta) {
        if (a <= 0) a = 1;
        parameter1 = a;
        if (theta <= 0) theta = 1;
        parameter2 = theta;

        double upper = parameter2*(20/parameter1); // / parameter1;
        double width = (upper - parameter2) / 200;
        super.setParameters(parameter2, upper, width, CONTINUOUS);
    }

    /** This method returns the parameter */
    public double getParameter() {
        return parameter1;
    }

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
        if (x < parameter2) return 0;
        else return ((parameter1 * Math.pow(parameter2, parameter1)) / Math.pow(x,
                parameter1 + 1));
    }

    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return parameter1 / parameter2;
    }

    /** This method computes the mean */
    public double getMean() {
        if (parameter1 > 1) return (parameter1 * parameter2 / (parameter1 - 1));
        else return Double.POSITIVE_INFINITY;
    }

    /** This method computes the variance */
    public double getVariance() {
    	if (parameter1 == 1 || parameter1 == 2) return Double.POSITIVE_INFINITY;
    	else if (parameter1 > 2) return (parameter1 * Math.pow(parameter2, 2) / ((parameter1 - 1)
                * (parameter1 - 1) * (parameter1 - 2)));
        else return Double.NaN;
    }

    /** This method comptues the cumulative distribution function */
    public double getCDF(double x) {
        return (1 - Math.pow(parameter2 / x, parameter1));
    }

    /** This method computes the getQuantile function */
    public double getQuantile(double p) {
        return parameter2 / Math.pow(1 - p, 1 / parameter1);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Pareto_distribution");
    }

    //implemented according to
    //http://en.wikipedia.org/wiki/Pareto_distribution#Parameter_estimation
    public void paramEstimate(double[] distData) {
    	double a, theta;
    	double sum = 0;
    	
    	//find minimum value in data array
    	//estimate theta (left start)
    	theta = distData[0];
    	for(int i = 1; i < distData.length; i++){
    		if(distData[i] < theta)
    			theta = distData[i];
    	}

    	//estimate alpha (power)
    	double logOfTheta = Math.log(theta);
    	for(int i = 0; i < distData.length; i++){
    		sum += (Math.log(distData[i]) - logOfTheta); 
    	}
    	
    	a = distData.length/sum;
    	setParameter(a, theta);
    }	
}

