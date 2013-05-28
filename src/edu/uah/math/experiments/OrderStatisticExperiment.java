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
import java.io.Serializable;
import java.awt.event.ItemEvent;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.distributions.Functions;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.ExponentialDistribution;
import edu.uah.math.distributions.OrderStatisticDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This experiment illustrates the distribution of the order statistic corresponding to a specified
* distribution and a specified order.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class OrderStatisticExperiment extends Experiment implements Serializable{
	//Variables
	private int sampleSize = 5, order = 1;
	private double[] sample = new double[sampleSize], orderStatistic = new double[sampleSize];
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "X(1)", "X(2)", "X(3)", "X(4)", "X(5)"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private Distribution basicDist = new ContinuousUniformDistribution(0, 1);
	private RandomVariable basicRV = new RandomVariable(basicDist, "X");
	private OrderStatisticDistribution orderDist = new OrderStatisticDistribution(basicDist, sampleSize, order);
	private RandomVariable orderRV = new RandomVariable(orderDist,  "X(1)");
	private JComboBox distChoice = new JComboBox();
	private Parameter sampleScroll = new Parameter(1, 10, 1, sampleSize, "Sample size", "n");
	private Parameter orderScroll = new Parameter(1, sampleSize, 1, order, "Order", "k");
	private RandomVariableGraph basicGraph = new RandomVariableGraph(basicRV);
	private RandomVariableGraph orderGraph = new RandomVariableGraph(orderRV);
	private RandomVariableTable orderTable = new RandomVariableTable(orderRV);

	/**
	* This method initializes the experiment, including the toolbar with scrollbars, the random
	* variable graphs and tables.
	*/
	public void init(){
		super.init();
		setName("Order Statistics Experiment");
		//Scrollbars
		sampleScroll.getSlider().addChangeListener(this);
		orderScroll.getSlider().addChangeListener(this);
		//Construct distribution choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Sampling distribution");
		distChoice.addItem("Uniform (0, 1)");
		distChoice.addItem("Exponential (1)");
		//Construct toolbar
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(distChoice);
		toolBar.add(sampleScroll);
		toolBar.add(orderScroll);
		addToolBar(toolBar);
		//Basic Graph
		addComponent(basicGraph, 0, 0, 1, 1);
		//Order Graph
		addComponent(orderGraph, 1, 0, 1, 1);
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
			+ "The experiment consists of selecting a random sample of size n from a specified\n"
			+ "distribution. The sampling distribution can be selected as either the uniform\n"
			+ "distribution on (0, 1) or the exponential distribution with parameter 1. The first\n"
			+ "graph shows the density function and moments of the sampling distribution in blue.\n"
			+ "For each run of the experiment, the sample density and moments are shown in red.\n"
			+ "The sample values are arranged in increasing order (these are the order statistics)\n"
			+ "and recorded in first table. For a specified k, the k'th order statistic is recorded\n"
			+ "on each update in the second table. The density and moments of X(k) are shown in the\n"
			+ "second graph in blue. As the experiment runs, the empirical density is shown in red.\n"
			+ "The moments of X(k) are given in the last table. As the experiment runs, the empirical\n"
			+ "moments are given also. The parameters n and k can be varied with scroll bars.";
	}

	/**
	* This method handles the choice events that correspond to changing the basic
	* distribution.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == distChoice){
			switch(distChoice.getSelectedIndex()){
			case 0: //uniform
				basicDist = new ContinuousUniformDistribution(0, 1);
				break;
			case 1: //Exponential
				basicDist = new ExponentialDistribution(1);
				break;
			}
			setDistributions();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events that correspond to changing the
	* sample size or the order.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			sample = new double[sampleSize];
			orderStatistic = new double[sampleSize];
			order = 1;
			orderScroll.setRange(1, sampleSize, 1, order);
			setDistributions();
		}
		else if (e.getSource() == orderScroll.getSlider()){
			order = (int)orderScroll.getValue();
			setDistributions();
		}
	}

	/**This method sets the distributions.*/
	public void setDistributions(){
		basicRV = new RandomVariable(basicDist, "X");
		orderDist.setParameters(basicDist, sampleSize, order);
		orderRV.setName("X(" + order + ")");
		basicGraph.setRandomVariable(basicRV);
		String[] variableNames = new String[sampleSize + 1];
		variableNames[0] = "Run";
		for (int i = 1; i <= sampleSize; i++) variableNames[i] = "X(" + i + ")";
		recordTable.setVariableNames(variableNames);
		reset();
	}

	/**
	* This method resets the experiment, including the random variable graphs and tables,
	* the sample table, and the record table.
	*/
	public void reset(){
		super.reset();
		basicRV.reset();
		orderRV.reset();
		orderTable.reset();
		recordTable.reset();
		basicGraph.reset();
		orderGraph.reset();
	}

	/**
	* This method defines the experiment.  A sample of the specified size from the basic
	* distribution is simulated and then the appropriate order statistic is computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		double x;
		boolean smallest;
		basicRV.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = basicRV.simulate();
		orderStatistic = Functions.sort(sample);
		orderRV.setValue(orderStatistic[order - 1]);
	}

	/**
	* This method updates the epxeriment, including the sample table, the record table,
	* and the random variable graphs and table.
	*/
	public void update(){
		super.update();
		double[] record = new double[sampleSize + 1];
		record[0] = getTime();
		for (int i = 0; i < sampleSize; i++) record[i + 1] = orderStatistic[i];
		recordTable.addRecord(record);
		basicGraph.reset();
		orderGraph.repaint();
		orderTable.repaint();
	}
	public void graphUpdate(){
		super.update();
		basicGraph.setShowModelDistribution(showModelDistribution);
		orderGraph.setShowModelDistribution(showModelDistribution);
		basicGraph.reset();
		orderGraph.repaint();
		orderTable.setShowModelDistribution(showModelDistribution);
		orderTable.repaint();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}
