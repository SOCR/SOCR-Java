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
* This class models the Pareto distribution with a specified parameter.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ParetoDistribution extends Distribution implements Serializable{
	//Variable
	private double parameter;

	/**
	* This general constructor creates a new Pareto distribuiton with a
	* specified parameter.
	* @param a the parameter
	*/
	public ParetoDistribution(double a){
		setParameter(a);
	}

	/**
	* The default constructor creates a new Pareto distribution with parameter 1.
	*/
	public ParetoDistribution(){
		this(1);
	}

	/**
	* This method sets the parameter and computes the default domain.
	* @param a the parameter
	*/
	public void setParameter(double a){
		double upper, width;
		if (a <= 0) a = 1;
		parameter = a;
		if (parameter <= 2) upper = 1 + 6 / parameter;
		else upper = 1 + 4 * getSD();
		width = (upper - 1) / 100;
		setDomain(1, upper, width, CONTINUOUS);
	}

	/**
	* This method returns the parameter.
	*/
	public double getParameter(){
		return parameter;
	}

	/**
	* This method computes the probability density function.
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		if (x < 1) return 0;
		else return parameter / Math.pow(x, parameter + 1);
	}

	/**
	* This method returns the maximum value of the probability density function.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		return parameter;
	}

	/**
	* This method computes the moment of a specified order.
	* @param n the order
	* @return the moment of order n
	*/
	public double getMoment(int n){
		if (parameter > n) return parameter / (parameter - n);
		else return Double.POSITIVE_INFINITY;
	}

	/**
	* This method computes the moment of a specified order about a specified point.
	* @param a the center
	* @param n the order
	* @return the moment of order n about a
	*/
	public double getMoment(double a, int n){
		double sum = 0;
		if(parameter > n){
			for(int k = 0; k <= n; k++) sum = sum + Functions.comb(n, k) * getMoment(k) * Math.pow(-a, n - k);
			return sum;
		}
		else if (n == 2 * n / 2) return Double.POSITIVE_INFINITY;
		else return Double.NaN;
	}

	/**
	* This method returns the moment generating function.
	* @param t a real number
	* @return the moment generating function at t
	*/
	public double getMGF(double t){
		return Double.POSITIVE_INFINITY;
	}

	/**
	* This method computes the variance of the distribution.
	* @return the variance
	*/
	public double getVariance(){
		if (parameter > 2) return parameter / ((parameter - 1) * (parameter - 1) * (parameter - 2));
		else if (parameter > 1) return Double.POSITIVE_INFINITY;
		else return Double.NaN;
	}

	/**
	* This method comptues the cumulative distribution function.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		return 1 - Math.pow(1 / x, parameter);
	}

	/**
	* This method computes the quantile function.
	* @param p a number in (0, 1)
	* @return the quantile of order p
	*/
	public double getQuantile(double p){
		return 1 / Math.pow(1 - p, 1 / parameter);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Pareto distribution [parameter = " + parameter + "]";
	}

}

