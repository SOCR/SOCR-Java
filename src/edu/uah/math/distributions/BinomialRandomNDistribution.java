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
* This class models the binomial distribution with a random number of trials.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BinomialRandomNDistribution extends Distribution{
	//Variables
	private double probability, sum;
	private Distribution distribution;

	/**
	* This general constructor creates a new randomized binomial distribution with a
	* specified probability of success and a specified distribution for the number of
	* trials.
	* @param d the distribution of the number of trials
	* @param p the probabiltiy of success
	*/
	public BinomialRandomNDistribution(Distribution d, double p){
		setParameters(d, p);
	}

	/**
	* This special constructor creates a new randomized binomial distribution with a
	* specified probability of success and the uniform distribution on {1, 2, 3, 4, 5, 6}
	* for the number of trials.
	* @param p the probability of success
	*/
	public BinomialRandomNDistribution(double p){
		this(new FiniteDistribution(), p);
	}

	/**
	* This default constructor creates a new randomized binomial distribution with
	* probability of success 0.5 and the uniform distribution on {1, 2, 3, 4, 5, 6}
	* or the number of trials.
	*/
	public BinomialRandomNDistribution(){
		this(0.5);
	}

	/**
	* This method sets the parameters: the distribution for the number of trials and the
	* probability of success. Also, the default domain is computed.
	* @param d the distribution of the number of trials
	* @param p the probability of success
	*/
	public void setParameters(Distribution d, double p){
		distribution = d;
		if (p < 0) p = 0; else if (p > 1) p = 1; probability = p;
		setDomain(0, distribution.getDomain().getUpperValue(), 1, DISCRETE);
	}

	/**
	* This method sets the distribution for the number of trials.
	* @param d the distribution of the number of trials.
	*/
	public void setDistribution(Distribution d){
		setParameters(d, probability);
	}

	/**
	* This method gets the distribution for the number of trials.
	* @return the distribution of the number of trials.
	*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**
	* This method sets the probability of success.
	* @param p the probability of success
	*/
	public void setProbability(double p){
		setParameters(distribution, p);
	}

	/**
	* This method returns the probability of success.
	* @return the probability of success
	*/
	public double getProbability(){
		return probability;
	}

	/**
	* This method computes the probability density function in terms of the
	* probability density of the number of trials and the probability of success.
	* @param x a number in the domain of the distribuiton
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)Math.rint(x);
		double trials;
		if (probability == 0){
			if (k == 0) return 1;
			else return 0;
		}
		else if (probability == 1) return distribution.getDensity(k);
		else{
			sum = 0;
			for(int i = 0; i < distribution.getDomain().getSize(); i++){
				trials = distribution.getDomain().getValue(i);
				sum = sum + distribution.getDensity(trials) *
					Functions.comb(trials, k) * Math.pow(probability, k) * Math.pow(1 - probability, trials - k);
			}
			return sum;
		}
	}

	/**
	* This method computes the mean in terms of the mean of the number of trials and
	* the probability of success.
	* @return the mean
	*/
	public double getMean(){
		return distribution.getMean() * probability;
	}

	/**
	* This method computes the variance in terms of the mean and variance of the
	* number of trials and the probability of success.
	* @return the variance
	*/
	public double getVariance(){
		return distribution.getMean() * probability * (1 - probability) + distribution.getVariance() * probability * probability;
	}

	/**
	* This method computes the probability generating function in terms of the probability
	* generating function of the number of trials and the probability of success.
	* @param t a real number
	* @return the probability generating function at t
	*/
	public double getPGF(double t){
		return distribution.getPGF(1 - probability + probability * t);
	}

	/**
	* This method computes the moment generating funciton in terms of the probability
	* generating function,
	* @param t a real number
	* @return the moment generating function at t
	*/
	public double getMGF(double t){
		return getPGF(Math.exp(t));
	}

	/**
	* This method simulates a value from the distribution, by simulating a random number
	* of Bernoulli trials and then computing the number of successes.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		int trials = (int)distribution.simulate();
		int successes = 0;
		for (int i = 1; i <= trials; i++){
			if (Math.random() < probability) successes++;
		}
		return successes;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Randomized binomail distribution [distribution for the number of trials = "
			+ distribution + ", probability of success = " + probability + "]";
	}


}



