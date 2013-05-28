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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.Ball;
import edu.uah.math.devices.Urn;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.BirthdayDistribution;
import edu.uah.math.distributions.RandomVariable;


/**
* This class models the birthday experiment: a sample of a specified
* size is chosen with replacement from a population of a specified size. The
* random variables of interest are the number of distinct values in the sample and the indicator variable that
* indicates a duplicate sample value.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BirthdayExperiment extends Experiment implements Serializable{
	//Variables
	private int popSize = 365, sampleSize = 20, occupiedCount, stage;
	private double p = 0.411;
	//Objects
	private JComboBox rvChoice = new JComboBox();
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "V", "I"});
	private Urn urn = new Urn(sampleSize, 26);
	private JToolBar toolBar = new JToolBar();
	private Parameter popScroll = new Parameter(1, 400, 1, popSize, "Population size", "m");
	private Parameter sampleScroll = new Parameter(1, 100, 1, sampleSize, "Sample size", "n");
	private BirthdayDistribution occupiedDist = new BirthdayDistribution(popSize, sampleSize);
	private RandomVariable occupiedRV = new RandomVariable(occupiedDist, "V");
	private BernoulliDistribution matchDist = new BernoulliDistribution(p);
	private RandomVariable matchRV = new RandomVariable(matchDist, "I");
	private RandomVariableGraph rvGraph = new RandomVariableGraph(occupiedRV);
	private RandomVariableTable rvTable = new RandomVariableTable(occupiedRV);
	private Timer timer = new Timer(100, this);


	/**
	* This method initializes the experiment, including the set up of the
	* toolbar with scrollbars, the panel containing the balls, the random
	* variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Birthday Experiment");
		//Random variable choice
		rvChoice.addItemListener(this);
		rvChoice.addItem("V");
		rvChoice.addItem("I");
		rvChoice.setToolTipText("V: number of distinct sample values, I: duplicate sample values");
		//Sliders
		popScroll.getSlider().addChangeListener(this);
		sampleScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rvChoice);
		toolBar.add(popScroll);
		toolBar.add(sampleScroll);
		addToolBar(toolBar);
		//Urn
		urn.setMinimumSize(new Dimension(100, 100));
		addComponent(urn, 0, 0, 1, 1);
		//Occupied Graph
		//rvGraph.setMinimumSize(new Dimension(100, 100));
		//addComponent(rvGraph, 1, 0, 1, 1);
		
		// New
		rvGraph.setMinimumSize(new Dimension(200, 100));
		addComponent(rvGraph, 1, 0, 2, 1, 20, 10);
		
		//Record Table
		recordTable.setDescription("V: number of distinct sample values, I: duplicate sample values");
		addComponent(recordTable, 0, 1, 1, 1);
		//Occupied table
		addComponent(rvTable, 1, 1, 1, 1);
		//Final actions
		validate();
		reset();
	}

	/**
	* This method provides basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The birthday experiment is to draw a random sample of n balls with replacement\n"
			+ "from a population of m balls, numbered from 1 to m. We are interested in two random variables:\n\n"
			+ "\t 1. Variable V that gives the number of distinct values in the sample, and \n"
			+ "\t 2. Indicator variable I that indicates a duplicate sample Value.\n\n"
			+"\t Typically, one is interested if a match has occurred (I=1)!\n"
			+"During each run sample of balls is shown in the first graph; a ball that duplicates a previously chosen\n"
			+ "ball is shown in RED (I=1), and otherwise is shown in GREEN (I=0). The parameters m and n\n"
			+ "can be varied with scroll bars.";
	}

	/**
	* This method handles the scrollbar events for adjusting the population
	* size and sample size.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == popScroll.getSlider()){
			popSize = (int)popScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			urn.setBallCount(sampleSize);
			setParameters();
		}
	}

	/**
	* This method handles the choice events associated with the random variable choice.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == rvChoice){
			if (rvChoice.getSelectedIndex() == 0){
				rvGraph.setRandomVariable(occupiedRV);
				rvTable.setRandomVariable(occupiedRV);
				rvGraph.setMomentType(RandomVariableGraph.MSDS);
				rvTable.setStatisticsType(RandomVariableTable.MSD);
			}
			else{
				rvGraph.setRandomVariable(matchRV);
				rvTable.setRandomVariable(matchRV);
				rvGraph.setMomentType(RandomVariableGraph.NONE);
				rvTable.setStatisticsType(RandomVariableTable.NONE);
			}
			rvGraph.repaint();
			rvTable.repaint();
		}
		else super.itemStateChanged(e);
	}


	/**
	* This method updates the paramerters of the distribution when these
	* parameters have been changed with the scrollbars.
	*/
	public void setParameters(){
		double prod = 1;
		occupiedDist.setParameters(popSize, sampleSize);
		if (sampleSize > popSize) p = 1.00;
		else{
			for (int i = 1; i <= sampleSize; i++) prod = prod * (popSize - i + 1) / popSize;
			p = 1 - prod;
		}
		matchDist.setProbability(p);
		reset();
	}

	/**
	* This method defines the experiment. A sample of the specified size is
	* selected with replacement. The number of distinct values is recorded and
	* duplicate sample values recorded.
	*/
	public void doExperiment(){
		super.doExperiment();
		selectBalls();
		occupiedRV.setValue(occupiedCount);
		if (occupiedCount < sampleSize) matchRV.setValue(1); else matchRV.setValue(0);
	}

	/**
	* This method selects the sample of balls and determines the number of distinct sample
	* values and whether a match has occurred.
	*/
	public void selectBalls(){
		occupiedCount = 0;
		boolean match;
		Ball ball;
		urn.sample(popSize, urn.WITH_REPLACEMENT);
		for (int i = 0; i < sampleSize; i++){
			match = false;
			ball = urn.getBall(i);
			for(int j = 0; j < i; j++) if (ball.getValue() == urn.getBall(j).getValue()) {
				match = true;
				break;
			}
			if (match) ball.setBallColor(Color.red);
			else{
				ball.setBallColor(GREEN);
				occupiedCount++;
			}
		}
	}

	/**
	* This method runs the experiment one time, with additional annimation and sound.
	*/
	public void step(){
		stop();
		urn.setDrawn(false);
		selectBalls();
		stage = 0;
		timer.start();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual run
	* method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual stop
	* method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}


	/**
	* This method updates the display, including the ball, random variable
	* graph and table and the record table.
	*/
	public void update(){
		super.update();
		urn.setDrawn(true);;
		recordTable.addRecord(new double[]{getTime(), occupiedCount, matchRV.getValue()});
		rvGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.repaint();
		rvTable.repaint();
   }

	public void graphUpdate(){
		super.update();
		//urn.setDrawn(true);;
		//recordTable.addRecord(new double[]{getTime(), occupiedCount, matchRV.getValue()});
		rvGraph.setShowModelDistribution(showModelDistribution);
		rvGraph.repaint();
		rvTable.setShowModelDistribution(showModelDistribution);
		rvTable.repaint();
   }
	/**
	* This method resets the experiment, including the balls, the random
	* variable graph and table, and the record table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		urn.setDrawn(false);
		occupiedRV.reset();
		matchRV.reset();
		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method handles the events associated with the step process. The balls are
	* shown one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (stage < sampleSize){
				urn.getBall(stage).setDrawn(true);
				//playnote(urn.getBall(stage).getValue());
				stage++;
			}
			else{
				timer.stop();
				occupiedRV.setValue(occupiedCount);
				if (occupiedCount < sampleSize) matchRV.setValue(1); else matchRV.setValue(0);
				recordTable.addRecord(new double[]{getTime(), occupiedCount, matchRV.getValue()});
				rvGraph.repaint();
				rvTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}

	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}


