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
import java.util.Vector;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import edu.uah.math.distributions.Domain;

/**
* This class defines a basic two-dimensional scatterplot that can be sub-classed.
* The points are stored as two vectors, one containing the x coordinates and one
* containing the y coordinates. The domains and names of the variables can be
* specified. The graphs shows axes of the two variables and the points as red dots.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class ScatterPlot extends Graph implements MouseMotionListener, Serializable{
	//Variables
	double xMin, xMax, yMin, yMax;
	private Vector<Double> xCoordinates = new Vector<Double>();
	private Vector<Double> yCoordinates = new Vector<Double>();
	private Vector<Color> colors = new Vector<Color>();
	private Domain xDomain, yDomain;
	private String xName, yName;
	private Color xAxisColor = Color.black, yAxisColor = Color.black, boxColor = Color.gray;

	/**
	* This general constructor creates a new scatterplot with specified domains
	* and names.
	* @param d1 the domain of the x variable
	* @param n1 the name of the x variable
	* @param d2 the domain of the y variable
	* @param n2 the name of the y variable
	*/
	public ScatterPlot(Domain d1, String n1, Domain d2, String n2){
		setDomains(d1, d2);
		setNames(n1, n2);
		setMargins(25, 20, 25, 20);
		this.addMouseMotionListener(this);
	}

	/**
	* This special constructor creates a new scatterplot with specified domains and
	* default names "x" and "y".
	* @param d1 the domain of the x variable
	* @param d2 the domain of the y variable
	*/
	public ScatterPlot(Domain d1, Domain d2){
		this(d1, "x", d2, "y");
	}

	/**
	* This special constructor creates a new scatterplot with a specified common x and y
	* domain and specified names.
	* @param d the common domain of the x and y variables
	* @param n1 the name of the x variable
	* @param n2 the name of the y variable
	*/
	public ScatterPlot(Domain d, String n1, String n2){
		this(d, n1, d, n2);
	}

	/**
	* This special constructor creates a new scatterplot with a specified common
	* domain and default names "x" and "y".
	* @param d the common domain of the x and y variables
	*/
	public ScatterPlot(Domain d){
		this(d, "x", "y");
	}

	/**
	* This default constructor creates a new scatterplot with common domain
	* [0, 1] and default names "x" and "y"
	*/
	public ScatterPlot(){
		this (new Domain());
	}

	/**
	* This method sets the domains.
	* @param d1 the domain of the x variable
	* @param d2 the domain of the y variable
	*/
	public void setDomains(Domain d1, Domain d2){
		xDomain = d1; yDomain = d2;
		xMin = xDomain.getLowerBound(); xMax = xDomain.getUpperBound();
		yMin = yDomain.getLowerBound(); yMax = yDomain.getUpperBound();
		setScale(xMin, xMax, yMin, yMax);
	}

	/**
	* This method sets the common domain for x and y.
	* @param d the common domain of the x and y variables
	*/
	public void setDomain(Domain d){
		setDomains(d, d);
	}

	/**
	* This method sets the domain of the x variable.
	* @param d the domain of the x variable.
	*/
	public void setXDomain(Domain d){
		setDomains(d, yDomain);
	}

	/**
	* This method returns the domain of the x variable.
	* @return the domain of the x variable
	*/
	public Domain getXDomain(){
		return xDomain;
	}

	/**
	* This method sets the domain of the y variable.
	* @param d the domain of the y variable
	*/
	public void setYDomain(Domain d){
		setDomains(xDomain, d);
	}

	/**
	* This method returns the domain of the y variable.
	* @return the domain of the y variable
	*/
	public Domain getYDomain(){
		return yDomain;
	}

	/**
	* This method sets the names of the varaibles.
	* @param n1 the name of the x variable
	* @param n2 the name of the y variable
	*/
	public void setNames(String n1, String n2){
		xName = n1;
		yName = n2;
	}

	/**
	* This method sets the name of the x variable.
	* @param n the name of the x variable
	*/
	public void setXName(String n){
		xName = n;
	}

	/**
	* This method returns the name of the x variable.
	* @return the name of the x variable
	*/
	public String getXName(){
		return xName;
	}

	/**
	* This method sets the name of the y variable
	* @param n the name of the y variable
	*/
	public void setYName(String n){
		yName = n;
	}

	/**
	* This method returns the name of the y variable.
	* @return the name of the y variable
	*/
	public String getYName(){
		return yName;
	}

	/**
	* This method paints the scatterplots, including the x and y axes and the boundary
	* box. The data points are plotted in red.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw axes
		g.setColor(xAxisColor);
		drawAxis(g, xDomain, yMin, HORIZONTAL);
		g.setColor(yAxisColor);
		drawAxis(g, yDomain, xMin, VERTICAL);
		//Draw bounding box
		g.setColor(boxColor);
		drawLine(g, xMax, yMin, xMax, yMax);
		drawLine(g, xMin, yMax, xMax, yMax);
		//Draw points
		if (! xCoordinates.isEmpty()){
			for (int i = 0; i < xCoordinates.size(); i++){
				g.setColor(getColor(i));
				drawPoint(g, getX(i), getY(i));
			}
		}
	}

	/**
	* This method adds a specified data point of a specified color to the dataset.
	* @param x the x coordinate of the point
	* @param y the y coordiante of the point
	*/
	public void addPoint(double x, double y, Color c){
		xCoordinates.addElement(new Double(x));
		yCoordinates.addElement(new Double(y));
		colors.addElement(c);
	}

	/**
	* This method adds a specified data point of default color red to the dataset.
	* @param x the x coordinate of the point
	* @param y the y coordiante of the point
	*/
	public void addPoint(double x, double y){
		addPoint(x, y, Color.red);
	}

	/**
	* This method adds a random data point of a specified color to the dataset.
	* The x and y coordiantes are uniformly distributed on their respective domains.
	*/
	public void addPoint(Color c){
		addPoint(xMin + (xMax - xMin) * Math.random(), yMin + (yMax - yMin) * Math.random(), c);
	}

	/**
	* This method adds a random data point of default color red to the dataset.
	* The x and y coordiantes are uniformly distributed on their respective domains.
	*/
	public void addPoint(){
		addPoint(Color.red);
	}

	/**
	* This method gets the x-coordinate of the point with a specified index.
	* @param i the index of the point
	* @return the x coordinate of the point
	*/
	public double getX(int i){
		return ((Double)xCoordinates.elementAt(i)).doubleValue();
	 }

	/**
	* This method returns the vector of x coordinates.
	* @return the vector of x coordinates
	*/
	public Vector getXCoordinates(){
		return xCoordinates;
	}

	/**
	* This method gets the y-coordinate of the point with a specified index.
	* @param i the index of the point
	* @return the y coordinate of the point
	*/
	public double getY(int i){
		return ((Double)yCoordinates.elementAt(i)).doubleValue();
	 }

	/**
	* This method returns the vector of y coordinates.
	* @return the vector of y coordinates
	*/
	public Vector getYCoordinates(){
		return yCoordinates;
	}

	/**
	* This method gets the color of the point with a specified index.
	* @param i the index of the point
	*/
	public Color getColor(int i){
		return (Color)colors.elementAt(i);
	}

	/**
	* This method returns the vector of colors.
	* @return the vector of colors
	*/
	public Vector getColors(){
		return colors;
	}

	/**
	* This method resets the data by clearing the vectors of x coordinates,
	* y coordinates, and colors.
	*/
	public void resetData(){
		xCoordinates.removeAllElements();
		yCoordinates.removeAllElements();
		colors.removeAllElements();
	}

	/**
	* This method resets the scatterplot by clearing the data and redrawing.
	*/
	public void reset(){
		resetData();
		repaint();
	}

	/**
	* This method returns the number of data points.
	* @return the number of points in the dataset
	*/
	public int getDataSize(){
		return xCoordinates.size();
	}

	/**
	* This method sets the x-axis color.
	* @param c the x-axis color
	*/
	public void setXAxisColor(Color c){
		xAxisColor = c;
	}

	/**
	* This method returns the x-axix color.
	* @return x-axis color
	*/
	public Color getXAxisColor(){
		return xAxisColor;
	}

	/**
	* This method sets the y-axis color.
	* @param c the y-axis color
	*/
	public void setYAxisColor(Color c){
		yAxisColor = c;
	}

	/**
	* This method returns the y-axis color.
	* @return y-axis color
	*/
	public Color getYAxisColor(){
		return yAxisColor;
	}

	/**
	* This method sets the box color.
	* @param c the box color
	*/
	public void setBoxColor(Color c){
		boxColor = c;
	}

	/**
	* This method returns the box color.
	* @return box color
	*/
	public Color getBoxColor(){
		return boxColor;
	}

	/**
	* This method handles the mouse move event.  The coordinates of the mouse (in scale
	* units) displayed in the tool tip.
	* @param e the mouse event
	*/
	public void mouseMoved(MouseEvent e){
		double xMouse = getXScale(e.getX());
		double yMouse = getYScale(e.getY());
		setToolTipText(xName + " = " + format(xMouse) + ", " + yName + " = " + format(yMouse));
	}

	//This mouse event is not handled
	public void mouseDragged(MouseEvent event){}
}

