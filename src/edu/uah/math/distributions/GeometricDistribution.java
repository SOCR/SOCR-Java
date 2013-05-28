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
* This class models the geometric distribution with a given success probability.
* This distribution models the trial number of the first success in a sequence
* of Bernoulli trials.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class GeometricDistribution extends NegativeBinomialDistribution implements Serializable{

	/**
	* This general constructor creates a new geometric distribution with
	* a specified success probabilitiy
	* @param p the success probability
	*/
	public GeometricDistribution(double p){
		super(1, p);
	}

	/**
	* This default constructor creates a new geometric distribution with parameter 0.5
	*/
	public GeometricDistribution(){
		this(0.5);
	}

	/**
	* This method computes the factorial moment of a specified order.
	* @param k the order
	* @return the factorial moment of order k
	*/
	public double getFactorialMoment(int k){
		double p = getProbability();
		return Functions.factorial(k) * Math.pow(1 - p, k - 1) / Math.pow(p, k);
	}

	/**
	* This method ensures that the number of successes is set at 1.
	* @param k the number of successes
	*/
	public void setSuccesses(int k){
		super.setSuccesses(1);
	}

	/**
	* This method sets the negative binomial parameters, and ensures that the
	* number of successes is set at 1.
	* @param k the number of successes
	* @param p the probability of success
	*/
	public void setParameters(int k, double p){
		super.setParameters(1, p);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Geometric distribution [probability = " + getProbability() + "]";
	}
}

