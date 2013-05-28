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
 * This class models the hypothesis testing experiment for the variance in the
 * standard normal model.
 */
public class VarianceTestExperiment extends Experiment {
    //Constants
    public final static int TWO_SIDED = 0, LEFT = 1, RIGHT = 2;
    //Variables
    private int distType = 0, sampleSize = 10, testType = TWO_SIDED, df;
    private boolean muKnown = true;
    private double testStdDev = 1, lowerCritical = 3.33, upperCritical = 16.92;
    private double selectedLevel = 0.1, sampleVariance;
    private double[] level = { 0.5, 0.4, 0.3, 0.2, 0.1, 0.05 }, sample;
    //Objects
    private JPanel toolbar1 = new JPanel();
    private JPanel toolbar2 = new JPanel();
    private JComboBox distJComboBox = new JComboBox();
    private JComboBox testJComboBox = new JComboBox();
    private JComboBox levelJComboBox = new JComboBox();
    private JComboBox muJComboBox = new JComboBox();
    private JLabel criticalLabel = new JLabel("Critical v = 3.33, 16.92");
    //Random Variables
    private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
    private IntervalData reject = new IntervalData(-0.5, 1.5, 1, "I");
    private RandomVariable chiSquareRV = new RandomVariable(
            new ChiSquareDistribution(9));
    //Graphs
    private VarianceTestGraph testGraph = new VarianceTestGraph(x, 1);
    private CriticalGraph chiSquareGraph = new CriticalGraph(chiSquareRV);
    private Histogram rejectGraph = new Histogram(reject, 0);
    //Tables
    private DataTable rejectTable = new DataTable(reject, 0);
    private JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public VarianceTestExperiment() {
        setName("Variance Test Experiment");
        createValueSetter("mu", Distribution.CONTINUOUS, 0, 20);
        createValueSetter("sigma", Distribution.CONTINUOUS, 1, 10);
        createValueSetter("n", Distribution.DISCRETE, 1, 20);
        createValueSetter("d0", Distribution.CONTINUOUS, 1, 20);

        rejectGraph.showSummaryStats(0);
        rejectTable.showSummaryStats(0);
        //JComboBoxs
        distJComboBox.addItem("Normal");
        distJComboBox.addItem("Gamma");
        distJComboBox.addItem("Uniform");
        testJComboBox.addItem("H0: d =d0");
        testJComboBox.addItem("H0: d >= d0");
        testJComboBox.addItem("H0: d <= d0");
        levelJComboBox.addItem("Level = 0.50");
        levelJComboBox.addItem("Level = 0.40");
        levelJComboBox.addItem("Level = 0.30");
        levelJComboBox.addItem("Level = 0.2");
        levelJComboBox.addItem("Level = 0.1");
        levelJComboBox.addItem("Level = 0.05");
        levelJComboBox.setSelectedIndex(4);
        muJComboBox.addItem("mu Known");
        muJComboBox.addItem("mu Unknown");
        //Event listeners
        distJComboBox.addItemListener(this);
        testJComboBox.addItemListener(this);
        levelJComboBox.addItemListener(this);
        muJComboBox.addItemListener(this);

        //Construct toolbars
        toolbar1.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar1.add(distJComboBox);
        toolbar2.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar2.add(levelJComboBox);
        toolbar2.add(testJComboBox);
        toolbar2.add(muJComboBox);
        toolbar2.add(criticalLabel);
        addToolbar(toolbar1);
        addToolbar(toolbar2);
        //Construct graph panel
        addGraph(testGraph);
        addGraph(chiSquareGraph);
        addGraph(rejectGraph);
        //Construct table panel
        addTable(sampleTable);
        sampleTable.setEditable(false);
        addTable(rejectTable);
//       setParameters();
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        x.reset();
        chiSquareRV.reset();
        reject.reset();
        sample = new double[sampleSize];
        testGraph.repaint();
        chiSquareGraph.repaint();
        rejectGraph.repaint();
        sampleTable.setText("Sample");
        getRecordTable().append("\tS2\tV\tI");
        rejectTable.update();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        double testStatistic;
        super.doExperiment();
        x.reset();
        for (int i = 0; i < sampleSize; i++)
            sample[i] = x.simulate();
        sampleVariance = x.getIntervalData().getVariance();
        testStatistic = df * sampleVariance / (testStdDev * testStdDev);
        chiSquareRV.reset();
        chiSquareRV.setValue(testStatistic);
        if (lowerCritical < testStatistic & testStatistic < upperCritical) reject
                .setValue(0);
        else reject.setValue(1);
    }

