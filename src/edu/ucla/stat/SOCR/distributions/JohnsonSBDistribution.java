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

import java.util.Observable;

import edu.ucla.stat.SOCR.core.*;

/**
 * This class models the Johnson SB (Special Bounded) distribution with specified first 4 parameters
 * (mean, SD, skewness, kurtosis):
 * The Johnson family of distributions (N.L. Johnson, 1949), is a versatile model distribution. 
 * It is based on a transformation of the standard normal variable, and includes 4 forms:
 *    1. Unbounded: the set of distributions that go to infinity in both the upper or lower tail.
 *    2. Bounded: the set of distributions that have a fixed boundary on either the upper or lower tail, or both.
 *    3. Log Normal: a border between the Unbounded and Bounded distribution forms.
 *    4. Normal: a special case of the Unbounded form.
 * 
 * The flexibility of Johnson family of distributions comes from the choice of form and fitting 
 * parameters which allows better fits data. The Johnson family involves a transformation of 
 * the raw variable to a Normal variable. This facilitates the estimates of the percentiles of 
 * the fitted distribution to be calculated from the Normal distribution percentiles. 
 * 
 * http://www.qualityamerica.com/knowledgecente/knowctrBest_Fit_Johnson.htm
 * http://www.mathwave.com/articles/johnson_sb_distribution.html
 */

public class JohnsonSBDistribution extends Distribution {
    //Parameters
    /**
     * @uml.property name="xi (location)"
     */
    private double xi;

    /**
     * @uml.property name="lambda (scale)"
     */
    private double lambda;

    /**
     * @uml.property name="gamma (shape)"
     */
    private double gamma;

    /**
     * @uml.property name="delta (shape)"
     */
    private double delta;

    NormalDistribution ND = new NormalDistribution(0,1);
    
    /**
     * This general constructor creates a new JohnsonSBDistribution distribution with
     * specified parameters
     * @param xi = location mean
     * @param lambda = scale SD
     * @param gamma = shape skewness
     * @param delta = shape kurtosis
     */
    public JohnsonSBDistribution(double _xi, double _lambda, double _gamma, double _delta) {
        setParameters(_xi, _lambda, _gamma, _delta);
    }

    /**
     * Default constructor: creates a beta distribution with xi and lambda
     * parameters equal to 1
     */
    public JohnsonSBDistribution() {
    	this(1.0, 1.0, 0, 1.0);
    	name = "Johnson Special-Bounded (SB) Distribution";
    }

    public void initialize() {
        createValueSetter("Xi (location)", CONTINUOUS, -10, 10, 1); // iv: initial value
        createValueSetter("Lambda (scale)", CONTINUOUS, 0, 10, 1);
        createValueSetter("Gamma (shape)", CONTINUOUS, -10, 10, 0);
        createValueSetter("Delta (shape)", CONTINUOUS, 0, 11, 1);
        setParameters(1.0, 1.0, 0.0, 1.0);
    }

    public void valueChanged(Observable o, Object arg) {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        double v4 = getValueSetter(3).getValue();
        setParameters(v1, v2, v3, v4);
    }

    /** This method sets the parameters, computes the default interval 
     * @param _xi = location
     * @param _lambda = scale
     * @param _gamma = shape
     * @param _delta = shape
     */
    public void setParameters(double _xi, double _lambda, double _gamma, double _delta) {
        //Correct parameters that are out of bounds
        if (_lambda <= 0) 	_lambda = 1.0;
        if (_delta <= 0)	_delta = 1.0;
        
        xi = _xi;
        lambda = _lambda;
        gamma = _gamma;
        delta = _delta;
        
        double lowerBound = xi;
        double upperBound = xi + lambda;
        
        //Specifiy the interval and partiton
        //System.err.println("xi="+xi+"\t lambda="+lambda+"\t gamma="+gamma+"\t delta="+delta);
        super.setParameters(lowerBound, upperBound, 0.01 * (upperBound-lowerBound), CONTINUOUS);
    }

    /** This method gets the 4 parameters 
     */
    public double[] getParameters() {
        return new double[] {this.xi, this.lambda, this.gamma, this.delta};
    }

    /**
     * @uml.property name="xi (location)"
     */
    public double getXi() {
        return xi;
    }
    /** This method sets mean */
    public void setXi(double _xi) {
        setParameters(_xi, this.lambda, this.gamma, this.delta);
    }

    /**
     * Get lambda
     * @uml.property name="lambda (scale)"
     */
    public double getLambda() {
        return lambda;
    }
    /** This method sets sigma */
    public void setLambda(double _lambda) {
    	setParameters(this.xi, _lambda, this.gamma, this.delta);
    }

    /**
     * Get  gamma
     * @uml.property name="gamma (shape)"
     */
    public double getGamma() {
        return gamma;
    }
    /** This method sets skewness */
    public void setGamma(double _gamma) {
    	setParameters(this.xi, this.lambda, _gamma, this.delta);
    }

    /**
     * Get delta
     * @uml.property name="delta (shape)"
     */
    public double getDelta() {
        return delta;
    }
    /** This method sets Kurtosis */
    public void setDelta(double _delta) {
    	setParameters(this.xi, this.lambda, this.gamma, _delta);
    }

    /** Define the beta getDensity function */
    public double getDensity(double x) {
    	// System.err.println("getDensity("+x+")");
    	if (x <= xi || x >= (xi+lambda)) return 0.0;
        double y = (x - xi)/lambda;
        double t1 = gamma + delta*Math.log (y/(1.0 - y));
        return delta/(lambda*y*(1.0 - y)*Math.sqrt (2.0*Math.PI))*Math.exp (-t1*t1/2.0);
    }

    /** This method computes the cumulative distribution function 
     * @param x = value to evaluate the CDF at
     * 
     * http://www.mathwave.com/articles/johnson_sb_distribution.html
     */
    public double getCDF(double x) {
    	if (x <= this.xi)	return 0.0;
    	if (x >= this.xi+this.lambda) return 1.0;
    	double y = (x - xi)/this.lambda;
    	return ND.getCDF(this.gamma + this.delta*Math.log (y/(1.0 - y)));
    }

    /**
     * Computes the inverse Johnson SB CDF function
     */
    public double inverseCDF (double probability) {
       double v, z;
       if (probability >= 1.0)  	return xi+lambda;
       else if (probability <= 0.0) return xi;

       z = ND.inverseStdNormalCDF(probability);
       v = (z - gamma)/delta;
       if ((z >= 40) || (v >= 1024*0.69314718055994530941)) return (xi+lambda);
       if ((z<= -40) || (v<= -1024*0.69314718055994530941)) return xi;
       v = Math.exp (v);
       return (xi+(xi+lambda)*v)/(1.0+v);
    }

    /** This method simulates a value from the distribution */
    public double simulate() {
        return inverseCDF(Math.random());
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://www.mathwave.com/articles/johnson_sb_distribution.html");
    }
}

