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
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.Timer;
import java.io.Serializable;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Die;
import edu.uah.math.devices.DiceBoard;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the classic chuck-a-luck experiment that consists of rolling three dice.
* The player bets on a number from 1 to 6.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version June 2002
*/
public class ChuckALuckExperiment extends Experiment implements Serializable{
	//Variables
	private int betScore = 1, score, matches, win, trial;
	private double[] prob = {125.0 / 216, 0.0, 75.0 / 216, 15.0 / 216, 1.0 / 216};
	//Objects
	private RecordTable recordTable = new RecordTable(new String[] {"Run", "X1", "X2", "X3", "W"});
	private JToolBar toolBar = new JToolBar("Parmaeter Toolbar");
	private DiceBoard diceBoard = new DiceBoard(3);
	private JComboBox betChoice = new JComboBox();
	private FiniteDistribution winDist = new FiniteDistribution(-1, 3, 1, prob);
	private RandomVariable winRV = new RandomVariable(winDist, "W");
	private RandomVariableGraph winGraph = new RandomVariableGraph(winRV);
	private RandomVariableTable winTable = new RandomVariableTable(winRV);
	private Timer timer = new Timer(200, this);

	/**
	* This method initialize the experiment, including the recrod table, the bet
	* choice, the toolbar, the dice board, and the random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Chuck A Luck Experiment");
		//Bet choice
		betChoice.addItemListener(this);
		for(int i = 0; i < 6; i++) betChoice.addItem("Bet on " + (i + 1));
		betChoice.setToolTipText("Bet Number");
		//toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(betChoice);
		addToolBar(toolBar);
		//Dice Board
		diceBoard.setMinimumSize(new Dimension(100, 50));
		diceBoard.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		addComponent(diceBoard, 0, 0, 1, 1, 0, 0);
		//Random variable graph
		winGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(winGraph, 1, 0, 1, 2);
		//Record table
		recordTable.setDescription("Xi: score of die i, W: profit");
		addComponent(recordTable, 0, 1, 1, 2);
		//Random variable table
		addComponent(winTable, 1, 2, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "In the game of chuck-a-luck, a gambler chooses an integer from 1 to 6 and then three fair\n"
			+ "dice are rolled. If exactly k dice show the gambler's number, the payoff is k:1.\n"
			+ "The three dice are shown in the first picture box on each update. Random variable W \n"
			+ " gives the gambler's net profit on a $1 bet and is recorded in the first table on each\n"
			+ "update. The density and moments of W are shown in blue in the second picture box\n"
			+ "and are recorded in the second table. On each update, the empirical density of W is \n"
			+ "shown in red in the second picture box and recorded in the second table.";
	}

	/**
	* This method defines the experiment. The dice are rolled and the outcome of the
	* player's bet determined.
	*/
	public void doExperiment(){
		super.doExperiment();
		matches = 0;
		diceBoard.roll();
		for (int i = 0; i < 3; i++) if (diceBoard.getValues(i) == betScore) matches++;
		if (matches == 0) win = -1;
		else win = matches;
		winRV.setValue(win);
	}

	/**
	* This method runs the the experiment one time, and adds additional sound and
	* annimation.
	*/
	public void step(){
		stop();
		diceBoard.setRolled(false);
		trial = 0;
		matches = 0;
		timer.start();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the dice board, the record table, the
	* win random variable, and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		diceBoard.setRolled(false);
		winRV.reset();
		winGraph.reset();
		winTable.reset();
	}

	/**
	* This method updates the display, including the dice board, the record table,
	* the random variable graph and table.
	*/
	public void update(){
		super.update();
		diceBoard.setRolled(true);
		recordTable.addRecord(new double[]{getTime(), diceBoard.getValues(0), diceBoard.getValues(1), diceBoard.getValues(2), win});
		winGraph.repaint();
		winTable.repaint();
	}
	public void graphUpdate(){
		super.update();
	//	diceBoard.setRolled(true);
	//	recordTable.addRecord(new double[]{getTime(), diceBoard.getValues(0), diceBoard.getValues(1), diceBoard.getValues(2), win});
		winGraph.setShowModelDistribution(showModelDistribution);
		winGraph.repaint();
		winTable.setShowModelDistribution(showModelDistribution);
		winTable.repaint();
	}
	/**
	* This method handles the choice events associated with the choice of the player's
	* bet number.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == betChoice){
			betScore = betChoice.getSelectedIndex() + 1;
			reset();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the events associated with the step timer. The dice are shown
	* one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < 3){
				Die die = diceBoard.getDie(trial);
				die.roll();
				die.setRolled(true);
				if (die.getValue() == betScore) matches++;
				//playnote(die.getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				if (matches == 0) win = -1;
				else win = matches;
				winRV.setValue(win);
				recordTable.addRecord(new double[]{getTime(), diceBoard.getValues(0), diceBoard.getValues(1), diceBoard.getValues(2), win});
				winGraph.repaint();
				winTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

