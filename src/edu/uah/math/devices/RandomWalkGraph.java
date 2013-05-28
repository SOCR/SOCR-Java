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
* This class models the graph of the simple ranodom walk on a specified
* interval of the form [0, n].
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RandomWalkGraph extends Graph implements Serializable{
	//Variables
	private int steps, maxValue, minValue, lastZero, yMin, yMax;
	private double probability;
	private int[] values;
	private boolean walkShown = true, minShown = true, maxShown = true, lastZeroShown = true;
	private Color graphColor = Color.red;

	/**
	* This general constructor creates a new random walk graph on a specified
	* interval with a specified probability of a step in the positive direction.
	* @param n the number of steps in the random walk
	* @param p the probability of a step to the right
	*/
	public RandomWalkGraph(int n, double p){
		setPreferredSize(new Dimension(200, 200));
		setToolTipText("Random walk graph");
		setParameters(n, p);
		setPointSize(5);
	}

	/**
	* This special constructor creates a new symmetric random walk graph
	* on a specified interval. Thus the probability of a step in the
	* positive direction is 0.5.
	* @param n the number of steps in the random walk
	*/
	public RandomWalkGraph(int n){
		this(n, 0.5);
	}

	/**
	* This default constructor creates a new symmetric random walk graph
	* on the interval [0, 10].
	*/
	public RandomWalkGraph(){
		this(10);
	}

	/**
	* This method sets the parameters, the number of steps and the
	* the probability of a step to the right.
	* @param n the number of steps in the random walk
	* @param p the probability of a step to the right
	*/
	public void setParameters(int n, double p){
		if (n < 0) n = 0; steps = n;
		if (p < 0) p = 0; else if (p > 1) p = 1; probability = p;
		values = new int[n + 1];
		double mean = steps * (2 * probability - 1);
		double stdDev = 2 * Math.sqrt(steps * probability * ( 1 - probability));
		yMin = (int)(mean - 3 * stdDev);
		yMax = (int)(mean + 3 * stdDev);
		setScale(0, n, yMin, yMax);
		walk(0);
	}

	/**
	* This method sets the time parameter.
	* @param n the number of setps in the random walk
	*/
	public void setSteps(int n){
		setParameters(n, probability);
	}

	/**
	* This method gets the time parameter.
	* @return the upper bound of the time interval
	*/
	public int getSteps(){
		return steps;
	}

	/**
	* This method sets the probabiltiy of a step in the positive direction.
	* @param p the probability of a step to the right
	*/
	public void setProbability(double p){
		setParameters(steps, p);
	}

	/**
	* This method returns the probability of a move in the positive direction.
	* @return the probability of a step to the right
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method performs the random walk, starting at a specified initial position.
	* @param v the initial position
	*/
	public void walk(int v){
		int step;
		values[0] = v;
		maxValue = values[0]; minValue = values[0];
		if (values[0] == 0) lastZero = 0; else lastZero = -1;
		for (int i = 1; i <= steps; i++){
			if (Math.random() < probability) step = 1; else step = -1;
			values[i] = values[i - 1] + step;
			if (values[i] > maxValue) maxValue = values[i];
			if (values[i] < minValue) minValue = values[i];
			if (values[i] == 0) lastZero = i;
		}
	}

	/**
	* This method returns the array of values (positions).
	* @return the array of positions of the random walk
	*/
	public int[] getValues(){
		return values;
	}

	/**
	* This method gets the position of the walk at a specified time.
	* @param i the time
	* @return the position at the specified time
	*/
	public int getValues(int i){
		if (i < 0) i = 0; else if (i > steps) i = steps;
		return values[i];
	}

	/**
	* This method returns the maximum values of the random walk.
	* @return the maximum position
	*/
	public int getMaxValue(){
		return maxValue;
	}

	/**
	* This method returns the minimum values of the random walk.
	* @return the minimum position
	*/
	public int getMinValue(){
		return minValue;
	}

	/**
	* This method returns the time of the last return to 0.
	* @return the time of the last return to 0
	*/
	public int getLastZero(){
		return lastZero;
	}

	/**
	* This method paints the random walk graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.black);
		//Draw x axes
		drawLine(g, 0, 0, steps, 0);
		for (int i = 0; i <= steps; i++) drawTick(g, i, 0, VERTICAL);
		drawLabel(g, "0", 0, 0, LEFT);
		drawLabel(g, String.valueOf(steps), steps, 0, RIGHT);
		//Draw vertical axes
		drawAxis(g, yMin, yMax, 1, 0, VERTICAL);
		g.setColor(Color.gray);
		drawLine(g, steps, yMin, steps, yMax);
		for (int i = yMin; i <= yMax; i++) drawTick(g, steps, i, HORIZONTAL);
		//Draw data
		if (walkShown){
			g.setColor(graphColor);
			for (int i = 0; i < steps; i++)	drawLine(g, i, values[i], i + 1, values[i + 1]);
			if (lastZeroShown) drawPoint(g, lastZero, 0);
			if (minShown) drawPoint(g, steps, minValue);
			if (maxShown) drawPoint(g, steps, maxValue);
		}
	}

	/**
	* This method sets the boolean state for showing the random walk
	* @param b true if the random walk is shown
	*/
	public void setWalkShown(boolean b){
		walkShown = b;
		repaint();
	}

	/**
	* This method returns the boolean state for showing the random walk
	* @return true if the random walk is shown
	*/
	public boolean isWalkShown(){
		return walkShown;
	}

	/**
	* This method sets the boolean condition for showing the maximum value.
	* @param b true if the maximum value is shown
	*/
	public void setMaxShown(boolean b){
		maxShown = b;
		repaint();
	}

	/**
	* This method returns the boolean condition for showing the maximum value
	* @return true if the maximum value is shown
	*/
	public boolean isMaxShown(){
		return maxShown;
	}

	/**
	* This method sets the boolean condition for showing the minimum value.
	* @param b true if the minimum value is shown
	*/
	public void setMinShown(boolean b){
		minShown = b;
		repaint();
	}

	/**
	* This method returns the boolean condition for showing the minimum value
	* @return true if the minimum value is shown
	*/
	public boolean isMinShown(){
		return minShown;
	}

	/**
	* This method sets the boolean condition for showing the last zero.
	* @param b true if the last zero is shown
	*/
	public void setLastZeroShown(boolean b){
		lastZeroShown = b;
		repaint();
	}

	/**
	* This method returns the boolean condition for showing the last zero
	* @return true if the last zero is shown
	*/
	public boolean isLastZeroShown(){
		return lastZeroShown;
	}

	/**
	* This method sets the graph color.
	* @param c the graph color
	*/
	public void setGraphColor(Color c){
		graphColor = c;
	}

	/**
	* This method returns the graph color
	* @return the graph color
	*/
	public Color getGraphColor(){
		return graphColor;
	}
}






