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
* The class models the Poisson distribution with a specified rate parameter.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class PoissonDistribution extends Distribution implements Serializable{
	//Variables
	private double parameter;

	/**
	* This default constructor creates a new Poisson distribution with a given
	* parameter value.
	* @param r the rate parameter
	*/
	public PoissonDistribution(double r){
		setParameter(r);
	}

	/**
	* This default constructor creates a new Poisson distribtiton with parameter 1.
	*/
	public PoissonDistribution(){
		this(1);
	}

	/**
	* This method sets the parameter and creates the domain of the distribution.
	* @param r the rate parameter
	*/
	public void setParameter(double r){
		//Correct for invalid parameter:
		if(r < 0) r = 1;
		parameter = r;
		//Sets the truncated set of values
		double a = Math.ceil(getMean() - 4 * getSD()), b = Math.ceil(getMean() + 4 * getSD());
		if (a < 0) a = 0;
		setDomain(a, b, 1, DISCRETE);
	}

	/**
	* This method sets the rate parameter.
	* @return the rate parameter
	*/
	public double getParameter(){
		return parameter;
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)Math.rint(x);
		if (parameter == 0){
			if (k == 0) return 1;
			else return 0;
		}
		else{
			if(k < 0) return 0;
			else return Math.exp(-parameter + k * Math.log(parameter) - Functions.logGamma(k + 1));
		}
	}

	/**
	* This method returns the maximum value of the density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode = Math.floor(parameter);
		return getDensity(mode);
	}

	/**
	* This method computes the cumulative distribution function in terms of the gamma
	* cumulative distribution function.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		return 1 - Functions.gammaCDF(parameter, x + 1);
	}

	/**
	* This method returns the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		return parameter;
	}

	/**
	* This method returns the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		return parameter;
	}

	/**
	* This method returns the factorial moment of a specified order.
	* @param n the order
	* @return the facotrial moment
	*/
	public double getFactorialMoment(int n){
		return Math.pow(parameter, n);
	}

	/**
	* This method returns the probability generating function.
	* @param t a real number
	* @return the probability generating function at t
	*/
	public double getPGF(double t){
		return Math.exp(parameter * (t - 1));
	}

	/**
	* This method returns the moment generating function.
	* @param t a real number
	* @return the moment generating function at t
	*/
	public double getMGF(double t){
		return getPGF(Math.exp(t));
	}

	/**
	* This method simulates a value from the distribution, by simulating the
	* number of arrivals in an interval of parameter length in a Poisson process
	* with rate 1.
	* @return a simulated value form the distribution
	*/
	public double simulate(){
		int arrivals = 0;
		double sum = -Math.log(1 - Math.random());
		while (sum <= parameter){
			arrivals++;
			sum = sum - Math.log(1 - Math.random());
		}
		return arrivals;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Poisson distribution [parameter = " + parameter + "]";
	}

}

