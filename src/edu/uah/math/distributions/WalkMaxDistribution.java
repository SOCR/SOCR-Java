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
* This class models the distribution of the maximum value of a symmetric random walk on the interval
* [0, n].
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class WalkMaxDistribution extends Distribution implements Serializable{
	//Paramters
	private int steps;

	/**
	* This general constructor creates a new max walk distribution with a specified
	* time parameter.
	* @param n the number of steps
	*/
	public WalkMaxDistribution(int n){
		setSteps(n);
	}

	/**
	* This default constructor creates a new walk max distribution with time
	* parameter 10.
	*/
	public WalkMaxDistribution(){
		this(10);
	}

	/**
	* This method sets the time parameter.
	* @param n the number of steps
	*/
	public void setSteps(int n){
		if (n < 1) n = 1;
		steps = n;
		setDomain(0, steps, 1, DISCRETE);
	}

	/**
	* This method defines the density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)Math.rint(x), m;
		if ((k + steps) % 2 == 0) m = (k + steps) / 2;
		else m = (k + steps + 1) / 2;
		return Functions.comb(steps, m) / Math.pow(2 , steps);
	}

	/**
	* This method returns the maximum value of the density function.
	* @return the maximum value of the density function
	*/
	public double getMaxDensity(){
		return getDensity(0);
	}

	/**
	* This method returns the time parameter.
	* @return the number of steps
	*/
	public double getSteps(){
		return steps;
	}

	/**
	* This method simulates a value from the distribution.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		int step, max = 0, position = 0;
		for (int i = 1; i <= steps; i++){
			if (Math.random() < 0.5) step = 1;
			else step = -1;
			position = position + step;
			if (position > max) max = position;
		}
		return max;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Walk max distribution [steps = " + steps + "]";
	}

}

