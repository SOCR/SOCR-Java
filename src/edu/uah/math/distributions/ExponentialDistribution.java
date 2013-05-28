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
* This class defines the standard exponential distribution with a specified rate parameter.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ExponentialDistribution extends GammaDistribution implements Serializable{
	//Parameter
	double rate;

	/**
	* This general constructor creates a new exponential distribution with a
	* specified rate.
	* @param r the rate
	*/
	public ExponentialDistribution(double r){
		setRate(r);
	}

	/**
	* This default constructor creates a new exponential distribution with rate 1.
	*/
	public ExponentialDistribution(){
		this(1);
	}

	/**
	* This method sets the rate parameter.
	* @param r the rate
	*/
	public void setRate(double r){
		if (r <= 0) r = 1;
		rate = r;
		super.setParameters(1, 1 / rate);
	}

	/**
	* This method gets the rate.
	* @return the rate
	*/
	public double getRate(){
		return 1 / getScale();
	}

	/**
	* This method computes the probability denstiy function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		if (x < 0) return 0;
		else return rate * Math.exp(-rate * x);
	}

	/**
	* This method defines the cumulative distribution function.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		return 1 - Math.exp(- rate * x);
	}

	/**
	* The method computes the quantile function.
	* @param p a probability in (0, 1)
	* @return the quantile of order p
	*/
	public double getQuantile(double p){
		return -Math.log(1 - p) / rate;
	}

	/**
	* This method computes the moment of order n.
	*/
	public double getMoment(int n){
		return Functions.factorial(n) / Math.pow(rate, n);
	}

	/**
	* This method sets the gamma shape parameter, which must be 1.
	* @param k the shape parameter
	*/
	public void setShape(double k){
		super.setShape(1);
	}

	/**
	* This method sets the gamma shape and scale parameters. The shape
	* parameter must be 1.
	* @param k the shape parameter
	* @param b the scale parameter
	*/
	public void setParameters(double k, double b){
		super.setParameters(1, b);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Exponential distribution [rate = " + rate + "]";
	}

}

