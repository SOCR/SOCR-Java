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

/** The binomial distribution with a random number of trials */
public class BinomialRandomNDistribution extends Distribution {
    //Variables
    double probability, sum;
    Distribution dist;

    /**
     * This general constructor creates a new randomized binomial distribution
     * with a specified probability of success and a specified distribution for
     * the number of trials
     */
    public BinomialRandomNDistribution(Distribution d, double p) {
        setParameters(d, p);
        name="Binomial Random Distribution";
    }

    /**
     * Special constructor: creates a new randomized binomial distribution with
     * a specified probability of success and the uniform distribution on {1, 2,
     * 3, 4, 5, 6} for the number of trials
     */
    public BinomialRandomNDistribution(double p) {
        this(new DiscreteUniformDistribution(1, 6, 1), p);
        name="Binomial Random Distribution";
    }

    /**
     * This default constructor: creates a new randomized binomial distribution
     * with probability of success 0.5 and the uniform distribution on {1, 2, 3,
     * 4, 5, 6} for the number of trials
     */
    public BinomialRandomNDistribution() {
        this(new DiscreteUniformDistribution(1, 6, 1), 0.5);
    }

    /**
     * Set the parameters: the distribution for the number of trials and the
     * probability of success
     */
    public void setParameters(Distribution d, double p) {
        dist = d;
        probability = p;
        super.setParameters(0, dist.getDomain().getUpperValue(), 1, DISCRETE);
    }

    //Density
    public double getDensity(double x) {
        int k = (int) Math.rint(x);
        double trials;
        if (probability == 0) {
            if (k == 0) return 1;
            else return 0;
        } else if (probability == 1) return dist.getDensity(k);
        else {
            sum = 0;
            for (int i = 0; i < dist.getDomain().getSize(); i++) {
                trials = dist.getDomain().getValue(i);
                sum = sum + dist.getDensity(trials) * comb(trials, k)
                        * Math.pow(probability, k)
                        * Math.pow(1 - probability, trials - k);
            }
            return sum;
        }
    }

    public double getMean() {
        return dist.getMean() * probability;
    }

    public double getVariance() {
        return dist.getMean() * probability * (1 - probability)
                + dist.getVariance() * probability * probability;
    }

    public double simulate() {
        int trials = (int) dist.simulate();
        int successes = 0;
        for (int i = 1; i <= trials; i++) {
            if (Math.random() < probability) successes++;
        }
        return successes;
    }

}

