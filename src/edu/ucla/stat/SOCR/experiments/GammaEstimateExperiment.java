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

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/** This class models the point estimation exerpiment for the gamma distribution. */
public class GammaEstimateExperiment extends EstimateExperiment {
    //Variables
    private double shape = 1, scale = 1;
    private double sampleMean, sampleVar, u, v, w, uBias, uMSE, vBias, vMSE, wBias,
            wMSE;
    //Objects
    private JLabel shapeLabel = new JLabel("shape = 1.0 ");
    private ParameterScrollbar shapeScroll = new ParameterScrollbar(1, 25, 0.5,
            shape);
    private JLabel scaleLabel = new JLabel("scale = 1.0 ");
    private ParameterScrollbar scaleScroll = new ParameterScrollbar(1, 5, 0.5,
            scale);
    private GammaDistribution dist = new GammaDistribution(shape, 1 / scale);
    private RandomVariable rv = new RandomVariable(dist, "X");

    /**
     * This method initializes the experiment, including the toolbar, scrollbars
     * and labels.
     */
    public GammaEstimateExperiment() {
        setName("Gamma Estimate Experiment");
        //Event listeners
        createValueSetter("shape", Distribution.CONTINUOUS, 1, 25);
        createValueSetter("scale", Distribution.CONTINUOUS, 1, 5);
        addTool(shapeScroll);
        addTool(shapeLabel);
        addTool(scaleScroll);
        addTool(scaleLabel);
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        super.doExperiment();
        resetSample();
        for (int i = 0; i < getSampleSize(); i++)
            rv.sample();
        sampleMean = rv.getIntervalData().getMean();
        sampleVar = rv.getIntervalData().getVariance();
        u = sampleMean * sampleMean / sampleVar;
        v = sampleVar / sampleMean;
        w = sampleMean / shape;
        int runs = getTime();
        uBias = ((runs - 1) * uBias + (u - shape)) / runs;
        uMSE = ((runs - 1) * uMSE + (u - shape) * (u - shape)) / runs;
        vBias = ((runs - 1) * vBias + (v - scale)) / runs;
        vMSE = ((runs - 1) * vMSE + (v - scale) * (v - scale)) / runs;
        wBias = ((runs - 1) * wBias + (w - scale)) / runs;
        wMSE = ((runs - 1) * wMSE + (w - scale) * (w - scale)) / runs;
    }

    /**
     * This method updates the experiment, including the record table, the
     * random variable graph and the statistics table.
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
     * This method resets the experiment, including the record table, the random
     * variable graph and the statistics table.
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tU\tV\tW");
        setStatistics("Est\tBias\tMSE");
    }

    /**
     * This method handles the scroll events associated with changing the
     * parameters of the gamma distribution.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            shape = getValueSetter(0).getValue();
            setDistribution();
        } else if (arg == getValueSetter(1)) {
            scale = getValueSetter(1).getValue();
            setDistribution();
        }
    }

    public void setDistribution() {
        dist.setParameters(shape, scale);
        reset();
    }
}

