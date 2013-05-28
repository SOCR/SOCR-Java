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

/** This class models the red and black experiment. */
public class RedBlackExperiment extends Experiment {
    //Parameters and variables
    public static final int TIMID_PLAY = 0, BOLD_PLAY = 1;
    private int strategy = TIMID_PLAY, initial = 8, target = 16, current, bet,
            trials;
    private double p = 0.5, successProb = 0.5, trialsDistMean = 64, trialsDataMean;
    private double[] boldProb = new double[129];
    private double[] boldMean = new double[129];
    //Objects
    private JPanel toolbar = new JPanel();
    private RedBlackGraph rbGraph = new RedBlackGraph(8, 16);
    private RandomVariable success = new RandomVariable(new BernoulliDistribution(
            0.5), "J");
    private RandomVariableGraph successGraph = new RandomVariableGraph(success);
    private RandomVariableTable successTable = new RandomVariableTable(success);
    private EstimateGraph trialsGraph = new EstimateGraph(trialsDistMean);
    private JTextArea trialsTable = new SOCRApplet.SOCRTextArea();
    private JComboBox strategyJComboBox = new JComboBox();
    private JComboBox targetJComboBox = new JComboBox();
    private JLabel definitionLabel = new JLabel("J: win, N: # trials");

    /** This method initializes the experiment. */
    public RedBlackExperiment() {
        setName("Red and Black Experiment");
        createValueSetter("x", Distribution.DISCRETE, 0, target, initial);
        createValueSetter("p", Distribution.CONTINUOUS, 0, 1);
        successGraph.showMoments(0);
        successGraph.setMargins(35, 20, 20, 20);
        successTable.showMoments(0);
        trialsTable.setEditable(false);
        //JComboBoxs
        strategyJComboBox.addItem("Timid Play");
        strategyJComboBox.addItem("Bold Play");
        for (int i = 1; i < 8; i++)
            targetJComboBox.addItem("a = " + Math.pow(2, i));
        targetJComboBox.setSelectedIndex(3);
        strategyJComboBox.addItemListener(this);
        targetJComboBox.addItemListener(this);
        //Construct toolbar
        toolbar.add(strategyJComboBox);
        toolbar.add(targetJComboBox);
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Construct graph panel
        addGraph(rbGraph);
        addGraph(successGraph);
        addGraph(trialsGraph);
        //Construct table panel
        addTable(successTable);
        addTable(trialsTable);
        //Final actions
        computeBoldParameters();
    }

    /** This method define the experiment. */
    public void doExperiment() {
        super.doExperiment();
        trials = 0;
        current = initial;
        while (current > 0 & current < target) {
            if (strategy == TIMID_PLAY) bet = 1;
            else if (strategy == BOLD_PLAY) {
                if (current < target - current) bet = current;
                else bet = target - current;
            }
            if (Math.random() <= p) current = current + bet;
            else current = current - bet;
            trials = trials + 1;
        }
        if (current == target) success.setValue(1);
        else success.setValue(0);
        int runs = getTime();
        trialsDataMean = ((runs - 1) * trialsDataMean + trials) / runs;
    }

    /**
     * This method runs the experiment one time, playing a sound that depends on
     * the outcome.
     */
    public void step() {
        doExperiment();
        update();
        try {
            if (current == target) play("sounds/1.au");
            else play("sounds/0.au");
        } catch (Exception e) {
            ;
        }
    }

    /** This method updates the experiment. */
    public void update() {
        super.update();
        rbGraph.setCurrent(current);
        rbGraph.drawCurrent();
        successGraph.repaint();
        trialsGraph.setEstimate(trialsDataMean);
        trialsGraph.repaint();
        getRecordTable().append("\t" + format(success.getValue()) + "\t" + trials);
        successTable.update();
        trialsTable.setText("N\tDist\tData\nMean\t" + format(trialsDistMean) + "\t"
                + format(trialsDataMean));
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        success.reset();
        rbGraph.setCurrent(initial);
        rbGraph.repaint();
        successGraph.repaint();
        trialsGraph.setEstimate(0);
        trialsGraph.repaint();
        getRecordTable().append("\tJ\tN");
        successTable.update();
        trialsTable.setText("N\tData\tDist\nMean\t" + format(trialsDistMean));
    }

