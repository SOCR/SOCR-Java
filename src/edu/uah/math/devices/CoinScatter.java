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
import edu.uah.math.distributions.Domain;

/**
* This class models the scatterplot for Buffon's coin experiment.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CoinScatter extends ScatterPlot implements Serializable{
	private double radius;
	private Color eventColor = Color.blue;

	/**
	* This general constructor creates a new Buffon coin scatterplot with a
	* specified radius.
	*@param r the radius of the coin.
	*/
	public CoinScatter(double r){
		super(new Domain(-0.5, 0.5, 0.1, 1));
		setRadius(r);
	}

	/**
	* This default constructor creates a new Buffon coin scatterplot with
	* radius 0.25.
	*/
	public CoinScatter(){
		this(0.25);
	}

	/**
	* This method sets the radius.
	* @param r the radius of the coin.
	*/
	public void setRadius(double r){
		//Correct invalid parameter
		if (r < 0) r = 0;
		if (r > 0.5) r = 0.5;
		radius = r;
	}

	/**
	* This method gets the radius.
	* @return the radius of the coin.
	*/
	public double getRadius(){
		return radius;
	}

	/**
	* This method sets the boundary color for the crack cross event.
	* @param c the event color
	*/
	public void setEventColor(Color c){
		eventColor = c;
	}

	/**
	* This method returns the boundary color for the crack cross event.
	* @return the event color
	*/
	public Color getEventColor(){
		return eventColor;
	}

	/**
	* This method paints the scatterplot.
	*@param g the graphics context.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw the event
		if (!showModelDistribution)
			return;
		g.setColor(eventColor);
		drawLine(g, radius - 0.5, radius - 0.5, 0.5 - radius, radius - 0.5);
		drawLine(g, radius - 0.5, 0.5 - radius, 0.5 - radius, 0.5 - radius);
		drawLine(g, radius - 0.5, radius - 0.5, radius - 0.5, 0.5 - radius);
		drawLine(g, 0.5 - radius, radius - 0.5, 0.5 - radius, 0.5 - radius);
	}
}

