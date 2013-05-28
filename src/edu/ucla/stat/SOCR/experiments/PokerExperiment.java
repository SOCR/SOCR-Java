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
import java.net.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the experiment of dealing a standard 5-card poker hand. The
 * random variable of interest is the type of hand: no value, one pair, two
 * pair, three of a kind, straight, flush, full house, four of a kind, straight
 * flush
 */
public class PokerExperiment extends Experiment {
    //Variables
    private int value, stopValue = -1;
    private int[] s = new int[5], v = new int[5];
    //Objects
    private URL url;
    private MediaTracker tracker;
    private Image[] cardImage = new Image[53];
    private FiniteDistribution handDist = new FiniteDistribution(0, 8, 1,
            new double[] { 0.501177, 0.422569, 0.047539, 0.021129, 0.003925,
                    0.001965, 0.001441, 0.000240, 0.000015 });
    private RandomVariable hand = new RandomVariable(handDist, "U");
    private RandomVariableGraph handGraph = new RandomVariableGraph(hand);
    private RandomVariableTable handTable = new RandomVariableTable(hand);
    private Deck deck = new Deck(5);

    /**
     * This method initializes the experiment, including the cards, random
     * variable graph and table, and the record table
     */
    public PokerExperiment() {
        setName("Poker Experiment");
        //Load card images
        tracker = new MediaTracker(applet);
        try {
            url = applet.getCodeBase();
            for (int i = 0; i < 53; i++) {
                applet.showStatus("Image " + String.valueOf(i));
                cardImage[i] = applet.getImage(url, "images/cards/card"
                        + String.valueOf(i) + ".gif");
                tracker.addImage(cardImage[i], i);
                Card.setImage(cardImage[i], i);
            }
            try {
                tracker.waitForAll();
            } catch (InterruptedException e) {}
        } catch (Exception e) {
            ;
        }

        //Graphs
        handGraph.showMoments(0);
        addGraph(deck);
        addGraph(handGraph);
        //Tables
        handTable.showMoments(0);
        handTable.setDecimals(6);
        addTable(handTable);
        addTable(handGraph);
        //Stop choices
        for (int i = 0; i < 9; i++)
            getStopChoice().addItem("Stop at V = " + i);
    }

    /**
     * This method overrides the corresponding method in Experiment, to
     * determine when stops occur automatically in run mode
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

    /**
     * This method defines the experiment. The 5 cards are dealt and the value
     * of the hand evaluated
     */
    public void doExperiment() {
        super.doExperiment();
        deck.deal(5);
        for (int i = 0; i < 5; i++) {
            s[i] = deck.getCard(i).getSuit();
            v[i] = deck.getCard(i).getValue();
        }
        value = evaluateHand();
        hand.setValue(value);
        setStopNow(value == stopValue);
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

    /**
     * This method resets the experiment, including the cards, the random
     * variable graph and table, and the record table
     */
    public void reset() {
        super.reset();
        getRecordTable().append("\tU");
        //Hide the cards
        deck.showCards(5, false);
        //Reset the random variable and its graph and table
        hand.reset();
        handGraph.reset();
        handTable.reset();
    }

    /**
     * This method updates the display, including the cards, the random varaible
     * graph and table, and the record table
     */
    public void update() {
        super.update();
        deck.showCards(5, true);
        getRecordTable().append("\t" + value);
        handGraph.repaint();
        handTable.update();
    }

    /**
     * This method evalutes the type of hand: no value, one pair, two pair,
     * three of a kind, straight, flush, full house, four of a kind, or straight
     * flush.
     */
    public int evaluateHand() {
        boolean straight = false;
        int value, minValue;
        if (v[1] == v[0]) {
            if (v[2] == v[1]) { // v[0] = v[1] = v[2]
                if (v[3] == v[2]) value = 7; // v[0] = v[1] = v[2] = v[3]
                else if (v[4] == v[3]) value = 6; //v[0] = v[1] = v[2], v[3] =
                                                  // v[4] <> v[2]
                else value = 3; //v[0] = v[1] = v[2], v[3], v[4] distinct
            } else if (v[3] == v[2]) { //v[0] = v[1], v[2] = v[3] distinct
                if (v[4] == v[1]) value = 6; //v[0] = v[1] = v[4], v[2] = v[3]
                                             // Distinct
                else if (v[4] == v[2]) value = 6; //v[0] = v[1], v[2] = v[3] =
                                                  // v[4] Distinct
                else value = 2; //v[0] = v[1], v[2] = v[3], v[4] distinct
            } else if (v[3] == v[1]) { //v[0] = v[1] = v[3], v[2] Distinct
                if (v[4] == v[2]) value = 6; //v[0] = v[1] = v[3], v[2] = v[4]
                                             // Distinct
                else if (v[4] == v[3]) value = 7; //v[0] = v[1] = v[3] = v[4]
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
                if (v[4] == v[3]) value = 6; //v[0] = v[3] = v[4], v[1], v[2]
                                             // distinct
                else if (v[4] == v[2]) value = 6; //v[0] = v[3], v[1] = v[2] =
                                                  // v[4] distinct
                else value = 2; //v[0] = v[3], v[1] = v[2], v[4] distinct
            } else if (v[3] == v[2]) { //v[0], v[1] = v[2] = v[3] distinct
                if (v[4] == v[3]) value = 7; //v[0], v[1] = v[2] = v[3] = v[4]
                                             // distinct
                else if (v[4] == v[0]) value = 6; //v[0] = v[4], v[1] = v[2] =
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
                if (v[4] == v[3]) value = 7; //v[0] = v[2] = v[3] = v[4], v[1]
                                             // distinct
                else if (v[4] == v[1]) value = 6; //v[0] = v[2] = v[3], v[1] =
                                                  // v[4] distinct
                else value = 3; //v[0] = v[2] = v[3], v[1], v[4] distinct
            } else if (v[3] == v[1]) { //v[0] = v[2], v[1] = v[3] distinct
                if (v[4] == v[2]) value = 6; //v[0] = v[2] = v[4], v[1] = v[3]
                                             // distinct
                else if (v[4] == v[3]) value = 6; //v[0] = v[2], v[1] = v[3] =
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
        else value = 0; //nothing so far
        //Test for flush
        if (value == 0) {
            if ((s[0] == s[1]) & (s[1] == s[2]) & (s[2] == s[3]) & (s[3] == s[4]))
                    value = 5; //flush
        }
        // Test for straight
        if ((value == 0) | (value == 5)) {
            minValue = 14;
            for (int i = 0; i < 5; i++)
                if (v[i] < minValue) minValue = v[i];
            for (int i = minValue; i < minValue + 5; i++) {
                straight = false;
                for (int j = 0; j < 5; j++) {
                    if (v[j] == i) {
                        straight = true;
                        break;
                    }
                }
                if (!straight) break;
            }
            if ((minValue == 1) & (!straight)) {
                for (int i = 10; i < 14; i++) {
                    straight = false;
                    for (int j = 0; j < 5; j++) {
                        if (v[j] == i) {
                            straight = true;
                            break;
                        }
                    }
                    if (!straight) break;
                }
            }
        }
        if (straight) {
            if (value == 5) value = 8;
            else value = 4;
        }
        return value;
    }
}

