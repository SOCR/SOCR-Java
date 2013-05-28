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

//import JSci.maths.statistics.HyperbolicSecantDistribution;
/**
 * This class encapsulates the Hyperbolic-Secant distribution -- no parameters.
 * <a href="http://en.wikipedia.org/wiki/Hyperbolic_secant_distribution">
 * http://en.wikipedia.org/wiki/Hyperbolic_secant_distribution</a>.
 */
public class HyperbolicSecantDistribution extends Distribution {
    //Paramters
    public final static double C = 0.5; // HyperbolicSecant constant

    /**
     * This general constructor creates a new HyperbolicSecant distribution with specified
     * parameter values
     */
    public HyperbolicSecantDistribution() {
    	
    	super.setParameters(-5, 5, 0.5, CONTINUOUS);
    	name = "HyperbolicSecant Distribution";
    }

    /** This method defines the getDensity function 
     *  http://en.wikipedia.org/wiki/Hyperbolic_secant_distribution
     * */
    public double getDensity(double x) {
        return C*sech(Math.PI*x / 2);
    }

    /** This method defines the hyperbolic-secant function
     *  http://en.wikipedia.org/wiki/Hyperbolic_function
     */
    public double sech(double x) {
    	return 2.0/(Math.exp(x)+Math.exp(-x));
    }

    
    /** This method returns the maximum value of the getDensity function */
    public double getMaxDensity() {
        return getDensity(getMean());
    }

    /** This method returns the median */
    public double getMedian() {
        return 0.0;
    }

    /** These methods return the mean */
    public double getMean() {
        return 0.0;
    }

    /** These methods return the variance */
    public double getVariance() {
        return 1.0;
    }

    /** This method computes the cumulative distribution function */
    public double getCDF(double x) {
        return (2.0/Math.PI)*(Math.atan(Math.exp(Math.PI*x/2)));
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) {
    	if (Math.abs(t) < Math.PI/2) return secant(t);
    	else return 0.0;
    }
    	
    /** Computes the secant function
     * http://en.wikipedia.org/wiki/Trigonometric_function
     *  */
    public double secant(double t) {
    	return Math.cos(t);
    }
    
    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Hyperbolic_secant_distribution");
    }
}

