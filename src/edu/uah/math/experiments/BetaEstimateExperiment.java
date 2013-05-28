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
import edu.uah.math.distributions.BetaDistribution;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.devices.RecordTable;
import edu.uah.math.devices.Parameter;

/**
* This class models the point estimation problem of the beta distribution. A random
* sample from the beta distribution, with left paramater a and right parameter b = 1
* is simulated. Standard point estimates of a are computed, along with the empirical
* bias and mean square error.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BetaEstimateExperiment extends EstimateExperiment implements Serializable{
	//Variables
	private double a = 1, u, v, uBias, uMSE, vBias, vMSE;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "U", "V"});
	private Parameter aScroll = new Parameter(1, 25, 0.5, a, "Left parameter", "a");
	private BetaDistribution dist = new BetaDistribution(a, 1);
	private RandomVariable rv = new RandomVariable(dist, "X");
	private StatisticsTable uTable = new StatisticsTable("U", new String[]{"Bias", "MSE"});
	private StatisticsTable vTable = new StatisticsTable("V", new String[]{"Bias", "MSE"});

	/**This method initializes the experiment, including the addition of the parameter
	label and scrollbar to the toolbar*/
	public void init(){
		super.init(rv);
		setName("Beta Estimate Experiment");
		//Slider
		aScroll.applyDecimalPattern("#.0");
		aScroll.getSlider().addChangeListener(this);
		addTool(aScroll);
		//Record table
		recordTable.setDescription("U, V: estimates of a");
		addComponent(recordTable, 0, 1, 1, 2);
		//U Table
		uTable.setDescription("Bias and mean square error of U as an estimator of a");
		uTable.setShow(StatisticsTable.DATA);
		addComponent(uTable, 1, 1, 1, 1);
		//V Table
		vTable.setDescription("Bias and mean square error of V as an estimator of a");
		vTable.setShow(StatisticsTable.DATA);
		addComponent(vTable, 1, 2, 1, 1);
		//Final actions
		validate();
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment is to generate a random sample X1, X2, ..., Xn of size n from the\n"
			+ "beta distribution with parameters a and 1. The distribution density is shown in blue\n"
			+ "in the graph, and on each update, the sample density is shown in red. On each update,\n"
			+ "the following statistics are recorded: U = M / (1 - M) where M = (X1 + X2 +     + Xn) / n\n"
			+ "V = -n / [ln(X1) + ln(X2) +     + ln(Xn)]. Statistics U and V are point estimators of\n"
			+ "a. As the experiment runs, the empirical bias and mean square error of each estimator\n"
			+ "are recorded in the second table. The parameters a and n can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment.  A sample of size n from the beta distribuion
	* is simulated.  The point estimate, empirical bias and MSE are computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		resetSample();
		double sum = 0, x;
		int runs = getTime(), n = getSampleSize();
		for (int i = 0; i < n; i++){
			x = rv.simulate();
			sum = sum + Math.log(x);
		}
		double sampleMean = rv.getIntervalData().getMean();
		u = sampleMean / (1 - sampleMean);
		v = -n / sum;
		uBias = ((runs - 1) * uBias + (u - a)) / runs;
		uMSE =  ((runs - 1) * uMSE + (u - a) * (u - a)) / runs;
		vBias = ((runs - 1) * vBias + (v - a)) / runs;
		vMSE =  ((runs - 1) * vMSE + (v - a) * (v - a)) / runs;
	}

	/**
	* This method resets the experiment.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		uTable.reset();
		vTable.reset();
	}

	/**
	* This method updates the experiment, including the record table, statistics table, and
	* random variable graph.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), u, v});
		uTable.setDataValues(new double[]{uBias, uMSE});
		vTable.setDataValues(new double[]{vBias, vMSE});
	}

	/**
	* This method handles the scroll events for changing the beta parameter. The
	* distribution of the random variable is changed appropriately, and the
	* {@link  #reset() reset} method is called.
	* @param e the change event.
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == aScroll.getSlider()){
			a = aScroll.getValue();
			dist.setParameters(a, 1);
			reset();
		}
		else super.stateChanged(e);
	}
	
	  public JTable getResultTable(){
	    	return  recordTable.getTable();
	    }
}

