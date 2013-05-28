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
* This class models the lognormal distribution with specified parameters. This is
* the distribution of Y = exp(X) where X has a normal distribution with the parameters.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class LogNormalDistribution extends Distribution implements Serializable{
	//variables
	public final static double C = Math.sqrt(2 * Math.PI);
	private double location, scale;

	/**
	* This general constructor creates a new lognormal distribution with
	* specified parameters.
	* @param m the location parameter of ln(Y)
	* @param s the scale parameter of ln(Y)
	*/
	public LogNormalDistribution(double m, double s){
		setParameters(m, s);
	}

	/**
	* This default constructor creates the standard lognormal distribution.
	*/
	public LogNormalDistribution(){
		this(0, 1);
	}

	/**
	* This method sets the parameters, computes the default domain.
	* @param m the location parameter of ln(Y)
	* @param s the scale parameter of ln(Y)
	*/
	public void setParameters(double m, double s){
		if (s <= 0) s = 1;
		location = m; scale = s;
		double upper = getMean() + 3 * getSD();
		setDomain(0, upper, 0.01 * upper, CONTINUOUS);
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution [0, &infin;)
	* @return the probability density at x
	*/
	public double getDensity(double x){
		double z = (Math.log(x) - location) / scale;
		return Math.exp(- z * z / 2) / (x * C * scale);
	}

	/**
	* This method computes the maximum value of the density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode = Math.exp(location - scale * scale);
		return getDensity(mode);
	}

	/**
	* This method computes the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		return Math.exp(location + scale * scale / 2);
	}

	/**
	* This method computes the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		double a = location + scale * scale;
		return Math.exp(2 * a) - Math.exp(location + a);
	}

	/**
	* This method computes the moment of a specified order.
	* @param n the order
	* @return the moment of order n about 0
	*/
	public double getMoment(int n){
		return Math.exp(n * location + n * n * scale * scale / 2);
	}

	/**
	* This method returns the moment of a specified order about a specified point.
	* @param n the order
	* @param a the center
	* @return the moment of order n about a
	*/
	public double getMoment(double a, int n){
		double sum = 0;
		for (int k = 0; k <= n; k++) sum = sum + Functions.comb(n, k) * getMoment(k) * Math.pow(-a, n - k);
		return sum;
	}

	/**
	* This method computes the moment generating function.
	* @param t a real number
	* @return the moment genrating function at t
	*/
	public double getMGF(double t){
		if (t == 0) return 1;
		else return Double.POSITIVE_INFINITY;
	}

	/**
	* This method simulates a value from the distribution.
	* @return a simulated value from the distribuiton
	*/
	public double simulate(){
		double r = Math.sqrt(-2 * Math.log(Math.random()));
		double theta = 2 * Math.PI * Math.random();
		return Math.exp(location + scale * r * Math.cos(theta));
	}

	/**
	* This method returns the location parameter of ln(Y).
	* @return the location parameter
	*/
	public double getLocation(){
		return location;
	}

	/**
	* This method sets the location parameter of ln(Y).
	* @param m the location parameter
	*/
	public void setLocation(double m){
		setParameters(m, scale);
	}

	/**
	* This method gets the scale parameter of ln(Y).
	* @return the scale parameter
	*/
	public double getScale(){
		return scale;
	}

	/**
	* This method sets the scale parameter of ln(Y).
	* @param s the scale parameter
	*/
	public void setScale(double s){
		setParameters(location, s);
	}

	/**
	* This method computes the cumulative distribution function.
	* @param x a value in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		double z = (Math.log(x) - location) / scale;
		if (z >= 0) return 0.5 + 0.5 * Functions.gammaCDF(z * z / 2, 0.5);
		else return 0.5 - 0.5 * Functions.gammaCDF(z * z / 2, 0.5);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Log normal distribution [location = " + location + ", scale = " + scale + "]";
	}
}

