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
* This class models the crcle distribution with a specified radius.  This is the
* distribution of X and Y when (X, Y) has the uniform distribution on a circular region
* centered at the origin, with the given radius.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class CircleDistribution extends Distribution implements Serializable{
	private double radius;

	/**
	* This general constructor creates a new circle distribution with a specified radius.
	* @param r the radius
	*/
	public CircleDistribution(double r){
		setRadius(r);
	}

	/**
	* This default constructor creates a new circle distribution with radius 1.
	*/
	public CircleDistribution(){
		this(1);
	}

	/**
	* This method sets the radius parameter and computes the domain of the distribution.
	* @param r the radius
	*/
	public void setRadius(double r){
		if (r <= 0) r =1;
		radius = r;
		setDomain(-radius, radius, 0.02 * radius, CONTINUOUS);
	}

	/**
	* This method computes the probability density function,
	* @param x a number in the domain of the distribution
	* @return the probability density at x
	*/
	public double getDensity(double x){
		if (-radius <= x & x <= radius)
			return 2 * Math.sqrt(radius * radius - x * x) / (Math.PI * radius * radius);
		else return 0;
	}

	/**
	* This method computes the maximum value of the density function, which is the
	* value at 0.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		return getDensity(0);
	}

	/**
	* This method computes the mean in terms of the radius parameter.
	* @return the mean
	*/
	public double getMean(){
		return 0;
	}

	/**
	* This method computes the variance in terms of the radius parameter.
	* @return the variance
	*/
	public double getVariance(){
		return radius * radius / 4;
	}

	/**
	* This method computes the median which is 0 regardless of the radius parameter.
	* @return the median
	*/
	public double getMedian(){
		return 0;
	}

	/**
	* This method returns the radius parameter.
	* @return the radius
	*/
	public double getRadius(){
		return radius;
	}

	/**
	* This method simulates a value from the distribution. This is done by simulating a
	* point chosen uniformly in the circle of the specified radius, centered at the
	* origin, and then computing the x-coordinate of the point.
	* @return a simulated value from the distribution
	*/
	public double simulate(){
		double u = radius * Math.random();
		double v = radius * Math.random();
		double r = Math.max(u, v);
		double theta = 2 * Math.PI * Math.random();
		return r * Math.cos(theta);
	}

	/**
	* This method compute the cumulative distribution functionin in terms of
	* the radius parameter.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		return 0.5 + Math.asin(x / radius) / Math.PI
			+ x * Math.sqrt(1 - x * x / (radius * radius)) / (Math.PI * radius);
	}

	/**
	* This method computes the quantile function in terms of the radius parameter.
	* @param p a probability in (0, 1)
	* @return the quantile of order p
	*/
	public double getQuantile(double p){
		if (p <= 0) return -radius;
		else if (p >= 1) return radius;
		else return radius * Math.sin(Math.PI * (p - 0.5));
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Circle distribution [radius = " + radius + "]";
	}
}

