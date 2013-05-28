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
 * A Java implementation of the Poisson distribution with specified Shift and
 * Mean parameters <a href="http://en.wikipedia.org/wiki/Poisson_distribution">
 * http://en.wikipedia.org/wiki/Poisson_distribution</a>.
 */

public class PoissonDistribution extends Distribution {
    /**
     * @uml.property name="Shift"
     */
    //Parameters
    private int shift=0;
    /**
     * @uml.property name="Lambda"
     */
    //Parameters
    private double lambda=1;

    private double errorRate = 0.000001;

    /**
     * Default constructor: creates a Poisson distribution with shift and lambda
     * parameters equal to 0 & 1
     */

    public PoissonDistribution() {
        name = "Poisson Distribution";
    }

    public PoissonDistribution(int s, double l) {
        if (l>0) setParameters(s, l);
        else  setParameters(s, 1.0);
    }

    public PoissonDistribution(double l) {
        if (l>0) setParameters(shift, l);
        else  setParameters(shift, 1.0);
    }

    public PoissonDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public PoissonDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public void initialize() {
        createValueSetter("Shift", DISCRETE, -20, 20, 0);
        createValueSetter("Lambda", CONTINUOUS, 0, 100, 5);
        setParameters(shift, lambda);
    }

    public void valueChanged() {
        setParameters((int)getValueSetter(0).getValue(), 
        		getValueSetter(1).getValue());
        // if (lambda<=100) Poisson(lambda)
        // else if (lambda>100) use NormalDistribution as approximation
        if (getValueSetter(1).getValue()<=100) 
        	name = "Poisson Distribution (Lambda="+lambda + ")";
        else name = "Normal ("+getMode()+", "+Math.sqrt(getMode())+") "+
        	"approx to Poisson Distribution("+lambda+")";
    }

    /**
     * Set the LAMBDA parameter
     */
    public void setParameter(double l) {
    	if (l>0) setParameters(shift, l);
    	else setParameters(shift, 1.0);
    }

    /**
     * Set the shift & lambda parameters
     */
    public void setParameters(int s, double l) {
        double lower, upper;
        //Correct parameters that are out of bounds
        if (l <= 0) l = 1;
        //Assign parameters
        shift = s;
        lambda = l;
        //Specifiy the interval and partiton
        double corr = 3;
        upper = inverseCDF(0.999999); // (int)(getMean() + corr*getSD());
        lower = inverseCDF(0.000001); 
        super.setParameters((int)(lower), (int)upper, 1, DISCRETE);
    	super.setMGFParameters();
    	super.setPGFParameters();
    }

    /** Sets the Location parameter */
    public void setLocation(int s) {
        setParameters(s, lambda);
    }

    /**
     * Sets the lambda
     * @uml.property name="lambda"
     */
    public void setLambda(double r) {
    	if (r>0) this.lambda = r;
    	else this.lambda = 1;
    }

    /**
     * Get the Location-Shift paramter
     * @uml.property name="shift"
     */
    public int getLocation() {
        return shift;
    }

    /**
     * Get the lambda paramter
     * @uml.property name="lambda"
     */
    public double getLambda() {
        return this.lambda;
    }

    public int getShift() {
        return shift;
    }

    /** Define the Poisson getDensity function */
    public double getDensity(double x) {
        //if (Math.abs(x-(int)(x))> errorRate) x = (int)(x+0.5);
    	x = (int) Math.rint(x);
        if(x<shift) return 0;
        else if (x==shift) return Math.exp(-lambda);
        else if (lambda<=100) return Math.pow(lambda, 
        		(int)(x-shift))*Math.exp(-lambda)/factorial((int)(x-shift));
        else {  	
        	NormalDistribution nd = new NormalDistribution(getMode(), Math.sqrt(getMode()));
			return nd.getDensity(x);
        }
    }

    /** Compute the maximum getDensity */
    public double getMaxDensity() {
        return getDensity(getMode());
    }

