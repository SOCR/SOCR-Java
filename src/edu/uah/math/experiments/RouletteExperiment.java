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
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.Timer;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.RouletteWheel;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.FiniteDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the casino roulette experiment.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RouletteExperiment extends Experiment implements Serializable{
	//Constants
	public final static int ONE = 0, TWO = 1, THREE = 2, FOUR = 3, SIX = 4, 
			TWELVE = 5, EIGHTEEN = 6, RED=7, BLACK=8;
	//Variables
	private int score, spinIndex, scoreIndex, spinCount, win, betType = ONE;
	private double[] prob;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X", "W"});
	private RouletteWheel wheel = new RouletteWheel();
	private JComboBox betChoice = new JComboBox();
	private JToolBar toolBar = new JToolBar("Parmaeter Toolbar");
	private FiniteDistribution dist = new FiniteDistribution();
	private RandomVariable winRV = new RandomVariable(dist, "W");
	private RandomVariableGraph winGraph = new RandomVariableGraph(winRV);
	private RandomVariableTable winTable = new RandomVariableTable(winRV);
	private Timer timer = new Timer(10, this);

	/**
	* This method initializes the experiment by setting up the toolbar with the bet
	* choice, and initializing the roulette wheel.
	*/
	public void init(){
		super.init();
		setName("Roulette Experiment");
		//Bet choice
		betChoice.addItemListener(this);
		betChoice.setToolTipText("Bet");
		betChoice.addItem("Bet on 1");
		betChoice.addItem("Bet on 1, 2");
		betChoice.addItem("Bet on 1, 2, 3");
		betChoice.addItem("Bet on 1, 2, 4, 5");
		betChoice.addItem("Bet on 1-6");
		betChoice.addItem("Bet on 1-12");
		betChoice.addItem("Bet on 1-18");
		betChoice.addItem("Bet on RED");
		betChoice.addItem("Bet on BLACK");
		
		//toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(betChoice);
		addToolBar(toolBar);
		//Graphs
		addComponent(wheel, 0, 0, 1, 1);
		winGraph.setMinimumSize(new Dimension(250, 150));
		addComponent(winGraph, 1, 0, 1, 1);
		//Tables
		recordTable.setDescription("X: wheel score, W: win");
		addComponent(recordTable, 0, 1, 1, 1);
		winTable.setMinimumSize(new Dimension(250, 150));
		addComponent(winTable, 1, 1, 1, 1);
		//Final actions
		setDistribution();
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
			+ "The American roulette wheel has 38 slots numbered 00, 0, and 1-36. Slots 00 and 0 are\n"
			+ "Green. Half of the slots numbered 1-36 are Red and half are Black. The experiment consists\n"
			+ "of rolling a ball in a groove in the wheel; the ball eventually falls randomly into one\n"
			+ "of the 38 slots. The roulette wheel is shown in the left graph panel; the ball is shown\n"
			+ "on each update. One of seven different bets can be selected from the list box:\n"
			+ "\t Bet on 1: this is an example of a straight bet, and bet pays 35:1. \n"
			+ "\t Bet on 1, 2: this is an example of a split bet, and pays 17:1. \n"
			+ "\t Bet on 1, 2, 3: this is an example of a row bet, and bet pays 11:1. \n"
			+ "\t Bet on 1, 2, 4, 5: this is an example of 4-number bet, and pays 8:1.\n"
			+ "\t Bet on 1-6: this is an example of a 2-row bet, and pays 5:1. \n"
			+ "\t Bet on 1-12: this is an example of a 12-number bet, and pays 2:1. \n"
			+ "\t Bet on 1-18: this is an example of an 18-number bet, and pays 1:1. \n"
			+ "\t Bet on RED: this is an example of betting on a red-colored number, and pays 1:1. \n"
			+ "\t Bet on BLACK: this is an example of betting on a black-colored number, and pays 1:1 \n"
			+ "On each update, the outcome X is shown graphically in the first panel\n"
			+ "and recorded numerically in the first table. Random variable W gives the net winnings\n"
			+ "for the chosen bet; this variable is recorded in the first table on each update.\n"
			+ "The density function and moments of W are shown in blue in the distribution graph and\n"
			+ "are recorded in the distribution table. On each update, the empirical density and moments\n"
			+ "of W are shown in red in the distribution graph and are recorded in the distribution table.";
	}

	/**
	* This method defines the exeperiment.  The wheel is spun and the result of the
	* bet determined.
	*/
	public void doExperiment(){
		super.doExperiment();
		score = wheel.spin();
		evaluate();
		winRV.setValue(win);
	}

	/**
	* This method runs the experiment one time and then plays a sound, depending on the
	* outcome.
	*/
	public void step(){
		stop();
		spinCount = 0;
		spinIndex = 0;
		score = wheel.spin();
		scoreIndex = wheel.getScoreIndex();
		wheel.setScoreIndex(spinIndex);
		wheel.setBallDrawn(true);
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
	* This method stops the experiment, by first stopping the step timer and then calling the usual
	* stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method resets the experiment, including the roulette wheel, the random
	* variable graph and table, and the record table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		wheel.setBallDrawn(false);
		winRV.reset();
		winGraph.reset();
		winTable.reset();
	}


	/**
	* This method updates the experiment, including the roulette wheel, the record
	* table and the random variable graph and table.
	*/
	public void update(){
		super.update();
		wheel.setBallDrawn(true);
		recordTable.addRecord(new double[]{getTime(), score, win});
		winGraph.repaint();
		winTable.repaint();
	}
	
	public void graphUpdate(){
		super.update();
		winGraph.setShowModelDistribution(showModelDistribution);
		winGraph.repaint();
		winTable.setShowModelDistribution(showModelDistribution);
		winTable.repaint();
	}
	/**
	* This method sets the distribution when the bet has changed.
	*/
	public void itemStateChanged(ItemEvent event){
		if (event.getSource() == betChoice){
			betType = betChoice.getSelectedIndex();
			setDistribution();
		}
		else super.itemStateChanged(event);
	}

	/**
	* This method handles the events associated with the step timer.
	* @param e the event
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			//The experiment is complete. Stop spinning and evaluate
			if (spinCount >= 111 & spinIndex == scoreIndex) {
				timer.stop();
				super.doExperiment();
				evaluate();
				winRV.setValue(win);
				update();
				//playnote(win + 1);
			}
			//Keep spinning
			else{
				spinIndex++;
				if (spinIndex == 38) spinIndex = 0;
				wheel.setScoreIndex(spinIndex);
				spinCount++;
				wheel.setBallDrawn(true);
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the distribution of the win random variable.
	*/
	public void setDistribution(){
		switch(betType){
		case ONE:
			prob = new double[37];
			prob[0] = 37.0 / 38; prob[36] = 1.0 / 38;
			dist.setParameters(-1, 35, 1, prob);
			break;
		case TWO:
			prob = new double[19];
			prob[0] = 36.0 / 38; prob[18] = 2.0 / 38;
			dist.setParameters(-1, 17, 1, prob);
			break;
		case THREE:
			prob = new double[13];
			prob[0] = 35.0 / 38; prob[12] = 3.0 / 38;
			dist.setParameters(-1, 11, 1, prob);
			break;
		case FOUR:
			prob = new double[10];
			prob[0] = 34.0 / 38; prob[9] = 4.0 / 38;
			dist.setParameters(-1, 8, 1, prob);
			break;
		case SIX:
			prob = new double[7];
			prob[0] = 32.0 / 38; prob[6] = 6.0 / 38;
			dist.setParameters(-1, 5, 1, prob);
			break;
		case TWELVE:
			prob = new double[4];
			prob[0] = 26.0 / 38; prob[3] = 12.0 / 38;
			dist.setParameters(-1, 2, 1, prob);
			break;
		case EIGHTEEN:
			prob = new double[3];
			prob[0] = 20.0 / 38; prob[2] = 18.0 / 38;
			dist.setParameters(-1, 1, 1, prob);
			break;
		case RED:
			prob = new double[3];
			prob[0] = 20.0 / 38; prob[2] = 18.0 / 38;
			dist.setParameters(-1, 1, 1, prob);
			break;
		case BLACK:
			prob = new double[3];
			prob[0] = 20.0 / 38; prob[2] = 18.0 / 38;
			dist.setParameters(-1, 1, 1, prob);
			break;
		}
		reset();
	}

	/**
	* This method evaluates win random variable.
	*/
	public void evaluate(){
		switch(betType){
		case ONE:
			if (score == 1) win = 35;
			else win = -1;;
			break;
		case TWO:
			if (score == 1 | score == 2) win = 17;
			else win = -1;
			break;
		case THREE:
			if (score >=1 & score <= 3) win = 11;
			else win = -1;
			break;
		case FOUR:
			if (score == 1 | score == 2 | score == 4 | score == 5) win = 8;
			else win = -1;
			break;
		case SIX:
			if (score >= 1 & score <= 6) win = 5;
			else win = -1;
			break;
		case TWELVE:
			if (score >= 1 & score <= 12) win = 2;
			else win = -1;
			break;
		case EIGHTEEN:
			if (score >= 1 & score <= 18) win = 1;
			else win = -1;
			break;
		case RED:
			if (score==1 || score==3 || score==5 || score==7 || score==9 || score==12 || score==14
					|| score==16 || score==18 || score==19 || score==21 || score==23 || score==25 
					|| score==27 || score==30 || score==32 || score==34 || score==36) 
				win = 1;
			else win = -1;
			break;
		case BLACK:
			if (score==2 || score==4 || score==6 || score==8 || score==10 || score==11 || score==13
					|| score==15 || score==17 || score==20 || score==22 || score==24 || score==26
					|| score==28 || score==29 || score==31 || score==33 || score==35) 
				win = 1;
			else win = -1;
			break;		
		}
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

