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
import java.net.URL;
import javax.swing.*;

/** This class models the casino roulette experiment. */
public class RouletteExperiment extends Experiment {
    //Variables
    private int score, betType, win;
    private double[] prob = { 37.0 / 38, 1.0 / 38 };
    //Constants
    public final static int ONE = 0, TWO = 1, THREE = 2, FOUR = 3, SIX = 4,
            TWELVE = 5, EIGHTEEN = 6;
    //Objects
    private Image image;
    private URL url;
    private MediaTracker tracker;
    private RouletteWheel wheel = new RouletteWheel();
    private JComboBox betJComboBox = new JComboBox();
    private FiniteDistribution dist = new FiniteDistribution(-1, 35, 36, prob);
    private RandomVariable winRV = new RandomVariable(dist, "W");
    private RandomVariableGraph winGraph = new RandomVariableGraph(winRV);
    private RandomVariableTable winTable = new RandomVariableTable(winRV);

    /**
     * This method initializes the experiment by setting up the toolbar with the
     * bet choice, and initializing the roulette wheel.
     */
    public RouletteExperiment() {
        setName("Roulette Experiment");
        tracker = new MediaTracker(applet);
        try {
            url = applet.getCodeBase();
            applet.showStatus("Loading Image");
            image = applet.getImage(url, "images/casino/RouletteWheel.gif");
            tracker.addImage(image, 0);
            RouletteWheel.setImage(image);
        } catch (Exception e) {
            ;
        }

        //Bet choice
        betJComboBox.addItem("Bet on 1");
        betJComboBox.addItem("Bet on 1, 2");
        betJComboBox.addItem("Bet on 1, 2, 3");
        betJComboBox.addItem("Bet on 1, 2, 4, 5");
        betJComboBox.addItem("Bet on 1-6");
        betJComboBox.addItem("Bet on 1-12");
        betJComboBox.addItem("Bet on 1-18");
        betJComboBox.addItemListener(this);

        addTool(betJComboBox);
        addGraph(wheel);
        addGraph(winGraph);
        winTable.setDecimals(4);
        addTable(winTable);
    }

    /**
     * This method defines the exeperiment. The wheel is spun and the result of
     * the bet determined.
     */
    public void doExperiment() {
        super.doExperiment();
        score = wheel.spin();
        switch (betType) {
        case ONE:
            if (score == 1) win = 35;
            else win = -1;
            ;
            break;
        case TWO:
            if (score == 1 | score == 2) win = 17;
            else win = -1;
            break;
        case THREE:
            if (score >= 1 & score <= 3) win = 11;
            else win = -1;
            break;
        case FOUR:
            if (score == 1 | score == 2 | score == 4 | score == 5) win = 8;
            else win = -1;
            break;
        case SIX:
            if (score >= 1 & score <= 6) win = 5;
            else win = -1;
            break;
        case TWELVE:
            if (score >= 1 & score <= 12) win = 2;
            else win = -1;
            break;
        case EIGHTEEN:
            if (score >= 1 & score <= 18) win = 1;
            else win = -1;
            break;
        }
        winRV.setValue(win);
    }

    /**
     * This method resets the experiment, including the roulette wheel, the
     * random variable graph and table, and the record table.
     */
    public void reset() {
        super.reset();
        wheel.reset();
        winRV.reset();
        getRecordTable().append("\tX\tW");
        winGraph.reset();
        winTable.reset();
    }

    /**
     * This method updates the experiment, including the roulette wheel, the
     * record table and the random variable graph and table.
     */
    public void update() {
        super.update();
        wheel.drawBall();
        getRecordTable().append("\t" + wheel.getLabel() + "\t" + win);
        winGraph.repaint();
        winTable.update();
    }

    /**
     * This method runs the experiment one time and then plays a sound,
     * depending on the outcome.
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

    /** This method sets the distribution when the bet has changed. */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == betJComboBox) {
            prob = new double[2];
            betType = betJComboBox.getSelectedIndex();
            switch (betType) {
            case ONE:
                prob[0] = 37.0 / 38;
                prob[1] = 1.0 / 38;
                dist.setParameters(-1, 35, 36, prob);
                break;
            case TWO:
                prob[0] = 36.0 / 38;
                prob[1] = 2.0 / 38;
                dist.setParameters(-1, 17, 18, prob);
                break;
            case THREE:
                prob[0] = 35.0 / 38;
                prob[1] = 3.0 / 38;
                dist.setParameters(-1, 11, 12, prob);
                break;
            case FOUR:
                prob[0] = 34.0 / 38;
                prob[1] = 4.0 / 38;
                dist.setParameters(-1, 8, 9, prob);
                break;
            case SIX:
                prob[0] = 32.0 / 38;
                prob[1] = 6.0 / 38;
                dist.setParameters(-1, 5, 6, prob);
                break;
            case TWELVE:
                prob[0] = 26.0 / 38;
                prob[1] = 12.0 / 38;
                dist.setParameters(-1, 2, 3, prob);
                break;
            case EIGHTEEN:
                prob[0] = 20.0 / 38;
                prob[1] = 18.0 / 38;
                dist.setParameters(-1, 1, 2, prob);
                break;
            }
            reset();
        } else super.itemStateChanged(event);
    }
}

