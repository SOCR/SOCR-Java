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
* This class creates covolution of a given distribution to a given power.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ConvolutionDistribution extends Distribution implements Serializable{
	private Distribution distribution;
	private int power;
	private double[][] pdf;

	/**
	* This general constructor: creates a new convolution distribution corresponding
	* to a specified distribution and convolution power.
	* @param d the distribution
	* @param n the covolution power
	*/
	public ConvolutionDistribution(Distribution d, int n){
		setParameters(d, n);
	}

	/**
	* This defalut constructor creates a new convolution distribution corrrepsonding to
	* the uniform distribution on (0,1), with convolution power 5.
	*/
	public ConvolutionDistribution(){
		this(new ContinuousUniformDistribution(0, 1), 5);
	}

	/**
	* This method sets the parameters: the distribution and convolution power.
	* The method computes and stores the values of the probability density function.
	* @param d the distribution
	* @param n the covolution power
	*/
	public void setParameters(Distribution d, int n){
		//Correct for invalid parameters
		if (n < 1) n = 1;
		distribution = d; power = n;
		Domain domain = distribution.getDomain();
		double l = domain.getLowerValue(), u = domain.getUpperValue(), w = domain.getWidth(), p, dx;
		int t = distribution.getType();
		if (t == DISCRETE) dx = 1; else dx = w;
		setDomain(power * l, power * u, w, t);
		int m = domain.getSize();
		pdf = new double[power][];
		for (int k = 0; k < n; k++) pdf[k] = new double[(k + 1) * m - k];
			for (int j = 0; j < m; j++) pdf[0][j] = distribution.getDensity(domain.getValue(j));
				for (int k = 1; k < n; k++){
				for (int j = 0; j < (k + 1) * m - k; j++){
					p = 0;
					for (int i = Math.max(0, j - m + 1); i < Math.min(j+1, k * m - k + 1); i++){
					   p = p + pdf[k - 1][i] * pdf[0][j - i];
				}
				pdf[k][j] = p;
			}
		}
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		return pdf[power - 1][getDomain().getIndex(x)];
	}

	/**
	* This method computes the mean of the convolution distribution in terms of the
	* mean of the given distribution and the convolution power.
	* @return the mean
	*/
	public double getMean(){
		return power * distribution.getMean();
	}

	/**
	* This method computes the variance of the convolution distribution in terms of the
	* variance of the given distribution and the convolution power.
	* @return the variance
	*/
	public double getVariance(){
		return power * distribution.getVariance();
	}

	/**
	* This method computes the moment generating function of the convolution distribution
	* in terms of the moment generating function of the given distribution and the
	* convolution power.
	* @param t a real number
	* @return the moment generating function at t
	*/
	public double getMGF(double t){
		return Math.pow(distribution.getMGF(t), power);
	}

	/**
 	* This method simulates a value from the convolution distribution by computing a sum
	* of simulated values from the given distribuiton.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		double sum = 0;
		for (int i = 0; i < power; i++)	sum = sum + distribution.simulate();
		return sum;
	}

	/**
	* This method sets the convolution power.
	* @param n the convolution power
	*/
	public void setPower(int n){
		setParameters(distribution, n);
	}

	/**
	* This method returns the convolution power.
	* @return the convolution power
	*/
	public int getPower(){
		return power;
	}

	/**
	* This method sets the distribution.
	* @param d the distribution to be convolved
	*/
	public void setDistribution(Distribution d){
		setParameters(d, power);
	}

	/**
	* This method returns the distribution.
	* @return the distribution to be convolved
	*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Convolution distribution [basic distribution = " + distribution
			+ ", power = " + power + "]";
	}
}



