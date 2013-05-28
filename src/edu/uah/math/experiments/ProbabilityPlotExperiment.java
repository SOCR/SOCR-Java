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
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.RandomVariableGraph;
import edu.uah.math.devices.RandomVariableTable;
import edu.uah.math.devices.ScatterPlot;
import edu.uah.math.devices.QuantileTable;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.ExponentialDistribution;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.Functions;

/**
* This class models the probability plot experiment. A sample from a specified
* distribution is simulated. The order statistics from this sample are plotted versus
* the quantiles of the same size from a test distribution. If the sampling distribution
* is of the same parametric type as the test distribution, the plot should be
* approximately linear.
* @version June, 2002
* @author Kyle Siegrist
* @author Dawn Duehring
*/
public class ProbabilityPlotExperiment extends Experiment implements Serializable{
	//Variables
	private int sampleSize = 5, distType = 0, testType = 0;
	private double[] sample = new double[sampleSize];
	private double[] orderStatistics = new double[sampleSize];
	//Objects
	private RecordTable recordTable = new RecordTable();
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox samplingChoice = new JComboBox();
	private JComboBox testChoice = new JComboBox();
	private Parameter sampleScroll = new Parameter(5, 100, 5, sampleSize, "Sample size", "n");
	private Parameter param1Scroll = new Parameter(-5, 5, 0.1, 0, "Mean", "\u03bc");
	private Parameter param2Scroll = new Parameter(0.1, 5, 0.1, 1, "Standard deviation", "\u03c3");
	private RandomVariable basicVariable = new RandomVariable(new NormalDistribution(0, 1), "X");
	private RandomVariable testVariable = new RandomVariable(new NormalDistribution(0, 1), "Y");
	private RandomVariableGraph basicGraph = new RandomVariableGraph(basicVariable);
	private RandomVariableGraph testGraph = new RandomVariableGraph(testVariable);
	private ScatterPlot probabilityPlot = new ScatterPlot(basicVariable.getDistribution().getDomain(), testVariable.getDistribution().getDomain());
	private QuantileTable quantileTable = new QuantileTable(testVariable.getDistribution(), sampleSize);

	/**
	* This method initializes the experiment, including the record table, the sliders
	* for the parameters of the sampling distribution, the graphs of the sampling
	* and test distributions, the choice boxes for the sampling and test distributions,
	* the toolbar, the probability plot graph, amd the quantile table.
	*/
	public void init(){
		super.init();
		setName("Probability Plot Experiment");
		//Parameter Scrollbar
		param1Scroll.applyDecimalPattern("0.0");
		param1Scroll.setWidth(140);
		param1Scroll.getSlider().addChangeListener(this);
		//Parameter Scrollbar
		param2Scroll.applyDecimalPattern("0.0");
		param2Scroll.setWidth(140);
		param2Scroll.getSlider().addChangeListener(this);
		//Sample Scroll
		sampleScroll.getSlider().addChangeListener(this);
		sampleScroll.setWidth(120);
		//Graphs and tables
		basicGraph.setMomentType(0);
		testGraph.setMomentType(0);
		basicGraph.setMargins(35, 20, 20, 20);
		testGraph.setMargins(35, 20, 20, 20);
		//Sampling choice
		samplingChoice.addItemListener(this);
		samplingChoice.setToolTipText("Sampling Distribution");
		samplingChoice.addItem("Normal");
		samplingChoice.addItem("Uniform");
		samplingChoice.addItem("Gamma");
		//Test choice
		testChoice.addItemListener(this);
		testChoice.setToolTipText("Test Distribution");
		testChoice.addItem("Normal (0, 1)");
		testChoice.addItem("Uniform (0, 1)");
		testChoice.addItem("Exponential (1)");
		//Construct toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(samplingChoice);
		toolBar.add(testChoice);
		toolBar.add(sampleScroll);
		toolBar.add(param1Scroll);
		toolBar.add(param2Scroll);
		addToolBar(toolBar);
		//Probability Plot
		addComponent(probabilityPlot, 0, 0, 1, 1);
		//Basic Graph
		addComponent(basicGraph, 1, 0, 1, 1);
		//Test Graph
		addComponent(testGraph, 2, 0, 1, 1);
		//Record Table
		recordTable.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		recordTable.setDescription("X(i): i'th order statistic");
		addComponent(recordTable, 0, 1, 2, 1);
		//Quantile Table
		addComponent(quantileTable, 2, 1, 1, 1);
		setQuantiles();
		//Final Actions
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
			+ "The experiment is to select a random sample of size n from a specified distribution\n"
			+ "and graphically test the data against a hypothesized parametric family of distributions.\n"
			+ "The sampling distribution can be chosen from the list box; the options are the normal,\n"
			+ "exponential, and uniform; in each case the appropriate parameters can be varied with\n"
			+ "scroll bars. Similarly, the test distribution can be selected from a list box; the options\n"
			+ "are the standard normal distribution, the uniform distribution on (0, 1), or the\n"
			+ "exponential distribution with parameter 1. The density function of the sampling distribution\n"
			+ "is shown in the first graph in blue, and on each update, the sample values are shown in\n"
			+ "red. The sample values are arranged in increasing order (these are the order statistics)\n"
			+ "and recorded in the first table on each update. The density function of the test distribution\n"
			+ "is shown in blue in the second graph. The quantiles of order i / (n + 1) for i = 1, 2, ..., n\n"
			+ "are shown in red in the second graph and are recorded in the second table. On each update,\n"
			+ "the order statistic-quantile pairs are plotted in the third graph (this is the probability\n"
			+ "plot).";
	}

