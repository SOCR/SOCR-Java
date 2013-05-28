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
import java.awt.Graphics;
import java.awt.Color;
import java.io.Serializable;
import edu.uah.math.distributions.RandomVariable;

/**
* This class defines a special graph used in the mean estimation experiment. The graph
* shows the distribution and data distribution for the sampling random variable, and a
* special boxplot showing the true mean and the interval estimate of the mean.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MeanEstimateGraph extends RandomVariableGraph implements Serializable{
	//Variables
	private double lowerEstimate, upperEstimate;

	/**
	* This general constructor creates a new mean estimate graph with a specified random
	* variable.
	* @param v the sampling random variable
	*/
	public MeanEstimateGraph(RandomVariable v){
		super(v);
		setMomentType(NONE);
	}

	/**
	* This default constructor creates a new mean estimate graph with a default
	* (normally distributed) random variable.
	*/
	public MeanEstimateGraph(){
		this(new RandomVariable());
	}

	/**
	* This method sets the lower and upper bounds of the interval estimate.
	* @param le the lower estimate
	* @param ue the upper estimate
	*/
	public void setEstimates(double le, double ue){
		if (le > Double.NEGATIVE_INFINITY) lowerEstimate = le;
		else lowerEstimate = getXScale(0);
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
	* This method returns the lower estimate
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
	* This method returns the upper estiamte.
	* @return the upper estimate
	*/
	public double getUpperEstimate(){
		return upperEstimate;
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		RandomVariable v = getRandomVariable();
		int j = getSize().height - 10;
		double y0 = getYScale(j);

		//		Draw Mean
		if (showModelDistribution){		
			g.setColor(getDistributionColor());
			drawTick(g, v.getDistribution().getMean(), y0, 6, 6, VERTICAL);
		}
		//Draw interval
		if (v.getIntervalData().getSize() > 0){
			g.setColor(getDataColor());
			fillBoxPlot(g, v.getIntervalData().getMean(), (upperEstimate - lowerEstimate) / 2, j);
		}
	}
}


