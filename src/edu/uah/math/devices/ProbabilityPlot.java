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

/**
* This class models the probability plot in the probability plot
* experiment. A probability plot is a graph of the order statistics
* of a sample from a distribution versus the corresponding quantiles
* of a test distribution. If the sampling distribution is the same as
* the test distribution, the plot will be close to linear.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version May 26, 2002
*/
public class ProbabilityPlot extends Graph implements Serializable{
	private double[] orderStatistics;
	private double[] testQuantiles;
	private boolean haveData;

	/**
	* This general constructor creates a new probability plot graph with
	* specified upper and lower bounds.
	* @param xMin the minimum x value
	* @param xMax the maximum x value
	* @param yMin the minimum y value
	* @param yMax the maximum y value
	*/
	public ProbabilityPlot(double xMin, double xMax, double yMin, double yMax){
		super(xMin, xMax, yMin, yMax);
		setToolTipText("Probability Plot");
	}

	/**
	* This default constructor creates a new probability plot with common lower bound 0
	* and common upper bound 1.
	*/
	public ProbabilityPlot(){
		this(0, 1, 0, 1);
	}

	/**
	* This method draws the probability plot graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw x axis
		double xMin = getXMin(), xMax = getXMax(), yMin = getYMin(), yMax = getYMax();
		g.setColor(Color.black);
		drawLine(g, xMin, yMin, xMax, yMin);
		for (double x = xMin; x <= xMax; x++) drawTick(g, x, yMin, VERTICAL);
		drawLabel(g, format(xMin), xMin, yMin, BELOW);
		drawLabel(g, format(xMax), xMax, yMin, BELOW);
		//Draw y axis
		drawLine(g, xMin, yMin, xMin, yMax);
		for (double y = yMin; y <= yMax; y++) drawTick(g, xMin, y, HORIZONTAL);
		drawLabel(g, format(yMin), xMin, yMin, LEFT);
		drawLabel(g, format(yMax), xMin, yMax, LEFT);
		g.setColor(Color.red);
		if (haveData){
			for (int i = 0; i < testQuantiles.length; i ++)	drawPoint(g, testQuantiles[i], orderStatistics[i]);
		}
	}

	/**
	* This method sets the test getQuantiles.
	* @param tq the array of test quantiles
	*/
	public void setQuantiles(double[] tq){
		testQuantiles = tq;
	}

	/**
	* This method sets the order statistics.
	* @param os the array of order statistics
	*/
	public void setStatistics(double[] os){
		orderStatistics = os;
		haveData = true;
	}

	/**
	* This method resets the graph and clears the data.
	*/
	public void reset(){
		haveData = false;
		repaint();
	}
}

