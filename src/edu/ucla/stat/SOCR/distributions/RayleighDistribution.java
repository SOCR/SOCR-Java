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
 * A Java implmentation of the Rayleigh distribution with specified alpha & beta
 * parameters <a href="http://mathworld.wolfram.com/RayleighDistribution.html">
 * http://mathworld.wolfram.com/RayleighDistribution.html </a>
 */
public class RayleighDistribution extends Distribution {

    /**
     * @uml.property name="beta"
     */
    //Parameters
    private double beta = 1.0;

    double xlo;
    double xhi;

    /**
     * @uml.property name="coef"
     */
    double coef = 1.0;

    /**
     * General Constructor: creates a Rayleigh distribution with specified beta
     * parameter
     */
    public RayleighDistribution(double b) {
        setParameters(b);
    }

    /** Default constructor: creates a Rayleigh distribution with beta parameter */
    public RayleighDistribution() {
        this(10.0);
        name = "Rayleigh Distribution";

    }

    public void initialize() {
        createValueSetter("Beta", CONTINUOUS, 0, 5);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        setParameters(v1);
    }

    /**
     * Set the parameters, compute the normalizing constant c, and specifies the
     * interval and partition
     * 
     * @uml.property name="coef"
     */
    public void setParameters(double b) {
        double lower, upper, step;
        //Correct parameters that are out of bounds
        if (b <= 0) beta = 10;
        else beta = b;

        coef = 1.0 / (beta * beta);

        //Specifiy the interval and partiton
        double upperBound = getMean() + 4 * getSD();
        double lowerBound = 0;
        super.setParameters(lowerBound, upperBound, 0.01, CONTINUOUS);
        super.setMGFParameters();
    }

    /**
     * Get the parameter
     * 
     * @uml.property name="beta"
     */
    public double getParameter() {
        return beta;
    }

    /** Define the beta getDensity function */
    public double getDensity(double x) {
        if (x > 0) return (coef * x * Math.exp(0.0 - 0.5 * x * x * coef));
        else return 0;
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return (Math.exp(-0.5) / beta);
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return beta * Math.sqrt(Math.PI / 2);
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return beta * beta * (4 - Math.PI) / 2;
    }

    /**
     * Compute the cumulative distribution function. The Rayleigh CDF is built
     * into the superclass Distribution
     */
    public double getCDF(double x) {
        return (1.0 - Math.exp(0.0 - 0.5 * x * x * coef));
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t)
    {	
    	double myERF_Value = NormalDistribution.errorFunction(beta*t/Math.sqrt(2.0));
    	double mgf =  1+beta*t*Math.exp((beta*beta*t*t)/2.0)*Math.sqrt(Math.PI/2.0)*(myERF_Value+1.0);
    	return mgf;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/RayleighDistribution.html");
    }
    
    //implemented according to
    //http://en.wikipedia.org/wiki/Rayleigh_distribution
    //under the section "Parameter estimation"
    public void paramEstimate(double[] distData){
    	double beta;
    	double sum = 0;
    	for(int i=0; i < distData.length; i++){
    		sum += (distData[i]*distData[i]);
    	}
    	beta = Math.sqrt(sum/(2*distData.length));
    	setParameters(beta);
    }    
}

