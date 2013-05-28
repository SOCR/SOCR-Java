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

/** This class models the 2 dimensional Poisson process */
public class Poisson2DExperiment extends Experiment {
    //Variables
    private double              width           = 2, rate = 1;
    private int                 pointCount;
    private PoissonDistribution dist            = new PoissonDistribution(rate
                                                        * width * width);
    private RandomVariable      points          = new RandomVariable(dist, "N");
    private RandomVariableGraph pointsGraph     = new RandomVariableGraph(points);
    private RandomVariableTable pointsTable     = new RandomVariableTable(points);
    private ScatterPlot         scatterPlot     = new ScatterPlot(0, width, 0,
                                                        width);
    private JLabel              definitionLabel = new JLabel("N: # arrivals");

    /**
     * This method initializes the experiment, including the toolbar with
     * scrollbars and labels, the scatterplot, and the random variable graph and
     * table.
     */
    public Poisson2DExperiment() {
        setName("Two-Dimensional Poisson Experiment");
        scatterPlot.setPointSize(2);
        createValueSetter("w", 5, 50, 0.1);
        createValueSetter("r", 5, 50, 0.1);

        addTool(definitionLabel);
        //Graphs
        addGraph(scatterPlot);
        addGraph(pointsGraph);
        //Tables
        addTable(pointsTable);
        reset();
    }

    /**
     * This method defines the experiment. The number of poins is simulated as a
     * value from the Poisson distribution. Then, the points uniformly
     * distributed in the scatterplot.
     */
    public void doExperiment() {
        double x, y;
        super.doExperiment();
        pointCount = (int) points.simulate();
        scatterPlot.clear();
        for (int i = 0; i < pointCount; i++) {
            x = width * Math.random();
            y = width * Math.random();
            scatterPlot.addPoint(x, y);
        }
    }

    /**
     * This method resets the experiment, including the scatterplot, record
     * table, random variable graph and table.
     */
    public void reset() {
        super.reset();
        scatterPlot.reset();
        getRecordTable().append("\tN");
        points.reset();
        pointsGraph.reset();
        pointsTable.reset();
    }

    /**
     * This method updates the experiment, including the scatterplot, record
     * table, random variable graph and table.
     */
    public void update() {
        super.update();
        scatterPlot.repaint();
        getRecordTable().append("\t" + pointCount);
        pointsGraph.repaint();
        pointsTable.update();
    }

    /**
     * This method hankdles the scrollbar events for changing the rate and width
     * properties.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(1)) {
            rate = getValueSetter(1).getValue();
        } else if (arg == getValueSetter(0)) {
            width = getValueSetter(0).getValue();
            scatterPlot.setParameters(0, width, 0, width);

        }
        setDistribution();
    }

    /** This method sets the distribution have the parameters have changed. */
    public void setDistribution() {
        dist.setParameter(rate * width * width);
        reset();
    }
}

