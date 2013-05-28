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
* This class models the hypergeometric distribution with a specified population size,
* sample size, and number of type 1 objects. This is the distribution of the number of
* type 1 objects in a sample chosen without replacement from a finite, two-type
* population.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class HypergeometricDistribution extends Distribution implements Serializable{
	private int populationSize, sampleSize, type1Size;
	private double c;

	/**
	* This general constructor creates a new hypergeometric distribution with specified
	* values of the parameters.
	* @param m the population size
	* @param r the number of type 1 objects in the population
	* @param n the sample size
	*/
	public HypergeometricDistribution(int m, int r, int n){
		setParameters(m, r, n);
	}

	/**
	* This default constructor: creates a new hypergeometric distribuiton with
	* population 100 containing 50 type 1 objects, and with sample size 10.
	*/
	public HypergeometricDistribution(){
		this(100, 50, 10);
	}

	/**
	* This method set the parameters of the distribution and computes the
	* domain.
	* @param m the population size
	* @param r the number of type 1 objects
	* @param n the sample size
	*/
	public void setParameters(int m, int r, int n){
		//Correct for invalid parameters
		if (m < 1) m = 1;
		if (r < 0) r = 0; else if (r > m) r = m;
		if (n < 0) n = 0; else if (n > m) n = m;
		//Assign parameter values
		populationSize = m;
		type1Size = r;
		sampleSize = n;
		c = Functions.comb(populationSize, sampleSize);
		setDomain(Math.max(0, sampleSize - populationSize + type1Size), Math.min(type1Size, sampleSize), 1, DISCRETE);
	}

	/**
	* This method computes the probability density function.
	* The PDF is f(x) = C(r, x) C(m &minus; r, n &minus; x) / C(m, n).
	* @param x an integer between 0 and the sample size
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)Math.rint(x);
		return Functions.comb(type1Size, k) * Functions.comb(populationSize - type1Size, sampleSize - k) / c;
	}

	/**
	* This method computes the aximum value of the probability density function.
	* The mode occurs at (n + 1)(r + 1)/(m + 2).
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		double mode = Math.floor(((double)(sampleSize + 1) * (type1Size + 1)) / (populationSize + 2));
		return getDensity(mode);
	}

	/**
	* This method computes the mean of the distribution, which is n r / m.
	*/
	public double getMean(){
		return (double)sampleSize * type1Size / populationSize;
	}

	/**
	* This method computes the variance, which
	* is given by n (r/m) (1 &minus; r/m)(m &minus; n) / (m &minus; 1).
	* @return the variance
	*/
	public double getVariance(){
		return (double)sampleSize * type1Size * (populationSize - type1Size) *
			(populationSize - sampleSize) / ( populationSize * populationSize * (populationSize - 1));
	}

	/**
	* This method sets population size.
	* @param m the population size
	*/
	public void setPopulationSize(int m){
		setParameters(m, type1Size, sampleSize);
	}

	/**
	* This method gets the population size.
	* @return the population size
	*/
	public int getPopulationSize(){
		return populationSize;
	}

	/**
	* This method sets the number of type 1 elements
	* @param r the number of type 1 elements
	*/
	public void setType1Size(int r){
		setParameters(populationSize, r, sampleSize);
	}

	/**
	* This method gets the number of type 1 elements.
	* @return the number of type 1 elements
	*/
	public int getType1Size(){
		return type1Size;
	}

	/**
	* This method sets the sample size.
	* @param n the sample size
	*/
	public void setSampleSize(int n){
		setParameters(populationSize, type1Size, n);
	}

	/**
	* This method gets the sample size.
	* @return the sample size
	*/
	public int getSampleSize(){
		return sampleSize;
	}

	/**
	* This method simulate a value from the distribution.
	* This is done by simulating a sample of size n without replacment from the
	* population {1, 2, ..., m} and  counting the number of elemets less than r.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		int[] sample = Functions.getSample(populationSize, sampleSize, Functions.WITHOUT_REPLACEMENT);
		int count = 0;
		for (int i = 0; i < sampleSize; i++) if (sample[i] <= type1Size) count++;
		return count;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Hypergeometric distribution [population size = " + populationSize
			+ ", sample size = " + sampleSize + ", type 1 size = " + type1Size + "]";
	}
}

