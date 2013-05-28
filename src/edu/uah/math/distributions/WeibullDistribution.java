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
* This class models the Weibull distribution with specified shape and scale
* parameters.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class WeibullDistribution extends Distribution implements Serializable{
	//Variables
	private double shape, scale, c;

	/**
	* This general constructor creates a new Weibull distribution with spcified
	* shape and scale parameters.
	* @param k the shape parameter
	* @param b the scale parameter
	*/
	public WeibullDistribution(double k, double b){
		setParameters(k, b);
	}

	/**
	* This default constructor creates a new Weibull distribution with shape
	* parameter 1 and scale parameter 1.
	*/
	public WeibullDistribution(){
		this(1, 1);
	}

	/**
	* This method sets the shape and scale parameter. The normalizing constant
	* is computed and the default domain defined.
	* @param k the shape parameter
	* @param b the scale parameter
	*/
	public void setParameters(double k, double b){
		double upper, width;
		if (k <= 0) k = 1;
		if (b <= 0) b = 1;
		//Assign parameters
		shape = k; scale = b;
		//Compute normalizing constant
		c = shape / Math.pow(scale, shape);
		//Define interval
		upper = Math.ceil(getMean() + 4 * getSD());
		width = upper/ 100;
		setDomain(0, upper, width, CONTINUOUS);
	}

	/**
	* This method computes the denstiy function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		return c * Math.pow(x, shape - 1) * Math.exp(-Math.pow(x / scale, shape));
	}

	/**
	* This method returns the maximum value of the density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode;
		if (shape < 1) mode = getDomain().getLowerValue();
		else mode = scale * Math.pow((shape - 1) / shape, 1 / shape);
		return getDensity(mode);
	}

	/**
	* The method returns the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		return scale * Functions.gamma(1 + 1 / shape);
	}

	/**
	* This method returns the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		double mu = getMean();
		return scale * scale * Functions.gamma(1 + 2 / shape) - mu * mu;
	}

	/**
	* This method returns the moment of a specified order.
	* @param n the order
	* @return the moment of that order
	*/
	public double getMoment(int n){
		return Math.pow(scale, n) * Functions.gamma(1 + n / shape);
	}

	/**
	* This method returns the moment of a specified order about a specified point.
	* @param a the center
	* @param n the order
	* @return the moment of order n about a
	*/
	public double getMoment(double a, int n){
		double sum = 0;
		for (int k = 0; k <= n; k++) sum = sum + Functions.comb(n, k) * Math.pow(-a, k) * getMoment(k);
		return sum;
	}

	/**
	* This method computes the cumulative distribution function.
	* @param x a number in the domain of the distribution
	* @return the cumulative distsribution at x.
	*/
	public double getCDF(double x){
		return 1 - Math.exp(-Math.pow(x / scale, shape));
	}

	/**
	* This method returns the quantile function
	* @param p a number in (0, 1)
	* @return the quantile of order p
	*/
	public double getQuantile(double p){
		return scale * Math.pow(-Math.log(1 - p), 1 / shape);
	}

	/**
	* This method computes the failure rate function.
	* @param x a number in the domain of the distribution
	* @return the failure rate at x
	*/
	public double getFailureRate(double x){
		return shape * Math.pow(x, shape - 1) / Math.pow(scale, shape);
	}


	/**
	* This method returns the shape parameter.
	* @return the shape parameter
	*/
	public double getShape(){
		return shape;
	}

	/**
	* This method sets the shape parameter.
	* @param k the shape parameter
	*/
	public void setShape(double k){
		setParameters(k, scale);
	}

	/**
	* This method returns the scale parameter.
	* @return the scale parameter
	*/
	public double getScale(){
		return scale;
	}

	/**
	* This method sets the scale parameter.
	* @param b the scale parameter
	*/
	public void setScale(double b){
		setParameters(shape, b);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Weibull distribution [shape = " + shape + ", scale = " + scale + "]";
	}
}

