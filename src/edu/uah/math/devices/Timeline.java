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
import java.io.Serializable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import edu.uah.math.distributions.Domain;

/**
* This class defines a simple timeline for displaying random points in time.
* The timeline is shown as an axis, with the points in time as dots in
* specified colors. This object is useful for the Poisson and negative binomial experiments.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class Timeline extends Graph implements MouseMotionListener, Serializable{
	private double min, max, currentTime;
	private Vector<Double> times = new Vector<Double>();
	private Vector<Color> colors = new Vector<Color>();
	private Domain domain;
	private String name;
	private Color axisColor = Color.black, currentTimeColor = Color.blue;

	/**
	* This general constructor creates a new timeline corresponding to a specified
	* domain and a specified name for the underlying variable.
	* @param d the domain
	* @param n the name
	*/
	public Timeline(Domain d, String n){
		this.addMouseMotionListener(this);
		setMargins(20, 20, 20, 10);
		setPointSize(6);
		setDomain(d);
		setName(n);
	}

	/**
	* This special constructor creates a new timeline corresponding to a specified
	* domain and the default name "x".
	*/
	public Timeline(Domain d){
		this(d, "x");
	}

	/**
	* This default constructor creates a new timeline corresponding to the default
	* domain (0, 1) (with step size 0.1), and with the default name "x".
	*/
	public Timeline(){
		this(new Domain());
	}

	/**
	* This method sets the domain.
	* @param d the domain of the variable
	*/
	public void setDomain(Domain d){
		domain = d;
		min = domain.getLowerBound();
		max = domain.getUpperBound();
		setScale(min, max, 0, 0);
	}

	/**
	* This method returns the domain.
	* @return the domain of the variable
	*/
	public Domain getDomain(){
		return domain;
	}

	/**
	* This method sets the name.
	* @param n the name of the variable
	*/
	public void setName(String n){
		name = n;
	}

	/**
	* This method retunrs the name.
	* @return the name of the variable
	*/
	public String getName(){
		return name;
	}

	/**
	* This method paints the timeline as an axis, with the data points as
	* dots in a specified color. The data points up to the current time are painted.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		double t;
		super.paintComponent(g);
		//Draw x axis
		g.setColor(axisColor);
		drawAxis(g, domain, 0, HORIZONTAL);
		//Draw data
		g.setColor(currentTimeColor);
		drawTick(g, currentTime, 0, 10, 10, VERTICAL);
		if (! times.isEmpty()){
			for (int i = 0; i < times.size(); i++){
				g.setColor(getColor(i));
				t = getTime(i);
				if (t <= currentTime) drawPoint(g, t, 0);
			}
		}
	}

	/**
	* This method specifies the axis color.
	* @param c the axis color
	*/
	public void setAxisColor(Color c){
		axisColor = c;
	}

	/**
	* This method returns the axis color.
	* @return the axis color
	*/
	public Color getAxisColor(){
		return axisColor;
	}

	/**
	* This method adds a new time with a specified color.
	* @param t the value (time) of the data point
	* @param c the color to be used to represent the point
	*/
	public void addTime(double t, Color c){
		Double d = new Double(t);
		times.addElement(d);
		colors.addElement(c);
	}

	/**
	* This method adds a new time with the default color red.
	* @param t the value (time) of the data point
	*/
	public void addTime(double t){
		addTime(t, Color.red);
	}

	/**
	* This method adds a new time point of a specified color that is uniformly
	* distributed on the domain.
	* @param c the color of the point
	*/
	public void addTime(Color c){
		double t = min + Math.random() * (max - min);
		if (domain.getType() == Domain.DISCRETE) t = domain.getNearestValue(t);
		addTime(t, c);
	}

	/**
	* This method adds a new time point of the default color red that is
	* uniformly distributed on the domain.
	*/
	public void addTime(){
		addTime(Color.red);
	}

	/**
	* This method returns the time at a specified index.
	* @param i the index
	* @return the time corresponding to the index
	*/
	public double getTime(int i){
		Double d = (Double)times.elementAt(i);
		return d.doubleValue();
	}

	/**
	* This method returns the Cummulative times UP-TO a specified index.
	* @param i the index
	* @return the Cummulative time corresponding to Sum of all time up-to the index
	*/
	public double getCummilativeTimesAtIndex(int ind){
		Double doubleVar;
		double cummulativeTime=0;
		if (0<= ind && ind < times.size()) {
			for (int i=0; i < ind; i++) {
				doubleVar = (Double)times.elementAt(i);
				cummulativeTime += doubleVar.doubleValue();
			}
		}
		return cummulativeTime;
	}

	/**
	* This method returns the entire vector of times.
	* @return the vector of time points
	*/
	public Vector getTimes(){
		return times;
	}

	/**
	* This method returns the number of data points.
	* @return the number of points in the dataset
	*/
	public int getDataSize(){
		return times.size();
	}

	/**
	* This method returns the color at a specified index.
	* @param i the index
	* @return the color corresponding to the index
	*/
	public Color getColor(int i){
		return (Color)colors.elementAt(i);
	}

	/**
	* This method returns the entire vector of colors
	* @return the vector of colors
	*/
	public Vector getColors(){
		return colors;
	}


	/**
	* This method resets the data by clearing the vector of times and color.
	*/
	public void resetData(){
		times.removeAllElements();
		colors.removeAllElements();
	}

	/**
	* This method resets the data, by clearing the vector of times and colors,
	* and then repaints the timeline.
	*/
	public void reset(){
		resetData();
		setCurrentTime(min);
	}

	/**
	* This method sets the current time.
	* @param t the current time
	*/
	public void setCurrentTime(double t){
		currentTime = t;
		repaint();
	}

	/**
	* This method gets the current time.
	* @return the current time
	*/
	public double getCurrentTime(){
		return currentTime;
	}

	/**
	* This method sets the color used for the current time tick mark.
	* @param c the color for the current time
	*/
	public void setCurrentTimeColor(Color c){
		currentTimeColor = c;
	}

	/**
	* This method returns the color used to denote the current time.
	* @return the color for the current time
	*/
	public Color getCurrentTimeColor(){
		return currentTimeColor;
	}

	/**
	* This method handles the mouse move event.  The coordinates of the mouse (in scale
	* units) are shown in the tool tip.
	* @param e the mouse event
	*/
	public void mouseMoved(MouseEvent e){
		double xMouse = getXScale(e.getX());
		if (domain.getType() == Domain.DISCRETE) xMouse = domain.getNearestValue(xMouse);
		setToolTipText(name + " = " + format(xMouse));
	}

	//This mouse event is not handled.
	public void mouseDragged(MouseEvent event){}

}

