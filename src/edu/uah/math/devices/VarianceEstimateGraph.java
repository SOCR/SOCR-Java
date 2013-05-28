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
import edu.uah.math.distributions.RandomVariable;

/**
* This class defines a special graph used in the interval estimate experiment for
* the variance. The graph shows the density function and empirical density function
* of the sampling variable, and shows the mean, true standard deviation, and
* estimated standard deviation as a type of horizontal boxplot.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class VarianceEstimateGraph extends RandomVariableGraph implements Serializable{
	//Variables
	private double lowerEstimate, upperEstimate;

	/**
	* This general constructor creates a new variance estimate graph corresponding to a
	* specified sampling random variable.
	* @param v the random variable
	*/
	public VarianceEstimateGraph(RandomVariable v){
		super(v);
		setMomentType(NONE);
	}

	/**
	* This default constructor creates a new variance estimate graph with a
	* normally distributed random variable.
	*/
	public VarianceEstimateGraph(){
		this(new RandomVariable());
	}

	/**
	* This method sets the interval estimate of the standard deviaiton.
	* @param le the lower estimate
	* @param ue the upper estimate
	*/
	public void setEstimates(double le, double ue){
		lowerEstimate = le;
		if (ue < Double.POSITIVE_INFINITY) upperEstimate = ue;
		else upperEstimate = getXScale(getSize().width);
	}

	/**
	* This method sets the lower estimate.
	* @param le the lower estimate
	*/
	public void setLowerEstimate(double le){
		setEstimates(le, upperEstimate);
	}

	/**
	* This method returns the lower estimate.
	* @return the lower estimate
	*/
	public double getLowerEstimate(){
		return lowerEstimate;
	}

	/**
	* This method sets the upper estimate.
	* @param ue the upper estimate
	*/
	public void setUpperEstimate(double ue){
		setEstimates(lowerEstimate, ue);
	}

	/**
	* This method returns the upper estimate.
	* @return the upper estimate
	*/
	public double getUpperEstimate(){
		return upperEstimate;
	}


	/**
	* This method paints the graph. The density and empirical density are shown
	* as in the standard random variable graph. A three-number boxplot shows the
	* true mean and standard deviation. A five-number boxplot shows the true mean
	* and the lower and upper estimates.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		RandomVariable v = getRandomVariable();
		double mean = v.getDistribution().getMean(), stdDev = v.getDistribution().getSD();
		int j = getSize().height - 10;
		//Draw Mean
		if (showModelDistribution){
			g.setColor(getDistributionColor());
		
			drawBoxPlot(g, mean, stdDev, j);
		}
		if (v.getIntervalData().getSize() > 0){
			g.setColor(getDataColor());
			fillBoxPlot(g, mean - upperEstimate, mean - lowerEstimate,
				mean, mean + lowerEstimate, mean + upperEstimate, j);
		}
	}
}


