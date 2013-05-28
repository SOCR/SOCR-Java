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
import java.util.Vector;
import java.io.Serializable;

/**
* This class models a simple implementation of a data distribution.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Data implements Serializable{
	//Variables
	private Vector<Double> values = new Vector<Double>(), orderStatistics = new Vector<Double>();
	private int size;
	private double value, mean, meanSquare, mode;
	private String name;

	/**
	* This general constructor creates a new data set with a prescribed name.
	* @param n the name
	*/
	public Data(String n){
		setName(n);
	}

	/**
	* This default constructor creates a new data with the name "X".
	*/
	public Data(){
		this("X");
	}

	/**
	* This method adds a new number to the data set and re-compute the mean, mean square,
	* minimum and maximum values, and order statistics.
	* @param x the new value of the data set
	*/
	public void setValue(double x){
		//Add the value to the data set
		values.addElement(new Double(x));
		value = x;
		//Re-compute the order statistics
		boolean notInserted = true;
		//Add the value to the data set
		double a, b;
		for (int i = 0; i < size - 1; i++){
			a = ((Double)orderStatistics.elementAt(i)).doubleValue();
			b = ((Double)orderStatistics.elementAt(i + 1)).doubleValue();
			if ((a <= x) & (x <= b)){
				orderStatistics.insertElementAt(new Double(x), i + 1);
				notInserted = false;
			}
		}
		if (notInserted) values.insertElementAt(new Double(x), 0);
		size++;
		//Re-compute mean and mean square
		mean = ((double)(size - 1) / size) * mean + value / size;
		meanSquare = ((double)(size - 1) / size) * meanSquare + value * value / size;
	}

	/**
	* This method gets the current value of the data set.
	* @return the current (last) value of the data set
	*/
	public double getValue(){
		return value;
	}

	/**
	* This method returns the value of the data set corresponding to a given index.
	* @param i the index
	* @return the value corresponding to the index
	*/
	public double getValue(int i){
		return ((Double)values.elementAt(i)).doubleValue();
	}

	/**
	* This method returns the mean.
	* @return the mean
	*/
	public double getMean(){
		return mean;
	}

	/**
	* This method returns the variance of the data set treated as a population.
	* @return the variance
	*/
	public double getPVariance(){
		double var = meanSquare - mean * mean;
		if (var < 0) var = 0;
		return var;
	}

	/**
	* This method gets the standard deviation of the data set treated as a population.
	* @return the standard deviation
	*/
	public double getPSD(){
		return Math.sqrt(getPVariance());
	}

	/**
	* This method gets the variance of the data set treated as a sample.
	* @return the variance
	*/
	public double getVariance(){
		return ((double)size / (size - 1)) * getPVariance();
	}

	/**
	* This method gets the sample standard deviation of the data set treated as
	* a sample.
	* @return the standard deviation
	*/
	public double getSD(){
		return Math.sqrt(getVariance());
	}

	/**
	* This method returns the i'th order statistic.
	* @param i the index
	* @return the  order statistic of index i;
	*/
	public double getOrderStatistic(int i){
		if (i < 0) i = 0;
		else if (i > size - 1) i = size - 1;
		return ((Double)orderStatistics.elementAt(i)).doubleValue();
	}

	/**
	* This method gets the minimum value of the data set.
	* @return the minimum value of the data set
	*/
	public double getMinValue(){
		return getOrderStatistic(0);
	}

	/**
	* This method gets the maximum value of the data set.
	* @return the maximum value of the data set.
	*/
	public double getMaxValue(){
		return getOrderStatistic(size - 1);
	}

	/**
	* This method resets the data set by removing all values.
	*/
	public void reset(){
		values.removeAllElements();
		orderStatistics.removeAllElements();
		size = 0;
	}

	/**
	* This method gets the number of points in the data set.
	* @return the size of the data set
	*/
	public int getSize(){
		return size;
	}

	/**
	* This method sets the name of the data set.
	* @param n the name of the data set
	*/
	public void setName(String n){
		name = n;
	}

	/**
	* This method gets the name of the data set.
	* return the name of the data set
	*/
	public String getName(){
		return name;
	}
}

