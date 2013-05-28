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
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;

/**
* This class models a ball that has a specified ball color, text color, size, value, and state. These
* parameters can be changed. The state of the ball is drawn or not drawn. If drawn,
* the ball is shown with its value and color. If not drawn, the ball is shown without
* its value and in the background color.
* The Ball object can be used as a metaphor for an element in a population, in a variety
* of simulations that involve sampling. In particular this object is useful for simulations that
* involve drawing balls from an urn..
* @author Kyle Siegrist
* @author Dawn Duehring
* @version August, 2003
*/
public class Ball extends JComponent implements Serializable{
	private int value;
	private boolean drawn;
	private Color ballColor = Color.red, textColor = Color.white;

	/**
	* This general constructor creates a new ball with a specified value, size, ball color,
	* and text color.
	* @param x the value of the ball
	* @param s the size of the ball
	* @param bc the ball color.
	* @param tc the text color
	*/
	public Ball(int x, int s, Color bc, Color tc){
		setValue(x);
		setPreferredSize(new Dimension(s, s));
		setColors(bc, tc);
		setToolTipText("Ball");
	}

	/**
	* This special constructor creates a new ball with a specified value amd size, and with
	* default ball color red and text color white
	* @param x the value of the ball.
	* @param s the size of the ball
	*/
	public Ball(int x, int s){
		this(x, s, Color.red, Color.white);
	}

	/**
	* This special constructor creates a new ball with a specified value and with
	* the default size 24 and default ball color red.
	* @param x the value of the ball.
	*/
	public Ball(int x){
		this(x, 32);
	}

	/**
	* This default constructor creates a new ball with value 0, size 24, and ball color red.
	*/
	public Ball(){
		this(0);
	}

	/**
	* This method paints the ball.
	* @param g the graphics context.
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int size = (int)Math.min(getSize().height, getSize().width);
		if (drawn) g.setColor(ballColor);
		else g.setColor(getBackground());
		g.fillOval(2, 2, size - 4, size - 4);
		g.setColor(Color.black);
		g.drawOval(2, 2, size - 4, size - 4);
		if (drawn){
			Font font = new Font("Arial", Font.BOLD, (size - 4) / 2);
 			g.setFont(font);
 			g.setColor(textColor);
			String label = String.valueOf(value);
			int x = g.getFontMetrics(font).stringWidth(label);
			int y = g.getFontMetrics(font).getAscent();
			g.drawString(label, size / 2 - x / 2, size / 2 + y / 2);
		}
	}

	/**
	* This method sets the ball to a specified value.
	* @param x the value of the ball.
	*/
	public void setValue(int x){
		value = x;
	}

	/**
	* This method gets the value of the ball.
	* @return the value of the ball.
	*/
	public int getValue(){
		return value;
	}

	/**
	* This method sets the colors of the ball and text.
	* @param bc the ball color
	* @param tc the text color
	*/
	public void setColors(Color bc, Color tc){
		ballColor = bc;
		textColor = tc;
	}

	/**
	* This method sets the color of the ball.
	* @param c the color of the ball.
	*/
	public void setBallColor(Color c){
		setColors(c, textColor);
	}

	/**
	* This method gets the ball color.
	* @return The color of the ball.
	*/
	public Color getBallColor(){
		return ballColor;
	}

	/**
	* This method sets the color of the text.
	* @param c the color of the ball.
	*/
	public void setTextColor(Color c){
		setColors(ballColor, c);
	}

	/**
	* This method gets the text color.
	* @return the color of the ball.
	*/
	public Color getTextColor(){
		return textColor;
	}


	/**
	* This method sets the state of the ball. If drawn, the ball is shown with
	* its ballColor and value. If not drawn, the ball is shown without its value
	* and in the background ballColor.
	* @param b true if drawn
	*/
	public void setDrawn(boolean b){
		drawn = b;
		repaint();
	}

	/**
	* This method returns the state of the ball. If drawn, the ball is shown with
	* its ballColor and value. If not drawn, the ball is shown without its value
	* and in the background ballColor.
	* @return true if drawn
	*/
	public boolean isDrawn(){
		return drawn;
	}
}

