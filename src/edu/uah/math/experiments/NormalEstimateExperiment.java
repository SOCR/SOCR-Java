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
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models the point estimation problem associated with the normal distribution.
* A sample of a specified size from the normal distribution is simulated. Point estimates
* of the mean and standard deviation are computed, along with the empirical bias and
* mean square error.
* @version June, 2002
* @author Kyle Siegrist
* @author Dawn Duehring
*/
public class NormalEstimateExperiment extends EstimateExperiment implements Serializable{
	//Variables
	private double mu = 0, sigma = 1, sigma2 = 1, x, sum;
	private double m, s2, t2, w2, mBias, mMSE, s2Bias, s2MSE, t2Bias, t2MSE, w2Bias, w2MSE;
	//Objects
	private RecordTable recordTable = new RecordTable(new String[]{"Run", "M", "S2", "T2", "W2"});
	private Parameter muScroll = new Parameter(-5, 5, 0.5, mu, "Mean", "\u03bc");
	private Parameter sigmaScroll = new Parameter(0.5, 5, 0.5, sigma, "Standard deviation", "\u03c3");
	private NormalDistribution dist = new NormalDistribution(mu, sigma);
	private RandomVariable rv = new RandomVariable(dist);
	private StatisticsTable mTable = new StatisticsTable("M", new String[]{"Bias", "MSE"});
	private StatisticsTable s2Table = new StatisticsTable("S2", new String[]{"Bias", "MSE"});
	private StatisticsTable t2Table = new StatisticsTable("T2", new String[]{"Bias", "MSE"});
	private StatisticsTable w2Table = new StatisticsTable("W2", new String[]{"Bias", "MSE"});

	/**
	* This method intializes the experiment, including the toolbar and scrollbars.
	*/
	public void init(){
		super.init(rv);
		setName("Normal Estimate Experiment");
		//Sliders
		muScroll.applyDecimalPattern("0.0");
		muScroll.getSlider().addChangeListener(this);
		sigmaScroll.applyDecimalPattern("0.0");
		sigmaScroll.getSlider().addChangeListener(this);
		//Toolbar
		addTool(muScroll);
		addTool(sigmaScroll);
		//Record Table
		recordTable.setDescription("M: estimate of \u03bc; S2, T2, W2: estimates of \u03c3^2");
		addComponent(recordTable, 0, 1, 1, 4);
		//M Table
		mTable.setDescription("Bias and mean squre error of M as an estimator of \u03bc");
		addComponent(mTable, 1, 1, 1, 1);
		//S2 Table
		s2Table.setDescription("Bias and mean square error of S2 as an estimator of \u03c3^2");
		addComponent(s2Table, 1, 2, 1, 1);
		//T2 Table
		t2Table.setDescription("Bias and mean square error of T2 as an estimator of \u03c3^2");
		addComponent(t2Table, 1, 3, 1, 1);
		//W2 Table
		w2Table.setDescription("Bias and mean square error of W2 as an estimator of \u03c3^2");
		addComponent(w2Table, 1, 4, 1, 1);
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
			+ "The experiment is to generate a random sample X1, X2, ..., Xn of size n from the normal\n"
			+ "distribution with mean \u03bc and standard deviation \u03c3. The distribution density is shown\n"
			+ "in blue in the graph, and on each update, the sample density is shown in red. On each\n"
			+ "update, the following statistics are recorded:\n"
			+ "M = (X1 + X2 +     + Xn) / n,\n"
			+ "S^2 = [(X1 - M)^2 + (X2 - M)^2 +     + (Xn - M)^2] / (n - 1)\n"
			+ "T^2 = [(X1 - M)^2 + (X2 - M)^2 +     + (Xn - M)^2] / n\n"
			+ "W^2 = [(X1 - m)^2 + (X2 - m)^2 +     + (Xn - m)^2] / n.\n"
			+ "The statistic M is a point estimator of the distribution mean \u03bc and the statistics S^2,\n"
			+ "T^2, and W^2 are point estimators of the distribution variance \u03c3^2. As the experiment runs,\n"
			+ "the empirical bias and mean square error of each estimator are recorded in the second\n"
			+ "table. The parameters m, d, and n can be varied with scroll bars.";
	}

