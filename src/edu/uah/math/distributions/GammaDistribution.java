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
* This class models the gamma distribution with a specified shape parameter and scale
* parameter. This distribution governs the time of an event in the Poisson process.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class GammaDistribution extends Distribution implements Serializable{
	//Parameters
	private double shape, scale, c;

	/**
	* This general constructor creates a new gamma distribution with specified
	* shape parameter and scale parameters.
	* @param k the shape parameter
	* @param b the scale parameter
	*/
	public GammaDistribution(double k, double b){
		setParameters(k, b);
	}

	/**
	* This default constructor creates a new gamma distribution with shape parameter
	* 1 and scale parameter 1.
	*/
	public GammaDistribution(){
		this(1, 1);
	}

	/**
	* This method sets the parameters and creates the default domain.
	* @param k the shape parameter
	* @param b the scale parameter
	*/
	public void setParameters(double k, double b){
		double upperBound;
		//Correct invalid parameters
		if(k < 0) k = 1;
		if(b < 0) b = 1;
		shape = k;
		scale = b;
		//Normalizing constant
		c = shape * Math.log(scale) + Functions.logGamma(shape);
		//Assign default partition:
		upperBound = getMean() + 4 * getSD();
		setDomain(0, upperBound, 0.01 * upperBound, CONTINUOUS);
	}

	/**
	* This method sets the shape parameter.
	* @param k the shape parameter
	*/
	public void setShape(double k){
		setParameters(k, scale);
	}

	/**
	* This method returns the shape parameter.
	* @return the shape parameter
	*/
	public double getShape(){
		return shape;
	}

	/**
	* This method sets the scale parameters.
	* @param b the scale parameter
	*/
	public void setScale(double b){
		setParameters(shape, b);
	}

	/**
	* This method returns the scale parameter.
	* @return the scale parameter
	*/
	public double getScale(){
		return scale;
	}

	/**
	* This method computes the probability density function,
	* @param x a number &gt; 0
	* @return the probability density at x
	*/
	public double getDensity(double x){
		if (x < 0) return 0;
		else if (x == 0 & shape < 1) return Double.POSITIVE_INFINITY;
		else if (x == 0 & shape == 1) return Math.exp(-c);
		else if (x == 0 & shape > 1) return 0;
		else return Math.exp(-c + (shape - 1) * Math.log(x) - x / scale);
	}

	/**
	* This method returns the maximum value of the density function.
	* If the shape parameter is &lt; 1, the maximum value is &infin; at x = 0.
	* If the shape parameter is &ge; 1, the maximum occurs at x = b(k &minus 1).
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode;
		if (shape < 1) mode = 0.01; else mode = scale * (shape - 1);
		return getDensity(mode);
	}

	/**
	* This method returns the mean bk.
	* @return the mean of the distribution
	*/
	public double getMean(){
		return shape * scale;
	}

	/**
	* This method returns the variance kb<sup>2</sup>.
	* @return the variance of the distribution
	*/
	public double getVariance(){
		return shape * scale * scale;
	}

	/**This method returns the moment of order n.*/
	public double getMoment(int n){
		return Math.pow(scale, n) * Functions.gamma(n + shape) / Functions.gamma(shape);
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
	* This method returns the moment generating function. This function is
	* given by M(t) = (1 &minus bt)<sup>&minus;k</sup> for t &lt; 1/b.
	* @param t a number in the domain of the MGF
	* @return the moment generating function at t
	*/
	public double getMGF(double t){
		return Math.pow(1 - scale * t, -shape);
	}

	/**
	* This method computes the cumulative distribution function of the distribution.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		return Functions.gammaCDF(x / scale, shape);
	}

	/**
	* This method simulates a value from the distribution. If shape parameter
	* is an integer, this is done by simulating an arrival time	in a Poisson
	* proccess.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		if (shape == Math.rint(shape)){
			double sum = 0;
			for (int i = 1; i <= shape; i++){
				sum = sum - scale * Math.log(1 - Math.random());
			}
			return sum;
		}
		else return super.simulate();
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Gamma distribution [shape = " + shape + ", scale = " + scale + "]";
	}
}

