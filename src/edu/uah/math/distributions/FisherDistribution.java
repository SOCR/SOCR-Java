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
* This class models the Fisher F distribution with a spcified number of
* degrees of freedom in the numerator and denominator.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class FisherDistribution extends Distribution implements Serializable{
	private int numeratorDegrees, denominatorDegrees;
	private double c, num, den;

	/**
	* This general constructor creates a new Fisher distribution with a
	* specified number of degrees of freedom in the numerator and denominator.
	* @param n the numerator degrees of freedom
	* @param d the denominator degrees of freedom
	*/
	public FisherDistribution(int n, int d){
		setParameters(n, d);
	}

	/**
	* This default constructor creates a new Fisher distribution with 5
	* degrees of freedom in numerator and denominator.
	*/
	public FisherDistribution(){
		this(5, 5);
	}

	/**
	* This method sets the parameters, the degrees of freedom in the numerator
	* and denominator. Additionally, the normalizing constant and default domain
	* are computed.
	* @param n the numerator degrees of freedom
	* @param d the denominator degrees of freedom
	*/
	public void setParameters(int n, int d){
		double upper, width;
		//Correct invalid parameters
		if (n < 1) n = 1; if (d < 1) d = 1;
		numeratorDegrees = n; denominatorDegrees = d;
		num = (double)numeratorDegrees; den = (double)denominatorDegrees;
		//Compute normalizing constant
		c = Functions.logGamma(0.5 * (num + den)) - Functions.logGamma(0.5 * num)
			- Functions.logGamma(0.5 * den) + 0.5 * numeratorDegrees * (Math.log(num)
			- Math.log(den));
		//Compute interval
		if (denominatorDegrees <= 4) upper = 20; else upper = getMean() + 4 * getSD();
		width = 0.01 * upper;
		setDomain(0, upper, width, CONTINUOUS);
	}

	/**
	* This method computes the probability denisty function.
	* @param x a real number
	* @return the probability density at x
	*/
	public double getDensity(double x){
		if (x < 0) return 0;
		else if (x == 0 & numeratorDegrees == 1) return Double.POSITIVE_INFINITY;
		else return Math.exp(c + (0.5 * num - 1) * Math.log(x)
			- 0.5 * (num + den) * Math.log(1 + num * x / den));
	}

	/**
	* This method computes the maximum value of the density function.
	* @return the maximum of the probability density function
	*/
	public double getMaxDensity(){
		double mode;
		if (numeratorDegrees == 1) mode = getDomain().getLowerValue();
		else mode = ((num - 2) * den) / (num * (den + 2));
		return getDensity(mode);
	}

	/**
	* This method returns the mean of the probability distribution.
	* The mean is &infin; if d &le; 2 and the mean is d / (d &minus; 2)
	* if d &gt; 2.
	* @return the mean
	*/
	public double getMean(){
		if (denominatorDegrees <= 2) return Double.POSITIVE_INFINITY;
		else return den / (den - 2);
	}

	/**
	* This method returns the variance of the distribution.
	* The variance is undefined if d &le; the variance is
	* &infin; if 2 &lt; d &le; 4; and the variance is
	* 2 d<sup>2</sup>(n + d - 2) / [(d - 2)<sup>2</sup> n (d - 4)]
	* if d &gt; 4.
	* @return the variance
	*/
	public double getVariance(){
		if (denominatorDegrees <= 2) return Double.NaN;
		else if (denominatorDegrees <= 4) return Double.POSITIVE_INFINITY;
		else return 2.0 * (den / (den - 2)) * (den / (den - 2))
			* (den + num - 2) / (num * (den - 4));
	}

	/**
	* This method returns the moment of a specified order.
	* @param n the order
	* @return the moment of order n
	*/
	public double getMoment(int n){
		return Functions.gamma(0.5 * num + n) * Functions.gamma(0.5 * den - n)
			* Math.pow(den / num, n) / (Functions.gamma(0.5 * num) * Functions.gamma(0.5 * den));
	}

	/**
	* This method returns the moment of a specified order about a specified point.
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
	* This method computes the cumulative distribution function in terms of
	* the beta CDF.
	* @param x a real number
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		double u = den / (den + num * x);
		if (x < 0) return 0;
		else return 1 - Functions.betaCDF(u, 0.5 * den, 0.5 * num);
	}

	/**
	* This method returns the numerator degrees of freedom.
	* @return the numerator degrees of freedom
	*/
	public double getNumeratorDegrees(){
		return numeratorDegrees;
	}

	/**
	* This method sets the numerator degrees of freedom.
	* @param n the numerator degrees of freedom
	*/
	public void setNumeratorDegrees(int n){
		setParameters(n, denominatorDegrees);
	}

	/**
	* This method gets the denominator degrees of freedom.
	* @return the denominator degrees of freedom
	*/
	public double getDenominatorDegrees(){
		return denominatorDegrees;
	}

	/**
	* This method sets the denominator degrees of freedom.
	* @param d the denominator degrees of freedom
	*/
	public void setDenominatorDegrees(int d){
		setParameters(numeratorDegrees, d);
	}

	/**
	* This method simulates a value from the distribution. This is done by
	* simulating independent variables U and V, where U is chi-square n and
	* V is chi-squared d, and then computing (U / n) / (V / d).
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		double U, V, Z, r, theta;
		U = 0;
		for (int i = 1; i <= numeratorDegrees; i++){
			r = Math.sqrt(-2 * Math.log(Math.random()));
			theta = 2 * Math.PI * Math.random();
			Z = r * Math.cos(theta);
			U = U + Z * Z;
		}
		V = 0;
		for (int j = 1; j <= denominatorDegrees; j++){
			r = Math.sqrt(-2 * Math.log(Math.random()));
			theta = 2 * Math.PI * Math.random();
			Z = r * Math.cos(theta);
			V = V + Z * Z;
		}
		return (U / numeratorDegrees) / (V / denominatorDegrees);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "F distribution [numerator degrees of freedom = " + numeratorDegrees
			+ ", denominator degrees of freedom = " + denominatorDegrees + "]";
	}
}

