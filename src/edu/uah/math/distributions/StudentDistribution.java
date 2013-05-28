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
* This class models the student t distribution with a specifed degrees of freeom
* parameter.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class StudentDistribution extends Distribution implements Serializable{
	private int degrees;
	private double c;

	/**
	* This general constructor creates a new student distribution with a specified
	* degrees of freedom.
	* @param n the degrees of freedom
	*/
	public StudentDistribution(int n){
		setDegrees(n);
	}

	/**
	* This default constructor creates a new student distribuion with 1 degree
	* of freedom.
	*/
	public StudentDistribution(){
		this(1);
	}

	/**
	* This method sets the degrees of freedom.
	* @param n the degrees of freedom
	*/
	public void setDegrees(int n){
		//Correct invalid parameter
		if (n < 1) n = 1;
		//Assign parameter
		degrees = n;
		//Compute normalizing constant
		c = Functions.logGamma(0.5 * (degrees + 1)) - 0.5 * Math.log(degrees)
			- 0.5 * Math.log(Math.PI) - Functions.logGamma(0.5 * degrees);
		//Compute upper bound
		double upper;
		if (n == 1) upper = 8;
		else if (n == 2) upper = 7;
		else upper = Math.ceil(getMean() + 4 * getSD());
		setDomain(-upper, upper, upper / 50, CONTINUOUS);
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		return Math.exp(c - 0.5 * (degrees + 1) * Math.log(1 + x * x / degrees));
	}

	/**
	* This method returns the maximum value of the probability density function.
	* @return the maximum value of the probability denstiy function
	*/
	public double getMaxDensity(){
		return getDensity(0);
	}

	/**
	* This method returns the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		if (degrees == 1) return Double.NaN;
		else return 0;
	}

	/**
	* This method returns the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		if (degrees == 1) return Double.NaN;
		else if (degrees == 2) return Double.POSITIVE_INFINITY;
		else return (double)degrees / (degrees - 2);
	}

	/**
	* This method returns the moment of order n about 0.
	* @param n the order
	* @return the moment of order n
	*/
	public double getMoment(int n){
		if (n > degrees) return Double.NaN;
		else if (n == 2 * (n / 2)) return Functions.gamma(0.5 * (n + 1))
			* Math.pow(Functions.gamma(0.5 * (degrees - n)), 0.5 * n)
			/ (Functions.gamma(0.5) * Functions.gamma(0.5 * degrees));
		else return 0;
	}

	/**
	* This method returns the moment a specified order about a specified point.
	* @param a the center
	* @param n the order
	* @return the moment of order n about a
	*/
	public double getMoment(double a, int n){
		double sum = 0;
		for (int k = 0; k <= n; k++)
			sum = sum + Functions.comb(n, k) * getMoment(k) * Math.pow(-a, n - k);
		return sum;
	}

	/**
	* This method computes the cumulative distribution function in terms of the
	* beta CDF.
	* @param x a number in the domain of the distribution
	* @return the cumulative distribution at x
	* @see edu.uah.math.distributions.Functions#betaCDF(double, double, double) betaCDF
	*/
	public double getCDF(double x){
		double u = degrees / (degrees + x * x);
		if (x > 0) return 1 - 0.5 * Functions.betaCDF(u, 0.5 * degrees, 0.5);
		else return 0.5 * Functions.betaCDF(u, 0.5 * degrees, 0.5);
	}

	/**
	* This method returns the degrees of freedom parameter.
	* @return the degrees of freedom
	*/
	public double getDegrees(){
		return degrees;
	}

	/**
	* This method simulates a value of the distribution.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		double v, z, r, theta;
		v = 0;
		for (int i = 1; i <= degrees; i++){
			r = Math.sqrt(-2 * Math.log(Math.random()));
			theta = 2 * Math.PI * Math.random();
			z = r * Math.cos(theta);
			v = v + z * z;
		}
		r = Math.sqrt(-2 * Math.log(Math.random()));
		theta = 2 * Math.PI * Math.random();
		z = r * Math.cos(theta);
		return z / Math.sqrt(v / degrees);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Student distribution [degrees of freedom = " + degrees + "]";
	}

}

