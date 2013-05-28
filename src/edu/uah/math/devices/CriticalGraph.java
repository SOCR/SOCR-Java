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
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.NormalDistribution;

/**
* This class defines a graph used in interval estimation and hypothesis testing experiments.
* The graph shows the density of specified random varaiable and an interval along the x-axis.
* The random critical value is shown as a red vertical line.  The event of interest is whether
* the critical value falls in the the specified interval.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CriticalGraph extends DistributionGraph implements Serializable{
	//Variables
	private double lowerCritical, upperCritical, value;
	private boolean success, valueDrawn;
	private Color intervalColor = Color.blue, valueColor = Color.red;

	/**
	* This general constructor creates a new critical graph with a specified distribution.
	* @param d the distribution that governs the test statistic
	*/
	public CriticalGraph(Distribution d){
		super(d);
		setMomentType(NONE);
	}

	/**
	* This default constructor creates a new critical graph with a normally
	* distributed random variable.
	*/
	public CriticalGraph(){
		this(new NormalDistribution());
	}

	/**
	* This method sets the critical values.  These are used to define the interval that is
	* shown as a horizontal bar.
	* @param lc the lower critical value
	* @param uc the upper critical value
	*/
	public void setCriticalValues(double lc, double uc){
		if (lc > Double.NEGATIVE_INFINITY) lowerCritical = lc;
		else lowerCritical = getXScale(0);
		if (uc < Double.POSITIVE_INFINITY) upperCritical = uc;
		else upperCritical = getXScale(getSize().width);
	}

	/**
	* This method sets the lower critical value.
	* @param lc the lower critical value
	*/
	public void setLowerCritical(double lc){
		setCriticalValues(lc, upperCritical);
	}

	/**
	* This method returns the lower critical value.
	* @return the lower critical value
	*/
	public double getLowerCritical(){
		return lowerCritical;
	}

	/**
	* This method sets the upper critical value.
	* @param uc the upper critical value
	*/
	public void setUpperCritical(double uc){
		setCriticalValues(lowerCritical, uc);
	}

	/**
	* This method gets the upper critical value.
	* @return the upper critical value
	*/
	public double getUpperCritical(){
		return upperCritical;
	}

	/**
	* This method sets the value of the test statistic.
	* @param x the value
	*/
	public void setValue(double x){
		value = x;
	}

	/**
	* This method returns the value of the test statistic.
	* @return the value
	*/
	public double getValue(){
		return value;
	}

	/**
	* This emthod sets the value of the test statistic to a value simulated
	* from the distribution.
	*/
	public void setValue(){
		setValue(getDistribution().simulate());
	}

	/**
	* This method paints the graph.
	* @param g the graphcis context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (valueDrawn){
			g.setColor(valueColor);
			drawLine(g, value, 0, value, getYMax());
		}
		if (!showModelDistribution)
			return;
		g.setColor(intervalColor);
		double y0 = getYScale(getSize().height - 10);
		drawBox(g, HORIZONTAL, lowerCritical, y0, upperCritical - lowerCritical, 3, 3);
	}

	/**
	* This method sets the color for the interval.
	* @param c the interval color
	*/
	public void setIntervalColor(Color c){
		intervalColor = c;
	}

	/**
	* This method returns the interval color.
	* @return the interval color
	*/
	public Color getIntervalColor(){
		return intervalColor;
	}

	/**
	* This method sets the color for the value.
	* @param c the value color
	*/
	public void setValueColor(Color c){
		valueColor = c;
	}

	/**
	* This method returns the value color.
	* @return the value color
	*/
	public Color getValueColor(){
		return valueColor;
	}

	/**
	* This method returns the boolean value of the success event. This event occurs
	* if the test value is in the critical interval.
	* @return true if the test value is in the critical interval
	*/
	public boolean isSuccess(){
		return (lowerCritical <= value & value <= upperCritical);
	}

	/**
	* This method sets the boolean condition for drawing the empirical value.
	* @param b true if the value is drawn.
	*/
	public void setValueDrawn(boolean b){
		valueDrawn = b;
		repaint();
	}

	/**
	* This method returns the boolean condition for drawing the empirical value.
	* @return true if the value is drawn
	*/
	public boolean isValueDrawn(){
		return valueDrawn;
	}


}

