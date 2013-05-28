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

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * The classic chuck-a-luck experiment that consists of rolling three dice. The
 * player bets on a number from 1 to 6
 */
public class ChuckALuckExperiment extends Experiment {
    //Variables
    private int betScore = 1, score, matches, win;
    private double[] prob = { 125.0 / 216, 0.0, 75.0 / 216, 15.0 / 216, 1.0 / 216 };
    //Objects
    private JLabel definitionLabel = new JLabel("W: Winings");
    private JPanel toolbar = new JPanel();
    private DiceBoard diceBoard = new DiceBoard(3);
    private JComboBox betJComboBox = new JComboBox();
    private FiniteDistribution winDist = new FiniteDistribution(-1, 3, 1, prob);
    private RandomVariable winRV = new RandomVariable(winDist, "W");
    private RandomVariableGraph winGraph = new RandomVariableGraph(winRV);
    private RandomVariableTable winTable = new RandomVariableTable(winRV);

    /** Initialize the experiment */
    public ChuckALuckExperiment() {
        setName("Chuck A Luck Experiment");
        //Event Listeners
        //Bet JComboBox
        for (int i = 0; i < 6; i++)
            betJComboBox.addItem("Bet: " + (i + 1));
        betJComboBox.addItemListener(this);

        //toolbars
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(betJComboBox);
        addToolbar(toolbar);
        //Graphs
        addGraph(diceBoard);
        addGraph(winGraph);
        //Table
        addTable(winTable);
        reset();
    }

    //Do Experiment
    public void doExperiment() {
        super.doExperiment();
        matches = 0;
        diceBoard.roll();
        for (int i = 0; i < 3; i++)
            if (diceBoard.getDie(i).getValue() == betScore) matches++;
        if (matches == 0) win = -1;
        else win = matches;
        winRV.setValue(win);
    }

    /**
     * This method runs the the experiment one time, and add sounds depending on
     * the outcome of the experiment.
     */
    public void step() {
        doExperiment();
        update();

        try {
            play("sounds/" + matches + ".au");
        } catch (Exception e) {
            ;
        }
    }

    //Reset
    public void reset() {
        super.reset();
        diceBoard.showDice(0);
        winRV.reset();
        getRecordTable().append("\tW");
        winGraph.reset();
        winTable.reset();
    }

    //Update
    public void update() {
        super.update();
        diceBoard.showDice(3);
        getRecordTable().append("\t" + win);
        winGraph.repaint();
        winTable.update();
    }

    //JComboBox events
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == betJComboBox) {
            betScore = betJComboBox.getSelectedIndex() + 1;
            reset();
        } else super.itemStateChanged(event);
    }
}

