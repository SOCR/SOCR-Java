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
import java.awt.Dimension;
import java.awt.event.ItemEvent;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.MeanEstimateGraph;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.StudentDistribution;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the interval estimation experiment in the standard normal model.
* A random sample from the normal distribution is simulated, and then an interval
* estimate of the mean is computed. The user can choose whether to compute the interval
* based on the standard normal statistic or the student t statistic. The confidence
* level and sample size can be chosen.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MeanEstimateExperiment extends Experiment implements Serializable{
	//Constants
	public final static int INTERVAL = 0, LOWER_BOUND = 1, UPPER_BOUND = 2, NORMAL = 0, STUDENT = 1;
	//Variables
	private int distType = 0, sampleSize = 5, intervalType = INTERVAL;
	private boolean sigmaKnown = true;
	private double[] sample;
	private double level = 0.95, lowerEstimate, upperEstimate, lowerCritical, upperCritical;
	private String stdScoreName = "Z";
	//Objects
	private RecordTable recordTable = new RecordTable();
	private JToolBar toolBar1 = new JToolBar("Parameter Toolbar");
	private JToolBar toolBar2 = new JToolBar("Distribution Toolbar");
	private JComboBox distChoice = new JComboBox(), intervalChoice = new JComboBox(), pivotChoice = new JComboBox();
	private Parameter param1Scroll = new Parameter(-5, 5, 0.5, 0, "Mean", "\u03bc");
	private Parameter param2Scroll = new Parameter(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
	private Parameter sampleScroll = new Parameter(5, 100, 5, sampleSize, "Sample size", "n");
	private Parameter levelScroll = new Parameter(0.05, 0.95, 0.05, level, "Confidence level", "1 - \u03b1");
	private JLabel criticalLabel = new JLabel("z = \u00b11.645");
	//Random Variables
	private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
	private RandomVariable success = new RandomVariable(new BernoulliDistribution(level), "I");
	private Distribution pivotDistribution = new NormalDistribution(0, 1);
	//Graphs
	private MeanEstimateGraph estimateGraph = new MeanEstimateGraph(x);
	private CriticalGraph criticalGraph = new CriticalGraph(pivotDistribution);
	private RandomVariableGraph successGraph = new RandomVariableGraph(success);
	//Tables
	private RandomVariableTable successTable = new RandomVariableTable(success);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the toolbars with scrollbars and labels
	* for changing parameters, choices for the sampling distribution, interval type, and confidence level,
	* graphs and tables for the sampling variable, the standard score variable, and the success variable.
	*/
	public void init(){
		super.init();
		setName("Mean Estimate Experiment");
		//Critical label
		criticalLabel.setToolTipText("Critical values");
		//Distribution Choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Distribution");
		distChoice.addItem("Normal");
		distChoice.addItem("Gamma");
		distChoice.addItem("Uniform");
		//Interval choice
		intervalChoice.addItemListener(this);
		intervalChoice.setToolTipText("Interval type");
		intervalChoice.addItem("Two-sided");
		intervalChoice.addItem("Lower Bound");
		intervalChoice.addItem("Upper Bound");
		//Pivot variable choice choice
		pivotChoice.addItemListener(this);
		pivotChoice.setToolTipText("Pivot variable");
		pivotChoice.addItem("Normal");
		pivotChoice.addItem("Student");
		//Scroll bars
		param1Scroll.applyDecimalPattern("0.0");
		param1Scroll.getSlider().addChangeListener(this);
		param2Scroll.applyDecimalPattern("0.0");
		param2Scroll.getSlider().addChangeListener(this);
		sampleScroll.getSlider().addChangeListener(this);
		levelScroll.applyDecimalPattern("0.00");
		levelScroll.getSlider().addChangeListener(this);
		//Toolbars
		toolBar1.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar1.add(pivotChoice);
		toolBar1.add(intervalChoice);
		toolBar1.add(sampleScroll);
		toolBar1.add(levelScroll);
		toolBar1.add(criticalLabel);
		toolBar2.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar2.add(distChoice);
		toolBar2.add(param1Scroll);
		toolBar2.add(param2Scroll);
		addToolBar(toolBar1);
		addToolBar(toolBar2);
		//Construct graph panel
		//Estimate Graph
		estimateGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(estimateGraph, 0, 0, 1, 1);
		//Critical Graph
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		//Success Graph
		successGraph.setMinimumSize(new Dimension(50, 100));
		successGraph.setMomentType(0);
		addComponent(successGraph, 2, 0, 1, 1);
		//Record Table
		addComponent(recordTable, 0, 1, 1, 1);
		//Sample Table
		addComponent(sampleTable, 1, 1, 1, 1);
		//Success Table
		successTable.setStatisticsType(0);
		addComponent(successTable, 2, 1, 1, 1);
		//Set parameters
		setParameters();
	}

	/**
	* This method resets the experiment, including the sampling variable, standard score variable
	* and success variable, and the corresponding graphs and tables.
	*/
	public void reset(){
		super.reset();
		//Random variables
		x.reset();
		success.reset();
		//Initialize sample array
		sample = new double[sampleSize];
		//Graphs
		estimateGraph.repaint();
		successGraph.reset();
		criticalGraph.setValueDrawn(false);
		//Tables
		sampleTable.reset();
		if (sigmaKnown) recordTable.setVariableNames(new String[]{"Run", "L", "R", "Z", "I"});
		else recordTable.setVariableNames(new String[]{"Run", "L", "R", "T", "I"});
		successTable.reset();
	}

	/**
	* This method defines the experiment.  The sample from the sampling distribution is simulated,
	* the confidence interval is computed.
	*/
	public void doExperiment(){
		double stdError, sampleMean, mean;
		super.doExperiment();
		x.reset();
		//Sample
		for (int i = 0; i < sampleSize; i++) sample[i] = x.simulate();
		//Compute interval estimate
		sampleMean = x.getIntervalData().getMean();
		mean = x.getDistribution().getMean();
		if (sigmaKnown) stdError = x.getDistribution().getSD() / Math.sqrt(sampleSize);
		else stdError = x.getIntervalData().getSD() / Math.sqrt(sampleSize);
		criticalGraph.setValue((sampleMean - mean) / stdError);
		lowerEstimate = sampleMean - upperCritical * stdError;
		upperEstimate = sampleMean - lowerCritical * stdError;
		//Determine success
		if (lowerEstimate < mean & mean < upperEstimate) success.setValue(1);
		else success.setValue(0);
	}

  	/**
  	* This method runs the experiment one time, and plays a sound depending on the outcome.
  	*/
  	public void step(){
		doExperiment();
		update();
		//playnote((int)success.getValue());
	}


	/**
	* This method handles evens related to the choice boxes, for the choice of
	* sampling distribution, interval type, significance level, type of pivot variable.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == distChoice){
			distType = distChoice.getSelectedIndex();
			switch(distType){
			case 0:	 //Normal
				param1Scroll.setProperties(-5, 5, 0.5, 0, "Mean", "\u03bc");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
				break;
			case 1:	 //Gamma
				param1Scroll.setProperties(1, 5, 0.5, 1, "Shape", "a");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Scale", "b");
				break;
			case 2:	 //Uniform
				param1Scroll.setProperties(-5, 5, 1, 0, "Left endpoint", "a");
				param2Scroll.setProperties(1, 10, 1, 1, "Length", "l");
				break;
			}
			setDistribution();
		}
		else if (e.getSource() == intervalChoice){
			intervalType = intervalChoice.getSelectedIndex();
			setParameters();
		}
		else if (e.getSource() == pivotChoice){
			sigmaKnown = (pivotChoice.getSelectedIndex() == 0);
			setParameters();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changing the distribution
	* parameters.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == levelScroll.getSlider()){
			level = levelScroll.getValue();
			success = new RandomVariable(new BernoulliDistribution(level), "I");
			successGraph.setRandomVariable(success);
			successTable.setRandomVariable(success);
			setParameters();
		}
		else if (e.getSource() == param1Scroll.getSlider()) setDistribution();
		else if (e.getSource() == param2Scroll.getSlider()) setDistribution();
	}

	/**
	* This method updates the experiment, including the graphs and tables.
	*/
	public void update(){
		super.update();
		//Graphs
		estimateGraph.setEstimates(lowerEstimate, upperEstimate);
		estimateGraph.repaint();
		criticalGraph.setValueDrawn(true);
		successGraph.repaint();
		//Tables
		sampleTable.setData(sample);
		recordTable.addRecord(new double[]{getTime(), lowerEstimate, upperEstimate,
			criticalGraph.getValue(), success.getValue()});
		successTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		estimateGraph.setShowModelDistribution(showModelDistribution);
		successGraph.setShowModelDistribution(showModelDistribution);
		criticalGraph.setShowModelDistribution(showModelDistribution);
		estimateGraph.setEstimates(lowerEstimate, upperEstimate);
		estimateGraph.repaint();
		successGraph.repaint();
		criticalGraph.repaint();
		successTable.setShowModelDistribution(showModelDistribution);
		successTable.repaint();
	}
	/**
	* This method sets the standard score variable and computes the critical values.
	*/
	public void setParameters(){
		if (sigmaKnown){
			pivotDistribution = new NormalDistribution(0, 1);
			stdScoreName = "z";
			recordTable.setDescription("L: lower bound, R: upper bound, Z: pivot variable, I: success indicator");
		}
		else {
			pivotDistribution = new StudentDistribution(sampleSize - 1);
			stdScoreName = "t";
			recordTable.setDescription("L: lower bound, R: upper bound, T: pivot variable, I: success indicator");
		}
		switch(intervalType){
		case INTERVAL:
			upperCritical = pivotDistribution.getQuantile(0.5 * (1 + level));
			lowerCritical = -upperCritical;
			criticalLabel.setText(stdScoreName + " = \u00b1" + format(upperCritical));
			break;
		case LOWER_BOUND:
			upperCritical = pivotDistribution.getQuantile(level);
			lowerCritical = Double.NEGATIVE_INFINITY;
			criticalLabel.setText(stdScoreName + " = " + format(upperCritical));
			break;
		case UPPER_BOUND:
			lowerCritical = -pivotDistribution.getQuantile(level);
			upperCritical = Double.POSITIVE_INFINITY;
			criticalLabel.setText(stdScoreName + " = " + format(lowerCritical));
			break;
		}
		criticalGraph.setDistribution(pivotDistribution);
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		reset();
	}

	/**
	* This method updates the sampling distribution when the parameters have changed.
	*/
	public void setDistribution(){
		switch(distType){
		case 0:	 //Normal
			double mu = param1Scroll.getValue();
			double sigma = param2Scroll.getValue();
			x = new RandomVariable(new NormalDistribution(mu, sigma), "X");
			break;
		case 1:	 //Gamma
			double shape = param1Scroll.getValue();
			double scale = param2Scroll.getValue();
			x = new RandomVariable(new GammaDistribution(shape, scale), "X");
			break;
		case 2:	 //Uniform
			double leftPoint = param1Scroll.getValue();
			double length = param2Scroll.getValue();
			x = new RandomVariable(new ContinuousUniformDistribution(leftPoint, leftPoint + length), "X");
			break;
		}
		estimateGraph.setRandomVariable(x);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