    /**
     * This method handles the choice events associated with a change in
     * strategy or a change in the target.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == strategyJComboBox) {
            strategy = strategyJComboBox.getSelectedIndex();
            setParameters();
        } else if (event.getSource() == targetJComboBox) {
            target = (int) (Math.pow(2, targetJComboBox.getSelectedIndex() + 1));
            initial = Math.min(initial, target);
            getValueSetter(0).setRange(0, target,initial);
            setParameters();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events associated with a change in the
     * initial fortune or a change in the probability of success.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            initial = getValueSetter(0).getValueAsInt();
            setParameters();
        } else if (arg == getValueSetter(1)) {
            p = getValueSetter(1).getValue();
            computeBoldParameters();
            setParameters();
        }
    }

    /**
     * This method sets the parameters: the success probability and the mean of
     * the number of trials.
     */
    public void setParameters() {
        rbGraph.setParameters(initial, target);
        if (strategy == TIMID_PLAY) {
            if (p == 0) {
                successProb = 0;
                trialsDistMean = initial;
            } else if (p == 0.5) {
                successProb = (double) initial / target;
                trialsDistMean = initial * (target - initial);

            } else {
                successProb = (Math.pow((1 - p) / p, initial) - 1)
                        / (Math.pow((1 - p) / p, target) - 1);
                trialsDistMean = (initial - target * successProb) / (1 - 2 * p);
            }
        } else {
            int index = (int) Math.pow(2, 6 - targetJComboBox.getSelectedIndex())
                    * initial;
            successProb = boldProb[index];
            trialsDistMean = boldMean[index];
        }
        success = new RandomVariable(new BernoulliDistribution(successProb), "J");
        successGraph.setRandomVariable(success);
        successTable.setRandomVariable(success);
        trialsGraph.setParameter(trialsDistMean);
        reset();
    }

    /**
     * This method computes the probability of reaching the target fortune and
     * the expected number of trials under the bold strategy.
     */
    public void computeBoldParameters() {
        int order = 0;
        int index, ones;
        double a, b, c, d, e, f;
        boldProb[0] = 0;
        boldMean[128] = 0;
        for (int i = 0; i <= 1; i++) {
            if (i == 1) {
                order = 1;
                a = 1 - p;
            } else a = p;
            for (int j = 0; j <= 1; j++) {
                if (j == 1) {
                    order = 2;
                    b = 1 - p;
                } else b = p;
                for (int k = 0; k <= 1; k++) {
                    if (k == 1) {
                        order = 3;
                        c = 1 - p;
                    } else c = p;
                    for (int l = 0; l <= 1; l++) {
                        if (l == 1) {
                            order = 4;
                            d = 1 - p;
                        } else d = p;
                        for (int m = 0; m <= 1; m++) {
                            if (m == 1) {
                                order = 5;
                                e = 1 - p;
                            } else e = p;
                            for (int n = 0; n <= 1; n++) {
                                if (n == 1) {
                                    order = 6;
                                    f = 1 - p;
                                } else f = p;
                                for (int r = 0; r <= 1; r++) {
                                    if (r == 1) order = 7;
                                    index = i * 64 + j * 32 + k * 16 + l * 8 + m
                                            * 4 + n * 2 + r;
                                    ones = i + j + k + l + m + n + r;
                                    boldProb[index + 1] = boldProb[index]
                                            + Math.pow(1 - p, ones)
                                            * Math.pow(p, 7 - ones);
                                    if (order == 0) boldMean[index] = 0;
                                    else if (order == 1) boldMean[index] = 1;
                                    else if (order == 2) boldMean[index] = 1 + a;
                                    else if (order == 3) boldMean[index] = 1 + a
                                            + a * b;
                                    else if (order == 4) boldMean[index] = 1 + a
                                            + a * b + a * b * c;
                                    else if (order == 5) boldMean[index] = 1 + a
                                            + a * b + a * b * c + a * b * c * d;
                                    else if (order == 6) boldMean[index] = 1 + a
                                            + a * b + a * b * c + a * b * c * d + a
                                            * b * c * d * e;
                                    else if (order == 7)
                                            boldMean[index] = 1 + a + a * b + a * b
                                                    * c + a * b * c * d + a * b * c
                                                    * d * e + a * b * c * d * e * f;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
