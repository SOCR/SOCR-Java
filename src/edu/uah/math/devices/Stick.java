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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.io.Serializable;

/**
* This class models a stick of unit length that can be broken in two pieces.
* The variable of interest is whether the pieces form no triangle, an acute triangle,
* or an obtuse triangle.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class Stick extends Graph implements Serializable{
	private double a, b, c;  //The side lengths in increasing order.
	private double cutX, cutY; //The cutpoints
	private int type; //The type of triangle: 0 (nothing), 1 (obtuse), 2 (acute)
	private Color stickColor = Color.red;

	/**
	* This general constructor creates a new stick object.
	*/
	public Stick(){
		super(0, 1, 0, 1);
		setMargins(5, 5, 5, 5);
		setPreferredSize(new Dimension(200, 200));
		setBackground(Color.yellow);
		setToolTipText("Stick");
		reset();
  	}

  	/**
  	* This method sets the cutpoints to specified values. These values should be
  	* between 0 and 1. The cutpoints do not have to be ordered.
  	* @param x the first cutpoint
  	* @param y the second cutpoint
  	*/
  	public void setCutPoints(double x, double y){
		if (x < 0) x = 0; else if (x > 1) x = 1;
		cutX = x;
		if (y < 0) y = 0; else if (y > 1) y = 1;
		cutY = y;
		if (cutX < cutY) setSides(cutX, cutY - cutX, 1 - cutY);
		else setSides(cutY, cutX - cutY, 1 - cutX);
	}

	/**
	* This method sets the cutpoints to random values in (0, 1).
	*/
	public void setCutPoints(){
		setCutPoints(Math.random(), Math.random());
	}

	/**
	* This method sets the first cut point.
	* @param x the cutpoint
	*/
	public void setCutX(double x){
		setCutPoints(x, cutY);
	}

	/**
	* This method returns the first cut point.
	* @return the first cutpoint
	*/
	public double getCutX(){
		return cutX;
	}

	/**
	* This method sets the second cut point.
	* @param y the cutpoint
	*/
	public void setCutY(double y){
		setCutPoints(cutX, y);
	}

	/**
	* This method gets the second cut point.
	* @return the second cutpoint
	*/
	public double getCutY(){
		return cutY;
	}

  	/**
  	* This method sets the sides of the stick to specified values and determines
  	* the type of triangle.	The values should be nonnegative and should sum to 1.
  	* @param u the first side length
  	* @param v the second side length
  	* @param w the third side lendgth
  	*/
  	public void setSides(double u, double v, double w){
		//Assign side lengths in increasing order
		if (u <= v && v <= w){
	  		a = u; b = v; c = w;
		}
		else if (u <= w && w <= v){
	  		a = u; b = w; c = v;
		}
		else if (v <= u && u <= w){
	  		a = v; b = u; c = w;
		}
		else if (v <= w && w <= u){
	  		a = v; b = w; c = u;
		}
		else if (w <= u && u <= v){
	  		a = w; b = u; c = v;
		}
		else{
	  		a = w; b = v; c = u;
		}
		//Determine type of triangle
		if (a < b + c && b < a + c && c < a + b){		//Triangle
			if (c * c < a * a + b * b) type = 2;	//Acute
	  		else type = 1;				//Obtuse
		}
		else type = 0;	//Not a triangle
  	}

  	/**
  	* This method returns the length of the sides, in increasing order.
  	* @param i the index (0, 1, or 2)
  	* @return the length of the corresponding side (smallest, middle, largest)
  	*/
  	public double getSize(int i){
		if (i == 0) return a;
		else if (i == 1) return b;
		else return c;
	}

  	/**
  	* This method returns the type of triangle.
  	* @return the triangle type (0 none, 1 obtuse, 2 acute)
  	*/
  	public int getType(){
		return type;
  	}

  	/**
  	* This method paints the stick. If broken, the stick is shown as a
  	* triangle if possible or as an open rectangle if not. If unbroken,
  	* the stick is shown as a line segment.
  	* @param g the graphics context
  	*/
  	public void paintComponent(Graphics g){
		super.paintComponent(g);
		double x0, y0, u, v, cos, sin;
		g.setColor(stickColor);
		x0 = (1 - c) / 2; y0 = 1.0 / 3;		//Shift values
		drawLine(g, x0, y0, x0 + c, y0);	//Draw base
		//If the pieces do not form a triangle, draw the broken stick
		if (type == 0){
	  		drawLine(g, x0, y0, x0, y0 + a);
	  		drawLine(g, x0 + c, y0, x0 + c, y0 + b);
		}
		//Draw acute triangle
		else if (type == 2){
	  		cos = (a * a + c * c - b * b) / (2 * a * c);
	  		sin = Math.sqrt(1 - cos * cos);
	  		u = a * cos;
	  		v = a * sin;
	  		drawLine(g, x0, y0, x0 + u, y0 + v);
	  		drawLine(g, x0 + c, y0, x0 + u, y0 + v);
		}
		//Draw obtuse triangle
		else{
	  		u = (a * a + c * c - b * b) / (2 * c);
	  		v = Math.sqrt(a - u);
	  		drawLine(g, x0, y0, x0 + u, y0 + v);
	  		drawLine(g, x0 + c, y0, x0 + u, y0 + v);
		}
  	}

  	/**
  	* This method resets the stick to its ubroken form.
  	*/
  	public void reset(){
		setCutPoints(0, 1);	//An unbroken stick
		repaint();
  	}

  	/**
  	* This method sets the stick color.
  	* @param c the stick color
  	*/
  	public void setStickColor(Color c){
		stickColor = c;
	}

	/**
	* This methd returns the stick color.
	* @return the stick color
	*/
	public Color getStickColor(){
		return stickColor;
	}
}
