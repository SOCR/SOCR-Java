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
 * This class models the interval estimate experiment for the variance in the
 * standard normal model.
 */
public class VarianceEstimateExperiment extends Experiment {
    //Constants
    public final static int INTERVAL = 0, LOWER_BOUND = 1, UPPER_BOUND = 2;
    //Variables
    private int distType = 0, sampleSize = 10, intervalType = INTERVAL;
    private boolean muKnown = true;
    private double lowerEstimate, upperEstimate, lowerCritical, upperCritical;
    private double[] level = { 0.5, 0.6, 0.7, 0.8, 0.9, 0.95 }, sample;
    private double selectedLevel = level[4];
    //Objects
    private JPanel toolbar1 = new JPanel();
    private JPanel toolbar2 = new JPanel();
    private JComboBox distJComboBox = new JComboBox();
    private JComboBox intervalJComboBox = new JComboBox();
    private JComboBox levelJComboBox = new JComboBox();
    private JComboBox muJComboBox = new JComboBox();
    private JLabel criticalLabel = new JLabel("Critical v = 3.33, 16.92");
    //Random Variables
    private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
    private RandomVariable success = new RandomVariable(new BernoulliDistribution(
            selectedLevel), "I");
    private RandomVariable chiSquareRV = new RandomVariable(
            new ChiSquareDistribution(9));
    //Graphs
    private VarianceEstimateGraph estimateGraph = new VarianceEstimateGraph(x);
    private CriticalGraph chiSquareGraph = new CriticalGraph(chiSquareRV);
    private RandomVariableGraph successGraph = new RandomVariableGraph(success);
    //Tables
    private RandomVariableTable successTable = new RandomVariableTable(success);
    private JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public VarianceEstimateExperiment() {
        setName("Variance Estimate Experiment");
        createValueSetter("mu", Distribution.CONTINUOUS, -5, 5, 0);
        createValueSetter("sigma", 5, 50, 0.1);
        createValueSetter("n", Distribution.DISCRETE, 5, 100, sampleSize);
        successGraph.showMoments(0);
        successTable.showMoments(0);
        //JComboBoxs
        distJComboBox.addItem("Normal");
        distJComboBox.addItem("Gamma");
        distJComboBox.addItem("Uniform");
        intervalJComboBox.addItem("Two-sided");
        intervalJComboBox.addItem("Lower Bound");
        intervalJComboBox.addItem("Upper Bound");
        levelJComboBox.addItem("Confidence = 0.50");
        levelJComboBox.addItem("Confidence = 0.60");
        levelJComboBox.addItem("Confidence = 0.70");
        levelJComboBox.addItem("Confidence = 0.80");
        levelJComboBox.addItem("Confidence = 0.90");
        levelJComboBox.addItem("Confidence = 0.95");
        levelJComboBox.setSelectedIndex(4);
        muJComboBox.addItem("mu Known");
        muJComboBox.addItem("mu Unknown");
        distJComboBox.addItemListener(this);
        intervalJComboBox.addItemListener(this);
        levelJComboBox.addItemListener(this);
        muJComboBox.addItemListener(this);

        //Construct toolbars
        toolbar1.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar1.add(distJComboBox);
        toolbar1.add(criticalLabel);
        toolbar2.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar2.add(levelJComboBox);
        toolbar2.add(intervalJComboBox);
        toolbar2.add(muJComboBox);
        addToolbar(toolbar1);
        addToolbar(toolbar2);
        //Construct graph panel
        addGraph(estimateGraph);
        addGraph(chiSquareGraph);
        addGraph(successGraph);
        //Construct table panel
        addTable(sampleTable);
        sampleTable.setEditable(false);
        addTable(successTable);
//        setParameters();
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        x.reset();
        chiSquareRV.reset();
        success.reset();
        sample = new double[sampleSize];
        estimateGraph.repaint();
        chiSquareGraph.repaint();
        successGraph.repaint();
        sampleTable.setText("Sample");
        getRecordTable().append("\tL\tR\tV\tI");
        successTable.update();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        double mean, variance, sumSquares;
        super.doExperiment();
        x.reset();
        for (int i = 0; i < sampleSize; i++)
            sample[i] = x.simulate();
        if (muKnown) mean = x.getDistribution().getMean();
        else mean = x.getIntervalData().getMean();
        variance = x.getDistribution().getVariance();
        sumSquares = 0;
        for (int i = 0; i < sampleSize; i++)
            sumSquares = sumSquares + (sample[i] - mean) * (sample[i] - mean);
        chiSquareRV.reset();
        chiSquareRV.setValue(sumSquares / variance);
        lowerEstimate = sumSquares / upperCritical;
        upperEstimate = sumSquares / lowerCritical;
        if (lowerEstimate < variance & variance < upperEstimate) success
                .setValue(1);
        else success.setValue(0);
    }

