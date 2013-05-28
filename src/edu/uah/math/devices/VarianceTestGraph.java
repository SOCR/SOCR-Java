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
package edu.uah.math.devices;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.RandomVariable;

/**
* This class models a special graph used in the hypothesis testing experiment for
* the variance in the standard normal model. The graph shows the true density
* and the empirical density of the sampling random variable, and shows the true
* and hypothesized standard deviations as a type of horizontal boxplot.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class VarianceTestGraph extends RandomVariableGraph implements Serializable{
	//Variables
	private double mean, stdDev, testSD;
	private IntervalData data;

	/**
	* This general constructor creates a new variance test graph with a specified random
	* variable and a specified test standard deviation.
	* @param v the sampling random variable
	* @param t the hypotesized test standard deviation
	*/
	public VarianceTestGraph(RandomVariable v, double t){
		super(v);
		setMomentType(NONE);
		testSD = t;
	}

	/**
	* This default constructor creates a new variance test graph with a
	* normally distributed random variable and test standard deviation 1.
	*/
	public VarianceTestGraph(){
		this(new RandomVariable(), 1);
	}

	/**
	* This method paints the graph.
	* The true and empirical densities are shown as in the standard random variable
	* graph. The mean, true standard deviation, and hypothesized standard deviation
	* are shown as a five-number boxplot. The empirical standar deviation is shown as
	* a three number boxplot. The outcome of the test can be determined graphically.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int j = getSize().height - 10;
		
		//Draw Mean, standard deviation bar
		if (showModelDistribution){
			g.setColor(getDistributionColor());
			drawBoxPlot(g, mean - testSD, mean - stdDev, mean, mean + stdDev, mean + testSD, j);
			}
		if (data.getSize() > 0){
			g.setColor(getDataColor());
			fillBoxPlot(g, mean, data.getSD(), j);
		}
	}

	/**
	* This method sets the test standard deviation.
	* @param t the test standard deaviation
	*/
	public void setTestSD(double t){
		testSD = t;
	}

	/**
	* This method returns the test standard deviation.
	* @return test standard deviation
	*/
	public double getTestSD(){
		return testSD;
	}

	/**
	* This method resets the graph and assigns the distribution mean and
	* standard deviation.
	*/
	public void reset(){
		super.reset();
		RandomVariable v = getRandomVariable();
		mean = v.getDistribution().getMean();
		stdDev = v.getDistribution().getSD();
		data = v.getIntervalData();
	}
}


