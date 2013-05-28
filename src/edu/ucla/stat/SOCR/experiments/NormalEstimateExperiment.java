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
package edu.ucla.stat.SOCR.experiments;

import java.util.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;

/**
 * This class models the point estimation problem associated with the normal
 * distribution.
 */
public class NormalEstimateExperiment extends EstimateExperiment {
    //Variables
    private double mu = 0, sigma = 1, sigma2 = 1, x, sum;
    private double m, mBias, mMSE, s2, s2Bias, s2MSE, t2, t2Bias, t2MSE, w2,
            w2Bias, w2MSE;
    private NormalDistribution dist = new NormalDistribution(mu, sigma);
    private RandomVariable rv = new RandomVariable(dist);

    /**
     * This method intializes the experiment, including the toolbar and
     * scrollbars.
     */
    public NormalEstimateExperiment() {
        setName("Normal Estimate Experiment");
    }

    public void initialize() {
        createValueSetter("mu", Distribution.CONTINUOUS, -5, 5, 0);
        createValueSetter("d", 5, 50, 0.1);
        initializePane();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        super.doExperiment();
        resetSample();
        sum = 0;
        int n = getSampleSize();
        for (int i = 0; i < n; i++) {
            x = rv.simulate();
            sum = sum + (x - mu) * (x - mu);
        }
        m = rv.getIntervalData().getMean();
        s2 = rv.getIntervalData().getVariance();
        t2 = rv.getIntervalData().getVarianceP();
        w2 = sum / n;
        int runs = getTime();
        mBias = ((runs - 1) * mBias + (m - mu)) / runs;
        mMSE = ((runs - 1) * mMSE + (m - mu) * (m - mu)) / runs;
        s2Bias = ((runs - 1) * s2Bias + (s2 - sigma2)) / runs;
        s2MSE = ((runs - 1) * s2MSE + (s2 - sigma2) * (s2 - sigma2)) / runs;
        t2Bias = ((runs - 1) * t2Bias + (t2 - sigma2)) / runs;
        t2MSE = ((runs - 1) * s2MSE + (t2 - sigma2) * (t2 - sigma2)) / runs;
        w2Bias = ((runs - 1) * w2Bias + (w2 - sigma2)) / runs;
        w2MSE = ((runs - 1) * w2MSE + (w2 - sigma2) * (w2 - sigma2)) / runs;
    }

    /**
     * This method updates the experiment, including the random variable graph,
     * record table and statistics table.
     */
    public void update() {
        super.update();
        getRecordTable().append(
                "\t" + format(m) + "\t" + format(s2) + "\t" + format(t2) + "\t"
                        + format(w2));
        setStatistics("Est\tBias\tMSE" + "\nM\t" + format(mBias) + "\t"
                + format(mMSE) + "\nS2\t" + format(s2Bias) + "\t" + format(s2MSE)
                + "\nT2\t" + format(t2Bias) + "\t" + format(t2MSE) + "\nW2\t"
                + format(w2Bias) + "\t" + format(w2MSE));
    }

    /**
     * This method resets the experiment, including the record table, statistics
     * table, and random variable graph.
     */
    public void reset() {
        super.reset();
        getRecordTable().append("Run\tM\tS^2\tT^2\tW^2");
        setStatistics("Est\tBias\tMSE\n");
    }

    /**
     * This method handles the scroll events associated with changes in the
     * parameters of the normal distribution.
     */
    public void update(Observable o, Object arg) {
        mu = getValueSetter(0).getValue();
        sigma = getValueSetter(1).getValue();
        sigma2 = sigma * sigma;
        setDistribution();
    }

    public void setDistribution() {
        dist.setParameters(mu, sigma);
        reset();
    }
}

