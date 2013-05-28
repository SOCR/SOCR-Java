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
import java.io.Serializable;
import edu.uah.math.distributions.Domain;

/**
* This class models the scatterplot for Buffon's needle experiment. The variables are
* the distance from the center of the needle to the nearest crack, and the
* angle from the horizontal to the upper half of the needle.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class NeedleScatter extends ScatterPlot implements Serializable{
	//Variables
	private double length;
	private Color eventColor = Color.blue;

	/**
	* This general constructor creates a new needle scatterplot with a specified
	* needle length.
	* @param l the needle lenght
	*/
	public NeedleScatter(double l){
		super(new Domain(0, Math.PI, 0.1, 1), new Domain(0, 1, 0.1, 1));
		setLength(l);
	}

	/**
	* This default constructor creates a new scatterplot with needle length 1/2.
	*/
	public NeedleScatter(){
		this(0.5);
	}

	/**
	* This method set the needle length.
	* @param l the needle length
	*/
	public void setLength(double l){
		//Correct for invalid parameter value
		if (l < 0) l = 0; else if (l > 1) l = 1;
		length = l;
	}

	/**
	* This method gets the needle length.
	*@return the needle length
	*/
	public double getLength(){
		return length;
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(java.awt.Graphics g){
		double x1, y0, y1;
		super.paintComponent(g);
		//Draw curves
		if (!showModelDistribution)
			return;
		
		g.setColor(eventColor);
		for (double x = 0; x + 0.1 <= Math.PI; x = x + 0.1){
			x1 = x + 0.1;
			y0 = 0.5 * length * Math.sin(x);
			y1 = 0.5 * length * Math.sin(x1);
			drawLine(g, x, y0, x1, y1);
			drawLine(g, x, 1 - y0, x1, 1 - y1);
		}
	}

	/**
	* This method sets the event color.
	* @param c the event color
	*/
	public void setEventColor(Color c){
		eventColor = c;
	}

	/**
	* This method returns the event color.
	* @return the event color
	*/
	public Color getEventColor(){
		return eventColor;
	}
}

