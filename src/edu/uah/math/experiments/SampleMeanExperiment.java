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
import java.io.Serializable;
import java.awt.event.ItemEvent;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.LocationScaleDistribution;
import edu.uah.math.distributions.PoissonDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the sample mean experiment. A random sample of a specified size is
* drawn from a specified distribution. The density of the sampling distribution and of the
* distribution of the sample mean are shown.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class SampleMeanExperiment extends Experiment implements Serializable{
	//Variables
	private int sampleSize = 1, distType = 0;
	private double[] sample = new double[1];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "M", "S"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
	private RandomVariable xBar = new RandomVariable(new NormalDistribution(0, 1), "M");
	private JComboBox distChoice = new JComboBox();
	private Parameter param1Scroll = new Parameter(-5, 5, 0.5, 0, "Mean", "\u03bc");
	private Parameter param2Scroll = new Parameter(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
	private Parameter sampleScroll = new Parameter(1, 200, 1, sampleSize, "Sample size", "N");
	private RandomVariableGraph xGraph = new RandomVariableGraph(x);
	private RandomVariableGraph xBarGraph = new RandomVariableGraph(xBar);
	private RandomVariableTable xTable = new RandomVariableTable(x);
	private RandomVariableTable xBarTable = new RandomVariableTable(xBar);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the record table,
	* the sliders for the parameters of the sampling distribution, the choice box
	* for the type of distribution, the toolbar, the graphs of the sampling and sample
	* mean random variables, the tables of the sampling and sample mean
	* random variables.
	*/
	public void init(){
		super.init();
		setName("Sample Mean Experiment");
		//Parameters
		param1Scroll.applyDecimalPattern("0.0");
		param1Scroll.getSlider().addChangeListener(this);
		param1Scroll.applyDecimalPattern("0.0");
		param2Scroll.getSlider().addChangeListener(this);
		sampleScroll.getSlider().addChangeListener(this);
		//Construct distribution choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Sampling distribution");
		distChoice.addItem("Normal");
		distChoice.addItem("Gamma");
		distChoice.addItem("Binomial");
		distChoice.addItem("Poisson");
		//Construct toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(distChoice);
		toolBar.add(param1Scroll);
		toolBar.add(param2Scroll);
		toolBar.add(sampleScroll);
		addToolBar(toolBar);
		//Random Variable Graph
		addComponent(xGraph, 0, 0, 2, 1);
		//Sample Mean Graph
		addComponent(xBarGraph, 2, 0, 2, 1);
		//Record Table
		recordTable.setDescription("M: sample mean, S: sample standard deviation");
		addComponent(recordTable, 0, 1, 1, 1);
		//Random Variable Table
		addComponent(xTable, 1, 1, 1, 1);
		//Sample Table
		addComponent(sampleTable, 2, 1, 1, 1);
		//Sample Mean Table
		addComponent(xBarTable, 3, 1, 1, 1);
		reset();
	}

	/**
	* This method handles the choice events associated with changing the sampling
	* distribution.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == distChoice){
			distType = distChoice.getSelectedIndex();
			switch(distType){
			case 0: //Normal
				param1Scroll.applyDecimalPattern("0.0");
				param1Scroll.setProperties(-5, 5, 0.5, 0, "Mean", "\u03bc");
				param2Scroll.applyDecimalPattern("0.0");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
				break;
			case 1: //Gamma
				param1Scroll.applyDecimalPattern("0.0");
				param1Scroll.setProperties(1, 5, 0.5, 1, "Shape", "k");
				param2Scroll.applyDecimalPattern("0.0");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Scale", "b");
				break;
			case 2: //Binomial
				param1Scroll.applyDecimalPattern("#");
				param1Scroll.setProperties(1, 10, 1, 1, "Number of trials", "n");
				param2Scroll.applyDecimalPattern("0.00");
				param2Scroll.setProperties(0, 1, 0.05, 0.5, "Probability of success", "p");
				break;
			case 3: //Poisson
				param1Scroll.applyDecimalPattern("0.0");
				param1Scroll.setProperties(0.5, 5, 0.5, 1, "Rate", "r");
				param2Scroll.setVisible(false);
				break;
			}
			setDistributions();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changes in the sample size
	* or the parameters of the sampling distribution.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			sample = new double[sampleSize];
			setDistributions();
		}
		else if (e.getSource() == param1Scroll.getSlider()) setDistributions();
		else if (e.getSource() == param2Scroll.getSlider())	setDistributions();
	}

	/**
	* This method resets the experiment, including the sampling and sample mean
	* random varibles, the table and graph of the sampling variable, the the table
	* and graph of the sample mean variable.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		x.reset();
		xBar.reset();
		xTable.reset();
		sampleTable.reset();
		xBarTable.reset();
		xGraph.reset();
		xBarGraph.reset();
	}

	/**
	* This method sets the distribution following changes in the parameters of the
	* sampling distribution.
	*/
	public void setDistributions(){
		switch(distType){
		case 0: //Normal
			double mu = param1Scroll.getValue();
			double sigma = param2Scroll.getValue();
			x = new RandomVariable(new NormalDistribution(mu, sigma), "X");
			xBar = new RandomVariable(new NormalDistribution(mu, sigma / Math.sqrt(sampleSize)), "M");
			break;
		case 1: //Gamma
			double shape = param1Scroll.getValue();
			double scale = param2Scroll.getValue();
			x = new RandomVariable(new GammaDistribution(shape, scale), "X");
			xBar = new RandomVariable(new GammaDistribution(sampleSize * shape, scale / sampleSize), "M");
			break;
		case 2: //Binomial
			int m = (int)param1Scroll.getValue();
			double p = param2Scroll.getValue();
			x = new RandomVariable(new BinomialDistribution(m, p), "X");
			xBar = new RandomVariable(new LocationScaleDistribution(new BinomialDistribution(sampleSize * m, p), 0, 1.0 / sampleSize), "M");
			break;
		case 3: //Poisson
			double lambda = param1Scroll.getValue();
			x = new RandomVariable(new PoissonDistribution(lambda), "X");
			xBar = new RandomVariable(new LocationScaleDistribution(new PoissonDistribution(sampleSize * lambda), 0, 1.0 / sampleSize), "M");
			break;
		}
		xGraph.setRandomVariable(x);
		xTable.setRandomVariable(x);
		xBarGraph.setRandomVariable(xBar);
		xBarTable.setRandomVariable(xBar);
		reset();
	}

	/**
	* This method defines the experiment. The sample is simulated and the sample mean
	* computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		x.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = x.simulate();
		xBar.setValue(x.getIntervalData().getMean());
	}

	/**
	* This method updates the display, including the table and graph of the sampling
	* variable, the table and graph of the sample mean variable, the record table
	* and the sample table.
	*/
	public void update(){
		super.update();
		xTable.repaint();
		sampleTable.setData(sample);
		xBarTable.repaint();
		recordTable.addRecord(new double[]{getTime(), x.getIntervalData().getMean(),
			x.getIntervalData().getSD()});
		xGraph.repaint();
		xBarGraph.repaint();
	}
	
	public void graphUpdate(){
		super.update();
		xGraph.setShowModelDistribution(showModelDistribution);
		xBarGraph.setShowModelDistribution(showModelDistribution);		
		xGraph.repaint();
		xBarGraph.repaint();
		
		xTable.setShowModelDistribution(showModelDistribution);
		xBarTable.setShowModelDistribution(showModelDistribution);
		xTable.repaint();
		xBarTable.repaint();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

