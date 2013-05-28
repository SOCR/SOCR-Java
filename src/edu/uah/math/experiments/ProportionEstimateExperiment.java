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
import edu.uah.math.devices.MeanEstimateGraph;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the experiment of estimating the probability of success in the
* Bernoulli trials model.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ProportionEstimateExperiment extends Experiment implements Serializable{
	//Constants
	public final static int INTERVAL = 0, LOWER_BOUND = 1, UPPER_BOUND = 2;
	//Variables
	private double p = 0.5, lowerCritical = -1.645, upperCritical = 1.645, level = 0.95,
		lowerEstimate, upperEstimate;
	private double[] sample;
	private int sampleSize = 5, intervalType = INTERVAL;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "L", "R", "Z", "I"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox intervalChoice = new JComboBox();
	private Parameter pScroll = new Parameter(0.05, 0.95, 0.05, p, "Probability of success", "p");
	private Parameter levelScroll = new Parameter(0.05, 0.95, 0.05, level, "confidence level", "1 - \u03b1");
	private Parameter sampleScroll = new Parameter(5, 100, 5, sampleSize, "Sample size", "n");
	private JLabel criticalLabel = new JLabel("z = \u00b11.645");
	//Random Variables
	private RandomVariable x = new RandomVariable(new BernoulliDistribution(p), "X");
	private RandomVariable success = new RandomVariable(new BernoulliDistribution(level), "I");
	private NormalDistribution pivotDistribution = new NormalDistribution(0, 1);
	//Graphs
	private MeanEstimateGraph estimateGraph = new MeanEstimateGraph(x);
	private CriticalGraph criticalGraph = new CriticalGraph(pivotDistribution);
	private RandomVariableGraph successGraph = new RandomVariableGraph(success);
	//Tables
	private RandomVariableTable successTable = new RandomVariableTable(success);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the record table, the sliders
	* for the sample size and the probability of heads, the choice boxes for the type
	* of interval and the significance level, the toolbar, the random variable graph
	* and table for the success random varaible, and the estimate and standard score
	* graphs.
	*/
	public void init(){
		super.init();
		setName("Proportion Estimate Experiment");
		//p Slider
		pScroll.applyDecimalPattern("0.00");
		pScroll.setWidth(150);
		pScroll.getSlider().addChangeListener(this);
		//Sample size slider
		sampleScroll.setWidth(125);
		sampleScroll.getSlider().addChangeListener(this);
		levelScroll.getSlider().addChangeListener(this);
		//Signficance level slider
		levelScroll.setWidth(150);
		levelScroll.applyDecimalPattern("0.00");
		//Graphs and tables
		successGraph.setMomentType(0);
		successTable.setStatisticsType(0);
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		criticalLabel.setToolTipText("Critical values");
		//Interval Choice
		intervalChoice.addItemListener(this);
		intervalChoice.setToolTipText("Interval Type");
		intervalChoice.addItem("Two-sided");
		intervalChoice.addItem("Lower Bound");
		intervalChoice.addItem("Upper Bound");
		//Construct toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(intervalChoice);
		toolBar.add(pScroll);
		toolBar.add(sampleScroll);
		toolBar.add(levelScroll);
		toolBar.add(criticalLabel);
		addToolBar(toolBar);
		//Estimate Graph
		estimateGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(estimateGraph, 0, 0, 1, 1);
		//Critical Graph
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		//Success Graph
		successGraph.setMinimumSize(new Dimension(50, 100));
		addComponent(successGraph, 2, 0, 1, 1);
		//Record Table
		recordTable.setDescription("(L, R): interval estimate, Z: standard score, I: interval contains p");
		addComponent(recordTable, 0, 1, 1, 1);
		//Sample Table
		addComponent(sampleTable, 1, 1, 1, 1);
		//Success Table
		addComponent(successTable, 2, 1, 1, 1);
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
			+ "The experiment is to select a random sample of size n from the Bernoulli distribution with\n"
			+ "parameter p, and then to construct an approximate confidence interval for p at a specified\n"
			+ "confidence level. The true value of p can be varied with a scroll bar. The density of the\n"
			+ "Bernoulli distribution and the the value of p are shown in blue in the first graph. The \n"
			+ "confidence level can be selected from a list box, as can the type of interval--two sided,\n"
			+ "upper bound, or lower bound. The interval is constructed using quantiles from the standard\n"
			+ "normal distribution. The standard normal density and the critical values are shown in blue in\n"
			+ "the second graph. Variables L and R denote the left and right endpoints of the confidence\n"
			+ "interval and I indicates the event that the confidence interval contains p. The theoretical\n"
			+ "density of I is shown in blue in the third graph. On each update, the sample density and the\n"
			+ "confidence interval are shown in red in the first graph, and the computed standard score is\n"
			+ "shown in red in the second graph. Note that the confidence interval contains p in the first\n"
			+ "graph if and only if the standard score falls between the critical values in the second graph.\n"
			+ "The third graph shows the proportion of successes and failures in red. The first table gives\n"
			+ "the sample values; the second table records L, R, the standard score Z, and I. Finally, the\n"
			+ "third table gives the theoretical and empirical densities of I.";
	}

	/**
	* This method resets the exeperiment, including the sampling, standard score, and
	* success random variables, the estimate, standard score and success graphs, and
	* the sample, record, and success tables.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		x.reset();
		criticalGraph.setValueDrawn(false);
		success.reset();
		sample = new double[sampleSize];
		estimateGraph.repaint();
		successGraph.repaint();
		sampleTable.reset();
		successTable.repaint();
	}

	/**
	* This method defines the experiment. The sample is computed and the interval
	* estimate of the probability parameter computed.
	*/
	public void doExperiment(){
		double sampleMean, stdError;
		super.doExperiment();
		x.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = x.simulate();
		sampleMean = x.getIntervalData().getMean();
		stdError = Math.sqrt(sampleMean * (1 - sampleMean) / sampleSize);
		criticalGraph.setValue((sampleMean - x.getDistribution().getMean()) / stdError);
		lowerEstimate = sampleMean - upperCritical * stdError;
		upperEstimate = sampleMean - lowerCritical * stdError;
		if (lowerEstimate < p & p < upperEstimate) success.setValue(1);
		else success.setValue(0);
	}

	/**
	* This method runs the experiment one time, playing a sound that depends on
	* the outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote((int)success.getValue());
	}

	/**
	* This method handles the choice events associated with changing the interval type
	* of the confidence level.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == intervalChoice){
			intervalType = intervalChoice.getSelectedIndex();
			setParameters();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changing the sample size
	* or the success parameter.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			reset();
		}
		else if (e.getSource() == pScroll.getSlider()){
			p = pScroll.getValue();
			x = new RandomVariable(new BernoulliDistribution(p), "X");
			estimateGraph.setRandomVariable(x);
			reset();
		}
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
	* This method updates the experiment, including the estimate, standard score,
	* and success graphs, the sample record, and success tables.
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
		recordTable.addRecord(new double[]{getTime(), lowerEstimate,
			upperEstimate, criticalGraph.getValue(), success.getValue()});
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
	* This method sets the parameters: the upper and lower critical values.
	*/
	public void setParameters(){
		switch(intervalType){
		case INTERVAL:
			upperCritical = pivotDistribution.getQuantile(0.5 * (1 + level));
			lowerCritical = -upperCritical;
			criticalLabel.setText("z = \u00b1" + format(upperCritical));
			break;
		case LOWER_BOUND:
			upperCritical = pivotDistribution.getQuantile(level);
			lowerCritical = Double.NEGATIVE_INFINITY;
			criticalLabel.setText("z = " + format(upperCritical));
			break;
		case UPPER_BOUND:
			lowerCritical = -pivotDistribution.getQuantile(level);
			upperCritical = Double.POSITIVE_INFINITY;
			criticalLabel.setText("z = " + format(lowerCritical));
			break;
		}
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}
