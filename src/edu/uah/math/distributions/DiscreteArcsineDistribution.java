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
* This class models the discrete arcsine distribution. The distribution governs the last
* zero in a symmetric random walk on an interval.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DiscreteArcsineDistribution extends Distribution implements Serializable{
	//Paramters
	private int steps;

	/**
	* This general constructor creates a new discrete arcsine distribution with a
	* specified number of steps.
	* @param n the number of steps
	*/
	public DiscreteArcsineDistribution(int n){
		setSteps(n);
	}

	/**
	* This default constructor creates a new discrete arcsine distribution with 10 steps.
	*/
	public DiscreteArcsineDistribution(){
		this(10);
	}

	/**
	* This method sets the steps, the number of steps, and then defines the default
	* domain.
	* @param n the number of steps
	*/
	public void setSteps(int n){
		steps = n;
		setDomain(0, steps, 2, DISCRETE);
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)x;
		return Functions.comb(k, k / 2) * Functions.comb(steps - k, (steps - k) / 2) / Math.pow(2, steps);
	}

	/**
	* This method computes the maximum value of the density function. The maximum value
	* is the value at 0, one of the modes of the distribution.
	* @return the maximum value of the probabiltiy density function
	*/
	public double getMaxDensity(){
		return getDensity(0);
	}

	/**
	* This method gets the steps, the number of steps.
	* @return the number of setps
	*/
	public int getSteps(){
		return steps;
	}

	/**
	* This method computes the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		return steps / 2;
	}

	/**
	* This method simulates a value from the distribution, by simulating a random walk
	* on the interval and computing the time of the last zero.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		int step, lastZero = 0, position = 0;
		for (int i = 1; i <= steps; i++){
			if (Math.random() < 0.5) step = 1;
			else step = -1;
			position = position + step;
			if (position == 0) lastZero = i;
		}
		return lastZero;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Discrete arcsine distribution [steps = " + steps + "]";
	}
}

