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
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;

/**
* This class models a standard, 6-sided die with specified colors and probabilities.
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Die extends JComponent implements Serializable{
	private int value;
	private boolean rolled;
	private Color backColor, spotColor;
	private double[] probabilities = new double[6];
	public final static int FAIR = 0, FLAT16 = 1, FLAT25 = 2, FLAT34 = 3, LEFT = 4, RIGHT = 5;

	/**
	* This general constructor creates a new die with specified probabilities, size,
	* back color, and spot color.
	* @param p the array of probabilities
	* @param s the preferred size
	* @param bc the back color
	* @param sc the spot color
	*/
	public Die(double[] p, int s, Color bc, Color sc){
		setPreferredSize(new Dimension(s, s));
		setProbabilities(p);
		setColors(bc, sc);
		setToolTipText("Die");
		roll();
	}

	/**
	* This special constructor creates a new die with and specified probabilities
	* and size, and with default back color red and spot color white.
	* @param p the array of probabilities
	* @param s the preferred size
	*/
	public Die(double[] p, int s){
		this(p, s, Color.red, Color.white);
	}

	/**
	* This special constructor creates a new die with and specified probabilities
	* and with default size 36 and with  back color red and spot color white.
	* @param p the array of probabilities
	*/
	public Die(double[] p){
		this(p, 36, Color.red, Color.white);
	}

	/**
	* This general constructor creates a new die with specified probabilities of special
	* type, and with specified size and colors.
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, skewed right)
	* @param s the preferred size
	* @param bc the back color
	* @param sc the spot color
	*/
	public Die(int t, int s, Color bc, Color sc){
		setPreferredSize(new Dimension(s, s));
		setSpecialProbabilities(t);
		setColors(bc, sc);
		setToolTipText("Die");
		roll();
	}

	/**
	* This special constructor creates a new die with specified probabilities of special
	* type, and with specified size, and with default colors red and white.
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, skewed right)
	* @param s the preferred size
	*/
	public Die(int t, int s){
		this(t, s, Color.red, Color.white);
	}

	/**
	* This special constructor creates a new die with specified probabilities of special
	* type, and with default size 36 and colors red and white.
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, skewed right)
	*/
	public Die(int t){
		this(t, 36, Color.red, Color.white);
	}

	/**
	* This default constructor creates a new fair die, and with default size and colors.
	*/
	public Die(){
		this(FAIR);
	}

	/**
	* This method paints the die.
	*@param g the graphics context
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int size = (int)Math.min(getSize().height, getSize().width) - 4,
			r = (int)Math.rint(3.0 * size / 32),		//Spot radius
			d1 = 2 + r,									//First position
			d2 = (int)Math.rint(2 + size / 2.0 - r),	//Second postion
			d3 = 2 + size - 3 * r;						//Third position
		if (rolled){
			g.setColor(backColor);
			g.fillRoundRect(2, 2, size, size, 2, 2);
			g.setColor(spotColor);
			switch(value){
			case 1:
				g.fillOval(d2, d2, 2 * r, 2 * r);
				break;
			case 2:
				g.fillOval(d1, d1, 2 * r, 2 * r);
				g.fillOval(d3, d3, 2 * r, 2 * r);
				break;
			case 3:
				g.fillOval(d1, d1, 2 * r, 2 * r);
				g.fillOval(d2, d2, 2 * r, 2 * r);
				g.fillOval(d3, d3, 2 * r, 2 * r);
				break;
			case 4:
				g.fillOval(d1, d1, 2 * r, 2 * r);
				g.fillOval(d3, d1, 2 * r, 2 * r);
				g.fillOval(d1, d3, 2 * r, 2 * r);
				g.fillOval(d3, d3, 2 * r, 2 * r);
				break;
			case 5:
				g.fillOval(d1, d1, 2 * r, 2 * r);
				g.fillOval(d3, d1, 2 * r, 2 * r);
				g.fillOval(d1, d3, 2 * r, 2 * r);
				g.fillOval(d3, d3, 2 * r, 2 * r);
				g.fillOval(d2, d2, 2 * r, 2 * r);
				break;
			case 6:
				g.fillOval(d1, d1, 2 * r, 2 * r);
				g.fillOval(d3, d1, 2 * r, 2 * r);
				g.fillOval(d1, d3, 2 * r, 2 * r);
				g.fillOval(d3, d3, 2 * r, 2 * r);
				g.fillOval(d1, d2, 2 * r, 2 * r);
				g.fillOval(d3, d2, 2 * r, 2 * r);
				break;
			}
		}
		g.setColor(Color.black);
		g.drawRoundRect(2, 2, size, size, 2, 2);
	}

	/**
	* This method sets the value of the die to a specified value.
	* @param x the value of the die.
	*/
	public void setValue(int x){
		//Correct for invalid value
		if (x < 1) x = 1; else if (x > 6) x = 6;
		value = x;
	}

	/**
	* This method sets the value of the die to a random value, as determined by the probabilities.
	*/
	public void roll(){
		double p = Math.random(), sum = 0;
		for (int i = 0; i < 6; i++){
			if (sum < p & p <= sum + probabilities[i]) value = i + 1;
			sum = sum + probabilities[i];
		}
	}

	/**
	* This method gets the value of the die.
	* @return the die score
	*/
	public int getValue(){
		return value;
	}

	/**
	* This method sets the probabilities to specified values.
	* @param p the array of probabilities
	*/
	public void setProbabilities(double[] p){
		if (p.length != 6) p = new double[6];
		double sum = 0;
		for (int i = 0; i < 6; i++){
			if (p[i] < 0) p[i] = 0;
			sum = sum + p[i];
		}
		if (sum == 0) for (int i = 0; i < 6; i++) probabilities[i] = 1.0 / 6;
		else for (int i = 0; i < 6; i++) probabilities[i] = p[i] / sum;
	}

	/**
	* This method sets an individual probability
	* @param i the index
	* @param p the probability corresponding to the index
	*/
	public void setProbabilities(int i, double p){
		if (i < 0) i = 0;
		else if (i > 5) i = 5;
		double[] q = new double[6];
		q[i] = p;
		for (int j = 0; j < 6; j++) if (j != i) q[j] = probabilities[j];
		setProbabilities(q);
	}


	/**
	* This method sets the probabilities to a special type.
	* @param t the type of distribution (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, skewed right)
	*/
	public void setSpecialProbabilities(int t){
		if (t == FLAT16)
			setProbabilities(new double[] {1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 4});
		else if (t == FLAT25)
			setProbabilities(new double[] {1.0 / 8, 1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 8});
		else if (t == FLAT34)
			setProbabilities(new double[] {1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 8, 1.0 / 8});
		else if (t == LEFT)
			setProbabilities(new double[] {1.0 / 21, 2.0 / 21, 3.0 / 21, 4.0 / 21, 5.0 / 21, 6.0 / 21});
		else if (t == RIGHT)
			setProbabilities(new double[] {6.0 / 21, 5.0 / 21, 4.0 / 21, 3.0 / 21, 2.0 / 21, 1.0 / 21});
		else
			setProbabilities(new double[] {1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6});
	}

	/**
	* This method returns the probability of a specified face.
	* @param i the face number
	*/
	public double getProbabilities(int i){
		if (i < 0) i = 0; else if (i > 5) i = 5;
		return probabilities[i];
	}

	/**
	* This method returns the probability distribution of the die.
	* @return the array of probabilities
	*/
	public double[] getProbabilities(){
		return probabilities;
	}

	/**
	* This method sets the colors.
	* @param bc the back color
	* @param sc the spot color
	*/
	public void setColors(Color bc, Color sc){
		backColor = bc;
		spotColor = sc;
	}

	/**
	* This method sets the back color.
	* @param bc the back color
	*/
	public void setBackColor(Color bc){
		backColor = bc;
	}

	/**
	* This method gets the back color.
	* @return the back color
	*/
	public Color getBackColor(){
		return backColor;
	}

	/**
	* This method sets the spot color.
	* @param sc the spot color
	*/
	public void setSpotColor(Color sc){
		spotColor = sc;
	}

	/**
	* The method gets the spot color.
	* @return the spot color
	*/
	public Color getSpotColor(){
		return spotColor;
	}

	/**
	* This method sets the state of the die (rolled or not rolled).
	* If rolled, the die is shown with the specified back color and spot color.
	* If not rolled, an outline of the die is shown in the background color.
	* @param b the state of the die (rolled or not rolled)
	*/
	public void setRolled(boolean b){
		rolled = b;
		repaint();
	}

	/**
	* This method returns the state of the die (rolled or not rolled).
	* If rolled, the die is shown with the specified back color and spot color.
	* If not rolled, an outline of the die is shown in the background color.
	* @return the state of the die (rolled or not rolled)
	*/
	public boolean isRolled(){
		return rolled;
	}
}

