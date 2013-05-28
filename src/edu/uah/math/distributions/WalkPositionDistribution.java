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

package edu.uah.math.distributions;
import java.io.Serializable;

/**
* This class models the distribution of the position at time n for a random walk
* on the interval [0, n].
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class WalkPositionDistribution extends Distribution implements Serializable{
	//Paramters
	private int steps ;
	private double probability;

	/**
	* This general constructor creates a new distribution with specified time and
	* probability parameters.
	* @param n the number of steps
	* @param p the probabiltiy of a step in the positive direction
	*/
	public WalkPositionDistribution(int n, double p){
		setParameters(n, p);
	}

	/**
	* This default constructor creates a new WalkPositionDistribution with time
	* parameter 10 and probability p = 0.5.
	*/
	public WalkPositionDistribution(){
		this(10, 0.5);
	}

	/**
	* This method sets the time and probability parameters.
	* @param n the number of steps
	* @param p the probability of a step in the positive direction
	*/
	public void setParameters(int n, double p){
		if (n < 0) n = 0;
		if (p < 0) p = 0; else if (p > 1) p = 1;
		steps = n;
		probability = p;
		setDomain(-steps, steps, 2, DISCRETE);
	}

	/**
	* This method computes the density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)Math.rint(x), m = (k + steps) / 2;
		return Functions.comb(steps, m) * Math.pow(probability, m) * Math.pow(1 - probability, steps - m);
	}

	/**
	* This method returns the maximum value of the density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode = 2 * Math.min(Math.floor((steps + 1) * probability), steps) - steps;
		return getDensity(mode);
	}

	/**
	* This method computes the mean.
	* @return the mean of the distribution
	*/
	public double getMean(){
		return 2 * steps * probability - steps;
	}

	/**
	* This method computes the variance.
	* @return the variance of the distribution
	*/
	public double getVariance(){
		return 4 * steps * probability * (1 - probability);
	}

	/**
	* This method sets the number of steps.
	* @param n the number of steps
	*/
	public void setSteps(int n){
		setParameters(n, probability);
	}

	/**
	* This method returns the number of steps in the random walk.
	* @return the number of steps
	*/
	public double getSteps(){
		return steps;
	}

	/**
	* This method sets the probability parameter.
	* @param p the probability of a step in the positve direciton
	*/
	public void setProbability(double p){
		setParameters(steps, p);
	}

	/**
	* This method returns the probability parameter.
	* @return the probability of a step in the positive direction.
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method simulates a value from the distribution.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		int step, position = 0;
		for (int i = 1; i <= steps; i++){
			if (Math.random() < probability) step = 1;
			else step = -1;
			position = position + step;
		}
		return position;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Walk position distribution [steps = " + steps
			+ ", probability = " + probability + "]";
	}

}

