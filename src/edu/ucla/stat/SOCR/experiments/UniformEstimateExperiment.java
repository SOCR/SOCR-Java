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
 * This class models the point estimation problem associated with the uniform
 * distribuiton on the interval [0, a].
 */
public class UniformEstimateExperiment extends EstimateExperiment {
    //Variables
    private double a = 1, u, v, w, uBias, uMSE, vBias, vMSE, wBias, wMSE;
    private ContinuousUniformDistribution dist = new ContinuousUniformDistribution(
            0, a);
    private RandomVariable rv = new RandomVariable(dist, "X");

    /**
     * This method initializes the experiment by calling the initialization
     * method in the superclass,and by setting up the toolbar containing the
     * prameter scrollbar and label.
     */
    public UniformEstimateExperiment() {
        setName("Uniform Estimate Experiment");
        createValueSetter("n", Distribution.DISCRETE, 1, 100, 10);
        createValueSetter("a = ", 5, 100, 0.1);
    }

    /**
     * This method defines the experiment. A sample from the uniform
     * distribution is simulated and the estimates of a are computed. The
     * empirical bias and mean square error are also computed.
     */
    public void doExperiment() {
        super.doExperiment();
        resetSample();
        int n = getSampleSize(), runs = getTime();
        for (int i = 0; i < n; i++)
            rv.sample();
        u = 2 * rv.getIntervalData().getMean();
        v = (n + 1) * rv.getIntervalData().getMaxValue() / n;
        w = (n + 1) * rv.getIntervalData().getMinValue();
        uBias = ((runs - 1) * uBias + (u - a)) / runs;
        uMSE = ((runs - 1) * uMSE + (u - a) * (u - a)) / runs;
        vBias = ((runs - 1) * vBias + (v - a)) / runs;
        vMSE = ((runs - 1) * vMSE + (v - a) * (v - a)) / runs;
        wBias = ((runs - 1) * wBias + (w - a)) / runs;
        wMSE = ((runs - 1) * wMSE + (w - a) * (w - a)) / runs;
    }

    /**
     * This method updates the experiment, including the record table, the
     * statistic table, and the random variable graph.
     */
    public void update() {
        super.update();
        getRecordTable().append(
                "\t" + format(u) + "\t" + format(v) + "\t" + format(w));
        setStatistics("Est\tBias\tMSE" + "\nU\t" + format(uBias) + "\t"
                + format(uMSE) + "\nV\t" + format(vBias) + "\t" + format(vMSE)
                + "\nW\t" + format(wBias) + "\t" + format(wMSE));
    }

    /**
     * This method resets the experiment, including the record table, the
     * statistics table, and the random variable graph.
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tU\tV\tW");
        setStatistics("Est\tBias\tMSE\n");
    }

    /**
     * This method handles the scrollbar events that occur when the parameter is
     * changed.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(1)) {
            a = getValueSetter(1).getValue();
            dist.setParameters(0, a);
            reset();
        } else super.update(o, arg);
    }
}

