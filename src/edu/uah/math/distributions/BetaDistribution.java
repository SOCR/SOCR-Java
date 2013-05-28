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
* This class is models the beta distribution with specified left and right parameters.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class BetaDistribution extends Distribution implements Serializable{
	//Parameters
	private double left, right, c;

	/**
	* This general constructor creates a beta distribution with specified left and right
	* parameters.
	* @param a the left parameter
	* @param b the right parameter
	*/
	public BetaDistribution(double a, double b){
		setParameters(a, b);
	}

	/**
	* This default constructor creates a beta distribution with left and right parameters
	* equal to 1. This is the uniform distribution on (0, 1).
	*/
	public BetaDistribution(){
		this(1, 1);
	}

	/**
	* This method sets the parameters, computes the normalizing constant c, and specifies the
	* default domain.
	* @param a the left parameter
	* @param b the right parameter
	*/
	public void setParameters(double a, double b){
		double lower, upper, step;
		//Correct parameters that are out of bounds
		if (a <= 0) a = 1;
		if (b <= 0) b = 1;
		//Assign parameters
		left = a; right = b;
		//Compute the normalizing constant
		c = Functions.logGamma(left + right) - Functions.logGamma(left) - Functions.logGamma(right);
		//Specifiy the interval and partiton
		setDomain(0, 1, 0.01, CONTINUOUS);
	}

	/**
	* This method sets the left parameter.
	* @param a the left parameter
	*/
	public void setLeft(double a){
		setParameters(a, right);
	}

	/**
	* This method sets the right parameter.
	* @param b the right parameter
	*/
	public void setRight(double b){
		setParameters(left, b);
	}

	/**
	* This method gets the left paramter.
	* @return the left parameter
	*/
	public double getLeft(){
		return left;
	}

	/**
	* This method gets the right parameter.
	* @return the right parameter
	*/
	public double getRight(){
		return right;
	}

  /**
  * This method computes the probability density function.
  * @param x a number in the domain of the distribution
  * @return the probability density at x
  */
	public double getDensity(double x){
		if ((x < 0) | (x > 1)) return 0;
		else if ((x == 0) & (left == 1)) return right;
		else if ((x == 0) & (left < 1)) return Double.POSITIVE_INFINITY;
		else if ((x == 0) & (left > 1)) return 0;
		else if ((x == 1) & (right == 1)) return left;
		else if ((x == 1) & (right < 1)) return Double.POSITIVE_INFINITY;
		else if ((x == 1) & (right > 1)) return 0;
		else return Math.exp(c + (left - 1) * Math.log(x) + (right - 1) * Math.log(1 - x));
	}

	/**
	* This method computes the maximum value of the density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode;
		if (left < 1) mode = 0.01;
		else if (right <= 1) mode = 0.99;
		else mode = (left - 1) / (left + right - 2);
		return getDensity(mode);
	}

	/**
	* This method compute the mean in terms of the parameters.
	* @return the mean
	*/
	public double getMean(){
		return left / (left + right);
	}

	/**
	* This method computes the variance in terms of the parameters.
	* @return the variance
	*/
	public double getVariance(){
		return left * right / ((left + right) * (left + right) * (left + right + 1));
	}

	/**
	* This method returns the moment of a specified order.
	* @param n the order
	* @return the moment of order n about 0
	*/
	public double getMoment(int n){
		return Functions.beta(left + n, right) / Functions.beta(left, right);
	}

	/**
	* This method returns the moment a specified order about a specified point.
	* @param a the center
	* @param n the order
	* @return the moment of order n about a
	*/
	public double getMoment(double a, int n){
		double sum = 0;
		for (int k = 0; k <= n; k++) sum = sum + Functions.comb(n, k) * getMoment(k) * Math.pow(-a, n - k);
		return sum;
	}

	/**
	* This method computes the cumulative distribution function.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		return Functions.betaCDF(x, left, right);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Beta distribution [left parameter = " + left
			+ ", right parameter = " + right + "]";
	}
}

