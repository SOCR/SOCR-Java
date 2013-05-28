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
* This is a special graph used in the sign test experiment.  The graph shows
* the density of the random variable, the median, and an hypothesized test median.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MedianGraph extends RandomVariableGraph implements Serializable{
	private double median, testMedian;
	private Color testColor = Color.green;

	/**
	* This general constructor creates a new median graph with a specified random
	* variable and a specified test median.
	* @param v the sampling random variable
	* @param m the test median
	*/
	public MedianGraph(RandomVariable v, double m){
		super(v);
		testMedian = m;
		setMomentType(NONE);
	}

	/**
	* This default constructor creates a new median graph with a normally distributed
	* random variable and test median 0.
	*/
	public MedianGraph(){
		this(new RandomVariable(), 0);
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw Medians
		g.setColor(getDistributionColor());
		double y0 = getYScale(getSize().height - 10);
		drawTick(g, median, y0, 6, 6, VERTICAL);
		g.setColor(testColor);
		drawTick(g, testMedian, y0, 6, 6, VERTICAL);
	}

	/**
	* This method resets the graph.
	*/
	public void reset(){
		super.reset();
		median = getRandomVariable().getDistribution().getMedian();
	}

	/**
	* This method sets the test median to a specified value.
	* @param m the test median
	*/
	public void setTestMedian(double m){
		testMedian = m;
	}

	/**
	* This method returns the test median.
	* @return the test median
	*/
	public double getTestMedian(){
		return testMedian;
	}

	/**
	* This method sets the color for the test median.
	* @param c the test color
	*/
	public void setTestColor(Color c){
		testColor = c;
	}

	/**
	* This method returns the color for the test medain.
	* @return the test color
	*/
	public Color getTestColor(){
		return testColor;
	}
}
