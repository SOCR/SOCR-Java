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
* This class models the negative binomial distribution with specified successes
* parameter and probability parameter.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class NegativeBinomialDistribution extends Distribution implements Serializable{
	//Paramters
	private int successes;
	private double probability;

	/**
	* This general constructor creates a new negative binomial distribution with
	* given parameter values.
	* @param k the number of successes
	* @param p the probability of success
	*/
	public NegativeBinomialDistribution(int k, double p){
		setParameters(k, p);
	}

	/**
	* This default constructor creates a new negative binomial distribution with
	* successes parameter 1 and probability parameter 0.5.
	*/
	public NegativeBinomialDistribution(){
		this(1, 0.5);
	}

	/**
	* This method set the paramters and the default domain.
	* @param k the number of successes
	* @param p the probability of success
	*/
	public void setParameters(int k, double p){
		//Correct for invalid parameters
		if(k < 1) k = 1;
		if(p <= 0) p = 0.05;
		if(p > 1) p = 1;
		//Assign parameters
		successes = k;
		probability = p;
		//Set truncated values
		setDomain(successes, Math.ceil(getMean() + 4 * getSD()), 1, DISCRETE);
	}

	/**
	* This method set the successes parameter.
	* @param k the number of successes
	*/
	public void setSuccesses(int k){
		setParameters(k, probability);
	}

	/**
	* This method returns the successes parameter.
	* @return the number of successes
	*/
	public int getSuccesses(){
		return successes;
	}

	/**
	* This method returns the probability parameter.
	* @return the probability of success
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method sets the probability parameter.
	* @param p the probability of success
	*/
	public void setProbability(double p){
		setParameters(successes, p);
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int n = (int)Math.rint(x);
		if(n < successes) return 0;
		else return Functions.comb(n - 1, successes - 1) * Math.pow(probability, successes)
			* Math.pow(1 - probability, n - successes);
	}

	/**
	* This method computes the maximum value of the density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode = Math.floor((successes - 1) / probability + 1);
		return getDensity(mode);
	}

	/**
	* This method computes the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		return successes / probability;
	}

	/**
	* This method computes the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		return (successes * (1 - probability)) / (probability * probability);
	}

	/**
	* This method computes the probability generating function.
	* @param t a real number
	* @return the probability generating function at t
	*/
	public double getPGF(double t){
		double r = 1 / (1 - probability);
		if(Math.abs(t) < r) return Math.pow(t * probability / (1 - t + t * probability), successes);
		else if (t >= r) return Double.POSITIVE_INFINITY;
		else return Double.NaN;
	}

	/**
	* This method computes moment generating function.
	* @param t a real number
	* @return the moment generating function
	*/
	public double getMGF(double t){
		return getPGF(Math.exp(t));
	}

	/**
	* This method simulates a value from the distribution, overriding the
	* correspondin default method in Distribution.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		int count = 0, trials = 0;
		while (count <= successes){
			if (Math.random() < probability) count++;
			trials++;
		}
		return trials;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Negative binomial distribution [successes = " + successes
			+ ", probability = " + probability + "]";
	}
}

