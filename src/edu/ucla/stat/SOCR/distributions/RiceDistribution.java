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
import edu.ucla.stat.SOCR.util.BesselFunction;

/**
 * This class models the Rice (Rician) distribution.
 * <a http://en.wikipedia.org/wiki/Rice_distribution">
 * http://en.wikipedia.org/wiki/Rice_distribution </a>.
 */
public class RiceDistribution extends Distribution {

    /**
     * @uml.property name="nu" (center/mean)
     */
    private double nu=0;

    /**
     * @uml.property name="sigma". Must be positive.
     */
    private double sigma=1;

    /**
     * This general constructor creates a new Rician distribution. 
     * The parameters nu and sigma are roughly measures of centrality and variation, respectively.
     */
    public RiceDistribution(double _nu, double _sigma) {
    	name = "Rice Distribution";
    	setParameters(_nu, _sigma);
    }

    /** This default constructor creates a new Von Mises distribuiton on (0, 1). */
    public RiceDistribution() {
        this(0, 1);
        name = "Rice Distribution";
    }

    public RiceDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public RiceDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public RiceDistribution(double[] distData, boolean calledByModeler) {
		initialize();
		paramEstimate(distData);
    }
    public RiceDistribution(float[] distData, boolean calledByModeler) {
		initialize();
		double[] distDat = new double[distData.length];
		for (int i = 0; i < distData.length; i++)
			distDat[i] = (double) distData[i];
		paramEstimate(distDat);
    }

    public void initialize() {
        createValueSetter("Center (nu)", CONTINUOUS, -10, 10, 1);
        createValueSetter("Spread (sigma)", CONTINUOUS, 0, 10, 1);
    }

    public void valueChanged(Observable o, Object arg) {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        if (v2 <= 0) {	
        	v2=1;
        	getValueSetter(1).setValue(v2);
        }
        setParameters(v1, v2);
    }

    /**
     * This method sets the parameters: the minimum and maximum values of the
     * interval.
     */
    public void setParameters(double _nu, double _sigma) {
        this.nu = _nu;
        if (_sigma<=0) _sigma=1;
        this.sigma = _sigma;
        double minValue = 0;
        double maxValue = getMean() + 4*Math.sqrt(getVariance());
        double step = 0.001 * (maxValue - minValue);
        super.setParameters(minValue, maxValue, step, CONTINUOUS);
    }

    /** This method computes the Rician density function. */
    public double getDensity(double x) {
        double density=0.0;
        if (x > 0) 
        	density = (x/(sigma*sigma))*Math.exp(-(x*x+nu*nu)/(sigma*sigma*2))*I0((x*nu)/(sigma*sigma));       
        return density;
    }

    /** This method computes the maximum value of the getDensity function. 
     * This needs work!!!
     *
    public double getMaxDensity() {
        return getDensity(this.nu);
    	//return getDensity(getMean());
    }
    */
    
    /* This function is used to compute the Rician moments
     * L = Lhalf(-0.5 * v^2 / s^2);
     * mu = s * sqrt(pi/2) * L;
     * vr = 2*s^2 + v^2 - (pi * s^2 / 2) * L^2;
     * 
     * function l = Lhalf(x)
     * % Laguerre polynomial L_{1/2}(x)
     * % see Moments section of http://en.wikipedia.org/wiki/Rice_distribution
     * l = exp(x/2) * ( (1-x) * bessel_i(0, -x/2) - x*bessel_i(1, -x/2) );
     */
    public double L_half(double x) {
    	return ( Math.exp(x/2) * ((1-x)*I0(-x/2) - x*I1(-x/2)) );
    }
    
    /** This method computes the Rician mean. */
    public double getMean() {
    	double L = L_half(-0.5 * this.nu*this.nu/(sigma*sigma));
        double mu = sigma * Math.sqrt(Math.PI/2) * L;
    	return mu;
    }

    /** This method computes the Rician variance. */
    public double getVariance() {
    	double L = L_half(-0.5 * this.nu*this.nu/(sigma*sigma));
        double variance = 2*sigma*sigma + nu*nu - (Math.PI * sigma*sigma/2) * L*L;
        return variance;
    }

    /** This method Calls the Bessel Function of the ZERO kind defined in
	edu.ucla.stat.SOCR.util.BesselFunction
     */
    public double I0(double x) {
	double besselValue = 0;
	try {	besselValue = edu.ucla.stat.SOCR.util.BesselFunction.i0(x);
	} catch (Exception e) { }
	return besselValue;
    }

    /** This method Calls the Bessel Function of the FIRST kind defined in
	edu.ucla.stat.SOCR.util.BesselFunction
     */
    public double I1(double x) {
	double besselValue = 0;
	try {	besselValue = edu.ucla.stat.SOCR.util.BesselFunction.i1(x);
	} catch (Exception e) { }
	return besselValue;
    }

    /** This method computes the Standard Diviation */
    public double getSD() {
        return Math.sqrt(getVariance());
    }

    /** This method returns the Nu and Sigma parameter estimates from a sample dataset. 
     */
    public void paramEstimate(double[] distData) {
        double mu2 = getSampleMoment(2, distData);
        double mu4 = getSampleMoment(4, distData);
        
        // The MOM estimates of Nu and Sigma are obtained using 2nd and 4th sample moments
        // as in: http://en.wikipedia.org/wiki/Rice_distribution
        double _nu = Math.pow(2*mu2*mu2 - mu4, 0.25);
        double _sigma = Math.pow((mu2-_nu*_nu)/2, 0.5);
        setParameters(_nu, _sigma);
    }

    /** This method computes the cumulative distribution function.
	Difficult in closed simple form: 
	http://en.wikipedia.org/wiki/Rice_distribution
    public double getCDF(double x) {
     }
    */

    /**
     * This method returns the NU value.
     * @uml.property name="nu"
     */
    public double getNu() {
        return this.nu;
    }

    /**
     * This method returns the SIGMA value.
     * @uml.property name="sigma"
     */
    public double getSigma() {
        return this.sigma;
    }
    
    /** This method simulates a value from the Rice distribution. 
     * Using Mathlab template code:
     * http://www.mathworks.com/matlabcentral/fileexchange/loadFile.do?objectId=14237&objectType=FILE 
     */
    public double simulate() {
        double x = sigma*Math.random() + nu;
		double y = sigma*Math.random();
		return Math.sqrt(x*x + y*y);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Rice_distribution");
    }

}

