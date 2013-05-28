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
 * The binomial distribution with specified parameters: the number of trials (n)
 * and the probability of success (p) <a
 * href="http://mathworld.wolfram.com/BinomialDistribution.html">
 * http://mathworld.wolfram.com/BinomialDistribution.html </a>.
 */
public class BinomialDistribution extends Distribution {

    /**
     * @uml.property name="trials"
     */
    //Variables
    private int trials, approxNormalBinomial=110;
    NormalDistribution ND = new NormalDistribution(0, 1);

    /**
     * @uml.property name="probability"
     */
    private double probability;

    /**
     * Default constructor: creates the binomial distribution with 10 trials and
     * probability of success 1/2
     */
    public BinomialDistribution() {
        name = "Binomial Distribution";
    }

    /**
     * General constructor: creates the binomial distribution with specified
     * parameters
     */
    public BinomialDistribution(int n, double p) {
        setParameters(n, p);
    }

    public BinomialDistribution(double[] distData) {
        paramEstimate(distData);

    }

    public BinomialDistribution(float[] distData) {
        double[] distDat = new double[distData.length];
        for (int i = 0; i < distData.length; i++)
            distDat[i] = (double) distData[i];
        paramEstimate(distDat);
    }

    public void initialize() {
        createValueSetter("No. of Trials", 0, 0, 60, 10);
        createValueSetter("Success Probability", 1, 0, 1, 0);
        setParameters(10, 0.5);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** Set the parameters */
    public void setParameters(int n, double p) {
        //Correct invalid parameters
        if (n < 1) n = 1;
        if (p < 0) p = 0;
        else if (p > 1) p = 1;
        trials = n;
        probability = p;
        //double upperBound = inverseCDF(0.999999); // (int)(getMean() + corr*getSD());
        //double lowerBound = inverseCDF(0.000001); 
        //super.setParameters(lowerBound, upperBound, 0.01*(upperBound-lowerBound), DISCRETE);
        //super.setParameters(0, trials, 1, DISCRETE);
		super.setMGFParameters();
		super.setPGFParameters();

	  if (n <approxNormalBinomial) { 
		  super.setParameters(0, trials, 1, DISCRETE);
		  name = "(exact) Binomial("+trials+", "+probability+")!";
	  }
	  else {
		  double sampleSD = Math.sqrt(trials*p*(1-p));
		  name = "(approximate) Normal ("+ n*p + ", "+ sampleSD+") ~~ Binomial("+trials+", "+probability+")!";
		  
		  	// Set the parameters of the Normal-distribution approximation
		  ND = new NormalDistribution(getMean(), getSD());
		   
		  double upperBound = inverseCDF(0.99999); // (int)(getMean() + corr*getSD());
		  double lowerBound = inverseCDF(0.00001); 
	  		//super.setParameters(lowerBound, upperBound, 1, DISCRETE);
		  //super.setParameters(n*p-4*sampleSD, n*p+4*sampleSD, 1, DISCRETE);
		  super.setParameters((int)(n*p-4*sampleSD+0.5), (int)(n*p+4*sampleSD+0.5), 1, DISCRETE);
		  	//super.setParameters(getMean()-4*Math.sqrt(getVariance()), 
			//getMean()+4*Math.sqrt(getVariance()), 1, DISCRETE);
	  }
    }

    /** Set the number of trails */
    public void setTrials(int n) {
        setParameters(n, probability);
    }

    /**
     * Get the number of trials
     * 
     * @uml.property name="trials"
     */
    public int getTrials() {
        return trials;
    }

    /** Set the probability of success */
    public void setProbability(double p) {
        setParameters(trials, p);
    }

    /**
     * Get the probability of success
     * 
     * @uml.property name="probability"
     */
    public double getProbability() {
        return probability;
    }

    /** Define the binomial getDensity function */
    public double getDensity(double x) {
        if (trials <approxNormalBinomial) {
        	int k = (int) Math.rint(x);
        	if (k < 0 | k > trials) return 0;
        	if (probability == 0) {
        	    if (k == 0) return 1;
        	    else return 0;
        	} else if (probability == 1) {
        	    if (k == trials) return 1;
        	    else return 0;
        	} else return comb(trials, k) * Math.pow(probability, k)
                * Math.pow(1 - probability, trials - k);
	   }
	   else {	//Normal Approximation
		   if (x>=0 && x<=trials) {
			   //System.err.println("PDF: Math.rint(x)="+Math.rint(x));
			   return ND.getDensity((int)Math.rint(x));
		   } else return 0;
	  }
    }

    /** Specify the maximum getDensity */
    public double getMaxDensity() {
        return getDensity(getMode());
    }

    /** Give the mean in closed form */
    public double getMean() {
        return trials * probability;
    }

    /** Give the Mode in closed form */
    public double getMode() {
        return Math.min(Math.floor((trials + 1) * probability), trials);
    }

    /** Specify the variance in close form */
    public double getVariance() {
        return trials * probability * (1 - probability);
    }

    /** Specify the SD in close form */
    public double getSD() {
        return Math.sqrt(trials * probability * (1 - probability));
    }

	/** Computes the moment generating function in closed form for a
	 * parameter t which lies in the domain of the distribution. 
	 */
	public double getMGF(double t) 
	{
		return Math.pow((1-probability)+probability*Math.exp(t),trials);
	}

	/** Computes the probability generating function in closed form for a
	 * parameter t which lies in the domain of the distribution. 
	 */
	public double getPGF(double t) 
	{
		return Math.pow((1-probability)+probability*t, trials);
	}

    /** Specify the CDF in terms of the beta CDF */
    public double getCDF(double x) {
        if (x < 0) return 0;
        else if (x >= trials) return 1;
        else if (trials<approxNormalBinomial) return 1 - betaCDF(probability, x + 1, trials - x);
	    else {	//Normal Approximation
	    	//System.err.println("CDF: Math.rint(x)="+Math.rint(x));
	    	return ND.getCDF(x); 
	    }
	  // May need to do POisson Approximation for small probability and large n=trials.
    }

    /**
     * Inverse of the cumulative Poisson distribution function.
     * @return the value X for which P(x&lt;X).
     */
     public double inverseCDF(double probability) {
		if (0<=probability && probability <=1)
               return Math.floor(findRoot(probability, getMean(),0.0,trials));
	 	else if (probability <0) return 0;
		else return 1.0;  // (1 < probability)
     }

    /** Simulate the binomial distribution as the number of successes in n trials */
    public double simulate() {
        int successes = 0;
        for (int i = 1; i <= trials; i++) {
            if (Math.random() < probability) successes++;
        }
        return successes;
    }

    /** overwrites the method in Distribution for estimating parameters 
     * @param distData array of raw data
     */
    public void paramEstimate(double[] distData) {
        int n;
        double p;
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);

        if (distData.length < 2) {
            setParameters(1, 0.5);
            return;
        }
        /***********************************************************************
         * http://en.wikipedia.org/wiki/Binomial_distribution
         **********************************************************************/
        //p=1-sVar/sMean;
        //n=(int)(0.5+sMean/p);
        n=(int)(0.5+sMean*sMean/(sMean-sVar));
        p=sMean/n;
        System.err.println("N="+n+"\tp="+p+"\n");
        setParameters(n, p);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/BinomialDistribution.html");
    }

}

