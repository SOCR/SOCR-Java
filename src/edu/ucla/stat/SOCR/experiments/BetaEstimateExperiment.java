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

/** This class models the point estimation problem of the beta distribution. */
public class BetaEstimateExperiment extends EstimateExperiment {
    private double a = 1, u, v, uBias, uMSE, vBias, vMSE;
    private BetaDistribution dist = new BetaDistribution(a, 1);
    private RandomVariable rv = new RandomVariable(dist, "X");

    /**
     * This method initializes the experiment, including the addition of the
     * parameter label and scrollbar to the toolbar
     */
    public BetaEstimateExperiment() {
        setName("Beta Estimate Experiment");
    }
    
    public void initialize() {
        createValueSetter("n", Distribution.DISCRETE, 1, 100, 10);
        createValueSetter("a", Distribution.DISCRETE, 1, 25, 1);
        initializePane();
    }

    /**
     * This method defines the experiment. A sample of size n from the beta
     * distribuion is simulated. The point estimate, empirical bias and MSE are
     * computed
     */
    public void doExperiment() {
        super.doExperiment();
        resetSample();
        double sum = 0, x;
        int runs = getTime(), n = getSampleSize();
        for (int i = 0; i < n; i++) {
            x = rv.simulate();
            sum = sum + Math.log(x);
        }
        double sampleMean = rv.getIntervalData().getMean();
        u = sampleMean / (1 - sampleMean);
        v = -n / sum;
        uBias = ((runs - 1) * uBias + (u - a)) / runs;
        uMSE = ((runs - 1) * uMSE + (u - a) * (u - a)) / runs;
        vBias = ((runs - 1) * vBias + (v - a)) / runs;
        vMSE = ((runs - 1) * vMSE + (v - a) * (v - a)) / runs;
    }

    /**
     * Thie method updates the experiment, including the record table,
     * statistics table, and random variable graph
     */
    public void update() {
        super.update();
        getRecordTable().append("\t" + format(u) + "\t" + format(v));
        setStatistics("Est\tBias\tMSE" + "\nU\t" + format(uBias) + "\t"
                + format(uMSE) + "\nV\t" + format(vBias) + "\t" + format(vMSE));
    }

    /**
     * This method resets the experiment, by resetting the random variable
     * graph, record table, and statistics graph
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tU\tV");
        setStatistics("Est\tBias\tMSE");
    }

    /** This method handles the scroll events for changing the beta parameter */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(1)) {
            a = getValueSetter(1).getValue();
            dist.setParameters(a, 1);
            reset();
        } else super.update(o, arg);
    }
}

