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
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.NormalDistribution;

/**
* This class defines a basic graph for displaying the probability density function or
* cumulative distribution function of a distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DistributionGraph extends Graph implements Serializable{
	//Constants
	public final static int PDF = 1, CDF = 2;
	public final static int NONE = 0, MSD = 1, MAD = 2;
	//Variables
	private int momentType = MSD, functionType = PDF, values;
	private double xMin, xMax, yMax, width;
	//Objects
 	private Distribution distribution;
 	private Domain domain;
 	private Color distributionColor = Color.blue;
 	
	/**
	* This general constructor creates a new distribution graph with a
	* specified distribution.
	* @param d the distribution
	*/
	public DistributionGraph(Distribution d){
		setMargins(35, 20, 30, 10);
		setDistribution(d);
	}

	/**
	* This default constructor creates a new distribution graph corresponding to the
	* normally distribution.
	*/
	public DistributionGraph(){
		this(new NormalDistribution());
	}
	
	/**
	* This method paints the graph of the density function, empirical density
	* function, moment bar, and empirical moment bar.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw axes
		g.setColor(Color.black);
		drawAxis(g, 0, yMax, 0.1 * yMax, xMin, VERTICAL);
		drawAxis(g, domain, 0, HORIZONTAL);
		//Draw function
		g.setColor(distributionColor);
		if (functionType == PDF) drawDistributionPDF(g);
		if (functionType == CDF) drawCDF(g);
		//Draw boxplot
		if (momentType == MSD) drawDistributionMSD(g);
	}

	/**
	* This method draws the graph of the probability density function of the
	* distribution.
	* @param g the graphics context
	*/
	public void drawDistributionPDF(Graphics g){
		if (!showModelDistribution)
			  return;
		
		double x, x1, x2;
		if (distribution.getType() == Distribution.DISCRETE){
			for (int i = 0; i < values; i++){
				x = domain.getValue(i);
 				drawBox(g, x - width / 2, 0, x + width / 2, distribution.getDensity(x));
 			}
		}
		else{
			for (int i = 0; i < values; i++){
				x1 = domain.getBound(i);
				x2 = x1 + width;
				drawLine(g, x1, distribution.getDensity(x1), x2, distribution.getDensity(x2));
			}
		 }
	 }

	 /**
	 * This method draws the graph of the cumulative distribution function.
	 * @param g the gaphics context
	 */
	 public void drawCDF(Graphics g){
		 if (!showModelDistribution)
			  return;
		 
		 double x, x1, x2;
		 if (distribution.getType() == Distribution.DISCRETE){
			for (int i = 0; i < values; i++){
				 x = domain.getValue(i);
				 drawBox(g, x - width / 2, 0, x + width / 2, distribution.getCDF(x));
			}
		}
		else{
			for (int i = 0; i < values; i++){
				x1 = domain.getBound(i);
				x2 = x1 + width;
				drawLine(g, x1, distribution.getCDF(x1), x2, distribution.getCDF(x2));
			}
		}
	}

	 /**
	 * This method draws a horizontal boxplot showing the mean and standard deviation.
	 * @param g the graphics context
	 */
	 public void drawDistributionMSD(Graphics g){
		 if (!showModelDistribution)
			  return;
		 int j = getSize().height - 10;
		 drawBoxPlot(g, distribution.getMean(), distribution.getSD(), j);
	 }


	/**
	* This method specifies the distribution and sets up graph paramters.
	* @param d the distribution
	*/
	public void setDistribution(Distribution d){
		distribution = d;
		setDomain(distribution.getDomain());
		setToolTipText(distribution.toString());
	}

	/**
	* This method returns the distribution associated with the graph.
	* @return the distribution
	*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**
	* This method sets the domain for the horizontal axis. This may be different\
	* than the the default distribution domain.
	* @param d the domain to be used in the graph
	*/
	public void setDomain(Domain d){
		domain = d;
		xMin = domain.getLowerBound();
		xMax = domain.getUpperBound();
		width = domain.getWidth();
		values = domain.getSize();
		if (functionType == 1 | functionType == 3){
			yMax = 1.2 * distribution.getMaxDensity();
			if (distribution.getType() == Distribution.DISCRETE & yMax > 1) yMax = 1;
		}
		else if (functionType == CDF) yMax = 1;
		//Set the scale;
		setScale(xMin, xMax, 0, yMax);
		repaint();
	}

	/**
	* This method returns the domain for the horizontal axis of the graph.
	* @return the domain
	*/
	public Domain getDomain(){
		return domain;
	}

	/**
	* This method sets the maximum value for the vertical axis.  This may be
	* different than the default value.
	* @param m the maximum y value
	*/
	public void setYMax(double m){
		yMax = m;
		setScale(xMin, xMax, 0, yMax);
		repaint();
	}

	/**
	* This method sets the graph scale. This method should be called whenever the
	* distribution parameters change, because changes in the parameters may cause
	* changes in the domain and in the maximum value of the density function.
	*/
	public void reset(){
		setDomain(distribution.getDomain());
	}

	/**
	* This method specifies the moments to display in the moment bar
	* (none or mean, standard deviation).
	* @param n the type of moment bar (NONE, MSD)
	*/
	public void setMomentType(int n){
		momentType = n;
		repaint();
	}

	/**
	* This method return the type of moment bar that is displayed.
	* @return the type of moment bar.
	*/
	public int getMomentType(){
		return momentType;
	}

	/**
	* This method sets the type of function that is drawn.
	* @param n the function type
	*/
	public void setFunctionType(int n){
		functionType = n;
		if (functionType == PDF){
			yMax = 1.2 * distribution.getMaxDensity();
			if (distribution.getType() == Distribution.DISCRETE & yMax > 1) yMax = 1;
		}
		else if (functionType == CDF) yMax = 1;
		//Set the scale;
		setScale(xMin, xMax, 0, yMax);
		repaint();

		repaint();
	}

	/**
	* This method returns the type of function that is drawn.
	* @return the function type
	*/
	public int getFunctionType(){
		return functionType;
	}

	/**
	* This method specifies the graph color.
	* @param c the color for the graph
	*/
	public void setDistributionColor(Color c){
		distributionColor = c;
	}

	/**
	* This method returns the graph color.
	* @return the graph color
	*/
	public Color getDistributionColor(){
		return distributionColor;
	}


}

