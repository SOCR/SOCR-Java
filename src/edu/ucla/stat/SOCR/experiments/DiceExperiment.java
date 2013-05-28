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
 * A basic experiment that consists of rolling n dice. The random variables of
 * interest are the sum of scores, the average score, the maximum score, the
 * minimum score, and the number of aces. The number of dice and the die
 * distribution can be varied
 */
public class DiceExperiment extends Experiment {
    //Variables
    private int rvIndex = 0, n = 1, dice = 20, x, sum, min, max, aces;
    private double average;
    //objects
    private JButton dieButton = new JButton("Die Probabilities");
    private JComboBox rvJComboBox = new JComboBox();
    private JPanel toolbar = new JPanel();
    private DiceBoard diceBoard = new DiceBoard(dice);
    //Distributions
    private DieDistribution dieDist = new DieDistribution(DieDistribution.FAIR);
    private Convolution sumDist = new Convolution(dieDist, n);
    private LocationScaleDistribution averageDist = new LocationScaleDistribution(
            sumDist, 0, 1.0);
    private OrderStatisticDistribution minDist = new OrderStatisticDistribution(
            dieDist, n, 1);
    private OrderStatisticDistribution maxDist = new OrderStatisticDistribution(
            dieDist, n, n);
    private BinomialDistribution acesDist = new BinomialDistribution(n, dieDist
            .getDensity(1));
    private RandomVariable[] rv = new RandomVariable[5];
    private RandomVariableGraph rvGraph;
    private RandomVariableTable rvTable;
    private DieProbabilityDialog dieProbabilityDialog;

    /** Initialize the experiment */
    public DiceExperiment() {
        /***********************************************************************
         * System.err.println("Problems!!!"); if (dieButton==null || rvJComboBox
         * ==null || nScroll==null)
         * System.err.println("dieButton="+dieButton+"\trvJComboBox =" +
         * rvJComboBox + "\tnScroll=" + nScroll + "!!!");
         **********************************************************************/

        //Random variables
        rv[0] = new RandomVariable(sumDist, "Y");
        rv[1] = new RandomVariable(averageDist, "M");
        rv[2] = new RandomVariable(minDist, "U");
        rv[3] = new RandomVariable(maxDist, "V");
        rv[4] = new RandomVariable(acesDist, "Z");
        rvGraph = new RandomVariableGraph(rv[rvIndex]);
        rvTable = new RandomVariableTable(rv[rvIndex]);

        setName("Dice Experiment");
        //Event listeners
        dieButton.addActionListener(this);
        rvJComboBox.addItemListener(this);
        createValueSetter("n", Distribution.DISCRETE, 1, dice, n);

        //Random variable choice
        rvJComboBox.addItem("Sum Y");
        rvJComboBox.addItem("Average M");
        rvJComboBox.addItem("Min U");
        rvJComboBox.addItem("Max V");
        rvJComboBox.addItem("Aces Z");
        rvJComboBox.setSelectedIndex(0);

        //Toolbar
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(dieButton);
        toolbar.add(rvJComboBox);
        addToolbar(toolbar);
        //Graphs
        addGraph(diceBoard);
        addGraph(rvGraph);
        //Table
        addTable(rvTable);
        reset();
    }

    /**
     * This method defines the experiment. The dice are rolled and the values of
     * the random variables computed
     */
    public void doExperiment() {
        super.doExperiment();
        sum = 0;
        min = 6;
        max = 1;
        aces = 0;
        for (int i = 0; i < n; i++) {
            x = (int) dieDist.simulate();
            diceBoard.getDie(i).setValue(x);
            sum = sum + x;
            if (x < min) min = x;
            if (x > max) max = x;
            if (x == 1) aces++;
        }
        average = (double) sum / n;
        //Set values for random varaibles
        rv[0].setValue(sum);
        rv[1].setValue(average);
        rv[2].setValue(min);
        rv[3].setValue(max);
        rv[4].setValue(aces);
    }

    /**
     * This method resets the experiment, including the record table, the dice,
     * the random variables, and the graph and table for the selected variable
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tY\tM\tU\tV\tZ");
        diceBoard.showDice(0);
        for (int i = 0; i < 5; i++)
            rv[i].reset();
        rvGraph.reset();
        rvTable.reset();
    }

    /**
     * This method updates the display, including the record table, the dice,
     * and the graph and table for the selected variable
     */
    public void update() {
        super.update();
        diceBoard.showDice(n);
        getRecordTable().append(
                "\t" + sum + "\t" + format(average) + "\t" + min + "\t" + max
                        + "\t" + aces);
        rvGraph.repaint();
        rvTable.update();
    }

    /**
     * This method handles the events associated with the die probabilities
     * button.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dieButton) {
            Point fp = applet.getLocationOnScreen(), dp;
            Dimension fs = applet.getSize();
            Dimension ds = dieProbabilityDialog.getSize();
            dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2
                    - ds.height / 2);
            dieProbabilityDialog.setLocation(dp);

            dieProbabilityDialog.setProbabilities(dieDist.getProbabilities());
            dieProbabilityDialog.setVisible(true);
            if (dieProbabilityDialog.isOK()) {
                dieDist.setProbabilities(dieProbabilityDialog.getProbabilities());
                setDistributions();
            }
        } else super.actionPerformed(e);
    }

    /**
     * This method handles the choice events, including the choice of the die
     * distribution and the choice of the selected variable
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == rvJComboBox) {
            rvIndex = rvJComboBox.getSelectedIndex();
            rvGraph.setRandomVariable(rv[rvIndex]);
            rvTable.setRandomVariable(rv[rvIndex]);
            rvGraph.repaint();
            rvTable.update();
        } else super.itemStateChanged(event);
    }

    /** This method handles the scroll events for the number of dice */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        setDistributions();
    }

    /**
     * This method sets the parameters of the distributions when the parameters
     * have changed
     */
    public void setDistributions() {
        sumDist.setParameters(dieDist, n);
        averageDist.setParameters(sumDist, 0, 1.0 / n);
        minDist.setParameters(dieDist, n, 1);
        maxDist.setParameters(dieDist, n, n);
        acesDist.setParameters(n, dieDist.getDensity(1));
        reset();
    }
}

