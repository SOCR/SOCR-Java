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
 * This class models the hypothesis test experiment in the standard normal
 * model.
 */
public class MeanTestExperiment extends Experiment {
    //Constants
    public final static int TWO_SIDED = 0, LEFT = 1, RIGHT = 2;
    //Variables
    private int distType = 0, sampleSize = 5, testType = TWO_SIDED;
    private boolean sigmaKnown = true;
    private double testMean = 0.0, mean = 0, stdDev = 1;
    private double lowerCritical, upperCritical, selectedLevel = 0.1;
    private double[] level = { 0.5, 0.4, 0.3, 0.2, 0.1, 0.05 }, sample;
    private String stdScoreName = "Z";
    //Objects
    private JPanel toolbar1 = new JPanel();
    private JComboBox distJComboBox = new JComboBox();
    private JComboBox testJComboBox = new JComboBox();
    private JComboBox levelJComboBox = new JComboBox();
    private JComboBox sigmaJComboBox = new JComboBox();
    private JLabel criticalJLabel = new JLabel("z = +- 1.96  ");
    //Random Variables
    private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
    private IntervalData reject = new IntervalData(-0.5, 1.5, 1, "I");
    private RandomVariable stdScore = new RandomVariable(new NormalDistribution(0,
            1));
    //Graphs
    private MeanTestGraph testGraph = new MeanTestGraph(x, 0);
    private CriticalGraph stdScoreGraph = new CriticalGraph(stdScore);
    private Histogram rejectGraph = new Histogram(reject, 0);
    //Tables
    private DataTable rejectTable = new DataTable(reject, 0);
    private JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public MeanTestExperiment() {
        setName("Mean Test Experiment");
        rejectGraph.showSummaryStats(0);
        rejectTable.showSummaryStats(0);
        //JComboBoxs
        distJComboBox.addItem("Normal");
        distJComboBox.addItem("Gamma");
        distJComboBox.addItem("Uniform");
        testJComboBox.addItem("H0: mu = mu0");
        testJComboBox.addItem("H0: mu <= mu0");
        testJComboBox.addItem("H0: mu >= mu0");
        levelJComboBox.addItem("Level = 0.50");
        levelJComboBox.addItem("Level = 0.40");
        levelJComboBox.addItem("Level = 0.30");
        levelJComboBox.addItem("Level = 0.2");
        levelJComboBox.addItem("Level = 0.1");
        levelJComboBox.addItem("Level = 0.05");
        sigmaJComboBox.addItem("sigma Known");
        sigmaJComboBox.addItem("sigma Unknown");
        distJComboBox.addItemListener(this);
        testJComboBox.addItemListener(this);
        levelJComboBox.addItemListener(this);
        sigmaJComboBox.addItemListener(this);
        createValueSetter("mu", Distribution.CONTINUOUS, -5, 5, 0);
        createValueSetter("sigma", 5, 50, 0.1);
        createValueSetter("n", Distribution.DISCRETE, 5, 100, sampleSize);
        createValueSetter("mu0", Distribution.CONTINUOUS,
                (int) (mean - 2 * stdDev), (int) (mean + 2 * stdDev), (int) mean);

        //Construct toolbars
        toolbar1.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar1.add(distJComboBox);
        toolbar1.add(levelJComboBox);
        toolbar1.add(criticalJLabel);
        toolbar1.add(levelJComboBox);
        toolbar1.add(sigmaJComboBox);
        toolbar1.add(testJComboBox);
        addToolbar(toolbar1);
        //Construct graph panel
        addGraph(testGraph);
        addGraph(stdScoreGraph);
        addGraph(rejectGraph);
        //Construct table panel
        addTable(sampleTable);
        addTable(rejectTable);
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        x.reset();
        stdScore.reset();
        reject.reset();
        sample = new double[sampleSize];
        testGraph.repaint();
        stdScoreGraph.repaint();
        rejectGraph.repaint();
        sampleTable.setText("Sample");
        if (sigmaKnown) getRecordTable().append("\tM\tZ\tI");
        else getRecordTable().append("\tM\tT\tI");
        rejectTable.update();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        double stdError;
        super.doExperiment();
        x.reset();
        for (int i = 0; i < sampleSize; i++)
            sample[i] = x.simulate();
        if (sigmaKnown) stdError = x.getDistribution().getSD()
                / Math.sqrt(sampleSize);
        else stdError = x.getIntervalData().getSD() / Math.sqrt(sampleSize);
        stdScore.reset();
        stdScore.setValue((x.getIntervalData().getMean() - testMean) / stdError);
        if (lowerCritical < stdScore.getValue()
                & stdScore.getValue() < upperCritical) reject.setValue(0);
        else reject.setValue(1);
    }

