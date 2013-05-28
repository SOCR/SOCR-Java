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
import edu.uah.math.distributions.BetaDistribution;

/**
* This class is a graph that shows a prior and posterior beta density.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BetaGraph extends Graph implements Serializable{
	//Variables
	private double left, right, probability, yMax;
	private int trials, successes;
	private boolean posteriorDrawn = false;
	//Objects
	private Color priorColor = Color.blue, posteriorColor = Color.red;
	private BetaDistribution priorDistribution, posteriorDistribution;

	/**
	* This general constructor creates a new beta graph with specified left and
	* right parameters for the prior distribution, specified number of trials and
	* probability of success.
	* @param a the left parameter.
	* @param b the right parameter.
	* @param n the sample size.
	* @param p the true probability of success.
	*/
	public BetaGraph(double a, double b, int n, double p){
		setMargins(30, 20, 30, 20);
		setToolTipText("Beta Graph");
		setParameters(a, b);
		setTrials(n);
		setProbability(p);
	}

	/**
	* This default constructor creates a new beta graph with 10 trials,
	* probability of success 0.5, and left and right parameters 1 (so the prior
	* distribution is uniform).
	*/
	public BetaGraph(){
		this(1, 1, 10, 0.5);
	}

	/**
	* This method sets the parameters: the left and right parameters for the
	* prior distribution, the number of trials, and the probability of success.
	* @param a the left parameter.
	* @param b the right parameter.
	*/
	public void setParameters(double a, double b){
		//Correct for invalid parameters and assign values
		if (a <= 0) a = 1; left = a;
		if (b <= 0) b = 1; right = b;
		//Setup distributions
		priorDistribution = new BetaDistribution(a, b);
		posteriorDistribution = new BetaDistribution(a, b);
		//Initialize parameters
		yMax = 1.3 * priorDistribution.getMaxDensity();
		setScale(0, 1, 0, yMax);
	}

	/**
	* This method sets the left beta parameter.
	* @param a the left beta parameter
	*/
	public void setLeft(double a){
		setParameters(a, right);
	}

	/**
	* This method returns the left beta parameter.
	* @return the left beta parameter
	*/
	public double getLeft(){
		return left;
	}

	/**
	* This method sets the right beta parameter.
	* @param b the right beta parameter
	*/
	public void setRight(double b){
		setParameters(left, b);
	}

	/**
	* This method returns the right beta parameter.
	* @return the right beta parameter
	*/
	public double getRight(){
		return right;
	}

	/**
	* This method paints the grpah.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		double x1;
		//Draw x axis
		g.setColor(Color.black);
		drawAxis(g, 0, 1, 0.1, 0, HORIZONTAL);
		drawAxis(g, 0, yMax, 0.1 * yMax, 0, VERTICAL);
		//Draw prior getDensity
		if (showModelDistribution){
			g.setColor(priorColor);
			drawTick(g, probability, 0, 6, 6, VERTICAL);
			drawLabel(g, "p", probability, 0, BELOW);
			for (double x = 0; x < 1; x = x + 0.01){
				x1 = x + 0.01;
				drawLine(g, x, priorDistribution.getDensity(x), x1, priorDistribution.getDensity(x1));
			}
		}
		
		//Draw posterior data
		if (posteriorDrawn){
			g.setColor(posteriorColor);
			for (double x = 0; x < 1; x = x + 0.01){
				x1 = x + 0.01;
				drawLine(g, x, posteriorDistribution.getDensity(x), x1, posteriorDistribution.getDensity(x1));
			}
		}

	}

	/**
	* This method sets the number of successes to a specified value.
	* @param k the number of successes.
	*/
	public void setSuccesses(int k){
		if (k < 0) k = 0; else if (k > trials) k = trials;
		successes = k;
		posteriorDistribution.setParameters(left + successes, right + trials - successes);
		yMax = 1.3 * Math.max(priorDistribution.getMaxDensity(), posteriorDistribution.getMaxDensity());
		super.setScale(0, 1, 0, yMax);
	}

	/**
	* This method sets the number of successes to a random value, as determined by
	* the binomial distribution.
	*/
	public void setSuccesses(){
		int sum = 0;
		for (int i = 0; i < trials; i++) if (Math.random() < probability) sum++;
		setSuccesses(sum);
	}

	/**
	* This method returns the number of successes.
	* @return the number of successes
	*/
	public int getSuccesses(){
		return successes;
	}

	/**
	* This method sets the number of trials.
	* @param n the number of trials
	*/
	public void setTrials(int n){
		if (n < 0) n = 0; trials = n;
	}

	/**
	* This method gets the number of trials.
	* @return the number of trials
	*/
	public int getTrials(){
		return trials;
	}

	/**
	* This method sets the probability of a success.
	* @param p the probability of a success
	*/
	public void setProbability(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1; probability = p;
	}

	/**
	* This method returns the probability of success.
	* @return the probability of a success
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method sets the color for the prior density graph.
	* @param c the prior color
	*/
	public void setPriorColor(Color c){
		priorColor = c;
	}

	/**
	* This method returns the color for the prior density graph
	* @return the prior color
	*/
	public Color getPriorColor(){
		return priorColor;
	}

	/**
	* This method sets the color for the posterior density graph.
	* @param c the posterior color
	*/
	public void setPosteriorColor(Color c){
		posteriorColor = c;
	}

	/**
	* This method returns the color for the posterior density graph
	* @return the posterior color
	*/
	public Color getPosteriorColor(){
		return posteriorColor;
	}

	/**
	* This method sets the boolean condition for drawing the posterior graph.
	* @param b true if the posterior graph is drawn
	*/
	public void setPosteriorDrawn(boolean b){
		posteriorDrawn = b;
		repaint();
	}

	/**
	* This method returns the boolean condition for drawing the posterior graph.
	* @return true if the posterior graph is drawn
	*/
	public boolean isPosteriorDrawn(){
		return posteriorDrawn;
	}
}