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

import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.MeanTestGraph;
import edu.uah.math.devices.CriticalGraph;
import edu.uah.math.devices.Histogram;
import edu.uah.math.devices.DataTable;
import edu.uah.math.devices.SampleTable;
import edu.uah.math.distributions.BernoulliDistribution;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the hypothesis testing experiment for the probability of success in the
* Bernoulli trials model.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ProportionTestExperiment extends Experiment implements Serializable{
	//Constants
	public final static int TWO_SIDED = 0, LEFT = 1, RIGHT = 2;
	//Variables
	private int testType = TWO_SIDED, sampleSize = 5;
	private double p = 0.5, p0 = 0.5, lowerCritical = -1.645, upperCritical = 1.645, level = 0.1;
	private double[] sample;
	//GUI Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "Z", "I"});
	private JToolBar toolBar = new JToolBar("Parameter Toolbar");
	private JComboBox testChoice = new JComboBox();
	private Parameter sampleScroll = new Parameter(5, 100, 5, sampleSize, "Sample size", "n");
	private Parameter pScroll = new Parameter(0.05, 0.95, 0.05, p, "Probability of success", "p");
	private Parameter p0Scroll = new Parameter(0.05, 0.95, 0.05, p0, "Null hypothesis value", "p0");
	private Parameter levelScroll = new Parameter(0.01, 0.99, 0.01, level, "Signficance level", "\u03b1");
	private JLabel criticalLabel = new JLabel("z = \u00b11.645");
	//Mathematical Objects
	private RandomVariable x = new RandomVariable(new BernoulliDistribution(p), "X");
	private IntervalData reject = new IntervalData(new Domain(0, 1, 1, Domain.DISCRETE), "I");
	private NormalDistribution pivotDistribution = new NormalDistribution(0, 1);
	//Graphs
	private MeanTestGraph testGraph = new MeanTestGraph(x, 0.5);
	private CriticalGraph criticalGraph = new CriticalGraph(pivotDistribution);
	private Histogram rejectGraph = new Histogram(reject);
	//Tables
	private DataTable rejectTable = new DataTable(reject);
	private SampleTable sampleTable = new SampleTable("X");

	/**
	* This method initializes the experiment, including the record table, the
	* sliders for the probability parameter and the test value, the graph and
	* table for the reject random variable, the choice boxes for the significance
	* level and type of test, the toolbar,
	*/
	public void init(){
		super.init();
		setName("Proportion Test Experiment");
		//p slider
		pScroll.applyDecimalPattern("0.00");
		pScroll.setWidth(110);
		pScroll.getSlider().addChangeListener(this);
		//p0 slider
		p0Scroll.applyDecimalPattern("0.00");
		p0Scroll.setWidth(125);
		p0Scroll.getSlider().addChangeListener(this);
		//Sample size slider
		sampleScroll.setWidth(110);
		sampleScroll.getSlider().addChangeListener(this);
		//Significance level slider
		levelScroll.applyDecimalPattern("0.00");
		levelScroll.setWidth(110);
		levelScroll.getSlider().addChangeListener(this);
		//Label
		criticalLabel.setToolTipText("Critical value");
		//Data
		rejectGraph.setStatisticsType(Histogram.NONE);
		rejectTable.setStatisticsType(DataTable.NONE);
		rejectTable.setDistributionType(DataTable.REL_FREQ);
		criticalGraph.setCriticalValues(lowerCritical, upperCritical);
		//Test choice
		testChoice.addItemListener(this);
		testChoice.setToolTipText("Test Type");
		testChoice.addItem("H0: p = p0");
		testChoice.addItem("H0: p \u2264 p0");
		testChoice.addItem("H0: p \u2265 p0");
		//Toolbars
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(testChoice);
		toolBar.add(pScroll);
		toolBar.add(p0Scroll);
		toolBar.add(sampleScroll);
		toolBar.add(levelScroll);
		toolBar.add(criticalLabel);
		addToolBar(toolBar);
		//Mean Test Graph
		testGraph.setMinimumSize(new Dimension(50, 100));
		addComponent(testGraph, 0, 0, 1, 1);
		//Critical Graph
		criticalGraph.setMinimumSize(new Dimension(100, 100));
		addComponent(criticalGraph, 1, 0, 1, 1);
		//Reject Graph
		rejectGraph.setMinimumSize(new Dimension(50, 100));
		addComponent(rejectGraph, 2, 0, 1, 1);
		//Record Table
		recordTable.setDescription("Z: test statistic, I: reject null hypothesis");
		addComponent(recordTable, 0, 1, 1, 1);
		//Sample Table
		addComponent(sampleTable, 1, 1, 1, 1);
		//Reject Table
		addComponent(rejectTable, 2, 1, 1, 1);
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
			+ "parameter p, and then test a hypothesis about p at a specified significance level. The sample\n"
			+ "size n can be varied with a scroll bar. The significance level can be selected with a list box,\n"
			+ "as can the type of test: two-sided, left-sided, or right-sided. The boundary point p0 between\n"
			+ "the null and alternative hypotheses can be varied with a scroll bar. The Bernoulli density and\n"
			+ "p are shown in blue in the first graph; p0 is shown in green. The test statistic has\n"
			+ "approximately a standard normal distribution. The standard normal density and the critical\n"
			+ "values are shown in the second graph in blue. On each update, the sample density is shown in\n"
			+ "red in the first graph and the sample values are recorded in the first table. The sample\n"
			+ "proportion M is shown in red in the first graph and the value of the test statistics Z is\n"
			+ "shown in red in the second graph. The variable I indicates the event that the null hypothesis\n"
			+ "is rejected. On each update, M, Z, and I are recorded in the second table. Note that the null\n"
			+ "hypothesis is reject (I = 1) if and only if the test statistic Z falls outside of the critical\n"
			+ "values. Finally, the empirical density of I is shown in red in the last graph and recorded in\n"
			+ "the last table.";
	}

	/**
	* This method resets the experiment, including the sampling, standard score, and
	* reject ranodm variables, the test, standard score, and reject graphs,
	* and the sample, record, and reject tables.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		x.reset();
		reject.reset();
		sample = new double[sampleSize];
		testGraph.repaint();
		criticalGraph.setValueDrawn(false);
		rejectGraph.repaint();
		sampleTable.reset();
		rejectTable.repaint();
	}

	/**
	* This method defines the experiment. The sample is simulated and the  hypothesis
	* test conducted.
	*/
	public void doExperiment(){
		super.doExperiment();
		x.reset();
		for (int i = 0; i < sampleSize; i++) sample[i] = (x.simulate());
		criticalGraph.setValue((x.getIntervalData().getMean() - p0) / Math.sqrt(p0 * (1 - p0) / sampleSize));
		if (criticalGraph.isSuccess()) reject.setValue(0);
		else reject.setValue(1);
	}

	/**
	* This method updates the experiment, including the test, standard score, and reject
	* graphs, the sample, record, and reject tables.
	*/
	public void update(){
		super.update();
		//Graphs
		testGraph.repaint();
		criticalGraph.setValueDrawn(true);
		rejectGraph.repaint();
		//Tables
		sampleTable.setData(sample);
		recordTable.addRecord(new double[]{getTime(), criticalGraph.getValue(), reject.getValue()});
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
	* This method runs the experiment one time, playing a sound that depends on
	* the outcome.
	*/
	public void step(){
		doExperiment();
		update();
		//playnote((int)reject.getValue());
	}


	/**
	* This method handles the choice events associated with changing the type of test and the
	* @param e the item event
	*/
	public void itemStateChanged(ItemEvent e){
		if (e.getSource() == testChoice){
			testType = testChoice.getSelectedIndex();
			setParameters();
		}
		else super.itemStateChanged(e);
	}

	/**
	* This method handles the scroll events associated with changes in the sample size, the
	* true value of the parameter and the hypothesized value of the parameter.
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
			testGraph.setRandomVariable(x);
			reset();
		}
		else if (e.getSource() == p0Scroll.getSlider()){
			p0 = p0Scroll.getValue();
			testGraph.setTestMean(p0);
			reset();
		}
		else if (e.getSource() == levelScroll.getSlider()){
			level = levelScroll.getValue();
			setParameters();
		}
		else super.stateChanged(e);
	}

	/**
	* This method computes the lower and upper critical values.
	*/
	public void setParameters(){
		switch(testType){
		case TWO_SIDED:
			upperCritical = pivotDistribution.getQuantile(1 - level / 2);
			lowerCritical = -upperCritical;
			criticalLabel.setText("z = \u00b1" + format(upperCritical));
			break;
		case LEFT:
			upperCritical = pivotDistribution.getQuantile(1 - level);
			lowerCritical = Double.NEGATIVE_INFINITY;
			criticalLabel.setText("z = " + format(upperCritical));
			break;
		case RIGHT:
			upperCritical = Double.POSITIVE_INFINITY;
			lowerCritical = -pivotDistribution.getQuantile(1 - level);
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
