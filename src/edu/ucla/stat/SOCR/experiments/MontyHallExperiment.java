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
import java.util.*;

import javax.swing.*;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * This class models the famous Monty Hall experiment. There are three doors; a
 * diamond is behind one, jockers are behind the other two. The player selects a
 * door and then the host opens another door. The player is given the option of
 * keeping her original choice or switching to the other unopened door.
 */
public class MontyHallExperiment extends Experiment {
    //Variables
    public final static int STANDARD = 0, BLIND = 1;
    private int hostStrategy = STANDARD, host1, host2, player1, player2, w, s, g;
    private double p = 1;
    //Objects
    private MediaTracker tracker;
    private Image jocker, diamond;
    private Door[] door = new Door[3];
    private JPanel toolbar = new JPanel();
    private JComboBox hostJComboBox = new JComboBox();
    private JLabel definitionLabel = new JLabel(
            "G: Host shows jocker, S: Player switch, W: Win");
    private BernoulliDistribution winDist = new BernoulliDistribution(2.0 / 3);
    private RandomVariable win = new RandomVariable(winDist, "W");
    private RandomVariableGraph winGraph = new RandomVariableGraph(win);
    private RandomVariableTable winTable = new RandomVariableTable(win);

    /**
     * This method initializes the experiment, including the toolbar with the
     * drop down boxes and scrollbar, the random variable table and graph, the
     * record table, and the doors.
     */
    public MontyHallExperiment() {
        setName("Monty Hall Experiment");
        //Listeners
        hostJComboBox.addItemListener(this);
        createValueSetter("Switch p", Distribution.CONTINUOUS, 0, 1);
        try {
            tracker = new MediaTracker(applet);
            jocker = applet.getImage(new URL(applet.getCodeBase(), "images/cards/"
                    + "joker.jpg"));
            tracker.addImage(jocker, 0);
            diamond = applet.getImage(new URL(applet.getCodeBase(), "images/cards/"
                    + "diamonds1.jpg"));
            tracker.addImage(diamond, 1);
        } catch (Exception e) {
            ;
        }

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {}
        //Doors
        for (int i = 0; i < 3; i++)
            door[i] = new Door();
        //Host choice
        hostJComboBox.addItem("Host: Standard");
        hostJComboBox.addItem("Host: Blind");
        //Toolbar
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(hostJComboBox);
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        //Graphs
        for (int i = 0; i < 3; i++)
            addGraph(door[i]);
        winGraph.showMoments(0);
        addGraph(winGraph);
        //Table
        winTable.showMoments(0);
        addTable(winTable);
    }

    /**
     * This method defines the experiment. The diamond is placed behind a
     * randomly chosen door and then the player chooses a door at random. The
     * host opens one of the two doors not chosen by the player.
     */
    public void doExperiment() {
        super.doExperiment();
        host1 = (int) (3 * Math.random()); //Door containing diamond
        player1 = (int) (3 * Math.random()); //Player's first choice
        //Select door for host to open:
        if ((hostStrategy == STANDARD) & (host1 != player1)) {
            for (int i = 0; i < 3; i++)
                if ((i != host1) & (i != player1)) host2 = i;
        } else {
            if (Math.random() < 0.5) host2 = player1 - 1;
            else host2 = player1 + 1;
            if (host2 == -1) host2 = 2;
            if (host2 == 3) host2 = 0;
        }
        //Determine if a jocker is behind the door opened by the host.
        if (host2 != host1) g = 1;
        else g = 0;
        //Determine if the player switches
        if (Math.random() < p) {
            for (int i = 0; i < 3; i++)
                if ((i != player1) & (i != host2)) player2 = i;
            s = 1;
        } else {
            player2 = player1;
            s = 0;
        }
        //Determine if the player wins
        if (player2 == host1) w = 1;
        else w = 0;
        win.setValue(w);
    }

    /**
     * This method resets the experiment, including the record table, the doors,
     * the random variable graph and table, and the record table
     */
    public void reset() {
        super.reset();
        for (int i = 0; i < 3; i++)
            door[i].close(String.valueOf(i + 1));
        getRecordTable().append("\tG\tS\tW");
        win.reset();
        winGraph.reset();
        winTable.reset();
    }

    /**
     * This method updates the displays, including the record table, the doors,
     * the random variable graph and table, and the record table
     */
    public void update() {
        super.update();
        if (g == 1) door[host2].open(jocker, String.valueOf(host2 + 1) + " Host");
        else door[host2].open(diamond, String.valueOf(host2 + 1) + " Host");
        if (w == 1) door[player2].open(diamond, String.valueOf(player2 + 1)
                + " Player");
        else door[player2].open(jocker, String.valueOf(player2 + 1) + " Player");
        for (int i = 0; i < 3; i++)
            if ((i != host2) & (i != player2))
                    door[i].close(String.valueOf(i + 1));
        winGraph.repaint();
        winTable.update();
        getRecordTable().append("\t" + g + "\t" + s + "\t" + w);
    }

    /**
     * This method runs the experiment one time, and plays a sound depending on
     * the outcome
     */
    public void step() {
        doExperiment();
        update();
        try {
            if (w == 0) play("sounds/0.au");
            else play("sounds/1.au");
        } catch (Exception e) {
            ;
        }
    }

    /**
     * This method handles the scrollbar event for adjusting the probability of
     * switching
     */
    public void update(Observable o, Object arg) {
        p = getValueSetter(0).getValue();
        setDistribution();
    }

    /**
     * This method handles the choice event that determines whether the host
     * follows the standard strategy or the blind strategy
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == hostJComboBox) {
            hostStrategy = hostJComboBox.getSelectedIndex();
            setDistribution();
        } else super.itemStateChanged(event);
    }

    /**
     * This method sets the parameters of the win distribution, when the
     * parameters of the experiment have changed.
     */
    public void setDistribution() {
        if (hostStrategy == STANDARD) winDist.setProbability((1 + p) / 3);
        else winDist.setProbability(1.0 / 3);
        reset();
    }
}

