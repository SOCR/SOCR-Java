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
import java.awt.FlowLayout;
import java.awt.Color;
import edu.uah.math.distributions.Functions;

/**
* This class is a container (actually a panel) that holds balls. It is useful in sampling experiments
* in which balls are used as metaphors for objects in the population. The balls can be
* shown or hidden, and a sample of balls can be selected.
* @author Dawn Duehring
* @author Kyle Siegrist
* @version August, 2003
*/
public class Urn extends JPanel{
	private int ballCount, size;
	private boolean drawn;
	private Color ballColor = Color.red, textColor = Color.white;
	public final static int WITHOUT_REPLACEMENT = 0, WITH_REPLACEMENT = 1;

	/**
	* This general constructor creates a new urn with a specified number of balls of
	* a specified preferred size and a specified ball color and text color
	* The balls are added to the panel using a flow layout.
	* @param n the number of balls
	* @param s the preferred size of the balls
	* @param bc the ball color
	* @param tc the text color
	*/
	public Urn(int n, int s, Color bc, Color tc){
		if (s < 10) s = 10;
		size = s;
		ballColor = bc;
		textColor = tc;
		Ball ball;
		//setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		setLayout(new java.awt.GridLayout(10, 10, 0, 0));
		setBorder(BorderFactory.createLineBorder(Color.black));
		setToolTipText("Urn");
		setBallCount(n);
	}

	/**
	* This special constructor creates a new urn with a specified number of balls
	* of a specified preferred size, and with the default ball color red and text color white
	* @param n the number of balls
	* @param s the preferred size of the balls
	*/
	public Urn(int n, int s){
		this(n, s, Color.red, Color.white);
	}

	/**
	* This special constructor creates a new urn with a specified number of balls
	* and with the default size 40 and color red.
	* @param n the number of balls
	*/
	public Urn(int n){
		this(n, 40);
	}

	/**
	* This default constructor creates a new urn with 10 balls of default size and color.
	*/
	public Urn(){
		this(10);
	}

	/**
	* This method returns a specified ball.
	* @param i the index
	* @return the ball corresponding to the index
	*/
	public Ball getBall(int i){
		if (i < 0) i = 0; else if (i > ballCount - 1) i = ballCount - 1;
		return (Ball)getComponent(i);
	}

	/**
	* This method sets the number of balls. If the new ball count is greater than the
	* old, an appropriate number of new balls are added. If the new ball count is
	* smaller than the old, an appropriate number of balls are removed. The ball count
	* updated.
	* @param n the number of balls in the urn
	*/
	public void setBallCount(int n){
		Ball ball;
		if (n < 1) n = 1;
		if (n > ballCount){
			for (int i = ballCount; i < n; i++){
				ball = new Ball(i + 1, size, ballColor, textColor);
				ball.setToolTipText("Ball " + (i + 1));
				add(ball);
			}
			ballCount = n;
		}
		else if (n < ballCount){
			for (int i = n; i < ballCount; i++) remove(n);
			ballCount = n;
		}
		validate();
	}

	/**
	* This method returns the number of balls.
	* @return the number of balls in the urn
	*/
	public int getBallCount(){
		return ballCount;
	}

	/**
	* This method sets the ballcolor of all of the balls.
	* @param c the new color
	*/
	public void setBallColor(Color c){
		for (int i = 0; i < ballCount; i++) getBall(i).setBallColor(c);
	}

	/**
	* This method sets the text color of all of the balls.
	* @param c the new color
	*/
	public void setTextColor(Color c){
		for (int i = 0; i < ballCount; i++) getBall(i).setTextColor(c);
	}

	/**
	* This method sets the common state of all of the balls. If drawn,
	* the balls are shown with their colors and values. If not drawn,
	* the balls are shown without their values and in the background color.
	* @param b true if drawn
	*/
	public void setDrawn(boolean b){
		drawn = b;
		for (int i = 0; i < ballCount; i++) getBall(i).setDrawn(drawn);
		repaint();
	}

	/**
	* This method returns the common state of all of the balls. If drawn,
	* the balls are shown with their colors and values. If not drawn,
	* the balls are shown without their values and in the background color.
	* @return true if drawn
	*/
	public boolean isDrawn(){
		return drawn;
	}

	/**
	* This method sets the array of ball values.
	* @param v the array of ball values
	*/
	public void setValues(int[] v){
		if (v.length == ballCount)
			for (int i = 0; i < ballCount; i++) getBall(i).setValue(v[i]);
	}

	/**
	* This method returns the array of ball values.
	* @return the array of ball values
	*/
	public int[] getValues(){
		int[] v = new int[ballCount];
		for (int i = 0; i < ballCount; i++) v[i] = getBall(i).getValue();
		return v;
	}

	/**
	* This method sets the value of an individual ball.
	* @param i the index
	* @param x the value
	*/
	public void setValues(int i, int x){
		if (i < 0) i = 0; else if (i > ballCount - 1) i = ballCount - 1;
		getBall(i).setValue(x);
	}

	/**
	* This method returns the value of an individual ball.
	* @param i the index
	* @return the value of the ball with the specified index
	*/
	public int getValues(int i){
		if (i < 0) i = 0; else if (i > ballCount - 1) i = ballCount - 1;
		return getBall(i).getValue();
	}

	/**
	* This method treats the balls as a sample from a population of a specified
	* size. The type of sampling (without replacement or with replacement) is specified.
	* @param N the popultaion size
	* @param t the type of sampling (WITH_REPLCEMENT or WITHOUT_REPLACEMENT)
	*/
	public void sample(int N, int t){
		setValues(Functions.getSample(N, ballCount, t));
	}
}






