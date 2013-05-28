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

package edu.ucla.stat.SOCR.games;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * This class models the red-black game. A player plays Bernoulli trials against
 * the house at even stakes until she loses her fortune or reaches a specified
 * target fortune.
 */
public class RedBlackGame extends Game implements ItemListener {
    private int runs = 0, initial = 8, target = 16, bet = 1, current, trials;
    private double p = 0.5, trialsMean;
    private RedBlackGraph rbGraph = new RedBlackGraph(8, 16);
    private IntervalData success = new IntervalData(-0.5, 1.5, 1, "J");
    private Histogram successGraph = new Histogram(success, 0);
    private DataTable successTable = new DataTable(success, 0);
    private JTextArea trialsTable = new SOCRApplet.SOCRTextArea();
    private JTextArea gameTable = new SOCRApplet.SOCRTextArea();
    private JComboBox targetJComboBox = new JComboBox();
    private JButton playButton = new JButton("Play");
    private JButton gameButton = new JButton("New Game");
    private JLabel definitionJLabel = new JLabel("J: win, N: # trials");

    /** This method initializes the experiment. */
    public RedBlackGame() {
        setName("Red and Black Game");
        //Event listeners
        playButton.addActionListener(this);
        gameButton.addActionListener(this);
        targetJComboBox.addItemListener(this);
        createValueSetter("X", Distribution.DISCRETE, 0, target, initial);
        createValueSetter("P", Distribution.CONTINUOUS, 0, 1);
        createValueSetter("Bet", Distribution.DISCRETE, 0, initial, bet);

        //Graphs and tables
        successGraph.showSummaryStats(0);
        successGraph.setMargins(35, 20, 20, 20);
        successTable.showSummaryStats(0);
        gameTable.setEditable(false);
        trialsTable.setEditable(false);
        //Target choice
        for (int i = 1; i < 8; i++)
            targetJComboBox.addItem("a = " + Math.pow(2, i));
        targetJComboBox.setSelectedIndex(3);
        //Construct toolbar
        addTool(gameButton);
        addTool(playButton);
        SOCRToolBar toolbar2 = new SOCRToolBar();
        addTool(targetJComboBox);
        addTool(definitionJLabel);

        //Construct graph panel
        addGraph(rbGraph);
        addGraph(successGraph);
        //Construct table panel
        addTable(gameTable);
        addTable(successTable);
        addTable(trialsTable);
        reset();
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        success.reset();
        resetGame();
        getRecordTable().append("\tJ\tN");
        trialsTable.setText("N\tData\nMean");
        successTable.update();
        applet.repaint();
    }

    /** This method handles the events associated with the buttons. */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playButton) {
            int i;
            trials = trials + 1;
            if (Math.random() <= p) {
                current = current + bet;
                i = 1;
            } else {
                current = current - bet;
                i = 0;
            }
            rbGraph.setCurrent(current);
            rbGraph.drawCurrent();
            gameTable.append("\n" + trials + "\t" + i + "\t" + current);
            resetBet();
            if ((current == 0) | (current == target)) {
                runs = runs++;
                if (current == target) {
                    success.setValue(1);
                    try {
                        play("sounds/0.au");
                    } catch (Exception e) {
                        ;
                    }
                } else {
                    success.setValue(0);
                    try {
                        play("sounds/0.au");
                    } catch (Exception e) {
                        ;
                    }
                }
                trialsMean = ((runs - 1) * trialsMean + trials) / runs;
                successGraph.repaint();
                successTable.update();
                trialsTable.setText("N\tData\nMean\t" + format(trialsMean));
                getRecordTable().append(
                        "\t" + format(success.getValue()) + "\t" + trials);
                playButton.setEnabled(false);
            }
        } else if (event.getSource() == gameButton) resetGame();
        else super.actionPerformed(event);
    }

    /**
     * This method handles the choice events assoicated with changing the target
     * fortune.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == targetJComboBox) {
            target = (int) (Math.pow(2, targetJComboBox.getSelectedIndex() + 1));
            initial = Math.min(initial, target);
            //fValueSetters[0].setRange(1, target);
            getValueSetter(0).setValue(initial);
            rbGraph.setParameters(initial, target);
            reset();
        }
    }

    /** This method resets the game. */
    public void resetGame() {
        trials = 0;
        current = initial;
        playButton.setEnabled(true);
        resetBet();
        gameTable.setText("Trial\tI\tX");
        rbGraph.setCurrent(current);
        rbGraph.repaint();
    }

    /** This method resets the bet. */
    public void resetBet() {
        bet = Math.min(bet, Math.min(current, target - current));
        //fValueSetters[1].setRange(0, Math.min(current, target - current));
        getValueSetter(2).setValue(bet);
    }

    /**
     * This method handles the scroll events associated with changing the
     * initial fortune or the probability of success.
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        if (arg ==getValueSetter(2)) {
            bet = getValueSetter(2).getValueAsInt();
        } else if (arg == getValueSetter(1)) {
            p = getValueSetter(1).getValue();
            reset();
        } else if (arg == getValueSetter(0)) {
            initial = getValueSetter(0).getValueAsInt();
            rbGraph.setParameters(initial, target);
            reset();
        }
    }
}

