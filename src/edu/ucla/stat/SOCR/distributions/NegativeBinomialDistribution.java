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
import edu.ucla.stat.SOCR.distributions.exception.ParameterOutOfBoundsException;

/**
 * This class models the negative binomial distribution with specified successes
 * parameter and probability parameter. <a
 * href="http://mathworld.wolfram.com/NegativeBinomialDistribution.html">
 * http://mathworld.wolfram.com/NegativeBinomialDistribution.html </a>.
 */
public class NegativeBinomialDistribution extends Distribution {

    /**
     * @uml.property name="successes"
     */
    //Paramters
    private int successes;

    /**
     * @uml.property name="probability"
     */
    private double probability;

    /**
     * General Constructor: creates a new negative binomial distribution with
     * given parameter values.
     */
    public NegativeBinomialDistribution(int k, double p) {
        setParameters(k, p);
    }

    /**
     * Default Constructor: creates a new negative binomial distribution with
     * successes parameter 1 and probability parameter 0.5,
     */
    public NegativeBinomialDistribution() {
        this(1, 0.5);
        name = "Negative-Binomial Distribution";
    }

    public void initialize() {
        createValueSetter("No. of Successes", DISCRETE, 0, 60);
        createValueSetter("Success Probability", CONTINUOUS, 0, 1);
        getValueSetter(1).setValue(0.5);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** This method set the paramters and the set of values. */
    public void setParameters(int k, double p) {
        //Correct for invalid parameters
        if (k < 1) { 
        	k = 1;
        	//getValueSetter(0).setValue(1);
        }
        if (p <= 0.001) { 
        	if (probability < 0.001) probability = 0.001;
        	p = probability;
        	getValueSetter(1).setValue(p);
        }
        if (p > 1) { 
        	p = 1;
        	//getValueSetter(1).setValue(1);
        }
        //Assign parameters
        successes = k;
        probability = p;
        //Set truncated values
        super.setParameters(successes, Math.ceil(getMean() + 4 * getSD()), 1, DISCRETE);
        super.setMGFParameters(0.0,Math.log(1.0/(1.0-probability)),1000.0);
        super.setPGFParameters(0, Math.log(1/(1-probability)), 200, .05);
    }

    /** Set the successes parameters */
    public void setSuccesses(int k) {
        setParameters(k, probability);
    }

    /**
     * Get the successes parameter
     * 
     * @uml.property name="successes"
     */
    public int getSuccesses() {
        return successes;
    }

    /**
     * Get the probability parameter
     * 
     * @uml.property name="probability"
     */
    public double getProbability() {
        return probability;
    }

    /** Set the probability parameters */
    public void setProbability(double p) {
        setParameters(successes, p);
    }

    /** Density function */
    public double getDensity(double x) {
        int n = (int) Math.rint(x);
        if (n < successes) return 0;
        else return comb(n - 1, successes - 1) * Math.pow(probability, successes)
                * Math.pow(1 - probability, n - successes);
    }

    /** Maximum value of getDensity function */
    public double getMaxDensity() {
        double mode = (successes - 1) / probability + 1;
        return getDensity(mode);
    }

    /** Mean */
    public double getMean() {
        return successes / probability;
    }

    /** Variance */
    public double getVariance() {
        if (probability>0 && probability <=1)
        	return (successes * (1 - probability)) / (probability * probability);
        else return Double.MAX_VALUE;
    }

    /** Simulate a value */
    public double simulate() {
        int count = 0, trials = 0;
        while (count <= successes) {
            if (Math.random() < probability) count++;
            trials++;
        }
        return trials;
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) throws ParameterOutOfBoundsException
    {
    	if (t >= Math.log(1.0/(1-probability)))
    		throw new ParameterOutOfBoundsException("Parameter t must be less than log(1-p)");
    	else
    		return Math.pow(probability*Math.exp(t)/(1-(1-probability)*Math.exp(t)),successes);
    }
    
    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getPGF(double t)
    {
    	return Math.pow(probability/(1-(1-probability)*t), successes);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String(
                "http://mathworld.wolfram.com/NegativeBinomialDistribution.html");
    }

}

