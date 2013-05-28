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
package edu.uah.math.devices;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import edu.uah.math.distributions.RandomVariable;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.Domain;

/**
* This class defines a basic graph for displaying the distribution
* density and moments, and the data density and moments for a specified
* random variable.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class RandomVariableGraph extends DistributionGraph{
	//Constants
	public static final int PDFS = 3, MSDS = 2;
	//Objects
 	private RandomVariable randomVariable;
 	private IntervalData data;
 	private Color dataColor = Color.red;

	/**
	* This general constructor creates a new random varaible graph with a
	* specified random variable.
	* @param rv the random variable
	*/
	public RandomVariableGraph(RandomVariable rv){
		setRandomVariable(rv);
		setFunctionType(PDFS);
		setMomentType(MSDS);
	}

	/**
	* This default constructor creates a new random variable graph with a
	* normally distributed random variable.
	*/
	public RandomVariableGraph(){
		this(new RandomVariable());
	}

	/**
	* This method paints the graph of the density function, empirical density
	* function, moment bar, and empirical moment bar.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int t = getFunctionType(), m = getMomentType();
		if (t == PDFS){
			g.setColor(dataColor);
			drawDataPDF(g);
			g.setColor(getDistributionColor());
			drawDistributionPDF(g);
		}
		if (m == MSDS){
			g.setColor(dataColor);
			drawDataMSD(g);
			g.setColor(getDistributionColor());
			drawDistributionMSD(g);
		}
	}

	/**
	* This method draws the density function of the data.
	* @param g the graphics context
	*/
	public void drawDataPDF(Graphics g){
		Domain domain = getDomain();
		double width = domain.getWidth(), x, d;
		if (data.getSize() > 0){
			for (int i = 0; i < domain.getSize(); i++){
				x = domain.getValue(i);
				if (domain.getType() == Domain.DISCRETE) d = data.getRelFreq(x);
				else d = data.getDensity(x);
				fillBox(g, x - width / 2, 0, x + width / 2, d);
			}
		}
	}

	/**
	* This method draws the mean-standard deviation bar for the data.
	* @param g the Graphics context
	*/
	public void drawDataMSD(Graphics g){
		int j = getSize().height - 10;
		if (data.getSize() > 0) fillBoxPlot(g, data.getMean(), data.getSD(), j);
	}

	/**
	* This method specifies the random variable and sets up graph paramters.
	* @param rv the random variable
	*/
	public void setRandomVariable(RandomVariable rv){
		randomVariable = rv;
		reset();
	}

	/**
	* This method resets the graph. This method must be called whenever
	* the distribution of the random variable changes.
	*/
	public void reset(){
		setDistribution(randomVariable.getDistribution());
		setToolTipText(randomVariable.getName() + " Distribution");
		data = randomVariable.getIntervalData();
	}

	/**
	* This method returns the random variable associated with the graph.
	* @return the random varaible
	*/
	public RandomVariable getRandomVariable(){
		return randomVariable;
	}

	/**
	* This method sets the color for the data graph elements.
	* @param c the data color
	*/
	public void setDataColor(Color c){
		dataColor = c;
	}

	/**
	* This method returns the color for the data graph elements.
	* @return the data color
	*/
	public Color getDataColor(){
		return dataColor;
	}
}

