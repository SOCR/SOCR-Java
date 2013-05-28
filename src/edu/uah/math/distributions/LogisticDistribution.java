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
* This class models the logistic distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class LogisticDistribution extends Distribution implements Serializable{

	/**
	* This default constructor creates a new logsitic distribution and sets up
	* the default computational domain (&minus;7, 7).
	*/
	public LogisticDistribution(){
		setDomain(-7, 7, 0.14, CONTINUOUS);
	}

	/**
	* This method computes the probability density function,which is
	* f(x) = e<sup>x</sup> / (1 + e<sup>x</sup>)<sup>2</sup>
	* @param x a real number
	* @return the probability density at x
	*/
	public double getDensity(double x){
	double e = Math.exp(x);
		return e / ((1 + e)*(1 + e));
	}

	 /**
	 * This method computes the maximum value of the density function, which is
	 * 1/4, the value of the density at x = 0.
	 * @return the maximum value of the density function
	 */
	 public double getMaxDensity(){
		return 0.25;
	}

	/**
	* This method computes the cumulative distribution function, which is
	* F(x) = e<sup>x</sup> / (1 + e<sup>x</sup>)
	* @param x a real number
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		double e = Math.exp(x);
		return e / (1 + e);
	}

	/**
	* This method comptues the quantile function.
	* @param p a probability in (0, 1)
	* @return the quantile of order p
	*/
	public double getQuantile(double p){
		return Math.log(p / (1 - p));
	}

	/**
	* This method returns the mean of the distribution, which is 0.
	* @return the mean
	*/
	public double getMean(){
		return 0;
	}

	/**
	* This method computes the variance of the distribution, which is
	* &pi;<sup>2</sup> / 3.
	* @return the variance
	method in Distribution.*/
	public double getVariance(){
		return Math.PI * Math.PI / 3;
	}

	/**
	* This method computes the moment generating function, which is
	* &Gamma;(1 + t) / &Gamma;(1 &minus; t).
	* @param t a number &lt; 1
	* @return the moment generating function at t
	*/
	public double getMGF(double t){
		return Functions.gamma(1 + t) * Functions.gamma(1 - t);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Logistic distribution";
	}
}

