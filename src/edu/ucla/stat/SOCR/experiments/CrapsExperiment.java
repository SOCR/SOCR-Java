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

/** The basic casino craps game */
public class CrapsExperiment extends Experiment {
    //Constants
    public final static int PASS = 0, DONTPASS = 1, FIELD = 2, CRAPS = 3,
            CRAPS2 = 4, CRAPS3 = 5, CRAPS12 = 6, SEVEN = 7, ELEVEN = 8, BIG6 = 9,
            BIG8 = 10, HARDWAY4 = 11, HARDWAY6 = 12, HARDWAY8 = 13, HARDWAY10 = 14;

    //Variables
    private int x, y, u, v, point1, point2, win, rolls, betType = PASS;
    private double[] prob = new double[] { 251.0 / 495, 0, 244.0 / 495 };
    //Objects
    private JPanel toolbar = new JPanel();
    private DiceBoard diceBoard = new DiceBoard(4);
    private JComboBox betJComboBox = new JComboBox();
    private FiniteDistribution dist = new FiniteDistribution(-1, 1, 1, prob);
    private RandomVariable profitRV = new RandomVariable(dist, "W");
    private RandomVariableGraph profitGraph = new RandomVariableGraph(profitRV);
    private RandomVariableTable profitTable = new RandomVariableTable(profitRV);

    /** Initialize the experiment */
    public CrapsExperiment() {
        setName("Craps Experiment");
        //Event listeners
        betJComboBox.addItemListener(this);
        //Bet choice
        betJComboBox.addItem("Pass");
        betJComboBox.addItem("Don't Pass");
        betJComboBox.addItem("Field");
        betJComboBox.addItem("Craps");
        betJComboBox.addItem("Craps 2");
        betJComboBox.addItem("Craps 3");
        betJComboBox.addItem("Craps 12");
        betJComboBox.addItem("Seven");
        betJComboBox.addItem("Eleven");
        betJComboBox.addItem("Big 6");
        betJComboBox.addItem("Big 8");
        betJComboBox.addItem("Hardway 4");
        betJComboBox.addItem("Hardway 6");
        betJComboBox.addItem("Hardway 8");
        betJComboBox.addItem("Hardway 10");
        //toolbars
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(betJComboBox);
        addToolbar(toolbar);
        //Graphs
        addGraph(diceBoard);
        addGraph(profitGraph);
        //Table
        addTable(profitTable);
        reset();
    }

    /**
     * Perform the experiment: roll the dice, and depending on the bet,
     * determine whether to roll the dice a second time. Finally, deterimine the
     * outcome of the bet
     */
    public void doExperiment() {
        super.doExperiment();
        rolls = 1;
        x = (int) Math.ceil(6 * Math.random());
        y = (int) Math.ceil(6 * Math.random());
        point1 = x + y;
        point2 = 0;
        switch (betType) {
        case PASS:
            if (point1 == 7 | point1 == 11) win = 1;
            else if (point1 == 2 | point1 == 3 | point1 == 12) win = -1;
            else {
                while (point2 != point1 & point2 != 7) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (point2 == point1) win = 1;
                else win = -1;
            }
            break;
        case DONTPASS:
            if (point1 == 7 | point1 == 11) win = -1;
            else if (point1 == 2 | point1 == 3) win = 1;
            else if (point1 == 12) win = 0;
            else {
                while (point2 != point1 & point2 != 7) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (point2 == point1) win = -1;
                else win = 1;
            }
            break;
        case FIELD:
            if (point1 == 3 | point1 == 4 | point1 == 9 | point1 == 10
                    | point1 == 11) win = 1;
            else if (point1 == 2 | point1 == 12) win = 2;
            else win = -1;
            break;
        case CRAPS:
            if (point1 == 2 | point1 == 3 | point1 == 12) win = 7;
            else win = -1;
            break;
        case CRAPS2:
            if (point1 == 2) win = 30;
            else win = -1;
            break;
        case CRAPS3:
            if (point1 == 3) win = 15;
            else win = -1;
            break;
        case CRAPS12:
            if (point1 == 12) win = 30;
            else win = -1;
            break;
        case SEVEN:
            if (point1 == 7) win = 4;
            else win = -1;
            break;
        case ELEVEN:
            if (point1 == 11) win = 15;
            else win = -1;
            break;
        case BIG6:
            if (point1 == 6) win = 1;
            else if (point1 == 7) win = -1;
            else {
                while (point2 != 6 & point2 != 7) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (point2 == 6) win = 1;
                else win = -1;
            }
            break;
        case BIG8:
            if (point1 == 8) win = 1;
            else if (point1 == 7) win = -1;
            else {
                while (point2 != 8 & point2 != 7) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (point2 == 8) win = 1;
                else win = -1;
            }
            break;
        case HARDWAY4:
            if (x == 2 & y == 2) win = 7;
            else if (point1 == 7 | point1 == 4) win = -1;
            else {
                while (point2 != 7 & point2 != 4) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (u == 2 & v == 2) win = 7;
                else win = -1;
            }
            break;
        case HARDWAY6:
            if (x == 3 & y == 3) win = 9;
            else if (point1 == 7 | point1 == 6) win = -1;
            else {
                while (point2 != 7 & point2 != 6) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (u == 3 & v == 3) win = 9;
                else win = -1;
            }
            break;
        case HARDWAY8:
            if (x == 4 & y == 4) win = 9;
            else if (point1 == 7 | point1 == 8) win = -1;
            else {
                while (point2 != 7 & point2 != 8) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (u == 4 & v == 4) win = 9;
                else win = -1;
            }
            break;
        case HARDWAY10:
            if (x == 5 & y == 5) win = 7;
            else if (point1 == 7 | point1 == 10) win = -1;
            else {
                while (point2 != 7 & point2 != 10) {
                    u = (int) Math.ceil(6 * Math.random());
                    v = (int) Math.ceil(6 * Math.random());
                    point2 = u + v;
                    rolls++;
                }
                if (u == 5 & v == 5) win = 7;
                else win = -1;
            }
            break;
        }
        profitRV.setValue(win);
    }