	/**
	* This method defines the experiment. The sample is simulated and the order
	* statistics computed. The order statistics are passed to the probability plot.
	*/
	public void doExperiment(){
		super.doExperiment();
		basicVariable.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = basicVariable.simulate();
		orderStatistics = Functions.sort(sample);
	}

	/**
	* This method updates the display, including the record table, the probability
	* plot, the graph of the sampling distribution.
	*/
	public void update(){
		super.update();
		probabilityPlot.resetData();
		for (int i = 0; i < sampleSize; i++) probabilityPlot.addPoint(orderStatistics[i], quantileTable.getQuantile(i));
		probabilityPlot.repaint();
		double[] record = new double[sampleSize + 1];
		record[0] = getTime();
		for (int i = 0; i < sampleSize; i++) record[i + 1] = orderStatistics[i];
		recordTable.addRecord(record);
		basicGraph.repaint();
	}

	public void graphUpdate(){
		super.update();
		probabilityPlot.setShowModelDistribution(showModelDistribution);
		basicGraph.setShowModelDistribution(showModelDistribution);
		testGraph.setShowModelDistribution(showModelDistribution);
		probabilityPlot.repaint();
		basicGraph.repaint();
		testGraph.repaint();
	}
	/**
	* This method handles the scroll events associated with changing the
	* parameters of the sampling distribution or the sample size.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			sample = new double[sampleSize];
			orderStatistics = new double[sampleSize];
			setQuantiles();
			reset();
		}
		else if (e.getSource() == param1Scroll.getSlider()){
			setDistribution();
		}
		else if (e.getSource() == param2Scroll.getSlider()){
			setDistribution();
		}
	}

	/**
	* This method handles the choice events associated with changing the
	* sampling distribution or the test distribution.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == testChoice){
			testType = testChoice.getSelectedIndex();
			switch(testType){
			case 0:	 //Normal
				testVariable.setDistribution(new NormalDistribution(0, 1));
				break;
			case 1:	 //Uniform
				testVariable.setDistribution(new ContinuousUniformDistribution(0, 1));
				break;
			case 2:	 //Exponential
				testVariable.setDistribution(new ExponentialDistribution(1));
				break;
			}
			testGraph.reset();
			setDomains();
			setQuantiles();
		}
		else if (e.getSource() == samplingChoice){
			distType = samplingChoice.getSelectedIndex();
			switch(distType){
			case 0:	 //Normal
				param1Scroll.setRange(-5, 5, 0.1, 0);
				param1Scroll.setStrings("Mean", "\u03bc");
				param2Scroll.setRange(0.1, 5, 0.1, 1);
				param2Scroll.setStrings("Standard deviation", "\u03c3");
				break;
			case 1:	 //Uniform
				param1Scroll.setRange(0, 10, 1, 0);
				param1Scroll.setStrings("Left endpoint", "a");
				param2Scroll.setRange(1, 10, 1, 1);
				param2Scroll.setStrings("Length", "l");
				break;
			case 2:	 //Gamma
				param1Scroll.setRange(1, 5, 1, 1);
				param1Scroll.setStrings("Shape", "k");
				param2Scroll.setRange(0.1, 5, 0.1, 1);
				param2Scroll.setStrings("Scale", "b");
				break;
			}
			setDistribution();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method sets the quantiles of the test distribution. These are passed to
	* the test graph, the quantile table, and the probability plot.
	*/
	public void setQuantiles(){
		double p;
		testVariable.reset();
		testGraph.reset();
		quantileTable.setDistribution(testVariable.getDistribution());
		quantileTable.setCount(sampleSize);
		for (int i = 0; i < sampleSize; i++) testVariable.setValue(quantileTable.getQuantile(i));
		testGraph.repaint();
		probabilityPlot.resetData();
		if (getTime() > 0) for (int i = 0; i < sampleSize; i++) probabilityPlot.addPoint(orderStatistics[i], quantileTable.getQuantile(i));
		probabilityPlot.repaint();

	}

	/**
	* This method resets the experiment, including the record table, probability plot,
	* and the graph of the sampling distribution.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		basicGraph.reset();
		String[] variables = new String[sampleSize + 1];
		variables[0] = "Run";
		for (int i = 1; i <= sampleSize; i++) variables[i] = "X(" + i + ")";
		recordTable.setVariableNames(variables);
		probabilityPlot.reset();
	}

	/**
	* This method sets the sampling distribution when the parameters have changed.
	*/
	public void setDistribution(){
		switch(distType){
		case 0:	 //Normal
			double mu = param1Scroll.getValue();
			double sigma = param2Scroll.getValue();
			basicVariable.setDistribution(new NormalDistribution(mu, sigma));
			break;
		case 1:	 //Uniform
			double a = param1Scroll.getValue();
			double l = param2Scroll.getValue();
			basicVariable.setDistribution(new ContinuousUniformDistribution(a, a + l));
			break;
		case 2:	 //Gamma
			double shape = param1Scroll.getValue();
			double scale = param2Scroll.getValue();
			basicVariable.setDistribution(new GammaDistribution(shape, scale));
			break;
		}
		setDomains();
		reset();
	}

	/**
	* This method sets the domains for the probability plot.
	*/
	private void setDomains(){
		double xMin = basicVariable.getDistribution().getDomain().getLowerBound(),
			xMax = basicVariable.getDistribution().getDomain().getUpperBound(),
			xWidth = 0.1 * (xMax - xMin),
			yMin = testVariable.getDistribution().getDomain().getLowerBound(),
			yMax = testVariable.getDistribution().getDomain().getUpperBound(),
			yWidth = 0.1 * (yMax - yMin);
		probabilityPlot.setDomains(new Domain(xMin, xMax, xWidth, Domain.CONTINUOUS), new Domain(yMin, yMax, yWidth, Domain.CONTINUOUS));
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

