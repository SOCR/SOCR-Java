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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.Timer;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Card;
import edu.uah.math.devices.CardHand;
import edu.uah.math.devices.Card;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the experiment of dealing a standard 5-card poker hand.
* The random variable of interest is the type of hand: no value, one pair, two
* pair, three of a kind, straight, flush, full house, four of a kind, straight
* flush.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class PokerExperiment extends Experiment implements Serializable{
	//Variables
	private int value, stopValue = -1, trial;
	private int[] s = new int[5], v = new int[5];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "V"});
	private FiniteDistribution handDist =
		new FiniteDistribution(0, 8, 1, new double[]{0.501177, 0.422569, 0.047539,
		0.021129, 0.003925, 0.001965, 0.001441, 0.000240, 0.000015});
	private RandomVariable handType = new RandomVariable(handDist, "V");
	private RandomVariableGraph handGraph = new RandomVariableGraph(handType);
	private RandomVariableTable handTable = new RandomVariableTable(handType);
	private CardHand hand = new CardHand(5);
	private Timer timer = new Timer(300, this);

	/**
	* This method initializes the experiment, including the cards, random
	* variable graph and table, and the record table.
	*/
	public void init(){
		super.init();
		setName("Poker Experiment");
		//Stop choices
		for (int i = 0; i < 9; i++)	getStopChoice().addItem("Stop at V = " + i);
		//Poker Hand
		hand.setMinimumSize(new Dimension(100, 120));
		hand.setLayout(new FlowLayout(FlowLayout.CENTER));
		addComponent(hand, 0, 0, 3, 1, 10, 0);
		//Record Table
		recordTable.setDescription("V: hand type");
		addComponent(recordTable, 0, 1, 1, 1);
		//Hand Table
		handTable.setStatisticsType(0);
		addComponent(handTable, 1, 1, 1, 1);
		//Hand Graph
		handGraph.setMomentType(0);
		addComponent(handGraph, 2, 1, 1, 1);
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
			+ "The poker experiment consists of dealing 5 cards at random from a standard deck of 52\n"
			+ "cards. V denotes the value of the hand; V = 0: no value, V = 1: one pair, V = 2: two pair\n"
			+ "V = 3: three of a kind, V = 4: straight, V = 5: flush, V = 6: full house, V = 7: four\n"
			+ "of a kind, V = 8: straight flush. The value V is recorded on each update in the first\n"
			+ "table. The density function of V is shown in blue in the graph and recorded in the\n"
			+ "second table. On each update, the empirical density function of V is shown in red in\n"
			+ "the graph and recorded in the second table. In the stop frequency list box, you can set\n"
			+ "the simulation to stop automatically when V is a particular value.";
	}

	/**
	* This method handles events for the stop frequency choice box. The experiment can
	* stopped when the hand is of a specified type.
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
 	* This method defines the experiment. The 5 cards are dealt and the value
	* of the hand evaluated.
	*/
	public void doExperiment(){
		super.doExperiment();
		hand.deal();
		for (int i = 0; i < 5; i++){
			s[i] = hand.getCard(i).getSuit();
			v[i] = hand.getCard(i).getValue();
		}
		value = evaluateHand();
		handType.setValue(value);
		setStopNow(value == stopValue);
	}

	/**
	* This method runs the the experiment one time, and adds additional sound and annimation.
	*/
	public void step(){
		stop();
		hand.setFaceUp(false);
		hand.deal();
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
	* This method resets the experiment, including the cards, the random
	* variable graph and table, and the record table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		//Hide the cards
		hand.setFaceUp(false);
		//Reset the random variable and its graph and table
		handType.reset();
		handGraph.reset();
		handTable.reset();
	}

	/**
	* This method updates the display, including the cards, the random varaible
	* graph and table, and the record table.
	*/
	public void update(){
		super.update();
		hand.setFaceUp(true);
		recordTable.addRecord(new double[]{getTime(), value});
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
	* This method handles the timer events associated with the step process. The cards are
	* shown one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < 5){
				Card card = hand.getCard(trial);
				card.setFaceUp(true);
				//playnote(card.getCardNumber());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				for (int i = 0; i < 5; i++){
					s[i] = hand.getCard(i).getSuit();
					v[i] = hand.getCard(i).getValue();
				}
				value = evaluateHand();
				handType.setValue(value);
				recordTable.addRecord(new double[]{getTime(), value});
				handGraph.repaint();
				handTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method evalutes the type of hand: 0 no value, 1 one pair, 2 two pair,
	* 3 three of a kind, 4 straight, 5 flush, 6 full house, 7 four of a kind, or
	* 8 straight flush.
	* @return the type of hand
	*/
	public int evaluateHand(){
		boolean straight = false;
		int value, minValue;
		if(v[1] == v[0]){
			if(v[2] == v[1]){ // v[0] = v[1] = v[2]
				if(v[3] == v[2]) value = 7; // v[0] = v[1] = v[2] = v[3]
				else if(v[4] == v[3]) value = 6;  //v[0] = v[1] = v[2], v[3] = v[4] <> v[2]
				else value = 3; //v[0] = v[1] = v[2], v[3], v[4] distinct
			}
			else if(v[3] == v[2]){ //v[0] = v[1], v[2] = v[3] distinct
				if(v[4] == v[1]) value = 6; //v[0] = v[1] = v[4], v[2] = v[3] Distinct
				else if(v[4] == v[2]) value = 6; //v[0] = v[1], v[2] = v[3] = v[4] Distinct
				else value = 2; //v[0] = v[1], v[2] = v[3], v[4] distinct
			}
			else if(v[3] == v[1]){ //v[0] = v[1] = v[3], v[2] Distinct
				if(v[4] == v[2]) value = 6; //v[0] = v[1] = v[3], v[2] = v[4] Distinct
				else if(v[4] == v[3]) value = 7; //v[0] = v[1] = v[3] = v[4]
				else value = 3; //v[0] = v[1] = v[3], v[2], v[4] Distinct
			}
			else if(v[4] == v[1]) value = 3; //v[0] = v[1] = v[4], v[2], v[3] Distinct
			else if(v[4] == v[2]) value = 2; //v[0] = v[1], v[2] = v[4], v[3] Distinct
			else if(v[4] == v[3]) value = 2; //v[0] = v[1], v[3] = v[4], v[2] distinct
			else value = 1; //v[0] = v[1], v[2], v[3], v[4] distinct
		}
		else if(v[2] == v[1]){ //v[0], v[1] = v[2] distinct
			if(v[3] == v[0]){ //v[0] = v[3], v[1] = v[2] distinct
				if(v[4] == v[3]) value = 6; //v[0] = v[3] = v[4], v[1], v[2] distinct
				else if(v[4] == v[2]) value = 6; //v[0] = v[3], v[1] = v[2] = v[4] distinct
				else value = 2; //v[0] = v[3], v[1] = v[2], v[4] distinct
			 }
			else if(v[3] == v[2]){ //v[0], v[1] = v[2] = v[3] distinct
				if(v[4] == v[3]) value = 7; //v[0], v[1] = v[2] = v[3] = v[4] distinct
				else if(v[4] == v[0]) value = 6; //v[0] = v[4], v[1] = v[2] = v[3] Distinct
				else value = 3; //v[0], v[1] = v[2] = v[3], v[4] distinct
			 }
			else if(v[4] == v[0]) value = 2; //v[0] = v[4], v[1] = v[2], v[3] distinct
			else if(v[4] == v[2]) value = 3; //v[0], v[1] = v[2] = v[4], v[3] Distinct
			else if(v[4] == v[3]) value = 2; //v[0], v[1] = v[2], v[3] = v[4] distinct
			else value = 1; //v[0], v[1] = v[2], v[3], v[4] distinct
		}
		else if(v[2] == v[0]){ //v[0] = v[2], v[1] distinct
			if(v[3] == v[2]){ //v[0] = v[2] = v[3], v[1] distinct
				if(v[4] == v[3]) value = 7; //v[0] = v[2] = v[3] = v[4], v[1] distinct
				else if(v[4] == v[1]) value = 6; //v[0] = v[2] = v[3], v[1] = v[4] distinct
				else value = 3; //v[0] = v[2] = v[3], v[1], v[4] distinct
			 }
			else if(v[3] == v[1]){ //v[0] = v[2], v[1] = v[3] distinct
				if(v[4] == v[2]) value = 6; //v[0] = v[2] = v[4], v[1] = v[3] distinct
				else if(v[4] == v[3]) value = 6; //v[0] = v[2], v[1] = v[3] = v[4] distinct
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
		else value = 0; //nothing so far
		//Test for flush
		if(value == 0){
			if((s[0] == s[1]) & (s[1] == s[2]) & (s[2] == s[3]) & (s[3] == s[4])) value = 5; //flush
		}
		// Test for straight
		if ((value == 0) | (value == 5)){
			minValue = 14;
			for (int i = 0; i < 5; i++) if (v[i] < minValue) minValue = v[i];
			for (int i = minValue; i < minValue + 5; i++){
				straight = false;
				for (int j = 0; j < 5; j++){
				   if (v[j] == i){
					  straight = true;
					  break;
				   }
				}
				if(!straight) break;
			}
			if ((minValue == 1) & (!straight)){
				for (int i = 10; i < 14; i++){
					straight = false;
					for (int j = 0; j < 5; j++){
						if (v[j] == i){
							straight = true;
							break;
						}
				   	}
					if(!straight) break;
				}
			}
		}
		if (straight){
			if (value == 5) value = 8;
			else value = 4;
		}
		return value;
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

