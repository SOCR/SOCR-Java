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
import edu.uah.math.distributions.Domain;

/**
* This class models a scatterplot for a bivariate distribution.  The means, standard
* deviations, and regression line are shown.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BivariateScatterPlot extends ScatterPlot implements Serializable{
	double distSlope, distIntercept, sampleSlope, sampleIntercept;
	double xMin, xMax;
	boolean sampleLine = false;

	/**
	* This general constructor creates a new BivariateScatterPlot with
	* specified domains and names.
	* @param d1 the domain of the x variable.
	* @param n1 the name of the x variable.
	* @param d2 the domain of the y variable.
	* @param n2 the name of the y variable.
	*/
	public BivariateScatterPlot(Domain d1, String n1, Domain d2, String n2){
		super(d1, n1, d2, n2);
	}

	/**
	* This special constructor creates a new BivariateScatterPlot with
	* common domain and specified names.
	* @param d the common domain of the x and y variables.
	* @param n1 the name of the x variable.
	* @param n2 the name of the y variable.
	*/
	public BivariateScatterPlot(Domain d, String n1, String n2){
		super(d, n1, n2);
	}

	/**
	* This special constructor creates a new BivariateScatterPlot with
	* specified domains and default names.
	* @param d1 the domain of the x variable.
	* @param d2 the domain of the y variable.
	*/
	public BivariateScatterPlot(Domain d1, Domain d2){
		super(d1, d2);
	}

	/**
	* This special constructor creates a new BivariateScatterPlot with
	* specified common domain and default names.
	* @param d the common domain of the x and y variables.
	*/
	public BivariateScatterPlot(Domain d){
		super(d);
	}

	/**
	* This default constructor creates a new BivariateScatterPlot
	* with default domain (0, 1) (with step size 0.1);
	*/
	public BivariateScatterPlot(){
		this(new Domain());
	}

	/**
	* This method sets the parameters (slope and intercept) of the distribution
	* regression line.
	* @param m the slope of the regression line.
	* @param b the y-intercept of the regression line.
	*/
	public void setParameters(double m, double b){
		distSlope = m;
		distIntercept = b;
	}

	/**
	* This method sets the statistics (slope and intercept) for the sample
	* regression line.
	* @param m the slope of the regression line.
	* @param b the y-intercept of the regression line.
	*/
	public void setStatistics(double m, double b){
		sampleSlope = m;
		sampleIntercept = b;
	}

	/**
	* This method paints the scatterplot.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		xMin = getXDomain().getLowerBound();
		xMax = getXDomain().getUpperBound();
		
		double y1 = distIntercept + distSlope * xMin;
		double y2 = distIntercept + distSlope * xMax;
		if (showModelDistribution){
				g.setColor(Color.blue);
			drawLine(g, xMin, y1, xMax, y2);
		}
		if (getDataSize() > 0){
			g.setColor(Color.red);
			y1 = sampleIntercept + sampleSlope * xMin;
			y2 = sampleIntercept + sampleSlope * xMax;
			drawLine(g, xMin, y1, xMax, y2);
		}
	}
}

