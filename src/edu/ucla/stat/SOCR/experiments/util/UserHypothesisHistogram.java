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
package edu.ucla.stat.SOCR.experiments.util;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.io.Serializable;

import edu.uah.math.devices.Histogram;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;

/**
* This class models an interactive histogram.  The user can click on the horizontal axes to
* add points to the data set.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class UserHypothesisHistogram extends Histogram implements Serializable{
	//Variables
	private int count;
	private Domain domain;
	private Vector<Double> values = new Vector<Double>();
	protected double userHypothesis,leftCutOff=0, rightCutOff=0;
	SimulationResampleInferencePanel inferencePanel;
	protected double pValue = 0;;

	
	/**
	* This general constructor creates a new interactive histogram corresponding to a specified
	* domain.
	* @param d the domain
	*/
	public UserHypothesisHistogram(Domain d, SimulationResampleInferencePanel link){
		inferencePanel= link;
		domain = d;
		data = new IntervalData(domain);
		setIntervalData(data);
		count = domain.getSize();
	}


	/**
	* This general constructor creates a new interactive histogram corresponding to a specified
	* domain, given in terms of its parameters.
	* @param a the lower bound or value of the domain
	* @param b the upper bound or value of the domain
	* @param w the step size of the domain
	* @param t the type of domain (DISCRETE or CONTINUOUS)
	*/
	public UserHypothesisHistogram(double a, double b, double w, int t, SimulationResampleInferencePanel link ) {
		this(new Domain(a, b, w, t), link);
	}

	public void paintComponent(Graphics g){
		//super.paintComponent(g);
		double x, d, yMax;
		//Set graph scale
		int size = data.getSize();
		domain = data.getDomain();
		double xMin = domain.getLowerBound(),
			xMax = domain.getUpperBound(),
			width = domain.getWidth();
		int intervals = domain.getSize();
		//Determine yMax
		
		yMax = data.getMaxFreq();
		
		
		if (yMax == 0) yMax = 1;
		setScale(xMin, xMax, 0, yMax);
		//Draw axes
		g.setColor(Color.black);
 		drawAxis(g, 0, yMax, 0.1 * yMax, xMin, VERTICAL);
		drawAxis(g, domain, 0, HORIZONTAL);
		//Draw data
		//System.out.println("paint Userhistogram xmax="+xMax+" yMax"+yMax+" data Size="+size);
		if (size > 0){
			for (int i = 0; i < intervals; i++){
				x = domain.getValue(i);
				d = data.getFreq(x);
				
				//System.out.println(" fillBox "+d);
				g.setColor(histogramColor);
				fillBox(g, x - width / 2, 0, x + width / 2, d);
				
				g.setColor(Color.black);
				drawBox(g, x - width / 2, 0, x + width / 2, d);
			}
			//Draw summary statistics
			g.setColor(boxPlotColor);
			
			int j = getSize().height - 10;
			switch(statisticsType){
			case MSD:		//True mean and standard deviation
				fillBoxPlot(g, data.getMean(), data.getSD(), j);
				break;
			case IMSD:		//Interval mean and standard deviation
				fillBoxPlot(g, data.getIntervalMean(), data.getIntervalSD(), j);
				break;
			case BOX:		//Five number summary
				fillBoxPlot(g, data.getMinValue(), data.getQuartile(1), data.getMedian(), data.getQuartile(3), data.getMaxValue(), j);
				break;
			case MAD:		//Median and mean absolute deviation
				//fillBoxPlot(g, data.getMedian(), data.getMAD(), j);
				break;
			}
  		}
		g.setColor(Color.red);
		

		if(!inferencePanel.isProportionInterval())
			drawBox(g, userHypothesis-width/30, 0, userHypothesis+-width/30, yMax);
		else {
			drawBox(g, leftCutOff-width/30, 0, leftCutOff+-width/30, yMax);
			drawBox(g, rightCutOff-width/30, 0, rightCutOff+-width/30, yMax);
		}
		
		g.setColor(Color.blue);
		
		this.fillBox(g, pValue-width/2, 0, pValue+width/2, width*2);
	}
	
	/**
	* This default domain creates a new interactive histogram with the default domain
	* (0, 1), with step size 0.1.
	*/
	public UserHypothesisHistogram(){
		this(new Domain(), null);
	}

	public void setPValue(double value){
		pValue= value;
	}
		
	/**
	* This method sets the domain.  The new domain is passed to the interval data set
	* and then the values are added to the data set.
	* @param d the domain
	*/
	public void setDomain(Domain d){
		domain = d;
		data.setDomain(domain);
		for (int i = 0; i < values.size(); i++) data.setValue(((Double)values.elementAt(i)).doubleValue());
		repaint();
	}

	/**
	* This method sets the width of the domain.
	* @param w the width
	*/
	public void setWidth(double w){
		setDomain(new Domain(domain.getLowerBound(), domain.getUpperBound(), w, domain.getType()));
	}

	/**
	* This method resets the interactive histogram by removing all values in the dataset.
	*/
	public void reset(){
		values.removeAllElements();
		data.reset();
		repaint();
	}

	/**
	* This method returns the values from the data set.
	* @param i the index of the value
	* @return the value of the data set corresponding to the index
	*/
	public double getValue(int i){
		if (i < 0) i = 0; else if (i >= values.size()) i = values.size() - 1;
		return ((Double)values.elementAt(i)).doubleValue();
	}

	/**
	 * This method returns the last value.
	 * @return the last value in the data set
	 */
	public double getValue(){
		return getValue(values.size() - 1);
	}

	/**
	* This method handles the events corresponding to mouse clicks.  If the user clicks on
	* the horizontal axes, the corresponding value is added to the data set.
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		System.out.println("mouse clicked");
		int y0 = getYGraph(0);
		int y = e.getY();
		double x = getXScale(e.getX());
		// Determine if a valid point has been selected
		if ((data.getDomain().getLowerBound() <= x) & (x <= data.getDomain().getUpperBound()) & (y0 - 10 < y) & (y < y0 + 10)){
			data.setValue(x);
			repaint();
			values.addElement(new Double(x));
		}
	}
	
	public void setUserHypothesis(double value){
		userHypothesis= value;
		
	}

	public void setLeftCutOff(double value){
		leftCutOff= value;
		
	}
	public void setRightCutOff(double value){
		rightCutOff= value;
		
	}
}
