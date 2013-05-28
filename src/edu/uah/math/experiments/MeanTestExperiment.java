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
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.MeanTestGraph;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.DataTable;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.StudentDistribution;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.IntervalData;

/**
* This class models the hypothesis test experiment in the standard normal model.
* A random sample from a chosen sampling distribution is simulated and then an
* hypothesis test of the mean performed. The significance level, test mean, and
* type of test statistic (standard normal or student t) can by specified.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MeanTestExperiment extends Experiment implements Serializable{
	//Constants
	public final static int TWO_SIDED = 0, LEFT = 1, RIGHT = 2;
	//Variables
	private int distType = 0, sampleSize = 5, testType = TWO_SIDED;
	private boolean sigmaKnown = true;
	private double testMean = 0.0, mean = 0, stdDev = 1, stdScore;
	private double lowerCritical, upperCritical, level = 0.1;
	private double[] sample;
	private String stdScoreName = "Z";
	//Objects
	private RecordTable recordTable = new RecordTable();
	private JToolBar toolBar1 = new JToolBar("Parameter Toolbar");
	private JToolBar toolBar2 = new JToolBar("Distribution Toolbar");
	private JComboBox distChoice = new JComboBox();
	private JComboBox testChoice = new JComboBox();
	private JComboBox sigmaChoice = new JComboBox();
	private Parameter param1Scroll = new Parameter(-5, 5, 0.5, 0, "Mean", "\u03bc");
	private Parameter param2Scroll = new Parameter(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
	private Parameter sampleScroll = new Parameter(5, 100, 5, sampleSize, "Sample size", "n");
	private Parameter meanScroll = new Parameter(mean - 2 * stdDev, mean + 2 * stdDev, 0.4 * stdDev, mean, "Null hypothesis mean", "\u03bc0");
	private Parameter levelScroll = new Parameter(0.01, 0.99, 0.01, level, "Significance level", "\u03b1");
	private JLabel criticalLabel = new JLabel("z = \u00b11.96  ");
	//Random Variables
	private RandomVariable x = new RandomVariable(new NormalDistribution(0, 1), "X");
	private IntervalData reject = new IntervalData(new Domain(0, 1, 1, Domain.DISCRETE), "I");
	private Distribution pivotDistribution = new NormalDistribution(0, 1);
	//Graphs
	private MeanTestGraph testGraph = new MeanTestGraph(x, 0);
	private CriticalGraph criticalGraph = new CriticalGraph(pivotDistribution);
	private Histogram rejectGraph = new Histogram(reject);
	//Tables
	private DataTable rejectTable = new DataTable(reject);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the slider for the
	* parameters of the sampling distribution, the graph and table showing the
	* reject random variable, the choice boxes for the type of sampling distribution,
	* the type of test, the significnce leve, and the type of test statistic, and
	* the toolbar.
	*/
	public void init(){
		//Initialize
		super.init();
		setName("Mean Test Experiment");
		//Parameter 1 slider
		param1Scroll.applyDecimalPattern("0.0");
		param1Scroll.setWidth(150);
		param1Scroll.getSlider().addChangeListener(this);
		//Parameter 2 slider
		param2Scroll.applyDecimalPattern("0.0");
		param2Scroll.setWidth(150);
		param2Scroll.getSlider().addChangeListener(this);
		//Mean slider
		meanScroll.applyDecimalPattern("0.0");
		meanScroll.setWidth(120);
		meanScroll.getSlider().addChangeListener(this);
		//Sample size slider
		sampleScroll.setWidth(120);
		sampleScroll.getSlider().addChangeListener(this);
		//Significance level slider
		levelScroll.applyDecimalPattern("0.00");
		levelScroll.setWidth(120);
		levelScroll.getSlider().addChangeListener(this);
		//Critical label
		criticalLabel.setToolTipText("Critical values");
		//Distribution choice
		distChoice.addItemListener(this);
		distChoice.setToolTipText("Sampling distribution");
		distChoice.addItem("Normal");
		distChoice.addItem("Gamma");
		distChoice.addItem("Uniform");
		//Hypothesis test choice
		testChoice.addItemListener(this);
		testChoice.setToolTipText("Null hypothesis");
		testChoice.addItem("\u03bc = \u03bc0");
		testChoice.addItem("\u03bc \u2264 \u03bc0");
		testChoice.addItem("\u03bc \u2265 \u03bc0");
		//Model choice
		sigmaChoice.addItemListener(this);
		sigmaChoice.setToolTipText("Model");
		sigmaChoice.addItem("Normal ");
		sigmaChoice.addItem("Student");
		//Toolbar 1
		toolBar1.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar1.add(testChoice);
		toolBar1.add(sigmaChoice);
		toolBar1.add(meanScroll);
		toolBar1.add(sampleScroll);
		toolBar1.add(levelScroll);
		toolBar1.add(criticalLabel);
		//Toolbar 2
		toolBar2.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar2.add(distChoice);
		toolBar2.add(param1Scroll);
		toolBar2.add(param2Scroll);
		addToolBar(toolBar1);
		addToolBar(toolBar2);
		//Test Graph
		testGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(testGraph, 0, 0, 1, 1);
		//Critical Graph
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		//Reject Graph
		rejectGraph.setMinimumSize(new Dimension(50, 100));
		rejectGraph.setStatisticsType(Histogram.NONE);
		addComponent(rejectGraph, 2, 0, 1, 1);
		//Record Table
		addComponent(recordTable, 0, 1, 1, 1);
		//Sample Table
		addComponent(sampleTable, 1, 1, 1, 1);
		//Reject Table
		rejectTable.setStatisticsType(DataTable.NONE);
		rejectTable.setDistributionType(DataTable.REL_FREQ);
		addComponent(rejectTable, 2, 1, 1, 1);
		//Set parameters
		setParameters();
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet infomration
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment is to select a random sample of size n from a selected distribution and then\n"
			+ "test a hypothesis about the mean   at a specified significance level. The distribution can\n"
			+ "be selected from a list box; the options are the normal, gamma, and uniform distributions.\n"
			+ "In each case, the appropriate parameters and the sample size n can be varied with scroll bars.\n"
			+ "The significance level can be selected with a list box, as can the type of test: two-sided,\n"
			+ "left-sided, or right-sided. The boundary point m0 between the null and alternative hypotheses\n"
			+ "can be varied with a scroll bar. The density of the distribution and   are shown in blue in\n"
			+ "the first graph; m0 is shown in green. The test can be constructed uder the assumption that\n"
			+ "the distribution standard deviation is known or unknown. In the first case, the test statistic\n"
			+ "has the standard normal distribution; in the second case the test statistics has the student t\n"
			+ "distribution with n - 1 degrees of freedom. The density and the critical values of the test\n"
			+ "statistic are shown in the second graph in blue. On each update, the sample density is shown\n"
			+ "in red in the first graph and the sample values are recorded in the first table. The sample\n"
			+ "mean M is shown in red in the first graph and the value of the test statistics (Z or T) is shown\n"
			+ "in red in the second graph. The variable I indicates the event that the null hypothesis is\n"
			+ "rejected. On each update, M, Z or T, and I are recorded in the second table. Note that the \n"
			+ "null hypothesis is reject (I = 1) if and only if the test statistic (Z or T) falls outside of\n"
			+ "the critical values. Finally, the empirical density of I is shown in red in the last graph and\n"
			+ "recorded in the last table.";
	}

	/**
	* This method resets the experiment, including the sampling random variable,
	* the standard score variable, the test graph, the standard score graph, the
	* reject graph and table, and the record table.
	*/
	public void reset(){
		super.reset();
		x.reset();
		reject.reset();
		sample = new double[sampleSize];
		testGraph.repaint();
		criticalGraph.setValueDrawn(false);
		rejectGraph.repaint();
		sampleTable.reset();
		if (sigmaKnown) recordTable.setVariableNames(new String[]{"Run", "M", "Z", "I"});
		else recordTable.setVariableNames(new String[]{"Run", "M", "T", "I"});
		rejectTable.reset();
	}

	/**
	* This method defines the experiment. The sample of the specified size is simulated,
	* and the test statistic computed. The test is conducted by comparing the test
	* statistic to the critical values.
	*/
	public void doExperiment(){
		double stdError;
		super.doExperiment();
		x.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = x.simulate();
		if (sigmaKnown) stdError = x.getDistribution().getSD() / Math.sqrt(sampleSize);
		else stdError = x.getIntervalData().getSD() / Math.sqrt(sampleSize);
		stdScore = (x.getIntervalData().getMean() - testMean) / stdError;
		if (lowerCritical < stdScore & stdScore < upperCritical) reject.setValue(0);
		else reject.setValue(1);
	}

  	/**
  	* This method runs the experiment one time, and plays a sound depending
  	* on the outcome.
  	*/
  	public void step(){
		doExperiment();
		update();
		//playnote((int)reject.getValue());
	}

	/**
	* This event handles the choice events associated with a change in the
	* sampling distribution, a change in the type of test, a change in the
	* significance level, or a change in the type of test statistic.
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == distChoice){
			distType = distChoice.getSelectedIndex();
			switch(distType){
			case 0: //Normal
				param1Scroll.setProperties(-5, 5, 0.5, 0, "Mean", "\u03bc");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Standard deviation", "\u03c3");
				break;
			case 1:	 //Gamma
				param1Scroll.setProperties(1, 5, 0.5, 1, "Shape", "k");
				param2Scroll.setProperties(0.5, 5, 0.5, 1, "Scale", "b");
				break;
			case 2:	 //Uniform
				param1Scroll.setProperties(-5, 5, 1, 0, "Left endpoint", "a");
				param2Scroll.setProperties(1, 10, 1, 1, "Length", "l");
				break;
			}
			setDistribution();
		}
		else if (e.getSource() == testChoice){
			testType = testChoice.getSelectedIndex();
			setParameters();
		}
		else if (e.getSource() == sigmaChoice){
			sigmaKnown = (sigmaChoice.getSelectedIndex() == 0);
			setParameters();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scrollbar events associated with changes in the
	* sample size, the distribution parameters, or the test mean.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == sampleScroll.getSlider()){
			sampleSize = (int)sampleScroll.getValue();
			setParameters();
		}
		else if (e.getSource() == param1Scroll.getSlider()){
			setDistribution();
		}
		else if (e.getSource() == param2Scroll.getSlider()){
			setDistribution();
		}
		else if (e.getSource() == meanScroll.getSlider()){
			testMean = meanScroll.getValue();
			testGraph.setTestMean(testMean);
			reset();
		}
		else if (e.getSource() == levelScroll.getSlider()){
			level = levelScroll.getValue();
			setParameters();
		}
		else super.stateChanged(e);
	}

	/**
	* This method updates the display, including the test graph, the standard
	* score graph, the reject graph and table, the sample table, and the record
	* table.
	*/
	public void update(){
		super.update();
		//Graphs
		testGraph.repaint();
		criticalGraph.setValue(stdScore);
		criticalGraph.setValueDrawn(true);
		rejectGraph.repaint();
		//Tables
		sampleTable.setData(sample);
		recordTable.addRecord(new double[]{getTime(), x.getIntervalData().getMean(),
			stdScore, reject.getValue()});
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
	* This method sets the distribution of the standard score, and then updates
	* the record table, and standard score graph appropriately.
	*/
	public void setParameters(){
		if (sigmaKnown){
			pivotDistribution = new NormalDistribution(0, 1);
			stdScoreName = "z";
			recordTable.setDescription("M: sample mean; Z: test statistics; I: reject indicator");
		}
		else{
			pivotDistribution = new StudentDistribution(sampleSize - 1);
			stdScoreName = "t";
			recordTable.setDescription("M: sample mean; T: test statistics; I: reject indicator");
		}
		switch(testType){
			case TWO_SIDED:
			upperCritical = pivotDistribution.getQuantile(1 - level / 2);
			lowerCritical = -upperCritical;
			criticalLabel.setText(stdScoreName + " = \u00b1" + format(upperCritical));
			break;
		case LEFT:
			upperCritical = pivotDistribution.getQuantile(1 - level);
			lowerCritical = Double.NEGATIVE_INFINITY;
			criticalLabel.setText(stdScoreName + " = " + format(upperCritical));
			break;
		case RIGHT:
			lowerCritical = -pivotDistribution.getQuantile(1 - level);
			upperCritical = Double.POSITIVE_INFINITY;
			criticalLabel.setText(stdScoreName + " = -" + format(lowerCritical));
			break;
		}
		criticalGraph.setDistribution(pivotDistribution);
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		reset();
	}

	/**
	* This method sets the distribution of the sampling variable when the
	* parameters of the distribution have changed. Appropriate changes are made
	* to the test graph, the slider for the test mean, and the test graph.
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
		mean = x.getDistribution().getMean();
		stdDev = x.getDistribution().getSD();
		testMean = mean;
		meanScroll.setRange(mean - 2 * stdDev, mean + 2 * stdDev, 0.4 * stdDev, mean);
		testGraph.setTestMean(testMean);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

