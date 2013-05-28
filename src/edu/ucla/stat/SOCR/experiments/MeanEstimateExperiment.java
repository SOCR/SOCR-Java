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
 * This class models the interval estimation experiment in the standard normal
 * model.
 */
public class MeanEstimateExperiment extends Experiment {
    //Constants
    public final static int INTERVAL = 0, LOWER_BOUND = 1, UPPER_BOUND = 2;
    public final static int NORMAL = 0, STUDENT = 1;
    //Variables
    private int distType = 0, sampleSize = 5, intervalType = INTERVAL;
    private boolean sigmaKnown = true;
    private double[] level = { 0.5, 0.6, 0.7, 0.8, 0.9, 0.95 }, sample;
    private double selectedLevel = level[4], lowerEstimate, upperEstimate,
            lowerCritical, upperCritical;
    private String stdScoreName = "Z";
    //Objects
    private JPanel toolbar1 = new JPanel(), toolbar2 = new JPanel();
    private JComboBox distJComboBox = new JComboBox(),
            intervalJComboBox = new JComboBox();
    private JComboBox levelJComboBox = new JComboBox(),
            pivotJComboBox = new JComboBox();
    private JLabel criticalLabel = new JLabel("Critical z = -1.645, 1.645");
    //Random Variables
    private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
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

    /**
     * This method initializes the experiment, including the toolbars with
     * scrollbars and labels for changing parameters, choices for the sampling
     * distribution, interval type, and confidence level, graphs and tables for
     * the sampling variable, the standard score variable, and the success
     * variable.
     */
    public MeanEstimateExperiment() {
        setName("Mean Estimate Experiment");
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.ucla.stat.SOCR.core.Experiment#initialize()
     */
    public void initialize() {
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
        pivotJComboBox.addItem("sigma Known");
        pivotJComboBox.addItem("sigma Unknown");
        //Toolbars
        toolbar1.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar1.add(distJComboBox);
        toolbar1.add(levelJComboBox);
        toolbar1.add(pivotJComboBox);
        toolbar1.add(intervalJComboBox);
        toolbar1.add(criticalLabel);

        addToolbar(toolbar1);
        addToolbar(toolbar2);
        //Construct graph panel
        addGraph(estimateGraph);
        addGraph(stdScoreGraph);
        addGraph(successGraph);
        //Construct table panel
        addTable(sampleTable);
        addTable(successTable);
        distJComboBox.addItemListener(this);
        intervalJComboBox.addItemListener(this);
        levelJComboBox.addItemListener(this);
        pivotJComboBox.addItemListener(this);
    }

    /**
     * This method resets the experiment, including the sampling variable,
     * standard score variable and success variable, and the corresponding
     * graphs and tables.
     */
    public void reset() {
        super.reset();
        //Random variables
        x.reset();
        stdScore.reset();
        success.reset();
        //Initialize sample array
        sample = new double[sampleSize];
        //Graphs
        estimateGraph.repaint();
        stdScoreGraph.repaint();
        successGraph.repaint();
        //Tables
        sampleTable.setText("Sample");
        if (sigmaKnown) getRecordTable().append("\tL\tR\tZ\tI");
        else getRecordTable().append("\tL\tR\tT\tI");
        successTable.update();
    }

    /**
     * This method defines the experiment. The sample from the sampling
     * distribution is simulated, the confidence interval is computed.
     */
    public void doExperiment() {
        double stdError, sampleMean, mean;
        super.doExperiment();
        x.reset();
        //Sample
        for (int i = 0; i < sampleSize; i++)
            sample[i] = x.simulate();
        //Compute interval estimate
        sampleMean = x.getIntervalData().getMean();
        mean = x.getDistribution().getMean();
        if (sigmaKnown) stdError = x.getDistribution().getSD()
                / Math.sqrt(sampleSize);
        else stdError = x.getIntervalData().getSD() / Math.sqrt(sampleSize);
        stdScore.reset();
        stdScore.setValue((sampleMean - mean) / stdError);
        lowerEstimate = sampleMean - upperCritical * stdError;
        upperEstimate = sampleMean - lowerCritical * stdError;
        //Determine success
        if (lowerEstimate < mean & mean < upperEstimate) success.setValue(1);
        else success.setValue(0);
    }

    //JComboBox events
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {
            distType = distJComboBox.getSelectedIndex();
            switch (distType) {
            case 0: //Normal
                getValueSetter(0).setRange(-5, 5, 0);
                getValueSetter(1).setRange(1, 5, 1);
                break;
            case 1: //Gamma
                getValueSetter(0).setRange(1, 5, 1);
                getValueSetter(1).setRange(1, 5, 1);
                break;
            case 2: //Uniform
                getValueSetter(0).setRange(-5, 5, 0);
                getValueSetter(1).setRange(1, 10, 1);
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
        } else if (event.getSource() == pivotJComboBox) {
            sigmaKnown = (pivotJComboBox.getSelectedIndex() == 0);
            setParameters();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events associated with changing the
     * distribution parameters.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(2)) {
            sampleSize = getValueSetter(2).getValueAsInt();
            setParameters();
        } else setDistribution();
    }

    /** This method updates the experiment, including the graphs and tables. */
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
            sampleText = sampleText + "\n" + (i + 1) + "\t" + format(sample[i]);
        }
        sampleTable.setText(sampleText);
        getRecordTable().append(
                "\t" + format(lowerEstimate) + "\t" + format(upperEstimate) + "\t"
                        + format(stdScore.getValue()) + "\t"
                        + format(success.getValue()));
        successTable.update();
    }

    /** This method sets the parameters. */
    public void setParameters() {
        if (sigmaKnown) {
            stdScore = new RandomVariable(new NormalDistribution(0, 1));
            stdScoreName = "z";
        } else {
            stdScore = new RandomVariable(new StudentDistribution(sampleSize - 1));
            stdScoreName = "t";
        }
        switch (intervalType) {
        case INTERVAL:
            upperCritical = stdScore.getDistribution().getQuantile(
                    0.5 * (1 + selectedLevel));
            lowerCritical = -upperCritical;
            criticalLabel.setText("Critical " + stdScoreName + " = +-"
                    + format(upperCritical));
            break;
        case LOWER_BOUND:
            upperCritical = stdScore.getDistribution().getQuantile(selectedLevel);
            lowerCritical = Double.NEGATIVE_INFINITY;
            criticalLabel.setText("Critical " + stdScoreName + " = "
                    + format(upperCritical));
            break;
        case UPPER_BOUND:
            lowerCritical = -stdScore.getDistribution().getQuantile(selectedLevel);
            upperCritical = Double.POSITIVE_INFINITY;
            criticalLabel.setText("Critical " + stdScoreName + " = "
                    + format(lowerCritical));
            break;
        }
        stdScoreGraph.setRandomVariable(stdScore);
        stdScoreGraph.setCriticalValues(lowerCritical, upperCritical);
        reset();
    }

    /** This method sets the distribution. */
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

