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
import java.awt.*;
import java.net.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class models the famous Monty Hall game. A diamond is behind one door,
 * jockers are behind the other two. The player first chooses a door. Then the
 * host opens a different door. The player can stay with her original choice or
 * switch to the remaining door.
 */
public class MontyHallGame extends Game implements ItemListener {
    //Variables
    public final static int STANDARD = 0, BLIND = 1;
    private int hostStrategy = STANDARD, runs = 0;
    private int host1, host2, player1, player2, w, s, g, stage;
    //Objects
    private JButton playButton = new JButton("Play");
    private MediaTracker tracker;
    private JComboBox hostJComboBox = new JComboBox();
    private Image jocker, diamond;
    private Door[] door = new Door[3];
    private JLabel definitionLabel = new JLabel(
            "G: Host shows jocker, S: Player switch, W: Win");
    private JLabel messageLabel = new JLabel(
            "Welcome to the Monty Hall Game.  Click play to start a game.");
    private IntervalData win = new IntervalData(-.5, 1.5, 1, "W");
    private Histogram winGraph = new Histogram(win, 0);
    private DataTable winTable = new DataTable(win, 0);

    /**
     * This method initializes the game, including the toolbar buttons, doors,
     * images
     */
    public MontyHallGame() {
        setName("Monty Hall Game");
        //Event listeners
        playButton.addActionListener(this);
        hostJComboBox.addItemListener(this);
        for (int i = 0; i < 3; i++) {
            door[i] = new Door();
            door[i].addMouseListener(this);
        }
        //Load images
        try {
            tracker = new MediaTracker(applet);
            jocker = applet.getImage(
                    new URL(applet.getCodeBase(), "images/cards/joker.jpg"));
            tracker.addImage(jocker, 0);
            diamond = applet.getImage(new URL(applet.getCodeBase(),"images/cards/diamonds1.jpg"));
            tracker.addImage(diamond, 1);
        } catch (Exception e) {
            ;
        }

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {}
        hostJComboBox.addItem("Host: Standard");
        hostJComboBox.addItem("Host: Blind");
        //Toolbars
        addTool(playButton);
        addTool(hostJComboBox);
        addTool(definitionLabel);
        addTool(messageLabel);

        //Graphs
        for (int i = 0; i < 3; i++)
            addGraph(door[i]);
        winGraph.showSummaryStats(0);
        winGraph.setMargins(35, 20, 20, 20);
        addGraph(winGraph);
        //Table
        winTable.showSummaryStats(0);
        addTable(winTable);
        reset();
    }

    /** This message resets the game */
    public void reset() {
        super.reset();
        for (int i = 0; i < 3; i++)
            door[i].close(String.valueOf(i + 1));
        messageLabel
                .setText("Welcome to the Monty Hall Game.  Click play to start a game.");
        getRecordTable().setText("Run\tG\tS\tW");
        win.reset();
        winGraph.repaint();
        winTable.update();

        System.out.println("MontyHallGame::OpenDoor::Reset()::jockerImage="
                + jocker);
        System.out.println("MontyHallGame::OpenDoor::Reset()::diamondImage="
                + diamond);
    }

    /** This method handles the events for the Play button */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playButton) {
            for (int i = 0; i < 3; i++)
                door[i].close(String.valueOf(i + 1));
            host1 = (int) (3 * Math.random());
            stage = 1;
            messageLabel.setText("Select a door.");
        } else super.actionPerformed(event);
        System.out
                .println("MontyHallGame::OpenDoor::actionPerformed()::jockerImage="
                        + jocker);
        System.out
                .println("MontyHallGame::OpenDoor::actionPerformed()::diamondImage="
                        + diamond);
    }

    /** This method handles the events for the host choice */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == hostJComboBox) {
            hostStrategy = hostJComboBox.getSelectedIndex();
            reset();
        }
    }

    /** This method handles events for the door clicks */
    public void mouseClicked(MouseEvent event) {
        System.out.println("MontyHallGame::OpenDoor::mouseClicked()::jockerImage="
                + jocker);
        System.out.println("MontyHallGame::OpenDoor::mouseClicked()::diamondImage="
                + diamond);

        for (int i = 0; i < 3; i++) {
            if ((event.getSource() == door[i]) & (stage == 1)) {
                player1 = i;
                door[i].close(String.valueOf(i + 1) + " Player");
                if ((hostStrategy == STANDARD) & (host1 != player1)) {
                    for (int j = 0; j < 3; j++)
                        if ((j != host1) & (j != player1)) host2 = j;
                } else {
                    if (Math.random() < 0.5) host2 = player1 - 1;
                    else host2 = player1 + 1;
                    if (host2 == -1) host2 = 2;
                    if (host2 == 3) host2 = 0;
                }
                if (host2 != host1) {
                    g = 1;
                    door[host2].open(jocker, String.valueOf(host2 + 1) + " Host");
                    System.out.println("MontyHallGame::OpenDoor::jockerImage="
                            + jocker);
                } else {
                    g = 0;
                    door[host2].open(diamond, String.valueOf(host2 + 1) + " Host");
                    System.out.println("MontyHallGame::OpenDoor::diamondImage="
                            + diamond);
                }
                stage = 2;
                messageLabel.setText("Do you want to switch?  Select a door.");
            } else if ((event.getSource() == door[i]) & (stage == 2) & (i != host2)) {
                player2 = i;
                if (player2 != player1) s = 1;
                else s = 0;
                door[player1].close(String.valueOf(player1 + 1));
                if (player2 != host1) {
                    w = 0;
                    door[player2].open(jocker, String.valueOf(player2 + 1)
                            + " Player");
                    System.out.println("MontyHallGame::OpenDoor::jockerImage="
                            + jocker);

                    try {
                        play("sounds/1.au");
                    } catch (Exception e) {
                        ;
                    }

                    messageLabel.setText("You lose!  Click play to start a game.");
                } else {
                    w = 1;
                    door[player2].open(diamond, String.valueOf(player2 + 1)
                            + " Player");
                    try {
                        play("sounds/1.au");
                    } catch (Exception e) {
                        ;
                    }
                    messageLabel.setText("You win!  Click play to start a game.");
                }
                runs++;
                win.setValue(w);
                winGraph.repaint();
                winTable.update();
                getRecordTable().append(
                        "\n" + runs + "\t" + g + "\t" + s + "\t" + w);
                stage = 3;
            }
        }
    }
}
