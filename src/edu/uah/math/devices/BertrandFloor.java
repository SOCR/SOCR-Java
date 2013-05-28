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

/**
* This class models the floor in Betrand's experiment.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BertrandFloor extends Graph implements Serializable{
 	//Variables
	private double xCoordinate, yCoordinate, distance, angle;
	private boolean chordEvent, chordDrawn = false;
	private Color circleColor, triangleColor, chordColor;
	//Constants
	public final static int UNIFORM_DISTANCE = 0, UNIFORM_ANGLE = 1, UNIFORM_POINT = 2;

	/**
	* This default constructor creates a new floor on the unit square
	* [-1, 1] x [-1, 1].
	*/
	public BertrandFloor(){
		super(-1, 1, -1, 1);
		setMargins(10, 10, 10, 10);
		setBackground(Color.yellow);
		setCircleColor(Color.blue);
		setTriangleColor(Color.blue);
		setChordColor(Color.red);
		setToolTipText("Bertrand's Floor");
		setPreferredSize(new Dimension(200, 200));
		setDistance();
	}

	/**
	* This method draws the floor.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw circle
		g.setColor(circleColor);
		drawCircle(g, 0, 0, 1);
		//Draw triangle
		g.setColor(triangleColor);
		drawLine(g, 1, 0, -0.5, 0.866);
		drawLine(g, 1, 0, -0.5, -0.866);
		drawLine(g, -0.5, 0.866, -0.5, -0.866);
		//Draw chord
		if (chordDrawn){
			g.setColor(chordColor);
			drawLine(g, 1, 0, xCoordinate, yCoordinate);
		}
	}

	/**
	* This method sets the chord drawn state.
	* @param b true if the chord is to be drawn
	*/
	public void setChordDrawn(boolean b){
		chordDrawn = b;
		repaint();
	}

	/**This method gets the chord drawn state
	* @return true if the chord is to be drawn
	*/
	public boolean isChordDrawn(){
		return chordDrawn;
	}

	/**
	* This method sets the distance parameter to a specified value.
	* @param d the distance from the center of the circle to the center of the chord.
	*/
	public void setDistance(double d){
		//Correct for invalid parameters
		if (d < 0) d = 0;
		else if (d > 1) d = 1;
		//Set parameters
		distance = d;
		angle = Math.acos(distance);
		setCoordinates();
	}

	/**
	* This method sets the distance parameter to a random value.
	*/
	public void setDistance(){
		setDistance(Math.random());
	}

	/**
	* This method gets the distance parameter.
	* @return the distance from the center of the circle to the center of the chord.
	*/
	public double getDistance(){
		return distance;
	}

	/**
	* This method sets the angle parameter to a specified value.
	* @param a the angle between the chord and the tangent line to the circle at (1, 0).
	*/
	public void setAngle(double a){
		//Correct for invalid parameters
		if (a < 0) a = 0;
		else if (a > Math.PI / 2) a = Math.PI / 2;
		//Set Parameters
		angle = a;
		distance = Math.cos(angle);
		setCoordinates();
	}

	/**
	* This method sets the angle to a random value.
	*/
	public void setAngle(){
		setAngle((Math.PI / 2) * Math.random());
	}

	/**
	* This method gets the angle parameter.
	* @return the angle between the chord and the tangent line to the circle at (1, 0).
	*/
	public double getAngle(){
		return angle;
	}

	/**
	* This method computes the x and y coordinates of the chord, from the distance
	* and angle parameters.
	*/
	private void setCoordinates(){
		xCoordinate = 2 * distance * distance - 1;
		yCoordinate = 2 * Math.sqrt(1 - distance * distance) * distance;
	}

	/**
	* This method sets the x-coordinate of the variable chord point to a
	* specified value.
	* @param x the x-coordinage of the point
	*/
	public void setXCoordinate(double x){
		if (x < -1) x = -1;
		else if (x > 1) x = 1;
		xCoordinate = x;
		distance = Math.sqrt((xCoordinate + 1) / 2);
		yCoordinate = 2 * Math.sqrt(1 - distance * distance) * distance;
		angle = Math.acos(distance);
	}

	/**
	* This method sets the x-coordinate of the variable chord point
	* to a random value in (-1, 1).
	*/
	public void setXCoordinate(){
		setXCoordinate(2 * Math.random() - 1);
	}

	/**
	* This method gets the x-corrdinate of the chord.
	* @return the x-coordinate of the left point of the chord.
	*/
	public double getXCoordinate(){
		return xCoordinate;
	}

	/**
	* This method gets the y-corrdinate of the chord.
	* @return the y-coordinate of the left point of the chord.
	*/
	public double getYCoordinate(){
		return yCoordinate;
	}

	/**
	* This method determines if the length of the chord is longer than the length of the
	* triangle side.
	* @return the boolean value of the event.
	*/
	public boolean chordEvent(){
		return (distance < 0.5);
	}

	/**
	* This method gets the probability of the event that the chord is longer than the lenght of the
	* triangle side.
	* @return the probability of the event.
	*/
	public double getProbability(int model){
		if (model == UNIFORM_DISTANCE) return 0.5;
		else if (model == UNIFORM_ANGLE)  return 1.0 / 3;
		else return 1.0 / 4;
	}

	/**
	* This method sets the color for the circle.
	* @param c the circle color
	*/
	public void setCircleColor(Color c){
		circleColor = c;
	}

	/**
	* This method returns the color of the circle.
	* @return the circle color
	*/
	public Color getCircleColor(){
		return circleColor;
	}


	/**
	* This method sets the color for the triangle.
	* @param c the triangle color
	*/
	public void setTriangleColor(Color c){
		triangleColor = c;
	}

	/**
	* This method returns the color of the triangle.
	* @return the triangle color
	*/
	public Color getTriangleColor(){
		return triangleColor;
	}

	/**
	* This method sets the color for the chord.
	* @param c the chord color
	*/
	public void setChordColor(Color c){
		chordColor = c;
	}

	/**
	* This method returns the color of the chord.
	* @return the chord color
	*/
	public Color getChordColor(){
		return chordColor;
	}


}

