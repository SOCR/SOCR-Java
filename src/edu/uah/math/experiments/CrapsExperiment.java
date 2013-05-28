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
import java.awt.event.ItemEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.Timer;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.DiceBoard;

/**
* This class simulates the basic casino craps game.  A variety of bets can be chosen,
* and the random variable of interest it the player's profit on a unit bet.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CrapsExperiment extends Experiment implements Serializable{
	//Constants
	public final static int PASS = 0, DONTPASS = 1, FIELD = 2, CRAPS = 3, CRAPS2 = 4, CRAPS3 = 5,
		CRAPS12 = 6, SEVEN = 7, ELEVEN = 8, BIG6 = 9, BIG8 = 10, HARDWAY4 = 11, HARDWAY6 = 12,
		HARDWAY8 = 13, HARDWAY10 = 14, CONTINUE = -2;;
	//Variables
	private int x, y, u, v, point1, point2, win, rolls, betType = PASS;
	private double[] prob = new double[]{251.0 / 495, 0, 244.0 / 495};
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "Y", "U", "V", "N", "W"});
	private DiceBoard diceBoard = new DiceBoard(2);
	private JComboBox betChoice = new JComboBox();
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private FiniteDistribution dist = new FiniteDistribution(-1, 1, 1, prob);
	private RandomVariable profitRV = new RandomVariable(dist, "W");
	private RandomVariableGraph profitGraph = new RandomVariableGraph(profitRV);
	private RandomVariableTable profitTable = new RandomVariableTable(profitRV);
	private Timer timer = new Timer(500, this);

	/**
	* This method initializes the experiment, including the record table, bet choice,
	* toolbar, dice board, and the random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Craps Experiment");
		//Bet choice
		betChoice.addItemListener(this);
		betChoice.setToolTipText("Bet");
		betChoice.addItem("Pass");
		betChoice.addItem("Don't Pass");
		betChoice.addItem("Field");
		betChoice.addItem("Craps");
		betChoice.addItem("Craps 2");
		betChoice.addItem("Craps 3");
		betChoice.addItem("Craps 12");
		betChoice.addItem("Seven");
		betChoice.addItem("Eleven");
		betChoice.addItem("Big 6");
		betChoice.addItem("Big 8");
		betChoice.addItem("Hardway 4");
		betChoice.addItem("Hardway 6");
		betChoice.addItem("Hardway 8");
		betChoice.addItem("Hardway 10");
		//toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(betChoice);
		addToolBar(toolBar);
		//Dice board
		diceBoard.setMinimumSize(new Dimension(150, 50));
		diceBoard.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
		addComponent(diceBoard, 0, 0, 1, 1, 0, 0);
		//Profit Graph
		profitGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(profitGraph, 1, 0, 1, 2);
		//Record Table
		recordTable.setDescription("(X, Y): first throw, (U, V): last throw, N: number of rolls, W: profit");
		addComponent(recordTable, 0, 1, 1, 2);
		//Profit Table
		addComponent(profitTable, 1, 2, 1, 1);
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
			+ "In the game of craps, the shooter rolls a pair of fair die. The rules are as follows:\n"
			+ "If the shooter rolls a sum of 7 or 11 on the first roll, she wins. If the shooter rolls\n"
			+ "a sum of 2, 3, or 12 on the first roll, she loses. If the shooter rolls a sum of 4, 5, 6,\n"
			+ "8, 9, or 10, this number becomes the shooter's point. The shooter continues to roll the\n"
			+ "dice until the sum is either the point (in which case she wins) or 7 (in which case she loses).\n"
			+ "Any of the following bets can be selected: Pass: this is the bet that the shooter will win,\n"
			+ "and pays 1:1. Don't Pass: this is the bet that the shooter will lose, except that an initial\n"
			+ "roll of 12 is excluded (that is, the shooter loses, but the don't pass bettor neither wins\n"
			+ "nor loses). The bet pays 1:1. Field: this is a bet on a single throw. It pays 1:1 if 3, 4, 9,\n"
			+ "10, or 11 is thrown, 2:1 if 2 or 12 is thrown, and loses otherwise. Craps: this is a bet that 2,\n"
			+ "3, or 12 will come up on a single throw, and pays 7:1. Craps 2: this is a bet that 2 will\n"
			+ "come up on a single throw, and pays 30:1. Craps 3: this is a bet that 3 will come up on\n"
			+ "a single throw, and pays 15:1. Craps 12: this is a bet that 12 will come up on a single throw,\n"
			+ "and pays 30:1. Seven: this is a bet that 7 will come up on a single throw, and pays 4:1.\n"
			+ "Eleven: this is a bet that 11 will come up on a single throw, and pays 15:1. Big 6. This is\n"
			+ "a bet that 6 will be thrown before 7, and pays 1:1. Big 8: this is a bet that 8 will be\n"
			+ "thrown before 7, and pays 1:1. Hardway 4: this is a bet that (2, 2) will be thrown before 7\n"
			+ "or any other 4, and pays 7:1. Hardway 6. this is a bet that (3, 3) will be thrown before 7\n"
			+ "or any other 6, and pays 9:1. Hardway 8: this is a bet that (4, 4) will be thrown before 7\n"
			+ "or any other 8, and pays 9:1. Hardway 10: this is a bet that (5, 5) will be thrown before 7\n"
			+ "or any other 10, and pays 7:1.";
	}

	/**
	* This method defines the experiment: the dice are rolled, and depending on the bet,
	* and the outcome, the dice may be rolled again. The outcome of the bet is computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		firstThrow();
		while(win == CONTINUE) nextThrow();
		profitRV.setValue(win);
	}

	/**
	* This method runs the the experiment one time, and adds sounds depending on the outcome
	* of the experiment.
	*/
	public void step(){
		stop();
		firstThrow();
		diceBoard.setValues(0, x);
		diceBoard.setValues(1, y);
		diceBoard.setRolled(true);
		//playnote(point1);
		if (win == CONTINUE) timer.start();
		else{
			super.doExperiment();
			profitRV.setValue(win);
			update();
		}
	}

	/**
	* This method starts run mode, by first turning off the step timer if necessary.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the experiment, by first stopping the step timer and then calling the usual
	* stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the dice board, profit random variable,
	* record table, and the random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		diceBoard.setRolled(false);
		profitRV.reset();
		profitGraph.reset();
		profitTable.reset();
	}

	/**
	* This method updates the display, including the dice board, record table, and the
	* random variable graph and table.
	*/
	public void update(){
		super.update();
		//Show the first throw
		diceBoard.setValues(0, x);
		diceBoard.setValues(1, y);
		diceBoard.setRolled(true);
		//If necessary, show the last throw
		if (rolls > 1){
			diceBoard.setValues(0, u);
			diceBoard.setValues(1, v);
			diceBoard.setRolled(true);
		}
		//Update other components
		recordTable.addRecord(new double[]{getTime(), x, y, u, v, rolls, win});
		profitGraph.repaint();
		profitTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		profitGraph.setShowModelDistribution(showModelDistribution);
		profitGraph.repaint();
		profitTable.setShowModelDistribution(showModelDistribution);
		profitTable.repaint();

	}
	/**
	* This method makes and evaluates the first throw of the dice.
	*/
	private void firstThrow(){
		rolls = 1;
		//Throw the dice
		x = (int)Math.ceil(6 * Math.random());
		y = (int)Math.ceil(6 * Math.random());
		u = x; v = y;
		point1 = x + y;
		//Evaluate the throw
		win = CONTINUE;
		switch(betType){
		case PASS:
			if (point1 == 7 | point1 == 11) win = 1;
			else if (point1 == 2 | point1 == 3 | point1 == 12) win = -1;
			break;
		case DONTPASS:
			if (point1 == 7 | point1 == 11) win = -1;
			else if (point1 == 2 | point1 == 3) win = 1;
			else if (point1 == 12) win = 0;
			break;
		case FIELD:
			if (point1 == 3 | point1 == 4 | point1 == 9 | point1 == 10 | point1 == 11) win = 1;
			else if (point1 == 2 | point1 == 12) win = 2;
			else win = -1;
			break;
		case CRAPS:
			if (point1 == 2 | point1 == 3 | point1 == 12) win = 7;
			else win = -1;
			break;
		case CRAPS2:
			if (point1 == 2) win = 30;
			else win = -1;
			break;
		case CRAPS3:
			if (point1 == 3) win = 15;
			else win = -1;
			break;
		case CRAPS12:
			if (point1 == 12) win = 30;
			else win = -1;
			break;
		case SEVEN:
			if (point1 == 7) win = 4;
			else win = -1;
			break;
		case ELEVEN:
			if (point1 == 11) win = 15;
			else win = -1;
			break;
		case BIG6:
			if (point1 == 6) win = 1;
			else if (point1 == 7) win = -1;
			break;
		case BIG8:
			if (point1 == 8) win = 1;
			else if (point1 == 7) win = -1;
			break;
		case HARDWAY4:
			if (x == 2 & y == 2) win = 7;
			else if (point1 == 7 | point1 == 4) win = -1;
			break;
		case HARDWAY6:
			if (x == 3 & y == 3) win = 9;
			else if (point1 == 7 | point1 == 6) win = -1;
			break;
		case HARDWAY8:
			if (x == 4 & y == 4) win = 9;
			else if (point1 == 7 | point1 == 8) win = -1;
			break;
		case HARDWAY10:
			if (x == 5 & y == 5) win = 7;
			else if (point1 == 7 | point1 == 10) win = -1;
			break;
		}
	}

	/**
	* This method makes and evaluates throws after the first throw.
	*/
	private void nextThrow(){
		//Throw the dice
		u = (int)Math.ceil(6 * Math.random());
		v = (int)Math.ceil(6 * Math.random());
		rolls++;
		point2 = u + v;
		//Evaluate
		switch(betType){
		case PASS:
			if (point2 == point1) win = 1;
			else if (point2 == 7) win = -1;
			break;
		case DONTPASS:
			if (point2 == point1) win = -1;
			else if (point2 == 7) win = 1;
			break;
		case BIG6:
			if (point2 == 6) win = 1;
			else if (point2 == 7) win = -1;
			break;
		case BIG8:
			if (point2 == 8) win = 1;
			else if (point2 == 7) win = -1;
			break;
		case HARDWAY4:
			if (u == 2 & v == 2) win = 7;
			else if (point2 == 7 | point2 == 4) win = -1;
			break;
		case HARDWAY6:
			if (u == 3 & v == 3) win = 9;
			else if (point2 == 6 | point2 == 7) win = -1;
			break;
		case HARDWAY8:
			if (u == 4 & v == 4) win = 9;
			else if (point2 == 8 | point2 == 7) win = -1;
			break;
		case HARDWAY10:
			if (u == 5 & v == 5) win = 7;
			else if (point2 == 7 | point2 == 10) win = -1;
			break;
		}
	}

	/**
	* This method handles the events associated with the step timer.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			nextThrow();
			diceBoard.setValues(0, u);
			diceBoard.setValues(1, v);
			diceBoard.setRolled(true);
			//playnote(point2);
			if (win != CONTINUE){
				timer.stop();
				super.doExperiment();
				profitRV.setValue(win);
				recordTable.addRecord(new double[]{getTime(), x, y, u, v, rolls, win});
				profitGraph.repaint();
				profitTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}



	/**
	* This method handles the events associated with the choice of the
	* player's bet.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == betChoice){
			betType = betChoice.getSelectedIndex();
			switch(betType){
			case PASS:
				prob = new double[3];
				prob[0] = 251.0 / 495; prob[2] = 244.0 / 495;
				dist.setParameters(-1, 1, 1, prob);
				betChoice.setToolTipText("Wins if 7 or 11 on first throw, or point before 7");
				break;
			case DONTPASS:
				prob = new double[3];
				prob[0] = 244.0 / 495; prob[1] = 1.0 / 36; prob[2] = 949.0 / 1980;
				dist.setParameters(-1, 1, 1, prob);
				betChoice.setToolTipText("Wins if 2 or 3 on first roll, or 7 before point");
				break;
			case FIELD:
				prob = new double[4];
				prob[0] = 5.0 / 9; prob[2] = 7.0 / 18; prob[3] = 1.0 / 18;
				dist.setParameters(-1, 2, 1, prob);
				betChoice.setToolTipText("Wins on 3, 4, 9, 10, 11; 2 and 12 pay double");
				break;
			case CRAPS:
				prob = new double[9];
				prob[0] = 8.0 / 9; prob[8] = 1.0 / 9;
				dist.setParameters(-1, 7, 1, prob);
				betChoice.setToolTipText("Wins on 2, 3, or 12");
				break;
			case CRAPS2:
				prob = new double[32];
				prob[0] = 35.0 / 36; prob[31] = 1.0 / 36;
				dist.setParameters(-1, 30, 1, prob);
				betChoice.setToolTipText("Wins on 2");
				break;
			case CRAPS3:
				prob = new double[17];
				prob[0] = 17.0 / 18; prob[16] = 1.0 / 18;
				dist.setParameters(-1, 15, 1, prob);
				betChoice.setToolTipText("Wins on 3");
				break;
			case CRAPS12:
				prob = new double[32];
				prob[0] = 35.0 / 36; prob[31] = 1.0 / 36;
				dist.setParameters(-1, 30, 1, prob);
				betChoice.setToolTipText("Wins on 12");
				break;
			case SEVEN:
				prob = new double[6];
				prob[0] = 5.0 / 6; prob[5] = 1.0 / 6;
				dist.setParameters(-1, 4, 1, prob);
				betChoice.setToolTipText("Wins on 7");
				break;
			case ELEVEN:
				prob = new double[17];
				prob[0] = 17.0 / 18; prob[16] = 1.0 / 18;
				dist.setParameters(-1, 15, 1, prob);
				betChoice.setToolTipText("Wins on 11");
				break;
			case BIG6:
				prob = new double[3];
				prob[0] = 6.0 / 11; prob[2] = 5.0 / 11;
				dist.setParameters(-1, 1, 1, prob);
				betChoice.setToolTipText("Wins if 6 before 7");
				break;
			case BIG8:
				prob = new double[3];
				prob[0] = 6.0 / 11; prob[2] = 5.0 / 11;
				dist.setParameters(-1, 1, 1, prob);
				betChoice.setToolTipText("Wins if 8 before 7");
				break;
			case HARDWAY4:
				prob = new double [9];
				prob[0] = 8.0 / 9; prob[8] = 1.0 / 9;
				dist.setParameters(-1, 7, 1, prob);
				betChoice.setToolTipText("Wins if (2, 2) before 7 or other 4");
				break;
			case HARDWAY6:
				prob = new double [11];
				prob[0] = 10. / 11; prob[10] = 1.0 / 11;
				dist.setParameters(-1, 9, 1, prob);
				betChoice.setToolTipText("Wins if (3, 3) before 7 or other 6");
				break;
			case HARDWAY8:
				prob = new double [11];
				prob[0] = 10. / 11; prob[10] = 1.0 / 11;
				dist.setParameters(-1, 9, 1, prob);
				betChoice.setToolTipText("Wins if (4, 4) before 7 or other 8");
				break;
			case HARDWAY10:
				prob = new double [9];
				prob[0] = 8.0 / 9; prob[8] = 1.0 / 9;
				dist.setParameters(-1, 7, 1, prob);
				betChoice.setToolTipText("Wins if (5, 5) before 7 or other 10");
				break;
			}
			reset();
		}
		else super.itemStateChanged(e);
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