    /**
     * This method runs the experiment one time, and plays a sound depending on
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
     * This event handles the choice events associated with a change in the
     * sampling distribution, a change in the type of test, a change in the
     * significance level, or a change in the knowledge of sigma.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distJComboBox) {
            distType = distJComboBox.getSelectedIndex();
            switch (distType) {

            case 0: //Normal
                getValueSetter(0).setRange(-5, 5, 0);
                getValueSetter(1).setRange(5, 50,10);
                getValueSetter(0).setTitle("mu");
                getValueSetter(1).setTitle("sigma");
                break;
            case 1: //Gamma
                getValueSetter(0).setRange(1, 5, 1);
                getValueSetter(1).setRange(5, 50,10);
                getValueSetter(0).setTitle("shape");
                getValueSetter(1).setTitle("scale");
                break;
            case 2: //Uniform
                getValueSetter(0).setRange(-5, 5, 0);
                getValueSetter(1).setRange(10, 100, 1);
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
        } else if (event.getSource() == sigmaJComboBox) {
            sigmaKnown = (sigmaJComboBox.getSelectedIndex() == 0);
            setParameters();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scrollbar events associated with changes in the
     * sample size, the distribution parameters, or the test mean.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(2)) {
            sampleSize = getValueSetter(2).getValueAsInt();
            setParameters();
        } else if (arg == getValueSetter(3)) {
            testMean = getValueSetter(3).getValue();
            testGraph.setTestMean(testMean);
            reset();
        } else setDistribution();
    }

    /** This method updates the experiment. */
    public void update() {
        super.update();
        //Graphs
        testGraph.repaint();
        stdScoreGraph.repaint();
        rejectGraph.repaint();
        //Tables
        String sampleText = "Sample";
        for (int i = 0; i < sampleSize; i++) {
            sampleText = sampleText + "\n" + (i + 1) + "\t" + format(sample[i]);
        }
        sampleTable.setText(sampleText);
        getRecordTable().append(
                "\t" + format(x.getIntervalData().getMean()) + "\t"
                        + format(stdScore.getValue()) + "\t"
                        + format(reject.getValue()));
        rejectTable.update();
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
        switch (testType) {
        case TWO_SIDED:
            upperCritical = stdScore.getDistribution().getQuantile(
                    1 - selectedLevel / 2);
            lowerCritical = -upperCritical;
            criticalJLabel.setText(stdScoreName + " = +-" + format(upperCritical));
            break;
        case LEFT:
            upperCritical = stdScore.getDistribution().getQuantile(
                    1 - selectedLevel);
            lowerCritical = Double.NEGATIVE_INFINITY;
            criticalJLabel.setText(stdScoreName + " = " + format(upperCritical));
            break;
        case RIGHT:
            lowerCritical = -stdScore.getDistribution().getQuantile(
                    1 - selectedLevel);
            upperCritical = Double.POSITIVE_INFINITY;
            criticalJLabel.setText(stdScoreName + " = - " + format(lowerCritical));
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
            double sigma = getValueSetter(1).getValue();
            x = new RandomVariable(new NormalDistribution(mu, sigma), "X");
            break;
        case 1: //Gamma
            double shape = getValueSetter(0).getValue();
            double scale = getValueSetter(1).getValue();
            x = new RandomVariable(new GammaDistribution(shape, scale), "X");
            break;
        case 2: //Uniform
            double leftPoint = getValueSetter(0).getValue();
            double length = getValueSetter(1).getValue();
            x = new RandomVariable(new ContinuousUniformDistribution(leftPoint,
                    leftPoint + length), "X");
            break;
        }
        testGraph.setRandomVariable(x);
        mean = x.getDistribution().getMean();
        stdDev = x.getDistribution().getSD();
        testMean = mean;
        getValueSetter(3).setRange((int) (mean - 2 * stdDev),
                (int) (mean + 2 * stdDev), (int) mean);
        testGraph.setTestMean(testMean);
        reset();
    }
}

