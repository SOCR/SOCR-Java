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
import java.awt.Graphics;
import java.awt.Color;
import java.io.Serializable;
import edu.uah.math.distributions.IntervalData;
import edu.uah.math.distributions.Domain;

/**
* This class is a basic graph for displaying the density and moments for a specified
* interval data set.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Histogram extends Graph implements Serializable{
	//Constants
	public final static int FREQ = 0, REL_FREQ = 1, DENSITY = 2;
	public final static int NONE = 0, MSD = 1, IMSD = 2, BOX = 3, MAD = 4, MMM = 5;
	//Variables
 	protected int statisticsType = MSD, histogramType = REL_FREQ;
	//Objects
 	protected IntervalData data;
	protected Color histogramColor = Color.red, boxPlotColor = Color.red;

	/**
	* This general constructor creates a new data graph with a specified domain.
	* @param d the domain
	*/
	public Histogram(IntervalData d){
		setMargins(35, 20, 30, 10);
		setIntervalData(d);
	}

	/**
	* This default constructor creates a new data graph with a new data set on the interval [0, 1]
	* with subintervals of length 0.1.
	*/
	public Histogram(){
		this(new IntervalData());
	}

	/**
	* This method paints the graph of the density function, empirical density function,
	* moment bar, and empirical moment bar.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		double x, d, yMax;
		//Set graph scale
		int size = data.getSize();
		Domain domain = data.getDomain();
		double xMin = domain.getLowerBound(),
			xMax = domain.getUpperBound(),
			width = domain.getWidth();
		int intervals = domain.getSize();
		//Determine yMax
		if (histogramType == FREQ) yMax = data.getMaxFreq();
		else if (histogramType == REL_FREQ) yMax = data.getMaxRelFreq();
		else yMax = data.getMaxDensity();
		
		if (yMax == 0) yMax = 1;
		setScale(xMin, xMax, 0, yMax);
		//Draw axes
		g.setColor(Color.black);
 		drawAxis(g, 0, yMax, 0.1 * yMax, xMin, VERTICAL);
		drawAxis(g, domain, 0, HORIZONTAL);
		//Draw data
		//System.out.println("paint histogram xmax="+xMax+" yMax"+yMax+" data Size="+size);
		if (size > 0){
			for (int i = 0; i < intervals; i++){
				x = domain.getValue(i);
				if (histogramType == FREQ) d = data.getFreq(x);
				else if (histogramType == REL_FREQ) d = data.getRelFreq(x);
				else  d = data.getDensity(x);
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
				fillBoxPlot(g, data.getMedian(), data.getMAD(), j);
				break;
			}
  		}
	}

	 /**
	 * This method assigns the data and sets up graph paramters.
	 * @param d the interval data set.
	 */
	public void setIntervalData(IntervalData d){
		data = d;
		setToolTipText(data.getName() + " Distribution");
		repaint();
	}

	/**
	* This method returns the data set.
	* @return the interval data set
	*/
	public IntervalData getIntervalData(){
		return data;
	}

	/**
	* This method sets the plot style for the histogram. The choices are
	* frequency distribution, relative frequency distribution, or density
	* distribution
	* @param i the type of plot style (FREQ, REL_FREQ, DENSITY)
	*/
	public void setHistogramType(int i){
		histogramType = i;
		repaint();
	}

	/**
	* This method returns the plot style for the histogram.
	* @return the plot style
	*/
	public int getHistogramType(){
		return histogramType;
	}

	/**
	* This method specifies the summary statistics to display. The choices are
	* none; mean, standard deviation; boxplot; median, mean absolute deviation;
	* and mean, meadian, mode
	* @param n the type of summary statistics (NONE, MSD, BOX, MAD, MMM)
	*/
	public void setStatisticsType(int n){
		statisticsType = n;
		repaint();
	}

	/**
	* This method returns the type of boxplot.
	* @return the boxplot type
	*/
	public int getStatisticsType(){
		return statisticsType;
	}

	/**
	* This method sets the color for the bars in the histogram.
	*/
	public void setHistogramColor(Color c){
		histogramColor = c;
	}

	/**
	* This method returns the color for the bars in the histogram.
	*/
	public Color getHistogramColor(){
		return histogramColor;
	}

	/**
	* This method sets the boxplot color.
	* @param c the boxplot color
	*/
	public void setBoxPlotColor(Color c){
		boxPlotColor = c;
	}

	/**
	* This method returns the boxplot color.
	* @return the boxplot color
	*/
	public Color getBoxPlotColor(){
		return boxPlotColor;
	}

}

