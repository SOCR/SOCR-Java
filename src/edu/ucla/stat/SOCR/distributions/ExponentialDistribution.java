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

import edu.ucla.stat.SOCR.distributions.exception.ParameterOutOfBoundsException;

/**
 * This class defines the (general) Exponential distribution with rate parameter
 * r and shift parameter s. <a href="http://mathworld.wolfram.com/ExponentialDistribution.html">
 * http://mathworld.wolfram.com/ExponentialDistribution.html </a>.
 */
public class ExponentialDistribution extends GammaDistribution {

    /**
     * @uml.property name="rate"
     * @uml.property name="shift"
     */
    //Parameter
    double rate=1;
    double shift=0;
    double lambda = 0;

    public ExponentialDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public ExponentialDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            rData[i] = (double) distData[i];

		paramEstimate(rData);
    }

    /**
     * This general constructor creates a new exponential distribution with a
     * specified rate
     */
    public ExponentialDistribution(double r) {
        setRate(r);
    }

    /**
     * This general constructor creates a new exponential distribution with a
     * specified rate and shift parameters
     */
    public ExponentialDistribution(double r, double s) {
    	setRate(r);
    	setShift(s);
    }

    /**
     * This default constructor creates a new exponential distribution with rate
     * 1 and shift 0.
     */
    public ExponentialDistribution() {
		this(1, 0);
		name = "Exponential Distribution";
    }

    public void initialize() {
		//System.out.println("ExponentialDistribution initialize rate = " + rate);
		createValueSetter("Lambda", CONTINUOUS, 0, 50, 1);
		createValueSetter("Shift", CONTINUOUS, -50, 50, 0);
    }

    public void valueChanged() {
		double v1 = getValueSetter(0).getValue();
		setRate(v1);
		double v2 = getValueSetter(1).getValue();
		setShift(v2);
    }

    /**
     * This method sets the rate parameter
     *
     * @uml.property name="rate"
     */
    public void setRate(double r) {
		if (r <= 0) r = 1;
		this.rate = r;
		//super.setParameters(1, 1 / rate);
		double upperBound = 1.0/rate + 5.0/rate;   // getMean() + 4*getSD();
		super.setParameters(shift, upperBound + shift, 0.01 * (upperBound), CONTINUOUS);
		super.setMGFParameters(0, this.rate);
    }

   /**
     * This method sets the Shift parameter
     * @uml.property name="shift"
     */
    public void setShift(double s) {
		shift = s;
		//super.setDomain(new Domain(s, s+5, 0.1, 1));
		double upperBound = 1.0/rate + 5.0/rate;   // getMean() + 4*getSD();
		super.setParameters(shift, upperBound + shift, 0.01 * (upperBound), CONTINUOUS);
		//System.out.println("ExponentialDistribution setShift shift = " + shift);
	}

    /**
     * This method gets the rate
     * @uml.property name="rate"
     */
    public double getRate() {
        return rate;
    }

    /**
     * This method gets the shift
     * @uml.property name="shift"
     */
    public double getShift() {
        return shift;
    }

    /** This method defines the getDensity function */
    public double getDensity(double x) {
        if (x < shift) return 0;
        else return rate * Math.exp(-rate * (x-shift));
    }

    /** This method defines the getMaxDensity function */
    public double getMaxDensity() {
        return rate;
    }

    /** This method defines the cumulative distribution function */
    public double getCDF(double x) {
        if (x>=shift) return 1 - Math.exp(-rate * (x-shift));
	else return 0.0;
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) throws ParameterOutOfBoundsException
    { 	if (rate == t)
    		throw new ParameterOutOfBoundsException("Rate/Lambda minus t, can not be less than or equal to zero");
    	else
    		return (rate/(rate-t));
    }

    /** The method defines the getQuantile function */
    public double getQuantile(double p) {
        return -Math.log(1 - p) / rate;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://mathworld.wolfram.com/ExponentialDistribution.html");
    }

     /** The method finds the minimum of a double array */
    public double minimum(double[] distData) {
        double min=0;
        if (distData.length > 0) min = distData[0];
        for (int i = 1; i < distData.length; i++)
            if (min > distData[i]) min = distData[i];
        return(min);
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return 1/getRate();
    }

    /** Compute the mean in closed form */
    public double getMode() {
        return getShift();
    }

    /** Compute the variance in closed form */
    public double getVariance() {
        return 1/(getRate()*getRate());
    }


    /** Compute the variance in closed form */
    public double getSD() {
        return 1/getRate();
    }

    /** This method estimates the parameters of this distribution. */
   public void paramEstimate(double[] distData) {
	     this.lambda = sampleMean(distData);
		rate = 1 / this.lambda;
		setRate(rate);
		shift = minimum(distData);
		setShift(shift);
		this.rate = 1 / this.lambda;
   }
   
   /** This method simulates an Exponential variable 
    */
   public double simulate() {
        /*
         * If rate parameter k is an integer, simulate as the k'th arrival time
         * in a Poisson proccess
         
        if (rate == Math.rint(rate)) {
            double sum = 0;
            for (int i = 1; i <= rate; i++) {
                sum = sum - super.getScale() * Math.log(1 - Math.random());
            }
            return shift + sum;
        } else return super.simulate();
        **/
	   ContinuousUniformDistribution CUD = new ContinuousUniformDistribution(0,1);
	   return (shift -(1.0/getRate())*Math.log(1-CUD.simulate())); 
    }
}

