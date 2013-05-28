/**
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
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
* This class models a simple spinner.  The number of sectors and the probability
* distribution that governs the spinner can be specified. The size of the each
* sector is proportional to the probability of the sector.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class Spinner extends JComponent implements MouseMotionListener, Serializable{
	private int divisions, value, width, height, radius;
	private double angle;
	private int[] steps = new int[4];
	private double[] probabilities = new double[4];
	private Color[] basicColors = {Color.blue, Color.green, Color.red, Color.cyan, Color.gray, Color.white,
		Color.magenta, Color.yellow, Color.pink, Color.orange};
	private Color[] colors;


	/**
	* This general constructor creates a new spinner with a specified array of
	* probabilities and a specified array of colors. The size of the array is the
	* number of divisions..The areas of the sectors are proportional to the probabilities.
	* @param p the array of probabilities
	* @param c the array of colors
	*/
	public Spinner(double[] p, Color[] c){
		this.addMouseMotionListener(this);
		setProbabilities(p);
		setColors(c);
	}

	/**
	* This special constructor creates a new spinner with a specified array of
	* probabilities and a the default array of colors. The size of the array is the
	* number of divisions..The areas of the sectors are proportional to the probabilities.
	* @param p the array of probabilities
	*/
	public Spinner(double[] p){
		setPreferredSize(new Dimension(200, 200));
		this.addMouseMotionListener(this);
		setToolTipText("Spinner");
		setProbabilities(p);
		setDefaultColors();
	}

	/**
	* This special constuctor creates a new spinner with a specified number of
	* equal sized divisions and a specified array of colors. Thus the probability
	* distribution is uniform.
	* @param n the number of divisions
	* @param c the array of colors
	*/
	public Spinner(int n, Color[] c){
		setPreferredSize(new Dimension(200, 200));
		this.addMouseMotionListener(this);
		setDivisions(n);
		setColors(c);
	}

	/**
	* This special constuctor creates a new spinner with a specified number of
	* equal sized divisions and the default array of colors. Thus the probability
	* distribution is uniform.
	* @param n the number of divisions
	*/
	public Spinner(int n){
		setPreferredSize(new Dimension(200, 200));
		this.addMouseMotionListener(this);
		setDivisions(n);
		setDefaultColors();
	}

	/**
	* This default constructor creates a new spinner with four equal divisions.
	*/
	public Spinner(){
		this(4);
	}

	/**
	* This method sets the array of probabilities. Error checking is performed
	* to ensure that the probabilities are nonnegative and sum to 1. The sizes
	* of the sectors are computed.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		//Get the number of divisions
		divisions = p.length;
		//Assign the probabilities, correcting for errors if necessary
		double psum = 0;
		for (int i = 0; i < divisions; i++){
			if (p[i] < 0) p[i] = 0;
			psum = psum + p[i];
		}
		if (psum == 0) for (int i = 0; i < divisions; i++) probabilities[i] = 1.0 / divisions;
		else for (int i = 0; i < divisions; i++) probabilities[i] = p[i] / psum;
		//Assign the sector sizes. Any leftover is given to the last sector
		steps = new int[divisions];
		for(int i = 0; i < divisions; i++) steps[i] = (int)Math.rint(360 * probabilities[i]);
		int sum = 0, difference = 0;
		for(int i = 0; i < divisions; i++) sum = sum + steps[i];
		if (sum < 360) difference = 360 - sum;
		steps[divisions - 1] = steps[divisions - 1] + difference;
		repaint();
	}

	/**
	* This method returns the all of the probabilities.
	* @return the array of probabilities
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method sets an individual probability. The other probabilities would be
	* rescaled appropriately.
	* @param i the index
	* @param p the probability
	*/
	public void setProbabilities(int i, double p){
		if (i < 0) i = 0;
		else if (i >= divisions) i = divisions - 1;
		probabilities[i] = p;
		setProbabilities(probabilities);
	}

	/**
	* This method returns an individual probability.
	* @param i the index
	* @return the probability corresponding to the index.
	*/
	public double getProbabilities(int i){
		return probabilities[i];
	}

	/**
	* This method sets the array of colors.
	* @param c the array of colors.
	*/
	public void setColors(Color[] c){
		if (c.length == divisions) colors = c;
		else setDefaultColors();
	}

	/**
	* This method returns the array of colors
	* @return the array of colors
	*/
	public Color[] getColors(){
		return colors;
	}

	/**
	* This method sets an individual color.
	* @param i the index
	* @param c the color of the index
	*/
	public void setColors(int i, Color c){
		if (i < 0) i = 0; else if (i > divisions - 1) i = divisions - 1;
		colors[i] = c;
	}

	/**
	* This method returns an individual color
	* @param i the index
	* @return the color of the index.
	*/
	public Color getColors(int i){
		return colors[i];
	}

	/**
	* This method defines the default color array. This array cylces among the 10
	* basic colors
	* @return the array of colors
	*/
	public void setDefaultColors(){
		colors = new Color[divisions];
		for (int i = 0; i < divisions; i++) colors[i] = basicColors[i % 10];
	}

	/**
	* This method sets the number of divisions.  The default unfiform probabilities
	* and colors are specified.
	* @param n the number of divisions
	*/
	public void setDivisions(int n){
		if (n < 1) n = 1;
		probabilities = new double[n];
		for (int i = 0; i < n; i++)	probabilities[i] = 1.0 / n;
		setProbabilities(probabilities);
		setDefaultColors();
	}

	/**
	* This method returns the number of divisions.
	*/
	public int getDivisions(){
		return divisions;
	}

	/**
	* This method sets a specific angle measure and computes the value of the spin
	* corresponding to that angle.
	* @param a the angle of the pointer relative to the horizontal ray
	*/
	public void setAngle(double a){
		a = a % 360;
		angle = a;
		value = getValue(angle);
	}

	/**
	* This method returns the angle.
	* @return the angle
	*/
	public double getAngle(){
		return angle;
	}

	/**
	* This method "spins" the spinner by computing  a random angle and the value of
	* a spin based on that angle.
	*/
	public void spin(){
		setAngle(360 * Math.random());
	}

	/**
	* This method computes the value (sector number) corresponding
	* to a give angle.
	* @param a the angle
	* @return the value
	*/
	public int getValue(double a){
		a = a % 360;
		double sum = 0;
		int v = 0;
		for(int i = 0; i < divisions; i++){
			if(a >= sum && a < sum + steps[i]) v = i + 1;
			sum = sum + steps[i];
		}
		return v;
	}

	/**
	* This method returns the current value of the spinner
	* @return the number of the sector containing the spinner pointer.
	*/
	public int getValue(){
		return value;
	}

	/**
	* This method paints the spinner based on the number of divisions and paints
	* the pointer based on the random angle computed in the spin method.
	* @param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		width = getSize().width;
		height = getSize().height;
		int min = Math.min(width, height);
		radius = min / 2;
		int sum = 0;
		for(int i = 0; i < divisions; i++){
			g.setColor(colors[i]);
			g.fillArc(0, 0, min, min, sum, (int)steps[i]);
			sum = sum + (int)steps[i];
		}
		/* draw the black line that will spin */
		g.setColor(Color.black);
		g.drawLine(radius, radius, radius + (int)(radius * Math.cos(angle * Math.PI / 180)),
			radius + (int)(radius * Math.sin(angle * Math.PI / 180)));
	}

	/**
	* This method handles the mouse move event.  The coordinates of the mouse (in scale
	* units) are tracked.
	* @param e the mouse event
	*/
	public void mouseMoved(MouseEvent e){
		double a0 = 0, a = 0;
		int x = e.getX() - radius;
		int y = e.getY() - radius;
		if (x * x + y * y < radius * radius){
			a0 = 180 * Math.atan((double)y / x) / Math.PI;
			if (x >= 0 && y >= 0) a = a0;
			else if (x < 0) a = a0 + 180;
			else if (y < 0) a = a0 + 360;
			int v = getValue(a);
			setToolTipText(String.valueOf(v));
		}
		else setToolTipText("Spinner");
	}

	//This method is not handled
	public void mouseDragged(MouseEvent event){}

}