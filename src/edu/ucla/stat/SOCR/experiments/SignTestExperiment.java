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

/** This class models the sign test experiment. */
public class SignTestExperiment extends Experiment {
    //Variables
    private int sampleSize = 10, distType = 0;
    private double p = 0.5, median = 0, testMedian = 0;
    private double lowerCritical = 2, upperCritical = 8, level = 0.1;
    private double[] sample = new double[sampleSize];
    private double[] levels = { 0.1, 0.05, 0.025, 0.01, 0.001 };
    //Panels
    private JPanel toolbar1 = new JPanel();
    private JPanel toolbar2 = new JPanel();
    private JLabel criticalJLabel = new JLabel("c = 2, d = 8  ");
    private JLabel medianJLabel = new JLabel("m = 0.0 ");
    //JComboBoxs
    private JComboBox levelJComboBox = new JComboBox();
    private JComboBox distJComboBox = new JComboBox();
    //Random Variables
    private IntervalData reject = new IntervalData(-0.5, 1.5, 1, "J");
    private RandomVariable sampleVariable = new RandomVariable(
            new NormalDistribution(0, 1), "X");
    private RandomVariable signVariable = new RandomVariable(
            new BinomialDistribution(sampleSize, 0.5), "U");
    //Graphs
    private Histogram rejectGraph = new Histogram(reject, 0);
    private CriticalGraph signGraph = new CriticalGraph(signVariable);
    private MedianGraph sampleGraph = new MedianGraph(sampleVariable, testMedian);
    //Tables
    private DataTable rejectTable = new DataTable(reject, 0);
    JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public SignTestExperiment() {
        setName("Sign Test Experiment");
        createValueSetter("mu", -50, 50, 0.1);
        createValueSetter("sigma", 5, 50, 10, 0.1);
        createValueSetter("n =", Distribution.DISCRETE, 1, 50, sampleSize);
        createValueSetter("m0", 1, 9, 0.1);

        for (int i = 0; i < 5; i++)
            levelJComboBox.addItem("Level " + levels[i]);
        distJComboBox.addItem("Normal");
        distJComboBox.addItem("Uniform");
        distJComboBox.addItem("Gamma");

        distJComboBox.addItemListener(this);
        levelJComboBox.addItemListener(this);

        //Toolbars
        toolbar1.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar1.add(distJComboBox);
        toolbar1.add(medianJLabel);
        toolbar2.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar2.add(levelJComboBox);
        toolbar2.add(criticalJLabel);
        addToolbar(toolbar1);
        addToolbar(toolbar2);
        //Graphs
        signGraph.setCriticalValues(2, 8);
        rejectGraph.showSummaryStats(0);
        addGraph(sampleGraph);
        addGraph(signGraph);
        addGraph(rejectGraph);

        rejectTable.showSummaryStats(0);
        addTable(sampleTable);
        addTable(rejectTable);
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        //Variables
        sampleVariable.reset();
        signVariable.reset();
        reject.reset();
        //Graphs
        signGraph.repaint();
        rejectGraph.repaint();
        sampleGraph.repaint();
        //Tables
        sampleTable.setText("Sample");
        getRecordTable().append("\tN\tJ");
        rejectTable.update();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        int signCount = 0;
        super.doExperiment();
        sampleVariable.reset();
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = sampleVariable.simulate();
            if (sample[i] > testMedian) signCount++;
        }
        signVariable.reset();
        signVariable.setValue(signCount);
        if (signCount < lowerCritical | signCount > upperCritical) reject
                .setValue(1);
        else reject.setValue(0);
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

    /** This method updates the experiment. */
    public void update() {
        super.update();
        //Graphs
        sampleGraph.repaint();
        signGraph.repaint();
        rejectGraph.repaint();
        //Tables
        String sampleText = "Sample";
        for (int i = 0; i < sampleSize; i++) {
            sampleText = sampleText + "\n" + (i + 1) + "\t" + format(sample[i]);
        }
        sampleTable.setText(sampleText);
        getRecordTable().append(
                "\t" + format(signVariable.getValue()) + "\t"
                        + format(reject.getValue()));
        rejectTable.update();
    }

    /**
     * This method handles the choice events associated with changes in the
     * sampling distribution.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {
            distType = distJComboBox.getSelectedIndex();
            switch (distType) {
            case 0: //Normal
                getValueSetter(0).setRange(-50, 50, 0);
                getValueSetter(1).setRange(5, 50);
                getValueSetter(0).setTitle("mu=");
                getValueSetter(1).setTitle("Sigma=");
                break;
            case 1: //Uniform
                getValueSetter(0).setRange(0, 100);
                getValueSetter(1).setRange(1, 100);
                getValueSetter(0).setTitle("a=");
                getValueSetter(1).setTitle("l=");
                break;
            case 2: //Gamma
                getValueSetter(0).setRange(10, 50);
                getValueSetter(1).setRange(5, 50);
                getValueSetter(0).setTitle("shape=");
                getValueSetter(1).setTitle("scale=");
                break;
            }
            setDistribution();
        } else if (event.getSource() == levelJComboBox) {
            level = levels[levelJComboBox.getSelectedIndex()];
            setCriticalValues();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events associated with changes in the
     * sample size or changes in the parameters of the sampling distribution.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0) || arg == getValueSetter(1)) {
            setDistribution();
        } else if (arg == getValueSetter(2)) {
            sampleSize = getValueSetter(2).getValueAsInt();
            sample = new double[sampleSize];
            signVariable = new RandomVariable(new BinomialDistribution(sampleSize,
                    0.5), "U");
            signGraph.setRandomVariable(signVariable);
            setCriticalValues();
            reset();
        } else if (arg == getValueSetter(3)) {
            p = getValueSetter(3).getValue();
            testMedian = sampleVariable.getDistribution().getQuantile(p);
            sampleGraph.setTestMedian(testMedian);
             reset();
        }
    }

    /** This method sets the distribution when the parameters have changed. */
    public void setDistribution() {
        double mu, sigma, shape, scale, a, l;
        switch (distType) {
        case 0: //Normal
            mu = getValueSetter(0).getValue();
            sigma = getValueSetter(1).getValue();
            sampleVariable = new RandomVariable(new NormalDistribution(mu, sigma),
                    "X");
            break;
        case 1: //Uniform
            a = getValueSetter(0).getValue();
            l = getValueSetter(1).getValue();
            sampleVariable = new RandomVariable(new ContinuousUniformDistribution(
                    a, a + l), "X");
            break;
        case 2: //Gamma
            shape = getValueSetter(0).getValue();
            scale = getValueSetter(1).getValue();
            sampleVariable = new RandomVariable(
                    new GammaDistribution(shape, scale), "X");
            break;
        }
        median = sampleVariable.getDistribution().getMedian();
        medianJLabel.setText("m = " + format(median));
        testMedian = median;
//        testMedianJLabel.setText("m0 = " + format(testMedian) + ", p = 0.5");
//        testMedianScroll.setParameter(0.5);
        getValueSetter(3).setValue(5);
        sampleGraph.setRandomVariable(sampleVariable);
        sampleGraph.setTestMedian(testMedian);
        reset();
    }

    public void setCriticalValues() {
        lowerCritical = signVariable.getDistribution().getQuantile(level / 2);
        upperCritical = signVariable.getDistribution().getQuantile(1 - level / 2);
        criticalJLabel.setText("c = " + format(lowerCritical) + ", d = "
                + format(upperCritical));
        signGraph.setCriticalValues(lowerCritical, upperCritical);
        reset();
    }
}
