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
 * This class models the hypothesis testing experiment for the probability of
 * success in the Bernoulli trials model.
 */
public class ProportionTestExperiment extends Experiment {
    //Constants
    public final static int TWO_SIDED = 0, LEFT = 1, RIGHT = 2;
    //Variables
    private int testType = TWO_SIDED, sampleSize = 5;
    private double p = 0.5, p0 = 0.5, lowerCritical = -1.645,
            upperCritical = 1.645;
    private double[] level = { 0.5, 0.4, 0.3, 0.2, 0.1, 0.05 };
    private double selectedLevel = level[4];
    private int[] sample = new int[sampleSize];
    //GUI Objects
    private JPanel toolbar1 = new JPanel();
    private JComboBox testJComboBox = new JComboBox();
    private JComboBox levelJComboBox = new JComboBox();
    private JLabel criticalLabel = new JLabel("Critical z = -1.645, 1.645");
    //Mathematical Objects
    private RandomVariable x = new RandomVariable(new BernoulliDistribution(p), "X");
    private IntervalData reject = new IntervalData(-0.5, 1.5, 1, "I");
    private RandomVariable stdScore = new RandomVariable(new NormalDistribution(0,
            1));
    //Graphs
    private MeanTestGraph testGraph = new MeanTestGraph(x, 0.5);
    private CriticalGraph stdScoreGraph = new CriticalGraph(stdScore);
    private Histogram rejectGraph = new Histogram(reject, 0);
    //Tables
    private DataTable rejectTable = new DataTable(reject, 0);
    private JTextArea sampleTable = new SOCRApplet.SOCRTextArea();

    /** This method initializes the experiment. */
    public ProportionTestExperiment() {
        setName("Proportion Test Experiment");
        rejectGraph.showSummaryStats(0);
        rejectTable.showSummaryStats(0);
        stdScoreGraph.setCriticalValues(lowerCritical, upperCritical);
        //JComboBoxs
        testJComboBox.addItem("H0: p = p0");
        testJComboBox.addItem("H0: p <= p0");
        testJComboBox.addItem("H0: p >= p0");
        levelJComboBox.addItem("Level = 0.50");
        levelJComboBox.addItem("Level = 0.40");
        levelJComboBox.addItem("Level = 0.30");
        levelJComboBox.addItem("Level = 0.20");
        levelJComboBox.addItem("Level = 0.10");
        levelJComboBox.addItem("Level = 0.05");
        levelJComboBox.setSelectedIndex(4);
        testJComboBox.addItemListener(this);
        levelJComboBox.addItemListener(this);

        //Toolbars
        toolbar1.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar1.add(testJComboBox);
        toolbar1.add(levelJComboBox);
        toolbar1.add(criticalLabel);
        createValueSetter("p", 5, 95, 0.01);
        createValueSetter("p0", 5, 95, 0.01);
        createValueSetter("n", Distribution.DISCRETE, 5, 100, sampleSize);
        addToolbar(toolbar1);
        //Construct graph panel
        addGraph(testGraph);
        addGraph(stdScoreGraph);
        addGraph(rejectGraph);
        //Construct table panel
        addTable(sampleTable);
        sampleTable.setEditable(false);
        addTable(rejectTable);
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        x.reset();
        stdScore.reset();
        reject.reset();
        sample = new int[sampleSize];
        testGraph.repaint();
        stdScoreGraph.repaint();
        rejectGraph.repaint();
        sampleTable.setText("Sample");
        getRecordTable().append("\tZ\tI");
        rejectTable.update();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        super.doExperiment();
        x.reset();
        for (int i = 0; i < sampleSize; i++)
            sample[i] = (int) (x.simulate());
        stdScore.reset();
        stdScore.setValue((x.getIntervalData().getMean() - p0)
                / Math.sqrt(p0 * (1 - p0) / sampleSize));
        if (lowerCritical < stdScore.getValue()
                & stdScore.getValue() < upperCritical) reject.setValue(0);
        else reject.setValue(1);
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
            sampleText = sampleText + "\n" + (i + 1) + "\t" + sample[i];
        }
        sampleTable.setText(sampleText);
        getRecordTable().append(
                "\t" + format(stdScore.getValue()) + "\t"
                        + format(reject.getValue()));
        rejectTable.update();
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
     * This method handles the choice events associated with changing the type
     * of test and the significance level.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == testJComboBox) {
            testType = testJComboBox.getSelectedIndex();
            setParameters();
        } else if (event.getSource() == levelJComboBox) {
            selectedLevel = level[levelJComboBox.getSelectedIndex()];
            setParameters();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scroll events associated with changes in the
     * sample size, the true value of the parameter and the hypothesized value
     * of the parameter.
     */
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            p = getValueSetter(0).getValue();
            x = new RandomVariable(new BernoulliDistribution(p), "X");
            testGraph.setRandomVariable(x);
        } else if (arg == getValueSetter(1)) {
            p0 = getValueSetter(1).getValue();
            testGraph.setTestMean(p0);

        } else if (arg == getValueSetter(2)) {
            sampleSize = getValueSetter(1).getValueAsInt();
            sample = new int[sampleSize];
        }
        reset();
    }

    /** This method computes the lower and upper critical values. */
    public void setParameters() {
        switch (testType) {
        case TWO_SIDED:
            upperCritical = stdScore.getDistribution().getQuantile(
                    1 - selectedLevel / 2);
            lowerCritical = -upperCritical;
            criticalLabel.setText("Critical z = " + format(lowerCritical) + ", "
                    + format(upperCritical));
            break;
        case LEFT:
            upperCritical = stdScore.getDistribution().getQuantile(
                    1 - selectedLevel);
            lowerCritical = Double.NEGATIVE_INFINITY;
            criticalLabel.setText("Critical z = " + format(upperCritical));
            break;
        case RIGHT:
            upperCritical = Double.POSITIVE_INFINITY;
            lowerCritical = -stdScore.getDistribution().getQuantile(
                    1 - selectedLevel);
            criticalLabel.setText("Critical z = " + format(lowerCritical));
            break;
        }
        stdScoreGraph.setCriticalValues(lowerCritical, upperCritical);
        reset();
    }
}
