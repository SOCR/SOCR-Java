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
* This class models the distribution of the sample size needed to get a specified number
* of distinct sample values when sampling with replacement from a finite population of
* a specified size.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CouponDistribution extends Distribution implements Serializable{
	private int populationSize, distinctValues, upperValue;
	private double[][] prob;

	/**
	* This general constructor creates a new coupon distribution with specified
	* population size and a specified number of distinct values.
	* @param m the population size
	* @param k the number of distinct sample values
	*/
	public CouponDistribution(int m, int k){
		setParameters(m, k);
	}

	/**
	* This general constructor creates a new coupon distribution with population size 10
	* and and 10 distinct values.
	*/
	public CouponDistribution(){
		this(10, 10);
	}


	/**
	* This method sets the parameters: the population size and number of distinct values
	* needed. The method also computes the default domain.
	* @param m the population size
	* @param k the number of distinct sample values
	*/
	public void setParameters(int m, int k){
		int upperIndex, maxIndex;
		//Correct for invalid parameters
		if (m < 1) m = 1;
		if (k < 1) k = 1; else if (k > m) k = m;
		populationSize = m; distinctValues = k;
		upperValue = (int)Math.ceil(getMean() + 4 * getSD());
		setDomain(distinctValues, upperValue, 1, DISCRETE);
		prob = new double[upperValue + 1][populationSize + 1];
		prob[0][0] = 1; prob[1][1] = 1;
		for (int i = 1; i < upperValue; i++){
		if (i < populationSize) upperIndex = i + 1; else upperIndex = populationSize;
			for (int n = 1; n <= upperIndex; n++){
				prob[i + 1][n] = prob[i][n] * ((double)n / populationSize) + prob[i][n - 1] * ((double)(populationSize - n + 1) / populationSize);
			}
		}
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)(Math.rint(x));
		if (k < distinctValues | k > upperValue) return 0;
		else return ((double)(populationSize - distinctValues + 1) / populationSize) * prob[k - 1][distinctValues - 1];
	}

	/**
	* This method computes the mean of the distribution.
	* @return the mean
	*/
	public double getMean(){
		double sum = 0;
		for (int i = 1; i <= distinctValues; i++)
			sum = sum + (double)populationSize / (populationSize - i + 1);
		return sum;
	}

	/**
	* This method computes the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		double sum = 0;
		for (int i = 1; i <= distinctValues; i++)
			sum = sum + (double)(populationSize * (i - 1)) / ((populationSize - i + 1) * (populationSize - i + 1));
		return sum;
	}

	/**
	* This method returns the population size.
	* @return the population size
	*/
	public int getPopulationSize(){
		return populationSize;
	}

	/**
	* This method sets the population size.
	* @param m the population size.
	*/
	public void setPopulationSize(int m){
		setParameters(m, distinctValues);
	}

	/**
	* This method returns the number of distinct values in the sample.
	* @return the number of distinct values in the sample
	*/
	public int getDistinctValues(){
		return distinctValues;
	}

	/**
	* This method sets the number of distinct values in the sample.
	* @param k the number of distinct values in the sample
	*/
	public void setDistinctValues(int k){
		setParameters(populationSize, k);
	}

	/**
	* This method simulates a value from the distribution, by sampling with replacement
	* from the population until the specified number of distinct sample values is
	* obtained.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		int[] cellCount = new int[(int)populationSize];
		double occupiedCells = 0;
		int ballCount = 0;
		while (occupiedCells <= distinctValues){
			ballCount++;
			int ballIndex = (int)(populationSize * Math.random());
			if (cellCount[ballIndex] == 0) occupiedCells++;
			cellCount[ballIndex] = cellCount[ballIndex]++;
		}
		return ballCount;
	}

	/**
	* This method computes the probability generating function of the distribution.
	* @param t a real number
	* @return the probability generating function at t
	*/
	public double getPGF(double t){
		double prod = 1, r = (double)populationSize / (distinctValues - 1);
		if (Math.abs(t) < r){
			for (int i = 1; i <= distinctValues; i++) prod = prod * (populationSize - i + 1) * t / (populationSize - (i - 1) * t);
			return prod;
		}
		else if (t >= r) return Double.POSITIVE_INFINITY;
		else return Double.NaN;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Coupon distribution [population size = " + populationSize
			+ ", distinct values = " + distinctValues + "]";
	}
}

