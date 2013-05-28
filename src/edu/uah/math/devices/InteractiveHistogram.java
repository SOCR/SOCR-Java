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
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.io.Serializable;
import edu.uah.math.distributions.Domain;
import edu.uah.math.distributions.IntervalData;

/**
* This class models an interactive histogram.  The user can click on the horizontal axes to
* add points to the data set.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class InteractiveHistogram extends Histogram implements MouseListener, Serializable{
	//Variables
	private int count;
	private Domain domain;
	private IntervalData data;
	private Vector<Double> values = new Vector<Double>();

	/**
	* This general constructor creates a new interactive histogram corresponding to a specified
	* domain.
	* @param d the domain
	*/
	public InteractiveHistogram(Domain d){
		this.addMouseListener(this);
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
	public InteractiveHistogram(double a, double b, double w, int t) {
		this(new Domain(a, b, w, t));
	}

	/**
	* This default domain creates a new interactive histogram with the default domain
	* (0, 1), with step size 0.1.
	*/
	public InteractiveHistogram(){
		this(new Domain());
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
		int y0 = getYGraph(0);
		int y = e.getY();
		double x = getXScale(e.getX());
		// Determine if a valid point has been selected
		if ((data.getDomain().getLowerBound() <= x) & (x <= data.getDomain().getUpperBound())) {
				// & (y0 - 100 < y) & (y < y0 + 10)){
			data.setValue(x);
			values.addElement(new Double(x));
			repaint();
		}
	}

	//The following method correspond to mouse events that are not handled.
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}

}
