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
 * This class models the distribution of the k'th order statistic for a sample
 * of size n chosen without replacement from {1, 2, ..., N} .
 */
public class FiniteOrderStatisticDistribution extends Distribution {
    Distribution dist;
    private int sampleSize, populationSize, order;

    /**
     * This general constructor creates a new finite order statistic
     * distribution with specified population and sample sizes, and specified
     * order.
     */
    public FiniteOrderStatisticDistribution(int N, int n, int k) {
        setParameters(N, n, k);
        name ="Finite Distribution";
    }

    /**
     * This default constructor creates a new finite order statistic
     * distribution with population size 50, sample size 10, and order 5.
     */
    public FiniteOrderStatisticDistribution() {
        this(50, 10, 5);
    }

    /**
     * This method sets the parameters: the sample size, population size, and
     * order.
     */
    public void setParameters(int N, int n, int k) {
        populationSize = N;
        sampleSize = n;
        order = k;
        super.setParameters(order, populationSize - sampleSize + order, 1,
                Distribution.DISCRETE);
    }

    /** This method computes the getDensity. */
    public double getDensity(double x) {
        int i = (int) Math.rint(x);
        return comb(i - 1, order - 1)
                * comb(populationSize - i, sampleSize - order)
                / comb(populationSize, sampleSize);
    }

    /** This method computes the mean. */
    public double getMean() {
        return (double) order * (populationSize + 1) / (sampleSize + 1);
    }

    /** This method computes the variance. */
    public double getVariance() {
        return (double) (populationSize + 1) * (populationSize - sampleSize)
                * order * (sampleSize + 1 - order)
                / ((sampleSize + 1) * (sampleSize + 1) * (sampleSize + 2));
    }

    /** This method sets the population size. */
    public void setPopulationSize(int N) {
        setParameters(N, sampleSize, order);
    }

    /** This method returns the population size. */
    public int getPopulationSize() {
        return populationSize;
    }

    /** This method sets the sample size. */
    public void setSampleSize(int n) {
        setParameters(populationSize, n, order);
    }

    /** This method returns the sampleSize. */
    public int getSampleSize() {
        return sampleSize;
    }

    /** This method sets the order. */
    public void setOrder(int k) {
        setParameters(populationSize, sampleSize, k);
    }

    /** This method returns the order. */
    public int getOrder() {
        return order;
    }
}

