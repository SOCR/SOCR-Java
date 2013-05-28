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
 * This experiment models the exponential distribution in terms of the inter-
 * arrival times of a Poisson process
 */
public class ExponentialExperiment extends Experiment {
    //Variables
    private double r = 1, time;
    //Objects
    private JPanel toolbar = new JPanel();
    private ExponentialDistribution dist = new ExponentialDistribution(r);
    private Timeline timeline = new Timeline(dist.getDomain().getLowerBound(), dist
            .getDomain().getUpperBound(), dist.getDomain().getWidth(), dist
            .getType());
    private RandomVariable arrivalTime = new RandomVariable(dist, "X");
    private RandomVariableGraph arrivalTimeGraph = new RandomVariableGraph(
            arrivalTime);
    private RandomVariableTable arrivalTimeTable = new RandomVariableTable(
            arrivalTime);
    private JLabel definitionLabel = new JLabel("X: interarrival time");

    /**
     * This method initializes the experiment by setting up the toolbar, graphs,
     * and tables
     */
    public ExponentialExperiment() {
        setName("Exponential Experiment");
        createValueSetter("r", 5, 50, 0.1);
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        timeline.setMargins(35, 20, 20, 20);
        addToolbar(timeline);
        //Graphs
        addGraph(arrivalTimeGraph);
        //Table
        addTable(arrivalTimeTable);
        reset();
    }

    /**
     * This method defines the experiment. A value of the exponential
     * distribution is simulated and passed to the timeline
     */
    public void doExperiment() {
        super.doExperiment();
        timeline.resetData();
        time = -Math.log(1 - Math.random()) / r;
        timeline.addTime(time);
        arrivalTime.setValue(time);
    }

    /**
     * This method resets the experiment, including the record table, random
     * variable graph and table, and the time line
     */
    public void reset() {
        super.reset();
        timeline.reset();
        getRecordTable().append("\tX");
        arrivalTime.reset();
        arrivalTimeGraph.reset();
        arrivalTimeTable.reset();
    }

    /**
     * This method updates the display, including the timeline, random variable
     * graph and table, and the record table
     */
    public void update() {
        super.update();
        timeline.repaint();
        arrivalTimeGraph.repaint();
        arrivalTimeTable.update();
        getRecordTable().append("\t" + format(time));
    }

    /**
     * This method handles the scrollbar events for adjusting the rate parameter
     * of the exponential distribution
     */
    public void update(Observable o, Object arg) {
        r = getValueSetter(0).getValue();
        dist.setRate(r);
        timeline.setRange(dist.getDomain().getLowerBound(), dist.getDomain()
                .getUpperBound(), dist.getDomain().getWidth());
        reset();
    }
}

