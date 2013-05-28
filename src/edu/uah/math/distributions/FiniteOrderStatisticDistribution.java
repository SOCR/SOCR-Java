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
* This class models the distribution of an order statistic for a sample
* chosen without replacement from {1, 2..., N} .
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class FiniteOrderStatisticDistribution extends Distribution implements Serializable{
	private int sampleSize, populationSize, order;

	/**
	* This general constructor creates a new finite order statistic distribution with
	* specified population and sample sizes, and specified order.
	* @param N the population size
	* @param n the sample size
	* @param k the order
	*/
	public FiniteOrderStatisticDistribution(int N, int n, int k){
		setParameters(N, n, k);
	}

	/**
	* This default constructor creates a new finite order statistic distribution with
	* population size 50, sample size 10, and order 5.
	*/
	public FiniteOrderStatisticDistribution(){
		this(50, 10, 5);
	}

	/**
	* This method sets the parameters: the sample size, population size, and order.
	* The default domain is also computed.
	* @param N the population size
	* @param n the sample size
	* @param k the order
	*/
	public void setParameters(int N, int n, int k){
		populationSize = N;
		sampleSize = n;
		order = k;
		setDomain(order, populationSize - sampleSize + order, 1, DISCRETE);
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int i = (int)Math.rint(x);
		return Functions.comb(i - 1, order - 1)	* Functions.comb(populationSize - i, sampleSize - order)
			/ Functions.comb(populationSize, sampleSize);
	}

	/**
	* This method computes the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		return (double)order * (populationSize + 1) / (sampleSize + 1);
	}


	/**
	* This method computes the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		return (double)(populationSize + 1) * (populationSize - sampleSize)
			* order * (sampleSize + 1 - order) / ((sampleSize + 1) * (sampleSize + 1) * (sampleSize + 2));
	}

	/**
	* This method simulates a value from the distribution.
	* @return a simulated value
	*/
	public double simulate(){
		int[] sample = Functions.getSample(populationSize, sampleSize, Functions.WITHOUT_REPLACEMENT);
		return (Functions.sort(sample))[order];
	}

	/**
	* This method sets the population size.
	* @param N the population size
	*/
	public void setPopulationSize(int N){
		setParameters(N, sampleSize, order);
	}

	/**
	* This method returns the population size.
	* @return the population size
	*/
	public int getPopulationSize(){
 		return populationSize;
	}

	/**
	* This method sets the sample size.
	* @param n the sample size
	*/
	public void setSampleSize(int n){
		setParameters(populationSize, n, order);
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
		setParameters(populationSize, sampleSize, k);
	}

	/**
	* This method returns the order.
	* @return the order
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
		return "Finite order statistic distribution [population size = " + populationSize
			+ ", sample size = " + sampleSize + ", order = " + order + "]";
	}

}

