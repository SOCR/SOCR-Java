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
* This class defines a simple implementation of an interval data distribution.
* The data distribution is based on a specified domain (that is, a partition of an
* interval). When values are added, frequency counts for the subintervals are computed
* and various statistic updated.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class IntervalData implements Serializable{
	//Variables
	private int size, maxFreq;
	private double value, minValue, maxValue, mean, meanSquare, mode;
	private int[] freq;
	//Objects
	private Domain domain;
	private String name;

	/**
	* This general constructor creates a new data distribution with a specified domain
	* and a specified name.
	* @param d the domain
	* @param n the name
	*/
	public IntervalData(Domain d, String n){
		name = n;
		setDomain(d);
	}

	/**
	* This general constructor creates a new data distribution with a specified
	* (continuous) domain and a	specified name.
	* @param a the lower bound
	* @param b the upper bound
	* @param w the step size
	* @param n the name
	*/
	public IntervalData(double a, double b, double w, String n){
		this(new Domain(a, b, w, 1), n);
	}

	/**
	* This special constructor creates a new data distribution with a specified
	* domain and the default name "X".
	* @param d the domain
	*/
	public IntervalData(Domain d){
		this(d, "X");
	}

	/**
	* This spcial constructor creates a new data distribution with a
	* specified (continuous) domain and	the name "X".
	* @param a the lower bound
	* @param b the upper bound
	* @param w the step size
	*/
	public IntervalData(double a, double b, double w){
		this(a, b, w, "X");
	}

	/**
	* This default constructor creates a new data distribution on the interval
	* [0, 1] with subintervals of length 0.1, and the default name "X".
	*/
	public IntervalData(){
		this(0, 1, 0.1);
	}

	/**
	* This method sets the domain of the data set.
	* @param d the domain
	*/
	public void setDomain(Domain d){
		domain = d;
		reset();
	}

	/**
	* This method returns the domain.
	* @return the domain
	*/
	public Domain getDomain(){
		return domain;
	}

	/**
	* This method sets the name of the data set.
	* @param n the name
	*/
	public void setName(String n){
		name = n;
	}

	/**
	* This method gets the name of the data set.
	* @return the name
	*/
	public String getName(){
		return name;
	}

	/**
	* This method resets the data set. The frequency distribution is cleared
	* and the minimum and maximum values are reset.
	*/
	public void reset(){
		freq = new int[domain.getSize()];
		size = 0;
		minValue = domain.getUpperBound();
		maxValue = domain.getLowerBound();
		maxFreq = 0;
		mean = 0;
		meanSquare = 0;
		value = Double.NaN;
	}

	/**
	* This method adds a new number to the data set and re-computes the mean, mean square,
	* minimum and maximum values, the frequency distribution, and the mode.
	* @param x the number to be added to the data set
	*/
	public void setValue(double x){
		value = x;
		//Update the size of the data set:
		size++;
		//Re-compute mean and mean square
		mean = ((double)(size - 1) / size) * mean + value / size;
		meanSquare = ((double)(size - 1) / size) * meanSquare + value * value / size;
		//Recompute minimum and maximum values
		if (value < minValue) minValue = value;
		if (value > maxValue) maxValue = value;
		//Update frequency distribution
		int i = domain.getIndex(x);
		if (i >= 0 & i < domain.getSize()){
			freq[i]++;
			//Re-compute mode
			if (freq[i] > maxFreq){
				maxFreq = freq[i];
				mode = domain.getValue(i);
			}
			else if (freq[i] == maxFreq) mode = Double.NaN; //There are two or more modes
		}
	}

	/**
	* This method returns the current value of the data set.
	* @return the current (last) value of the data set
	*/
	public double getValue(){
		return value;
	}

	/**
	* This method returns the domain value (midpoint) closest to given value of x.
	* @param x a real number
	* @return the midpoint of the subinterval containing x
	*/
	public double getDomainValue(double x){
		return domain.getValue(domain.getIndex(x));
	}

	/**
	* This method returns the frequency of the class containing a given value.
	* @param x a real number
	* @return the frequency of the subinterval containing x
	*/
	public int getFreq(double x){
		int i = domain.getIndex(x);
		if (i < 0 | i >= domain.getSize()) return 0;
		else return freq[i];
	}

	public int[] getFrequencies(){
		return freq;
	}

	/**
	* This method returns the relative frequency of the class containing
	* a given value.
	* @param x a real number
	* @return the relative frequency of the class containing x
	*/
	public double getRelFreq(double x){
		if (size > 0) return (double)(getFreq(x)) / size;
		else return 0;
	}

	/**
	* This method returns the density of the data distribution at a given value. The
	* density is the relative frequency divided by the width of the subinterval.
	* @param x a real number
	* @return the density at x
	*/
	public double getDensity(double x){
		return getRelFreq(x) / domain.getWidth();
	}

	/**
	* This method returns the true mean of the data set.
	* @return the mean
	*/
	public double getMean(){
		return mean;
	}

	/**
	* This method returns the mean of the frequency distribution.  The interval
	* mean is an approximation of the true mean of the data set, and is equivalent
	* to rounding each value in the data set to the nearest interval midpoint.
	* @return the interval mean
	*/
	public double getIntervalMean(){
		double sum = 0;
		for (int i = 0; i < domain.getSize(); i++) sum = sum + domain.getValue(i) * freq[i];
		return sum / size;
	}

	/**
	* This method returns the variance of the data set treated as a population (that is,
	* the sum is divided by the number of points n rather than n &minus; 1).
	* @return the variance
	*/
	public double getVarianceP(){
		double var = meanSquare - mean * mean;
		if (var < 0) var = 0;
		return var;
	}

	/**
	* This method returns the standard deviation of the data set treated as
	* a population (that is, the sum is divided by the number of points n rather
	* than n &minus; 1).
	*/
	public double getSDP(){
		return Math.sqrt(getVarianceP());
	}

	/**
	* This method returns the variance of the data set treated as a sample (that is,
	* the sum is divided by n &minus; 1 where n is the number of points).
	* @return the variance.
	*/
	public double getVariance(){
		if (size > 0) return ((double)size / (size - 1)) * getVarianceP();
		else return 0;
	}

	/**
	* This method returns the standard deviation of the data set treated as a
	* sample (that is, the sum is divided by n &minus; 1 where n is the number
	* of points.
	* @return the standard deviation
	*/
	public double getSD(){
		return Math.sqrt(getVariance());
	}

	/**
	* This method returns the variance of the frequency distribution. This is
	* an approximation to the true variance, and is equivalent to rounding each
	* value in the data set to the nearest interval midpoint.
	* @return the interval variance
	*/
	public double getIntervalVariance(){
		double m = getIntervalMean(), sum = 0, x;
		for (int i = 0; i < domain.getSize(); i++){
			x = domain.getValue(i);
			sum = sum + (x - m) * (x - m) * freq[i];
		}
		return sum / size;
	}

	/**
	* This method returns the interval standard deviation. This is an
	* approximation to the true standard deviation, and is equivalent to
	* rounding each value in the data set to the nearest interval midpoint.
	* @return the interval standard deviation
	*/
	public double getIntervalSD(){
		return Math.sqrt(getIntervalVariance());
	}

	/**
	* This method returns the minimum value of the data set.
	* @return the minimum value
	*/
	public double getMinValue(){
		return minValue;
	}

	/**
	* This method returns the maximum value of the data set.
	* @return the maximum value
	*/
	public double getMaxValue(){
		return maxValue;
	}

	/**
	* This method computes the median of the values in the data set between two
	* specified values.
	* @param a the lower value
	* @param b the upper value
	* @return the median of the values between a and b
	*/
	public double getMedian(double a, double b){
		int sumFreq = 0, numValues = 0, lRank, uRank;
		double lValue = a - 1, uValue = b + 1, w = domain.getWidth();
		//Compute sum of frequencies between a and b
		for (double x = a; x <= b + 0.5 * w; x = x + w) numValues = numValues + getFreq(x);
		//Determine parity and ranks
		if (2 * (numValues / 2) == numValues) {
			lRank = numValues / 2;
			uRank = lRank + 1;
		}
		else {
			lRank = (numValues + 1) / 2;
			uRank = lRank;
		}
		//Determine values
		for (double x = a; x <= b + 0.5 * w; x = x + w) {
			sumFreq = sumFreq + getFreq(x);
			if ((lValue == a - 1) & (sumFreq >= lRank)) lValue = x;
			if ((uValue == b + 1) & (sumFreq >= uRank)) uValue = x;
		}
		//Return average of upper and lower values
		return (uValue + lValue) / 2;
	}

	/**
	* This method computes the median of the entire data set.
	* @return the median
	*/
	public double getMedian(){
		return getMedian(domain.getLowerValue(), domain.getUpperValue());
	}

	/**
	* This method returns the quartiles of the data set.
	* @param i the index of the quartile (1, 2, or 3)
	* @return the quartile of index i
	*/
	public double getQuartile(int i){
		if (i < 1) i = 1; else if (i > 3) i = 3;
		if (i == 1) return getMedian(domain.getLowerValue(), getMedian());
		else if (i == 2) return getMedian();
		else return getMedian(getMedian(), domain.getUpperValue());
	}

	/**
	* This method computes the mean absoulte deviation from the median.
	* @return the mean absoluted deviation
	*/
	public double getMAD(){
		double mad = 0, x;
		double m = getMedian();
		for (int i = 0; i < domain.getSize(); i++){
			x = domain.getValue(i);
			mad = mad + getRelFreq(x) * Math.abs(x - m);
		}
		return mad;
	}

	/**
	* This method returns the number of points in the data set.
	* @return the size of hte data set.
	*/
	public int getSize(){
		return size;
	}

	/**
	* This method returns the maximum frequency of the data set.
	* @return the maximum frequency
	*/
	public int getMaxFreq(){
		return maxFreq;
	}

	/**
	* This method returns the maximum relative frequency of the data set.
	* @return the maximum relative frequency
	*/
	public double getMaxRelFreq(){
		if (size > 0) return (double)maxFreq / size;
		else return 0;
	}

	/**
	* This method returns the maximum density of the data set.
	* @return the maximum density
	*/
	public double getMaxDensity(){
		return getMaxRelFreq() / domain.getWidth();
	}

	/**
	* This method returns the mode of the distribution, if it is unique.
	* Otherwise, the mode does not exist. The mode is the midpoint of the
	* interval with the largest frequency.
	* @return the mode
	*/
	public double getMode(){
		return mode;
	}
}

