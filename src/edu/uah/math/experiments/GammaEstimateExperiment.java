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
import edu.uah.math.devices.Parameter;
import edu.uah.math.devices.StatisticsTable;
import edu.uah.math.distributions.GammaDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the point estimation exerpiment for the gamma distribution. A random
* sample from the gamma distribution is computed and point estimates for the shape and
* scale parameters are computed. Empirical bias and mean square error are computed.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class GammaEstimateExperiment extends EstimateExperiment implements Serializable{
	//Variables
	private double shape = 1, scale = 1;
	private double sampleMean, sampleVar, u, v, w, uBias, uMSE, vBias, vMSE, wBias, wMSE;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "U", "V", "W"});
	private Parameter shapeScroll = new Parameter(1, 25, 0.5, shape, "Shape parameter", "k");
	private Parameter scaleScroll = new Parameter(1, 5, 0.5, scale, "Scale parameter", "b");
	private GammaDistribution dist = new GammaDistribution(shape, 1 / scale);
	private RandomVariable rv = new RandomVariable(dist, "X");
	private StatisticsTable uTable = new StatisticsTable("U", new String[]{"Bias", "MSE"});
	private StatisticsTable vTable = new StatisticsTable("V", new String[]{"Bias", "MSE"});
	private StatisticsTable wTable = new StatisticsTable("W", new String[]{"Bias", "MSE"});


	/**
	* This method initializes the experiment, including the toolbar, scrollbars and
	* labels.
	*/
	public void init(){
		super.init(rv);
		setName("Gamma Estimate Experiment");
		//Sliders
		shapeScroll.applyDecimalPattern("0.0");
		shapeScroll.getSlider().addChangeListener(this);
		scaleScroll.applyDecimalPattern("0.0");
		scaleScroll.getSlider().addChangeListener(this);
		addTool(shapeScroll);
		addTool(scaleScroll);
		//Record table
		recordTable.setDescription("U: estimate of k; V, W: estimates of b");
		addComponent(recordTable, 0, 1, 1, 3);
		//U Table
		uTable.setDescription("Bias and mean square error of U as an estimator of k");
		uTable.setShow(StatisticsTable.DATA);
		addComponent(uTable, 1, 1, 1, 1);
		//V Table
		vTable.setDescription("Bias and mean square error of V as an estimator of b");
		vTable.setShow(StatisticsTable.DATA);
		addComponent(vTable, 1, 2, 1, 1);
		//W Table
		wTable.setDescription("Bias and mean square error of W as an estimator of b");
		wTable.setShow(StatisticsTable.DATA);
		addComponent(wTable, 1, 3, 1, 1);
	}

	/**
	* This method gives basic information about the applet, including copyright
	* information, descriptive information, and instructions.
	* @return applet information
	*/
	public String getAppletInfo(){
		return super.getAppletInfo() + "\n\n"
			+ "The experiment is to generate a random sample X1, X2, ..., Xn of size n from the gamma\n"
			+ "distribution with shape parameter k and scale parameter b. The distribution density\n"
			+ "is shown in blue in the graph, and on each update, the sample density is shown in red.\n"
			+ "On each update, the following statistics are recorded: U = M^2 / T^2, V = T^2 / M, W = M / k\n"
			+ "where M = (X1 + X2 +     + Xn) / n and T^2 = [(X1 - M)^2 + (X2 - M)^2 +     + (Xn - M)^2] / n\n"
			+ "Statistic U is a point estimator of k and statistics V and W are point estimators of b.\n"
			+ "As the experiment runs, the empirical bias and mean square error of each estimator\n"
			+ "are recorded in the second table. The parameters k, b, and n can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment. A sample of a specified size from the gamma
	* distribution is computed and then the estimates of the shape and scale parameters
	* are computed. Finally the empirical bias and mean square error are computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		resetSample();
		for (int i = 0; i < getSampleSize(); i++) rv.sample();
		sampleMean = rv.getIntervalData().getMean();
		sampleVar = rv.getIntervalData().getVariance();
		u = sampleMean * sampleMean / sampleVar;
		v = sampleVar / sampleMean;
		w = sampleMean / shape;
		int runs = getTime();
		uBias = ((runs - 1) * uBias + (u - shape)) / runs;
		uMSE =  ((runs - 1) * uMSE + (u - shape) * (u - shape)) / runs;
		vBias = ((runs - 1) * vBias + (v - scale)) / runs;
		vMSE =  ((runs - 1) * vMSE + (v - scale) * (v - scale)) / runs;
		wBias = ((runs - 1) * wBias + (w - scale)) / runs;
		wMSE =  ((runs - 1) * wMSE + (w - scale) * (w - scale)) / runs;
	}

	/**
	* This method updates the experiment, including the record table, the random variable
	* graph and the statistics table.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), u, v, w});
		uTable.setDataValues(new double[]{uBias, uMSE});
		vTable.setDataValues(new double[]{vBias, vMSE});
		wTable.setDataValues(new double[]{wBias, wMSE});
	}

	/**
	* This method resets the experiment.
	*/
	public void reset(){
		super.reset();
		recordTable.reset();
		uTable.reset();
		vTable.reset();
		wTable.reset();
	}

	/**
	* This method handles the scroll events associated with changing the parameters of
	* the gamma distribution.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == shapeScroll.getSlider()){
			shape = shapeScroll.getValue();
			setDistribution();
		}
		else if (e.getSource() == scaleScroll.getSlider()){
			scale = scaleScroll.getValue();
			setDistribution();
		}
		else super.stateChanged(e);
	}

	/**
	* This method sets the distribution when the parameters have been changed.
	*/
	public void setDistribution(){
		dist.setParameters(shape, scale);
		reset();
	}
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

