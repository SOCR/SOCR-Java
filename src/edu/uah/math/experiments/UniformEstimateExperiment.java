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
import java.io.Serializable;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.Parameter;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the point estimation problem associated with the uniform distribuiton
* on the interval [0, a].
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class UniformEstimateExperiment extends EstimateExperiment implements Serializable{
	//Variables
	private double a = 1, u, v, w, uBias, uMSE, vBias, vMSE, wBias, wMSE;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "U", "V", "W"});
	private ContinuousUniformDistribution dist = new ContinuousUniformDistribution(0, a);
	private Parameter aScroll = new Parameter(0.5, 10, 0.5, a, "Right endpoint", "a");
	private RandomVariable rv = new RandomVariable(dist, "X");
	private StatisticsTable uTable = new StatisticsTable("U", new String[]{"Bias", "MSE"});
	private StatisticsTable vTable = new StatisticsTable("V", new String[]{"Bias", "MSE"});
	private StatisticsTable wTable = new StatisticsTable("W", new String[]{"Bias", "MSE"});

	/**
	* This method initializes the experiment by calling the initialization method
	* in the superclass,and by setting up the toolbar containing the prameter scrollbar
	* and label.
	*/
	public void init(){
		super.init(rv);
		setName("Uniform Estimate Experiment");
		//Parameter slider
		aScroll.applyDecimalPattern("0.0");
		aScroll.getSlider().addChangeListener(this);
		addTool(aScroll);
		//Record Table
		recordTable.setDescription("U, V, W: estimate of a");
		addComponent(recordTable, 0, 1, 1, 3);
		//U Table
		uTable.setDescription("Bias and mean square error of U as an estimator of a");
		addComponent(uTable, 1, 1, 1, 1);
		//V Table
		vTable.setDescription("Bias and mean square error of V as an estimator of a");
		addComponent(vTable, 1, 2, 1, 1);
		//U Table
		wTable.setDescription("Bias and mean square error of W as an estimator of a");
		addComponent(wTable, 1, 3, 1, 1);
	}

	/**
	* This method returns basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment is to generate a random sample X1, X2, ..., Xn of size n from the uniform\n"
			+ "distribution on (0, a). The distribution density is shown in blue in the graph, and on\n"
			+ "each update, the sample density is shown in red. On each update, the following statistics\n"
			+ "are recorded: U = 2M where M = (X1 + X2 +     + Xn) / n (the sample mean)\n"
			+ "V = (n + 1)X(n) / n where X(n) = max{X1, X2, ..., Xn} (the n'th order statistic)\n"
			+ "W = (n + 1)X(1) where X(1) = min{X1, X2, ..., Xn} (the first order statistic).";
	}

	/**
	* This method defines the experiment. A sample from the uniform distribution
	* is simulated and the estimates of a are computed. The empirical bias and mean
	* square error are also computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		resetSample();
		int n = getSampleSize(), runs = getTime();
		for (int i = 0; i < n; i++) rv.sample();
		u = 2 * rv.getIntervalData().getMean();
		v = (n + 1) * rv.getIntervalData().getMaxValue() / n;
		w = (n + 1) * rv.getIntervalData().getMinValue();
		uBias = ((runs - 1) * uBias + (u - a)) / runs;
		uMSE =  ((runs - 1) * uMSE + (u - a) * (u - a)) / runs;
		vBias = ((runs - 1) * vBias + (v - a)) / runs;
		vMSE =  ((runs - 1) * vMSE + (v - a) * (v - a)) / runs;
		wBias = ((runs - 1) * wBias + (w - a)) / runs;
		wMSE =  ((runs - 1) * wMSE + (w - a) * (w - a)) / runs;
	}

	/**
	* This method updates the experiment, including the record table, the
	* statistic table, and the random variable graph.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), u, v, w});
		uTable.setDataValues(new double[]{uBias, uMSE});
		vTable.setDataValues(new double[]{vBias, vMSE});
		wTable.setDataValues(new double[]{wBias, wMSE});
	}
	
	public void graphUpdate(){
		super.update();
		
		uTable.setShowModelDistribution(showModelDistribution);
		vTable.setShowModelDistribution(showModelDistribution);
		wTable.setShowModelDistribution(showModelDistribution);

		uTable.repaint();
		vTable.repaint();
		wTable.repaint();
	}
	/**
	* This method handles the scrollbar events that occur when the parameter is changed.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == aScroll.getSlider()){
			a = aScroll.getValue();
			dist.setParameters(0, a);
			reset();
		}
		else super.stateChanged(e);
	}

	/**
	* This method resets the experiment.
	*/
	public void reset(){
		int n = getSampleSize();
		super.reset();
		recordTable.reset();
		uTable.reset();
		vTable.reset();
		wTable.reset();
		uTable.setDistributionValues(new double[]{0, a * a / (3 * n)});
		vTable.setDistributionValues(new double[]{0, a * a / (n * (n + 2))});
		wTable.setDistributionValues(new double[]{0, n * a * a / (n + 2)});
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

