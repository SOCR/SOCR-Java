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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.Timeline;
import edu.uah.math.distributions.PoissonDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Domain;

/**
* This class models the two-type Poisson experiment. Each arrival, independently,
* is one of two types. The arrivals are shown as dots on a timeline, colored red
* or green to indicate the type. The random variables of interest are the number
* of arrivals of the two types in an interval of a specified size. The rate of the
* process and the probability that governs the type can also be varied.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class PoissonSplitExperiment extends Experiment implements Serializable{
	//Variables
	private double r = 3, t = 4, p = 0.5, currentTime, timeStep = 0.04;
	private int count0, count1;
	//Ojbects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "M", "N"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter rScroll = new Parameter(0.5, 10, 0.5, r, "Rate", "r");
	private Parameter tScroll = new Parameter(0.5, 10, 0.5, t, "Time", "t");
	private Parameter pScroll = new Parameter(0, 1, 0.05, p, "Red point probability", "p");
	private PoissonDistribution dist0 = new PoissonDistribution(r * t * (1 - p));
	private PoissonDistribution dist1 = new PoissonDistribution(r * t * p);
	private RandomVariable rv0 = new RandomVariable(dist0, "M");
	private RandomVariable rv1 = new RandomVariable(dist1, "N");
	private RandomVariableGraph graph0 = new RandomVariableGraph(rv0);
	private RandomVariableGraph graph1 = new RandomVariableGraph(rv1);
	private RandomVariableTable table0 = new RandomVariableTable(rv0);
	private RandomVariableTable table1 = new RandomVariableTable(rv1);
	private Timeline timeline = new Timeline(new Domain(0, t, timeStep, Domain.CONTINUOUS), "s");
	private Timer timer = new Timer(20, this);

	/**
	* This method initializes the experiment, including the toolbar with the
	* scrollbars and labels, the random variable graphs and tables, the timeline
	* and the record table.
	*/
	public void init(){
		super.init();
		setName("Two-Type Poisson Experiment");
		//Parameters
		rScroll.applyDecimalPattern("0.0");
		rScroll.getSlider().addChangeListener(this);
		tScroll.applyDecimalPattern("0.0");
		tScroll.getSlider().addChangeListener(this);
		pScroll.applyDecimalPattern("0.00");
		pScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(rScroll);
		toolBar.add(tScroll);
		toolBar.add(pScroll);
		addToolBar(toolBar);
		addToolBar(timeline);
		//Timeline
		timeline.setMinimumSize(new Dimension(100, 40));
		timeline.setToolTipText("Arrival timeline; type 0: green, type 1: red");
		addComponent(timeline, 0, 0, 2, 1, 10, 2);
		//Graph for type 0 arrivals
		graph0.setDataColor(GREEN);
		addComponent(graph0, 0, 1, 1, 1);
		//Graph for type 1 arrivals
		addComponent(graph1, 1, 1, 1, 1);
		//Table for type 0 arrivals
		addComponent(table0, 0, 2, 1, 1);
		//Table for type 1 arrivals
		addComponent(table1, 1, 2, 1, 1);
		//Record table
		recordTable.setDescription("M: number of green points, N: number of red point");
		addComponent(recordTable, 0, 3, 2, 1);
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
			+ "The experiment is to run a Poisson process until time t. Each arrival is type 1 with probability\n"
			+ "p or type 0 with probability 1 - p. In the timeline, the type 1 arrivals are shown as red dots\n"
			+ "and the type 0 arrivals as green dots. The number of type 1 arrivals M and the number of type 0\n"
			+ "arrivals N are recorded on each update. The densities and moments of M and N are shown in the\n"
			+ "distribution graphs and the distribution tables. The parameters are the rate of the process r,\n"
			+ "the time t, and the probability p. These can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment. The arrivals are generated and tested
	* to see if they fall in the interval. Each interval is randomly assigned a type.
	* The values of the random variables are computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		computeArrivalTimes();
		rv0.setValue(count0);
		rv1.setValue(count1);
	}

	public void computeArrivalTimes(){
		double arrivalTime;
		timeline.resetData();
		arrivalTime = - Math.log(1 - Math.random()) / r;
		count0 = 0; count1 = 0;
		while (arrivalTime <= t){
			if (Math.random() < p){
				count1++;
				timeline.addTime(arrivalTime, RED);
			}
			else{
				count0++;
				timeline.addTime(arrivalTime, GREEN);
			}
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
	* This method resets the experiment, including the timeline, the random
	* variable graphs and tables, and the record table.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		timeline.reset();
		rv0.reset();
		rv1.reset();
		graph0.reset();
		graph1.reset();
		table0.reset();
		table1.reset();
	}


	/**
	* This method updates the displays, including the timeline, the random
	* variable graphs and tables, and the record table.
	*/
	public void update(){
		super.update();
		timeline.setCurrentTime(t);
		recordTable.addRecord(new double[]{getTime(), count0, count1});
		graph0.repaint();
		graph1.repaint();
		table0.repaint();
		table1.repaint();
	}
	public void graphUpdate(){
		super.update();
		graph0.setShowModelDistribution(showModelDistribution);
		graph1.setShowModelDistribution(showModelDistribution);
		graph0.repaint();
		graph1.repaint();
		table0.setShowModelDistribution(showModelDistribution);
		table1.setShowModelDistribution(showModelDistribution);
		table0.repaint();
		table1.repaint();
	}
	/**
	* This method handles the scrollbar events for adjusting the size
	* of the interval, the rate of the process, and the probability of a
	* type 1 arrival.
	* @param e the change event
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
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			setDistribution();
		}
	}

	/**
	* This method sets the parameters of the distribution when the parameters
	* of the experiment have changed.
	*/
	public void setDistribution(){
		dist0.setParameter(r * t * (1 - p));
		dist1.setParameter(r * t * p);
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
				super.doExperiment();
				rv0.setValue(count0);
				//playnote(count0);
				rv1.setValue(count1);
				//playnote(count1);
				update();
			}
		}
		else super.actionPerformed(e);
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

