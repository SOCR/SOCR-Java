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

import edu.ucla.stat.SOCR.util.*;
import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * This class models the experiment of estimating the probability of success in
 * the Bernoulli trials model.
 */
public class ProportionEstimateExperiment extends Experiment {
    //Constants
    public final static int INTERVAL = 0, LOWER_BOUND = 1, UPPER_BOUND = 2;
    //Variables
    private double p = 0.5, lowerCritical = -1.645, upperCritical = 1.645;
    private int sampleSize = 5, intervalType = INTERVAL;
    private double[] level = { 0.5, 0.6, 0.7, 0.8, 0.9, 0.95 };
    private double selectedLevel = level[4], lowerEstimate, upperEstimate;
    private int[] sample;

    private JPanel toolbar2 = new JPanel();
    private JComboBox intervalJComboBox = new JComboBox();
    private JComboBox levelJComboBox = new JComboBox();
    private JLabel criticalLabel = new JLabel("Critical z = -1.645, 1.645");
    //Random Variables
    private RandomVariable x = new RandomVariable(new BernoulliDistribution(p), "X");
    private RandomVariable success = new RandomVariable(new BernoulliDistribution(
            selectedLevel), "I");
    private RandomVariable stdScore = new RandomVariable(new NormalDistribution(0,
            1));
    //Graphs
    private MeanEstimateGraph estimateGraph = new MeanEstimateGraph(x);
    private CriticalGraph stdScoreGraph = new CriticalGraph(stdScore);
    private RandomVariableGraph successGraph = new RandomVariableGraph(success);
    //Tables
    private RandomVariableTable successTable = new RandomVariableTable(success);
    private JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public ProportionEstimateExperiment() {
        setName("Proportion Estimate Experiment");
        //Listeners
        createValueSetter("p", 5, 95, 0.01);
        createValueSetter("n", Distribution.DISCRETE, 5, 100, sampleSize);
        successGraph.showMoments(0);
        successTable.showMoments(0);
        stdScoreGraph.setCriticalValues(lowerCritical, upperCritical);
        //JComboBoxs
        intervalJComboBox.addItem("Two-sided");
        intervalJComboBox.addItem("Lower Bound");
        intervalJComboBox.addItem("Upper Bound");
        levelJComboBox.addItem("Confidence = 0.50");
        levelJComboBox.addItem("Confidence = 0.60");
        levelJComboBox.addItem("Confidence = 0.70");
        levelJComboBox.addItem("Confidence = 0.80");
        levelJComboBox.addItem("Confidence = 0.90");
        levelJComboBox.addItem("Confidence = 0.95");
        intervalJComboBox.addItemListener(this);
        levelJComboBox.addItemListener(this);

        toolbar2.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar2.add(levelJComboBox);
        toolbar2.add(intervalJComboBox);
        toolbar2.add(criticalLabel);

        addToolbar(toolbar2);
        //Construct graph panel
        addGraph(estimateGraph);
        addGraph(stdScoreGraph);
        addGraph(successGraph);
        //Construct table panel
        addTable(sampleTable);
        sampleTable.setEditable(false);
        addTable(successTable);
    }

    /** This method resets the exeperiment. */
    public void reset() {
        super.reset();
        x.reset();
        stdScore.reset();
        success.reset();
        sample = new int[sampleSize];
        estimateGraph.repaint();
        stdScoreGraph.repaint();
        successGraph.repaint();
        sampleTable.setText("Sample");
        getRecordTable().append("\tL\tR\tZ\tI");
        successTable.update();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        double sampleMean, stdError;
        super.doExperiment();
        x.reset();
        for (int i = 0; i < sampleSize; i++)
            sample[i] = (int) x.simulate();
        sampleMean = x.getIntervalData().getMean();
        stdError = Math.sqrt(sampleMean * (1 - sampleMean) / sampleSize);
        stdScore.reset();
        stdScore.setValue((sampleMean - x.getDistribution().getMean()) / stdError);
        lowerEstimate = sampleMean - upperCritical * stdError;
        upperEstimate = sampleMean - lowerCritical * stdError;
        if (lowerEstimate < p & p < upperEstimate) success.setValue(1);
        else success.setValue(0);
    }

    /**
     * This method handles the choice events associated with changing the
     * interval type of the confidence level.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == intervalJComboBox) {
            intervalType = intervalJComboBox.getSelectedIndex();
            setParameters();
        } else if (event.getSource() == levelJComboBox) {
            selectedLevel = level[levelJComboBox.getSelectedIndex()];
            success = new RandomVariable(new BernoulliDistribution(selectedLevel),
                    "I");
            successGraph.setRandomVariable(success);
            successTable.setRandomVariable(success);
            setParameters();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events associated with changing the sample
     * size or the success parameter.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(1)) {
            sampleSize = getValueSetter(1).getValueAsInt();
            reset();
        } else if (arg == getValueSetter(0)) {
            p = getValueSetter(0).getValue();
            x = new RandomVariable(new BernoulliDistribution(p), "X");
            estimateGraph.setRandomVariable(x);
            reset();
        }
    }

    /** This method updates the experiment, */
    public void update() {
        super.update();
        //Graphs
        estimateGraph.setEstimates(lowerEstimate, upperEstimate);
        estimateGraph.repaint();
        stdScoreGraph.repaint();
        successGraph.repaint();
        //Tables
        String sampleText = "Sample";
        for (int i = 0; i < sampleSize; i++) {
            sampleText = sampleText + "\n" + (i + 1) + "\t" + sample[i];
        }
        sampleTable.setText(sampleText);
        getRecordTable().append(
                "\t" + format(lowerEstimate) + "\t" + format(upperEstimate) + "\t"
                        + format(stdScore.getValue()) + "\t"
                        + format(success.getValue()));
        successTable.update();
    }

    /** This method sets the parameters: the upper and lower critical values. */
    public void setParameters() {
        switch (intervalType) {
        case INTERVAL:
            upperCritical = stdScore.getDistribution().getQuantile(
                    0.5 * (1 + selectedLevel));
            lowerCritical = -upperCritical;
            criticalLabel.setText("Critical z = +-" + format(upperCritical));
            break;
        case LOWER_BOUND:
            upperCritical = stdScore.getDistribution().getQuantile(selectedLevel);
            lowerCritical = Double.NEGATIVE_INFINITY;
            criticalLabel.setText("Critical z = " + format(upperCritical));
            break;
        case UPPER_BOUND:
            lowerCritical = -stdScore.getDistribution().getQuantile(selectedLevel);
            upperCritical = Double.POSITIVE_INFINITY;
            criticalLabel.setText("Critical z = " + format(lowerCritical));
            break;
        }
        stdScoreGraph.setCriticalValues(lowerCritical, upperCritical);
        reset();
    }
}