    /**
     * This method handles the choice events associated with changing
     * distribution type, interval type, confidence level, or the state of
     * knowledge of mu.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {

            distType = distJComboBox.getSelectedIndex();
            switch (distType) {

            case 0: //Normal
                getValueSetter(0).setRange(-5, 5, 0);
                getValueSetter(1).setRange(5, 50, 10);
                break;
            case 1: //Gamma
                getValueSetter(0).setRange(1, 5, 1);
                getValueSetter(1).setRange(5, 50, 10);
                break;
            case 2: //Uniform
                getValueSetter(0).setRange(-5, 5, 0);
                getValueSetter(1).setRange(10, 100, 1);
                break;
            }
            setDistribution();
        } else if (event.getSource() == intervalJComboBox) {
            intervalType = intervalJComboBox.getSelectedIndex();
            setParameters();
        } else if (event.getSource() == levelJComboBox) {
            selectedLevel = level[levelJComboBox.getSelectedIndex()];
            success = new RandomVariable(new BernoulliDistribution(selectedLevel),
                    "I");
            successGraph.setRandomVariable(success);
            successTable.setRandomVariable(success);
            setParameters();
        } else if (event.getSource() == muJComboBox) {
            muKnown = (muJComboBox.getSelectedIndex() == 0);
            setParameters();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events associated with changing the sample
     * size or the distribution parameters.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(2)) {
            sampleSize = getValueSetter(2).getValueAsInt();
            setParameters();
        } else setDistribution();

    }

    /** This method updates the displays. */
    public void update() {
        super.update();
        //Graphs
        estimateGraph.setEstimates(Math.sqrt(lowerEstimate), Math
                .sqrt(upperEstimate));
        estimateGraph.repaint();
        chiSquareGraph.repaint();
        successGraph.repaint();
        //Tables
        String sampleText = "Sample";
        for (int i = 0; i < sampleSize; i++)
            sampleText = sampleText + "\n" + (i + 1) + "\t" + format(sample[i]);
        sampleTable.setText(sampleText);
        getRecordTable().append(
                "\t" + format(lowerEstimate) + "\t" + format(upperEstimate) + "\t"
                        + format(chiSquareRV.getValue()) + "\t"
                        + format(success.getValue()));
        successTable.update();
    }

    /**
     * This method computes the upper and lower critical values when the
     * distribution or parameters have changed.
     */
    public void setParameters() {
        double alpha = 1 - selectedLevel;
        int df;
        switch (intervalType) {
        case INTERVAL:
            upperCritical = chiSquareRV.getDistribution()
                    .getQuantile(1 - alpha / 2);
            lowerCritical = chiSquareRV.getDistribution().getQuantile(alpha / 2);
            criticalLabel.setText("Critical v = " + format(lowerCritical) + ", "
                    + format(upperCritical));
            break;
        case LOWER_BOUND:
            upperCritical = chiSquareRV.getDistribution().getQuantile(1 - alpha);
            lowerCritical = +0.0;
            criticalLabel.setText("Critical v = " + format(upperCritical));
            break;
        case UPPER_BOUND:
            upperCritical = Double.POSITIVE_INFINITY;
            lowerCritical = chiSquareRV.getDistribution().getQuantile(alpha);
            criticalLabel.setText("Critical v = " + format(lowerCritical));
            break;
        }
        if (muKnown) df = sampleSize;
        else df = sampleSize - 1;
        chiSquareRV = new RandomVariable(new ChiSquareDistribution(df));
        chiSquareGraph.setRandomVariable(chiSquareRV);
        chiSquareGraph.setRandomVariable(chiSquareRV);
        chiSquareGraph.setCriticalValues(lowerCritical, upperCritical);
        reset();
    }

    /**
     * This method sets the scrollbars to the appropriate ranges when the
     * distribution type changes.
     */
    public void setDistribution() {
        switch (distType) {
        case 0: //Normal
            double mu = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("mu");
            double sigma = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("sigma");
            x = new RandomVariable(new NormalDistribution(mu, sigma), "X");
            break;
        case 1: //Gamma
            double shape = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("shape");
            double scale = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("scale");
            x = new RandomVariable(new GammaDistribution(shape, scale), "X");
            break;
        case 2: //Uniform
            double leftPoint = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("a");
            double length = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("l");
            x = new RandomVariable(new ContinuousUniformDistribution(leftPoint,
                    leftPoint + length), "X");
            break;
        }

        estimateGraph.setRandomVariable(x);
        reset();
    }
}

