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
 * The distribution of the order statistic of a specified order from a random
 * sample of a specified size from a specified sampling distribution
 */
public class OrderStatisticDistribution extends Distribution {
    Distribution dist;
    int sampleSize, order;

    /**
     * General constructor: creates a new order statistic distribution
     * corresponding to a specified sampling distribution, sample size, and
     * order
     */
    public OrderStatisticDistribution(Distribution d, int n, int k) {
        setParameters(d, n, k);
    }

    public OrderStatisticDistribution() {}

    /** Set the parameters: the sampling distribution, sample size, and order */
    public void setParameters(Distribution d, int n, int k) {
        //Correct for invalid parameters
        if (n < 1) n = 1;
        if (k < 1) k = 1;
        else if (k > n) k = n;
        //Assign parameters
        dist = d;
        sampleSize = n;
        order = k;
        int t = dist.getType();
        Domain domain = dist.getDomain();
        if (t == DISCRETE) super.setParameters(domain.getLowerValue(), domain
                .getUpperValue(), domain.getWidth(), t);
        else super.setParameters(domain.getLowerBound(), domain.getUpperBound(),
                domain.getWidth(), t);
    }

    /** Density function */
    public double getDensity(double x) {
        double p = dist.getCDF(x);
        if (dist.getType() == DISCRETE) return getCDF(x)
                - getCDF(x - getDomain().getWidth());
        else return order * comb(sampleSize, order) * Math.pow(p, order - 1)
                * Math.pow(1 - p, sampleSize - order) * dist.getDensity(x);
    }

    /** Cumulative distribution function */
    public double getCDF(double x) {
        double sum = 0;
        double p = dist.getCDF(x);
        for (int j = order; j <= sampleSize; j++)
            sum = sum + comb(sampleSize, j) * Math.pow(p, j)
                    * Math.pow(1 - p, sampleSize - j);
        return sum;
    }
}

