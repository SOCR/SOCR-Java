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

/** This class models the probability plot experiment. */
public class ProbabilityPlotExperiment extends Experiment {
    //Variables
    private int sampleSize = 5, distType = 0, testType = 0;
    private double[] quantiles = new double[sampleSize];
    private double[] orderStatistics = new double[sampleSize];

    private JPanel toolbar2 = new JPanel();
    private JComboBox samplingJComboBox = new JComboBox();
    private JComboBox testJComboBox = new JComboBox();
    private RandomVariable basicVariable = new RandomVariable(
            new NormalDistribution(0, 1), "X");
    private RandomVariable testVariable = new RandomVariable(
            new NormalDistribution(0, 1), "Y");
    private RandomVariableGraph basicGraph = new RandomVariableGraph(basicVariable);
    private RandomVariableGraph testGraph = new RandomVariableGraph(testVariable);
    private ProbabilityPlot probabilityPlot = new ProbabilityPlot(-4.0, 4.0, -4.0,
            4.0);
    private JTextArea testQuantilesTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public ProbabilityPlotExperiment() {
        setName("Probability Plot Experiment");
        //Listeners
        samplingJComboBox.addItemListener(this);
        testJComboBox.addItemListener(this);
        createValueSetter("n = ", Distribution.DISCRETE, 5, 25, sampleSize);
        createValueSetter("mu = ", Distribution.CONTINUOUS, -5, 5);
        createValueSetter("sigma = ", 1, 50, 0.1);

        //Graphs and tables
        basicGraph.showMoments(0);
        testGraph.showMoments(0);
        basicGraph.setMargins(35, 20, 20, 20);
        testGraph.setMargins(35, 20, 20, 20);
        testQuantilesTable.setEditable(false);
        //JComboBoxs
        samplingJComboBox.addItem("Sampling: Normal");
        samplingJComboBox.addItem("Sampling: Uniform");
        samplingJComboBox.addItem("Sampling: Gamma");
        testJComboBox.addItem("Test: Normal (0, 1)");
        testJComboBox.addItem("Test: Uniform (0, 1)");
        testJComboBox.addItem("Test: Exponential (1)");
        toolbar2.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar2.add(samplingJComboBox);
        toolbar2.add(testJComboBox);
        addToolbar(toolbar2);
        //Graphs
        addGraph(basicGraph);
        addGraph(testGraph);
        addGraph(probabilityPlot);
        //Tables
        addTable(testQuantilesTable);
        setQuantiles();
        reset();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        super.doExperiment();
        double x;
        basicVariable.reset();
        boolean smallest;
        for (int i = 0; i < sampleSize; i++) {
            x = basicVariable.simulate();
            smallest = true;
            for (int j = i - 1; j >= 0; j = j - 1) {
                if (orderStatistics[j] <= x) {
                    orderStatistics[j + 1] = x;
                    smallest = false;
                    break;
                }
                orderStatistics[j + 1] = orderStatistics[j];
            }
            if (smallest) orderStatistics[0] = x;
        }
        probabilityPlot.setStatistics(orderStatistics);
    }

    /** This method updates the experiment. */
    public void update() {
        super.update();
        String orderStatisticsText = "Order Statistics";
        for (int i = 0; i < sampleSize; i++) {
            orderStatisticsText = orderStatisticsText + "\n" + (i + 1) + "\t"
                    + format(orderStatistics[i]);
        }
        getRecordTable().setText(orderStatisticsText);
        basicGraph.repaint();
        probabilityPlot.repaint();
    }

    /**
     * This method handles the scroll events associated with changing the
     * parameters of the sampling distribution or the sample size.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            sampleSize = getValueSetter(0).getValueAsInt();
            orderStatistics = new double[sampleSize];
            quantiles = new double[sampleSize];
            setQuantiles();
            reset();
        } else {
            setDistribution();
        }
    }

    /**
     * This method handles the choice events associated with changing the
     * sampling distribution or the test distribution.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == testJComboBox) {
            testType = testJComboBox.getSelectedIndex();
            switch (testType) {
            case 0: //Normal
                testVariable.setDistribution(new NormalDistribution(0, 1));
                break;
            case 1: //Uniform
                testVariable
                        .setDistribution(new ContinuousUniformDistribution(0, 1));
                break;
            case 2: //Exponential
                testVariable.setDistribution(new ExponentialDistribution(1));
                break;
            }
            testGraph.reset();
            Domain testDomain = testVariable.getDistribution().getDomain(), basicDomain = basicVariable
                    .getDistribution().getDomain();
            probabilityPlot.setScale(testDomain.getLowerBound(), testDomain
                    .getUpperBound(), basicDomain.getLowerValue(), basicDomain
                    .getUpperValue());
            probabilityPlot.repaint();
            setQuantiles();
        } else if (event.getSource() == samplingJComboBox) {
            distType = samplingJComboBox.getSelectedIndex();
            switch (distType) {
            case 0: //Normal
                getValueSetter(1).setRange(-5, 5, 0);
                getValueSetter(2).setRange(0, 5, 1);
                break;
            case 1: //Uniform
                getValueSetter(1).setRange(0, 10, 0);
                getValueSetter(2).setRange(1, 10, 1);
                break;
            case 2: //Gamma
                getValueSetter(1).setRange(1, 5, 1);
                getValueSetter(2).setRange(0, 5, 1);
                break;
            }
            setDistribution();
        } else super.itemStateChanged(event);
    }

    /** This method sets the quantiles. */
    public void setQuantiles() {
        double p;
        testVariable.reset();
        testGraph.reset();
        String quantileText = "Quantiles";
        for (int i = 0; i < sampleSize; i++) {
            p = (double) (i + 1) / (sampleSize + 1);
            quantiles[i] = testVariable.getDistribution().getQuantile(p);
            quantileText = quantileText + "\n" + (i + 1) + "\t"
                    + format(quantiles[i]);
            testVariable.setValue(quantiles[i]);
        }
        testQuantilesTable.setText(quantileText);
        testGraph.repaint();
        probabilityPlot.setQuantiles(quantiles);
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        basicGraph.reset();
        getRecordTable().setText("Order Statistics");
        probabilityPlot.reset();
    }

    /** This method sets the sampling distribution. */
    public void setDistribution() {
        switch (distType) {
        case 0: //Normal
            double mu = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("mu");
            double sigma = getValueSetter(2).getValue();
            getValueSetter(2).setTitle("sigma");
            basicVariable.setDistribution(new NormalDistribution(mu, sigma));
            break;
        case 1: //Uniform
            double a = getValueSetter(1).getValue();
            double l = getValueSetter(2).getValue();
            getValueSetter(1).setTitle("a ");
            getValueSetter(2).setTitle("l");
            basicVariable.setDistribution(new ContinuousUniformDistribution(a, a
                    + l));
            break;
        case 2: //Gamma
            double shape = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("shape");
            double scale = getValueSetter(2).getValue();
            getValueSetter(2).setTitle("scale");
            basicVariable.setDistribution(new GammaDistribution(shape, scale));
            break;
        }
        Domain testDomain = testVariable.getDistribution().getDomain(), basicDomain = basicVariable
                .getDistribution().getDomain();
        probabilityPlot.setScale(testDomain.getLowerBound(), testDomain
                .getUpperBound(), basicDomain.getLowerBound(), basicDomain
                .getUpperBound());
        reset();
    }
}

