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
 * This class models the distribution of the number of distinct sample values
 * when a sample of a specified size is chosen with replacement from a finite
 * population of a specified size.
 */
public class BirthdayDistribution extends Distribution {
    private int popSize;
    private int sampleSize;
    private double[][] prob;

    /**
     * This default constructor creates a new birthday distribution with
     * population size 365 and sample size 20
     */
    public BirthdayDistribution() {
        name = "Birthday Distribution";
    }

    public BirthdayDistribution(int n, int k) {
        setParameters(n, k);
    }

    public void initialize() {
        createValueSetter("Population-Size", DISCRETE, 0, 365, 365);
        createValueSetter("Sample-Size", DISCRETE, 0, 365,20);
        setParameters(365, 20);
    }

    public void valueChanged() {
        int v1 = getValueSetter(0).getValueAsInt();
        int v2 = getValueSetter(1).getValueAsInt();
        setParameters(v1, v2);
    }

    /**
     * This method sets the parameters: the population size and the sample size.
     * Also, the probabilities are computed and stored in an array
     */
    public void setParameters(int n, int k) {
        //Correct for invalid parameters
        if (n < 1) n = 1;
        if (k < 1) k = 1;
        int upperIndex;
        popSize = n;
        sampleSize = k;
        super.setParameters(1, Math.min(popSize, sampleSize), 1, DISCRETE);
        prob = new double[sampleSize + 1][popSize + 1];
        prob[0][0] = 1;
        prob[1][1] = 1;
        for (int j = 1; j < sampleSize; j++) {
            if (j < popSize) upperIndex = j + 1;
            else upperIndex = (int) popSize;
            for (int m = 1; m <= upperIndex; m++) {
                prob[j + 1][m] = prob[j][m] * ((double) m / popSize)
                        + prob[j][m - 1] * ((double) (popSize - m + 1) / popSize);
            }
        }
    }

    /** This method computes the getDensity function */
    public double getDensity(double x) {
        int m = (int) (Math.rint(x));
        return prob[sampleSize][m];
    }

    /** This method computes the mean */
    public double getMean() {
        return popSize * (1 - Math.pow(1 - 1.0 / popSize, sampleSize));
    }

    /** This method computes the variance */
    public double getVariance() {
        return popSize * (popSize - 1) * Math.pow(1 - 2.0 / popSize, sampleSize)
                + popSize * Math.pow(1 - 1.0 / popSize, sampleSize) - popSize
                * popSize * Math.pow(1 - 1.0 / popSize, 2 * sampleSize);
    }

    /** This method returns the population size */
    public double getPopSize() {
        return popSize;
    }

    /** This method sets the population size */
    public void setPopSize(int n) {
        setParameters(n, sampleSize);
    }

    /** This method returns the sample size */
    public double getSampleSize() {
        return sampleSize;
    }

    /** This method sets the sample size */
    public void setSampleSize(int k) {
        setParameters(popSize, k);
    }

    /**
     * This method simulates a value from the distribution, as the number of
     * distinct sample values
     */
    public double simulate() {
        int[] count = new int[popSize];
        double distinct = 0;
        for (int i = 1; i <= sampleSize; i++) {
            int j = (int) (popSize * Math.random());
            if (count[j] == 0) distinct++;
            count[j] = count[j]++;
        }
        return distinct;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Birthday_Paradox");
    }

}

