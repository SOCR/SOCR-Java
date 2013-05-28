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
 * http://mathworld.wolfram.com/BetaBinomialDistribution.html">
 * http://mathworld.wolfram.com/BetaBinomialDistribution.html </a>.
 */
public class BetaBinomialDistribution extends Distribution {

    /**
     * @uml.property name="trials"
     */
    private int trials;

    /**
     * @uml.property name="alpha"
     */
    private double alpha;

    /**
     * @uml.property name="beta"
     */
    private double beta;

    /**
     * Default constructor: creates the Beta-Binomial distribution with 10 trials, a=1, b=2
     */
    public BetaBinomialDistribution() {
    	this(10, 1.0, 2.0);
    }

    /**
     * General constructor: creates the beta-binomial distribution with specified
     * parameters
     */
    public BetaBinomialDistribution(int n, double a, double b) {
        name = "Beta-Binomial Distribution";
        setParameters(n, a, b);
    }

    public void initialize() {
        createValueSetter("No. of Trials", 0, 0, 60, 10);
        createValueSetter("Alpha", 1, 0, 20, 1);
        createValueSetter("Beta", 1, 0, 20, 2);
        setParameters(10, 1.0, 2.0);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        double v2 = getValueSetter(1).getValue();
        double v3 = getValueSetter(2).getValue();
        setParameters(v1, v2, v3);
    }

    /** Set the parameters */
    public void setParameters(int n, double a, double b) {
        //Correct invalid parameters
        if (n < 1) n = 1;
        if (a <= 0) a = 1;
        if (b <= 0) b = 1;
        // if (a>=b) b=a+1;
        trials = n;
        alpha = a;
        beta=b;
        
		super.setParameters(0, trials, 1, DISCRETE);
		name = "Beta-Binomial("+trials+", "+alpha+", " +beta+")!";
    }

    /** Set the number of trails */
    public void setTrials(int n) {
        setParameters(n, alpha, beta);
    }

    /**
     * Get the number of trials
     * @uml.property name="trials"
     */
    public int getTrials() {
        return trials;
    }

    /** Set alpha parameter */
    public void setAlpha(double a) {
        setParameters(trials, a, beta);
    }

    /**
     * Get alpha parameter
     * @uml.property name="alpha"
     */
    public double getAlpha() {
        return alpha;
    }

    /** Set beta parameter */
    public void setBeta(double b) {
        setParameters(trials, alpha, b);
    }

    /**
     * Get beta parameter
     * @uml.property name="beta"
     */
    public double getBeta() {
        return beta;
    }

    /** Define the Beta-Binomial getDensity function */
    public double getDensity(double x) {
		int k = (int) Math.rint(x);
		
        if (k < 0 | k > trials) return 0;
        return (gamma(k+alpha)*gamma(trials-k+beta)*gamma(alpha+beta)*gamma(trials+2))/
        	((trials+1)*gamma(alpha+beta+trials)*gamma(alpha)*gamma(beta)*gamma(k+1)*gamma(trials-k+1));
    }

    /** Specify the maximum getDensity --
    public double getMaxDensity() {
        return getDensity(getMode());
    }
    ******/

    /** Give the mean in closed form */
    public double getMean() {
        return trials*alpha/(alpha+beta);
    }

    /** Specify the variance in close form */
    public double getVariance() {
        return trials*alpha*(trials*(1+alpha)+beta)/((alpha+beta)*(1+alpha+beta)) - getMean()*getMean();
    }

    /** Specify the SD in close form */
    public double getSD() {
        return Math.sqrt(getVariance());
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://mathworld.wolfram.com/BetaBinomialDistribution.html");
    }
}

