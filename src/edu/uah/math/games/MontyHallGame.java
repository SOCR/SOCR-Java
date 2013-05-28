/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.games;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.Domain;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Door;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.DataTable;

/**
* This class models the famous Monty Hall game.  A car is behind one door,
* goats are behind the other two.  The player first chooses a door. Then the host
* opens a different door. The player can stay with her original choice or switch to
* the remaining door.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MontyHallGame extends Game implements Serializable{
	//Variables
	public final static int STANDARD = 0, BLIND = 1;
	private int hostStrategy = STANDARD, runs = 0;
	private int host1, host2, player1, player2, w, s, g, stage;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Game", "G", "S", "W"});
	private JButton playButton = new JButton();
	private JComboBox hostChoice = new JComboBox();
	private Door[] door = new Door[3];
	private JLabel messageLabel = new JLabel("Welcome to the Monty Hall Game.  Click play to start a game.");
	private IntervalData win = new IntervalData(new Domain(0, 1, 1, Domain.DISCRETE), "W");
	private Histogram winGraph = new Histogram(win);
	private DataTable winTable = new DataTable(win);

	/**
	* This method initializes the game, including the toolbar buttons, doors,
	* images.
	*/
	public void init(){
		super.init();
		setName("Monty Hall Game");
		//Doors
		for (int i = 0; i < 3; i++){
			door[i] = new Door();
			door[i].addMouseListener(this);
			door[i].setToolTipText("Door " + (i + 1));
			addComponent(door[i], i, 1, 1, 1);
		}
		//Play button
		playButton.addActionListener(this);
		playButton.setIcon(new ImageIcon(getClass().getResource("step.gif")));
		playButton.setToolTipText("Play");
		//Host choice
		hostChoice.addItemListener(this);
		hostChoice.setToolTipText("Host strategy");
		hostChoice.addItem("Standard");
		hostChoice.addItem("Blind");
		//Toolbars
		addTool(playButton);
		addTool(hostChoice);
		//Label
		addComponent(messageLabel, 0, 0, 3, 1, 0, 0);
		//Record Table
		recordTable.setDescription("G: host shows goat, S: player switches, W: player wins");
		addComponent(recordTable, 0, 2, 1, 1);
		//Win Table
		winTable.setStatisticsType(0);
		addComponent(winTable, 1, 2, 1, 1);
		//Win Graph
		winGraph.setMinimumSize(new Dimension(150, 150));
		winGraph.setStatisticsType(0);
		winGraph.setMargins(35, 20, 20, 20);
		addComponent(winGraph, 2, 2, 1, 1);

		//Final Actions
		validate();
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "In the Monty Hall game, there are three doors; a car is behind one and goats are behind the\n"
			+ "other two. Click on the Play button to start a new game. Then select a door by clicking.\n"
			+ "The host will open one of the other two doors. Finally select a door again (you may stick\n"
			+ "with your original selection or switch). This door is opened and you either win or lose.\n"
			+ "Either of two host strategies can be selected with the list box. In the standard strategy,\n"
			+ "the host always opens a door with a goat); in the blind strategy, the host randomly opens\n"
			+ "one of the two doors available to him. Variable G indicates the event that the host opened\n"
			+ "a door with a goat; variable S indicates the event that you switched doors from your first\n"
			+ "selection to your second; and variable W indicates the event that you win the car.\n"
			+ "The empirical distribution of W is shown in the distribution graph and is recorded in the\n"
			+ "distribution table.";
	}

	/**
	* This message resets the game by closing the doors and resetting the text message,
	* win graph, win table, and record table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		for (int i = 0; i < 3; i++) door[i].close(String.valueOf(i + 1));
		messageLabel.setText("Welcome to the Monty Hall Game.  Click play to start a game.");
		win.reset();
		winGraph.repaint();
		winTable.reset();
	}

	/**
	* This method handles the events for the Play button.  The doors are closed,
	* the host's first choice made, and the appropriate message displayed.
	*/
	public void actionPerformed(ActionEvent event){
		if (event.getSource() == playButton){
			for (int i = 0; i < 3; i++) door[i].close(String.valueOf(i + 1));
			host1 = (int)(3 * Math.random());
			stage = 1;
			messageLabel.setText("Select a door.");
		}
		else super.actionPerformed(event);
	}

	/**
	* This method handles the events for the choice of host strategy, blind or
	* standard.
	*/
	public void itemStateChanged(ItemEvent event){
		if (event.getSource() == hostChoice){
			hostStrategy = hostChoice.getSelectedIndex();
			reset();
		}
		else super.itemStateChanged(event);
	}

	/**
	* This method handles events for the door clicks. If this is the player's first
	* choice, the label on the door is changed to indicate the choice, and the
	* host's second choice is made. If this is the player's second choice, the outcome
	* of the game is determined and the door opened.
	*/
	public void mouseClicked(MouseEvent event){
		for (int i = 0; i < 3; i++){
			if ((event.getSource() == door[i]) & (stage == 1)){
				player1 = i;
				door[i].close(String.valueOf(i + 1) + " Player");
				if ((hostStrategy == STANDARD) & (host1 != player1)){
				   for (int j = 0; j < 3; j++) if ((j != host1) & (j != player1)) host2 = j;
				}
				else{
					if (Math.random() < 0.5) host2 = player1 - 1;
					else host2 = player1 + 1;
					if (host2 == -1) host2 = 2;
					if (host2 == 3) host2 = 0;
				}
				if (host2 != host1){
					g = 1;
					door[host2].open(Door.GOAT, String.valueOf(host2 + 1) + " Host");
				}
				else{
					g = 0;
					door[host2].open(Door.CAR, String.valueOf(host2 + 1) + " Host");
				}
				stage = 2;
				messageLabel.setText("Do you want to switch?  Select a door.");
			}
			else if ((event.getSource() == door[i]) & (stage == 2) & (i != host2)){
				player2 = i;
				if (player2 != player1) s = 1; else s = 0;
				door[player1].close(String.valueOf(player1 + 1));
				if (player2 != host1){
					w = 0;
					door[player2].open(Door.GOAT, String.valueOf(player2 + 1) + " Player");
					messageLabel.setText("You lose!  Click play to start a game.");
				}
				else{
					w = 1;
					door[player2].open(Door.CAR, String.valueOf(player2 + 1) + " Player");
					messageLabel.setText("You win!  Click play to start a game.");
				}
				//playnote(w);
				runs++;
				win.setValue(w);
				winGraph.repaint();
				winTable.repaint();
				recordTable.addRecord(new double[]{runs, g,  s, w});
				stage = 3;
			}
		}
	}
}
