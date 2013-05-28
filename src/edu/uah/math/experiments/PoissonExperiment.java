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
import edu.uah.math.distributions.PoissonDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Domain;

/**
* This class models the number of arrivals in an interval of a specified size
* for the Poisson process with a specified rate. The arrivals are shown as dots
* on a timeline.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class PoissonExperiment extends Experiment implements Serializable{
	//Variables
	private double r = 4, t = 5, currentTime, timeStep = 0.05;
	private int arrivalCount;
	//Ojbects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "N"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter rScroll = new Parameter(0.5, 10, 0.5, r, "Rate", "r");
	private Parameter tScroll = new Parameter(0.5, 10, 0.5, t, "Time", "t");
	private PoissonDistribution dist = new PoissonDistribution(r * t);
	private RandomVariable arrivals = new RandomVariable(dist, "N");
	private RandomVariableGraph arrivalsGraph = new RandomVariableGraph(arrivals);
	private RandomVariableTable arrivalsTable = new RandomVariableTable(arrivals);
	private Timeline timeline = new Timeline(new Domain(0, t, timeStep, Domain.CONTINUOUS), "s");
	private Timer timer = new Timer(20, this);

	/**
	* This method initializes the experiment, including the timeline, the
	* random variable graph and table, the record table, and the toolbar containing
	* the scrollbars and label.
	*/
	public void init(){
		super.init();
		setName("Poisson Experiment");
		//Parameters
		rScroll.applyDecimalPattern("0.0");
		rScroll.getSlider().addChangeListener(this);
		tScroll.applyDecimalPattern("0.0");
		tScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rScroll);
		toolBar.add(tScroll);
		addToolBar(toolBar);
		addToolBar(timeline);
		//Timeline
		timeline.setMinimumSize(new Dimension(100, 40));
		timeline.setToolTipText("Arrival timeline");
		addComponent(timeline, 0, 0, 2, 1, 10, 2);
		//Random variable graph
		addComponent(arrivalsGraph, 0, 1, 2, 1);
		//Record table
		recordTable.setDescription("N: number of points");
		addComponent(recordTable, 0, 2, 1, 1);
		//Random variable table
		addComponent(arrivalsTable, 1, 2, 1, 1);
		reset();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiement to run a Poisson process until time t. The arrivals are shown as red dots on\n"
			+ "a timeline, and the number of arrivals N is recorded on each update. The density and moments\n"
			+ "of N are shown in the distribution graph and the distribution table. The parameters of the\n"
			+ "experiment are the rate of the proccess r and the time t, which can be varied with scroll bars.";
	}

	/**
	* Thie method defines the experiment: the arrivals are generated and tested
	* to see if they belong to the interval [0, t].
	*/
	public void doExperiment(){
		super.doExperiment();
		computeArrivalTimes();
		arrivals.setValue(arrivalCount);
	}

	/**
	* This method computes the arrival times and adds these times to the timeline.
	*/
	public void computeArrivalTimes(){
		double arrivalTime;
		timeline.resetData();
		arrivalTime = - Math.log(1 - Math.random()) / r;
		arrivalCount = 0;
		while (arrivalTime <= t){
			arrivalCount++;
			timeline.addTime(arrivalTime);
			arrivalTime = arrivalTime - Math.log(1 - Math.random()) / r;
		}
	}

	/**
	* This method stops the run process, if necessary, and then starts the single step process.
	* This process shows time passing and the arrival times ocurring.
	*/
	public void step(){
		stop();
		computeArrivalTimes();
		currentTime = 0;
		timeline.setCurrentTime(currentTime);
		timer.start();
	}

	/**
	* This method stops the single step process, if necessary, and then calls the usual
	* stop method.
	*/
	public void stop(){
		timer.stop();
		super.stop();
	}

	/**
	* This method stops the step process, if necessary, and then calls the usual
	* run method.
	*/
	public void run(){
		timer.stop();
		super.run();
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
		arrivals.reset();
		arrivalsGraph.reset();
		arrivalsTable.reset();
	}

	/**
	* This method updates the displays, including the timeline, record table,
	* random variable graph and table.
	*/
	public void update(){
		super.update();
		timeline.setCurrentTime(t);
		recordTable.addRecord(new double[]{getTime(), arrivalCount});
		arrivalsGraph.repaint();
		arrivalsTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		arrivalsGraph.setShowModelDistribution(showModelDistribution);
		arrivalsGraph.repaint();
		arrivalsTable.setShowModelDistribution(showModelDistribution);
		arrivalsTable.repaint();
	}
	/**
	* This method handles the scrollbar events for adjusting the size of the
	* time interval and the rate of the Poisson process.
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == tScroll.getSlider()){
			t = tScroll.getValue();
			timeStep = t / 100;
			timeline.setDomain(new Domain(0, t, timeStep, Domain.CONTINUOUS));
			setDistribution();
		}
		else if (e.getSource() == rScroll.getSlider()){
			r = rScroll.getValue();
			setDistribution();
		}
	}

	/**
	* This method sets the Poisson distribution, when the parameters
	* have changed.
	*/
	public void setDistribution(){
		dist.setParameter(r * t);
		reset();
	}

	/**
	* This method handles the events associated with the timer. The random points up to the
	* current time are drawn.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (currentTime < t){
				currentTime = currentTime + timeStep;
				timeline.setCurrentTime(currentTime);
			}
			else{
				timer.stop();
				//playnote(arrivalCount);
				super.doExperiment();
				arrivals.setValue(arrivalCount);
				update();
			}
		}
		else super.actionPerformed(e);
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

