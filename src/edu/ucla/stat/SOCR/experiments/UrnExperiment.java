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

/** A basic experiment based on drawing balls from an urn */
public class UrnExperiment extends Experiment {
    //Variables
    public final static int WITH_REPLACEMENT = 1, WITHOUT_REPLACEMENT = 0;
    private int type = WITHOUT_REPLACEMENT, totalBalls = 50, totalRed = 25;
    private int sampleSize = 10, sum;
    //Objects
    private Urn urn = new Urn(60);
    private HypergeometricDistribution dist0 = new HypergeometricDistribution(
            totalBalls, totalRed, sampleSize);
    private BinomialDistribution dist1 = new BinomialDistribution(sampleSize,
            (double) totalRed / totalBalls);
    private RandomVariable rvSum = new RandomVariable(dist0, "Y");
    private RandomVariableGraph sumGraph = new RandomVariableGraph(rvSum);
    private RandomVariableTable sumTable = new RandomVariableTable(rvSum);
    private JComboBox samplingJComboBox = new JComboBox();

    /**
     * Initialize the experiment: tables, graphs, scrollbars, labels, drop-down
     * box
     */
    public UrnExperiment() {
        setName("Ball and Urn Experiment");
        createValueSetter("N = ", Distribution.DISCRETE, 10, 100, totalBalls);
        createValueSetter("n = ", Distribution.DISCRETE, 1, totalBalls, sampleSize);
        createValueSetter("R = ", Distribution.DISCRETE, 0, totalBalls, totalRed);
        samplingJComboBox.addItem("Without Replacement");
        samplingJComboBox.addItem("With Replacmement");

        samplingJComboBox.addItemListener(this);
        addTool(samplingJComboBox);
        addGraph(urn);
        addGraph(sumGraph);
        //Tables
        addTable(sumTable);
    }

    //JComboBox events
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == samplingJComboBox) {
            type = samplingJComboBox.getSelectedIndex();
            if (type == WITH_REPLACEMENT) getValueSetter(1).setRange(1, 60,
                    sampleSize);
            else {
                sampleSize = Math.min(sampleSize, totalBalls);
                getValueSetter(1).setRange(1, Math.min(60, totalBalls), sampleSize);
            }
            setParameters();
        } else super.itemStateChanged(event);
    }

    //Scroll events
    public void update(Observable o, Object arg) {
        if (arg == getValueSetter(0)) {
            totalBalls = getValueSetter(0).getValueAsInt();
            totalRed = Math.min(totalRed, totalBalls);
            getValueSetter(2).setRange(0, totalBalls, totalRed);
            if (type == WITHOUT_REPLACEMENT) {
                sampleSize = Math.min(sampleSize, totalBalls);
                getValueSetter(1).setRange(1, Math.min(60, totalBalls), sampleSize);
            }
        } else if (arg == getValueSetter(1)) {
            sampleSize = getValueSetter(1).getValueAsInt();
        } else if (arg == getValueSetter(2)) {
            totalRed = getValueSetter(2).getValueAsInt();
        }
        setParameters();
    }

    public void setParameters() {
        dist0.setParameters(totalBalls, totalRed, sampleSize);
        dist1.setParameters(sampleSize, (double) totalRed / totalBalls);
        if (type == WITH_REPLACEMENT) rvSum.setDistribution(dist1);
        else rvSum.setDistribution(dist0);
        reset();
    }

    public void doExperiment() {
        sum = 0;
        Ball ball;
        super.doExperiment();
        urn.sample(sampleSize, totalBalls, type);
        for (int i = 0; i < sampleSize; i++) {
            ball = urn.getBall(i);
            if (ball.getValue() <= totalRed) {
                sum = sum + 1;
                ball.setColor(Color.red);
            } else ball.setColor(Color.green);
        }
        rvSum.setValue(sum);
    }

    public void update() {
        super.update();
        urn.showBalls(sampleSize);
        sumGraph.repaint();
        getRecordTable().append("\t" + sum);
        sumTable.update();
    }

    public void reset() {
        //Initialize variables
        super.reset();
        urn.showBalls(0);
        rvSum.reset();
        sumGraph.reset();
        sumTable.reset();
        getRecordTable().append("\tY");
    }
}

