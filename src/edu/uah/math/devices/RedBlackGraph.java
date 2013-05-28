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

/**
* This is a specialized graph used in the red-black experiment. The graph
* shows the initial, current, and target fortunes of a gambler betting
* on a sequence of Bernoulli trials.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RedBlackGraph extends Graph{
	private int initial, target, current;

	/**
	* This general constructor creates a new red-black graph with a specified
	* initial fortune and target fortune.
	* @param i the initial fortune
	* @param t the target fortune
	*/
	public RedBlackGraph(int i, int t){
		setMargins(40, 20, 20, 20);
		setParameters(i, t);
		current = initial;
		setToolTipText("Fortune graph");
	}

	/**
	* This special constructor creates a new red-black graph with initial fortune 5
	* and target fortune 10.
	*/
	public RedBlackGraph(){
		this(5, 10);
	}

	/**
	* This method sets the parameters: the initial fortune and the target fortune.
	* @param i the initial fortune
	* @param t the target fortune
	*/
	public void setParameters(int i, int t){
		initial = i;
		target = t;
		setScale(0, 4, 0, target);
	}

	/**
	* This method paints the graph. The initial, current, and target fortunes
	* are shown as bars.
	* @param g the graphics context.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw axes
		drawLine(g, 0, 0, 4, 0);
		drawAxis(g, 0, target, 1, 0, VERTICAL);
		g.setColor(Color.red);
		fillBox(g, 0.5, 0, 1.5, initial);
		g.setColor(Color.black);
		fillBox(g, 0.5, initial, 1.5, target);
		g.setColor(Color.red);
		fillBox(g,2.5, 0, 3.5, current);
		g.setColor(Color.black);
		fillBox(g, 2.5, current, 3.5, target);
	}

	/**
	* This method sets the current fortune.
	* @param c the current fortune
	*/
	public void setCurrent(int c){
		current = c;
	}
}

