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

/**
* This class models a random variable in terms of a distribution and an
* interval dataset. The dataset holds a random sample from the distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RandomVariable{
	private Distribution distribution;
	private IntervalData intervalData;
	private String name;

	/**
	* This general constructor creates a new random variable with a specified
	* probability distribution and name.
	* @param d the probability distribution
	* @param n the name of the variable
	*/
	public RandomVariable(Distribution d, String n){
		distribution = d;
		name = n;
		intervalData = new IntervalData(distribution.getDomain(), name);

	}

	/**
	* This special constructor creates a new random variable with a specified
	* probability distribution and the default name "X".
	* @param d the probability distribution
	*/
	public RandomVariable(Distribution d){
		this(d, "X");
	}

	/**
	* This default constructor creates a new random variable with a normal
	* distribution and with the default name "X".
	*/
	public RandomVariable(){
		this(new NormalDistribution());
	}

	/**
	* This method assigns the probability distribution and create a corresponding
	* interval data distribution.
	* @param d the probability distribution
	*/
	public void setDistribution(Distribution d){
		distribution = d;
		intervalData.setDomain(distribution.getDomain());
	}

	/**
	* This method gets the probability distribution.
	* @return the probability distribution
	*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**
	* Get the data distribution.
	* @return the interval data distribution
	*/
	public IntervalData getIntervalData(){
		return intervalData;
	}

	/**
	* This method assigns a value to the random variable.
	*/
	public void setValue(double x){
		intervalData.setValue(x);
	}

	/**
	* This method gets the current value of the random variable.
	* @return the current value of the random variable
	*/
	public double getValue(){
		return intervalData.getValue();
	}

	/**
	* This method simulates a value of the probability distribution and assigns the value
	* to the data distribution.
	*/
	public void sample(){
		intervalData.setValue(distribution.simulate());
	}

	/**
	* This method simulates a value of the probability distribution, assigns the value
	* to the data distribution, and returns the value.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		double x = distribution.simulate();
		intervalData.setValue(x);
		return x;
	}

	/**
	* This method resets the data distribution.
	*/
	public void reset(){
		intervalData.setDomain(distribution.getDomain());
	}

	/**
	* This method gets the name of the random variable.
	* @return the name of the variable
	*/
	public String getName(){
		return name;
	}

	/**
	* This method assign a name to the random variable.
	* @param n the name of the random variable
	*/
	public void setName(String n){
		name = n;
		intervalData.setName(name);
	}
}

