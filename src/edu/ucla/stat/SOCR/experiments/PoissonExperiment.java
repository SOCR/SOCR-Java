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

/**
 * This class models the number of arrivals in an interval of a specified size
 * for the Poisson process with a specified rate. The arrivals are shown as dots
 * on a timeline
 */
public class PoissonExperiment extends Experiment {
    //Variables
    private double              r               = 4, t = 5, time;
    private int                 arrivalCount;

    private PoissonDistribution dist            = new PoissonDistribution(r * t);
    private RandomVariable      arrivals        = new RandomVariable(dist, "N");
    private RandomVariableGraph arrivalsGraph   = new RandomVariableGraph(arrivals);
    private RandomVariableTable arrivalsTable   = new RandomVariableTable(arrivals);
    private Timeline            timeline        = new Timeline(0, t, 0.1 * t, 1);
    JLabel                      definitionLabel = new JLabel("N: # arrivals");

    /**
     * This method initializes the experiment, including the timeline, the
     * random variable graph and table, the record table, and the toolbar
     * containing the scrollbars and label
     */
    public PoissonExperiment() {
        setName("Poisson Experiment");
        createValueSetter("r", Distribution.CONTINUOUS, 1, 10, 4);
        createValueSetter("t", Distribution.CONTINUOUS, 1, 10, 5);

        addTool(definitionLabel);
        addToolbar(timeline);
        //Graphs
        addGraph(arrivalsGraph);
        //Tables
        addTable(arrivalsTable);
    }

    /**
     * Thie method defines the experiment: the arrivals are generated and tested
     * to see if they belong to the interval [0, t]
     */
    public void doExperiment() {
        super.doExperiment();
        timeline.resetData();
        time = -Math.log(1 - Math.random()) / r;
        arrivalCount = 0;
        while (time <= t) {
            arrivalCount++;
            timeline.addTime(time);
            time = time - Math.log(1 - Math.random()) / r;
        }
        arrivals.setValue(arrivalCount);
    }

    /**
     * This method resets the experiment, including the timeline, record table,
     * random variable graph and table
     */
    public void reset() {
        super.reset();
        timeline.reset();
        getRecordTable().append("\tN");
        arrivals.reset();
        arrivalsGraph.reset();
        arrivalsTable.reset();
    }

    /**
     * This method updates the displays, including the timeline, record table,
     * random variable graph and table
     */
    public void update() {
        super.update();
        timeline.repaint();
        getRecordTable().append("\t" + arrivalCount);
        arrivalsGraph.repaint();
        arrivalsTable.update();
    }

    /**
     * This method handles the scrollbar events for adjusting the size of the
     * time interval and the rate of the Poisson process
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(1)) {
            t = getValueSetter(1).getValue();
            timeline.setRange(0, t, t / 10);
        } else if (arg == getValueSetter(0)) {
            r = getValueSetter(0).getValue();

        }
        setDistribution();
    }

    /**
     * This method sets the parameters of the distribution, when the parameters
     * have changed
     */
    public void setDistribution() {
        dist.setParameter(r * t);
        reset();
    }
}

