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
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the classical Galton board experiment, illustrating the
 * binomial distribution
 */
public class GaltonBoardExperiment extends Experiment {
    //Variables
    private int n = 15, x, rvIndex = 0;;
    private double p = 0.5, m;
    private int[] outcome = new int[n];
    //Objects
    private JPanel toolbar = new JPanel();
    private GaltonBoard galtonBoard = new GaltonBoard(n, 8, 5);
    private JComboBox rvJComboBox = new JComboBox();
    private BinomialDistribution sumDist = new BinomialDistribution(n, p);
    private LocationScaleDistribution averageDist = new LocationScaleDistribution(
            sumDist, 0, 1.0 / n);
    private RandomVariable sumRV = new RandomVariable(sumDist, "X");
    private RandomVariable averageRV = new RandomVariable(averageDist, "M");
    private RandomVariableGraph rvGraph = new RandomVariableGraph(sumRV);
    private RandomVariableTable rvTable = new RandomVariableTable(averageRV);

    /**
     * This method initializes the experiment, including the Galton board,
     * random variable table and graph, and the toolbar
     */
    public GaltonBoardExperiment() {
        setName("Galton Board Experiment");
        //Event listeners
        createValueSetter("n", Distribution.DISCRETE, 1, n, n);
        createValueSetter("p", Distribution.CONTINUOUS, 0, 1);
        rvJComboBox.addItemListener(this);
        //JComboBox
        rvJComboBox.addItem("X: Number of succcesses");
        rvJComboBox.addItem("M: Proportion of successes");
        //Toolbars
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(rvJComboBox);
        addToolbar(toolbar);
        //Graphs
        addGraph(galtonBoard);
        addGraph(rvGraph);
        //Tables
        addTable(rvTable);
        //Initialize
        reset();
    }

    /**
     * This method defines the experiment: n Bernoulli trials are performed and
     * the the values of the random variable are computed
     */
    public void doExperiment() {
        super.doExperiment();
        x = 0;
        for (int i = 0; i < n; i++) {
            if (Math.random() < p) {
                outcome[i] = 1;
                x++;
            } else outcome[i] = 0;
        }
        m = (double) x / n;
        sumRV.setValue(x);
        averageRV.setValue(m);
    }

    /**
     * This method resets the experiment, including the Galton board, the record
     * table, the random variable graph and table
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tX\tM");
        sumRV.reset();
        averageRV.reset();
        galtonBoard.reset();
        rvGraph.reset();
        rvTable.reset();
    }

    /**
     * This method updates the display, including the Galton board, the record
     * table, the random variable graph and table
     */
    public void update() {
        super.update();
        getRecordTable().append("\t" + x + "\t" + format(m));
        galtonBoard.setPath(outcome);
        galtonBoard.repaint();
        rvGraph.repaint();
        rvTable.update();
    }

    /**
     * This method handles the scroll events that adjust the number of trials
     * and the probability of success
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            n = getValueSetter(0).getValueAsInt();
            setDistribution();
            outcome = new int[n];
            galtonBoard.setRows(n);
            reset();
        } else {
            p = getValueSetter(1).getValue();
            setDistribution();
            reset();
        }
    }

    /**
     * This method handles the choice event, for selecting the random variable
     * to display
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == rvJComboBox) {
            int rvIndex = rvJComboBox.getSelectedIndex();
            if (rvIndex == 0) {
                rvGraph.setRandomVariable(sumRV);
                rvTable.setRandomVariable(sumRV);
            } else {
                rvGraph.setRandomVariable(averageRV);
                rvTable.setRandomVariable(averageRV);
            }
            rvGraph.repaint();
            rvTable.update();
        } else super.itemStateChanged(event);
    }

    /** This method sets the parameters of the distributions */
    public void setDistribution() {
        sumDist.setParameters(n, p);
        averageDist.setParameters(sumDist, 0, 1.0 / n);
        reset();
    }
}

