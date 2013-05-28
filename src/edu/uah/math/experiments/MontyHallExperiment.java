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
package edu.uah.math.experiments;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.io.Serializable;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.Door;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the famous Monty Hall experiment. There are three doors;
* a car is behind one, goats are behind the other two. The player selects a door
* and then the host opens another door. The player is given the option of keeping
* her original choice or switching to the other unopened door.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MontyHallExperiment extends Experiment implements Serializable{
	//Variables
	public final static int STANDARD = 0, BLIND = 1;
	private int hostStrategy = STANDARD, host1, host2, player1, player2, win, switched, goat, stage;
	private double p = 1;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "G", "S", "W"});
	private Door[] door = new Door[3];
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox hostChoice = new JComboBox();
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Switch probability", "p");
	private BernoulliDistribution winDist = new BernoulliDistribution(2.0 / 3);
	private RandomVariable winRV = new RandomVariable(winDist, "W");
	private RandomVariableGraph winGraph = new RandomVariableGraph(winRV);
	private RandomVariableTable winTable = new RandomVariableTable(winRV);
	private Timer timer = new Timer(750, this);

	/**
	* This method initializes the experiment, including the toolbar with the
	* drop down boxes and scrollbar, the random variable table and graph, the
	* record table, and the doors.
	*/
	public void init(){
		super.init();
		setName("Monty Hall Experiment");
		//Probability scroll
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Doors
		for (int i = 0; i < 3; i++){
			door[i] = new Door();
			door[i].setToolTipText("Door " + (i + 1));
			addComponent(door[i], i, 0, 1, 1);
		}
		//Host choice
		hostChoice.addItemListener(this);
		hostChoice.setToolTipText("Host behavior");
		hostChoice.addItem("Standard");
		hostChoice.addItem("Blind");
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(hostChoice);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		//Record Table
		recordTable.setDescription("G: host shows goat, S: player switches, W: player wins");
		addComponent(recordTable, 0, 1, 1, 1);
		//Win Table
		winTable.setStatisticsType(0);
		addComponent(winTable, 1, 1, 1, 1);
		//Win Graph
		winGraph.setMinimumSize(new Dimension(150, 150));
		winGraph.setMomentType(0);
		addComponent(winGraph, 2, 1, 1, 1);
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
			+ "In the Monty Hall experiment, there are three doors; a car is behind one and goats are\n"
			+ "behind the other two. The player selects a door and then the host opens one of the other\n"
			+ "two doors. The player can then stay with her original selection or switch to the remaining\n"
			+ "unopened door. The door finally selected by the player is opened and she either wins or\n"
			+ "loses. Either of two host strategies can be selected with the list box. In the standard\n"
			+ "strategy, the host always opens a door with a goat); in the blind strategy, the host\n"
			+ "randomly opens one of the two doors available to him. The player switches doors with\n"
			+ "probability p, that can be varied with a scroll bar. Variable G indicates the event that\n"
			+ "the host opened a door with a goat; variable S indicates the event that the player\n"
			+ "switched doors; and variable W indicates the event that the player won the car.\n"
			+ "The density of W is shown in the distribution graph and the distribution table.";
	}

	/**
	* This method defines the experiment. The car is placed behind a randomly
	* chosen door and then the player chooses a door at random. The host opens one
	* of the two doors not chosen by the player.
	*/
	public void doExperiment(){
		super.doExperiment();
		selectDoors();
	}

	/**
	* This method selects the doors. The car is placed behind a randomly
	* chosen door and then the player chooses a door at random. The host opens one
	* of the two doors not chosen by the player.
	*/
	public void selectDoors(){
		host1 = (int)(3 * Math.random());  //Door containing car
		player1 = (int)(3 * Math.random());  //Player's first choice
		//Select door for host to open:
		if ((hostStrategy == STANDARD) & (host1 != player1)){
			for (int i = 0; i < 3; i++) if ((i != host1) & (i != player1)) host2 = i;
		}
		else{
			if (Math.random() < 0.5) host2 = player1 - 1;
			else host2 = player1 + 1;
			if (host2 == -1) host2 = 2;
			if (host2 == 3) host2 = 0;
		}
		//Determine if a goat is behind the door opened by the host.
		if (host2 != host1) goat = 1; else goat = 0;
		//Determine if the player switches
		if (Math.random() < p){
			for (int i = 0; i < 3; i++) if ((i != player1) & (i != host2)) player2 = i;
			switched = 1;
		}
		else{
			player2 = player1;
			switched = 0;
		}
		//Determine if the player wins
		if (player2 == host1) win = 1;
		else win = 0;
		winRV.setValue(win);
	}

	/**
	* This method runs the experiment one time, adding sounds and delays.
	*/
	public void step(){
		stop();
		selectDoors();
		for (int i = 0; i < 3; i++) door[i].close(String.valueOf(i + 1));
		stage = 1;
		timer.start();
	}

	/**
	* This method starts run mode, by first turning off the step timer if necessary.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the experiment, by first stopping the step timer if necessary and then
	* calling the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the record table, the doors,
	* the random variable graph and table, and the record table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		for (int i = 0; i < 3; i++) door[i].close(String.valueOf(i + 1));
		winRV.reset();
		winGraph.reset();
		winTable.reset();
	}

	/**
	* This method updates the displays, including the record table, the doors,
	* the random variable graph and table, and the record table.
	*/
	public void update(){
		super.update();
		if (goat == 1) door[host2].open(Door.GOAT, String.valueOf(host2 + 1) + " Host");
		else door[host2].open(Door.CAR, String.valueOf(host2 + 1) + " Host");
		if (win == 1) door[player2].open(Door.CAR, String.valueOf(player2 + 1) + " Player");
		else door[player2].open(Door.GOAT, String.valueOf(player2 + 1) + " Player");
		for (int i = 0; i < 3; i++) if ((i != host2) & (i != player2)) door[i].close(String.valueOf(i + 1));
		winGraph.repaint();
		winTable.repaint();
		recordTable.addRecord(new double[]{getTime(), goat, switched, win});
	}
	public void graphUpdate(){
		super.update();
		winGraph.setShowModelDistribution(showModelDistribution);
		winGraph.repaint();
		winTable.setShowModelDistribution(showModelDistribution);
		winTable.repaint();
	}

	/**
	* This method handles the scrollbar event for adjusting the probability
	* of switching.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			setDistribution();
		}
	}

	/**
	* This method handles the choice event that determines whether the host
	* follows the standard strategy or the blind strategy.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == hostChoice){
			hostStrategy = hostChoice.getSelectedIndex();
			setDistribution();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the events associated with the step timer.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (stage == 1){
				door[player1].close(String.valueOf(player1 + 1) + " Player");
				//playnote(1);
				stage = 2;
			}
			else if (stage == 2){
				if (goat == 1) door[host2].open(Door.GOAT, String.valueOf(host2 + 1) + " Host");
				else door[host2].open(Door.CAR, String.valueOf(host2 + 1) + " Host");
				//playnote(1);
				stage = 3;
			}
			else if (stage == 3){
				door[player1].close(String.valueOf(player1 + 1));
				door[player2].close(String.valueOf(player2 + 1) + " Player");
				//playnote(1);
				stage = 4;
			}
			else if (stage == 4){
				timer.stop();
				super.doExperiment();
				if (win == 1) door[player2].open(Door.CAR, String.valueOf(player2 + 1) + " Player");
				else door[player2].open(Door.GOAT, String.valueOf(player2 + 1) + " Player");
				if (win == 0) ; //playnote(0);
				else ;  //playnote(4);
				winGraph.repaint();
				winTable.repaint();
				recordTable.addRecord(new double[]{getTime(), goat, switched, win});
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the parameters of the win distribution, when the parameters
	* of the experiment have changed.
	*/
	public void setDistribution(){
		if (hostStrategy == STANDARD) winDist.setProbability((1 + p) / 3);
		else winDist.setProbability(1.0 / 3);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

