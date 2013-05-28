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

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * The poker dice exeperiment consists of rolling 5 dice. The variable of
 * interest is the type of hand: one pair, two pair, three of a kind, four of a
 * kind, five of a kind, or straight.
 */
public class PokerDiceExperiment extends Experiment {
    //Variables
    private int value, stopValue = -1;
    private int[] v = new int[5];
    //Objects
    private JLabel definitionLabel = new JLabel("V: Hand Value");
    private DiceBoard diceBoard = new DiceBoard(5);
    private RandomVariable hand = new RandomVariable(new FiniteDistribution(0, 6,
            1, new double[] { 720.0 / 7776, 3600.0 / 7776, 1800.0 / 7776,
                    1200.0 / 7776, 300.0 / 7776, 150.0 / 7776, 6.0 / 7776 }), "V");
    private RandomVariableGraph handGraph = new RandomVariableGraph(hand);
    private RandomVariableTable handTable = new RandomVariableTable(hand);

    /** Initialize the experiment: graphs, tables, dice */
    public PokerDiceExperiment() {
        setName("Poker Dice Experiment");
        addTool(definitionLabel);
        //Graphs
        handGraph.showMoments(0);
        addGraph(diceBoard);
        addGraph(handGraph);
        //Table
        handTable.showMoments(0);
        handTable.setDecimals(4);
        addTable(handTable);
        //Stop choices
        for (int i = 0; i < 7; i++)
            getStopChoice().addItem("Stop at V = " + i);
    }

    /**
     * This method defines the experiment. The dice are rolled and the hand
     * evaluate
     */
    public void doExperiment() {
        super.doExperiment();
        diceBoard.roll();
        for (int i = 0; i < 5; i++)
            v[i] = diceBoard.getDie(i).getValue();
        value = evaluateHand();
        hand.setValue(value);
        setStopNow(stopValue == value);
    }

    /**
     * This method runs the the experiment one time, and add sounds depending on
     * the outcome of the experiment.
     */
    public void step() {
        doExperiment();
        update();
        String url = "sounds/" + value + ".au";
        try {
            play(url);
        } catch (Exception e) {
            ;
        }
    }

    /** Reset the experiment */
    public void reset() {
        super.reset();
        diceBoard.showDice(0);
        hand.reset();
        getRecordTable().append("\tV");
        handGraph.reset();
        handTable.reset();
    }

    /** Update the display */
    public void update() {
        super.update();
        diceBoard.showDice(5);
        getRecordTable().append("\t" + value);
        handGraph.repaint();
        handTable.update();
    }

