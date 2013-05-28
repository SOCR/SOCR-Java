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
* This class models the floor in Buffon's needle experiment, and shows the ruled
* floor and the needle.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class NeedleFloor extends Graph implements Serializable{
	//Variables
	private double distance, angle, length;
	private boolean needleDropped = true;
	private Color needleColor = Color.red;

	/**
	* This general constructor creates a new floor with a specified needle length.
	* @param l the needle length
	*/
	public NeedleFloor(double l){
		super(-1, 1, -0.5, 1.5);
		setMargins(0, 0, 0, 0);
		setBackground(Color.yellow);
		setToolTipText("Buffon's Floor");
		setLength(l);
		setValues();
	}

	/**
	* This default constructor creates a new NeedleFloor with needle length 0.5.
	*/
	public NeedleFloor(){
		this(0.5);
	}

	/**
	* This method paints the floor.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		double x, y1, y2;
		super.paintComponent(g);
		//Draw floorboard cracks
		g.setColor(Color.black);
		drawLine(g, -1, 0, 1, 0);
		drawLine(g, -1, 1, 1, 1);
		//Draw needle
		if(needleDropped){
			g.setColor(needleColor);
			x = (length / 2) * Math.cos(angle);
			y1 = distance + (length / 2) * Math.sin(angle);
			y2 = distance - (length / 2) * Math.sin(angle);
			drawLine(g, x, y1, -x, y2);
		}
	}

	/**
	* This method sets the boolean condition for the needle drop.
	* @param b true if the needle is dropped.
	*/
	public void setNeedleDropped(boolean b){
		needleDropped = b;
		repaint();
	}

	/**
	* This method returns the boolean condition for the needle drop.
	* @return true if the needle is dropped
	*/
	public boolean isNeedleDropped(){
		return needleDropped;
	}

	/**
	This method sets the length of the needle.
	* @param l the needle length
	*/
	public void setLength(double l){
		//correct for invalid parameter
		if (l < 0) l = 0; else if (l > 1) l = 1;
		length = l;
	}

	/**
	* This method gets the needle length.
	* @return the needle length
	*/
	public double getLength(){
		return length;
	}

	/**
	* This method sets the distance and angle values of the needle
	* to specified values.
	* @param d the distance from the center of the needle to the nearest crack
	* @param a the angle between the horizontal and the top half of the needle
	*/
	public void setValues(double d, double a){
		//Correct for invalid values
		if (d < 0) d = 0; else if (d > 1) d = 1;
		if (a < 0) a = 0; else if (a > Math.PI) a = Math.PI;
		distance = d;
		angle = a;
	}

	/**
	* This method sets the distance and angle values of the needle to random values.
	*/
	public void setValues(){
		setValues(Math.random(), Math.random() * Math.PI);
	}

	/**
	* This method sets the distance value of the needle.
	* @param d the distance from the center of the needle to the nearest crack.
	*/
	public void setDistance(double d){
		setValues(d, angle);
	}

	/**
	* This method gets the distance.
	* @return the distance from the center of the needle to the nearest crack
	*/
	public double getDistance(){
		return distance;
	}

	/**
	* This method sets the angle value of the needle.
	* @param a the angle between the horizontal and the top half of the needle
	*/
	public void setAngle(double a){
		setValues(distance, a);
	}

	/**
	* This method gets the angle.
	* @return the angle from the horizontal to the top half of the needle
	*/
	public double getAngle(){
		return angle;
	}

	/**
	* This method determines if the cross event has occurred.
	* @return true if the needle crosses a crack in the floorboards
	*/
	public boolean crossEvent(){
		if (distance < 0.5 * length * Math.sin(angle) |
			distance > 1 - 0.5 * length * Math.sin(angle)) return true;
		else return false;
	}

	/**
	* This method gets the probability of the cross event.
	* @return the probability that the needle crosses a crack in the floorboards
	*/
	public double getProbability(){
		return 2 * length / Math.PI;
	}

	/**
	* This method sets the color for the needle.
	* @param c the needle color
	*/
	public void setNeedleColor(Color c){
		needleColor = c;
	}

	/**
	* This method returns the color for the needle.
	* @return the needle color
	*/
	public Color getNeedleColor(){
		return needleColor;
	}
}

