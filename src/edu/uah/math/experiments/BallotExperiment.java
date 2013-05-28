/*
Copyright (C) 2001-2004  Kyle Siegrist, Dawn Duehring
Department of Mathematical Sciences
University of Alabama in Huntsville

This program is part of Virtual Laboratories in Probability and Statistics,
http://www.math.uah.edu/stat/.

This program is licensed under a Creative Commons License. Basically, you are free to copy,
distribute, and modify this program, and  to make commercial use of the program.
However you must give proper attribution.
See http://creativecommons.org/licenses/by/2.0/ for more information.
*/
package edu.uah.math.experiments;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import java.awt.Dimension;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.BallotGraph;

/**
* This class implements the classical ballot experiment: the winner has a specified number of
* votes and the loser has a specified, smaller number of votes. The votes are counted in a
* random order. The event of interest is that the winner is always ahead in the vote count.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BallotExperiment extends Experiment implements Serializable{
	//Variables
	private int winnerTotal = 10, loserTotal = 5;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "I"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private BallotGraph ballotGraph = new BallotGraph(winnerTotal, loserTotal);
	private BernoulliDistribution dist = new BernoulliDistribution(ballotGraph.getProbability());
	private RandomVariable rv = new RandomVariable(dist, "I");
	private Parameter winnerScroll = new Parameter(1, 50, 1, winnerTotal, "Winner trials", "W");
	private Parameter loserScroll = new Parameter(0, winnerTotal - 1, 1, loserTotal, "Loser trials", "L");
	private RandomVariableGraph rvGraph = new RandomVariableGraph(rv);
	private RandomVariableTable rvTable = new RandomVariableTable(rv);

	/**
	* This method initialize the experiment, including the name, the scroll bars, the toolbar,
	* the graphs and tables.
	*/
	public void init(){
		super.init();
		setName("Ballot Experiment");
		//Scroll bars
		winnerScroll.getSlider().addChangeListener(this);
		loserScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(winnerScroll, null);
		toolBar.add(loserScroll, null);
		addToolBar(toolBar);
		//Ballot Graph
		ballotGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(ballotGraph, 0, 0, 1, 1);
		//Random variable graph
		rvGraph.setMomentType(0);
		rvGraph.setMinimumSize(new Dimension(25, 100));
		addComponent(rvGraph, 1, 0, 1, 1);
		//Record Table
		recordTable.setDescription("I: Winner always ahead");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random variable table
		rvTable.setStatisticsType(0);
		addComponent(rvTable, 1, 1, 1, 1);
		reset();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "In an election, candidate A receives a votes and candidate B receives b votes, where a > b.\n"
			+ "The votes are assumed to be randomly ordered. The first graph shows the difference\n"
			+ "between the number of votes for A and the number of votes for B, as the votes are counted.\n"
			+ "The event of interest is that A is always ahead of B in the vote count, or equivalently,\n"
			+ "that the graph is always above the horizontal axis (except of course at the origin).\n"
			+ "The indicator variable I of this event is recorded in the first table on each update.\n"
			+ "The density function of I is shown in blue in the second graph and is recorded in the\n"
			+ "second table. On each update, the empirical density of I is shown as red in the second\n"
			+ "graph and recorded in the second table. The parameters a and b can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment: the random walk is performed and the ballot event is
	* determined.
	*/
	public void doExperiment(){
		super.doExperiment();
		ballotGraph.countVotes();
		if (ballotGraph.isWinnerAlwaysAhead()) rv.setValue(1);
		else rv.setValue(0);
	}

	/**
	* This method runs the the experiment one time, and getContentPane().add sounds depending on the outcome
	* of the experiment.
	*/
	public void step(){
		doExperiment();
		update();
		//////playnote((int)rv.getValue());
	}

	/**
	* This method resets the experiment, including the record table, the ballot graph,
	* the random variable graph and table, and the indicator variable for the ballot event.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		ballotGraph.reset();
		rv.reset();
		rvGraph.reset();
		rvTable.reset();
	}

	/**
	* This method updates the record table, the random variable graph and table, and the
	* ballot graph.
	*/
	public void update(){
		super.update();
		ballotGraph.repaint();
		recordTable.addRecord(new double[]{getTime(), rv.getValue()});
		rvGraph.repaint();
		rvTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		rvGraph.setShowModelDistribution(showModelDistribution);
	//	ballotGraph.setShowModelDistribution(showModelDistribution);
		ballotGraph.repaint();
		//recordTable.addRecord(new double[]{getTime(), rv.getValue()});
		
		rvGraph.repaint();
		rvTable.setShowModelDistribution(showModelDistribution);
		rvTable.repaint();
	}
	/**
	* This method handles the scroll events associated with changing the vary the
	* winner and loser counts.
	* @param e the mouse event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == winnerScroll.getSlider()) {
			winnerTotal = (int)winnerScroll.getValue();
			if (loserTotal >= winnerTotal) loserTotal = winnerTotal - 1;
			loserScroll.setMax(winnerTotal - 1);
			loserScroll.setValue(loserTotal);
			setDistribution();
		}
		else if (e.getSource() == loserScroll.getSlider()){
			loserTotal = (int)loserScroll.getValue();
			setDistribution();
		}
	}

	/**
	* This method sets the parameters of the ballot graph and the distribution of the
	* indicator random variable.
	*/
	public void setDistribution(){
		double p = (double)(winnerTotal - loserTotal) / (winnerTotal + loserTotal);
		ballotGraph.setParameters(winnerTotal, loserTotal);
		dist.setProbability(ballotGraph.getProbability());
		reset();
	}
	
	public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}



