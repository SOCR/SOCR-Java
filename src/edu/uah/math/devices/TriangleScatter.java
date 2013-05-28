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
* This class defines the specialized scatterplot for the triangle experiment. The
* scatterplot shows the (0, 1) by (0, 1) square, with the events in the triangle
* experiment (no triangle, obtuse triangle, acute triangle).
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class TriangleScatter extends ScatterPlot implements Serializable{
	private Color eventColor = Color.blue;

	/**
	* This default constructor creates a new triangle scatterplot.
	*/
	public TriangleScatter(){
		super(new Domain(0, 1, 0.1, Domain.CONTINUOUS));
	}

	/**
	* This method paints the scatterplot by drawing the event curves and then calling
	* the corresponding method in the superclass.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		double x1, y, y1;
		if (!showModelDistribution)
			return;
		
		g.setColor(eventColor);
		drawLine(g, 0.5, 0, 0.5, 1);
		drawLine(g, 0, 0.5, 1, 0.5);
		drawLine(g, 0.5, 0, 1, 0.5);
		drawLine(g, 0, 0.5, 0.5, 1);
		for (double x = 0; x + 0.01 <= 0.5; x = x + 0.01){
			x1 = x + 0.01;
			y = 1 / (2 * (1 - x));
			y1 = 1 / (2 * (1 - x1));
			drawLine(g, x, y, x1, y1);
	  		drawLine(g, y, x, y1, x1);
		}
		for (double x = 0; x + 0.01 <= 0.5; x = x + 0.01){
			x1 = x + 0.01;
			y = (1 - 2 * x * x) / (2 * (1 - x));
			y1 = (1 - 2 * x1 * x1) / (2 * (1 - x1));
			drawLine(g, x, y, x1, y1);
	  		drawLine(g, y, x, y1, x1);
		}
		for (double x = 0.5; x + 0.01 <= 1; x = x + 0.01){
			x1 = x + 0.01;
			y = (1 - 2 * x + 2 * x * x) / (2 * x);
			y1 = (1 - 2 * x1 + 2 * x1 * x1) / (2 * x1);
			drawLine(g, y, x, y1, x1);
	  		drawLine(g, x, y, x1, y1);
		}
	}

	/**
	* This method sets the color of the event borders.
	* @param c the event color
	*/
	public void setEventColor(Color c){
		eventColor = c;
	}

	/**
	* Ths methd returns the color of the event borders.
	* @return the event color
	*/
	public Color getEventColor(){
		return eventColor;
	}
}