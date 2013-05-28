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
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import edu.uah.math.distributions.Functions;
import edu.uah.math.distributions.FiniteOrderStatisticDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Ball;
import edu.uah.math.devices.Urn;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;

/**
* This class models the experiment of selecting a sample of a specified size (without
* replacement) from from the population 1, 2, ..., m.  The order statistics are the random
* variables of interest.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class FiniteOrderStatisticExperiment extends Experiment implements Serializable{
	//Variables
	private int populationSize = 10, sampleSize = 5, order = 1, value, trial;
	private int[] sample = new int[sampleSize], orderedSample = new int[sampleSize];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X(1)", "X(2)", "X(3)", "X(4)", "X(5)"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Urn urn = new Urn(sampleSize, 26);
	private FiniteOrderStatisticDistribution dist = new FiniteOrderStatisticDistribution(populationSize, sampleSize, order);
	private RandomVariable orderRV = new RandomVariable(dist, "X(1)");
	private Parameter populationScroll = new Parameter(1, 100, 1, populationSize, "Population size", "m");
	private Parameter sampleScroll = new Parameter(1, populationSize, 1, sampleSize, "Sample size", "n");
	private Parameter orderScroll = new Parameter(1, sampleSize, 1, order, "Order", "k");
	private RandomVariableGraph orderGraph = new RandomVariableGraph(orderRV);
	private RandomVariableTable orderTable = new RandomVariableTable(orderRV);
	private Timer timer = new Timer(100, this);

	/**
	* This method initializes the experiment, including the ball panel, the random variable
	* table and graph, the toolbar with the scrollbars and labels, and the record table.
	*/
	public void init(){
		super.init();
		setName("Finite Order Statistic Experiment");
		//Paraemters
		populationScroll.getSlider().addChangeListener(this);
		sampleScroll.getSlider().addChangeListener(this);
		orderScroll.getSlider().addChangeListener(this);
		//Construct toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(populationScroll);
		toolBar.add(sampleScroll);
		toolBar.add(orderScroll);
		addToolBar(toolBar);
		//urn
		urn.setMinimumSize(new Dimension(100, 100));
		addComponent(urn, 0, 0, 1, 1);
		//Order Graph
		orderGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(orderGraph, 1, 0, 1, 1);;
		//Record Table
		recordTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		recordTable.setDescription("X(k): k'th order statistic");
		addComponent(recordTable, 0, 1, 1, 1);
		//Order Table
		addComponent(orderTable, 1, 1, 1, 1);
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
			+ "The experiment consists of selecting n balls at random (without replacement)\n"
			+ "from a population of N balls, numbered from 1 to N. For i = 1, 2, ..., n,\n"
			+ "the i'th smallest number in the sample, X(i) (the i'th order statistic), is recorded\n"
			+ "on each update. For a selected k, the distribution and moments of X(k) are shown in the\n"
			+ "distribution graph and the distribution table. The parameters N, n, and k can be varied\n"
			+ "with scroll bars.";
	}

	/**
	* This event handles the scroll events that correspond to changes in the parameters:
	* the population size, the sample size, and the order.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == populationScroll.getSlider()){
			populationSize = (int)populationScroll.getValue();
			if (sampleSize > populationSize) sampleSize = populationSize;
			sampleScroll.setMax(Math.min(populationSize, 60));
			sampleScroll.setValue(sampleSize);
			if (order > sampleSize) order = sampleSize;
			orderScroll.setMax(sampleSize);
			orderScroll.setValue(order);
			sample = new int[sampleSize];
			orderedSample = new int[sampleSize];
			setDistributions();
		}
		else if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			if (order > sampleSize) order = sampleSize;
			orderScroll.setMax(sampleSize);
			orderScroll.setValue(order);
			sample = new int[sampleSize];
			orderedSample = new int[sampleSize];
			urn.setBallCount(sampleSize);
			setDistributions();
		}
		else if (e.getSource() == orderScroll.getSlider()){
			order = (int)orderScroll.getValue();
			setDistributions();
		}
	}

	/**
	* This method sets the distribution after the parameters have changed.
	*/
	public void setDistributions(){
		dist.setParameters(populationSize, sampleSize, order);
		orderRV.setName("X(" + order + ")");
		String[] variableNames = new String[sampleSize + 1];
		variableNames[0] = "Run";
		for (int i = 1; i <= sampleSize; i++) variableNames[i] = "X(" + i + ")";
		recordTable.setVariableNames(variableNames);
		reset();
	}

	/**
	* This method runs the experiment one time, with additional annimation and sound.
	*/
	public void step(){
		stop();
		computeOrderStatistics();
		trial = 0;
		urn.setValues(sample);
		urn.setBallColor(GREEN);
		urn.setDrawn(false);
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
	* This method resets the experiment, including the random variable table and graph, and
	* the ball panel.
	*/
	public void reset(){
		timer.stop();
		super.reset();
		recordTable.reset();
		urn.setDrawn(false);
		orderRV.reset();
		orderTable.reset();
		orderGraph.reset();
	}

	/**
	* This method defines the experiment.  The sample is selected and the values of the
	* order statistics computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		computeOrderStatistics();
		orderRV.setValue(value);
	}

	/**
	* This method sorts the sample and computes the order statistics.
	*/
	private void computeOrderStatistics(){
		sample = Functions.getSample(populationSize, sampleSize, Functions.WITHOUT_REPLACEMENT);
		orderedSample = Functions.sort(sample);
		value = orderedSample[order - 1];
	}


	/**
	* This method updates the experiment, including the ball panel, the random variable and
	* graph, and the record table.
	*/
	public void update(){
		super.update();
		double[] record = new double[sampleSize + 1];
		record[0] = getTime();
		urn.setValues(sample);
		urn.setBallColor(GREEN);
		for (int i = 0; i < sampleSize; i++){
			if (urn.getBall(i).getValue() == value){
				urn.getBall(i).setBallColor(RED);
			}
			record[i + 1] = orderedSample[i];
		}
		urn.setDrawn(true);
		recordTable.addRecord(record);
		orderGraph.repaint();
		orderTable.repaint();
	}
	public void graphUpdate(){
		super.update();
		orderGraph.setShowModelDistribution(showModelDistribution);
		orderGraph.repaint();
		orderTable.setShowModelDistribution(showModelDistribution);
		orderTable.repaint();
	}
	/**
	* This method handles the events associated with the step timer. The balls are shown
	* one at a time.
	*/
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == timer){
			if (trial < sampleSize){
				urn.getBall(trial).setDrawn(true);
				//playnote(urn.getBall(trial).getValue());
				trial++;
			}
			else{
				timer.stop();
				super.doExperiment();
				double[] record = new double[sampleSize + 1];
				record[0] = getTime();
				orderRV.setValue(value);
				orderGraph.repaint();
				orderTable.repaint();
				for (int i = 0; i < sampleSize; i++){
					Ball ball = urn.getBall(i);
					if (ball.getValue() == value){
						ball.setBallColor(RED);
						ball.setDrawn(true);
					}
					record[i + 1] = orderedSample[i];
				}
				recordTable.addRecord(record);
			}
		}
		else super.actionPerformed(e);
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

