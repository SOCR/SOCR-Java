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
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.io.Serializable;
import edu.uah.math.distributions.Distribution;
import edu.uah.math.distributions.NormalDistribution;
import edu.uah.math.distributions.Domain;

/**
* This class shows the density function or cumulative distribution function of
* a specified distribuiton. A specified value and quantile are shown.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class QuantileGraph extends DistributionGraph{
	//Constants
	private double quantile, probability;
	private Color quantileColor = Color.red;

	/**
	* This general constructor creates a new quantile graph that shows a specified
	* distribution and quanitle.
	* @param d the distribution
	* @param x the quantile
	*/
	public QuantileGraph(Distribution d, double x){
		setMargins(35, 20, 20, 20);
		setDistribution(d);
		setQuantile(x);
		setMomentType(NONE);
	}

	/**
	* This special constructor creates a new quantile graph that shows a specified
	* distribution and the median (quanitle of order 0.5).
	* @param d the distribution
	*/
	public QuantileGraph(Distribution d){
		this(d, d.getMedian());
	}

	/**
	* This default constructor creates a new quantile graph that shows the standard
	* normal distribution and the median.
	*/
	public QuantileGraph(){
		this(new NormalDistribution(0, 1), 0);
	}

	/**
	* This method paints the graph.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		double x1, x2, y1, y2;
		Domain domain = getDomain();
		Distribution distribution = getDistribution();
		double width = domain.getWidth();
		Polygon polygon;
		//Draw graph
		//Continous PDF
		g.setColor(quantileColor);
		if ((distribution.getType() == distribution.CONTINUOUS) & (getFunctionType() == PDF)){
			for (int i = 0; i < domain.getSize(); i++){
				x1 = domain.getBound(i);
				x2 = x1 + width;
				y1 = distribution.getDensity(x1);
				y2 = distribution.getDensity(x2);
				if (x2 <= quantile){
					int[] xPoints = {getXGraph(x1), getXGraph(x1), getXGraph(x2), getXGraph(x2)};
					int[] yPoints = {getYGraph(0), getYGraph(y1), getYGraph(y2), getYGraph(0)};
					polygon = new Polygon(xPoints, yPoints, 4);
					g.fillPolygon(polygon);
				}
			}
		}
		//Continuous CDF
		else if ((distribution.getType() == distribution.CONTINUOUS) & (getFunctionType() == CDF)){
			drawLine(g, quantile, 0, quantile, probability);
			drawLine(g, getXMin(), probability, quantile, probability);
		}
	}

	/**
	* This method sets the quantile.
	* @param x the quantile
	*/
	public void setQuantile(double x){
		quantile = x;
		probability = getDistribution().getCDF(x);
	}

	/**
	* This method returns the quantile.
	* @return the quantile
	*/
	public double getQuantile(){
		return quantile;
	}

	/**
	* This method sets the probability.
	* @param p the probability
	*/
	public void setProbability(double p){
		if (p < 0) p = 0; else if (p > 1) p = 1;
		probability = p;
		quantile = getDistribution().getQuantile(p);
	}



}

