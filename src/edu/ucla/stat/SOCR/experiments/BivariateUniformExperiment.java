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
import java.awt.event.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models a bivariate uniform experiment. Three regions can be
 * selected: a square region, a circular region, and a triangular region.
 */
public class BivariateUniformExperiment extends Experiment {
    //Variables
    private int distType = 0;
    double rho = 0, sum, sampleCov, sampleCor;
    //Objects
    private JPanel toolbar = new JPanel();
    private RandomVariable x = new RandomVariable(
            new ContinuousUniformDistribution(-6, 6), "X");
    private RandomVariable y = new RandomVariable(
            new ContinuousUniformDistribution(-6, 6), "Y");
    private RandomVariableGraph xGraph = new RandomVariableGraph(x);
    private RandomVariableGraph yGraph = new RandomVariableGraph(y);
    private RandomVariableTable xTable = new RandomVariableTable(x);
    private RandomVariableTable yTable = new RandomVariableTable(y);
    private BivariateScatterPlot xyScatter = new BivariateScatterPlot(-6, 6, 1, -6,
            6, 1);
    private JComboBox distJComboBox = new JComboBox();
    private JTextArea correlationTable = new SOCRApplet.SOCRTextArea();

    /**
     * This method initializes the experiment, including the distribution
     * choice, toolbar, scatterplot, random variable tables and graphs.
     */
    public BivariateUniformExperiment() {
        setName("Bivariate Uniform Experiment");
        distJComboBox.addItemListener(this);
        xyScatter.setParameters(0, 0);
        //Toolbars
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        distJComboBox.addItem("Square");
        distJComboBox.addItem("Triangle");
        distJComboBox.addItem("Circle");
        toolbar.add(distJComboBox);
        addToolbar(toolbar);
        //Graphs
        addGraph(xyScatter);
        addGraph(xGraph);
        addGraph(yGraph);
        //Tables
        correlationTable.setEditable(false);
        addTable(xTable);
        addTable(yTable);
        addTable(correlationTable);
        reset();
    }

    /**
     * This method defines the experiment. A point from the specified uniform
     * distribution is simulated and passed to the scatterplot.
     */
    public void doExperiment() {
        double r, theta, u, v;
        super.doExperiment();
        switch (distType) {
        case 0:
            x.setValue(12 * Math.random() - 6);
            y.setValue(12 * Math.random() - 6);
            break;
        case 1:
            u = 12 * Math.random() - 6;
            v = 12 * Math.random() - 6;
            x.setValue(Math.max(u, v));
            y.setValue(Math.min(u, v));
            break;
        case 2:
            u = 6 * Math.random();
            v = 6 * Math.random();
            r = Math.max(u, v);
            theta = 2 * Math.PI * Math.random();
            x.setValue(r * Math.cos(theta));
            y.setValue(r * Math.sin(theta));
            break;
        }
        sum = sum + x.getValue() * y.getValue();
        xyScatter.drawPoint(x.getValue(), y.getValue());
    }

    /**
     * This method updates the experiment, including the record table, random
     * variable graphs and tables, correlation table.
     */
    public void update() {
        super.update();
        int runs = getTime();
        sampleCov = (sum - runs * x.getIntervalData().getMean()
                * y.getIntervalData().getMean())
                / (runs - 1);
        sampleCor = sampleCov
                / (x.getIntervalData().getSD() * y.getIntervalData().getSD());
        getRecordTable().append(
                "\t" + format(x.getValue()) + "\t" + format(y.getValue()));
        xGraph.repaint();
        yGraph.repaint();
        xTable.update();
        yTable.update();
        correlationTable.setText("Correlation\nDist\tData\n" + format(rho) + "\t"
                + format(sampleCor));
        double slope = sampleCov / x.getIntervalData().getVariance();
        double intercept = y.getIntervalData().getMean() - slope
                * x.getIntervalData().getMean();
        if (runs > 1) xyScatter.drawSampleLine(slope, intercept);
    }

    /**
     * This method resets the experiment, including the random variable graphs
     * and tables, the record table, the scatterplot, and the correlation table.
     */
    public void reset() {
        super.reset();
        sum = 0;
        x.reset();
        y.reset();
        xyScatter.reset();
        xGraph.reset();
        yGraph.reset();
        xTable.update();
        yTable.update();
        getRecordTable().append("\tX\tY");
        correlationTable.setText("Correlation\nDist\tData\n" + format(rho));
    }

    /**
     * This method handles the choice events that occur when the distribuiton is
     * changed.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {
            distType = distJComboBox.getSelectedIndex();
            switch (distType) {
            case 0:
                x.setDistribution(new ContinuousUniformDistribution(-6, 6));
                y.setDistribution(new ContinuousUniformDistribution(-6, 6));
                rho = 0;
                break;
            case 1:
                x.setDistribution(new TriangleDistribution(-6, 6,
                        TriangleDistribution.UP));
                y.setDistribution(new TriangleDistribution(-6, 6,
                        TriangleDistribution.DOWN));
                rho = 0.5;
                break;
            case 2:
                x.setDistribution(new CircleDistribution(6));
                y.setDistribution(new CircleDistribution(6));
                rho = 0;
            }
            double slope = rho * y.getDistribution().getSD()
                    / x.getDistribution().getSD();
            double intercept = y.getDistribution().getMean() - slope
                    * x.getDistribution().getMean();
            xyScatter.setParameters(slope, intercept);
            reset();
        } else super.itemStateChanged(event);
    }
}

