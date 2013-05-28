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
* This class models the Cauchy distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CauchyDistribution extends StudentDistribution implements Serializable{
	//Constructor
	public CauchyDistribution(){
		super(1);
	}

	/**
	* This method computes the cumulative distribuiton function in closed form.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		return 0.5 + Math.atan(x) / Math.PI;
	}

	/**
	* This method computes the quantile function in closed form.
	* @param p a probability in (0, 1)
	* @return the quantile of order p
	*/
	public double getQuantile(double p){
		return Math.tan(Math.PI * (p - 0.5));
	}

	/**
	* This method sets the degrees of freedom which is fixed at 1.
	* @param n the degrees of freedom
	*/
	public void setDegrees(int n){
		super.setDegrees(1);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Cauchy distribution";
	}
}

