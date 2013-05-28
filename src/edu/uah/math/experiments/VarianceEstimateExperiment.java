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
import java.awt.event.ItemEvent;
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.VarianceEstimateGraph;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.ChiSquareDistribution;

/**
* The experiment is to select a random sample of size n from a specified distribution,
* and then to construct an approximate confidence interval for the standard deviation at a
* specified confidence level. The distribution can be chosen with a list box; the options
* are normal, gamma, and uniform. In each case, the appropriate parameters and the sample
* size can be varied with scroll bars. The density, mean, and standard deviation of the
* selected distribution are shown in blue in the first graph. The confidence level can be
* selected from a list box, as can the type of interval--two sided, upper bound, or lower
* bound. The interval can be constructed assuming either that the distribution mean is known
* or unknown. In the first case the pivot variable V has the chi-square distribution with
* degrees of freedom; in the second case the pivot variable V has chi-square distribution
* with n - 1 degrees of freedom. The density and the critical values of V are shown in blue
* in the second graph. Variables L and R denote the left and right endpoints of the confidence
* interval and I indicates the event that the confidence interval contains the distribution
* mean. The theoretical density of I is shown in blue in the third graph. On each update,
* the sample density and the confidence interval are shown in red in the first graph, and the
* value of V is shown in red in the second graph. Note that the confidence interval contains
* the mean in the first graph if and only if V falls between the critical values in the
* second graph. The third graph shows the proportion of successes and failures in red.
* The first table gives the sample values; the second table records L, R, V, and I. Finally,
* the third table gives the theorectial and empirical densities of I.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class VarianceEstimateExperiment extends Experiment implements Serializable{
	//Constants
	public final static int INTERVAL = 0, LOWER_BOUND = 1, UPPER_BOUND = 2;
	//Variables
	private int distType = 0, sampleSize = 10, intervalType = INTERVAL;
	private boolean muKnown = true;
	private double lowerEstimate, upperEstimate, lowerCritical, upperCritical, level = 0.9;
	private double[] sample;
	//Objects
	private JToolBar toolBar1 = new JToolBar("Parameter Toolbar");
	private JToolBar toolBar2 = new JToolBar("Distribution Toolbar");
	private JComboBox distChoice = new JComboBox();
	private JComboBox intervalChoice = new JComboBox();
	private JComboBox muChoice = new JComboBox();
	private Parameter param1Scroll = new Parameter(-5, 5, 0.5, 0, "Mean", "\u03bc");
	private Parameter param2Scroll = new Parameter(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
	private Parameter sampleScroll = new Parameter(5, 100, 1, sampleSize, "Sample size", "n");
	private Parameter levelScroll = new Parameter(0.01, 0.99, 0.01, level, "Confidence level", "1 - \u03b1");
	private JLabel criticalLabel = new JLabel("v = 3.33, 16.92");
	//Random Variables
	private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
	private RandomVariable success = new RandomVariable(new BernoulliDistribution(level), "I");
	private ChiSquareDistribution pivotDistribution = new ChiSquareDistribution(sampleSize - 1);
	//Graphs
	private VarianceEstimateGraph estimateGraph = new VarianceEstimateGraph(x);
	private CriticalGraph criticalGraph = new CriticalGraph(pivotDistribution);
	private RandomVariableGraph successGraph = new RandomVariableGraph(success);
	//Tables
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "L", "R", "V", "I"});
	private RandomVariableTable successTable = new RandomVariableTable(success);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the record table, the label for
	* the critical values, the sliders that control the parameters of the sampling
	* distribution, the choice box for the type of distribution, the choice box for the
	* type of interval, the choice box for the confidence level, the choice box for
	* mu known or unknown, the toolbar, the estimate graph, the chi-square graph, the
	* success graph, and the success table.
	*/
	public void init(){
		super.init();
		setName("Variance Estimate Experiment");
		//Critical label
		criticalLabel.setToolTipText("Critical value");
		//Parameter 1 slider
		param1Scroll.applyDecimalPattern("0.0");
		param1Scroll.getSlider().addChangeListener(this);
		//Parameter 2 slider
		param2Scroll.applyDecimalPattern("0.0");
		param2Scroll.getSlider().addChangeListener(this);
		//Sample Scroll
		sampleScroll.setWidth(125);
		sampleScroll.getSlider().addChangeListener(this);
		//Level Slicer
		levelScroll.applyDecimalPattern("0.00");
		levelScroll.setWidth(150);
		levelScroll.getSlider().addChangeListener(this);
		//Distribution choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Distribution");
		distChoice.addItem("Normal");
		distChoice.addItem("Gamma");
		distChoice.addItem("Uniform");
		//Interval Choice
		intervalChoice.addItemListener(this);
		intervalChoice.setToolTipText("Interval Type");
		intervalChoice.addItem("Two-sided");
		intervalChoice.addItem("Lower Bound");
		intervalChoice.addItem("Upper Bound");
		//Mu choice
		muChoice.addItemListener(this);
		muChoice.setToolTipText("Model");
		muChoice.addItem("\u03bc Known");
		muChoice.addItem("\u03bc Unknown");
		//Construct toolbars
		toolBar1.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar1.add(intervalChoice);
		toolBar1.add(muChoice);
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
		estimateGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(estimateGraph, 0, 0, 1, 1);
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		successGraph.setMinimumSize(new Dimension(50, 100));
		successGraph.setMomentType(0);
		addComponent(successGraph, 2, 0, 1, 1);
		//Tables
		recordTable.setDescription("(L,R): interval estimate, V: pivot variable, I: interval contains mean");
		addComponent(recordTable, 0, 1, 1, 1);
		addComponent(sampleTable, 1, 1, 1, 1);
		successTable.setStatisticsType(0);
		addComponent(successTable, 2, 1, 1, 1);
		//Final actions
		validate();
		setParameters();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment is to select a random sample of size n from a specified distribution,\n"
			+ "and then to construct an approximate confidence interval for the standard deviation at a\n"
			+ "specified confidence level. The distribution can be chosen with a list box; the options\n"
			+ "are normal, gamma, and uniform. In each case, the appropriate parameters and the sample\n"
			+ "size can be varied with scroll bars. The density, mean, and standard deviation of the\n"
			+ "selected distribution are shown in blue in the first graph. The confidence level can be\n"
			+ "selected from a list box, as can the type of interval--two sided, upper bound, or lower\n"
			+ "bound. The interval can be constructed assuming either that the distribution mean is known\n"
			+ "or unknown. In the first case the pivot variable V has the chi-square distribution with n\n"
			+ "degrees of freedom; in the second case the pivot variable V has chi-square distribution\n"
			+ "with n - 1 degrees of freedom. The density and the critical values of V are shown in blue\n"
			+ "in the second graph. Variables L and R denote the left and right endpoints of the confidence\n"
			+ "interval and I indicates the event that the confidence interval contains the distribution\n"
			+ "mean. The theoretical density of I is shown in blue in the third graph. On each update,\n"
			+ "the sample density and the confidence interval are shown in red in the first graph, and the\n"
			+ "value of V is shown in red in the second graph. Note that the confidence interval contains\n"
			+ "the mean in the first graph if and only if V falls between the critical values in the\n"
			+ "second graph. The third graph shows the proportion of successes and failures in red.\n"
			+ "The first table gives the sample values; the second table records L, R, V, and I. Finally,\n"
			+ "the third table gives the theorectial and empirical densities of I.";
	}

	/**
	* This method resets the experiment, including the samping and chi-square random
	* variables, the success graph, the sample array, the estimate graph, the chi-
	* square graph,the sample table, the record table, and the success table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		x.reset();
		success.reset();
		sample = new double[sampleSize];
		estimateGraph.repaint();
		criticalGraph.setValueDrawn(false);
		successGraph.reset();
		sampleTable.reset();
		successTable.reset();
	}

	/**
	* This method defines the experiment. The sample is simulated and the interval estimate
	* computed. The success variable is computed.
	*/
	public void doExperiment(){
		double mean, variance, sumSquares;
		super.doExperiment();
		x.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = x.simulate();
		if (muKnown) mean = x.getDistribution().getMean();
		else mean = x.getIntervalData().getMean();
		variance = x.getDistribution().getVariance();
		sumSquares = 0;
		for (int i = 0; i < sampleSize; i++) sumSquares = sumSquares + (sample[i] - mean) * (sample[i] - mean);
		criticalGraph.setValue(sumSquares / variance);
		lowerEstimate = sumSquares / upperCritical;
		upperEstimate = sumSquares / lowerCritical;
		if (lowerEstimate < variance & variance < upperEstimate) success.setValue(1);
		else success.setValue(0);
	}

	/**
	* This method runs the experiment one time, playing a sound that depends on the
	* outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote((int)success.getValue());
	}

	/**
	* This method handles the choice events associated with changing distribution type,
	* interval type, confidence level, or the state of knowledge of mu.
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
				param1Scroll.setProperties(1, 5, 0.5, 1, "Shape", "k");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Scale", "b");
				break;
			case 2:	 //Uniform
				param1Scroll.setProperties(-5, 5, 0.5, 0, "Left endpoint", "a");
				param2Scroll.setProperties(1, 10, 0.5, 1, "Length", "l");
				break;
			}
			setDistribution();
		}
		else if (e.getSource() == intervalChoice){
			intervalType = intervalChoice.getSelectedIndex();
			setParameters();
		}
		else if (e.getSource() == muChoice){
			muKnown = (muChoice.getSelectedIndex() == 0);
			setParameters();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changing the sample size or
	* the distribution parameters.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == param1Scroll.getSlider()) setDistribution();
		else if (e.getSource() == param2Scroll.getSlider()) setDistribution();
		else if (e.getSource() == levelScroll.getSlider()){
			level = levelScroll.getValue();
			success = new RandomVariable(new BernoulliDistribution(level), "I");
			successGraph.setRandomVariable(success);
			successTable.setRandomVariable(success);
			setParameters();
		}
		else super.stateChanged(e);
	}

	/**
	* This method updates the displays, including the estimate graph, the chi-square
	* graph, the success graph, the sample table, the record table, and the success
	* table.
	*/
	public void update(){
		super.update();
		//Graphs
		estimateGraph.setEstimates(Math.sqrt(lowerEstimate), Math.sqrt(upperEstimate));
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
		criticalGraph.setShowModelDistribution(showModelDistribution);
		successGraph.setShowModelDistribution(showModelDistribution);
		estimateGraph.repaint();
		criticalGraph.repaint();
		successGraph.repaint();
		successTable.setShowModelDistribution(showModelDistribution);
		successTable.repaint();
	}
	/**
	* This method computes the upper and lower critical values when the distribution
	* or parameters have changed.
	*/
	public void setParameters(){
		double alpha = 1 - level;
		int df;
		switch(intervalType){
		case INTERVAL:
			upperCritical = pivotDistribution.getQuantile(1 - alpha / 2);
			lowerCritical = pivotDistribution.getQuantile(alpha / 2);
			criticalLabel.setText("v = " + format(lowerCritical) + ", " + format(upperCritical));
			break;
		case LOWER_BOUND:
			upperCritical = pivotDistribution.getQuantile(1 - alpha);
			lowerCritical = +0.0;
			criticalLabel.setText("v = " + format(upperCritical));
			break;
		case UPPER_BOUND:
			upperCritical = Double.POSITIVE_INFINITY;
			lowerCritical = pivotDistribution.getQuantile(alpha);
			criticalLabel.setText("v = " + format(lowerCritical));
			break;
		}
		if(muKnown) df = sampleSize;
		else df = sampleSize - 1;
		pivotDistribution = new ChiSquareDistribution(df);
		criticalGraph.setDistribution(pivotDistribution);
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		reset();
	}

	/**
	* This method sets the scrollbars to the appropriate ranges when the distribution type
	* changes.
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

