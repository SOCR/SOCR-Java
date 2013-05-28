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
 * This class models the Maxwell distribution with parameter a. The distribution
 * of speeds of molecules in thermal equilibrium as given by statistical
 * mechanics. <a href="http://mathworld.wolfram.com/MaxwellDistribution.html">
 * http://mathworld.wolfram.com/MaxwellDistribution.html </a>.
 */
public class MaxwellDistribution extends Distribution {

    /**
     * @uml.property name="a"
     */
    private double a;

    /** This general constructor creates a new Maxwell distribution. */
    public MaxwellDistribution(double a) {
        setParameter(a);
    }

    /** This special constructor creates a new Maxwell distribution with radius 1 */
    public MaxwellDistribution() {
        this(1);
        name = "Maxwell Distribution";

    }

    public void initialize() {
        createValueSetter("Alpha", CONTINUOUS, 0, 15);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        setParameter(v1);
    }

    /**
     * This method sets the radius parameter
     * 
     * @uml.property name="a"
     */
    public void setParameter(double _a) {
        if (_a <= 0) a = 1;
        else a = _a;
        double upper = Math.max(getMean() + 4 * getVariance(), getMean() + 2 * a
                * getVariance());
        super.setParameters(0, upper, 0.02, CONTINUOUS);
        super.setMGFParameters();
    }

    /** This method computes the getDensity function. */
    public double getDensity(double x) {
        if (0 <= x) return (4 * Math.sqrt(1 / Math.PI) * Math.pow(a, 1.5) * x * x * Math
                .exp(-a * x * x));
        else return 0;
    }

    /***************************************************************************
     * This method computes the maximum value of the getDensity function. public
     * double getMaxDensity(){ return 2*Math.sqrt(2*a/Math.PI); }
     **************************************************************************/

    /** This method computes the mean */
    public double getMean() {
        return 2 * Math.sqrt(2 / (Math.PI * a));
    }

    /** This method computes the variance */
    public double getVariance() {
        return (3 * Math.PI - 8) / (a * Math.PI);
    }

    /**
     * This method returns the radius parameter.
     * 
     * @uml.property name="a"
     */
    public double getParameter() {
        return a;
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t)
    {
    	double erfval1 = NormalDistribution.errorFunction((Math.sqrt(Math.pow(a, 4)*t*t)/a*a)/Math.sqrt(2));
    	double erfval2 = NormalDistribution.errorFunction((Math.sqrt(t*t*a*a/a*a)/Math.sqrt(2)));
    	return Math.sqrt(2/Math.PI)*1/Math.pow(a,3)*1/2*t*
    		t*Math.pow(a,4)*(2*t+Math.exp(t*t*a*a/2)*Math.sqrt(2*Math.PI)*Math.sqrt(1/a*a)+Math.exp(t*t*a*a/2)*Math.sqrt(2*Math.PI)*t*t*Math.sqrt(1/a*a)*a*a)
    		+(Math.exp(t*t*a*a/2)*Math.sqrt(2*Math.PI)*Math.pow(t*t/a*a, 3/2)*Math.pow(a, 8)*erfval1+Math.exp(t*t*a*a/2)*Math.sqrt(2*Math.PI)*a*a*Math.sqrt(t*t*a*a)*erfval2);
    }

    /***************************************************************************
     * This method compute the cumulative distribution function. public double
     * getCDF(double x){ if (x>0) { NormalDistribution nd = new
     * NormalDistribution(0.0, 1/Math.sqrt(2)); return (
     * (nd.getCDF(x*Math.sqrt(a)) -0.5) - ( x*Math.exp(-a*x*x) *
     * 2*Math.sqrt(a/Math.PI)) ); } else return 0; }
     **************************************************************************/

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/MaxwellDistribution.html");
    }

}

