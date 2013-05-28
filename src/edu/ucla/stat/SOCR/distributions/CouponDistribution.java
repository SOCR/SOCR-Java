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
 * This class models the distribution of the sample size needed to get a
 * specified number of distinct sample values when sampling with replacement
 * from a finite population of a specified size:
 * <a href="http://www.math.uah.edu/stat/urn/Coupon.xhtml">
 * http://www.math.uah.edu/stat/urn/Coupon.xhtml</a>
 */
public class CouponDistribution extends Distribution {
    int popSize, distinctValues, upperValue;
    double[][] prob;

    /**
     * This general constructor: creates a new coupon distribution with
     * specified population size and distinct sample size.
     */
    public CouponDistribution(int m, int k) {
        setParameters(m, k);
    }

    /**
     * This general constructor creates a new coupon distribution with
     * population size 10 and distinct sample size 10.
     */
    public CouponDistribution() {
        this(10, 10);
        name = "Coupon Distribution";
    }

    public void initialize() {
    	createValueSetter("Population size", DISCRETE, 1, 200, 10);
        createValueSetter("Desired number of distinct values", DISCRETE, 1, 200, 10);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        int v2 = getValueSetter(1).getValueAsInt();
        setParameters(v1, v2);
    }

    /**
     * This method sets the parameters: the population size (m) and number of
     * distinct values needed (k)
     */
    public void setParameters(int m, int k) {
        int upperIndex, maxIndex;
        //Correct for invalid parameters
        if (m < 1) m = 1;
        if (k < 1) k = 1;
        else if (k > m) k = m;
        popSize = m;
        distinctValues = k;
        upperValue = (int) Math.ceil(getMean() + 4 * getSD());
        super.setParameters(distinctValues, upperValue, 1, DISCRETE);
        prob = new double[upperValue + 1][popSize + 1];
        prob[0][0] = 1;
        prob[1][1] = 1;
        for (int i = 1; i < upperValue; i++) {
            if (i < popSize) upperIndex = i + 1;
            else upperIndex = popSize;
            for (int n = 1; n <= upperIndex; n++) {
                prob[i + 1][n] = prob[i][n] * ((double) n / popSize)
                        + prob[i][n - 1] * ((double) (popSize - n + 1) / popSize);
            }
        }
    }

    /** Density function */
    public double getDensity(double x) {
        int k = (int) (Math.rint(x));
        if (k < distinctValues | k > upperValue) return 0;
        else return ((double) (popSize - distinctValues + 1) / popSize)
                * prob[k - 1][distinctValues - 1];
    }

    /** Mean */
    public double getMean() {
        double sum = 0;
        for (int i = 1; i <= distinctValues; i++)
            sum = sum + (double) popSize / (popSize - i + 1);
        return sum;
    }

    /** Variance */
    public double getVariance() {
        double sum = 0;
        for (int i = 1; i <= distinctValues; i++)
            sum = sum + (double) (popSize * (i - 1))
                    / ((popSize - i + 1) * (popSize - i + 1));
        return sum;
    }

    /** Get the population size */
    public double getPopSize() {
        return popSize;
    }

    /** Set the population size */
    public void setPopSize(int m) {
        setParameters(m, distinctValues);
    }

    /** Get the number of distinct values */
    public double getDistinctValues() {
        return distinctValues;
    }

    /** Set the number of distinct values */
    public void setDistinctValues(int k) {
        setParameters(popSize, k);
    }

    /** Simulate a value from the distribution */
    public double simulate() {
        int[] cellCount = new int[(int) popSize];
        double occupiedCells = 0;
        int ballCount = 0;
        while (occupiedCells <= distinctValues) {
            ballCount++;
            int ballIndex = (int) (popSize * Math.random());
            if (cellCount[ballIndex] == 0) occupiedCells++;
            cellCount[ballIndex] = cellCount[ballIndex]++;
        }
        return ballCount;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return "http://www.math.uah.edu/stat/urn/Coupon.xhtml"; 
    }

}

