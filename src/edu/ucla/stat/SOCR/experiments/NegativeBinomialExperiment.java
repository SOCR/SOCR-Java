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

import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * This class models the negative binomial experiment; the random variable of
 * interest is the number of trials until a specified number of successes. The
 * expreiment is illustrated in terms of a timeline.
 */
public class NegativeBinomialExperiment extends Experiment {
    //Variables
    private int k = 1, trialCount, headCount;
    private double p = 0.5;
    //Objects
    private JPanel toolbar = new JPanel();
    private JLabel definitionJLabel = new JLabel("Y: Trial Number of k'th Success");
    private NegativeBinomialDistribution trialsDist = new NegativeBinomialDistribution(
            k, p);
    private RandomVariable trials = new RandomVariable(trialsDist, "Y");
    private RandomVariableGraph trialsGraph = new RandomVariableGraph(trials);
    private RandomVariableTable trialsTable = new RandomVariableTable(trials);
    private Timeline timeline = new Timeline(1, trialsDist.getDomain()
            .getUpperValue(), 1);

    /**
     * This method initializes the experiment by setting up the toolbar, random
     * variable graph and table, timeline
     */
    public NegativeBinomialExperiment() {
        setName("Negative Binomial Experiment");
        createValueSetter("k", Distribution.DISCRETE, 1, 5, k);
        createValueSetter("p", 5, 100, 0.01);
        timeline.setMargins(35, 20, 20, 20);
        //Toolbars
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionJLabel);
        addToolbar(toolbar);
        addToolbar(timeline);
        //Graphs
        trialsGraph.setDomain(new Domain(1, trialsDist.getDomain().getUpperValue(),
                1));
        addGraph(trialsGraph);
        //Table
        addTable(trialsTable);
    }

    /**
     * This method defines the experiment. The Bernoulli trials are performed
     * until the specified number of successes have occurred.
     */
    public void doExperiment() {
        super.doExperiment();
        timeline.resetData();
        trialCount = 0;
        headCount = 0;
        while (headCount < k) {
            trialCount++;
            if (Math.random() < p) {
                headCount++;
                if (headCount == k) timeline.addTime(trialCount, Color.red);
                else timeline.addTime(trialCount, Color.green);
            }
        }
        trials.setValue(trialCount);
    }

    /**
     * This method resets the experiment, including the timeline, record table,
     * random variable graph and table.
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tY");
        timeline.reset();
        trials.reset();
        trialsGraph.reset();
        trialsTable.reset();
        trialsGraph.setDomain(new Domain(0.5, trialsDist.getDomain()
                .getUpperBound(), 1));
        //timeline.setRange(1, trialsGraph.getUpperValue());
    }

    /**
     * This method updates the experiment, including the timeline, record table
     * random variable graph and table.
     */
    public void update() {
        super.update();
        timeline.repaint();
        trialsGraph.repaint();
        trialsTable.update();
        getRecordTable().append("\t" + trialCount);
    }

    /**
     * This method handles the scrollbar events for changing the parameters of
     * the experiment, the number of successes and the probability of success.
     */
    public void update(Observable o, Object arg) {
        k = getValueSetter(0).getValueAsInt();
        p = getValueSetter(0).getValue();
        setDistribution();
        reset();
    }

    /**
     * This method sets the parameters of the distribution, when these have been
     * changed with the scrollbars.
     */
    public void setDistribution() {
        trialsDist.setParameters(k, p);
        timeline.setRange(1, trialsDist.getDomain().getUpperValue(), 1);
    }
}