    /**
     * This method runs the experiment one time, playing a sound that depends on
     * the outcome.
     */
    public void step() {
        doExperiment();
        update();
        try {
            if (reject.getValue() == 1) play("sounds/1.au");
            else play("sounds/0.au");
        } catch (Exception e) {
            ;
        }
    }

    /**
     * This method handles the choice events associated with a change of
     * distribution type, test type, significance level, or state of knowledge
     * of mu.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {
            distType = distJComboBox.getSelectedIndex();
            switch (distType) {
            case 0: //Normal
                getValueSetter(0).setRange(0, 20);
                getValueSetter(1).setRange(1, 10);
                getValueSetter(0).setTitle("mu");
                getValueSetter(1).setTitle("sigma");
                break;
            case 1: //Gamma
                getValueSetter(0).setRange(2, 10);
                getValueSetter(1).setRange(1, 10);
                getValueSetter(0).setTitle("shape");
                getValueSetter(1).setTitle("scale");
                break;
            case 2: //Uniform
                getValueSetter(0).setRange(0, 10);
                getValueSetter(1).setRange(1, 10);
                getValueSetter(0).setTitle("a");
                getValueSetter(1).setTitle("l");
                break;
            }
            setDistribution();
        } else if (event.getSource() == testJComboBox) {
            testType = testJComboBox.getSelectedIndex();
            setParameters();
        } else if (event.getSource() == levelJComboBox) {
            selectedLevel = level[levelJComboBox.getSelectedIndex()];
            setParameters();
        } else if (event.getSource() == muJComboBox) {
            muKnown = (muJComboBox.getSelectedIndex() == 0);
            setParameters();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events associated with a change in sample
     * size or a change in the parameters of the distribution.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(2)) {
            sampleSize = getValueSetter(2).getValueAsInt();
            setParameters();
        } else if (arg == getValueSetter(3)) {
            testStdDev = x.getDistribution().getSD() * getValueSetter(3).getValue()
                    / 10;
            testGraph.setTestStdDev(testStdDev);
            reset();
        } else setDistribution();
    }

    /** This method updates the graphs and tables. */
    public void update() {
        super.update();
        //Graphs
        testGraph.repaint();
        chiSquareGraph.repaint();
        rejectGraph.repaint();
        //Tables
        String sampleText = "Sample";
        for (int i = 0; i < sampleSize; i++)
            sampleText = sampleText + "\n" + (i + 1) + "\t" + format(sample[i]);
        sampleTable.setText(sampleText);
        getRecordTable().append(
                "\t" + format(x.getIntervalData().getVariance()) + "\t"
                        + format(chiSquareRV.getValue()) + "\t"
                        + format(reject.getValue()));
        rejectTable.update();
    }

    /**
     * This method computes the upper and lower critical values and sets up the
     * graphs when the distribution changes.
     */
    public void setParameters() {
        switch (testType) {
        case TWO_SIDED:
            upperCritical = chiSquareRV.getDistribution().getQuantile(
                    1 - selectedLevel / 2);
            lowerCritical = chiSquareRV.getDistribution().getQuantile(
                    selectedLevel / 2);
            criticalLabel.setText("Critical v = " + format(lowerCritical) + ", "
                    + format(upperCritical));
            break;
        case LEFT:
            upperCritical = chiSquareRV.getDistribution().getQuantile(
                    1 - selectedLevel);
            lowerCritical = +0.0;
            criticalLabel.setText("Critical v = " + format(upperCritical));
            break;
        case RIGHT:
            lowerCritical = chiSquareRV.getDistribution()
                    .getQuantile(selectedLevel);
            upperCritical = Double.POSITIVE_INFINITY;
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
     * This method sets up the scroll bars and graphs when the type of
     * distribution changes.
     */
    public void setDistribution() {
        switch (distType) {
        case 0: //Normal
            double mu = (getValueSetter(0).getValue() - 10) / 2;
            double sigma = (getValueSetter(1).getValue()) / 2;
            x = new RandomVariable(new NormalDistribution(mu, sigma), "X");
            break;
        case 1: //Gamma
            double shape = (getValueSetter(0).getValue()) / 2;
            double scale = (getValueSetter(1).getValue()) / 2;
            x = new RandomVariable(new GammaDistribution(shape, scale), "X");
            break;
        case 2: //Uniform
            int endpoint = getValueSetter(0).getValueAsInt() - 5;
            int length = getValueSetter(1).getValueAsInt();
            x = new RandomVariable(new ContinuousUniformDistribution(endpoint,
                    endpoint + length), "X");
            break;
        }
        testGraph.setRandomVariable(x);
        testStdDev = x.getDistribution().getSD() * getValueSetter(3).getValue()
                / 10;
        testGraph.setTestStdDev(testStdDev);
        reset();
    }
}

