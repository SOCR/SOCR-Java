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
* This class applies a location-scale tranformation to a given distribution. In terms of
* the corresponding random variable X, the transformation is Y = a + bX.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class LocationScaleDistribution extends Distribution implements Serializable{
	private Distribution distribution;
	private double location, scale;

	/**
	* This general constructor creates a new location-scale transformation on
	* a given distribuiton with given location and scale parameters.
	* @param d the distribution
	* @param a the location parameter
	* @param b the scale parameter
	*/
	public LocationScaleDistribution(Distribution d, double a, double b){
		setParameters(d, a, b);
	}

	/**
	* This default constructor creates a new location-scale distribution
	* on the normal distribution with location parameter 0 and scale
	* parameter 1. Of course, this is simply the standard normal distribution.
	*/
	public LocationScaleDistribution(){
		this(new NormalDistribution(), 0, 1);
	}

	/**
	* This method sets the parameters, the distribution and the location and
	* scale parameters, and sets up the domain.
	* @param d the distribution
	* @param a the location parameter
	* @param b the scale parameter
	*/
	public void setParameters(Distribution d, double a, double b){
		distribution = d; location = a; scale = b;
		Domain domain = distribution.getDomain();
		double l, u, w = domain.getWidth();
		int t = distribution.getType();
		if (t == DISCRETE){
			l = domain.getLowerValue(); u = domain.getUpperValue();
		}
		else{
			l = domain.getLowerBound(); u = domain.getUpperBound();
		}
		if (scale == 0) setDomain(location, location, 1, DISCRETE);
		else if (scale < 0) setDomain(location + scale * u, location + scale * l, -scale * w, t);
		else setDomain(location + scale * l, location + scale * u, scale * w, t);
	}

	/**
	* This method computes the probability density function of the
	* location-scale distribution in terms of the location
	* and scale parameters and the density function of the given distribution.
	* @param x a number in the domain of the distribution
	* @return the probability density function of x.
	*/
	public double getDensity(double x){
		if (getType() == DISCRETE){
			if (scale == 0){
				if (x == location) return 1;
				else return 0;
			}
			else return distribution.getDensity((x - location) / scale);
		}
		else return distribution.getDensity((x - location) / scale) * Math.abs(1 / scale);
	}

	/**
	* This method returns the maximum value of the probability density function of the
	* location-scale distribution, which is the same as the maximum value of the
	* probability density function of the given distribution.
	* @return the maximum value of the probability density function
	*/
	public double getMaxDensity(){
		return distribution.getMaxDensity();
	}

	/**
	* This mtehod computes the mean of the location-scale distribution in terms
	* of the mean of the given distribution and the location and scale parameters.
	* @return the mean of the distribution
	*/
	public double getMean(){
		return location + scale * distribution.getMean();
	}

	/**
	* This method returns the variance of the location-scale distribution
	* in terms of the given distribution and the location and scale parameters.
	* @return the variance of the distribution
	*/
	public double getVariance(){
		return (scale * scale) * distribution.getVariance();
	}

	/**
	* This method returns a simulated value from the location-scale distribution
	* in terms of the given distribution and the location and scale parameters.
	*/
	public double simulate(){
		return location + scale * distribution.simulate();
	}

	/**
	* This method returns the cumulative distribution function of the location-
	* scale distribution in terms of the CDF of the given distribution and the
	* location and scale parameters.
	* @param x a number in the domain of the distribution
	* @return the cumulative probability at x
	*/
	public double getCDF(double x){
		if (scale > 0) return distribution.getCDF((x - location) / scale);
		else return 1 - distribution.getCDF((x - location) / scale);
	}

	/**
	* This method returns the quantile function of the location-scale
	* distribution in terms of the quantile function of the given distribution
	* and the location and scale parameters.
	* @param p a probability in (0, 1)
	* @return the quanitle of order p
	*/
	public double getQuantile(double p){
		if (scale > 0) return location + scale * distribution.getQuantile(p);
		else return location + scale * distribution.getQuantile(1 - p);
	}

	/**
	* This method sets the location parameter.
	* @param a the location parameter
	*/
	public void setLocation(double a){
		setParameters(distribution, a, scale);
	}

	/**
	* This method returns the location parameter.
	* @return the location parameter
	*/
	public double getLocation(){
		return location;
	}

	/**
	* This method sets the scale parameter.
	* @param b the scale parameter
	*/
	public void setScale(double b){
		setParameters(distribution, location, b);
	}

	/**
	* This method gets the scale parameter
	* @return the scale parameter
	*/
	public double getScale(){
		return scale;
	}

	/**
	* This method sets the distribution to be moved and scaled.
	* @param d the distribution
	*/
	public void setDistribution(Distribution d){
		setParameters(d, location, scale);
	}

	/**
	* This method gets the underlying distribution that is being moved and scaled.
	* @return the distribution
	*/
	public Distribution getDistribution(){
		return distribution;
	}

	/**
	* This method returns a string that gives the name of the distribution and the values of
	* the parameters.
	* @return a string giving the name of the distribution and the values of the parameters
	*/
	public String toString(){
		return "Location-scale distribution [basic distribution = " + distribution
			+ ", location = " + location + ", scale = " + scale + "]";
	}
}

