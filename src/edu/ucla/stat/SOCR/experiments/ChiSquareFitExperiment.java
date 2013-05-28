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

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

// Chi Square Goodness of Fit Experiment

public class ChiSquareFitExperiment extends Experiment {
    //Variables
    int sampleSize = 10;
    double[] level = { 0.1, 0.05, 0.025, 0.01, 0.001 };
    double criticalValue = 9.236;
    String[] densityNames = { "Fair", "1-6 Flat", "2-5 Flat", "3-4 Flat",
            "Skewed Left", "Skewed Right" };
    double[] testFrequency = { 10 / 6.0, 10 / 6.0, 10 / 6.0, 10 / 6.0, 10 / 6.0,
            10 / 6.0 };
    //Objects
    JPanel toolbar2 = new JPanel();
    JPanel toolbar3 = new JPanel();
    JLabel criticalLabel = new JLabel("Critical Value = 9.236   ");
    JComboBox levelJComboBox = new JComboBox();
    JComboBox samplingJComboBox = new JComboBox();
    JComboBox testJComboBox = new JComboBox();
    DieDistribution samplingDist = new DieDistribution(DieDistribution.FAIR);
    DieDistribution testDist = new DieDistribution(DieDistribution.FAIR);
    RandomVariable dieVariable = new RandomVariable(samplingDist, "X");
    RandomVariable testStatistic = new RandomVariable(new ChiSquareDistribution(5),
            "U");
    //IntervalData reject = new IntervalData(0., 1., 1., Data.DISCRETE, "I");
    IntervalData reject = new IntervalData(0., 1., 1., "I");
    //Data reject = new Data("I");

    DiceDistributionGraph diceGraph = new DiceDistributionGraph(dieVariable,
            testDist);
    DataGraph rejectGraph = new DataGraph(reject, 1);
    CriticalGraph criticalGraph = new CriticalGraph(testStatistic);
    JTextArea sampleTable = new SOCRApplet.SOCRTextArea();
    JTextArea recordTable = new SOCRApplet.SOCRTextArea();
    DataTable rejectTable = new DataTable(reject);
    JLabel definitionLabel = new JLabel(
            "X: die score, U: chi-square statistic, I: reject");

    //Initialize
    public ChiSquareFitExperiment() {
        setName("Chi Square Dice Experiment");
    }
    
    public void initialize() {
        createValueSetter("n", Distribution.DISCRETE, 10, 1000, sampleSize);
        //JComboBoxs
        for (int i = 0; i < 5; i++)
            levelJComboBox.addItem("Significance " + level[i]);
        for (int i = 0; i < 6; i++) {
            samplingJComboBox.addItem("Sampling: " + densityNames[i]);
            testJComboBox.addItem("Test: " + densityNames[i]);
        }
        
        levelJComboBox.addItemListener(this);
        samplingJComboBox.addItemListener(this);
        testJComboBox.addItemListener(this);

        toolbar2.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar2.add(levelJComboBox);
        toolbar2.add(criticalLabel);

        toolbar3.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar3.add(samplingJComboBox);
        toolbar3.add(testJComboBox);
        toolbar3.add(definitionLabel);
        addToolbar(toolbar2);
        addToolbar(toolbar3);
        //Graphs
        criticalGraph.setCriticalValues(0, 9.236);
        rejectGraph.showSummaryStats(0);
        addGraph(diceGraph);
        addGraph(criticalGraph);
        addGraph(rejectGraph);
        //Tables
        rejectTable.showSummaryStats(0);
        sampleTable.setEditable(false);
        recordTable.setEditable(false);
        addTable(sampleTable);
        addTable(recordTable);
        addTable(rejectTable);
        //Reset
        //reset();
    }

    //JComboBox events
    public void itemStateChanged(ItemEvent event) {
        //Sampling distribution choice
        if (event.getSource() == samplingJComboBox) {
            samplingDist = new DieDistribution(-samplingJComboBox
                    .getSelectedIndex());
            dieVariable.setDistribution(samplingDist);
            diceGraph.setParameters(dieVariable, testDist);
            reset();
        }
        //Test distribution choice
        else if (event.getSource() == testJComboBox) {
            testDist = new DieDistribution(-testJComboBox.getSelectedIndex());
            for (int i = 0; i < 6; i++)
                testFrequency[i] = sampleSize * testDist.getDensity(i + 1);
            diceGraph.setParameters(dieVariable, testDist);
            reset();
        }
        //Significance level choice
        else if (event.getSource() == levelJComboBox) {
            int index = levelJComboBox.getSelectedIndex();
            criticalValue = testStatistic.getDistribution().getQuantile(
                    1 - level[index]);
            ;
            criticalLabel.setText("Critical Value = " + format(criticalValue));
            criticalGraph.setCriticalValues(0, criticalValue);
            reset();
        } else super.itemStateChanged(event);
    }

    //Scroll events
    public void update(Observable o, Object arg) {
        sampleSize = getValueSetter(0).getValueAsInt();
        for (int i = 0; i < 6; i++)
            testFrequency[i] = sampleSize * testDist.getDensity(i + 1);
        reset();
    }

    public void doExperiment() {
        double expected, chi;
        int observed;
        super.doExperiment();
        dieVariable.reset();
        for (int i = 0; i < sampleSize; i++)
            dieVariable.sample();
        chi = 0;
        for (int i = 0; i < 6; i++) {
            observed = dieVariable.getIntervalData().getFreq((double) (i + 1));
            expected = testFrequency[i];
            chi = chi + ((observed - expected) * (observed - expected)) / expected;
        }
        testStatistic.reset();
        testStatistic.setValue(chi);
        if (criticalValue < chi) reject.setValue(1);
        else reject.setValue(0);
    }

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

    public void update() {
        super.update();
        //Graphs
        recordTable.append("\n" + "\t" + format(testStatistic.getValue()) + "\t"
                + format(reject.getValue()));
        diceGraph.drawData();
        //criticalGraph.drawData();

        criticalGraph.repaint();

        //Tables
        String sampleText = "X\tFe\tFo";
        for (int i = 0; i < 6; i++)
            sampleText = sampleText + "\n" + (i + 1) + "\t"
                    + format(testFrequency[i]) + "\t"
                    + dieVariable.getIntervalData().getFreq((double) (i + 1));
        sampleTable.setText(sampleText);
        rejectGraph.repaint();
        // rejectGraph.drawData();
        rejectTable.update();
    }

    public void reset() {
        super.reset();
        dieVariable.reset();
        testStatistic.reset();
        reject.reset();
        diceGraph.repaint();
        criticalGraph.repaint();
        rejectGraph.repaint();
        rejectTable.update();
        recordTable.setText("Run\tU\tI");
        String sampleText = "X\tFe\tFo";
        for (int i = 0; i < 6; i++)
            sampleText = sampleText + "\n" + (i + 1) + "\t"
                    + format(testFrequency[i]);
        sampleTable.setText(sampleText);
    }
}

