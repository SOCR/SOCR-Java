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
* This class models the uniform distribution on a specified interval.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ContinuousUniformDistribution extends Distribution implements Serializable{
	private double lowerBound, upperBound;

	/**
	* This general constructor creates a new uniform distribution on a specified interval.
	* @param a the left endpoint
	* @param b the right endpoint
	*/
	public ContinuousUniformDistribution(double a, double b){
		setParameters(a, b);
	}

	/**
	* This default constructor creates a new uniform distribuiton on (0, 1).
	*/
	public ContinuousUniformDistribution(){
		this(0, 1);
	}

	/**
	* This method sets the parameters: the minimum and maximum values of the interval.
	* The default domain is sepcified.
	* @param a the left enpdoint
	* @param b the right endpoint
	*/
	public void setParameters(double a, double b){
		lowerBound = a; upperBound = b;
		double step = 0.01 * (upperBound - lowerBound);
		setDomain(lowerBound, upperBound, step, CONTINUOUS);
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		if (lowerBound <= x & x <= upperBound) return 1 / (upperBound - lowerBound);
		else return 0;
	}

	/**
	* This method computes the maximum value of the density function, which is any
	* value, since the density function is constant.
	* @return the maximum value of the density function
	*/
	public double getMaxDensity(){
		return 1 / (upperBound - lowerBound);
	}

	/**
	* This method computes the mean of the distribution in terms of the parameters.
	* @return the mean
	*/
	public double getMean(){
		return (lowerBound + upperBound) / 2;
	}

	/**
	* This method computes the variance of the distribution in terms of the parameters.
	* @return the variance
	*/
	public double getVariance(){
		return (upperBound - lowerBound) * (upperBound - lowerBound) / 12;
	}

	/**'
	* This method computes the moment of a specified order about a specified point, in
	* terms of the parameters.
	* @param a the center
	* @param n the order
	* @return the moment of order n about a
	*/
	public double getMoment(double a, int n){
		return (Math.pow(upperBound - a, n + 1) - Math.pow(lowerBound - a, n + 1)) / ((n + 1) * (upperBound - lowerBound));
	}

	/**
	* This method computes the moment generating function in terms of the parameters.
	* @param t a real number
	* @return the moment generating function at t
	*/
	public double getMGF(double t){
		if (t == 0) return 1;
		else return (Math.exp(upperBound * t) - Math.exp(lowerBound * t)) / (t * (upperBound - lowerBound));
	}

	/**
	* This method computes the cumulative distribution function in terms of the parameters.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		if (x < lowerBound) return 0;
		else if (x >= upperBound) return 1;
		else return (x - lowerBound) / (upperBound - lowerBound);
	}

	/**
	* This method computes the quantile function in terms of the parameters.
	* @param p a probability in (0, 1)
	* @return the quantile of order p
	*/
	public double getQuantile(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1;
		return lowerBound + (upperBound - lowerBound) * p;
	}

	/**
	* This method sets the lower bound of the domain.
	* @param a the lower bound
	*/
	public void setLowerBound(double a){
		setParameters(a, upperBound);
	}

	/**
	* This method gets the lower bound of the domain of the distribution.
	* @return the left endpoint of the interval
	*/
	public double getLowerBound(){
		return lowerBound;
	}

	/**
	* This method sets the upper bound of the domain.
	* @param b the upper bound
	*/
	public void setUpperBound(double b){
		setParameters(lowerBound, b);
	}

	/**
	* This method returns the upper bound of the domain.
	* @return the right enpoint of the interval
	*/
	public double getUpperBound(){
		return upperBound;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Continuous uniform distribution [lower bound = " + lowerBound
			+ ", upper bound = " + upperBound + "]";
	}
}

