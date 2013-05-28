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
import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.Timer;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Ball;
import edu.uah.math.devices.Urn;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.distributions.MatchDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the matching experiment. A random permutation of the numbers
* from 1 to n is chosen. A match occurs if a number in the permutation is in its
* natural position. The number of matches is the random variable of interest.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MatchExperiment extends Experiment implements Serializable{
	//Variables
	private int n = 10, stage, matchCount;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "N"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Urn urn = new Urn(n, 26);
	private Parameter nScroll = new Parameter(2, 20, 1, n, "Number of balls", "n");
	private MatchDistribution dist = new MatchDistribution(n);
	private RandomVariable matches = new RandomVariable(dist, "N");
	private RandomVariableGraph matchesGraph = new RandomVariableGraph(matches);
	private RandomVariableTable matchesTable = new RandomVariableTable(matches);
	private Timer timer = new Timer(100, this);

	/**
	* This method initializes the experiment, including the ball panel, the
	* toolbar with scrollbar, and the random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Match Experiment");
		//Slider
		nScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(nScroll);
		addToolBar(toolBar);
		//Urn
		urn.setMinimumSize(new Dimension(100, 40));
		addComponent(urn, 0, 0, 2, 1, 10, 0);
		//Random variable graph
		addComponent(matchesGraph, 0, 1, 2, 1);
		//Record Table
		recordTable.setDescription("N: number of matches");
		addComponent(recordTable, 0, 2, 1, 1);
		//Random Variable Table
		addComponent(matchesTable, 1, 2, 1, 1);
		//Finalize
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
			+ "The matching experiment is to randomly permute n balls, numbered 1 to n.\n"
			+ "A match occurs whenever the ball number and the position number agree. The matches are\n"
			+ "shown in red. The number of matches N is recorded on each update. The distribution and\n"
			+ "moments of N are shown in the distribution graph and the distribution table. The parameter\n"
			+ "n can be varied with a scroll bar.";
	}

	/**
	* The method handles the scrollbar events for adjusting the size
	* of the random permutation.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == nScroll.getSlider()){
			n = (int)nScroll.getValue();
			urn.setBallCount(n);
			dist.setParameter(n);
			reset();
		}
	}

	/**
	* This method defines the experiment. A random permutation is chosen and
	* the number of matches computed.
	*/
	public void doExperiment(){
		matchCount = 0;
		Ball ball;
		super.doExperiment();
		urn.sample(n, Urn.WITHOUT_REPLACEMENT);
		for (int i = 0; i < n; i++){
			ball = urn.getBall(i);
			if (ball.getValue() == i + 1){
				matchCount++;
				ball.setBallColor(RED);
			}
			else ball.setBallColor(GREEN);
		}
		matches.setValue(matchCount);
	}


	/**
	* This method updates the display, including the ball panel, the record
	* table, and the random variable graph and table.
	*/
	public void update(){
		super.update();
		urn.setDrawn(true);
		recordTable.addRecord(new double[]{getTime(), matchCount});
		matchesGraph.repaint();
		matchesTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		matchesGraph.setShowModelDistribution(showModelDistribution);
		matchesGraph.repaint();
		matchesTable.setShowModelDistribution(showModelDistribution);
		matchesTable.repaint();
		}
	/**
	* This method runs the experiment one time, adding sounds and annimation.
	*/
	public void step(){
		stop();
		matchCount = 0;
		stage = 0;
		urn.setDrawn(false);
		urn.sample(n, Urn.WITHOUT_REPLACEMENT);
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
	* This method resets the experiment, including the ball panel, the record
	* table, and the random variable graph and table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		urn.setDrawn(false);
		matches.reset();
		matchesGraph.reset();
		matchesTable.reset();
	}

	/**
	* This method handles events associated with the step process. The balls are shown one at a time,
	* with a sound depending on whether there is a match.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (stage < n){
				Ball ball = urn.getBall(stage);
				if (ball.getValue() == stage + 1){
					matchCount++;
					ball.setBallColor(RED);
				}
				else ball.setBallColor(GREEN);
				ball.setDrawn(true);
				//playnote(ball.getValue());
				stage++;
			}
			else{
				timer.stop();
				super.doExperiment();
				matches.setValue(matchCount);
				recordTable.addRecord(new double[]{getTime(), matchCount});
				matchesGraph.repaint();
				matchesTable.repaint();
			}
		}
		else super.actionPerformed(e);
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

