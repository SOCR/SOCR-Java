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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.Timeline;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.NegativeBinomialDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the negative binomial experiment; the random variable
* of interest is the number of trials until a specified number of successes.
* The expreiment is illustrated in terms of a timeline.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class NegativeBinomialExperiment extends Experiment implements Serializable{
	//Variables
	private int k = 1, trialCount, headCount;
	private double p = 0.5, currentTime;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "Y"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter kScroll = new Parameter(1, 90, 1, k, "Number of successes", "k");
	private Parameter pScroll = new Parameter(0.05, 1, 0.05, p, "Probability of success", "p");
	private NegativeBinomialDistribution trialsDist = new NegativeBinomialDistribution(k, p);
	private RandomVariable trials = new RandomVariable(trialsDist, "Y");
	private RandomVariableGraph trialsGraph = new RandomVariableGraph(trials);
	private RandomVariableTable trialsTable = new RandomVariableTable(trials);
	private Timeline timeline = new Timeline(new Domain(1, trialsDist.getDomain().getUpperValue(), 1, Domain.DISCRETE));
	private Timer timer = new Timer(100, this);

	/**
	* This method initializes the experiment by setting up the toolbar, random
	* variable graph and table, timeline.
	*/
	public void init(){
		super.init();
		setName("Negative Binomial Experiment");
		//Listeners
		kScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(kScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		addToolBar(timeline);
		//Graphs
		//Timeline
		timeline.setMargins(35, 20, 20, 20);
		timeline.setToolTipText("Bernoulli trials timeline");
		timeline.setMinimumSize(new Dimension(100, 40));
		addComponent(timeline, 0, 0, 2, 1, 10, 2);
		//Trials Graph
		trialsGraph.setDomain(new Domain(1, trialsDist.getDomain().getUpperValue(), 1, Domain.DISCRETE));
		addComponent(trialsGraph, 0, 1, 2, 1);
		//Record Table
		recordTable.setDescription("Y: number of trials");
		addComponent(recordTable, 0, 2, 1, 1);
		//Trials Table
		addComponent(trialsTable, 1, 2, 1, 1);
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The negative binomial experiment consists of performing Bernoulli trials, with probability\n"
			+ "of success p, until the k'th success occurs. The successes are shown as red dots in the\n"
			+ "timeline. The number of trials Y is recorded on each update. The density and moments of\n"
			+ "Y are shown in the distribution graph and the distribution table. The parameters k and p\n"
			+ "can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment.  The Bernoulli trials are performed
	* until the specified number of successes have occurred. The trial outcomes
	* are passed to the timeline. The number of trials is recorded and passed
	* to the random variable.
	*/
	public void doExperiment(){
		super.doExperiment();
		performTrials();
		trials.setValue(trialCount);
	}

	/**
	* This method performs the Bernoulli trials and adds the random points to the timeline.
	*/
	public void performTrials(){
		timeline.resetData();
		trialCount = 0;
		headCount = 0;
		while (headCount < k){
			trialCount++;
			if (Math.random() < p){
				headCount++;
				timeline.addTime(trialCount, Color.red);
 			}
		}
	}

	/**
	* This method starts the step timer and shows the experiment with additional annimation
	* and sound.
	*/
	public void step(){
		stop();
		performTrials();
		currentTime = 1;
		timeline.setCurrentTime(currentTime);
		timer.start();
	}

	/**
	* This method stops the step timer, if necessary, and then calls the usual run method.
	*/
	public void run(){
		timer.stop();
		super.run();
	}

	/**
	* This method stops the step timer, if necessary, and then calls the usual stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}


	/**
	* This method resets the experiment, including the timeline, record table,
	* random variable graph and table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		timeline.reset();
		trials.reset();
		trialsGraph.reset();
		trialsTable.reset();
		trialsGraph.setDomain(new Domain(1, trialsDist.getDomain().getUpperValue(), 1, Domain.DISCRETE));
	}

	/**
	* This method updates the experiment, including the timeline, record table
	* random variable graph and table.
	*/
	public void update(){
		super.update();
		timeline.setCurrentTime(trialCount);
		trialsGraph.repaint();
		trialsTable.repaint();
		recordTable.addRecord(new double[]{getTime(), trialCount});
	}
	
	public void graphUpdate(){
		super.update();
		
		trialsGraph.setShowModelDistribution(showModelDistribution);
		trialsGraph.repaint();	
		trialsTable.setShowModelDistribution(showModelDistribution);
		trialsTable.repaint();	
	}

	/**
	* This method handles the scrollbar events for changing the parameters of
	* the experiment, the number of successes and the probability of success.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == kScroll.getSlider()){
			k = (int)kScroll.getValue();
			setDistribution();
			reset();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			setDistribution();
			reset();
		}
	}

	/**
	* This method handles the events associated with the timer. The random points up to the
	* current time are drawn.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (currentTime < trialCount){
				currentTime++;
				timeline.setCurrentTime(currentTime);
			}
			else{
				timer.stop();
				super.doExperiment();
				trials.setValue(trialCount);
				//playnote(trialCount);
				update();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the parameters of the distribution, when these have
	* been changed with the scrollbars.
	*/
	public void setDistribution(){
		trialsDist.setParameters(k, p);
		double n = trialsDist.getDomain().getUpperValue();
		timeline.setDomain(new Domain(1, n, 1, Domain.DISCRETE));
		timer.setDelay(2000 / (int)n);
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}
