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

/**
 * This class models the distribution of the position at time n for a random
 * walk on the interval [0, n].
 */
package edu.ucla.stat.SOCR.distributions;

import edu.ucla.stat.SOCR.core.*;

public class WalkPositionDistribution extends Distribution {
    //Paramters
    private int steps;

    /**
     * @uml.property name="probability"
     */
    private double probability;

    /**
     * This general constructor creates a new distribution with specified time
     * and probability parameters.
     */
    public WalkPositionDistribution(int n, double p) {
        setParameters(n, p);
    }

    /**
     * This default constructor creates a new WalkPositionDistribution with time
     * parameter 10 and probability p.
     */
    public WalkPositionDistribution() {
        this(10, 0.5);
    }

    /** This method sets the time and probability parameters. */
    public void setParameters(int n, double p) {
        if (n < 0) n = 0;
        if (p < 0) p = 0;
        else if (p > 1) p = 1;
        steps = n;
        probability = p;
        super.setParameters(-steps, steps, 2, DISCRETE);
        name = "Walk Position ("+steps+","+probability+") Distribution";
    }

    /** This method computes the density function. */
    public double getDensity(double x) {
        int k = (int) Math.rint(x+0.5), m = (k + steps) / 2;
        return comb(steps, m) * Math.pow(probability, m)
                * Math.pow(1 - probability, steps - m);
    }

    /** This method returns the maximum value of the density function. */
    public double getMaxDensity() {
        double mode = 2 * Math.min(Math.floor((steps + 1) * probability), steps)
                - steps;
        return getDensity(mode);
    }

    /** This method computes the mean. */
    public double getMean() {
        return 2 * steps * probability - steps;
    }

    /** This method computes the variance. */
    public double getVariance() {
        return 4 * steps * probability * (1 - probability);
    }

    /** This method returns the number of steps. */
    public double getSteps() {
        return steps;
    }

    /**
     * This method returns the probability of a step to the right.
     * 
     * @uml.property name="probability"
     */
    public double getProbability() {
        return probability;
    }

    /** This method simulates a value from the distribution. */
    public double simulate() {
        int step, position = 0;
        for (int i = 1; i <= steps; i++) {
            if (Math.random() < probability) step = 1;
            else step = -1;
            position = position + step;
        }
        return position;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Random_walk");
    }

}

