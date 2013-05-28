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
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.MedianGraph;
import edu.uah.math.devices.DataTable;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.BinomialDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.IntervalData;

/**
* This class models the sign test experiment.
* The experiment is to select a random sample of size n from a distribution, and then to
* perform a hypothesis test about the median m of the distribution at a specified significance
* level. The distribution can be selected from a list box; the options are the normal, gamma,
* and uniform distributions. In each case, the appropriate parameters and the sample size
* can be varied with scroll bars. The significance level can be selected with a list box
* The null hypothesized value m0 of the median can be selected with a scroll bar; the true
* quantile level p of m0 is also given. The density of the distribution and m are shown in
* blue in the first graph; m0 is shown in green. The test statistic N is the number of sample
* values greater than m0. Under the null hypothesis, N has the binomial distribution with
* parameters n and 1/2. The density of this distribution and the critical values are shown in
* the second graph in blue. On each update, the sample density is shown in red in the first
* graph and the sample values are recorded in the first table. The value of the test statistics
* N is shown in red in the second graph. The variable J indicates the event that the null
* hypothesis is rejected. On each update, N and J are recorded in the second table. Note that
* the null hypothesis is reject (J = 1) if and only if the test statistic N falls outside of
* the critical values. Finally, the empirical density of I is shown in red in the last graph
* and recorded in the last table.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class SignTestExperiment extends Experiment implements Serializable{
	//Variables
	private int sampleSize = 10, distType =  0;
	private double p = 0.5, median = 0, testMedian = 0, lowerCritical = 2, upperCritical = 8, level = 0.1;
	private double[] sample = new double[sampleSize];
	//Panels
	private JToolBar toolBar1 = new JToolBar("Distribution Toolbar");
	private JToolBar toolBar2 = new JToolBar("Parameter Toolbar");
	//Labels
	private JLabel criticalLabel = new JLabel("c = 2, d = 8  ");
	private JLabel medianLabel = new JLabel("m = 0.0 ");
	private JLabel testMedianLabel = new JLabel("m0 = 0.0");
	//Scrollbars
	private Parameter param1Scroll = new Parameter(-5, 5, 0.5, 0, "Mean", "\u03bc");
	private Parameter param2Scroll = new Parameter(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
	private Parameter sampleScroll = new Parameter(1, 50, 5, 10, "Sample size", "n");
	private Parameter quantileScroll = new Parameter(0.1, 0.9, 0.1, p, "Quantile", "p");
	private Parameter levelScroll = new Parameter(0.01, 0.99, 0.01, level, "Significance level", "\u03b1");
	//Choices
	private JComboBox distChoice = new JComboBox();
	//Random Variables
	private IntervalData reject = new IntervalData(new Domain(0, 1, 1, Domain.DISCRETE), "J");
	private RandomVariable sampleVariable = new RandomVariable(new NormalDistribution(0, 1), "X");
	private BinomialDistribution pivotDistribution = new BinomialDistribution(sampleSize, 0.5);
	//Graphs
	private Histogram rejectGraph = new Histogram(reject);
	private CriticalGraph criticalGraph = new CriticalGraph(pivotDistribution);
	private MedianGraph sampleGraph = new MedianGraph(sampleVariable, testMedian);
	//Tables
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "N", "J"});
	private DataTable rejectTable = new DataTable(reject);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the record table, the labels
	* for the median, test median, and critical values, the sliders for the parameters
	* of the sampling distribution, the sliders for the quantile and sample size,
	* the choice box for the type of distribution, the toolbars, the sign test graph,
	* the graph and table for the reject random variable, and the sample table.
	*/
	public void init(){
		super.init();
		setName("Sign Test Experiment");
		//Labels
		medianLabel.setToolTipText("Distribution median");
		testMedianLabel.setToolTipText("Test median");
		criticalLabel.setToolTipText("Critical values of N");
		//Parameter 1 Scroll
		param1Scroll.applyDecimalPattern("0.0");
		param1Scroll.getSlider().addChangeListener(this);
		//Parameter 2 Scroll
		param2Scroll.applyDecimalPattern("0.0");
		param2Scroll.getSlider().addChangeListener(this);
		//Quantile Scroll
		quantileScroll.applyDecimalPattern("0.0");
		quantileScroll.getSlider().addChangeListener(this);
		quantileScroll.setWidth(150);
		//Sample Scroll
		sampleScroll.getSlider().addChangeListener(this);
		sampleScroll.setWidth(150);
		//Level Scroll
		levelScroll.applyDecimalPattern("0.00");
		levelScroll.getSlider().addChangeListener(this);
		levelScroll.setWidth(150);
		//Disstribution choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Distribution");
		distChoice.addItem("Normal");
		distChoice.addItem("Uniform");
		distChoice.addItem("Gamma");
		//Toolbars
		//Toolbar1
		toolBar1.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar1.add(distChoice);
		toolBar1.add(param1Scroll);
		toolBar1.add(param2Scroll);
		toolBar1.add(medianLabel);
		//Toolbar2
		toolBar2.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar2.add(sampleScroll);
		toolBar2.add(quantileScroll);
		toolBar2.add(testMedianLabel);
		toolBar2.add(levelScroll);
		toolBar2.add(criticalLabel);
		addToolBar(toolBar1);
		addToolBar(toolBar2);
		//Sample Graph
		sampleGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(sampleGraph, 0, 0, 1, 1);
		//Critical Graph
		criticalGraph.setCriticalValues(2, 8);
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		//Reject Graph
		rejectGraph.setStatisticsType(0);
		rejectGraph.setMinimumSize(new Dimension(50, 100));
		addComponent(rejectGraph, 2, 0, 1, 1);
		//Record Table
		recordTable.setDescription("N: number of sample values > m0, J: reject null hypothesis");
		addComponent(recordTable, 0, 1, 1, 1);
		//Sample Table
		addComponent(sampleTable, 1, 1, 1, 1);
		//Reject Table
		rejectTable.setStatisticsType(0);
		addComponent(rejectTable, 2, 1, 1, 1);
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
			+ "The experiment is to select a random sample of size n from a distribution, and then to\n"
			+ "perform a hypothesis test about the median m of the distribution at a specified significance\n"
			+ "level. The distribution can be selected from a list box; the options are the normal, gamma,\n"
			+ "and uniform distributions. In each case, the appropriate parameters and the sample size n\n"
			+ "can be varied with scroll bars. The significance level can be selected with a list box.\n"
			+ "The null hypothesized value m0 of the median can be selected with a scroll bar; the true\n"
			+ "quantile level p of m0 is also given. The density of the distribution and m are shown in\n"
			+ "blue in the first graph; m0 is shown in green. The test statistic N is the number of sample\n"
			+ "values greater than m0. Under the null hypothesis, N has the binomial distribution with\n"
			+ "parameters n and 1/2. The density of this distribution and the critical values are shown in\n"
			+ "the second graph in blue. On each update, the sample density is shown in red in the first\n"
			+ "graph and the sample values are recorded in the first table. The value of the test statistics\n"
			+ "N is shown in red in the second graph. The variable J indicates the event that the null\n"
			+ "hypothesis is rejected. On each update, N and J are recorded in the second table. Note that\n"
			+ "the null hypothesis is reject (J = 1) if and only if the test statistic N falls outside of\n"
			+ "the critical values. Finally, the empirical density of I is shown in red in the last graph\n"
			+ "and recorded in the last table.";
	}

	/**
	* This method resets the experiment, including the sample, sign, and reject
	* random variables, the graph and table for the reject random variable, the
	* graph and table for the sample random variable.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		//Variables
		sampleVariable.reset();
		reject.reset();
		//Graphs
		criticalGraph.setValueDrawn(false);
		rejectGraph.repaint();
		sampleGraph.repaint();
		//Tables
		sampleTable.reset();
		rejectTable.repaint();
	}

	/**
	* This method defines the experiment. The sample is simulated and the sign variable
	* computed. The outcome of the test is then determined.
	*/
	public void doExperiment(){
		int signCount = 0;
		super.doExperiment();
		sampleVariable.reset();
		for (int i = 0; i < sampleSize; i++){
			sample[i] = sampleVariable.simulate();
			if (sample[i] > testMedian) signCount++;
		}
		criticalGraph.setValue(signCount);
		if (signCount < lowerCritical | signCount > upperCritical) reject.setValue(1);
		else reject.setValue(0);
	}

	/**
	* This method runs the experiment one time, playing a sound that depends on
	* the outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote((int)reject.getValue());
	}

	/**
	* This method updates the experiment, including the sample graph, the sign graph,
	* the reject graph, sample table, the record table, and the reject table.
	*/
	public void update(){
		super.update();
		//Graphs
		sampleGraph.repaint();
		criticalGraph.setValueDrawn(true);
		rejectGraph.repaint();
		//Tables
		sampleTable.setData(sample);
		recordTable.addRecord(new double[]{getTime(), criticalGraph.getValue(), reject.getValue()});
		rejectTable.repaint();
	}

	
	public void graphUpdate(){
		super.update();
		sampleGraph.setShowModelDistribution(showModelDistribution);
		rejectGraph.setShowModelDistribution(showModelDistribution);
		criticalGraph.setShowModelDistribution(showModelDistribution);
		sampleGraph.repaint();
		criticalGraph.repaint();
		rejectGraph.repaint();
	}
	/**
	* This method handles the choice events associated with changes in the
	* sampling distribution.
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
			case 1:	 //Uniform
				param1Scroll.setProperties(0, 10, 1, 0, "Left endpoint", "a");
				param2Scroll.setProperties(1, 10, 1, 1, "Length", "l");
				break;
			case 2:	 //Gamma
				param1Scroll.setProperties(1, 5, 0.5, 1, "Shape", "k");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Scale", "b");
				break;
			}
			setDistribution();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changes in the sample size
	* or changes in the parameters of the sampling distribution.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		//Sample size
		if(e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			sample = new double[sampleSize];
			pivotDistribution = new BinomialDistribution(sampleSize, 0.5);
			criticalGraph.setDistribution(pivotDistribution);
			setCriticalValues();
			reset();
		}
		else if (e.getSource() == param1Scroll.getSlider()) setDistribution();
		else if (e.getSource() == param2Scroll.getSlider()) setDistribution();
		else if (e.getSource() == quantileScroll.getSlider()){
			p = quantileScroll.getValue();
			testMedian = sampleVariable.getDistribution().getQuantile(p);
			sampleGraph.setTestMedian(testMedian);
			testMedianLabel.setText("m0 = " + format(testMedian));
			reset();
		}
		else if (e.getSource() == levelScroll.getSlider()){
			level = levelScroll.getValue();
			setCriticalValues();
		}
		else super.stateChanged(e);
	}

	/**
	* This method sets the distribution when the parameters have changed.
	*/
	public void setDistribution(){
		double mu, sigma, shape, scale, a, l;
		switch(distType){
		case 0:	 //Normal
			mu = param1Scroll.getValue();
			sigma = param2Scroll.getValue();
			sampleVariable = new RandomVariable(new NormalDistribution(mu, sigma), "X");
			break;
		case 1:	 //Uniform
			a = param1Scroll.getValue();
			l = param2Scroll.getValue();
			sampleVariable = new RandomVariable(new ContinuousUniformDistribution(a, a + l), "X");
			break;
		case 2:	 //Gamma
			shape = param1Scroll.getValue();
			scale = param2Scroll.getValue();
			sampleVariable = new RandomVariable(new GammaDistribution(shape, scale), "X");
			break;
		}
		median = sampleVariable.getDistribution().getMedian();
		medianLabel.setText("m = " + format(median));
		testMedian = median;
		testMedianLabel.setText("m0 = " + format(testMedian));
		quantileScroll.setValue(0.5);
		sampleGraph.setRandomVariable(sampleVariable);
		sampleGraph.setTestMedian(testMedian);
		reset();
	}

	/**
	* This method computes the lower and upper critical values, updates the label for
	* the critical values, and updates the sign graph.
	*/
	public void setCriticalValues(){
		lowerCritical = pivotDistribution.getQuantile(level / 2);
		upperCritical = pivotDistribution.getQuantile(1 - level / 2);
		criticalLabel.setText("c = " + format(lowerCritical) + ", d = " + format(upperCritical));
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}
