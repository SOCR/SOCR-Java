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
* This class models the distribution of the number of matches in a random
* permutation.  A match occurs whenever an element is in its natural order.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MatchDistribution extends Distribution implements Serializable{
	private int parameter;

	/**
	* This general constructor creates a new matching distribution with a
	* specified parameter.
	* @param n the number of elements
	*/
	public MatchDistribution(int n){
		setParameter(n);
	}

	/**
	* This default constructor creates a new mathcing distribuiton with
	* parameter 5.
	*/
	public MatchDistribution(){
		this(5);
	}

	/**
	* This method sets the parameter of the distribution.
	* @param n the size of the random permutation
	*/
	public void setParameter(int n){
		if (n < 1) n = 1;
		parameter = n;
		setDomain(0, parameter, 1, DISCRETE);
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain {0, 1, 2, ...}
	* @return the probability density at x
	*/
	public double getDensity(double x){
		int k = (int)Math.rint(x);
		double sum = 0;
		int sign = -1;
		for (int j = 0; j <= parameter - k; j++){
			sign = -sign;
			sum = sum + sign / Functions.factorial(j);
		}
		return sum / Functions.factorial(k);
	}

	/**
	* This method gives the maximum value of the probability density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		if (parameter == 2) return getDensity(0);
		else return getDensity(1);
	}

	/**
	* This method returns the mean, which is 1, regardless of the parameter
	* value.
	* @return the mean of the distribution
	*/
	public double getMean(){
		return 1;
	}

	/**
	* This method returns the variance, which is 1 regardless of the parameter
	* value.
	* @return the variance of the distribution
	*/
	public double getVariance(){
		return 1;
	}

	/**
	* This method gets the parameter.
	* @return the size of the random permutation
	*/
	public int getParameter(){
		return parameter;
	}

	/**
	* This method simulates a value from the distribution, by generating
	* a random permutation and computing the number of matches.
	*/
	public double simulate(){
		int[] p = Functions.getSample(parameter, parameter, Functions.WITHOUT_REPLACEMENT);
		int matches = 0;
		for (int i = 0; i < parameter; i++) if (p[i] == i + 1) matches++;
		return matches;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Matching distribution [parameter = " + parameter + "]";
	}
}

