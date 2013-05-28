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
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;

/**
* This class models an interactive scatterplot. The user adds data points by
* clicking in the graph. The data points are shown as red dots. The mean and
* standard deviation for each variable is shown graphically with crosshairs.
* The regression line is also shown.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class UserScatterPlot extends ScatterPlot implements MouseListener{
	private IntervalData xData, yData;
	private int n;
	private double xMouse, yMouse;
	private double sum, covariance, correlation;
	private double slope, intercept;
	private boolean sampleLines;

	/**
	* This general constructor creates a new scatterplot correpsonding to specified
	* domains and names. The x and y interval datasets are created to hold the data.
	* @param d1 the domain of the x variable
	* @param n1 the name of the x variable
	* @param d2 the domain of the y variable
	* @param n2 the name of the y variable
	*/
	public UserScatterPlot(Domain d1, String n1, Domain d2, String n2){
		super(d1, n1, d2, n2);
		this.addMouseListener(this);
		xData = new IntervalData(d1);
		yData = new IntervalData(d2);
		setSize(250, 250);
	}

	/**
	* This special constructor creates a new user scatterplot with spcified domains
	* and default names "x" and "y".
	* @param d1 the domain of the x variable
	* @param d2 the domain of the y variable
	*/
	public UserScatterPlot(Domain d1, Domain d2){
		this(d1, "x", d2, "y");
	}

	/**
	* This special constructur creates a new user scatterplot with common domain
	* for the variables, and with specified names.
	* @param d the common domain of the x and y variables
	* @param n1 the name of the x variable
	* @param n2 the name of the y variable
	*/
	public UserScatterPlot(Domain d, String n1, String n2){
		this(d, n1, d, n2);
	}

	/**
	* This special constructor creates a new user scatterplot with a specified common
	* domain for the variables and with default names "x" and "y".
	* @param d the common domain of the x and y variables
	*/
	public UserScatterPlot(Domain d){
		this(d, "x", d, "y");
	}

	/**
	* This default constructor creates a new user scatterplot with default common domain
	* (0, 1) with step size 0.1, and with default names "x" and "y".
	*/
	public UserScatterPlot(){
		this(new Domain());
	}

	/**
	* This method paints the scatterplot. A horizontal green line is centered at the
	* mean of the x data and extends one standard deviation in each direction. A
	* vertical blue line is centered at the mean of the y data and extends one
	* standard deviation in each direction. The regression line is shown in red.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (n > 0){
			double y1, y2;
			double xMin = getXDomain().getLowerBound(), xMax = getXDomain().getUpperBound();
			double yMin = getYDomain().getLowerBound(), yMax = getYDomain().getUpperBound();
			g.setColor(Color.green);
			drawLine(g, xData.getMean() - xData.getSD(), yData.getMean(), xData.getMean() + xData.getSD(), yData.getMean());
			g.setColor(Color.blue);
			drawLine(g, xData.getMean(), yData.getMean() - yData.getSD(), xData.getMean(), yData.getMean() + yData.getSD());
			y1 = intercept + slope * xMin;
			y2 = intercept + slope * xMax;
			g.setColor(Color.red);
			drawLine(g, xMin, y1, xMax, y2);
		}
	}

	/**
	* This method handles the mouse click event.  The new point is added to the dataset.
	* @param e the mouse event
	*/
	public void mouseClicked(MouseEvent e){
		xMouse = getXScale(e.getX());
		yMouse = getYScale(e.getY());
		if (getXDomain().getLowerBound() < xMouse & xMouse < getXDomain().getUpperBound()
			& getYDomain().getLowerBound() < yMouse & yMouse < getYDomain().getUpperBound()){
			n++;
			addPoint(xMouse, yMouse);
			repaint();
			xData.setValue(xMouse);
			yData.setValue(yMouse);
			sum = sum + xMouse * yMouse;
			covariance = (sum -  n * xData.getMean() * yData.getMean()) / (n - 1);
			correlation = covariance / (xData.getSD() * yData.getSD());
			slope = covariance / xData.getVariance();
			intercept = yData.getMean() - slope * xData.getMean();
		}
	}

	//The following mouse events are not handled.
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mousePressed(MouseEvent event){}
	public void mouseReleased(MouseEvent event){}

	/**
	* This method resets the scatterplot by clearing the data.
	*/
	public void reset(){
		super.reset();
		sum = 0;
		n = 0;
		xData.reset();
		yData.reset();
	}

	/**
	* This method returns the covariance between the x data and the y data.
	* @return the covariance
	*/
	public double getCovariance(){
		return covariance;
	}

	/**
	* This method returns the correlation between the x data and the y data.
	* @return the correlation
	*/
	public double getCorrelation(){
		return correlation;
	}

	/**
	* This method returns the slope of the regression line between the x data and the y data.
	* @return the slope
	*/
	public double getSlope(){
		return slope;
	}

	/**
	* This method returns the intercept of the regression line between the x data and the y data.
	* @return the intercept
	*/
	public double getIntercept(){
		return intercept;
	}

	/**
	* This method returns the x data as an interval dataset.
	* @return the x interval dataset
	*/
	public IntervalData getXData(){
		return xData;
	}

	/**
	* This method returns the y data as an interval dataset.
	* @return the y interval data set
	*/
	public IntervalData getYData(){
		return yData;
	}
}

