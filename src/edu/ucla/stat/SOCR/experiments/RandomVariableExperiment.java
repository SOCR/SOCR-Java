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
 * This is a basic experiment for simulating the values from a number of
 * distributions. The true and empirical densities are shown in a graph and
 * table
 */
public class RandomVariableExperiment extends Experiment {
    //Variables
    private int distributionType = 0;

    private JComboBox distributionJComboBox = new JComboBox();
    private RandomVariable rv = new RandomVariable(new NormalDistribution(0, 1),
            "X");
    private RandomVariableTable rvTable = new RandomVariableTable(rv);
    private RandomVariableGraph rvGraph = new RandomVariableGraph(rv);

    /**
     * This method initializes the experiment, including the toolbar with the
     * distribution choice, scrollbars and label, and the random variable graph
     * and table
     */
    public RandomVariableExperiment() {
        setName("Random Variable Experiment");
        distributionJComboBox.addItem("Normal");
        distributionJComboBox.addItem("Gamma");
        distributionJComboBox.addItem("Chi-square");
        distributionJComboBox.addItem("Student");
        distributionJComboBox.addItem("F");
        distributionJComboBox.addItem("Beta");
        distributionJComboBox.addItem("Weibull");
        distributionJComboBox.addItem("Pareto");
        distributionJComboBox.addItem("Logistic");
        distributionJComboBox.addItem("Lognormal");
        distributionJComboBox.addItemListener(this);
        createValueSetter("mu", Distribution.CONTINUOUS, -5, 5, 0);
        createValueSetter("sigma", 5, 50, 10, 0.1);
        addTool(distributionJComboBox);

        addTable(rvTable);

        addGraph(rvGraph);
    }

    /**
     * This method defines the experiment: a value from the selected random
     * variable is simulated.
     */
    public void doExperiment() {
        super.doExperiment();
        rv.sample();
    }

    /**
     * This method handles the choice events for changing the selected
     * distribution
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == distributionJComboBox) {
            distributionType = distributionJComboBox.getSelectedIndex();
            switch (distributionType) {
            case 0: //Normal
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setRange(-5, 5);
                getValueSetter(1).setRange(5, 50);
                break;
            case 1: //Gamma
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setRange(1, 5);
                getValueSetter(1).setRange(5, 50);
                break;
            case 2: //Chi-square
                getValueSetter(1).setEnabled(false);
                getValueSetter(0).setRange(1, 20);
                break;
            case 3: //Student
                getValueSetter(1).setEnabled(false);
                getValueSetter(0).setRange(1, 50);
                break;
            case 4: //Fisher
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setRange(1, 50);
                getValueSetter(1).setRange(1, 50);
                break;
            case 5: //Beta
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setRange(5, 50);
                getValueSetter(1).setRange(5, 50);
                break;
            case 6: //Weibull
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setRange(1, 5);
                getValueSetter(1).setRange(5, 50);
                break;
            case 7: //Pareto
                getValueSetter(1).setEnabled(false);
                getValueSetter(0).setRange(1, 10);
                break;
            case 8: //Logistic

                break;
            case 9: //Lognormal
                getValueSetter(1).setEnabled(true);
                getValueSetter(0).setRange(-2, 2);
                getValueSetter(1).setRange(5, 20);
                break;
            }
            setDistribution();
        } else super.itemStateChanged(event);
    }

    /**
     * This method handles the scrollbar events for changing the parameters of
     * the selected distsribution
     */
    public void update(Observable o, Object arg) {
        //if (arg == getValueSetter(1)) {
        setDistribution();
    }

    /**
     * This method updates the experiment, including the record table, random
     * variable graph and table
     */
    public void update() {
        super.update();
        getRecordTable().append("\t" + format(rv.getValue()));
        rvGraph.repaint();
        rvTable.update();
    }

    /**
     * This method resets the experiment, including the record table and the
     * random variable graph and table
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\t" + rv.getName());
        rv.reset();
        rvTable.reset();
        rvGraph.reset();
    }

    /**
     * This method sets the distribution, after the selected distribution or the
     * parameters of the distribution have changed
     */
    public void setDistribution() {
        double mu, sigma, a, b, k;
        int n, m;
        switch (distributionType) {
        case 0: //Normal
            mu = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("mu = ");
            sigma = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("sigma");
            rv = new RandomVariable(new NormalDistribution(mu, sigma), "X");
            break;
        case 1: //Gamma
            k = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("shape = ");
            b = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("scale = ");
            rv = new RandomVariable(new GammaDistribution(k, b), "X");
            break;
        case 2: //Chi-square
            n = (int) getValueSetter(0).getValue();
            getValueSetter(0).setTitle("DF = " + n);
            rv = new RandomVariable(new ChiSquareDistribution(n), "X");
            break;
        case 3: //Student
            n = (int) getValueSetter(0).getValue();
            getValueSetter(0).setTitle("DF = ");
            rv = new RandomVariable(new StudentDistribution(n), "X");
            break;
        case 4: //Fisher
            m = (int) getValueSetter(0).getValue();
            getValueSetter(0).setTitle("Num DF = ");
            n = (int) getValueSetter(1).getValue();
            getValueSetter(1).setTitle("Den DF = ");
            rv = new RandomVariable(new FisherDistribution(m, n), "X");
            break;
        case 5: //Beta
            a = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("a = ");
            b = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("b = ");
            rv = new RandomVariable(new BetaDistribution(a, b), "X");
            break;
        case 6: //Wiebull
            k = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("shape = ");
            b = getValueSetter(1).getValue();
            getValueSetter(1).setTitle("scale = ");
            rv = new RandomVariable(new WeibullDistribution(k, b), "X");
            break;
        case 7: //Pareto
            a = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("shape = ");
            rv = new RandomVariable(new ParetoDistribution(a), "X");
            break;
        case 8: //Logistic
            rv = new RandomVariable(new LogisticDistribution(), "X");
            break;
        case 9: //Lognormal
            mu = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("mu = ");
            sigma = getValueSetter(0).getValue();
            getValueSetter(0).setTitle("sigma = ");
            rv = new RandomVariable(new LogNormalDistribution(mu, sigma), "X");
            break;
        }
        rvGraph.setRandomVariable(rv);
        rvTable.setRandomVariable(rv);
        reset();
    }
}

