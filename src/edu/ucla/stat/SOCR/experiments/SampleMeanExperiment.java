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
 * This class models the sample mean experiment. A random sample of a specified
 * size is drawn from a specified distribution. The density of the sampling
 * distribution and of the distribution of the sample mean are shown.
 */
public class SampleMeanExperiment extends Experiment {
    //Variables
    private int sampleSize = 1, distType = 0;
    private double[] sample = new double[1];
    private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
    private RandomVariable xBar = new RandomVariable(new NormalDistribution(0, 1),
            "M");
    private JComboBox distJComboBox = new JComboBox();
    private RandomVariableGraph xGraph = new RandomVariableGraph(x);
    private RandomVariableGraph xBarGraph = new RandomVariableGraph(xBar);
    private RandomVariableTable xTable = new RandomVariableTable(x);
    private RandomVariableTable xBarTable = new RandomVariableTable(xBar);
    private JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public SampleMeanExperiment() {
        setName("Sample Mean Experiment");
        distJComboBox.addItem("Normal");
        distJComboBox.addItem("Gamma");
        distJComboBox.addItem("Binomial");
        distJComboBox.addItem("Poisson");
        distJComboBox.addItemListener(this);

        //Construct toolbar
        addTool(distJComboBox);
        createValueSetter("mu", -50, 50, 0.1);
        createValueSetter("sigma", 5, 50, 10, 0.1);
        createValueSetter("n1 =", Distribution.DISCRETE, 1, 200, sampleSize);
        addGraph(xGraph);
        addGraph(xBarGraph);
        //Construct table panel
        addTable(xTable);
        addTable(sampleTable);
        addTable(xBarTable);
    }

    /**
     * This method handles the choice events associated with changing the
     * sampling distribution.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {
            distType = distJComboBox.getSelectedIndex();
            switch (distType) {
            case 0: //Normal
                getValueSetter(0).setRange(-50, 50, 0);
                getValueSetter(1).setRange(5, 50);
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setTitle("mu = ");
                getValueSetter(1).setTitle("d = ");
                break;
            case 1: //Gamma
                getValueSetter(0).setRange(10, 50);
                getValueSetter(1).setRange(5, 50);
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setTitle("shape = ");
                getValueSetter(1).setTitle("scale = ");
                break;
            case 2: //Binomial
                getValueSetter(0).setRange(10, 100);
                getValueSetter(1).setRange(0, 10);
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setTitle("m = ");
                getValueSetter(1).setTitle("p = ");
                break;
            case 3: //Poisson
                getValueSetter(0).setRange(5, 50);
                getValueSetter(0).setTitle("c = ");
                getValueSetter(1).setEnabled(false);
                break;
            }
            setDistributions();
        }
    }

    /**
     * This method handles the scroll events associated with changes in the
     * sample size or the parameters of the sampling distribution.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(2)) {
            sampleSize = getValueSetter(2).getValueAsInt();
            sample = new double[sampleSize];
        }
        setDistributions();
    }

    /** This method resets the distribution. */
    public void reset() {
        super.reset();
        x.reset();
        xBar.reset();
        xTable.update();
        sampleTable.setText("Current Sample");
        xBarTable.update();
        getRecordTable().append("\tM\tS");
        xGraph.repaint();
        xBarGraph.repaint();
    }

    /**
     * This method sets the distribution following changes in the parameters of
     * the sampling distribution.
     */
    public void setDistributions() {
        switch (distType) {
        case 0: //Normal
            double mu = getValueSetter(0).getValue();
            double sigma = getValueSetter(1).getValue();
            x = new RandomVariable(new NormalDistribution(mu, sigma), "X");
            xBar = new RandomVariable(new NormalDistribution(mu, sigma
                    / Math.sqrt(sampleSize)), "M");
            break;
        case 1: //Gamma
            double shape = getValueSetter(0).getValue();
            double scale = getValueSetter(1).getValue();
            x = new RandomVariable(new GammaDistribution(shape, scale), "X");
            xBar = new RandomVariable(new GammaDistribution(sampleSize * shape,
                    scale / sampleSize), "M");
            break;
        case 2: //Binomial
            int m = getValueSetter(0).getValueAsInt();
            double p = getValueSetter(1).getValue();
            x = new RandomVariable(new BinomialDistribution(m, p), "X");
            xBar = new RandomVariable(new LocationScaleDistribution(
                    new BinomialDistribution(sampleSize * m, p), 0,
                    1.0 / sampleSize), "M");
            break;
        case 3: //Poisson
            double lambda = getValueSetter(0).getValue();
            x = new RandomVariable(new PoissonDistribution(lambda), "X");
            xBar = new RandomVariable(new LocationScaleDistribution(
                    new PoissonDistribution(sampleSize * lambda), 0,
                    1.0 / sampleSize), "M");
            break;
        }
        xGraph.setRandomVariable(x);
        xTable.setRandomVariable(x);
        xBarGraph.setRandomVariable(xBar);
        xBarTable.setRandomVariable(xBar);
        reset();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        super.doExperiment();
        x.reset();
        for (int i = 0; i < sampleSize; i++)
            sample[i] = x.simulate();
        xBar.setValue(x.getIntervalData().getMean());
    }

    /** This method updates the experiment. */
    public void update() {
        super.update();
        xTable.update();
        String sampleText = "Current Sample";
        for (int i = 0; i < sampleSize; i++) {
            sampleText = sampleText + "\n" + (i + 1) + "\t" + format(sample[i]);
        }
        sampleTable.setText(sampleText);
        xBarTable.update();
        getRecordTable().append(
                "\t" + format(x.getIntervalData().getMean()) + "\t"
                        + format(x.getIntervalData().getSD()));
        xGraph.repaint();
        xBarGraph.repaint();
    }
}

