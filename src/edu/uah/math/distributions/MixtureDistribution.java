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
* This class models a distributions which is the mixture of a given set of
* distributions using a given set of probabilities as the mixing parameters
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class MixtureDistribution extends Distribution implements Serializable{
	Distribution[] distributions;
	int n, type;
	double[] probabilities;

	/**
	* This general constructor creates the mixture of a given array
	* of distribuitons using a given array of probabilities as the
	* mixing parameters.
	* @param d the array of distributions to be mixed
	* @param p the array of mixing probabilities
	*/
	public MixtureDistribution(Distribution[] d, double[] p){
		setParameters(d, p);
	}

	/**
	* This special constructor creates the mixture of two distributions
	* using a specified number and its complement as the mixing probabilities.
	* @param d0 the index 0 distributions
	* @param d1 the index 1 distributions
	* @param a the index 1 mixing parameter (the index 0 parameter is 1 &minus; a)
	*/
	public MixtureDistribution(Distribution d0, Distribution d1, double a){
		setParameters(d0, d1, a);
	}

	/**
	* This special constructor creates the mixture of two distributions with equal mixing
	* probabilities
	* @param d0 the index 0 distribution
	* @param d1 the index 1 distribution
	*/
	public MixtureDistribution(Distribution d0, Distribution d1){
		this(d0, d0, 0.5);
	}

	/**
	* This default constructor creates the mixture of two standard normal distributions
	* with equal mixing parameters. The result, of course, is simply another standard
	* normal distribution.
	*/
	public MixtureDistribution(){
		this(new NormalDistribution(0, 1), new NormalDistribution(0, 1));
	}

	/**
	* This method sets up the domain of the general mixture distributions in terms of the
	* distributions being mixed.
	* @param d the array of distributions being mixed
	* @param p the array of mixing probabilities
	*/
	public void setParameters(Distribution[] d, double[] p){
		double minLower = Double.POSITIVE_INFINITY, maxUpper = Double.NEGATIVE_INFINITY, minWidth = Double.POSITIVE_INFINITY;
		double a, b, w;
		Domain domain;
		distributions = d;
		int t0 = distributions[0].getType(), t;
		n = distributions.length;
		boolean mixed = false;
		for (int i = 0; i < n; i++){
			domain = distributions[i].getDomain();
			t = distributions[i].getType();
			if (t == DISCRETE) a = domain.getLowerValue(); else a = domain.getLowerBound();
			if (a < minLower) minLower = a;
			if (t == DISCRETE) b = domain.getUpperValue(); else b = domain.getUpperBound();
			if (b > maxUpper) maxUpper = b;
			w = domain.getWidth();
			if (w < minWidth) minWidth = w;
			if (t != t0) mixed = true;
		}
		if (mixed) t = 2; else t = t0;
		setDomain(minLower, maxUpper, minWidth, t);
		//Assign probabilities. Correct errors if necessary
		if (p.length != n){
			p = new double[n];
			for (int i = 0; i < n; i++) p[i] = 1.0 / n;
		}
		else probabilities = Functions.getProbabilities(p);
	}

	/**
	* This method sets up the domain of for the mixture of two distributions.
	* @param d0 the index 0 distributions
	* @param d1 the index 1 distributions
	* @param a the index 1 mixing parameter (the index 0 parameter is 1 &minus; a)
	*/
	public void setParameters(Distribution d0, Distribution d1, double a){
		setParameters(new Distribution[]{d0, d1}, new double[]{1 - a, a});
	}

	/**
	* This method computes the density function of the mixture distributions
	* as a linear combination of the densities of the given distributions
	* using the mixing probabilities.
	* @param x a number in the domain of the distributions
	* @return the probability density at x
	*/
	public double getDensity(double x){
		double d = 0;
		for (int i = 0; i < n; i++) d = d + probabilities[i] * distributions[i].getDensity(x);
		return d;
	}

	/**
	* This method computes the cumulative distributions function of the
	* mixture distributions as a linear combination of the CDFs of the
	* given distributions, using the mixing probabilities.
	*/
	public double getCDF(double x){
		double sum = 0;
		for (int i = 0; i < n; i++) sum = sum + probabilities[i] * distributions[i].getCDF(x);
		return sum;
	}

	/**
	* This method computes the mean of the mixture distributions as a linear
	* combination of the means of the given distributions, using the mixing
	* probabilities.
	* @return the mean
	*/
	public double getMean(){
		double sum = 0;
		for (int i = 0; i < n; i++) sum = sum + probabilities[i] * distributions[i].getMean();
		return sum;
	}

	/**
	* This method computes the variance of the mixture distributions in terms
	* of the variances and means of the given distributions and the mixing
	* parameters.
	* @return the variance
	*/
	public double getVariance(){
		double sum = 0, mu = getMean(), m;
		for (int i = 0; i < n; i++){
			m = distributions[i].getMean();
			sum = sum + probabilities[i] * (distributions[i].getVariance() + m * m);
		}
		return sum - mu * mu;
	}

	/**
	* This method simulates a value from the mixture distributions. This is done
	* by selecting an index at random, according to the mixing parameters, and
	* then simulating a value from the randomly chosen distributions.
	*/
	public double simulate(){
		double sum = 0, p = Math.random();
		int i = -1;
		while (sum < p & i < n){
			sum = sum + probabilities[i];
			i = i + 1;
		}
		return distributions[i].simulate();
	}

	/**
	* This method sets the distributions.
	* @param d the array of distributions
	*/
	public void setDistributions(Distribution[] d){
		setParameters(d, probabilities);
	}

	/**
	* This method sets a particular distribution.
	* @param i the index
	* @param d the distribution
	*/
	public void setDistributions(int i, Distribution d){
		if (i < 0) i = 0; else if (i > n - 1) i = n - 1;
		distributions[i] = d;
		setParameters(distributions, probabilities);
	}

	/**
	* This method returns the array of distributions.
	* @return the array of distributions
	*/
	public Distribution[] getDistributions(){
		return distributions;
	}

	/**
	* This method returns a particular distribution.
	* @param i the index
	* @return the distribution corresponding to the index
	*/
	public Distribution getDistributions(int i){
		if (i < 0) i = 0; else if (i > n - 1) i = n - 1;
		return distributions[i];
	}

	/**
	* This method sets the probabilities.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		setParameters(distributions, p);
	}

	/**
	* This method sets a particular probability.
	* @param i the index
	* @param p the probability
	*/
	public void setProbabilities(int i, double p){
		if (i < 0) i = 0; else if (i > n - 1) i = n - 1;
		probabilities[i] = p;
		setParameters(distributions, probabilities);
	}

	/**
	* This method returns the array of probabilities.
	* @return the array of distributions
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method returns a particular probability .
	* @param i the index
	* @return the probability corresponding to the index
	*/
	public double getProbability(int i){
		if (i < 0) i = 0; else if (i > n - 1) i = n - 1;
		return probabilities[i];
	}
}

