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
import java.awt.Dimension;
import java.io.Serializable;

/**
* This class models the floor in Buffon's coin experiment.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CoinFloor extends Graph implements Serializable{
	//Variables
	private double xCenter = 0, yCenter = 0, radius = 0.25;
	private Color coinColor;
	private boolean coinDropped = true;

	/**
	* This general constructor creates a new Buffon floor with a specified coin radius.
	* @param r the radius of the coin.
	*/
	public CoinFloor(double r){
		super(-0.5, 0.5, -0.5, 0.5);
		setMargins(10, 10, 10, 10);
		setPreferredSize(new Dimension(200, 200));
		setBackground(Color.yellow);
		setCoinColor(Color.red);
		setRadius(r);
		setToolTipText("Buffon's floor");
	}

	/**
	* This default constructor creates a new Buffon floor with coin radius 0.25.
	*/
	public CoinFloor(){
		this(0.25);
	}

	/**
	* This method paints the floor tile and the coin.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw floorboard cracks
		g.setColor(Color.black);
		drawLine(g, -0.5, -0.5, 0.5, -0.5);
		drawLine(g, -0.5, 0.5, 0.5, 0.5);
		drawLine(g, -0.5, -0.5, -0.5, 0.5);
		drawLine(g, 0.5, -0.5, 0.5, 0.5);
		//Draw the coin
		if (coinDropped){
			g.setColor(coinColor);
			fillCircle(g, xCenter, yCenter, radius);
		}
	}

	/**
	* This method sets the coin radius to a specified value.
	* @param r the radius of the coin.
	*/
	public void setRadius(double r){
		//Correct for invalid parameters
		if (r < 0) r = 0;
		if (r > 0.5) r = 0.5;
		radius = r;
	}

	/**
	* This method gets the coin radius,
	* @return the coin radius.
	*/
	public double getRadius(){
		return radius;
	}

	/**
	* This method sets the coin color.
	* @param c the coin color
	*/
	public void setCoinColor(Color c){
		coinColor = c;
	}

	/**
	* This method returns the coin color
	* @return the coin color
	*/
	public Color getCoinColor(){
		return coinColor;
	}

	/**
	* This method sets a boolean condition for dropping the coin.
	* @param b true if the coin is dropped.
	*/
	public void setCoinDropped(boolean b){
		coinDropped = b;
		repaint();
	}

	/**
	* This method returns the boolean condition of dorpping the coin.
	* @return true if the coin is dropped
	*/
	public boolean isCoinDropped(){
		return coinDropped;
	}

	/**
	* This method sets the center of the coin to specified values.
	* @param x the x coordinate of the center.
	* @param y the y coordinate of the center.
	*/
	public void setValues(double x, double y){
		//Correct for invalid parameters
		if (x < -0.5) x = -0.5; else if (x > 0.5) x = 0.5;
		if (y < -0.5) y = -0.5; else if (y > 0.5) y = 0.5;
		xCenter = x; yCenter = y;
	}

	/**
	* This method sets the center of the coin to random values.
	*/
	public void setValues(){
		setValues(-0.5 + Math.random(), -0.5 + Math.random());
	}


	/**
	* This method sets the x-coordinate of the coin center.
	* @param x the horizontal coordinate of the coin center
	*/
	public void setXCenter(double x){
		setValues(x, yCenter);
	}

	/**
	* This method gets the x coordinate of the coin center.
	* @return the x coordinate of the coin center.
	*/
	public double getXCenter(){
		return xCenter;
	}

	/**
	* This method sets the y coordinate of the coin center.
	* @param y the vertical coordinate of the coin center
	*/
	public void setYCenter(double y){
		setValues(xCenter, y);
	}

	/**
	* This method gets the y coordinate of the coin center.
	* @return the y coordinate of the coin center.
	*/
	public double getYCenter(){
		return yCenter;
	}

	/**
	* This method determines if the crossing event has occurred.
	* @return true if the coin crosses a crack.
	*/
	public boolean crossEvent(){
		if (radius - 0.5 < xCenter & xCenter < 0.5 - radius
			& radius - 0.5 < yCenter & yCenter < 0.5 - radius) return false;
		else return true;
	}

	/**
	* This method gets the probability of the cross event.
	* @return the probability that the coin crosses a crack.
	*/
	public double getProbability(){
		return 4 * radius * (1 - radius);
	}
}

