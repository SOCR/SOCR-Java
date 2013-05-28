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
import java.awt.Dimension;
import java.io.Serializable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;

/**
* This class defines a special graph used in conjunction with an interactive histogram.
* The graph shows the mean square error function or the mean absolute error function
* corresponding to a data set.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ErrorGraph extends Graph implements Serializable{
	//Variables
	private int errorType;
	private IntervalData data;
	private Color graphColor = Color.red;

	/**
	* This general constructor creates a new error graph of a specified type and
	* with a specified data set.
	* @param d the interval data set
	* @param t the type of error function (0 mean square, 1 mean absolute)
	*/
	public ErrorGraph(IntervalData d, int t){
		setPreferredSize(new Dimension(200, 200));
		setMargins(35, 20, 30, 10);
		setData(d);
		setErrorType(t);
		setToolTipText("Error Graph");
	}

	/**
	* This default constructor creates a new mean square error graph on the
	* defalt domain (0, 1) (with step size 0.1);
	*/
	public ErrorGraph(){
		this(new IntervalData(), 0);
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Domain domain = data.getDomain();
		double xMin = domain.getLowerBound(), xMax = domain.getUpperBound(), yMax, x0, x1, y0, y1, c;
		if (data.getSize() > 0) yMax = Math.rint(Math.max(1, Math.max(getError(xMin), getError(xMax))));
		else yMax = 1;
		setScale(xMin, xMax, 0, yMax);
		//Draw x axis
		g.setColor(Color.black);
		drawAxis(g, domain, 0, HORIZONTAL);
		//Draw y axis
		drawAxis(g, 0, yMax, 0.1 * yMax, domain.getLowerBound(), VERTICAL);
		//Draw graph of error function
		g.setColor(graphColor);
		if (data.getSize() > 0){
			for (int i = 0; i < domain.getSize(); i++){
				x0 = domain.getBound(i);
				x1 = domain.getBound(i + 1);
				y0 = getError(x0);
				y1 = getError(x1);
				drawLine(g, x0, y0, x1, y1);
			}
			c = getCenter();
			drawLine(g, c, 0, c, getError(c));
		}
	}

	/**
	* This method computes the error function.
	* @param t the independent variable
	* @return the error value
	*/
	public double getError(double t){
		Domain domain = data.getDomain();
		double sum = 0, x;
		for (int i = 0; i < domain.getSize(); i++){
			x = domain.getValue(i);
			if (errorType == 0) sum = sum + data.getRelFreq(x) * (t - x) * (t - x);
			else sum = sum + data.getRelFreq(x) * Math.abs(t - x);
		}
		return sum;
	}

	/**
	* This method returns the measure of center.
	* @return mean if the error type is 0 and the median if the error type is 1.
	*/
	public double getCenter(){
		if (errorType == 0) return data.getMean();
		else return data.getMedian();
	}

	/**
	* This method sets the interval data set.
	* @param d the interval data set.
	*/
	public void setData(IntervalData d){
		data = d;
		repaint();
	}

	/**
	* This method returns the data set.
	* @return the interval data set
	*/
	public IntervalData getData(){
		return data;
	}

	/**
	* This method sets the error type.
	* @param t the type of error function (0 mean square, 1 mean absolute)
	*/
	public void setErrorType(int t){
		if (t != 0) t = 1;
		errorType = t;
		repaint();
	}

	/**
	* This method returns the error type.
	* @return the type of error function (0 mean square, 1 mean absolute)
	*/
	public int getErrorType(){
		return errorType;
	}

	/**
	* This method sets the graph color.
	* @param c the graph color
	*/
	public void setGraphColor(Color c){
		graphColor = c;
	}

	/**
	* This method returns the graph color.
	* @return the graph color
	*/
	public Color getGraphColor(){
		return graphColor;
	}
}

