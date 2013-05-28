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
import edu.uah.math.distributions.DieDistribution;
import edu.uah.math.distributions.RandomVariable;

/**
* This class is a special graph that shows a true die distribution, a hypothesized die distribution
* and a data distribution, for use in a goodness of fit test.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DiceDistributionGraph extends RandomVariableGraph{
	private RandomVariable dieVariable;
	private DieDistribution  testDist;
	private Color darkGreen = new Color(0, 180, 0);

	/**
	* This general constructor creates a new graph with a specified random variable
	* (which includes the true and data distributions), and a specified hypothesized
	* distribution.
	* @param v the random variable
	* @param d the test distribution
	*/
	public DiceDistributionGraph(RandomVariable v, DieDistribution d){
		super(v);
		setMomentType(NONE);
		setParameters(v, d);
		setToolTipText("Sampling, test, and empirical distributions");
	}

	/**
	* This default constructor creates a new dice distribution graph with
	* fair true distribution and test distribution.
	*/
	public DiceDistributionGraph(){
		this(new RandomVariable(new DieDistribution()), new DieDistribution());
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		// Draw test distribution
		if (showModelDistribution){
			g.setColor(darkGreen);
			for (int i = 1; i <= 6; i++){
				drawBox(g, i - 0.5, 0, i + 0.5, testDist.getDensity(i));
			}
		}
	}

	/**
	* This method sets the random variable (which includes the true and data
	* distributions) and the test distribution.
	* @param v the random variable
	* @param d the test distribution
	*/
	public void setParameters(RandomVariable v, DieDistribution d){
		setRandomVariable(v);
		testDist = d;
		double m = Math.max(v.getDistribution().getMaxDensity(), d.getMaxDensity());
		setYMax(1.2 * m);
	}
}
