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
* This class models the discrete uniform distribution on a finite set.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DiscreteUniformDistribution extends FiniteDistribution implements Serializable{

	/**
	* This general constructor creates a new discrete uniform distribution
	* on a specified domain.
	* @param a the lower value of the domain
	* @param b the upper value of the domain
	* @param w the  step size of the domain
	*/
	public DiscreteUniformDistribution(double a, double b, double w){
		super(a, b, w);
	}

	/**
	* This default constructor creates a new discrete uniform distribution
	* on {1, 2, 3, 4, 5, 6}.
	*/
	public DiscreteUniformDistribution(){
		this(1, 6, 1);
	}

	/**
	* This method simulates a value from the distribution.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		Domain d = getDomain();
		double a = d.getLowerValue(), b = d.getUpperValue();
		double x = a + Math.random() * (b - a);
		return d.getNearestValue(x);
	}

	/**
	* This method sets the probabilities to ensure that the uniform
	* distribution is not changed.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){}

	/**
	* This method sets the finite distribution parameters to ensure that
	* the uniform distribution is not changed.
	* @param a the lower value
	* @param b the upper value
	* @param w the width
	* @param p the array of probabilities
	*/
	public void setParameters(double a, double b, double w, double[] p){
		setParameters(a, b, w);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Discrete uniform distribution [domain = " + getDomain() + "]";
	}

}