    /** Compute the mean in closed form */
    public double getMean() {
        return lambda+shift;
    }

    /** Compute the Mode in closed form */
    public double getMode() {
        //return (double)((int)(lambda + shift));
    	return ((int) Math.rint(lambda) + shift);
    }

    /** overwrites the method in distribution for estimating parameters
	* By assuming that the shape parameter is known, the location and scale parameters could
	* be easily obtained by using the maximum likelihood estimation method. The estimate of
	* the shape parameter p is an open problem, so far. See this paper for an idea of how to implement a
 	* numerical scheme for estimation of the Shape parameter:
	* http://www.jstatsoft.org/v12/i04/v12i04.pdf#search=%22%22exponential%20power%20distribution%22%20estimate%20%22shape%22%22
	*/
    /** Parameter estimation function from raw data
     * 	For some reason this always OVERESTIMATES the SHIFT parameter.
     *  We need a better analytical strategy for estimation of parameters here!
     */
	public void paramEstimate(double[] distData) {
		double sMean = sampleMean(distData);
		this.shift = (int)(sMean - Math.floor(minimum(distData)));
				
		double m=0, v=0;
		for (int i = 0; i< distData.length; i++) {
			m += distData[i];
			v += distData[i]*distData[i];
		}
		m /= distData.length;
		v = v/distData.length - m*m;
		
		// MOM estimate for the shift and Poisson-lambda parameters
		//setLambda(m);
		//setShift((int)(m-v));
		setParameters((int)(m-v), v);
		
		//System.err.println("Lambda==="+v+"\tMean==="+m+"\tSHIFT==="+(int)(m-v));
		
		//setLambda(sMean-this.shift);
		//setShift(this.shift);
	}

	public void setShift(int s) {
		this.shift = s;
		double upperBound, lowerBound;
	  	upperBound = inverseCDF(0.999999); // (int)(getMean() + corr*getSD());
	  	lowerBound = inverseCDF(0.000001); 
		super.setParameters(lowerBound, upperBound, 0.01 * (upperBound), DISCRETE);
	}

	/** The method finds the minimum of a double array
	 * directly copied from ExponentialDistribution.java. annie che 20060714 
	 */
	public double minimum(double[] distData) {
		double min=0;
		if (distData.length > 0) min = distData[0];
		for (int i = 1; i < distData.length; i++)
			if (min > distData[i]) min = distData[i];
		return(min);
	}

    /** Compute the variance in closed form */
    public double getVariance() {
        return this.lambda;
    }

    /** Compute the variance in closed form */
    public double getSD() {
        if (lambda>=0) return Math.sqrt(this.lambda);
        else return 0.0;
    }

    /**
     * Compute the cumulative distribution function.     */
    public double getCDF(double x) {
      double cdf = 0;
      if (x>=shift)
      {	for (int i=shift; i<=x; i++)
			cdf+=getDensity(i);

		return cdf;
      } else return 0;
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t)
    {
    	return Math.exp(lambda*(Math.exp(t)-1));
    	
    }
    
    /** Computes the probability generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getPGF(double t)
    {
    	return Math.exp(lambda*(t-1));
    }

    /**
     * Inverse of the cumulative Poisson distribution function.
     * @return the value X for which P(x&lt;X).
     */
     public double inverseCDF(double probability) {
		if (0<=probability && probability <=1)
               return Math.floor(findRoot(probability, getMode(),(double)shift,Double.MAX_VALUE));
	 	else if (probability <0) return 0;
		else return 1.0;  // (1 < probability)
     }

    /** Simulate a value */
    public double simulate() {
    	int arrivals = 0;
        double sum = -Math.log(1 - Math.random());
        while (sum <= lambda) {
            arrivals++;
            sum = sum - Math.log(1 - Math.random());
        }
        return this.shift + arrivals;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/PoissonDistribution.html");
    }
}

