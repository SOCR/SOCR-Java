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
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Timeline;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the gamma distribution in terms of the arrival times of a
* Poisson process.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class GammaExperiment extends Experiment implements Serializable{
	//Variables
	private int k = 1;
	private double r = 1, time, currentTime, timeStep;
	//Objects
	private RecordTable recordTable = new RecordTable();
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Parameter kScroll = new Parameter(1, 10, 1, k, "Shape parameter", "k");
	private Parameter rScroll = new Parameter(0.5, 5, 0.5, r, "Rate parameter", "r");
	private GammaDistribution dist = new GammaDistribution(k, 1 / r);
	private Timeline timeline = new Timeline(dist.getDomain());
	private RandomVariable arrivalTime = new RandomVariable(dist, "T(1)");
	private RandomVariableGraph arrivalTimeGraph = new RandomVariableGraph(arrivalTime);
	private RandomVariableTable arrivalTimeTable = new RandomVariableTable(arrivalTime);
	private Timer timer = new Timer(20, this);

	/**
	* This method initializes the experiment, including the layout of the
	* toolbar, timeline, random variable graph and table.
	*/
	public void init(){
		super.init();
		setName("Gamma Experiment");
		//Timeline
		//Sliders
		kScroll.getSlider().addChangeListener(this);
		rScroll.applyDecimalPattern("0.0");
		rScroll.getSlider().addChangeListener(this);
		//Toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(kScroll);
		toolBar.add(rScroll);
		addToolBar(toolBar);
		addToolBar(timeline);
		//Timeline
		timeline.setMargins(35, 20, 20, 20);
		timeline.setMinimumSize(new Dimension(100, 40));
		timeline.setToolTipText("Arrival timeline");
		addComponent(timeline, 0, 0, 2, 1, 10, 2);
		//Arrival Time Graph
		addComponent(arrivalTimeGraph, 0, 1, 2, 1);
		//Record Table
		recordTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		recordTable.setDescription("T(i): i'th arrival time");
		addComponent(recordTable, 0, 2, 1, 1);
		//Arrival Time Record
		addComponent(arrivalTimeTable, 1, 2, 1, 1);
		//Final actions
		timeStep = dist.getDomain().getWidth();
		validate();
		reset();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The gamma experiment consists of running a Poisson process until the time of the k'th\n"
			+ "arrival. The arrivals are shown as red dots in the timeline. The density function of\n"
			+ "the k'th arrival Tk is shown in the graph box, and the moments are given in the\n"
			+ "distribution table. The arrival times are recorded in the update table. The parameters\n"
			+ "are the rate of the process r and the arrival number k, which can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment: <var>k</var> arrivals in the Poisson process are
	* simulated. The last arrival time has the gamma distribution with shape
	* parameter <var>k</var> and scale parameter 1/<var>r</var>, where <var>r</var> is the rate.
	*/
	public void doExperiment(){
		super.doExperiment();
		computeArrivalTimes();
		arrivalTime.setValue(time);
	}

	/**
	* This method computes the arrival times and passes them to the timeline.
	*/
	public void computeArrivalTimes(){
		timeline.resetData();
		time = 0;
		for (int i = 0; i < k; i++){
			time = time - Math.log(1 - Math.random()) / r;
			timeline.addTime(time);
		}
	}

	/**
	* This method starts the step timer and shows the experiment with additional annimation
	* and sound.
	*/
	public void step(){
		stop();
		computeArrivalTimes();
		currentTime = 0;
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
		timeline.reset();
		String[] variables = new String[k + 1];
		variables[0] = "Run";
		for (int i = 1; i <= k; i++) variables[i] = "T(" + i + ")";
		recordTable.setVariableNames(variables);
		arrivalTime.reset();
		arrivalTimeGraph.reset();
		arrivalTimeTable.reset();
	}

	/**
	* This method updates the display, including the timeline, record table,
	* random variable graph and table.
	*/
	public void update(){
		super.update();
		timeline.setCurrentTime(time);
		arrivalTimeGraph.repaint();
		arrivalTimeTable.repaint();
		double[] record = new double[k + 1];
		record[0] = getTime();
		for (int i = 0; i < k; i++) record[i + 1] = timeline.getTime(i);
		recordTable.addRecord(record);
	}
	
	public void graphUpdate(){
		super.update();
		arrivalTimeGraph.setShowModelDistribution(showModelDistribution);
		arrivalTimeGraph.repaint();
		arrivalTimeTable.setShowModelDistribution(showModelDistribution);
		arrivalTimeTable.repaint();
	}
	/**
	* This method handles the scrollbar events that correspond to varying the
	* rate and the order of the arrival time.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == kScroll.getSlider()){
			k = (int)kScroll.getValue();
			setDistribution();
			reset();
		}
		else if (e.getSource() == rScroll.getSlider()){
			r = rScroll.getValue();
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
			if (currentTime + timeStep < time){
				currentTime = currentTime + timeStep;
				timeline.setCurrentTime(currentTime);
			}
			else{
				timer.stop();
				super.doExperiment();
				arrivalTime.setValue(time);
				//playnote((int)time);
				update();
			}
		}
		else super.actionPerformed(e);
	}

	/**
	* This method sets the distribution of the arrival time random variable
	* after the parameters have been changed.
	*/
	public void setDistribution(){
		dist.setParameters(k, 1 / r);
		arrivalTime.setName("T(" + k + ")");
		timeline.setDomain(dist.getDomain());
		timeStep = dist.getDomain().getWidth();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}



