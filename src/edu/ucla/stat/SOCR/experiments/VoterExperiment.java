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
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the voter experiment. Each member of a rectangular lattice
 * of voters can, at any time, be in one of a finite number of states,
 * represented by colors. At each time, a voter is chosen at random and then a
 * neighbor of the voter is chosen with various probabilities. The state of the
 * chosen voter is then changed to the state of the chosen neighbor.
 */
public class VoterExperiment extends Experiment {
    private final static String[] colorName = { "Black", "Blue", "Cyan", "Green",
            "Purple", "Orange", "Pink", "Red", "White", "Yellow" };
    //Variables
    private int colors = 10;
    private double[] prob = new double[4];
    private boolean stopWhenNewDeath = false;
    //Objects
    private Voters voters = new Voters(10, 5, 50);
    private JComboBox voterChoice = new JComboBox();
    private JComboBox colorChoice = new JComboBox();
    private JLabel mouseLabel = new JLabel("		 ");

    /**
     * This method initializes the experiment, including the set of voters, the
     * toolbar with choices and labels.
     */
    public VoterExperiment() {
        setName("Voter Experiment");
        //Voters
        voters.addMouseListener(this);
        voters.addMouseMotionListener(this);
        voters.setEditState(0);
        //Stop Choice
        JComboBox stopChoice = getStopChoice();
        stopChoice.addItem("Stop when Color Dies");
        stopChoice.setSelectedIndex(5);
        setStopFreq(-1);
        stopWhenNewDeath = true;
        //Voter choice
        voterChoice.addItem("Voters: 10 by 5");
        voterChoice.addItem("Voters: 20 by 10");
        voterChoice.addItem("Voters: 50 by 25");
        voterChoice.addItemListener(this);
        //Color choice
        colorChoice.addItemListener(this);
        for (int i = 0; i < colors; i++)
            colorChoice.addItem("Set State: " + colorName[i]);
        addTool2(voterChoice);
        addTool2(colorChoice);
        addTool2(mouseLabel);
        //Graphs
        addGraph(voters);
        reset();
    }

    /** This method defines the experiment. */
    public void doExperiment() {
        super.doExperiment();
        voters.doExperiment();
        setStopNow((stopWhenNewDeath & voters.newDeath()) | voters.consensus());
    }

    /** This method resets the experiment. */
    public void reset() {
        super.reset();
        voters.reset();
        voters.repaint();
        String recordText = "Time";
        for (int i = 0; i < 10; i++)
            recordText = recordText + "\t" + colorName[i];
        getRecordTable().setText(recordText);
        recordText = "\n" + getTime();
        for (int i = 0; i < 10; i++)
            recordText = recordText + "\t" + voters.stateCount(i);
        getRecordTable().append(recordText);
    }

    /**
     * This method updates the experiment, including the voters and the record
     * table.
     */
    public void update() {
        super.update();
        voters.update();
        String recordText = "";
        for (int i = 0; i < 10; i++)
            recordText = recordText + "\t" + voters.stateCount(i);
        getRecordTable().append(recordText);
    }

    /**
     * This method handles the events associated with the stop choice and the
     * voter choice.
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == getStopChoice()) {
            int j = getStopChoice().getSelectedIndex();
            if (j == 5) {
                setStopFreq(-1);
                stopWhenNewDeath = true;
            } else super.itemStateChanged(event);
        } else if (event.getSource() == voterChoice) {
            switch (voterChoice.getSelectedIndex()) {
            case 0:
                voters.setParameters(10, 5, 50);
                break;
            case 1:
                voters.setParameters(20, 10, 25);
                break;
            case 2:
                voters.setParameters(50, 25, 10);
                break;
            }
            reset();
        } else if (event.getSource() == colorChoice) voters
                .setEditState(colorChoice.getSelectedIndex());
        else super.itemStateChanged(event);
    }

    /**
     * This method handles the mouse events that correspond moving the mouse
     * over the voters.
     */
    public void mouseMoved(MouseEvent event) {
        if (event.getSource() == voters) mouseLabel.setText(voters.getX() + ", "
                + voters.getY());
        else super.mouseMoved(event);
    }

    /**
     * This method handles the mouse events that correspond to clicking on a
     * voter.
     */
    public void mouseClicked(MouseEvent event) {
        if (event.getSource() == voters) {
            voters.update();
            String recordText = "\n" + getTime();
            for (int i = 0; i < 10; i++)
                recordText = recordText + "\t" + voters.stateCount(i);
            getRecordTable().append(recordText);
        } else super.mouseMoved(event);
    }
}

