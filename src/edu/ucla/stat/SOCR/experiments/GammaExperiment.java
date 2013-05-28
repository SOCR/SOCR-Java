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
 * This class models the gamma distribution in terms of the arrival times of a
 * Poisson process
 */
public class GammaExperiment extends Experiment {
    //Variables
    private int k = 1;
    private double r = 1, time;
    //Objects
    private JPanel toolbar = new JPanel();
    private GammaDistribution dist = new GammaDistribution(k, 1 / r);
    private Timeline timeline = new Timeline(dist.getDomain().getLowerBound(), dist
            .getDomain().getUpperBound(), dist.getDomain().getWidth(), 1);
    private RandomVariable arrivalTime = new RandomVariable(dist, "T(1)");
    private RandomVariableGraph arrivalTimeGraph = new RandomVariableGraph(
            arrivalTime);
    private RandomVariableTable arrivalTimeTable = new RandomVariableTable(
            arrivalTime);
    private JLabel definitionLabel = new JLabel("T(k): k'th arrival time");

    /**
     * This method initializes the experiment, including the layout of the
     * toolbar, timeline, random variable graph and table
     */
    public GammaExperiment() {
        setName("Gamma Experiment");
    }
    
    public void initialize() {
        timeline.setMargins(35, 20, 20, 20);
        //Event Listeners
        createValueSetter("k", Distribution.DISCRETE, 1, 5, k);
        createValueSetter("r", 5, 50, 0.1);
        //Toolbar
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        addToolbar(timeline);
        //Graphs
        addGraph(arrivalTimeGraph);
        //Table
        addTable(arrivalTimeTable);
        //reset();
    }

    /**
     * This method defines the experiment: k arrivals in the Poisson process are
     * simulated
     */
    public void doExperiment() {
        super.doExperiment();
        timeline.resetData();
        time = 0;
        for (int i = 0; i < k; i++) {
            time = time - Math.log(1 - Math.random()) / r;
            timeline.addTime(time);
        }
        arrivalTime.setValue(time);
    }

    /**
     * This method resets the experiment, including the timeline, record table,
     * random variable graph and table
     */
    public void reset() {
        super.reset();
        timeline.reset();
        String recordText = "";
        for (int i = 0; i < k; i++)
            recordText = recordText + "\tT(" + String.valueOf(i + 1) + ")";
        getRecordTable().append(recordText);
        arrivalTime.reset();
        arrivalTimeGraph.reset();
        arrivalTimeTable.reset();
    }

    /**
     * This method updates the display, including the timeline, record table,
     * random variable graph and table
     */
    public void update() {
        super.update();
        timeline.repaint();
        arrivalTimeGraph.repaint();
        arrivalTimeTable.update();
        String recordText = "";
        for (int i = 0; i < k; i++)
            recordText = recordText + "\t" + format(timeline.timeAt(i));
        getRecordTable().append(recordText);
    }

    /**
     * This method handles the scrollbar events that correspond to varying the
     * rate and the order of the arrival time
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            k = getValueSetter(0).getValueAsInt();
        } else {
            r = getValueSetter(1).getValue();

        }
        setDistribution();
        reset();

    }

    /** This method sets the parameters of the distribution */
    public void setDistribution() {
        dist.setParameters(k, 1 / r);
        arrivalTime.setName("T(" + String.valueOf(k) + ")");
        timeline.setRange(dist.getDomain().getLowerBound(), dist.getDomain()
                .getUpperBound(), dist.getDomain().getWidth());
    }
}

