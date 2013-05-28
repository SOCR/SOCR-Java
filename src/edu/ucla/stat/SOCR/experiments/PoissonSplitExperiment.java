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

import java.awt.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the two-type Poisson experiment. Each arrival,
 * independently, is one of two types. The arrivals are shown as dots on a
 * timeline, colored red or green to indicate the type. The random variables of
 * interest are the number of arrivals of the two types in an interval of a
 * specified size. The rate of the process and the probability that governs the
 * type can also be varied
 */
public class PoissonSplitExperiment extends Experiment {
    //Variables
    private double              r               = 3, t = 4, p = 0.5, time;
    private int                 count0, count1;

    private PoissonDistribution dist0           = new PoissonDistribution(r * t
                                                        * (1 - p));
    private PoissonDistribution dist1           = new PoissonDistribution(r * t * p);
    private RandomVariable      rv0             = new RandomVariable(dist0, "W");
    private RandomVariable      rv1             = new RandomVariable(dist1, "M");
    private RandomVariableGraph graph0          = new RandomVariableGraph(rv0);
    private RandomVariableGraph graph1          = new RandomVariableGraph(rv1);
    private RandomVariableTable table0          = new RandomVariableTable(rv0);
    private RandomVariableTable table1          = new RandomVariableTable(rv1);
    Timeline                    timeline        = new Timeline(0, t, 0.1 * t, 1);
    JLabel                      definitionLabel = new JLabel(
                                                        "W: # type 0 arrivals, M: # type 1 arrivals");

    /**
     * This method initializes the experiment, including the toolbar with the
     * scrollbars and labels, the random variable graphs and tables, the
     * timeline and the record table
     */
    public PoissonSplitExperiment() {
        setName("Two-Type Poisson Experiment");
        createValueSetter("r", 5, 50, 0.1);
        createValueSetter("t", 5, 50, 0.1);
        createValueSetter("p", 0, 20, 0.05);

        addTool(definitionLabel);
        addToolbar(timeline);
        //Graphs
        addGraph(graph0);
        addGraph(graph1);
        //Tables
        addTable(table0);
        addTable(table1);
        reset();
    }

    /**
     * This method defines the experiment. The arrivals are generated and tested
     * to see if they fall in the interval. Each interval is randomly assigned a
     * type. The values of the random variables are computed.
     */
    public void doExperiment() {
        super.doExperiment();
        timeline.resetData();
        time = -Math.log(1 - Math.random()) / r;
        count0 = 0;
        count1 = 0;
        while (time <= t) {
            if (Math.random() < p) {
                count1++;
                timeline.addTime(time, Color.red);
            } else {
                count0++;
                timeline.addTime(time, Color.green);
            }
            time = time - Math.log(1 - Math.random()) / r;
        }
        rv0.setValue(count0);
        rv1.setValue(count1);
    }

    /**
     * This method resets the experiment, including the timeline, the random
     * variable graphs and tables, and the record table
     */
    public void reset() {
        super.reset();
        timeline.reset();
        getRecordTable().append("\tW\tM");
        rv0.reset();
        rv1.reset();
        graph0.reset();
        graph1.reset();
        table0.reset();
        table1.reset();
    }

    /**
     * This method updates the displays, including the timeline, the random
     * variable graphs and tables, and the record table
     */
    public void update() {
        super.update();
        timeline.repaint();
        getRecordTable().append("\t" + count0 + "\t" + count1);
        graph0.repaint();
        graph1.repaint();
        table0.update();
        table1.update();
    }

    /**
     * This method handles the scrollbar events for adjusting the size of the
     * interval, the rate of the process, and the probability of a type 1
     * arrival
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            r = getValueSetter(0).getValue();
        } else if (arg == getValueSetter(1)) {
            t = getValueSetter(1).getValue();
            timeline.setRange(0, t, 0.1 * t);
        } else if (arg == getValueSetter(2)) {
            p = getValueSetter(2).getValue();
        }
        setDistribution();
    }

    /**
     * This method sets the parameters of the distribution when the parameters
     * of the experiment have changed
     */
    public void setDistribution() {
        dist0.setParameter(r * t * (1 - p));
        dist1.setParameter(r * t * p);
        reset();
    }
}

