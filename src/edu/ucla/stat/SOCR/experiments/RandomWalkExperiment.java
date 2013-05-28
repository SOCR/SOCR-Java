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

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the simple random walk on the time interval [0, 2n]. The
 * random variables of interest are the final position, the maximum position,
 * and the last zero.
 */
public class RandomWalkExperiment extends Experiment {

    private int n = 10, rvIndex = 0;
    private RandomWalkGraph walkGraph = new RandomWalkGraph(n);
    private RandomVariable[] walkRV = new RandomVariable[3];
    private JComboBox rvJComboBox = new JComboBox();
    private RandomVariableGraph rvGraph;
    private RandomVariableTable rvTable;

    /**
     * This method initializes the experiment, including the toolbar containing
     * the scrollbar, choice, and label, the graph panel containing the random
     * walk graph and the random variable graph, and the table panel containing
     * the record table and the radnom walk table.
     */
    public RandomWalkExperiment() {
        setName("Randowm Walk Experiment");
        createValueSetter("Steps", Distribution.DISCRETE, 10, 50, 10);
        //Event listeners
        rvJComboBox.addItemListener(this);
        //rvJComboBox
        rvJComboBox.addItem("Last Value");
        rvJComboBox.addItem("Maximum Value");
        rvJComboBox.addItem("Last Zero");

        //Random Variables
        walkRV[0] = new RandomVariable(new WalkPositionDistribution(n, 0.5), "Y");
        walkRV[1] = new RandomVariable(new WalkMaxDistribution(n), "M");
        walkRV[2] = new RandomVariable(new DiscreteArcsineDistribution(n), "L");
        rvGraph = new RandomVariableGraph(walkRV[rvIndex]);
        rvTable = new RandomVariableTable(walkRV[rvIndex]);

        rvJComboBox.setSelectedIndex(0);

        //Toolbar
        addTool(rvJComboBox);

        addGraph(walkGraph);
        addGraph(rvGraph);

        addTable(rvTable);
    }

    /**
     * This method defines the experiment. The random walk is performed and the
     * values of the random variables computed.
     */
    public void doExperiment() {
        super.doExperiment();
        walkGraph.walk(0);
        walkRV[0].setValue(walkGraph.getValue(n));
        walkRV[1].setValue(walkGraph.getMaxValue());
        walkRV[2].setValue(walkGraph.getLastZero());
    }

    /**
     * This method resets the experiment, including the random walk graph, the
     * random variable table and graph, and the record table.
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tY\tM\tL");
        walkGraph.reset();
        for (int i = 0; i < 3; i++)
            walkRV[i].reset();
        rvGraph.reset();
        rvTable.reset();
    }

    /**
     * This method updates the experiment, including the random walk graph, the
     * random variable table and graph, and the record table.
     */
    public void update() {
        super.update();
        walkGraph.repaint();
        getRecordTable().append(
                "\t" + walkGraph.getValue(n) + "\t" + walkGraph.getMaxValue()
                        + "\t" + walkGraph.getLastZero());
        rvGraph.repaint();
        rvTable.update();
    }

    /** This method handles the event associated with the random variable choice. */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == rvJComboBox) {
            try {
                rvIndex = rvJComboBox.getSelectedIndex();
                rvGraph.setRandomVariable(walkRV[rvIndex]);
                rvTable.setRandomVariable(walkRV[rvIndex]);
                rvGraph.repaint();
                rvTable.update();
            } catch (Exception e) {
                super.itemStateChanged(event);
            }
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the events associated with the time parameter
     * scrollbar.
     */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        walkRV[0] = new RandomVariable(new WalkPositionDistribution(n, 0.5), "Y");
        walkRV[1] = new RandomVariable(new WalkMaxDistribution(n), "M");
        walkRV[2] = new RandomVariable(new DiscreteArcsineDistribution(n), "L");
        walkGraph.setUpperTime(n);
        rvGraph.setRandomVariable(walkRV[rvIndex]);
        rvTable.setRandomVariable(walkRV[rvIndex]);
        reset();

    }
}

