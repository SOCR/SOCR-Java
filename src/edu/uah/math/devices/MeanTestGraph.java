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
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.IntervalData;

/**
* This class defines a special graph used in the hypothesis testing experiment
* for the mean in the standard normal model. The graph shows the true and
* empirical distributions for the sampling random variable, and shows the true
* and hypothesized mean.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MeanTestGraph extends RandomVariableGraph implements Serializable{
	//Variables
	private double testMean;
	private Color testColor = Color.green;

	/**
	* This general constructor creates a new mean test graph with a specified
	* random variable and a specified test mean.
	* @param v the sampling variable
	* @param tm the test median
	*/
	public MeanTestGraph(RandomVariable v, double tm){
		super(v);
		setMomentType(NONE);
		setTestMean(tm);
	}

	/**
	* This default constructor creates a new mean test graph with a default
	* (normally distributed) random variable and with test mean 0.
	*/
	public MeanTestGraph(){
		this(new RandomVariable(), 0);
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		RandomVariable v = getRandomVariable();
		
		
		double y0 = getYScale(getSize().height - 10);
//		Draw Mean
		if (showModelDistribution){
		g.setColor(getDistributionColor());
		drawTick(g, v.getDistribution().getMean(), y0, 6, 6, VERTICAL);
//		Draw test Mean
		g.setColor(testColor);
		drawTick(g, testMean, y0, 6, 6, VERTICAL);
		}
		
		
		if (v.getIntervalData().getSize() > 0){
			g.setColor(getDataColor());
			drawTick(g, v.getIntervalData().getMean(), y0, 6, 6, VERTICAL);
		}
	}

	/**
	* This method sets the test mean.
	* @param tm the test mean
	*/
	public void setTestMean(double tm){
		testMean = tm;
	}

	/**
	* This method returns the test mean.
	* @return the test mean
	*/
	public double getTestMean(){
		return testMean;
	}

	/**
	* This method sets the mean color.
	* @param c the mean color
	*/
	public void setTestColor(Color c){
		testColor = c;
	}

	/**
	* This method returns the mean color.
	* @return the mean color
	*/
	public Color getTestColor(){
		return testColor;
	}
}


