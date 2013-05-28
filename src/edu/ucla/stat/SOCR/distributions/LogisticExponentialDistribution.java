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

/** This class models the LogisticExponential distribution 
 * <a href="http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/lexpdf.htm">
 * http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/lexpdf.htm </a>
 * */
public class LogisticExponentialDistribution extends Distribution {

	//Paramters
    /**
     * @uml.property name="mu" LOCATION
     */
    private double mu;

    /**
     * @uml.property name="sigma" SCALE
     */
    private double sigma;

    /**
     * @uml.property name="shape" SHAPE
     */
    private double shape;

    /**
     * This general constructor creates a new LogisticExponential distribution with specified
     * parameter values
     */
    public LogisticExponentialDistribution(double mu, double sigma, double shape) {
        setParameters(mu, sigma, shape);
        name = "Logistic-Exponential Distribution";
    }

    /** This default constructor creates a new logitic-exponential distribution */
    public LogisticExponentialDistribution() {
        this(0.0,1.0, 0.5);
    }
    
    public void initialize() { 
        createValueSetter("Location (mu)", CONTINUOUS, -20, 20, 0);
        createValueSetter("Scale (sigma)", CONTINUOUS, 0, 10, 1);
        createValueSetter("Shape (beta)", CONTINUOUS, 0, 1, 1);
    }
    
    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        setParameters(v1, v2, v3);
    }

    /** This method sets the parameters */
    public void setParameters(double m, double s, double _shape) { 
    	// m = location and s = scale, and _shape = shape.
		double lower, upper, width;
			//Correct for invalid scale >0
		if (s <= 0.1)
		{	s = 1;
			try {
				ValueSetter valueSetter = getValueSetter(1);
				getValueSetter(1).setValue(1);
			} catch (Exception e) {
			}
		}
		else {
			try {
				ValueSetter valueSetter = getValueSetter(1);
			} catch (Exception e) {
			}
		}
		
		if (_shape <=0) _shape = 1.0;
		mu = m;
		sigma = s;
		shape = _shape;
		
		upper = inverseCDF(0.999); // mu + 9 * sigma;
		width = upper / 100;
		super.setParameters(mu, upper, width, CONTINUOUS);
		name = "Logistic-Exponential ("+mu+", "+sigma+", "+shape+") Distribution";
    }

    /**
     * Inverse of the cumulative Normal distribution function.
     * @return the value X for which P(x&lt;X).
     */
     public double inverseCDF(double probability) {
		if (0<=probability && probability <=1)
               return findRoot(probability, mu+0.0001, mu, mu+10); 
	 	else if (probability <0) return 0;
		else return 1.0;  // (1 < probability)
     }

    /** This method computes the getDensity function */
    public double getDensity(double x) {
        if (mu<x) return stdLogistictExpPDF((x-mu)/sigma)/sigma;
        else return 0.0;
    }

    /** This method computes the MAX value of the Density  */
    public double getMaxDensity() {
        return getDensity(mu+0.01);
    }

    /** This method computes the Standard LogLogit Density function */
    public double stdLogistictExpPDF(double x) {
    	if (0<x) 
    		return shape*Math.exp(x)*Math.pow(Math.exp(x)-1, shape-1)/
    			((1+Math.pow(Math.exp(x)-1, shape))*(1+Math.pow(Math.exp(x)-1, shape)));
        else return 0.0;
    }

    /** This method computes the CDF function */
    public double getCDF(double x) {
        if (mu<x) return stdLogistictExpCDF((x-mu)/sigma);
        else return 0.0;
    }

    /** This method computes the Standard LogLogit CDF function */
    public double stdLogistictExpCDF(double x) {
    	if (0<x) 
    		return 1-(1.0/(1 + Math.pow(Math.exp(x)-1, shape)));
        else return 0.0;
    }

    /** This method computes the maximum value of the getDensity function --
    public double getMaxDensity() {
        return getDensity(mu);
    }
    ***/

    /** This method returns the mean */
    public double getLocation() {
        return mu;
    }

    /** This method returns the scale */
    public double getScale() {
        return sigma;
    }

    /** This method returns the shape */
    public double getShape() {
        return shape;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://www.itl.nist.gov/div898/software/dataplot/refman2/auxillar/lexpdf.htm");
    }
}