	/**
	* This method defines the experiment. The sample form the normal distribution is
	* simulated, the point estimates computed, and the empirical bias and mean square
	* error computed.
	*/
	public void doExperiment(){
		super.doExperiment();
		resetSample();
		sum = 0;
		int n = getSampleSize();
		for (int i = 0; i < n; i++){
			x = rv.simulate();
			sum = sum + (x - mu) * (x - mu);
		}
		m = rv.getIntervalData().getMean();
		s2 = rv.getIntervalData().getVariance();
		t2 = rv.getIntervalData().getVarianceP();
		w2 = sum / n;
		int runs = getTime();
		mBias = ((runs - 1) * mBias + (m - mu)) / runs;
		mMSE =  ((runs - 1) * mMSE + (m - mu) * (m - mu)) / runs;
		s2Bias = ((runs - 1) * s2Bias + (s2 - sigma2)) / runs;
		s2MSE =  ((runs - 1) * s2MSE + (s2 - sigma2) * (s2 - sigma2)) / runs;
		t2Bias = ((runs - 1) * t2Bias + (t2 - sigma2)) / runs;
		t2MSE =  ((runs - 1) * s2MSE + (t2 - sigma2) * (t2 - sigma2)) / runs;
		w2Bias = ((runs - 1) * w2Bias + (w2 - sigma2)) / runs;
		w2MSE =  ((runs - 1) * w2MSE + (w2 - sigma2) * (w2 - sigma2)) / runs;
	}

	/**
	* This method updates the display, including the random variable graph, record table
	* and statistics table.
	*/
	public void update(){
		super.update();
		recordTable.addRecord(new double[]{getTime(), m, s2, t2, w2});
		mTable.setDataValues(new double[]{mBias, mMSE});
		s2Table.setDataValues(new double[]{s2Bias, s2MSE});
		t2Table.setDataValues(new double[]{t2Bias, t2MSE});
		w2Table.setDataValues(new double[]{w2Bias, w2MSE});
	}

	public void graphUpdate(){
		super.update();
		
		mTable.setShowModelDistribution(showModelDistribution);
		s2Table.setShowModelDistribution(showModelDistribution);
		t2Table.setShowModelDistribution(showModelDistribution);
		w2Table.setShowModelDistribution(showModelDistribution);
		
		mTable.repaint();
		s2Table.repaint();
		t2Table.repaint();
		w2Table.repaint();
	}
	/**
	* This method resets the experiment.
	*/
	public void reset(){
		int n = getSampleSize();
		super.reset();
		recordTable.reset();
		mTable.reset();
		s2Table.reset();
		t2Table.reset();
		w2Table.reset();
		mTable.setDistributionValues(new double[]{0, sigma * sigma / n});
		s2Table.setDistributionValues(new double[]{0, 2 * Math.pow(sigma, 4) / (n - 1)});
		t2Table.setDistributionValues(new double[]{-sigma * sigma / n, (2 * n - 1) * Math.pow(sigma, 4) / (n * n)});
		w2Table.setDistributionValues(new double[]{0, 2 * Math.pow(sigma, 4) / n});
	}


	/**
	* This method handles the scroll events associated with changes in the parameters of
	* the normal distribution.
	* @param e the change event
	*/
	public void stateChanged(ChangeEvent e){
		if (e.getSource() == muScroll.getSlider()){
			mu = muScroll.getValue();
			setDistribution();
		}
		else if (e.getSource() == sigmaScroll.getSlider()){
			sigma = sigmaScroll.getValue();
			sigma2 = sigma * sigma;
			setDistribution();
		}
		else super.stateChanged(e);
	}

	/**
	* This method sets the sampling normal distribution after the parameters have
	* been changed.
	*/
	public void setDistribution(){
		dist.setParameters(mu, sigma);
		reset();
	}
	
	public JTable getResultTable(){
    	return  recordTable.getTable();
    }
}

