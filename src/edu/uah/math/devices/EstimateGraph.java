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
import java.awt.Dimension;
import java.awt.Color;
import java.io.Serializable;
import edu.uah.math.distributions.Domain;

/**
* This class models a basic graph for showing the true and estimated values of a positive
* parameter.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class EstimateGraph extends Graph implements Serializable{
	//Variables
	private double parameter, estimate;
	private Domain domain;
	private Color estimateColor = Color.red, parameterColor = Color.blue;

	/**
	* This general constructor creates a new estimate graph with a specified parameter value,
	* and domain
	* @param p the true value of the parameter
	*/
	public EstimateGraph(double p){
		setMargins(35, 20, 30, 10);
		setPreferredSize(new Dimension(200, 200));
		setParameter(p);
	}

	/**
	* This default constructor creates a new estimate graph with parameter
	* value 1 and default domain.
	*/
	public EstimateGraph(){
		this(1);
	}

	/**
	* This method sets the true value of the parameter.
	* @param p the true value of the parameter.
	*/
	public void setParameter(double p){
		parameter = p;
		domain = new Domain(0, 1.2 * p, 0.12 * p, Domain.CONTINUOUS);
		setScale(0, 2, domain.getLowerBound(), domain.getUpperBound());
	}

	/**
	* This method gets the true value of the parameter.
	* @return the parameter value
	*/
	public double getParameter(){
		return parameter;
	}

	/**
	* This method sets the estimated value of the parameter.
	* @param x the estimated value of the prameter.
	*/
	public void setEstimate(double x){
		estimate = x;
	}

	/**
	* This method gets the estimated value of the parameter.
	* @return the estimated value of the parameter
	*/
	public double getEstimate(){
		return estimate;
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw x axis
		drawLine(g, 0, 0, 2, 0);
		//Draw y axis
		drawAxis(g, domain, 0, VERTICAL);
		//Draw bars
		g.setColor(estimateColor);
		fillBox(g, 0.5, 0, 1.5, estimate);
		if (showModelDistribution){
			g.setColor(parameterColor);
			drawBox(g, 0.5, 0, 1.5, parameter);
		}
	}

	/**
	* This method sets the estimate color.
	* @param c the estimate color
	*/
	public void setEstimateColor(Color c){
		estimateColor = c;
	}

	/**
	* This method returns the estimate color.
	* @return the estimate color.
	*/
	public Color getEstimateColor(){
		return estimateColor;
	}

	/**
	* This method sets the parameter color.
	* @param c the parameter color
	*/
	public void setParameterColor(Color c){
		parameterColor = c;
	}

	/**
	* This method returns the parameter color.
	* @return the parameter color.
	*/
	public Color getParameterColor(){
		return parameterColor;
	}


}

