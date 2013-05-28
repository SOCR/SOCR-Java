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
import java.io.Serializable;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.JTable;
import javax.swing.Timer;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Die;
import edu.uah.math.devices.DiceBoard;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* The poker dice exeperiment consists of rolling 5 dice. The variable of interest
* is the type of hand: one pair, two pair, three of a kind, four of a kind, five of
* a kind, or nothing.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class PokerDiceExperiment extends Experiment implements Serializable{
	//Variables
	private int value, stopValue = -1, trial;
	private int[] v = new int[5];
 	//Objects
 	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X1", "X2", "X3", "X4", "X5", "V"});
	private DiceBoard diceBoard = new DiceBoard(5);
  	private RandomVariable hand = new RandomVariable(new FiniteDistribution(0, 6, 1, new double[]{720.0 / 7776, 3600.0 / 7776, 1800.0 / 7776, 1200.0 / 7776, 300.0 / 7776, 150.0 / 7776, 6.0 / 7776}), "V");
	private RandomVariableGraph handGraph = new RandomVariableGraph(hand);
	private RandomVariableTable handTable = new RandomVariableTable(hand);
	private Timer timer = new Timer(250, this);

	/**
	* This method initializes the experiment, including the record table, the random
	* variable graph and table, and the choice box for the stopping frequency.
	*/
	public void init(){
		super.init();
		setName("Poker Dice Experiment");
		//Dice Board
		diceBoard.setMinimumSize(new Dimension(100, 50));
		diceBoard.setLayout(new FlowLayout(FlowLayout.CENTER));
		addComponent(diceBoard, 0, 0, 1, 1, 10, 0);
		//Random variable graph
		handGraph.setMomentType(0);
		addComponent(handGraph, 1, 0, 1, 2);
		//Record table
		recordTable.setDescription("Xi: score of die i, V: hand type");
		addComponent(recordTable, 0, 1, 1, 2);
		//Random variable table
		handTable.setStatisticsType(0);
		addComponent(handTable, 1, 2, 1, 1);
		//Stop choices
		for (int i = 0; i < 7; i++)	getStopChoice().addItem("Stop at V = " + i);
		//Final actions
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
			+ "The poker dice experiment consists of rolling 5 fair dice. Random variable V denotes the\n"
			+ "value of the hand; V = 0: all distinct, V = 1: one pair, V = 2: two pair, V = 3: three\n"
			+ "of a kind, V = 4: full house, V = 5: four of a kind, V = 6: five of a kind. The die scores\n"
			+ " and the value V are recorded on each update in the first table. The density function of V is shown in\n"
			+ "blue in the graph and recorded in the second table. On each update, the empirical density\n"
			+ "function of V is shown in red in the graph and recorded in the second table. In the stop\n"
			+ "frequency list box, you can set the simulation to stop automatically when V is a particular\n"
			+ "value.";
	}

	/**
	* This method defines the experiment. The dice are rolled and the hand
	* evaluated.
	*/
	public void doExperiment(){
		super.doExperiment();
		diceBoard.roll();
		v = diceBoard.getValues();
		value = evaluateHand();
		hand.setValue(value);
		setStopNow(stopValue == value);
	}

	/**
	* This method runs the the experiment one time, adding additional sound and annimation.
	*/
	public void step(){
		stop();
		diceBoard.setRolled(false);
		diceBoard.roll();
		trial = 0;
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
	* This method resets the experiment, including the dice board, hand type random
	* variable, record table, and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		diceBoard.setRolled(false);
		hand.reset();
		handGraph.reset();
		handTable.reset();
	}

	/**
	* This method updates the display, including the dice board, record table,
	* and the graph and table for the hand type random variable.
	*/
	public void update(){
		super.update();
		diceBoard.setRolled(true);
		recordTable.addRecord(new double[]{getTime(), v[0], v[1], v[2], v[3], v[4], value});
		handGraph.repaint();
		handTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		handGraph.setShowModelDistribution(showModelDistribution);
		handGraph.repaint();
		handTable.setShowModelDistribution(showModelDistribution);
		handTable.repaint();
	}
	/**
	* This method handles events for the stop frequency choice box. The  experiment
	* can be stopped when the hand is a certain value.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == getStopChoice()){
			int j = getStopChoice().getSelectedIndex();
			if (j >= 5){
				setStopFreq(-1);
				stopValue = j - 5;
			}
			else super.itemStateChanged(e);
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the timer events associated with the step process. The dice are
	* shown one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < 5){
				Die die = diceBoard.getDie(trial);
				die.setRolled(true);
				//playnote(die.getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				v = diceBoard.getValues();
				evaluateHand();
				hand.setValue(value);
				recordTable.addRecord(new double[]{getTime(), v[0], v[1], v[2], v[3], v[4], value});
				handGraph.repaint();
				handTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}


	/**
	* This method evaluates the hand: 0 mothing, 1 one pair, 2 two pair, 3 three of a kind,
	* 4 full house, 5 four of a kind, 6 five of a kind.
	* @return the hand type
	*/
	public int evaluateHand(){
		int value;
		if(v[1] == v[0]){
			if(v[2] == v[1]){ // v[0] = v[1] = v[2]
				if(v[3] == v[2]){
				if (v[4] == v[3]) value = 6; // v[0] = v[1] = v[2] = v[3] = v[4]
				else value = 5;  //v[0] = v[1] = v[2] = v[3], v[4] distinct
				}
				else if (v[4] == v[2]) value = 5; //v[0] = v[1] = v[2] = v[4]. v[3] distinct
				else if (v[4] == v[3]) value = 4; //v[0] = v[1] = v[2], v[3], v[4] distinct
				else value = 3; //v[0] = v[1] = v[2], v[3], v[4] distinct
			}
			else if(v[3] == v[2]){ //v[0] = v[1], v[2] = v[3] distinct
				if(v[4] == v[1]) value = 4; //v[0] = v[1] = v[4], v[2] = v[3] Distinct
				else if(v[4] == v[2]) value = 4; //v[0] = v[1], v[2] = v[3] = v[4] Distinct
				else value = 2; //v[0] = v[1], v[2] = v[3], v[4] distinct
		 	}
			else if(v[3] == v[1]){ //v[0] = v[1] = v[3], v[2] Distinct
				if(v[4] == v[2]) value = 4; //v[0] = v[1] = v[3], v[2] = v[4] Distinct
				else if(v[4] == v[3]) value = 5; //v[0] = v[1] = v[3] = v[4]
				else value = 3; //v[0] = v[1] = v[3], v[2], v[4] Distinct
		 	}
			else if(v[4] == v[1]) value = 3; //v[0] = v[1] = v[4], v[2], v[3] Distinct
			else if(v[4] == v[2]) value = 2; //v[0] = v[1], v[2] = v[4], v[3] Distinct
			else if(v[4] == v[3]) value = 2; //v[0] = v[1], v[3] = v[4], v[2] distinct
			else value = 1; //v[0] = v[1], v[2], v[3], v[4] distinct
		}
		else if(v[2] == v[1]){ //v[0], v[1] = v[2] distinct
			if(v[3] == v[0]){ //v[0] = v[3], v[1] = v[2] distinct
				if(v[4] == v[3]) value = 4; //v[0] = v[3] = v[4], v[1] = v[2] distinct
				else if(v[4] == v[2]) value = 4; //v[0] = v[3], v[1] = v[2] = v[4] distinct
				else value = 2; //v[0] = v[3], v[1] = v[2], v[4] distinct
			}
			else if(v[3] == v[2]){ //v[0], v[1] = v[2] = v[3] distinct
				if(v[4] == v[3]) value = 5; //v[0], v[1] = v[2] = v[3] = v[4] distinct
				else if(v[4] == v[0]) value = 4; //v[0] = v[4], v[1] = v[2] = v[3] Distinct
				else value = 3; //v[0], v[1] = v[2] = v[3], v[4] distinct
			}
			else if(v[4] == v[0]) value = 2; //v[0] = v[4], v[1] = v[2], v[3] distinct
			else if(v[4] == v[2]) value = 3; //v[0], v[1] = v[2] = v[4], v[3] Distinct
			else if(v[4] == v[3]) value = 2; //v[0], v[1] = v[2], v[3] = v[4] distinct
			else value = 1; //v[0], v[1] = v[2], v[3], v[4] distinct
		}
		else if(v[2] == v[0]){ //v[0] = v[2], v[1] distinct
			if(v[3] == v[2]){ //v[0] = v[2] = v[3], v[1] distinct
				if(v[4] == v[3]) value = 5; //v[0] = v[2] = v[3] = v[4], v[1] distinct
				else if(v[4] == v[1]) value = 4; //v[0] = v[2] = v[3], v[1] = v[4] distinct
				else value = 3; //v[0] = v[2] = v[3], v[1], v[4] distinct
		 	}
			else if(v[3] == v[1]){ //v[0] = v[2], v[1] = v[3] distinct
				if(v[4] == v[2]) value = 4; //v[0] = v[2] = v[4], v[1] = v[3] distinct
				else if(v[4] == v[3]) value = 4; //v[0] = v[2], v[1] = v[3] = v[4] distinct
				else value = 2; //v[0] = v[2], v[1] = v[3], v[4] distinct
		 	}
			else if(v[4] == v[2]) value = 3; //v[0] = v[2] = v[4], v[1], v[3] distinct
			else if(v[4] == v[1]) value = 2; //v[0] = v[2], v[1] = v[4], v[3] distinct
			else if(v[4] == v[3]) value = 2; //v[0] = v[2], v[1], v[3] = v[4] distinct
			else value = 1; //v[0] = v[2], v[1], v[3], v[4] distinct
		}
		else if(v[3] == v[0]){ //v[0] = v[3], v[1], v[2] distinct
			if(v[4] == v[3]) value = 3; //v[0] = v[3] = v[4], v[1], v[2] distinct
			else if(v[4] == v[1]) value = 2; //v[0] = v[3], v[1] = v[4], v[2] distinct
			else if(v[4] == v[2]) value = 2; //v[0] = v[40, v[1], v[2] = v[4] distinct
			else value = 1; //v[0] = v[3], v[1], v[2], v[4] distinct
	  	}
		else if(v[3] == v[1]){ //v[0], v[1] = v[3], v[2] distinct
			if(v[4] == v[3]) value = 3; //v[0], v[1] = v[3] = v[4], v[2] distinct
			else if(v[4] == v[0]) value = 2; //v[0] = v[4], v[1] = v[3], v[2] distinct
			else if(v[4] == v[2]) value = 2; //v[0], v[1] = v[3], v[2] = v[4] distinct
			else value = 1; //v[0], v[1] = v[3], v[2], v[4] distinct
		}
		else if(v[3] == v[2]){ //v[0], v[1], v[2] = v[3] distint
			if(v[4] == v[0]) value = 2; //v[0] = v[4], v[1], v[2] = v[3] distinct
			else if(v[4] == v[1]) value = 2; //v[0], v[1] = v[4], v[2] = v[3] distinct
			else if(v[4] == v[3]) value = 3; //v[0], v[1], v[2] = v[3] = v[4] distinc
			else value = 1; //v[0], v[1], v[2] = v[3], v[4], distinct
		}
		else if(v[4] == v[0]) value = 1; //v[0] = v[4], v[1], v[2], v[3] distinct
		else if(v[4] == v[1]) value = 1; //v[0], v[1] = v[4], v[2], v[3] distinct
		else if(v[4] == v[2]) value = 1; //v[0], v[1], v[2] = v[4], v[3] distinct
		else if(v[4] == v[3]) value = 1; //v[0], v[1], v[2], v[3] = v[4] Distinct
		else value = 0; //nothing
		return value;
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

