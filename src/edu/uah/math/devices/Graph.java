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
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.io.Serializable;
import edu.uah.math.distributions.Domain;

/**
* This class defines a basic two-dimensional graph to be sub-classed. The class
* provides basic drawing methods.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Graph extends JPanel implements Serializable{
	//Variables
	private int pointSize = 2, leftMargin, rightMargin, topMargin, bottomMargin;
	private double xMin, xMax, yMin, yMax;
	//Constants
	public final static int LEFT = 0, RIGHT = 1, ABOVE = 2, BELOW = 3;
	public final static int VERTICAL = 0, HORIZONTAL = 1;
	//Objects
	private DecimalFormat decimalFormat = new DecimalFormat();

	protected boolean showModelDistribution = true;
	//Constructors

	/**
	* This general constructor creates a new graph with specified ranges on the x an
	* y axes,
	* @param x0 the minimum x value
	* @param x1 the maximum x value
	* @param y0 the minimun y value
	* @param y1 the maximum y value
	*/
	public Graph(double x0, double x1, double y0, double y1){
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setToolTipText("Graph");
		setScale(x0, x1, y0, y1);
		setMargins(20, 20, 20, 20);
		setMinimumSize(new Dimension(50, 50));
	}

	/**
	* This default constructor creates a new graph with x and y between 0 and 1.
	*/
	public Graph(){
		this(0, 1, 0, 1);
	}

	public void setShowModelDistribution(boolean b ){
		showModelDistribution = b; 
	 }
	//Methods for setting and getting scale parameters

	/**
	* This method sets the minimum and maximum values on the x and y axes.
	* @param x0 the minimum x value
	* @param x1 the maximum x value
	* @param y0 the minimum y value
	* @param y1 the maximum y value
	*/
	public void setScale(double x0, double x1, double y0, double y1){
		xMin = x0; xMax = x1;
		yMin = y0; yMax = y1;
	}

	/**
	* This method returns the minimum x value.
	* @return the minimum x value
	*/
	public double getXMin(){
		return xMin;
	}

	/**
	* This method returns the maximum x value.
	* @return the maximum x value
	*/
	public double getXMax(){
		return xMax;
	}

	/**
	* This method returns the minimum y value.
	* @return the minimum y value
	*/
	public double getYMin(){
		return yMin;
	}

	/**
	* This method returns the maximum y value.
	* @return the maximum y value
	*/
	public double getYMax(){
		return yMax;
	}

	//Methods for setting and getting margin parameters

	/**
	* This method sets the margin (in pixels).
	* @param l the left margin
	* @param r the right margin
	* @param b the bottom margin
	* @param t the top margin
	*/
	public void setMargins(int l, int r, int b, int t){
		leftMargin = l; rightMargin = r;
		bottomMargin = b; topMargin = t;
	}

	/**
	* This method gets the left margin.
	* @return the left margin
	*/
	public int getLeftMargin(){
		return leftMargin;
	}

	/**
	* This method gets the right margin.
	* @return the right margin
	*/
	public int getRightMargin(){
		return rightMargin;
	}

	/**
	* This method gets the top margin.
	* @return the top margin
	*/
	public int getTopMargin(){
		return topMargin;
	}

	/**
	* This method gets the bottom margin.
	* @return the bottom margin
	*/
	public int getBottomMargin(){
		return bottomMargin;
	}

	//Methods for converting between graph units (in pixels) and scale units

	/**
	* This method converts between scale and graph units.
	* @param x the x-coordinate in scale units
	* @return the x-coordinate in graph units
	*/
	public int getXGraph(double x){
		return leftMargin + (int)(((x - xMin)/(xMax - xMin)) * (getSize().width - leftMargin - rightMargin));
	}

	/**
	* This method converts between scale and graph units for the y variable.
	* @param y the y-coordinate in scale units
	* @return the y-coordinate in graph units
	*/
	public int getYGraph(double y){
		return getSize().height - bottomMargin - (int)(((y - yMin)/(yMax - yMin)) * (getSize().height - bottomMargin - topMargin));
	}

	/**
	* This method converts between graph and scale units for the x variable.
	* @param x the x-coordinate in graph units
	* @return the x-coordinate in scale units.
	*/
	public double getXScale(int x){
		return xMin + ((double)(x - leftMargin)/(getSize().width - leftMargin - rightMargin)) * (xMax - xMin);
	}

	/**
	* This method converts between graph and scale units for the y variable.
	* @param y the y-coordinate in graph units
	* @return the y-coordinate in scale units
	*/
	public double getYScale(int y){
		return yMin + ((double)(getSize().height - y - bottomMargin)/(getSize().height  - bottomMargin - topMargin)) * (yMax - yMin);
	}

	/**
	* This method converts scale units to graph units for the x variable.
	* @param x the value in scale units
	* @return the value in graph units
	*/
	public int getXPixels(double x){
		return getXGraph(xMin + x) - getXGraph(xMin);
	}

	/**
	* This method converts scale units to graph units for the y variable.
	* @param y the value in scale units
	* @return the value in graph units
	*/
	public int getYPixels(double y){
		return getYGraph(yMin + y) - getYGraph(yMin);
	}

	//Methods for drawling lines, labels and axes

	/**
	* This method draws a line between (x1, y1) and (x2, y2), where the coordinates
	* are in scale units.
	* @param x1 the x-coordinate of the first point
	* @param x2 the x-coordinate of the second point
	* @param y1 the y-coordinate of the first point
	* @param y2 the y-coordinate of the second point
	*/
	public void drawLine(Graphics g, double x1, double y1, double x2, double y2){
		g.drawLine(getXGraph(x1), getYGraph(y1), getXGraph(x2), getYGraph(y2));
	}

	/**
	* This method draws a tick mark at a specified point (in scale units),
	* a specified number of pixels in the positive and negative directions,
	* with a specified orientation.
	* @param g the graphics context
	* @param x the x-coordinate of the point in scale units
	* @param y the y-coordinate of the point in scale units
	* @param i the size of the tick mark in the positive direction
	* @param j the size of the tick mark in the negative direction
	* @param o the orientation (HORIZONTAL or VERTICAL)
	*/
	public void drawTick(Graphics g, double x, double y, int i, int j, int o){
		int a = getXGraph(x), b = getYGraph(y);
		if (o == VERTICAL) g.drawLine(a, b - j, a, b + i);
		else g.drawLine(a - i, b, a + j, b);
	}

	/**
	* This method draws a tick mark at a specified point, 3 pixels in the each direction,
	* with a specified oreintation.
	* @param g the graphics context
	* @param x the x-coordinate of the point in scale units
	* @param y the y-coordinate of the point in scale units
	* @param o the orientation (HORIZONTAL or VERTICAL)
	*/
	public void drawTick(Graphics g, double x, double y, int o){
		drawTick(g, x, y, 3, 3, o);
	}

	/**
	* This method draws a sepcified label at a specified orientation next to a specified
	* point.
	* @param g the graphics context
	* @param s the label
	* @param x the x-coordinate of the point in scale units
	* @param y the y-coordinate of the point in scale units
	* @param o the orientation (LEFT, RIGHT, ABOVE, BELOW)
	*/
	public void drawLabel(Graphics g, String s, double x, double y, int o){
		FontMetrics fm = this.getFontMetrics(this.getFont());
		int h = fm.getHeight(), w = fm.stringWidth(s);
		switch(o){
		case LEFT:
			g.drawString(s, getXGraph(x) - w - 3, getYGraph(y) + h / 2);
			break;
		case RIGHT:
			g.drawString(s, getXGraph(x) + 3, getYGraph(y) + h / 2);
			break;
		case ABOVE:
			g.drawString(s, getXGraph(x) - w / 2, getYGraph(y) + 1);
			break;
		case BELOW:
			g.drawString(s, getXGraph(x) - w / 2, getYGraph(y) + h);
			break;
		}
	}

	/**
	* This method draws an axis corresponding to a sepcified domain at specified poisiton
	* relative to the other variable, with a specified orientation. The type of domain
	* (DISCRETE or CONTINUOUS) determines whether the midpoints or the bounds are
	* indicated with tick marks.
	* @param g the graphics context
	* @param d the domain
	* @param c the position relative to the other variable
	* @param o the orientatin (HORIZONTAL or VERTICAL)
	*/
	public void drawAxis(Graphics g, Domain d, double c, int o){
		int type = d.getType();
		double t;
		if (o == HORIZONTAL){
			//Draw thte line
			drawLine(g, d.getLowerBound(), c, d.getUpperBound(), c);
			//Draw tick marks, depending on type
			for (int i = 0; i < d.getSize(); i++){
				if (type == Domain.DISCRETE) t = d.getValue(i); else t = d.getBound(i);
				drawTick(g, t, c, VERTICAL);
			}
			if (type == Domain.CONTINUOUS) drawTick(g, d.getUpperBound(), c, VERTICAL);
			//Draw labels
			if (type == Domain.DISCRETE) t = d.getLowerValue(); else t = d.getLowerBound();
			drawLabel(g, format(t), t, c, BELOW);
			if (type == Domain.DISCRETE) t = d.getUpperValue(); else t = d.getUpperBound();
			drawLabel(g, format(t), t, c, BELOW);
		}
		else{
			//Draw thte line
			drawLine(g, c, d.getLowerBound(), c, d.getUpperBound());
			//Draw tick marks, depending on type
			for (int i = 0; i < d.getSize(); i++){
				if (type == Domain.DISCRETE) t = d.getValue(i); else t = d.getBound(i);
				drawTick(g, c, t, HORIZONTAL);
			}
			if (type == Domain.CONTINUOUS) drawTick(g, c, d.getUpperBound(), HORIZONTAL);
			//Draw labels
			if (type == Domain.DISCRETE) t = d.getLowerValue(); else t = d.getLowerBound();
			drawLabel(g, format(t), c, t, LEFT);
			if (type == Domain.DISCRETE) t = d.getUpperValue(); else t = d.getUpperBound();
			drawLabel(g, format(t), c, t, LEFT);
		}
	}

	/**
	* This method draws an axis corresponding to a sepcified domain at specified poisiton
	* relative to the other variable, with a specified orientation. The type of domain
	* (DISCRETE or CONTINUOUS) determines whether the midpoints or the bounds are
	* indicated with tick marks.
	* @param g the graphics context
	* @param a the lower bound or value of the domain
	* @param b the upper bound or value of the domain
	* @param w the step size of the domain
	* @param t the type of domain (DISCRETE or CONTINUOUS)
	* @param c the position relative to the other variable
	* @param o the orientation (HORIZONTAL or VERTICAL)
	*/
	public void drawAxis(Graphics g, double a, double b, double w, int t, double c, int o){
		drawAxis(g, new Domain(a, b, w, t), c, o);
	}

	/**
	* This method draws an axis corresponding to a specified continuous domain, at a
	* specified position relative to the other variable, with a specified orientation.
	* @param g the graphics context
	* @param a the lower bound of the domain
	* @param b the upper bound of the domain
	* @param w the step size of the domain
	* @param c the position relative to the other variable
	* @param o the orientation
	*/
	public void drawAxis(Graphics g, double a, double b, double w, double c, int o){
		drawAxis(g, a, b, w, Domain.CONTINUOUS, c, o);
	 }

	//Methods for drawing boxes

	/**
	* This method draws a box between the specified corner points in scale units.
	* @param g the graphics context
	* @param x0 the x-coordinate of the first point
	* @param y0 the y-coordinate of the first point
	* @param x1 the x-coordinate of the second point
	* @param y1 the y-coordinate of the second point
	*/
	public void drawBox(Graphics g, double x0, double y0, double x1, double y1){
		g.drawRect(getXGraph(x0), getYGraph(y1), getXGraph(x1) - getXGraph(x0), getYGraph(y0) - getYGraph(y1));
	}

 	/**
 	* This method fills a box between the specified corner points in scale units.
	* @param g the graphics context
	* @param x0 the x-coordinate of the first point
	* @param y0 the y-coordinate of the first point
	* @param x1 the x-coordinate of the second point
	* @param y1 the y-coordinate of the second point
 	*/
	public void fillBox(Graphics g, double x0, double y0, double x1, double y1){
		g.fillRect(getXGraph(x0), getYGraph(y1), getXGraph(x1) - getXGraph(x0), getYGraph(y0) - getYGraph(y1));
	}

	/**
	* This method draws a point at the specified x and y coordinates (in scale units).
	* @param g the graphics context
	* @param x the x-coordinate
	* @param y the y-coordinate
	*/
	public void drawPoint(Graphics g, double x, double y){
		g.fillRect(getXGraph(x) - pointSize / 2, getYGraph(y) - pointSize / 2, pointSize, pointSize);
	}

	/**
	* This method sets the point size (in pixels) for drawing points.
	* @param n the point size
	*/
	public void setPointSize(int n){
		pointSize = n;
	}

	/**
	* This method returns the points size.
	* @return the point size
	*/
	public int getPointSize(){
		return pointSize;
	}

	/**
	* The following method draws a symmetric, horizontal boxplot, centered at
	* at a specified point with a specified radius (in scale units), at a
	* specified vertical position in pixels.
	* @param g the graphics context
	* @param x the center of the boxplot
	* @param r the radius of the boxplot
	* @param y the vertical poistion of the boxplot
	*/
	public void drawBoxPlot(Graphics g, double x, double r, int y){
		g.drawRect(getXGraph(x - r), y - 3, getXGraph(x + r) - getXGraph(x - r), 6);
		g.drawLine(getXGraph(x), y - 6, getXGraph(x), y + 6);
	}

	/**
	* The following method fills a symmetric, horizontal boxplot, centered at
	* a specified point, with a specified radius r (in scale units), at a specified
	* vertical poisiton in pixels.
	* @param g the graphics context
	* @param x the center of the boxplot
	* @param r the radius of the boxplot
	* @param y the vertical position of the boxplot
	*/
	public void fillBoxPlot(Graphics g, double x, double r, int y){
		g.fillRect(getXGraph(x - r), y - 3, getXGraph(x + r) - getXGraph(x - r), 6);
		g.drawLine(getXGraph(x), y - 6, getXGraph(x), y + 6);
	}

	/**
	* The following method draws a five-number, horizontal boxplot.
	* @param g the graphics context
	* @param x1 the minimum value
	* @param x2 the lower quartile value
	* @param x3 the median value
	* @param x4 the upper quartile value
	* @param x5 the maximum value
	* @param y the vertical position in pixels.
	*/
	public void drawBoxPlot(Graphics g, double x1, double x2, double x3, double x4, double x5, int y){
		g.drawLine(getXGraph(x1), y, getXGraph(x5), y);
		g.drawLine(getXGraph(x1), y - 3, getXGraph(x1), y + 3);
		g.drawLine(getXGraph(x5), y - 3, getXGraph(x5), y + 3);
		g.drawRect(getXGraph(x2), y - 3, getXGraph(x4) - getXGraph(x2), 6);
		g.drawLine(getXGraph(x3), y - 6, getXGraph(x3), y + 6);
	}

	/**
	* The following method fills a five-number, horizontal boxplot.
	* @param g the graphics context
	* @param x1 the minimum value
	* @param x2 the lower quartile value
	* @param x3 the median value
	* @param x4 the upper quartile value
	* @param x5 the maximum value
	* @param y the vertical position in pixels.
	*/
	public void fillBoxPlot(Graphics g, double x1, double x2, double x3, double x4, double x5, int y){
		g.drawLine(getXGraph(x1), y, getXGraph(x5), y);
		g.drawLine(getXGraph(x1), y - 6, getXGraph(x1), y + 6);
		g.drawLine(getXGraph(x5), y - 6, getXGraph(x5), y + 6);
		g.fillRect(getXGraph(x2), y - 3, getXGraph(x4) - getXGraph(x2), 6);
		g.drawLine(getXGraph(x3), y - 8, getXGraph(x3), y + 8);
	}

	/**
	* This method draws a box at a specified point with a specified
	* length and width. One dimension is given in scale units and the other in pixels.
	* @param g the graphics context
	* @param s the orientation (HORIZONTAL or VERTICAL)
	* @param x the x-coordinate of the point (in scale units)
	* @param y the y-coordinate of the point (in scale units)
	* @param l the length in scale units
	* @param i the width in the negative direction (in pixels)
	* @param j the with in the positive direction (in pixels)
	*/
	public void drawBox(Graphics g, int s, double x, double y, double l, int i, int j){
		if (s == VERTICAL) g.drawRect(getXGraph(x) - i, getYGraph(y + l), i + j, getYGraph(y) - getYGraph(y + l));
		else if (s  == HORIZONTAL) g.drawRect(getXGraph(x), getYGraph(y) - i, getXGraph(x + l) - getXGraph(x), i + j);
	}

	/**
	* This method fills a box at a specified point with a specified
	* length and width. One dimension is given in scale units and the other in pixels.
	* @param g the graphics context
	* @param s the orientation (HORIZONTAL or VERTICAL)
	* @param x the x-coordinate of the point (in scale units)
	* @param y the y-coordinate of the point (in scale units)
	* @param l the length in scale units
	* @param i the width in the negative direction (in pixels)
	* @param j the with in the positive direction (in pixels)
	*/
	public void fillBox(Graphics g, int s, double x, double y, double l, int i, int j){
		if (s == VERTICAL) g.fillRect(getXGraph(x) - i, getYGraph(y + l), i + j, getYGraph(y) - getYGraph(y + l));
		else if (s  == HORIZONTAL) g.fillRect(getXGraph(x), getYGraph(y) - i, getXGraph(x + l) - getXGraph(x), i + j);
	}

	//Methods for drawing circles

	/**
	* This method draws a circle with a specified center and radius (in scale units).
	* @param g the graphics context
	* @param x the x-coordinate of the center
	* @param y the y-coordinate of the center
	* @param r the radius of the circle
	*/
	public void drawCircle(Graphics g, double x, double y, double r){
		if (r > 0) g.drawOval(getXGraph(x - r), getYGraph(y + r), getXGraph(x + r) - getXGraph(x - r), getYGraph(y - r) - getYGraph(y + r));
		else drawPoint(g, x, y);
	}

	/**
	* This method fills a circle with a specified center and radius (in scale units).
	* @param g the graphics context
	* @param x the x-coordinate of the center
	* @param y the y-coordinate of the center
	* @param r the radius of the circle
	*/
	public void fillCircle(Graphics g, double x, double y, double r){
		if (r > 0) g.fillOval(getXGraph(x - r), getYGraph(y + r), getXGraph(x + r) - getXGraph(x - r), getYGraph(y - r) - getYGraph(y + r));
		else drawPoint(g, x, y);
	}

	//Formating
	/**
	* This method formats a specified number.
	* @param x the number
	* @return the formated number as a string
	*/
	public String format(double x){
		return decimalFormat.format(x);
	}

}


