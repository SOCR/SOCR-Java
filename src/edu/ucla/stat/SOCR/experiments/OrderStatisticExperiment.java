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
 * This experiment illustrates the distribution of the order statistic
 * corresponding to a specified distribution and a specified order.
 */
public class OrderStatisticExperiment extends Experiment {
    //Variables
    private int sampleSize = 5, order = 1;
    private double[] sample = new double[sampleSize];
    private double[] orderStatistic = new double[sampleSize];
    //Objects
    private Distribution basicDist = new ContinuousUniformDistribution(0, 1);
    private RandomVariable basicRV = new RandomVariable(basicDist, "X");
    private OrderStatisticDistribution orderDist = new OrderStatisticDistribution(
            basicDist, sampleSize, order);
    private RandomVariable orderRV = new RandomVariable(orderDist, "X(k)");
    private JComboBox distJComboBox = new JComboBox();
    private RandomVariableGraph basicGraph = new RandomVariableGraph(basicRV);
    private RandomVariableGraph orderGraph = new RandomVariableGraph(orderRV);
    private RandomVariableTable orderTable = new RandomVariableTable(orderRV);
    private JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /**
     * This method initializes the experiment, including the toolbar with
     * scrollbars, the random variable graphs and tables.
     */
    public OrderStatisticExperiment() {
        setName("Order Statistics Experiment");
        //Listeners
        distJComboBox.addItemListener(this);
        createValueSetter("n", Distribution.DISCRETE, 1, 10);
        createValueSetter("k", Distribution.DISCRETE, 1, 10);

        distJComboBox.addItem("Uniform (0, 1)");
        distJComboBox.addItem("Exponential (1)");
        //Construct toolbar
        addTool(distJComboBox);
        //Construct graph panel
        addGraph(basicGraph);
        addGraph(orderGraph);
        //Construct table panel
        sampleTable.setEditable(false);
        addTable(sampleTable);
        addTable(orderTable);
    }

    /**
     * This method handles the choice events that correspond to changing the
     * basic distribution.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {
            switch (distJComboBox.getSelectedIndex()) {
            case 0: //uniform
                basicDist = new ContinuousUniformDistribution(0, 1);
                break;
            case 1: //Exponential
                basicDist = new ExponentialDistribution(1);
                break;
            }
            setDistributions();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events that correspond to changing the
     * sample size or the order.
     */
    public void update(Observable o, Object arg) {
        sampleSize = getValueSetter(0).getValueAsInt();
        order = getValueSetter(1).getValueAsInt();
        //if (sampleSize < order) {
        if (arg == getValueSetter(0)) {
            sample = new double[sampleSize];
            orderStatistic = new double[sampleSize];
            order = 1;
            getValueSetter(0).setRange(1, sampleSize, order);
            setDistributions();
        } else if (arg == getValueSetter(1)) {

            setDistributions();
        }
        //}
    }

    /** This method sets the distributions. */
    public void setDistributions() {
        basicRV = new RandomVariable(basicDist, "X");
        orderDist.setParameters(basicDist, sampleSize, order);
        basicGraph.setRandomVariable(basicRV);
        reset();
    }

    /**
     * This method resets the experiment, including the random variable graphs
     * and tables, the sample table, and the record table.
     */
    public void reset() {
        super.reset();
        basicRV.reset();
        orderRV.reset();
        orderTable.reset();
        sampleTable.setText("Sample\tOrder Statistics");
        getRecordTable().append("\tX(k)");
        basicGraph.reset();
        orderGraph.reset();
    }

    /**
     * This method defines the experiment. A sample of the specified size from
     * the basic distribution is simulated and then the appropriate order
     * statistic is computed.
     */
    public void doExperiment() {
        super.doExperiment();
        double x;
        boolean smallest;
        basicRV.reset();
        for (int i = 0; i < sampleSize; i++) {
            //Simulate a value from the basic distribution
            x = basicRV.simulate();
            sample[i] = x;
            //Recompute the order statistics
            smallest = true;
            for (int j = i - 1; j >= 0; j = j - 1) {
                if (orderStatistic[j] <= x) {
                    orderStatistic[j + 1] = x;
                    smallest = false;
                    break;
                }
                orderStatistic[j + 1] = orderStatistic[j];
            }
            if (smallest) orderStatistic[0] = x;
        }
        //Assign the value to the specified order statistic
        orderRV.setValue(orderStatistic[order - 1]);
    }

    /**
     * This method updates the epxeriment, including the sample table, the
     * record table, and the random variable graphs and table.
     */
    public void update() {
        super.update();
        String sampleText = "i\tXi\tX(i)";
        for (int i = 0; i < sampleSize; i++) {
            sampleText = sampleText + "\n" + (i + 1) + "\t" + format(sample[i])
                    + "\t" + format(orderStatistic[i]);
        }
        sampleTable.setText(sampleText);
        getRecordTable().append("\t" + format(orderStatistic[order - 1]));
        basicGraph.reset();
        basicGraph.repaint();
        orderGraph.repaint();
        orderTable.update();
    }
}
