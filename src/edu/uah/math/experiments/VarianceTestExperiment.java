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
import edu.uah.math.devices.VarianceTestGraph;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.DataTable;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.ChiSquareDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.IntervalData;

/**
* This class models the hypothesis testing experiment for the variance in the standard normal
* model. The experiment is to select a random sample of size n from a selected distribution and then
* test a hypothesis about the standard deviation d at a specified significance level. The
* distribution can be selected from a list box; the options are the normal, gamma, and uniform
* distributions. In each case, the appropriate parameters and the sample size n can be varied
* with scroll bars.  The significance level can be selected with a list box, as can the type
* of test: two-sided, left-sided, or right-sided. The boundary point d0 between the null and
* alternative hypotheses can be varied with a scroll bar. The density of the distribution,
* as well as m and d, are shown in blue in the first graph; d0 is shown in green. The test
* can be constructed uder the assumption that the distribution mean is known or unknown.
* In the first case, the test statistic has the chi-square distribution with n degrees of
* freedom; in the second case the test statistics has the chi-square distribution with n - 1
* degrees of freedom. The density and the critical values of the test statistic V are shown
* in the second graph in blue.  On each update, the sample density is shown in red in the
* first graph and the sample values are recorded in the first table. The sample standard
* deviation S is shown in red in the first graph and the value of the test statistic V is
* shown in red in the second graph. The variable I indicates the event that the null hypothesis
* is rejected. On each update, S^2, V, and I are recorded in the second table. Note that the
* null hypothesis is reject (I = 1) if and only if the test statistic V falls outside of the
* critical values. Finally, the empirical density of I is shown in red in the last graph and
* recorded in the last table.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class VarianceTestExperiment extends Experiment implements Serializable{
	//Constants
	public final static int TWO_SIDED = 0, LEFT = 1, RIGHT = 2;
	//Variables
	private int distType = 0, sampleSize = 10, testType = TWO_SIDED, df;
	private boolean muKnown = true;
	private double testSD = 1, lowerCritical = 3.33, upperCritical = 16.92;
	private double level = 0.1, sampleVariance;
	private double[] sample;
	//Objects
	private JToolBar toolBar1 = new JToolBar("Parameter Toolbar");
	private JToolBar toolBar2 = new JToolBar("Distribution Toolbar");
	private JComboBox distChoice = new JComboBox();
	private JComboBox testChoice = new JComboBox();
	private JComboBox muChoice = new JComboBox();
	private Parameter param1Scroll = new Parameter(-5, 5, 0.5, 0, "Mean", "\u03bc");
	private Parameter param2Scroll = new Parameter(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
	private Parameter sampleScroll = new Parameter(5, 100, 5, sampleSize, "Sample size", "n");
	private Parameter stdDevScroll = new Parameter(0.5, 2, 0.1, 1, "Test standard deviation", "\u03c30");
	private Parameter levelScroll = new Parameter(0.01, 0.99, 0.01, level, "Significance level", "\u03b1");
	private JLabel criticalLabel = new JLabel("v = 3.33, 16.92");
	//Random Variables
	private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
	private IntervalData reject = new IntervalData(new Domain(0, 1, 1, Domain.DISCRETE), "I");
	private ChiSquareDistribution pivotDistribution = new ChiSquareDistribution(sampleSize - 1);
	//Graphs
	private VarianceTestGraph testGraph = new VarianceTestGraph(x, 1);
	private CriticalGraph criticalGraph = new CriticalGraph(pivotDistribution);
	private Histogram rejectGraph = new Histogram(reject);
	//Tables
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "S2", "V", "I"});
	private DataTable rejectTable = new DataTable(reject);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the record table, the label for
	* the critical values, the sliders for adjusting the parameters of the sampling
	* distribution, the graph and table for the reject random variable, the choice box
	* for the type of sampling distribution, the choice box for the type of text, the
	* choice box for the significance level, the choice box for mu known or unknown,
	* the toolbar, the test graph and chi-square graph, and the sample table.
	*/
	public void init(){
		//Initialize
		super.init();
		setName("Variance Test Experiment");
		//Critical label
		criticalLabel.setToolTipText("Critical values");
		//Parameter 1 Slider
		param1Scroll.applyDecimalPattern("0.0");
		param1Scroll.setWidth(150);
		param1Scroll.getSlider().addChangeListener(this);
		//Parameter 2 slider
		param2Scroll.applyDecimalPattern("0.0");
		param2Scroll.setWidth(150);
		param2Scroll.getSlider().addChangeListener(this);
		//Sample Scroll
		sampleScroll.setWidth(110);
		sampleScroll.getSlider().addChangeListener(this);
		//Standard Deviation Scroll
		stdDevScroll.applyDecimalPattern("0.0");
		stdDevScroll.setWidth(110);
		stdDevScroll.getSlider().addChangeListener(this);
		//Level Scroll
		levelScroll.applyDecimalPattern("0.00");
		levelScroll.setWidth(110);
		levelScroll.getSlider().addChangeListener(this);
		//Reject data
		rejectGraph.setStatisticsType(Histogram.NONE);
		rejectTable.setStatisticsType(DataTable.NONE);
		rejectTable.setDistributionType(DataTable.REL_FREQ);
		//Distribution choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Sampling Distribution");
		distChoice.addItem("Normal");
		distChoice.addItem("Gamma");
		distChoice.addItem("Uniform");
		//Test choice
		testChoice.addItemListener(this);
		testChoice.setToolTipText("Null hypothesis");
		testChoice.addItem("\u03c3 = \u03c30");
		testChoice.addItem("\u03c3 \u2265 \u03c30");
		testChoice.addItem("\u03c3 \u2264 \u03c30");
		//Model choice
		muChoice.addItemListener(this);
		muChoice.setToolTipText("Distribution mean");
		muChoice.addItem("\u03bc known");
		muChoice.addItem("\u03bc unknown");
		//Construct toolbars
		toolBar1.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar1.add(testChoice);
		toolBar1.add(muChoice);
		toolBar1.add(sampleScroll);
		toolBar1.add(stdDevScroll);
		toolBar1.add(levelScroll);
		toolBar1.add(criticalLabel);
		toolBar2.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar2.add(distChoice);
		toolBar2.add(param1Scroll);
		toolBar2.add(param2Scroll);
		addToolBar(toolBar1);
		addToolBar(toolBar2);
		//Construct graph panel
		testGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(testGraph, 0, 0, 1, 1);
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		rejectGraph.setMinimumSize(new Dimension(50, 100));
		addComponent(rejectGraph, 2, 0, 1, 1);
		//Tables
		recordTable.setDescription("S2: sample variance, V: test statistic, I: reject null hypothesis");
		addComponent(recordTable, 0, 1, 1, 1);
		addComponent(sampleTable, 1, 1, 1, 1);
		addComponent(rejectTable, 2, 1, 1, 1);
		//Final actions
		validate();
		setParameters();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment is to select a random sample of size n from a selected distribution and then\n"
			+ "test a hypothesis about the standard deviation d at a specified significance level. The\n"
			+ "distribution can be selected from a list box; the options are the normal, gamma, and uniform\n"
			+ "distributions. In each case, the appropriate parameters and the sample size n can be varied\n"
			+ "with scroll bars.  The significance level can be selected with a list box, as can the type\n"
			+ "of test: two-sided, left-sided, or right-sided. The boundary point d0 between the null and\n"
			+ "alternative hypotheses can be varied with a scroll bar. The density of the distribution,\n"
			+ "as well as m and d, are shown in blue in the first graph; d0 is shown in green. The test\n"
			+ "can be constructed uder the assumption that the distribution mean is known or unknown.\n"
			+ "In the first case, the test statistic has the chi-square distribution with n degrees of\n"
			+ "freedom; in the second case the test statistics has the chi-square distribution with n - 1\n"
			+ "degrees of freedom. The density and the critical values of the test statistic V are shown\n"
			+ "in the second graph in blue.  On each update, the sample density is shown in red in the\n"
			+ "first graph and the sample values are recorded in the first table. The sample standard\n"
			+ "deviation S is shown in red in the first graph and the value of the test statistic V is\n"
			+ "shown in red in the second graph. The variable I indicates the event that the null hypothesis\n"
			+ "is rejected. On each update, S^2, V, and I are recorded in the second table. Note that the\n"
			+ "null hypothesis is reject (I = 1) if and only if the test statistic V falls outside of the\n"
			+ "critical values. Finally, the empirical density of I is shown in red in the last graph and\n"
			+ "recorded in the last table.";
	}

	/**
	* This method resets the experiment, including the sampling, chi-square, and
	* reject random variables, , the sample array, the test graph, the chi-square
	* graph, the reject graph, the sample table, the record table, and the reject
	* table.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		x.reset();
		reject.reset();
		sample = new double[sampleSize];
		testGraph.reset();
		criticalGraph.setValueDrawn(false);
		rejectGraph.repaint();
		sampleTable.reset();
		rejectTable.reset();
	}

	/**
	* This method defines the experiment. The sample is simulated and the test
	* statistic computed. The test is performed and the reject variable updated.
	*/
	public void doExperiment(){
		double testStatistic;
		super.doExperiment();
		x.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = x.simulate();
		sampleVariance = x.getIntervalData().getVariance();
		testStatistic = df * sampleVariance / (testSD * testSD);
		criticalGraph.setValue(testStatistic);
		if (lowerCritical < testStatistic & testStatistic < upperCritical) reject.setValue(0);
		else reject.setValue(1);
	}

	/**
	* This method runs the experiment one time, playing a sound that depends on the
	* outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote((int)reject.getValue());
	}

	/**
	* This method handles the choice events associated with a change of distribution type,
	* test type, significance level, or state of knowledge of mu.
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
		else if (e.getSource() == testChoice){
			testType = testChoice.getSelectedIndex();
			setParameters();
		}
		else if (e.getSource() == muChoice){
			muKnown = (muChoice.getSelectedIndex() == 0);
			setParameters();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with a change in sample size
	* or a change in the parameters of the distribution.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == param1Scroll.getSlider()) setDistribution();
		else if (e.getSource() == param2Scroll.getSlider()) setDistribution();
		else if (e.getSource() == stdDevScroll.getSlider()){
			testSD = stdDevScroll.getValue();
			testGraph.setTestSD(testSD);
			reset();
		}
		else if (e.getSource() == levelScroll.getSlider()){
			level = levelScroll.getValue();
			setParameters();
		}
		else super.stateChanged(e);
	}

	/**
	* This method updates the display, including the test, chi square and reject
	* graphs, and the sample, record and reject tables.
	*/
	public void update(){
		super.update();
		//Graphs
		testGraph.repaint();
		criticalGraph.setValueDrawn(true);
		rejectGraph.repaint();
		//Tables
		sampleTable.setData(sample);
		recordTable.addRecord(new double[]{getTime(), x.getIntervalData().getVariance(),
			criticalGraph.getValue(), reject.getValue()});
		rejectTable.repaint();
	}

	public void graphUpdate(){
		super.update();
		testGraph.setShowModelDistribution(showModelDistribution);
		criticalGraph.setShowModelDistribution(showModelDistribution);
		rejectGraph.setShowModelDistribution(showModelDistribution);
		testGraph.repaint();
		criticalGraph.repaint();
		rejectGraph.repaint();
	}
	/**
	* This method computes the upper and lower critical values and sets up the graphs when
	* the distribution changes.
	*/
	public void setParameters(){
		switch(testType){
		case TWO_SIDED:
			upperCritical = pivotDistribution.getQuantile(1 - level / 2);
			lowerCritical = pivotDistribution.getQuantile(level / 2);
			criticalLabel.setText("v = " + format(lowerCritical) + ", " + format(upperCritical));
			break;
		case LEFT:
			upperCritical = pivotDistribution.getQuantile(1 - level);
			lowerCritical = +0.0;
			criticalLabel.setText("v = " + format(upperCritical));
			break;
		case RIGHT:
			lowerCritical = pivotDistribution.getQuantile(level);
			upperCritical = Double.POSITIVE_INFINITY;
			criticalLabel.setText("v = " + format(lowerCritical));
			break;
		}
		if (muKnown) df = sampleSize;
		else df = sampleSize - 1;
		pivotDistribution = new ChiSquareDistribution(df);
		criticalGraph.setDistribution(pivotDistribution);
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		reset();
	}

	/**
	* This method sets up the scroll bars and graphs when the type of distribution
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
			double endpoint = param1Scroll.getValue();
			double length = param2Scroll.getValue();
			x = new RandomVariable(new ContinuousUniformDistribution(endpoint, endpoint + length), "X");
			break;
		}
		testGraph.setRandomVariable(x);
		testSD = x.getDistribution().getSD();
		stdDevScroll.setRange(0.5 * testSD, 2 * testSD, 0.1 * testSD, testSD);
		testGraph.setTestSD(testSD);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

