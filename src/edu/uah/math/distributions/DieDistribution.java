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
* This class models the distribution for a standard 6-sided die.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class DieDistribution extends FiniteDistribution implements Serializable{
	public final static int FAIR = 0, FLAT16 = 1, FLAT25 = 2, FLAT34 = 3, LEFT = 4, RIGHT = 5;

	/**
	* This general constructor creates a new die distribution with specified
	* probabilities.
	* @param p the array of probabilities
	*/
	public DieDistribution(double[] p){
		super(1, 6, 1, p);
	}

	/**
	* This special constructor creates a new die distribution of a special type (1-6 flat,
	* 2-5 flat, 3-4 flat, skewed left, skewed right, or fair).
	* @param n the type of distribution
	*/
	public DieDistribution(int n){
		super(1, 6, 1);
		setProbabilities(n);
	}

	/**
	* This default constructor creates a new fair die distribution.
	*/
	public DieDistribution(){
		this(FAIR);
	}

	/**
	* This method specifies the probabilities for the special types (fair, 1-6 flat,
	* 2-5 flat, 3-4 flat, skewed left, or skewed right).
	* @param n the type of distribution
	*/
	public void setProbabilities(int n){
		if (n == FLAT16)
			setProbabilities(new double[] {1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 4});
		else if (n == FLAT25)
			setProbabilities(new double[] {1.0 / 8, 1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 8});
		else if (n == FLAT34)
			setProbabilities(new double[] {1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 8, 1.0 / 8});
		else if (n == LEFT)
			setProbabilities(new double[] {1.0 / 21, 2.0 / 21, 3.0 / 21, 4.0 / 21, 5.0 / 21, 6.0 / 21});
		else if (n == RIGHT)
			setProbabilities(new double[] {6.0 / 21, 5.0 / 21, 4.0 / 21, 3.0 / 21, 2.0 / 21, 1.0 / 21});
		else
			setProbabilities(new double[] {1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6});
	}

	/**
	* This method sets the probabilities. The length of the array must be 6.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		if (p.length == 6) super.setProbabilities(p);
	}

	/**
	* This method ensures that the finite distribution parameters are not changed
	* to inappropriate values.
	*/
	public void setParameters(double a, double b, double w, double[] p){
		super.setParameters(1, 6, 1, p);
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Die distribution [probabilities = " + getProbabilities() + "]";
	}
}