    /**
     * This method runs the the experiment one time, and add sounds depending on
     * the outcome of the experiment.
     */
    public void step() {
        doExperiment();
        update();
        try {
            if (win == -1) play("sounds/0.au");
            else play("sounds/1.au");
        } catch (Exception e) {
            ;
        }
    }

    //Reset
    public void reset() {
        super.reset();
        diceBoard.showDice(0);
        profitRV.reset();
        getRecordTable().append("\t(X,Y)\t(U,V)\tN\tW");
        profitGraph.reset();
        profitTable.reset();
    }

    //Update
    public void update() {
        super.update();
        String updateText;
        diceBoard.getDie(0).setValue(x);
        diceBoard.getDie(1).setValue(y);
        if (rolls > 1) {
            diceBoard.getDie(2).setValue(u);
            diceBoard.getDie(3).setValue(v);
            diceBoard.showDice(4);
        } else diceBoard.showDice(2);
        updateText = "\t(" + x + "," + y + ")";
        if (rolls > 1) updateText = updateText + "\t(" + u + "," + v + ")";
        else updateText = updateText + "\t(*,*)";
        updateText = updateText + "\t" + rolls + "\t" + win;
        getRecordTable().append(updateText);
        profitGraph.repaint();
        profitTable.update();
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == betJComboBox) {
            betType = betJComboBox.getSelectedIndex();
            switch (betType) {
            case PASS:
                prob = new double[3];
                prob[0] = 251.0 / 495;
                prob[2] = 244.0 / 495;
                dist.setParameters(-1, 1, 1, prob);
                break;
            case DONTPASS:
                prob = new double[3];
                prob[0] = 244.0 / 495;
                prob[1] = 1.0 / 36;
                prob[2] = 949.0 / 1980;
                dist.setParameters(-1, 1, 1, prob);
                break;
            case FIELD:
                prob = new double[4];
                prob[0] = 5.0 / 9;
                prob[2] = 7.0 / 18;
                prob[3] = 1.0 / 18;
                dist.setParameters(-1, 2, 1, prob);
                break;
            case CRAPS:
                prob = new double[9];
                prob[0] = 8.0 / 9;
                prob[8] = 1.0 / 9;
                dist.setParameters(-1, 7, 1, prob);
                break;
            case CRAPS2:
                prob = new double[32];
                prob[0] = 35.0 / 36;
                prob[31] = 1.0 / 36;
                dist.setParameters(-1, 30, 1, prob);
                break;
            case CRAPS3:
                prob = new double[17];
                prob[0] = 17.0 / 18;
                prob[16] = 1.0 / 18;
                dist.setParameters(-1, 15, 1, prob);
                break;
            case CRAPS12:
                prob = new double[32];
                prob[0] = 35.0 / 36;
                prob[31] = 1.0 / 36;
                dist.setParameters(-1, 30, 1, prob);
                break;
            case SEVEN:
                prob = new double[6];
                prob[0] = 5.0 / 6;
                prob[5] = 1.0 / 6;
                dist.setParameters(-1, 4, 1, prob);
                break;
            case ELEVEN:
                prob = new double[17];
                prob[0] = 17.0 / 18;
                prob[16] = 1.0 / 18;
                dist.setParameters(-1, 15, 1, prob);
                break;
            case BIG6:
                prob = new double[3];
                prob[0] = 6.0 / 11;
                prob[2] = 5.0 / 11;
                dist.setParameters(-1, 1, 1, prob);
                break;
            case BIG8:
                prob = new double[3];
                prob[0] = 6.0 / 11;
                prob[2] = 5.0 / 11;
                dist.setParameters(-1, 1, 1, prob);
                break;
            case HARDWAY4:
                prob = new double[9];
                prob[0] = 8.0 / 9;
                prob[8] = 1.0 / 9;
                dist.setParameters(-1, 7, 1, prob);
                break;
            case HARDWAY6:
                prob = new double[11];
                prob[0] = 10. / 11;
                prob[10] = 1.0 / 11;
                dist.setParameters(-1, 9, 1, prob);
                break;
            case HARDWAY8:
                prob = new double[11];
                prob[0] = 10. / 11;
                prob[10] = 1.0 / 11;
                dist.setParameters(-1, 9, 1, prob);
                break;
            case HARDWAY10:
                prob = new double[9];
                prob[0] = 8.0 / 9;
                prob[8] = 1.0 / 9;
                dist.setParameters(-1, 7, 1, prob);
                break;
            }
            reset();
        } else super.itemStateChanged(event);
    }
}