    /**
     * This method causes the experiment to stop when the hand is a certain
     * value. The method overrides the corresponding method in the parent class
     * Experiment
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == getStopChoice()) {
            int j = getStopChoice().getSelectedIndex();
            if (j >= 5) {
                setStopFreq(-1);
                stopValue = j - 5;
            } else super.itemStateChanged(event);
        } else super.itemStateChanged(event);
    }

    /** Evaluate the hand */
    public int evaluateHand() {
        int value;
        if (v[1] == v[0]) {
            if (v[2] == v[1]) { // v[0] = v[1] = v[2]
                if (v[3] == v[2]) {
                    if (v[4] == v[3]) value = 6; // v[0] = v[1] = v[2] = v[3] =
                                                 // v[4]
                    else value = 5; //v[0] = v[1] = v[2] = v[3], v[4] distinct
                } else if (v[4] == v[2]) value = 5; //v[0] = v[1] = v[2] =
                                                    // v[4]. v[3] distinct
                else if (v[4] == v[3]) value = 4; //v[0] = v[1] = v[2], v[3],
                                                  // v[4] distinct
                else value = 3; //v[0] = v[1] = v[2], v[3], v[4] distinct
            } else if (v[3] == v[2]) { //v[0] = v[1], v[2] = v[3] distinct
                if (v[4] == v[1]) value = 4; //v[0] = v[1] = v[4], v[2] = v[3]
                                             // Distinct
                else if (v[4] == v[2]) value = 4; //v[0] = v[1], v[2] = v[3] =
                                                  // v[4] Distinct
                else value = 2; //v[0] = v[1], v[2] = v[3], v[4] distinct
            } else if (v[3] == v[1]) { //v[0] = v[1] = v[3], v[2] Distinct
                if (v[4] == v[2]) value = 4; //v[0] = v[1] = v[3], v[2] = v[4]
                                             // Distinct
                else if (v[4] == v[3]) value = 5; //v[0] = v[1] = v[3] = v[4]
                else value = 3; //v[0] = v[1] = v[3], v[2], v[4] Distinct
            } else if (v[4] == v[1]) value = 3; //v[0] = v[1] = v[4], v[2],
                                                // v[3] Distinct
            else if (v[4] == v[2]) value = 2; //v[0] = v[1], v[2] = v[4], v[3]
                                              // Distinct
            else if (v[4] == v[3]) value = 2; //v[0] = v[1], v[3] = v[4], v[2]
                                              // distinct
            else value = 1; //v[0] = v[1], v[2], v[3], v[4] distinct
        } else if (v[2] == v[1]) { //v[0], v[1] = v[2] distinct
            if (v[3] == v[0]) { //v[0] = v[3], v[1] = v[2] distinct
                if (v[4] == v[3]) value = 4; //v[0] = v[3] = v[4], v[1] = v[2]
                                             // distinct
                else if (v[4] == v[2]) value = 4; //v[0] = v[3], v[1] = v[2] =
                                                  // v[4] distinct
                else value = 2; //v[0] = v[3], v[1] = v[2], v[4] distinct
            } else if (v[3] == v[2]) { //v[0], v[1] = v[2] = v[3] distinct
                if (v[4] == v[3]) value = 5; //v[0], v[1] = v[2] = v[3] = v[4]
                                             // distinct
                else if (v[4] == v[0]) value = 4; //v[0] = v[4], v[1] = v[2] =
                                                  // v[3] Distinct
                else value = 3; //v[0], v[1] = v[2] = v[3], v[4] distinct
            } else if (v[4] == v[0]) value = 2; //v[0] = v[4], v[1] = v[2],
                                                // v[3] distinct
            else if (v[4] == v[2]) value = 3; //v[0], v[1] = v[2] = v[4], v[3]
                                              // Distinct
            else if (v[4] == v[3]) value = 2; //v[0], v[1] = v[2], v[3] = v[4]
                                              // distinct
            else value = 1; //v[0], v[1] = v[2], v[3], v[4] distinct
        } else if (v[2] == v[0]) { //v[0] = v[2], v[1] distinct
            if (v[3] == v[2]) { //v[0] = v[2] = v[3], v[1] distinct
                if (v[4] == v[3]) value = 5; //v[0] = v[2] = v[3] = v[4], v[1]
                                             // distinct
                else if (v[4] == v[1]) value = 4; //v[0] = v[2] = v[3], v[1] =
                                                  // v[4] distinct
                else value = 3; //v[0] = v[2] = v[3], v[1], v[4] distinct
            } else if (v[3] == v[1]) { //v[0] = v[2], v[1] = v[3] distinct
                if (v[4] == v[2]) value = 4; //v[0] = v[2] = v[4], v[1] = v[3]
                                             // distinct
                else if (v[4] == v[3]) value = 4; //v[0] = v[2], v[1] = v[3] =
                                                  // v[4] distinct
                else value = 2; //v[0] = v[2], v[1] = v[3], v[4] distinct
            } else if (v[4] == v[2]) value = 3; //v[0] = v[2] = v[4], v[1],
                                                // v[3] distinct
            else if (v[4] == v[1]) value = 2; //v[0] = v[2], v[1] = v[4], v[3]
                                              // distinct
            else if (v[4] == v[3]) value = 2; //v[0] = v[2], v[1], v[3] = v[4]
                                              // distinct
            else value = 1; //v[0] = v[2], v[1], v[3], v[4] distinct
        } else if (v[3] == v[0]) { //v[0] = v[3], v[1], v[2] distinct
            if (v[4] == v[3]) value = 3; //v[0] = v[3] = v[4], v[1], v[2]
                                         // distinct
            else if (v[4] == v[1]) value = 2; //v[0] = v[3], v[1] = v[4], v[2]
                                              // distinct
            else if (v[4] == v[2]) value = 2; //v[0] = v[40, v[1], v[2] = v[4]
                                              // distinct
            else value = 1; //v[0] = v[3], v[1], v[2], v[4] distinct
        } else if (v[3] == v[1]) { //v[0], v[1] = v[3], v[2] distinct
            if (v[4] == v[3]) value = 3; //v[0], v[1] = v[3] = v[4], v[2]
                                         // distinct
            else if (v[4] == v[0]) value = 2; //v[0] = v[4], v[1] = v[3], v[2]
                                              // distinct
            else if (v[4] == v[2]) value = 2; //v[0], v[1] = v[3], v[2] = v[4]
                                              // distinct
            else value = 1; //v[0], v[1] = v[3], v[2], v[4] distinct
        } else if (v[3] == v[2]) { //v[0], v[1], v[2] = v[3] distint
            if (v[4] == v[0]) value = 2; //v[0] = v[4], v[1], v[2] = v[3]
                                         // distinct
            else if (v[4] == v[1]) value = 2; //v[0], v[1] = v[4], v[2] = v[3]
                                              // distinct
            else if (v[4] == v[3]) value = 3; //v[0], v[1], v[2] = v[3] = v[4]
                                              // distinc
            else value = 1; //v[0], v[1], v[2] = v[3], v[4], distinct
        } else if (v[4] == v[0]) value = 1; //v[0] = v[4], v[1], v[2], v[3]
                                            // distinct
        else if (v[4] == v[1]) value = 1; //v[0], v[1] = v[4], v[2], v[3]
                                          // distinct
        else if (v[4] == v[2]) value = 1; //v[0], v[1], v[2] = v[4], v[3]
                                          // distinct
        else if (v[4] == v[3]) value = 1; //v[0], v[1], v[2], v[3] = v[4]
                                          // Distinct
        else value = 0; //nothing
        return value;
    }
}

