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
* This class models the Bernoulli distribution with a specified parameter. This is
* the distribution of an indicator random variable (taking values 0 and 1).
* @author Kyle Siegrist
* @author Dawn Duehring
* @version June 2002
*/
public class BernoulliDistribution extends BinomialDistribution implements Serializable{

	/**
	* This general constructor creates a new Bernoulli distribution with a specified
	* parameter.
	* @param p the probaiblity of 1
	*/
	public BernoulliDistribution(double p){
		super(1, p);
	}

	/**
	* This default constructor creates a new Bernoulli distribution with parameter
	* p = 0.5.
	*/
	public BernoulliDistribution(){
		this(0.5);
	}

	/**This method returns the maximum value of the probability density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		return 1;
	}

	/**
	* This method sets the number of trials, which is always 1.
	* @param n the number of trials.
	*/
	public void setTrials(int n){
		super.setTrials(1);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Bernouli distribution [probability = " + getProbability() + "]";
	}
}

