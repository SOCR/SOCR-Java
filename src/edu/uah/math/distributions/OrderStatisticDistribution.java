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
* This class models the distribution of the order statistic of a specified order from a
* random sample of a specified size from a specified sampling distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class OrderStatisticDistribution extends Distribution implements Serializable{
	private Distribution distribution;
	private int sampleSize, order;

	/**
	* This general constructor creates a new order statistic distribution
	* corresponding to a specified sampling distribution, sample size, and
	* order.
	* @param d the probability distribution
	* @param n the sample size
	* @param k the order
	*/
	public OrderStatisticDistribution(Distribution d, int n, int k){
		setParameters(d, n, k);
	}

	/**
	* This default constructor creates a new order statistic distribution with
	* the uniform distribution, sample size n = 2, and order k = 1.
	*/
	public OrderStatisticDistribution(){
		this(new ContinuousUniformDistribution(), 2, 1);
	}

	/**
	* This method sets the parameters: the sampling distribution, sample size, and order.
	* @param d the probability distribution
	* @param n the sample size
	* @param k the order
	*/
	public void setParameters(Distribution d, int n, int k){
		//Correct for invalid parameters
		if (n < 1) n = 1;
		if (k < 1) k = 1; else if (k > n) k = n;
		//Assign parameters
		distribution = d;
		sampleSize = n;
		order = k;
		setDomain(distribution.getDomain());
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		double p = distribution.getCDF(x);
		if (distribution.getType() == DISCRETE) return getCDF(x) - getCDF(x - getDomain().getWidth());
		else return order * Functions.comb(sampleSize, order) * Math.pow(p, order - 1) * Math.pow(1 - p, sampleSize - order) * distribution.getDensity(x);
	}

	/**
	* This method computes the cumulative distribution function.
	* @param x a number in the domain of x
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		double sum = 0;
		double p = distribution.getCDF(x);
		for (int j = order; j <= sampleSize; j++) sum = sum + Functions.comb(sampleSize, j) * Math.pow(p, j) * Math.pow(1 - p, sampleSize - j);
		return sum;
	}

	/**
	* This method sets the sampling distribution.
	* @param d the sampling distribution
	*/
	public void setDistribution(Distribution d){
		setParameters(d, sampleSize, order);
	}

	/**
	* This method returns the sampling distribution.
	* @return the sampling distribution
	*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**
	* This method sets the sample size.
	* @param n the sample size
	*/
	public void setSampleSize(int n){
		setParameters(distribution, n, order);
	}

	/**
	* This method returns the sample size.
	* @return the sample size
	*/
	public int getSampleSize(){
		return sampleSize;
	}

	/**
	* This method sets the order.
	* @param k the order
	*/
	public void setOrder(int k){
		setParameters(distribution, sampleSize, k);
	}

	/**
	* This method returns the order
	*/
	public int getOrder(){
		return order;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Order statistic distribution [sampling distribution = " + distribution
			+ ", sample size = " + sampleSize + "order + " + order + "]";
	}
}

